/**
 * 
 */
package org.ledor.jio;

import java.util.ArrayList;
import java.util.List;

import org.ledor.jio.command.AIOData;
import org.ledor.jio.exception.CIOException;
import org.ledor.util.CDataConverter;

/**
 * @author ledorsapmalat
 *
 */
public class CIDTechCreditCardReader {

	private CUSBCreditCardReader cardReader;

	public CIDTechCreditCardReader() throws CIOException {
		short vendor = (short) Integer.parseInt("0acd", 16);
		short product = (short) Integer.parseInt("2510", 16);
		cardReader = new CUSBCreditCardReader(vendor, product);
	}

	/**
	 * TODO: Read for Response
	 * @throws CIOException
	 */
	public void open() throws CIOException {
		if (cardReader != null) {
			cardReader.sendCommand(cardReader.getSecurityLevel());
			cardReader.sendCommand(cardReader.getEncryptionLevel());
			cardReader.sendCommand(cardReader.getFormatLevel());
			cardReader.sendCommand(cardReader.getEnableAutoTransmit());
			cardReader.sendCommand(cardReader.getEnableReportOnWithdrawalOnly());
			cardReader.sendCommand(cardReader.getEnableEnhanceEncryption());
			cardReader.sendCommand(cardReader.enableHashOption());
			cardReader.sendCommand(cardReader.enableMaskOption());
			cardReader.sendCommand(cardReader.getEnableEncryptionOption());
		}
	}

	/**
	 * TODO: Read for Response
	 * @throws CIOException
	 */
	public void close() throws CIOException {
		if (cardReader != null) {
			cardReader.sendCommand(cardReader.getDisableReader());
			cardReader.unplug();
		}
	}

	/**
	 * @throws CIOException
	 */
	public List<AIOData> read() throws CIOException {

		List<AIOData> responses = new ArrayList<AIOData>();
		if (cardReader != null) {
			for (byte[] byt : cardReader.read()) {
				AIOData response = new CCreditCardResponseParser().swipe().data(CDataConverter.stringHex(byt)).parse();
				responses.add(response);
			}
		}
		return responses;
	}
}
