package pokechu22.plugins.SkyblockExtension;

import org.bukkit.command.CommandSender;

/**
 * Handles all permissions.
 * @author Pokechu22
 *
 */
public class PermissionHandler {
	
	/**
	 * The permission error message, displayed when a player doesn't have the 
	 * needed permission.
	 * <br><br>
	 * It looks like this in-game: 
	 * "<font color=#FF5555>You don't have the needed permission: 
	 * sbe.permission.example</font>"
	 */
	protected static final String permissionErrorMessage = 
			"§cYou don't have the needed permision: ";
	
	/**
	 * Checks if a player has a permission.  
	 * 
	 * If it fails, it sends the player an error message.
	 * 
	 * Currently a (Fairly pointless) wrapper for CommandSender.hasPermision(String),
	 * but will have a use in a bit.
	 * @param sender The CommandSender to test.
	 * @param permission The permission to check.
	 */
	public static boolean HasPermision(CommandSender sender, String permission) {
		if (sender.hasPermission(permission)) {
			return true;
		} else {
			sender.sendMessage(permissionErrorMessage + permission);
			return false;
		}
	}
}
