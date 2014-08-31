package pokechu22.plugins.SkyblockExtension.protection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import pokechu22.plugins.SkyblockExtension.ErrorHandler;
import pokechu22.plugins.SkyblockExtension.ThrowableReport;
import pokechu22.plugins.SkyblockExtension.protection.flags.*;

/**
 * <b>Serializable</b> protection configuration data, for a single entry.
 * 
 * @author Pokechu22
 *
 */
@SerializableAs("IslandProtectionDataSet")
public class IslandProtectionDataSet implements ConfigurationSerializable {
	/**
	 * List of all flags.  A copy of 
	 * {@link IslandProtectionDataSetFlag#flagTypes}.
	 */
	public static final List<String> flags = Collections.unmodifiableList(
			new ArrayList<String>(
					IslandProtectionDataSetFlag.flagTypes.keySet()));
	
	/*
	 * A note about the way all of these work, by way of examples: <br>
	 * (Foo is a verb, eg "Attack", and bar is a noun, eg "Mobs")
	 * 
	 * <ul><li>
	 * If <code>canFooAllBars</code> is set to <i>true</i>, then any Bar not
	 * specified in <code>fooBannedBars</code> can be foo'd.  
	 * <code>fooAllowedBars</code> is ignored. 
	 * </li><li>
	 * If <code>canFooAllBars</code> is set to <i>false</i>, then only Bars
	 * specified in <code>fooAllowedBars</code> can be placed.  If a Bar is
	 * specified in both <code>fooAllowedBars</code> and 
	 * <code>fooBannedBars</code>, the Bar cannot be foo'd.
	 * </li></ul>
	 * 
	 */
	
	/**
	 * Can one build all blocks?
	 */
	public BooleanFlag canBuildAllBlocks;
	/**
	 * Blocks that are allowed to be placed.
	 */
	public MaterialListFlag buildAllowedBlocks;
	/**
	 * Blocks that cannot be placed.
	 */
	public MaterialListFlag buildBannedBlocks;
	
	/**
	 * Can one break all blocks?
	 */
	public BooleanFlag canBreakAllBlocks;
	/**
	 * Blocks that are allowed to be broken.
	 */
	public MaterialListFlag breakAllowedBlocks;
	/**
	 * Blocks that cannot be broken.
	 */
	public MaterialListFlag breakBannedBlocks;
	
	/**
	 * Can one use all items?
	 */
	public BooleanFlag canUseAllItems;
	/**
	 * Items that are allowed to be used.
	 */
	public MaterialListFlag useAllowedBlocks;
	/**
	 * Items that cannot be used.
	 */
	public MaterialListFlag useBannedBlocks;
	
	/**
	 * Can one attack all entities?
	 * 
	 * NOTE: When using {@link #canAttackAnimals} and 
	 * {@link #canAttackHostile}, this counts as both of them being true,
	 * along with handling all other entities.  Otherwise, the normal rules
	 * are applied.
	 */
	public BooleanFlag canAttackAllEntities;
	/**
	 * Can one attack all passive mobs?
	 */
	public BooleanFlag canAttackAnimals;
	/**
	 * Can one attack all hostile mobs?
	 */
	public BooleanFlag canAttackHostile;
	/**
	 * Entities that are allowed to be attacked.
	 */
	public EntityListFlag attackAllowedEntities;
	/**
	 * Mobs that cannot be attacked.
	 */
	public EntityListFlag attackBannedEntities;
	
	/**
	 * Can one eat?
	 */
	public BooleanFlag canEat;
	
	/**
	 * Can one break all hanging entities?
	 */
	public BooleanFlag canBreakAllHanging;
	/**
	 * Hanging entities that are allowed to be broken.
	 */
	public HangingListFlag breakAllowedHangings;
	/**
	 * Hanging entities that cannot be broken.
	 */
	public HangingListFlag breakBannedHangings;
	
	/**
	 * Can one use beds?
	 */
	public BooleanFlag canUseBeds;
	
	/**
	 * Can one PVP?
	 */
	public BooleanFlag canPVP;
	
	/**
	 * Can one fill buckets?
	 */
	public BooleanFlag canFillBuckets;
	/**
	 * Can one empty buckets?
	 */
	public BooleanFlag canEmptyBuckets;
	
	/**
	 * Can one shear sheep?
	 */
	public BooleanFlag canShearSheep;
	
	/**
	 * Can one use (right-click) all entities?
	 */
	public BooleanFlag canUseAllEntities;
	/**
	 * Entities of which use is allowed.
	 */
	public EntityListFlag useAllowedEntities;
	/**
	 * Entities of which use is banned.
	 */
	public EntityListFlag useBannedEntities;
	
	/**
	 * Can one damage all vehicles?
	 */
	public BooleanFlag canDamageAllVehicles;
	/**
	 * Vehicles that are allowed to be damaged.
	 */
	public VehicleListFlag damageAllowedVehicles;
	/**
	 * Vehicles that cannot be damaged.
	 */
	public VehicleListFlag damageBannedVehicles;
	
	/**
	 * Can one enter all vehicles?
	 */
	public BooleanFlag canEnterAllVehicles;
	/**
	 * Vehicles that are allowed to be entered.
	 */
	public VehicleListFlag enterAllowedVehicles;
	/**
	 * Vehicles that cannot be entered.
	 */
	public VehicleListFlag enterBannedVehicles;
	
	/**
	 * Default constructor.
	 */
	public IslandProtectionDataSet() {
		//TODO
	}
	
	/**
	 * Gets the raw flag object.
	 * 
	 * @param Flag
	 * @return
	 */
	public IslandProtectionDataSetFlag getFlag(String flag) {
		try {
			return ((IslandProtectionDataSetFlag)(this.getClass()
					.getField(flag).get(this)));
		} catch (IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			ErrorHandler.logError(new ThrowableReport(e, "Exception while getting flag "
					+ flag + " of IslandProtectionDataSet."));
			return null;
		}
	}
	
	/**
	 * Gets the raw flag object.
	 * 
	 * @param flag
	 * @param value
	 * @return
	 */
	public boolean setFlag(String flag, IslandProtectionDataSetFlag value) {
		try {
			this.getClass().getField(flag).set(this, value);
			
			return true;
		} catch (IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			ErrorHandler.logError(new ThrowableReport(e, "Exception while setting flag "
					+ flag + " of IslandProtectionDataSet."));
			return false;
		}
	}
	
	/**
	 * Gets a flag (as a String!).
	 * @return The flag, or an error message.  
	 */
	public String getFlagValue(String flag) {
		try {
			return ((IslandProtectionDataSetFlag)(this.getClass()
					.getField(flag).get(this))).getDispayValue();
		} catch (IllegalArgumentException e) {
			ErrorHandler.logError(new ThrowableReport(e, 
					"Failed to use reflection to get field.  Flag: " + 
							flag + "."));
			return "§cAn error occured: " + e.toString();
		} catch (IllegalAccessException e) {
			ErrorHandler.logError(new ThrowableReport(e, 
					"Failed to use reflection to get field.  Flag: " + 
							flag + "."));
			return "§cAn error occured: " + e.toString();
		} catch (NoSuchFieldException e) {
			ErrorHandler.logError(new ThrowableReport(e, 
					"Failed to use reflection to find relevant field.  " + 
							"Flag: " + flag + ".  " + 
							"This probably means that flags is incorect."));
			return "§cAn error occured: " + e.toString();
		} catch (SecurityException e) {
			ErrorHandler.logError(new ThrowableReport(e, 
					"Failed to use reflection to get field.  Flag: " + 
							flag + "."));
			return "§cAn error occured: " + e.toString();
		} catch (NullPointerException e) {
			ErrorHandler.logError(new ThrowableReport(e, 
					"Flag is null.  Flag: " + 
							flag + "."));
			return "§cAn error occured - Flag is null?: " + e.toString();
		}
	}
	
	/**
	 * Adds additional data to an existing flag.
	 * @param flag
	 * @param addition
	 * @param force
	 * 			Whether to force it - If false, returns an error when trying
	 * 			to add a value that exists in the flag already.  Otherwise,
	 * 			it gives a warning, but still merges. 
	 * @returns A message relating to success or failure.  
	 * 			If you want to know if there was success, check the second 
	 * 			char.  If it is "c", it is failure.  If it is "a", it is 
	 * 			success.
	 */
	public String addToFlagValue(String flag, String addition, 
			boolean force) {
		try {
			return ((IslandProtectionDataSetFlag)(this.getClass()
					.getField(flag).get(this))).addToValue(addition, force);
		} catch (IllegalArgumentException e) {
			ErrorHandler.logError(new ThrowableReport(e, 
					"Failed to use reflection to add to field.  Flag: " + 
							flag + "; Addition: " + addition + "."));
			return "§cAn error occured: " + e.toString();
		} catch (IllegalAccessException e) {
			ErrorHandler.logError(new ThrowableReport(e, 
					"Failed to use reflection to add ti field.  Flag: " + 
							flag + "; Addition: " + addition + "."));
			return "§cAn error occured: " + e.toString();
		} catch (NoSuchFieldException e) {
			ErrorHandler.logError(new ThrowableReport(e, 
					"Failed to use reflection to find relevant field.  " + 
							"Flag: " + flag + "; Addition: " + addition +  
					".  This probably means that flags is incorect."));
			return "§cAn error occured: " + e.toString();
		} catch (SecurityException e) {
			ErrorHandler.logError(new ThrowableReport(e, 
					"Failed to use reflection to add to field.  Flag: " + 
							flag + "; Addition: " + addition + "."));
			return "§cAn error occured: " + e.toString();
		} catch (NullPointerException e) {
			ErrorHandler.logError(new ThrowableReport(e, 
					"Failed to use reflection to add to field.  Flag: " + 
							flag + "; Addition: " + addition + "."));
			return "§cAn error occured: " + e.toString();
		}
	}
	
	/**
	 * Sets a flag on this.  
	 * Note: Uses reflection.
	 * 
	 * @param flag
	 * @param value
	 * 
	 * @returns A message relating to success or failure.  
	 * 			If you want to know if there was success, check the second 
	 * 			char.  If it is "c", it is failure.  If it is "a", it is 
	 * 			success.
	 */
	public String setFlagValue(String flag, String value) {
		try {
			return ((IslandProtectionDataSetFlag)(this.getClass()
					.getField(flag).get(this))).setValue(value);
		} catch (IllegalArgumentException e) {
			ErrorHandler.logError(new ThrowableReport(e, 
					"Failed to use reflection to set field.  Flag: " + 
							flag + "; Value: " + value + "."));
			return "§cAn error occured: " + e.toString();
		} catch (IllegalAccessException e) {
			ErrorHandler.logError(new ThrowableReport(e, 
					"Failed to use reflection to set field.  Flag: " + 
							flag + "; Value: " + value + "."));
			return "§cAn error occured: " + e.toString();
		} catch (NoSuchFieldException e) {
			ErrorHandler.logError(new ThrowableReport(e, 
					"Failed to use reflection to find relevant field.  " + 
							"Flag: " + flag + "; Value: " + value + ".  " + 
							"This probably means that flags is incorect."));
			return "§cAn error occured: " + e.toString();
		} catch (SecurityException e) {
			ErrorHandler.logError(new ThrowableReport(e, 
					"Failed to use reflection to set field.  Flag: " + 
							flag + "; Value: " + value + "."));
			return "§cAn error occured: " + e.toString();
		} catch (NullPointerException e) {
			ErrorHandler.logError(new ThrowableReport(e, 
					"Failed to use reflection to set field.  Flag: " + 
							flag + "; Value: " + value + "."));
			return "§cAn error occured: " + e.toString();
		}
	}
	
	/**
	 * Tab completion.
	 * 
	 * @param flagName
	 * @param action
	 * @param partialValues
	 * @return
	 */
	public List<String> tabComplete(String flagName, String action, String[] partialValues) {
		IslandProtectionDataSetFlag flag = this.getFlag(flagName);
		
		if (flag != null) {
			return flag.tabComplete(action, partialValues);
		} else {
			return new ArrayList<String>();
		}
	}
	
	/**
	 * Gets the actual values 
	 * @return
	 */
	public String getAllValuesForChat() {
		return getAllValues("", "§1", "§7: §b", "", "\n", "");
	}
	
	/**
	 * Gets all values of this IslandProtectionDataSet.
	 * <br>
	 * <h1>Examples:</h1>
	 * <i>(With only a 2 flags - both booleans, Foo and Bar)
	 * <dt><code>getAllValues("Example - ", "{", ": ", "}", ", ", ".")</code></dt> 
	 * <dd><samp>Example - {Foo: false}, {Bar: true}.</samp></dd>
	 * <dt><code>getAllValues("", "§1", "§7: §b", "", "\n", "")</code></dt>
	 * <dd><samp>§1Foo§7: §bfalse\n§1Bar§7: §bfalse</samp><br>
	 * <i>In-game style:</i><br>
	 * <samp><font color=#0000AA>Foo</font><font color=#AAAAAA>: </font><font color=#55FFFF>false</font><br>
	 * <font color=#0000AA>Bar</font><font color=#AAAAAA>: </font><font color=#55FFFF>false</font></samp></dd>
	 * <dt><code>getAllValues("", "", "", "", "", "")</code></dt>
	 * <dd><samp>FoofalseBartrue</samp></dd>
	 *
	 * @param start Text put at the very start.
	 * @param seperatorStart Text put at the start of each individual flag.
	 * @param middleSeperator Text put between the flag name and it's value.
	 * @param seperatorEnd Text put at the end of each individual flag.
	 * @param joiner Used to join any value EXCEPT for the last.
	 * @param end Text put at the very end.
	 * 
	 * @return
	 */
	public String getAllValues(String start, String seperatorStart, 
			String middleSeperator, String seperatorEnd, 
			String joiner, String end) {
		StringBuilder returned = new StringBuilder();
		
		returned.append(start);
		
		Iterator<String> iterator = flags.iterator();
		
		while (iterator.hasNext()) {
			String flag = iterator.next();
			returned.append(seperatorStart);
			returned.append(flag);
			returned.append(middleSeperator);
			returned.append(getFlag(flag));
			returned.append(seperatorEnd);
			
			//In all cases other than 
			if (iterator.hasNext()) {
				returned.append(joiner);
			}
		}
		
		returned.append(end);
		
		return returned.toString();
	}
	
	public String toString() {
		return getAllValues(this.getClass().getName(), "{", ": ", "} ", "", "");
	}
	
	/**
	 * Serialization for use with a configuration file.
	 */
	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		for (String flagName : flags) {
			IslandProtectionDataSetFlag flag = this.getFlag(flagName);
			if (flag == null) {
				//TODO Add default value otherwise
				continue;
			}
			String value = flag.getSerializedValue();
			if (value != null) {
				map.put(flagName, value);
			} // else { //TODO Add default value otherwise
			
			// }
		}
		
		return map;
	}
	
	/**
	 * Constructor for deserialization of this from a configuration file.
	 * @param map
	 * @return
	 */
	public IslandProtectionDataSet(Map<String, Object> map) {
		for (String flagName : flags) {
			IslandProtectionDataSetFlag flag = 
					IslandProtectionDataSetFlag.deserialize(flagName, 
							(String) map.get(flagName));
			if (flag != null) {
				this.setFlag(flagName, flag);
			} else {
				//TODO Add the default value otherwise.
				//result = this.setFlagRaw(flag, (String) 
				// 			/*getDefaultFlagSomehow(flag)*/ "");
				//if (result == false) {
				//	//TODO ConfigurationErrorReport?
				//}
			}
		}
	}
	
	/**
	 * Deserialization of this from a configuration file.
	 * @param map
	 * @return
	 */
	public static IslandProtectionDataSet deserialize(
			Map<String, Object> map) {
		return new IslandProtectionDataSet(map);
	}

	/**
	 * Deserialization of this from a configuration file.
	 * @param map
	 * @return
	 */
	public static IslandProtectionDataSet valueOf(Map<String, Object> map) {
		return new IslandProtectionDataSet(map);
	}
}
