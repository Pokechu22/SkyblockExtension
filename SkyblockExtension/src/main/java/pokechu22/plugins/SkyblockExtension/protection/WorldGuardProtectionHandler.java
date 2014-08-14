package pokechu22.plugins.SkyblockExtension.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.BukkitPlayer;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;

public class WorldGuardProtectionHandler extends ProtectionHandler {

	/**
	 * Is the area protected?
	 * 
	 * @param location The location to test.
	 * @param player The player to use.
	 * @return
	 */
	@Override
	public boolean isProtected(Location location, Player player) {
		RegionManager regionManager = WGBukkit.getRegionManager(player.getWorld());
		
		ApplicableRegionSet regions = regionManager.getApplicableRegions(location);
		
		return regions.isMemberOfAll(new BukkitPlayer(WGBukkit.getPlugin(), player));
	}

}
