package pokechu22.plugins.SkyblockExtension;

import org.bukkit.plugin.java.JavaPlugin;

/**\
 * 
 * @author Pokechu22
 *
 */
public class SkyblockExtension extends JavaPlugin {
	
	/**\
	 * The instance of this plugin.
	 * 
	 * Private so that (at least I asume; this is from the bukkit tutorial) it is not overwritable.
	 */
	private static SkyblockExtension inst;
	
	/**\
	 * Gets the instance of this plugin.
	 * @return The instance of this plugin.
	 */
	public static SkyblockExtension inst() {
		return inst;
	}
	
}
