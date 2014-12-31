package pokechu22.plugins.SkyblockExtension.protection.flags;

import java.util.ArrayList;

import org.bukkit.entity.EntityType;

public class EntityListFlag extends ListFlag<EntityType> {

	/**
	 * Constructor for use with deserialization.
	 * 
	 * @deprecated Not really, but this should NOT be called normally;
	 * 				only through reflection.
	 */
	@Deprecated
	public EntityListFlag() {
		value = new ArrayList<EntityType>();
	}
	
	/**
	 * Deserialization.
	 * 
	 * @throws IllegalArgumentException When given an invalid input.
	 */
	public EntityListFlag(String serialized) 
			throws IllegalArgumentException {
		String result = this.setValue(serialized);
		if (result.startsWith("§a")) {
			return; //Valid value.
		} else {
			throw new IllegalArgumentException(result.substring(2));
		}
		
	}
	
	@Override
	protected String getTypeName(boolean capitalize) {
		return capitalize ? "Entity" : "entity";
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
	@Override
	protected EntityType matchEnumValue(String valueToMatch)
			throws IllegalArgumentException, NullPointerException {
		if (valueToMatch == null) {
			throw new IllegalArgumentException("Cannot match with null!");
		}

		EntityType result = null;

		try {
			result = EntityType.fromId(Integer.parseInt(valueToMatch));
		} catch (NumberFormatException ex) {
		}

		if (result == null) {
			String filtered = valueToMatch.toLowerCase();

			filtered = filtered.replaceAll("\\s+", "_")
					.replaceAll("\\W", "");
			result = EntityType.fromName(filtered);
			if (result == null) {
				try {
					result = EntityType.valueOf(filtered.toUpperCase());
				} catch (IllegalArgumentException ex) {
					//Do nothing, as null will be returned anyways.
				}
			}
		}

		return result;
	}

	@Override
	public FlagType getType() {
		return FlagType.ENTITYLIST;
	}

}
