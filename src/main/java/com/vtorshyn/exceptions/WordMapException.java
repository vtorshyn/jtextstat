package com.vtorshyn.exceptions;

/**!
 * 
 * An exception thrown whenever command line options
 * provided is not sufficient or missed.
 */
public class WordMapException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public WordMapException(String reason) {
		super(reason);
	}
}
