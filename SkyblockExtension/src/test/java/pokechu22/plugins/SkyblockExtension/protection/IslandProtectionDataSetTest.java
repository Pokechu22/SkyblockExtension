package pokechu22.plugins.SkyblockExtension.protection;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.junit.Test;

/**
 * Test of islandProtectionDataSets.  This test uses owner 
 * as the default permissions.
 *
 * @author Pokechu22
 *
 */
public class IslandProtectionDataSetTest {
	IslandProtectionDataSet set;
	
	/**
	 * Get a default value.
	 */
	{
		ConfigurationSerialization.registerClass(IslandProtectionDataSet.class, 
				"IslandProtectionDataSet");
		YamlConfiguration cfg = new YamlConfiguration();
		try {
			cfg.load(this.getClass().getResource("/default_protection.yml").openStream());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		set = (IslandProtectionDataSet) cfg.get("owner");
	}
	
	@Test
	public void testSerialization() {
		assertThat(IslandProtectionDataSet.deserialize(set.serialize()), is(set));
	}
	
	@Test
	public void test() {
		
	}
}
