package pokechu22.plugins.SkyblockExtension;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import us.talabrek.ultimateskyblock.PlayerInfo;
import us.talabrek.ultimateskyblock.Settings;
import us.talabrek.ultimateskyblock.uSkyBlock;

/**
 * Provides various utilities for common island tasks.
 * 
 * @author Pokechu22
 *
 */
public class IslandUtils {
	/**
	 * Tests if the player is active, and thus player info can be obtained via
	 * {@linkplain #getPlayerInfo(Player)}.
	 * 
	 * @param p
	 * @return
	 */
	public static boolean canGetPlayerInfo(Player p) {
		return uSkyBlock.getInstance().getActivePlayers().containsKey(p.getName());
	}
	
	/**
	 * Gets the {@linkplain PlayerInfo} for a player.
	 * 
	 * @param p
	 * @return The player info, or null if there is no info available.
	 */
	public static PlayerInfo getPlayerInfo(Player p) {
		return uSkyBlock.getInstance().getActivePlayers().get(p.getName());
	}
	
	/**
	 * Gets the island that the specified location is in, or null if it is not
	 * in an island. The Y-value of the location is set to 120, the height of 
	 * the bedrock on the island.
	 * <br>
	 * TODO: Stop using int rounding and start using Math.Ceil or Math.floor.
	 * Save that for when you aren't coding at midnight, though.
	 * 
	 * @param startingLocation
	 *            The location to start with.
	 * @return The island location.
	 * @throws IllegalArgumentException
	 *             when location is null.
	 */
	public static Location getOccupyingIsland(Location startingLocation) {
		Location returned = null;
		if (startingLocation == null) {
			throw new IllegalArgumentException("Location cannot be null!");
		}
		
		int semiX = (startingLocation.getBlockX() + 
				(Settings.island_distance / 2)) / Settings.island_distance;
		int semiZ = (startingLocation.getBlockZ() + 
				(Settings.island_distance / 2)) / Settings.island_distance;
		
		double x = (semiX * Settings.island_distance);
		double z = (semiZ * Settings.island_distance);
		
		returned = new Location(startingLocation.getWorld(), x, 120d, z);
		return returned;
	}
}
