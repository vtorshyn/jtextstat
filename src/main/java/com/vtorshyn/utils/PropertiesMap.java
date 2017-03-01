package com.vtorshyn.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;

import com.vtorshyn.exceptions.HelpException;
import com.vtorshyn.exceptions.WordMapException;

public class PropertiesMap extends HashMap<String,String>{

	private static final long serialVersionUID = 1L;
	private Logger logger;

	public PropertiesMap(Logger logger, String[] strings, String delimiter, int expectedPropertiesCount) throws WordMapException, HelpException {
		super();
		this.logger = logger;
		parse(strings, delimiter, expectedPropertiesCount);
	}
	
	
	private PropertiesMap bindIntegerProperties(Class<? extends Object> _class, Object o, Field f, Annotation a) throws WordMapException, Exception {
		if (a instanceof IntegerCommandLineOption) {
			IntegerCommandLineOption i = (IntegerCommandLineOption) a;
			int v = i.defaultValue();
			if (containsKey(f.getName())) {
				v = intValue(f.getName());
			} else {
				if (i.mandatory()) {
					throw new WordMapException(
							_class.getName() 
							+ ": Property missed: " 
							+ f.getName()
						);
				}
			}
			logger.log(LogLevels.LOG_DEBUG, 
					_class.getName() + 
							"::" + 
							f.getName() + 
							"::" + v);
			f.set(o, new Integer(v));
		}
		return this;
	}
	
	public PropertiesMap bind(Object o) throws WordMapException, Exception {
		Class _class = o.getClass();
		if (null == _class) {
			throw new WordMapException(
					this.getClass().getName() + 
					"::bind() - Bind parameter 0 is NULL");
		}
		Field[] fields = _class.getFields();//getDeclaredFields();
		logger.log(LogLevels.LOG_DEBUG1, "bind::(T="+_class.getName()+", Object=" + o.toString() + ").fields=" + fields.length);		
		for (Field f : fields) {
			Annotation [] allAnnotations = f.getDeclaredAnnotations();
			logger.log(LogLevels.LOG_DEBUG2, "bind::(T="+_class.getName()+", Object=" + o.toString() + ").annotations=" + allAnnotations.length);
			for (Annotation a : allAnnotations) {
				logger.log(LogLevels.LOG_DEBUG, "bind::(T="+_class.getName()+", Object=" + o.toString());
				bindIntegerProperties(_class, o, f, a);
				bindStringProperties(_class, o, f, a);

			}
		}
		return this;
	}
	
	private PropertiesMap bindStringProperties(Class<? extends Object> _class, Object o, Field f, Annotation a) throws WordMapException, Exception {
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
		return this;
		
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
	 * @throws WordMapException 
	 * - if unknown option supplied
	 * @throws HelpException 
	 */
	public void parse(String[] strings, String delim, int expectedLen) throws WordMapException, HelpException {
		String option = null;
		int len = strings.length;
		
		for(String v : strings) {
			if (!v.startsWith(delim)) {
				if (null != option) {
					this.put(option, v);
					option = null;
					continue;
				}
				throw new WordMapException("Unknown option: " + v);
			} else if (null != option) {
				this.put(option, "");
			}
			option = v.replaceFirst("-", "");
			
			if ("help".equals(option)) {
				throw new HelpException("help"); // Funny ha?
			}
		}
		if (len < expectedLen)
			throw new WordMapException(
					this.getClass().getName() 
						+ " : Invalid number of arguments. Expected at least: " 
						+ expectedLen
						);
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

