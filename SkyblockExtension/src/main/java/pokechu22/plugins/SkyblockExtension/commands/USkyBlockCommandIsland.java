package pokechu22.plugins.SkyblockExtension.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import pokechu22.plugins.SkyblockExtension.protection.IslandInfo;
import pokechu22.plugins.SkyblockExtension.util.IslandUtils;
import us.talabrek.ultimateskyblock.IslandCommand;
import us.talabrek.ultimateskyblock.PlayerInfo;
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
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
	
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\
 * END OF USKYBLOCK CODE! NONE OF THIS IS PART OF THE ORIGIONAL COMMAND. *
\* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	
	public static boolean enableHelp2 = true;
	
	public static boolean allowIgnoreCase = true;
	
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
	 * <h2><code>on_ultimateskyblock</code></h2>
	 * You must be on the server <code>UltimateSkyblock</code>.
	 * I'm not kidding.  This is a real requirement in the plugin.
	 * 
	 * <h1>[2]:</h1> Required value in config.  The first value should be 
	 * either <code>sbe</code> or <code>uSkyBlock</code>, followed by the
	 * full config key path.  Or empty.  As with before, this can be 
	 * comma-separated and using an <code>!</code> will inverse it.
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
		map.put("restart", new String[]{});
		map.put("reset", new String[]{});
		map.put("sethome", new String[]{});
		map.put("tpset", new String[]{});
		map.put("lock", new String[]{});
		map.put("unlock", new String[]{});
		map.put("help", new String[]{});
		map.put("top", new String[]{});
		map.put("info", new String[]{});
		map.put("level", new String[]{});
		map.put("invite", new String[]{});
		map.put("accept", new String[]{});
		map.put("reject", new String[]{});
		//These all seem like disabled debug commands, but some work.
		map.put("partypurge", new String[]{});
		map.put("partyclean", new String[]{});
		map.put("purgeinvites", new String[]{});
		map.put("partylist", new String[]{});
		map.put("invitelist", new String[]{});
		//More normal commands.
		map.put("leave", new String[]{});
		map.put("party", new String[]{});
		//3-args commands, now.  Some are duplicates, which are commented out.
		//map.put("info", new String[]{});
		//map.put("level", new String[]{});
		//map.put("invite", new String[]{});
		map.put("remove", new String[]{});
		map.put("kick", new String[]{});
		map.put("makeleader", new String[]{});
		map.put("checkparty", new String[]{});
		//And overridden, custom commands.
		map.put("help2", new String[]{});
		
		
		subCommands = Collections.unmodifiableMap(map);
	}
	
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
	 * Alternative help system.
	 * 
	 * @param sender
	 * @param cmd
	 * @param label
	 * @param args
	 */
	public void help2(CommandSender sender, Command cmd, 
			String label, String args[]) {
		//TODO
	}
}
