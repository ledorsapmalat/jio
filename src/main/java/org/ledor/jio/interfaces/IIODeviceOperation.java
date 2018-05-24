/**
 * 
 */
package org.ledor.jio.interfaces;

import org.ledor.jio.exception.CIOException;

/**
 * @author ledorsapmalat
 *
 */
public interface IIODeviceOperation {

	public void listen() throws CIOException;

	public void issue() throws CIOException;

	public IIOEvent getEvent() throws CIOException;

	public boolean isTerminated();

}
