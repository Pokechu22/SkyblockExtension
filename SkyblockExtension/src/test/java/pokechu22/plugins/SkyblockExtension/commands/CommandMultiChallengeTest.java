package pokechu22.plugins.SkyblockExtension.commands;

//Import the test functions.
import static org.hamcrest.Matchers.*; 
import static org.junit.Assert.*; 

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import pokechu22.plugins.SkyblockExtension.SkyblockExtension;


public class CommandMultiChallengeTest {
	/**
	 * Challenges used for testing, as uSkyblock won't actually exist.
	 */
	private List<String> testChallenges = new ArrayList<String>();
	
	/*
	 * Set up the default values of CommandMultiChalenge
	 */
	{
		testChallenges.add("TestChallenge");
		testChallenges.add("TestChallenge2");
		
		//Fake initiation.
		CommandMultiChallenge.challengeNames = testChallenges;
		CommandMultiChallenge.initiated = true;
	}
	
	/**
	 * Tests if tab completion lists challenges.
	 */
	@Test
	public void tabCompletionShouldListChallenges() {
		assertThat(CommandMultiChallenge.onTabComplete(SkyblockExtension.inst().getServer().getConsoleSender(), SkyblockExtension.inst().getCommand("multichallenge"), "multichallenge", new String[0]), containsInAnyOrder(testChallenges.toArray()));
	}
}
