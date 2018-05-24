/**
 * 
 */
package org.ledor.jio.command;

/**
 * @author ledorsapmalat
 *
 */
public abstract class AIODataBuilder {

	protected AIOData command;
	
	public AIOData build(){
		return command;
	}
	
	public AIODataBuilder command(String commandStr){
		command.setCommand(commandStr);
		return this;
	}
	
	public AIODataBuilder payloadSize(String payloadSize){
		command.setPayloadSize(payloadSize);
		return this;
	}	

}
