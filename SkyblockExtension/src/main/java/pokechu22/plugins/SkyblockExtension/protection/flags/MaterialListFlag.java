package pokechu22.plugins.SkyblockExtension.protection.flags;

import java.util.ArrayList;

import org.bukkit.Material;

public class MaterialListFlag extends ListFlag<Material> {
	
	/**
	 * Constructor for use with deserialization.
	 * 
	 * @deprecated Not really, but this should NOT be called normally;
	 * 				only through reflection.
	 */
	@Deprecated
	public MaterialListFlag() {
		value = new ArrayList<Material>();
	}
	
	/**
	 * Deserialization.
	 * 
	 * @throws IllegalArgumentException when given an invalid value.
	 */
	public MaterialListFlag(String serialized)
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
		return FlagType.MATERIALLIST;
	}

	@Override
	protected String getTypeName(boolean capitalize) {
		return capitalize ? "Material" : "material";
	}

	@Override
	protected Material matchEnumValue(String valueToMatch)
			throws IllegalArgumentException, NullPointerException {
		return Material.matchMaterial(valueToMatch);
	}
}
