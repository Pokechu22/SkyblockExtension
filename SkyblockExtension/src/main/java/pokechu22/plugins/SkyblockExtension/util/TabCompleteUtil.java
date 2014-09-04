package pokechu22.plugins.SkyblockExtension.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
	public static List<String> TabLimit(Collection<String> values, String starting){
		return TabLimit(new ArrayList<String>(values), starting);
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
		
		String fullValue = "";
		for (String partialValue : partialValues) {
			fullValue += partialValue;
			fullValue += " ";
		}
		fullValue = fullValue.trim();
		
		if (fullValue.startsWith("[") && fullValue.endsWith("]")) {
			//Already filled.
			return returned;
		}
		
		//Almost duplicate code, but functional.
		if (partialValues.length == 1) {
			if (!partialValues[0].startsWith("[")) {
				returned.add("[" + partialValues[0]);
				return returned;
			}
			
			int splitPoint = partialValues[0].lastIndexOf(",");
			
			String previousValues; 
			String currentValue;
			
			System.out.println(splitPoint);
			
			if (splitPoint == -1) {
				previousValues = "[";
				currentValue = partialValues[0].substring(1);
			} else {
				previousValues = partialValues[0].substring(0, splitPoint + 1);
				currentValue = partialValues[0].substring(splitPoint + 1);
			}
			
			for (String option : options) {
				//Complete match
				if (option.equalsIgnoreCase(currentValue)) {
					returned.add(previousValues + option + ", ");
					return returned;
				}
				//Partial match
				if (option.toUpperCase(Locale.ENGLISH).startsWith(
						currentValue.toUpperCase())) {
					returned.add(previousValues + option);
				}
			}
			
			returned.add(previousValues + "]");
			
			return returned;
		} else {
			//Position to modify.
			int i = partialValues.length - 1;
			
			int splitPoint = partialValues[i].lastIndexOf(",");
			
			String previousValues; 
			String currentValue;
			
			System.out.println(splitPoint);
			
			if (splitPoint == -1) {
				previousValues = "";
				currentValue = partialValues[i];
			} else {
				previousValues = partialValues[i].substring(0, splitPoint + 1);
				currentValue = partialValues[i].substring(splitPoint + 1);
			}
			
			for (String option : options) {
				//Complete match
				if (option.equalsIgnoreCase(currentValue)) {
					returned.add(previousValues + option + ", ");
					return returned;
				}
				//Partial match
				if (option.toUpperCase(Locale.ENGLISH).startsWith(
						currentValue.toUpperCase())) {
					returned.add(previousValues + option);
				}
			}
			
			returned.add(previousValues + "]");
			
			return returned;
		}
	}
	
	public static List<String> TabLimit(String[] partialValues, 
			List<String> options) {
		//TODO
		return TabLimit(partialValues, 
				options.toArray(new String[options.size()]));
	}
	
	public static List<String> TabLimit(String[] partialValues, 
			Enum<?>[] options) {
		String[] optionsList = new String[options.length];
		for (int i = 0; i < options.length; i++) {
			optionsList[i] = options[i].toString();
		}
		//TODO
		return TabLimit(partialValues, optionsList);
	}
}
