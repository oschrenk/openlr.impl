package org.hhu.c2c.openlr.util;

/**
 * This exception indicates that an error has occured while performing a
 * validate operation.
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * 
 */
public class ValidationException extends Exception {

	/**
	 * The serial version uid
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see Exception
	 * @param string
	 */
	public ValidationException(final String string) {
		super(string);
	}
}
