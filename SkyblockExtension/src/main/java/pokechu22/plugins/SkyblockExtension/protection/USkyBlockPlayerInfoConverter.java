package pokechu22.plugins.SkyblockExtension.protection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
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
	/**
	 * Log messages to the player?
	 */
	private boolean hasLogging;
	
	/**
	 * Where to send the logging to.
	 * Most commonly, will be a PlayerPrintStream.
	 */
	private PrintStream logTo;
	
	/**
	 * Creates a converter with no logging.
	 */
	public USkyBlockPlayerInfoConverter() {
		this.hasLogging = false;
	}
	
	/**
	 * Creates a converter with logging.
	 */
	public USkyBlockPlayerInfoConverter(PrintStream stream) {
		this.hasLogging = true;
		this.logTo = stream;
	}
	
	/**
	 * Prepares an schedules a converter with no logging, which starts in 
	 * 4 ticks.
	 */
	public static void start() {
		Bukkit.getScheduler().runTaskLater(uSkyBlock.getInstance(), 
				new USkyBlockPlayerInfoConverter(), 4L);
	}
	
	/**
	 * Prepares an schedules a converter with logging, which starts in 
	 * 4 ticks.
	 */
	public static void start(PrintStream stream) {
		Bukkit.getScheduler().runTaskLater(uSkyBlock.getInstance(), 
				new USkyBlockPlayerInfoConverter(stream), 4L);
	}
	
	@Override
	public void run() {
		SkyblockExtension.inst().getLogger()
				.info("Started porting USkyBlockPlayerInfo's.");
		if (hasLogging) {
			logTo.println("Started porting USkyBlockPlayerInfos.");
		}
		
		File playerDir = uSkyBlock.getInstance().directoryPlayers;

		List<String> players = 
				new ArrayList<String>(Arrays.asList(playerDir.list()));
		List<String> ignored = new ArrayList<String>();
		
		for (String name : players) {
			if (ignored.contains(name)) {
				continue;
			}
			
			if (hasLogging) {
				logTo.println("Porting " + name + "'s info.");
			}
			
			PlayerInfo info = uSkyBlock.getInstance().readPlayerFile(name);
			
			ignored.remove(info.getPartyLeader());
			ignored.removeAll(info.getMembers());
			
			if (info.getHasIsland()) {
				//Save the value to disk.
				try {
					IslandInfo.convertFromPlayerInfo(info).saveToDisk();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					if (hasLogging) {
						logTo.println("Error - " + e.toString());
						e.printStackTrace(logTo);
					}
				} catch (IOException e) {
					e.printStackTrace();
					if (hasLogging) {
						logTo.println("Error - " + e.toString());
						e.printStackTrace(logTo);
					}
				}
			}
			
			if (hasLogging) {
				logTo.println("Ported.");
			}
		}
		
		SkyblockExtension.inst().getLogger()
				.info("Finished porting USkyBlockPlayerInfo's.");
		if (hasLogging) {
			logTo.println("Finished porting USkyBlockPlayerInfos.");
		}
	}
}
