package pokechu22.plugins.SkyblockExtension.commands;

import static pokechu22.plugins.SkyblockExtension.util.StringUtil.trailOff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import pokechu22.plugins.SkyblockExtension.SkyblockExtension;
import pokechu22.plugins.SkyblockExtension.errorhandling.ErrorHandler;
import pokechu22.plugins.SkyblockExtension.errorhandling.GenericReport;
import pokechu22.plugins.SkyblockExtension.protection.IslandInfo;
import pokechu22.plugins.SkyblockExtension.util.IslandUtils;
import us.talabrek.ultimateskyblock.IslandCommand;
import us.talabrek.ultimateskyblock.PlayerInfo;
import us.talabrek.ultimateskyblock.Settings;
import us.talabrek.ultimateskyblock.uSkyBlock;

/**
 * Intended to do the same thing as IslandCommand, but override some 
 * functionality.  It is intended to replace the vanilla command.
 *
 * @author Pokechu22
 *
 */
public class USkyBlockCommandIsland extends IslandCommand implements TabCompleter {
	public USkyBlockCommandIsland() {
		super();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, 
			String label, String[] args) {
		if (allowIgnoreCase) {
			//Only applied to first argument at this time, but that seems fine.
			if (args.length > 0) {
				args[0] = args[0].toLowerCase(Locale.ENGLISH);
			}
		}
		
		if (enableHelp2) {
			//This override needs to occur beforehand.
			if (args.length > 0 && args[0].equalsIgnoreCase("help2")) {
				help2(sender, command, label, args);
				return true;
			}
		}
		boolean superResult = super.onCommand(sender, command, label, args);
		if (enableHelp2) {
			if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
				alertAboutNewHelp(sender);
			}
		}
		return superResult;
	}
	
	/**
	 * Adds a player to the party.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean addPlayertoParty(String playerName, String partyLeader) {
		if (IslandUtils.canGetPlayerInfo(partyLeader)) {
			//Should ALWAYS happen.
			Player leader = Bukkit.getPlayer(partyLeader);
			PlayerInfo info = IslandUtils.getPlayerInfo(leader);
			
			IslandInfo islandInfo = IslandUtils.getIslandInfo(info);
			islandInfo.freshenOwner(leader);
		}
		if (IslandUtils.canGetPlayerInfo(playerName)) {
			//Should ALWAYS happen.
			Player member = Bukkit.getPlayer(playerName);
			PlayerInfo info = IslandUtils.getPlayerInfo(member);
			
			IslandInfo islandInfo = IslandUtils.getIslandInfo(info);
			islandInfo.freshenMember(member);
		}
		//IslandUtils.getPlayerInfo()
		boolean superResult = super.addPlayertoParty(playerName, partyLeader);
		return superResult;
	}
	
	/**
	 * Removes a player from the party.
	 */
	@Override
	public void removePlayerFromParty(String playerName, String partyLeader) {
		super.removePlayerFromParty(playerName, partyLeader);
		return;
	}
	
	/**
	 * Calculates island level.
	 * 
	 * @param player
	 * @param islandPlayer
	 * @return
	 */
	@Override
	public boolean getIslandLevel(Player player, String islandPlayer) {
		if (!useCustomBlockValues) {
			return super.getIslandLevel(player, islandPlayer);
		} else {
			if ((!uSkyBlock.getInstance().hasIsland(islandPlayer)) && 
					(!uSkyBlock.getInstance().hasParty(islandPlayer))) {
				player.sendMessage(ChatColor.RED + 
						"That player is invalid or does not have an island!");
				this.allowInfo = true;
				return false;
			}
			SkyblockExtension.inst().getServer().getScheduler()
					.runTaskAsynchronously(SkyblockExtension.inst(),
							new AsyncIslandLevelCalculator(player, 
									islandPlayer));
			return true;
		}
	}
	
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\
 * END OF USKYBLOCK CODE! NONE OF THIS IS PART OF THE ORIGIONAL COMMAND. *
\* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	
	/**
	 * Asynchronously calculates the island level.  
	 *
	 * @author Pokechu22
	 *
	 */
	protected static class AsyncIslandLevelCalculator implements Runnable {

		/**
		 * The player that receives initiated the request and thus 
		 * receives associated chat messages.
		 */
		private Player player;
		/**
		 * The player who's island is being calculated.
		 */
		private String islandPlayer;
		
		/**
		 * The mapping for different block values.
		 * TODO: use.
		 */
		public static Map<Integer, Integer> blockValues;
		/**
		 * The maximum amount of cobble that will be counted.
		 */
		public static int maximumCobble = 10000;
		
		public AsyncIslandLevelCalculator(Player player, String islandPlayer) {
			this.player = player;
			this.islandPlayer = islandPlayer;
		}
		
		@Override
		public void run() {
			System.out.print("Calculating island level in async thread");
			try {
				String playerName = player.getName();
				Location l;
				if (((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playerName)).getHasParty())
				{
					l = ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playerName)).getPartyIslandLocation();
				}
				else
					l = ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playerName)).getIslandLocation();
				int blockcount = 0;
				if (playerName.equalsIgnoreCase(islandPlayer))
				{
					int cobblecount = 0;
					int endcount = 0;
					int px = l.getBlockX();
					int py = l.getBlockY();
					int pz = l.getBlockZ();
					for (int x = -50; x <= 50; x++) {
						for (int y = Settings.island_height * -1; y <= 255 - Settings.island_height; y++) {
							for (int z = -50; z <= 50; z++)
							{
								Block b = new Location(l.getWorld(), px + x, py + y, pz + z).getBlock();
								if (b.getTypeId() == 57) {
									blockcount += 300;
								}
								if ((b.getTypeId() == 41) || (b.getTypeId() == 116) || (b.getTypeId() == 122)) {
									blockcount += 150;
								}
								if ((b.getTypeId() == 49) || (b.getTypeId() == 42)) {
									blockcount += 10;
								}
								if ((b.getTypeId() == 47) || (b.getTypeId() == 84)) {
									blockcount += 5;
								}
								if ((b.getTypeId() == 79) || (b.getTypeId() == 82) || (b.getTypeId() == 112) || (b.getTypeId() == 2) || (b.getTypeId() == 110)) {
									blockcount += 3;
								}
								if ((b.getTypeId() == 45) || (b.getTypeId() == 35) || (b.getTypeId() == 24) || 
										(b.getTypeId() == 121) || (b.getTypeId() == 108) || (b.getTypeId() == 109) || (b.getTypeId() == 43) || 
										(b.getTypeId() == 20) || (b.getTypeId() == 89) || (b.getTypeId() == 155) || (b.getTypeId() == 156)) {
									blockcount += 2;
								}
								if (((b.getTypeId() != 0) && (b.getTypeId() != 8) && (b.getTypeId() != 106) && (b.getTypeId() != 9) && (b.getTypeId() != 10) && (b.getTypeId() != 11) && (b.getTypeId() != 4)) || ((b.getTypeId() == 4) && (cobblecount < 10000)) || ((b.getTypeId() == 121) && (endcount < 5000)))
								{
									blockcount++;
									if (b.getTypeId() == 4) {
										cobblecount++;
									}
									if (b.getTypeId() == 121) {
										endcount++;
									}
								}
							}
						}
					}
				}

				if (playerName.equalsIgnoreCase(islandPlayer))
				{
					((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playerName)).setIslandLevel(blockcount / 100);
				}
			} catch (Exception e) {
				System.out.print("Error while calculating Island Level: " + e);
			}
			System.out.print("Finished async info thread");

//			uSkyBlock.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(uSkyBlock.getInstance(), new Runnable()
//			{
//				public void run()
//				{
//					System.out.print("Back to sync thread for info");
//					if (Bukkit.getPlayer(this) != null)
//					{
//						Bukkit.getPlayer(this.val$playerx).sendMessage(ChatColor.YELLOW + "Information about " + this.val$islandPlayerx + "'s Island:");
//						if (this.val$playerx.equalsIgnoreCase(this.val$islandPlayerx))
//						{
//							Bukkit.getPlayer(this.val$playerx).sendMessage(ChatColor.GREEN + "Island level is " + ChatColor.WHITE + ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(this.val$playerx)).getIslandLevel());
//						}
//						else {
//							PlayerInfo pi = uSkyBlock.getInstance().readPlayerFile(this.val$islandPlayerx);
//							if (pi != null)
//								Bukkit.getPlayer(this.val$playerx).sendMessage(ChatColor.GREEN + "Island level is " + ChatColor.WHITE + pi.getIslandLevel());
//							else
//								Bukkit.getPlayer(this.val$playerx).sendMessage(ChatColor.RED + "Error: Invalid Player");
//						}
//					}
//					System.out.print("Finished with sync thread for info");
//				}
//			}
//			, 0L);
		}
		
	}
	
	/**
	 * Enable the enhanced help2 system?
	 */
	public static boolean enableHelp2 = true;
	
	/**
	 * Allow uSkyBlock to be case insensitive? 
	 * (Potential source of bugs!)
	 */
	public static boolean allowIgnoreCase = true;
	
	/**
	 * Use custom island block value calculator?
	 */
	public static boolean useCustomBlockValues = true;
	
	/**
	 * Registers all hooks on this command, overriding uSkyBlock code.
	 */
	public static void registerHooks() {
		USkyBlockCommandIsland inst = new USkyBlockCommandIsland();
		//Command itself.
		uSkyBlock.getInstance().getCommand("island").setExecutor(inst);
		//Tab completion.
		uSkyBlock.getInstance().getCommand("island").setTabCompleter(inst);
	}
	
	/**
	 * Information for when help is created.
	 * This controls args[0]'s effects.
	 * 
	 * Internally, the array represents this: 
	 * <h1>[0]:</h1> The help message.
	 * 
	 * <h1>[1]:</h1> The needed permission.  This is comma-separated, if
	 * multiple permissions are needed.  One can also prepend an entry with
	 * <code>!</code> to inverse it. This will usually be a permission 
	 * string, but if it starts with a <code>_</code>, it is A) assumed 
	 * to be <code>usb.island.create</code> (as that's normally needed) 
	 * and B) processed based on if it's one of the following: 
	 * <h2><code>has_island</code></h2>
	 * The player must have an island.
	 * <h2><code>has_party</code></h2>
	 * The player must have a party.
	 * <h2><code>is_owner</code></h2>
	 * You must be the owner of your island.
	 * <h2><code>is_member</code></h2>
	 * You must be at least a member.  (Includes owner; 
	 * if you want to exclude owner add <code>!is_owner</code>.)
	 * <h2><code>has_invite</code></h2>
	 * You must have an invite to an island.
	 * <h2><code>on_skyblock_world</code></h2>
	 * You must be on the Skyblock world.
	 * <h2><code>on_own_island</code></h2>
	 * You must be on your own island.
	 * <h2><code>on_ultimateskyblock</code></h2>
	 * You must be on the server <code>UltimateSkyblock</code>.
	 * I'm not kidding.  This is a real requirement in the plugin.
	 * <h2><code>false</code></h2>
	 * Automatically fails.  This is usefull for commands which are 
	 * disabled and thus unusable.  
	 * <code>!false</code> automatically succeeds, but that's pointless.
	 * Both of these use gray text rather than red text on failure.
	 * 
	 * <h1>[2]:</h1> Required value in config.  The first value should be 
	 * either <code>sbe</code> or <code>uskyblock</code>, followed by the
	 * full config key path, followed by a <code>=</code>, followed by the
	 * expected value.  Expected value is a string.  As with before, this
	 * can be comma-separated and using an <code>!</code> will inverse it.
	 * <hr>
	 * These parameters can be missing, as needed.
	 */
	private static final Map<String, String[]> subCommands;
	/*
	 * Static initializer for subCommands.
	 */
	static {
		TreeMap<String, String[]> map = new TreeMap<String, String[]>(
				String.CASE_INSENSITIVE_ORDER);
		
		//TODO
		//These were all taken from the decompilation.
		map.put("restart", new String[]{
				//Help
				"Restarts your island.\n" +
				"Your inventory will be cleared, and everything " +
				"reset.  You cannot use this command if you aren't " +
				"the island owner, nor can you use it if you have " +
				"other players in your party.  §6/island kick§r them " +
				"if you must.  \nUsage\n:§6/island reset§r: reset " +
				"your island.\nNote: this command operates on a " +
				"cooldown; you cannot spam it.  Also note that " +
				"§l§ochallenges will NOT be reset§r, and thus you " +
				"may end up not being able to recomplete 1-time " +
				"challenges.  You have been warned.\n" +
				"Aliases: \n· §6/island reset§f.",
				//Perm
				"_is_owner," +
				"_!has_party",
				//Conf
				""
		});
		map.put("reset", new String[]{
				//Help
				"Restarts your island.\n" +
				"Your inventory will be cleared, and everything " +
				"reset.  You cannot use this command if you aren't " +
				"the island owner, nor can you use it if you have " +
				"other players in your party.  §6/island kick§r them " +
				"if you must.  \nUsage\n:§6/island restart§r: reset " +
				"your island.\nNote: this command operates on a " +
				"cooldown; you cannot spam it.  Also note that " +
				"§l§ochallenges will NOT be reset§r, and thus you " +
				"may end up not being able to recomplete 1-time " +
				"challenges.  You have been warned.\n" +
				"Aliases: \n· §6/island restart§f.",
				//Perm
				"_is_owner," +
				"_!has_party",
				//Conf
				""
		});
		map.put("sethome", new String[]{
				//Help
				"Sets the home location of your island.\n" +
				"This is where you go when you do §6/island§r.  It " +
				"must be on your own island.\nUsage:\n" +
				"§6/island sethome§r.\n" +
				"Aliases: \n· §6/island tpset§f.",
				//TODO Does this work when you are not the owner?
				//And is it player-dependent or independent?
				//Perm
				"usb.island.sethome," +
				"on_skyblock_world," +
				"on_own_island",
				//Conf
				""
		});
		map.put("tpset", new String[]{
				//Help
				"Sets the home location of your island.\n" +
				"This is where you go when you do §6/island§r.  It " +
				"must be on your own island.\nUsage:\n" +
				"§6/island tpset§r.\n" +
				"Aliases: \n· §6/island sethome§f.",
				//TODO Does this work when you are not the owner?
				//And is it player-dependent or independent?
				//Perm
				"usb.island.sethome," +
				"on_skyblock_world," +
				"on_own_island",
				//Conf
				""
		});
		map.put("lock", new String[]{
				//Help
				"Locks your island, which prevents players who are not " +
				"members of your island from entering it.\n" +
				"This is only functional when the server uses " +
				"WorldGaurd for permissions.  \nUsage:\n" +
				"§6/island lock§r: locks your own island.\n" +
				"See also §6/island unlock§r.",
				//Perm
				"usb.lock," +
				"_is_owner",
				//Conf
				"uskyblock.options.island.allowIslandLock," +
				"uskyblock.options.island.protectWithWorldGuard"
		});
		map.put("unlock", new String[]{
				//Help
				"Unlocks your island, which allows players who are not " +
				"members of your island to enter it.\n" +
				"This is only functional when the server uses " +
				"WorldGaurd for permissions.  \nUsage:\n" +
				"§6/island unlock§r: unlocks your own island.\n" +
				"See also §6/island lock§r.",
				//Perm
				"usb.lock," +
				"_is_owner",
				//Conf
				"uskyblock.options.island.allowIslandLock," +
				"uskyblock.options.island.protectWithWorldGuard"
		});
		map.put("help", new String[]{
				//Help
				"Older help information, which is not ideal but can be " +
				"helpful.  Use §6/island help2§r instead, as it is " +
				"better (this totally isn't biassed...)\nUsage:\n" +
				"§6/island help§r: provides 1 meager sentence of help " +
				"for the specific command, or just ignores it if you " +
				"cannot use the command in the first place.\n" +
				"There is no extended help for any command.",
				//Perm
				"",
				//Conf
				""
		});
		map.put("top", new String[]{
				//Help
				"Display the top 10 ranked islands.\n" + 
				"Usage: §6/island top§f.",
				//Perm
				"usb.island.topten",
				//Conf
				"uskyblock.options.island.useTopTen"
		});
		map.put("info", new String[]
				{
				//Help
				"Check your own or another player's island level.\n" + 
				"Usage: \n§6/island info§f: Get your own island's level " +
				"(only usable on your own island, for odd reasons)\n" +
				"§6/island info <player>§f: Get the level of " +
				"<player>'s island.  (Can even be used to get your " +
				"own level on other islands).  \n" +
				"Note that this command can behave strangely at times, " +
				"such as giving level 0 for someone really high level.  " +
				"It is also §lrequired§r that you run this command " +
				"before completing any challenge that requires island " +
				"level to be at least some number.\n" +
				"Aliases: \n· §6/island level§f.",
				//Perm
				"usb.island.info",
				//Conf
				"uskyblock.options.island.useIslandLevel"
		});
		map.put("level", new String[]
				{
				//Help
				"Check your own or another player's island level.\n" + 
				"Usage: \n§6/island level§f: Get your own island's level " +
				"(only usable on your own island, for odd reasons)\n" +
				"§6/island level <player>§f: Get the level of " +
				"<player>'s island.  (Can even be used to get your " +
				"own level on other islands).  \n" +
				"Note that this command can behave strangely at times, " +
				"such as giving level 0 for someone really high level.  " +
				"It is also §lrequired§r that you run this command " +
				"before completing any challenge that requires island " +
				"level to be at least some number.\n" +
				"Aliases: \n· §6/island info§f.",
				//Perm
				"usb.island.info",
				//Conf
				"uskyblock.options.island.useIslandLevel"
		});
		map.put("invite", new String[]{
				//Help
				"Invites a player to your island.  The other player " +
				"lose their island if they accept.\n" +
				"Usage: \n§6/island invite <player>§f: Invite <player>" +
				"to your island.\n§6/island invite§f: View how many " +
				"more people you can invite and stuff like that.",
				//Perm
				"usb.party.create," +
				"_is_owner",
				//Conf
				""
		});
		map.put("accept", new String[]{
				//Help
				"Accepts an existing invite.  You will lose your own " +
				"island if you have one.  \nUsage: \n§6/island accept§f.",
				//Perm
				"usb.party.join" +
				"_has_invite",
				//Conf
				""
		});
		map.put("reject", new String[]{
				//Help
				"Rejects an existing invite.\n" +
				"Usage: \n§6/island accept§f.",
				//Perm
				"_has_invite",
				//Conf
				""
		});
		//These all seem like disabled debug commands, but some work.
		map.put("partypurge", new String[]{
				//Help
				"Disabled moderator command.  Cannot be used.\n" +
				"There may be a similar command in §6/dev§f, but that" +
				"is out of the scope of help.  Sorry.  Too bad.",
				//Perm
				"usb.mod.party," +
				"_false",
				//Conf
				""
		});
		map.put("partyclean", new String[]{
				//Help
				"Disabled moderator command.  Cannot be used.\n" +
				"There may be a similar command in §6/dev§f, but that" +
				"is out of the scope of help.  Sorry.  Too bad.",
				//Perm
				"usb.mod.party," +
				"_false",
				//Conf
				""
		});
		map.put("purgeinvites", new String[]{
				//Help
				"Moderator command that resets all invites.\n" +
				"Usage:\n§6/island purgeinvites§f.",
				//Perm
				"usb.mod.party",
				//Conf
				""
		});
		map.put("partylist", new String[]{
				//Help
				"Disabled moderator command.  Cannot be used.\n" +
				"There may be a similar command in §6/dev§f, but that" +
				"is out of the scope of help.  Sorry.  Too bad.\n" +
				"However, §6/island checkparty§f may be helpful.",
				//Perm
				"usb.mod.party," +
				"_false",
				//Conf
				""
		});
		map.put("invitelist", new String[]{
				//Help
				"Moderator command that lists all invites.\n" +
				"Usage:\n§6/island invitelist§f.",
				//Perm
				"usb.mod.party",
				//Conf
				""
		});
		//More normal commands.
		map.put("leave", new String[]{
				//Help
				"Leaves your current island.  Cannot be used by the " +
				"owner of the island; use §6/island kick§r for that.  " +
				"Also cannot be used if you are the only person.  In " +
				"that case, use §6/island restart§r.\nUsage:\n" +
				"§6/island leave§r.",
				//Perm
				"usb.party.join," +
				"_has_party," +
				"_!is_owner",
				//Conf
				""
		});
		map.put("party", new String[]{
				//Help
				"Gives you a list of who is in your party.  \nUsage:\n" +
				"§6/island party§f.\n" +
				"Note that this CAN also be used when you don't have a " +
				"party - if you have an invite it will show you who " +
				"invited you.  ",
				//TODO: If I make a raw alternative, link it here?
				//Perm
				"_has_party",
				//Conf
				""
		});
		//3-args commands, now.  Some are duplicates, which are commented out.
		//map.put("info", new String[]{});
		//map.put("level", new String[]{});
		//map.put("invite", new String[]{});
		map.put("remove", new String[]{
				//Help
				"Forcibly removes another player from your island.\n" +
				"This applies only to people who were invited; neither " +
				"visitors nor guests are included.  \n" +
				"You must be owner to use this command.  Attempting to " +
				"use this with no party results in a " + 
				"sarcastic comment.  Usage:\n" +
				"§6/island remove <player>§r: Removes <player>.\n" +
				"Aliases: \n· §6/island kick§f.",
				//Perm
				"usb.party.kick," +
				"_is_owner" + 
				"_has_party",
				//Conf
				""
		});
		map.put("kick", new String[]{
				//Help
				"Forcibly removes another player from your island.\n" +
				"This applies only to people who were invited; neither " +
				"visitors nor guests are included.  \n" +
				"You must be owner to use this command.  Attempting to " +
				"use this with no party results in a " + 
				"sarcastic comment.  Usage:\n" +
				"§6/island kick <player>§r: Removes <player>.\n" +
				"Aliases: \n· §6/island remove§f.",
				//Perm
				"usb.party.kick," +
				"_is_owner" + 
				"_has_party",
				//Conf
				""
		});
		map.put("makeleader", new String[]{
				//Help
				"Transfers ownership of your island to another player.\n" +
				"§lRequirements:§r\n" +
				"  · You must be the current owner\n" +
				"  · The other player must be a member of your island\n" +
				"  · There must be no other players in your island party" +
				"\nUsage:\n§6/island makeleader <player>§r: " +
				"Makes <player> the leader of your island.",
				//Perm
				"usb.party.makeleader," +
				"_has_party," +
				"_is_owner",
				//Conf
				""
		});
		map.put("checkparty", new String[]{
				//Help
				"Provides a list of player in another player's party.\n" +
				"Usage:\n§6/island checkparty <player>§r: Lists " +
				"players in <player>'s party.\n" +
				"NOTE: There is a similar, but disabled, command: " +
				"§6/island partylist§f.  That cannot be used.", 
				//Perm
				"usb.mod.party",
				//Conf
				""
		});
		//And overridden, custom commands.
		map.put("help2", new String[]{});
		
		
		subCommands = Collections.unmodifiableMap(map);
	}
	
	private static final String[] rootHelp = {
		"Start your island, or teleport back to one you have.\n" + 
		"§fUsage: \n§6/island§f.",
		"usb.island.create"
	};
	
	/**
	 * Tab-completion.  This wasn't part of the vanilla system!
	 * @param sender
	 * @param cmd
	 * @param label
	 * @param args
	 * @return
	 */
	public List<String> onTabComplete(CommandSender sender, Command cmd, 
			String label, String args[]) {
		//TODO
		return new ArrayList<String>(subCommands.keySet());
	}
	
	/**
	 * Sends a message about the existence of help2.
	 * 
	 * @param sender
	 */
	public void alertAboutNewHelp(CommandSender sender) {
		sender.sendMessage("§2[SBE]§f: §aFor a nicer help system, do " + 
				"§e/island help2§a.");
	}
	
	/**
	 * The error message for when no help available.
	 */
	private static final String nonexistantHelpMessage = 
			"§c: " + "There is no help for this subcommand.  It may be " + 
			"an invalid command, or it may be that help has not been " +
			"written.  Note: If you are including a parameter, don't do " +
			"that.  Try removing each parameter, one at a time.  ";
	
	/**
	 * Alternative help system.
	 * 
	 * @param sender
	 * @param cmd
	 * @param label
	 * @param args
	 */
	public void help2(CommandSender sender, Command cmd, 
			String label, String args[]) {
		if (args.length == 0) {
			//Shouldn't happen.
			return;
		}
		String[] helpArgs = Arrays.copyOfRange(args, 1, args.length);
		if (helpArgs.length == 0) {
			//Message prepended to each message.
			final String preface = getColorPreface(rootHelp, sender, cmd,
					label, args);
			
			String value_main;
			if (rootHelp.length != 0) {
				value_main = rootHelp[0];
			} else {
				value_main = "";
			}
			
			sender.sendMessage(getColorPreface(rootHelp, 
					sender, cmd, preface, args) + "§f: " + 
					value_main);
			
			
			
			for (Map.Entry<String, String[]> entry : subCommands.entrySet()) {
				String value;
				if (entry.getValue().length != 0) {
					value = entry.getValue()[0];
				} else {
					value = "";
				}
				sender.sendMessage(trailOff(preface + entry.getKey() + 
						"§f:" + value));
			}
			return;
		}
		if (helpArgs.length == 1) {
			//Message prepended to each message.
			final String preface = helpArgs[0];
			
			//If there is no help message...
			if (!subCommands.containsKey(helpArgs[0])) {
				sender.sendMessage(preface + nonexistantHelpMessage);
				return;
			}
		    
			String value;
			if (subCommands.get(helpArgs[0]).length != 0) {
				value = subCommands.get(helpArgs[0])[0];
			} else {
				value = "";
			}
			
			//Send the root message if it exists
			sender.sendMessage(getColorPreface(subCommands.get(helpArgs[0]), 
					sender, cmd, preface, args) + "§f: " + 
					value);
			return;
		}
		sender.sendMessage(nonexistantHelpMessage);
	}
	
	/**
	 * Gets the preface for the specific HelpData, including 
	 * processing whether the command is accessible.
	 * 
	 * This uses color green (§a) for available, red for unavailable (§c),
	 * and gray (§7) for commands not enabled (via config).
	 * @param helpData
	 * @param sender
	 * @param cmd
	 * @param label
	 * @param args
	 * @return
	 * @throws IllegalArgumentException when PlayerInfo can't be obtained.
	 */
	private String getColorPreface(String[] helpData,
			CommandSender sender, Command cmd, 
			String label, String args[]) throws IllegalArgumentException {
		
		String commandText = "/" + label + " ";
		
		if (!(sender instanceof Player)) {
			return "§c" + commandText;
		}
		Player player = (Player) sender;
		PlayerInfo playerInfo = IslandUtils.getPlayerInfo(player);
		if (playerInfo == null) {
			throw new IllegalArgumentException("§cCouldn't get player " +
					"info for " + player.toString() + "!");
		}
		
		//Permission tests.
		if (helpData.length >= 2) {
			loop: //Goto-like label, to make actions logical.
			for (String perm : helpData[1].split(",")) {
				if (perm.startsWith("_")) {
					perm = perm.substring(1);
					switch (perm.toLowerCase(Locale.ENGLISH)) {
					case "has_island": {
						if (!playerInfo.getHasIsland()) {
							return "§c" + commandText; 
						}
						continue loop;
					}
					case "!has_island": {
						if (playerInfo.getHasIsland()) {
							return "§c" + commandText; 
						}
						continue loop;
					}
					case "has_party": {
						if (!playerInfo.getHasParty()) {
							return "§c" + commandText; 
						}
						continue loop;
					}
					case "!has_party": {
						if (playerInfo.getHasParty()) {
							return "§c" + commandText; 
						}
						continue loop;
					}
					case "is_owner": {
						if (!playerInfo.getPartyLeader()
								.equalsIgnoreCase(sender.getName())) {
							return "§c" + commandText; 
						}
						continue loop;
					}
					case "!is_owner": {
						if (playerInfo.getPartyLeader()
								.equalsIgnoreCase(sender.getName())) {
							return "§c" + commandText; 
						}
						continue loop;
					}
					case "is_member": {
						//TODO
						continue loop;
					}
					case "!is_member": {
						//TODO
						continue loop;
					}
					case "has_invite": {
						if (!getInviteList().containsKey(sender.getName())) {
							return "§c" + commandText;
						}
						continue loop;
					}
					case "!has_invite": {
						if (getInviteList().containsKey(sender.getName())) {
							return "§c" + commandText;
						}
						continue loop;
					}
					case "on_skyblock_world": {
						if (!uSkyBlock.getSkyBlockWorld().equals(
								player.getWorld())) {
							return "§c" + commandText;
						}
						continue loop;
					}
					case "!on_skyblock_world": {
						if (uSkyBlock.getSkyBlockWorld().equals(
								player.getWorld())) {
							return "§c" + commandText;
						}
						continue loop;
					}
					case "on_own_island": {
						if (!uSkyBlock.getInstance()
								.playerIsOnIsland(player)) {
							return "§c" + commandText;
						}
						continue loop;
					}
					case "!on_own_island": {
						if (uSkyBlock.getInstance()
								.playerIsOnIsland(player)) {
							return "§c" + commandText;
						}
						continue loop;
					}
					case "on_ultimateskyblock": {
						if (!Bukkit.getServer().getServerId()
								.equalsIgnoreCase("UltimateSkyblock")) {
							return "§c" + commandText;
						}
						continue loop;
					}
					case "!on_ultimateskyblock": {
						if (Bukkit.getServer().getServerId()
								.equalsIgnoreCase("UltimateSkyblock")) {
							return "§c" + commandText;
						}
						continue loop;
					}
					case "false": {
						return "§7" + commandText;
					}
					case "!false": {
						//Seems utterly pointless
						continue loop;
					}
					default: {
						ErrorHandler.logError(
								new GenericReport("Failed to process " +
										"help value for /" + label + 
										" with args " + Arrays
										.toString(args) + ": Unknown " +
										"requirement " + perm + " " +
										" (HelpData: " + Arrays
										.toString(helpData) + "."));
					}
					}
					continue;
				} else {
					if (!sender.hasPermission(perm)) {
						return "§c" + commandText;
					}
					continue;
				}
			}
		}
		
		//Config tests.

		if (helpData.length >= 3) {
			loop: 
			for (String val : helpData[2].split(",")) {
				String[] subVals = val.split(".", 2);
				if (subVals.length != 2) {
					ErrorHandler.logError(
							new GenericReport("Failed to process help " +
									"value for /" + label + " with args " +
									Arrays.toString(args) + ": Spliting " +
									val + " with '.' produced an array " +
									" of length other than 2 (" + subVals
									.length + "): " + Arrays.toString(
											subVals) + "."));
					continue;
				}
				String[] confVals = subVals[1].split("=", 2);
				if (confVals.length != 2) {
					ErrorHandler.logError(
							new GenericReport("Failed to process help " +
									"value for /" + label + " with args " +
									Arrays.toString(args) + ": Spliting " +
									subVals[0] + " with '=' produced an " +
									"array of length other than 2 (" + 
									confVals.length + "): " + Arrays
									.toString(confVals) + ".  This " +
									"is part of " + Arrays.toString(
											subVals) + "."));
					continue;
				}
				switch (subVals[0].toLowerCase(Locale.ENGLISH)) {
				case "sbe": {
					if (SkyblockExtension.inst().getConfig()
							.get(confVals[0], "null").toString()
							.equalsIgnoreCase(confVals[1])) {
						return "§7" + commandText;
					}
					continue loop;
				}
				case "uskyblock": {
					if (uSkyBlock.getInstance().getConfig()
							.get(confVals[0], "null").toString()
							.equalsIgnoreCase(confVals[1])) {
						return "§7" + commandText;
					}
					continue loop;
				}
				default: {
					ErrorHandler.logError(
							new GenericReport("Failed to process help " +
									"value for /" + label +" with args " +
									Arrays.toString(args) + ": Unknown " +
									"configuration file " + subVals[0] +
									" (of " + Arrays.toString(subVals) +
									")."));
				}
				}
			}
		}
		
		return "§a" + commandText;
	}
	
	/**
	 * Gets the invite list, which is private and must be obtained 
	 * with reflection.
	 * @return
	 * @throws RuntimeException when any exception is thrown, eg 
	 * SecurityException.
	 */
	@SuppressWarnings("unchecked")
	protected HashMap<String, String> getInviteList() 
			throws RuntimeException {
		try {
		return (HashMap<String, String>) super.getClass()
				.getDeclaredField("inviteList").get(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
