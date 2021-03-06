package pokechu22.plugins.SkyblockExtension;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;

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
		WitherWarner.enabled = true;
		
		Player player = mock(Player.class);
		when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
		
		// SKULL_ITEM:1, AKA wither skull.
		ItemStack stack = new ItemStack(Material.SKULL_ITEM, 64, 
				(short)SkullType.WITHER.ordinal());
		
		//The placed event used here.
		BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
				stack, player, true);
		
		WitherWarner w = new WitherWarner();
		w.onWitherSkullPlaced(e);
		
		verify(player, times(1)).sendMessage(warningText);
		verify(player, times(1)).sendMessage(optOutText);
	}
	
	/**
	 * Checks whether placement of OTHER items does not give a message.
	 */
	@Test
	public void otherPlacementsShouldNotGiveMessages() {
		WitherWarner.enabled = true;
		
		//Check other types of skulls.
		for (SkullType type : SkullType.values()) {
			if (type == SkullType.WITHER) {
				continue; //Skip wither.
			}
			
			Player player = mock(Player.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
			
			ItemStack stack = new ItemStack(Material.SKULL_ITEM, 64, 
					(short)type.ordinal());
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			verify(player, never()).sendMessage(warningText);
			verify(player, never()).sendMessage(optOutText);
		}
		
		//Check a few other items.
		//Dirt
		{
			Player player = mock(Player.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
			
			ItemStack stack = new ItemStack(Material.DIRT, 64);
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			verify(player, never()).sendMessage(warningText);
			verify(player, never()).sendMessage(optOutText);
		}
		//Flint and steel
		{
			Player player = mock(Player.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
			
			ItemStack stack = new ItemStack(Material.FLINT_AND_STEEL, 64);
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			verify(player, never()).sendMessage(warningText);
			verify(player, never()).sendMessage(optOutText);
		}
		//Flowing Lava block
		{
			Player player = mock(Player.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
			
			ItemStack stack = new ItemStack(Material.LAVA, 64);
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			verify(player, never()).sendMessage(warningText);
			verify(player, never()).sendMessage(optOutText);
		}
		//Stationary lava block
		{
			Player player = mock(Player.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
			
			ItemStack stack = new ItemStack(Material.STATIONARY_LAVA, 64);
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			verify(player, never()).sendMessage(warningText);
			verify(player, never()).sendMessage(optOutText);
		}
		//Air
		{
			Player player = mock(Player.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
			
			ItemStack stack = new ItemStack(Material.AIR, 64);
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			verify(player, never()).sendMessage(warningText);
			verify(player, never()).sendMessage(optOutText);
		}
		//Dirt with data value of that of a wither skull
		{
			Player player = mock(Player.class);  
			when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
			
			ItemStack stack = new ItemStack(Material.LAVA, 64,
					(short)SkullType.WITHER.ordinal());
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			verify(player, never()).sendMessage(warningText);
			verify(player, never()).sendMessage(optOutText);
		}
	}
	
	/**
	 * Tests that the WitherWarner does not give a message when disabled.
	 * This tests 4 ways: 
	 * <ul>
	 * <li>With a wither skull</li>
	 * <li>With a normal skeleton skull</li>
	 * <li>With a block of dirt</li>
	 * <li>With a block of dirt, with DV of that of a wither skull</li>
	 * </ul>
	 */
	@Test
	public void disabledWitherWarnerShouldNotDisplayMessages() {
		WitherWarner.enabled = false;
		{
			Player player = mock(Player.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
			
			// SKULL_ITEM:1, AKA wither skull.
			ItemStack stack = new ItemStack(Material.SKULL_ITEM, 64, 
					(short)SkullType.WITHER.ordinal());
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			verify(player, never()).sendMessage(warningText);
			verify(player, never()).sendMessage(optOutText);
		}
		{
			Player player = mock(Player.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
			
			// Regular skeleton skull
			ItemStack stack = new ItemStack(Material.SKULL_ITEM, 64, 
					(short)SkullType.SKELETON.ordinal());
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			verify(player, never()).sendMessage(warningText);
			verify(player, never()).sendMessage(optOutText);
		}
		{
			Player player = mock(Player.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
			
			// Regular dirt
			ItemStack stack = new ItemStack(Material.DIRT, 64);
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			verify(player, never()).sendMessage(warningText);
			verify(player, never()).sendMessage(optOutText);
		}
		{
			Player player = mock(Player.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 0L));
			
			// Dirt with DV of wither skull.
			ItemStack stack = new ItemStack(Material.DIRT, 64, 
					(short)SkullType.WITHER.ordinal());
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			verify(player, never()).sendMessage(warningText);
			verify(player, never()).sendMessage(optOutText);
		}
		WitherWarner.enabled = true;
	}
	
	/**
	 * Tests that opted out players do not receive the message, 
	 * both with the WitherWarner enabled and disabled.
	 * 
	 * (Player with UUID of 1 is opted out)
	 */
	@Test
	public void optedOutPlayersShouldNotReceiveMessages() {
		WitherWarner.enabled = false;
		
		{
			Player player = mock(Player.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 1L));
			
			// SKULL_ITEM:1, AKA wither skull.
			ItemStack stack = new ItemStack(Material.SKULL_ITEM, 64, 
					(short)SkullType.WITHER.ordinal());
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			verify(player, never()).sendMessage(warningText);
			verify(player, never()).sendMessage(optOutText);
		}
		{
			Player player = mock(Player.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 1L));
			
			// Regular skeleton skull
			ItemStack stack = new ItemStack(Material.SKULL_ITEM, 64, 
					(short)SkullType.SKELETON.ordinal());
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			verify(player, never()).sendMessage(warningText);
			verify(player, never()).sendMessage(optOutText);
		}
		{
			Player player = mock(Player.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 1L));
			
			// Regular dirt
			ItemStack stack = new ItemStack(Material.DIRT, 64);
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			verify(player, never()).sendMessage(warningText);
			verify(player, never()).sendMessage(optOutText);
		}
		{
			Player player = mock(Player.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 1L));
			
			// Dirt with DV of wither skull.
			ItemStack stack = new ItemStack(Material.DIRT, 64, 
					(short)SkullType.WITHER.ordinal());
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			verify(player, never()).sendMessage(warningText);
			verify(player, never()).sendMessage(optOutText);
		}
		
		WitherWarner.enabled = true;
		{
			Player player = mock(Player.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 1L));
			
			// SKULL_ITEM:1, AKA wither skull.
			ItemStack stack = new ItemStack(Material.SKULL_ITEM, 64, 
					(short)SkullType.WITHER.ordinal());
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			verify(player, never()).sendMessage(warningText);
			verify(player, never()).sendMessage(optOutText);
		}
		{
			Player player = mock(Player.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 1L));
			
			// Regular skeleton skull
			ItemStack stack = new ItemStack(Material.SKULL_ITEM, 64, 
					(short)SkullType.SKELETON.ordinal());
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			verify(player, never()).sendMessage(warningText);
			verify(player, never()).sendMessage(optOutText);
		}
		{
			Player player = mock(Player.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 1L));
			
			// Regular dirt
			ItemStack stack = new ItemStack(Material.DIRT, 64);
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			verify(player, never()).sendMessage(warningText);
			verify(player, never()).sendMessage(optOutText);
		}
		{
			Player player = mock(Player.class);
			when(player.getUniqueId()).thenReturn(new UUID(0L, 1L));
			
			// Dirt with DV of wither skull.
			ItemStack stack = new ItemStack(Material.DIRT, 64, 
					(short)SkullType.WITHER.ordinal());
			
			//The placed event used here.
			BlockPlaceEvent e = new BlockPlaceEvent(null, null, null,
					stack, player, true);
			
			WitherWarner w = new WitherWarner();
			w.onWitherSkullPlaced(e);
			
			verify(player, never()).sendMessage(warningText);
			verify(player, never()).sendMessage(optOutText);
		}
	}
}
