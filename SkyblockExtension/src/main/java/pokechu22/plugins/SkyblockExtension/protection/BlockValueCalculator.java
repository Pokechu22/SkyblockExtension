package pokechu22.plugins.SkyblockExtension.protection;

import java.io.File;
import java.io.IOException;
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
 * TODO: Is this the propper package?
 * 
 * @author Pokechu22
 *
 */
public class BlockValueCalculator {
	
	private Map<String, Integer> usedItems = 
			new HashMap<String, Integer>();
	public int islandPoints;
	
	public BlockValueCalculator() {
		islandPoints = 0;
	}
	
	public void addBlock(Block block) {
		
	}
	
	/**
	 * Custom configuration for block values.
	 */
	private FileConfiguration blockValuesConfig = null;
	/**
	 * And the file associated with {@linkplain #blockValuesConfig it}.
	 */
	private File blockValuesConfigFile = null;
	
	private void reloadblockValuesConfig() {
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
	 * Gets the blockValues config.
	 * @return the blockValues config.
	 */
	private FileConfiguration getblockValuesConfig() {
		if (blockValuesConfig == null) {
			reloadblockValuesConfig();
		}
		return blockValuesConfig;
	}

	/**
	 * Saves the blockValues config.
	 */
	private void saveblockValuesConfig() {
		if (blockValuesConfig == null || blockValuesConfigFile == null) {
			return;
		}
		try {
			getblockValuesConfig().save(blockValuesConfigFile);
		} catch (IOException ex) {
			SkyblockExtension.inst().getLogger().log(Level.SEVERE,
					"Could not save config to " + blockValuesConfigFile, 
					ex);
		}
	}

	/**
	 * Saves the default blockValues config.
	 */
	private void saveDefaultblockValuesConfig() {
		
		if (blockValuesConfigFile == null) {
			blockValuesConfigFile = new File(SkyblockExtension.inst()
					.getDataFolder(), "block_values.yml");
		}
		if (!blockValuesConfigFile.exists()) {
			SkyblockExtension.inst()
					.saveResource("block_values.yml", false);
		}
	}
}
