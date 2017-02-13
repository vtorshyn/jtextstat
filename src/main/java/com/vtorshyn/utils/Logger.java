package com.vtorshyn.utils;
/**!
 * 
 * @author vtorshyn
 * Just a simple wrapper for logging
 */
public class Logger {
	private ApplicationOptions options;
	private static Logger instance = null;
	
	private Logger(ApplicationOptions options) {
		this.options = options;
	}
	/**!
	 * 
	 * @param options
	 * @return
	 */
	public static Logger initLogger(ApplicationOptions options) {
		if (null == Logger.instance) {
			Logger.instance = new Logger(options);
		}
		return Logger.instance;
	}
	
	public void log(String what) {
		System.out.println(what);
	}
}
