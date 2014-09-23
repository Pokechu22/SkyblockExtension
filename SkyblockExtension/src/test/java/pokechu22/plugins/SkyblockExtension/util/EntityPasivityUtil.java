package pokechu22.plugins.SkyblockExtension.util;

import java.util.EnumMap;

import org.bukkit.entity.EntityType;

/**
 * Tests is the entity is passive, hostile, or neutral.
 * @author Pokechu22
 *
 */
public class EntityPasivityUtil {
	/**
	 * Contains all passive entities.
	 * 
	 * @author Pokechu22
	 *
	 */
	protected static enum PassiveEntity {
		BAT(EntityType.BAT),
		PIG(EntityType.PIG),
		SHEEP(EntityType.SHEEP),
		COW(EntityType.COW),
		CHICKEN(EntityType.CHICKEN),
		SQUID(EntityType.SQUID),
		WOLF(EntityType.WOLF),
		MUSHROOM_COW(EntityType.MUSHROOM_COW),
		OCELOT(EntityType.OCELOT),
		HORSE(EntityType.HORSE),
		VILLAGER(EntityType.VILLAGER);
		
		private static final EnumMap<EntityType, PassiveEntity> 
				byEntityType = new EnumMap<>(EntityType.class);
		static {
			for (PassiveEntity e : PassiveEntity.values()) {
				byEntityType.put(e.correspondingType, e);
			}
		}
		
		private final EntityType correspondingType;
		private PassiveEntity(EntityType type) {
			this.correspondingType = type;
		}
		
		/**
		 * Checks if specified entityType is passive.
		 * @param e
		 * @return
		 */
		public static boolean isPassive(EntityType e) {
			return byEntityType.containsKey(e);
		}
	}
	
	/**
	 * Contains a list of all hostile entities.
	 * @author Pokechu22
	 *
	 */
	protected static enum HostileEntity {
		CREEPER(EntityType.CREEPER),
		SKELETON(EntityType.SKELETON),
		SPIDER(EntityType.SPIDER),
		GIANT(EntityType.GIANT),
		ZOMBIE(EntityType.ZOMBIE),
		SLIME(EntityType.SLIME),
		GHAST(EntityType.GHAST),
		PIG_ZOMBIE(EntityType.PIG_ZOMBIE),
		ENDERMAN(EntityType.ENDERMAN),
		CAVE_SPIDER(EntityType.CAVE_SPIDER),
		SILVERFISH(EntityType.SILVERFISH),
		BLAZE(EntityType.BLAZE),
		MAGMA_CUBE(EntityType.MAGMA_CUBE),
		ENDER_DRAGON(EntityType.ENDER_DRAGON),
		WITHER(EntityType.WITHER),
		WITCH(EntityType.WITCH);
		
		private static final EnumMap<EntityType, HostileEntity> 
				byEntityType = new EnumMap<>(EntityType.class);
		static {
			for (HostileEntity e : HostileEntity.values()) {
				byEntityType.put(e.correspondingType, e);
			}
		}
		
		private final EntityType correspondingType;
		private HostileEntity(EntityType type) {
			this.correspondingType = type;
		}
		
		/**
		 * Checks if specified entityType is passive.
		 * @param e
		 * @return
		 */
		public static boolean isHostile(EntityType e) {
			return byEntityType.containsKey(e);
		}
	}
	
	/**
	 * Returns true when the provided EntityType represents a passive mob.
	 * 
	 * @param e
	 */
	public static boolean isPassive(EntityType e) {
		return PassiveEntity.isPassive(e);
	}
	
	/**
	 * Returns true when the provided EntityType represents a hostile mob.
	 * 
	 * @param e
	 */
	public static boolean isHostile(EntityType e) {
		return HostileEntity.isHostile(e);
	}
	
	/**
	 * Returns true when the provided EntityType represents neither a
	 * hostile mob nor a passive mob.
	 * 
	 * @param e
	 */
	public static boolean isNuetralOrOther(EntityType e) {
		return (!isHostile(e) && !isPassive(e));
	}
}
