package pokechu22.plugins.SkyblockExtension.testutil;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 * Contains useful methods for generating mock events.
 * Specifically, gets players.
 * 
 * @author Pokechu22
 *
 */
public class MockEventUtil {
	/**
	 * Gets a mock owner at the specified location.
	 * This player has bypassprotection disabled, and is the owner of
	 * the island.
	 * 
	 * @param location
	 * @return
	 */
	public static Player getMockOwner(Location location) {
		Player mockPlayer = mock(Player.class);
		when(mockPlayer.getLocation()).thenReturn(location);
		
		when(mockPlayer.getName()).thenReturn("owner");
		when(mockPlayer.getUniqueId()).thenReturn(new UUID(0, 0));
		when(mockPlayer.hasPermission("usb.mod.bypassprotection")).thenReturn(false);
		
		return mockPlayer;
	}
	
	/**
	 * Gets a mock member at the specified location.
	 * This player has bypassprotection disabled, and is a member of
	 * the island.
	 * 
	 * @param location
	 * @return
	 */
	public static Player getMockMember(Location location) {
		Player mockPlayer = mock(Player.class);
		when(mockPlayer.getLocation()).thenReturn(location);
		
		when(mockPlayer.getName()).thenReturn("member");
		when(mockPlayer.getUniqueId()).thenReturn(new UUID(0, 1));
		when(mockPlayer.hasPermission("usb.mod.bypassprotection")).thenReturn(false);
		
		return mockPlayer;
	}
	
	/**
	 * Gets a mock guest at the specified location.
	 * This player has bypassprotection disabled, and is a guest of
	 * the island.
	 * 
	 * @param location
	 * @return
	 */
	public static Player getMockGuest(Location location) {
		Player mockPlayer = mock(Player.class);
		when(mockPlayer.getLocation()).thenReturn(location);
		
		when(mockPlayer.getName()).thenReturn("guest");
		when(mockPlayer.getUniqueId()).thenReturn(new UUID(0, 2));
		when(mockPlayer.hasPermission("usb.mod.bypassprotection")).thenReturn(false);
		
		return mockPlayer;
	}
	
	/**
	 * Gets a mock nobody at the specified location.
	 * This player has bypassprotection disabled, and is not a member
	 * of the island.
	 * 
	 * @param location
	 * @return
	 */
	public static Player getMockNobody(Location location) {
		Player mockPlayer = mock(Player.class);
		when(mockPlayer.getLocation()).thenReturn(location);
		
		when(mockPlayer.getName()).thenReturn("nobody");
		when(mockPlayer.getUniqueId()).thenReturn(new UUID(0, 3));
		when(mockPlayer.hasPermission("usb.mod.bypassprotection")).thenReturn(false);
		
		return mockPlayer;
	}
	
	/**
	 * Gets a mock admin at the specified location.
	 * This player has bypassprotection enabled, but is not a member of
	 * the island.
	 * 
	 * @param location
	 * @return
	 */
	public static Player getMockAdmin(Location location) {
		Player mockPlayer = mock(Player.class);
		when(mockPlayer.getLocation()).thenReturn(location);
		
		when(mockPlayer.getName()).thenReturn("admin");
		when(mockPlayer.getUniqueId()).thenReturn(new UUID(0xFFFFFFFFFFFFFFFFL, 
				0xFFFFFFFFFFFFFFFFL));
		when(mockPlayer.hasPermission("usb.mod.bypassprotection")).thenReturn(true);
		
		return mockPlayer;
	}
	
	/**
	 * Gets a mock entity at the specified location.
	 * 
	 * @param location
	 * @return
	 */
	public static Entity getMockEntity(Location location, EntityType type) {
		Entity mockEntity = mock(Entity.class);
		when(mockEntity.getLocation()).thenReturn(location);
		when(mockEntity.getType()).thenReturn(type);
		
		return mockEntity;
	}
}
