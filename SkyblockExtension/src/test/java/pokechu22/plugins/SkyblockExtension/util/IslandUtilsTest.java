package pokechu22.plugins.SkyblockExtension.util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.bukkit.Location;

import org.junit.Test;

/**
 * Test of {@link IslandUtils}.
 * Assumes that the values set in the 
 * {@linkplain us.talabrek.ultimateskyblock.Config Configuration} are at
 * the defaults, which should be the case due to the overwriting.
 * 
 * @author Pokechu22
 *
 */
public class IslandUtilsTest {
	/**
	 * Test of {@link IslandUtils#getOccupyingIsland(Location)} throwing
	 * an exception when given null. 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getOccupyingIslandShouldThrowWithNull() {
		IslandUtils.getOccupyingIsland(null);
	}
	
	@Test
	public void getOccupyingIslandTest() {
		//Check 0,0.
		assertThat(IslandUtils.getOccupyingIsland(new Location(null,0d,120d,0d)),
				is(new Location(null,0d,120d,0d)));
		//Check near 0,0.
		assertThat(IslandUtils.getOccupyingIsland(new Location(null,2d,120d,2d)),
				is(new Location(null,0d,120d,0d)));
		//And further from 0,0, but close.
		assertThat(IslandUtils.getOccupyingIsland(new Location(null,50d,120d,50d)),
				is(new Location(null,0d,120d,0d)));
		//Square around at the farthest.
		{
			assertThat(IslandUtils.getOccupyingIsland(new Location(null,54d,120d,54d)),
					is(new Location(null,0d,120d,0d)));
			assertThat(IslandUtils.getOccupyingIsland(new Location(null,0d,120d,54d)),
					is(new Location(null,0d,120d,0d)));
			assertThat(IslandUtils.getOccupyingIsland(new Location(null,-54d,120d,54d)),
					is(new Location(null,0d,120d,0d)));
			
			assertThat(IslandUtils.getOccupyingIsland(new Location(null,54d,120d,0d)),
					is(new Location(null,0d,120d,0d)));
			assertThat(IslandUtils.getOccupyingIsland(new Location(null,-54d,120d,0d)),
					is(new Location(null,0d,120d,0d)));
			
			assertThat(IslandUtils.getOccupyingIsland(new Location(null,54d,120d,-54d)),
					is(new Location(null,0d,120d,0d)));
			assertThat(IslandUtils.getOccupyingIsland(new Location(null,0d,120d,-54d)),
					is(new Location(null,0d,120d,0d)));
			assertThat(IslandUtils.getOccupyingIsland(new Location(null,-54d,120d,-54d)),
					is(new Location(null,0d,120d,0d)));
		}
		
		//Slightly further island, all should return larger values.
		{
			assertThat(IslandUtils.getOccupyingIsland(new Location(null,56d,120d,56d)),
					is(new Location(null,110d,120d,110d)));
			assertThat(IslandUtils.getOccupyingIsland(new Location(null,0d,120d,56d)),
					is(new Location(null,0d,120d,110d)));
			assertThat(IslandUtils.getOccupyingIsland(new Location(null,-56d,120d,56d)),
					is(new Location(null,-110d,120d,110d)));
			
			assertThat(IslandUtils.getOccupyingIsland(new Location(null,56d,120d,0d)),
					is(new Location(null,110d,120d,0d)));
			assertThat(IslandUtils.getOccupyingIsland(new Location(null,-56d,120d,0d)),
					is(new Location(null,-110d,120d,0d)));
			
			assertThat(IslandUtils.getOccupyingIsland(new Location(null,56d,120d,-56d)),
					is(new Location(null,110d,120d,-110d)));
			assertThat(IslandUtils.getOccupyingIsland(new Location(null,0d,120d,-56d)),
					is(new Location(null,0d,120d,-110d)));
			assertThat(IslandUtils.getOccupyingIsland(new Location(null,-56d,120d,-56d)),
					is(new Location(null,-110d,120d,-110d)));
		}
	}
}
