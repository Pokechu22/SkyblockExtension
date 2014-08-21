package pokechu22.plugins.SkyblockExtension.protection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.EntityType;

import pokechu22.plugins.SkyblockExtension.ErrorHandler;
import pokechu22.plugins.SkyblockExtension.ThrowableReport;

/**
 * <b>Serializable</b> protection configuration data, for a single entry.
 * 
 * @author Pokechu22
 *
 */
public class IslandProtectionDataSet implements ConfigurationSerializable {
	/**
	 * Different flags available.
	 * First value is the name, second is the type.<br>
	 * Currently used types: 
	 * <dl>
	 * <dt><code>Boolean</code></dt>
	 * <dd>Requires a boolean value of either true or false.<br>
	 * Actual type: boolean</dd>
	 * 
	 * <dt><code>MaterialList</code></dt>
	 * <dd>Represents list of materials: Both blocks and items.<br>
	 * Actual type: <tt>{@link List}&lt;{@link Material}&gt;</tt></dd>
	 * 
	 * <dt><code>EntityList</code></dt>
	 * <dd>Represents a list of all entities.<br>
	 * Actual type: <tt>{@link List}&lt;{@link EntityType}&gt;</tt></dd>
	 * 
	 * <dt><code>HangingList</code></dt>
	 * <dd>Represents a list of all hangings.<br>
	 * Actual type: <tt>{@link List}&lt;{@link HangingType}&gt;</tt></dd>
	 * 
	 * <dt><code>VehicleList</code></dt>
	 * <dd>Represents a list of all vehicles.<br>
	 * Actual type: <tt>{@link List}&lt;{@link VehicleType}&gt;</tt></dd>
	 * </dl>
	 */
	public static final Map<String, String> flags;
	
	/**
	 * Sets up flags.
	 */
	static {
		HashMap<String, String> flagsTemp = new HashMap<String, String>();
		flagsTemp.put("canBuildAllBlocks", "Boolean");
		flagsTemp.put("buildAllowedBlocks", "MaterialList");
		flagsTemp.put("buildBannedBlocks", "MaterialList");
		flagsTemp.put("canBreakAllBlocks", "Boolean");
		flagsTemp.put("breakAllowedBlocks", "MaterialList");
		flagsTemp.put("breakBannedBlocks", "MaterialList");
		flagsTemp.put("canUseAllItems", "Boolean");
		flagsTemp.put("useAllowedBlocks", "MaterialList");
		flagsTemp.put("useBannedBlocks", "MaterialList");
		flagsTemp.put("canAttackAllEntities", "Boolean");
		flagsTemp.put("canAttackAnimals", "Boolean");
		flagsTemp.put("canAttackHostile", "Boolean");
		flagsTemp.put("attackAllowedEntities", "EntityList");
		flagsTemp.put("attackBannedEntities", "EntityList");
		flagsTemp.put("canEat", "Boolean");
		flagsTemp.put("canBreakAllHanging", "Boolean");
		flagsTemp.put("breakAllowedHangings", "HangingList");
		flagsTemp.put("breakBannedHangings", "HangingList");
		flagsTemp.put("canUseBeds", "Boolean");
		flagsTemp.put("canPVP", "Boolean");
		flagsTemp.put("canFillBuckets", "Boolean");
		flagsTemp.put("canEmptyBuckets", "Boolean");
		flagsTemp.put("canShearSheep", "Boolean");
		flagsTemp.put("canUseAllEntities", "Boolean");
		flagsTemp.put("useAllowedEntities", "EntityList");
		flagsTemp.put("useBannedEntities", "EntityList");
		flagsTemp.put("canDamageAllVehicles", "Boolean");
		flagsTemp.put("damageAllowedVehicles", "VehicleList");
		flagsTemp.put("damageBannedVehicles", "VehicleList");
		flagsTemp.put("canEnterAllVehicles", "Boolean");
		flagsTemp.put("enterAllowedVehicles", "VehicleList");
		flagsTemp.put("enterBannedVehicles", "VehicleList");
		flags = Collections.unmodifiableMap(flagsTemp);
	};
	
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
	public boolean canBuildAllBlocks;
	/**
	 * Blocks that are allowed to be placed.
	 */
	public List<Material> buildAllowedBlocks;
	/**
	 * Blocks that cannot be placed.
	 */
	public List<Material> buildBannedBlocks;
	
	/**
	 * Can one break all blocks?
	 */
	public boolean canBreakAllBlocks;
	/**
	 * Blocks that are allowed to be broken.
	 */
	public List<Material> breakAllowedBlocks;
	/**
	 * Blocks that cannot be broken.
	 */
	public List<Material> breakBannedBlocks;
	
	/**
	 * Can one use all items?
	 */
	public boolean canUseAllItems;
	/**
	 * Items that are allowed to be used.
	 */
	public List<Material> useAllowedBlocks;
	/**
	 * Items that cannot be used.
	 */
	public List<Material> useBannedBlocks;
	
	/**
	 * Can one attack all entities?
	 * 
	 * NOTE: When using {@link #canAttackAnimals} and 
	 * {@link #canAttackHostile}, this counts as both of them being true,
	 * along with handling all other entities.  Otherwise, the normal rules
	 * are applied.
	 */
	public boolean canAttackAllEntities;
	/**
	 * Can one attack all passive mobs?
	 */
	public boolean canAttackAnimals;
	/**
	 * Can one attack all hostile mobs?
	 */
	public boolean canAttackHostile;
	/**
	 * Entities that are allowed to be attacked.
	 */
	public List<EntityType> attackAllowedEntities;
	/**
	 * Mobs that cannot be attacked.
	 */
	public List<EntityType> attackBannedEntities;
	
	/**
	 * Can one eat?
	 */
	public boolean canEat;
	
	/**
	 * Can one break all hanging entities?
	 */
	public boolean canBreakAllHanging;
	/**
	 * Hanging entities that are allowed to be broken.
	 */
	public List<HangingType> breakAllowedHangings;
	/**
	 * Hanging entities that cannot be broken.
	 */
	public List<HangingType> breakBannedHangings;
	
	/**
	 * Can one use beds?
	 */
	public boolean canUseBeds;
	
	/**
	 * Can one PVP?
	 */
	public boolean canPVP;
	
	/**
	 * Can one fill buckets?
	 */
	public boolean canFillBuckets;
	/**
	 * Can one empty buckets?
	 */
	public boolean canEmptyBuckets;
	
	/**
	 * Can one shear sheep?
	 */
	public boolean canShearSheep;
	
	/**
	 * Can one use (right-click) all entities?
	 */
	public boolean canUseAllEntities;
	/**
	 * Entities of which use is allowed.
	 */
	public List<EntityType> useAllowedEntities;
	/**
	 * Entities of which use is banned.
	 */
	public List<EntityType> useBannedEntities;
	
	/**
	 * Can one damage all vehicles?
	 */
	public boolean canDamageAllVehicles;
	/**
	 * Vehicles that are allowed to be damaged.
	 */
	public List<VehicleType> damageAllowedVehicles;
	/**
	 * Vehicles that cannot be damaged.
	 */
	public List<VehicleType> damageBannedVehicles;
	
	/**
	 * Can one enter all vehicles?
	 */
	public boolean canEnterAllVehicles;
	/**
	 * Vehicles that are allowed to be entered.
	 */
	public List<VehicleType> enterAllowedVehicles;
	/**
	 * Vehicles that cannot be entered.
	 */
	public List<VehicleType> enterBannedVehicles;
	
	/**
	 * Default constructor.
	 */
	public IslandProtectionDataSet() {
		
	}
	
	/**
	 * Gets a flag (as a String!).
	 * @return The flag, or an error message.  
	 */
	public String getFlag(String flag) {
		if (!flags.containsKey(flag)) {
			return "§cFlag " + flag + " does not exist.";
		}
		
		try {
			return this.getClass().getField(flag).get(this).toString();
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
	public String setFlag(String flag, String value) {
		if (!flags.containsKey(flag)) {
			return "§cFlag " + flag + " does not exist.";
		}
		try {
			switch (flags.get(flag)) {
			case "Boolean": {
				if (value.equalsIgnoreCase("true")) {
					this.getClass().getField(flag).set(this, true);
					return "§aFlag set successfully.";
				} else if (value.equalsIgnoreCase("false")) {
					this.getClass().getField(flag).set(this, true);
					return "§aFlag set successfully.";
				} else {
					return "§cValue " + value + "is not a valid boolean."; 
				}
			}
			case "MaterialList": {
				//If there is only 1 [ and 1 ], and both are at the start 
				//and end of the strings...
				if (!((value.indexOf("[") == 0) 
						&& value.indexOf("[", 1) == -1)
						&& (value.indexOf("]") != value.length() - 1)) {
					return "§cList format is invalid: It must start with " +
							"'[' and end with ']', and not have any '['" + 
							" or ']' anywhere else in it.";
				}
				String[] materialsStrings = 
						value.substring(1, value.length() - 1)
						.split(",");
				List<Material> tempList = new ArrayList<Material>();
				for (int i = 0; i < materialsStrings.length; i++) {
					Material material = 
							Material.matchMaterial(materialsStrings[i]);
					if (material == null) {
						return "§cMaterial \"" + materialsStrings[i] + 
								"\" is not recognised.\n(Location: " + 
								//Bolds the error.
								value.replaceAll(materialsStrings[i], 
										"§4§l" + materialsStrings[i] + 
										"§r§c") + ")\n" + 
								"§cTry using tab completion next time.";
					}
					
					if (tempList.contains(material)) {
						return "§cMaterial \"" + materialsStrings[i] + 
								"\" is already entered.  (Repeat entries " +
								"are not allowed).\n(Location: " + 
								//Bolds the error.
								value.replaceAll(materialsStrings[i], 
										"§4§l" + materialsStrings[i] + 
										"§r§c") + ")";
					}
					
					tempList.add(material);
					
				}
				
				//Now handle the field.
				this.getClass().getField(flag).set(this, tempList);
				
				return "§aFlag set successfully.";
			}
			case "EntityList": {
				//If there is only 1 [ and 1 ], and both are at the start 
				//and end of the strings...
				if (!((value.indexOf("[") == 0) 
						&& value.indexOf("[", 1) == -1)
						&& (value.indexOf("]") != value.length() - 1)) {
					return "§cList format is invalid: It must start with " +
							"'[' and end with ']', and not have any '['" + 
							" or ']' anywhere else in it.";
				}
				String[] entityStrings = 
						value.substring(1, value.length() - 1)
						.split(",");
				List<EntityType> tempList = new ArrayList<EntityType>();
				for (int i = 0; i < entityStrings.length; i++) {
					EntityType entity = 
							EntityType.valueOf(entityStrings[i].trim()
									.toUpperCase());
					if (entity == null) {
						return "§cEntity \"" + entityStrings[i] + 
								"\" is not recognised.\n(Location: " + 
								//Bolds the error.
								value.replaceAll(entityStrings[i], 
										"§4§l" + entityStrings[i] + 
										"§r§c") + ")\n" + 
								"§cTry using tab completion next time.";
					}
					
					if (tempList.contains(entity)) {
						return "§cEntity \"" + entityStrings[i] + 
								"\" is already entered.  (Repeat entries " +
								"are not allowed).\n(Location: " + 
								//Bolds the error.
								value.replaceAll(entityStrings[i], 
										"§4§l" + entityStrings[i] + 
										"§r§c") + ")";
					}
					
					tempList.add(entity);
					
				}
				
				//Now handle the field.
				this.getClass().getField(flag).set(this, tempList);
				
				return "§aFlag set successfully.";
			}
			case "HangingList": {
				//If there is only 1 [ and 1 ], and both are at the start 
				//and end of the strings...
				if (!((value.indexOf("[") == 0) 
						&& value.indexOf("[", 1) == -1)
						&& (value.indexOf("]") != value.length() - 1)) {
					return "§cList format is invalid: It must start with " +
							"'[' and end with ']', and not have any '['" + 
							" or ']' anywhere else in it.";
				}
				String[] hangingStrings = 
						value.substring(1, value.length() - 1)
						.split(",");
				List<HangingType> tempList = new ArrayList<HangingType>();
				for (int i = 0; i < hangingStrings.length; i++) {
					HangingType hanging = 
							HangingType.valueOf(hangingStrings[i].trim()
									.toUpperCase());
					if (hanging == null) {
						return "§cHanging \"" + hangingStrings[i] + 
								"\" is not recognised.\n(Location: " + 
								//Bolds the error.
								value.replaceAll(hangingStrings[i], 
										"§4§l" + hangingStrings[i] + 
										"§r§c") + ")\n" + 
								"§cTry using tab completion next time.";
					}
					
					if (tempList.contains(hanging)) {
						return "§cHanging \"" + hangingStrings[i] + 
								"\" is already entered.  (Repeat entries " +
								"are not allowed).\n(Location: " + 
								//Bolds the error.
								value.replaceAll(hangingStrings[i], 
										"§4§l" + hangingStrings[i] + 
										"§r§c") + ")";
					}
					
					tempList.add(hanging);
					
				}
				
				//Now handle the field.
				this.getClass().getField(flag).set(this, tempList);
				
				return "§aFlag set successfully.";
			}
			case "VehicleList": {
				//If there is only 1 [ and 1 ], and both are at the start 
				//and end of the strings...
				if (!((value.indexOf("[") == 0) 
						&& value.indexOf("[", 1) == -1)
						&& (value.indexOf("]") != value.length() - 1)) {
					return "§cList format is invalid: It must start with " +
							"'[' and end with ']', and not have any '['" + 
							" or ']' anywhere else in it.";
				}
				String[] vehicleStrings = 
						value.substring(1, value.length() - 1)
						.split(",");
				List<VehicleType> tempList = new ArrayList<VehicleType>();
				for (int i = 0; i < vehicleStrings.length; i++) {
					VehicleType vehicle = 
							VehicleType.valueOf(vehicleStrings[i].trim()
									.toUpperCase());
					if (vehicle == null) {
						return "§cVehicle \"" + vehicleStrings[i] + 
								"\" is not recognised.\n(Location: " + 
								//Bolds the error.
								value.replaceAll(vehicleStrings[i], 
										"§4§l" + vehicleStrings[i] + 
										"§r§c") + ")\n" + 
								"§cTry using tab completion next time.";
					}
					
					if (tempList.contains(vehicle)) {
						return "§cVehicle \"" + vehicleStrings[i] + 
								"\" is already entered.  (Repeat entries " +
								"are not allowed).\n(Location: " + 
								//Bolds the error.
								value.replaceAll(vehicleStrings[i], 
										"§4§l" + vehicleStrings[i] + 
										"§r§c") + ")";
					}
					
					tempList.add(vehicle);
					
				}
				
				//Now handle the field.
				this.getClass().getField(flag).set(this, tempList);
				
				return "§aFlag set successfully.";
			}
			default: {
				return "§cInternal error: Flag type " + flags.get(flag) + 
						"is not recognised.";
				
			}
			}
		//Most of the exceptions here shouldn't happen, but they might.
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
	 * Serialization for use with a configuration file.
	 */
	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("canBuildAllBlocks", canBuildAllBlocks);
		map.put("buildAllowedBlocks", buildAllowedBlocks);
		map.put("buildBannedBlocks", buildBannedBlocks);
		
		map.put("canBreakAllBlocks", canBreakAllBlocks);
		map.put("breakAllowedBlocks", breakAllowedBlocks);
		map.put("breakBannedBlocks", breakBannedBlocks);
		
		map.put("canUseAllItems", canUseAllItems);
		map.put("useAllowedBlocks", useAllowedBlocks);
		map.put("useBannedBlocks", useBannedBlocks);
		
		map.put("canAttackAllEntities", canAttackAllEntities);
		map.put("canAttackAnimals", canAttackAnimals);
		map.put("canAttackHostile", canAttackHostile);
		map.put("attackAllowedEntities", attackAllowedEntities);
		map.put("attackBannedEntities", attackBannedEntities);
		
		map.put("canEat", canEat);
		
		map.put("canBreakAllHanging", canBreakAllHanging);
		map.put("breakAllowedHangings", breakAllowedHangings);
		map.put("breakBannedHangings", breakBannedHangings);
		
		map.put("canUseBeds", canUseBeds);
		
		map.put("canPVP", canPVP);
		
		map.put("canFillBuckets", canFillBuckets);
		map.put("canEmptyBuckets", canEmptyBuckets);
		
		map.put("canShearSheep", canShearSheep);
		
		map.put("canUseAllEntities", canUseAllEntities);
		map.put("useAllowedEntities", useAllowedEntities);
		map.put("useBannedEntities", useBannedEntities);
		
		map.put("canDamageAllVehicles", canDamageAllVehicles);
		map.put("damageAllowedVehicles", damageAllowedVehicles);
		map.put("damageBannedVehicles", damageBannedVehicles);
		
		map.put("canEnterAllVehicles", canEnterAllVehicles);
		map.put("enterAllowedVehicles", enterAllowedVehicles);
		map.put("enterBannedVehicles", enterBannedVehicles);
		
		return map;
	}
	
	/**
	 * Constructor for deserialization of this from a configuration file.
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IslandProtectionDataSet(Map<String, Object> map) {
		this.canBuildAllBlocks = (boolean) map.get("canBuildAllBlocks");
		this.buildAllowedBlocks = (List<Material>) map.get("buildAllowedBlocks");
		this.buildBannedBlocks = (List<Material>) map.get("buildBannedBlocks");
		
		this.canBreakAllBlocks = (boolean) map.get("canBreakAllBlocks");
		this.breakAllowedBlocks = (List<Material>) map.get("breakAllowedBlocks");
		this.breakBannedBlocks = (List<Material>) map.get("breakBannedBlocks");
		
		this.canUseAllItems = (boolean) map.get("canUseAllItems");
		this.useAllowedBlocks = (List<Material>) map.get("useAllowedBlocks");
		this.useBannedBlocks = (List<Material>) map.get("useBannedBlocks");
		
		this.canAttackAllEntities = (boolean) map.get("canAttackAllEntities");
		this.canAttackAnimals = (boolean) map.get("canAttackAnimals");
		this.canAttackHostile = (boolean) map.get("canAttackHostile");
		this.attackAllowedEntities = (List<EntityType>) map.get("attackAllowedEntities");
		this.attackBannedEntities = (List<EntityType>) map.get("attackBannedEntities");
		
		this.canEat = (boolean) map.get("canEat");
		
		this.canBreakAllHanging = (boolean) map.get("canBreakAllHanging");
		this.breakAllowedHangings = (List<HangingType>) map.get("breakAllowedHangings");
		this.breakBannedHangings = (List<HangingType>) map.get("breakBannedHangings");
		
		this.canUseBeds = (boolean) map.get("canUseBeds");
		
		this.canPVP = (boolean) map.get("canPVP");
		
		this.canFillBuckets = (boolean) map.get("canFillBuckets");
		this.canEmptyBuckets = (boolean) map.get("canEmptyBuckets");
		
		this.canShearSheep = (boolean) map.get("canShearSheep");
		
		this.canUseAllEntities = (boolean) map.get("canUseAllEntities");
		this.useAllowedEntities = (List<EntityType>) map.get("useAllowedEntities");
		this.useBannedEntities = (List<EntityType>) map.get("useBannedEntities");
		
		this.canDamageAllVehicles = (boolean) map.get("canDamageAllVehicles");
		this.damageAllowedVehicles = (List<VehicleType>) map.get("damageAllowedVehicles");
		this.damageBannedVehicles = (List<VehicleType>) map.get("damageBannedVehicles");
		
		this.canEnterAllVehicles = (boolean) map.get("canEnterAllVehicles");
		this.enterAllowedVehicles = (List<VehicleType>) map.get("enterAllowedVehicles");
		this.enterBannedVehicles = (List<VehicleType>) map.get("enterBannedVehicles");
	}
	
	/**
	 * Deserialization of this from a configuration file.
	 * @param map
	 * @return
	 */
	public static IslandProtectionDataSet deserialize(Map<String, Object> map) {
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
