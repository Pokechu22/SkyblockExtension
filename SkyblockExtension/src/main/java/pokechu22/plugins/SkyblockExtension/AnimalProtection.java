package pokechu22.plugins.SkyblockExtension;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.BukkitPlayer;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import pokechu22.plugins.SkyblockExtension.errorhandling.ErrorHandler;
import pokechu22.plugins.SkyblockExtension.errorhandling.ThrowableReport;

/**
 * Protects animals in WorldGaurd regions.
 * 
 * From the old version of SBE.
 * 
 * @author Pokechu22
 */
public class AnimalProtection implements Listener {
	/**
	 * Whether or not AnimalProtection is enabled -- configuration setting.
	 */
	public static boolean enabled = true;
	
	@EventHandler(priority = EventPriority.HIGH)
	public void preventHurting(EntityDamageByEntityEvent e) {
		try {
			if (!enabled) {
				return;
			}
			if (e.getEntity() instanceof Player) {
				//Don't handle PVP as PVP is handled by main WorldGaurd.
				return;
			}
			if (e.getDamager() instanceof Projectile) {
				Projectile projectile = (Projectile) e.getDamager();
				if (projectile.getShooter() instanceof Player) {
					Player shooter = (Player) projectile.getShooter();
					if (!hasPermissionToHarm(shooter, e.getEntity())) {
						if(isProtected(e.getEntity().getLocation(), shooter)) {
							e.setCancelled(true);
							projectile.remove();
							shooter.sendMessage("§cYou don't have permission.");
						}
					}
				}
			} else if (e.getDamager() instanceof Player) {
				Player damager = (Player) e.getDamager();
				if (!hasPermissionToHarm(damager, e.getEntity())) {
					if(isProtected(e.getEntity().getLocation(), damager)) {
						e.setCancelled(true);
						damager.sendMessage("§cYou don't have permission.");
					}
				}
			}
		} catch (Exception ex) {
			ThrowableReport report = new ThrowableReport(ex, 
					"Handling animal protection event: " + e.getEventName() +
					"\nDamage: " + e.getDamage() + 
					"\nCause: " + e.getCause() + 
					"\nDamager" + e.getDamager() + 
					"\nEntity" + e.getEntity() + 
					"\nEntityType" + e.getEntityType() + 
					"\nHandlers" + e.getHandlers() + 
					"\nisAsynchronous" + e.isAsynchronous() + 
					"\nisCanceled" + e.isCancelled());
			ErrorHandler.logError(report);
		}
	}
	
	private boolean hasPermissionToHarm(Player player, Entity entity) {
		switch(entity.getType()) {
		case BAT:
		case CHICKEN:
		case COW:
		case HORSE:
		case IRON_GOLEM:
		case MUSHROOM_COW:
		case OCELOT: 
		case PIG:
		case SHEEP:
		case SNOWMAN: 
		case SQUID: 
		case VILLAGER:
		case WOLF: 
			return player.hasPermission("sbe.protection.animals");
		case BLAZE:
		case CAVE_SPIDER: 
		case CREEPER: 
		case ENDER_DRAGON: 
		case ENDERMAN: 
		case GHAST: 
		case GIANT: 
		case MAGMA_CUBE:
		case PIG_ZOMBIE: 
		case SILVERFISH:
		case SKELETON: 
		case SLIME: 
		case SPIDER: 
		case WITCH: 
		case ZOMBIE: 
			return player.hasPermission("sbe.protection.monsters");
		default:
			return player.hasPermission("sbe.protection.others");
		}
	}

	/**
	 * Checks if the specified location is within a worldgaurd protected
	 * region.
	 * 
	 * @param location The location to check.
	 * @param player The player to check.
	 * @return Whether the specified player is a member or owner of all
	 *        regions in the given area.
	 */
	private boolean isProtected(Location location, Player player) {
		RegionManager manager = WGBukkit.getRegionManager(player.getWorld());
		List<String> regions = manager.getApplicableRegionsIDs(
				new Vector(location.getX(), location.getY(), location.getZ()));
		for (String region : regions) {
			ProtectedRegion pr = manager.getRegion(region);
			if (!(pr.isMember(new BukkitPlayer(WGBukkit.getPlugin(), player)))) {
				return true;
			}
		}
		return false;
	}
}
