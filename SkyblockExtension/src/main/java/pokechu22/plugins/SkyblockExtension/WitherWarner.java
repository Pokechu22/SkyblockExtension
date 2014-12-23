package pokechu22.plugins.SkyblockExtension;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Warns players who are making a wither that withers are actually 4 blocks high!
 * 
 * @author Pokechu22
 *
 */
public class WitherWarner implements Listener {
	/**
	 * Whether or not the warning is enabled.
	 */
	public static boolean enabled = true;
	/**
	 * The main warning message, displayed first.
	 */
	public static String warningText = "§fHey!  Be careful!  While the model to make a wither is 3 by 3, the actual wither is 1 by 4!  If you're trying to cage it, it's possible for it to escape unless your cage is 4 high!";
	/**
	 * The "Opt out" message, displayed second.
	 */
	public static String optOutText = "§7§oTo disable this message, do '§r§7/pokechu22 WitherWarning off§7§o'.";
	
	public static List<UUID> optedOutPlayers = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	public static void load() {
		saveDefaultOptOutConfig();
		
		List<String> temp = (List<String>) getOptOutConfig().getList("optedOut", new ArrayList<String>());
		
		optedOutPlayers = new ArrayList<UUID>();
		
		for (String s : temp) {
			optedOutPlayers.add(UUID.fromString(s));
		}
	}
	
	public static void save() {
		List<String> temp = new ArrayList<String>();
		for (UUID uuid : optedOutPlayers) {
			temp.add(uuid.toString());
		}
		getOptOutConfig().set("optedOut", temp);
		
		saveOptOutConfig();
	}
	
	private static File optOutConfigFile;
	private static FileConfiguration optOutConfig;
	
	private static void reloadOptOutConfig() {
		if (optOutConfigFile == null) {
			optOutConfigFile = new File(SkyblockExtension.inst().getDataFolder(), 
					"witherWarningOptOut.yml");
		}
		optOutConfig = YamlConfiguration.loadConfiguration(optOutConfigFile);

		// Look for defaults in the jar
		Reader defConfigStream;
		try {
			defConfigStream = new InputStreamReader(
					SkyblockExtension.inst().getResource("witherWarningOptOut.yml"), "UTF8");
		} catch (UnsupportedEncodingException e) { //Probably shouldn't happen...
			SkyblockExtension.inst().getLogger().severe("Failed to create input stream reader.");
			SkyblockExtension.inst().getLogger().log(Level.SEVERE, "Exception: ", e);
			return;
		}
		
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration
					.loadConfiguration(defConfigStream);
			optOutConfig.setDefaults(defConfig);
		}
	}

	/**
	 * Gets the optOut config.
	 * @return the optOut config.
	 */
	private static FileConfiguration getOptOutConfig() {
		if (optOutConfig == null) {
			reloadOptOutConfig();
		}
		return optOutConfig;
	}

	/**
	 * Saves the optOut config.
	 */
	private static void saveOptOutConfig() {
		if (optOutConfig == null || optOutConfigFile == null) {
			return;
		}
		try {
			getOptOutConfig().save(optOutConfigFile);
		} catch (IOException ex) {
			SkyblockExtension.inst().getLogger().log(Level.SEVERE,
					"Could not save config to " + optOutConfigFile, ex);
		}
	}

	/**
	 * Saves the default optOut config.
	 */
	private static void saveDefaultOptOutConfig() {
		if (optOutConfigFile == null) {
			optOutConfigFile = new File(SkyblockExtension.inst().getDataFolder(), 
					"witherWarningOptOut.yml");
		}
		if (!optOutConfigFile.exists()) {
			SkyblockExtension.inst().saveResource("witherWarningOptOut.yml", false);
		}
	}
	
	/**
	 * The actual even handler. 
	 * Checks if a player is holding a skull, and informs them if so.
	 * @param e
	 */
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onWitherSkullPlaced(BlockPlaceEvent e) {
		if (enabled) {
			if (e.isCancelled() || !e.canBuild()) {
				return; //If canceled, no point in warning.
			}
			if (e.getItemInHand().getType() == Material.SKULL_ITEM) {
				//I can't figure out how to do this properly.  DV 1 is that of wither skull.
				if (e.getItemInHand().getData().getData() == (byte)1) {
					if (!optedOutPlayers.contains(e.getPlayer().getUniqueId())) {
						e.getPlayer().sendMessage(warningText);
						e.getPlayer().sendMessage(optOutText);
					}
				}
			}
		}
	}
}
