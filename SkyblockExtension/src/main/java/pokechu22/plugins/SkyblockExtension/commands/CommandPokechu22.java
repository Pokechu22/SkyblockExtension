package pokechu22.plugins.SkyblockExtension.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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
		if (args.length != 0) {
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
					int line;
					try {
						line = Integer.parseInt(args[2]);
					} catch (NumberFormatException e) {
						sender.sendMessage("§cFailed to parse line number (Got " +args[2] + ").");
						sender.sendMessage("Usage: /" + label + " crashes help");
						return;
					}
					ErrorHandler.listCrashes(sender, line);
					return;
				} else {
					sender.sendMessage("§cError: Too many parameters.");
					sender.sendMessage("Usage: /" + label + " crashes help");
					return;
				}
			}
		}
		sender.sendMessage("Usage: /" + label + " crashes help");
		return;
	}
}
