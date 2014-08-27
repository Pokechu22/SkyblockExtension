package pokechu22.plugins.SkyblockExtension.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
	public static List<String> TabLimit(List<String> list, String starting){
		ArrayList<String> returned = new ArrayList<String>();
		for (String s : list) {
			if (s.toLowerCase(Locale.ENGLISH).startsWith(starting
					.toLowerCase(Locale.ENGLISH))) {
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
		return TabLimit(Arrays.asList(values), starting);
	}
	
	public static List<String> TabLimit(String[] partialValues, 
			String[] options) {
		List<String> returned = new ArrayList<String>();
		
		if (partialValues.length == 0) {
			returned.add("[");
			return returned;
		}
		
		//First letter to be used.
		int firstChar = 0;
		
		if (partialValues.length == 1) {
			//Starting char.
			if (partialValues[0].length() == 0) {
				returned.add("[");
				return returned;				
			}
			if (!partialValues[0].startsWith("[")) {
				partialValues[0] = "[" + partialValues[0];
				firstChar = 1;
			}
		}
		
		//Index in the array.
		int i = partialValues.length - 1;
		
		String subValue = (partialValues[i]).substring(
				(partialValues[i].contains(",") ? 
						partialValues[i].lastIndexOf(",") : firstChar), 
						partialValues[i].length());
		
		for (String option : options) {
			//If it is an EXACT match
			if (option.equalsIgnoreCase(subValue)) {
				returned.add(partialValues[i] + ", ");
				return returned;
			}
			//If it starts.
			if (option.toUpperCase(Locale.ENGLISH)
					.startsWith(subValue.toUpperCase(Locale.ENGLISH))) {
				//Get the new section and add it.
				String origional = (partialValues[i]).substring(firstChar,
						(partialValues[i].contains(",") ? 
								partialValues[i].lastIndexOf(",") : 
									partialValues[i].length()));
				String addition = partialValues[i].replace(subValue, "");
				returned.add(origional + addition);
			}
		}
		
		return returned;
	}
	
	public static List<String> TabLimit(String[] partialValues, 
			List<String> options) {
		return TabLimit(partialValues, 
				options.toArray(new String[options.size()]));
	}
	
	public static List<String> TabLimit(String[] partialValues, 
			Enum<?>[] options) {
		String[] optionsList = new String[options.length];
		for (int i = 0; i < options.length; i++) {
			optionsList[i] = options[i].toString();
		}
		return TabLimit(partialValues, optionsList);
	}
}
