package pokechu22.plugins.SkyblockExtension;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import pokechu22.plugins.SkyblockExtension.commands.*;
import pokechu22.plugins.SkyblockExtension.protection.IslandProtectionDataSet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author Pokechu22
 *
 */
public class SkyblockExtension extends JavaPlugin {

	/**
	 * The instance of this plugin.
	 * 
	 * Private so that (at least I assume; this is from the bukkit tutorial) it
	 * is not possible to overwrite.
	 */
	private static SkyblockExtension inst;

	/**
	 * Gets the instance of this plugin.
	 * 
	 * @return The instance of this plugin.
	 */
	public static SkyblockExtension inst() {
		return inst;
	}

	/**
	 * Called when the plugin is enabled.
	 */
	@Override
	public void onEnable() {
		
		// Register crash reports as being serializable.
		//TODO: Move to own methods.
		ConfigurationSerialization.registerClass(CrashReport.class,
				"CrashReport");
		ConfigurationSerialization.registerClass(ThrowableReport.class,
				"ThrowableReport");
		ConfigurationSerialization.registerClass(ConfigurationErrorReport.class,
				"ConfigurationErrorReport");
		ConfigurationSerialization.registerClass(GenericReport.class,
				"GenericReport");
		ConfigurationSerialization.registerClass(IslandProtectionDataSet.class, 
				"IslandProtectionDataSet");

		this.saveDefaultVersionsOfAllConfigs();

		inst = this;
		Config.loadConfig();
	}

	/**
	 * Called when the plugin is disabled.
	 */
	@Override
	public void onDisable() {
		Config.saveConfig();
	}

	/**
	 * Called when a command is run.
	 * 
	 * @param sender
	 *            The person who sent the command.
	 * @param cmd
	 *            The command.
	 * @param lable
	 *            The given name of the command (EG "example" for "/example")
	 * @param args
	 *            The arguments provided to the command.
	 * @return True if the command syntax was correct, false if you want the
	 *         message in plugin.yml to be displayed.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		try {
			switch (cmd.getName().toLowerCase()) {
			case "pokechu22": { // "/pokechu22" command

				CommandPokechu22.Run(sender, cmd, label, args);

				break;
			}
			case "multichallenge": {
				CommandMultiChallenge.Run(sender, cmd, label, args);
				break;
			}
			case "islandprotection": {
				CommandIslandProtection.Run(sender, cmd, label, args);
				break;
			}
			default: {
				// Tell player.
				sender.sendMessage("§4[SBE]: Unrecognised command: " + cmd.getName()
						+ " (Label: " + label + ")");

				// Log to console.
				getLogger().warning(
						"§4Unrecognised command: " + cmd.getName() + "(Label: "
								+ label + ")");

				// TODO: Log to player.
				break;
			}
			}

		} catch (Throwable e) {

			// Tell the player that an error occurred.
			sender.sendMessage("§4A unhandled error occoured while preforming this command.");
			sender.sendMessage("§4" + e.toString());

			// Put the error message in the console / log file.
			getLogger().severe("An error occoured:");
			getLogger().log(Level.SEVERE, "Exception:", e);
			getLogger().severe("Context: ");
			getLogger().severe(
					"    Command name: " + cmd.getName() + "(Label: " + label
							+ ")");
			getLogger().severe("    Arguments: ");
			for (int i = 0; i < args.length; i++) {
				// For each of the values output it with a number next to it.
				getLogger().severe("        " + i + ": " + args[i]);
			}

			// Log the error for command access.
			ErrorHandler.logError(new ThrowableReport(e, sender, cmd, label, args, 
					"Executing onCommand."));

			// Errors are typically things that shouldn't be caught (EG
			// ThreadDeath), so they will be rethrown.
			if (e instanceof Error) {
				getLogger().severe("Rethrowing Error...");
				sender.sendMessage("§4Rethrowing, as it extends error.");
				throw e;
			}

		}

		return true;
	}

	/**
	 * Handles tab completion.
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String args[]) {
		try {
			switch (cmd.getName().toLowerCase()) {
			case "pokechu22": { // "/pokechu22" command

				break; //TODO
			}
			case "multichallenge": {
				
				return CommandMultiChallenge.onTabComplete(sender, cmd, label, args);
			}
			case "islandprotection": {
				return CommandIslandProtection.onTabComplete(sender, cmd, label, args);
			}
			default: {
				// Tell player.
				sender.sendMessage("§4[SBE]: Unrecognised command: " + cmd.getName()
						+ " (Label: " + label + ")");

				// Log to console.
				getLogger().warning(
						"§4Unrecognised command: " + cmd.getName() + "(Label: "
								+ label + ")");

				// TODO: Log to player.
				break;
			}
			}

		} catch (Throwable e) {

			// Tell the player that an error occurred.
			sender.sendMessage("§4A unhandled error occoured while tab completing.");
			sender.sendMessage("§4" + e.toString());

			// Put the error message in the console / log file.
			getLogger().severe("An error occoured:");
			getLogger().log(Level.SEVERE, "Exception:", e);
			getLogger().severe("Context: ");
			getLogger().severe(
					"    Command name: " + cmd.getName() + "(Label: " + label
							+ ")");
			getLogger().severe("    Arguments: ");
			for (int i = 0; i < args.length; i++) {
				// For each of the values output it with a number next to it.
				getLogger().severe("        " + i + ": " + args[i]);
			}

			// Log the error for command access.
			ErrorHandler.logError(new ThrowableReport(e, sender, cmd, label, args, 
					"Executing onTabComplete."));

			// Errors are typically things that shouldn't be caught (EG
			// ThreadDeath), so they will be rethrown.
			if (e instanceof Error) {
				getLogger().severe("Rethrowing Error...");
				sender.sendMessage("§4Rethrowing, as it extends error.");
				throw e;
			}

		}

		return new ArrayList<String>();
	}
	
	/**
	 * Custom configuration for crashes.
	 */
	private FileConfiguration crashesConfig = null;
	/**
	 * And the file associated with {@linkplain #crashesConfig it}.
	 */
	private File crashesConfigFile = null;
	
	public void reloadCrashesConfig() {
		if (crashesConfigFile == null) {
			crashesConfigFile = new File(getDataFolder(), "crashes.yml");
		}
		crashesConfig = YamlConfiguration.loadConfiguration(crashesConfigFile);

		// Look for defaults in the jar
		Reader defConfigStream;
		try {
			defConfigStream = new InputStreamReader(
				this.getResource("crashes.yml"), "UTF8");
		} catch (UnsupportedEncodingException e) { //Probably shouldn't happen...
			getLogger().severe("Failed to create input stream reader.");
			getLogger().log(Level.SEVERE, "Exception: ", e);
			return;
		}
		
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration
					.loadConfiguration(defConfigStream);
			crashesConfig.setDefaults(defConfig);
		}
	}

	/**
	 * Gets the crashes config.
	 * @return the crashes config.
	 */
	public FileConfiguration getCrashesConfig() {
		if (crashesConfig == null) {
			reloadCrashesConfig();
		}
		return crashesConfig;
	}

	/**
	 * Saves the crashes config.
	 */
	public void saveCrashesConfig() {
		if (crashesConfig == null || crashesConfigFile == null) {
			return;
		}
		try {
			getCrashesConfig().save(crashesConfigFile);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE,
					"Could not save config to " + crashesConfigFile, ex);
		}
	}

	/**
	 * Saves the default crashes config.
	 */
	public void saveDefaultCrashesConfig() {
		
		if (crashesConfigFile == null) {
			crashesConfigFile = new File(getDataFolder(), "crashes.yml");
		}
		if (!crashesConfigFile.exists()) {
			this.saveResource("crashes.yml", false);
		}
	}
	
	/**
	 * Reloads all configs - Convenience method.
	 */
	public void reloadAllConfigs() {
		reloadConfig();
		reloadCrashesConfig();
	}
	
	/**
	 * Saves all configs - Convenience method.
	 */
	public void saveAllConfigs() {
		saveConfig();
		saveCrashesConfig();
	}
	
	/**
	 * Saves the default versions of all configs - Convenience method.
	 */
	public void saveDefaultVersionsOfAllConfigs() {
		saveDefaultConfig();
		saveDefaultCrashesConfig();
	}
}
