package pokechu22.plugins.SkyblockExtension.protection.flags;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pokechu22.plugins.SkyblockExtension.ConfigurationErrorReport;
import pokechu22.plugins.SkyblockExtension.ErrorHandler;
import pokechu22.plugins.SkyblockExtension.SkyblockExtension;
import pokechu22.plugins.SkyblockExtension.ThrowableReport;

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
		 * Flag that requires a boolean value of either true or false.
		 */
		BOOLEAN(BooleanFlag.class, false),
		/**
		 * Represents list of materials: Both blocks and items.
		 */
		MATERIALLIST(MaterialListFlag.class, true),
		/**
		 * Represents a list of all entities.
		 */
		ENTITYLIST(EntityListFlag.class, true),
		/**
		 * Represents a list of all hangings.
		 */
		HANGINGLIST(HangingListFlag.class, true),
		/**
		 * Represents a list of all vehicles.
		 */
		VEHICLELIST(VehicleListFlag.class, true);
		
		private final Class<? extends IslandProtectionDataSetFlag> clazz;
		private boolean canBeAddedTo;
		
		private FlagType(Class<? extends IslandProtectionDataSetFlag> clazz,
				boolean canBeAddedTo) {
			this.clazz = clazz;
			this.canBeAddedTo = canBeAddedTo;
		}
		
		public Class<? extends IslandProtectionDataSetFlag> getFlagClass() {
			return clazz;
		}
		
		public boolean canBeAddedTo() {
			return this.canBeAddedTo;
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
	public abstract FlagType getType();
	
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
	 * @param force Force merging.  (If not present, it is an error to add
	 *        something already present.  Otherwise, it is allowed, but a 
	 *        warning)
	 * @returns A message relating to success or failure.  
	 * 			If you want to know if there was success, check the second 
	 * 			char.  If it is "c", it is failure.  If it is "a", it is 
	 * 			success.
	 */
	public abstract String addToValue(String addition, boolean force);
	
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
		} catch (IllegalArgumentException e) {
			ErrorHandler.logError(new ConfigurationErrorReport(e, 
					flagTypes.get(flag).clazz.getName(), false).setContext(
							"Failed to deserialize " + 
									"IslandProtectionDataSetFlag " + flag + 
									" of type " +  
									flagTypes.get(flag).clazz.getName() 
									+ " using " + serialized + "."));
			SkyblockExtension.inst().getLogger().severe(
					"Failed to deserialize IslandProtectionDataSetFlag " + 
							flag + " of type " + 
							flagTypes.get(flag).clazz.getName() + 
							" using " + serialized + ".");
		} catch (Exception e) {
			ErrorHandler.logError(new ThrowableReport(e, 
					"Failed to deserialize IslandProtectionDataSetFlag " + 
							flag + " of type " + 
							flagTypes.get(flag).clazz.getName() + 
							" using " + serialized + "."));
			SkyblockExtension.inst().getLogger().severe(
					"Failed to deserialize IslandProtectionDataSetFlag " + 
							flag + " of type " + 
							flagTypes.get(flag).clazz.getName() + 
							" using " + serialized + ".");
		}
		return null;
	}
}
