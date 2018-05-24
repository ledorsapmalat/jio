/**
 * 
 */
package org.ledor.jio.exception;

/**
 * @author ledorsapmalat
 *
 */
public class CIOException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CIOException(Throwable e) {
		super(e);
	}

	public CIOException(String string) {
		super(string);
	}

}
