package pokechu22.plugins.SkyblockExtension.protection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import pokechu22.plugins.SkyblockExtension.SkyblockExtension;
import pokechu22.plugins.SkyblockExtension.protection.IslandInfo.GuestInfo;
import pokechu22.plugins.SkyblockExtension.protection.IslandInfo.MemberInfo;
import pokechu22.plugins.SkyblockExtension.util.IslandUtils;
import us.talabrek.ultimateskyblock.uSkyBlock;

/**
 * Contains a cache of all existing IslandInfos.
 * @author Pokechu22
 *
 */
public class IslandInfoCache {
	/**
	 * Contains the location of the island.
	 * @author Pokechu22
	 *
	 */
	static class IslandLocation {
		private int x;
		private int z;
		
		/**
		 * Creates an IslandLocation at those islandX and islandZ coords.
		 * 
		 * @param x
		 * @param z
		 */
		public IslandLocation(int x, int z) {
			this.x = x;
			this.z = z;
		}
		
		/**
		 * Creates an IslandLocation from that LocationString (formated <code>#x#z</code>).
		 * 
		 * @param x
		 * @param z
		 */
		public IslandLocation(String locationString) {
			//Validate the format.
			if (!(locationString.contains("x") && 
					locationString.contains("z"))) {
				throw new IllegalArgumentException("Failed to parse " +
						"location string " + locationString + ".\n" +
						"It must be in the form of {num}x{num}z.\n" +
						"Could not find 'x' and/or 'z'.");
			}
			
			if (locationString.indexOf("x") > locationString.indexOf("z")) {
				throw new IllegalArgumentException("Failed to parse " +
						"location string " + locationString + ".\n" +
						"It must be in the form of {num}x{num}z.\n" +
						"Z came after the x.");
			}
			
			if (locationString.lastIndexOf("x") != 
					locationString.indexOf("x")) {
				throw new IllegalArgumentException("Failed to parse " +
						"location string " + locationString + ".\n" +
						"It must be in the form of {num}x{num}z.\n" +
						"There are multiples occurences of 'x'.");
			}
			
			if (locationString.lastIndexOf("z") != 
					locationString.indexOf("z")) {
				throw new IllegalArgumentException("Failed to parse " +
						"location string " + locationString + ".\n" +
						"It must be in the form of {num}x{num}z.\n" +
						"There are multiples occurences of 'z'.");
			}
			
			if (!locationString.endsWith("z")) {
				throw new IllegalArgumentException("Failed to parse " +
						"location string " + locationString + ".\n" +
						"It must be in the form of {num}x{num}z.\n" +
						"There is text after the final 'z'.");
			}
			
			locationString = locationString.replace("z", "");
			//Parse the values.
			String[] values = locationString.split("x");
			
			try {
				this.x = Integer.parseInt(values[0]);
				this.z = Integer.parseInt(values[1]);
			} catch (Exception e) {
				throw new IllegalArgumentException("Failed to parse " +
						"location string " + locationString + ".\n" +
						"It must be in the form of {num}x{num}z.", e);
			}
		}
		
		/**
		 * Creates an IslandLocation located based off of the IslandCoords from there.
		 * 
		 * @param x
		 * @param z
		 */
		public IslandLocation(Location islandLocation) {
			this(IslandUtils.getNearestIslandName(islandLocation));
		}
		
		/**
		 * Creates an IslandLocation based off of the location of that {@link IslandInfo}.
		 * 
		 * @param x
		 * @param z
		 */
		public IslandLocation(IslandInfo info) {
			this(info.getXID(), info.getZID());
		}
		
		/**
		 * Gets the IslandInfo used for a specific player's island.
		 * 
		 * @throws RuntimeException when a player has no island.
		 * 
		 * @return
		 */
		public static IslandLocation IslandInfoForPlayer(String player) {
			Location location = IslandUtils.getPlayerIslandLocation(player);
			if (location == null) {
				throw new RuntimeException("§cPlayer " + player + " does not appear to have an island.");
			} else {
				return new IslandLocation(location);
			}
		}
		
		/**
		 * Gets the IslandInfo used for a specific player's island.
		 * 
		 * @throws RuntimeException when a player has no island.
		 * 
		 * @return
		 */
		public static IslandLocation IslandInfoForPlayer(Player player) {
			return IslandInfoForPlayer(player.getName());
		}

		@Override
		public int hashCode() {
			return x * (z << 16);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof IslandLocation)) {
				return false;
			}
			IslandLocation other = (IslandLocation) obj;
			if (x != other.x) {
				return false;
			}
			if (z != other.z) {
				return false;
			}
			return true;
		}
		
		@Override
		public String toString() {
			return x + "x" + z + "z";
		}
	}
	
	/**
	 * Controls what is returned when a non-existent island is found.
	 */
	public static interface NoIslandFoundBehavior {
		/**
		 * Performs the action for this behavior at specified location.
		 * @param location The location that was attempted to have an info retrieved for.
		 * @return
		 */
		public IslandInfo performAction(IslandLocation location) throws Exception;
		
		/**
		 * Performs the action for this behavior at specified location.
		 * @param location The location that was attempted to have an info retrieved for.
		 * @param cause The throwable that caused the island not to be found.
		 * @return
		 */
		public IslandInfo performAction(IslandLocation location, Throwable cause) throws Exception;
		
		/**
		 * Performs the action for this behavior for the specified player.
		 * @param playerName the player who's island was being retrieved.
		 * @return
		 */
		public IslandInfo performAction(String playerName) throws Exception;
		
		/**
		 * Performs the action for this behavior for the specified player.
		 * @param playerName the player who's island was being retrieved.
		 * @param cause The throwable that caused the island not to be found.
		 * @return
		 */
		public IslandInfo performAction(String playerName, Throwable cause) throws Exception;
		
		/**
		 * Performs the action for this behavior.
		 * @return
		 */
		public IslandInfo performAction() throws Exception;
		
		/**
		 * Performs the action for this behavior.
		 * @param cause The throwable that caused the island not to be found.
		 * @return
		 */
		public IslandInfo performAction(Throwable cause) throws Exception;
	}
	
	/**
	 * Controls what is returned when a non-existent island is found.
	 * 
	 * This enum contains some default implementations of {@link NoIslandFoundBehavior}.
	 */
	public static enum NoIslandFoundBehaviors implements NoIslandFoundBehavior {
		/**
		 * Returns an unprotected area IslandInfo.
		 */
		RETURN_UNPROTECTED {

			@Override
			public IslandInfo performAction(IslandLocation location)
					throws Exception {
				return IslandInfo.getUnprotectedIslandInfo();
			}
			
			@Override
			public IslandInfo performAction(IslandLocation location, Throwable cause)
					throws Exception {
				return IslandInfo.getUnprotectedIslandInfo();
			}

			@Override
			public IslandInfo performAction(String playerName) throws Exception {
				return IslandInfo.getUnprotectedIslandInfo();
			}
			
			@Override
			public IslandInfo performAction(String playerName, Throwable cause) throws Exception {
				return IslandInfo.getUnprotectedIslandInfo();
			}

			@Override
			public IslandInfo performAction() throws Exception {
				return IslandInfo.getUnprotectedIslandInfo();
			}
			
			@Override
			public IslandInfo performAction(Throwable cause) throws Exception {
				return IslandInfo.getUnprotectedIslandInfo();
			}
			
		},
		
		/**
		 * Throws an exception for trying to get a nonexistant island.
		 */
		THROW_EXCEPTION {

			@Override
			public IslandInfo performAction(IslandLocation location)
					throws Exception {
				throw new IllegalArgumentException("There is no island at " + location);
			}
			
			@Override
			public IslandInfo performAction(IslandLocation location, Throwable cause)
					throws Exception {
				throw new IllegalArgumentException("There is no island at " + location, cause);
			}

			@Override
			public IslandInfo performAction(String playerName) throws Exception {
				throw new IllegalArgumentException(playerName + " has no island!");
			}
			
			@Override
			public IslandInfo performAction(String playerName, Throwable cause) throws Exception {
				throw new IllegalArgumentException(playerName + " has no island!", cause);
			}

			@Override
			public IslandInfo performAction() throws Exception {
				throw new IllegalArgumentException("There is no island at that location.");
			}
			
			@Override
			public IslandInfo performAction(Throwable cause) throws Exception {
				throw new IllegalArgumentException("There is no island at that location.", cause);
			}
		},
		
		/**
		 * Return null when no island is found.
		 */
		RETURN_NULL {

			@Override
			public IslandInfo performAction(IslandLocation location)
					throws Exception {
				return null;
			}
			
			@Override
			public IslandInfo performAction(IslandLocation location, Throwable cause)
					throws Exception {
				return null;
			}

			@Override
			public IslandInfo performAction(String playerName) throws Exception {
				return null;
			}
			
			@Override
			public IslandInfo performAction(String playerName, Throwable cause) throws Exception {
				return null;
			}

			@Override
			public IslandInfo performAction() throws Exception {
				return null;
			}
			
			@Override
			public IslandInfo performAction(Throwable cause) throws Exception {
				return null;
			}
		};
	}
	
	/**
	 * Private constructor - don't call this!
	 */
	private IslandInfoCache() {
		throw new AssertionError("Should never be called");
	}
	
	private static Map<IslandLocation, IslandInfo> cache = new HashMap<>();
	
	/**
	 * Trims the cache; can be run asynchronously.
	 * 
	 * @author Pokechu22
	 */
	private static class CacheTrimmer implements Runnable {
		/**
		 * Currently in use (and therefore inaccessable)?
		 */
		private static volatile boolean inUse = false;
		
		/**
		 * The list of players in the uSkyBlock world.
		 */
		private Set<UUID> playersOnWorld = new HashSet<>();
		
		public CacheTrimmer() {
			initPlayersList();
		}
		
		/**
		 * Creates the list of players that are to be checked.
		 */
		@SuppressWarnings("unchecked")
		private void initPlayersList() {
			List<Player> allPlayers;
			
			//TODO Ugly code to try and deal with my development environment using two types...
			Object temp = Bukkit.getOnlinePlayers();
			if (temp instanceof Player[]) {
				allPlayers = Arrays.asList((Player[])temp);
			} else if (temp instanceof Collection<?>) {
				allPlayers = new ArrayList<Player>((Collection<Player>)temp);
			} else {
				throw new Error("Failed to parse Bukkit.getOnlinePlayers(): " +
						"returned unknown type " + temp.getClass());
			}
			for (Player player : allPlayers) {
				if (player.getWorld().equals(uSkyBlock.getSkyBlockWorld())) {
					playersOnWorld.add(player.getUniqueId());
				}
			}
		}
		
		/**
		 * Is the item currently in use? 
		 * @param item
		 * @return Whether the item is in use.  If false, it can be removed.
		 */
		private boolean itemInUse(IslandInfo item) {
			if (playersOnWorld.contains(item.ownerInfo.playerUUID)) {
				return true;
			}
			for (MemberInfo info : item.members) {
				if (playersOnWorld.contains(info.playerUUID)) {
					return true;
				}
			}
			for (GuestInfo info : item.guests) {
				if (playersOnWorld.contains(info.playerUUID)) {
					return true;
				}
			}
			
			return false;
		}
		
		@Override
		public void run() {
			if (inUse) {
				throw new IllegalStateException("Already in use!");
			}
			inUse = true;
			
			try {
				Set<IslandInfoCache.IslandLocation> toRemove = new HashSet<>();
				for (Map.Entry<IslandInfoCache.IslandLocation, IslandInfo> entry : IslandInfoCache.cache.entrySet()) {
					if (!itemInUse(entry.getValue())) {
						toRemove.add(entry.getKey());
					}
				}
				
				for (IslandLocation location : toRemove) {
					cache.remove(location);
				}
			} finally {
				inUse = false;
			}
		}
		
	}
	
	/**
	 * Trims the cache of IslandInfo's by removing offline players.<br>
	 * Trimmed island infos are written to disk.<br>
	 * <i>NOTE: Cache trimming is performed asynchronously.</i>
	 */
	public static void trimCache() {
		CacheTrimmer trimmer = new CacheTrimmer();
		Bukkit.getScheduler().runTaskAsynchronously(SkyblockExtension.inst(), trimmer);
	}
	
	/**
	 * Dumps the entire cache of island infos, removing all of them.<br>
	 * Dumped island infos are written to disk.<br>
	 * <i>NOTE: Cache dumping is performed asynchronously.</i>
	 */
	public static void dumpCache() {
		//Remove all of them.
		//It's a duplicate set because otherwise running through it would
		//lead to a ConcurentModificationException.
		Set<Map.Entry<IslandInfoCache.IslandLocation, IslandInfo>> toRemove
				= new HashSet<>(IslandInfoCache.cache.entrySet());
		
		for (Map.Entry<IslandInfoCache.IslandLocation, IslandInfo> entry
				: toRemove) {
			try {
				cache.remove(entry.getKey());
				entry.getValue().saveToDisk();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// TODO This will need an error report.
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Gets the IslandInfo used that the specified location and adds
	 * it to the cache.
	 * 
	 * @param location
	 * @return
	 * @throws Exception 
	 */
	public static IslandInfo getLocationIslandInfo(Location location,
			NoIslandFoundBehavior noIslandFoundBehavior) throws Exception {
		IslandLocation loc = new IslandLocation(location);
		return getIslandInfo(loc, noIslandFoundBehavior);
	}
	
	/**
	 * Gets the IslandInfo for the island with the specified ID, and adds
	 * it to the cache.
	 * 
	 * @param location
	 * @return
	 * @throws Exception 
	 */
	public static IslandInfo getLocationIslandInfo(String islandID,
			NoIslandFoundBehavior noIslandFoundBehavior) throws Exception {
		IslandLocation loc = new IslandLocation(islandID);
		return getIslandInfo(loc, noIslandFoundBehavior);
	}
	
	/**
	 * Gets the IslandInfo used that those specific island ID coords, and
	 * adds it to the cache.
	 * 
	 * @param location
	 * @return
	 * @throws Exception 
	 */
	public static IslandInfo getLocationIslandInfo(int islandX, int islandY,
			NoIslandFoundBehavior noIslandFoundBehavior) throws Exception {
		IslandLocation loc = new IslandLocation(islandX, islandY);
		return getIslandInfo(loc, noIslandFoundBehavior);
	}
	
	/**
	 * Gets the island info for the specified player. 
	 * 
	 * @param player
	 * @return
	 * @throws Exception 
	 */
	public static IslandInfo getPlayerIslandInfo(Player player,
			NoIslandFoundBehavior noIslandFoundBehavior) throws Exception {
		try {
			IslandLocation loc = IslandLocation.IslandInfoForPlayer(player);
			return getIslandInfo(loc, noIslandFoundBehavior);
		} catch (Exception e) {
			return noIslandFoundBehavior.performAction(player != null ? player.getName() : ""
				, e);
		}
	}
	
	/**
	 * Gets the island info found for the specified location or player
	 * name.  The purpose of this is for use in a command, as it
	 * takes either a '1x1z' style number or a player's name.
	 * 
	 * @param playerOrLocation
	 * @return
	 * @throws Exception 
	 */
	public static IslandInfo getCommandIslandInfo(String playerOrLocation,
			NoIslandFoundBehavior noIslandFoundBehavior) throws Exception {
		IslandLocation location;
		try {
			//Try to parse it as an actual location.
			location = new IslandLocation(playerOrLocation);
		} catch (IllegalArgumentException e) {
			//If the format for the location was invalid, try parsing
			//it as a player's name.
			try {
				location = IslandLocation.IslandInfoForPlayer(
						playerOrLocation);
			} catch (Exception e_) {
				return noIslandFoundBehavior.performAction(playerOrLocation, e_);
			}
		}
		
		return getIslandInfo(location, noIslandFoundBehavior);
	}
	
	/**
	 * Gets the island info for the specified player. 
	 * 
	 * @param player
	 * @return
	 * @throws Exception 
	 */
	public static IslandInfo getPlayerIslandInfo(String player,
			NoIslandFoundBehavior noIslandFoundBehavior) throws Exception {
		try {
			IslandLocation loc = IslandLocation.IslandInfoForPlayer(player);
			return getIslandInfo(loc, noIslandFoundBehavior);
		} catch (Exception e) {
			return noIslandFoundBehavior.performAction(player, e);
		}
	}
	
	/**
	 * Gets the island info located at that location, and adds it to 
	 * the cache. 
	 * @param location
	 * @param noIslandFoundBehavior TODO
	 * @return
	 * @throws Exception 
	 */
	private static IslandInfo getIslandInfo(IslandLocation location, 
			NoIslandFoundBehavior noIslandFoundBehavior) throws Exception {
		if (cache.containsKey(location)) {
			return cache.get(location);
		}
		
		IslandInfo info = null;
		
		try {
			info = IslandInfo.readFromDisk(location.x, location.z);
			cache.put(location, info);
		} catch (Exception e) {
			return noIslandFoundBehavior.performAction(location, e);
		}
		return info;
	}
}
