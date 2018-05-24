/**
 * 
 */
package org.ledor.jio.command;

import org.ledor.jio.interfaces.IIOData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author ledorsapmalat
 *
 */
public abstract class AIOData implements IIOData {
	protected static Logger LOGGER = LoggerFactory.getLogger((String) AIOData.class.getName());
	protected String header; 
	protected String payloadSize;
	protected String command;
	protected String checkSum;
	
	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}
	
	/**
	 * @return the payloadSize
	 */
	public String getPayloadSize() {
		return payloadSize;
	}

	/**
	 * @param payloadSize the payloadSize to set
	 */
	public void setPayloadSize(String payloadSize) {
		this.payloadSize = payloadSize;
	}
	
	@Override
	public String toString() {
		return "Command String: " + create().toUpperCase();
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract String getCheckSum();
	
	/**
	 * 
	 * @return
	 */
	public abstract String create();
}
