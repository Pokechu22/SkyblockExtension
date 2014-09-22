package pokechu22.plugins.SkyblockExtension.util;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import pokechu22.plugins.SkyblockExtension.SkyblockExtension;

/**
 * Calculates the values of blocks in an island.
 * 1 should be created per calculation.
 * 
 * @author Pokechu22
 *
 */
public class BlockValueCalculator {
	/**
	 * The default value for blocks.
	 */
	private final int defaultBlockValue;
	
	/**
	 * Pool of used values.
	 */
	private Map<String, Integer> poolValues = 
			new HashMap<String, Integer>();
	
	public int islandPoints;
	
	public BlockValueCalculator() {
		islandPoints = 0;
		
		defaultBlockValue = getBlockValuesConfig()
				.getInt("defaultBlockValue");
	}
	
	@SuppressWarnings("deprecation")
	public void addBlock(Block block) {
		final String keyName = "blockValues." + block.getTypeId();
		final String data = ".data" + block.getData();
		Object object = getBlockValuesConfig().get(keyName);
		if (object == null) {
			islandPoints += defaultBlockValue;
			return;
		}
		if (object instanceof Integer) {
			islandPoints += ((Integer) object).intValue();
			return;
		}
		//If not an integer, treat it as a map.
		int value = getBlockValuesConfig().getInt(keyName + 
				".value", defaultBlockValue);
		int postPoolValue = getBlockValuesConfig().getInt(
				keyName + ".postPoolValue", 0);
		String maximumPool = getBlockValuesConfig().getString(
				keyName + ".maximumPool", "default");
		
		//For any unspecified values, it uses the generalized value.
		//It's an odd-looking method, but it's equivilant to doing
		//int dataSpecificValue = getBlockValuesConfig().getInt(
		//keyName + data);
		//if (dataSpecificValue != null) {
		//    value = dataSpecificValue)
		//}
		//Except this is clearer, and it actually probably wouldn't
		//return null if I use getInt()...
		
		Object dataSpecificObject = getBlockValuesConfig().get(
				keyName + data);
		if (dataSpecificObject instanceof Integer) {
			islandPoints += ((Integer) dataSpecificObject).intValue();
			return;
		}
		
		int dataSpecificValue = getBlockValuesConfig().getInt(
				keyName + data, value);
		int dataSpecificPostPoolValue = getBlockValuesConfig().getInt(
				keyName + data, postPoolValue);
		String dataSpecificMaximumPool = getBlockValuesConfig().getString(
				keyName + data, maximumPool);
		
		//OK, actual process now.
		if (!poolValues.containsKey(dataSpecificMaximumPool)) {
			poolValues.put(dataSpecificMaximumPool, 0);
		}
		if (poolValues.get(dataSpecificMaximumPool) < 
				getBlockValuesConfig().getInt("maximumPools." + 
						dataSpecificMaximumPool + ".max")) {
			islandPoints += dataSpecificValue;
			//I don't know if using ++ is safe here, so this is done.
			Integer currentPoolValue = poolValues.get(
					dataSpecificMaximumPool);
			currentPoolValue ++;
			poolValues.put(dataSpecificMaximumPool, currentPoolValue);
		} else {
			islandPoints += dataSpecificPostPoolValue;
			//I don't know if using ++ is safe here, so this is done.
			Integer currentPoolValue = poolValues.get(
					dataSpecificMaximumPool);
			currentPoolValue ++;
			poolValues.put(dataSpecificMaximumPool, currentPoolValue);
		}
	}
	
	/**
	 * Custom configuration for block values.
	 */
	private FileConfiguration blockValuesConfig = null;
	/**
	 * And the file associated with {@linkplain #blockValuesConfig it}.
	 */
	private File blockValuesConfigFile = null;
	
	private void reloadBlockValuesConfig() {
		if (blockValuesConfigFile == null) {
			blockValuesConfigFile = new File(SkyblockExtension.inst()
					.getDataFolder(), "block_values.yml");
		}
		blockValuesConfig = YamlConfiguration
				.loadConfiguration(blockValuesConfigFile);

		// Look for defaults in the jar
		Reader defConfigStream;
		try {
			defConfigStream = new InputStreamReader(
					SkyblockExtension.inst()
					.getResource("block_values.yml"), "UTF8");
		} catch (UnsupportedEncodingException e) { 
			//Probably shouldn't happen...
			SkyblockExtension.inst().getLogger()
					.severe("Failed to create input stream reader.");
			SkyblockExtension.inst()
					.getLogger().log(Level.SEVERE, "Exception: ", e);
			return;
		}
		
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration
					.loadConfiguration(defConfigStream);
			blockValuesConfig.setDefaults(defConfig);
		}
	}

	/**
	 * Gets the BlockValues config.
	 * @return the BlockValues config.
	 */
	private FileConfiguration getBlockValuesConfig() {
		if (blockValuesConfig == null) {
			reloadBlockValuesConfig();
		}
		return blockValuesConfig;
	}
}
