package us.talabrek.ultimateskyblock;

/**
 * Dupliate that overrides the vanilla uSkyBlock configuration, for testing.
 * 
 * Obtained using <code>javap -c [...]\Settings.class</code>.
 *
 * @author Pokechu22
 *
 */
public class Settings {
	public static final int general_maxPartySize = 20;

	public static final java.lang.String general_worldName = "skyworld";

	public static final int island_distance = 110;

	public static final int island_height = 120;

	public static final int general_spawnSize = 50;

	public static final boolean island_removeCreaturesByTeleport = true;

	public static final boolean island_protectWithWorldGuard = false;

	public static final int island_protectionRange = 100;

	public static final java.lang.String island_allowPvP = "deny";

	@SuppressWarnings("deprecation")
	public static final org.bukkit.inventory.ItemStack[] island_chestItems = {
		new org.bukkit.inventory.ItemStack(79, 2),
		new org.bukkit.inventory.ItemStack(360, 1),
		new org.bukkit.inventory.ItemStack(81, 1),
		new org.bukkit.inventory.ItemStack(327, 1),
		new org.bukkit.inventory.ItemStack(40, 1),
		new org.bukkit.inventory.ItemStack(39, 1),
		new org.bukkit.inventory.ItemStack(361, 1),
		new org.bukkit.inventory.ItemStack(338, 1),
		new org.bukkit.inventory.ItemStack(323, 1)
	};

	//Changed from default
	public static final boolean island_addExtraItems = false;

	//I don't know how this actually works
	public static final java.lang.String[] island_extraPermissions = {
		"smallbonus: '4:32 320:5'",
		"mediumbonus: '50:16 327:1'",
		"largebonus: '3:10 12:10'"
	};

	public static final boolean island_useOldIslands = false;

	public static final boolean island_allowIslandLock = false;

	public static final boolean island_useIslandLevel = true;

	public static final boolean island_useTopTen = true;

	//I'm not quite sure what this is, but it must be something.
	//I'll assume that it is general_cooldownInfo, or the configuration  
	//value options.general.cooldownInfo.
	public static final int island_listTime = 30;

	public static final int general_cooldownInfo = 30;

	public static final int general_cooldownRestart = 600;

	public static final boolean extras_sendToSpawn = false;

	public static final boolean extras_obsidianToLava = true;

	public static final java.lang.String island_schematicName = "yourschematicname";

	public static final boolean challenges_broadcastCompletion = true;

	//I'd use §6, but whatever, that's how the config works...
	public static final java.lang.String challenges_broadcastText = "&6";

	public static final java.lang.String[] challenges_ranks = {
		"Easy",
		"Medium",
		"Hard",
		"Master"
	};

	public static final boolean challenges_requirePreviousRank = true;

	public static final int challenges_rankLeeway = 1;

	public static final java.lang.String challenges_challengeColor = "&e";

	public static final java.lang.String challenges_finishedColor = "&2";

	public static final java.lang.String challenges_repeatableColor = "&a";

	public static final boolean challenges_enableEconomyPlugin = true;

	public static final boolean challenges_allowChallenges = true;

	//Not sure what to put here...
	public static final java.util.Set<java.lang.String> challenges_challengeList =
			//Double bracket syntax - anonymous inner class and stuff;
			//allows putting values easily.
			new java.util.HashSet<String>() {
				//Although eclipse will be a pain and force this.
				private static final long serialVersionUID = 1L;
				{
					add("TestChallenge1");
					add("TestChallenge2");
					add("TestChallenge3");
					add("Example");
				}
			};
}