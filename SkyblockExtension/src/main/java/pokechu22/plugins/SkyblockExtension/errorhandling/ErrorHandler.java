package pokechu22.plugins.SkyblockExtension.errorhandling;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
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
	 * Should errors be broadcast by default?
	 * (Only goes to players with specific permissions)
	 */
	public static boolean broadcastOnError;
	
	/**
	 * List of previous crashes.
	 */
	public static List<CrashReport> errors = new ArrayList<CrashReport>();
	
	/**
	 * Logs an error.  Uses the broadcast value to check.
	 * @param c The crash report.
	 */
	public static void logError (CrashReport c) {
		logError(c, ErrorHandler.broadcastOnError);
	}
	
	/**
	 * Logs an error, and broadcasts it if enabled.
	 * @param c
	 * @param broadcast
	 */
	public static void logError (CrashReport c, boolean broadcast) {
		errors.add(c);
		if (broadcast) {
			broadcastError(c);
		}
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
		
		int mainIndex = 0;
		
		for (int i = 0; i < ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 2; i ++) {
			int currentIndex = mainIndex + first;
			mainIndex ++;
			if (currentIndex >= errors.size()) {
				sender.sendMessage(""); //Due to no message, just skip.
				continue;
			}
			CrashReport c = errors.get(currentIndex);
			if (c.isHiddenFrom(sender.getName())) {
				//This iteration of the loop didn't happen.
				i --;
				continue;
			}
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
	
	/**
	 * Gets the number of errors that this sender has not yet read.
	 * @param sender
	 */
	public static int getNumberOfUnreadIssues(CommandSender sender) {
		int returned = 0;
		for (CrashReport c : errors) {
			if (!c.readers.contains(sender.getName())) {
				returned++;
			}
		}
		return returned;
	}
	
	/**
	 * Broadcasts a new error to specific players.
	 * @param report
	 */
	public static void broadcastError(CrashReport report) {
		Bukkit.getServer().broadcast("§c[SBE]: An error occured: " + 
				report.getTitle(9999999), 
				"sbe.debug.crashes.broadcast");
		Bukkit.getServer().broadcast("§c(View this error in " + 
				"§4/pokechu22 crashes list§c)",
				"sbe.debug.crashes.broadcast");
	}
	
	/**
	 * Gets a crash report by an ID, for use in commands.
	 * @param id
	 * @return The CrashRreport.
	 * @throws IllegalArgumentException when ID is invalid.
	 * 			(Contains player-friendly context)
	 */
	public static CrashReport getReportByID(String id) 
			throws IllegalArgumentException {
		return getReportByID(id, null);
	}
	
	/**
	 * Gets a crash report by an ID, for use in commands.
	 * @param id
	 * @param usage The usage text to provide.  Do not prepend with \n; 
	 * 				that's done automatically.  If null, no usage is given.
	 * 				However, if an empty string (""), it will have a newline
	 * 				beforehand, which looks odd and is probably a bad thing.
	 * @return The CrashReport.
	 * @throws IllegalArgumentException when ID is invalid.  
	 * 			(Contains player-friendly context)
	 */
	public static CrashReport getReportByID(String id, String usage) 
			throws IllegalArgumentException {
		int CrashID;
		
		//Kind of odd-seeming, but this means that it will be on a new line.
		if (usage != null) {
			usage = "\n" + usage;
		} else {
			usage = "";
		}
		
		try {
			CrashID = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("§cFailed to parse crash ID." +
					"(Got " + id + ", expected Integer)." + usage);
		}
		
		if (ErrorHandler.getNumberOfCrashes() == 0) {
			throw new IllegalArgumentException("§cThere are no crashes to show!");
		}
		
		if (CrashID > ErrorHandler.getLastCrashID()) {
			throw new IllegalArgumentException("§cCrash ID is beyond the " +
					"maximum!\n§cMaximum ID is currently " + ErrorHandler
					.getLastCrashID() +	", got " + id + "." + usage);
		}
		
		if (CrashID < 0) {
			throw new IllegalArgumentException("§cCrash ID not allowed to" +
					"be negative!" + usage);
		}
		
		return errors.get(CrashID);
	}
}
