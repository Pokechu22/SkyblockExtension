package pokechu22.plugins.SkyblockExtension.errorhandling;

import static pokechu22.plugins.SkyblockExtension.util.StringUtil.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.ChatPaginator;

/**
 * Crash report as an abstract class.
 * <br>
 * NOTE: Use <code>super.{@link #serializeBase()}</code> to serialize the
 * base values.
 * @author Pokechu22
 *
 */

@SerializableAs("CrashReport")
public abstract class CrashReport implements ConfigurationSerializable {
	
	/**
	 * The date the error occured on.
	 */
	public Date loggedDate;
	
	/**
	 * The stacktrace that lead to this event.
	 */
	public StackTraceElement[] localStackTrace;
	
	/**
	 * Who has read the report.
	 */
	public HashSet<String> readers;
	
	/**
	 * Who has hidden the report.
	 */
	public HashSet<String> hiders;
	
	/**
	 * Initiates the internal values.
	 */
	protected CrashReport() {
		this.loggedDate = new Date(); //Current time.
		this.localStackTrace = new Throwable().getStackTrace();
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
	 * 
	 * This method is called by {@link #getAsText}, and is used internally for many other things.
	 * @return The text.
	 */
	public abstract String getAsTextNoMark();

	/**
	 * Gets the title of the crash report, shortened to fit within sizeLimit.
	 * If it does not fit, it has the text "..." put on the end.
	 * @param sizeLimit The size that the title must fit in.
	 * @return The title.
	 */
	public String getTitle(int sizeLimit) {
		return trailOff(getTitle(), sizeLimit);
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
		return trailOff(getTitle(), sizeLimit, trailOff);
	}

	/**
	 * Gets the title of the CrashReport.
	 * @return The title.
	 */
	public abstract String getTitle();

	/**
	 * Prepended to headers of messages not yet read by anyone.
	 */
	protected static final String NEVERREAD = "§c";
	/**
	 * Prepended to headers of messages read by someone, but not the current player.
	 */
	protected static final String READBYOTHERS = "§e";
	/**
	 * Prepended to headers of messages read by the current player.
	 */
	protected static final String READBYME = "§a";
	
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
	 * Sends the RAW text of this report.
	 * As in, the base YML.  
	 * It's kind of ugly looking, but will provide the raw data.
	 * The only use for this is accessing parameters that aren't given
	 * normally; for instance the localStackTrace field here.
	 */
	public final String getAsRawYaml() {
		YamlConfiguration cfg = new YamlConfiguration();
		cfg.createSection("ReportRoot", this.serialize());
		return cfg.saveToString();
	}
	
	/**
	 * Marks this report as read for this specific player.
	 * @param sender
	 */
	public boolean setRead(String sender) {
		return this.readers.add(sender);
	}
	
	/**
	 * Marks this report as unread for this specific player.
	 * Returns whether the user was present in the first place.
	 * @param sender
	 */
	public boolean setUnread(String sender) {
		return this.readers.remove(sender);
	}

	/**
	 * Hides this report from this specific player.
	 * @param sender
	 */
	public boolean hideFrom(String sender) {
		return this.hiders.add(sender);
	}
	
	/**
	 * Unhides this report from this specific player.
	 * Returns whether the user was present in the first place.
	 * @param sender
	 */
	public boolean unhideFrom(String sender) {
		return this.hiders.remove(sender);
	}
	
	/**
	 * Deserializes the CrashReport from an existing map.
	 *
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	protected CrashReport(Map<String, Object> map) {
		this.loggedDate = new Date((Long)map.get("LoggedDate"));
		//Deserialize stacktrace.
		ArrayList<HashMap<String, Object>> stackTraceListOld = 
				(ArrayList<HashMap<String, Object>>) map.get("LocalStackTrace");

		this.localStackTrace = new StackTraceElement[stackTraceListOld.size()];
		for (int i = 0; i < stackTraceListOld.size(); i++) {
			HashMap<String, Object> stackTraceElementMap = stackTraceListOld.get(i);

			this.localStackTrace[i] = new StackTraceElement(
					(String) stackTraceElementMap.get("ClassName"), //declaringClass
					(String) stackTraceElementMap.get("MethodName"), //methodName
					(String) stackTraceElementMap.get("FileName"), //fileName
					(int) stackTraceElementMap.get("LineNumber") //lineNumber
					);
		}
		
		this.readers = (HashSet<String>)map.get("Readers");
	}
	
	/**
	 * Serializes the crashReport to a map.
	 */
	protected Map<String, Object> serializeBase() {
		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> stackTraceList = 
				new ArrayList<HashMap<String, Object>>();
		
		for (int i = 0; i < localStackTrace.length; i++) {
			HashMap<String, Object> stackTraceMap = new HashMap<String, Object>();
			stackTraceMap.put("ClassName", localStackTrace[i].getClassName());
			stackTraceMap.put("MethodName", localStackTrace[i].getMethodName());
			stackTraceMap.put("FileName", localStackTrace[i].getFileName());
			stackTraceMap.put("LineNumber", localStackTrace[i].getLineNumber());
			stackTraceList.add(stackTraceMap);
		}
		
		map.put("LocalStackTrace", stackTraceList);
		map.put("LoggedDate", this.loggedDate.getTime());
		
		map.put("Readers", this.readers);
		
		return map;
	}
}