package pokechu22.plugins.SkyblockExtension.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import us.talabrek.ultimateskyblock.Settings;

/**
 * Things to check if an area is protected.
 * 
 * <b>This does NOT handle actual protection events.</b>
 * 
 * @author Pokechu22
 *
 */
public abstract class ProtectionHandler_OLD {
	
	/**
	 * Gets the ProtectionHanlder that should be used in this context.
	 * Mainly for the purpose of dealing with uSkyblock having multiple 
	 * protection options.
	 * 
	 * @return
	 */
	public static ProtectionHandler_OLD getProtectionHandler() {
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
	
	/**
	 * Tests if a player has permissions in an area.  
	 * Exactly the same as <code>!{@linkplain #isProtected(Location, Player)}</code>.
	 * 
	 * @param location
	 * @param player
	 * @return
	 */
	public boolean hasPermissionsIn(Location location, Player player) {
		return !isProtected(location, player);
	}
	
	/**
	 * Tests if a player has permissions in an area.  
	 * Exactly the same as <code>!{@linkplain #isProtected(Location, Player)}</code>.
	 * <br>
	 * This may seem like it is a redundant method, but it is needed to prevent
	 * pains dealing with ProtectionEvents code.
	 * 
	 * @param location
	 * @param player
	 * @return
	 */
	public boolean hasPermissionsIn(Player player, Location location) {
		return !isProtected(location, player);
	}
}
