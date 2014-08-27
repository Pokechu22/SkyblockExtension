package pokechu22.plugins.SkyblockExtension.protection.flags;

import static pokechu22.plugins.SkyblockExtension.util.TabCompleteUtil.TabLimit;

import java.util.ArrayList;
import java.util.List;


public class BooleanFlag extends IslandProtectionDataSetFlag {

	protected boolean value;
	
	/**
	 * Deserialization constructor.
	 * @throws IllegalArgumentException when given invalid boolean.
	 */
	public BooleanFlag(String value) throws IllegalArgumentException {
		if (value.equalsIgnoreCase("false")) {
			this.value = false;
		} else if (value.equalsIgnoreCase("true")) {
			this.value = true;
		} else {
			throw new IllegalArgumentException("Invalid boolean type: " + 
					value + ".");
		}
	}
	
	@Override
	public FlagType getType() {
		return FlagType.BOOLEAN;
	}

	@Override
	public String getSerializedValue() {
		return Boolean.toString(value);
	}

	@Override
	public String getDispayValue() {
		return Boolean.toString(value);
	}

	@Override
	public String setValue(String value) {
		if (value.equalsIgnoreCase("false")) {
			this.value = false;
		} else if (value.equalsIgnoreCase("true")) {
			this.value = true;
		} else {
			return "�c\"" + value + "\" is not a valid boolean value!";
		}
		return "�aValue set sucessfully.";
	}

	@Override
	public boolean canAddToValue() {
		return false;
	}

	@Override
	public String addToValue(String addition, boolean force) {
		return "�cBooleans cannot be added to!";
	}
	
	@Override
	public List<String> tabComplete(String action, String[] partialValues) {
		if (partialValues.length == 0) {
			List<String> returned = new ArrayList<String>();
			returned.add("false");
			returned.add("true");
			return returned;
		} else if (partialValues.length == 1) {
			List<String> returned = new ArrayList<String>();
			returned.add("false");
			returned.add("true");
			return TabLimit(returned, partialValues[0]);
		} else {
			return new ArrayList<String>();
		}
	}
}
