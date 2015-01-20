package pokechu22.plugins.SkyblockExtension.util;

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
	private IslandUtils() {throw new AssertionError("Should never be called.");}
	
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
	 * TODO: This seems like a slightly odd method; mabye a better general method?
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
	 * TODO: This seems like a slightly odd method; mabye a better general method?
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
		return getNearestIslandLocalX(location) + "x" + 
				getNearestIslandLocalZ(location) + "z";
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
			return IslandInfo.readFromDisk(getNearestIslandName(location) + ".nbt");
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
	
	/**
	 * Checks if the specified location is on that player's island.
	 * 
	 * @param location
	 * @param player
	 * @return
	 */
	public static boolean locationIsOnPlayerIsland(Location location, String player) {
		if (!uSkyBlock.getInstance().hasIsland(player)) {
			return false;
			//TODO this might not be the best response to no island.
		}
		
		return locationsShareIslands(location, 
				uSkyBlock.getInstance().getPlayerIsland(player));
	}
	
	/**
	 * Checks if all of the specified locations share islands.
	 * 
	 * @param locations
	 * @return
	 */
	public static boolean locationsShareIslands(Location... locations) {
		if (locations.length == 0) { return true; }
		
		int x = getNearestIslandLocalX(locations[0]);
		int z = getNearestIslandLocalZ(locations[0]);
		
		for (Location location : locations) {
			if (getNearestIslandLocalX(location) != x) {
				return false;
			}
			if (getNearestIslandLocalZ(location) != z) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Checks if all the specified players are members of the same island.
	 * 
	 * @param players
	 * @return
	 */
	public static boolean playersShareIslands(String... players) {
		if (players.length == 0) { return true; }
		
		Location mainLoc = uSkyBlock.getInstance().getPlayerIsland(players[0]);
		
		for (String player : players) {
			Location playerLoc = uSkyBlock.getInstance().getPlayerIsland(player);
			
			if (!uSkyBlock.getInstance().hasIsland(player)) {
				return false;
			}
			
			//Checking x and z specifically rather than using .equals() because
			//I can't trust the y value to be the same.
			if (mainLoc.getBlockX() != playerLoc.getBlockX() ||
					mainLoc.getBlockZ() != playerLoc.getBlockZ()) {
				return false;
			}
		}
		
		return false;
	}
}
