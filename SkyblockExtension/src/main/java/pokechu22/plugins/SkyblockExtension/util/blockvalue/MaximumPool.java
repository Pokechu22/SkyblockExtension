package pokechu22.plugins.SkyblockExtension.util.blockvalue;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;


@SerializableAs("SBEMaximumPool")
public class MaximumPool implements ConfigurationSerializable {
	
	public MaximumPool(int maximumValue) {
		this.maximumValue = maximumValue;
	}
	
	public final int maximumValue;
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> returned = new HashMap<>();
		
		returned.put("max", maximumValue);
		
		return returned;
	}
	
	public MaximumPool(Map<String, Object> map) {
		this.maximumValue = (int)map.get("max");
	}
	
	public static MaximumPool deserialize(Map<String, Object> map) {
		return new MaximumPool(map);
	}
	
	public static MaximumPool valueOf(Map<String, Object> map) {
		return new MaximumPool(map);
	}
}
