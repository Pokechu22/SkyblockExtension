package pokechu22.plugins.SkyblockExtension.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pokechu22.plugins.SkyblockExtension.protection.IslandInfo;
import pokechu22.plugins.SkyblockExtension.util.IslandUtils;
import us.talabrek.ultimateskyblock.IslandCommand;
import us.talabrek.ultimateskyblock.PlayerInfo;
import us.talabrek.ultimateskyblock.VaultHandler;
import us.talabrek.ultimateskyblock.uSkyBlock;

/**
 * Intended to do the same thing as IslandCommand, but override some 
 * functionality.  It is intended to replace the vanilla command.
 *
 * @author Pokechu22
 *
 */
public class USkyBlockCommandIsland extends IslandCommand {
	public static void registerHooks() {
		uSkyBlock.getInstance().getCommand("island").setExecutor(
				new USkyBlockCommandIsland());
	}
	
	public USkyBlockCommandIsland() {
		super();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return super.onCommand(sender, command, label, args);
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
}
