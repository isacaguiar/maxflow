/*
 * Created on 04.01.2004
 */
package org.jbpm.gpd.exception;

/**
 * @author Jan Philipp Bolle
 * The JGraph model is not valid
 */
public class InvalidModelException extends Exception {

	/**
	 * @param string
	 */
	public InvalidModelException(String string) {
		super (string);
	}

}
