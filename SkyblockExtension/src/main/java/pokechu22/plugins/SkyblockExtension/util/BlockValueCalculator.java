package pokechu22.plugins.SkyblockExtension.util;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Material;
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
	protected Map<String, Integer> poolValues = 
			new HashMap<String, Integer>();
	
	public int islandPoints;
	
	public BlockValueCalculator() {
		islandPoints = 0;
		
		defaultBlockValue = getBlockValuesConfig()
				.getInt("defaultBlockValue");
	}
	
	/**
	 * Creates a calculator from the stream as a config.
	 */
	@SuppressWarnings("deprecation")
	public BlockValueCalculator(InputStream stream) {
		islandPoints = 0;
		
		this.blockValuesConfig = YamlConfiguration.loadConfiguration(stream);
		
		defaultBlockValue = getBlockValuesConfig()
				.getInt("defaultBlockValue");
	}
	
	/**
	 * Adds the value of the specified block.
	 * 
	 * @param block
	 */
	@SuppressWarnings("deprecation")
	public void addBlock(Block block) {
		addBlock(block.getTypeId(), block.getData());
	}
	
	/**
	 * Adds the value of the specified material, assuming data value of 0.
	 * @param material
	 */
	@SuppressWarnings("deprecation")
	public void addBlock(Material material) {
		addBlock(material.getId(), (byte) 0);
	}

	/**
	 * Adds the value of the specified material with the specified
	 * data value.
	 * @param material
	 */
	@SuppressWarnings("deprecation")
	public void addBlock(Material material, byte dataValue) {
		addBlock(material.getId(), dataValue);
	}

	/**
	 * Adds the value of the specified material, assuming data value of 0.
	 * @param material
	 */
	public void addBlock(int materialId) {
		addBlock(materialId, (byte) 0);
	}

	/**
	 * Adds the value of the specified material with the specified
	 * data value.
	 * @param material
	 */
	public void addBlock(int materialId, byte dataValue) {
		final String keyName = "blockValues." + materialId;
		final String data = ".data" + dataValue;
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
		
		int dataSpecificValue;
		int dataSpecificPostPoolValue;
		String dataSpecificMaximumPool;
		
		if (dataSpecificObject instanceof Integer) {
			dataSpecificValue = ((Integer) dataSpecificObject).intValue();
			dataSpecificPostPoolValue = postPoolValue;
			dataSpecificMaximumPool = maximumPool;
		} else {
			dataSpecificValue = getBlockValuesConfig().getInt(
					keyName + data + ".value", value);
			dataSpecificPostPoolValue = getBlockValuesConfig().getInt(
					keyName + data + ".postPoolValue", postPoolValue);
			dataSpecificMaximumPool = getBlockValuesConfig().getString(
					keyName + data + ".maximumPool", maximumPool);
		}
		
		//OK, actual process now.
		if (usesPool(dataSpecificMaximumPool)) {
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
		} else {
			islandPoints += dataSpecificValue;
		}
	}
	
	public boolean usesPool(String dataSpecificMaximumPool) {
		return getBlockValuesConfig().getInt("maximumPools." + 
				dataSpecificMaximumPool + ".max") != -1;
	}
	
	/**
	 * Custom configuration for block values.
	 */
	protected FileConfiguration blockValuesConfig = null;
	/**
	 * And the file associated with {@linkplain #blockValuesConfig it}.
	 */
	protected File blockValuesConfigFile = null;
	
	protected void reloadBlockValuesConfig() {
		if (blockValuesConfigFile == null) {
			blockValuesConfigFile = new File(SkyblockExtension.getRealDataFolder(), "block_values.yml");
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
	protected FileConfiguration getBlockValuesConfig() {
		if (blockValuesConfig == null) {
			reloadBlockValuesConfig();
		}
		return blockValuesConfig;
	}
}
