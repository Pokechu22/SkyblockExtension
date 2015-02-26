package pokechu22.plugins.SkyblockExtension.commands;

import static pokechu22.plugins.SkyblockExtension.util.TabCompleteUtil.*;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import pokechu22.plugins.SkyblockExtension.PermissionHandler;
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
	@Override
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
			
			if (args.length != 1) {
				sender.sendMessage("Usage: /" + label + " <playerName>");
				return true;
			}
			
			Player player = (Player) sender;
			
			Essentials essentials = (Essentials)Bukkit.getPluginManager()
					.getPlugin("Essentials");
			
			User user = essentials.getOfflineUser(args[0]);
			if (user == null) {
				sender.sendMessage("§cThere is no such player " + 
						args[0] + ".");
				return true;
			}
			if (player.getName().equalsIgnoreCase(
					user.getTeleportRequest())) {
				player.sendMessage("§6Withdrew request from " + 
						user.getDisplayName() + "§6.");
				user.sendMessage(player.getDisplayName() + 
						"§6 withdrew their teleport request.");
				//Cancel the teleport, by setting the person who has
				//requested a teleport to that user to null.
				user.requestTeleport(null, false);
			} else {
				player.sendMessage("§cYou have no pending teleport for " +
						user.getDisplayName() + "§c.");
			}
			
			return true;
		} catch (Throwable e) {
			ErrorHandler.logExceptionOnCommand(sender, cmd, label, args, e);
			return true;
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, 
			Command cmd, String label, String args[]) {
		if (args.length == 0) {
			ArrayList<String> returned = new ArrayList<>();
			for (Player player : Bukkit.getOnlinePlayers()) {
				returned.add(player.getName());
			}
			return returned;
		}
		if (args.length == 1) {
			ArrayList<String> returned = new ArrayList<>();
			for (Player player : Bukkit.getOnlinePlayers()) {
				returned.add(player.getName());
			}
			return TabLimit(returned, args[0]);
		}
		
		return new ArrayList<String>();
	}
}
