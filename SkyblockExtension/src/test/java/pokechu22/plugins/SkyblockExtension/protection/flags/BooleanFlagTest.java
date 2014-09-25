package pokechu22.plugins.SkyblockExtension.protection.flags;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import pokechu22.plugins.SkyblockExtension.protection.flags.IslandProtectionDataSetFlag.FlagType;

/**
 * Tests the functionality of BooleanFlags.
 *
 * @author Pokechu22
 *
 */
public class BooleanFlagTest {
	/**
	 * Ensures that a boolean flag's type is boolean flag.
	 */
	@Test
	public void flagTypeShouldBeCorrect() {
		BooleanFlag flag = new BooleanFlag("false");
		assertThat(flag.getType(), is(FlagType.BOOLEAN));
	}
	
	/**
	 * Tests normal values - ones that should not cause errors.
	 */
	@Test
	public void testNormalValues() {
		BooleanFlag flag = new BooleanFlag("false");
		assertThat(flag.getValue(), is(false));
		flag.setValue("true");
		assertThat(flag.getValue(), is(true));
		flag.setValue("false");
		assertThat(flag.getValue(), is(false));
	}
	
	/**
	 * Tests case sensitivity.
	 */
	@Test
	public void testCaseSensitivity() {
		BooleanFlag flag = new BooleanFlag("false");
		assertThat(flag.getValue(), is(false));
		flag.setValue("true");
		assertThat(flag.getValue(), is(true));
		flag.setValue("false");
		assertThat(flag.getValue(), is(false));
		flag.setValue("TRUE");
		assertThat(flag.getValue(), is(true));
		flag.setValue("FALSE");
		assertThat(flag.getValue(), is(false));
		flag.setValue("True");
		assertThat(flag.getValue(), is(true));
		flag.setValue("False");
		assertThat(flag.getValue(), is(false));
		flag.setValue("tRUE");
		assertThat(flag.getValue(), is(true));
		flag.setValue("fALSE");
		assertThat(flag.getValue(), is(false));
		flag.setValue("TrUe");
		assertThat(flag.getValue(), is(true));
		flag.setValue("FaLsE");
		assertThat(flag.getValue(), is(false));
		//And now constructors.
		flag = new BooleanFlag("false");
		assertThat(flag.getValue(), is(false));
		flag = new BooleanFlag("true");
		assertThat(flag.getValue(), is(true));
		flag = new BooleanFlag("FALSE");
		assertThat(flag.getValue(), is(false));
		flag = new BooleanFlag("TRUE");
		assertThat(flag.getValue(), is(true));
		flag = new BooleanFlag("False");
		assertThat(flag.getValue(), is(false));
		flag = new BooleanFlag("True");
		assertThat(flag.getValue(), is(true));
		flag = new BooleanFlag("fALSE");
		assertThat(flag.getValue(), is(false));
		flag = new BooleanFlag("tRUE");
		assertThat(flag.getValue(), is(true));
		flag = new BooleanFlag("FaLsE");
		assertThat(flag.getValue(), is(false));
		flag = new BooleanFlag("TrUe");
		assertThat(flag.getValue(), is(true));
	}
	
	/**
	 * Tests invalid null value.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testNullValue() {
		new BooleanFlag(null);
	}
	
	/**
	 * Tests another invalid value
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidValue() {
		new BooleanFlag("potato");
	}
	
	/**
	 * Tests adding to the flag.
	 */
	@Test
	public void addingToFlagShouldNotBeAllowed() {
		BooleanFlag flag = new BooleanFlag("false");
		assertThat(flag.canAddToValue(), is(false));
		//If it starts with §c, it is treated as an error.
		assertThat(flag.addToValue("hi", false), startsWith("§c"));
	}
	
	/**
	 * Tests direct serialization.
	 * The value returned from getSerializedValue should be usable
	 * with setValue.
	 */
	@Test
	public void serializationTest() {
		BooleanFlag falseFlag = new BooleanFlag("false");
		assertThat(new BooleanFlag(falseFlag.getSerializedValue()), is(falseFlag));
		
		BooleanFlag trueFlag = new BooleanFlag("true");
		assertThat(new BooleanFlag(trueFlag.getSerializedValue()), is(trueFlag));
	}
}
