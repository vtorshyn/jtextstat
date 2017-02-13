package com.vtorshyn.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.vtorshyn.exceptions.CommandLineException;

public class ApplicationOptions extends HashMap<String,String>{

	private static final long serialVersionUID = 1L;

	public ApplicationOptions(String[] mandatoryKeys, String[] stringList) throws CommandLineException {
		super();
		loadFromStringArray(stringList, "-");
		validateKeys(mandatoryKeys);
	}
	/**!
	 * Loads options into self.
	 * It detects options by using @param delim.
	 * 
	 * Supported formats:
	 * - options without parameters flags (e.g. -help) 
	 * - options with single parameter (-option parameter)
	 * - options with multiple parameters separated by space (-option "this is complex")
	 *
	 * @param strings - input String[], usually args from main()
	 * @param delim - delimiter used to detect option
	 * @throws CommandLineException 
	 * - if unknown option supplied
	 */
	public void loadFromStringArray(String[] strings, String delim) throws CommandLineException {
		String option = null;
		int len = strings.length,
				idx = 0;
		for (; idx < len; ++idx) {
			String v = strings[idx];
			if (!v.startsWith(delim)) {
				if (null != option) {
					this.put(option, v);
					option = null;
					continue;
				}
				throw new CommandLineException("Unknown option: " + v);
			} else if (null != option) {
				this.put(option, "");
				option = v.replaceFirst("-", "");
			} else {
				option = v.replaceFirst("-", "");
			}
		}
	}
	
	/**!
	 * Returns associated value for option  @param key
	 * @param key - an option name
	 * @return 
	 * - value for a option or null if key is not found
	 * - empty string for flags
	 */
	public String value(String key) {
		if (this.containsKey(key)) {
			return get(key);
		}
		return null;
	}
	/**!
	 * Converts value associated with @param key to integer
	 * and return it.
	 * If key is missed zero value returned.
	 * @param key
	 * @return 
	 * - integer representation of value associated with option 
	 */
	public int intValue(String key) {
		String v = value(key);
		if (null == v || v.length() == 0) {
			return 0;
		}
		return Integer.parseInt(v);
	}
	
	/**!
	 * Simple validation for required keys listed in @param keys.
	 * If some key from @param keys is missed, exception will be thrown.
	 * @param keys
	 * @return
	 * @throws CommandLineException
	 */
	public List<String> validateKeys(String[] keys) throws CommandLineException {
		List<String> missedKeys = null;
		
		for (String k : keys) {
			if (!containsKey(k)) {
				if (null == missedKeys) {
					missedKeys = new ArrayList<String>();
				}
				missedKeys.add(k);
			}
		}
		
		if (null != missedKeys) {
			throw new CommandLineException(
					"Missed command line options: " + missedKeys.toString());
		}
		return missedKeys;
	}
}
