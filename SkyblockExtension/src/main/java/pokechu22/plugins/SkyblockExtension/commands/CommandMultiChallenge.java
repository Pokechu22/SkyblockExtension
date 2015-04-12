package pokechu22.plugins.SkyblockExtension.commands;

import static pokechu22.plugins.SkyblockExtension.util.TabCompleteUtil.TabLimit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import pokechu22.plugins.SkyblockExtension.PermissionHandler;
import pokechu22.plugins.SkyblockExtension.SkyblockExtension;
import pokechu22.plugins.SkyblockExtension.errorhandling.ErrorHandler;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.PlayerCache;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.commands.Challenges;
import com.wasteofplastic.askyblock.events.ChallengeCompleteEvent;
import com.wasteofplastic.askyblock.util.VaultHelper;

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
	public static enum MultiChallengeRoundingMode {
		ROUND_UP_LOSSY {
			@Override
			public int apply(int quantity, int times, double tax) {
				return ceil(tax * quantity) * times;
			}
		},
		ROUND_UP_KEEP {
			@Override
			public int apply(int quantity, int times, double tax) {
				return ceil(tax * quantity * times);
			}
		},
		ROUND_NEAREST_LOSSY {
			@Override
			public int apply(int quantity, int times, double tax) {
				return nearest(tax * quantity) * times;
			}
		},
		ROUND_NEAREST_KEEP {
			@Override
			public int apply(int quantity, int times, double tax) {
				return nearest(tax * quantity * times);
			}
		},
		ROUND_DOWN_LOSSY {
			@Override
			public int apply(int quantity, int times, double tax) {
				return floor(tax * quantity) * times;
			}
		},
		ROUND_DOWN_KEEP {
			@Override
			public int apply(int quantity, int times, double tax) {
				return floor(tax * quantity * times);
			}
		};
		
		public abstract int apply(int quantity, int times, 
				double tax);
		
		private static int ceil(double value) {
			return (int)Math.ceil(value);
		}
		private static int floor(double value) {
			return (int)Math.floor(value);
		}
		private static int nearest(double value) {
			return (int)Math.round(value);
		}
		
		public static MultiChallengeRoundingMode match(
				String name) {
			return MultiChallengeRoundingMode.valueOf(
					name.toUpperCase().replaceAll("\\s+", "_")
					.replaceAll("\\W", ""));
		}
	}
	
	/**
	 * The way that quantities are rounded.
	 */
	public static MultiChallengeRoundingMode roundMode;
	/**
	 * The tax to apply.
	 */
	public static double tax;
	
	private Challenges challenges;
	private PlayerCache players;
	private FileConfiguration challengesConfig;
	
	public CommandMultiChallenge() {
		this.challenges = ASkyBlock.getPlugin().getChallenges();
		this.players = ASkyBlock.getPlugin().getPlayers();
		this.challengesConfig = Challenges.getChallengeConfig();
	}
	
	public CommandMultiChallenge(Challenges challenges, PlayerCache players, FileConfiguration challengesConfig) {
		this.challenges = challenges;
		this.players = players;
		this.challengesConfig = challengesConfig;
	}
	
	/**
	 * Tab-completion.  (For challenge names)
	 * 
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
				if (!canDoChallengeWithoutRunningOutOfTimes(player, challengeName, repititions)) {
					sender.sendMessage("§cDoing " + challengeName + " " + repititions + 
							" more times would excede the maximum times!");
					return true;
				}
				if (!isChallengeAvailable(player, challengeName)) {
					sender.sendMessage("§cYou have not unlocked " + challengeName + ".");
					sender.sendMessage("§cTo be able to use a challenge with /" + commandLabel + 
							", you must first complete it with the regular /c command.");
					return true;
				}
				
				if (!hasRequired(player, challengeName, repititions)) {
					sender.sendMessage("§cYou don't have the items needed to complete " + 
							challengeName + " " + repititions + " time" + 
							(repititions == 1 ? "" : "s") + "!");
					return true;
				}
				
				giveReward(player, challengeName, repititions);
				
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
		return players.challengeExists(player.getUniqueId(), challengeName);
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
		String level = challengesConfig.getString("challenges.challengeList." + challengeName + ".level");
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
		if (!challengesConfig.getBoolean(
				"challenges.challengeList." + challengeName + ".repeatable")) {
			return false;
		}
		if (!challengesConfig.getString(
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
		int maxTimes = challengesConfig.getInt(
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
	
	/**
	 * Checks if the player has the required items, and if so, takes them.  
	 * 
	 * @author tastybento
	 * 
	 * @param player
	 * @param challenge
	 * @param times
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public boolean hasRequired(Player player, String challenge, int times) {
		final String[] reqList = challengesConfig.getString(
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

		if (challengesConfig.getBoolean("challenges.challengeList." + challenge + ".takeItems")) {
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
	
	/**
	 * Gives the reward to the specified player.
	 * This command <b>only</b> gives repeat rewards.  
	 * 
	 * @author tastybento 
	 * 
	 * @param player
	 * @param challenge
	 * @param times
	 */
	private void giveReward(Player player, String challenge, int times) {
		// Grab the rewards from the config.yml file
		String[] permList;
		String[] itemRewards;
		int moneyReward = 0;
		int expReward = 0;
		String rewardText = "";

		itemRewards = challengesConfig.getString("challenges.challengeList." + challenge.toLowerCase() + ".repeatItemReward", "").split(" ");
		moneyReward = challengesConfig.getInt("challenges.challengeList." + challenge.toLowerCase() + ".repeatMoneyReward", 0);
		rewardText = ChatColor.translateAlternateColorCodes('&',
				challengesConfig.getString("challenges.challengeList." + challenge.toLowerCase() + ".repeatRewardText", "Goodies!"));
		expReward = challengesConfig.getInt("challenges.challengeList." + challenge + ".repeatExpReward", 0);

		int moneySuccesses = 0;
		
		// Give items
		Material rewardItem;
		int rewardQty;
		// Build the item stack of rewards to give the player
		for (final String s : itemRewards) {
			final String[] element = s.split(":");
			if (element.length == 2) {
				try {
					rewardItem = Material.getMaterial(element[0].toUpperCase());
					rewardQty = roundMode.apply(Integer.parseInt(element[1]), times, tax);
					final HashMap<Integer, ItemStack> leftOvers = player.getInventory().addItem(new ItemStack[] { new ItemStack(rewardItem, rewardQty) });
					if (!leftOvers.isEmpty()) {
						player.getWorld().dropItemNaturally(player.getLocation(), leftOvers.get(0));
					}
				} catch (Exception e) {
					player.sendMessage(ChatColor.RED + ASkyBlock.getPlugin().myLocale(player.getUniqueId()).challengeserrorRewardProblem);
					getLogger().severe("Could not give " + element[0] + ":" + element[1] + " to " + player.getName() + " for challenge reward!");
					String materialList = "";
					boolean hint = false;
					for (Material m : Material.values()) {
						materialList += m.toString() + ",";
						if (element[0].length() > 3) {
							if (m.toString().startsWith(element[0].substring(0, 3))) {
								getLogger().severe("Did you mean " + m.toString() + "? If so, put that in challenges.yml.");
								hint = true;
							}
						}
					}
					if (!hint) {
						getLogger().severe("Sorry, I have no idea what " + element[0] + " is. Pick from one of these:");
						getLogger().severe(materialList.substring(0, materialList.length() - 1));
					}
				}
			} else if (element.length == 3) {
				try {
					rewardItem = Material.getMaterial(element[0]);
					rewardQty = roundMode.apply(Integer.parseInt(element[2]), times, tax);
					// Check for POTION
					if (rewardItem.equals(Material.POTION)) {
						// Add the effect of the potion
						final PotionEffectType potionType = PotionEffectType.getByName(element[1]);
						if (potionType == null) {
							getLogger().severe("Reward potion effect type in config.yml challenges is unknown - skipping!");
						} else {
							final Potion rewPotion = new Potion(PotionType.getByEffect(potionType));
							final HashMap<Integer, ItemStack> leftOvers = player.getInventory().addItem(new ItemStack[] { rewPotion.toItemStack(rewardQty) });
							if (!leftOvers.isEmpty()) {
								player.getWorld().dropItemNaturally(player.getLocation(), leftOvers.get(0));
							}
						}
					} else {
						// Normal item, not a potion
						int rewMod = Integer.parseInt(element[1]);
						final HashMap<Integer, ItemStack> leftOvers = player.getInventory().addItem(
								new ItemStack[] { new ItemStack(rewardItem, rewardQty, (short) rewMod) });
						if (!leftOvers.isEmpty()) {
							player.getWorld().dropItemNaturally(player.getLocation(), leftOvers.get(0));
						}
					}
				} catch (Exception e) {
					player.sendMessage(ChatColor.RED + "There was a problem giving your reward. Ask Admin to check log!");
					getLogger().severe("Could not give " + element[0] + ":" + element[1] + " to " + player.getName() + " for challenge reward!");
					if (element[0].equalsIgnoreCase("POTION")) {
						String potionList = "";
						boolean hint = false;
						for (PotionEffectType m : PotionEffectType.values()) {
							potionList += m.toString() + ",";
							if (element[1].length() > 3) {
								if (m.toString().startsWith(element[1].substring(0, 3))) {
									getLogger().severe("Did you mean " + m.toString() + "?");
									hint = true;
								}
							}
						}
						if (!hint) {
							getLogger().severe("Sorry, I have no idea what potion type " + element[1] + " is. Pick from one of these:");
							getLogger().severe(potionList.substring(0, potionList.length() - 1));
						}
					} else {
						String materialList = "";
						boolean hint = false;
						for (Material m : Material.values()) {
							materialList += m.toString() + ",";
							if (m.toString().startsWith(element[0].substring(0, 3))) {
								getLogger().severe("Did you mean " + m.toString() + "? If so, put that in challenges.yml.");
								hint = true;
							}
						}
						if (!hint) {
							getLogger().severe("Sorry, I have no idea what " + element[0] + " is. Pick from one of these:");
							getLogger().severe(materialList.substring(0, materialList.length() - 1));
						}
					}
				}
			}
		}
		//Give all other components of the reward using a loop.
		for (int i = 0; i < times; i++) {
			// Report the rewards and give out exp, money and permissions if
			// appropriate
			if (expReward > 0) {
				player.giveExp(expReward);
			}
			if (Settings.useEconomy && moneyReward > 0 && (VaultHelper.econ != null)) {
				EconomyResponse e = VaultHelper.econ.depositPlayer(player, Settings.worldName, moneyReward);
				if (e.transactionSuccess()) {
					moneySuccesses ++;
				} else {
					getLogger().severe("Error giving player " + player.getUniqueId() + " challenge money:" + e.errorMessage);
					getLogger().severe("Reward was $" + moneyReward);
				}
			}
			// Dole out permissions
			permList = challengesConfig.getString("challenges.challengeList." + challenge.toLowerCase() + ".permissionReward", "").split(" ");
			for (final String s : permList) {
				if (!s.isEmpty()) {
					if (!VaultHelper.checkPerm(player, s)) {
						VaultHelper.addPerm(player, s);
						getLogger().info("Added permission " + s + " to " + player.getName() + "");
					}
				}
			}
			
			// Run reward commands
			List<String> commands = challengesConfig.getStringList("challenges.challengeList." + challenge.toLowerCase() + ".repeatrewardcommands");
			for (String cmd : commands) {
				// Substitute in any references to player
				try {
					if (!Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("[player]", player.getName()))) {
						getLogger().severe("Problem executing challenge repeat reward commands - skipping!");
						getLogger().severe("Command was : " + cmd);
					}
				} catch (Exception e) {
					getLogger().severe("Problem executing challenge repeat reward commands - skipping!");
					getLogger().severe("Command was : " + cmd);
					getLogger().severe("Error was: " + e.getMessage());
					e.printStackTrace();
				}
			}
	
			// Mark the challenge as complete
			players.completeChallenge(player.getUniqueId(), challenge);
			// Call the Challenge Complete Event
			final ChallengeCompleteEvent event = new ChallengeCompleteEvent(player, challenge, permList, itemRewards, moneyReward, expReward, rewardText);
			Bukkit.getPluginManager().callEvent(event);
		}
		
		//And now we handle the messages.
		player.sendMessage(ChatColor.GOLD + ASkyBlock.getPlugin().myLocale(player.getUniqueId()).challengesrewards + ": " + ChatColor.WHITE + 
				replacedNumbers(rewardText, times, tax, roundMode));
		
		if (expReward > 0) {
			player.sendMessage(ChatColor.GOLD + ASkyBlock.getPlugin().myLocale(player.getUniqueId()).challengesexpReward + ": " + ChatColor.WHITE + (expReward * times));
		}
		if (moneySuccesses > 0) {
			player.sendMessage(ChatColor.GOLD + ASkyBlock.getPlugin().myLocale(player.getUniqueId()).challengesmoneyReward + ": " + ChatColor.WHITE + VaultHelper.econ.format(moneyReward * moneySuccesses));
		}
		
		player.getWorld().playSound(player.getLocation(), Sound.ITEM_PICKUP, 1F, 1F);
	}
	
	protected static String replacedNumbers(String description, int times, 
			double tax, MultiChallengeRoundingMode roundMode) {
		//Prepend and apend a space for the below regex.
		StringBuilder edited = new StringBuilder(description)
				.insert(0, ' ').append(' ');
		//The following regex matches a series of digits
		//that have a space before and after them.
		//The '+' means that it goes for all that it can
		//(rather than grabbing only 1).
		Matcher m = Pattern.compile("\\s+\\d+\\s+").matcher(edited);
		while (m.find()) {
			String sub = edited.substring(m.start(), m.end());
			String number = sub.trim();
			int val = Integer.parseInt(sub.trim());
			int newVal = roundMode.apply(val, times, tax); 
			String newNumber = Integer.toString(newVal);
			String newSub = sub.replace(number, newNumber);
			edited.replace(m.start(), m.end(), sub.replace(number,
					newNumber));
			
			m.region(m.start() + newSub.length(), edited.length());
		}
		
		return edited.toString().trim();
	}
	
	private Logger getLogger() {
		return SkyblockExtension.inst().getLogger();
	}
}
