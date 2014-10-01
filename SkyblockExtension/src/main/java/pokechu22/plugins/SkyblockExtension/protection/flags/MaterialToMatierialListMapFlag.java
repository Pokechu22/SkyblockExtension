package pokechu22.plugins.SkyblockExtension.protection.flags;

import static pokechu22.plugins.SkyblockExtension.util.TabCompleteUtil.TabLimit;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

import org.bukkit.Material;

/**
 * Flag mapping materials (EG in hand) to a list of materials 
 * (eg blocks that can interacted with)
 * 
 * TODO: This is a terrible name.
 * 
 * @author Pokechu22
 *
 */
public class MaterialToMatierialListMapFlag extends IslandProtectionDataSetFlag {

	/**
	 * Data for the individual values, eg things that can be placed on.
	 * 
	 * It's a []-based list, but it has the following special conditions:
	 * [*] = all values allowed.  [*, -DIRT] bans placement of dirt only.
	 * If it uses *, it is invalid to have any non-minus values; without
	 * the * it is illegal to have any minus values.
	 * 
	 * TODO: This is a worse name :(
	 * 
	 * @author Pokechu22
	 *
	 */
	static class SecondHalfData {
		/**
		 * Gets a serialized reperenstentation.
		 * 
		 * @return
		 */
		public String serialize() {
			return null; //TODO
		}
		
		/**
		 * Loads the values from the serialized version into this.
		 * 
		 * @param serialized
		 */
		public void deserialize(String serialized) {
			//TODO
		}
	}
	
	protected EnumMap<Material, EnumSet<Material>> value;
	
	/**
	 * Deserialization.
	 */
	public MaterialToMatierialListMapFlag(String serialized)
			throws IllegalArgumentException {
		value = new EnumMap<Material, EnumSet<Material>>(Material.class);
		
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
