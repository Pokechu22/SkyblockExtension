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
	public static boolean Run(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
		//TODO: This is debug code that should be removed.
		if (args.length != 0) {
			if (args[0].equalsIgnoreCase("catchtest")) {
				throw new Exception("Test");
			}
			if (args[0].equalsIgnoreCase("crashes")) {
				ErrorHandler.listCrashes(sender, 0);
				return true;
			}
		}
		
		
		sender.sendMessage("Command sent sucessfully"); //TODO: Test.
		return true;
	}
}
