package pokechu22.plugins.SkyblockExtension.protection;

import org.bukkit.entity.EntityType;

public enum VehicleType {
	BOAT(EntityType.BOAT), 
	HORSE(EntityType.HORSE),
	PIG(EntityType.PIG),
	MINECART(EntityType.MINECART),
	MINECART_CHEST(EntityType.MINECART_CHEST),
	MINECART_COMMAND(EntityType.MINECART_COMMAND),
	MINECART_FURNACE(EntityType.MINECART_FURNACE),
	MINECART_HOPPER(EntityType.MINECART_HOPPER),
	MINECART_MOB_SPAWNER(EntityType.MINECART_MOB_SPAWNER),
	MINECART_TNT(EntityType.MINECART_TNT);
	
	public boolean matches(EntityType entity) {
		return type.equals(entity);
	}
	
	private EntityType type;
	
	private VehicleType(EntityType t) {
		this.type = t;
	}
	
	public static boolean isVehicle(EntityType entity) {
		for (VehicleType vehicleType : VehicleType.values()) {
			if (vehicleType.matches(entity)) {
				return true;
			}
		}
		return false;
	}
}