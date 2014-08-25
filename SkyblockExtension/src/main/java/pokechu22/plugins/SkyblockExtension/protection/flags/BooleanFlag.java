package pokechu22.plugins.SkyblockExtension.protection.flags;


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
			return "§c\"" + value + "\" is not a valid boolean value!";
		}
		return "§aValue set sucessfully.";
	}

	@Override
	public boolean canAddToValue() {
		return false;
	}

	@Override
	public String addToValue(String addition, boolean force) {
		return "§cBooleans cannot be added to!";
	}
	
}
