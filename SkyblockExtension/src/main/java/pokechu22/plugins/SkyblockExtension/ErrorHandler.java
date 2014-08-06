package pokechu22.plugins.SkyblockExtension;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.ChatPaginator;

/**
 * Handles player-accessible logging of crashes.
 * @author Pokechu22
 *
 */
public class ErrorHandler {
	/**
	 * List of previous crashes.
	 */
	protected static List<CrashReport> errors = new ArrayList<CrashReport>();
	
	/**
	 * Logs an error.
	 * @param c The crash report.
	 */
	public static void logError (CrashReport c) {
		errors.add(c);
	}
	
	/**
	 * Logs an error.
	 * 
	 * All arguments are converted to a single {@link CrashReport}, and then calls
	 * {@link #logError(CrashReport)} with it.
	 */
	public static void logError (Throwable thrown, CommandSender sender, Command cmd, String label, String[] args) {
		logError(new CrashReport(thrown,sender,cmd,label,args));
	}
	
	/**
	 * Lists available crash reports.
	 * @param sender Who do send the reports to.
	 * @param first The one to display at the top of the list..
	 */
	public static void listCrashes(CommandSender sender, int first) {
		sender.sendMessage("§a-=[§3Crashes§a]=- §f(" + first + " to " +
				(first + ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 2) + 
				" of " + errors.size() + ")");
		sender.sendMessage("§aGreen: Read by me§f, §eYellow:Read by others§f, §cRed: Unread");
		
		for (int i = 0; i < ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 2; i ++) {
			if (i >= errors.size()) {
				sender.sendMessage(""); //Due to no message, just skip.
				continue;
			}
			CrashReport c = errors.get(i);
			sender.sendMessage(c.getTitleFor(sender.getName(), 
					ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH));
			
		}
	}
}