package pokechu22.plugins.SkyblockExtension.errorhandling;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.ChatPaginator;

import pokechu22.plugins.SkyblockExtension.SkyblockExtension;

/**
 * Handles player-accessible logging of crashes.
 * @author Pokechu22
 *
 */
public class ErrorHandler {
	/**
	 * List of previous crashes.
	 */
	public static List<CrashReport> errors = new ArrayList<CrashReport>();
	
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
	 * @deprecated
	 */
	public static void logError (Throwable thrown, CommandSender sender, Command cmd, String label, String[] args) {
		logError(new ThrowableReport(thrown,sender,cmd,label,args));
	}
	
	/**
	 * Lists available crash reports.
	 * @param sender Who do send the reports to.
	 * @param first The one to display at the top of the list..
	 */
	public static void listCrashes(CommandSender sender, int first) {
		if (first < 0) {
			sender.sendMessage("§cThe first crash shown cannot be negative.");
		}
		sender.sendMessage("§a-=[§3Crashes§a]=- §f(" + first + " to " +
				(first + ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 2 - 1) + 
				" of " + errors.size() + ")");
		sender.sendMessage("§aGreen: Read by me§f, §eYellow: Read by others§f, §cRed: Unread");
		
		for (int i = 0; i < ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 2; i ++) {
			int currentIndex = i + first;
			if (currentIndex >= errors.size()) {
				sender.sendMessage(""); //Due to no message, just skip.
				continue;
			}
			CrashReport c = errors.get(currentIndex);
			sender.sendMessage(currentIndex + ": " + c.getTitleFor(sender.getName(), 
					ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH - 
					(Integer.toString(currentIndex).length() + 2)));
			
		}
	}
	
	public static void getCrashInfo(CommandSender sender, int CrashID, int page) {
		if (CrashID >= errors.size() || CrashID < 0) {
			sender.sendMessage("§4Crash report " + CrashID + " does not exist (out of bounds)!");
		}
		
		CrashReport c = errors.get(CrashID);
		int lastPage = c.getNumberOfPages(ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 1) + 1;
		
		if (page > lastPage) {
			sender.sendMessage("§cPage " + page + " is out of bounds.  (Maximum is " + 
					lastPage + ")");
			return;
		}
		
		sender.sendMessage("§a-=[§3Crash ID " + CrashID + "§a]=- §f(Page " + 
				page + " of " + lastPage + ")");
		
		int startingIndex = (page - 1) * (ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 1);
		
		//Not using ChatPaginator.paginate, because it behaves stupidly with out of bounds data.
		String[] message = ChatPaginator.wordWrap(c.getAsText(sender.getName()), 
				ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH);
		
		for (int i = 0; i < ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 1; i++) {
			int usedIndex = i + startingIndex;
			if (usedIndex >= message.length) {
				sender.sendMessage("");
			} else {
				sender.sendMessage(message[usedIndex]);
			}
		}
		
	}
	
	/**
	 * Gets the number of crashes that have occurred.
	 * @return the number of crashes.
	 */
	public static int getNumberOfCrashes() {
		return errors.size();
	}
	
	/**
	 * Gets the last available crash ID.
	 * @return the last available crash ID.
	 */
	public static int getLastCrashID() {
		return getNumberOfCrashes() - 1;
	}
	
	/**
	 * Removes a single crash. <br>
	 * Will not attempt to check if the crash is in-bounds.
	 * 
	 * @param CrashID The crash to remove.
	 */
	public static void removeCrash(int CrashID) {
		errors.remove(CrashID);
	}
	
	/**
	 * Removes all previous crashes.
	 * @param sender
	 */
	public static void resetAllCrashes(CommandSender sender) {
		errors.clear();
		SkyblockExtension.inst().getLogger().info(
				"All crashes reset by " + sender.getName() + ".");
		sender.sendMessage("All crashes successfully reset.");
	}
}