package pokechu22.plugins.SkyblockExtension.protection.flags;

import static org.bukkit.entity.EntityType.*;
import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.bukkit.entity.EntityType;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the EntityListFlag.  
 * 
 * This seems over the top, but I've had issues with this...
 * 
 * @author Pokechu22
 */
@RunWith(JUnitParamsRunner.class)
public class EntityListFlagTest {
	/**
	 * Checks to make sure that valid values are, in fact, valid.
	 * 
	 * @param param
	 * @param expected
	 */
	@Test
	@Parameters(method="parametersFor_validValuesShouldWork")
	public void validValuesShouldWork(String param, EntityType[] expected) {
		EntityListFlag flag = new EntityListFlag(param);
		
		assertThat(flag.value, is(Arrays.asList(expected)));
		
		flag = new EntityListFlag("[]");
		
		flag.setValue(param);
		assertThat(flag.value, is(Arrays.asList(expected)));
	}
	
	/**
	 * Parameters for {@link #validValuesShouldWork(String, EntityType[])}.
	 */
	Object[] parametersFor_validValuesShouldWork() {
		return $(
				$("[]", new EntityType[0]), //Empty is valid.
				$("[ARROW]", new EntityType[]{ARROW}), //Normal single.
				$("[ARROW, PIG]", new EntityType[]{ARROW, PIG}), //Normal 2.
				$("[arrow]", new EntityType[]{ARROW}), //Case-sensitivity.
				$("[Arrow]", new EntityType[]{ARROW}), //Case-sensitivity.
				$("[ArRoW]", new EntityType[]{ARROW}), //Case-sensitivity.
				$("[aRrOw]", new EntityType[]{ARROW}) //Case-sensitivity.
		);
	}
	
	/**
	 * Checks that invalid parameters are invalid.
	 * @param param
	 */
	@Test(expected=IllegalArgumentException.class)
	@Parameters(method="parametersFor_invalidValuesShouldNotWork")
	public void invalidValuesShouldNotWork(String param) {
		//Validate setvalue first, as the latter should throw an exception.
		EntityListFlag flag = new EntityListFlag("[]");
		
		assertThat(flag.setValue(param), startsWith("§c"));
		
		//Validate the constructor, now.  An exception should be thrown. 
		flag = new EntityListFlag(param); 
		
		//If no exception was thrown, the test fails.
	}
	
	/**
	 * Parameters for {@link #invalidValuesShouldNotWork(String)}.
	 */
	Object[] parametersFor_invalidValuesShouldNotWork() {
		return $(
				$(""), //Improper list
				$("adsf"), //Improper list
				$("[PIG, PIG]"), //Duplicate value
				$("[PIG, COW, PIG]"), //Duplicate value
				$("[ARROW, PIG, ARROW]"), //Duplicate value
				$("[NONEXISTANT]"), //Nonexistent value
				$("[ARROW, NONEXISTANT]") //Nonexistent value
		);
	}
	
	/**
	 * Tests that {@link EntityListFlag#preformAction(String, String...)}
	 * works with set as an action for valid actions, and does not accept
	 * invalid actions.
	 * 
	 * @param param The parameter to test
	 * @param expected The expected final value, or null if invalid.
	 */
	@Test
	@Parameters(method="parametersFor_setValueActionShouldSetValue")
	public void setValueActionShouldSetValue(String param, 
			EntityType[] expected) {
		EntityListFlag flag = new EntityListFlag("[]");
		
		String result = flag.preformAction("set", param);
		
		if (expected == null) {
			assertThat(result, startsWith("§c"));
		} else {
			assertThat(result, startsWith("§a"));
			assertThat(flag.value, is(Arrays.asList(expected)));
		}
	}
	
	/**
	 * Parameters for 
	 * {@link #setValueActionShouldSetValue(String, EntityType[])}.
	 */
	Object[] parametersFor_setValueActionShouldSetValue() {
		return $(
				//Valid. 
				
				//Empty.
				$("[]", new EntityType[0]),
				//Normal.
				$("[ARROW]", new EntityType[]{ARROW}),
				$("[ARROW, PIG]", new EntityType[]{ARROW, PIG}),
				//Case-sensitivity.
				$("[arrow]", new EntityType[]{ARROW}),
				$("[Arrow]", new EntityType[]{ARROW}),
				$("[ArRoW]", new EntityType[]{ARROW}),
				$("[aRrOw]", new EntityType[]{ARROW}),
				
				//Invalid.
				
				//Improper list
				$("", null), 
				$("adsf", null),
				//Duplicate values
				$("[PIG, PIG]", null),
				$("[PIG, COW, PIG]", null),
				$("[ARROW, PIG, ARROW]", null),
				//Nonexistent value
				$("[NONEXISTANT]", null),
				$("[ARROW, NONEXISTANT]", null)
		);
	}
}
