package pokechu22.plugins.SkyblockExtension.util;

import static pokechu22.plugins.SkyblockExtension.util.StringUtil.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

/**
 * Test for StringUtils.
 *
 * @author pokechu22
 */
public class StringUtilTest {
	/**
	 * Tests the 2-argument trailOff method
	 * {@link StringUtil#trailOff(String, int, String)}.
	 * 
	 * Performs the tests shown in the JavaDoc.
	 */
	@Test
	public void trailOffTwoArgsTest() {
		assertThat(trailOff("Hello, world!", 80), is("Hello, world!"));
		assertThat(trailOff("Hello, world!", 10), is("Hello, ..."));
		assertThat(trailOff("Not Hello World", 11), is("Not Hell..."));
	}
	
	/**
	 * Tests 2-argument trailOff method
	 * {@link StringUtil#trailOff(String, int, String)} throwing an
	 * exception.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void trailOffTwoArgsExceptionTest() {
		trailOff("Arbitrary String", 1);
	}
	
	/**
	 * Tests the 3-argument trailOff method
	 * {@link StringUtil#trailOff(String, int, String)}.
	 * 
	 * Performs the tests shown in the JavaDoc.
	 */
	@Test
	public void trailOffThreeArgsTest() {
		assertThat(trailOff("Hello, world!", 80, "..."), is("Hello, world!"));
		assertThat(trailOff("Hello, world!", 10, "..."), is("Hello, ..."));
		assertThat(trailOff("Not Hello World", 8, ""), is("Not Hell"));
	}
	
	/**
	 * Tests 3-argument trailOff method
	 * {@link StringUtil#trailOff(String, int, String)} throwing an
	 * exception.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void trailOffThreeArgsExceptionTest() {
		trailOff("Irelivant string", 1, "...");
	}
}
