package pokechu22.plugins.SkyblockExtension.protection;

import java.util.ArrayList;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredListener;

import us.talabrek.ultimateskyblock.ProtectionEvents;
import us.talabrek.ultimateskyblock.uSkyBlock;

/**
 * Handles overriding any existing uSkyBlock protections, and new implementations
 * of the previous ones.
 * 
 * @author Pokechu22
 * @author Talabrek
 * @author wolfwork
 */
public class USkyBlockProtectionListener extends ProtectionEvents implements Listener {
	/**
	 * Constructor - Also removes the original ProtectionEvents.
	 */
	public USkyBlockProtectionListener() {
		removeExistingProtectionEvents();
	}
	
	/**
	 * Destroys the original version added by uSkyBlock.
	 */
	public static void removeExistingProtectionEvents() {
		ArrayList<RegisteredListener> uSkyBlockListeners = 
				HandlerList.getRegisteredListeners(uSkyBlock.getInstance());
		
		for (RegisteredListener registeredListener : uSkyBlockListeners) {
			Listener listener = registeredListener.getListener();
			
			if (listener instanceof ProtectionEvents) {
				HandlerList.unregisterAll(listener);
			}
		}
	}
	
	
}
