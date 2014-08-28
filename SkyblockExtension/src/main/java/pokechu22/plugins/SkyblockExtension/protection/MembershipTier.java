package pokechu22.plugins.SkyblockExtension.protection;

import java.util.Locale;

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
	
	/**
	 * Matches a tier, non-capitalized and trim()ed.
	 * 
	 * @param name
	 * @return The tier, or null if not found.
	 */
	public static MembershipTier matchTier(String name) {
		try {
			return MembershipTier.valueOf(name.toLowerCase(Locale.ENGLISH)
					.trim());
		} catch (Exception e) {} //Do nothing.
		return null;
	}
}
