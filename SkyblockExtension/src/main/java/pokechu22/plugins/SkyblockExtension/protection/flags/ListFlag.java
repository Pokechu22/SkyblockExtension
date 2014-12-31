package pokechu22.plugins.SkyblockExtension.protection.flags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;

import pokechu22.plugins.SkyblockExtension.util.nbt.ListTag;
import pokechu22.plugins.SkyblockExtension.util.nbt.StringTag;
import pokechu22.plugins.SkyblockExtension.util.nbt.Tag;

/**
 * Base flag type that provides general list flag actions. 
 * 
 * @author Pokechu22
 *
 * @param <E> The enumeration to use for this flag.
 */
public abstract class ListFlag<E extends Enum<E>> extends
		IslandProtectionDataSetFlag {
	
	protected ArrayList<E> value;
	
	/**
	 * Gets the name of the type to use here.
	 * This is used in error message outputs.
	 * <br>
	 * For example, EntityListFlag would return "entity".<br>
	 * TODO: This seems like a rather inelegant solution.  Find 
	 * something better.
	 * 
	 * @param capitalize Whether or not the first letter should be
	 *                   capitalized.
	 * @return The singular form of the thing that is listed.
	 */
	protected abstract String getTypeName(boolean capitalize);

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
		String[] valueStrings =
				value.substring(1, value.length() - 1)
				.split(",");
		ArrayList<E> tempList = new ArrayList<>();
		for (int i = 0; i < valueStrings.length; i++) {
			//Skip empty values.
			if (valueStrings[i].isEmpty()) {
				continue;
			}
			
			E entity = matchEnumValue(valueStrings[i].trim());
			if (entity == null) {
				return "§c" + getTypeName(false) + " \"" + valueStrings[i] +
						"\" is not recognised.\n(Location: " + 
						//Bolds the error.
						value.replaceAll(valueStrings[i], 
								"§4§l" + valueStrings[i] + 
								"§r§c") + ")\n" + 
						"§cTry using tab completion next time.";
			}
			
			if (tempList.contains(entity)) {
				return "§c" + getTypeName(false) + " \"" + valueStrings[i] + 
						"\" is already entered.  (Repeat entries " +
						"are not allowed).\n(Location: " + 
						//Bolds the error.
						value.replaceAll(valueStrings[i], 
								"§4§l" + valueStrings[i] + 
								"§r§c") + ")";
			}
			
			tempList.add(entity);
			
		}
		
		//Now handle the field.
		this.value = tempList;
		
		return "§aFlag set successfully.";
	}
	
	/**
	 * {@inheritDoc}
	 * <hr>
	 * The available actions are:
	 * <!-- TODO I don't know if using list items like this is OK, but it
	 *      seems to work right now.  I want a bulleted list here. --> 
	 * <ul><dl>
	 * <li><dt><code>set</code></dt></li>
	 * <dd>
	 * Sets the value (same as {@link #setValue(String)}).
	 * Expects a list in the usual format.
	 * </dd>
	 * <li><dt><code>get</code></dt></li>
	 * <dd>Gets the value (same as {@link #getDisplayValue()}).</dd>
	 * <li><dt><code>add</code></dt></li>
	 * <dd>Adds a single element. Warns if that element already exists.</dd>
	 * <li><dt><code>addmultiple</code></dt></li>
	 * <dd>
	 * Adds multiple elements.
	 * This list of elements is in the usual list format.<br>
	 * Already-existing elements are illegal and result in cancellation of 
	 * the entire action.  (To avoid this, use <code>addmultiple-f</code>.)
	 * </dd>
	 * <li><dt><code>addmultiple-f</code></dt></li>
	 * <dd>
	 * Adds multiple elements.
	 * This list of elements is in the usual list format.<br>
	 * Already-existing elements are allowed.
	 * </dd>
	 * <li><dt><code>remove</code></dt></li>
	 * <dd>
	 * Removes a single element.<br>
	 * If that element already exists, an error is presented.
	 * </dd> 
	 * <li><dt><code>removemultiple</code></dt></li>
	 * <dd>
	 * Removes multiple elements.
	 * This list of elements is in the usual list format.<br>
	 * Already-nonexisting elements are illegal and result in cancellation
	 * of the entire action. (To avoid this, use
	 * <code>removemultiple-f</code>.)
	 * </dd>
	 * <li><dt><code>removemultiple-f</code></dt></li>
	 * <dd>
	 * Adds multiple elements.
	 * This list of elements is in the usual list format.<br>
	 * Already-nonexisting elements are allowed.</dd>
	 * </dl></ul>
	 */
	@Override
	public List<String> getActions() {
		return Arrays.asList(new String[]{
				"set",
				"get",
				"add",
				"addmultiple",
				"addmultiple-f",
				"remove",
				"removemultiple",
				"removemultiple-f"
				});
	}

	@Override
	public String preformAction(String action, String[] args) {
		String singleArgs;
		//Wrapping to control visibility!
		{
			StringBuilder m = new StringBuilder();
			for (int i = 0; i < args.length; i++) {
				m.append(args[i]);
				if (i != args.length - 1) { //If NOT final value
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
			return addToValue(singleArgs);
		}
		case "addmultiple": {
			return addMultipleToValue(singleArgs, false);
		}
		case "addmultiple-f": {
			return addMultipleToValue(singleArgs, true);
		}
		case "remove": {
			return removeFromValue(singleArgs);
		}
		case "removemultiple": {
			return removeMultipleFromValue(singleArgs, false);
		}
		case "removemultiple-f": {
			return removeMultipleFromValue(singleArgs, true);
		}
		}
		return "§c" + action + " is not a valid action for this flag!";
	}
	
	/**
	 * Adds a single value. 
	 * @param valueToAdd The value to remove.
	 * @return
	 */
	public String addToValue(String valueToAdd) {
		E e = matchEnumValue(valueToAdd);
		
		if (e == null) {
			return "§c" + valueToAdd + "is not a recognised " +
					getTypeName(false) + "!";
		}
		
		if (!this.value.contains(e)) {
			this.value.add(e);
			return "§aValue added successfully.";
		} else {
			return "§c" + valueToAdd + " is already in the list!";
		}
	}
	
	/**
	 * Adds multiple values to this flag.
	 * 
	 * @param addition The list-formated group of values to add.
	 * @param force Whether or not to allow adding already-existing values.
	 * @return A value indicating the result of the action.
	 */
	public String addMultipleToValue(String addition, boolean force) {
		//If there is only 1 [ and 1 ], and both are at the start 
		//and end of the strings...
		if (!((addition.indexOf("[") == 0) 
				&& (addition.indexOf("[", 1) == -1)
				&& (addition.indexOf("]") == (addition.length() - 1)))) {
			return "§cList format is invalid: It must start with " +
					"'[' and end with ']', and not have any '['" + 
					" or ']' anywhere else in it.";
		}
		String[] valueStrings = 
				addition.substring(1, addition.length() - 1)
				.split(",");
		List<E> addList = new ArrayList<>();
		for (int i = 0; i < valueStrings.length; i++) {
			E newValue = matchEnumValue(valueStrings[i].trim());
			if (newValue == null) {
				return "§c" + getTypeName(false) + " \"" + valueStrings[i] +
						"\" is not recognised.\n(Location: " + 
						//Bolds the error.
						addition.replaceAll(valueStrings[i], 
								"§4§l" + valueStrings[i] + 
								"§r§c") + ")\n" + 
								"§cTry using tab completion next time.";
			}

			if (addList.contains(newValue)) {
				return "§c" + getTypeName(false) + " \"" + valueStrings[i] +
						"\" is already entered.  (Repeat entries " +
						"are not allowed).\n(Location: " + 
						//Bolds the error.
						addition.replaceAll(valueStrings[i], 
								"§4§l" + valueStrings[i] + 
								"§r§c") + ")";
			}

			addList.add(newValue);

		}

		///Number of errors while merging, for the error message.
		int mergeErrorCount = 0;
		///The actual error message.
		String highlightedErrors = addition;
		ArrayList<String> erroredValues = new ArrayList<String>();

		//Color used for the string.
		String usedColor = (force ? "§e" : "§c");

		for (E e : this.value) {
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
				this.value.addAll(addList);
				
				//Displays in yellow, but starts with red for error checking
				return "§c§ePrevious values (Count: " + mergeErrorCount + 
						") are already listed.  (Forcible merging " + 
						"was done by using the force option.\n" + 
						"§eAlready-existant values: " + 
						erroredValues.toString() + "\n" + 
						"§eLocations: " + highlightedErrors + ".";
			}
		}

		this.value.addAll(addList);

		return "§aFlag added successfully.";
	}
	
	/**
	 * Removes a single value. 
	 * @param valueToRemove The value to remove.
	 * @return
	 */
	public String removeFromValue(String valueToRemove) {
		E e = matchEnumValue(valueToRemove);
		
		if (e == null) {
			return "§c" + valueToRemove + "is not a recognised " +
					getTypeName(false) + "!";
		}
		
		boolean valueWasInList = this.value.remove(e);
		if (valueWasInList) {
			return "§aValue removed successfully.";
		} else {
			return "§c" + valueToRemove + " was already missing!";
		}
	}
	
	//TODO: Tab completion
	
	/**
	 * Removes multiple values from this flag.
	 * 
	 * @param removal The list-formated group of values to remove.
	 * @param force Whether or not to allow removing already-missing values.
	 * @return A value indicating the result of the action.
	 */
	public String removeMultipleFromValue(String removal, boolean force) {
		//If there is only 1 [ and 1 ], and both are at the start 
		//and end of the strings...
		if (!((removal.indexOf("[") == 0) 
				&& (removal.indexOf("[", 1) == -1)
				&& (removal.indexOf("]") == (removal.length() - 1)))) {
			return "§cList format is invalid: It must start with " +
					"'[' and end with ']', and not have any '['" + 
					" or ']' anywhere else in it.";
		}
		String[] valueStrings = 
				removal.substring(1, removal.length() - 1)
				.split(",");
		List<E> removalList = new ArrayList<>();
		for (int i = 0; i < valueStrings.length; i++) {
			E newValue = matchEnumValue(valueStrings[i].trim());
			if (newValue == null) {
				return "§c" + getTypeName(false) + " \"" + valueStrings[i] +
						"\" is not recognised.\n(Location: " + 
						//Bolds the error.
						removal.replaceAll(valueStrings[i], 
								"§4§l" + valueStrings[i] + 
								"§r§c") + ")\n" + 
								"§cTry using tab completion next time.";
			}

			if (removalList.contains(newValue)) {
				return "§c" + getTypeName(false) + " \"" + valueStrings[i] +
						"\" is already entered.  (Repeat entries " +
						"are not allowed).\n(Location: " + 
						//Bolds the error.
						removal.replaceAll(valueStrings[i], 
								"§4§l" + valueStrings[i] + 
								"§r§c") + ")";
			}

			removalList.add(newValue);

		}

		///Number of errors while merging, for the error message.
		int mergeErrorCount = 0;
		///The actual error message.
		String highlightedErrors = removal;
		ArrayList<String> erroredValues = new ArrayList<String>();

		//Color used for the string.
		String usedColor = (force ? "§e" : "§c");

		for (E e : removalList) {
			if (!this.value.contains(e)) {
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
						") are already missing.  (Forcible merging " + 
						"can be done by using the force option.\n" + 
						"§cAlready-nonexistant values: " + 
						erroredValues.toString() + "\n" + 
						"§cLocations: " + highlightedErrors + ".";
			} else {
				this.value.removeAll(removalList);
				
				//Displays in yellow, but starts with red for error checking
				return "§c§ePrevious values (Count: " + mergeErrorCount + 
						") are already missing.  (Forcible merging " + 
						"was done by using the force option.\n" + 
						"§eAlready-nonexistant values: " + 
						erroredValues.toString() + "\n" + 
						"§eLocations: " + highlightedErrors + ".";
			}
		}

		this.value.removeAll(removalList);

		return "§aFlag added successfully.";
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
		if (!(obj instanceof ListFlag)) {
			return false;
		}
		ListFlag<?> other = (ListFlag<?>) obj;
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	public ArrayList<E> getValue() {
		return value;
	}
	
	@Override
	public ListTag<StringTag> serializeToNBT(String name) {
		ListTag<StringTag> returned = new ListTag<>(name);
		
		for (E e : this.value) {
			returned.add(new StringTag("Value", e.name()));
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
			this.value = new ArrayList<E>();
			
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
				E type = matchEnumValue(values.get(i).data);
				
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
	
	/**
	 * Matches an enum value, such that capitalization is ignored.
	 * This method may also take into account numeric IDs and such.
	 * Most enum types already have a method that handles this, but
	 * it is not part of the base enum class.
	 * 
	 * This method should not throw any exception, <i>unless
	 * <code>null</code> is provided as an argument</i>, in which case an 
	 * {@link IllegalArgumentException} or {@link NullPointerException}
	 * should be thrown.  Null should be returned when no such value exists
	 * in the enum.
	 * 
	 * @param valueToMatch
	 * @return An enum value.
	 */
	protected abstract E matchEnumValue(String valueToMatch)
			throws IllegalArgumentException, NullPointerException;
}
