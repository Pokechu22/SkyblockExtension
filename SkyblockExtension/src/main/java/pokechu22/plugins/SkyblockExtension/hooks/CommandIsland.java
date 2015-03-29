package pokechu22.plugins.SkyblockExtension.hooks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import pokechu22.plugins.SkyblockExtension.PermissionHandler;
import pokechu22.plugins.SkyblockExtension.errorhandling.ErrorHandler;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.commands.IslandCmd;

/**
 * Intended to do the same thing as IslandCommand, but override some 
 * functionality.  It is intended to replace the vanilla command.
 *
 * @author Pokechu22
 *
 */
public class CommandIsland extends IslandCmd implements TabCompleter {
	private ASkyBlockAPI aSkyBlock;
	
	public CommandIsland() {
		super (ASkyBlock.getPlugin());
		
		this.aSkyBlock = ASkyBlockAPI.getInstance();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, 
			String label, String[] args) {
		try {
			if (args.length > 0 && args[0].equalsIgnoreCase("levle")) {
				//Correction for a stupid typo I make way too much.
				sender.sendMessage("§eLevel is not spelled levle.");
				args[0] = "level";
			}
			
			if (args.length > 0 && (args[0].equalsIgnoreCase("sendhome")
					|| args[0].equalsIgnoreCase("sendback")
					|| args[0].equalsIgnoreCase("dismiss")
					|| args[0].equalsIgnoreCase("awaywithyou"))) {
				if (args.length > 1) {
					sendHome(args[1], sender, label + " " + args[0], true, 
							Arrays.copyOfRange(args, 1, args.length));
				} else {
					sender.sendMessage("§cInvalid format.  Syntax: /" + label + 
							" " + args[0] + " <player> [message]");
				}
				return true;
			}
			if (args.length > 0 && args[0].equalsIgnoreCase("shiphome")) {
				if (args.length == 2) {
					if (sender instanceof Player) {
						String playerName = ((Player)sender).getDisplayName();
						sendHome(args[1], sender, label + " " + args[0], true,
								"Woosh!  " + playerName + " has boxed you up and shipped you back to your island.");
					}
				} else {
					sender.sendMessage("§cInvalid format.  Syntax: /" + label + 
							" " + args[0] + " <player>");
				}
				return true;
			}
			if (args.length > 0 && args[0].equalsIgnoreCase("fusrodah")) {
				if (args.length == 2) {
					sendHome(args[1], sender, label + " " + args[0], false,
							"§lFUS RO DAH!");
				} else {
					sender.sendMessage("§cInvalid format.  Syntax: /" + label + 
							" " + args[0] + " <player>");
				}
				return true;
			}
			if (args.length > 0 && args[0].equalsIgnoreCase("sayonarasucker")) {
				if (args.length == 2) {
					sendHome(args[1], sender, label + " " + args[0], false,
							"§oSayonara, sucker!");
				} else {
					sender.sendMessage("§cInvalid format.  Syntax: /" + label + 
							" " + args[0] + " <player>");
				}
				return true;
			}
			
			boolean superResult = super.onCommand(sender, command, label, args);
			
			if (superResult == false) {
				sender.sendMessage("Syntax error; see /island help for usage.");
			}
		} catch (Throwable e) {
			ErrorHandler.logExceptionOnCommand(sender, command, label, args, e);
		}
		return true;
	}
	
	/**
	 * Whether or not /spawn should be used to take the player to spawn.
	 * If disabled, sends to {@link uSkyBlock#getSkyBlockWorld()}'s spawn
	 * point.
	 */
	public static boolean useSpawnCommandForSpawn = true;
	/**
	 * Controls whether members of an island have access to sendhome.
	 */
	public static boolean membersCanSendHome = true;
	
	/**
	 * 
	 * @param args
	 * @param senderC
	 * @param label
	 * @param message
	 */
	public void sendHome(String player, CommandSender senderC, String label, 
			boolean includeSender, String... messageWords) {
		StringBuilder message = new StringBuilder();
		for (int i = 0; i < messageWords.length; i++) {
			message.append(messageWords[i]);
			if (i != messageWords.length - 1) {
				message.append(" ");
			}
		}
		sendHome(player, senderC, label, includeSender, message.toString());
	}
	
	/**
	 * Sends a player back to their island.
	 * 
	 * @param args
	 * @param sender
	 * @param label The entire set of commands to here.
	 */
	@SuppressWarnings("deprecation")
	public void sendHome(String player, CommandSender senderC, String label, 
			boolean includeSender, String message) {
		final Player sender;
		final Player sent;
		
		//All of the sender stuff.
		if (!(senderC instanceof Player)) {
			senderC.sendMessage("§cThis command can only be used by Players as it performs checks on both player's islands.");
			senderC.sendMessage("§cTry using §f/sudo <player> is§c instead.");
			return;
		}
		
		sender = (Player) senderC;
		
		//Validate that the sender has permission.
		if (!PermissionHandler.HasPermission(sender, "sbe.island.sendhome")) {
			sender.sendMessage("§cYou don't have permission to use this command!");
			return;
		}
		//Validate that the sender actually has an island
		if (!aSkyBlock.hasIsland(sender.getUniqueId())) {
			sender.sendMessage("§cYou cannot remove people from an island that you don't have!");
			return;
		}
		
		//Validate that the sender owns their island, if only the owner can perform this command.
		if (!membersCanSendHome) {
			//I don't know if this is the best way to do this check, but
			//it seems to make sense.
			if (aSkyBlock.getOwner(aSkyBlock.getIslandLocation(
					sender.getUniqueId())).equals(sender.getUniqueId())) {
				sender.sendMessage("§cOnly the owner can remove people from their island!");
				return;
			}
		}
		
		//Now validate the sent player.
		sent = Bukkit.getPlayer(player);
		
		//Validate that the sent player is online.
		if (sent == null) {
			sender.sendMessage("§cPlayer " + player + " was not found.");
			return;
		}
		if (!sent.getName().equalsIgnoreCase(player)) {
			sender.sendMessage("§cPlayer " + player + " was not found.");
			sender.sendMessage("§cDid you mean " + sent.getName() + "?");
			return;
		}
		
		if (PermissionHandler.HasPermissionSilent(sent, "sbe.mod.nosendhome")) {
			sender.sendMessage(sent.getDisplayName() + "§c is a moderator and cannot be sent.");
			return;
		}
		
		if (!aSkyBlock.hasIsland(sent.getUniqueId()) && 
				!aSkyBlock.inTeam(sent.getUniqueId())) {
			//If that permission is enabled, ignore it.
			if (!sender.hasPermission("sbe.mod.sendhomeall")) {
				if (aSkyBlock.getTeamMembers(sender.getUniqueId()).contains(sent.getUniqueId())) {
					sender.sendMessage("§cCannot teleport " + sent.getDisplayName() + "§c as they are a member of your island.");
					return;
				}
				
				//Validate that the sent player is on the other player's island.
				if (aSkyBlock.isOnIsland(sender, sent)) {
					sender.sendMessage("§cPlayer " + player + "§c is not currently on your island.");
					return;
				}
			}
		} //If they have no island, we allow transport.
		
		sender.sendMessage("§aSending " + sent.getDisplayName() +
				" to their island.");
		
		sender.sendMessage("§aSending " + player + " to their island.");
		
		Location homeLocation = aSkyBlock.getHomeLocation(sent.getUniqueId());
		if (homeLocation != null) {
			sent.teleport(homeLocation);
		} else {
			if (useSpawnCommandForSpawn) {
				sent.performCommand("spawn");
			} else {
				sent.teleport(ASkyBlock.getIslandWorld().getSpawnLocation());
			}
		}
		
		//Send a message to the player to explain.
		if (includeSender) {
			sent.sendMessage(sender.getDisplayName() + 
					" has sent you back to your island.");
		}
		if (message != null && !message.equalsIgnoreCase("")) {
			sent.sendMessage(message);
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		//TODO Not yet implemented.
		return new ArrayList<String>();
	}
}
