package pokechu22.plugins.SkyblockExtension.protection.flags;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the BooleanFlag.  
 * 
 * This seems over the top, but I've had issues with this...
 * 
 * @author Pokechu22
 */
@RunWith(JUnitParamsRunner.class)
public class BooleanFlagTest {
	/**
	 * Checks that all valid values work.
	 * 
	 * @param flagValue the value of the flag to test.
	 */
	@Test
	@Parameters({"false", "true"})
	public void validValuesShouldWork(String flagValue) throws Exception {
		//Check that it is valid with the constructor.
		BooleanFlag flag = new BooleanFlag(flagValue);
		
		//Check that it is valid with setValue.
		assertThat(flag.setValue(flagValue), startsWith("§a"));
	}
	
	/**
	 * Checks that illegal values are illegal in the constructor.
	 * 
	 * @param flagValue the value of the flag to test.
	 */
	@Test(expected=IllegalArgumentException.class)
	@Parameters({"", "_", "adsf", "mabye", "yes", "no"})
	public void invalidValuesShouldFailInTheConstructor(String flagValue)
			throws Exception {
		//Check that it is valid with the constructor.
		new BooleanFlag(flagValue);
	}
	
	/**
	 * Checks that it is illegal to use setValue with invalid values.
	 * (Illegal is defined as starts with §c)
	 * 
	 * @param flagValue the value of the flag to test.
	 */
	@SuppressWarnings("deprecation")
	@Test
	@Parameters({"", "_", "adsf", "mabye", "yes", "no"})
	public void invalidValuesShouldFailWithSetValue(String flagValue) {
			//Check that it is valid with setValue.
			assertThat(new BooleanFlag().setValue(flagValue),
					startsWith("§c"));
	}
	
	/**
	 * Tests to make sure that the result of serialization with NBT is 
	 * the same as the input value.
	 * 
	 * @param flagValue the value of the flag to test.
	 */
	@SuppressWarnings("deprecation")
	@Test
	@Parameters({"false", "true"})
	public void NBTSerializationShouldMatch(String flagValue)
			throws Exception {
		BooleanFlag flag = new BooleanFlag(flagValue);
		
		BooleanFlag serialized = new BooleanFlag();
		serialized.deserializeFromNBT(flag.serializeToNBT(""));
		
		assertThat(serialized, is(flag));
	}
	
	/**
	 * Ensures that {@link BooleanFlag#preformAction(String, String[])}
	 * works properly with "set" as an action.
	 */
	@Test
	public void setActionShouldSet() {
		BooleanFlag flag = new BooleanFlag("false");
		
		assertThat(flag.getValue(), is(false));
		flag.preformAction("set", new String[]{"true"});
		assertThat(flag.getValue(), is(true));
		
		flag = new BooleanFlag("true");
		
		assertThat(flag.getValue(), is(true));
		flag.preformAction("set", new String[]{"false"});
		assertThat(flag.getValue(), is(false));
	}
	
	/**
	 * Checks that {@link BooleanFlag#setValue(String)} works properly. 
	 */
	@Test
	public void setValueShouldSetValue() {
		BooleanFlag flag = new BooleanFlag("false");
		
		assertThat(flag.getValue(), is(false));
		flag.setValue("true");
		assertThat(flag.getValue(), is(true));
		
		flag = new BooleanFlag("true");
		
		assertThat(flag.getValue(), is(true));
		flag.setValue("false");
		assertThat(flag.getValue(), is(false));
	}
}
