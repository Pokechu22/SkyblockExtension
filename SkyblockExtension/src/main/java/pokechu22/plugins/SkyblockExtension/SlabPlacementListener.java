package pokechu22.plugins.SkyblockExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.wasteofplastic.askyblock.events.ChallengeCompleteEvent;

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
	 * Replaces placed slabs with the proper variants.
	 * 
	 * @param e
	 */
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.LOWEST)
	public void onBlockPlace(BlockPlaceEvent e) {
		if (enabled) {
			if (e.getBlockPlaced().getTypeId() == HALF_STONE_SLAB_1_ID){
				if (e.getItemInHand().getData().getData() >= 8) {
					e.getBlockPlaced().setTypeId(FULL_STONE_SLAB_1_ID);
				}
			}
			if (e.getBlockPlaced().getTypeId() == HALF_STONE_SLAB_2_ID){
				if (e.getItemInHand().getData().getData() >= 8) {
					e.getBlockPlaced().setTypeId(FULL_STONE_SLAB_2_ID);
				}
			}
		}
	}
	
	/**
	 * Replaces slabs with proper variants in challenge rewards.
	 * <br/>
	 * Note that this requires ChallengeCompleteEvent to have already
	 * given the reward items before the event fires -- as of right now
	 * ASkyBlock does this but it may change in the future.
	 *  
	 * @param e
	 */
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.LOWEST)
	public void onChallengeComplete(ChallengeCompleteEvent e) {
		ItemFactory itemFactory = Bukkit.getItemFactory();
		
		HashMap<Integer, ItemStack> items = new HashMap<>();
		
		items.putAll(e.getPlayer().getInventory().all(FULL_STONE_SLAB_1_ID));
		items.putAll(e.getPlayer().getInventory().all(FULL_STONE_SLAB_2_ID));
		items.putAll(e.getPlayer().getInventory().all(HALF_STONE_SLAB_1_ID));
		items.putAll(e.getPlayer().getInventory().all(HALF_STONE_SLAB_2_ID));
		
		for (Map.Entry<Integer, ItemStack> item : items.entrySet()) {
			ItemStack stack = item.getValue();
			
			//Replace any invalid slabs with the valid counterparts.
			if (stack.getTypeId() == FULL_STONE_SLAB_1_ID) {
				stack.setTypeId(HALF_STONE_SLAB_1_ID);
			}
			if (stack.getTypeId() == FULL_STONE_SLAB_2_ID) {
				stack.setTypeId(HALF_STONE_SLAB_2_ID);
			}
			
			if (stack.getTypeId() == HALF_STONE_SLAB_1_ID) {
				ItemMeta meta;
				if (stack.hasItemMeta()) {
					meta = stack.getItemMeta();
				} else {
					meta = itemFactory.getItemMeta(stack.getType());
				}
				if (meta.hasDisplayName()) {
					continue; //Abort updating the name.
				}
				switch (stack.getData().getData()) {
				case 2: meta.setDisplayName(
						"§rStone Wood Half Slab");
					break;
				case 8:
					meta.setDisplayName("§rSuper Stone! " + 
						"(Seamless Double Smooth Stone Half Slab)");
					break;
				case 9:
					meta.setDisplayName(
						"§rSeamless Double Sandstone Half Slab");
					break;
				case 10: meta.setDisplayName(
						"§rDouble Stone Wood Half Slab");
					break;
				}
				
				meta.setLore(Arrays.asList(
						"This is a special block that",
						"1.8 doesn't like.  Sorry :/",
						"But at least it works.",
						"--Pokechu22"));
				
				stack.setItemMeta(meta);
			}
			if (stack.getTypeId() == HALF_STONE_SLAB_2_ID) {
				ItemMeta meta;
				if (stack.hasItemMeta()) {
					meta = stack.getItemMeta();
				} else {
					meta = itemFactory.getItemMeta(stack.getType());
				}
				if (meta.hasDisplayName()) {
					continue; //Abort updating the name.
				}
				switch (stack.getData().getData()) {
				case 8:
					meta.setDisplayName(
							"§rSeamless Double Red Sandstone Half Slab");
					break;
				}
				
				meta.setLore(Arrays.asList(
						"This is a special block that",
						"1.8 doesn't like.  Sorry :/",
						"But at least it works.",
						"--Pokechu22"));
				
				stack.setItemMeta(meta);
			}
		}
		
		//Ensure that the inventory is updated (may be unneeded)
		for (Map.Entry<Integer, ItemStack> item : items.entrySet()) {
			e.getPlayer().getInventory().setItem(item.getKey(), 
					item.getValue());
		}
	}
}
