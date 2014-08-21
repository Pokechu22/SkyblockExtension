package pokechu22.plugins.SkyblockExtension.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import pokechu22.plugins.SkyblockExtension.protection.IslandProtectionDataSet;

public class CommandIslandProtection {
	/**
	 * Subcommands, belonging to args[0].
	 */
	private static final String[] subCommands = new String[] {
		"view",
		"set"
	};
	
	/**
	 * Levels of membership.
	 */
	private static final String[] membershipTiers = new String[] {
		"owner",
		"member",
		"guest",
		"nonmember"
	};
	
	/**
	 * Tab-completion.  
	 * 
	 * @param sender
	 * @param cmd
	 * @param label
	 * @param args
	 * @return
	 */
	public static List<String> onTabComplete(CommandSender sender, Command cmd, String label, String args[]) 
	{
		if (args.length == 1) {
			return TabLimit(Arrays.asList(subCommands), args[0]);
		}
		if (args.length == 2) {
			switch (args[0].toLowerCase()) {
			case "view": //Fall thru to next
			case "set": {
				return TabLimit(Arrays.asList(membershipTiers), args[1]);
			}
			}
		}

		//Basically, return nothing, rather than null which gives all online players.
		return new ArrayList<String>();
	}
	
	/**
	 * Runs the command.
	 * 
	 * @param sender
	 * @param cmd
	 * @param label
	 * @param args
	 */
	public static void Run(CommandSender sender, Command cmd, String label, String args[]) {
		
	}
	
	public static IslandProtectionDataSet test;
	
	/**
	 * Filters tab completion for starting with same letter.
	 * TODO: Move to a better location.
	 * @param list
	 * @param starting
	 * @return
	 */
	static List<String> TabLimit(List<String> list, String starting) {
		ArrayList<String> returned = new ArrayList<String>();
		for (String s : list) {
			if (s.startsWith(starting)) {
				returned.add(s);
			}
		}
		return returned;
	}
}
