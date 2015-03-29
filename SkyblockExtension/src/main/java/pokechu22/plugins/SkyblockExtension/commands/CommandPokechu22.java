package pokechu22.plugins.SkyblockExtension.commands;

import static pokechu22.plugins.SkyblockExtension.util.StringUtil.trailOff;
import static pokechu22.plugins.SkyblockExtension.util.TabCompleteUtil.TabLimit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.ChatPaginator;

import pokechu22.plugins.SkyblockExtension.PermissionHandler;
import pokechu22.plugins.SkyblockExtension.SkyblockExtension;
import pokechu22.plugins.SkyblockExtension.WitherWarner;
import pokechu22.plugins.SkyblockExtension.errorhandling.CrashReport;
import pokechu22.plugins.SkyblockExtension.errorhandling.ErrorHandler;

/**
 * Provides support for the /pokechu22 command, which does basic stuff.
 * 
 * @author Pokechu22
 * 
 */
public class CommandPokechu22 implements CommandExecutor, TabCompleter {
	
	/**
	 * Information for when help is created.
	 * This controls args[0]'s effects.
	 */
	private static final Map<String, String> subCommands;
	/*
	 * Static initializer for subCommands.
	 */
	static {
		TreeMap<String, String> map = new TreeMap<String, String>(
				String.CASE_INSENSITIVE_ORDER);
		
		map.put("test", "Various tests for debuging purposes.  " +
				"Most of these commands will be of no useless to " + 
				"actual players.  \nUsage: \n" + 
				"§e/pokechu22 test <TestName>§f.");
		map.put("crashes", "Handles any error reports that may occur." +
				"\nUsage: \nSee §e/pokechu22 help crashes§f.");
		map.put("logo", "Shows a rather pointless logo.\nUsage: \n" + 
				"§e/pokechu22 logo§f.");
		map.put("help", "View this help.  It is clear you understand.\n" +
				"Usage: \n§e/pokechu22 help [commandName, subCommand, " + 
				"...]§f.");
		map.put("reloadConfig", "Reloads one or all of the configuration(s).\n" +
				"Usage: \n§e/pokechu22 help [configName]§f.");
		
		subCommands = Collections.unmodifiableMap(map);
	}
	
	/**
	 * Help information for each test.
	 */
	private static final Map<String, String> tests;
	/*
	 * Static initializer for tests.
	 */
	static {
		TreeMap<String, String> map = new TreeMap<String, String>(
				String.CASE_INSENSITIVE_ORDER);
		
		map.put("MyIslandLocation", "Provides your own island location." +
				"\nUsage: \n§e/pokechu22 test MyIslandLocation§f.");
		map.put("NearestIslandLocation", "Provides the nearest valid " + 
				"island location.  This location should have a bedrock " + 
				"block in it.\nUsage: \n" + 
				"§e/pokechu22 test NearestIslandLocation§f.");
		map.put("PrintPlayerInfo", "Reads an entire PlayerInfo.\n" +
				"Usage:\n§e/pokechu22 test PrintPlayerInfo <player> [m][f]§f.");
		
		tests = Collections.unmodifiableMap(map);
	}
	
	/**
	 * Help information for the subcommands of /pokechu22 crashes.
	 */
	private static final Map<String, String> crashesCommands;
	/*
	 * Static initializer for crashesCommands.
	 */
	static {
		TreeMap<String, String> map = new TreeMap<String, String>(
				String.CASE_INSENSITIVE_ORDER);
		
		map.put("show", "Shows a single crash report.  \nUsage: \n" +
				"§e/pokechu22 crashes show <crashId> [page]§f");
		map.put("list", "Lists all crash reports.  \nUsage: \n" +
				"§e/pokechu22 crashes list [page]§f");
		map.put("remove", "Removes a single crash report.  \nUsage: \n" +
				"§e/pokechu22 crashes remove <crashId>§f  \nNOTE: After " + 
				"running this command, crashes may be shifted.  " + 
				"Make sure that this will not cause issues.");
		map.put("reset", "Removes all crash reports.  \nUsage: \n" + 
				"§e/pokechu22 crashes reset§f - Provides you with your magic number.\n" + 
				"§e/pokechu22 crashes reset <magicNumber>§f - actually resets the crashes.\n" + 
				"The magic number is obtained via a hashcode of your name-string.");
		map.put("viewraw", "Views the raw YAML markup of a crash report.\n" +
				"This may seem useless, but it can be used to access " +
				"information not provided normally.\nUsage:\n"+ 
				"§e/pokechu22 crashes viewraw <crashID>§f.");
		map.put("markread", "Marks a crash report as having been read.\nUsage: \n" + 
				"§e/pokechu22 crashes markread <crashID>§f - marks said crash as read.");
		map.put("markunread", "Marks a crash report as not having been read.\nUsage: \n" + 
				"§e/pokechu22 crashes markunread <crashID>§f - marks said crash as read.");
		map.put("hide", "Hides a crash report from view.\nUsage: \n" + 
				"§e/pokechu22 crashes hide <crashID>§f - hides said crash report.");
		map.put("unhide", "Returns a crash report to your view.\nUsage: \n" + 
				"§e/pokechu22 crashes unhide <crashID>§f - unhides said crash report.");
		map.put("markallread", "Marks all reports as read.\nUsage: \n" + 
				"§e/pokechu22 crashes markallread <crashID>§f.");
		
		
		
		crashesCommands = Collections.unmodifiableMap(map);
	}
	
	/**
	 * Called when a command is run.
	 * 
	 * @param sender The person who sent the command.
	 * @param cmd The command.
	 * @param label The name of the command (EG "example" for "/example") 
	 * @param args The arguments provided to the command.
	 * @return True if the command syntax was correct, 
	 *     false if you want the message in plugin.yml to be displayed. 
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		try {
			if (args.length == 0) {
				sender.sendMessage("Usage: /" + label + " help");
				return true;
			} else {
				switch (args[0].toLowerCase()) {
				case "test": {
					Test(sender, cmd, label, args);
					break;
				}
				case "crashes": {
					Crashes(sender,cmd,label,args);
					break;
				}
				case "logo": {
					//This is pointless, but is ported from the old version.
					ShowLogo(sender);
					break;
				}
				case "help": {
					Help(sender, cmd, label, args);
					break;
				}
				case "witherwarning": {
					witherWarning(sender, cmd, label, args);
					break;
				}
				case "reloadconfig": {
					reloadConfigs(sender, cmd, label, args);
					break;
				}
				default: {
					sender.sendMessage("Usage: /" + label + " help");
					break;
				}
				}
			}
		} catch (Throwable e) {
			ErrorHandler.logExceptionOnCommand(sender, cmd, label, args, e);
		}
		return true;
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
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String args[]) {
		try {
			if (args.length == 0) {
				return new ArrayList<String>(subCommands.keySet());
			}
			if (args.length == 1) {
				return TabLimit(subCommands.keySet(), args[0]);
			}
			
			if (args.length == 2) {
				switch (args[0].toLowerCase()) {
				case "help": {
					return TabLimit(subCommands.keySet(), args[1]);
				}
				case "test": {
					return TabLimit(tests.keySet(), args[1]);
				}
				case "crashes": {
					return TabLimit(crashesCommands.keySet(), args[1]);
				}
				case "logo": {
					return new ArrayList<String>();
				}
				}
			}
			
			if (args.length == 3) {
				//Provides tab-completion for reset.
				if (args[0].equalsIgnoreCase("crashes")) {
					if (args[1].equalsIgnoreCase("reset")) {
						ArrayList<String> returned = new ArrayList<>();
						returned.add("" + getResetConfirmCode(sender));
						return returned;
					}
				}
				if (args[0].equalsIgnoreCase("help")) {
					if (args[1].equalsIgnoreCase("test")) {
						return TabLimit(tests.keySet(), args[2]);
					}
					if (args[1].equalsIgnoreCase("crashes")) {
						return TabLimit(crashesCommands.keySet(), args[2]);
					}
				}
			}
		} catch (Throwable e) {
			ErrorHandler.logExceptionOnTabComplete(sender, cmd, label, args, e);
		}
		
		return new ArrayList<String>();
	}

	/**
	 * Subcommand for /pokechu22 crashes.
	 * @param sender
	 * @param cmd
	 * @param label
	 * @param args
	 * @Throws {@link Error} when args.length is 0, which should never happen.
	 */
	protected void Crashes(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			throw new IllegalArgumentException("Args.length should NEVER be 0.");
		}
		if (args.length == 1) {
			sender.sendMessage("Usage: /" + label + " crashes help");
			return;
		}
		if (args[1].equalsIgnoreCase("list")) {
			if (PermissionHandler.HasPermission(sender, "sbe.debug.crashes.list")) {
				if (args.length == 2) {
					ErrorHandler.listCrashes(sender, 0);
					return;
				} else if (args.length == 3){
					int page;
					
					try {
						page = Integer.parseInt(args[2]);
					} catch (NumberFormatException e) {
						sender.sendMessage("§cFailed to parse page number (Got " + args[2] + 
								", expected Integer).");
						sender.sendMessage("For usage, do /" + label + " crashes help");
						return;
					}
					
					if (page < 0) {
						sender.sendMessage("§cPage cannot be negative.  (Got " + args[2] + ")");
						return;
					}
					
					if (page == 0) {
						sender.sendMessage("§cPages start at 1.");
						return;
					}
					
					int firstCrash = (page - 1) * (ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 2);
					
					ErrorHandler.listCrashes(sender, firstCrash);
					return;
				} else {
					sender.sendMessage("§cError: Too many parameters.");
					sender.sendMessage("For usage, do /" + label + " crashes help");
					return;
				}
			}
			return;
		}
		if (args[1].equalsIgnoreCase("show")) {
			if (PermissionHandler.HasPermission(sender, "sbe.debug.crashes.show")) {
				if (args.length == 2) {
					sender.sendMessage("§cError: Too few parameters.");
					sender.sendMessage("For usage, do /" + label + " crashes help");
					return;
				} else if (args.length == 3) {
					int CrashID;
					
					try {
						CrashID = Integer.parseInt(args[2]);
					} catch (NumberFormatException e) {
						sender.sendMessage("§cFailed to parse crash ID (Got " + args[2] + 
								", expected Integer).");
						sender.sendMessage("For usage, do /" + label + " crashes help");
						return;
					}
					
					if (ErrorHandler.getNumberOfCrashes() == 0) {
						sender.sendMessage("§cThere are no crashes to show!");
						return;
					}
					
					if (CrashID > ErrorHandler.getLastCrashID()) {
						sender.sendMessage("§cCrash ID is beyond the maximum!");
						sender.sendMessage("§cMaximum ID is currently " + 
								ErrorHandler.getLastCrashID() +	", got " + args[2] + ".");
						sender.sendMessage("For usage, do /" + label + " crashes help");
						return;
					}
					
					if (CrashID < 0) {
						sender.sendMessage("§cCrash ID not allowed to be negative!");
						sender.sendMessage("For usage, do /" + label + " crashes help");
						return;
					}
					
					ErrorHandler.getCrashInfo(sender, CrashID, 1);
					return;
				} else if (args.length == 4) {
					int CrashID;
					int page;
					
					try {
						CrashID = Integer.parseInt(args[2]);
					} catch (NumberFormatException e) {
						sender.sendMessage("§cFailed to parse crash ID (Got " + args[2] + 
								", expected Integer).");
						sender.sendMessage("For usage, do /" + label + " crashes help");
						return;
					}
					
					if (ErrorHandler.getNumberOfCrashes() == 0) {
						sender.sendMessage("§cThere are no crashes to show!");
						return;
					}
					
					if (CrashID > ErrorHandler.getLastCrashID()) {
						sender.sendMessage("§cCrash ID is beyond the maximum!");
						sender.sendMessage("§cMaximum ID is currently " + 
								ErrorHandler.getLastCrashID() +	", got " + args[2] + ".");
						sender.sendMessage("For usage, do /" + label + " crashes help");
						return;
					}
					
					if (CrashID < 0) {
						sender.sendMessage("§cCrash ID not allowed to be negative!");
						sender.sendMessage("For usage, do /" + label + " crashes help");
						return;
					}
					
					try {
						page = Integer.parseInt(args[3]);
					} catch (NumberFormatException e) {
						sender.sendMessage("§cFailed to parse page number (Got " + args[3] + 
								", expected Integer).");
						sender.sendMessage("For usage, do /" + label + " crashes help");
						return;
					}
					
					if (page < 0) {
						sender.sendMessage("§cPage number cannot be negative! (Got " + 
								args[3] + ")");
						return;
					}
					
					if (page == 0) {
						sender.sendMessage("§cPage numbers start at one.");
						return;
					}
					
					ErrorHandler.getCrashInfo(sender, CrashID, page);
					return;
				} else {
					sender.sendMessage("§cError: Too many parameters.");
					sender.sendMessage("For usage, do /" + label + " crashes help");
					return;
				}
			}
			return;
		}
		if (args[1].equalsIgnoreCase("remove")) {
			if (PermissionHandler.HasPermission(sender, "sbe.debug.crashes.remove")) {
				if (args.length <= 2) {
					sender.sendMessage("§cError: Too few parameters.  ");
					sender.sendMessage("For usage, do /" + label + " crashes help");
					return;
				}
				if (args.length >= 4) {
					sender.sendMessage("§cError: Too many parameters.");
					sender.sendMessage("For usage, do /" + label + " crashes help");
					return;
				}
				//Args.length can be assumed to be 3.
				
				int CrashID;
				
				try {
					CrashID = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					sender.sendMessage("§cFailed to parse crash ID (Got " + args[2] + 
							", expected Integer).");
					sender.sendMessage("For usage, do /" + label + " crashes help");
					return;
				}
				
				if (ErrorHandler.getNumberOfCrashes() == 0) {
					sender.sendMessage("§cThere are no crashes to remove!");
					return;
				}
				
				if (CrashID > ErrorHandler.getLastCrashID()) {
					sender.sendMessage("§cCrash ID is beyond the maximum!");
					sender.sendMessage("§cMaximum ID is currently " + 
							ErrorHandler.getLastCrashID() +	", got " + args[2] + ".");
					sender.sendMessage("For usage, do /" + label + " crashes help");
					return;
				}
				
				if (CrashID < 0) {
					sender.sendMessage("§cCrash ID not allowed to be negative!");
					sender.sendMessage("For usage, do /" + label + " crashes help");
					return;
				}
				
				ErrorHandler.removeCrash(CrashID);
				
				sender.sendMessage("§1NOTE: If you plan on removing aditional crashes, " 
						+ "the numbers may have been shifted.");
				
				return;
			}
			return;
		}
		if (args[1].equalsIgnoreCase("reset")) {
			//There's some checking here involving string hash codes.  It forces confirmation.
			if (PermissionHandler.HasPermission(sender, "sbe.debug.crashes.reset")) {
				if (args.length >= 4) {
					sender.sendMessage("§cError: Too many parameters.");
					sender.sendMessage("For usage, do /" + label + " crashes help");
					return;
				}
				if (args.length == 2) {
					sender.sendMessage("§c§lAre you sure you wish to reset all previous crash logging?");
					sender.sendMessage("To confirm, please run /" + label + " crashes reset " +
							getResetConfirmCode(sender));
					return;
				}
				if (args.length == 3) {
					int confirmationCode;
					try {
						confirmationCode = Integer.parseInt(args[2]);
					} catch (NumberFormatException e) {
						//We can assume that if it is not an integer, it doesn't match an integer.
						sender.sendMessage("§cConfirmation code does not match.");
						sender.sendMessage("To confirm, please run /" + label + 
								" crashes reset " + getResetConfirmCode(sender));
						return;
					}
					if (confirmationCode != getResetConfirmCode(sender)) {
						sender.sendMessage("§cConfirmation code does not match.");
						sender.sendMessage("To confirm, please run /" + label + 
								" crashes reset " + getResetConfirmCode(sender));
						return;
					}
					ErrorHandler.resetAllCrashes(sender);
					return;
				}
				return;
			}
			return;
		}
		if (args[1].equalsIgnoreCase("viewraw")) {
			if (!PermissionHandler.HasPermission(sender, "sbe.debug.crashes.viewraw")) {
				return;
			}
			if (args.length != 3) {
				sender.sendMessage("§cUsage: /" + label + " help crashes viewraw");
			}
			
			try {
				sender.sendMessage(ErrorHandler.getReportByID(args[2], 
						"§cUsage: /" + label + " help crashes viewraw")
						.getAsRawYaml());
			} catch (IllegalArgumentException e) {
				sender.sendMessage(e.getMessage());
			}
			
			return;
		}
		if (args[1].equalsIgnoreCase("markread")) {
			if (!PermissionHandler.HasPermission(sender, "sbe.debug.crashes.markread")) {
				return;
			}
			if (args.length != 3) {
				sender.sendMessage("§cUsage: /" + label + " help crashes markread");
			}
			
			try {
				ErrorHandler.getReportByID(args[2], 
						"§cUsage: /" + label + " help crashes markread")
						.setRead(sender.getName());
				sender.sendMessage("§aMarked report " + args[2] + " as read.");
			} catch (IllegalArgumentException e) {
				sender.sendMessage(e.getMessage());
			}
			return;
		}
		if (args[1].equalsIgnoreCase("markunread")) {
			if (!PermissionHandler.HasPermission(sender, "sbe.debug.crashes.markunread")) {
				return;
			}
			if (args.length != 3) {
				sender.sendMessage("§cUsage: /" + label + " help crashes markunread");
			}
			
			try {
				ErrorHandler.getReportByID(args[2], 
						"§cUsage: /" + label + " help crashes markunread")
						.setUnread(sender.getName());
				sender.sendMessage("§aMarked report " + args[2] + " as unread.");
			} catch (IllegalArgumentException e) {
				sender.sendMessage(e.getMessage());
			}
			return;
		}
		if (args[1].equalsIgnoreCase("hide")) {
			if (!PermissionHandler.HasPermission(sender, "sbe.debug.crashes.hide")) {
				return;
			}
			if (args.length != 3) {
				sender.sendMessage("§cUsage: /" + label + " help crashes hide");
			}
			try {
				ErrorHandler.getReportByID(args[2], 
						"§cUsage: /" + label + " help crashes hide")
						.hideFrom(sender.getName());
				sender.sendMessage("§aHid report " + args[2] + " from you.");
			} catch (IllegalArgumentException e) {
				sender.sendMessage(e.getMessage());
			}
			return;
		}
		if (args[1].equalsIgnoreCase("unhide")) {
			if (!PermissionHandler.HasPermission(sender, "sbe.debug.crashes.unhide")) {
				return;
			}
			if (args.length != 3) {
				sender.sendMessage("§cUsage: /" + label + " help crashes unhide");
			}
			try {
				ErrorHandler.getReportByID(args[2], 
						"§cUsage: /" + label + " help crashes unhide")
						.unhideFrom(sender.getName());
				sender.sendMessage("§aYou can now see report " + args[2] + " again.");
			} catch (IllegalArgumentException e) {
				sender.sendMessage(e.getMessage());
			}
			return;
		}
		if (args[1].equalsIgnoreCase("markallread")) {
			if (!PermissionHandler.HasPermission(sender, "sbe.debug.crashes.markallread")) {
				return;
			}
			if (args.length != 2) {
				sender.sendMessage("§cUsage: /" + label + " help crashes markallread");
			}
			int changedCount = 0;
			for (CrashReport c : ErrorHandler.errors) {
				//The returned result is if it was changed (right?)
				if (c.setRead(sender.getName())) {
					changedCount++;
				}
			}
			sender.sendMessage("§aMarked " + changedCount + " reports as read.");
			return;
		}
		sender.sendMessage("Usage: /" + label + " crashes help");
		return;
	}
	
	private int getResetConfirmCode(CommandSender sender) {
		return sender.getName().hashCode();
	}
	
	/**
	 * Handles the various tests.  
	 * @param sender
	 * @param cmd
	 * @param label
	 * @param args
	 */
	protected void Test(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 2) {
			sender.sendMessage("§cError: Too few arguments.");
			return;
		}
		
		sender.sendMessage("§c\"" + args[1] + "\" is not a recognised test!");
		return;
	}
	
	/**
	 * The text for the pointless logo.
	 */
	private static final String[] logoText = {
		"§f\u2588§6\u2588§6\u2588§6\u2588§6\u2588§6\u2588§6\u2588§f\u2588§f\u2588",
		"§f\u2588§6\u2588§f\u2588§2\u2588§2\u2588§2\u2588§6\u2588§2\u2588§2\u2588",
		"§4\u2588§4\u2588§4\u2588§2\u2588§4\u2588§4\u2588§6\u2588§f\u2588§2\u2588",
		"§4\u2588§6\u2588§3\u2588§3\u2588§3\u2588§4\u2588§3\u2588§3\u2588§2\u2588",
		"§4\u2588§6\u2588§3\u2588§2\u2588§f\u2588§4\u2588§6\u2588§3\u2588§2\u2588",
		"§4\u2588§6\u2588§6\u2588§2\u2588§6\u2588§6\u2588§6\u2588§3\u2588§2\u2588",
		"§4\u2588§f\u2588§3\u2588§2\u2588§2\u2588§4\u2588§2\u2588§2\u2588§2\u2588",
		"§4\u2588§4\u2588§3\u2588§4\u2588§4\u2588§4\u2588§f\u2588§3\u2588§f\u2588",
		"§f\u2588§f\u2588§3\u2588§3\u2588§3\u2588§3\u2588§3\u2588§3\u2588§f\u2588",
		"§0-§8=§7[§aPOKECHU22§7]§8=§0-"
	};
	
	/**
	 * Totally pointless logo dummy command.
	 * 
	 * @param sender The CommandSender to show the logo to.
	 */
	private void ShowLogo(CommandSender sender) {
		sender.sendMessage(logoText);
	}
	
	/**
	 * The error message for when no help available.
	 */
	private static final String nonexistantHelpMessage = 
			"§c: " + "There is no help for this subcommand.  It may be " + 
			"an invalid command, or it may be that help has not been " +
			"written.  Note: If you are including a parameter, don't do " +
			"that.  Try removing each parameter, one at a time.  ";
	
	/**
	 * Shows help information.
	 * @param sender
	 * @param cmd
	 * @param label
	 * @param args
	 */
	protected void Help(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			//Shouldn't happen.
			return;
		}
		String[] helpArgs = Arrays.copyOfRange(args, 1, args.length);
		if (helpArgs.length == 0) {
			//Message prepended to each message.
			final String preface = "§7/" + label + " ";
			for (Map.Entry<String, String> entry : subCommands.entrySet()) {
				sender.sendMessage(trailOff(preface + entry.getKey() + "§f:" + entry.getValue()));
			}
			return;
		}
		if (helpArgs.length == 1) {
			//Message prepended to each message.
			final String preface = "§7/" + label + " " + helpArgs[0] + " ";
			
			//If there is no help message...
			if (!subCommands.containsKey(helpArgs[0])) {
				sender.sendMessage(preface + nonexistantHelpMessage);
				return;
			}
		    
			//Send the root message if it exists
			sender.sendMessage(preface + "§f: " + 
					subCommands.get(helpArgs[0]));
			
			switch (helpArgs[0].toLowerCase(Locale.ENGLISH)) {
			case "test": {
				//Send the sub-help.
				for (Map.Entry<String, String> entry : tests.entrySet()) {
					sender.sendMessage(trailOff(preface + entry.getKey() + "§f: " + entry.getValue()));
				}
				return;
			}
			case "crashes": {
				//Send the sub-help.
				for (Map.Entry<String, String> entry : crashesCommands.entrySet()) {
					sender.sendMessage(trailOff(preface + entry.getKey() + "§f: " + entry.getValue()));
				}
				return;
			}
			}
		}
		if (helpArgs.length == 2) {
			//Message prepended to each message.
			final String preface = "§7/" + label + " " + helpArgs[0] + " "
					+ helpArgs[1] + " ";
			
			//If there is no help message...
			if (!subCommands.containsKey(helpArgs[0])) {
				sender.sendMessage(preface + nonexistantHelpMessage + 
						"Specifically, " + helpArgs[0] + " is not known.");
				return;
			}
			
			switch (helpArgs[0].toLowerCase(Locale.ENGLISH)) {
			case "test": {
				//If there is no help message...
				if (!tests.containsKey(helpArgs[1])) {
					sender.sendMessage(preface + nonexistantHelpMessage + 
							"Specifically, " + helpArgs[1] + " is not known.");
					return;
				}
				sender.sendMessage(preface + "§f: " + tests.get(helpArgs[1]));
				return;
			}
			case "crashes": {
				//If there is no help message...
				if (!crashesCommands.containsKey(helpArgs[1])) {
					sender.sendMessage(preface + nonexistantHelpMessage + 
							"Specifically, " + helpArgs[1] + " is not known.");
					return;
				}
				sender.sendMessage(preface + "§f: " + crashesCommands.get(helpArgs[1]));
				return;
			}
			default: {
				sender.sendMessage(preface + nonexistantHelpMessage);
				return;
			}
			}
		}
	}
	
	/**
	 * Sends the warning about withers ({@linkplain WitherWarner}).
	 * @param sender
	 * @param cmd
	 * @param label
	 * @param args
	 */
	protected void witherWarning(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			return;
		}
		if (!WitherWarner.enabled) {
			sender.sendMessage("§cThis functionality has been disabled by " +
					"a server administrator.");
			return;
		}
		if (args.length == 1) {
			//Inform the player about the usage.
			sender.sendMessage("This functionality provides a notice when " +
					"making a wither that withers are 4 blocks tall, not 3.");
			sender.sendMessage("To opt out, do '/" + label + " WitherWarning off'.");
			sender.sendMessage("To opt back in, do '/" + label + " WitherWarning on'.");
			if (sender instanceof Player) {
				Player player = (Player) sender;
				 
				if (WitherWarner.optedOutPlayers.contains(player.getUniqueId())) {
					player.sendMessage("You are currently §copted out§r.");
				} else {
					player.sendMessage("You are currently §aopted in§r.");
				}
			}
			return;
		}
		if (args.length == 2) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cYou must be a player to use this command.");
				return;
			}
			Player player = (Player)sender;
			switch (args[1].toLowerCase()) {
			case "off": {
				if (WitherWarner.optedOutPlayers.contains(player.getUniqueId())) {
					sender.sendMessage("§cYou are already opted out!");
					return;
				}
				WitherWarner.optedOutPlayers.add(player.getUniqueId());
				sender.sendMessage("You have been §copted out§r.");
				return;
			}
			case "on": {
				if (!WitherWarner.optedOutPlayers.contains(player.getUniqueId())) {
					sender.sendMessage("§cYou are already opted in!");
					return;
				}
				WitherWarner.optedOutPlayers.remove(player.getUniqueId());
				sender.sendMessage("You have been §aopted in§r.");
				return;
			}
			}
		}
		
		sender.sendMessage("§cSyntax error!  Usage: /" + label + " WitherWarning [off/on]");
	}
	
	/**
	 * Reloads configs.
	 * @param sender
	 * @param cmd
	 * @param label
	 * @param args
	 */
	protected void reloadConfigs(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			return;
		}
		if (!PermissionHandler.HasPermission(sender, "sbe.mod.reload")) {
			return;
		}
		if (args.length == 1) {
			SkyblockExtension.inst().reloadAllConfigs();
			sender.sendMessage("§aSuccessfully reloaded all configs!");
			return;
		}
		if (args.length == 2) {
			switch (args[1].toLowerCase()) {
			case "main": //Fall through
			case "default": {
				SkyblockExtension.inst().reloadConfig();
				sender.sendMessage("§aSuccessfully reloaded the main configuration!");
				return;
			}
			case "block_value": {
				SkyblockExtension.inst().reloadBlockValueConfig();
				sender.sendMessage("§aSuccessfully reloaded the block value configuration!");
				return;
			}
			//Don't want to allow this.
			//case "crashes": {
			//	SkyblockExtension.inst().reloadCrashesConfig();
			//	sender.sendMessage("§aSuccessfully reloaded crashes config.");
			//}
			default: {
				sender.sendMessage("§c" + args[1] + " is not a recognised configuration!");
				return;
			}
			}
		}
		if (args.length >= 3) {
			sender.sendMessage("§cToo many arguments - see help.");
		}
	}
}
