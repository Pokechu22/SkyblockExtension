package pokechu22.plugins.SkyblockExtension.util.blockvalue;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
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
	public static class MaximumPoolCollection implements 
			ConfigurationSerializable {
		public MaximumPool defaultPool;
		
		public Map<String, MaximumPool> map;

		public MaximumPool getValueOrDefault(String key) {
			if (key.equalsIgnoreCase("default")) {
				return defaultPool;
			}
			if (map.containsKey(key)) {
				return map.get(key);
			} else {
				return defaultPool;
			}
		}
		
		@Override
		public Map<String, Object> serialize() {
			Map<String, Object> map = new HashMap<>();
			
			map.putAll(this.map);
			map.put("default", defaultPool);
			
			return map;
		}
		
		public MaximumPoolCollection(Map<String, Object> serialized) {
			for (Map.Entry<String, Object> e : serialized.entrySet()) {
				if (e.getKey().equals("default")) {
					this.defaultPool = (MaximumPool) e.getValue();
				} else {
					this.map.put(e.getKey(), (MaximumPool) e.getValue());
				}
			}
		}
		
		public static BlockValueCollection deserialize(
				Map<String, Object> map) {
			return new BlockValueCollection(map);
		}
		
		public static BlockValueCollection valueOf(
				Map<String, Object> map) {
			return new BlockValueCollection(map);
		}
	}
	
	/**
	 * Collection of {@link BlockValuation}s, intended for serialization.
	 * 
	 * @author Pokechu22
	 */
	public static class BlockValueCollection implements
			ConfigurationSerializable {
		public BlockValuation defaultBlockValuation = new BlockValuation();
		
		public Map<Material, BlockValuation> map;
		
		@Override
		public Map<String, Object> serialize() {
			Map<String, Object> returned = new HashMap<>();
			returned.put("defaultBlockValuation", defaultBlockValuation);
			for (Map.Entry<Material, BlockValuation> e : map.entrySet()) {
				returned.put(e.getKey().toString(), e.getValue());
			}
			
			return returned;
		}
		
		public BlockValueCollection(Map<String, Object> serialized) {
			this.map = new HashMap<>();
			for (Map.Entry<String, Object> e : serialized.entrySet()) {
				if (e.getKey().equals("defaultBlockValuation")) {
					this.defaultBlockValuation = 
							(BlockValuation) e.getValue();
				} else {
					Material m = Material.matchMaterial(e.getKey());
					if (m == null) {
						throw new RuntimeException(
								new InvalidConfigurationException(
								"Failed to parse material: " + 
								m + " (this is during BlockValueMapping" +
								"deserialization)..."));
					}
					this.map.put(m, (BlockValuation) e.getValue());
				}
			}
		}
		
		public static MaximumPoolCollection deserialize(
				Map<String, Object> map) {
			return new MaximumPoolCollection(map);
		}
		
		public static MaximumPoolCollection valueOf(
				Map<String, Object> map) {
			return new MaximumPoolCollection(map);
		}
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
