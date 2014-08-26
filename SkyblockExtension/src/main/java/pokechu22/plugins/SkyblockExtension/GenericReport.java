package pokechu22.plugins.SkyblockExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
	public StackTraceElement[] stackTrace;
	
	public GenericReport(String context) {
		this.context = context;
		
		//Create the stacktrace.
		Throwable t = new Throwable();
		this.stackTrace = t.getStackTrace();
		
		this.readers = new HashSet<String>();
	}

	@Override
	public String getAsTextNoMark() {
		StringBuilder text = new StringBuilder();
		
		text.append("Generic Error; no specific information.\n");
		text.append("Context: " + context + "\n");
		
		//Add the stacktrace.
		text.append("Stacktrace: \n");
		for (int i = 0; i < this.stackTrace.length; i++) {
			text.append(this.stackTrace[i].toString());
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
		//Create stacktrace.
		ArrayList<HashMap<String, Object>> stackTraceList = 
				new ArrayList<HashMap<String, Object>>();
		
		for (int i = 0; i < stackTrace.length; i++) {
			HashMap<String, Object> stackTraceMap = new HashMap<String, Object>();
			stackTraceMap.put("ClassName", stackTrace[i].getClassName());
			stackTraceMap.put("MethodName", stackTrace[i].getMethodName());
			stackTraceMap.put("FileName", stackTrace[i].getFileName());
			stackTraceMap.put("LineNumber", stackTrace[i].getLineNumber());
			stackTraceList.add(stackTraceMap);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Context", this.context);
		map.put("StackTrace", stackTraceList);
		return map;
	}
	
	/**
	 * Deserializes the crashReport for configuration loading.
	 */
	@SuppressWarnings("unchecked")
	public GenericReport(Map<String, Object> map) {
		//Deserialize stacktrace.
		ArrayList<HashMap<String, Object>> stackTraceListOld = 
				(ArrayList<HashMap<String, Object>>) map.get("StackTrace");
		
		this.stackTrace = new StackTraceElement[stackTraceListOld.size()];
		for (int i = 0; i < stackTraceListOld.size(); i++) {
			HashMap<String, Object> stackTraceElementMap = stackTraceListOld.get(i);
			
			this.stackTrace[i] = new StackTraceElement(
					(String) stackTraceElementMap.get("ClassName"), //declaringClass
					(String) stackTraceElementMap.get("MethodName"), //methodName
					(String) stackTraceElementMap.get("FileName"), //fileName
					(int) stackTraceElementMap.get("LineNumber") //lineNumber
					);
		}
		
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
