package pokechu22.plugins.SkyblockExtension.commands;

import static pokechu22.plugins.SkyblockExtension.util.TabCompleteUtil.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import pokechu22.plugins.SkyblockExtension.protection.IslandProtectionDataSet;
import pokechu22.plugins.SkyblockExtension.protection.MembershipTier;
import pokechu22.plugins.SkyblockExtension.protection.flags.IslandProtectionDataSetFlag;

/**
 * Island-protection configuration command.  
 * 
 * Format: 
 * /isprot [name|location] [tier] [flag] [action] [values...]
 * 
 * @author Pokechu22
 *
 */
public class CommandIslandProtection {
	/**
	 * Levels of membership.
	 */
	private static final List<String> tiers;
	
	/**
	 * Different flags avaliable.
	 */
	private static final List<String> flags;
	
	static {
		tiers = new ArrayList<>();
		for (MembershipTier tier : MembershipTier.values()) {
			tiers.add(tier.name().toLowerCase(Locale.ENGLISH));
		}
	}
	
	static {
		flags = new ArrayList<>();
		flags.addAll(IslandProtectionDataSetFlag.flagTypes.keySet());
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
		if (args.length == 0) {
			//Produce tiers from scratch
			return tiers;
		}
		if (args.length == 1) {
			//Produce tiers based off of args.
			return TabLimit(tiers, args[0]);
		}
		if (args.length == 2) {
			//Validate tiers.
			if (!tiers.contains(args[0])) {
				return new ArrayList<String>();
			}
			
			//Produce flag name.
			return TabLimit(flags, args[1]);
		}
		if (args.length == 3) {
			//Validate tiers.
			if (!tiers.contains(args[0])) {
				return new ArrayList<String>();
			}
			
			//Validate flag.
			if (!IslandProtectionDataSetFlag.flagTypes.entrySet().contains(args[1])) {
				return new ArrayList<String>();
			}
			
			return TabLimit(IslandProtectionDataSetFlag.flagTypes.get(args[1]).getAllowedActions(), args[2]);
		}
		
		if (args.length >= 4) {
			//Validate tiers.
			if (!tiers.contains(args[0])) {
				return new ArrayList<String>();
			}
			
			//Validate flag.
			if (!IslandProtectionDataSetFlag.flagTypes.entrySet().contains(args[1])) {
				return new ArrayList<String>();
			}
			
			//Validate action.
			if (!IslandProtectionDataSetFlag.flagTypes.get(args[1]).canPreformAction(args[3])) {
				return new ArrayList<String>();
			}
		
			//TODO: Get the location.
			return new ArrayList<String>();
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
		//TODO
	}
	
	/**
	 * Values of different tiers.
	 * TODO: this is a test value.
	 */
	public static Map<String, IslandProtectionDataSet> tieredValues;
}
