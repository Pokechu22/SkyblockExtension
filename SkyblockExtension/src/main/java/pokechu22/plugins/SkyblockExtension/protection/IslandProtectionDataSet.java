package pokechu22.plugins.SkyblockExtension.protection;

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
	 * Can one attack all mobs?
	 * 
	 * NOTE: When using {@link #canAttackAnimals} and 
	 * {@link #canAttackHostile}, this counts as both of them being true,
	 * along with handling all other entities.  Otherwise, the normal rules
	 * are applied.
	 */
	public boolean canAttackAllMobs;
	/**
	 * Can one attack all passive mobs?
	 */
	public boolean canAttackAnimals;
	/**
	 * Can one attack all hostile mobs?
	 */
	public boolean canAttackHostile;
	/**
	 * Mobs that are allowed to be attacked.
	 */
	public List<EntityType> attackAllowedMobs;
	/**
	 * Mobs that cannot be attacked.
	 */
	public List<EntityType> attackBannedMobs;
	
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
	 * Serialization for use with a configuration file.
	 */
	@Override
	public Map<String, Object> serialize() {
		
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Constructor for deserialization of this from a configuration file.
	 * @param map
	 * @return
	 */
	public IslandProtectionDataSet(Map<String, Object> map) {
		
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
