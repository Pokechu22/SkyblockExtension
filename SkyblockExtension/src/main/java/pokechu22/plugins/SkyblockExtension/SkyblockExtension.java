package pokechu22.plugins.SkyblockExtension;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import pokechu22.plugins.SkyblockExtension.commands.CommandMultiChallenge;
import pokechu22.plugins.SkyblockExtension.commands.CommandPokechu22;
import pokechu22.plugins.SkyblockExtension.commands.CommandTpCancel;
import pokechu22.plugins.SkyblockExtension.errorhandling.ConfigurationErrorReport;
import pokechu22.plugins.SkyblockExtension.errorhandling.CrashReport;
import pokechu22.plugins.SkyblockExtension.errorhandling.GenericReport;
import pokechu22.plugins.SkyblockExtension.errorhandling.LoginErrorBroadcaster;
import pokechu22.plugins.SkyblockExtension.errorhandling.ThrowableReport;
import pokechu22.plugins.SkyblockExtension.hooks.CommandIsland;
import pokechu22.plugins.SkyblockExtension.util.blockvalue.BlockValuation;
import pokechu22.plugins.SkyblockExtension.util.blockvalue.BlockValuation.BlockValueData;
import pokechu22.plugins.SkyblockExtension.util.blockvalue.BlockValueMapping;
import pokechu22.plugins.SkyblockExtension.util.blockvalue.BlockValueMapping.BlockValueCollection;
import pokechu22.plugins.SkyblockExtension.util.blockvalue.BlockValueMapping.MaximumPoolCollection;
import pokechu22.plugins.SkyblockExtension.util.blockvalue.MaximumPool;
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

		ConfigurationSerialization.registerClass(BlockValueMapping.class,
				"SBEBlockValueMapping");
		ConfigurationSerialization.registerClass(MaximumPoolCollection.class,
				"SBEMaximumPoolCollection");
		ConfigurationSerialization.registerClass(BlockValueCollection.class,
				"SBEBlockValueCollection");
		ConfigurationSerialization.registerClass(BlockValuation.class,
				"SBEBlockValuation");
		ConfigurationSerialization.registerClass(BlockValueData.class,
				"SBEBlockData");
		ConfigurationSerialization.registerClass(MaximumPool.class,
				"SBEMaximumPool");
		
		// Register events.
		getServer().getPluginManager().registerEvents(
				new LoginErrorBroadcaster(), this);
		getServer().getPluginManager().registerEvents(new WitherWarner(), 
				this);
		getServer().getPluginManager().registerEvents(
				new SlabPlacementListener(), this);

		// Other registration.
		registerCommands();
		CommandIsland.registerHooks();

		this.saveDefaultVersionsOfAllConfigs();

		inst = this;

		WitherWarner.load();

		Config.loadConfig();

		MetricsHandler.start();
	}

	private void registerCommands() {
		CommandMultiChallenge multichallenge = new CommandMultiChallenge();
		this.getCommand("multichallenge").setExecutor(multichallenge);
		this.getCommand("multichallenge").setTabCompleter(multichallenge);
		CommandPokechu22 pokechu22 = new CommandPokechu22();
		this.getCommand("pokechu22").setExecutor(pokechu22);
		this.getCommand("pokechu22").setTabCompleter(pokechu22);
		CommandTpCancel tpcancel = new CommandTpCancel();
		this.getCommand("tpcancel").setExecutor(tpcancel);
		this.getCommand("tpcancel").setTabCompleter(tpcancel);
	}

	/**
	 * Called when the plugin is disabled.
	 */
	@Override
	public void onDisable() {
		Config.saveConfig();
		WitherWarner.save();
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
