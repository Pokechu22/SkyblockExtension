package pokechu22.plugins.SkyblockExtension.protection;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mockito.asm.tree.TryCatchBlockNode;

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
		
		public IslandLocation(int x, int z) {
			this.x = x;
			this.z = z;
		}
		
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
		
		public IslandLocation(Location islandLocation) {
			this(IslandUtils.getNearestIslandName(islandLocation));
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
}
