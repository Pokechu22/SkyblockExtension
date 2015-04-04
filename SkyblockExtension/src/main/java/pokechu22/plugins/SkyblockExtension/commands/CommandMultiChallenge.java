package pokechu22.plugins.SkyblockExtension.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.PlayerCache;
import com.wasteofplastic.askyblock.commands.Challenges;

import pokechu22.plugins.SkyblockExtension.PermissionHandler;
import pokechu22.plugins.SkyblockExtension.SkyblockExtension;
import pokechu22.plugins.SkyblockExtension.errorhandling.ConfigurationErrorReport;
import pokechu22.plugins.SkyblockExtension.errorhandling.ErrorHandler;
import static pokechu22.plugins.SkyblockExtension.util.TabCompleteUtil.*;

/**
 * Allows a player to complete a challenge multiple times.  
 * <br>
 * Based off of the A-SkyBlock challenges code: 
 * https://github.com/tastybento/askyblock/blob/master/src/com/wasteofplastic/askyblock/commands/IslandCmd.java
 * 
 * @author Pokechu22
 * @author 
 */
public class CommandMultiChallenge implements CommandExecutor, TabCompleter {
	private ASkyBlockAPI aSkyBlock;
	private Challenges challenges;
	private PlayerCache players;
	
	public CommandMultiChallenge() {
		this.aSkyBlock = ASkyBlockAPI.getInstance();
		this.challenges = ASkyBlock.getPlugin().getChallenges();
		this.players = ASkyBlock.getPlugin().getPlayers();
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
			if (!(sender instanceof Player)) {
				return null;
				//This probably won't happen.
			}
			
			Player player = (Player) sender;
			
			if (args.length == 0) {
				return getAvailableChallenges(player);
			}
			if (args.length == 1) {
				List<String> availableChallenges = getAvailableChallenges(player);
				return TabLimit(availableChallenges, args[0]);
			}
		} catch (Throwable e) {
			ErrorHandler.logExceptionOnTabComplete(sender, cmd, commandLabel, args, e);
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
				sender.sendMessage("§cOnly players are allowed to complete challenges!");
				return true;
			}
			
			Player player = (Player)sender;
			
			if (!Settings.challenges_allowChallenges)
			{
				sender.sendMessage("§cChallenges are not enabled.");
				return true;
			}
			
			if (!PermissionHandler.HasPermission(sender, "sbe.commands.multichallenge")) {
				return true;
			}
			
			if (!ASkyBlock.getIslandWorld().equals(player.getWorld())) //If not in skyblock world
			{
				sender.sendMessage("§cYou can only submit challenges in the skyblock world!");
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
				sender.sendMessage("§cERROR: Too few parameters.");
				sender.sendMessage("For syntax, do /" + commandLabel + " help");
				return true;
			}
			
			if (args.length == 2) {
				int repititions;
				String challengeName;
				
				try {
					repititions = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					sender.sendMessage("§cFailed to parse repititions (Got " + args[1] + 
							", expected Integer).");
					sender.sendMessage("For usage, do /" + commandLabel + " help");
					return true;
				}
				
				if (repititions < 0) {
					sender.sendMessage("§cError: Repititions must be positive.  " + 
							"(Got " + args[1] + ")");
					
					return true;
				}
				
				if (repititions == 0) {
					//It would be pointless if this were allowed.
					sender.sendMessage("§cError: Repititions must not be 0.  ");
					return true;
				}
				
				challengeName = args[0];
				if (!challengeExists(player, challengeName)) {
					sender.sendMessage("§cChallenge " + challengeName + " does not exist!");
					return true;
				}
				if (!challengeUnlocked(player, challengeName)) {
					sender.sendMessage("§cChallenge " + challengeName + " has not been unlocked!");
					return true;
				}
				if (!isChallengeRepeatable(challengeName)) {
					sender.sendMessage("§cChallenge " + challengeName + " is not repeatable!");
					return true;
				}
				if (!isChallengeAvailable(player, challengeName)) {
					sender.sendMessage("§cYou have not unlocked " + challengeName + ".");
					sender.sendMessage("§cTo be able to use a challenge with /" + commandLabel + 
							", you must first complete it with the regular /c command.");
					return true;
				}
				
				if (!canCompleteChallenge(player, challengeName, repititions)) {
					sender.sendMessage("§cYou don't have the items needed to complete " + 
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
				sender.sendMessage("§cERROR: Too many parameters.");
				sender.sendMessage("For syntax, do /" + commandLabel + " help");
			}
		} catch (Throwable e) {
			ErrorHandler.logExceptionOnCommand(sender, cmd, commandLabel, args, e);
		}
		return true;
	}
	
	protected void sendHelp(CommandSender sender, String label) {
		sender.sendMessage("/" + label + " help:");
		sender.sendMessage(
				"This command provides completion of any challenge multiple times in quick " + 
				"succession, instead of spaming the /c command.");
		sender.sendMessage("Syntax: /" + label + " <challengeName> <repititions>");
		sender.sendMessage("Note that you §lMUST§r complete the challenge once using the " + 
				"regular challenges command (/c) first.");
	}
	
	/**
	 * Checks if a challenge exists.
	 * 
	 * @param player
	 * @param challengeName
	 * @return
	 */
	protected boolean challengeExists(Player player, String challengeName) {
		return players.challengeExists(playerName, challengeName);
	}
	
	/**
	 * Checks if a challenge has had its rank unlocked.
	 * 
	 * @param player
	 * @param challengeName
	 * @return
	 */
	protected boolean challengeUnlocked(Player player, String challengeName) {
		//ASkyBlock code.
		String level = Challenges.getChallengeConfig().getString("challenges.challengeList." + challengeName + ".level");
		// Only check if the challenge has a level, otherwise it's a free level
		if (!level.isEmpty()) {
		    if (!challenges.isLevelAvailable(player, level)) {
		    	return false;
		    }
		}
		return true;
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
	protected boolean isChallengeAvailable(Player player, String challengeName) {
		//Player has completed the challenge once & challenge is repeatable.
		return (players.checkChallenge(player.getUniqueId(), challengeName) && 
				isChallengeRepeatable(challengeName));
	}
	
	/**
	 * Checks if a challenge is repeatable.  It does this by reading the config.
	 * This also returns false if it isn't a challenge that takes items.
	 * 
	 * @param challengeName
	 * @return
	 */
	protected boolean isChallengeRepeatable(String challengeName) {
		if (!Challenges.getChallengeConfig().getBoolean(
				"challenges.challengeList." + challengeName + ".repeatable")) {
			return false;
		}
		if (!Challenges.getChallengeConfig().getString(
				"challenges.challengeList." + challengeName + ".type")
				.equalsIgnoreCase("inventory")) {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the player can perform the specified challenge the 
	 * specified number of times without hitting the maximum.
	 * @param player
	 * @param challenge
	 * @param times
	 * @return
	 */
	protected boolean canDoChallengeWithoutRunningOutOfTimes(Player player,
			String challenge, int times) {
		int currentTimes = players.checkChallengeTimes(player.getUniqueId(),
				challenge);
		int maxTimes = Challenges.getChallengeConfig().getInt(
				"challenges.challengeList." + challenge + ".maxtimes", 0);
	    if (maxTimes > 0) {
			if (currentTimes + times > maxTimes) {
				return false;
			}
	    }
		return true;
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
	protected List<String> getAvailableChallenges(Player player) {
		ArrayList<String> availableChallenges = new ArrayList<String>();
		
		Map<String, Boolean> challengeStatus = 
				players.getChallengeStatus(player.getUniqueId());
		
		for (Map.Entry<String, Boolean> e : challengeStatus.entrySet()) {
			if (e.getValue()) {
				availableChallenges.add(e.getKey());
			}
		}
		
		return availableChallenges;
	}
	
	@SuppressWarnings("deprecation")
	public boolean hasRequired(String challenge, Player player, int times) {
		final String[] reqList = Challenges.getChallengeConfig().getString(
				"challenges.challengeList." + challenge + ".requiredItems")
				.split(" ");
		
		List<ItemStack> toBeRemoved = new ArrayList<ItemStack>();
		Material reqItem;
		int reqAmount = 0;
		for (final String s : reqList) {
			final String[] part = s.split(":");
			// Material:Qty
			if (part.length == 2) {
				try {
					// Correct some common mistakes
					if (part[0].equalsIgnoreCase("potato")) {
						part[0] = "POTATO_ITEM";
					} else if (part[0].equalsIgnoreCase("brewing_stand")) {
						part[0] = "BREWING_STAND_ITEM";
					} else if (part[0].equalsIgnoreCase("carrot")) {
						part[0] = "CARROT_ITEM";
					} else if (part[0].equalsIgnoreCase("cauldron")) {
						part[0] = "CAULDRON_ITEM";
					} else if (part[0].equalsIgnoreCase("skull")) {
						part[0] = "SKULL_ITEM";
					}
					reqItem = Material.getMaterial(part[0]);
					reqAmount = Integer.parseInt(part[1]) * times;
					ItemStack item = new ItemStack(reqItem);

					if (!player.getInventory().contains(reqItem)) {
						return false;
					} else {
						// check amount
						int amount = 0;
						// Go through all the inventory and try to find
						// enough required items
						for (Map.Entry<Integer, ? extends ItemStack> en
								: player.getInventory().all(reqItem).entrySet()) {
							// Get the item
							ItemStack i = en.getValue();
							// Map needs special handling because the
							// durability increments every time a new one is
							// made by the player
							// TODO: if there are any other items that act
							// in the same way, they need adding too...
							if (i.getDurability() == 0 || (reqItem == Material.MAP && i.getType() == Material.MAP)) {
								// Clear any naming, or lore etc.
								//POKECHU22-TODO: Why clear it!?
								i.setItemMeta(null);
								player.getInventory().setItem(en.getKey(), i);
								// #1 item stack qty + amount is less than
								// required items - take all i
								// #2 item stack qty + amount = required
								// item -
								// take all
								// #3 item stack qty + amount > req items -
								// take
								// portion of i
								// amount += i.getAmount();
								if ((amount + i.getAmount()) < reqAmount) {
									// Remove all of this item stack - clone
									// otherwise it will keep a reference to
									// the
									// original
									toBeRemoved.add(i.clone());
									amount += i.getAmount();
								} else if ((amount + i.getAmount()) == reqAmount) {
									toBeRemoved.add(i.clone());
									amount += i.getAmount();
									break;
								} else {
									// Remove a portion of this item

									item.setAmount(reqAmount - amount);
									item.setDurability(i.getDurability());
									toBeRemoved.add(item);
									amount += i.getAmount();
									break;
								}
							}
						}
						if (amount < reqAmount) {
							return false;
						}
					}
				} catch (Exception e) {
					getLogger().severe("Problem with " + s + " in challenges.yml!");
					player.sendMessage("§cThere are errors in the challenges configuration!  Command cannot be used.");
					String materialList = "";
					boolean hint = false;
					for (Material m : Material.values()) {
						materialList += m.toString() + ",";
						if (m.toString().contains(s.substring(0, 3).toUpperCase())) {
							getLogger().severe("Did you mean " + m.toString() + "?");
							hint = true;
						}
					}
					if (!hint) {
						getLogger().severe("Sorry, I have no idea what " + s + " is. Pick from one of these:");
						getLogger().severe(materialList.substring(0, materialList.length() - 1));
					} else {
						getLogger().severe("Correct challenges.yml with the correct material.");
					}
					return false;
				}
			} else if (part.length == 3) {
				// This handles items with durability or potions
				try {
					// Correct some common mistakes
					if (part[0].equalsIgnoreCase("potato")) {
						part[0] = "POTATO_ITEM";
					} else if (part[0].equalsIgnoreCase("brewing_stand")) {
						part[0] = "BREWING_STAND_ITEM";
					} else if (part[0].equalsIgnoreCase("carrot")) {
						part[0] = "CARROT_ITEM";
					} else if (part[0].equalsIgnoreCase("cauldron")) {
						part[0] = "CAULDRON_ITEM";
					} else if (part[0].equalsIgnoreCase("skull")) {
						part[0] = "SKULL_ITEM";
					}
					reqItem = Material.getMaterial(part[0]);
					int reqDurability = Integer.parseInt(part[1]);
					reqAmount = Integer.parseInt(part[2]) * times;
					int count = reqAmount;
					ItemStack item = new ItemStack(reqItem);
					// Check for potions
					if (reqItem.equals(Material.POTION)) {
						// Contains at least does not work for potions
						ItemStack[] playerInv = player.getInventory().getContents();
						for (ItemStack i : playerInv) {
							if (i != null && i.getType().equals(Material.POTION)) {
								if (i.getDurability() == reqDurability) {
									item = i.clone();
									if (item.getAmount() > reqAmount) {
										item.setAmount(reqAmount);
									}
									count = count - item.getAmount();
									// If the item stack has more in it than
									// required, just take the minimum
									toBeRemoved.add(item);
								}
							}
							if (count == 0) {
								break;
							}
						}
						if (count > 0) {
							return false;
						}
						// They have enough
					} else {
						// Item
						item.setDurability((short) reqDurability);
						// check amount
						int amount = 0;
						// Go through all the inventory and try to find
						// enough required items
						for (Map.Entry<Integer, ? extends ItemStack> en : player.getInventory().all(reqItem).entrySet()) {
							// Get the item
							ItemStack i = en.getValue();
							if (i.getDurability() == reqDurability) {
								// Clear any naming, or lore etc.
								i.setItemMeta(null);
								player.getInventory().setItem(en.getKey(), i);
								// #1 item stack qty + amount is less than
								// required items - take all i
								// #2 item stack qty + amount = required
								// item -
								// take all
								// #3 item stack qty + amount > req items -
								// take
								// portion of i
								if ((amount + i.getAmount()) < reqAmount) {
									// Remove all of this item stack - clone
									// otherwise it will keep a reference to
									// the
									// original
									toBeRemoved.add(i.clone());
									amount += i.getAmount();
								} else if ((amount + i.getAmount()) == reqAmount) {
									toBeRemoved.add(i.clone());
									amount += i.getAmount();
									break;
								} else {
									// Remove a portion of this item

									item.setAmount(reqAmount - amount);
									item.setDurability(i.getDurability());
									toBeRemoved.add(item);
									amount += i.getAmount();
									break;
								}
							}
						}
						if (amount < reqAmount) {
							return false;
						}
					}
				} catch (Exception e) {
					getLogger().severe("Problem with " + s + " in challenges.yml!");
					player.sendMessage("§cThere are errors in the challenges configuration!  Command cannot be used.");
					if (part[0].equalsIgnoreCase("POTION")) {
						getLogger().severe("Format POTION:TYPE:QTY where TYPE is the number of the following:");
						for (PotionType p : PotionType.values()) {
							getLogger().info(p.toString() + ":" + p.getDamageValue());
						}
					} else {
						String materialList = "";
						boolean hint = false;
						for (Material m : Material.values()) {
							materialList += m.toString() + ",";
							if (m.toString().contains(s.substring(0, 3))) {
								getLogger().severe("Did you mean " + m.toString() + "?");
								hint = true;
							}
						}
						if (!hint) {
							getLogger().severe("Sorry, I have no idea what " + s + " is. Pick from one of these:");
							getLogger().severe(materialList.substring(0, materialList.length() - 1));
						} else {
							getLogger().severe("Correct challenges.yml with the correct material.");
						}
						return false;
					}
					return false;
				}
			}
		}
		// Build up the items in the inventory and remove them if they are
		// all there.

		if (Challenges.getChallengeConfig().getBoolean("challenges.challengeList." + challenge + ".takeItems")) {
			for (ItemStack i : toBeRemoved) {
				HashMap<Integer, ItemStack> leftOver = player.getInventory().removeItem(i);
				if (!leftOver.isEmpty()) {
					getLogger().warning(
							"Exploit? Could not remove the following in challenge " + challenge + " for player " + player.getName() + ":");
					for (ItemStack left : leftOver.values()) {
						getLogger().info(left.toString());
					}
					return false;
				}
			}
		}
		return true;
	}
	
	private Logger getLogger() {
		return SkyblockExtension.inst().getLogger();
	}
}
