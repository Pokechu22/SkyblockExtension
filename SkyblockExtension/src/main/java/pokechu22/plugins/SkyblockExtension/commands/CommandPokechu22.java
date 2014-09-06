package pokechu22.plugins.SkyblockExtension.commands;

import static pokechu22.plugins.SkyblockExtension.util.TabCompleteUtil.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.ChatPaginator;

import pokechu22.plugins.SkyblockExtension.ErrorHandler;
import pokechu22.plugins.SkyblockExtension.PermissionHandler;
import pokechu22.plugins.SkyblockExtension.SkyblockExtension;
import pokechu22.plugins.SkyblockExtension.protection.IslandInfo;
import pokechu22.plugins.SkyblockExtension.protection.ProtectionHandler;
import pokechu22.plugins.SkyblockExtension.protection.USkyBlockPlayerInfoConverter;
import pokechu22.plugins.SkyblockExtension.protection.USkyBlockProtectionListener;
import pokechu22.plugins.SkyblockExtension.util.IslandUtils;

/**
 * Provides support for the /pokechu22 command, which does basic stuff.
 * 
 * @author Pokechu22
 * 
 */
public class CommandPokechu22 {
	
	/**
	 * Information for when help is created.
	 * This controls args[0]'s effects.
	 */
	private static final Map<String, String> subCommands;
	/*
	 * Static initializer for subCommands.
	 */
	static {
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("test", "Various tests for debuging purposes.  " 
				+ "Most of these commands will be of no useless to " + 
				"actual players.");
		map.put("crashes", "Handles any error reports that may occur.");
		map.put("logo", "Shows a rather pointless logo.");
		map.put("help", "View the help.");
		
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
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("ThrowableReport", "A test that creates an example " + 
				"ThrowableReport.");
		map.put("ConfigurationErrorReport", "A test that creates example " +
				"ConfigurationErrorReports, using all avaliable " + 
				"constructors.");
		map.put("IsProtected", "Checks if the area you are currently in " + 
				"allows you to place blocks.");
		//TODO Remove this, as it WILL be done automatically.
		map.put("ReplaceDefaultProtections", "Replaces the default " + 
				"USkyBlockProtection system with the custom one which " + 
				"allows for player-based protection choises.  " + 
				"NOTE: This should be done automatically.  Remove this.");
		map.put("MyIslandLocation", "Provides your own island location.");
		map.put("NearestIslandLocation", "Provides the nearest valid " + 
				"island location.  This location should have a bedrock " + 
				"block in it.");
		map.put("IslandInfoSerialization", "Tests serializing and " + 
				"deserializing of an IslandInfo, to an NBT file.  This " +
				"WILL override the IslandInfo for 0,0; however, this " + 
				"be spawn, and thus not be an issue.");
		map.put("USkyBlockPlayerInfoConversion", "Manualy converts all " +
				"of the USkyBlock PlayerInfo's (which contain island " + 
				"and challenge information for each player) to the custom" +
				"format used here.  This is done when needed normally " + 
				"(EG on the first run), and for any new players as well, " +
				"but can be forced using this.");
		
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
		HashMap<String, String> map = new HashMap<String, String>();
		
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
	public static void Run(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage("Usage: /" + label + " help");
			return;
		} else {
			switch (args[0].toLowerCase()) {
			case "test": {
				Test(sender, cmd, label, args);
				return;
			}
			case "crashes": {
				Crashes(sender,cmd,label,args);
				return;
			}
			case "logo": {
				//This is pointless, but is ported from the old version.
				ShowLogo(sender);
				return;
			}
			case "help": {
				Help(sender, cmd, label, args);
			}
			}
		}
		sender.sendMessage("Usage: /" + label + " help");
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
	public static List<String> onTabComplete(CommandSender sender, Command cmd, String label, String args[]) {
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
			switch (args[1]) {
			case "test": {
				return TabLimit(tests.keySet(), args[2]);
			}
			case "crashes": {
				return TabLimit(crashesCommands.keySet(), args[2]);
			}
			default: {
				return new ArrayList<String>();
			}
			}
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
	protected static void Crashes(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			throw new Error("Args.length should NEVER be 0.");
		}
		if (args.length == 1) {
			sender.sendMessage("Usage: /" + label + " crashes help");
			return;
		}
		if (args[1].equalsIgnoreCase("list")) {
			if (PermissionHandler.HasPermision(sender, "sbe.debug.crashes.list")) {
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
			if (PermissionHandler.HasPermision(sender, "sbe.debug.crashes.show")) {
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
			if (PermissionHandler.HasPermision(sender, "sbe.debug.crashes.remove")) {
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
			if (PermissionHandler.HasPermision(sender, "sbe.debug.crashes.reset")) {
				if (args.length >= 4) {
					sender.sendMessage("§cError: Too many parameters.");
					sender.sendMessage("For usage, do /" + label + " crashes help");
					return;
				}
				if (args.length == 2) {
					sender.sendMessage("§c§lAre you sure you wish to reset all previous crash logging?");
					sender.sendMessage("To confirm, please run /" + label + " crashes reset " + sender.getName().hashCode());
					return;
				}
				if (args.length == 3) {
					int confirmationCode;
					try {
						confirmationCode = Integer.parseInt(args[2]);
					} catch (NumberFormatException e) {
						//We can assume that if it is not an integer, it doesn't match an integer.
						sender.sendMessage("§cConfirmation code does not match.");
						sender.sendMessage("To confirm, please run /" + label + " crashes reset " + sender.getName().hashCode());
						return;
					}
					if (confirmationCode != sender.getName().hashCode()) {
						sender.sendMessage("§cConfirmation code does not match.");
						sender.sendMessage("To confirm, please run /" + label + " crashes reset " + sender.getName().hashCode());
						return;
					}
					ErrorHandler.resetAllCrashes(sender);
					return;
				}
				return;
			}
			return;
		}
		sender.sendMessage("Usage: /" + label + " crashes help");
		return;
	}
	
	/**
	 * Handles the various tests.  
	 * @param sender
	 * @param cmd
	 * @param label
	 * @param args
	 */
	protected static void Test(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 2) {
			sender.sendMessage("§cError: Too few arguments.");
			return;
		}
		
		if (args[1].equalsIgnoreCase("ThrowableReport")) {
			if (PermissionHandler.HasPermision(sender, "sbe.debug.test.ThrowableReport")) {
				throw new RuntimeException("Test");
			}
			return;
		}
		
		if (args[1].equalsIgnoreCase("ConfigurationErrorReport")) {
			if (PermissionHandler.HasPermision(sender,"sbe.debug.test.ConfigErrorReport")) {
				//Test ALL the constructors!
				sender.sendMessage("Testing all configurationerrorreport constructors.");
				sender.sendMessage("Check /pokechu22 crashes list.");
				SkyblockExtension.inst().getLogger().warning(sender.getName() + 
						" just tested ConfigurationErrorReports...");
				//With loading...
				ErrorHandler.logError(
						new pokechu22.plugins.SkyblockExtension.ConfigurationErrorReport(
								false));
				ErrorHandler.logError(
						new pokechu22.plugins.SkyblockExtension.ConfigurationErrorReport(
								new Exception("Test").fillInStackTrace(), 
								false));
				ErrorHandler.logError(
						new pokechu22.plugins.SkyblockExtension.ConfigurationErrorReport(
								"pokechu22.plugins.SkyblockExtension.CommandPokechu22",
								false));
				ErrorHandler.logError(
						new pokechu22.plugins.SkyblockExtension.ConfigurationErrorReport(
								new Exception("Test").fillInStackTrace(),
								"pokechu22.plugins.SkyblockExtension.CommandPokechu22", 
								false));
				ErrorHandler.logError(
						new pokechu22.plugins.SkyblockExtension.ConfigurationErrorReport(
								"Key.Example", 
								SkyblockExtension.inst().getConfig().getName(), 
								false));
				ErrorHandler.logError(
						new pokechu22.plugins.SkyblockExtension.ConfigurationErrorReport(
								new Exception("Test").fillInStackTrace(), 
								"Key.Example", 
								SkyblockExtension.inst().getConfig().getName(), 
								false));
				ErrorHandler.logError(
						new pokechu22.plugins.SkyblockExtension.ConfigurationErrorReport(
								"Key.Example", 
								SkyblockExtension.inst().getConfig().getName(), 
								"pokechu22.plugins.SkyblockExtension.CommandPokechu22",
								false));
				ErrorHandler.logError(
						new pokechu22.plugins.SkyblockExtension.ConfigurationErrorReport(
								new Exception("Test").fillInStackTrace(), 
								"Key.Example", 
								SkyblockExtension.inst().getConfig().getName(), 
								"pokechu22.plugins.SkyblockExtension.CommandPokechu22",
								false));
				//And with saving.
				ErrorHandler.logError(
						new pokechu22.plugins.SkyblockExtension.ConfigurationErrorReport(
								true));
				ErrorHandler.logError(
						new pokechu22.plugins.SkyblockExtension.ConfigurationErrorReport(
								new Exception("Test").fillInStackTrace(), 
								true));
				ErrorHandler.logError(
						new pokechu22.plugins.SkyblockExtension.ConfigurationErrorReport(
								"pokechu22.plugins.SkyblockExtension.CommandPokechu22",
								true));
				ErrorHandler.logError(
						new pokechu22.plugins.SkyblockExtension.ConfigurationErrorReport(
								new Exception("Test").fillInStackTrace(),
								"pokechu22.plugins.SkyblockExtension.CommandPokechu22", 
								true));
				ErrorHandler.logError(
						new pokechu22.plugins.SkyblockExtension.ConfigurationErrorReport(
								"Key.Example", 
								SkyblockExtension.inst().getConfig().getName(), 
								true));
				ErrorHandler.logError(
						new pokechu22.plugins.SkyblockExtension.ConfigurationErrorReport(
								new Exception("Test").fillInStackTrace(), 
								"Key.Example", 
								SkyblockExtension.inst().getConfig().getName(), 
								true));
				ErrorHandler.logError(
						new pokechu22.plugins.SkyblockExtension.ConfigurationErrorReport(
								"Key.Example", 
								SkyblockExtension.inst().getConfig().getName(), 
								"pokechu22.plugins.SkyblockExtension.CommandPokechu22",
								true));
				ErrorHandler.logError(
						new pokechu22.plugins.SkyblockExtension.ConfigurationErrorReport(
								new Exception("Test").fillInStackTrace(), 
								"Key.Example", 
								SkyblockExtension.inst().getConfig().getName(), 
								"pokechu22.plugins.SkyblockExtension.CommandPokechu22",
								true));

				return;
			}
			return;
		}
		
		if (args[1].equalsIgnoreCase("IsProtected")) {
			if (!PermissionHandler.HasPermision(sender,"sbe.debug.test.IsProtected")) {
				return;
			}
			
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cYou must be a player.");
				return;
			}
			Player player = (Player) sender;
			if (ProtectionHandler.getProtectionHandler().isProtected(
					player.getLocation(), player)) {
				sender.sendMessage("You have permission in this area.");
			} else {
				sender.sendMessage("You do not have permission in this area.");
			}
			return;
		}
		
		if (args[1].equalsIgnoreCase("ReplaceDefaultProtections")) {
			if (!PermissionHandler.HasPermision(sender, "sbe.debug.test.ReplaceDefaultProtections")) {
				return;
			}
			sender.sendMessage("Removing default uSkyBlock protection system.");
			USkyBlockProtectionListener.removeExistingProtectionEvents();
			sender.sendMessage("Installing new protection system.");
			SkyblockExtension
					.inst()
					.getServer()
					.getPluginManager()
					.registerEvents(new USkyBlockProtectionListener(),
							SkyblockExtension.inst());
			sender.sendMessage("Done!");
			return;
		}
		
		if (args[1].equalsIgnoreCase("MyIslandLocation")) {
			//Provides location of own island.
			if (!PermissionHandler.HasPermision(sender,"sbe.debug.test.MyIslandLocation")) {
				return;
			}
			
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cYou must be a player.");
				return;
			}
			Player player = (Player) sender;
			if (!IslandUtils.canGetPlayerInfo(player)) {
				sender.sendMessage("§cError: You do not have an island.");
				return;
			}
			//ToString is commented out because it caused issues with null.  
			//The same effect is triggered automatically, but null is displayed as "null".
			sender.sendMessage("§6getIslandLocation(): §e" + 
					IslandUtils.getPlayerInfo(player).getIslandLocation()/*.toString()*/);
			sender.sendMessage("§6getPartyIslandLocation(): §e" + 
					IslandUtils.getPlayerInfo(player).getPartyIslandLocation()/*.toString()*/);
			sender.sendMessage("§6getHomeLocation(): §e" + 
					IslandUtils.getPlayerInfo(player).getHomeLocation()/*.toString()*/);
			return;
		}
		
		if (args[1].equalsIgnoreCase("NearestIslandLocation")) {
			//Provides location of nearest island.
			if (!PermissionHandler.HasPermision(sender,"sbe.debug.test.NearestIslandLocation")) {
				return;
			}
			
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cYou must be a player.");
				return;
			}
			Player player = (Player) sender;
			Location location = IslandUtils.getOccupyingIsland(player.getLocation());
			sender.sendMessage("Nearest island is at " + location + ".");
			if (location.getBlock().getType() == Material.BEDROCK) {
				sender.sendMessage("Bedrock is found.");
			} else {
				sender.sendMessage("Bedrock is not found.");
			}
			return;
		}
		if (args[1].equalsIgnoreCase("IslandInfoSerialization")) {
			if (!PermissionHandler.HasPermision(sender,
					"sbe.debug.test.IslandInfoSerialization")) {
				return;
			}
			
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cYou must be a player.");
				return;
			}
			
			Player player = (Player) sender;
			
			try {
				IslandInfo info = new IslandInfo(new Location(player.getWorld(),
						0,120,0), player);
				player.sendMessage("Starting NBT data: ");
				player.sendMessage(info.serializeToNBT().toString());
				info.saveToDisk();
				player.sendMessage("Saved.");
				IslandInfo loaded = IslandInfo.readFromDisk(0, 0);
				player.sendMessage("Loaded.");
				player.sendMessage("Loaded NBT data: ");
				player.sendMessage(loaded.serializeToNBT().toString());
				player.sendMessage("Done.");
				return;
			} catch (Exception e) {
				player.sendMessage("Exception: " + e.toString());
				e.printStackTrace();
				return;
			}
		}
		if (args[1].equalsIgnoreCase("USkyBlockPlayerInfoConversion")) {
			//Very light test of conversion.
			if (!PermissionHandler.HasPermision(sender,
					"sbe.debug.test.USkyBlockPlayerInfoConversion")) {
				return;
			}
			
			USkyBlockPlayerInfoConverter.start();
			sender.sendMessage("Started.");
			
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
	private static void ShowLogo(CommandSender sender) {
		sender.sendMessage(logoText);
	}
	
	/**
	 * The error message for when no help available.
	 */
	private static final String nonexistantHelpMessage = 
			"§c: " + "There is no help for this subcommand.  It may be " + 
			"an invalid command, or it may be that help has not been " +
			"written.  Note: If you are including a parameter, don't do " +
			"that.  Try removing each parameter, one at a time.";
	
	/**
	 * Shows help information.
	 * @param sender
	 * @param cmd
	 * @param label
	 * @param args
	 */
	protected static void Help(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			//Shouldn't happen.
			return;
		}
		String[] helpArgs = Arrays.copyOfRange(args, 1, args.length);
		if (helpArgs.length == 0) {
			//Message prepended to each message.
			final String preface = "§7/" + label + " ";
			for (Map.Entry<String, String> entry : subCommands.entrySet()) {
				sender.sendMessage(preface + entry.getKey() + "§f:" + entry.getValue());
			}
			return;
		}
		if (helpArgs.length == 1) {
			//Message prepended to each message.
			final String preface = "§7/" + label + " " + helpArgs[0] + " ";
			
			//If there is no help message...
			if (!subCommands.containsKey(helpArgs[0].toLowerCase())) {
				sender.sendMessage(preface + nonexistantHelpMessage);
				return;
			}
		    
			//Send the root message if it exists
			sender.sendMessage(preface + "§f: " + 
					subCommands.get(helpArgs[0].toLowerCase()));
			
			switch (helpArgs[0].toLowerCase()) {
			case "test": {
				//Send the sub-help.
				for (Map.Entry<String, String> entry : subCommands.entrySet()) {
					sender.sendMessage(preface + entry.getKey() + "§f: " + entry.getValue());
				}
				return;
			}
			case "crashes": {
				//Send the sub-help.
				for (Map.Entry<String, String> entry : subCommands.entrySet()) {
					sender.sendMessage(preface + entry.getKey() + "§f: " + entry.getValue());
				}
				return;
			}
			}
		}
	}
}
