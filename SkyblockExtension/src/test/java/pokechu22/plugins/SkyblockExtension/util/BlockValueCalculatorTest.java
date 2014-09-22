package pokechu22.plugins.SkyblockExtension.util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Test;

public class BlockValueCalculatorTest {
	/**
	 * Gets a calculator.
	 * Required because some stupid stuff happens with configs.
	 */
	private BlockValueCalculator getCalculator() {
		BlockValueCalculator calc = new BlockValueCalculator(
				this.getClass().getResourceAsStream("/block_values.yml"));
		return calc;
	}
	
	/**
	 * Raw test, which makes sure that the basic calculator doesn't break.
	 */
	@Test
	public void canCreateCalculator() {
		BlockValueCalculator calc = getCalculator();
		calc.addBlock(Material.AIR);
	}
}
