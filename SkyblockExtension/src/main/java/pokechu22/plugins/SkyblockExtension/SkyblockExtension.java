package pokechu22.plugins.SkyblockExtension;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import pokechu22.plugins.SkyblockExtension.commands.CommandIslandProtection;
import pokechu22.plugins.SkyblockExtension.commands.CommandMultiChallenge;
import pokechu22.plugins.SkyblockExtension.commands.CommandPokechu22;
import pokechu22.plugins.SkyblockExtension.commands.CommandTpCancel;
import pokechu22.plugins.SkyblockExtension.errorhandling.ConfigurationErrorReport;
import pokechu22.plugins.SkyblockExtension.errorhandling.CrashReport;
import pokechu22.plugins.SkyblockExtension.errorhandling.ErrorHandler;
import pokechu22.plugins.SkyblockExtension.errorhandling.GenericReport;
import pokechu22.plugins.SkyblockExtension.errorhandling.LoginErrorBroadcaster;
import pokechu22.plugins.SkyblockExtension.errorhandling.ThrowableReport;
import pokechu22.plugins.SkyblockExtension.hooks.CommandIsland;
import pokechu22.plugins.SkyblockExtension.protection.IslandInfoCache;
import pokechu22.plugins.SkyblockExtension.protection.IslandProtectionDataSet;
import pokechu22.plugins.SkyblockExtension.protection.IslandProtectionDataSetFactory;
import pokechu22.plugins.SkyblockExtension.util.mcstats.MetricsHandler;

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
		// TODO: Move to own methods.
		ConfigurationSerialization.registerClass(CrashReport.class,
				"CrashReport");
		ConfigurationSerialization.registerClass(ThrowableReport.class,
				"ThrowableReport");
		ConfigurationSerialization.registerClass(
				ConfigurationErrorReport.class, "ConfigurationErrorReport");
		ConfigurationSerialization.registerClass(GenericReport.class,
				"GenericReport");
		ConfigurationSerialization.registerClass(IslandProtectionDataSet.class,
				"IslandProtectionDataSet");

		// Register events.
		getServer().getPluginManager().registerEvents(
				new LoginErrorBroadcaster(), this);
		getServer().getPluginManager().registerEvents(new WitherWarner(), this);

		// Other registration.
		registerCommands();
		CommandIsland.registerHooks();

		this.saveDefaultVersionsOfAllConfigs();

		inst = this;

		// Load the default protection.
		try {
			IslandProtectionDataSetFactory.init();
			WitherWarner.load();
		} catch (InvalidConfigurationException e) {
			// If this fails, we want it to be fully thrown to stop loading.
			throw new RuntimeException(e);
		}

		Config.loadConfig();

		MetricsHandler.start();
	}

	private void registerCommands() {
		CommandMultiChallenge multichallenge = new CommandMultiChallenge();
		this.getCommand("multichallenge").setExecutor(multichallenge);
		this.getCommand("multichallenge").setTabCompleter(multichallenge);
	}

	/**
	 * Called when the plugin is disabled.
	 */
	@Override
	public void onDisable() {
		Config.saveConfig();
		WitherWarner.save();
		IslandInfoCache.dumpCache();
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
			case "islandprotection": {
				CommandIslandProtection.Run(sender, cmd, label, args);
				break;
			}
			case "tpcancel": {
				CommandTpCancel.Run(sender, cmd, label, args);
				break;
			}
			default: {
				// Tell player.
				sender.sendMessage("§4[SBE]: Unrecognised command: "
						+ cmd.getName() + " (Label: " + label + ")");

				// Log to console.
				getLogger().warning(
						"§4Unrecognised command: " + cmd.getName() + "(Label: "
								+ label + ")");

				// Log to error handler.
				ErrorHandler.logError(new GenericReport(
						"Unrecognised command while tabCompleting: "
								+ cmd.getName() + "(Label: " + label + ")"));

				break;
			}
			}

		} catch (Throwable e) {
			ErrorHandler.logExceptionOnCommand(sender, cmd, label, args, e);
		}

		return true;
	}

	/**
	 * Handles tab completion.
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String label, String args[]) {
		try {
			switch (cmd.getName().toLowerCase()) {
			case "pokechu22": { // "/pokechu22" command

				return CommandPokechu22.onTabComplete(sender, cmd, label, args);
			}
			case "islandprotection": {
				return CommandIslandProtection.onTabComplete(sender, cmd,
						label, args);
			}
			case "tpcancel": {
				return CommandTpCancel.onTabComplete(sender, cmd, label, args);
			}
			default: {
				// Tell player.
				sender.sendMessage("§4[SBE]: Unrecognised command: "
						+ cmd.getName() + " (Label: " + label + ")");

				// Log to console.
				getLogger().warning(
						"§4Unrecognised command: " + cmd.getName() + "(Label: "
								+ label + ")");

				// Log to error handler.
				ErrorHandler.logError(new GenericReport(
						"Unrecognised command while tabCompleting: "
								+ cmd.getName() + "(Label: " + label + ")"));
				break;
			}
			}

		} catch (Throwable e) {
			ErrorHandler.logExceptionOnTabComplete(sender, cmd, label, args, e);
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
		} catch (UnsupportedEncodingException e) { // Probably shouldn't
													// happen...
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
	 * 
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
		// saveConfig(); //DISABLED - no need to save it, and would overwrite
		// stuff anyways.
		saveCrashesConfig();
	}

	/**
	 * Saves the default versions of all configs - Convenience method.
	 */
	public void saveDefaultVersionsOfAllConfigs() {
		saveDefaultConfig();
		saveDefaultCrashesConfig();
	}

	/**
	 * Gets the directory for island info. This is, by default, the same as the
	 * dataFolder, but it will use a different location during unit testing.
	 * 
	 * @return
	 */
	public static File getRealDataFolder() {
		File returned = inst() != null ? inst().getDataFolder() : null;
		if (returned != null) {
			return returned;
		}

		// Ugly...
		returned = new File((new File(SkyblockExtension.class.getResource(
				"/plugin.yml").getFile())).getParentFile().getParentFile(),
				"test-classes");

		return returned;
	}
}
