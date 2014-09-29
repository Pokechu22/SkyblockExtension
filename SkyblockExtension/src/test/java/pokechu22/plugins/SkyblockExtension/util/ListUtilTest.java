package pokechu22.plugins.SkyblockExtension.util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Locale;

import org.junit.Test;
import org.bukkit.entity.EntityType;
import org.bukkit.Material;

import pokechu22.plugins.SkyblockExtension.protection.HangingType;
import pokechu22.plugins.SkyblockExtension.protection.VehicleType;

public class ListUtilTest {
	/**
	 * An enum intended for testing purposes.
	 * 
	 * @author Pokechu22
	 *
	 */
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
}
