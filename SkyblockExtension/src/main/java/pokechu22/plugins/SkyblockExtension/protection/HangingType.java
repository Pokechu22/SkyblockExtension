package pokechu22.plugins.SkyblockExtension.protection;

import org.bukkit.entity.EntityType;

enum HangingType {
	PAINTING(EntityType.PAINTING), 
	ITEM_FRAME(EntityType.ITEM_FRAME),
	LEASH_HITCH(EntityType.LEASH_HITCH);
	
	public boolean matches(EntityType entity) {
		return type.equals(entity);
	}
	
	private EntityType type;
	
	private HangingType(EntityType t) {
		this.type = t;
	}
	
	public static boolean isHanging(EntityType entity) {
		for (HangingType hangingType : HangingType.values()) {
			if (hangingType.matches(entity)) {
				return true;
			}
		}
		return false;
	}
}