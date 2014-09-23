package pokechu22.plugins.SkyblockExtension.protection;

import static pokechu22.plugins.SkyblockExtension.util.IslandUtils.*;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;

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
	 * Tests if the player has the specified override/
	 * 
	 * If they have the override, ("usb.mod.bypassprotection"), they get
	 * a warning but are allowed. 
	 */
	private boolean hasModOverride(Player player) {
		return (player.hasPermission("usb.mod.bypassprotection"));
	}
}
