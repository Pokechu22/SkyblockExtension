package pokechu22.plugins.SkyblockExtension.protection;

import static pokechu22.plugins.SkyblockExtension.util.IslandUtils.*;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import pokechu22.plugins.SkyblockExtension.util.EntityPasivityUtil;

public class ProtectionListener {
	/**
	 * Called when a player interacts with (EG right-clicks) an entity.
	 * 
	 * @param event
	 */
	@EventHandler(priority=EventPriority.NORMAL)
	public void onEntityInteract(PlayerInteractEntityEvent e) {
		if (hasModOverride(e.getPlayer())) {
			return;
		}
		
		IslandProtectionDataSet set = getDataSetFor(e.getPlayer(), 
				e.getRightClicked().getLocation());
		
		if (set.useBannedEntities.getValue().contains(e.getRightClicked().getType())) {
			e.setCancelled(true);
			e.getPlayer().sendMessage("§cYou aren't allowed to do that in this area!");
			return;
		} else {
			if (set.canUseAllEntities.getValue()) {
				return;
			} else {
				if (set.useAllowedEntities.getValue().contains(e.getRightClicked().getType())) {
					return;
				} else {
					e.setCancelled(true);
					e.getPlayer().sendMessage("§cYou aren't allowed to do that in this area!");
					return;
				}
			}
		}
	}
	
	/**
	 * Called when an entity attacks another; used to check for player
	 * hurting mobs.
	 * 
	 * @param e
	 */
	public void onEntityAttackEntity(EntityDamageByEntityEvent e) {
		//Do some stuff to check if the attacker is a player.
		Player attacker = null;
		
		if (e.getDamager() instanceof Player) {
			attacker = (Player) e.getDamager();
		} else if (e.getDamager() instanceof Projectile) {
			//Changed from uSkyBlock:
			//now uses projectile rather than arrow.
			Projectile p = (Projectile) e.getDamager();
			if (p.getShooter() instanceof Player) {
				attacker = (Player) p.getShooter();
			}
		}
		
		if (attacker == null) {
			//If the attacker wasn't a player.
			return;
		}
		
		if (hasModOverride(attacker)) {
			return;
		}
		
		IslandProtectionDataSet set = getDataSetFor(attacker, 
				e.getEntity().getLocation());
		
		if (set.attackBannedEntities.getValue().contains(e.getEntity().getType())) {
			e.setCancelled(true);
			attacker.sendMessage("§cYou aren't allowed to do that in this area!");
			return;
		} else {
			//Deviating from standard code here.  Animals/hostiles are tested.
			if (set.canAttackAllEntities.getValue()) {
				return;
			}
			
			if (set.canAttackAnimals.getValue()) {
				if (EntityPasivityUtil.isPassive(e.getEntityType())) {
					return;
				}
			}
			if (set.canAttackHostile.getValue()) {
				if (EntityPasivityUtil.isHostile(e.getEntityType())) {
					return;
				}
			}
			
			//else {
			if (set.attackAllowedEntities.getValue().contains(e.getEntity().getType())) {
				return;
			} else {
				e.setCancelled(true);
				attacker.sendMessage("§cYou aren't allowed to do that in this area!");
				return;
			}
			//}
		}
	}
	
	/**
	 * Called when a player enters a bed. 
	 * 
	 * @param event
	 */
	public void onPlayerBedEnter(PlayerBedEnterEvent e) {
		if (hasModOverride(e.getPlayer())) {
			return;
		}
		
		IslandProtectionDataSet set = getDataSetFor(e.getPlayer(), 
				e.getBed().getLocation());
		
		if (set.canUseBeds.getValue()) {
			return;
		} else {
			e.setCancelled(true);
			e.getPlayer().sendMessage("§cYou aren't allowed to do that in this area!");
			return;
		}
	}
	
	/**
	 * Called when a player breaks a block.
	 * 
	 * @param event
	 */
	public void onPlayerBlockBreak(BlockBreakEvent event) {
		if (hasModOverride(event.getPlayer())) {
			return;
		}
		
		IslandProtectionDataSet set = getDataSetFor(event.getPlayer(), 
				event.getBlock().getLocation());
		
		if (set.breakBannedBlocks.getValue().contains(event.getBlock().getType())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§cYou aren't allowed to do that in this area!");
			return;
		} else {
			if (set.canBreakAllBlocks.getValue()) {
				return;
			} else {
				if (set.breakAllowedBlocks.getValue().contains(event.getBlock().getType())) {
					return;
				} else {
					event.setCancelled(true);
					event.getPlayer().sendMessage("§cYou aren't allowed to do that in this area!");
					return;
				}
			}
		}
	}
	
	/**
	 * Called when a player places a block.
	 * 
	 * @param event
	 */
	public void onPlayerBlockPlace(BlockPlaceEvent event) {
		if (hasModOverride(event.getPlayer())) {
			return;
		}
		
		IslandProtectionDataSet set = getDataSetFor(event.getPlayer(), 
				event.getBlock().getLocation());
		
		if (set.buildAllowedBlocks.getValue().contains(event.getBlock().getType())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§cYou aren't allowed to do that in this area!");
			return;
		} else {
			if (set.canBuildAllBlocks.getValue()) {
				return;
			} else {
				if (set.buildBannedBlocks.getValue().contains(event.getBlock().getType())) {
					return;
				} else {
					event.setCancelled(true);
					event.getPlayer().sendMessage("§cYou aren't allowed to do that in this area!");
					return;
				}
			}
		}
	}
	
	/**
	 * Called when a player breaks a hanging.
	 * 
	 * @param event
	 */
	public void onPlayerBreakHanging(HangingBreakByEntityEvent event) {
		if (!(event.getRemover() instanceof Player)) {
			//Only handle players.
			return;
		}
		Player player = (Player) event.getRemover();
		
		if (hasModOverride(player)) {
			return;
		}
		
		IslandProtectionDataSet set = getDataSetFor(player, 
				event.getEntity().getLocation());
		
		if (set.breakBannedHangings.getValue().contains(HangingType.getHangingType(event.getEntity().getType()))) {
			event.setCancelled(true);
			player.sendMessage("§cYou aren't allowed to do that in this area!");
			return;
		} else {
			if (set.canBreakAllHanging.getValue()) {
				return;
			} else {
				if (set.breakAllowedHangings.getValue().contains(HangingType.getHangingType(event.getEntity().getType()))) {
					return;
				} else {
					event.setCancelled(true);
					player.sendMessage("§cYou aren't allowed to do that in this area!");
					return;
				}
			}
		}
		
	}
	
	/**
	 * Tests if the player has the specified override.
	 * 
	 * If they have the override, ("usb.mod.bypassprotection"), they get
	 * a warning but are allowed. 
	 */
	private boolean hasModOverride(Player player) {
		return (player.hasPermission("usb.mod.bypassprotection"));
	}
}
