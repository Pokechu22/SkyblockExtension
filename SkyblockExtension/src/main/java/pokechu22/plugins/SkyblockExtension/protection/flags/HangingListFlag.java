package pokechu22.plugins.SkyblockExtension.protection.flags;

import static pokechu22.plugins.SkyblockExtension.util.TabCompleteUtil.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pokechu22.plugins.SkyblockExtension.protection.HangingType;

public class HangingListFlag extends IslandProtectionDataSetFlag {

	public ArrayList<HangingType> value;
	
	/**
	 * Deserialization.
	 * 
	 * @throws IllegalArgumentException When given an invalid input.
	 */
	public HangingListFlag(String serialized) 
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
		return FlagType.HANGINGLIST;
	}

	@Override
	public String getSerializedValue() {
		return value.toString();
	}

	@Override
	public String getDispayValue() {
		return value.toString();
	}

	@Override
	public String setValue(String value) {
		//If there is only 1 [ and 1 ], and both are at the start 
		//and end of the strings...
		if (!((value.indexOf("[") == 0) 
				&& (value.indexOf("[", 1) == -1)
				&& (value.indexOf("]") == (value.length() - 1)))) {
			return "§cList format is invalid: It must start with " +
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
					HangingType.valueOf(hangingStrings[i].trim()
							.toUpperCase());
			if (hanging == null) {
				return "§cHanging \"" + hangingStrings[i] + 
						"\" is not recognised.\n(Location: " + 
						//Bolds the error.
						value.replaceAll(hangingStrings[i], 
								"§4§l" + hangingStrings[i] + 
								"§r§c") + ")\n" + 
						"§cTry using tab completion next time.";
			}
			
			if (tempList.contains(hanging)) {
				return "§cHanging \"" + hangingStrings[i] + 
						"\" is already entered.  (Repeat entries " +
						"are not allowed).\n(Location: " + 
						//Bolds the error.
						value.replaceAll(hangingStrings[i], 
								"§4§l" + hangingStrings[i] + 
								"§r§c") + ")";
			}
			
			tempList.add(hanging);
			
		}
		
		//Now handle the field.
		this.value = tempList;
		
		return "§aFlag set successfully.";
	}

	@Override
	public boolean canAddToValue() {
		return true;
	}

	@Override
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
		String[] hangingStrings = 
				addition.substring(1, addition.length() - 1)
				.split(",");
		ArrayList<HangingType> addList = new ArrayList<HangingType>();
		for (int i = 0; i < hangingStrings.length; i++) {
			HangingType hanging = 
					HangingType.matchHangingType(hangingStrings[i]);
			if (hanging == null) {
				return "§cHanging \"" + hangingStrings[i] + 
						"\" is not recognised.\n(Location: " + 
						//Bolds the error.
						addition.replaceAll(hangingStrings[i], 
								"§4§l" + hangingStrings[i] + 
								"§r§c") + ")\n" + 
								"§cTry using tab completion next time.";
			}

			if (addList.contains(hanging)) {
				return "§cHanging \"" + hangingStrings[i] + 
						"\" is already entered.  (Repeat entries " +
						"are not allowed).\n(Location: " + 
						//Bolds the error.
						addition.replaceAll(hangingStrings[i], 
								"§4§l" + hangingStrings[i] + 
								"§r§c") + ")";
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
		String usedColor = (force ? "§e" : "§c");

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

	@Override
	public List<String> tabComplete(String action, String[] partialValues) {
		ArrayList<String> returned = new ArrayList<String>();
		
		if (action.equalsIgnoreCase("view")) {
			return new ArrayList<String>();
		}
		
		String currentValueGroup;
		String currentValue;
		
		//Add the starting [ if needed.
		if ((partialValues.length == 0) || 
				(partialValues.length == 1 && partialValues[0].isEmpty())) {
			returned.add("[");
			return returned;
		} else if (partialValues.length == 1 && 
				!partialValues[0].startsWith("[")) {
			returned.add("[" + partialValues[0]);
			return returned;
		}
		
		currentValueGroup = partialValues[partialValues.length - 1];
		
		String[] currentValueGroupArray = currentValueGroup.split(",");
		currentValue = 
				currentValueGroupArray[currentValueGroupArray.length - 1];
		currentValue = currentValue.trim();
		
		for (HangingType h : HangingType.values()) {
			if (h.name().toUpperCase(Locale.ENGLISH)
					.startsWith(currentValue.toUpperCase(Locale.ENGLISH))) {
				returned.add(currentValueGroup.replace(currentValue, h.name()));
			}
		}
		
		return returned;
	}
}
