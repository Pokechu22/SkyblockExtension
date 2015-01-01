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
				$("[]", new EntityType[0]),
				$("[ARROW]", new EntityType[]{ARROW}),
				$("[ARROW, PIG]", new EntityType[]{ARROW, PIG})
		);
	}
}
