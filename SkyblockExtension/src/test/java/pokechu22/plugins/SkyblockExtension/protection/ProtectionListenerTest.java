package pokechu22.plugins.SkyblockExtension.protection;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.junit.Test;

/**
 * Tests the {@link ProtectionListener}.
 * 
 * @author Pokechu22
 *
 */
public class ProtectionListenerTest {
	/**
	 * Gets a mock player at the specified location.
	 * Also specifies permissions and stuff like that.
	 * 
	 * @param location
	 * @return
	 */
	private Player getMockPlayer(Location location) {
		Player mockPlayer = mock(Player.class);
		when(mockPlayer.getLocation()).thenReturn(location);
		
		when(mockPlayer.getName()).thenReturn("owner");
		when(mockPlayer.getUniqueId()).thenReturn(new UUID(0, 0)); 
		when(mockPlayer.hasPermission("usb.mod.bypassprotection")).thenReturn(false);
		
		return mockPlayer;
	}
	
	/**
	 * Gets a mock entity at the specified location.
	 * 
	 * @param location
	 * @return
	 */
	private Entity getMockEntity(Location location) {
		Entity mockEntity = mock(Entity.class);
		when(mockEntity.getLocation()).thenReturn(location);
		
		return mockEntity;
	}
	
	/**
	 * Tests the 
	 * {@link ProtectionListener#onEntityInteract(PlayerInteractEntityEvent)}
	 * method.
	 */
	@Test
	public void onEntityInteractTest() {
		Player mockPlayer = getMockPlayer(new Location(null, 0, 0, 100));
		
		Entity mockEntity = getMockEntity(new Location(null, 0, 0, 0));
		
		ProtectionListener l = new ProtectionListener();
		
		PlayerInteractEntityEvent event = new PlayerInteractEntityEvent(
				mockPlayer, mockEntity);
		event.setCancelled(false);
		
		l.onEntityInteract(event);
		
		assertThat(event.isCancelled(), is(true));
	}
}
