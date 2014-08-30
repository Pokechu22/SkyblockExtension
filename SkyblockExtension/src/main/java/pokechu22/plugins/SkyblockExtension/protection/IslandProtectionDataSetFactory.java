package pokechu22.plugins.SkyblockExtension.protection;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import pokechu22.plugins.SkyblockExtension.ConfigurationErrorReport;
import pokechu22.plugins.SkyblockExtension.ErrorHandler;
import pokechu22.plugins.SkyblockExtension.SkyblockExtension;
/**
 * Tool that provides IslandProtectionDataSets with the default values.
 *
 * @author Pokechu22
 *
 */
public class IslandProtectionDataSetFactory {
	
	private static final EnumMap<MembershipTier, IslandProtectionDataSet> 
			defaultValues = new EnumMap<MembershipTier, 
					IslandProtectionDataSet>(MembershipTier.class); 
	
	/**
	 * Gets all default values for all tiers.
	 * 
	 * @return
	 */
	public static Map<String, IslandProtectionDataSet> 
			getDefaultValues() {
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
		return defaultValues.get(m);
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
		
		defaultProtectionConfigFile = new File(SkyblockExtension.inst()
				.getDataFolder(), "default_protection.yml");
		
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
				System.out.println(t.name());
				i = (IslandProtectionDataSet)
						objects.get(t.name());
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
	}
}
