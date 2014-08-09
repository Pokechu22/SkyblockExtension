package pokechu22.plugins.SkyblockExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.configuration.Configuration;

/**
 * Utility class for dealing with bukkit configurations.
 * 
 * TODO Use this.  Currently not needed.
 * 
 * @author Pokechu22
 *
 */
public class Config {
	/**
	 * Loads the configuration.
	 */
	static void loadConfig() {
		getLogger().info("Loading configuration.");
		
		try {
			//Sigh, java provides no good way to do this...
			@SuppressWarnings("unchecked")
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
			//TODO: The below method is the exact reason for the interface.
			errors.add(new ConfigurationErrorReport(e, "CrashReports", "CrashesConfig", false));  
			ErrorHandler.errors = errors;
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
	@SuppressWarnings("unused")
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
