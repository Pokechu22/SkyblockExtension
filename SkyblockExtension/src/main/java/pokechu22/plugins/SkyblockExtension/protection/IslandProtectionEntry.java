package pokechu22.plugins.SkyblockExtension.protection;

import java.util.Arrays;

import org.bukkit.Material;

import com.avaje.ebean.enhance.asm.Type;

/**
 * A single island protection property.
 * 
 * @author Pokechu22
 *
 */
public class IslandProtectionEntry {
	/**
	 * Different types of entries.
	 * 
	 * @author Pokechu22
	 *
	 */
	public enum EntryType {
		/**
		 * Entry represents a boolean value.
		 */
		BOOLEAN,
		/**
		 * Entry represents an array of materials.
		 */
		MATERIAL_ARRAY
	}
	
	/**
	 * The name of the entry.
	 */
	public String name;
	
	/**
	 * The type of the entry. 
	 */
	public EntryType type;
	
	/**
	 * The boolean data, if this represents a boolean.
	 */
	Boolean boolean_value = null;
	
	/**
	 * The material data, if this represents an array of materials. 
	 */
	Material[] material_data = null;
	
	/**
	 * Gets the name of the entry.  
	 */
	@Override
	public String toString() {
		return getName();
	}
	
	/**
	 * Gets the name of this entry.
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the value of the entry.
	 * @return
	 */
	public String getValue() {
		switch(type) {
		case BOOLEAN:
			return boolean_value.toString();
		case MATERIAL_ARRAY:
			return Arrays.toString(material_data);
		default:
			throw new RuntimeException("Type is not valid!  It is " + type.toString());
		}
	}
	
	/**
	 * 
	 */
	public IslandProtectionEntry(String name, Boolean value) {
		this.type = EntryType.BOOLEAN;
		
		this.name = name;
		this.boolean_value = value;
	}
	
	public IslandProtectionEntry(String name, Material[] value) {
		this.type = EntryType.MATERIAL_ARRAY;
		
		this.name = name;
		this.material_data = value;
	}
}
