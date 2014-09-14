package pokechu22.plugins.SkyblockExtension.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.Commandtpa;

/**
 * A command that cancels your own TPA.
 *
 * @author Pokechu22
 *
 */
@SuppressWarnings("unused")
public class CommandTpCancel {
	/**
	 * Runnable for iterating thru all players and canceling TPAs.
	 *  
	 * @author Pokechu22
	 *
	 */
	public static class AsyncTpCanceler implements Runnable {
		
		
		@Override
		public void run() {
			//TODO
		}
	}
	public static void Run(CommandSender sender, Command cmd, 
			String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(
					"§cYou must be a player to use this command.");
			return;
		}
		
		Player player = (Player) sender;
		
		Essentials e = (Essentials)Bukkit.getPluginManager()
				.getPlugin("Essentials");
		
		//Iterate thru all teleport requests.
		Set<String> userNames = e.getUserMap().getAllUniqueUsers();
	}
	
	public static List<String> onTabComplete(CommandSender sender, 
			Command cmd, String label, String args[]) {
		return new ArrayList<String>();
	}
}
