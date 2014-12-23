package pokechu22.plugins.SkyblockExtension.testutil;

import java.util.ArrayDeque;
import java.util.Queue;

import org.bukkit.entity.Player;

/**
 * A player that stores every message that it receives in a list.
 * 
 * @author Pokechu22
 *
 */
public abstract class ChatLoggedPlayer implements Player {
	/**
	 * Provides the messages
	 */
	public Queue<String> messageQueue = new ArrayDeque<>();
	
	public void sendMessage(String message) {
		messageQueue.add(message);
	}
}
