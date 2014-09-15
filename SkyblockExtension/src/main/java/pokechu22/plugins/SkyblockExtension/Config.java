package pokechu22.plugins.SkyblockExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import pokechu22.plugins.SkyblockExtension.commands.CommandIslandProtection;
import pokechu22.plugins.SkyblockExtension.errorhandling.ConfigurationErrorReport;
import pokechu22.plugins.SkyblockExtension.errorhandling.CrashReport;
import pokechu22.plugins.SkyblockExtension.errorhandling.ErrorHandler;
import pokechu22.plugins.SkyblockExtension.errorhandling.LoginErrorBroadcaster;
import pokechu22.plugins.SkyblockExtension.protection.IslandProtectionDataSet;
import pokechu22.plugins.SkyblockExtension.protection.IslandProtectionDataSetFactory;

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
		
		//Test code.
		//Set it to the default values; if something missing is found, skip it.
		CommandIslandProtection.tieredValues = IslandProtectionDataSetFactory.getDefaultValues();
		try {
			ConfigurationSection tempCfg = getDefaultConfig().getConfigurationSection("testdefault");
			if (tempCfg != null) {
				Map<String, Object> tempValue = tempCfg.getValues(true);
				for (Map.Entry<String, Object> entry : tempValue.entrySet()) {
					CommandIslandProtection.tieredValues.put(entry.getKey(), (IslandProtectionDataSet) entry.getValue());
				}
			}
		} catch (ClassCastException e) {
			e.printStackTrace(); //TODO
		}
		
		ErrorHandler.broadcastOnError = getDefaultConfig()
				.getBoolean("errorHandling.broadcastOnError", true);
		LoginErrorBroadcaster.broadcastOnLogin = getDefaultConfig()
				.getBoolean("errorHandling.notifyOfExistingOnLogin", false);
		
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
		
		getDefaultConfig().set("errorHandling.broadcastOnError", 
				ErrorHandler.broadcastOnError);
		getDefaultConfig().set("errorHandling.notifyOfExistingOnLogin", 
				LoginErrorBroadcaster.broadcastOnLogin);
		
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
}
