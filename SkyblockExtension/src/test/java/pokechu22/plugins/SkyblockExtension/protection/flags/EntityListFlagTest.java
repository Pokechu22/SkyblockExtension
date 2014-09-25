package pokechu22.plugins.SkyblockExtension.protection.flags;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.bukkit.entity.EntityType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class EntityListFlagTest {
	/**
	 * Used to ensure proper exceptions are thrown in certain cases,
	 * without exiting the test.
	 * 
	 * @see http://stackoverflow.com/a/2935935/3991344
	 */
	@Rule
	public ExpectedException expected = ExpectedException.none();
	
	/**
	 * Ensures the flag type is correct.
	 */
	@Test
	public void flagTypeShouldBeCorrect() {
		EntityListFlag flag = new EntityListFlag("[]");
		assertThat(flag.getType(), is(IslandProtectionDataSetFlag.FlagType.ENTITYLIST));
	}
	
	/**
	 * Ensures flags created with [] are empty.
	 */
	@Test
	public void emptyFlagTest() {
		EntityListFlag flag = new EntityListFlag("[]");
		assertThat(flag.getValue().size(), is(0));
	}
	
	/**
	 * Tests adding to the flag.
	 */
	@Test
	public void flagAdditionTest() {
		EntityListFlag flag = new EntityListFlag("[]");
		assertThat(flag.canAddToValue(), is(true));
		//If it starts with §c, it is treated as an error.
		assertThat(flag.getValue().size(), is(0));
		assertThat(flag.addToValue("[Pig]", false), startsWith("§a"));
		assertThat(flag.getValue(), hasItem(EntityType.PIG));
	}
}
