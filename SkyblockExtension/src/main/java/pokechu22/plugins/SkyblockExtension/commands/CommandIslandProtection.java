package pokechu22.plugins.SkyblockExtension.commands;

import static pokechu22.plugins.SkyblockExtension.util.TabCompleteUtil.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import pokechu22.plugins.SkyblockExtension.protection.IslandProtectionDataSet;
import pokechu22.plugins.SkyblockExtension.protection.IslandProtectionDataSetFactory;
import pokechu22.plugins.SkyblockExtension.protection.MembershipTier;
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
	private static final String[] membershipTiers;
	
	static {
		MembershipTier[] values = MembershipTier.values();
		membershipTiers = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			membershipTiers[i] = values.toString();
		}
	}
	
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
		
		if (args.length >= 4) {
			MembershipTier parsedTier = MembershipTier.matchTier(args[1]); 
			
			if (parsedTier == null) {
				//Unlike with run, we don't want to send messages.
				return new ArrayList<String>();
			}
			
			IslandProtectionDataSet relevantDataSet = tieredValues.get(parsedTier);
			
			return relevantDataSet.tabComplete
					(args[2], args[0], Arrays.copyOfRange(args, 3, args.length));
		}

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
		if (args.length < 3) {
			return;
			//TODO error message.
		}
		
		MembershipTier parsedTier = MembershipTier.matchTier(args[1]); 
		
		if (parsedTier == null) {
			sender.sendMessage("§cInvlaid tier"); //TODO better message
			return;
		}
		
		IslandProtectionDataSet relevantDataSet = tieredValues.get(parsedTier);
		
		if (args.length >= 4) {
			String longParam = "";
			for (int i = 3; i < args.length; i++) {
				longParam += args[i];
				longParam += " ";
			}
			longParam = longParam.trim();
			
			if (args[0].equalsIgnoreCase("set")) {
				sender.sendMessage(relevantDataSet.setFlagValue(args[2], longParam));
			}
			if (args[0].equalsIgnoreCase("add")) {
				sender.sendMessage(relevantDataSet.addToFlagValue(args[2], longParam, false));
			}
			if (args[0].equalsIgnoreCase("add-f")) {
				sender.sendMessage(relevantDataSet.addToFlagValue(args[2], longParam, true));
			}
		}
		if (args.length == 3) {
			if (args[0].equalsIgnoreCase("view")) {
				sender.sendMessage(relevantDataSet.getFlagValue(args[2]));
			}
		}
	}
	
	/**
	 * Values of different tiers.
	 * TODO: this is a test value.
	 */
	public static Map<MembershipTier, IslandProtectionDataSet> tieredValues
			= IslandProtectionDataSetFactory.getDefaultValues();
}
