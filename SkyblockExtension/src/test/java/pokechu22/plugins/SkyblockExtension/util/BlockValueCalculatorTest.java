package pokechu22.plugins.SkyblockExtension.util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.bukkit.Material;
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
	
	/**
	 * Tests basic addition.
	 * Assumes that cobble is worth 1 point.
	 */
	@Test
	public void basicPointAdition() {
		BlockValueCalculator calc = getCalculator();
		assertThat(calc.islandPoints, is(0));
		
		calc.addBlock(Material.COBBLESTONE);
		assertThat(calc.islandPoints, is(1));
		
	}
	
	/**
	 * Checks that air is ignored.  (It better be)
	 */
	@Test
	public void airShouldBeIgnored() {
		BlockValueCalculator calc = getCalculator();
		assertThat(calc.islandPoints, is(0));
		
		calc.addBlock(Material.AIR);
		assertThat(calc.islandPoints, is(0));
	}
	
	/**
	 * Check that liquids are ignored.
	 */
	@Test
	public void liquidsShouldBeIgnored() {
		BlockValueCalculator calc = getCalculator();
		assertThat(calc.islandPoints, is(0));
		
		calc.addBlock(Material.WATER);
		assertThat(calc.islandPoints, is(0));
		
		calc.addBlock(Material.STATIONARY_WATER);
		assertThat(calc.islandPoints, is(0));
		
		calc.addBlock(Material.LAVA);
		assertThat(calc.islandPoints, is(0));
		
		calc.addBlock(Material.STATIONARY_LAVA);
		assertThat(calc.islandPoints, is(0));
	}
	
	/**
	 * Check vines being ignored.
	 */
	@Test
	public void vinesShouldBeIgnored() {
		BlockValueCalculator calc = getCalculator();
		assertThat(calc.islandPoints, is(0));
		
		calc.addBlock(Material.VINE);
		assertThat(calc.islandPoints, is(0));
	}
	
	/**
	 * Checks the cobble maximum.
	 * This will take a while, though.
	 */
	@Test
	public void cobbleCountShouldCap() {
		BlockValueCalculator calc = getCalculator();
		assertThat(calc.islandPoints, is(0));
		
		for (int i = 1; i <= 10000; i++) {
			calc.addBlock(Material.COBBLESTONE);
			assertThat(calc.islandPoints, is(i));
		}
		for (int i = 10001; i <= 10100; i++) {
			calc.addBlock(Material.COBBLESTONE);
			assertThat(calc.islandPoints, is(10000));
		}
	}
	
	/**
	 * Checks with end stone now.
	 */
	@Test
	public void endCountShouldCap() {
		BlockValueCalculator calc = getCalculator();
		assertThat(calc.islandPoints, is(0));
		
		for (int i = 1; i <= 5000; i++) {
			calc.addBlock(Material.ENDER_STONE);
			assertThat(calc.islandPoints, is(i * 3));
		}
		for (int j = 1; j <= 100; j++) {
			calc.addBlock(Material.ENDER_STONE);
			assertThat(calc.islandPoints, is((5000 * 3) + (j * 2)));
		}
	}
	
	/**
	 * Checks undefined materials working
	 * (As of MC1.7.10, there is no block with ID 1000)
	 */
	@Test
	public void undefinedMaterialsShouldAdd1() {
		BlockValueCalculator calc = getCalculator();
		assertThat(calc.islandPoints, is(0));
		
		calc.addBlock(1000);
		assertThat(calc.islandPoints, is(1));
	}
}
