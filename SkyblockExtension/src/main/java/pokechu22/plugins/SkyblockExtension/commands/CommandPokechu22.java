package pokechu22.plugins.SkyblockExtension.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.ChatPaginator;

import pokechu22.plugins.SkyblockExtension.ErrorHandler;

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
			case "crashtest": {
				throw new RuntimeException("Test"); //TODO Test code.
			}
			case "crashes": {
				Crashes(sender,cmd,label,args);
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
		} else {
			if (args[1].equalsIgnoreCase("list")) {
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
			if (args[1].equalsIgnoreCase("show")) {
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
		}
		sender.sendMessage("Usage: /" + label + " crashes help");
		return;
	}
}
