package pokechu22.plugins.SkyblockExtension.protection;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.potion.Potion;

import us.talabrek.ultimateskyblock.*;

/**
 * Almost the exact same thing as the original 
 * {@linkplain us.talabrek.ultimateskyblock.ProtectionEvents ProtectionEvents}, 
 * but returns booleans rather than canceling events.
 * 
 * @author Talabrek
 * @author Pokechu22
 *
 */
public class USkyBlockProtectionEvents {
	private Player breaker = null;

	public boolean onHorseLead(final PlayerInteractEntityEvent event) {
		if (event.getRightClicked().getType() == EntityType.HORSE || event.getRightClicked().getType() == EntityType.ITEM_FRAME) {
			final Player player = event.getPlayer();
			if (player.getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
				if (!ProtectionHandler.getProtectionHandler().hasPermissionsIn(player, event.getRightClicked().getLocation()) && !uSkyBlock.getInstance().playerIsInSpawn(event.getPlayer()) && !VaultHandler.checkPerk(player.getName(), "usb.mod.bypassprotection", player.getWorld()) && !player.isOp()) {
					return true;
				}
			}
		}
	}

	public boolean onPlayerAttack(final EntityDamageByEntityEvent event) {
		if (!event.getEntity().getWorld().getName().equalsIgnoreCase(Settings.general_worldName))
			return false;

		if (!(event.getDamager() instanceof Player) && !(event.getDamager() instanceof Projectile))
			return false;
		
		if (event.getEntity() instanceof Player)
			return false;

		Player damager = null;

		if (event.getDamager() instanceof Projectile) {

			Projectile projectile = (Projectile) event.getDamager();

			if (projectile.getShooter() instanceof Player)
				damager = (Player) projectile.getShooter();
		} else if (event.getDamager() instanceof Player)
			damager = (Player) event.getDamager();

		if (damager == null)
			return false;
		
		if (damager.hasPermission("usb.mod.bypassprotection"))
			return false;

		if (!uSkyBlock.getInstance().playerIsOnIsland(damager))
			return true;
	}

	public boolean onPlayerBedEnter(final PlayerBedEnterEvent event) {
		if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
			if (!uSkyBlock.getInstance().playerIsOnIsland(event.getPlayer()) && !VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld()) && !event.getPlayer().isOp()) {
				return true;
			}
		}
	}

	public boolean onPlayerBlockBreak(final BlockBreakEvent event) {
		if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
			if (!ProtectionHandler.getProtectionHandler().hasPermissionsIn(event.getPlayer(), event.getBlock().getLocation()) && !VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld()) && !event.getPlayer().isOp()) {
				return true;
			}
		}
	}

	public boolean onPlayerBlockPlace(final BlockPlaceEvent event) {
		if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
			if (!ProtectionHandler.getProtectionHandler().hasPermissionsIn(event.getPlayer(), event.getBlock().getLocation()) && !VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld()) && !event.getPlayer().isOp()) {
				return true;
			}
		}
	}

	public boolean onPlayerBreakHanging(final HangingBreakByEntityEvent event) {
		if (event.getRemover() instanceof Player) {
			breaker = (Player) event.getRemover();
			if (breaker.getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
				if (!ProtectionHandler.getProtectionHandler().hasPermissionsIn(breaker, event.getEntity().getLocation()) && !VaultHandler.checkPerk(breaker.getName(), "usb.mod.bypassprotection", breaker.getWorld()) && !breaker.isOp()) {
					return true;
				}
			}
		}
	}

	public boolean onPlayerBucketEmpty(final PlayerBucketEmptyEvent event) {
		if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
			if (!ProtectionHandler.getProtectionHandler().hasPermissionsIn(event.getPlayer(), event.getBlockClicked().getLocation()) && !VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld()) && !event.getPlayer().isOp()) {
				return true;
			}
		}
	}

	public boolean onPlayerBucketFill(final PlayerBucketFillEvent event) {
		if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
			if (!ProtectionHandler.getProtectionHandler().hasPermissionsIn(event.getPlayer(), event.getBlockClicked().getLocation()) && !VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld()) && !event.getPlayer().isOp()) {
				return true;
			}
		}
	}

	public boolean onPlayerInteract(final PlayerInteractEvent event) {
		if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
			if (!uSkyBlock.getInstance().playerIsOnIsland(event.getPlayer()) && !uSkyBlock.getInstance().playerIsInSpawn(event.getPlayer()) && !VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld()) && !event.getPlayer().isOp()) {
				if (event.getMaterial() == Material.ENDER_PEARL) {
					return true;
					return false;
				}

				if (event.getClickedBlock() != null && !event.getClickedBlock().getType().isEdible()) {
					return true;
				}
			}
		}
	}

	public boolean onPlayerShearEntity(final PlayerShearEntityEvent event) {
		if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
			if (!uSkyBlock.getInstance().playerIsOnIsland(event.getPlayer()) && !VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld()) && !event.getPlayer().isOp()) {
				return true;
			}
		}
	}

	public boolean onPlayerVehicleDamage(final VehicleDamageEvent event) {
		if (event.getAttacker() instanceof Player) {
			breaker = (Player) event.getAttacker();
			if (breaker.getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
				if (!ProtectionHandler.getProtectionHandler().hasPermissionsIn(breaker, event.getVehicle().getLocation()) && !VaultHandler.checkPerk(breaker.getName(), "usb.mod.bypassprotection", breaker.getWorld()) && !breaker.isOp()) {
					return true;
				}
			}
		}
	}

	
	public boolean onPlayerEnterVehicle(VehicleEnterEvent event) {
		if (!(event.getEntered() instanceof Player))
			return false;

		Player player = (Player) event.getEntered();

		if (player.hasPermission("usb.mod.bypassprotection"))
			return false;

		if (!event.getEntered().getWorld().getName().equalsIgnoreCase(Settings.general_worldName))
			return false;

		if (!ProtectionHandler.getProtectionHandler().hasPermissionsIn(player, event.getVehicle().getLocation())) {
			return true;
		}
	}

	
	public boolean onPotionThrow(PlayerInteractEvent event) {
		if (event.getPlayer().hasPermission("usb.mod.bypassprotection"))
			return false;

		if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName))
			return false;

		if (ProtectionHandler.getProtectionHandler().hasPermissionsIn(event.getPlayer(), event.getPlayer().getLocation()))
			return false;

		if (event.getMaterial() == Material.POTION && event.getItem().getDurability() != 0) {

			Potion potion = null;

			try {
				potion = Potion.fromItemStack(event.getItem());
			} catch (IllegalArgumentException e) {
				return false;
			}

			if (potion == null)
				return false;

			if (potion.isSplash()) {
				return true;
			}
		}
	}

  
	public boolean onEntityInteract(EntityInteractEvent event) {
		if (!(event.getEntity() instanceof Arrow))
			return false;

		Arrow arrow = (Arrow) event.getEntity();

		if (!(arrow.getShooter() instanceof Player))
			return false;

		Player damager = (Player) arrow.getShooter();
		
		if (damager == null)
			return false;

		if (damager.hasPermission("usb.mod.bypassprotection"))
			return false;

		if (!uSkyBlock.getInstance().playerIsOnIsland(damager))
			return true;
	}
}
