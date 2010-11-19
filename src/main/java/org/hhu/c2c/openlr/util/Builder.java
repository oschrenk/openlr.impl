package org.hhu.c2c.openlr.util;

/**
 * Descibes a builder used for creating objects. The intention is to abstract
 * steps of construction of objects so that different implementations of these
 * steps can construct different representations of objects.
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * 
 * @param <B>
 *            The class of the builder for use in a fluid interface
 * @param <O>
 *            The class of the object being built
 */
public interface Builder<B, O> {

	/**
	 * 
	 * @return the final object
	 * @throws LocationReferenceException
	 */
	O build() throws LocationReferenceException;

	/**
	 * Resets the construction
	 * 
	 * @return The class of the builder for use in a fluid interface
	 */
	B reset();

	/**
	 * Checks if the current state of the object being built describes a valid
	 * object
	 * @throws LocationReferenceException 
	 */
	void validate() throws LocationReferenceException;
}
