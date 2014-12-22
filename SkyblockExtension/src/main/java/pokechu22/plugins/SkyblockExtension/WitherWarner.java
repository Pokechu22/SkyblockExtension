package pokechu22.plugins.SkyblockExtension;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Warns players who are making a wither that withers are actually 4 blocks high!
 * 
 * @author Pokechu22
 *
 */
public class WitherWarner implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void OnWitherSkullPlaced(BlockPlaceEvent e) {
		if (e.isCancelled()) {
			return; //If canceled, no point in warning.
		}
		if (e.getItemInHand().getType() == Material.SKULL_ITEM) {
			//I can't figure out how to do this properly.  DV 1 is that of wither skull.
			if (e.getItemInHand().getData().getData() == (byte)1) {
				//TODO: Check if the message *has* been disabled.
				e.getPlayer().sendMessage("§fHey!  Be careful!  While the model to make a wither is 3 by 3, the actual wither is 1 by 4!  If you're trying to cage it, it's possible for it to escape unless your cage is 4 high!");
				e.getPlayer().sendMessage("§7§oTo disable this message, do '§r§7/pokechu22 WitherWarning off§7§o'.");
			}
		}
	}
}
