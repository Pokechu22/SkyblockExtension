package pokechu22.plugins.SkyblockExtension.protection;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import pokechu22.plugins.SkyblockExtension.SkyblockExtension;
import pokechu22.plugins.SkyblockExtension.errorhandling.ConfigurationErrorReport;
import pokechu22.plugins.SkyblockExtension.errorhandling.ErrorHandler;
import pokechu22.plugins.SkyblockExtension.protection.flags.IslandProtectionDataSetFlag;
/**
 * Tool that provides IslandProtectionDataSets with the default values.
 *
 * @author Pokechu22
 *
 */
public class IslandProtectionDataSetFactory {
	
	/**
	 * For auto-initiation.
	 */
	private static boolean initiated = false;
	
	/**
	 * The text before the actual value to get.  
	 * For example: "permissions.".  This means that it searches for 
	 * "permissions.owner". 
	 */
	private static final String config_prepender = "permissions.";
	
	private static final EnumMap<MembershipTier, IslandProtectionDataSet> 
			defaultValues = new EnumMap<MembershipTier, 
					IslandProtectionDataSet>(MembershipTier.class); 
	
	private static IslandProtectionDataSet outsideIslandInfo;
	
	/**
	 * Gets all default values for all tiers.
	 * 
	 * @return
	 */
	public static Map<String, IslandProtectionDataSet> 
			getDefaultValues() {
		if (!initiated) {
			try {
				init();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			initiated = true;
		}
		
		//No longer using EnumMaps, because that makes serialization tough.
		Map<String, IslandProtectionDataSet> returned = 
				new HashMap<String, IslandProtectionDataSet>(
						defaultValues.size());
		for (Map.Entry<MembershipTier, IslandProtectionDataSet> m : 
			defaultValues.entrySet()) {
			returned.put(m.getKey().name(), m.getValue());
		}
		
		return returned;
	}
	
	/**
	 * Gets the default value for a single tier.
	 *
	 * @param m
	 * @return
	 */
	public static IslandProtectionDataSet getDefaultValue(MembershipTier m){
		if (!initiated) {
			try {
				init();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			initiated = true;
		}
		return defaultValues.get(m);
	}
	
	/**
	 * Gets the IslandProtectionDataSet used in areas that are not part
	 * of any island 
	 * are not
	 * @return
	 */
	public static IslandProtectionDataSet getUnprotectedAreaValue() {
		if (!initiated) {
			try {
				init();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			initiated = true;
		}
		
		return outsideIslandInfo;
	}
	
	/**
	 * Gets the default value for an individual flag.  
	 * 
	 * @param tier
	 * @param flag
	 * @return
	 */
	public static IslandProtectionDataSetFlag 
			getDefaultValue(MembershipTier tier, String flag) {
		return defaultValues.get(tier).getFlag(flag);
	}
	
	/**
	 * Gets all of the unprotected values, suitable for using in a map.
	 * 
	 * @return
	 */
	public static Map<String, IslandProtectionDataSet>
			getUnprotectedValues() {
		if (!initiated) {
			try {
				init();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			initiated = true;
		}
		
		//No longer using EnumMaps, because that makes serialization tough.
		Map<String, IslandProtectionDataSet> returned = 
				new HashMap<String, IslandProtectionDataSet>(
						defaultValues.size());
		for (Map.Entry<MembershipTier, IslandProtectionDataSet> m : 
			defaultValues.entrySet()) {
			returned.put(m.getKey().name(), getUnprotectedAreaValue());
		}
		
		return returned;
	}

	/**
	 * Saves the default configs, as needed.
	 * @throws InvalidConfigurationException 
	 * 			if the default configuration is invalid
	 */
	public static void init() throws InvalidConfigurationException {
		//Set up the config file.
		
		//Default file, modifiable, in plugin data folder.
		FileConfiguration defaultProtectionConfig;
		File defaultProtectionConfigFile;
		
		defaultProtectionConfigFile = new File(SkyblockExtension.getRealDataFolder(), 
				"default_protection.yml");
		
		if (!defaultProtectionConfigFile.exists()) {
			SkyblockExtension.inst().saveResource("default_protection.yml"
					, false);
		}
		
		defaultProtectionConfig = YamlConfiguration
				.loadConfiguration(defaultProtectionConfigFile);

		//Now set up the defaults.
		Map<String, Object> objects;
		
		objects = defaultProtectionConfig.getValues(true);
		
		for (MembershipTier t : MembershipTier.values()) {
			IslandProtectionDataSet i;
			try {
				i = (IslandProtectionDataSet)
						objects.get(config_prepender + t.name());
			} catch (Exception e) {
				ErrorHandler.logError(new ConfigurationErrorReport(e, 
						t.name(), "default_protection.yml", 
						IslandProtectionDataSetFactory.class.getName(), 
						false).setContext("An exception was caught.  " + 
						"Loading the default IslandProtectionDataSet for "+ 
						"defaultValues." + t.name() + ".  " + 
						"The configuration is probably broken."));
				SkyblockExtension.inst().getLogger().severe(
						"An error occured while loading the default " + 
						"protection configuration: Exception thrown while "+
						"reading defaultValues." + t.name() + ".");
				throw new InvalidConfigurationException(
						"An error occured while loading the default " + 
						"protection configuration: Exception thrown while "+
						"reading defaultValues." + t.name() + ".", e);
			}
			if (i == null) {
				ErrorHandler.logError(new ConfigurationErrorReport( 
						t.name(), "default_protection.yml", 
						IslandProtectionDataSetFactory.class.getName(), 
						false).setContext("Value was null.  " + 
						"Loading the default IslandProtectionDataSet for " +
						"defaultValues." + t.name() + ".  " + 
						"The configuration is probably broken."));
				SkyblockExtension.inst().getLogger().severe(
						"An error occured while loading the default " + 
						"protection configuration: defaultValues." + 
						t.name() + "is null!");
				throw new InvalidConfigurationException(
						"An error occured while loading the default " + 
						"protection configuration: defaultValues." +
						t.name() + "is null!");
			}
			
			//Put the enum value and the corresponding 
			//IslandProectionDatSet in the map.
			defaultValues.put(t, i);
		}
		
		//Default file, modifiable, in plugin data folder.
		FileConfiguration externalProtectionConfig;
		File externalProtectionConfigFile;
		
		externalProtectionConfigFile = new File(SkyblockExtension.getRealDataFolder(), 
				"unprotected_areas.yml");
		
		if (!externalProtectionConfigFile.exists()) {
			SkyblockExtension.inst().saveResource("unprotected_areas.yml"
					, false);
		}
		
		externalProtectionConfig = YamlConfiguration
				.loadConfiguration(defaultProtectionConfigFile);
		
		try {
			outsideIslandInfo = (IslandProtectionDataSet)
					externalProtectionConfig.get(config_prepender + "unprotected");
		} catch (Exception e) {
			ErrorHandler.logError(new ConfigurationErrorReport(e, 
					"unprotected", "unprotected_areas.yml", 
					IslandProtectionDataSetFactory.class.getName(), 
					false).setContext("An exception was caught.  " + 
					"Loading the default IslandProtectionDataSet for "+ 
					"defaultValues." + "unprotected" + ".  " + 
					"The configuration is probably broken."));
			SkyblockExtension.inst().getLogger().severe(
					"An error occured while loading the default " + 
					"protection configuration: Exception thrown while "+
					"reading defaultValues." + "unprotected" + ".");
			throw new InvalidConfigurationException(
					"An error occured while loading the default " + 
					"protection configuration: Exception thrown while "+
					"reading defaultValues." + "unprotected" + ".", e);
		}
		if (outsideIslandInfo == null) {
			ErrorHandler.logError(new ConfigurationErrorReport( 
					"unprotected", "unprotected_areas.yml", 
					IslandProtectionDataSetFactory.class.getName(), 
					false).setContext("Value was null.  " + 
					"Loading the default IslandProtectionDataSet for " +
					"defaultValues." +"unprotected" + ".  " + 
					"The configuration is probably broken."));
			SkyblockExtension.inst().getLogger().severe(
					"An error occured while loading the default " + 
					"protection configuration: defaultValues." + 
					"unprotected" + "is null!");
			throw new InvalidConfigurationException(
					"An error occured while loading the default " + 
					"protection configuration: defaultValues." +
					"unprotected" + "is null!");
		}
	}
}
