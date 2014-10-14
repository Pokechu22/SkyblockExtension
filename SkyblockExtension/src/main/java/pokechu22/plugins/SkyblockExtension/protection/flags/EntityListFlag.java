package pokechu22.plugins.SkyblockExtension.protection.flags;

import static pokechu22.plugins.SkyblockExtension.util.TabCompleteUtil.TabLimit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.EntityType;

import pokechu22.plugins.SkyblockExtension.util.ListUtil;
import pokechu22.plugins.SkyblockExtension.util.nbt.ListTag;
import pokechu22.plugins.SkyblockExtension.util.nbt.StringTag;
import pokechu22.plugins.SkyblockExtension.util.nbt.Tag;

public class EntityListFlag extends IslandProtectionDataSetFlag {

	protected ArrayList<EntityType> value;
	
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
	public FlagType getType() {
		return FlagType.ENTITYLIST;
	}

	@Override
	public String getSerializedValue() {
		return value.toString();
	}

	@Override
	public String getDisplayValue() {
		return value.toString();
	}

	@Override
	public String setValue(String value) {
		if (value == null) {
			return "§cValue cannot be null!";
		}
		
		//If there is only 1 [ and 1 ], and both are at the start 
		//and end of the strings...
		if (!((value.indexOf("[") == 0) 
				&& (value.indexOf("[", 1) == -1)
				&& (value.indexOf("]") == (value.length() - 1)))) {
			return "§cList format is invalid: It must start with " +
					"'[' and end with ']', and not have any '['" + 
					" or ']' anywhere else in it.";
		}
		String[] entityStrings = 
				value.substring(1, value.length() - 1)
				.split(",");
		ArrayList<EntityType> tempList = new ArrayList<EntityType>();
		for (int i = 0; i < entityStrings.length; i++) {
			//Skip empty values.
			if (entityStrings[i].isEmpty()) {
				continue;
			}
			
			EntityType entity = 
					/*EntityType.*/matchEntityType(entityStrings[i].trim());
			if (entity == null) {
				return "§cEntity \"" + entityStrings[i] + 
						"\" is not recognised.\n(Location: " + 
						//Bolds the error.
						value.replaceAll(entityStrings[i], 
								"§4§l" + entityStrings[i] + 
								"§r§c") + ")\n" + 
						"§cTry using tab completion next time.";
			}
			
			if (tempList.contains(entity)) {
				return "§cEntity \"" + entityStrings[i] + 
						"\" is already entered.  (Repeat entries " +
						"are not allowed).\n(Location: " + 
						//Bolds the error.
						value.replaceAll(entityStrings[i], 
								"§4§l" + entityStrings[i] + 
								"§r§c") + ")";
			}
			
			tempList.add(entity);
			
		}
		
		//Now handle the field.
		this.value = tempList;
		
		return "§aFlag set successfully.";
	}

	@Override
	public List<String> getActions() {
		return Arrays.asList(new String[]{"set", "get", "add", "add-f"});
	}

	@Override
	public String preformAction(String action, String[] args) {
		String singleArgs;
		//Wrapping to control visibility!
		{
			StringBuilder m = new StringBuilder();
			for (int i = 0; i < args.length; i++) {
				m.append(args[i]);
				if (i == args.length - 1) {
					m.append(" ");
				}
			}
			singleArgs = m.toString();
		}
		
		switch (action) {
		case "set": {
			return setValue(singleArgs);
		}
		case "get": {
			return getDisplayValue();
		}
		case "add": {
			return addToValue(singleArgs, false);
		}
		case "add-f": {
			return addToValue(singleArgs, true);
		}
		}
		return "§c" + action + " is not a valid action for this flag!";
	}

	public String addToValue(String addition, boolean force) {
		//If there is only 1 [ and 1 ], and both are at the start 
		//and end of the strings...
		if (!((addition.indexOf("[") == 0) 
				&& (addition.indexOf("[", 1) == -1)
				&& (addition.indexOf("]") == (addition.length() - 1)))) {
			return "§cList format is invalid: It must start with " +
					"'[' and end with ']', and not have any '['" + 
					" or ']' anywhere else in it.";
		}
		String[] entityStrings = 
				addition.substring(1, addition.length() - 1)
				.split(",");
		List<EntityType> addList = new ArrayList<EntityType>();
		for (int i = 0; i < entityStrings.length; i++) {
			EntityType entity = 
					/*EntityType.*/matchEntityType(entityStrings[i].trim());
			if (entity == null) {
				return "§cEntity \"" + entityStrings[i] + 
						"\" is not recognised.\n(Location: " + 
						//Bolds the error.
						addition.replaceAll(entityStrings[i], 
								"§4§l" + entityStrings[i] + 
								"§r§c") + ")\n" + 
								"§cTry using tab completion next time.";
			}

			if (addList.contains(entity)) {
				return "§cEntity \"" + entityStrings[i] + 
						"\" is already entered.  (Repeat entries " +
						"are not allowed).\n(Location: " + 
						//Bolds the error.
						addition.replaceAll(entityStrings[i], 
								"§4§l" + entityStrings[i] + 
								"§r§c") + ")";
			}

			addList.add(entity);

		}

		//Now handle the field.
		List<EntityType> oldList = this.value;

		///Number of errors while merging, for the error message.
		int mergeErrorCount = 0;
		///The actual error message.
		String highlightedErrors = addition;
		ArrayList<String> erroredValues = new ArrayList<String>();

		//Color used for the string.
		String usedColor = (force ? "§e" : "§c");

		for (EntityType e : oldList) {
			if (addList.contains(e)) {
				mergeErrorCount ++;
				erroredValues.add(e.toString());
			}
		}
		if (mergeErrorCount != 0) {
			for (String errored : erroredValues) {
				highlightedErrors = highlightedErrors
						.replaceAll(errored, 
								"§4§l" + errored + "§r" + usedColor);
			}

			if (!force) {
				return "§cPrevious values (Count: " + mergeErrorCount + 
						") are already listed.  (Forcible merging " + 
						"can be done by using the force option.\n" + 
						"§cAlready-existant values: " + 
						erroredValues.toString() + "\n" + 
						"§cLocations: " + highlightedErrors + ".";
			} else {
				oldList.addAll(addList);
				
				//Displays in yellow, but starts with red for error checking
				return "§c§ePrevious values (Count: " + mergeErrorCount + 
						") are already listed.  (Forcible merging " + 
						"can be done by using the force option.\n" + 
						"§cAlready-existant values: " + 
						erroredValues.toString() + "\n" + 
						"§cLocations: " + highlightedErrors + ".";
			}
		}

		oldList.addAll(addList);

		return "§aFlag set successfully.";
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

            filtered = filtered.replaceAll("\\s+", "_")
            		.replaceAll("\\W", "");
            result = EntityType.fromName(filtered);
            if (result == null)
            	try {
            	    result = EntityType.valueOf(filtered.toUpperCase());
            	}
                catch (IllegalArgumentException ex){
                	// okay to swallow it; we'll just return null
                } 
        }

        return result;
    }

    @Override
	public List<String> tabComplete(String action, String[] partialValues) {
		switch (action) {
		case "view": {
			return new ArrayList<String>();
		}
		case "add": //Fall thru
		case "add-f": //Fall thru
		case "set": {
			return TabLimit(partialValues, EntityType.values());
		}
		default: {
			return new ArrayList<String>();
		}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		EntityListFlag other = (EntityListFlag) obj;
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	@Override
	public ArrayList<EntityType> getValue() {
		return value;
	}
	
	@Override
	public ListTag<StringTag> serializeToNBT(String name) {
		ListTag<StringTag> returned = new ListTag<>(name);
		
		for (EntityType type : this.value) {
			returned.add(new StringTag(type.name()));
		}
		
		return returned;
	}
	
	@Override
	public void deserializeFromNBT(Tag value) throws InvalidConfigurationException {
		if (value == null) {
			throw new InvalidConfigurationException("Expected StringTag or ListTag, got " + 
					"null value");
		}
		
		if (value instanceof StringTag) {
			StringTag tag = (StringTag) value;
			this.setValue(tag.data);
			return;
		} else if (value instanceof ListTag) {
			this.value = new ArrayList<EntityType>();
			
			//Ensure that it is actually a ListTag<StringTag>.
			if (((ListTag<?>) value).size() > 0) {
				Object at0 = ((ListTag<?>)value).get(0); 
				if (!(at0 instanceof StringTag)) {
					throw new InvalidConfigurationException("Expected StringTag or " +
							"ListTag<StringTag>, got " + 
							value.getClass().getName() + "<" + at0.getClass().getName() + 
							">.  (Value: " + value.toString() + ")");
				}
			} else {
				return;
			}
			
			//Actually get the values.
			@SuppressWarnings("unchecked")
			ListTag<StringTag> values = (ListTag<StringTag>) value;
			
			for (int i = 0; i < values.size(); i++) {
				EntityType type = ListUtil
						.matchEnumValue(values.get(i).data, EntityType.class);
				
				if (type == null) {
					throw new InvalidConfigurationException("Failed to parse ListTag - " + 
							"Failed to match enum value for " + values.get(i).data + 
							".  Index: " + i + ".  (Full Value: " +
							value.toString() + ")");
				} else {
					this.value.add(type);
				}
			}
			
			return;
		}
		
		throw new InvalidConfigurationException("Expected StringTag Or ListTag, got " + 
				value.getClass().getName() + ".  (Value: " +
				value.toString() + ")");
	}
}
