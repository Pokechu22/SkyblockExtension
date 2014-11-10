package pokechu22.plugins.SkyblockExtension.protection.flags;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static junitparams.JUnitParamsRunner.$;

import java.text.ParseException;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the {@link MaterialToMaterialListMapFlag}
 * @author Pokechu22
 *
 */
@RunWith(JUnitParamsRunner.class)
public class MaterialToMaterialListMapFlagTest {
	/**
	 * Tests creation of an empty value.
	 */
	@Test
	public void emptyValueTest() {
		new MaterialToMaterialListMapFlag("[]");
	}
	
	/**
	 * Tests creation of various valid values.
	 */
	@Test
	@Parameters(method="paramsFor_validInputsShouldBeAccepted")
	public void validInputsShouldBeAccepted(String value) {
		new MaterialToMaterialListMapFlag(value);
	}
	
	/**
	 * Retrieves parameters for 
	 * {@link #paramsFor_validInputsShouldBeAccepte(String)}.
	 * 
	 * Needed because of commas in the string.
	 * 
	 * Called via reflection, so this is actually used, but it doesn't
	 * look like it.
	 * @return
	 */
	@SuppressWarnings("unused")
	private Object[] paramsFor_validInputsShouldBeAccepted() {
		return $(
				$("[]"), //Empty value
				$("[]")
			);
	}
	
	/**
	 * Tests creation of various valid values.
	 */
	@Test(expected = Exception.class)
	@Parameters(method="paramsFor_invalidInputsShouldNotBeAccepted")
	public void invalidInputsShouldNotBeAccepted(String value) {
		new MaterialToMaterialListMapFlag(value);
	}
	
	/**
	 * Retrieves parameters for 
	 * {@link #invalidInputsShouldNotBeAccepted(String)}.
	 * 
	 * Needed because of commas in the string.
	 * 
	 * Called via reflection, so this is actually used, but it doesn't
	 * look like it.
	 * @return
	 */
	@SuppressWarnings("unused")
	private Object[] paramsFor_invalidInputsShouldNotBeAccepted() {
		return $(
				new Object[]{null}, //Null.  Duh.
				$(""), //Empty string.
				$("Hello World!"), //Just invalid.
				$()
			);
	}
}
