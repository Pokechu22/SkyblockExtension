package pokechu22.plugins.SkyblockExtension.protection.flags;

import java.util.ArrayList;

import pokechu22.plugins.SkyblockExtension.protection.VehicleType;

public class VehicleListFlag extends ListFlag<VehicleType> {

	/**
	 * Constructor for use with deserialization.
	 * 
	 * @deprecated Not really, but this should NOT be called normally;
	 * 				only through reflection.
	 */
	@Deprecated
	public VehicleListFlag() {
		value = new ArrayList<VehicleType>();
	}
	
	/**
	 * Deserialization.
	 * 
	 * @throws IllegalArgumentException when given invalid data.
	 */
	public VehicleListFlag(String serialized)
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
		return FlagType.VEHICLELIST;
	}

	@Override
	protected String getTypeName(boolean capitalize) {
		return capitalize ? "Vehicle" : "vehicle";
	}

	@Override
	protected VehicleType matchEnumValue(String valueToMatch)
			throws IllegalArgumentException, NullPointerException {
		return VehicleType.matchVehicleType(valueToMatch);
	}
}
