package pokechu22.plugins.SkyblockExtension.util.blockvalue;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

/**
 * Contains all of the info used for block_value.yml.
 * 
 * @author Pokechu22
 */
@SerializableAs("SBEBlockValueData")
public class BlockValueData implements ConfigurationSerializable {
	/**
	 * Collection of {@link MaximumPool}s, intended for serialization.
	 * 
	 * @author Pokechu22
	 */
	public static class MaximumPoolCollection {
		//TODO: Serialization.
		
		public MaximumPool defaultPool;
		
		public Map<String, MaximumPool> map;
	}
	
	/**
	 * Collection of {@link BlockValuation}s, intended for serialization.
	 * 
	 * @author Pokechu22
	 */
	public static class BlockValueCollection {
		public BlockValuation defaultBlockValuation = new BlockValuation();
		
		public Map<Material, BlockValuation> map;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		return map;
		//TODO
	}
	
	public BlockValueData(Map<String, Object> map) {
		//TODO
	}
	
	public static BlockValueData deserialize(Map<String, Object> map) {
		return new BlockValueData(map);
	}
	
	public static BlockValueData valueOf(Map<String, Object> map) {
		return new BlockValueData(map);
	}
}
