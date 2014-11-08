package pokechu22.plugins.SkyblockExtension.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pokechu22.plugins.SkyblockExtension.PermissionHandler;
import pokechu22.plugins.SkyblockExtension.protection.IslandInfo;
import pokechu22.plugins.SkyblockExtension.util.IslandUtils;
import us.talabrek.ultimateskyblock.PlayerInfo;
import us.talabrek.ultimateskyblock.uSkyBlock;

/**
 * Command that removes unauthorized players from one's island.
 * 
 * Syntax: /expunge &lt;player&gt;.
 * 
 * Can only be used on nonmembers and guests.
 * 
 * @author Pokechu22
 *
 */
public class CommandExpunge {
	public static boolean membersCanExpunge = false;
	
	/**
	 * Tab-completion.  
	 * 
	 * @param sender
	 * @param cmd
	 * @param label
	 * @param args
	 * @return
	 */
	public static List<String> onTabComplete(CommandSender sender, Command cmd, String label, String args[]) {
		return null;
	}
	
	/**
	 * Runs the command.
	 * 
	 * @param senderC
	 * @param cmd
	 * @param label
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void Run(CommandSender senderC, Command cmd, String label, String args[]) {
		final Player sender;
		final Player sent;
		
		final PlayerInfo senderInfo;
		final PlayerInfo sentInfo;
		
		final Location sentIslandLoc;
		final Location senderIslandLoc;
		
		//All of the sender stuff.
		
		if (!(senderC instanceof Player)) {
			senderC.sendMessage("�cThis command can only be used by Players as it performs checks on both player's islands.");
			senderC.sendMessage("�cTry using �f/sudo <player> is�c instead.");
			return;
		}
		
		sender = (Player) senderC;
		senderInfo = IslandUtils.getPlayerInfo(sender);
		
		//Validate that the sender has permission.
		if (!PermissionHandler.HasPermision(sender, "sbe.island.expunge")) {
			sender.sendMessage("�cYou don't have permission to use this command!");
			return;
		}
		
		//Validate that the sender actually has an island
		if (senderInfo == null) {
			sender.sendMessage("�cYou cannot remove people from an island that you don't have!");
			return;
		}
		
		//Validate that the sender owns their island, if only the owner can perform this command.
		if (!membersCanExpunge) {
			if (!senderInfo.getPartyLeader().equalsIgnoreCase(sender.getName())) {
				sender.sendMessage("�cOnly the owner can remove people from their island!");
				return;
			}
		}
		
		//Provide help if syntax is invalid.
		if (args.length != 1) {
			sender.sendMessage("�cUsage: �4/" + label + " <player>�c.");
			sender.sendMessage("�fThis command moves the specified player back to their island from your own island.");
			sender.sendMessage("�fIt can only be used if the other player is A) currently standing within your island's bounderies, and B) is not a member of your island.  It also can only be used by the island owner.");
			return;
		}
		
		//Now validate the sent player.
		
		sent = Bukkit.getPlayer(args[0]);
		
		//Validate that the sent player is online.
		if (sent == null) {
			sender.sendMessage("�cPlayer " + args[0] + " is offline.");
			return;
		}
		
		//Validate that the sent player is on the other player's island.
		if (!(IslandUtils.getOccupyingIsland(sent.getLocation())
				.equals(senderInfo.getIslandLocation()) || 
				IslandUtils.getOccupyingIsland(sent.getLocation())
				.equals(senderInfo.getPartyIslandLocation()))) {
			sender.sendMessage("�cPlayer " + args[0] + " is not currently on your island.");
			return;
		}
		
		sender.sendMessage("�aSending " + args[0] + " to their island.");
		uSkyBlock.getInstance().homeTeleport(sent);
	}
}