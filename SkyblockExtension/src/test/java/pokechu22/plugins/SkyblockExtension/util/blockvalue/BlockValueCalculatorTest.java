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
	
	/**
	 * Checks that maximum pools work.
	 */
	@Test
	public void maximumPoolsShouldWork() {
		BlockValueMapping mapping = new BlockValueMapping();
		mapping.blockValues.defaultBlockValuation.defaultData = 
				new BlockValuation.BlockValueData() {{
					this.maximumPool = "foo";
					this.value = 1;
					this.postPoolValue = 2;
					this.poolChange = 1;
				}};
		mapping.maximumPools.map.put("foo", new MaximumPool(5));
		
		BlockValueCalculator c = new BlockValueCalculator(mapping);
		
		assertThat(c.islandPoints, is(0));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(1));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(2));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(3));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(4));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(5));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(7));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(9));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(11));
	}
	
	/**
	 * Checks that {@link BlockValuation.BlockValueData#poolChange} works.
	 */
	@Test
	public void poolChangeShouldWork() {
		BlockValueMapping mapping = new BlockValueMapping();
		mapping.blockValues.defaultBlockValuation.defaultData = 
				new BlockValuation.BlockValueData() {{
					this.maximumPool = "foo";
					this.value = 1;
					this.postPoolValue = 2;
					this.poolChange = 1;
				}};
		mapping.blockValues.map.put(Material.DIAMOND, 
				new BlockValuation() {{
					this.defaultData =
					new BlockValuation.BlockValueData() {{
						this.maximumPool = "foo";
						this.value = 1;
						this.postPoolValue = 2;
						this.poolChange = -3;
					}};
				}});
		mapping.maximumPools.map.put("foo", new MaximumPool(5));
		
		BlockValueCalculator c = new BlockValueCalculator(mapping);
		
		assertThat(c.islandPoints, is(0));
		assertThat(c.getPoolValue("foo"), is(0));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(1));
		assertThat(c.getPoolValue("foo"), is(1));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(2));
		assertThat(c.getPoolValue("foo"), is(2));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(3));
		assertThat(c.getPoolValue("foo"), is(3));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(4));
		assertThat(c.getPoolValue("foo"), is(4));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(5));
		assertThat(c.getPoolValue("foo"), is(5));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(7));
		assertThat(c.getPoolValue("foo"), is(6));
		c.addBlock(Material.DIAMOND);
		assertThat(c.islandPoints, is(9));
		assertThat(c.getPoolValue("foo"), is(3));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(10));
		assertThat(c.getPoolValue("foo"), is(4));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(11));
		assertThat(c.getPoolValue("foo"), is(5));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(13));
		assertThat(c.getPoolValue("foo"), is(6));
		c.addBlock(Material.DIRT);
		assertThat(c.islandPoints, is(15));
		assertThat(c.getPoolValue("foo"), is(7));
	}
	
	/**
	 * Checks that sub data values work.
	 */
	@Test
	public void dataValuesShouldBeDistinct() {
		BlockValueMapping mapping = new BlockValueMapping();
		mapping.blockValues.defaultBlockValuation = new BlockValuation() {{ 
				this.defaultData = new BlockValueData() {{
					this.value = 1;
				}};
				this.dataValues.put((byte)1, new BlockValueData() {{
					this.value = 2;
				}});
				this.dataValues.put((byte)2, new BlockValueData() {{
					this.value = 4;
				}});
		}};
		
		BlockValueCalculator c = new BlockValueCalculator(mapping);
		
		assertThat(c.islandPoints, is(0));
		c.addBlock(Material.DIRT, (byte)0);
		assertThat(c.islandPoints, is(1));
		c.addBlock(Material.DIRT, (byte)0);
		assertThat(c.islandPoints, is(2));
		c.addBlock(Material.DIRT, (byte)1);
		assertThat(c.islandPoints, is(4));
		c.addBlock(Material.DIRT, (byte)1);
		assertThat(c.islandPoints, is(6));
		c.addBlock(Material.DIRT, (byte)1);
		assertThat(c.islandPoints, is(8));
		c.addBlock(Material.DIRT, (byte)2);
		assertThat(c.islandPoints, is(12));
		c.addBlock(Material.DIRT, (byte)2);
		assertThat(c.islandPoints, is(16));
	}
}
