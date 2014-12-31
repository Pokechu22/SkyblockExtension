package pokechu22.plugins.SkyblockExtension.protection.flags;

import java.util.ArrayList;

import pokechu22.plugins.SkyblockExtension.protection.HangingType;

public class HangingListFlag extends ListFlag<HangingType> {

	/**
	 * Constructor for use with deserialization.
	 * 
	 * @deprecated Not really, but this should NOT be called normally;
	 * 				only through reflection.
	 */
	@Deprecated
	public HangingListFlag() {
		value = new ArrayList<HangingType>();
	}
	
	/**
	 * Deserialization.
	 * 
	 * @throws IllegalArgumentException When given an invalid input.
	 */
	public HangingListFlag(String serialized) 
			throws IllegalArgumentException {
		String result = this.setValue(serialized);
		if (result.startsWith("§a")) {
			return; //Valid value.
		} else {
			throw new IllegalArgumentException(result.substring(2));
		}
		
	}
	
	@Override
	public FlagType getType() {
		return FlagType.HANGINGLIST;
	}

	@Override
	protected String getTypeName(boolean capitalize) {
		return capitalize ? "Hanging entity" : "hanging entity";
	}

	@Override
	protected HangingType matchEnumValue(String valueToMatch)
			throws IllegalArgumentException, NullPointerException {
		return HangingType.matchHangingType(valueToMatch);
	}
}
