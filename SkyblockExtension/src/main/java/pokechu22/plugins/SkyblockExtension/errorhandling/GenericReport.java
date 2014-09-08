package pokechu22.plugins.SkyblockExtension.errorhandling;

import java.util.Map;

import org.bukkit.configuration.serialization.SerializableAs;

/**
 * A generic error report that only contains a detail message and a stacktrace.
 *
 * @author Pokechu22
 *
 */
@SerializableAs("GenericReport")
public class GenericReport extends CrashReport {

	public String context;
	
	public GenericReport(String context) {
		super();
		
		this.context = context;
	}

	@Override
	public String getAsTextNoMark() {
		StringBuilder text = new StringBuilder();
		
		text.append("Generic Error; no specific information.\n");
		text.append("Context: " + context + "\n");
		
		//Add the stacktrace.
		text.append("Stacktrace: \n");
		for (int i = 0; i < super.localStackTrace.length; i++) {
			text.append(super.localStackTrace[i].toString());
			text.append("\n");
		}
		
		return text.toString();
	}

	@Override
	public String getTitle() {
		return "Generic Error: " + context;
	}

	/**
	 * Serializes the crashReport for configuration saving.
	 */
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = super.serializeBase();
		
		map.put("Context", this.context);
		return map;
	}
	
	/**
	 * Deserializes the crashReport for configuration loading.
	 */
	public GenericReport(Map<String, Object> map) {
		super(map);
		
		this.context = (String) map.get("Context");
	}
	
	/**
	 * Deserialization; calls the {@link #CrashReport(Map)} constructor.
	 * @param map
	 * @return
	 */
	public static CrashReport deserialize(Map<String, Object> map) {
		
		return new GenericReport(map);
	}
	
	/**
	 * Deserialization; calls the {@link #CrashReport(Map)} constructor.
	 * @param map
	 * @return
	 */
	public static CrashReport valueOf(Map<String, Object> map) {
		return new GenericReport(map);
	}
}
