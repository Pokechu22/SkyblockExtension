package pokechu22.plugins.SkyblockExtension.errorhandling;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Broadcasts information about existing errors when a player logins.
 * @author Pokechu22
 *
 */
public class LoginErrorBroadcaster implements Listener {
	/**
	 * Should errors be broadcast on player login?
	 * (Only goes to players with specific permissions)
	 * 
	 * This basically controls whether or not this is enabled.
	 */
	public static boolean broadcastOnLogin = true;
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerLogin(PlayerJoinEvent e) {
		if (LoginErrorBroadcaster.broadcastOnLogin) {
			if (e.getPlayer()
					.hasPermission("sbe.debug.crashes.loginBroadcast")) {
				int unreadReports = ErrorHandler.getNumberOfUnreadIssues(
						e.getPlayer());
				if (unreadReports == 0) {
					//TODO: Might want to still have a message.
					return;
				}
				if (unreadReports == 1) {
					//Singular case.
					e.getPlayer().sendMessage("�4There is " + unreadReports + 
							" report that you have not yet read.");
					e.getPlayer().sendMessage("�4To view it, do " + 
							"�e/pokechu22 crashes list�4.");
					return;
				}
				e.getPlayer().sendMessage("�4There are " + unreadReports + 
						" reports that you have not yet read.");
				e.getPlayer().sendMessage("�4To view these, do " + 
						"�e/pokechu22 crashes list�4.");
			}
		}
	}
}
