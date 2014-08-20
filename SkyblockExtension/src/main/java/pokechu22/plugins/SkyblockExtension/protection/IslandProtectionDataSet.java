package pokechu22.plugins.SkyblockExtension.protection;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.EntityType;

public class IslandProtectionDataSet implements ConfigurationSerializable {
	public boolean canBuildAll;
	public List<Material> buildAllowedBlocks;
	public List<Material> buildBannedBlocks;
	
	public boolean canBreakAll;
	public List<Material> breakAllowedBlocks;
	public List<Material> breakBannedBlocks;
	
	public boolean canUseAll;
	public List<Material> useAllowedBlocks;
	public List<Material> useBannedBlocks;
	
	public boolean canAttackAllMobs;
	public boolean canAttackAnimals;
	public boolean canAttackHostile;
	public List<EntityType> attackAllowedMobs;
	public List<EntityType> attackBannedMobs;
	
	public boolean canEat;
	
	public boolean canBreakAllHanging;
	public List<HangingType> breakAllowedHangings;
	public List<HangingType> breakBannedHangings;
	
	public boolean canUseBeds;
	
	public boolean canPVP;
	
	public boolean canFillBuckets;
	public boolean canEmptyBuckets;
	
	public boolean canShearSheep;
	
	public boolean canUseEntities;
	public List<EntityType> useAllowedEntities;
	public List<EntityType> useBannedEntities;
	
	public boolean canDamageAllVehicles;
	public List<VehicleType> damageAllowedVehicles;
	public List<VehicleType> damageBannedVehicles;
	
	public boolean canEnterAllVehicles;
	public List<VehicleType> enterAllowedVehicles;
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
