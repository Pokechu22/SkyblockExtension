package pokechu22.plugins.SkyblockExtension.protection.flags;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import pokechu22.plugins.SkyblockExtension.ErrorHandler;
import pokechu22.plugins.SkyblockExtension.SkyblockExtension;
import pokechu22.plugins.SkyblockExtension.ThrowableReport;
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
	 */
	public static final Map<String, FlagType> flagTypes;
	
	/**
	 * Sets up flagTypes.
	 */
	static {
		HashMap<String, FlagType> flagTypesTemp = new HashMap<String, FlagType>();
		flagTypesTemp.put("canBuildAllBlocks", FlagType.BOOLEAN);
		flagTypesTemp.put("buildAllowedBlocks", FlagType.MATERIALLIST);
		flagTypesTemp.put("buildBannedBlocks", FlagType.MATERIALLIST);
		flagTypesTemp.put("canBreakAllBlocks", FlagType.BOOLEAN);
		flagTypesTemp.put("breakAllowedBlocks", FlagType.MATERIALLIST);
		flagTypesTemp.put("breakBannedBlocks", FlagType.MATERIALLIST);
		flagTypesTemp.put("canUseAllItems", FlagType.BOOLEAN);
		flagTypesTemp.put("useAllowedBlocks", FlagType.MATERIALLIST);
		flagTypesTemp.put("useBannedBlocks", FlagType.MATERIALLIST);
		flagTypesTemp.put("canAttackAllEntities", FlagType.BOOLEAN);
		flagTypesTemp.put("canAttackAnimals", FlagType.BOOLEAN);
		flagTypesTemp.put("canAttackHostile", FlagType.BOOLEAN);
		flagTypesTemp.put("attackAllowedEntities", FlagType.ENTITYLIST);
		flagTypesTemp.put("attackBannedEntities", FlagType.ENTITYLIST);
		flagTypesTemp.put("canEat", FlagType.BOOLEAN);
		flagTypesTemp.put("canBreakAllHanging", FlagType.BOOLEAN);
		flagTypesTemp.put("breakAllowedHangings", FlagType.HANGINGLIST);
		flagTypesTemp.put("breakBannedHangings", FlagType.HANGINGLIST);
		flagTypesTemp.put("canUseBeds", FlagType.BOOLEAN);
		flagTypesTemp.put("canPVP", FlagType.BOOLEAN);
		flagTypesTemp.put("canFillBuckets", FlagType.BOOLEAN);
		flagTypesTemp.put("canEmptyBuckets", FlagType.BOOLEAN);
		flagTypesTemp.put("canShearSheep", FlagType.BOOLEAN);
		flagTypesTemp.put("canUseAllEntities", FlagType.BOOLEAN);
		flagTypesTemp.put("useAllowedEntities", FlagType.ENTITYLIST);
		flagTypesTemp.put("useBannedEntities", FlagType.ENTITYLIST);
		flagTypesTemp.put("canDamageAllVehicles", FlagType.BOOLEAN);
		flagTypesTemp.put("damageAllowedVehicles", FlagType.VEHICLELIST);
		flagTypesTemp.put("damageBannedVehicles", FlagType.VEHICLELIST);
		flagTypesTemp.put("canEnterAllVehicles", FlagType.BOOLEAN);
		flagTypesTemp.put("enterAllowedVehicles", FlagType.VEHICLELIST);
		flagTypesTemp.put("enterBannedVehicles", FlagType.VEHICLELIST);
		
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
	
	/**
	 * Sets the value of the flag, fit for being sent to a player.
	 * 
	 * @param value The new value.
	 * @returns A message relating to success or failure.  
	 * 			If you want to know if there was success, check the second 
	 * 			char.  If it is "c", it is failure.  If it is "a", it is 
	 * 			success.
	 */
	public abstract String setValue(String value);
	
	/**
	 * Does this type support adding to the flag?
	 */
	public abstract boolean canAddToValue();
	
	/**
	 * Adds to the value.
	 * 
	 * @param addition The thing to add.
	 * @returns A message relating to success or failure.  
	 * 			If you want to know if there was success, check the second 
	 * 			char.  If it is "c", it is failure.  If it is "a", it is 
	 * 			success.
	 */
	public abstract String addToValue(String addition);
	
	/**
	 * Deserializes.  Handles making sure that the type is right, by using 
	 * reflection.
	 * 
	 * @param flag
	 * @param serialized
	 * @return
	 */
	public static IslandProtectionDataSetFlag deserialize(String flag, 
			String serialized) {
		try {
			return flagTypes.get(flag).clazz.getConstructor(String.class)
				.newInstance(serialized);
		} catch (Exception e) {
			ErrorHandler.logError(new ThrowableReport(e, 
					"Failed to deserialize IslandProtectionDataSetFlag " + 
							flag + "."));
			SkyblockExtension.inst().getLogger().severe(
					"Failed to deserialize IslandProtectionDataSetFlag " + 
							flag + ".");
		}
		return null;
	}
}
