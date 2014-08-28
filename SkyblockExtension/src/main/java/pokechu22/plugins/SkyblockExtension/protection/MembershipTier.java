package pokechu22.plugins.SkyblockExtension.protection;

/**
 * Different tiers of membership.
 *
 * @author Pokechu22
 *
 */
public enum MembershipTier {
	/**
	 * The owner of the island.
	 */
	owner,
	/**
	 * Any other member of an island.
	 */
	member,
	/**
	 * A guest, temporarily part of the island.
	 */
	guest,
	/**
	 * Any player who is in none of the above groups.
	 */
	nonmember;
}
