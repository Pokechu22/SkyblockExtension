package pokechu22.plugins.SkyblockExtension.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import pokechu22.plugins.SkyblockExtension.protection.IslandProtectionDataSet;
import pokechu22.plugins.SkyblockExtension.protection.flags.IslandProtectionDataSetFlag;

public class CommandIslandProtection {
	/**
	 * Subcommands, belonging to args[0].
	 */
	private static final String[] subCommands = new String[] {
		"view", //Shows a flag's value.
		"add", //Adds to a flag's value.
		"add-f", //Forcibly adds to a flag's value.
		"set" //Sets a flag's value.
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
			case "add": //Fall thru to next
			case "add-f": //Fall thru to next
			case "set": {
				return TabLimit(Arrays.asList(membershipTiers), args[1]);
			}
			}
		}
		if (args.length == 3) {
			switch (args[0].toLowerCase()) {
			case "view": //Fall thru to next
			case "set": {
				return TabLimit(IslandProtectionDataSet.flags, args[2]);
			}
			case "add-f": //Fall thru to next
			case "add": {
				ArrayList<String> returned = new ArrayList<String>();
				for (Map.Entry<String, IslandProtectionDataSetFlag.FlagType> e : 
					IslandProtectionDataSetFlag.flagTypes.entrySet()) {
					switch (e.getValue()) {
					case BOOLEAN:
						break;
					case ENTITYLIST: //Fall thru
					case HANGINGLIST: //Fall thru
					case MATERIALLIST: //Fall thru
					case VEHICLELIST:
						returned.add(e.getKey());
						break;
					default:
						break;
					}
				}
				return TabLimit(returned, args[2]);
			}
			}
		}
		//NYI
		/*if (args.length == 4) {
			switch (args[0].toLowerCase()) {
			case "view": //Fall thru to next
			case "set": {
				if (!IslandProtectionDataSet.flags.containsKey(args[2])) {
					//Return nothing.
					return new ArrayList<String>();
				}
				
				switch (IslandProtectionDataSet.flags.get(args[2])) {
				default: {
					
				}
				}
			}
			}
		}*/

		//Basically, return nothing, rather than null which gives all online players.
		return new ArrayList<String>();
	}
	
	/**
	 * Runs the command.
	 * 
	 * TODO: Make better (EG Error messages).
	 * 
	 * @param sender
	 * @param cmd
	 * @param label
	 * @param args
	 */
	public static void Run(CommandSender sender, Command cmd, String label, String args[]) {
		if (args.length == 4) {
			if (args[0].equalsIgnoreCase("set")) {
				sender.sendMessage(test.setFlagValue(args[2], args[3]));
			}
			if (args[0].equalsIgnoreCase("add")) {
				sender.sendMessage(test.addToFlagValue(args[2], args[3], false));
			}
			if (args[0].equalsIgnoreCase("add-f")) {
				sender.sendMessage(test.addToFlagValue(args[2], args[3], true));
			}
		}
		if (args.length == 3) {
			if (args[0].equalsIgnoreCase("view")) {
				sender.sendMessage(test.getFlagValue(args[2]));
			}
		}
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
