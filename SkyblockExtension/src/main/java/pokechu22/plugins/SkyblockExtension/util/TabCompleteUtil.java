package pokechu22.plugins.SkyblockExtension.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tab-completion utilities, such as limiting to the argument.
 * 
 * @author Pokechu22
 *
 */
public class TabCompleteUtil {
	/**
	 * Filters tab completion for starting with same phrase.
	 *
	 * @param list
	 * @param starting
	 * @return
	 */
	public static List<String> TabLimit(List<String> list, String starting) {
		ArrayList<String> returned = new ArrayList<String>();
		for (String s : list) {
			if (s.startsWith(starting)) {
				returned.add(s);
			}
		}
		return returned;
	}
	
	/**
	 * Filters tab completion for starting with same phrase.
	 *
	 * @param list
	 * @param starting
	 * @return
	 */
	public static List<String> TabLimit(String[] values, String starting) {
		List<String> list = Arrays.asList(values);
		
		ArrayList<String> returned = new ArrayList<String>();
		for (String s : list) {
			if (s.startsWith(starting)) {
				returned.add(s);
			}
		}
		return returned;
	}
}
