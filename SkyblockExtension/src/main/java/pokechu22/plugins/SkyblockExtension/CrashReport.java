package pokechu22.plugins.SkyblockExtension;

import java.util.Date;
import java.util.HashSet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * An individual crash report.
 * Needed to store data in the settings.
 * 
 * @author Pokechu22
 *
 */
public class CrashReport {
	//From the constructor.
	public Throwable thrown;
	public CommandSender sender;
	public Command cmd;
	public String label;
	public String[] args;
	
	/**
	 * When the error occurred.
	 */
	public Date loggedDate;
	
	/**
	 * Who has read the report.
	 */
	public HashSet<String> readers;
	
	/**
	 * Creates a crash report.
	 */
	public CrashReport(Throwable thrown, CommandSender sender, Command cmd, String label, String[] args) {
		this.thrown = thrown;
		this.sender = sender;
		this.cmd = cmd;
		this.label = label;
		this.args = args;
		
		this.loggedDate = new Date(); //Current time.
		this.readers = new HashSet<String>();
	}
	
	/**
	 * Gets the text of this crash report, and sender to the list of people who
	 * have viewed this crash report.
	 * @param sender
	 * @return The text.
	 */
	public String getAsText(String sender) {
		readers.add(sender);
		return getAsTextNoMark();
	}
	
	/**
	 * Gets the text of this crash report, without marking a specific reader.
	 * @return The text.
	 */
	public String getAsTextNoMark() {
		StringBuilder text = new StringBuilder();
		
		text.append(thrown.toString() + "\n");
		text.append("Occured on: " + loggedDate.toString() + "\n"); 
		text.append("Sender: " + sender.getName() + "\n"); 
		text.append("Command: " + cmd.getName() + "(Label: " + label + ")\n");
		text.append("Arguments: Length = " + args.length + "\n"); 
		//Add each argument.
		for (int i = 0; i < args.length; i++) {
			text.append("args[" + i + "]: " + args[i] + "\n");
		}
		//Add the stacktrace.
		text.append("Stacktrace: \n");
		StackTraceElement[] elements = thrown.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			text.append(elements[i].toString());
		}
		
		return text.toString();
				
	}
	
	/**
	 * Gets the title of the crash report, shortened to fit within sizeLimit.
	 * If it does not fit, it has the text "..." put on the end.
	 * @param sizeLimit The size that the title must fit in.
	 * @return The title.
	 */
	public String getTitle(int sizeLimit) {
		return getTitle(sizeLimit, "...");
	}
	
	/**
	 * Gets the title of the crash report, shortened to fit within sizeLimit.
	 * If it does not fit, it has trailOff's value put on the end.
	 * @param sizeLimit
	 * @param trailOff
	 * @return The title.
	 * @throws IllegalArgumentException if trailOff is longer than sizeLimit.
	 */
	public String getTitle(int sizeLimit, String trailOff) {
		///The raw string version, which holds the original title.
		String rawString = getTitle();
		
		//This probably won't happen, but if it does there will be issues.
		if (sizeLimit < trailOff.length()) {
			SkyblockExtension.inst().getLogger().severe("Size limit was");
			throw new IllegalArgumentException("sizeLimit (" + sizeLimit + 
					")is too small for trailOff (" + trailOff.length() + "to fit!");
		}
		
		if (rawString.length() <= sizeLimit) {
			return rawString;
		} else {
			return rawString.substring(0, sizeLimit - trailOff.length()) + trailOff;
		}
	}
	
	/**
	 * Gets the title of the CrashReport.
	 * @return The title.
	 */
	public String getTitle() {
		return thrown.toString();
	}
}
