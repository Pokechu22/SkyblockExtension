package pokechu22.plugins.SkyblockExtension.protection.flags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Base flag type that provides general list flag actions. 
 * 
 * @author Pokechu22
 *
 * @param <E> The enumeration to use for this flag.
 */
public abstract class ListFlag<E extends Enum<E>> extends
		IslandProtectionDataSetFlag {
	
	protected ArrayList<E> value;
	
	/**
	 * {@inheritDoc}
	 * <hr>
	 * The available actions are:
	 * <!-- TODO I don't know if using list items like this is OK, but it
	 *      seems to work right now.  I want a bulleted list here. --> 
	 * <ul><dl>
	 * <li><dt><code>set</code></dt></li>
	 * <dd>
	 * Sets the value (same as {@link #setValue(String)}).
	 * Expects a list in the usual format.
	 * </dd>
	 * <li><dt><code>get</code></dt></li>
	 * <dd>Gets the value (same as {@link #getDisplayValue()}).</dd>
	 * <li><dt><code>add</code></dt></li>
	 * <dd>Adds a single element. Warns if that element already exists.</dd>
	 * <li><dt><code>addmultiple</code></dt></li>
	 * <dd>
	 * Adds multiple elements.
	 * This list of elements is in the usual list format.<br>
	 * Already-existing elements are illegal and result in cancellation of 
	 * the entire action.  (To avoid this, use <code>addmultiple-f</code>.)
	 * </dd>
	 * <li><dt><code>addmultiple-f</code></dt></li>
	 * <dd>
	 * Adds multiple elements.
	 * This list of elements is in the usual list format.<br>
	 * Already-existing elements are allowed.
	 * </dd>
	 * <li><dt><code>remove</code></dt></li>
	 * <dd>
	 * Removes a single element.<br>
	 * If that element already exists, an error is presented.
	 * </dd> 
	 * <li><dt><code>removemultiple</code></dt></li>
	 * <dd>
	 * Removes multiple elements.
	 * This list of elements is in the usual list format.<br>
	 * Already-nonexisting elements are illegal and result in cancellation
	 * of the entire action. (To avoid this, use
	 * <code>removemultiple-f</code>.)
	 * </dd>
	 * <li><dt><code>removemultiple-f</code></dt></li>
	 * <dd>
	 * Adds multiple elements.
	 * This list of elements is in the usual list format.<br>
	 * Already-nonexisting elements are allowed.</dd>
	 * </dl></ul>
	 */
	@Override
	public List<String> getActions() {
		return Arrays.asList(new String[]{
				"set",
				"get",
				"add",
				"addmultiple",
				"addmultiple-f",
				"remove",
				"removemultiple",
				"removemultiple-f"
				});
	}
	
	@Override
	public String getSerializedValue() {
		return value.toString();
	}

	@Override
	public String getDisplayValue() {
		return value.toString();
	}
	
	@Override
	public String setValue(String value) {
		if (value == null) {
			return "§cValue cannot be null!";
		}
		
		//If there is only 1 [ and 1 ], and both are at the start 
		//and end of the strings...
		if (!((value.indexOf("[") == 0) 
				&& (value.indexOf("[", 1) == -1)
				&& (value.indexOf("]") == (value.length() - 1)))) {
			return "§cList format is invalid: It must start with " +
					"'[' and end with ']', and not have any '['" + 
					" or ']' anywhere else in it.";
		}
		String[] valueStrings =
				value.substring(1, value.length() - 1)
				.split(",");
		ArrayList<E> tempList = new ArrayList<>();
		for (int i = 0; i < valueStrings.length; i++) {
			//Skip empty values.
			if (valueStrings[i].isEmpty()) {
				continue;
			}
			
			E entity = matchEnumValue(valueStrings[i].trim());
			if (entity == null) {
				return "§c" + getTypeName(false) + "\"" + valueStrings[i] +
						"\" is not recognised.\n(Location: " + 
						//Bolds the error.
						value.replaceAll(valueStrings[i], 
								"§4§l" + valueStrings[i] + 
								"§r§c") + ")\n" + 
						"§cTry using tab completion next time.";
			}
			
			if (tempList.contains(entity)) {
				return "§c" + getTypeName(false) + "\"" + valueStrings[i] + 
						"\" is already entered.  (Repeat entries " +
						"are not allowed).\n(Location: " + 
						//Bolds the error.
						value.replaceAll(valueStrings[i], 
								"§4§l" + valueStrings[i] + 
								"§r§c") + ")";
			}
			
			tempList.add(entity);
			
		}
		
		//Now handle the field.
		this.value = tempList;
		
		return "§aFlag set successfully.";
	}
	
	@Override
	public String preformAction(String action, String[] args) {
		String singleArgs;
		//Wrapping to control visibility!
		{
			StringBuilder m = new StringBuilder();
			for (int i = 0; i < args.length; i++) {
				m.append(args[i]);
				if (i != args.length - 1) { //If NOT final value
					m.append(" ");
				}
			}
			singleArgs = m.toString();
		}
		
		switch (action) {
		case "set": {
			return setValue(singleArgs);
		}
		case "get": {
			return getDisplayValue();
		}
		case "add": {
			return addToValue(singleArgs, false);
		}
		case "add-f": {
			return addToValue(singleArgs, true);
		}
		}
		return "§c" + action + " is not a valid action for this flag!";
	}
	
	/**
	 * Matches an enum value, such that capitalization is ignored.
	 * This method may also take into account numeric IDs and such.
	 * Most enum types already have a method that handles this, but
	 * it is not part of the base enum class.
	 * 
	 * This method should not throw any exception, <i>unless
	 * <code>null</code> is provided as an argument</i>, in which case an 
	 * {@link IllegalArgumentException} or {@link NullPointerException}
	 * should be thrown.  Null should be returned when no such value exists
	 * in the enum.
	 * 
	 * @param valueToMatch
	 * @return An enum value.
	 */
	protected abstract E matchEnumValue(String valueToMatch)
			throws IllegalArgumentException, NullPointerException;
	
	/**
	 * Gets the name of the type to use here.
	 * This is used in error message outputs.
	 * <br>
	 * For example, EntityListFlag would return "entity".<br>
	 * TODO: This seems like a rather inelegant solution.  Find 
	 * something better.
	 * 
	 * @param capitalize Whether or not the first letter should be
	 *                   capitalized.
	 * @return The singular form of the thing that is listed.
	 */
	protected abstract String getTypeName(boolean capitalize);
}
