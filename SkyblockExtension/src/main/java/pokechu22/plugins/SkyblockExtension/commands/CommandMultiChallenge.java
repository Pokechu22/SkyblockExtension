package pokechu22.plugins.SkyblockExtension.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pokechu22.plugins.SkyblockExtension.PermissionHandler;
import us.talabrek.ultimateskyblock.PlayerInfo;
import us.talabrek.ultimateskyblock.Settings;
import us.talabrek.ultimateskyblock.uSkyBlock;

/**
 * Hijacks the existing functionality of uSkyblock to allow players to quickly
 * complete a challenge multiple times.
 * <br>
 * Based off of the source code that I got 
 * <a href=https://github.com/wolfwork/uSkyBlock>here</a>.
 * 
 * TODO: This WILL break if the plugin uses UUIDPlayerInfo rather than PlayerInfo.  
 * 
 * @author Pokechu22
 * @author Talabrek
 * @author wolfwork
 *
 */
public class CommandMultiChallenge {
	
	private static ArrayList<String> challengeNames = null;
	private static boolean initiated = false;
	
	/**
	 * Initiates this.
	 */
	protected static void initiate() {
		initiated = true;
		challengeNames = new ArrayList<String>(Settings.challenges_challengeList.size());
		challengeNames.addAll(Settings.challenges_challengeList);
	}
	
	/**
	 * Tab-completion.  (For challenge names)
	 * 
	 * @author wolfwork
	 * @param sender
	 * @param cmd
	 * @param commandLabel
	 * @param args
	 * @return
	 */
	public static List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String args[]) 
	{
		if (!initiated) {
			initiate();
		}
		
		if (!(sender instanceof Player)) {
			return null;
			//This probably won't happen.
		}
		
		Player player = (Player) sender;
		
		if (!canGetPlayerInfo(player)) {
			//If player info is unavailable, return nothing.
			return new ArrayList<String>();
		}
		
		if (args.length == 1) {
			List<String> availableChallenges = getAvailableChallenges(player);
			ArrayList<String> returned = new ArrayList<String>();
	
			String find = args[0].toLowerCase();
			
			for (String name : availableChallenges) {
				if (name.startsWith(find))
					returned.add(name);
			}
			
			return returned;
		}

		//Basically, return nothing, rather than null which gives all online players.
		return new ArrayList<String>();
	}
	
	/**
	 * Actually runs.
	 * 
	 * @param sender
	 * @param cmd
	 * @param commandLabel
	 * @param args
	 */
	public static void Run(CommandSender sender, Command cmd, String commandLabel, String args[]) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cOnly players are allowed to complete challenges!");
			return;
		}
		
		Player player = (Player)sender;
		
		if (!Settings.challenges_allowChallenges)
		{
			sender.sendMessage("§cChallenges are not enabled.");
			return;
		}
		
		if (!PermissionHandler.HasPermision(sender, "sbe.challenges.multicomplete")) {
			return;
		}
		
		if (!uSkyBlock.getSkyBlockWorld().equals(player.getWorld())) //If not in skyblock world
		{
			sender.sendMessage("§cYou can only submit challenges in the skyblock world!");
			return;
		}
		
		if (!canGetPlayerInfo(player)) {
			sender.sendMessage("§4Internal error: You are not in the list of active players!");
			sender.sendMessage("§4You cannot use this command.  You probably don't even exist.");
			sender.sendMessage("§4Try completing a challenge with /c and then trying again.");
			return;
		}
		
		PlayerInfo playerInfo = getPlayerInfo(player);
		
		if (args.length == 0) {
			sendHelp(sender, commandLabel);
			return;
		}
		
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
				sendHelp(sender, commandLabel);
				return;
			}
			sender.sendMessage("§cERROR: Too few parameters.");
			sender.sendMessage("For syntax, do /" + commandLabel + "help");
			return;
		}
		
		if (args.length == 2) {
			
			return;
		}
		
		if (args.length >= 3) {
			sender.sendMessage("§cERROR: Too many parameters.");
			sender.sendMessage("For syntax, do /" + commandLabel + "help");
			return;
		}
	}
	
	protected static void sendHelp(CommandSender sender, String label) {
		sender.sendMessage("/" + label + " help:");
		sender.sendMessage(
				"This command provides completion of any challenge multiple times in quick " + 
				"succession, instead of spaming the /c command.");
		sender.sendMessage("Syntax: /" + label + " <repititions> <challengeName>");
		sender.sendMessage("Note that you §lMUST§r complete the challenge once using the " + 
				"regular challenges command (/c) first.");
	}
	
	/**
	 * Gets challenges available to the player for use via this command.
	 * 
	 * More specifically, returns a List<String> of challenges that the specified
	 * player has completed, assuming the information 
	 * {@linkplain #canGetPlayerInfo(Player) is available}.  
	 * 
	 * @param sender
	 * @param label
	 * @return
	 */
	public static List<String> getAvailableChallenges(Player sender) {
		ArrayList<String> availableChallenges = new ArrayList<String>();
		
		PlayerInfo p = getPlayerInfo(sender);
		
		for (String challengeName : challengeNames) {
			//checkChallenge sees if a player has completed a challenge.
			if (p.checkChallenge(challengeName)) {
				availableChallenges.add(challengeName);
			}
		}
		
		return availableChallenges;
	}
	
	/**
	 * Tests if the player is active, and thus player info can be obtained via
	 * {@linkplain #getPlayerInfo(Player)}.
	 * 
	 * @param p
	 * @return
	 */
	protected static boolean canGetPlayerInfo(Player p) {
		return uSkyBlock.getInstance().getActivePlayers().containsKey(p.getName());
	}
	
	/**
	 * Gets the {@linkplain PlayerInfo} for a player.
	 * 
	 * @param p
	 * @return The player info, or null if there is no info available.
	 */
	protected static PlayerInfo getPlayerInfo(Player p) {
		return uSkyBlock.getInstance().getActivePlayers().get(p.getName());
	}
}
