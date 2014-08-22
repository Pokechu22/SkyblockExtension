package pokechu22.plugins.SkyblockExtension.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles serialization and deserialization of enums.
 * 
 * @author Pokechu22
 *
 */
public class EnumSerializationUtil {
	/**
	 * Gets a serializable array for 
	 * @param e
	 * @return
	 */
	public static String[] getSerializable(List<Enum<?>> e) {
		String[] returned = new String[e.size()];
		for (int i = 0; i < returned.length; i++) {
			returned[i] = e.get(i).name();
		}
		return returned;
	}
	
	/**
	 * Deserializes to a List&lt;Enum&gt;.
	 * @param serialized
	 * @param type
	 * @return
	 */
	public static <T extends Enum<T>> List<T> deserialize(
			String[] serialized, Class<T> type) {
		ArrayList<T> returned = 
				new ArrayList<T>(serialized.length);
		for (int i = 0; i < serialized.length; i++) {
			returned.add(Enum.valueOf(type, serialized[i]));
		}
		return returned;
	}
}
