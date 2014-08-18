package pokechu22.plugins.SkyblockExtension.protection;

import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * Handles checking for custom protections, by island.
 * 
 * @author Pokechu22
 *
 */
public class IslandProtectionOverrides {
	/**
	 * 
	 * @param event
	 * @return
	 */
	public static boolean areaAllowsPlacing(Location islandLocation, Block block) {
		return false;
	}
}
