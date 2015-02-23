package pokechu22.plugins.SkyblockExtension.util.blockvalue;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;

import pokechu22.plugins.SkyblockExtension.SkyblockExtension;
import pokechu22.plugins.SkyblockExtension.util.blockvalue.BlockValuation.BlockValueData;

/**
 * Calculates the values of blocks in an island.
 * 1 should be created per calculation.
 * 
 * @author Pokechu22
 *
 */
public class BlockValueCalculator {
	public int islandPoints;
	
	private Map<String, Integer> poolValues = new HashMap<>();
	private BlockValueMapping mapping;
	
	/**
	 * Creates a BlockValueCalculator with the default
	 * {@link BlockValueMapping} in use, as specified by 
	 * <code>block_value.yml</code>.
	 */
	public BlockValueCalculator() {
		this.mapping = (BlockValueMapping) SkyblockExtension.inst().getBlockValueConfig().get("block_value");
	}
	
	/**
	 * Creates a BlockValueCalculator with the specified
	 * {@link BlockValueMapping} in use.
	 * @param mapping
	 */
	public BlockValueCalculator(BlockValueMapping mapping) {
		this.mapping = mapping;
	}
	
	/**
	 * Adds the value of the specified block.
	 * 
	 * @param block The block to add.
	 */
	@SuppressWarnings("deprecation")
	public void addBlock(Block block) {
		addBlock(block.getType(), block.getData());
	}
	
	/**
	 * Adds the value of the specified material, assuming data value of 0.
	 * 
	 * @param materialId the numeric ID of the material.
	 */
	@SuppressWarnings("deprecation")
	public void addBlock(int materialId) {
		addBlock(Material.getMaterial(materialId), (byte) 0);
	}

	/**
	 * Adds the value of the specified material with the specified
	 * data value.
	 * 
	 * @param materialId the numeric ID of the material.
	 * @param dataValue the numeric data value (damage value) to use.
	 */
	@SuppressWarnings("deprecation")
	public void addBlock(int materialId, byte dataValue) {
		addBlock(Material.getMaterial(materialId), dataValue);
	}
	
	/**
	 * Adds the value of the specified material, assuming data value of 0.
	 * 
	 * @param material the material to add.
	 */
	public void addBlock(Material material) {
		addBlock(material, (byte) 0);
	}

	/**
	 * Adds the value of the specified material with the specified
	 * data value.
	 * 
	 * @param material the material to add.
	 * @param dataValue the numeric data value (damage value) to use.
	 */
	public void addBlock(Material material, byte dataValue) {
		BlockValueData valueData = mapping.blockValues.getValueOrDefault(
				material, dataValue);
		
		if (!poolValues.containsKey(valueData.maximumPool)) {
			poolValues.put(valueData.maximumPool, 0);
		}
		
		if (poolValues.get(valueData.maximumPool) <= mapping.maximumPools
				.getValueOrDefault(valueData.maximumPool).maximumValue ||
				mapping.maximumPools.getValueOrDefault(
				valueData.maximumPool).maximumValue == -1) {
			islandPoints += valueData.value;
		} else {
			islandPoints += valueData.postPoolValue;
		}
		
		poolValues.put(valueData.maximumPool, poolValues.get(
				valueData.maximumPool) + valueData.poolChange);
	}
}
