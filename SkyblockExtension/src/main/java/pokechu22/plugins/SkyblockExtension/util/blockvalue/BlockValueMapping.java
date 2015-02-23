package pokechu22.plugins.SkyblockExtension.util.blockvalue;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import pokechu22.plugins.SkyblockExtension.util.blockvalue.BlockValuation.BlockValueData;

/**
 * Contains all of the info used for block_value.yml.
 * 
 * @author Pokechu22
 */
@SerializableAs("SBEBlockValueMapping")
public class BlockValueMapping implements ConfigurationSerializable {
	/**
	 * Collection of {@link MaximumPool}s, intended for serialization.
	 * 
	 * @author Pokechu22
	 */
	@SerializableAs("SBEMaximumPoolCollection")
	public static class MaximumPoolCollection implements 
			ConfigurationSerializable {
		public MaximumPoolCollection() {
			this.defaultPool = new MaximumPool(-1);
			this.map = new HashMap<>();
		}
		
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
			this.map = new HashMap<>();
			
			for (Map.Entry<String, Object> e : serialized.entrySet()) {
				if (e.getKey().equals("==")) {
					//Ugly code to handle serialization issues.
					continue;
				}
				
				if (e.getKey().equals("default")) {
					this.defaultPool = (MaximumPool) e.getValue();
				} else {
					this.map.put(e.getKey(), (MaximumPool) e.getValue());
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
	
	/**
	 * Collection of {@link BlockValuation}s, intended for serialization.
	 * 
	 * @author Pokechu22
	 */
	@SerializableAs("SBEBlockValueCollection")
	public static class BlockValueCollection implements
			ConfigurationSerializable {
		public BlockValueCollection() {
			this.defaultBlockValuation = new BlockValuation();
			this.map = new HashMap<>();
		}
		
		public BlockValuation defaultBlockValuation;
		
		public Map<Material, BlockValuation> map;
		
		/**
		 * Gets the {@link BlockValuation} used for the specified material.
		 * 
		 * @param dataValue
		 * @return
		 */
		public BlockValuation getValueOrDefault(Material material) {
			if (map.containsKey(material)) {
				return map.get(material);
			} else {
				return defaultBlockValuation;
			}
		}
		
		/**
		 * Gets the {@link BlockValueData} used for the specified material
		 * and data value.
		 * 
		 * @param dataValue
		 * @return
		 */
		public BlockValueData getValueOrDefault(Material material, 
				Byte dataValue) {
			return getValueOrDefault(material).getValueOrDefault(dataValue);
		}
		
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
				if (e.getKey().equals("==")) {
					//Ugly code to handle serialization issues.
					continue;
				}
				
				if (e.getKey().equals("defaultBlockValuation")) {
					this.defaultBlockValuation = 
							(BlockValuation) e.getValue();
				} else {
					Material m = Material.matchMaterial(e.getKey());
					if (m == null) {
						throw new RuntimeException(
								new InvalidConfigurationException(
								"Failed to parse material: " + e.getKey() + 
								" (this is during BlockValueMapping " +
								"deserialization)..."));
					}
					this.map.put(m, (BlockValuation) e.getValue());
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

	public MaximumPoolCollection maximumPools;
	public BlockValueCollection blockValues;
	
	public BlockValueMapping() {
		this.maximumPools = new MaximumPoolCollection();
		this.blockValues = new BlockValueCollection();
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put("maximumPools", maximumPools);
		map.put("blockValues", blockValues);
		return map;
	}
	
	public BlockValueMapping(Map<String, Object> map) {
		this.maximumPools = (MaximumPoolCollection) map.get("maximumPools");
		this.blockValues = (BlockValueCollection) map.get("blockValues");
	}
	
	public static BlockValueMapping deserialize(Map<String, Object> map) {
		return new BlockValueMapping(map);
	}
	
	public static BlockValueMapping valueOf(Map<String, Object> map) {
		return new BlockValueMapping(map);
	}
}
