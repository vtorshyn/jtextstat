package com.vtorshyn.exceptions;

/**!
 * 
 * An exception thrown whenever command line options
 * provided is not sufficient or missed.
 */
public class CommandLineException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public CommandLineException(String reason) {
		super(reason);
	}
}
