package pokechu22.plugins.SkyblockExtension.errorhandling;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.SerializableAs;

import pokechu22.plugins.SkyblockExtension.SkyblockExtension;

/**
 * An individual crash report.
 * Needed to store data in the settings.
 * 
 * @author Pokechu22
 *
 */

@SerializableAs("ThrowableReport")
public class ThrowableReport extends CrashReport {
	
	//From the constructor.
	public Throwable thrown;
	
	public boolean hasCommand;
	public String senderClass;
	public String senderName;
	public boolean senderIsOp;
	public String cmd;
	public String label;
	public String[] args;
	
	public boolean hasContext;
	public String context;
	
	/**
	 * When the error occurred.
	 */
	public Date loggedDate;
	
	/**
	 * Creates a report.
	 * @param thrown
	 */
	public ThrowableReport(Throwable thrown) {
		this(thrown, false, null, null, null, null, false, null);
	}
	
	/**
	 * Creates a report.
	 * @param thrown
	 * @param context
	 */
	public ThrowableReport(Throwable thrown, String context) {
		this(thrown, false, null, null, null, null, true, context);
	}
	
	/**
	 * Creates a crash report.
	 */
	public ThrowableReport(Throwable thrown, CommandSender sender, Command cmd, 
			String label, String[] args) {
		this(thrown, true, sender, cmd, label, args, false, null);
	}
	
	/**
	 * Creates a crash report.
	 */
	public ThrowableReport(Throwable thrown, CommandSender sender, Command cmd, 
			String label, String[] args, String context) {
		this(thrown, true, sender, cmd, label, args, true, context);
	}
	
	/**
	 * Internal creation of a report.
	 */
	private ThrowableReport(Throwable thrown, boolean hasCommand, CommandSender sender, 
			Command cmd, String label, String[] args, boolean hasContext, String context) {
		this.thrown = thrown;
		
		this.hasCommand = hasCommand;
		if (hasCommand) {
			this.senderClass = sender.getClass().getName();
			this.senderName = sender.getName();
			this.senderIsOp = sender.isOp();
			this.cmd = cmd.toString();
			this.label = label;
		}
		
		this.hasContext = hasContext;
		if (hasContext) {
			this.context = context;
		}
		
		this.args = args;
		
		this.loggedDate = new Date(); //Current time.
	}
	
	/**
	 * Gets the text of this crash report, without marking a specific reader.
	 * @return The text.
	 */
	@Override
	public String getAsTextNoMark() {
		StringBuilder text = new StringBuilder();
		
		text.append(thrown.toString() + "\n");
		text.append("Occured on: " + loggedDate.toString() + "\n");
		if (this.hasContext) {
			text.append("Context: " + context + "\n");
		}
		if (this.hasCommand) {
			text.append("Command info: ");
			text.append("  Sender: " + senderName + (senderIsOp ? " (Op)" : "") + "\n");
			text.append("  Sender is ");
			if (senderClass.startsWith("a") || 
					senderClass.startsWith("e") || 
					senderClass.startsWith("i") || 
					senderClass.startsWith("o") || 
					senderClass.startsWith("u")) {
				text.append("a ");
			} else {
				text.append("an ");
			}
			text.append(senderClass + "\n");
			text.append("  Command: " + cmd + "\n");
			text.append("  (Label: " + label + ")\n");
			text.append("  Arguments: Length = " + args.length + "\n"); 
			//Add each argument.
			for (int i = 0; i < args.length; i++) {
				text.append("  args[" + i + "]: " + args[i] + "\n");
			}
		}
		//Add the stacktrace.
		text.append("Stacktrace: \n");
		StackTraceElement[] elements = thrown.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			text.append(elements[i].toString());
			text.append("\n");
			
		}
		
		Throwable cause = thrown.getCause();
		//Append each subvalue.
		while (cause != null) {
			text.append("\n\nCaused By:\n");
			text.append(cause.toString() + "\n");
			
			//Add the stacktrace.
			text.append("Stacktrace: \n");
			StackTraceElement[] causeElements = cause.getStackTrace();
			for (int i = 0; i < causeElements.length; i++) {
				text.append(causeElements[i].toString());
				text.append("\n");
				
			}
			
			cause = cause.getCause();
		}
		
		if (thrown instanceof java.lang.Error) {
			text.append("This was rethrown due to being an instance of java.lang.error.\n");
		}
		
		text.append("End of report.");
		
		return text.toString();
				
	}
	
	/**
	 * Gets the title of the CrashReport, which is "Throwable caught: " + thrown.toString().
	 * @return The title.
	 */
	@Override
	public String getTitle() {
		return "Throwable caught: " + thrown.toString();
	}
	
	/**
	 * Serializes the crashReport for configuration saving.
	 */
	@Override
	public Map<String, Object> serialize() {
		SkyblockExtension.inst().getLogger().finest("Saving crash report to map.");
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		//Serialize thrown as a map.
		//map.put("Thrown", this.thrown);
		Map<String, Object> thrownMap = serializeThrowable(thrown);
		
		//And now args.  (Apparently, configurations don't like String[]?)
		ArrayList<String> argsList = new ArrayList<String>();
		if (this.hasCommand) {
			for (int i = 0; i < this.args.length; i++) {
				argsList.add(this.args[i]);
			}
		}
	
		
		//The casts are useless but show the type.
		map.put("Thrown", (Map<String, Object>) thrownMap);
		map.put("HasCommand", this.hasCommand);
		if (this.hasCommand) {
			map.put("SenderClass", (String) senderClass);
			map.put("SenderName", (String) senderName);
			map.put("SenderIsOp", (boolean) senderIsOp);
			map.put("Cmd", (String) this.cmd);
			map.put("Label", (String) this.label);
			map.put("Args", (ArrayList<String>) argsList);
		}
		map.put("HasContext", this.hasContext);
		if (this.hasContext) {
			map.put("Context", context);
		}
		map.put("LoggedDate", (Date) this.loggedDate);
		map.put("Readers", (HashSet<String>) this.readers);
		
		return map;
	}
	
	/**
	 * Deserializes the crashReport for configuration loading.
	 */
	@SuppressWarnings("unchecked")
	public ThrowableReport(Map<String, Object> map) {
		SkyblockExtension.inst().getLogger().finest("Creating crash report from map.");
		try {
			
			//Create thrown.
			Throwable t = deserializeThrowable((Map<String, Object>) map.get("Thrown"));
			
			//And convert args.
			this.hasCommand = (boolean) map.get("HasCommand");
			String[] argsArray = null;
			if (this.hasCommand) {
				ArrayList<String> argsList = (ArrayList<String>) map.get("Args");
				argsArray = argsList.toArray(new String[argsList.size()]);
			}
			
			this.thrown = t;
			if (this.hasCommand) {
				this.senderName = (String) map.get("SenderName");
				this.senderClass = (String) map.get("SenderClass");
				this.senderIsOp = (boolean) map.get("SenderIsOp");
				//this.sender = (CommandSender) map.get("Sender");
				this.cmd = (String) map.get("Cmd");
				this.label = (String) map.get("Label");
				this.args = argsArray;
			}
			this.hasContext = (boolean) map.get("HasContext");
			if (this.hasContext) {
				this.context = (String) map.get("Context");
			}
			this.loggedDate = (Date) map.get("LoggedDate");
			this.readers = (HashSet<String>) map.get("Readers");
		} catch (ClassCastException | InstantiationException | IllegalAccessException 
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException 
				| SecurityException | ClassNotFoundException e) {
			SkyblockExtension.inst().getLogger().warning("Failed to create: " + e.toString());
			SkyblockExtension.inst().getLogger().log(Level.WARNING, "Exception: ", e);
			ErrorHandler.logError(new ConfigurationErrorReport(e, this.getClass().getName(), 
					false));
			return;
		}
		
		SkyblockExtension.inst().getLogger().finest("Succeeded!");
	}
	
	/**
	 * Deserialization; calls the {@link #CrashReport(Map)} constructor.
	 * @param map
	 * @return
	 */
	public static CrashReport deserialize(Map<String, Object> map) {
		return new ThrowableReport(map);
	}
	
	/**
	 * Deserialization; calls the {@link #CrashReport(Map)} constructor.
	 * @param map
	 * @return
	 */
	public static CrashReport valueOf(Map<String, Object> map) {
		return new ThrowableReport(map);
	}
	
	/**
	 * Serializes a throwable.
	 * TODO Move to a better location.
	 * 
	 * @param t
	 * @return
	 */
	private static Map<String, Object> serializeThrowable(Throwable t) {
		if (t == null) {
			return null;
		}
		
		HashMap<String, Object> thrownMap = new HashMap<String, Object>();
		thrownMap.put("Class", t.getClass().getName());
		thrownMap.put("Message", t.getMessage());
		
		ArrayList<HashMap<String, Object>> stackTraceList = 
				new ArrayList<HashMap<String, Object>>();
		
		StackTraceElement[] stackTrace = t.getStackTrace();
		for (int i = 0; i < stackTrace.length; i++) {
			HashMap<String, Object> stackTraceMap = new HashMap<String, Object>();
			stackTraceMap.put("ClassName", stackTrace[i].getClassName());
			stackTraceMap.put("MethodName", stackTrace[i].getMethodName());
			stackTraceMap.put("FileName", stackTrace[i].getFileName());
			stackTraceMap.put("LineNumber", stackTrace[i].getLineNumber());
			stackTraceList.add(stackTraceMap);
		}
		
		thrownMap.put("StackTrace", stackTraceList);
		
		Map<String, Object> causeMap;
		if (t.getCause() != null) {
			causeMap = serializeThrowable(t.getCause());
			thrownMap.put("Cause", causeMap);
		}
		
		return thrownMap;
	}
	
	/**
	 * Serializes a throwable.
	 * TODO Move to a better location.
	 * 
	 * @param t
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unchecked")
	private static Throwable deserializeThrowable(Map<String, Object> map) 
			throws InstantiationException, IllegalAccessException, 
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, 
			SecurityException, ClassNotFoundException {
		if (map == null) {
			return null;
		}
		
		String thrownClass = (String) map.get("Class");
		String thrownMessage = (String) map.get("Message");
		
		ArrayList<HashMap<String, Object>> stackTraceListOld = 
				(ArrayList<HashMap<String, Object>>) map.get("StackTrace");
		
		StackTraceElement[] stackTrace = new StackTraceElement[stackTraceListOld.size()];
		for (int i = 0; i < stackTraceListOld.size(); i++) {
			HashMap<String, Object> stackTraceElementMap = stackTraceListOld.get(i);
			
			stackTrace[i] = new StackTraceElement(
					(String) stackTraceElementMap.get("ClassName"), //declaringClass
					(String) stackTraceElementMap.get("MethodName"), //methodName
					(String) stackTraceElementMap.get("FileName"), //fileName
					(int) stackTraceElementMap.get("LineNumber") //lineNumber
					);
		}
		
		Map<String, Object> causeMap = (Map<String, Object>) map.get("Cause");
		
		Throwable t = null;
		
		if (causeMap != null) {
			Throwable cause = deserializeThrowable(causeMap);
			Constructor<?>[] constructors = Class.forName(thrownClass)
					.getConstructors();
			//Stupid issue with nonexistant message.
			for (Constructor<?> c : constructors) {
				if (c.getParameterTypes().length == 2 && 
						c.getParameterTypes()[0].equals(String.class) &&
						c.getParameterTypes()[1].equals(Throwable.class)) {
					t = (Throwable) c.newInstance(thrownMessage, cause);
				} else if (c.getParameterTypes().length == 2 && 
						c.getParameterTypes()[0].equals(Throwable.class) &&
						c.getParameterTypes()[1].equals(String.class)) {
					//InvocationTargetException is like this...
					t = (Throwable) c.newInstance(cause, thrownMessage);
				} else if (c.getParameterTypes().length == 1 &&
						c.getParameterTypes()[0].equals(Throwable.class)) {
					t = (Throwable) c.newInstance(cause);
				} else {
					return null;
				}
			}
			//t = (Throwable) Class.forName(thrownClass)
			//		.getConstructor(String.class, Throwable.class)
			//		.newInstance(thrownMessage, cause);
		} else {
			t = (Throwable) Class.forName(thrownClass)
					.getConstructor(String.class)
					.newInstance(thrownMessage);
		}
		
		t.setStackTrace(stackTrace);
		
		return t;
	}
}
