package pokechu22.plugins.SkyblockExtension.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import pokechu22.plugins.SkyblockExtension.PermissionHandler;
import pokechu22.plugins.SkyblockExtension.SkyblockExtension;
import pokechu22.plugins.SkyblockExtension.errorhandling.ConfigurationErrorReport;
import pokechu22.plugins.SkyblockExtension.errorhandling.ErrorHandler;
import pokechu22.plugins.SkyblockExtension.errorhandling.ThrowableReport;
import us.talabrek.ultimateskyblock.PlayerInfo;
import us.talabrek.ultimateskyblock.Settings;
import us.talabrek.ultimateskyblock.uSkyBlock;

//Static imports - Imports a function, not a class.
import static pokechu22.plugins.SkyblockExtension.util.IslandUtils.getPlayerInfo;

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
public class CommandMultiChallenge implements CommandExecutor, TabCompleter {
	
	/**
	 * Challenge names.
	 */
	private List<String> challengeNames = null;
	/**
	 * Has this command been initiated?
	 */
	private boolean initiated = false;
	
	/**
	 * Initiates this.
	 */
	protected void initiate() {
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
	public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String args[]) 
	{
		try {
			if (!initiated) {
				initiate();
			}
			
			if (!(sender instanceof Player)) {
				return null;
				//This probably won't happen.
			}
			
			Player player = (Player) sender;
			
			if (args.length == 2) {
				List<String> availableChallenges = getAvailableChallenges(player);
				ArrayList<String> returned = new ArrayList<String>();
		
				String find = args[1].toLowerCase();
				
				for (String name : availableChallenges) {
					if (name.startsWith(find))
						returned.add(name);
				}
				
				return returned;
			}
		} catch (Throwable e) {
			// Tell the player that an error occurred.
			sender.sendMessage("�4An unhandled error occoured while tab completing.");
			sender.sendMessage("�4" + e.toString());

			// Put the error message in the console / log file.
			getLogger().severe("An error occoured:");
			getLogger().log(Level.SEVERE, "Exception:", e);
			getLogger().severe("Context: ");
			getLogger().severe(
					"    Command name: " + cmd.getName() + "(Label: " + commandLabel
							+ ")");
			getLogger().severe("    Arguments: ");
			for (int i = 0; i < args.length; i++) {
				// For each of the values output it with a number next to it.
				getLogger().severe("        " + i + ": " + args[i]);
			}

			// Log the error for command access.
			ErrorHandler.logError(new ThrowableReport(e, sender, cmd, commandLabel,
					args, "Executing onTabComplete."));

			// Errors are typically things that shouldn't be caught (EG
			// ThreadDeath), so they will be rethrown.
			if (e instanceof Error) {
				getLogger().severe("Rethrowing Error...");
				sender.sendMessage("�4Rethrowing, as it extends error.");
				throw e;
			}
		}
		
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
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {
		try {
		if (!(sender instanceof Player)) {
			sender.sendMessage("�cOnly players are allowed to complete challenges!");
			return true;
		}
		
		Player player = (Player)sender;
		
		if (!Settings.challenges_allowChallenges)
		{
			sender.sendMessage("�cChallenges are not enabled.");
			return true;
		}
		
		if (!PermissionHandler.HasPermission(sender, "sbe.challenges.multicomplete")) {
			return true;
		}
		
		if (!uSkyBlock.getSkyBlockWorld().equals(player.getWorld())) //If not in skyblock world
		{
			sender.sendMessage("�cYou can only submit challenges in the skyblock world!");
			return true;
		}
		
		if (args.length == 0) {
			sendHelp(sender, commandLabel);
			return true;
		}
		
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
				sendHelp(sender, commandLabel);
				return true;
			}
			sender.sendMessage("�cERROR: Too few parameters.");
			sender.sendMessage("For syntax, do /" + commandLabel + " help");
			return true;
		}
		
		if (args.length == 2) {
			int repititions;
			String challengeName;
			
			try {
				repititions = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				sender.sendMessage("�cFailed to parse repititions (Got " + args[0] + 
						", expected Integer).");
				sender.sendMessage("For usage, do /" + commandLabel + " help");
				return true;
			}
			
			if (repititions < 0) {
				sender.sendMessage("�cError: Repititions must be positive.  " + 
						"(Got " + args[0] + ")");
				
				return true;
			}
			
			if (repititions == 0) {
				//It would be pointless if this were allowed.
				sender.sendMessage("�cError: Repititions must not be 0.  ");
				return true;
			}
			
			challengeName = args[1];
			if (!challengeExists(player, challengeName)) {
				sender.sendMessage("�cChallenge " + challengeName + " does not exist!");
				return true;
			}
			if (!challengeUnlocked(player, challengeName)) {
				sender.sendMessage("�cChallenge " + challengeName + " has not been unlocked!");
				return true;
			}
			if (!isChallengeRepeatable(challengeName)) {
				sender.sendMessage("�cChallenge " + challengeName + " is not repeatable!");
				return true;
			}
			if (!isChallengeAvailable(player, challengeName)) {
				sender.sendMessage("�cYou have not unlocked " + challengeName + ".");
				sender.sendMessage("�cTo be able to use a challenge with /" + commandLabel + 
						", you must first complete it with the regular /c command.");
				return true;
			}
			
			if (!canCompleteChallenge(player, challengeName, repititions)) {
				sender.sendMessage("�cYou don't have the items needed to complete " + 
						challengeName + " " + repititions + " time" + 
						(repititions == 1 ? "" : "s") + "!");
				return true;
			}
			
			for (int i = 0; i < repititions; i++) {
				///Result of taking items.
				boolean result;
				//Takes the items required.
				result = uSkyBlock.getInstance().takeRequired(player, challengeName, "onPlayer");
				if (!result) {
					throw new RuntimeException("Player does not have all needed items " + 
							"despite canCompleteChallenge returning true!");
				}
				
				//Give the reward.
				uSkyBlock.getInstance().giveReward(player, challengeName);
			}
			
			return true;
		}
		
		if (args.length >= 3) {
			sender.sendMessage("�cERROR: Too many parameters.");
			sender.sendMessage("For syntax, do /" + commandLabel + " help");
		}
		} catch (Throwable e) {
			// Tell the player that an error occurred.
			sender.sendMessage("�4An unhandled error occoured while preforming this command.");
			sender.sendMessage("�4" + e.toString());

			// Put the error message in the console / log file.
			getLogger().severe("An error occoured:");
			getLogger().log(Level.SEVERE, "Exception:", e);
			getLogger().severe("Context: ");
			getLogger().severe(
					"    Command name: " + cmd.getName() + "(Label: " + commandLabel
							+ ")");
			getLogger().severe("    Arguments: ");
			for (int i = 0; i < args.length; i++) {
				// For each of the values output it with a number next to it.
				getLogger().severe("        " + i + ": " + args[i]);
			}

			// Log the error for command access.
			ErrorHandler.logError(new ThrowableReport(e, sender, cmd, commandLabel,
					args, "Executing onCommand."));

			// Errors are typically things that shouldn't be caught (EG
			// ThreadDeath), so they will be rethrown.
			if (e instanceof Error) {
				getLogger().severe("Rethrowing Error...");
				sender.sendMessage("�4Rethrowing, as it extends error.");
				throw e;
			}
		}
		return true;
	}
	
	protected static void sendHelp(CommandSender sender, String label) {
		sender.sendMessage("/" + label + " help:");
		sender.sendMessage(
				"This command provides completion of any challenge multiple times in quick " + 
				"succession, instead of spaming the /c command.");
		sender.sendMessage("Syntax: /" + label + " <repititions> <challengeName>");
		sender.sendMessage("Note that you �lMUST�r complete the challenge once using the " + 
				"regular challenges command (/c) first.");
	}
	
	/**
	 * Checks if a challenge exists.
	 * 
	 * @param player
	 * @param challengeName
	 * @return
	 */
	protected static boolean challengeExists(Player player, String challengeName) {
		return getPlayerInfo(player).challengeExists(challengeName);
	}
	
	/**
	 * Checks if a challenge has had its rank unlocked.
	 * 
	 * @param player
	 * @param challengeName
	 * @return
	 */
	protected static boolean challengeUnlocked(Player player, String challengeName) {
		return (uSkyBlock.getInstance().isRankAvailable(
				player, uSkyBlock.getInstance().getConfig()
				.getString("options.challenges.challengeList." 
				+ challengeName + ".rankLevel")));
	}
	
	/**
	 * Checks if the specified challenge is available for use via this command.  
	 * 
	 * Please ensure that {@linkplain #canGetPlayerInfo(Player)} returns true
	 * beforehand.  
	 * 
	 * @param player
	 * @param challengeName
	 * @return
	 */
	protected static boolean isChallengeAvailable(Player player, String challengeName) {
		PlayerInfo p = getPlayerInfo(player);
		
		//Player has completed the challenge once & challenge is repeatable.
		return (p.checkChallenge(challengeName) && isChallengeRepeatable(challengeName));
	}
	
	/**
	 * Checks if a challenge is repeatable.  It does this by reading the config.
	 * This also returns false if it isn't a challenge that takes items.
	 * 
	 * @author talabrek
	 * 
	 * @param challengeName
	 * @return
	 */
	protected static boolean isChallengeRepeatable(String challengeName) {
		//Will need to change getConfig() with getChallengeConfig() in the future
		return (("onPlayer".equalsIgnoreCase(uSkyBlock
				.getInstance()
				.getConfig()
				.getString(
						"options.challenges.challengeList."
								+ challengeName.toLowerCase() + ".type")) && uSkyBlock
				.getInstance()
				.getConfig()
				.getBoolean(
						"options.challenges.challengeList." + challengeName
								+ ".repeatable")));
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
	protected List<String> getAvailableChallenges(Player sender) {
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
	 * Can a player complete a challenge the required number of times?
	 * 
	 * Assumes challenge requires items.  Also uses deprecated constructors, 
	 * because numeric ids.  Yay.
	 * 
	 * Based off of {@link uSkyBlock#hasRequired(Player, String, String)}.
	 * 
	 * @param player
	 * @param challengeName
	 * @param times
	 * @return
	 */
	@SuppressWarnings("deprecation")
	protected boolean canCompleteChallenge(Player player, String challengeName, int times) {
		//Will look something like "1:4 44:1:64", where the first number is the numeric ID,
		//and the last one is the quantity.  If it exists, the middle number is the data value.
		String[] requiredItemsList = uSkyBlock
				.getInstance()
				.getConfig()
				.getString(
						"options.challenges.challengeList." + challengeName
								+ ".requiredItems").split(" ");
		int requiredItemID = 0;
		int requiredAmount = 0;
		int requiredDataValue = -1;
		
		for (int i = 0; i < requiredItemsList.length; i++) {
			String[] requiredItemData = requiredItemsList[i].split(":");
			if (requiredItemData.length == 2) {
				try {
					requiredItemID = Integer.parseInt(requiredItemData[0]);
				} catch (NumberFormatException e) {
					player.sendMessage(
							"�4Internal error: " + 
							"Failed to parse uSkyBlock challenges configuration.");
					
					ErrorHandler.logError(new ConfigurationErrorReport(
							e,
							"options.challenges.challengeList." + challengeName	+ ".requiredItems",
							"uSkyBlock challenges config", false)
					.setContext("Failed to parse int for requiredItemID.\n" + 
							"Expected integer, got " + requiredItemData[0] + ".\n" + 
							"Parsing: " + requiredItemsList[i] + ", index 0.\n" + 
							"Index is " + i + ".")
							);
					
					SkyblockExtension.inst().getLogger().warning(
							"Failed to parse uSkyBlock challenges configuration.");
					return false;
				}
				try {
					requiredAmount = Integer.parseInt(requiredItemData[1]);
				} catch (NumberFormatException e) {
					player.sendMessage(
							"�4Internal error: " + 
							"Failed to parse uSkyBlock challenges configuration.");
					
					ErrorHandler.logError(new ConfigurationErrorReport(
							e,
							"options.challenges.challengeList." + challengeName	+ ".requiredItems",
							"uSkyBlock challenges config", false)
					.setContext("Failed to parse int for requiredAmount.\n" + 
							"Expected integer, got " + requiredItemData[1] + ".\n" + 
							"Parsing: " + requiredItemsList[i] + ", index 1.\n" + 
							"Index is " + i + ".")
							);
					
					SkyblockExtension.inst().getLogger().warning(
							"Failed to parse uSkyBlock challenges configuration.");
					
					return false;
				}
				
				if (!player.getInventory().contains(requiredItemID, requiredAmount * times)) {
					return false;
				}
				
			} else if (requiredItemData.length == 3) {
				try {
					requiredItemID = Integer.parseInt(requiredItemData[0]);
				} catch (NumberFormatException e) {
					player.sendMessage(
							"�4Internal error: " + 
							"Failed to parse uSkyBlock challenges configuration.");
					
					ErrorHandler.logError(new ConfigurationErrorReport(
							e,
							"options.challenges.challengeList." + challengeName	+ ".requiredItems",
							"uSkyBlock challenges config", false)
					.setContext("Failed to parse int for requiredItemID.\n" + 
							"Expected integer, got " + requiredItemData[0] + ".\n" + 
							"Parsing: " + requiredItemsList[i] + ", index 0.\n" + 
							"Index is " + i + ".")
							);
					
					SkyblockExtension.inst().getLogger().warning(
							"Failed to parse uSkyBlock challenges configuration.");
					return false;
				}
				try {
					requiredAmount = Integer.parseInt(requiredItemData[2]);
				} catch (NumberFormatException e) {
					player.sendMessage(
							"�4Internal error: " + 
							"Failed to parse uSkyBlock challenges configuration.");
					
					ErrorHandler.logError(new ConfigurationErrorReport(
							e,
							"options.challenges.challengeList." + challengeName	+ ".requiredItems",
							"uSkyBlock challenges config", false)
					.setContext("Failed to parse int for requiredAmount.\n" + 
							"Expected integer, got " + requiredItemData[2] + ".\n" + 
							"Parsing: " + requiredItemsList[i] + ", index 2.\n" + 
							"Index is " + i + ".")
							);
					
					SkyblockExtension.inst().getLogger().warning(
							"Failed to parse uSkyBlock challenges configuration.");
					return false;
				}
				try {
					requiredDataValue = Integer.parseInt(requiredItemData[1]);
				} catch (NumberFormatException e) {
					player.sendMessage(
							"�4Internal error: " + 
							"Failed to parse uSkyBlock challenges configuration.");
					
					ErrorHandler.logError(new ConfigurationErrorReport(
							e,
							"options.challenges.challengeList." + challengeName	+ ".requiredItems",
							"uSkyBlock challenges config", false)
					.setContext("Failed to parse int for requiredDataValue.\n" + 
							"Expected integer, got " + requiredItemData[1] + ".\n" + 
							"Parsing: " + requiredItemsList[i] + ", index 1.\n" + 
							"Index is " + i + ".")
							);
					
					SkyblockExtension.inst().getLogger().warning(
							"Failed to parse uSkyBlock challenges configuration.");
					return false;
				}
				if (!player.getInventory().containsAtLeast(
						new ItemStack(requiredItemID, requiredAmount,
								(short) requiredDataValue),
						requiredAmount * times)) {
					return false;
				}
			} else {
				//Error state.
				player.sendMessage(
						"�4Internal error: Failed to parse uSkyBlock challenges configuration.");
				
				ErrorHandler.logError(new ConfigurationErrorReport(
						"options.challenges.challengeList." + challengeName	+ ".requiredItems",
						"uSkyBlock challenges config", false)
				.setContext("requiredItemData.length was not valid.\n" + 
						"Expected either 2 or 3, got " + requiredItemData.length + ".\n" + 
						"Value is: " + requiredItemsList[i] + "\n" + 
						"Index is " + i + ".")
						);
				
				SkyblockExtension.inst().getLogger().warning(
						"Failed to parse uSkyBlock challenges configuration.");
				return false;
			}
		}
		
		return true;
	}
	
	private Logger getLogger() {
		return SkyblockExtension.inst().getLogger();
	}
}
