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
			List<CrashReport> errors = (List<CrashReport>) getConfig().getList("CrashReports");
			if (errors == null) {
				getLogger().severe("Previous error messages was null.  If this is not the " +
						"first time running this version of the plugin, they may have been " + 
						"corrupted.  ");
				errors = new ArrayList<CrashReport>();
			}
			ErrorHandler.errors = errors;
		} catch (ClassCastException e) {
			
			getLogger().severe("Failed to load previous error messages.  ");
			List<CrashReport> errors  = new ArrayList<CrashReport>();
			errors.add(new CrashReport(e, null, null, null, null)); //TODO
			ErrorHandler.errors = errors;
		}
		
		getLogger().info("Configuration loaded!");
	}
	
	/**
	 * Saves the configuration.
	 */
	static void saveConfig() {
		getLogger().info("Saving configuration.");
		
		getConfig().set("CrashReports", ErrorHandler.errors);
		getLogger().config("Saved CrashReports.");
		
		getLogger().info("Configuration saved!");
		
		SkyblockExtension.inst().saveConfig();
	}
	
	/**
	 * Convenience method: Gets the configuration.
	 *
	 * @return The configuration.
	 */
	private static Configuration getConfig() {
		return SkyblockExtension.inst().getConfig();
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
