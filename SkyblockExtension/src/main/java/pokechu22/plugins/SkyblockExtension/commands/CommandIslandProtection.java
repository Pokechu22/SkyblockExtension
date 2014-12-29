package pokechu22.plugins.SkyblockExtension.commands;

import static pokechu22.plugins.SkyblockExtension.util.TabCompleteUtil.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import pokechu22.plugins.SkyblockExtension.protection.IslandInfo;
import pokechu22.plugins.SkyblockExtension.protection.IslandInfoCache;
import pokechu22.plugins.SkyblockExtension.protection.IslandInfoCache.NoIslandFoundBehaviors;
import pokechu22.plugins.SkyblockExtension.protection.IslandProtectionDataSet;
import pokechu22.plugins.SkyblockExtension.protection.MembershipTier;
import pokechu22.plugins.SkyblockExtension.protection.flags.IslandProtectionDataSetFlag;
import pokechu22.plugins.SkyblockExtension.util.PlayerPrintStream;

/**
 * Island-protection configuration command.  
 * 
 * Format: 
 * /isprot [name|islandCode] [tier] [flag] [action] [values...]
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
	 * Different flags available.
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
		if (args.length >= 3) {
			IslandInfo info;
			MembershipTier tier;
			IslandProtectionDataSetFlag flag;
			IslandProtectionDataSet set;
			
			try {
				info = IslandInfoCache.getCommandIslandInfo(args[0], 
						NoIslandFoundBehaviors.THROW_EXCEPTION);
			} catch (Exception e) {
				try (PlayerPrintStream s = new PlayerPrintStream(sender)) {
					e.printStackTrace(s);
				}
				return;
			}
			
			tier = MembershipTier.matchTier(args[1]);
			if (tier == null) {
				sender.sendMessage("§cInvalid membership tier: " + args[1]);
				return;
			}
			
			set = info.permissions.get(tier.name());
			flag = set.getFlag(args[2]);
			
			if (flag == null) {
				sender.sendMessage("There is no flag " + args[2]);
				return;
			}
			
			if (args.length == 3) {
				sender.sendMessage("Information about flag " + args[2] + " for " + args[1] + "s of " + args[0] + ":");
				
				sender.sendMessage("Value: " + flag.getDisplayValue());
				sender.sendMessage("Type: " + flag.getType());
				sender.sendMessage("Available actions: ");
				if (flag.getActions().size() == 0) {
					sender.sendMessage("§cNone."); //I doubt this will ever happen.
				} else {
					for (String action : flag.getActions()) {
						// \u2022 is bullet point.  Seems to display correctly in minecraft.
						sender.sendMessage(" \u2022 " + action);
					}
				}
				return;
			} else {
				sender.sendMessage(flag.preformAction(args[3], 
						Arrays.copyOfRange(args, 4, args.length)));
				return;
			}
		} else {
			sender.sendMessage("§cInvalid input"); //TODO
		}
	}
}
