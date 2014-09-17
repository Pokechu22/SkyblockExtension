package pokechu22.plugins.SkyblockExtension.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.bukkit.command.CommandSender;

/**
 * For methods that need to be printed to a player, as a convenience.
 * NOTE: This allows all command senders, not just players.
 * 
 * @author Pokechu22
 *
 */
public class PlayerPrintStream extends PrintStream {
	/**
	 * For methods that need to be printed to a player, as a convenience.
	 * And this is needed because {@link PrintStream} needs an {@link OutputStream}.
	 * @author Pokechu22
	 *
	 */
	public static class PlayerOutputStream extends OutputStream {
		/**
		 * The player to send information to.
		 */
		private CommandSender player;
		
		/**
		 * A string to prepend the text, intended for coloration.
		 */
		private String prepend = "";
		
		/**
		 * The message to send.
		 */
		StringBuilder cachedMessage = new StringBuilder();
		
		public PlayerOutputStream(CommandSender player) {
			this.player = player;
		}
		
		public PlayerOutputStream(CommandSender player, String prepend) {
			this.player = player;
			this.prepend = prepend;
		}
		
		/**
		 * Appends some text.  
		 * Obviously assumes that it is being given a char.
		 */
		@Override
		public void write(int codePoint) throws IOException {
			//Ugly, but I don't know the right way...
			if (codePoint == (int) '\r') {
				//Minecraft doesn't do \r.
				return;
			}
			if (codePoint == (int) '\n') {
				this.flush();
				return;
			}
			cachedMessage.appendCodePoint(codePoint);
		}
		
		@Override
		public void flush() throws IOException {
			player.sendMessage(prepend + cachedMessage.toString());
			//Clear the output.
			cachedMessage = new StringBuilder();
		}
	}
	
	public PlayerPrintStream(CommandSender sender) {
		super(new PlayerOutputStream(sender), false);
	}
	
	public PlayerPrintStream(CommandSender player, String prepend) {
		super(new PlayerOutputStream(player, prepend), false);
	}
}
