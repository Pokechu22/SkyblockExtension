package pokechu22.plugins.SkyblockExtension.protection;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
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
	private static class IslandLocation {
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
	 * Private constructor - don't call this!
	 */
	private IslandInfoCache() {
		throw new AssertionError("Should never be called");
	}
	
	private static HashMap<IslandLocation, IslandInfo> cache;
	
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
		private void initPlayersList() {
			Player[] allPlayers = Bukkit.getOnlinePlayers();
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
	 * Trims the cache of IslandInfo's by removing offline players.
	 */
	protected static void trimCache() {
		CacheTrimmer trimmer = new CacheTrimmer();
		Bukkit.getScheduler().runTaskAsynchronously(SkyblockExtension.inst(), trimmer);
	}
	
	/**
	 * Gets the IslandInfo used that the specified location and adds
	 * it to the cache.
	 * 
	 * @param location
	 * @return
	 */
	public static IslandInfo getIslandInfo(Location location) {
		IslandLocation loc = new IslandLocation(location);
		return getIslandInfo(loc);
	}
	
	/**
	 * Gets the IslandInfo for the island with the specified ID, and adds
	 * it to the cache.
	 * 
	 * @param location
	 * @return
	 */
	public static IslandInfo getIslandInfo(String islandID) {
		IslandLocation loc = new IslandLocation(islandID);
		return getIslandInfo(loc);
	}
	
	/**
	 * Gets the IslandInfo used that those specific island ID coords, and
	 * adds it to the cache.
	 * 
	 * @param location
	 * @return
	 */
	public static IslandInfo getIslandInfo(int islandX, int islandY) {
		IslandLocation loc = new IslandLocation(islandX, islandY);
		return getIslandInfo(loc);
	}
	
	/**
	 * Gets the island info located at that location, and adds it to 
	 * the cache. 
	 * @param location
	 * @return
	 */
	private static IslandInfo getIslandInfo(IslandLocation location) {
		if (cache.containsKey(location)) {
			return cache.get(location);
		}
		
		IslandInfo info = null;
		
		try {
			info = IslandInfo.readFromDisk(location.x, location.z);
			cache.put(location, info);
		} catch (FileNotFoundException e) {
			return IslandInfo.getUnprotectedIslandInfo();
		} catch (Exception e) {
			return null;
		}
		return info;
	}
	
	/**
	 * Gets the IslandInfo used that the specified location but does not
	 * add it to the cache.
	 * 
	 * @param location
	 * @return
	 */
	public static IslandInfo getIslandInfoNoAddToCache(Location location) {
		IslandLocation loc = new IslandLocation(location);
		return getIslandInfoNoAddToCache(loc);
	}

	/**
	 * Gets the IslandInfo for the island with the specified ID but does not
	 * add it to the cache.
	 * 
	 * @param location
	 * @return
	 */
	public static IslandInfo getIslandInfoNoAddToCache(String islandID) {
		IslandLocation loc = new IslandLocation(islandID);
		return getIslandInfoNoAddToCache(loc);
	}

	/**
	 * Gets the IslandInfo used that those specific island ID coords but
	 * does not add it to the cache.
	 * 
	 * @param location
	 * @return
	 */
	public static IslandInfo getIslandInfoNoAddToCache(int islandX, int islandY) {
		IslandLocation loc = new IslandLocation(islandX, islandY);
		return getIslandInfoNoAddToCache(loc);
	}

	/**
	 * Gets the island info located at that location but does not
	 * add it to the cache.
	 * 
	 * @param location
	 * @return
	 */
	private static IslandInfo getIslandInfoNoAddToCache(IslandLocation location) {
		if (cache.containsKey(location)) {
			return cache.get(location);
		}
		return null; //TODO: Get from file.
	}

	/**
	 * Adds said island info to the cache.
	 * 
	 * @param info
	 */
	private static void addToCache(IslandInfo info) {
		IslandLocation location = new IslandLocation(info);
		cache.put(location, info);
	}
}
