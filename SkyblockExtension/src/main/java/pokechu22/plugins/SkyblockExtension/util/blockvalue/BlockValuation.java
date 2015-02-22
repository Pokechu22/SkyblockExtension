package pokechu22.plugins.SkyblockExtension.util.blockvalue;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

/**
 * Represents the value of a block and its data values.
 * @author Pokechu22
 *
 */
@SerializableAs("SBEBlockValuation")
public class BlockValuation implements ConfigurationSerializable {
	/**
	 * Simple class that contains the values of a single block / subblock.
	 * 
	 * @author Pokechu22
	 *
	 */
	@SerializableAs("BlockValueData")
	public static class BlockValueData implements 
			ConfigurationSerializable {
		public BlockValueData() {
			
		}
		
		/**
		 * The value used (before the pool max has been reached).
		 */
		public int value = 1;
		/**
		 * The value used after reaching the pool max.
		 */
		public int postPoolValue = 0;
		/**
		 * The amount to change the pool by.
		 */
		public int poolChange = 1;
		/**
		 * The pool to modify.
		 */
		public String maximumPool = "default";
		
		public Map<String, Object> serialize() {
			Map<String, Object> map = new HashMap<>();
			
			map.put("value", this.value);
			map.put("postPoolValue", this.postPoolValue);
			map.put("poolChange", this.poolChange);
			map.put("maximumPool", this.maximumPool);
			
			return map;
		}
		
		public BlockValueData(Map<String, Object> map) {
			for (Map.Entry<String, Object> e : map.entrySet()) {
				switch (e.getKey()) {
				case "value": {
					this.value = (int)e.getValue();
					break;
				}
				case "postPoolValue": {
					this.postPoolValue = (int)e.getValue();
					break;
				}
				case "poolChange": {
					this.postPoolValue = (int)e.getValue();
					break;
				}
				case "maximumPool": {
					this.maximumPool = (String)e.getValue();
					break;
				}
				}
			}
		}
		
		public static BlockValueData deserialize(Map<String, Object> s) {
			return new BlockValueData(s);
		}
		
		public static BlockValueData valueOf(Map<String, Object> s) {
			return new BlockValueData(s);
		}
	}
	
	public BlockValuation() {
		
	}
	
	/**
	 * The {@link BlockValueData} used by default.
	 */
	public BlockValueData defaultData = new BlockValueData();
	/**
	 * Specific, overriding block value datas.
	 */
	public Map<Byte, BlockValueData> dataValues = new HashMap<>();
	
	/**
	 * Gets the {@link BlockValueData} used for the specified data value.
	 * 
	 * @param dataValue
	 * @return
	 */
	public BlockValueData getValueOrDefault(Byte dataValue) {
		if (dataValues.containsKey(dataValue)) {
			return dataValues.get(dataValue);
		} else {
			return defaultData;
		}
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put("default", defaultData);
		
		for (Map.Entry<Byte, BlockValueData> e : dataValues.entrySet()) {
			map.put(e.getKey().toString(), e.getValue());
		}
		return map;
	}

	public BlockValuation(Map<String, Object> serialized) {
		this.defaultData = (BlockValueData) serialized.get("default");
		//TODO handle null defualtData (throw exception?)
		for (Byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
			BlockValueData data = (BlockValueData) serialized.get(
					b.toString());
			if (data != null) {
				dataValues.put(b, data);
			}
		}
	}
	
	public static BlockValuation deserialize(Map<String, Object> s) {
		return new BlockValuation(s);
	}
	
	public static BlockValuation valueOf(Map<String, Object> s) {
		return new BlockValuation(s);
	}
}
