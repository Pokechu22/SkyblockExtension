package pokechu22.plugins.SkyblockExtension.commands;

import org.bukkit.World;
import org.bukkit.entity.Player;

import us.talabrek.ultimateskyblock.IslandCommand;

/**
 * Intended to do the same thing as IslandCommand, but override some 
 * functionality.  It's Intended to be called in an 
 * {org.bukkit.event.player.PlayerCommandPreprocessEvent 
 * PlayerCommandPreprocessEvent}, and has a run method that takes the
 * needed parameters from that as well as the regular run from the 
 * supertype.
 *
 * @author Pokechu22
 *
 */
public class USkyBlockCommandIsland extends IslandCommand {
	public USkyBlockCommandIsland() {
		super();
	}
	
	/**
	 * Disabled.
	 */
	@Override
	public void generateIslandBlocks(int x, int z, Player player, World world) {
		return;
	}
	
	/**
	 * Disabled.
	 */
	@Override
	public void oldGenerateIslandBlocks(int x, int z, Player player, World world) {
		return;
	}
}
