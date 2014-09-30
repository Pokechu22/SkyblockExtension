package pokechu22.plugins.SkyblockExtension.util.mcstats;

import java.io.IOException;

import org.bukkit.Bukkit;

import pokechu22.plugins.SkyblockExtension.SkyblockExtension;
import pokechu22.plugins.SkyblockExtension.util.PlayerPrintStream;
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
	
	/**
	 * Starts Metrics and adds the needed data.
	 */
	public static void start() {
		try {
	        metrics = new Metrics(SkyblockExtension.inst());
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
