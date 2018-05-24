/**
 * 
 */
package org.ledor.jio;

import org.ledor.jio.command.AIOData;
import org.ledor.jio.constant.CCreditCardConstants;
import org.ledor.jio.interfaces.IIOCreditCardData;
import org.ledor.util.CCRCGenerator;
import org.ledor.util.CDataConverter;

/**
 * @author ledorsapmalat
 *
 */
public abstract class AIOCreditCardData extends AIOData implements IIOCreditCardData {
	public String getHeader() {
		return CCreditCardConstants.HEADER_STRING;
	}

	public String getFooter() {
		return CCreditCardConstants.END_STRING;
	}

	/**
	 * Retrieve Middle Command Strings
	 * 
	 * @return
	 */
	private String forLRC() {
		StringBuilder sb = new StringBuilder();
		sb.append(getHeader());
		sb.append(getPayloadSize());
		sb.append(getCommand());
		return sb.toString().toLowerCase();
	}

	/**
	 * @return the checkSum
	 */
	@Override
	public String getCheckSum() {
		int[] forLRC = {};
		String compLRC = null;
		try {
			forLRC = CDataConverter.toUBytes(forLRC());
			compLRC = CDataConverter.stringHex(CCRCGenerator.computeLRC(forLRC, forLRC.length));
		} catch (Exception e) {
			LOGGER.error(">>>>> ISSUE FOUND: {}", e.getMessage());
		}
		return compLRC;
	}
	
	/**
	 * create the command
	 */
	@Override
	public String create(){
		StringBuilder sb = new StringBuilder();
		sb.append(getHeader());
		sb.append(getPayloadSize());
		sb.append(getCommand());
		sb.append(getCheckSum());
		sb.append(getFooter());
		return sb.toString().toLowerCase();		
	}

}
