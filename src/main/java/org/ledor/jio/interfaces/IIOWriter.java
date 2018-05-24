/**
 * 
 */
package org.ledor.jio.interfaces;

import org.ledor.jio.exception.CIOException;

/**
 * @author ledorsapmalat
 * Interface for All IO Writer
 *
 */
public interface IIOWriter extends IIODevice {

	/**
	 * 
	 * @param data
	 * @throws CIOException
	 */
	public void write(String data) throws CIOException;
	
	/**
	 * 
	 * @param data
	 * @throws CIOException
	 */
	public void write(String... data)throws CIOException;
	
	/**
	 * 
	 * @param port
	 * @param data
	 * @throws CIOException
	 */
	public void write(String port, String data) throws CIOException;
	
	/**
	 * 
	 * @param port
	 * @param data
	 * @throws CIOException
	 */
	public void write(String port, String... data) throws CIOException;
}
