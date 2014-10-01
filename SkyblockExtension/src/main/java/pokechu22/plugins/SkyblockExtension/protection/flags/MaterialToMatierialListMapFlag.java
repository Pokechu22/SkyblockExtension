package pokechu22.plugins.SkyblockExtension.protection.flags;

import static pokechu22.plugins.SkyblockExtension.util.TabCompleteUtil.TabLimit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.EntityType;

public class MaterialToMatierialListMapFlag extends IslandProtectionDataSetFlag {

	protected Object value;
	
	/**
	 * Deserialization.
	 */
	public MaterialToMatierialListMapFlag(String serialized)
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
		return FlagType.MaterialToMatierialListMapFlag;
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
		//TODO
		return "§cNot Yet Implemented";
		//return "§aFlag set successfully.";
	}
    
    @Override
	public boolean canAddToValue() {
		return true;
	}

	@Override
	public List<String> tabComplete(String action, String[] partialValues) {
		switch (action) {
		//TODO
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
		MaterialToMatierialListMapFlag other = (MaterialToMatierialListMapFlag) obj;
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
	public Object getValue() {
		return value;
	}
}
