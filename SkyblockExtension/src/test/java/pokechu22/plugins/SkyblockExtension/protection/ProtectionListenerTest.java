package pokechu22.plugins.SkyblockExtension.protection;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import static pokechu22.plugins.SkyblockExtension.testutil.MockEventUtil.*;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
	 * Tests the 
	 * {@link ProtectionListener#onEntityInteract(PlayerInteractEntityEvent)}
	 * method.
	 * @throws InvalidConfigurationException 
	 */
	@Test
	public void onEntityInteractTest() throws InvalidConfigurationException {
		IslandProtectionDataSetFactory.init();
		
		Entity mockEntity = getMockEntity(new Location(null, 0, 0, 0), EntityType.SHEEP);
		
		ProtectionListener l = new ProtectionListener();
		
		PlayerInteractEntityEvent event;
		
		//Nobody
		event = new PlayerInteractEntityEvent(getMockNobody(
				new Location(null, 0, 0, 100)), mockEntity);
		event.setCancelled(false);
		l.onEntityInteract(event);
		assertThat(event.isCancelled(), is(true));
		
		//Guest
		event = new PlayerInteractEntityEvent(getMockGuest(
				new Location(null, 0, 0, 100)), mockEntity);
		event.setCancelled(false);
		l.onEntityInteract(event);
		assertThat(event.isCancelled(), is(true));
		
		//Member
		event = new PlayerInteractEntityEvent(getMockMember(
				new Location(null, 0, 0, 100)), mockEntity);
		event.setCancelled(false);
		l.onEntityInteract(event);
		assertThat(event.isCancelled(), is(false));
		
		//Owner
		event = new PlayerInteractEntityEvent(getMockOwner(
				new Location(null, 0, 0, 100)), mockEntity);
		event.setCancelled(false);
		l.onEntityInteract(event);
		assertThat(event.isCancelled(), is(false));
		
		//Nobody
		event = new PlayerInteractEntityEvent(getMockAdmin(
				new Location(null, 0, 0, 100)), mockEntity);
		event.setCancelled(false);
		l.onEntityInteract(event);
		assertThat(event.isCancelled(), is(false));
	}
}
