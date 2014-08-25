package pokechu22.plugins.SkyblockExtension.protection.flags;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import pokechu22.plugins.SkyblockExtension.protection.HangingType;
import pokechu22.plugins.SkyblockExtension.protection.VehicleType;

/**
 * Represents a single flag.
 * @author Pokechu22
 *
 */
public abstract class IslandProtectionDataSetFlag {
	/**
	 * Different legal types of flags.
	 * 
	 * @author Pokechu22
	 *
	 */
	static enum FlagType {
		/**
		 * Flag that requires a boolean value of either true or false.<br>
		 * Actual used type: <tt>boolean</tt>
		 */
		BOOLEAN(BooleanFlag.class),
		/**
		 * Represents list of materials: Both blocks and items.<br>
		 * Actual used type: <tt>{@link List}&lt;{@link Material}&gt;</tt>
		 */
		MATERIALLIST(MaterialListFlag.class),
		/**
		 * Represents a list of all entities.<br>
		 * Actual used type: <tt>{@link List}&lt;{@link EntityType}&gt;</tt>
		 */
		ENTITYLIST(EntityListFlag.class),
		/**
		 * Represents a list of all hangings.<br>
		 * Actual used type: <tt>{@link List}&lt;{@link HangingType}&gt;</tt>
		 */
		HANGINGLIST(HangingListFlag.class),
		/**
		 * Represents a list of all vehicles.<br>
		 * Actual used type: <tt>{@link List}&lt;{@link VehicleType}&gt;</tt>
		 */
		VEHICLELIST(VehicleListFlag.class);
		
		private final Class<? extends IslandProtectionDataSetFlag> clazz;
		
		private FlagType(Class<? extends IslandProtectionDataSetFlag> clazz) {
			this.clazz = clazz;
		}
		
		public Class<? extends IslandProtectionDataSetFlag> getFlagClass() {
			return clazz;
		}
	}
	/**
	 * Different flags available, and their types.
	 * First value is the name, second is the type.<br>
	 * Currently used types: 
	 * <dl>
	 * 
	 * 
	 * <dt><code>MaterialList</code></dt>
	 * 
	 * 
	 * <dt><code>EntityList</code></dt>
	 * 
	 * 
	 * <dt><code>HangingList</code></dt>
	 * 
	 * 
	 * <dt><code>VehicleList</code></dt>
	 * 
	 * </dl>
	 */
	public static final Map<String, String> flagTypes;
	
	/**
	 * Sets up flagTypes.
	 */
	static {
		HashMap<String, String> flagTypesTemp = new HashMap<String, String>();
		flagTypesTemp.put("canBuildAllBlocks", "Boolean");
		flagTypesTemp.put("buildAllowedBlocks", "MaterialList");
		flagTypesTemp.put("buildBannedBlocks", "MaterialList");
		flagTypesTemp.put("canBreakAllBlocks", "Boolean");
		flagTypesTemp.put("breakAllowedBlocks", "MaterialList");
		flagTypesTemp.put("breakBannedBlocks", "MaterialList");
		flagTypesTemp.put("canUseAllItems", "Boolean");
		flagTypesTemp.put("useAllowedBlocks", "MaterialList");
		flagTypesTemp.put("useBannedBlocks", "MaterialList");
		flagTypesTemp.put("canAttackAllEntities", "Boolean");
		flagTypesTemp.put("canAttackAnimals", "Boolean");
		flagTypesTemp.put("canAttackHostile", "Boolean");
		flagTypesTemp.put("attackAllowedEntities", "EntityList");
		flagTypesTemp.put("attackBannedEntities", "EntityList");
		flagTypesTemp.put("canEat", "Boolean");
		flagTypesTemp.put("canBreakAllHanging", "Boolean");
		flagTypesTemp.put("breakAllowedHangings", "HangingList");
		flagTypesTemp.put("breakBannedHangings", "HangingList");
		flagTypesTemp.put("canUseBeds", "Boolean");
		flagTypesTemp.put("canPVP", "Boolean");
		flagTypesTemp.put("canFillBuckets", "Boolean");
		flagTypesTemp.put("canEmptyBuckets", "Boolean");
		flagTypesTemp.put("canShearSheep", "Boolean");
		flagTypesTemp.put("canUseAllEntities", "Boolean");
		flagTypesTemp.put("useAllowedEntities", "EntityList");
		flagTypesTemp.put("useBannedEntities", "EntityList");
		flagTypesTemp.put("canDamageAllVehicles", "Boolean");
		flagTypesTemp.put("damageAllowedVehicles", "VehicleList");
		flagTypesTemp.put("damageBannedVehicles", "VehicleList");
		flagTypesTemp.put("canEnterAllVehicles", "Boolean");
		flagTypesTemp.put("enterAllowedVehicles", "VehicleList");
		flagTypesTemp.put("enterBannedVehicles", "VehicleList");
		
		flagTypes = Collections.unmodifiableMap(flagTypesTemp);
	};
	
	/**
	 * Gets the type of value this is.
	 */
	public abstract String getType();
	
	/**
	 * Gets the serialized value.  
	 *
	 * @return The value, or null.
	 */
	public abstract String getSerializedValue();
	
	/**
	 * Gets the value of the flag, fit for being sent to a player.
	 *
	 * @return The value, or an error message.  
	 */
	public abstract String getDispayValue();
	
	
}
