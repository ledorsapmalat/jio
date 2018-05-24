/**
 * 
 */
package org.ledor.jio.interfaces.thread;

import java.util.concurrent.Callable;

/**
 * @author ledorsapmalat
 *
 */
public interface IIOPriority<V> extends Callable<V> {
	int getPriority();
}
