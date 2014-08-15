package pokechu22.plugins.SkyblockExtension.protection;

import java.util.ArrayList;

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
	/**
	 * Constructor - Also removes the original ProtectionEvents.
	 */
	public USkyBlockProtectionListener() {
		removeExistingProtectionEvents();
	}
	
	/**
	 * Destroys the original version added by uSkyBlock.
	 */
	@EventHandler(priority=EventPriority.NORMAL)
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
		
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerAttack(EntityDamageByEntityEvent event) {
		
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerBedEnter(PlayerBedEnterEvent event) {
		
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerBlockBreak(BlockBreakEvent event) {
		event.getPlayer().sendMessage("Test");
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerBlockPlace(BlockPlaceEvent event) {
		
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerBreakHanging(HangingBreakByEntityEvent event) {
		
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerShearEntity(PlayerShearEntityEvent event) {
		
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerVehicleDamage(VehicleDamageEvent event) {
		
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerEnterVehicle(VehicleEnterEvent event) {
		
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPotionThrow(PlayerInteractEvent event) {
		
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onEntityInteract(EntityInteractEvent event) {
		
	}
	
}
