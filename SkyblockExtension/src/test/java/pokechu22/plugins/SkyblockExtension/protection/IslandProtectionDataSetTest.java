package pokechu22.plugins.SkyblockExtension.protection;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class IslandProtectionDataSetTest {
	/**
	 * Tests de- and re-serialization of sets. 
	 */
	@Test
	public void serializationTest() {
		IslandProtectionDataSet set = 
				IslandProtectionDataSetFactory.getDefaultValue(MembershipTier.owner);
		IslandProtectionDataSet set2 = new IslandProtectionDataSet();
		set2.deserializeFromNBT(set.serializeToNBT());
		
		assertThat(set2, is(set));
	}
}
