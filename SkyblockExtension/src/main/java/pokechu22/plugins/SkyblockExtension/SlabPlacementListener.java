package pokechu22.plugins.SkyblockExtension;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Listens for the placement of certain blocks, and handles their behavior.
 * <br/>
 * Specifically: 
 * <br/><br/>
 * <table>
 * <tr><th>Placed block ID and name</th><th>Used block ID and name</th></tr>
 * <tr><td><code>43:8</code> (Super stone) (Double smooth stone slab)</td>
 * <td><code>44:8</code> (Top smooth stone half slab, unused)</td></tr>
 * <tr><td><code>43:9</code> (Double smooth seamless sandstone slab)</td>
 * <td><code>44:9</code> (Top sandstone half slab, unused)</td></tr>
 * <tr><td><code>43:9</code> (Wood double stone slab (wat))</td>
 * <td><code>44:10</code> (Top wood stone slab (wat))</td></tr>
 * <tr>
 * <td><code>181:8</code> (Double smooth red seamless sandstone slab)</td>
 * <td><code>182:8</code> (Upper smooth red seamless sandstone slab)</td>
 * </tr>
 * </table>
 * <br/>
 * Technically, it actually works by changing the ID of any slab with a 
 * data value greater than 8.
 * 
 * @author Pokechu22
 *
 */
public class SlabPlacementListener implements Listener {
	/**
	 * Whether or not this functionality is enabled.
	 */
	public static boolean enabled = true;
	
	/**
	 * Numeric block ID for stone half slab 1.<br/>
	 * I'm using numeric IDs because I can't get bukkit 1.8 to work, and
	 * thus can't use 1.8 Materials.
	 */
	private static final int HALF_STONE_SLAB_1_ID = 44;
	/**
	 * Numeric block ID for double stone half slab 1.<br/>
	 * I'm using numeric IDs because I can't get bukkit 1.8 to work, and
	 * thus can't use 1.8 Materials.
	 */
	private static final int FULL_STONE_SLAB_1_ID = 43;
	/**
	 * Numeric block ID for stone half slab 2.<br/>
	 * I'm using numeric IDs because I can't get bukkit 1.8 to work, and
	 * thus can't use 1.8 Materials.
	 */
	private static final int HALF_STONE_SLAB_2_ID = 182;
	/**
	 * Numeric block ID for double stone half slab 2.<br/>
	 * I'm using numeric IDs because I can't get bukkit 1.8 to work, and
	 * thus can't use 1.8 Materials.
	 */
	private static final int FULL_STONE_SLAB_2_ID = 181;
	
	/**
	 * Replaces slabs with the proper variants.
	 * 
	 * @param e
	 */
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.LOWEST)
	public void onBlockPlace(BlockPlaceEvent e) {
		if (enabled) {
			if (e.getBlockPlaced().getTypeId() == HALF_STONE_SLAB_1_ID){
				if (e.getBlockPlaced().getData() >= 8) {
					e.getBlockPlaced().setTypeId(FULL_STONE_SLAB_1_ID);
				}
			}
			if (e.getBlockPlaced().getTypeId() == HALF_STONE_SLAB_2_ID){
				if (e.getBlockPlaced().getData() >= 8) {
					e.getBlockPlaced().setTypeId(FULL_STONE_SLAB_2_ID);
				}
			}
		}
	}
}
