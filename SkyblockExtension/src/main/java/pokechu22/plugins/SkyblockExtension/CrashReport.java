package pokechu22.plugins.SkyblockExtension;

import java.util.HashSet;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.ChatPaginator;

/**
 * Crash report as an abstract class.
 * 
 * NOTE: You MUST implement a public ThrowableReport(Map<String, Object> map) { 
 * @author Pokechu22
 *
 */

@SerializableAs("CrashReport")
public abstract class CrashReport implements ConfigurationSerializable {
	
	/**
	 * Who has read the report.
	 */
	public HashSet<String> readers = new HashSet<String>();
	
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
	
	
}