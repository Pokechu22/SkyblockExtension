package pokechu22.plugins.SkyblockExtension.util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static junitparams.JUnitParamsRunner.$;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.bukkit.entity.EntityType;
import org.bukkit.Material;

import pokechu22.plugins.SkyblockExtension.protection.HangingType;
import pokechu22.plugins.SkyblockExtension.protection.VehicleType;

@RunWith(JUnitParamsRunner.class)
public class ListUtilTest {
	/**
	 * Used to ensure proper exceptions are thrown in certain cases,
	 * without exiting the test.
	 * 
	 * @see http://stackoverflow.com/a/2935935/3991344
	 */
	@Rule
	public ExpectedException expected = ExpectedException.none();
	
	/**
	 * An enum intended for testing purposes.
	 * 
	 * @author Pokechu22
	 *
	 */
	@Parameters
	public static enum TestEnum {
		TEST1,
		TEST2,
		TEST3;
	}
	
	/**
	 * Tests standard enum values, using the {@link TestEnum} included.
	 */
	@Test
	public void standardEnumMatchTest() {
		for (TestEnum e : TestEnum.values()) {
			//Normal case.
			assertThat(ListUtil.matchEnumValue(e.name(), TestEnum.class), is(e));
			//Lower case
			assertThat(ListUtil.matchEnumValue(e.name().toLowerCase(Locale.ENGLISH),
					TestEnum.class), is(e));
			//With leading whitespace.
			assertThat(ListUtil.matchEnumValue(" " + e.name(), TestEnum.class), is(e));
			//With trailing whitespace.
			assertThat(ListUtil.matchEnumValue(e.name() + " ", TestEnum.class), is(e));
			//With whitespace on both sides.
			assertThat(ListUtil.matchEnumValue(" " + e.name() + " ", TestEnum.class), is(e));
		}
	}
	
	/**
	 * Tests the {@link EntityType} enum.
	 */
	@Test
	public void entityTypeMatchTest() {
		for (EntityType e : EntityType.values()) {
			//Normal case.
			assertThat(ListUtil.matchEnumValue(e.name(), EntityType.class), is(e));
			//Lower case
			assertThat(ListUtil.matchEnumValue(e.name().toLowerCase(Locale.ENGLISH),
					EntityType.class), is(e));
			//With leading whitespace.
			assertThat(ListUtil.matchEnumValue(" " + e.name(), EntityType.class), is(e));
			//With trailing whitespace.
			assertThat(ListUtil.matchEnumValue(e.name() + " ", EntityType.class), is(e));
			//With whitespace on both sides.
			assertThat(ListUtil.matchEnumValue(" " + e.name() + " ", EntityType.class), is(e));
		}
	}
	
	/**
	 * Tests the {@link Material} enum.
	 */
	@Test
	public void materialMatchTest() {
		for (Material e : Material.values()) {
			//Normal case.
			assertThat(ListUtil.matchEnumValue(e.name(), Material.class), is(e));
			//Lower case
			assertThat(ListUtil.matchEnumValue(e.name().toLowerCase(Locale.ENGLISH),
					Material.class), is(e));
			//With leading whitespace.
			assertThat(ListUtil.matchEnumValue(" " + e.name(), Material.class), is(e));
			//With trailing whitespace.
			assertThat(ListUtil.matchEnumValue(e.name() + " ", Material.class), is(e));
			//With whitespace on both sides.
			assertThat(ListUtil.matchEnumValue(" " + e.name() + " ", Material.class), is(e));
		}
	}
	
	/**
	 * Tests the {@link HangingType} enum.
	 */
	@Test
	public void hangingTypeMatchTest() {
		for (HangingType e : HangingType.values()) {
			//Normal case.
			assertThat(ListUtil.matchEnumValue(e.name(), HangingType.class), is(e));
			//Lower case
			assertThat(ListUtil.matchEnumValue(e.name().toLowerCase(Locale.ENGLISH),
					HangingType.class), is(e));
			//With leading whitespace.
			assertThat(ListUtil.matchEnumValue(" " + e.name(), HangingType.class), is(e));
			//With trailing whitespace.
			assertThat(ListUtil.matchEnumValue(e.name() + " ", HangingType.class), is(e));
			//With whitespace on both sides.
			assertThat(ListUtil.matchEnumValue(" " + e.name() + " ", HangingType.class), is(e));
		}
	}
	
	/**
	 * Tests the {@link VehicleType} enum.
	 */
	@Test
	public void vehicleTypeMatchTest() {
		for (VehicleType e : VehicleType.values()) {
			//Normal case.
			assertThat(ListUtil.matchEnumValue(e.name(), VehicleType.class), is(e));
			//Lower case
			assertThat(ListUtil.matchEnumValue(e.name().toLowerCase(Locale.ENGLISH),
					VehicleType.class), is(e));
			//With leading whitespace.
			assertThat(ListUtil.matchEnumValue(" " + e.name(), VehicleType.class), is(e));
			//With trailing whitespace.
			assertThat(ListUtil.matchEnumValue(e.name() + " ", VehicleType.class), is(e));
			//With whitespace on both sides.
			assertThat(ListUtil.matchEnumValue(" " + e.name() + " ", VehicleType.class), is(e));
		}
	}
	
	/**
	 * Attempts to parse a list.
	 * @throws ParseException 
	 */
	@Test
	public void listParseTest() throws ParseException {
		String listValue = "[TEST1, TEST2]";
		
		List<TestEnum> expectedValue = new ArrayList<>();
		expectedValue.add(TestEnum.TEST1);
		expectedValue.add(TestEnum.TEST2);
		
		assertThat(ListUtil.parseList(listValue, TestEnum.class), is(expectedValue));
	}
	
	/**
	 * Attempts to parse a list with duplicate values.
	 */
	@Test
	public void duplicateValuesShouldFail() throws ParseException {
		expected.expect(ParseException.class);
		expected.expectMessage(startsWith("§c" + TestEnum.class.getSimpleName() +
				" \"TEST1\" is already entered.  (Repeat entries " +
				"are not allowed)."));
		ListUtil.parseList("[TEST1, TEST1]", TestEnum.class);
		
		fail();
	}
	
	/**
	 * Attempts to parse a list with unknown values.
	 */
	@Test
	public void unknownValuesShouldFail() throws ParseException {
		expected.expect(ParseException.class);
		expected.expectMessage(startsWith("§c" + TestEnum.class.getSimpleName() +
				" \"INVALIDTEST\" is not recognised."));
		ListUtil.parseList("[TEST1, INVALIDTEST]", TestEnum.class);
		
		fail();
	}
	
	/**
	 * Attempts to parse several lists with broken brackets.
	 * @throws ParseException 
	 */
	@Test
	@Parameters({"[[]",
				"[]]",
				"[] hi",
				"hi",
				"[h]i",
				"[hi []",
				"[[]]"})
	public void invalidListFromatShouldFail(String toTest) throws ParseException {
		expected.expect(ParseException.class);
		expected.expectMessage(startsWith("§cList format is invalid: It must start with " +
				"'[' and end with ']', and not have any '['" + 
				" or ']' anywhere else in it."));
		
		ListUtil.parseList(toTest, TestEnum.class);
	}
	
	/**
	 * Checks duplicate values that don't share an exact name.
	 * Capitalization and numeric IDs are tested.  All are using
	 * EntityType for the enum.
	 * 
	 * Note that the warning message won't quite work right.
	 * 
	 * (Note: may break if they fully remove the deprecated numeric ID
	 * stuff.  Some of these use it).
	 */
	@Test
	@Parameters(
			method="paramsFor_duplicateValuesWithDifferentNamesShouldFail"
			)
	public void duplicateValuesWithDifferentNamesShouldFail(String toTest)
			throws ParseException {
		expected.expect(ParseException.class);
		expected.expectMessage(allOf(startsWith("§c" + EntityType.class.getSimpleName() +
				" \""), containsString(" is already entered.  (Repeat entries " +
				"are not allowed).")));
		
		ListUtil.parseList(toTest, EntityType.class);
	}
	
	/**
	 * Retrieves parameters for 
	 * {@link #duplicateValuesWithDifferentNamesShouldFail(String)}.
	 * 
	 * Needed because of commas in the string.
	 * 
	 * Called via reflection, so this is actually used, but it doesn't
	 * look like it.
	 * @return
	 */
	@SuppressWarnings("unused")
	private Object[] paramsFor_duplicateValuesWithDifferentNamesShouldFail() {
		return $(
			$("[PIG,PIG]"),
			$("[PIG,pig]"),
			$("[PIG,Pig]"),
			$("[pig,pig]"),
			$("[pIG,Pig]"),
			$("[  PIG ,   pig          ]"),
			$("[90, 90]"),
			$("[90, Pig]"),
			$("[Pig, 90]"),
			$("[Pig, PIG, 90, Pig, pIG, 90]"),
			$("[Cow, Pig, Chicken, Creeper, 90]"),
			//Magma cube is known as LavaSlime by saveid.  Also 62
			$("[MAGMA_CUBE, PIG_ZOMBIE, Magma_Cube]"),
			$("[Magma_cube, lavaSlime]"),
			$("[magma_CUBE, LAVAslime]"),
			$("[Magma_Cube, 62]"),
			$("[62, PIG_ZOMBIE, PIG, MINECART_MOB_SPAWNER, MAGMA_CUBE]")
		);
	}
	
	/**
	 * Ensures empty values are ignored.
	 * Yes, this looks ugly, but it's accepted behavior.  Mainly for 
	 * the trailing comma, but other parts too.
	 * @throws ParseException 
	 */
	@Test
	public void emptyValuesShouldBeIgnored() throws ParseException {
		List<TestEnum> expectedValue = new ArrayList<>();
		//Is empty
		assertThat(ListUtil.parseList("[]", TestEnum.class), is(expectedValue));
		assertThat(ListUtil.parseList("[ ]", TestEnum.class), is(expectedValue));
		assertThat(ListUtil.parseList("[,]", TestEnum.class), is(expectedValue));
		assertThat(ListUtil.parseList("[, ]", TestEnum.class), is(expectedValue));
		assertThat(ListUtil.parseList("[ , ]", TestEnum.class), is(expectedValue));
		assertThat(ListUtil.parseList("[,,]", TestEnum.class), is(expectedValue));
		
		//Has only Test1
		expectedValue.add(TestEnum.TEST1);
		
		assertThat(ListUtil.parseList("[TEST1]", TestEnum.class), is(expectedValue));
		assertThat(ListUtil.parseList("[TEST1,]", TestEnum.class), is(expectedValue));
		assertThat(ListUtil.parseList("[TEST1, ]", TestEnum.class), is(expectedValue));
		assertThat(ListUtil.parseList("[TEST1 ,]", TestEnum.class), is(expectedValue));
		assertThat(ListUtil.parseList("[TEST1 , ]", TestEnum.class), is(expectedValue));
		assertThat(ListUtil.parseList("[,TEST1]", TestEnum.class), is(expectedValue));
		assertThat(ListUtil.parseList("[, TEST1]", TestEnum.class), is(expectedValue));
		assertThat(ListUtil.parseList("[ ,TEST1]", TestEnum.class), is(expectedValue));
		assertThat(ListUtil.parseList("[ , TEST1]", TestEnum.class), is(expectedValue));
		assertThat(ListUtil.parseList("[,TEST1,]", TestEnum.class), is(expectedValue));
		assertThat(ListUtil.parseList("[,,,TEST1,,,]", TestEnum.class), is(expectedValue));
	}
}
