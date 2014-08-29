package pokechu22.plugins.SkyblockExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.configuration.Configuration;

import pokechu22.plugins.SkyblockExtension.commands.CommandIslandProtection;
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
		CommandIslandProtection.tieredValues = (Map<String, IslandProtectionDataSet>) getDefaultConfig().get("testdefault", IslandProtectionDataSetFactory.getDefaultValues());
		
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
		
		getDefaultConfig().set("testdefault", CommandIslandProtection.tieredValues);
		
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
