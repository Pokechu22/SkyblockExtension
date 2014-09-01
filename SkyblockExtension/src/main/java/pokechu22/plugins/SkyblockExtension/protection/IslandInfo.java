package pokechu22.plugins.SkyblockExtension.protection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

/**
 * Contains information about an island.
 *
 * @author Pokechu22
 *
 */
public class IslandInfo implements ConfigurationSerializable {
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
	 * Serialization for use with a configuration file.
	 */
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("islandCenter", islandCenter);
		
		map.put("owner", owner);
		map.put("ownerUUID", ownerUUID);
		
		map.put("members", members);
		map.put("memberUUIDs", memberUUIDs);
		
		map.put("guests", guests);
		map.put("guestUUIDs", guestUUIDs);
		
		map.put("permissions", permissions);
		
		return map;
	}
	
	/**
	 * Constructor for deserialization of this from a configuration file.
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IslandInfo(Map<String, Object> map) {
		this.islandCenter = (Location) map.get("islandCenter");
		
		this.owner = (String) map.get("owner");
		this.ownerUUID = (UUID) map.get(ownerUUID);
		
		this.members = (List<String>) map.get("members");
		this.memberUUIDs = (List<UUID>) map.get("memberUUIDs");
		
		this.guests = (List<String>) map.get("guestUUIDs");
		this.guestUUIDs = (List<UUID>) map.get("geustUUIDs");
		
		//Deserialize permissions
		this.permissions = new HashMap<String, IslandProtectionDataSet>();
		for (MembershipTier t : MembershipTier.values()) {
			IslandProtectionDataSet i;
			i = (IslandProtectionDataSet)map.get("permissions." + t.name());
			
			this.permissions.put(t.name(), i);
		}
	}
	
	/**
	 * Deserialization of this from a configuration file.
	 * @param map
	 * @return
	 */
	public static IslandInfo deserialize(
			Map<String, Object> map) {
		return new IslandInfo(map);
	}

	/**
	 * Deserialization of this from a configuration file.
	 * @param map
	 * @return
	 */
	public static IslandInfo valueOf(Map<String, Object> map) {
		return new IslandInfo(map);
	}
}
