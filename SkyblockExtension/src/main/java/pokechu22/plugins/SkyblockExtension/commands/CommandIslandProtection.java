package pokechu22.plugins.SkyblockExtension.commands;

import static pokechu22.plugins.SkyblockExtension.util.TabCompleteUtil.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import pokechu22.plugins.SkyblockExtension.errorhandling.ErrorHandler;
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
public class CommandIslandProtection implements CommandExecutor, TabCompleter {
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
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String args[]) 
	{
		try {
			if (args.length == 0) {
				List<String> returned = new ArrayList<>();
				if (sender instanceof Player) {
					//Add the sender to the start of the list, to simplify
					//modification of one's own island.
					returned.add(((Player)sender).getName());
				}
				for (Player player : Bukkit.getOnlinePlayers()) {
					returned.add(player.getName());
				}
				return returned;
			}
			if (args.length == 1) {
				List<String> returned = new ArrayList<>();
				if (sender instanceof Player) {
					//Add the sender to the start of the list, to simplify
					//modification of one's own island.
					returned.add(((Player)sender).getName());
				}
				for (Player player : Bukkit.getOnlinePlayers()) {
					returned.add(player.getName());
				}
				return TabLimit(returned, args[0]);
			}
			if (args.length == 2) {
				//Produce tiers based off of args.
				return TabLimit(tiers, args[1]);
			}
			if (args.length == 3) {
				//Validate tiers.
				if (!tiers.contains(args[1])) {
					return new ArrayList<String>();
				}
				
				//Produce flag name.
				return TabLimit(flags, args[2]);
			}
			if (args.length == 4) {
				//Validate tiers.
				if (!tiers.contains(args[1])) {
					System.out.println("invalid teir");
					return new ArrayList<String>();
				}
				
				System.out.println("args[2]: \"" + args[2] + "\"");
				//Validate flag.
				if (!IslandProtectionDataSetFlag.flagTypes.keySet().contains(args[2])) {
					System.out.println("invalid flag");
					return new ArrayList<String>();
				}
				
				return TabLimit(IslandProtectionDataSetFlag.flagTypes.get(args[2]).getAllowedActions(), args[3]);
			}
			
			if (args.length >= 5) {
				//Validate tiers.
				if (!tiers.contains(args[1])) {
					return new ArrayList<String>();
				}
				
				//Validate flag.
				if (!IslandProtectionDataSetFlag.flagTypes.keySet().contains(args[2])) {
					return new ArrayList<String>();
				}
				
				//Validate action.
				if (!IslandProtectionDataSetFlag.flagTypes.get(args[2]).canPreformAction(args[3])) {
					return new ArrayList<String>();
				}
			
				//TODO Tabcomplete flag using it's function to do so.
				return new ArrayList<String>();
			}
		} catch (Throwable e) {
			ErrorHandler.logExceptionOnTabComplete(sender, cmd, label, args, e);
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
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		try {
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
					return true;
				}
				
				tier = MembershipTier.matchTier(args[1]);
				if (tier == null) {
					sender.sendMessage("§cInvalid membership tier: " + args[1]);
					return true;
				}
				
				set = info.permissions.get(tier.name());
				
				if (!IslandProtectionDataSet.flags.contains(args[2])) {
					sender.sendMessage("§cThere is no flag " + args[2] + ".");
					return true;
				}
				
				flag = set.getFlag(args[2]);
				
				if (flag == null) {
					sender.sendMessage("§cThere is no flag " + args[2] + ".");
					return true;
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
					return true;
				} else {
					sender.sendMessage(flag.preformAction(args[3], 
							Arrays.copyOfRange(args, 4, args.length)));
					return true;
				}
			} else {
				sender.sendMessage("§cInvalid input"); //TODO
			}
		} catch (Throwable e) {
			ErrorHandler.logExceptionOnCommand(sender, cmd, label, args, e);
		}
		return true;
	}
}
