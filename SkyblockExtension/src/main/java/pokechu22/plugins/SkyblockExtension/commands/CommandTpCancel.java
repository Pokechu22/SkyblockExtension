package pokechu22.plugins.SkyblockExtension.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import pokechu22.plugins.SkyblockExtension.PermissionHandler;
import pokechu22.plugins.SkyblockExtension.SkyblockExtension;
import pokechu22.plugins.SkyblockExtension.errorhandling.ErrorHandler;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

/**
 * A command that cancels your own TPA.
 *
 * @author Pokechu22
 *
 */
public class CommandTpCancel implements CommandExecutor, TabCompleter {
	/**
	 * Runnable for iterating thru all players and canceling TPAs.
	 *  
	 * @author Pokechu22
	 *
	 */
	public static class AsyncTpCanceler implements Runnable {
		public Player player;
		
		public AsyncTpCanceler(Player player) {
			this.player = player;
		}
		
		@Override
		public void run() {
			Essentials essentials = (Essentials)Bukkit.getPluginManager()
					.getPlugin("Essentials");
			
			int usedTeleports = 0;
			
			for (String userName : essentials.getUserMap()
					.getAllUniqueUsers()) {
				User user = essentials.getOfflineUser(userName);
				if (player.getName().equalsIgnoreCase(user
						.getTeleportRequest())) {
					player.sendMessage("§6Withdrew request from " + 
						user.getDisplayName() + "§6.");
					user.sendMessage(player.getDisplayName() + 
							"§6 withdrew their teleport request.");
					//Cancel the teleport.
					user.requestTeleport(null, false);
					usedTeleports++;
				}
			}
			if (usedTeleports == 0) {
				player.sendMessage("§cError: §4You do not have any " + 
						"pending requests.");
			}
		}
	}
	public boolean onCommand(CommandSender sender, Command cmd, 
			String label, String[] args) {
		try {
			if (!PermissionHandler.HasPermission(sender, 
					"sbe.commands.multichallenge")) {
				return true;
			}
			
			if (!(sender instanceof Player)) {
				sender.sendMessage(
						"§cYou must be a player to use this command.");
				return true;
			}
			
			Player player = (Player) sender;
			
			Bukkit.getScheduler().runTaskAsynchronously(SkyblockExtension.inst(), 
					new AsyncTpCanceler(player));
			
			return true;
		} catch (Throwable e) {
			ErrorHandler.logExceptionOnCommand(sender, cmd, label, args, e);
			return true;
		}
	}
	
	public List<String> onTabComplete(CommandSender sender, 
			Command cmd, String label, String args[]) {
		return new ArrayList<String>();
	}
}
