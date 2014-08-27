package pokechu22.plugins.SkyblockExtension.protection;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import pokechu22.plugins.SkyblockExtension.SkyblockExtension;

/**
 * Tool that provides IslandProtectionDataSets with the default values.
 *
 * @author Pokechu22
 *
 */
public class IslandProtectionDataSetFactory {
	
	private FileConfiguration defaultProtectionConfig = null;
	private File defaultProtectionConfigFile = null;

	/**
	 * Saves the default configs.
	 */
	public void saveDefaultConfigs() {
		//Owner
		if (defaultProtectionConfigFile == null) {
			defaultProtectionConfigFile = new File(SkyblockExtension.inst()
					.getDataFolder(), "default_protection.yml");
		}
		if (!defaultProtectionConfigFile.exists()) {
			SkyblockExtension.inst().saveResource("default_protection.yml"
					, false);
		}
		defaultProtectionConfig = YamlConfiguration
				.loadConfiguration(defaultProtectionConfigFile);

	}
}
