/**
 * 
 */
package org.ledor.jio;

import java.util.ArrayList;

import org.ledor.jio.command.AIOData;
import org.ledor.jio.exception.CIOException;
import org.ledor.util.CDataConverter;
import org.slf4j.LoggerFactory;


/**
 * This wrapper class will be used to encapsulate a USB Credit Card Reader Device. 
 * Commands are based on Secure MOIR Encrypted Magnetic-Only Insert Reader Device.
 * 
 * @author ledorsapmalat
 *
 */
public class CUSBCreditCardReader extends CUSBCardReader {

	public CUSBCreditCardReader(short vendorId, short productId) throws CIOException {
		super(vendorId, productId);
		LOGGER_COMMAND = LoggerFactory.getLogger("cardCommand");
		RESP_DATA_SIZE = 9;
	}
	
	@Override
	protected void init(){
		// Need to think if this can be handled by ENUMS
		AIOData eATcommand = new CCreditCardRequestBuilder()
			.command("531A0131")
			.payloadSize("0004").build();
		
		AIOData dCRcommand = new CCreditCardRequestBuilder()
			.command("531A0130")
			.payloadSize("0004").build();
		
		AIOData eENCcommand = new CCreditCardRequestBuilder()
				.command("53850100")
				.payloadSize("0004").build();
		
		AIOData eRWcommand = new CCreditCardRequestBuilder()
			.command("531D0103")
			.payloadSize("0004").build();
		
		AIOData eRWOcommand = new CCreditCardRequestBuilder()
				.command("531D0104")
				.payloadSize("0004").build();
		
		AIOData eBIWcommand = new CCreditCardRequestBuilder()
				.command("531D0101")
				.payloadSize("0004").build();
		
		AIOData eEOcommand = new CCreditCardRequestBuilder()
				.command("53840108")
				.payloadSize("0004").build();
		
		AIOData eHOcommand = new CCreditCardRequestBuilder()
				.command("535C0107")
				.payloadSize("0004").build();
		
		AIOData eMOcommand = new CCreditCardRequestBuilder()
				.command("53860107")
				.payloadSize("0004").build();
		
		AIOData authcommand = new CCreditCardRequestBuilder()
				.command("5280")
				.payloadSize("0002").build();
		
		AIOData replycommand = new CCreditCardRequestBuilder()
				.command("5382")
				.payloadSize("0002").build();
		
		AIOData resetcommand = new CCreditCardRequestBuilder()
				.command("49")
				.payloadSize("0001").build();
		
		AIOData securityCommand = new CCreditCardRequestBuilder()
				.command("527E")
				.payloadSize("0002").build();
		
		AIOData encryptionCommand = new CCreditCardRequestBuilder()
				.command("524C")
				.payloadSize("0002").build();
		
		AIOData formatCommand = new CCreditCardRequestBuilder()
				.command("522F")
				.payloadSize("0002").build();
		
		enableAutoTransmit.append(eATcommand.create());
		enableReportOnWithdrawal.append(eRWcommand.create());
		enableReportOnWithdrawalOnly.append(eRWOcommand.create());
		enableReportOnBothInsertAndWithdrawal.append(eBIWcommand.create());
		enableEnhanceEncryption.append(eENCcommand.create());
		enableEncrpyptionOption.append(eEOcommand.create());
		enableHashOption.append(eHOcommand.create());
		enableMaskOption.append(eMOcommand.create());
		disableReader.append(dCRcommand.create()); 
		resetReader.append(resetcommand.create());
		enableActAuthOption.append(authcommand.create());
		enableActReplyOption.append(replycommand.create());
		
		enableSecurityLevel.append(securityCommand.create());
		enableEncryptionLevel.append(encryptionCommand.create());
		enableFormatLevel.append(formatCommand.create());
	}

	public String getEnableEncryptionOption(){
		return enableEncrpyptionOption.toString();
	}
	
	public String enableHashOption(){
		return enableHashOption.toString();
	}
	
	public String enableMaskOption(){
		return enableMaskOption.toString();
	}
	
	/**
	 * @return the enableAutoTransmit
	 */
	public String getEnableAutoTransmit() {
		return enableAutoTransmit.toString();
	}

	/**
	 * @return the enableReportOnWithdrawal
	 */
	public String getEnableReportOnWithdrawal() {
		return enableReportOnWithdrawal.toString();
	}
	
	/**
	 * @return the enableReportOnWithdrawal
	 */
	public String getEnableReportOnWithdrawalOnly() {
		return enableReportOnWithdrawalOnly.toString();
	}
	
	/**
	 * @return the enableReportOnWithdrawal
	 */
	public String getEnableBothInsertAndWithdrawal() {
		return enableReportOnBothInsertAndWithdrawal.toString();
	}
	
	/**
	 * @return the enableSecurityLevel
	 */
	public String getSecurityLevel() {
		return enableSecurityLevel.toString();
	}
	
	/**
	 * @return the enableEncryptionLevel
	 */
	public String getEncryptionLevel() {
		return enableEncryptionLevel.toString();
	}

	
	/**
	 * @return the enableFormatLevel
	 */
	public String getFormatLevel() {
		return enableFormatLevel.toString();
	}


	/**
	 * @return the disableReader
	 */
	public String getDisableReader() {
		return disableReader.toString();
	}
	
	/**
	 * @return the enableEnhanceEncryption
	 */
	public String getEnableEnhanceEncryption() {
		return enableEnhanceEncryption.toString();
	}
	
	public String getEnableActAuthentication(){
		return enableActAuthOption.toString();
	}
	
	public String getEnableActReplyOption(){
		return enableActReplyOption.toString();
	}
	
	// reset 
	public String getResetReader() {
		return resetReader.toString();
	}
	
	protected byte[] retrieveResponse(String response) throws Exception{
		StringBuffer cmdResp = new StringBuffer();
		int sizeCnt = 0;
		int sizePayload = 0;
		int payloadCnt = 0;
		StringBuffer cmdLen = new StringBuffer();
		boolean startResp = false;
		boolean startPayload = false;
		
		for (byte b: CDataConverter.toBytes(response.toString())){
			if (b==0x60 && !startResp){
				startResp = true;
				cmdResp.append(CDataConverter.stringHex(b,1));
			} else if (startResp && !startPayload){
				if (sizeCnt<2){
					cmdLen.append(b==0x00?"00":CDataConverter.stringHex(b,1));
					cmdResp.append(b==0x00?"00":CDataConverter.stringHex(b,1));
					sizeCnt++;
					if (sizeCnt==2){
						sizePayload = Integer.parseInt(cmdLen.toString().trim(), 16 );
						startPayload = true;
					}
				} 
			} else if (startPayload){
				payloadCnt++;
				if (payloadCnt<=sizePayload+2){ 
					cmdResp.append(b==0x00?"00":CDataConverter.stringHex(b,1));
				} 
			}
		}
		return CDataConverter.toBytes(cmdResp.toString());
	}
	
	public ArrayList<byte[]> read() throws CIOException {
		if (hidDevice != null) {
			ArrayList<byte[]> responses;
			try {
				responses = new ArrayList<byte[]>();
				boolean moreData = true;
				StringBuffer cmdResp = new StringBuffer();
				int sizeCnt = 0;
				int sizePayload = 0;
				int payloadCnt = 0;
				StringBuffer cmdLen = new StringBuffer();
				boolean startResp = false;
				boolean startPayload = false;
				hidDevice.setNonBlocking(false);
				while (moreData) {
					byte resp[] = new byte[maxPacketSize];
					// This method will now block 
					int val = hidDevice.read(resp,2000);
					switch (val) {
						case -1:
							throw new CIOException("Error Read : " + hidDevice.getLastErrorMessage());
						case 0: moreData = false; break;
						default:
							for (byte b: resp){
								if (b==0x60 && !startResp){
									startResp = true;
									cmdResp.append(CDataConverter.stringHex(b,1));
								} else if (startResp && !startPayload){
									if (sizeCnt<2){
										cmdLen.append(b==0x00?"00":CDataConverter.stringHex(b,1));
										cmdResp.append(b==0x00?"00":CDataConverter.stringHex(b,1));
										sizeCnt++;
										if (sizeCnt==2){
											sizePayload = Integer.parseInt(cmdLen.toString().trim(), 16 );
											startPayload = true;
											sizeCnt = 0;
										}
									} 
								} else if (startPayload){
									payloadCnt++;
									if (payloadCnt<=sizePayload+2){ 
										cmdResp.append(b==0x00?"00":CDataConverter.stringHex(b,1));
									} else{
										responses.add(CDataConverter.toBytes(cmdResp.toString()));
										// reset all
										cmdResp = new StringBuffer();
										cmdLen = new StringBuffer();
										startResp = false;
										startPayload = false;
										sizeCnt = 0;
										payloadCnt = 0;
										sizePayload = 0;
									}
								}
							}
							break;
					}
				}
				return responses;
			} catch (Exception e) {
				throw new CIOException(e);
			}
		}
		return null;
	}
}
