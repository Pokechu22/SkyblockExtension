package pokechu22.plugins.SkyblockExtension.util.mcstats;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import pokechu22.plugins.SkyblockExtension.SkyblockExtension;
import pokechu22.plugins.SkyblockExtension.WitherWarner;
import pokechu22.plugins.SkyblockExtension.errorhandling.CrashReport;
import pokechu22.plugins.SkyblockExtension.errorhandling.ErrorHandler;
import pokechu22.plugins.SkyblockExtension.util.PlayerPrintStream;
import pokechu22.plugins.SkyblockExtension.util.mcstats.Metrics.Graph;
import pokechu22.plugins.SkyblockExtension.util.mcstats.Metrics.Plotter;

public class MetricsHandler {
	/**
	 * Using metrics to do stuff.
	 * Please see <a href="http://mcstats.org">mcstats.org</a>.
	 */
	private static Metrics metrics;

	/**
	 * Gets the instance of metrics.
	 *
	 * @return
	 */
	public static Metrics metrics() {
		return metrics;
	}

	static class BooleanPlotter extends Plotter {
		public final boolean value;

		/**
		 * Constructs a BooleanPlotter with that name and value.
		 * @param name
		 */
		public BooleanPlotter(String name, boolean value) {
			super (name);
			this.value = value;
		}

		@Override
		public int getValue() {
			return value ? 0 : 1;
		}
	}
	
	static class IntPlotter extends Plotter {
		private final int value;

		/**
		 * Constructs a BooleanPlotter with that name and value.
		 * @param name
		 */
		public IntPlotter(String name, int value) {
			super (name);
			this.value = value;
		}

		@Override
		public int getValue() {
			return value;
		}
	}

	/**
	 * Starts Metrics and adds the needed data.
	 */
	public static void start() {
		try {
			metrics = new Metrics(SkyblockExtension.inst());
			
			Graph errors = metrics.createGraph("ErrorHandler current count");
			
			errors.addPlotter(new Plotter("ErrorHandler current count") {
				@Override
				public int getValue() {
					// TODO Auto-generated method stub
					return ErrorHandler.getNumberOfCrashes();
				}
			});
			
			Graph errorTypes = metrics.createGraph("Error Type Distribution");
			
			//Contains each of the types of errors; full class name :3
			Map<String, Integer> errorCounts = new HashMap<String, Integer>();
			for (CrashReport c : ErrorHandler.errors) {
				Integer i = errorCounts.get(c.getClass().getName());
				if (i == null) {
					i = 0;
				}
				i++;
				//Once again, not sure if doing i++ will work.
				errorCounts.put(c.getClass().getName(), i);
			}
			
			//Now convert it to a set of plotters.
			for (Map.Entry<String, Integer> entry : errorCounts.entrySet()) {
				errorTypes.addPlotter(new IntPlotter(entry.getKey(), entry.getValue()));
			}
			
			//WitherWarner text.
			Graph witherWarnerEnabled = metrics.createGraph("Wither Warner Enabled");
			
			witherWarnerEnabled.addPlotter(
					new BooleanPlotter("Wither Warner Enabled",
							WitherWarner.enabled));
			
			metrics.start();
		} catch (IOException e) {
			metrics = null;
			Bukkit.getConsoleSender().sendMessage("§c[SBE] An error " +
					"occured whilst initializing pluginmetrics: ");
			Bukkit.getConsoleSender().sendMessage("§c" + e.toString());
			PlayerPrintStream output = new PlayerPrintStream(
					Bukkit.getConsoleSender(), "§c");
			e.printStackTrace(output);
			Bukkit.getConsoleSender().sendMessage("§cWhile the plugin " +
					"will still be run, there may be other errors due " +
					"to attempting to access a null metrics.  " + 
					"Please keep this in mind.");
		}
	}
}
