package pokechu22.plugins.SkyblockExtension.protection.flags;

import static pokechu22.plugins.SkyblockExtension.util.TabCompleteUtil.TabLimit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BooleanFlag extends IslandProtectionDataSetFlag {

	protected boolean value;
	
	/**
	 * Package-Protected constructor for use with deserialization.
	 */
	BooleanFlag() {
		value = false;
	}
	
	/**
	 * Deserialization constructor.
	 * @throws IllegalArgumentException when given invalid boolean.
	 */
	public BooleanFlag(String value) throws IllegalArgumentException {
		if (value == null) {
			throw new IllegalArgumentException("Value cannot be null!");
		}
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
	public String getDisplayValue() {
		return Boolean.toString(value);
	}

	@Override
	public String setValue(String value) {
		if (value == null) {
			return "§cValue cannot be null!";
		}
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
	public List<String> getActions() {
		return Arrays.asList(new String[]{"set", "get"});
	}

	@Override
	public String preformAction(String action, String[] args) {
		switch (action) {
		case "set": {
			StringBuilder m = new StringBuilder();
			for (int i = 0; i < args.length; i++) {
				m.append(args[i]);
				if (i == args.length - 1) {
					m.append(" ");
				}
			}
			return setValue(m.toString());
		}
		case "get": {
			return getDisplayValue();
		}
		}
		return "§c" + action + " is not a valid action for this flag!";
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (value ? 1231 : 1237);
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
		BooleanFlag other = (BooleanFlag) obj;
		if (value != other.value) {
			return false;
		}
		return true;
	}

	@Override
	public Boolean getValue() {
		return new Boolean(value);
	}
}
