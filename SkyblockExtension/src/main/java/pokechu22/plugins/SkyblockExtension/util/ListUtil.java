package pokechu22.plugins.SkyblockExtension.util;

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
		
		if (result instanceof EntityType) {
			result = (T) matchEntityType(s);
			return result;
		}
		if (result instanceof Material) {
			result = (T) Material.matchMaterial(s);
			return result;
		}
		if (result instanceof HangingType) {
			result = (T) HangingType.matchHangingType(s);
			return result;
		}
		if (result instanceof VehicleType) {
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
