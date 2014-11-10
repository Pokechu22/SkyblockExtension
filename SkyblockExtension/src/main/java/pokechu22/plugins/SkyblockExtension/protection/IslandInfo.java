package pokechu22.plugins.SkyblockExtension.protection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import pokechu22.plugins.SkyblockExtension.SkyblockExtension;
import pokechu22.plugins.SkyblockExtension.util.nbt.*;
import us.talabrek.ultimateskyblock.PlayerInfo;
import us.talabrek.ultimateskyblock.Settings;

/**
 * Contains information about an island.
 *
 * @author Pokechu22
 *
 */
public class IslandInfo {
	public static class MemberInfo {
		public String playerName;
		public UUID playerUUID;
		
		/**
		 * Constructs a PlayerInfo off of a player.
		 *
		 * @param player
		 */
		public MemberInfo(Player player) {
			this.playerName = player.getName();
			this.playerUUID = player.getUniqueId();
		}
		
		/**
		 * Constructs a playerInfo using the specified information.
		 *
		 * @param playerName
		 * @param playerUUID
		 */
		public MemberInfo(String playerName, UUID playerUUID) {
			this.playerName = playerName;
			this.playerUUID = playerUUID;
		}
		
		/**
		 * Deserializes this from an NBT file.
		 * Not a constructor - modifies existing values.
		 * @throws IllegalArgumentException when given a tag other than
		 * TAG_COMPOUND.
		 * @param serialized
		 */
		public MemberInfo(Tag serialized) {
			if (!(serialized instanceof CompoundTag)) {
				throw new IllegalArgumentException("Can only deserialize " +
						MemberInfo.class.getName() + "from Compound Tags." +
						"  Got tag of type " + serialized.getClass()
						.getName() + ", expected " + CompoundTag.class
						.getName() + ".  Value (from toString): " + 
						serialized.toString());
			}
			CompoundTag tag = (CompoundTag) serialized;
			
			this.playerName = tag.getString("PlayerName");
			this.playerUUID = new UUID(tag.getLong("PlayerUUIDMost"),
					tag.getLong("PlayerUUIDLeast"));  
		}
		
		/**
		 * Serializes this to a NBT tag.
		 * @return
		 */
		public Tag serializeToNBT() {
			CompoundTag tag = new CompoundTag();
			
			tag.putString("PlayerName", this.playerName);
			tag.putLong("PlayerUUIDMost", 
					this.playerUUID.getMostSignificantBits());
			tag.putLong("PlayerUUIDLeast", 
					this.playerUUID.getLeastSignificantBits());
			
			return tag;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((playerUUID == null) ? 0 : playerUUID.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (obj instanceof MemberInfo) {
				MemberInfo other = (MemberInfo) obj;
				if (playerUUID == null) {
					if (other.playerUUID != null) {
						return false;
					}
				} else if (!playerUUID.equals(other.playerUUID)) {
					return false;
				}
				return true;
			} else if (obj instanceof GuestInfo) {
				//Conversion for returning stuff.
				GuestInfo other = (GuestInfo) obj;
				if (playerUUID == null) {
					if (other.playerUUID != null) {
						return false;
					}
				} else if (!playerUUID.equals(other.playerUUID)) {
					return false;
				}
				return true;
			}
			return false;
		}
	}
	
	public static class GuestInfo {
		/**
		 * A date to use for the default value, where guest will never 
		 * expire.  (Fine, it will expire, but that's in several thousand
		 * years)
		 */
		private static final Date NEVER_EXPIRE_DATE = 
				new Date(0x7FFFFFFFFFFFFFFFL);
		
		public String playerName;
		public UUID playerUUID;
		public Date guestUntil;
		
		/**
		 * Creates a geustInfo for the specified player that never expires.
		 * @param player
		 */
		public GuestInfo(Player player) {
			this(player, NEVER_EXPIRE_DATE);
		}
		
		/**
		 * Creates a geustInfo for the specified player that never expires.
		 * @param player
		 * @param guestUntil
		 */
		public GuestInfo(Player player, Date guestUntil) {
			this.playerName = player.getName();
			this.playerUUID = player.getUniqueId();
			this.guestUntil = guestUntil;
		}
		
		/**
		 * Constructs a GuestInfo with the specified information, that will
		 * never expire.
		 *
		 * @param playerName
		 * @param playerUUID
		 */
		public GuestInfo(String playerName, UUID playerUUID) {
			this(playerName, playerUUID, NEVER_EXPIRE_DATE);
		}
		
		/**
		 * Constructs a GuestInfo with the specified information.
		 *
		 * @param playerName
		 * @param playerUUID
		 * @param guestUntil
		 */
		public GuestInfo(String playerName, UUID playerUUID, 
				Date guestUntil) {
			this.playerName = playerName;
			this.playerUUID = playerUUID;
			this.guestUntil = guestUntil;
		}
		
		/**
		 * Checks if the player is still a guest.
		 *
		 * @return
		 */
		public boolean isStillGuest() {
			Date now = new Date();
			return now.before(this.guestUntil);
		}
		
		/**
		 * Deserializes this from an NBT file.
		 * Not a constructor - modifies existing values.
		 * @throws IllegalArgumentException when given a tag other than
		 * TAG_COMPOUND.
		 * @param serialized
		 */
		public GuestInfo(Tag serialized) {
			if (!(serialized instanceof CompoundTag)) {
				throw new IllegalArgumentException("Can only deserialize " +
						GuestInfo.class.getName() + "from Compound Tags." +
						"  Got tag of type " + serialized.getClass()
						.getName() + ", expected " + CompoundTag.class
						.getName() + ".  Value (from toString): " + 
						serialized.toString());
			}
			CompoundTag tag = (CompoundTag) serialized;
			
			this.playerName = tag.getString("PlayerName");
			this.playerUUID = new UUID(tag.getLong("PlayerUUIDMost"),
					tag.getLong("PlayerUUIDLeast"));
			this.guestUntil = new Date(tag.getLong("GuestUntil"));
		}
		
		/**
		 * Serializes this to a NBT tag.
		 * @return
		 */
		public Tag serializeToNBT() {
			CompoundTag tag = new CompoundTag();
			
			tag.putString("PlayerName", this.playerName);
			tag.putLong("PlayerUUIDMost", 
					this.playerUUID.getMostSignificantBits());
			tag.putLong("PlayerUUIDLeast", 
					this.playerUUID.getLeastSignificantBits());
			
			tag.putLong("GuestUntil", guestUntil.getTime());
			
			return tag;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((playerUUID == null) ? 0 : playerUUID.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (obj instanceof GuestInfo) {
				GuestInfo other = (GuestInfo) obj;
				if (playerUUID == null) {
					if (other.playerUUID != null) {
						return false;
					}
				} else if (!playerUUID.equals(other.playerUUID)) {
					return false;
				}
				return true;
			} else if (obj instanceof MemberInfo) {
				//Makes it easier to remove from lists.
				MemberInfo other = (MemberInfo) obj;
				if (playerUUID == null) {
					if (other.playerUUID != null) {
						return false;
					}
				} else if (!playerUUID.equals(other.playerUUID)) {
					return false;
				}
				return true;
			}
			
			return false;
		}
	}
	
	/**
	 * The location of the bedrock in the center of the island.
	 * This is based off of a uSkyBlock value.
	 */
	public Location islandCenter;
	
	/**
	 * Information about the owner of the island.  
	 * (Uses a member info because I don't want to create a redundant class)
	 */
	public MemberInfo ownerInfo;
	
	/**
	 * Information about all members of the island.
	 */
	public List<MemberInfo> members;
	
	/**
	 * Information about all the guests on the island.
	 */
	public List<GuestInfo> guests;
	
	public Map<String, IslandProtectionDataSet> permissions;
	
	/**
	 * Deserialization empty constructor.
	 */
	private IslandInfo() {
		
	}
	
	/**
	 * Creates an islandInfo with only the specified information.
	 * All other parameters are set at default.
	 *
	 * @param islandCenter
	 * @param owner
	 */
	public IslandInfo(Location islandCenter, Player owner) {
		this.islandCenter = islandCenter;
		this.ownerInfo = new MemberInfo(owner);
		
		this.members = new ArrayList<MemberInfo>();
		
		this.guests = new ArrayList<GuestInfo>();
		
		this.permissions = IslandProtectionDataSetFactory
				.getDefaultValues();
	}
	
	/**
	 * Sets a player as an owner.
	 * The existing owner is moved to members.
	 * 
	 * @param player The new owner.
	 */
	public void setOwner(Player player) {
		MemberInfo oldOwner = this.ownerInfo;
		MemberInfo newOwner = new MemberInfo(player);
		
		this.members.remove(newOwner); //Remove previous owner.
		this.members.add(oldOwner); //Add old owner to members.
		
		this.guests.remove(newOwner);
		
		this.ownerInfo = newOwner;
	}
	
	/**
	 * Updates the owner with previous information, as needed.
	 * If the owner was changed, the old one is moved to members.
	 *
	 * @param owner
	 */
	public void freshenOwner(Player owner) {
		MemberInfo info = new MemberInfo(owner);
		if (info.equals(this.ownerInfo)) {
			return;
		}
		this.setOwner(owner);
	}
	
	/**
	 * Updates the member with previous information, as needed.
	 * If the player was previously a guest, they are moved to
	 * member.  If the player is owner, this action is ignored.
	 *
	 * @param owner
	 */
	public void freshenMember(Player member) {
		MemberInfo info = new MemberInfo(member);
		if (this.ownerInfo.equals(info)) {
			return;
		}
		if (this.members.contains(info)) {
			return;
		}
		this.addMember(member);
	}
	
	//TODO: freshenGuest
	
	/**
	 * Adds a player as a member.
	 *
	 * @param player
	 */
	public void addMember(Player player) {
		//If the player is already an owner, do nothing.
		if (this.ownerInfo.equals(player)) {
			return;
		}
		
		MemberInfo memberInfo = new MemberInfo(player);
		
		if (this.members.contains(memberInfo)) {
			return;
		}
		
		this.guests.remove(memberInfo);
		this.members.add(memberInfo);
	}
	
	/**
	 * 
	 * @param player
	 * @param seconds Seconds to have this player be a guest.
	 */
	public void addGuest(Player player, int seconds) {
		Calendar expiryTime = Calendar.getInstance(); //Now.
		expiryTime.add(Calendar.SECOND, seconds);
		this.addGuest(player, expiryTime.getTime());
	}
	
	/**
	 * 
	 * @param player
	 * @param guestUntil
	 */
	public void addGuest(Player player, Date guestUntil) {
		GuestInfo added = new GuestInfo(player, guestUntil);
		if (this.members.contains(added) || this.ownerInfo.equals(added)) {
			return; //Already a member.
		}
		
		this.guests.remove(added); //If it already exists, replace it.
		this.guests.add(added); //Add it in now.
	}

	/**
	 * 
	 * @param player
	 */
	public void addGuest(Player player) {
		GuestInfo added = new GuestInfo(player);
		if (this.members.contains(added) || this.ownerInfo.equals(added)) {
			return; //Already a member.
		}
		
		this.guests.remove(added); //If it already exists, replace it.
		this.guests.add(added); //Add it in now.
	}
	
	/**
	 * 
	 * @param player
	 */
	public void removeMember(Player player) {
		//TODO
	}
	
	public void removeGuest(Player player) {
		//TODO
	}
	
	/**
	 * Deserializes this from an NBT file.
	 * Not a constructor - modifies existing values.
	 * @param serialized
	 */
	public void deserializeFromNBT(Tag serialized) {
		if (!(serialized instanceof CompoundTag)) {
			throw new IllegalArgumentException("Can only deserialize " +
					IslandInfo.class.getName() + "from Compound Tags." +
					"  Got tag of type " + serialized.getClass()
					.getName() + ", expected " + CompoundTag.class
					.getName() + ".  Value (from toString): " + 
					serialized.toString());
		}
		
		CompoundTag tag = (CompoundTag) serialized;
		
		CompoundTag location = tag.getCompound("Location");
		
		World w = null;
		try {
			w = Bukkit.getWorld(location.getString("World"));
		} catch (NullPointerException e) {}
		
		this.islandCenter = new Location(w, location.getDouble("X"), 
				location.getDouble("Y"), location.getDouble("Z"));
		
		this.ownerInfo = new MemberInfo(tag.getCompound("OwnerInfo"));
		
		this.members = new ArrayList<MemberInfo>();
		ListTag<? extends Tag> memberList = tag.getList("Members");
		for (int i = 0; i < memberList.size(); i++) {
			Tag member = memberList.get(i);
			this.members.add(new MemberInfo(member));
		}
		
		this.guests = new ArrayList<GuestInfo>();
		ListTag<? extends Tag> guestList = tag.getList("Guests");
		for (int i = 0; i < guestList.size(); i++) {
			Tag guest = guestList.get(i);
			this.guests.add(new GuestInfo(guest));
		}
		
		CompoundTag perms = tag.getCompound("Permissions");
		this.permissions = 
				IslandProtectionDataSetFactory.getDefaultValues();
		for (Map.Entry<String, IslandProtectionDataSet> entry : 
			this.permissions.entrySet()) {
			entry.getValue().deserializeFromNBT(
					perms.getCompound(entry.getKey()));
		}
	}
	
	/**
	 * Gets whether the specified player is the owner.
	 * 
	 * @param player
	 * @return
	 */
	public boolean isOwner(Player player) {
		return player.getUniqueId().equals(ownerInfo.playerUUID);
	}
	
	/**
	 * Gets whether the specified player is a member.
	 * If the player is an owner, this returns false.
	 * 
	 * @param player
	 * @return
	 */
	public boolean isMember(Player player) {
		for (MemberInfo info : this.members) {
			if (player.getUniqueId().equals(info.playerUUID)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets whether the specified player is a member.
	 * If the player is an owner or guest, this returns false.
	 * 
	 * @param player
	 * @return
	 */
	public boolean isGuest(Player player) {
		for (GuestInfo info : this.guests) {
			if (player.getUniqueId().equals(info.playerUUID)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the membership tier that the specified player has.
	 * 
	 * @param player
	 * @return
	 */
	public MembershipTier getMembershipTierOfPlayer(Player player) {
		if (isOwner(player)) {
			return MembershipTier.owner;
		} else if (isMember(player)) {
			return MembershipTier.member;
		} else if (isGuest(player)) {
			return MembershipTier.guest;
		} else {
			return MembershipTier.nonmember;
		}
	}
	
	/**
	 * Gets the {@link IslandProtectionDataSet} that should be used for
	 * the specified player.
	 * 
	 * @param player
	 * @return
	 */
	public IslandProtectionDataSet getDataSetForPlayer(Player player) {
		if (isOwner(player)) {
			return this.permissions.get("owner");
		} else if (isMember(player)) {
			return this.permissions.get("member");
		} else if (isGuest(player)) {
			return this.permissions.get("guest");
		} else {
			return this.permissions.get("nonmember");
		}
	}
	
	/**
	 * Serializes this to a NBT tag.
	 * @return
	 */
	public Tag serializeToNBT() {
		CompoundTag tag = new CompoundTag();
		
		CompoundTag location = new CompoundTag("Location");
		try {
			location.putString("World", this.islandCenter.getWorld().getName());
		} catch (NullPointerException e) {
			location.putString("World", "null");
		}
		location.putDouble("X", this.islandCenter.getX());
		location.putDouble("Y", this.islandCenter.getY());
		location.putDouble("Z", this.islandCenter.getZ());
		tag.putCompound("Location", location);
		
		tag.put("OwnerInfo", this.ownerInfo.serializeToNBT());
		
		ListTag<CompoundTag> members = new ListTag<CompoundTag>("Members");
		for (MemberInfo memberInfo : this.members) {
			members.add((CompoundTag) memberInfo.serializeToNBT());
		}
		tag.put("Members", members);
		
		ListTag<CompoundTag> guests = new ListTag<CompoundTag>("Guests");
		for (GuestInfo guestInfo : this.guests) {
			guests.add((CompoundTag) guestInfo.serializeToNBT());
		}
		tag.put("guests", guests);
		
		CompoundTag perms = new CompoundTag("Permissions");
		this.permissions = 
				IslandProtectionDataSetFactory.getDefaultValues();
		for (Map.Entry<String, IslandProtectionDataSet> entry : 
			this.permissions.entrySet()) {
			
			perms.put(entry.getKey(), entry.getValue().serializeToNBT());
		}
		tag.putCompound("Permissions", perms);
		
		return tag;
	}
	
	/**
	 * Gets the file name, but not the extension or path.
	 * It's of the form 0x0z, which is the island location.
	 * @return
	 */
	public String getFileName() {
		int xPos;
		int zPos;
		
		xPos = (int)(this.islandCenter.getX() / Settings.island_distance);
		zPos = (int)(this.islandCenter.getZ() / Settings.island_distance);
		
		return xPos + "x" + zPos + "z" + ".nbt";
	}
	
	/**
	 * Saves this to disk as a .NBT file.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void saveToDisk() throws FileNotFoundException, IOException {
		NbtIo.writeCompressed((CompoundTag) this.serializeToNBT(), 
				new FileOutputStream(new File(SkyblockExtension
						.getRealDataFolder(), (this.getFileName()))));
	}
	
	/**
	 * Reads this from disk as a .NBT file.
	 * @param x X-id of the island.
	 * @param z Z-id of the island.
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static IslandInfo readFromDisk(int x, int z) 
			throws FileNotFoundException, IOException {
		return IslandInfo.readFromDisk(x + "x" + z + "z" + ".nbt");
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static IslandInfo readFromDisk(String fileName) 
			throws FileNotFoundException, IOException {
		IslandInfo returned = new IslandInfo();
		
		returned.deserializeFromNBT(NbtIo.readCompressed(
				new FileInputStream(new File(SkyblockExtension
						.getRealDataFolder(), fileName))));
		
		return returned;
	}
	
	/**
	 * Creates an IslandInfo from a {@link PlayerInfo}.
	 *
	 * @return Either the new IslandInfo, or null.
	 */
	@SuppressWarnings("deprecation")
	public static IslandInfo convertFromPlayerInfo(PlayerInfo info) {
		if (!info.getHasIsland() && !info.getHasParty()) {
			return null;
		}
		
		IslandInfo returned = new IslandInfo();
		
		returned.islandCenter = info.getIslandLocation();
		
		OfflinePlayer ownerPlayer = 
				Bukkit.getOfflinePlayer(info.getPartyLeader());
		returned.ownerInfo =  new MemberInfo(ownerPlayer.getName(), 
				ownerPlayer.getUniqueId());
		
		returned.members = new ArrayList<MemberInfo>();
		for (String memberName : info.getMembers()) {
			if (!memberName.equals(returned.ownerInfo.playerName)) {
				//Needed to convert UUID.
				OfflinePlayer player = Bukkit.getOfflinePlayer(memberName);
				MemberInfo member = new MemberInfo(player.getName(), 
						player.getUniqueId());
				returned.members.add(member);
			}
		}
		
		returned.guests = new ArrayList<GuestInfo>();
		
		returned.permissions = 
				IslandProtectionDataSetFactory.getDefaultValues();
		
		return returned;
	}
}
