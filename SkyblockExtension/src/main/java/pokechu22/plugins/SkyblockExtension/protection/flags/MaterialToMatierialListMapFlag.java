package pokechu22.plugins.SkyblockExtension.protection.flags;

import static pokechu22.plugins.SkyblockExtension.util.TabCompleteUtil.TabLimit;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;

import pokechu22.plugins.SkyblockExtension.util.ListUtil;
import pokechu22.plugins.SkyblockExtension.util.nbt.CompoundTag;
import pokechu22.plugins.SkyblockExtension.util.nbt.IntTag;
import pokechu22.plugins.SkyblockExtension.util.nbt.ListTag;
import pokechu22.plugins.SkyblockExtension.util.nbt.StringTag;
import pokechu22.plugins.SkyblockExtension.util.nbt.Tag;

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
	 * A value corresponding to a single Material.
	 * 
	 * @author Pokechu22
	 */
	public class Value {
		public EnumSet<Material> items;
		public boolean isInverted;
		
		public Value() {
			this.items = EnumSet.noneOf(Material.class);
			this.isInverted = false;
		}
		
		/**
		 * Does this value allow use of the specific material?
		 * 
		 * @param m
		 * @return
		 */
		public boolean allows(Material m) {
			return (this.isInverted ^ items.contains(m));
		}
		
		/**
		 * Gets the value for chat.
		 * @return
		 */
		public String getDisplayValue() {
			//Red or green.
			final String header = (this.isInverted ? "§c" : "§a");
			final String contents = ListUtil.convertListToString(
					new ArrayList<Material>(this.items), "; ", "{", "}");
			return header + contents + "§r";
		}
		
		/**
		 * Gets the value for serialization.
		 */
		public String getSerializedValue() {
			final String invertedHeader = (this.isInverted ? "!" : "");
			
			final String contents = invertedHeader + ListUtil.convertListToString(
					new ArrayList<Material>(this.items), "; ", "{", "}");
			return contents;
		}
		
		/**
		 * Deserializes, or sets the value from a string.
		 */
		public String setValue(String value) {
			try {
				this.isInverted = value.startsWith("!");
				if (this.isInverted) {
					value = value.substring(1); //Remove leading "!".
				}
				
				List<Material> m = ListUtil.parseList(value, Material.class, "; ", "{", "}");
				
				this.items = EnumSet.noneOf(Material.class);
				this.items.addAll(m);
				
				return "§aValue set sucessfully";
			} catch (ParseException e) {
				return e.toString();
			}
		}
		
		/**
		 * Serializes to an NBT tag.
		 */
		@SuppressWarnings("deprecation")
		public CompoundTag serializeToNBT(String tagName) {
			CompoundTag returned = new CompoundTag(tagName);
			returned.putBoolean("isInverted", this.isInverted);
			
			ListTag<IntTag> list = new ListTag<IntTag>("items");
			for (Material m : this.items) {
				list.add(new IntTag(null, m.getId()));
			}
			
			return returned;
		}
		
		/**
		 * Sets the values of this from a tag.
		 * @param tag
		 * @throws IllegalArgumentException when given an unrecognised tag.
		 */
		@SuppressWarnings("deprecation")
		public void deserializeFromNBT(Tag tag) throws IllegalArgumentException {
			if (tag instanceof StringTag) {
				this.setValue(((StringTag) tag).data);
				
				return;
			} else if (tag instanceof CompoundTag) {
				this.isInverted = ((CompoundTag) tag).getBoolean("isInverted");
				this.items = EnumSet.noneOf(Material.class);
				
				ListTag<?> list = (ListTag<?>)(((CompoundTag) tag).getList("items"));
				
				for (int i = 0; i < list.size(); i++) {
					IntTag _int = (IntTag) list.get(i);
					
					items.add(Material.getMaterial(_int.data));
				}
				
				return;
			} else {
				throw new IllegalArgumentException("Failed to deseialize:\n" +
						"Expected StringTag or CompoundTag, got " + 
						tag.getClass().getName() + ".  Data: " + tag.toString());
			}
		}
	}
	
	protected EnumMap<Material, MaterialToMatierialListMapFlag.Value> values;
	
	/**
	 * Constructor for use with deserialization.
	 * 
	 * @deprecated Not really, but this should NOT be called normally;
	 * 				only through reflection.
	 */
	@Deprecated
	public MaterialToMatierialListMapFlag() {
		values = new EnumMap<Material, MaterialToMatierialListMapFlag.Value>(Material.class);
	}
	
	/**
	 * Deserialization.
	 */
	public MaterialToMatierialListMapFlag(String serialized)
			throws IllegalArgumentException {
		values = new EnumMap<Material, MaterialToMatierialListMapFlag.Value>(Material.class);
		
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
		//Slightly redundant but not quite.
		StringBuilder returned = new StringBuilder();
		returned.append("[");
		
		Iterator<Map.Entry<Material, MaterialToMatierialListMapFlag.Value>>
				itr = values.entrySet().iterator();
		
		while (itr.hasNext()) {
			Map.Entry<Material, MaterialToMatierialListMapFlag.Value> e
					= itr.next();
			
			returned.append(e.getKey().name());
			returned.append("->");
			returned.append(e.getValue().getSerializedValue());
			
			if (itr.hasNext()) {
				returned.append(", ");
			}
		}
		
		returned.append("]");
		return returned.toString();
	}

	@Override
	public String getDisplayValue() {
		return "§cNot Yet Implemented";
	}

	@Override
	public String setValue(String value) {
		//Ensure valid formating.
		try {
			ListUtil.validateList(value, "[", "]");
		} catch (ParseException e) {
			return e.toString();
		}
		final String subValues = value.substring(1, value.length() - 1);
		String[] individualValues = subValues.split(",");
		
		//Trim each value.
		for (int i = 0; i < individualValues.length; i++) {
			individualValues[i] = individualValues[i].trim();
		}
		
		EnumMap<Material, Value> newValues = new EnumMap<>(Material.class);
		
		for (String individual : individualValues) {
			final String materialString;
			final String dataString;
			
			final Material material;
			final Value val = new Value();
			
			//Individual block for some quick processing.
			{
				final String[] split = individual.split("->");
				//Validate that the length is propper.
				if (split.length != 2) {
					return "§cInvalid maping value - Each individual " + 
							"entry must have exactly 1 \"->\" in it.  " +
							"Error index: \n" + 
							value.replace(individual, "§4§l" + individual +
									"§r§c");
				}
				
				materialString = split[0];
				dataString = split[1];
			}
			
			material = ListUtil
					.matchEnumValue(materialString, Material.class);
			
			//If invalid material.
			if (material == null) {
				return ("§c" + "Material" + " \"" + 
						materialString + "\" is not recognised.\n(Location: " + 
						//Bolds the error.
						value.replaceAll(materialString, 
								"§4§l" + materialString + 
								"§r§c") + ")\n" + 
						"§cTry using tab completion next time.");
			}
			
			String valueResult = val.setValue(dataString);
			//Ensure valid.
			if (!valueResult.startsWith("§a")) {
				return "§cInvalid mapped value - " + valueResult + "\n" + 
						"§c(Location: " + value.replaceAll(dataString, 
								"§4§l" + dataString + "§r§c") + ")";
			}
			
			//Ok, that is good.
			newValues.put(material, val);
		}
		
		this.values = newValues;
		
		return "§aFlag set successfully.";
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
	public EnumMap<Material, MaterialToMatierialListMapFlag.Value> getValue() {
		return new EnumMap<Material, MaterialToMatierialListMapFlag.Value>(this.values);
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
	
	/**
	 * Check allowance of specific action.
	 * @param held
	 * @param on
	 * @return
	 */
	public boolean allows(Material held, Material on) { 
		if (!this.values.containsKey(held)) {
			return false;
		}
		Value v = values.get(held);
		return v.allows(on);
	}
}
