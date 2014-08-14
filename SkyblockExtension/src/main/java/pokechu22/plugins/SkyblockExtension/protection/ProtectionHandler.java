package pokechu22.plugins.SkyblockExtension.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import us.talabrek.ultimateskyblock.Settings;

/**
 * Things to check if an area is protected.
 * 
 * @author Pokechu22
 *
 */
public abstract class ProtectionHandler {
	
	/**
	 * Gets the ProtectionHanlder that should be used in this context.
	 * Mainly for the purpose of dealing with uSkyblock having multiple 
	 * protection options.
	 * 
	 * @return
	 */
	public static ProtectionHandler getProtectionHandler() {
		if (Settings.island_protectWithWorldGuard) {
			return new WorldGuardProtectionHandler();
		} else {
			return new USkyBlockProtectionHandler();
		}
	}
	
	/**
	 * Is the area protected?
	 * 
	 * @param location
	 * @param player
	 * @return
	 */
	public abstract boolean isProtected(Location location, Player player);
}
