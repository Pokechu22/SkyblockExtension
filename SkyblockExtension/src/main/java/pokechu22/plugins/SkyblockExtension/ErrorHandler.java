package pokechu22.plugins.SkyblockExtension;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Handles player-accessible logging of crashes.
 * @author Pokechu22
 *
 */
public class ErrorHandler {
	/**
	 * List of previous crashes.
	 */
	protected static List<CrashReport> errors;
	
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
}