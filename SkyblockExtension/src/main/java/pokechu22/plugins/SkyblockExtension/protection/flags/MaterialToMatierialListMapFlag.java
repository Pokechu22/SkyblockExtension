package pokechu22.plugins.SkyblockExtension.protection.flags;

import static pokechu22.plugins.SkyblockExtension.util.TabCompleteUtil.TabLimit;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	
	protected EnumMap<Material, EnumSet<Material>> valueAllowed;
	protected EnumMap<Material, EnumSet<Material>> valueBanned;
	
	/**
	 * Deserialization.
	 */
	public MaterialToMatierialListMapFlag(String serialized)
			throws IllegalArgumentException {
		valueAllowed = new EnumMap<Material, EnumSet<Material>>(Material.class);
		valueBanned = new EnumMap<Material, EnumSet<Material>>(Material.class);
		
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
		return "§cNot Yet Implemented";
	}

	@Override
	public String getDisplayValue() {
		return "§cNot Yet Implemented";
	}

	@Override
	public String setValue(String value) {
		//TODO
		return "§cNot Yet Implemented";
		//return "§aFlag set successfully.";
	}
    
    @Override
	public List<String> getActions() {
		return null;
	}
    
    @Override
    public String preformAction(String action, String[] args) {
    	return "";
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
		Object value = null; //TODO
		
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@SuppressWarnings("unused")
	@Override
	public boolean equals(Object obj) {
		Object value = null; //TODO
		
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
			if (null/*other.value*/ != null) {
				return false;
			}
		} else if (!value.equals(null/*other.value*/)) {
			return false;
		}
		return true;
	}

	@Override
	public Object getValue() {
		return null;
	}
	
	/**
	 * Converts the map value to a formated string.
	 */
	private String valueToString(EnumMap<Material, EnumSet<Material>> value) {
		StringBuilder returned = new StringBuilder();
		returned.append("[");
		
		for (Map.Entry<Material, EnumSet<Material>> entry : value.entrySet()) {
			returned.append(entry.getKey().name());
			returned.append("->");
			
			if (entry.getValue() == null) {
				returned.append("*");
			} else {
				returned.append("{");
				
				Iterator<Material> i = entry.getValue().iterator();
				
				while (i.hasNext()) {
					Material m = i.next();
					returned.append(m.name());
					if (i.hasNext()) {
						returned.append(". ");
					}
				}
				
				returned.append("}");
			}
			
			returned.append(", ");
		}
		
		returned.append("]");
		return returned.toString();
	}
}
