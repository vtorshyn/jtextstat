package com.vtorshyn.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.vtorshyn.exceptions.WordMapException;

/**!
 * 
 * @author vtorshyn
 * Just a simple wrapper for logging
 */
public class Logger {
	
	@IntegerCommandLineOption(defaultValue=1, mandatory=false, help="If you want to see more information, please lower debug level. With level 0 lots of debug messages will be dumped.")
	public Integer debugLevel = 1;

	/**!
	 * Constructs logger object. 
	 * 
	 * Please use {@link Logger#bind(PropertiesMap)} if you want to override default log level. 
	 * @throws WordMapException
	 */
	public Logger() {
	}

	public void log(String what) {
		System.out.println(what);
	}
	
	public void log(LogLevels level, String what) {
		/*if (level.compareTo(LogLevels.LOG_DEBUG) == 0) {
			log(this.getClass().getName() + "::log::LogLevel("+level+")::log("+what + ")");
		}*/
		
		if (level.intValue() <= debugLevel) {
			log(what);
		}
	}
	
	public synchronized void logSync(LogLevels level, String what) {
		log(level, what);
	}
	
	@SuppressWarnings("rawtypes")
	public void logSupportedProperties(Class _class) {
		Field[] fields = _class.getDeclaredFields();
		for (Field f : fields) {
			Annotation [] allAnnotations = f.getDeclaredAnnotations();
			for (Annotation a : allAnnotations) {
				if (a instanceof IntegerCommandLineOption) {
					IntegerCommandLineOption i = (IntegerCommandLineOption) a;
					int v = i.defaultValue();
					log(LogLevels.LOG_MESSAGE, 
							"-"+f.getName()+ " <integer value> - " + i.help() + " (default is \"" + v + "\")");
				}
				if (a instanceof StringCommandLineOption) {
					StringCommandLineOption s = (StringCommandLineOption) a;
					String v = s.defaultValue();
					log(LogLevels.LOG_MESSAGE, 
							"-"+f.getName()+ " <string> - " + s.help() + " (default=" + v + ")");
				}
			}
		}
	}
}
