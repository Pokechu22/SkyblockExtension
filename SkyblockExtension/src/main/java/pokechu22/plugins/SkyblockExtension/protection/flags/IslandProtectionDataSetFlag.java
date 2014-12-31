package pokechu22.plugins.SkyblockExtension.protection.flags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bukkit.configuration.InvalidConfigurationException;

import pokechu22.plugins.SkyblockExtension.SkyblockExtension;
import pokechu22.plugins.SkyblockExtension.errorhandling.ConfigurationErrorReport;
import pokechu22.plugins.SkyblockExtension.errorhandling.ErrorHandler;
import pokechu22.plugins.SkyblockExtension.errorhandling.ThrowableReport;
import pokechu22.plugins.SkyblockExtension.util.nbt.StringTag;
import pokechu22.plugins.SkyblockExtension.util.nbt.Tag;

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
	public static enum FlagType {
		/**
		 * Flag that requires a boolean value of either true or false.
		 */
		BOOLEAN(BooleanFlag.class, new String[]{"get", "set"}),
		/**
		 * Represents list of materials: Both blocks and items.
		 */
		MATERIALLIST(MaterialListFlag.class, new String[]{"get", "set", "add", "add-f"}),
		/**
		 * Represents a list of all entities.
		 */
		ENTITYLIST(EntityListFlag.class, new String[]{"get", "set", "add", "add-f"}),
		/**
		 * Represents a list of all hangings.
		 */
		HANGINGLIST(HangingListFlag.class, new String[]{"get", "set", "add", "add-f"}),
		/**
		 * Represents a list of all vehicles.
		 */
		VEHICLELIST(VehicleListFlag.class, new String[]{"get", "set", "add", "add-f"});
		
		private final Class<? extends IslandProtectionDataSetFlag> clazz;
		private String[] allowedActions;
		
		private static HashMap<String, EnumSet<FlagType>> byAction = new HashMap<>();
		
		static {
			for (FlagType f : values()) {
				for (String action : f.allowedActions) {
					EnumSet<FlagType> val = byAction.get(action.toLowerCase(Locale.ENGLISH));
					if (val == null) {
						val = EnumSet.noneOf(FlagType.class);
					}
					val.add(f);
					byAction.put(action.toLowerCase(Locale.ENGLISH), val);
				}
			}
		}
		
		private FlagType(Class<? extends IslandProtectionDataSetFlag> clazz,
				String[] allowedActions) {
			this.clazz = clazz;
			this.allowedActions = allowedActions;
		}
		
		public Class<? extends IslandProtectionDataSetFlag> getFlagClass() {
			return clazz;
		}
		
		public boolean canPreformAction(String action) {
			for (String allowed : allowedActions) {
				if (allowed.equalsIgnoreCase(action)) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * Gets the actions that can be performed.
		 * @return
		 */
		public String[] getAllowedActions() {
			return allowedActions;
		}
		
		/**
		 * Gets a set of FlagTypes capable of performing said action.
		 * 
		 * @param action The action to search for.
		 * @return
		 */
		public static EnumSet<FlagType> preformingAction(String action) {
			return byAction.get(action.toLowerCase(Locale.ENGLISH));
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
	public abstract String getDisplayValue();
	
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
	 * Gets the list of possible actions.
	 * 
	 * @return
	 */
	public abstract List<String> getActions();
	
	/**
	 * Performs one of the available actions.
	 * 
	 * @param action The action to perform.
	 * @param args The arguments to give to the action.
	 * @returns A message relating to success or failure.  
	 * 			If you want to know if there was success, check the second 
	 * 			char.  If it is "c", it is failure.  If it is "a", it is 
	 * 			success.  (Interpret anything other than a as a fail)
	 */
	public abstract String preformAction(String action, String[] args);
	
	/**
	 * Gets the raw value of the flag.
	 * Implementations are encouraged to specify this further. 
	 */
	public abstract Object getValue();
	
	/**
	 * For tab-completion.  
	 *
	 * @param action The action, eg set or add.
	 * @param partialValue The value.  May contain spaces.
	 * @return
	 */
	public List<String> tabComplete(String action, String[] partialValues) {
		//Empty list.
		return new ArrayList<String>();
	}
	
	/**
	 * Deserializes from NBT.  Handles making sure that the type is right,  
	 * by using reflection.
	 * 
	 * @param flag The name of the flag to deserialize.  (Used to determine
	 *             the type of the flag)
	 * @param serialized The NBT tag to deserialize from.  The type of tag
	 *                   can vary; implementations will take multiple.
	 * @return
	 */
	public static IslandProtectionDataSetFlag deserialize(String flag, 
			Tag serialized) {
		try {
			IslandProtectionDataSetFlag f;
			f = flagTypes.get(flag).clazz.getConstructor()
				.newInstance();
			f.deserializeFromNBT(serialized);
			return f;
		} catch (IllegalArgumentException e) {
			try {
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
			} catch (Exception f) {
				throw new RuntimeException(e); //If an error occurred in reporting
			}
		} catch (Exception e) {
			try {
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
			} catch (Exception f) {
				throw new RuntimeException(e); //If an error occurred in reporting
			}
		}
		return null;
	}
	
	/**
	 * Deserializes.  Handles making sure that the type is right, by using 
	 * reflection.
	 * 
	 * @param flag The name of the flag to deserialize.  (Used to determine
	 *             the type of the flag)
	 * @param serialized The string-serialized form of the flag.
	 * @return
	 */
	public static IslandProtectionDataSetFlag deserialize(String flag, 
			String serialized) {
		try {
			return flagTypes.get(flag).clazz.getConstructor(String.class)
				.newInstance(serialized);
		} catch (IllegalArgumentException e) {
			try {
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
			} catch (Exception f) {
				throw new RuntimeException(e); //If an error occurred in reporting
			}
		} catch (Exception e) {
			try {
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
			} catch (Exception f) {
				throw new RuntimeException(e); //If an error occurred in reporting
			}
		}
		return null;
	}
	
	/**
	 * Serializes this flag to an NBT value.
	 * @param name The name of the Tag to serialize into.
	 * @return
	 */
	public Tag serializeToNBT(String name) {
		return new StringTag(name, this.getSerializedValue());
	}
	
	/**
	 * Deserializes the flag from an NBT value.
	 * @param value The tag to deserialize from.
	 * @throws InvalidConfigurationException 
	 */
	public void deserializeFromNBT(Tag value) throws InvalidConfigurationException {
		if (value == null) {
			throw new InvalidConfigurationException("Expected StringTag, got " + 
					"null value");
		}
		if (!(value instanceof StringTag)) {
			throw new InvalidConfigurationException("Expected StringTag, got " + 
					value.getClass().getName() + ".  (Value: " +
					value.toString() + ")");
		}
		StringTag tag = (StringTag) value;
		this.setValue(tag.data);
	}
	
	/**
	 * Returns a string representation of this flag.  
	 * 
	 * By default, this is equal to {@link #getSerializedValue()}.
	 *
	 * @return The string representation.
	 */
	@Override
	public String toString() {
		return this.getSerializedValue();
	}
}
