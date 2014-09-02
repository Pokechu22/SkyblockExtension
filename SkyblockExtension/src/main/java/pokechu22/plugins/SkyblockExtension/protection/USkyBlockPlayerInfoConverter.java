package pokechu22.plugins.SkyblockExtension.protection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;

import pokechu22.plugins.SkyblockExtension.SkyblockExtension;
import us.talabrek.ultimateskyblock.PlayerInfo;
import us.talabrek.ultimateskyblock.uSkyBlock;

/**
 * Converts USkyBlock's {@link us.talabrek.ultimateskyblock.PlayerInfo 
 * PlayerInfo} (And will do so with 
 * {@link us.talabrek.ultimateskyblock.UUIDPlayerInfo UUIDPlayerInfo} when
 * it is introduced) to the SBE format.
 * 
 * @author Pokechu22
 *
 */
public class USkyBlockPlayerInfoConverter implements Runnable {
	public static void start() {
		Bukkit.getScheduler().runTaskLater(uSkyBlock.getInstance(), 
				new USkyBlockPlayerInfoConverter(), 4L);
	}
	
	@Override
	public void run() {
		SkyblockExtension.inst().getLogger().info("Started porting USkyBlockPlayerInfo's.");
		
		File playerDir = uSkyBlock.getInstance().directoryPlayers;

		List<String> players = Arrays.asList(playerDir.list());
		for (String name : players) {
			PlayerInfo info = uSkyBlock.getInstance().readPlayerFile(name);
			
			players.remove(info.getPartyLeader());
			players.removeAll(info.getMembers());
			
			//Save the value to disk.
			try {
				IslandInfo.convertFromPlayerInfo(info).saveToDisk();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		SkyblockExtension.inst().getLogger().info("Finished porting USkyBlockPlayerInfo's.");
	}
}
