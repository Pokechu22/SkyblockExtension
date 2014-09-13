package pokechu22.plugins.SkyblockExtension.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;

/**
 * A player-based logger.
 *
 * @author Pokechu22
 * @deprecated Not yet implemented.
 *
 */
public class PlayerLogger extends Logger {
	private CommandSender player;
	
	public PlayerLogger(CommandSender player) {
		this(player, null, null);
	}
	
	public PlayerLogger(CommandSender player, String name, String resourceBundleName) {
		super(name, resourceBundleName);
		
		this.player = player;
	}

	@Override
	public void log(Level level, String message) {
		String prefix = "";
		
		switch (level.getName()) {
		case "SEVERE": {
			prefix = "§c";
			break;
		}
		case "WARNING": {
			prefix = "§e";
			break;
		}
		case "INFO": {
			prefix = "§9";
			break;
		}
		case "CONFIG": {
			prefix = "§f";
			break;
		}
		case "FINE": {
			prefix = "§7";
			break;
		}
		case "FINER": {
			prefix = "§8";
			break;
		}
		case "FINEST": {
			prefix = "§0";
			break;
		}
		}
		
		player.sendMessage(prefix + "[" + level.getName() + "]" + message);
	}
}
