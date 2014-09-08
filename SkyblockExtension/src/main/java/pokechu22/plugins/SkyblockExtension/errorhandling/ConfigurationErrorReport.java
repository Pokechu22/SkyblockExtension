package pokechu22.plugins.SkyblockExtension.errorhandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.configuration.serialization.SerializableAs;

import pokechu22.plugins.SkyblockExtension.SkyblockExtension;

/**
 * Error report for when configuration loading fails.
 * @author Pokechu22
 *
 */
@SerializableAs("ConfigurationErrorReport")
public class ConfigurationErrorReport extends CrashReport {

	/**
	 * Was there an error?
	 * 
	 * Controls use of {@link #error}.
	 */
	public boolean hasError;
	
	/**
	 * The throwable that caused the error.  
	 */
	public Throwable error;
	
	/**
	 * Do we know the configuration file information?  
	 * 
	 * Controls use of {@link #key} and {@link #configurationFile}.
	 */
	public boolean configurationInfoKnown;
	
	/**
	 * The key that was being read.
	 */
	public String key;
	
	/**
	 * The configuration file that was being read.
	 */
	public String configurationFile;
	
	/**
	 * Is there a serializing class behind this issue?
	 * 
	 * Controls use of {@link #serializingClassName}.
	 */
	public boolean hasSerializingClass;
	
	/**
	 * The name of the class doing serialization.
	 */
	public String serializingClassName;
	
	/**
	 * Was it being saved, or loaded?
	 */
	public boolean saving;
	
	/**
	 * Context of the error.
	 */
	public String context;
	
	/**
	 * For use when no other constructor is valid.  Otherwise, don't use it!
	 * @param saving
	 */
	public ConfigurationErrorReport(boolean saving) {
		this(false, null, false, null, null, false, null, saving);
	}
	
	/**
	 * For use when no other constructor is valid.  Otherwise, don't use it!
	 * @param t
	 * @param saving
	 */
	public ConfigurationErrorReport(Throwable t, boolean saving) {
		this(true, t, false, null, null, false, null, saving);
	}
	
	/**
	 * Creates a ConfigurationErrorReport.  For use when serializing/
	 * deserializing something.
	 * @param className The name of the class in which the error occurred.
	 * @param saving True if serializing, false if deserializing.
	 */
	public ConfigurationErrorReport(String className, boolean saving) {
		this(false, null, false, null, null, true, className, saving);
	}

	/**
	 * Creates a ConfigurationErrorReport.  For use when serializing/
	 * deserializing something.
	 * @param t The throwable that caused the error.
	 * @param className The name of the class in which the error occurred.
	 * @param saving True if serializing, false if deserializing.
	 */
	public ConfigurationErrorReport(Throwable t, String className, boolean saving) {
		this(true, t, false, null, null, true, className, saving);
	}
	
	/**
	 * Creates a ConfigurationErrorReport.  For use when reading/writing a key
	 * from a configuration.
	 * @param key The key that was being read.
	 * @param configurationFile The name of the configuration file (use the 
	 * {@linkplain org.bukkit.configuration.Configuration#getName() getName()} 
	 * method).
	 * @param saving Was the config being written to or read from?  
	 * (True if being written to)
	 */
	public ConfigurationErrorReport(String key, String configurationFile, 
			boolean saving) {
		this(false, null, true, key, configurationFile, false, null, saving);
	}
	
	/**
	 * Creates a ConfigurationErrorReport.  For use when reading/writing a key
	 * from a configuration.
	 * @param error The throwable that caused the error.
	 * @param key The key that was being read.
	 * @param configurationFile The name of the configuration file (use the 
	 * {@linkplain org.bukkit.configuration.Configuration#getName() getName()} 
	 * method).
	 * @param saving Was the config being written to or read from?  
	 * (True if being written to)
	 */
	public ConfigurationErrorReport(Throwable error, String key, String configurationFile, 
			boolean saving) {
		this(true, error, true, key, configurationFile, false, null, saving);
	}
	
	/**
	 * Creates a ConfigurationErrorReport.  For use when reading/writing a key
	 * from a configuration, and a serialization issue happens.
	 * @param key The key that was being read.
	 * @param configurationFile The name of the configuration file (use the 
	 * {@linkplain org.bukkit.configuration.Configuration#getName() getName()} 
	 * method).
	 * @param className The name of the class in which the error occurred.
	 * @param saving Was the config being written to or read from?  
	 * (True if being written to)
	 */
	public ConfigurationErrorReport(String key, String configurationFile, 
			String className, boolean saving) {
		this(false, null, true, key, configurationFile, true, className, saving);
	}
	
	/**
	 * Creates a ConfigurationErrorReport.  For use when reading/writing a key
	 * from a configuration, and a serialization issue happens.
	 * @param error The throwable that caused the error.
	 * @param key The key that was being read.
	 * @param configurationFile The name of the configuration file (use the 
	 * {@linkplain org.bukkit.configuration.Configuration#getName() getName()} 
	 * method).
	 * @param className The name of the class in which the error occurred.
	 * @param saving Was the config being written to or read from?  
	 * (True if being written to)
	 */
	public ConfigurationErrorReport(Throwable error, String key, String configurationFile, 
			String className, boolean saving) {
		this(true, error, true, key, configurationFile, true, className, saving);
	}
	
	/**
	 * Creates a ConfigurationErrorReport.
	 * @param hasError Was there an error?
	 * @param error The throwable that caused the error, or null if there was none.
	 * @param key The key that was being read.
	 * @param configurationFile The name of the configuration file (use the 
	 * {@linkplain org.bukkit.configuration.Configuration#getName() getName()} 
	 * method).
	 * @param saving Was the config being written to or read from?  
	 * (True if being written to)
	 */
	private ConfigurationErrorReport(boolean hasError, Throwable error, 
			boolean configurationInfoKnown, String key, String configurationFile, 
			boolean hasSerializingClass, String serializingClassName,
			boolean saving) {
		super();
		
		this.hasError = hasError;
		this.error = error;
		this.configurationInfoKnown = configurationInfoKnown;
		this.key = key;
		this.configurationFile = configurationFile;
		this.hasSerializingClass = hasSerializingClass;
		this.serializingClassName = serializingClassName;
		this.saving = saving;
		
		this.context = "";
	}
	
	/**
	 * Gets the text of this crash report, without marking a specific reader.
	 * @return The text.
	 */
	@Override
	public String getAsTextNoMark() {
		StringBuilder text = new StringBuilder();
		
		text.append("Configuration error.\n");
		text.append("Occured on: " + loggedDate.toString() + "\n");
		
		if (configurationInfoKnown) {
			text.append("Occured while ");
			text.append((saving ? "saving " : "loading "));
			text.append(key); 
			text.append(" to Configuration file ");
			text.append(configurationFile + ".\n");
		}
		
		if (this.context != null) {
			text.append("Context: " + this.context + "\n");
		}
		
		if (hasSerializingClass) {
			text.append("Occured while ");
			text.append((saving ? "serializing " : "deserializing "));
			text.append("a");
			//Check if "an" is needed (this is probably overkill).
			if (serializingClassName.startsWith("a") ||
					serializingClassName.startsWith("e") ||
					serializingClassName.startsWith("i") ||
					serializingClassName.startsWith("o") ||
					serializingClassName.startsWith("u")) {
				text.append("n");
			}
			
			text.append(" " + serializingClassName + ".\n");
		}
		
		if (hasError) {
			text.append("Caused by: ");
			text.append(error.toString() + "\n");
			text.append("Stacktrace: \n");
			StackTraceElement[] elements = error.getStackTrace();
			for (int i = 0; i < elements.length; i++) {
				text.append(elements[i].toString());
				text.append("\n");
			}
		}
		
		text.append("End of report.");
		
		return text.toString();
	}

	/**
	 * Gets the title of the CrashReport.
	 * @return The title.
	 */
	@Override
	public String getTitle() {
		String title = "Configuration error during " + 
				(saving ? "saving" : "loading");
		if (hasError) {
			title += ": ";
			title += error.toString();
		} else {
			title += ".";
		}
		
		return title;
	}
	
	/**
	 * Serializes the CrashReport for configuration saving.
	 */
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = super.serializeBase();
		
		map.put("HasError", (boolean) this.hasError);
		if (hasError) {
			//Serialize error as a map.
			
			HashMap<String, Object> errorMap = new HashMap<String, Object>();
			errorMap.put("Class",this.error.getClass().getName());
			errorMap.put("Message", this.error.getMessage());
			
			ArrayList<HashMap<String, Object>> stackTraceList = 
					new ArrayList<HashMap<String, Object>>();
			
			StackTraceElement[] stackTrace = this.error.getStackTrace();
			for (int i = 0; i < stackTrace.length; i++) {
				HashMap<String, Object> stackTraceMap = new HashMap<String, Object>();
				stackTraceMap.put("ClassName", stackTrace[i].getClassName());
				stackTraceMap.put("MethodName", stackTrace[i].getMethodName());
				stackTraceMap.put("FileName", stackTrace[i].getFileName());
				stackTraceMap.put("LineNumber", stackTrace[i].getLineNumber());
				stackTraceList.add(stackTraceMap);
			}
			
			errorMap.put("StackTrace", stackTraceList);
			
			map.put("Error", (HashMap<String, Object>) errorMap);
		}
		
		map.put("ConfigurationInfoKnown", (boolean) this.configurationInfoKnown);
		if (this.configurationInfoKnown) {
			map.put("Key", (String) this.key);
			map.put("ConfigurationFile", this.configurationFile);
		}
		
		map.put("HasSerializingClass", (boolean) this.hasSerializingClass);
		if (this.hasSerializingClass) {
			map.put("SerializingClassName", this.serializingClassName);
		}
		
		map.put("Saving", this.saving);
		
		map.put("Context", this.context);
		
		return map;
	}
	
	/**
	 * Deserializes the crashReport for configuration loading.
	 */
	@SuppressWarnings("unchecked")
	public ConfigurationErrorReport(Map<String, Object> map) {
		super(map);
		
		try {
			this.hasError = (boolean) map.get("HasError");
			if (this.hasError) {
				//Create error.
				HashMap<String,Object> errorMap = (HashMap<String, Object>) map.get("Error");
				String errorClass = (String) errorMap.get("Class");
				String errorMessage = (String) errorMap.get("Message");
				
				ArrayList<HashMap<String, Object>> stackTraceListOld = 
						(ArrayList<HashMap<String, Object>>) errorMap.get("StackTrace");
				
				StackTraceElement[] stackTrace = new StackTraceElement[stackTraceListOld.size()];
				for (int i = 0; i < stackTraceListOld.size(); i++) {
					HashMap<String, Object> stackTraceElementMap = stackTraceListOld.get(i);
					
					stackTrace[i] = new StackTraceElement(
							(String) stackTraceElementMap.get("ClassName"), //declaringClass
							(String) stackTraceElementMap.get("MethodName"), //methodName
							(String) stackTraceElementMap.get("FileName"), //fileName
							(int) stackTraceElementMap.get("LineNumber") //lineNumber
							);
				}
				
				Throwable t = (Throwable) Class.forName(errorClass)
						.getConstructor(String.class)
						.newInstance(errorMessage);
				t.setStackTrace(stackTrace);
				
				this.error = t;
			} else {
				this.error = null;
			}
			
			this.configurationInfoKnown = (boolean) map.get("ConfigurationInfoKnown");
			if (this.configurationInfoKnown) {
				this.key = (String) map.get("Key");
				this.configurationFile = (String) map.get("ConfigurationFile");
			} else {
				this.key = null;
				this.configurationFile = null;
			}
			
			this.hasSerializingClass = (boolean) map.get("HasSerializingClass");
			if (this.hasSerializingClass) {
				this.serializingClassName = (String) map.get("SerializingClassName");
			} else {
				this.serializingClassName = null;
			}
			
			this.saving = (boolean) map.get("Saving");
			
			this.context = (String) map.get("Context");
			
		} catch (Exception e) {
			SkyblockExtension.inst().getLogger().warning("Failed to create " + 
					"ConfigurationErrorReport: " + e.toString());
			SkyblockExtension.inst().getLogger().log(Level.WARNING, "Exception: ", e);
			//Well, this might cause an infinite loop, but it is still a good idea.
			ErrorHandler.logError(new ConfigurationErrorReport(
					e, this.getClass().getName(), false));
		}
	}
	
	/**
	 * Deserialization; calls the {@link #ConfigurationErrorReport(Map)} constructor.
	 * @param map
	 * @return
	 */
	public static CrashReport deserialize(Map<String, Object> map) {
		return new ConfigurationErrorReport(map);
	}
	
	/**
	 * Deserialization; calls the {@link #ConfigurationErrorReport(Map)} constructor.
	 * @param map
	 * @return
	 */
	public static CrashReport valueOf(Map<String, Object> map) {
		return new ConfigurationErrorReport(map);
	}
	
	/**
	 * Sets context.
	 * @param context
	 * @return
	 */
	public CrashReport setContext(String context) {
		this.context = context;
		return this;
	}
}
