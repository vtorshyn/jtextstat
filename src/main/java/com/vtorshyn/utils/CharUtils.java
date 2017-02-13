package com.vtorshyn.utils;

/**
 * !
 * 
 * @author vtorshyn
 *
 */
public class CharUtils {
	/**
	 * ! Simple and fast enough check if @param _ch is alphanumeric.
	 * 
	 * @param _ch
	 *            - an char object to be examined.
	 * @return true if (a-zA-Z0-9), false otherwise
	 */
	public static boolean isazAZ09(char _ch) {
		if ((_ch >= 'a' && _ch <= 'z') 
				|| (_ch >= 'A' && _ch <= 'Z') 
				|| (_ch >= '0' && _ch <= '9')) {
			return true;
		}
		return false;
	}
}
