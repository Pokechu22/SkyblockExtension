package pokechu22.plugins.SkyblockExtension.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

/**
 * Handles list values over text.
 * 
 * Note that this does handle special types.
 * 
 * @author Pokechu22
 */
public class ListUtil {
	
	/**
	 * Checks the formating of a list to ensure that it is valid.
	 * If no exception is thrown, it is valid.
	 * 
	 * Assumes list starts with '[' and ends with ']'.  For other chars,
	 * use the other functions.
	 * 
	 * @param listData 
	 * @throws ParseException when list is invalid.
	 */
	public static void validateList(String[] listData) throws ParseException {
		if (listData == null) {
			throw new ParseException("§cValue cannot be null!", -1);
		}
		
		validateList(listData, "[", "]");
	}
	
	/**
	 * Checks the formating of a list to ensure that it is valid.
	 * If no exception is thrown, it is valid.
	 * 
	 * Assumes list starts with '[' and ends with ']'.  For other chars,
	 * use the other functions.
	 * 
	 * @param listData
	 * @throws ParseException when list is invalid.
	 */
	public static void validateList(String listData) throws ParseException {
		if (listData == null) {
			throw new ParseException("§cValue cannot be null!", -1);
		}
		
		validateList(listData, "[", "]");
	}
	
	/**
	 * Checks the formating of a list to ensure that it is valid.
	 * If no exception is thrown, it is valid.
	 * 
	 * @param listData
	 * @param opening The opening character.
	 * @param closing The closing character.
	 * @throws ParseException when the list is invalid.
	 */
	public static void validateList(String[] listData, String opening, String closing) 
			throws ParseException {
		if (listData == null) {
			throw new ParseException("§cValue cannot be null!", -1);
		}
		
		StringBuilder actualData = new StringBuilder();
		for (String value : listData) {
			actualData.append(value);
		}
		
		validateList(actualData.toString(), "[", "]");
	}
	
	/**
	 * Checks the formating of a list to ensure that it is valid.
	 * If no exception is thrown, it is valid.
	 * 
	 * @param listData
	 * @param opening The opening character.
	 * @param closing The closing character.
	 * @throws ParseException when the list is invalid.
	 */
	public static void validateList(String listData, String opening, String closing) 
			throws ParseException {
		if (listData == null) {
			throw new ParseException("§cValue cannot be null!", -1);
		}
		
		if (!((listData.indexOf(opening) == 0) 
				&& (listData.indexOf(opening, 1) == -1)
				&& (listData.indexOf(closing) == (listData.length() - 1)))) {
			throw new ParseException("§cList format is invalid: It must start with '" +
					opening + "' and end with '" + closing + "', and not have any '" +
					opening + "' or '" + closing + "' anywhere else in it.", 0);
		}
	}
	
	/**
	 * Parses a list of the specified type.
	 * 
	 * @param listData an array of all data.  Each value is joined together.
	 *                 Multiple values can be put together at a single index
	 *                 as long as they are comma-separated. 
	 * @param type The enum to use for the list.
	 * @return The list.
	 * @throws ParseException when given invalid list.  This contains
	 *         information that can be sent to a player, including
	 *         color formating.
	 */
	public static <T extends Enum<T>> List<T> parseList(String[] listData, Class<T> type)
			throws ParseException {
		if (listData == null) {
			throw new ParseException("§cValue cannot be null!", -1);
		}
		
		return parseList(listData, type, ",");
	}
	
	/**
	 * Parses a list of the specified type.
	 * 
	 * @param listData All data, with each value comma-separated.  Spaces
	 *                 are ignored.
	 * @param type The enum to use for the list.
	 * @return The list.
	 * @throws ParseException when given invalid list.  This contains
	 *         information that can be sent to a player, including
	 *         color formating.
	 */
	public static <T extends Enum<T>> List<T> parseList(String listData, Class<T> type)
			throws ParseException {
		if (listData == null) {
			throw new ParseException("§cValue cannot be null!", -1);
		}
		
		return parseList(listData, type, ",");
	}
	
	/**
	 * Parses a list of the specified type.
	 * 
	 * @param listData All data, with each value separated as
	 *                 specified below.  Spaces are ignored.
	 * @param type The enum to use for the list.
	 * @param seperator The separator to use.
	 * @return The list.
	 * @throws ParseException when given invalid list.  This contains
	 *         information that can be sent to a player, including
	 *         color formating.
	 */
	public static <T extends Enum<T>> List<T> parseList(String[] listData, Class<T> type, 
			String seperator) throws ParseException {
		if (listData == null) {
			throw new ParseException("§cValue cannot be null!", -1);
		}
		
		StringBuilder actualData = new StringBuilder();
		for (String value : listData) {
			actualData.append(value);
		}
		
		return parseList(actualData.toString(), type, ",");
	}
	
	/**
	 * Parses a list of the specified type.
	 * 
	 * @param listData All data, with each value separated as
	 *                 specified below.  Spaces are ignored.
	 * @param type The enum to use for the list.
	 * @param seperator The separator to use.
	 * @return The list.
	 * @throws ParseException when given invalid list.  This contains
	 *         information that can be sent to a player, including
	 *         color formating.
	 */
	public static <T extends Enum<T>> List<T> parseList(String listData, Class<T> type, 
			String seperator) throws ParseException {
		if (listData == null) {
			throw new ParseException("§cValue cannot be null!", -1);
		}
		
		return parseList(listData, type, seperator, "[", "]");
	}

	/**
	 * Parses a list of the specified type.
	 * 
	 * @param listData The data to parse.
	 * @param type The type to use
	 * @param opening The opening symbol of the list
	 * @param closing The closing symbol of the list
	 * @return The list
	 * @throws ParseException When list format is invalid
	 */
	public static <T extends Enum<T>> List<T> parseList(String listData[], Class<T> type, 
			String opening, String closing) throws ParseException {
		if (listData == null) {
			throw new ParseException("§cValue cannot be null!", -1);
		}
		
		StringBuilder actualData = new StringBuilder();
		for (String value : listData) {
			actualData.append(value);
		}
		
		return parseList(actualData.toString(), type, ",", opening, closing);
	}

	/**
	 * Parses a list of the specified type.
	 * 
	 * @param listData The data to parse.
	 * @param type The type to use
	 * @param opening The opening symbol of the list
	 * @param closing The closing symbol of the list
	 * @return The list
	 * @throws ParseException When list format is invalid
	 */
	public static <T extends Enum<T>> List<T> parseList(String listData, Class<T> type, 
			String opening, String closing) throws ParseException {
		if (listData == null) {
			throw new ParseException("§cValue cannot be null!", -1);
		}
		
		return parseList(listData, type, ",", opening, closing);
	}
	
	/**
	 * Parses a list of the specified type.
	 * 
	 * @param listData The data to parse.
	 * @param type The type to use
	 * @param seperator The separator between values
	 * @param opening The opening symbol of the list
	 * @param closing The closing symbol of the list
	 * @return The list
	 * @throws ParseException When list format is invalid
	 */
	public static <T extends Enum<T>> List<T> parseList(String listData[], Class<T> type, 
			String seperator, String opening, String closing) throws ParseException {
		if (listData == null) {
			throw new ParseException("§cValue cannot be null!", -1);
		}
		
		StringBuilder actualData = new StringBuilder();
		for (String value : listData) {
			actualData.append(value);
		}
		
		return parseList(actualData.toString(), type, seperator, opening, closing);
	}

	/**
	 * Parses a list of the specified type.
	 * 
	 * @param listData The data to parse.
	 * @param type The type to use
	 * @param seperator The separator between values
	 * @param opening The opening symbol of the list
	 * @param closing The closing symbol of the list
	 * @return The list
	 * @throws ParseException When list format is invalid
	 */
	public static <T extends Enum<T>> List<T> parseList(String listData, Class<T> type, 
			String seperator, String opening, String closing) throws ParseException {
		if (listData == null) {
			throw new ParseException("§cValue cannot be null!", -1);
		}
		
		//Needed to allow for spacing.
		seperator = seperator.trim();
		
		//Ensure list is valid.
		validateList(listData, opening, closing);
		
		String[] stringValues = 
				listData.substring(1, listData.length() - 1)
				.split(seperator);
		
		ArrayList<T> tempList = new ArrayList<T>();
		for (int i = 0; i < stringValues.length; i++) {
			//Trim the value.
			stringValues[i] = stringValues[i].trim();
			
			//Skip empty values.
			if (stringValues[i].isEmpty()) {
				continue;
			}
			
			T value = matchEnumValue(stringValues[i], type);
			if (value == null) {
				throw new ParseException("§c" + type.getSimpleName() + " \"" + 
						stringValues[i] + "\" is not recognised.\n(Location: " + 
						//Bolds the error.
						listData.replaceAll(stringValues[i], 
								"§4§l" + stringValues[i] + 
								"§r§c") + ")\n" + 
						"§cTry using tab completion next time.", 
						listData.indexOf(stringValues[i]));
			}
			
			if (tempList.contains(value)) {
				throw new ParseException("§c" + type.getSimpleName() +
						" \"" + stringValues[i] + 
						"\" is already entered.  (Repeat entries " +
						"are not allowed).\n(Location: " + 
						//Bolds the error.
						listData.replaceAll(stringValues[i], 
								"§4§l" + stringValues[i] + 
								"§r§c") + ")", 
								//Slightly odd, but this is the seccond index of it.
								listData.indexOf(stringValues[i],
										listData.indexOf(stringValues[i]) + 1));
			}
			
			tempList.add(value);
			
		}
		
		return tempList;
	}
	
	/**
	 * Converts a list to a comma-separated string.
	 * This method uses the {@link java.util.AbstractCollection#toString()}
	 * method to create the list.  It starts and ends with brackets.
	 * <br>
	 * EG A list containing EGGS and MILK gives <samp>[EGGS, MILK]</samp>.
	 * @param value
	 * @return
	 */
	public static <T> String convertListToString(List<T> value) {
		//Creating an ArrayList in case there is no AbstractCollection.
		return new ArrayList<T>(value).toString();
	}
	
	/**
	 * Converts a list to a string, using that separator.
	 * The list starts and ends with brackets.
	 * <br>
	 * EG A list containing EGGS and MILK gives <samp>[EGGS; MILK]</samp>
	 * when separator "; " is used.
	 * 
	 * @param value
	 * @param separator
	 * @return
	 */
	public static <T> String convertListToString(List<T> value, String separator) {
		return convertListToString(value, separator, "[", "]");
	}
	
	/**
	 * Converts a list to a string, using the provided symbols.
	 * It is comma-separated.
	 * <br>
	 * EG A list containing EGGS and MILK gives <samp>{EGGS, MILK}</samp>
	 * when using "{" and "}".
	 * 
	 * @param value
	 * @param openingSymbol
	 * @param closingSymbol
	 * @return
	 */
	public static <T> String convertListToString(List<T> value, 
			String openingSymbol, String closingSymbol) {
		return convertListToString(value, ", ", openingSymbol, closingSymbol);
	}
	
	/**
	 * Converts a list to a string, using the provided symbols.
	 * It is comma-separated.
	 * <br>
	 * EG A list containing EGGS and MILK gives <samp>{EGGS; MILK}</samp>
	 * when using "{" and "}" for symbols and "; " for separator.
	 * 
	 * @param value
	 * @param separator
	 * @param openingSymbol
	 * @param closingSymbol
	 * @return
	 */
	public static <T> String convertListToString(List<T> value, String separator, 
			String openingSymbol, String closingSymbol) {
		StringBuilder ret = new StringBuilder();
		ret.append(openingSymbol);
		
		Iterator<T> itr = value.iterator();
		while(itr.hasNext()) {
			ret.append(itr.next());
			//Append the separator only if there is another value.
			if (itr.hasNext()) {
				ret.append(separator);
			}
		}
		
		ret.append(closingSymbol);
		return ret.toString();
	}
	
	/**
	 * Matches an enum value.
	 * <br>
	 * For a few specific enums, the value is obtained with a different
	 * method: EntityType and {@link #matchEntityType(String)}; Material
	 * and {@link Material#matchMaterial(String)}; HangingType and 
	 * {@link HangingType#matchHangingType(String)}; VehicleType and
	 * {@link VehicleType#matchVehicleType(String)}.
	 * 
	 * @param s
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Enum<T>> T matchEnumValue(String s, Class<T> type) {
		T result = null;
		
		s = s.trim();
		
		if (type.equals(EntityType.class)) {
			result = (T) matchEntityType(s);
			return result;
		}
		if (type.equals(Material.class)) {
			result = (T) Material.matchMaterial(s);
			return result;
		}
		
		String filtered = s.toUpperCase(Locale.ENGLISH);

		filtered = filtered.replaceAll("\\s+", "_").replaceAll("\\W", "");
		
		try {
			result = Enum.valueOf(type, filtered);
		} catch (IllegalArgumentException ex) {
			// okay to swallow it; we'll just return null
		}
		
		return result;
	}
	
	/**
	 * Method taken from upcoming bukkit source (see 
	 * <a href=https://github.com/Bukkit/Bukkit/pull/1107>Pull Request #1107
	 * </a> and <a href=https://bukkit.atlassian.net/browse/BUKKIT-5777>
	 * BUKKIT-5777 Jira Ticket</a>.
	 * 
	 * TODO: Remove this when added to the actual API.
	 * <hr>
	 * Attempts to match the EntityType with the given name.
	 * <p>
	 * This is a match lookup; names will be converted to lowercase, then
	 * stripped of special characters in an attempt to format it like the
	 * enum.
	 * <p>
	 * Using this for match by ID is deprecated.
	 *
	 * @param name Name of the entity type to get
	 * @return EntityType if found, or null
	 */
	@SuppressWarnings("deprecation")
	private static EntityType matchEntityType(final String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null");
		}

		EntityType result = null;

		try {
			result = EntityType.fromId(Integer.parseInt(name));
		} catch (NumberFormatException ex) {}

		if (result == null) {
			String filtered = name.toLowerCase();

			filtered = filtered.replaceAll("\\s+", "_").replaceAll("\\W", "");
			result = EntityType.fromName(filtered);
			if (result == null) {
				try {
					result = EntityType.valueOf(filtered.toUpperCase());
				} catch (IllegalArgumentException ex) {
					// okay to swallow it; we'll just return null
				}
			}
		}

		return result;
	}
}
