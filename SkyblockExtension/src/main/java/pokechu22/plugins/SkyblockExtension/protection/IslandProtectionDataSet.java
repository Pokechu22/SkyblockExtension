package pokechu22.plugins.SkyblockExtension.protection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.EntityType;

/**
 * <b>Serializable</b> protection configuration data, for a single entry.
 * 
 * @author Pokechu22
 *
 */
public class IslandProtectionDataSet implements ConfigurationSerializable {
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
