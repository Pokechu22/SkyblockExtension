package pokechu22.plugins.SkyblockExtension.protection;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
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
import org.bukkit.plugin.RegisteredListener;

import us.talabrek.ultimateskyblock.ProtectionEvents;
import us.talabrek.ultimateskyblock.uSkyBlock;

/**
 * Handles overriding any existing uSkyBlock protections, and new implementations
 * of the previous ones.
 * 
 * @author Pokechu22
 * @author Talabrek
 * @author wolfwork
 */
public class USkyBlockProtectionListener extends ProtectionEvents implements Listener {
	private USkyBlockProtectionEvents USBProtection;
	
	/**
	 * Constructor - Also removes the original ProtectionEvents.
	 */
	public USkyBlockProtectionListener() {
		USBProtection = new USkyBlockProtectionEvents();
		
		removeExistingProtectionEvents();
	}
	
	/**
	 * Destroys the original version added by uSkyBlock.
	 */
	public static void removeExistingProtectionEvents() {
		ArrayList<RegisteredListener> uSkyBlockListeners = 
				HandlerList.getRegisteredListeners(uSkyBlock.getInstance());
		
		for (RegisteredListener registeredListener : uSkyBlockListeners) {
			Listener listener = registeredListener.getListener();
			
			if (listener instanceof ProtectionEvents) {
				HandlerList.unregisterAll(listener);
			}
		}
	}
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onHorseLead(PlayerInteractEntityEvent event) {
		if (USBProtection.onHorseLead(event)) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§cYou don't have permission to do that in this area.");
		}
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerAttack(EntityDamageByEntityEvent event) {
		if (USBProtection.onPlayerAttack(event)) {
			event.setCancelled(true);
			if (event.getDamager() instanceof Player) {
				Player damager = (Player) event.getDamager();
				damager.sendMessage("§cYou don't have permission to do that in this area.");
			}
		}
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerBedEnter(PlayerBedEnterEvent event) {
		if (USBProtection.onPlayerBedEnter(event)) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§cYou don't have permission to do that in this area.");
		}
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerBlockBreak(BlockBreakEvent event) {
		if (USBProtection.onPlayerBlockBreak(event)) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§cYou don't have permission to do that in this area.");
		}
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerBlockPlace(BlockPlaceEvent event) {
		if (USBProtection.onPlayerBlockPlace(event)) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§cYou don't have permission to do that in this area.");
		}
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerBreakHanging(HangingBreakByEntityEvent event) {
		if (USBProtection.onPlayerBreakHanging(event)) {
			event.setCancelled(true);
			if (event.getRemover() instanceof Player) {
				Player remover = (Player) event.getRemover();
				remover.sendMessage("§cYou don't have permission to do that in this area.");
			}
		}
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		if (USBProtection.onPlayerBucketEmpty(event)) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§cYou don't have permission to do that in this area.");
		}
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		if (USBProtection.onPlayerBucketFill(event)) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§cYou don't have permission to do that in this area.");
		}
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!USBProtection.onPlayerInteract(event)) {
			return; //Exit if uSkyBlock would have allowed it.
		}
		
		if (event.hasItem()) {
			//TODO: User overrides, by material or numeric ID.
			
			if (event.getItem().getType() == Material.ENDER_PEARL) {
				//TODO: Check if we allow this.
				event.setCancelled(true);
				event.getPlayer().sendMessage("§cYou don't have permission to throw ender " + 
						"perals from this area.");
				return;
			}
			if (event.getItem().getType().isEdible()) {
				//TODO: Check if we allow this.  
				event.setCancelled(true);
				event.getPlayer().sendMessage("§cYou aren't allowed to eat in this area.");
				return;
			}
			
			//TODO: Default cases.
		}
		
		if (event.hasBlock()) {
			//TODO: User overrides, by material or numeric ID.
			
			//IF IS A REDSTONE ITEM {
			if (event.getClickedBlock().getType() == Material.STONE_BUTTON || 
					event.getClickedBlock().getType() == Material.WOOD_BUTTON) {
				//TODO: Check if we allow this.  
				event.setCancelled(true);
				event.getPlayer().sendMessage("§cYou aren't allowed to use buttons " + 
						"in this area.");
				return;
			}
			if (event.getClickedBlock().getType() == Material.LEVER) {
				//TODO: Check if we allow this.  
				event.setCancelled(true);
				event.getPlayer().sendMessage("§cYou aren't allowed to use levers " + 
						"in this area.");
				return;
			}
			//This includes some items as well, but it's hard to know which are and which aren't.
			if (event.getClickedBlock().getType() == Material.REDSTONE_COMPARATOR || 
					event.getClickedBlock().getType() == Material.REDSTONE_COMPARATOR_OFF ||
					event.getClickedBlock().getType() == Material.REDSTONE_COMPARATOR_ON ||
					event.getClickedBlock().getType() == Material.DIODE || //Repeater
					event.getClickedBlock().getType() == Material.DIODE_BLOCK_ON ||
					event.getClickedBlock().getType() == Material.DIODE_BLOCK_OFF) {
				//TODO: Check if we allow this.  
				event.setCancelled(true);
				event.getPlayer().sendMessage("§cYou aren't allowed to change the setting of " + 
						"that in this area.");
				return;
			}
			//} ELSE IF IS A DOOR {
			if (event.getClickedBlock().getType() == Material.WOODEN_DOOR || 
					event.getClickedBlock().getType() == Material.TRAP_DOOR) {
				//TODO: Check if we allow this.  
				event.setCancelled(true);
				event.getPlayer().sendMessage("§cYou aren't allowed to use doors " + 
						"in this area.");
				return;
			}
			//} ELSE IF IS A STORAGE THINGY {
			if (event.getClickedBlock().getType() == Material.CHEST || 
					event.getClickedBlock().getType() == Material.TRAPPED_CHEST) {
				//TODO: Check if we allow this.  
				event.setCancelled(true);
				event.getPlayer().sendMessage("§cYou aren't allowed to use chests " + 
						"in this area.");
				return;
			}
			if (event.getClickedBlock().getType() == Material.FURNACE || 
					event.getClickedBlock().getType() == Material.BURNING_FURNACE) {
				//TODO: Check if we allow this.  
				event.setCancelled(true);
				event.getPlayer().sendMessage("§cYou aren't allowed to use furnaces " + 
						"in this area.");
				return;
			}
			if (event.getClickedBlock().getType() == Material.FURNACE || 
					event.getClickedBlock().getType() == Material.BURNING_FURNACE) {
				//TODO: Check if we allow this.  
				event.setCancelled(true);
				event.getPlayer().sendMessage("§cYou aren't allowed to use furnaces " + 
						"in this area.");
				return;
			}
			if (event.getClickedBlock().getType() == Material.ANVIL) {
				//TODO: Check if we allow this.  
				event.setCancelled(true);
				event.getPlayer().sendMessage("§cYou aren't allowed to use anvils " + 
						"in this area.");
				return;
			}
			if (event.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE) {
				//TODO: Check if we allow this.  
				event.setCancelled(true);
				event.getPlayer().sendMessage("§cYou aren't allowed to use enchantment tables " + 
						"in this area.");
				return;
			}
			if (event.getClickedBlock().getType() == Material.BREWING_STAND) {
				//TODO: Check if we allow this.  
				event.setCancelled(true);
				event.getPlayer().sendMessage("§cYou aren't allowed to use brewing stands " + 
						"in this area.");
				return;
			}
			//}
		
			//TODO: Check if we allow this.  
			event.setCancelled(true);
			event.getPlayer().sendMessage("§cYou aren't allowed to do that in this area.");
			return;
		}
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerShearEntity(PlayerShearEntityEvent event) {
		if (USBProtection.onPlayerShearEntity(event)) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§cYou don't have permission to do that in this area.");
		}
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerVehicleDamage(VehicleDamageEvent event) {
		if (USBProtection.onPlayerVehicleDamage(event)) {
			event.setCancelled(true);
			if (event.getAttacker() instanceof Player) {
				Player attacker = (Player) event.getAttacker();
				attacker.sendMessage("§cYou don't have permission to do that in this area.");
			}
		}
	}

	//This is not in the old version of uSkyBlock but it is in the new one.
	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerEnterVehicle(VehicleEnterEvent event) {
		if (USBProtection.onPlayerEnterVehicle(event)) {
			event.setCancelled(true);
			if (event.getEntered() instanceof Player) {
				Player attacker = (Player) event.getEntered();
				attacker.sendMessage("§cYou don't have permission to do that in this area.");
			}
		}
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPotionThrow(PlayerInteractEvent event) {
		if (USBProtection.onPotionThrow(event)) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§cYou don't have permission to do that in this area.");
		}
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onEntityInteract(EntityInteractEvent event) {
		if (USBProtection.onEntityInteract(event)) {
			event.setCancelled(true);
			if (event.getEntity() instanceof Player) {
				Player interactingPlayer = (Player) event.getEntity();
				interactingPlayer.sendMessage("§cYou don't have permission to do that in this area.");
			}
		}
	}
}
