package org.ledor.jio.interfaces;

/**
 * @author ledorsapmalat
 *
 */
public interface IIOData {

	public String getHeader();
	public String getPayloadSize();
	public void setPayloadSize(String size);
	public String getCommand();
	public void setCommand(String command);

}

