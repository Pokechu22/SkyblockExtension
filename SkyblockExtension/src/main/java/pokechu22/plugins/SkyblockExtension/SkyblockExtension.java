package pokechu22.plugins.SkyblockExtension;

import java.util.logging.Level;

import pokechu22.plugins.SkyblockExtension.commands.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author Pokechu22
 *
 */
public class SkyblockExtension extends JavaPlugin {
	
	/**
	 * The instance of this plugin.
	 * 
	 * Private so that (at least I asume; this is from the bukkit tutorial) it is not overwritable.
	 */
	private static SkyblockExtension inst;
	
	/**
	 * Gets the instance of this plugin.
	 * @return The instance of this plugin.
	 */
	public static SkyblockExtension inst() {
		return inst;
	}
	
	/**
	 * Called when the plugin is enabled.
	 */
	@Override
	public void onEnable() {
		//In 
		ConfigurationSerialization.registerClass(CrashReport.class, "CrashReport");
		
		this.saveDefaultConfig();
		
		inst = this;
		Config.loadConfig();
	}
 
	/**
	 * Called when the plugin is disabled.
	 */
	@Override
	public void onDisable() {
		Config.saveConfig();
	}

	
	/**
	 * Called when a command is run.
	 * 
	 * @param sender The person who sent the command.
	 * @param cmd The command.
	 * @param lable The given name of the command (EG "example" for "/example")
	 * @param args The arguments provided to the command.
	 * @return True if the command syntax was correct, 
	 *     false if you want the message in plugin.yml to be displayed.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		try {
			//TODO add the actual commands.
			switch (cmd.getName().toLowerCase()) {
			case "pokechu22": { // "/pokechu22" command
				
				CommandPokechu22.Run(sender, cmd, label, args);
				
				break;
			}
			default: {
				//Tell player.
				sender.sendMessage("§4Unrecognised command: " + cmd.getName() + "(Label: " + label + ")");
				
				//Log to console.
				getLogger().warning("§4Unrecognised command: " + cmd.getName() + "(Label: " + label + ")");
				
				//TODO: Log to player.	
				break;
			}
			}
			
		} catch (Throwable e) {
			
			//Tell the player that an error occurred.
			sender.sendMessage("§4A unhandled error occoured while preforming this command.");
			sender.sendMessage("§4" + e.toString());
			
			//Put the error message in the console / log file.
			getLogger().severe("A error occoured:");
			getLogger().log(Level.SEVERE, "Exception:", e);
			getLogger().severe("Context: ");
			getLogger().severe("    Command name: " + cmd.getName() + "(Label: " + label + ")");
			getLogger().severe("    Arguments: ");
			for (int i = 0; i < args.length; i++) {
				//For each of the values output it with a number next to it.
				getLogger().severe("        " + i + ": " + args[i]);
			}
			
			//Log the error for command access.
			ErrorHandler.logError(e, sender, cmd, label, args);
			
			//Errors are typicaly things that shouldn't be caught (EG ThreadDeath), so they will
			//be rethrown.
			if (e instanceof Error) {
				getLogger().severe("Rethrowing Error...");
				sender.sendMessage("§4Rethrowing, as it extends error.");
				throw e;
			}
			
		}
		
		return true; 
	}
}
