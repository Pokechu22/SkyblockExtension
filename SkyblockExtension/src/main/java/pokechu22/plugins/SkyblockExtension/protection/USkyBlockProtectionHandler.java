package pokechu22.plugins.SkyblockExtension.protection;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import us.talabrek.ultimateskyblock.PlayerInfo;
import us.talabrek.ultimateskyblock.Settings;
import us.talabrek.ultimateskyblock.uSkyBlock;

/**
 * Checks protection using built-in uSkyBlock protection.
 * 
 * TODO: Implement.
 * 
 * @author Pokechu22
 *
 */
public class USkyBlockProtectionHandler extends ProtectionHandler {

	/**
	 * Is the area protected?  
	 * 
	 * Based off of 
	 * {@linkplain us.talabrek.ultimateskyblock.uSkyBlock#playerIsOnIsland(Player)}
	 * 
	 * @param location
	 * @param player
	 * @return
	 */
	@Override
	public boolean isProtected(Location location, Player player) {
		HashMap<String, PlayerInfo> players = uSkyBlock.getInstance().getActivePlayers();
		if (!players.containsKey(player.getName())) {
			return true;
		}
		
		PlayerInfo playerInfo = players.get(player.getName());
		Location islandLocation = null;
		
		if (playerInfo.getHasIsland()) {
			islandLocation = playerInfo.getIslandLocation();
		} else if (playerInfo.getHasParty()) {
			islandLocation = playerInfo.getPartyIslandLocation();
		}
		
		if (islandLocation == null) {
			return true;
		}
		//NOTE: This uses actual position rather than player position.
		if (location.getX() > islandLocation.getX()
				- Settings.island_protectionRange / 2
				&& location.getX() < islandLocation.getX()
						+ Settings.island_protectionRange / 2
				&& location.getZ() > islandLocation.getZ()
						- Settings.island_protectionRange / 2
				&& location.getZ() < islandLocation.getZ()
						+ Settings.island_protectionRange / 2) {
			return false;
		}
		
		return true;
	}

}
