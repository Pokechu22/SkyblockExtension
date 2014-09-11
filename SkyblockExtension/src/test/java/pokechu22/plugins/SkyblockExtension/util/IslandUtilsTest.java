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
	
	/**
	 * Tests the {@link IslandUtils#getNearestIslandLocalX(Location)}
	 * method.
	 */
	@Test
	public void getNearestIslandLocalXTest() {
		assertThat(IslandUtils.getNearestIslandLocalX(
				new Location(null, 0d, 0d, 0d)), is(0));
		assertThat(IslandUtils.getNearestIslandLocalX(
				new Location(null, 56d, 0d, 0d)), is(1));
		assertThat(IslandUtils.getNearestIslandLocalX(
				new Location(null, -56d, 0d, 0d)), is(-1));
		
		//More centralized tests.
		assertThat(IslandUtils.getNearestIslandLocalX(
				new Location(null, 110d, 0d, 0d)), is(1));
		assertThat(IslandUtils.getNearestIslandLocalX(
				new Location(null, 164d, 0d, 0d)), is(1));
		
		assertThat(IslandUtils.getNearestIslandLocalX(
				new Location(null, -110d, 0d, 0d)), is(-1));
		assertThat(IslandUtils.getNearestIslandLocalX(
				new Location(null, -164d, 0d, 0d)), is(-1));
		
		//Further distance.
		assertThat(IslandUtils.getNearestIslandLocalX(
				new Location(null, 166d, 0d, 0d)), is(2));
		assertThat(IslandUtils.getNearestIslandLocalX(
				new Location(null, 220d, 0d, 0d)), is(2));
		assertThat(IslandUtils.getNearestIslandLocalX(
				new Location(null, 274d, 0d, 0d)), is(2));
		
		assertThat(IslandUtils.getNearestIslandLocalX(
				new Location(null, -166d, 0d, 0d)), is(-2));
		assertThat(IslandUtils.getNearestIslandLocalX(
				new Location(null, -220d, 0d, 0d)), is(-2));
		assertThat(IslandUtils.getNearestIslandLocalX(
				new Location(null, -274d, 0d, 0d)), is(-2));
		
		//Tests showing that z is ignored.
		assertThat(IslandUtils.getNearestIslandLocalX(
				new Location(null, 0d, 0d, 100000d)), is(0));
		assertThat(IslandUtils.getNearestIslandLocalX(
				new Location(null, 56d, 0d, 100000d)), is(1));
		assertThat(IslandUtils.getNearestIslandLocalX(
				new Location(null, -56d, 0d, -10000d)), is(-1));
		
	}
	
	/**
	 * Tests the {@link IslandUtils#getNearestIslandLocalZ(Location)}
	 * method.
	 */
	@Test
	public void getNearestIslandLocalZTest() {
		assertThat(IslandUtils.getNearestIslandLocalZ(
				new Location(null, 0d, 0d, 0d)), is(0));
		assertThat(IslandUtils.getNearestIslandLocalZ(
				new Location(null, 0d, 0d, 56d)), is(1));
		assertThat(IslandUtils.getNearestIslandLocalZ(
				new Location(null, 0d, 0d, -56d)), is(-1));
		
		//More centralized tests.
		assertThat(IslandUtils.getNearestIslandLocalZ(
				new Location(null, 0d, 0d, 110d)), is(1));
		assertThat(IslandUtils.getNearestIslandLocalZ(
				new Location(null, 0d, 0d, 164d)), is(1));
		
		assertThat(IslandUtils.getNearestIslandLocalZ(
				new Location(null, 0d, 0d, -110d)), is(-1));
		assertThat(IslandUtils.getNearestIslandLocalZ(
				new Location(null, 0d, 0d, -164d)), is(-1));
		
		//Further distance.
		assertThat(IslandUtils.getNearestIslandLocalZ(
				new Location(null, 0d, 0d, 166d)), is(2));
		assertThat(IslandUtils.getNearestIslandLocalZ(
				new Location(null, 0d, 0d, 220d)), is(2));
		assertThat(IslandUtils.getNearestIslandLocalZ(
				new Location(null, 0d, 0d, 274d)), is(2));
		
		assertThat(IslandUtils.getNearestIslandLocalZ(
				new Location(null, 0d, 0d, -166d)), is(-2));
		assertThat(IslandUtils.getNearestIslandLocalZ(
				new Location(null, 0d, 0d, -220d)), is(-2));
		assertThat(IslandUtils.getNearestIslandLocalZ(
				new Location(null, 0d, 0d, -274d)), is(-2));
		
		//Tests showing that z is ignored.
		assertThat(IslandUtils.getNearestIslandLocalZ(
				new Location(null, 100000d, 0d, 0d)), is(0));
		assertThat(IslandUtils.getNearestIslandLocalZ(
				new Location(null, 100000d, 0d, 56d)), is(1));
		assertThat(IslandUtils.getNearestIslandLocalZ(
				new Location(null, -10000d, 0d, -56d)), is(-1));
		
	}
}
