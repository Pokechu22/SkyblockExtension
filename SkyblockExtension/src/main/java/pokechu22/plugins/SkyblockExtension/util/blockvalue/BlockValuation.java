package pokechu22.plugins.SkyblockExtension.util.blockvalue;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("SBEBlockValuation")
/**
 * Represents the value of a block and its data values.
 * @author Pokechu22
 *
 */
public class BlockValuation implements ConfigurationSerializable {
	/**
	 * Simple class that contains the values of a single block / subblock.
	 * 
	 * Intentionally does NOT implement {@link ConfigurationSerializable}, 
	 * as it is used as a subvalue of the class in which it is nested.
	 * 
	 * @author Pokechu22
	 *
	 */
	public static class BlockValueData {
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
		
		/**
		 * Writes to a map, intended for serialization.
		 * 
		 * @return
		 */
		private Map<String, Object> writeToYML() {
			Map<String, Object> map = new HashMap<>();
			
			map.put("value", this.value);
			map.put("postPoolValue", this.postPoolValue);
			map.put("poolChange", this.poolChange);
			map.put("maximumPool", this.maximumPool);
			
			return map;
		}
		
		/**
		 * Reads from a map, intended for deserialization.
		 * @param map
		 */
		private void readFromYML(Map<String, Object> map) {
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
	}
	
	public BlockValuation() {
		
	}
	
	public BlockValueData defaultData = new BlockValueData();
	
	public Map<Short, BlockValuation> dataValues = new HashMap<>(); //TODO
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.putAll(defaultData.writeToYML());
		return map;
	}

	public BlockValuation(Map<String, Object> serialized) {
		this.defaultData = new BlockValueData();
		defaultData.readFromYML(serialized);
	}
	
	public static BlockValuation deserialize(Map<String, Object> s) {
		return new BlockValuation(s);
	}
	
	public static BlockValuation valueOf(Map<String, Object> s) {
		return new BlockValuation(s);
	}
}
