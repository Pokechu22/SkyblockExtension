package pokechu22.plugins.SkyblockExtension.util;

import static pokechu22.plugins.SkyblockExtension.util.IslandUtils.getIslandInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import pokechu22.plugins.SkyblockExtension.SkyblockExtension;
import pokechu22.plugins.SkyblockExtension.protection.IslandInfo;
import pokechu22.plugins.SkyblockExtension.protection.IslandProtectionDataSet;
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
	 * @param player
	 * @return
	 */
	public static boolean canGetPlayerInfo(Player player) {
		return canGetPlayerInfo(player.getName());
	}
	
	/**
	 * Tests if the player is active, and thus player info can be obtained via
	 * {@linkplain #getPlayerInfo(Player)}.
	 * 
	 * @param player
	 * @return
	 */
	public static boolean canGetPlayerInfo(String player) {
		return uSkyBlock.getInstance().getActivePlayers().containsKey(player);
	}
	
	/**
	 * Gets the {@linkplain PlayerInfo} for a player.
	 * 
	 * @param player
	 * @return The player info, or null if there is no info available.
	 */
	public static PlayerInfo getPlayerInfo(Player player) {
		return getPlayerInfo(player.getName());
	}
	
	/**
	 * Gets the {@linkplain PlayerInfo} for a player.
	 * 
	 * @param player
	 * @return The player info, or null if there is no info available.
	 */
	public static PlayerInfo getPlayerInfo(String player) {
		return uSkyBlock.getInstance().getActivePlayers().get(player);
	}
	
	/**
	 * Gets the island that the specified location is in, or null if it is not
	 * in an island. The Y-value of the location is set to 120, the height of 
	 * the bedrock on the island.
	 * <br>
	 * TODO: Stop using int rounding and start using Math.Ceil or Math.floor.
	 * Save that for when you aren't coding at midnight, though.
	 * 
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
		
		int semiX = getNearestIslandLocalX(startingLocation);
		int semiZ = getNearestIslandLocalZ(startingLocation);
		
		double x = (semiX * Settings.island_distance);
		double z = (semiZ * Settings.island_distance);
		
		returned = new Location(startingLocation.getWorld(), x, 120d, z);
		return returned;
	}
	
	/**
	 * Gets the x-coordinate of the nearest island, using the distance from spawn.
	 * <br>
	 * NOTE: At the exact border, the exact behavior is uncertain, and doesn't
	 * matter.  It does not need to be tested; so long as 55 returns either 1 or 0,
	 * we are good.
	 * 
	 * @param location
	 * @return
	 */
	public static int getNearestIslandLocalX(Location location) {
		if (location.getBlockX() < 0) { //Special case...
			return (location.getBlockX() - 
					(Settings.island_distance / 2)) / Settings.island_distance;
		}
		
		return (location.getBlockX() + 
				(Settings.island_distance / 2)) / Settings.island_distance;
	}
	
	/**
	 * Gets the z-coordinate of the nearest island, using the distance from spawn.
	 * <br>
	 * NOTE: At the exact border, the exact behavior is uncertain, and doesn't
	 * matter.  It does not need to be tested; so long as 55 returns either 1 or 0,
	 * we are good.
	 * 
	 * @param location
	 * @return
	 */
	public static int getNearestIslandLocalZ(Location location) {
		if (location.getBlockZ() < 0) { //Special case...
			return (location.getBlockZ() - 
					(Settings.island_distance / 2)) / Settings.island_distance;
		}
		
		return (location.getBlockZ() + 
				(Settings.island_distance / 2)) / Settings.island_distance;
	}
	
	/**
	 * Gets the name of the island, in the form of "x" + local x + "z" + local z 
	 * @param location
	 * @return
	 */
	public static String getNearestIslandName(Location location) {
		return "x" + getNearestIslandLocalX(location) + 
				"z" + getNearestIslandLocalZ(location);
	}
	
	/**
	 * Gets the relevant IslandInfo for a location.
	 * Returns null if the data could not be found.  Does not attempt
	 * to create a new island info.
	 * @param location
	 * @return
	 */
	public static IslandInfo getIslandInfo(Location location) {
		try {
			return IslandInfo.readFromDisk(getNearestIslandName(location));
		} catch (FileNotFoundException e) {
			//None existing at this time, OK to return null.
			return null;
		} catch (IOException e) {
			//This is more of a problem.
			SkyblockExtension.inst().getLogger().log(Level.SEVERE, 
					"Failed to read IslandInfo for location " + 
							location + ".", e);
			return null;
		}
	}
	
	/**
	 * Gets the relevant IslandInfo for a PlayerInfo.
	 * Returns null if the data could not be found.  If the player has
	 * an island, but no PlayerInfo, a new one is created.
	 * @param location
	 * @return
	 */
	public static IslandInfo getIslandInfo(PlayerInfo playerInfo) {
		if (!playerInfo.getHasIsland()) {
			IslandInfo returned;
			//See if we can get one directly.
			returned = getIslandInfo(playerInfo.getIslandLocation());
			//If there isn't one, try creating one.
			if (returned == null) {
				returned = IslandInfo.convertFromPlayerInfo(playerInfo);
			}
			//If that STILL didn't work, we have an issue.
			if (returned == null) {
				SkyblockExtension.inst().getLogger().log(Level.SEVERE, 
						"Failed to read IslandInfo for playerinfo " + 
								playerInfo + ".  Location: " + 
								playerInfo.getIslandLocation() + 
								", Player: " + playerInfo.getPlayer() + 
								", Party: " + playerInfo.getMembers());
			}
			return returned;
		}
		//No info.
		return null;
	}
	
	/**
	 * Gets the IslandProtectionDataSet that applies to that player in the specific area.
	 * 
	 * @param player
	 * @param at The location of the island to check.
	 * @return
	 */
	public static IslandProtectionDataSet getDataSetFor(Player player, Location at) {
		return getIslandInfo(at).getDataSetForPlayer(player);
	}
}
