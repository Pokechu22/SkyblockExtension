package pokechu22.plugins.SkyblockExtension;

import org.bukkit.command.CommandSender;

/**
 * Handles all permissions.
 * @author Pokechu22
 *
 */
public class PermissionHandler {
	/**
	 * Checks if a player has a permission.  
	 * 
	 * Currently a (Fairly pointless) wrapper for CommandSender.hasPermision(String),
	 * but will have a use in a bit.
	 * @param sender The CommandSender to test.
	 * @param permission The permission to check.
	 */
	public static boolean HasPermision(CommandSender sender, String permission) {
		return sender.hasPermission(permission);
	}
	
	/**
	 * Sends a player the permission error message.
	 * 
	 * @param sender The CommandSender
	 * @param permission The permission.
	 */
	public static void SendPermisionError(CommandSender sender, String permission) {
		sender.sendMessage("§cYou don't have the needed permision: " + permission);
	}
}
