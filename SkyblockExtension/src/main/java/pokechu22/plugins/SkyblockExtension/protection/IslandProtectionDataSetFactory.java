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
	
	private FileConfiguration defaultOwnerConfig = null;
	private File defaultOwnerConfigFile = null;
	
	private FileConfiguration defaultMemberConfig = null;
	private File defaultMemberConfigFile = null;
	
	private FileConfiguration defaultGuestConfig = null;
	private File defaultGuestConfigFile = null;
	
	private FileConfiguration defaultNonmemberConfig = null;
	private File defaultNonmemberConfigFile = null;

	/**
	 * Saves the default configs.
	 */
	public void saveDefaultConfigs() {
		//Owner
		if (defaultOwnerConfigFile == null) {
			defaultOwnerConfigFile = new File(SkyblockExtension.inst()
					.getDataFolder(), "defaultownerconfig.yml");
		}
		if (!defaultOwnerConfigFile.exists()) {
			SkyblockExtension.inst().saveResource("defaultownerconfig.yml"
					, false);
		}
		defaultOwnerConfig = YamlConfiguration
				.loadConfiguration(defaultOwnerConfigFile);
		
		//Member
		if (defaultMemberConfigFile == null) {
			defaultMemberConfigFile = new File(SkyblockExtension.inst()
					.getDataFolder(), "defaultmemberconfig.yml");
		}
		if (!defaultMemberConfigFile.exists()) {
			SkyblockExtension.inst().saveResource("defaultmemberconfig.yml"
					, false);
		}
		defaultMemberConfig = YamlConfiguration
				.loadConfiguration(defaultMemberConfigFile);

		//Guest
		if (defaultGuestConfigFile == null) {
			defaultGuestConfigFile = new File(SkyblockExtension.inst()
					.getDataFolder(), "defaultguestconfig.yml");
		}
		if (!defaultGuestConfigFile.exists()) {
			SkyblockExtension.inst().saveResource("defaultguestconfig.yml"
					, false);
		}
		defaultGuestConfig = YamlConfiguration
				.loadConfiguration(defaultGuestConfigFile);

		//Nonmember
		if (defaultNonmemberConfigFile == null) {
			defaultNonmemberConfigFile = new File(SkyblockExtension.inst()
					.getDataFolder(), "defaultnonmemberconfig.yml");
		}
		if (!defaultNonmemberConfigFile.exists()) {
			SkyblockExtension.inst()
				.saveResource("defaultnonmemberconfig.yml", false);
		}
		defaultNonmemberConfig = YamlConfiguration
				.loadConfiguration(defaultNonmemberConfigFile);

	}
}
