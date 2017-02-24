package com.vtorshyn.utils;
/**!
 * 
 * @author vtorshyn
 * Just a simple wrapper for logging
 */
public class Logger {
	private OptionsMap options;
	private static Logger instance = null;
	
	private Logger(OptionsMap options) {
		this.options = options;
	}
	/**!
	 * 
	 * @param options
	 * @return
	 */
	public static Logger init(OptionsMap options) {
		if (null == Logger.instance) {
			Logger.instance = new Logger(options);
		}
		return get();
	}
	
	public static Logger get() {
		if (null == Logger.instance) {
			Logger.instance = new Logger(null);
		}
		return Logger.instance; 
	}
	
	public void log(String what) {
		System.out.println(what);
	}
	
	public void log(int level, String what) {
		if (level > 0) {
			log(what);
		}
	}
}
