package pokechu22.plugins.SkyblockExtension.protection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.jnbt.*;

/**
 * Contains information about an island.
 *
 * @author Pokechu22
 *
 */
public class IslandInfo {
	public Location islandCenter;
	
	public String owner;
	public UUID ownerUUID;
	
	public List<String> members;
	public List<UUID> memberUUIDs;
	
	public List<String> guests;
	public List<UUID> guestUUIDs;
	
	public Map<String, IslandProtectionDataSet> permissions;
	
	/**
	 * Creates an islandInfo with only the specified information.
	 * All other parameters are set at default.
	 *
	 * @param islandCenter
	 * @param owner
	 */
	public IslandInfo(Location islandCenter, Player owner) {
		this.islandCenter = islandCenter;
		this.owner = owner.getName();
		this.ownerUUID = owner.getUniqueId();
		
		this.members = new ArrayList<String>();
		this.memberUUIDs = new ArrayList<UUID>();
		
		this.guests = new ArrayList<String>();
		this.guestUUIDs = new ArrayList<UUID>();
		
		this.permissions = IslandProtectionDataSetFactory.getDefaultValues();
	}
	
	/**
	 * Sets a player as an owner.
	 * The existing owner is moved to members.
	 * 
	 * @param player The new owner.
	 */
	public void setOwner(Player player) {
		//If the player is a guest, remove them.
		guests.remove(player.getName());
		guestUUIDs.remove(player.getUniqueId());
		//If the player is a member, remove them.
		members.remove(player.getName());
		memberUUIDs.remove(player.getUniqueId());
		
		//If the owner is a guest, remove them.  (Shouldn't happen)
		guests.remove(owner);
		guestUUIDs.remove(ownerUUID);
		//If the player is a member, remove them.  (Shouldn't happen)
		members.remove(owner);
		memberUUIDs.remove(ownerUUID);
		
		//Add the previous owner to the members list.
		members.add(owner);
		memberUUIDs.add(ownerUUID);
		
		//Set the new owner.
		owner = player.getName();
		ownerUUID = player.getUniqueId();
	}
	
	/**
	 * Adds a player as a member.
	 *
	 * @param player
	 */
	public void addMember(Player player) {
		//If the player is a guest, remove them.
		guests.remove(player.getName());
		guestUUIDs.remove(player.getUniqueId());
		
		//If the player is an owner, do nothing.
		if (owner.equals(player.getName()) || ownerUUID.equals(player.getUniqueId())) {
			return;
		}
		
		//If the player is already a member, do nothing.
		if (members.contains(player.getName()) || memberUUIDs.contains(player.getUniqueId())) {
			return;
		}
		
		members.add(player.getName());
		memberUUIDs.add(player.getUniqueId());
	}
	
	//Serialization.
	
	/**
	 * 
	 */
	public void SaveToNBT() {
		
	}
}
