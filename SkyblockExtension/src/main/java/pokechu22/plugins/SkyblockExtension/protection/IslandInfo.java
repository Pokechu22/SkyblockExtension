package pokechu22.plugins.SkyblockExtension.protection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
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
	private static class MemberInfo {
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
		public boolean equals(Object obj) {
			if (obj instanceof MemberInfo) {
				return ((MemberInfo)(obj)).playerUUID
						.equals(this.playerUUID);
			} else if (obj instanceof GuestInfo) {
				return ((GuestInfo)(obj)).playerUUID
						.equals(this.playerUUID);
			} else if (obj instanceof Player) {
				return ((Player)(obj)).getUniqueId()
						.equals(this.playerUUID);
			}
			return false;
		}
	}
	
	private static class GuestInfo {
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
		
		/**
		 * Checks equality.  
		 * NOTE: Expiration time is ignored in this case.
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof GuestInfo) {
				return ((GuestInfo)(obj)).playerUUID
						.equals(this.playerUUID);
			} else if (obj instanceof MemberInfo) {
				return ((MemberInfo)(obj)).playerUUID
						.equals(this.playerUUID);
			} else if (obj instanceof Player) {
				return ((Player)(obj)).getUniqueId()
						.equals(this.playerUUID);
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
		this.islandCenter = new Location(SkyblockExtension.inst()
				.getServer().getWorld(location.getString("World")), 
				location.getDouble("X"), location.getDouble("Y"), 
				location.getDouble("Z"));
		
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
	 * Serializes this to a NBT tag.
	 * @return
	 */
	public Tag serializeToNBT() {
		CompoundTag tag = new CompoundTag();
		
		CompoundTag location = new CompoundTag("Location");
		location.putString("World", this.islandCenter.getWorld().getName());
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
		
		return xPos + "x" + zPos + "z";
	}
	
	/**
	 * Saves this to disk as a .NBT file.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void saveToDisk() throws FileNotFoundException, IOException {
		NbtIo.writeCompressed((CompoundTag) this.serializeToNBT(), 
				new FileOutputStream(new File(SkyblockExtension.inst()
						.getDataFolder(), (this.getFileName() + ".nbt"))));
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
				new FileInputStream(new File(SkyblockExtension.inst()
				.getDataFolder(), fileName))));
		
		return returned;
	}
	
	/**
	 * Creates an IslandInfo from a {@link PlayerInfo}.
	 *
	 * @return Either the new IslandInfo, or null.
	 */
	@SuppressWarnings("deprecation")
	public static IslandInfo convertFromPlayerInfo(PlayerInfo info) {
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
