package pokechu22.plugins.SkyblockExtension.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Command that removes unauthorized players from one's island.
 * 
 * Syntax: /expunge &lt;player&gt;.
 * 
 * Can only be used on nonmembers and guests.
 * 
 * @author Pokechu22
 *
 */
public class CommandExpunge {
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
		return null;
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
		
	}
}
