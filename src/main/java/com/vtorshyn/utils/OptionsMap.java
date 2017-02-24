package com.vtorshyn.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;

import com.vtorshyn.WordMapBuilder;
import com.vtorshyn.exceptions.CommandLineException;
import com.vtorshyn.word.processor.WordProcessorBuilder;
import com.vtorshyn.word.processor.WordsRangeReader;
import com.vtorshyn.word.processor.WordProcessor;


public class OptionsMap extends HashMap<String,String>{

	private static final long serialVersionUID = 1L;
	private static OptionsMap instance = null;

	private OptionsMap(String[] stringList) throws CommandLineException {
		super();
		loadFromStringArray(stringList, "-");
	}
	
	@SuppressWarnings("rawtypes")
	public void help(Class _class) {
		Field[] fields = _class.getDeclaredFields();
		for (Field f : fields) {
			Annotation [] allAnnotations = f.getDeclaredAnnotations();
			for (Annotation a : allAnnotations) {
				if (a instanceof IntegerCommandLineOption) {
					IntegerCommandLineOption i = (IntegerCommandLineOption) a;
					int v = i.defaultValue();
					Logger.get().log("-"+f.getName()+ " <integer> - " + i.help() + " (default=" + v + ")");
				}
				if (a instanceof StringCommandLineOption) {
					StringCommandLineOption s = (StringCommandLineOption) a;
					String v = s.defaultValue();
					Logger.get().log("-"+f.getName()+ " <string> - " + s.help() + " (default=" + v + ")");
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void bind(Class _class, Object o) throws Exception {
		if (null == _class) {
			
		}
		Field[] fields = _class.getDeclaredFields();
		for (Field f : fields) {
			Annotation [] allAnnotations = f.getDeclaredAnnotations();
			for (Annotation a : allAnnotations) {
				if (a instanceof IntegerCommandLineOption) {
					IntegerCommandLineOption i = (IntegerCommandLineOption) a;
					int v = i.defaultValue();
					if (containsKey(f.getName())) {
						v = intValue(f.getName());
					} else {
						if (i.mandatory()) {
							// TODO: bad stuff
							throw new Exception("Mandatory options missed: " + f.getName());
						}
					}
					f.set(o, new Integer(v));

				}
				if (a instanceof StringCommandLineOption) {
					StringCommandLineOption s = (StringCommandLineOption) a;
					if (f.getType() == s.type()) {
						String v = s.defaultValue();
						if (containsKey(f.getName())) {
							v = value(f.getName());
						} else {
							if (s.mandatory()) {
								// TODO: bad stuff
								throw new Exception("Mandatory options missed: " + f.getName());
							}
						}
						f.set(o, new String(v));
					} else {
						// TODO: bad stuff 
						throw new Exception("Wrong annotation type for: " + f.getName());
					}
				}
			}
		}
	}
	
	public static OptionsMap init(String[] stringList) throws CommandLineException {
		if (null == OptionsMap.instance) {
			OptionsMap.instance = new OptionsMap(stringList);
		}
		return OptionsMap.instance;
	}
	
	public static OptionsMap get() throws CommandLineException {
		if (null == OptionsMap.instance) {
			throw new CommandLineException("Has not been initialized yet");
		}
		return OptionsMap.instance;
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
			if ("help".equals(option)) {
				help(WordsRangeReader.class);
				help(WordProcessorBuilder.class);
				help(WordProcessor.class);
				help(WordMapBuilder.class);
				System.exit(0);
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
}

