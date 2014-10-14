package pokechu22.plugins.SkyblockExtension.protection.flags;

import static pokechu22.plugins.SkyblockExtension.util.TabCompleteUtil.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;

import pokechu22.plugins.SkyblockExtension.protection.HangingType;
import pokechu22.plugins.SkyblockExtension.util.ListUtil;
import pokechu22.plugins.SkyblockExtension.util.nbt.ListTag;
import pokechu22.plugins.SkyblockExtension.util.nbt.StringTag;
import pokechu22.plugins.SkyblockExtension.util.nbt.Tag;

public class HangingListFlag extends IslandProtectionDataSetFlag {

	public ArrayList<HangingType> value;
	
	/**
	 * Constructor for use with deserialization.
	 * 
	 * @deprecated Not really, but this should NOT be called normally;
	 * 				only through reflection.
	 */
	@Deprecated
	public HangingListFlag() {
		value = new ArrayList<HangingType>();
	}
	
	/**
	 * Deserialization.
	 * 
	 * @throws IllegalArgumentException When given an invalid input.
	 */
	public HangingListFlag(String serialized) 
			throws IllegalArgumentException {
		String result = this.setValue(serialized);
		if (result.startsWith("�a")) {
			return; //Valid value.
		} else {
			throw new IllegalArgumentException(result.substring(2));
		}
		
	}
	
	@Override
	public FlagType getType() {
		return FlagType.HANGINGLIST;
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
			return "�cValue cannot be null!";
		}
		
		//If there is only 1 [ and 1 ], and both are at the start 
		//and end of the strings...
		if (!((value.indexOf("[") == 0) 
				&& (value.indexOf("[", 1) == -1)
				&& (value.indexOf("]") == (value.length() - 1)))) {
			return "�cList format is invalid: It must start with " +
					"'[' and end with ']', and not have any '['" + 
					" or ']' anywhere else in it.";
		}
		String[] hangingStrings = 
				value.substring(1, value.length() - 1)
				.split(",");
		ArrayList<HangingType> tempList = new ArrayList<HangingType>();
		for (int i = 0; i < hangingStrings.length; i++) {
			//Skip empty values.
			if (hangingStrings[i].isEmpty()) {
				continue;
			}
			
			HangingType hanging = 
					HangingType.matchHangingType(hangingStrings[i].trim());
			if (hanging == null) {
				return "�cHanging \"" + hangingStrings[i] + 
						"\" is not recognised.\n(Location: " + 
						//Bolds the error.
						value.replaceAll(hangingStrings[i], 
								"�4�l" + hangingStrings[i] + 
								"�r�c") + ")\n" + 
						"�cTry using tab completion next time.";
			}
			
			if (tempList.contains(hanging)) {
				return "�cHanging \"" + hangingStrings[i] + 
						"\" is already entered.  (Repeat entries " +
						"are not allowed).\n(Location: " + 
						//Bolds the error.
						value.replaceAll(hangingStrings[i], 
								"�4�l" + hangingStrings[i] + 
								"�r�c") + ")";
			}
			
			tempList.add(hanging);
			
		}
		
		//Now handle the field.
		this.value = tempList;
		
		return "�aFlag set successfully.";
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
		return "�c" + action + " is not a valid action for this flag!";
	}

	public String addToValue(String addition, boolean force) {
		//If there is only 1 [ and 1 ], and both are at the start 
		//and end of the strings...
		if (!((addition.indexOf("[") == 0) 
				&& (addition.indexOf("[", 1) == -1)
				&& (addition.indexOf("]") == (addition.length() - 1)))) {
			return "�cList format is invalid: It must start with " +
					"'[' and end with ']', and not have any '['" + 
					" or ']' anywhere else in it.";
		}
		String[] hangingStrings = 
				addition.substring(1, addition.length() - 1)
				.split(",");
		ArrayList<HangingType> addList = new ArrayList<HangingType>();
		for (int i = 0; i < hangingStrings.length; i++) {
			HangingType hanging = 
					HangingType.matchHangingType(hangingStrings[i].trim());
			if (hanging == null) {
				return "�cHanging \"" + hangingStrings[i] + 
						"\" is not recognised.\n(Location: " + 
						//Bolds the error.
						addition.replaceAll(hangingStrings[i], 
								"�4�l" + hangingStrings[i] + 
								"�r�c") + ")\n" + 
								"�cTry using tab completion next time.";
			}

			if (addList.contains(hanging)) {
				return "�cHanging \"" + hangingStrings[i] + 
						"\" is already entered.  (Repeat entries " +
						"are not allowed).\n(Location: " + 
						//Bolds the error.
						addition.replaceAll(hangingStrings[i], 
								"�4�l" + hangingStrings[i] + 
								"�r�c") + ")";
			}

			addList.add(hanging);

		}

		//Now handle the field.
		ArrayList<HangingType> oldList = this.value;

		///Number of errors while merging, for the error message.
		int mergeErrorCount = 0;
		///The actual error message.
		String highlightedErrors = addition;
		ArrayList<String> erroredValues = new ArrayList<String>();

		//Color used for the string.
		String usedColor = (force ? "�e" : "�c");

		for (HangingType e : oldList) {
			if (addList.contains(e)) {
				mergeErrorCount ++;
				erroredValues.add(e.toString());
			}
		}
		if (mergeErrorCount != 0) {
			for (String errored : erroredValues) {
				highlightedErrors = highlightedErrors
						.replaceAll(errored, 
								"�4�l" + errored + "�r" + usedColor);
			}

			if (!force) {
				return "�cPrevious values (Count: " + mergeErrorCount + 
						") are already listed.  (Forcible merging " + 
						"can be done by using the force option.\n" + 
						"�cAlready-existant values: " + 
						erroredValues.toString() + "\n" + 
						"�cLocations: " + highlightedErrors + ".";
			} else {
				oldList.addAll(addList);
				
				//Displays in yellow, but starts with red for error checking
				return "�c�ePrevious values (Count: " + mergeErrorCount + 
						") are already listed.  (Forcible merging " + 
						"can be done by using the force option.\n" + 
						"�cAlready-existant values: " + 
						erroredValues.toString() + "\n" + 
						"�cLocations: " + highlightedErrors + ".";
			}
		}

		oldList.addAll(addList);

		return "�aFlag set successfully.";
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
			return TabLimit(partialValues, HangingType.values());
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
		HangingListFlag other = (HangingListFlag) obj;
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
	public ArrayList<HangingType> getValue() {
		return value;
	}
	
	@Override
	public ListTag<StringTag> serializeToNBT(String name) {
		ListTag<StringTag> returned = new ListTag<>(name);
		
		for (HangingType type : this.value) {
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
			this.value = new ArrayList<HangingType>();
			
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
				HangingType type = ListUtil
						.matchEnumValue(values.get(i).data, HangingType.class);
				
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
