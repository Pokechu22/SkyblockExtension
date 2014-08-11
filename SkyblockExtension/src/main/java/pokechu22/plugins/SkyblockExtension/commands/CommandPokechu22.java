package pokechu22.plugins.SkyblockExtension.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.ChatPaginator;

import pokechu22.plugins.SkyblockExtension.ErrorHandler;
import pokechu22.plugins.SkyblockExtension.PermissionHandler;
import pokechu22.plugins.SkyblockExtension.SkyblockExtension;

/**
 * Provides support for the /pokechu22 command, which does basic stuff.
 * 
 * @author Pokechu22
 * 
 */
public class CommandPokechu22 {
	
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
			}
		}
		sender.sendMessage("Usage: /" + label + " help");
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
}
