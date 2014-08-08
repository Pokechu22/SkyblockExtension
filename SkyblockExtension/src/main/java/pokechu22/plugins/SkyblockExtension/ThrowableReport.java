package pokechu22.plugins.SkyblockExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.ChatPaginator;

/**
 * An individual crash report.
 * Needed to store data in the settings.
 * 
 * @author Pokechu22
 *
 */

@SerializableAs("CrashReport")
public class ThrowableReport implements ConfigurationSerializable {
	
	//From the constructor.
	public Throwable thrown;
	public CommandSender sender;
	public String cmd;
	public String label;
	public String[] args;
	
	/**
	 * When the error occurred.
	 */
	public Date loggedDate;
	
	/**
	 * Who has read the report.
	 */
	public HashSet<String> readers;
	
	/**
	 * Creates a crash report.
	 */
	public CrashReport(Throwable thrown, CommandSender sender, Command cmd, String label, String[] args) {
		this.thrown = thrown;
		this.sender = sender;
		this.cmd = cmd.toString();
		this.label = label;
		this.args = args;
		
		this.loggedDate = new Date(); //Current time.
		this.readers = new HashSet<String>();
	}
	
	/**
	 * Gets the text of this crash report, and sender to the list of people who
	 * have viewed this crash report.
	 * @param sender
	 * @return The text.
	 */
	public String getAsText(String sender) {
		readers.add(sender);
		return getAsTextNoMark();
	}
	
	/**
	 * Gets the text of this crash report, without marking a specific reader.
	 * @return The text.
	 */
	public String getAsTextNoMark() {
		StringBuilder text = new StringBuilder();
		
		text.append(thrown.toString() + "\n");
		text.append("Occured on: " + loggedDate.toString() + "\n"); 
		text.append("Sender: " + sender.getName() + "\n"); 
		text.append("Command: " + cmd + "\n");
		text.append("(Label: " + label + ")\n");
		text.append("Arguments: Length = " + args.length + "\n"); 
		//Add each argument.
		for (int i = 0; i < args.length; i++) {
			text.append("args[" + i + "]: " + args[i] + "\n");
		}
		//Add the stacktrace.
		text.append("Stacktrace: \n");
		StackTraceElement[] elements = thrown.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			text.append(elements[i].toString());
			text.append("\n");
			
		}
		
		text.append("End of report.");
		
		return text.toString();
				
	}
	
	/**
	 * Gets the title of the crash report, shortened to fit within sizeLimit.
	 * If it does not fit, it has the text "..." put on the end.
	 * @param sizeLimit The size that the title must fit in.
	 * @return The title.
	 */
	public String getTitle(int sizeLimit) {
		return getTitle(sizeLimit, "...");
	}
	
	/**
	 * Gets the title of the crash report, shortened to fit within sizeLimit.
	 * If it does not fit, it has trailOff's value put on the end.
	 * @param sizeLimit
	 * @param trailOff
	 * @return The title.
	 * @throws IllegalArgumentException if trailOff is longer than sizeLimit.
	 */
	public String getTitle(int sizeLimit, String trailOff) {
		///The raw string version, which holds the original title.
		String rawString = getTitle();
		
		//This probably won't happen, but if it does there will be issues.
		if (sizeLimit < trailOff.length()) {
			SkyblockExtension.inst().getLogger().severe("Size limit was");
			throw new IllegalArgumentException("sizeLimit (" + sizeLimit + 
					")is too small for trailOff (" + trailOff.length() + "to fit!");
		}
		
		if (rawString.length() <= sizeLimit) {
			return rawString;
		} else {
			return rawString.substring(0, sizeLimit - trailOff.length()) + trailOff;
		}
	}
	
	/**
	 * Gets the title of the CrashReport.
	 * @return The title.
	 */
	public String getTitle() {
		return thrown.toString();
	}
	
	/**
	 * Prepended to headers of messages not yet read by anyone.
	 */
	static final String NEVERREAD = "§c";
	/**
	 * Prepended to headers of messages read by someone, but not the current player.
	 */
	static final String READBYOTHERS = "§e";
	/**
	 * Prepended to headers of messages read by the current player.
	 */
	static final String READBYME = "§a";
	
	/**
	 * Gets the title for a specific player, providing the read/unread/other
	 * coloration.
	 * @param player The player.
	 * @return The title for said player.
	 */
	public String getTitleFor(String player) {
		return getTitleFor(player, ChatPaginator.AVERAGE_CHAT_PAGE_WIDTH, "...");
	}
	
	/**
	 * Gets the title for a specific player, providing the read/unread/other
	 * coloration, and within the specified sizelimit.
	 * @param player The player.
	 * @param sizeLimit The sizelimit.
	 * @return The title for said player.
	 */
	public String getTitleFor(String player, int sizeLimit) {
		return getTitleFor(player, sizeLimit, "...");
	}
	
	/**
	 * Gets the title for a specific player, providing the read/unread/other
	 * coloration, and within the specified sizelimit, using trailOff at the
	 * end of the string if it goes out of bounds.
	 * @param player The player.
	 * @param sizeLimit The sizelimit.
	 * @param trailOff The trail off.
	 * @return The title for said player.
	 */
	public String getTitleFor(String player, int sizeLimit, String trailOff) {
		String rawTitle = getTitle(sizeLimit, trailOff);
		String finalTitle;
		if (readers.isEmpty()) {
			finalTitle = NEVERREAD + rawTitle;
		} else if (readers.contains(player)) {
			finalTitle = READBYME + rawTitle;
		} else {
			finalTitle = READBYOTHERS + rawTitle;
		}
		return finalTitle;
	}
	
	/**
	 * Gets the number of pages this crash report takes up.
	 * @return The number of pages.
	 */
	public int getNumberOfPages() {
		return getNumberOfPages(ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT);
	}
	
	/**
	 * Gets the number of pages this crash report takes up.
	 * @param pageHeight The number of lines a page is.
	 * @return The number of pages.
	 */
	public int getNumberOfPages(int pageHeight) {
		String[] paginatedText = ChatPaginator.wordWrap(this.getAsTextNoMark(), 
				ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH);
		return (int) Math.ceil(paginatedText.length / pageHeight);
	}

	/**
	 * Serializes the crashReport for configuration saving.
	 */
	@Override
	public Map<String, Object> serialize() {
		SkyblockExtension.inst().getLogger().info("Saving crash report to map.");
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		//Serialize thrown as a map.
		//map.put("Thrown", this.thrown);
		HashMap<String, Object> thrownMap = new HashMap<String, Object>();
		thrownMap.put("Class",this.thrown.getClass().getName());
		thrownMap.put("Message", this.thrown.getMessage());
		
		ArrayList<HashMap<String, Object>> stackTraceList = 
				new ArrayList<HashMap<String, Object>>();
		
		StackTraceElement[] stackTrace = this.thrown.getStackTrace();
		for (int i = 0; i < stackTrace.length; i++) {
			HashMap<String, Object> stackTraceMap = new HashMap<String, Object>();
			stackTraceMap.put("ClassName", stackTrace[i].getClassName());
			stackTraceMap.put("MethodName", stackTrace[i].getMethodName());
			stackTraceMap.put("FileName", stackTrace[i].getFileName());
			stackTraceMap.put("LineNumber", stackTrace[i].getLineNumber());
			stackTraceList.add(stackTraceMap);
		}
		
		thrownMap.put("StackTrace", stackTraceList);
		
		//And now args.  (Apparently, configurations don't like String[]?)
		ArrayList<String> argsList = new ArrayList<String>();
		for (int i = 0; i < this.args.length; i++) {
			argsList.add(this.args[i]);
		}
		
		//The casts are useless but show the type.
		map.put("Thrown", (HashMap<String, Object>) thrownMap);
		map.put("Sender", (CommandSender) this.sender);
		map.put("Cmd", (String) this.cmd);
		map.put("Label", (String) this.label);
		map.put("Args", (ArrayList<String>) argsList);
		map.put("LoggedDate", (Date) this.loggedDate);
		map.put("Readers", (HashSet<String>) this.readers);
		
		return map;
	}
	
	/**
	 * Deserializes the crashReport for configuration loading.
	 */
	@SuppressWarnings("unchecked")
	public CrashReport(Map<String, Object> map) {
		SkyblockExtension.inst().getLogger().info("Creating crash report from map.");
		try {
			//Create thrown.
			HashMap<String,Object> thrownMap = (HashMap<String, Object>) map.get("Thrown");
			String thrownClass = (String) thrownMap.get("Class");
			String thrownMessage = (String) thrownMap.get("Message");
			
			ArrayList<HashMap<String, Object>> stackTraceListOld = 
					(ArrayList<HashMap<String, Object>>) thrownMap.get("StackTrace");
			
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
			
			Throwable t = (Throwable) Class.forName(thrownClass)
					.getConstructor(String.class)
					.newInstance(thrownMessage);
			t.setStackTrace(stackTrace);
			
			//And convert args.
			ArrayList<String> argsList = (ArrayList<String>) map.get("Args");
			String[] argsArray = argsList.toArray(new String[argsList.size()]);
			
			this.thrown = t;
			this.sender = (CommandSender) map.get("Sender");
			this.cmd = (String) map.get("Cmd");
			this.label = (String) map.get("Label");
			this.args = argsArray;
			this.loggedDate = (Date) map.get("LoggedDate");
			this.readers = (HashSet<String>) map.get("Readers");
		} catch (ClassCastException | InstantiationException | IllegalAccessException 
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException 
				| SecurityException | ClassNotFoundException e) {
			SkyblockExtension.inst().getLogger().warning("Failed to create: " + e.toString());
			SkyblockExtension.inst().getLogger().log(Level.WARNING, "Exception: ", e);
			return;
		}
		
		SkyblockExtension.inst().getLogger().info("Succeeded!");
	}
	
	/**
	 * Deserialization; calls the {@link #CrashReport(Map)} constructor.
	 * @param map
	 * @return
	 */
	public static CrashReport deserialize(Map<String, Object> map) {
		return new CrashReport(map);
	}
	
	/**
	 * Deserialization; calls the {@link #CrashReport(Map)} constructor.
	 * @param map
	 * @return
	 */
	public static CrashReport valueOf(Map<String, Object> map) {
		return new CrashReport(map);
	}
}
