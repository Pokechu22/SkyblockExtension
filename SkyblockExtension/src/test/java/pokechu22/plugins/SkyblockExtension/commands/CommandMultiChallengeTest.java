package pokechu22.plugins.SkyblockExtension.commands;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static junitparams.JUnitParamsRunner.$;
import static pokechu22.plugins.SkyblockExtension.commands
		.CommandMultiChallenge.MultiChallengeRoundingMode.*;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

import pokechu22.plugins.SkyblockExtension.commands
		.CommandMultiChallenge.MultiChallengeRoundingMode;

@RunWith(JUnitParamsRunner.class)
public class CommandMultiChallengeTest {
	@Test
	@Parameters(method="paramsFor_roundModeTest")
	public void roundModeTest(MultiChallengeRoundingMode mode,
			int quantity, int times, float tax, int expected) {
		assertThat(mode.apply(quantity, times, tax), is(expected));
	}
	
	@SuppressWarnings("unused")
	private Object[] paramsFor_roundModeTest() {
		return $(
			//Round up lossy
			$(ROUND_UP_LOSSY, 0, 1, 1f, 0),
			$(ROUND_UP_LOSSY, 0, 1, .9f, 0),
			$(ROUND_UP_LOSSY, 0, 1, .5f, 0),
			$(ROUND_UP_LOSSY, 0, 1, .1f, 0),
			$(ROUND_UP_LOSSY, 0, 1, 0f, 0),
			$(ROUND_UP_LOSSY, 1, 1, 1f, 1),
			$(ROUND_UP_LOSSY, 1, 1, .9f, 1),
			$(ROUND_UP_LOSSY, 1, 1, .5f, 1),
			$(ROUND_UP_LOSSY, 1, 1, .1f, 1),
			$(ROUND_UP_LOSSY, 1, 1, 0f, 0),
			$(ROUND_UP_LOSSY, 2, 1, 1f, 2),
			$(ROUND_UP_LOSSY, 2, 1, .9f, 2),
			$(ROUND_UP_LOSSY, 2, 1, .5f, 1),
			$(ROUND_UP_LOSSY, 2, 1, .1f, 1),
			$(ROUND_UP_LOSSY, 2, 1, 0f, 0),
			$(ROUND_UP_LOSSY, 1, 0, 1f, 0),
			$(ROUND_UP_LOSSY, 1, 0, .9f, 0),
			$(ROUND_UP_LOSSY, 1, 0, .5f, 0),
			$(ROUND_UP_LOSSY, 1, 0, .1f, 0),
			$(ROUND_UP_LOSSY, 1, 0, 0f, 0),
			$(ROUND_UP_LOSSY, 1, 1, 1f, 1),
			$(ROUND_UP_LOSSY, 1, 1, .9f, 1),
			$(ROUND_UP_LOSSY, 1, 1, .5f, 1),
			$(ROUND_UP_LOSSY, 1, 1, .1f, 1),
			$(ROUND_UP_LOSSY, 1, 1, 0f, 0),
			$(ROUND_UP_LOSSY, 1, 2, 1f, 2),
			$(ROUND_UP_LOSSY, 1, 2, .9f, 2),
			$(ROUND_UP_LOSSY, 1, 2, .5f, 2),
			$(ROUND_UP_LOSSY, 1, 2, .1f, 2),
			$(ROUND_UP_LOSSY, 1, 2, 0f, 0),
			//Round up keep
			$(ROUND_UP_KEEP, 0, 1, 1f, 0),
			$(ROUND_UP_KEEP, 0, 1, .9f, 0),
			$(ROUND_UP_KEEP, 0, 1, .5f, 0),
			$(ROUND_UP_KEEP, 0, 1, .1f, 0),
			$(ROUND_UP_KEEP, 0, 1, 0f, 0),
			$(ROUND_UP_KEEP, 1, 1, 1f, 1),
			$(ROUND_UP_KEEP, 1, 1, .9f, 1),
			$(ROUND_UP_KEEP, 1, 1, .5f, 1),
			$(ROUND_UP_KEEP, 1, 1, .1f, 1),
			$(ROUND_UP_KEEP, 1, 1, 0f, 0),
			$(ROUND_UP_KEEP, 2, 1, 1f, 2),
			$(ROUND_UP_KEEP, 2, 1, .9f, 2),
			$(ROUND_UP_KEEP, 2, 1, .5f, 1),
			$(ROUND_UP_KEEP, 2, 1, .1f, 1),
			$(ROUND_UP_KEEP, 2, 1, 0f, 0),
			$(ROUND_UP_KEEP, 1, 0, 1f, 0),
			$(ROUND_UP_KEEP, 1, 0, .9f, 0),
			$(ROUND_UP_KEEP, 1, 0, .5f, 0),
			$(ROUND_UP_KEEP, 1, 0, .1f, 0),
			$(ROUND_UP_KEEP, 1, 0, 0f, 0),
			$(ROUND_UP_KEEP, 1, 1, 1f, 1),
			$(ROUND_UP_KEEP, 1, 1, .9f, 1),
			$(ROUND_UP_KEEP, 1, 1, .5f, 1),
			$(ROUND_UP_KEEP, 1, 1, .1f, 1),
			$(ROUND_UP_KEEP, 1, 1, 0f, 0),
			$(ROUND_UP_KEEP, 1, 2, 1f, 2),
			$(ROUND_UP_KEEP, 1, 2, .9f, 2),
			$(ROUND_UP_KEEP, 1, 2, .5f, 1),
			$(ROUND_UP_KEEP, 1, 2, .1f, 1),
			$(ROUND_UP_KEEP, 1, 2, 0f, 0),
			//Round nearest lossy
			$(ROUND_NEAREST_LOSSY, 0, 1, 1f, 0),
			$(ROUND_NEAREST_LOSSY, 0, 1, .9f, 0),
			$(ROUND_NEAREST_LOSSY, 0, 1, .5f, 0),
			$(ROUND_NEAREST_LOSSY, 0, 1, .1f, 0),
			$(ROUND_NEAREST_LOSSY, 0, 1, 0f, 0),
			$(ROUND_NEAREST_LOSSY, 1, 1, 1f, 1),
			$(ROUND_NEAREST_LOSSY, 1, 1, .9f, 1),
			$(ROUND_NEAREST_LOSSY, 1, 1, .5f, 1),
			$(ROUND_NEAREST_LOSSY, 1, 1, .1f, 0),
			$(ROUND_NEAREST_LOSSY, 1, 1, 0f, 0),
			$(ROUND_NEAREST_LOSSY, 2, 1, 1f, 2),
			$(ROUND_NEAREST_LOSSY, 2, 1, .9f, 2),
			$(ROUND_NEAREST_LOSSY, 2, 1, .5f, 1),
			$(ROUND_NEAREST_LOSSY, 2, 1, .1f, 0),
			$(ROUND_NEAREST_LOSSY, 2, 1, 0f, 0),
			$(ROUND_NEAREST_LOSSY, 1, 0, 1f, 0),
			$(ROUND_NEAREST_LOSSY, 1, 0, .9f, 0),
			$(ROUND_NEAREST_LOSSY, 1, 0, .5f, 0),
			$(ROUND_NEAREST_LOSSY, 1, 0, .1f, 0),
			$(ROUND_NEAREST_LOSSY, 1, 0, 0f, 0),
			$(ROUND_NEAREST_LOSSY, 1, 1, 1f, 1),
			$(ROUND_NEAREST_LOSSY, 1, 1, .9f, 1),
			$(ROUND_NEAREST_LOSSY, 1, 1, .5f, 1),
			$(ROUND_NEAREST_LOSSY, 1, 1, .1f, 0),
			$(ROUND_NEAREST_LOSSY, 1, 1, 0f, 0),
			$(ROUND_NEAREST_LOSSY, 1, 2, 1f, 2),
			$(ROUND_NEAREST_LOSSY, 1, 2, .9f, 2),
			$(ROUND_NEAREST_LOSSY, 1, 2, .5f, 2),
			$(ROUND_NEAREST_LOSSY, 1, 2, .1f, 0),
			$(ROUND_NEAREST_LOSSY, 1, 2, 0f, 0),
			//Round nearest keep
			$(ROUND_NEAREST_KEEP, 0, 1, 1f, 0),
			$(ROUND_NEAREST_KEEP, 0, 1, .9f, 0),
			$(ROUND_NEAREST_KEEP, 0, 1, .5f, 0),
			$(ROUND_NEAREST_KEEP, 0, 1, .1f, 0),
			$(ROUND_NEAREST_KEEP, 0, 1, 0f, 0),
			$(ROUND_NEAREST_KEEP, 1, 1, 1f, 1),
			$(ROUND_NEAREST_KEEP, 1, 1, .9f, 1),
			$(ROUND_NEAREST_KEEP, 1, 1, .5f, 1),
			$(ROUND_NEAREST_KEEP, 1, 1, .1f, 0),
			$(ROUND_NEAREST_KEEP, 1, 1, 0f, 0),
			$(ROUND_NEAREST_KEEP, 2, 1, 1f, 2),
			$(ROUND_NEAREST_KEEP, 2, 1, .9f, 2),
			$(ROUND_NEAREST_KEEP, 2, 1, .5f, 1),
			$(ROUND_NEAREST_KEEP, 2, 1, .1f, 0),
			$(ROUND_NEAREST_KEEP, 2, 1, 0f, 0),
			$(ROUND_NEAREST_KEEP, 1, 0, 1f, 0),
			$(ROUND_NEAREST_KEEP, 1, 0, .9f, 0),
			$(ROUND_NEAREST_KEEP, 1, 0, .5f, 0),
			$(ROUND_NEAREST_KEEP, 1, 0, .1f, 0),
			$(ROUND_NEAREST_KEEP, 1, 0, 0f, 0),
			$(ROUND_NEAREST_KEEP, 1, 1, 1f, 1),
			$(ROUND_NEAREST_KEEP, 1, 1, .9f, 1),
			$(ROUND_NEAREST_KEEP, 1, 1, .5f, 1),
			$(ROUND_NEAREST_KEEP, 1, 1, .1f, 0),
			$(ROUND_NEAREST_KEEP, 1, 1, 0f, 0),
			$(ROUND_NEAREST_KEEP, 1, 2, 1f, 2),
			$(ROUND_NEAREST_KEEP, 1, 2, .9f, 2),
			$(ROUND_NEAREST_KEEP, 1, 2, .5f, 1),
			$(ROUND_NEAREST_KEEP, 1, 2, .1f, 0),
			$(ROUND_NEAREST_KEEP, 1, 2, 0f, 0),
			//Round down lossy
			$(ROUND_DOWN_LOSSY, 0, 1, 1f, 0),
			$(ROUND_DOWN_LOSSY, 0, 1, .9f, 0),
			$(ROUND_DOWN_LOSSY, 0, 1, .5f, 0),
			$(ROUND_DOWN_LOSSY, 0, 1, .1f, 0),
			$(ROUND_DOWN_LOSSY, 0, 1, 0f, 0),
			$(ROUND_DOWN_LOSSY, 1, 1, 1f, 1),
			$(ROUND_DOWN_LOSSY, 1, 1, .9f, 0),
			$(ROUND_DOWN_LOSSY, 1, 1, .5f, 0),
			$(ROUND_DOWN_LOSSY, 1, 1, .1f, 0),
			$(ROUND_DOWN_LOSSY, 1, 1, 0f, 0),
			$(ROUND_DOWN_LOSSY, 2, 1, 1f, 2),
			$(ROUND_DOWN_LOSSY, 2, 1, .9f, 1),
			$(ROUND_DOWN_LOSSY, 2, 1, .5f, 1),
			$(ROUND_DOWN_LOSSY, 2, 1, .1f, 0),
			$(ROUND_DOWN_LOSSY, 2, 1, 0f, 0),
			$(ROUND_DOWN_LOSSY, 1, 0, 1f, 0),
			$(ROUND_DOWN_LOSSY, 1, 0, .9f, 0),
			$(ROUND_DOWN_LOSSY, 1, 0, .5f, 0),
			$(ROUND_DOWN_LOSSY, 1, 0, .1f, 0),
			$(ROUND_DOWN_LOSSY, 1, 0, 0f, 0),
			$(ROUND_DOWN_LOSSY, 1, 1, 1f, 1),
			$(ROUND_DOWN_LOSSY, 1, 1, .9f, 0),
			$(ROUND_DOWN_LOSSY, 1, 1, .5f, 0),
			$(ROUND_DOWN_LOSSY, 1, 1, .1f, 0),
			$(ROUND_DOWN_LOSSY, 1, 1, 0f, 0),
			$(ROUND_DOWN_LOSSY, 1, 2, 1f, 2),
			$(ROUND_DOWN_LOSSY, 1, 2, .9f, 0),
			$(ROUND_DOWN_LOSSY, 1, 2, .5f, 0),
			$(ROUND_DOWN_LOSSY, 1, 2, .1f, 0),
			$(ROUND_DOWN_LOSSY, 1, 2, 0f, 0),
			//Round down keep
			$(ROUND_DOWN_KEEP, 0, 1, 1f, 0),
			$(ROUND_DOWN_KEEP, 0, 1, .9f, 0),
			$(ROUND_DOWN_KEEP, 0, 1, .5f, 0),
			$(ROUND_DOWN_KEEP, 0, 1, .1f, 0),
			$(ROUND_DOWN_KEEP, 0, 1, 0f, 0),
			$(ROUND_DOWN_KEEP, 1, 1, 1f, 1),
			$(ROUND_DOWN_KEEP, 1, 1, .9f, 0),
			$(ROUND_DOWN_KEEP, 1, 1, .5f, 0),
			$(ROUND_DOWN_KEEP, 1, 1, .1f, 0),
			$(ROUND_DOWN_KEEP, 1, 1, 0f, 0),
			$(ROUND_DOWN_KEEP, 2, 1, 1f, 2),
			$(ROUND_DOWN_KEEP, 2, 1, .9f, 1),
			$(ROUND_DOWN_KEEP, 2, 1, .5f, 1),
			$(ROUND_DOWN_KEEP, 2, 1, .1f, 0),
			$(ROUND_DOWN_KEEP, 2, 1, 0f, 0),
			$(ROUND_DOWN_KEEP, 1, 0, 1f, 0),
			$(ROUND_DOWN_KEEP, 1, 0, .9f, 0),
			$(ROUND_DOWN_KEEP, 1, 0, .5f, 0),
			$(ROUND_DOWN_KEEP, 1, 0, .1f, 0),
			$(ROUND_DOWN_KEEP, 1, 0, 0f, 0),
			$(ROUND_DOWN_KEEP, 1, 1, 1f, 1),
			$(ROUND_DOWN_KEEP, 1, 1, .9f, 0),
			$(ROUND_DOWN_KEEP, 1, 1, .5f, 0),
			$(ROUND_DOWN_KEEP, 1, 1, .1f, 0),
			$(ROUND_DOWN_KEEP, 1, 1, 0f, 0),
			$(ROUND_DOWN_KEEP, 1, 2, 1f, 2),
			$(ROUND_DOWN_KEEP, 1, 2, .9f, 1),
			$(ROUND_DOWN_KEEP, 1, 2, .5f, 1),
			$(ROUND_DOWN_KEEP, 1, 2, .1f, 0),
			$(ROUND_DOWN_KEEP, 1, 2, 0f, 0)
		);
	}
	
	@Test
	@Parameters(method="paramsFor_roundModeMatchTest")
	public void roundModeMatchTest(String mode, 
			MultiChallengeRoundingMode expected) {
		assertThat(MultiChallengeRoundingMode.match(mode), is(expected));
	}
	
	@SuppressWarnings("unused")
	private Object[] paramsFor_roundModeMatchTest() {
		return $(
			//Actual enum constants
			$("ROUND_UP_LOSSY", ROUND_UP_LOSSY),
			$("ROUND_UP_KEEP", ROUND_UP_KEEP), 
			$("ROUND_NEAREST_LOSSY", ROUND_NEAREST_LOSSY), 
			$("ROUND_NEAREST_KEEP", ROUND_NEAREST_KEEP), 
			$("ROUND_DOWN_LOSSY", ROUND_DOWN_LOSSY), 
			$("ROUND_DOWN_KEEP", ROUND_DOWN_KEEP),
			//Lowercase, spaced versions.
			$("round up lossy", ROUND_UP_LOSSY),
			$("round up keep", ROUND_UP_KEEP), 
			$("round nearest lossy", ROUND_NEAREST_LOSSY), 
			$("round nearest keep", ROUND_NEAREST_KEEP), 
			$("round down lossy", ROUND_DOWN_LOSSY), 
			$("round down keep", ROUND_DOWN_KEEP),
			//Obvious syntax versions.
			$("Round up: Lossy", ROUND_UP_LOSSY),
			$("Round up: Keep", ROUND_UP_KEEP), 
			$("Round nearest: Lossy", ROUND_NEAREST_LOSSY), 
			$("Round nearest: Keep", ROUND_NEAREST_KEEP), 
			$("Round down: Lossy", ROUND_DOWN_LOSSY), 
			$("Round down: Keep", ROUND_DOWN_KEEP) 
		);
	}
	
	@Test
	@Parameters(method="paramsFor_replacedNumbersTest")
	public void replacedNumbersTest(String desc, int times, float tax, 
			MultiChallengeRoundingMode roundMode, String expected) {
		assertThat(CommandMultiChallenge.replacedNumbers(desc, times,
				tax, roundMode), is(expected));
	}
	
	@SuppressWarnings("unused")
	private Object[] paramsFor_replacedNumbersTest() {
		return $(
			//Normal conditions
			$("4", 2, .6f, ROUND_UP_LOSSY, "6"), 
			$("4", 2, .6f, ROUND_UP_KEEP, "5"),
			$("4", 2, .6f, ROUND_NEAREST_LOSSY, "4"), 
			$("4", 2, .6f, ROUND_NEAREST_KEEP, "5"),
			$("4", 2, .6f, ROUND_DOWN_LOSSY, "4"), 
			$("4", 2, .6f, ROUND_DOWN_KEEP, "4"),
			//Repeated number issue
			$("16 redstone dust, 1 cocoa bean, 1 spawn egg" +
					"(chicken,cow,pig)", 11, .9f, ROUND_UP_LOSSY,
					"165 redstone dust, 11 cocoa bean, 11 " +
					"spawn egg(chicken,cow,pig)"),
			//Middle numbers (yes, this is a random string, 
			//but it is a good test value)
			$("Pokechu22 teh h4x0r 1s eating 2 diamonds",
					2, 1f, ROUND_UP_LOSSY,
					"Pokechu22 teh h4x0r 1s eating 4 diamonds")
		);
	}
}
