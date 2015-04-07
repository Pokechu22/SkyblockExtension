package pokechu22.plugins.SkyblockExtension;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

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
	@EventHandler(priority = EventPriority.HIGH)
	public void preventHurting(EntityDamageByEntityEvent e) {
		try {
			if (e.getEntity() instanceof Player) {
				//Don't handle PVP as PVP is handled by main WorldGaurd.
				return;
			}
			if (e.getDamager() instanceof Projectile) {
				Projectile projectile = (Projectile) e.getDamager();
				if (projectile.getShooter() instanceof Player) {
					Player shooter = (Player) projectile.getShooter();
					if(isProtected(e.getEntity().getLocation(), shooter, hasPerms(shooter, e.getEntity()))) {
						e.setCancelled(true);
						projectile.remove();
						shooter.sendMessage("§cYou don't have permission.");
					}
				}
			} else if (e.getDamager() instanceof Player) {
				Player damager = (Player) e.getDamager();
				if(isProtected(e.getEntity().getLocation(), damager, hasPerms(damager, e.getEntity()))) {
					e.setCancelled(true);
					damager.sendMessage("§cYou don't have permission.");
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
}
