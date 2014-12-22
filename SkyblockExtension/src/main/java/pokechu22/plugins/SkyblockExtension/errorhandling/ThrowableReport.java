package pokechu22.plugins.SkyblockExtension.errorhandling;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
		super();
		
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
		
		Map<String, Object> map = super.serializeBase();
		
		try {
			//And now args.  (Apparently, configurations don't like String[]?)
			ArrayList<String> argsList = new ArrayList<String>();
			if (this.hasCommand) {
				for (int i = 0; i < this.args.length; i++) {
					argsList.add(this.args[i]);
				}
			}
		
			//The casts are useless but show the type.
			map.put("Thrown", (byte[]) serializeThrowable(this.thrown));
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
		} catch (IOException e) {
			SkyblockExtension.inst().getLogger().warning("Failed to safe crash report: " +
					e.toString());
			SkyblockExtension.inst().getLogger().log(Level.WARNING, "Exception: ", e);
			ErrorHandler.logError(new ConfigurationErrorReport(e, this.getClass().getName(), 
					true));
			throw new RuntimeException(e);
		}
		
		return map;
	}
	
	/**
	 * Deserializes the crashReport for configuration loading.
	 */
	@SuppressWarnings("unchecked")
	public ThrowableReport(Map<String, Object> map) {
		super(map);
		
		try {
			
			//Create thrown.
			Throwable t = deserializeThrowable((byte[]) map.get("Thrown"));
			
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
		} catch (Exception e) {
			SkyblockExtension.inst().getLogger().warning("Failed to load crash report: " + 
					e.toString());
			SkyblockExtension.inst().getLogger().log(Level.WARNING, "Exception: ", e);
			ErrorHandler.logError(new ConfigurationErrorReport(e, this.getClass().getName(), 
					false));
			throw new RuntimeException(e);
		}
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
	 * @throws IOException 
	 */
	private static byte[] serializeThrowable(Throwable t) throws IOException {
		if (t == null) {
			return null;
		}
		
		byte[] result;
		
		//Try-with-resources, similar to C#'s "using".
		try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
			try (ObjectOutputStream output = new ObjectOutputStream(buffer)) {
				output.writeObject(t);
			}
			result = buffer.toByteArray();
		}
		
		return result;
	}
	
	/**
	 * Serializes a throwable.
	 * TODO Move to a better location.
	 * 
	 * @param t
	 * @return 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private static Throwable deserializeThrowable(byte[] data) throws IOException, ClassNotFoundException {
		if (data == null) {
			return null;
		}
		
		//Try-with-resources, similar to C#'s "using".
		try (ByteArrayInputStream buffer = new ByteArrayInputStream(data);
				ObjectInputStream input = new ObjectInputStream(buffer)) {
			return (Throwable) input.readObject();
		}
	}
}
