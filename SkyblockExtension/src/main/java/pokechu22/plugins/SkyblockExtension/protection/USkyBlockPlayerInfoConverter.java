package pokechu22.plugins.SkyblockExtension.protection;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	 * A stream to log to.  If null, no logging occurs.
	 */
	private PrintStream logStream;
	
	/**
	 * A logger to log to.  If null, no logging occurs.
	 */
	private Logger logger;
	
	/**
	 * Creates a converter that logs to the default SBE logger.
	 * <br>
	 * <i>(If you wish to not log at all, you can do so by calling
	 * {@linkplain #USkyBlockPlayerInfoConverter(Logger) one}
	 * {@linkplain #USkyBlockPlayerInfoConverter(PrintStream) of}
	 * {@linkplain #USkyBlockPlayerInfoConverter(PrintStream, Logger) the}
	 * other constructors with <code>null</code> as the argument(s).)</i>
	 */
	public USkyBlockPlayerInfoConverter() {
		this(SkyblockExtension.inst().getLogger());
	}
	
	/**
	 * Creates a converter with logging to a stream.
	 * <br>
	 * @param stream The stream to log to.  If null, no logging occurs.
	 */
	public USkyBlockPlayerInfoConverter(PrintStream stream) {
		this.logStream = stream;
	}
	
	/**
	 * Creates a converter with logging to a {@link Logger}.
	 * <br>
	 * @param logger The Logger to log to.  If null, no logging occurs.
	 */
	public USkyBlockPlayerInfoConverter(Logger logger) {
		this.logger = logger;
	}
	
	/**
	 * Creates a converter with logging to both a stream and a 
	 * {@link Logger}.
	 * <br>
	 * @param stream The stream to log to.  If null, no logging occurs.
	 * @param logger The Logger to log to.  If null, no logging occurs.
	 */
	public USkyBlockPlayerInfoConverter(PrintStream stream, Logger logger) {
		this.logStream = stream;
		this.logger = logger;
	}
	
	/**
	 * Prepares and schedules a converter which logs to the default 
	 * SBE logger, and starts in 4 ticks.
	 */
	public static void start() {
		Bukkit.getScheduler().runTaskLater(uSkyBlock.getInstance(), 
				new USkyBlockPlayerInfoConverter(), 4L);
	}
	
	/**
	 * Prepares and schedules a converter with logging, which starts in 
	 * 4 ticks
	 */
	public static void start(PrintStream stream) {
		Bukkit.getScheduler().runTaskLater(uSkyBlock.getInstance(), 
				new USkyBlockPlayerInfoConverter(stream), 4L);
	}
	
	@Override
	public void run() {
		if (logger != null) {
			logger.info("Started porting USkyBlockPlayerInfos.");
		}
		if (logStream != null) {
			logStream.println("Started porting USkyBlockPlayerInfos.");
		}
		
		File playerDir = uSkyBlock.getInstance().directoryPlayers;

		List<String> players = 
				new ArrayList<String>(Arrays.asList(playerDir.list()));
		List<String> ignored = new ArrayList<String>();
		
		for (String name : players) {
			if (ignored.contains(name)) {
				continue;
			}
			
			if (logger != null) {
				logger.fine("Porting " + name + "'s info.");
			}
			if (logStream != null) {
				logStream.println("Porting " + name + "'s info.");
			}
			
			PlayerInfo info = uSkyBlock.getInstance().readPlayerFile(name);
			
			ignored.remove(info.getPartyLeader());
			ignored.removeAll(info.getMembers());
			
			if (info.getHasIsland()) {
				//Save the value to disk.
				try {
					IslandInfo.convertFromPlayerInfo(info).saveToDisk();
				} catch (Exception e) {
					e.printStackTrace();
					if (logger != null) {
						logger.log(Level.SEVERE, "Error while porting " +
								name + "'s info:  ", e);
					}
					if (logStream != null) {
						logStream.println("Error while porting " +
								name + "'s info:  " + e.toString());
						e.printStackTrace(logStream);
					}
					continue;
				}
			}
			
			if (logger != null) {
				logger.fine(name + "'s info was successfully ported!");
			}
			if (logStream != null) {
				logStream.println(name + "'s info was sucessfully ported!");
			}
		}
		
		if (logger != null) {
			logger.info("Finished porting USkyBlockPlayerInfos.");
		}
		if (logStream != null) {
			logStream.println("Finished porting USkyBlockPlayerInfos.");
		}
	}
}
