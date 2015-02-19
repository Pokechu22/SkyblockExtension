package pokechu22.plugins.SkyblockExtension;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import pokechu22.plugins.SkyblockExtension.errorhandling.ConfigurationErrorReport;
import pokechu22.plugins.SkyblockExtension.errorhandling.CrashReport;
import pokechu22.plugins.SkyblockExtension.errorhandling.ErrorHandler;
import pokechu22.plugins.SkyblockExtension.errorhandling.LoginErrorBroadcaster;
import pokechu22.plugins.SkyblockExtension.hooks.CommandIsland;
import pokechu22.plugins.SkyblockExtension.util.blockvalue.BlockValuation;

/**
 * Utility class for dealing with bukkit configurations.
 * 
 * @author Pokechu22
 *
 */
public class Config {
	/**
	 * Loads the configuration.
	 */
	@SuppressWarnings("unchecked")
	static void loadConfig() {
		getLogger().info("Loading configuration.");
		
		try {
			//Sigh, java provides no good way to do this...
			List<CrashReport> errors = (List<CrashReport>) getCrashesConfig()
					.getList("CrashReports");
			if (errors == null) {
				getLogger().warning("Previous error messages was null.  If this is not the " +
						"first time running this version of the plugin, they may have been " + 
						"corrupted.  ");
				errors = new ArrayList<CrashReport>();
			}
			ErrorHandler.errors = errors;
		} catch (ClassCastException e) {
			
			getLogger().severe("Failed to load previous error messages.  ");
			List<CrashReport> errors  = new ArrayList<CrashReport>();
			errors.add(new ConfigurationErrorReport(e, "CrashReports", "CrashesConfig", false));
			ErrorHandler.errors.addAll(errors);
		}
		
		ErrorHandler.broadcastOnError = getDefaultConfig()
				.getBoolean("errorHandling.broadcastOnError", true);
		LoginErrorBroadcaster.broadcastOnLogin = getDefaultConfig()
				.getBoolean("errorHandling.notifyOfExistingOnLogin", false);
		CommandIsland.enableHelp2 = getDefaultConfig().getBoolean(
				"uSkyBlockOverrides.islandCommand.enableHelp2", true);
		CommandIsland.allowIgnoreCase = getDefaultConfig().getBoolean(
				"uSkyBlockOverrides.islandCommand.allowIgnoreCase", true);
		CommandIsland.useCustomBlockValues = getDefaultConfig().getBoolean(
				"uSkyBlockOverrides.islandCommand.useCustomBlockValues", true);
		CommandIsland.membersCanSendHome = getDefaultConfig().getBoolean(
				"sendHome.allowMembersToSendHome", true);
		CommandIsland.useSpawnCommandForSpawn = getDefaultConfig().getBoolean(
				"sendHome.useSpawnCommandForSpawn", true);
		
		WitherWarner.enabled = getDefaultConfig().getBoolean(
				"witherWarning.enabled");
		WitherWarner.warningText = getDefaultConfig().getString(
				"witherWarning.warningText")
				//Escaping.
				.replace("\\&", "\uFFFF").replace("&", "§").replace("\uFFFF", "&");
		WitherWarner.optOutText = getDefaultConfig().getString(
				"witherWarning.optOutText")
				//Escaping.
				.replace("\\&", "\uFFFF").replace("&", "§").replace("\uFFFF", "&");
		
		SlabPlacementListener.enabled = getDefaultConfig().getBoolean("enableSlabOverrides");
		
		getBlockValuesConfig().set("Test", new BlockValuation());
		try {
			getBlockValuesConfig().save(blockValuesConfigFile);
		} catch (IOException e) {
			throw new RuntimeException();
		}
		
		getLogger().info("Configuration loaded!");
	}
	
	/**
	 * Saves the configuration.
	 */
	static void saveConfig() {
		getLogger().info("Saving configuration.");
		
		getCrashesConfig().set("CrashReports", ErrorHandler.errors);
		getLogger().config("Saved CrashReports.");
		
		getLogger().info("Configuration saved!");
		
		SkyblockExtension.inst().saveAllConfigs();
	}
	
	/**
	 * Convenience method: Gets the default configuration.
	 *
	 * @return The configuration.
	 */
	//@SuppressWarnings("unused")
	private static Configuration getDefaultConfig() {
		return SkyblockExtension.inst().getConfig();
	}
	
	/**
	 * Convenience method: Gets the crashes configuration.
	 *
	 * @return The configuration.
	 */
	private static Configuration getCrashesConfig() {
		return SkyblockExtension.inst().getCrashesConfig();
	}
	
	/**
	 * Convenience method: Gets the logger.
	 * 
	 * @return The logger.
	 */
	private static Logger getLogger() {
		return SkyblockExtension.inst().getLogger();
	}
	
	/**
	 * Custom configuration for block values.
	 */
	protected static FileConfiguration blockValuesConfig = null;
	/**
	 * And the file associated with {@linkplain #blockValuesConfig it}.
	 */
	protected static File blockValuesConfigFile = null;
	
	protected static void reloadBlockValuesConfig() {
		if (blockValuesConfigFile == null) {
			blockValuesConfigFile = new File(SkyblockExtension.getRealDataFolder(), "block_values.yml");
		}
		blockValuesConfig = YamlConfiguration
				.loadConfiguration(blockValuesConfigFile);

		// Look for defaults in the jar
		Reader defConfigStream;
		try {
			defConfigStream = new InputStreamReader(
					SkyblockExtension.inst()
					.getResource("block_values.yml"), "UTF8");
		} catch (UnsupportedEncodingException e) { 
			//Probably shouldn't happen...
			SkyblockExtension.inst().getLogger()
					.severe("Failed to create input stream reader.");
			SkyblockExtension.inst()
					.getLogger().log(Level.SEVERE, "Exception: ", e);
			return;
		}
		
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration
					.loadConfiguration(defConfigStream);
			blockValuesConfig.setDefaults(defConfig);
		}
	}

	/**
	 * Gets the BlockValues config.
	 * @return the BlockValues config.
	 */
	protected static FileConfiguration getBlockValuesConfig() {
		if (blockValuesConfig == null) {
			reloadBlockValuesConfig();
		}
		return blockValuesConfig;
	}
}
