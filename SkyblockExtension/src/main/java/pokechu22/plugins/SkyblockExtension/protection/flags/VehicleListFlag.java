package pokechu22.plugins.SkyblockExtension.protection.flags;

import static pokechu22.plugins.SkyblockExtension.util.TabCompleteUtil.TabLimit;

import java.util.ArrayList;
import java.util.List;

import pokechu22.plugins.SkyblockExtension.protection.VehicleType;

public class VehicleListFlag extends IslandProtectionDataSetFlag {

	protected ArrayList<VehicleType> value;
	
	/**
	 * Deserialization.
	 * 
	 * @throws IllegalArgumentException when given invalid data.
	 */
	public VehicleListFlag(String serialized)
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
		return FlagType.VEHICLELIST;
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
		String[] vehicleStrings = 
				value.substring(1, value.length() - 1)
				.split(",");
		ArrayList<VehicleType> tempList = new ArrayList<VehicleType>();
		for (int i = 0; i < vehicleStrings.length; i++) {
			//Skip empty values.
			if (vehicleStrings[i].isEmpty()) {
				continue;
			}
			
			VehicleType vehicle = 
					VehicleType.matchVehicleType(vehicleStrings[i].trim());
			if (vehicle == null) {
				return "§cVehicle \"" + vehicleStrings[i] + 
						"\" is not recognised.\n(Location: " + 
						//Bolds the error.
						value.replaceAll(vehicleStrings[i], 
								"§4§l" + vehicleStrings[i] + 
								"§r§c") + ")\n" + 
						"§cTry using tab completion next time.";
			}
			
			if (tempList.contains(vehicle)) {
				return "§cVehicle \"" + vehicleStrings[i] + 
						"\" is already entered.  (Repeat entries " +
						"are not allowed).\n(Location: " + 
						//Bolds the error.
						value.replaceAll(vehicleStrings[i], 
								"§4§l" + vehicleStrings[i] + 
								"§r§c") + ")";
			}
			
			tempList.add(vehicle);
			
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
		String[] vehicleStrings = 
				addition.substring(1, addition.length() - 1)
				.split(",");
		ArrayList<VehicleType> addList = new ArrayList<VehicleType>();
		for (int i = 0; i < vehicleStrings.length; i++) {
			VehicleType vehicle = 
					VehicleType.matchVehicleType(vehicleStrings[i].trim());
			if (vehicle == null) {
				return "§cVehicle \"" + vehicleStrings[i] + 
						"\" is not recognised.\n(Location: " + 
						//Bolds the error.
						addition.replaceAll(vehicleStrings[i], 
								"§4§l" + vehicleStrings[i] + 
								"§r§c") + ")\n" + 
								"§cTry using tab completion next time.";
			}

			if (addList.contains(vehicle)) {
				return "§cVehicle \"" + vehicleStrings[i] + 
						"\" is already entered.  (Repeat entries " +
						"are not allowed).\n(Location: " + 
						//Bolds the error.
						addition.replaceAll(vehicleStrings[i], 
								"§4§l" + vehicleStrings[i] + 
								"§r§c") + ")";
			}

			addList.add(vehicle);

		}

		//Now handle the field.
		ArrayList<VehicleType> oldList = this.value;

		///Number of errors while merging, for the error message.
		int mergeErrorCount = 0;
		///The actual error message.
		String highlightedErrors = addition;
		ArrayList<String> erroredValues = new ArrayList<String>();

		//Color used for the string.
		String usedColor = (force ? "§e" : "§c");

		for (VehicleType e : oldList) {
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
		switch (action) {
		case "view": {
			return new ArrayList<String>();
		}
		case "add": //Fall thru
		case "add-f": //Fall thru
		case "set": {
			return TabLimit(partialValues, VehicleType.values());
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
		VehicleListFlag other = (VehicleListFlag) obj;
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
	public ArrayList<VehicleType> getValue() {
		return value;
	}
}
