/**
 * 
 */
package org.ledor.jio.interfaces;

import org.ledor.jio.exception.CIOException;

/**
 * @author ledorsapmalat
 *
 */
public interface IIODevice {
	
	public void open() throws CIOException;
	public void close() throws CIOException;

}
