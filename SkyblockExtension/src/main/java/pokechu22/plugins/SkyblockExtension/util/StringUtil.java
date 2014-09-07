package pokechu22.plugins.SkyblockExtension.util;

import org.bukkit.util.ChatPaginator;

/**
 * Provides convenience methods for sending a message.
 * TODO: Might want a better name for this.
 *
 * @author Pokechu22
 *
 */
public class StringUtil {
	/**
	 * Truncates a string to fit within 
	 * {@link ChatPaginator#GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH}.  
	 * If it does not fit, it cuts off the string and appends "..." at 
	 * the end.  (sizeLimit includes the size of the "..." if it does 
	 * not fit).  
	 * 
	 * <h1>Examples:</h1>
	 * No useful examples can be provided due to ChatPaginator's value
	 * potentially changing - Check the other functions listed in &#64;see. 
	 * 
	 * @param string The original text.
	 * @param sizeLimit The size to limit it to.
	 * 
	 * @throws IllegalArgumentException when given a sizeLimit that is 
	 * 		   smaller than 3.
	 * 
	 * @return The cutoff string.
	 * 
	 * @see {@link #trailOff(String, int)} - Allows use of a custom size, 
	 * 		and a <code>trailOff</code> of "...".
	 * @see {@link #trailOff(String, int, String)} - Allows full 
	 * 		customization of <code>sizeLimit</code> and 
	 * 		<code>trailOff</code>.
	 */
	public static String trailOff(String string) 
			throws IllegalArgumentException {
		return trailOff(string, 
				ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH, "...");
	}
	
	/**
	 * Truncates a string to fit within sizeLimit.  If it does not fit, it 
	 * cuts off the string and appends "..." at the end.  (sizeLimit 
	 * includes the size of the "..." if it does not fit).  
	 * 
	 * <h1>Examples:</h1>
	 * <dl>
	 * <dt><code>trailOff("Hello, world!", 80)</code></dt>
	 * <dd><samp>"Hello, world!"</samp></dd>
	 * <dt><code>trailOff("Hello, world!", 10)</code></dt>
	 * <dd><samp>"Hello, ..."</dd>
	 * <dt><code>trailOff("Not Hello World", 11)</code></dt>
	 * <dd><samp>"Not Hell..."</samp></dd>
	 * <dt><code>trailOff("Arbitrary String", 1)</code></dt>
	 * <dd>A thrown {@link IllegalArgumentException}.</dd>
	 * </dl>
	 * 
	 * @param string The original text.
	 * @param sizeLimit The size to limit it to.
	 * 
	 * @throws IllegalArgumentException when given a sizeLimit that is 
	 * 		   smaller than 3.
	 * 
	 * @return The cutoff string.
	 * 
	 * @see {@link #trailOff(String)} - Calls this with a 
	 * 		<code>sizeLimit</code> of 
	 * 		{@link ChatPaginator#GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH} (a 
	 * 		nice default value) and a <code>trailOff</code> of "...".
	 * @see {@link #trailOff(String, int, String)} - Allows full 
	 * 		customization of <code>sizeLimit</code> and 
	 * 		<code>trailOff</code>.
	 */
	public static String trailOff(String string, int sizeLimit) 
			throws IllegalArgumentException {
		return trailOff(string, sizeLimit, "...");
	}
	
	/**
	 * Truncates a string to fit within sizeLimit.  If it does not fit, it 
	 * cuts off the string and appends trailOff at the end.  (sizeLimit 
	 * includes the size of trailOff if it does not fit).  
	 * 
	 * <h1>Examples:</h1>
	 * <dl>
	 * <dt><code>trailOff("Hello, world!", 80, "...")</code></dt>
	 * <dd><samp>"Hello, world!"</samp></dd>
	 * <dt><code>trailOff("Hello, world!", 10, "...")</code></dt>
	 * <dd><samp>"Hello, ..."</samp></dd>
	 * <dt><code>trailOff("Not Hello World", 8, "")</code></dt>
	 * <dd><samp>"Not Hell"</samp></dd>
	 * <dt><code>trailOff("Arbitrary", 4, "Long trailOff")</code></dt>
	 * <dd>A thrown {@link IllegalArgumentException}.</dd>
	 * </dl>
	 * 
	 * @param string The original text.
	 * @param sizeLimit The size to limit it to.
	 * @param trailOff The text to put at the end.
	 * 
	 * @throws IllegalArgumentException when given a sizeLimit that is 
	 * 		   smaller than trailOff.
	 * 
	 * @return The cutoff string.
	 * 
	 * @see {@link #trailOff(String, int)} - Calls this with a 
	 * 		<code>trailOff</code> of "...".
	 * @see {@link #trailOff(String)} - Calls this with a 
	 * 		<code>sizeLimit</code> of 
	 * 		{@link ChatPaginator#GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH} (a 
	 * 		nice default value) and a <code>trailOff</code> of "...".
	 */
	public static String trailOff(String string, int sizeLimit, 
			String trailOff) throws IllegalArgumentException {
		if (sizeLimit < trailOff.length()) {
			throw new IllegalArgumentException("sizeLimit (" + sizeLimit + 
					")is too small for trailOff (" + trailOff.length() + 
					"to fit!");
		}
		
		if (string.length() <= sizeLimit) {
			return string;
		} else {
			return string.substring(0, sizeLimit - trailOff.length()) 
					+ trailOff;
		}
	}
}
