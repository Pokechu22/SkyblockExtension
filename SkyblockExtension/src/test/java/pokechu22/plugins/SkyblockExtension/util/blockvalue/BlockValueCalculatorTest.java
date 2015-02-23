package pokechu22.plugins.SkyblockExtension.util.blockvalue;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.junit.Test;

public class BlockValueCalculatorTest {
	/**
	 * Checks whether or not BlockValueCalculator actually adds.
	 * <br/>
	 * This method uses {@link BlockValueCalculator#addBlock(Material)}. 
	 */
	@Test
	public void basicAdditionTest() {
		BlockValueCalculator c = 
				new BlockValueCalculator(new BlockValueMapping());
		assertThat(c.islandPoints, is(0));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(1));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(2));
	}
	
	/**
	 * Checks that all of the <code>addBlock</code> methods actually add
	 * a value.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void differentMethodsShouldAllFunction() {
		BlockValueCalculator c = 
				new BlockValueCalculator(new BlockValueMapping());
		
		Block mockBlock = mock(Block.class);
		when(mockBlock.getType()).thenReturn(Material.DIRT);
		when(mockBlock.getTypeId()).thenReturn(Material.DIRT.getId());
		when(mockBlock.getData()).thenReturn((byte) 0);
		
		assertThat(c.islandPoints, is(0));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(1));
		c.addBlock(Material.DIRT, (byte)0);
		assertThat(c.islandPoints, is(2));
		c.addBlock(Material.DIRT.getId());
		assertThat(c.islandPoints, is(3));
		c.addBlock(Material.DIRT.getId(), (byte)0);
		assertThat(c.islandPoints, is(4));
		c.addBlock(mockBlock);
		assertThat(c.islandPoints, is(5));
	}
	
	/**
	 * Checks whether a value that has been manually assigned uses the
	 * assigned value rather than the default, and that non-assigned ones
	 * do use the default.
	 */
	@Test
	public void assignedValuesShouldBeUsedOverDefault() {
		BlockValueMapping mapping = new BlockValueMapping();
		mapping.blockValues.defaultBlockValuation.defaultData.value = 0;
		mapping.blockValues.map.put(Material.TNT, new BlockValuation());
		
		BlockValueCalculator c = new BlockValueCalculator(mapping);
		
		assertThat(c.islandPoints, is(0));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(0));
		c.addBlock(Material.DIRT, (byte)1);
		assertThat(c.islandPoints, is(0));
		c.addBlock(Material.TNT);
		assertThat(c.islandPoints, is(1));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(1));
		c.addBlock(Material.TNT);
		assertThat(c.islandPoints, is(2));
	}
}
