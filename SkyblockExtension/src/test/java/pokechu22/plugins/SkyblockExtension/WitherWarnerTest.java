package pokechu22.plugins.SkyblockExtension;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static pokechu22.plugins.SkyblockExtension.testutil.MockEventUtil.*;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;

import pokechu22.plugins.SkyblockExtension.testutil.ChatLoggedPlayer;

/**
 * Tests the WitherWarner.
 * 
 * Probably unneeded, but still.
 * 
 * @author Pokechu22
 *
 */
public class WitherWarnerTest {
	
	private static final String warningText = "Warning text";
	private static final String optOutText = "Opt out text";
	
	/**
	 * Fake-initializes WitherWarner.
	 */
	static {
		//The only opted out player has a UUID of 1.
		WitherWarner.optedOutPlayers = new ArrayList<UUID>();
		WitherWarner.optedOutPlayers.add(new UUID(0L, 1L));
		
		WitherWarner.enabled = true;
		
		WitherWarner.warningText = warningText;
		WitherWarner.optOutText = optOutText;
	}
	
	/**
	 * Checks whether skull placement actually gives a message.
	 */
	@Test
	public void witherSkullPlacementShouldGiveMessages() {
		ChatLoggedPlayer player = mock(ChatLoggedPlayer.class);
		when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
		
		// SKULL_ITEM:1, AKA wither skull.
		ItemStack stack = new ItemStack(Material.SKULL_ITEM, 64, 
				(short)SkullType.WITHER.ordinal());
		
		//The placed event used here.
		BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
				stack, player, true);
		
		WitherWarner w = new WitherWarner();
		w.onWitherSkullPlaced(e);
		
		assertThat(player.messageQueue.size(), is(2));
		
		assertThat(player.messageQueue.peek(), is(warningText));
		player.messageQueue.remove();
		assertThat(player.messageQueue.peek(), is(optOutText));
		player.messageQueue.remove();
		
		assertTrue(player.messageQueue.isEmpty());
	}
	
	/**
	 * Checks whether placement of OTHER items does not give a message.
	 */
	@Test
	public void otherPlacementsShouldNotGiveMessages() {
		//Check other types of skulls.
		for (SkullType type : SkullType.values()) {
			if (type == SkullType.WITHER) {
				continue; //Skip wither.
			}
			
			ChatLoggedPlayer player = mock(ChatLoggedPlayer.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
			
			ItemStack stack = new ItemStack(Material.SKULL_ITEM, 64, 
					(short)type.ordinal());
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			assertTrue(player.messageQueue.isEmpty());
		}
		
		//Check a few other items.
		//Dirt
		{
			ChatLoggedPlayer player = mock(ChatLoggedPlayer.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
			
			ItemStack stack = new ItemStack(Material.DIRT, 64);
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			assertTrue(player.messageQueue.isEmpty());
		}
		//Flint and steel
		{
			ChatLoggedPlayer player = mock(ChatLoggedPlayer.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
			
			ItemStack stack = new ItemStack(Material.FLINT_AND_STEEL, 64);
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			assertTrue(player.messageQueue.isEmpty());
		}
		//Flowing Lava block
		{
			ChatLoggedPlayer player = mock(ChatLoggedPlayer.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
			
			ItemStack stack = new ItemStack(Material.LAVA, 64);
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			assertTrue(player.messageQueue.isEmpty());
		}
		//Stationary lava block
		{
			ChatLoggedPlayer player = mock(ChatLoggedPlayer.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
			
			ItemStack stack = new ItemStack(Material.STATIONARY_LAVA, 64);
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			assertTrue(player.messageQueue.isEmpty());
		}
		//Air
		{
			ChatLoggedPlayer player = mock(ChatLoggedPlayer.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
			
			ItemStack stack = new ItemStack(Material.AIR, 64);
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			assertTrue(player.messageQueue.isEmpty());
		}
		//Dirt with data value of that of a wither skull
		{
			ChatLoggedPlayer player = mock(ChatLoggedPlayer.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
			
			ItemStack stack = new ItemStack(Material.LAVA, 64,
					(short)SkullType.WITHER.ordinal());
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			assertTrue(player.messageQueue.isEmpty());
		}
	}
}
