package pokechu22.plugins.SkyblockExtension.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import pokechu22.plugins.SkyblockExtension.protection.HangingType;
import pokechu22.plugins.SkyblockExtension.protection.VehicleType;

/**
 * Handles list values over text.
 * 
 * Note that this does handle special types.
 * 
 * @author Pokechu22
 */
public class ListUtil {
	
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
		
		StringBuilder actualData = new StringBuilder();
		for (String value : listData) {
			actualData.append(value);
		}
		
		return parseList(actualData.toString(), type);
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
		//If there is only 1 [ and 1 ], and both are at the start 
		//and end of the strings...
		if (!((listData.indexOf("[") == 0) 
				&& (listData.indexOf("[", 1) == -1)
				&& (listData.indexOf("]") == (listData.length() - 1)))) {
			throw new ParseException("§cList format is invalid: It must start with " +
					"'[' and end with ']', and not have any '['" + 
					" or ']' anywhere else in it.", 0);
		}
		
		String[] stringValues = 
				listData.substring(1, listData.length() - 1)
				.split(",");
		
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
		if (type.equals(HangingType.class)) {
			result = (T) HangingType.matchHangingType(s);
			return result;
		}
		if (type.equals(VehicleType.class)) {
			result = (T) VehicleType.matchVehicleType(s);
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
