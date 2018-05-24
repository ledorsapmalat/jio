/**
 * 
 */
package org.ledor.jio;

import javax.usb.UsbDevice;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;

import org.hid4java.HidDevice;
import org.hid4java.HidException;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;
import org.ledor.jio.exception.CIOException;
import org.ledor.util.CDataConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This wrapper class will be used to encapsulate a USB Card Reader Device. It
 * wraps two different USB API: usb4java and hid4java
 * 
 * @author ledorsapmalat
 *
 */
public class CUSBCardReader implements HidServicesListener {
	
	protected Logger LOGGER_COMMAND = LoggerFactory.getLogger(this.getClass());
	
	protected int RESP_DATA_SIZE = 9;

	protected UsbDevice device = null;
	protected HidServices hidServices;
	protected HidDevice hidDevice;
	protected int maxPacketSize;
	
	protected StringBuffer enableSecurityLevel = new StringBuffer();
	protected StringBuffer enableEncryptionLevel = new StringBuffer();
	protected StringBuffer enableFormatLevel = new StringBuffer();
	
	
	protected StringBuffer enableAutoTransmit = new StringBuffer();
	protected StringBuffer enableReportOnWithdrawal = new StringBuffer();
	protected StringBuffer enableReportOnWithdrawalOnly = new StringBuffer();
	protected StringBuffer enableReportOnBothInsertAndWithdrawal = new StringBuffer();
	protected StringBuffer enableEnhanceEncryption = new StringBuffer();
	protected StringBuffer enableHashOption = new StringBuffer();
	protected StringBuffer enableMaskOption = new StringBuffer();
	protected StringBuffer enableEncrpyptionOption = new StringBuffer();
	protected StringBuffer enableActAuthOption = new StringBuffer();
	protected StringBuffer enableActReplyOption = new StringBuffer();
	
	protected StringBuffer disableReader = new StringBuffer();
	protected StringBuffer resetReader = new StringBuffer();
	
	public CUSBCardReader(short vendorId, short productId)
			throws CIOException {
		try {
			device = CUSBDeviceManager.findDevice(UsbHostManager
					.getUsbServices().getRootUsbHub(), vendorId, productId);
			maxPacketSize = CUSBDeviceManager.getMaxPacketSize(device);
			hidServices = HidManager.getHidServices();
			hidServices.addHidServicesListener(this);
			hidDevice = hidServices.getHidDevice(vendorId, productId, null);
			
			init();
		} catch (SecurityException | UsbException | HidException e) {
			throw new CIOException(e);
		}
	}
	
	protected void init() {	}
	
	/**
	 * This is a wrapper for the set feature command of HID API
	 * 
	 * @param hexString
	 * @return
	 */
	public byte[] sendCommand(String hexString) throws CIOException {
		try {
			if (hidDevice != null) {
				byte[] messg = CDataConverter.toBytes(hexString);
				byte[] data = new byte[maxPacketSize]; // required to be 32
														// bytes

				int k = data.length;
				for (int i = messg.length - 1; i >= 0; i--) {
					data[(k--) - 1] = messg[i];
				}
				int val = hidDevice.sendFeatureReport(data, (byte) 0); // report
																		// id 0

				if (val < 0)
					throw new CIOException("Error Feature Report: "
							+ hidDevice.getLastErrorMessage());

				// wait for 50 ms for the card reader to respond
				Thread.sleep(50);

				StringBuffer buff = new StringBuffer();
				for (int j = 0; j < 100; j++) { // loop 100 times (old logic)
					byte[] resp = new byte[RESP_DATA_SIZE]; // > 8 in length
					val = hidDevice.getFeatureReport(resp, (byte) 0);
					
					if (val < 0)
						throw new CIOException("Error Feature Report: "
								+ hidDevice.getLastErrorMessage());
					
					boolean respFound = false;
					int idx = 1;
					int start = 0;

					// Error Response
					if (resp[0] == (byte) 0xE0) {
						byte[] msg = null;
						for (int h = 1; h < resp.length; h++)
							if (resp[h] != (byte) 0x00 && !respFound) {
								respFound = true;
								idx = h;
							} else if (respFound) {
								if (msg == null) {
									msg = new byte[resp.length - (idx + 1)];
								}
								msg[start++] = resp[h];
							}

						if (respFound)
							buff.append(CDataConverter.stringHex(msg).substring(0,
									CDataConverter.stringHex(msg).length() - 2));

					} else { // normal response
						String s = CDataConverter.stringHex(resp).substring(0,
								CDataConverter.stringHex(resp).length() - 2);
						buff.append(s);
					}
					Thread.sleep(1);
				}
				byte[] response = retrieveResponse(buff.toString());
				
				LOGGER_COMMAND.info("Request {} ==> Response {}",hexString,CDataConverter.stringHex(response));
				
				return response;
			}
		} catch (Exception e) {
			throw new CIOException(e);
		}
		return null;
	}
	
	protected Logger getLogger() {
		return null;
	}

	protected byte[] retrieveResponse(String string) throws Exception {
		return null;
	}

	public void unplug(){
		hidServices.removeUsbServicesListener(this);
		hidDevice.close();
		device = null;
	}
		
	public short getProductId() {
		return hidDevice == null ? 0 : hidDevice.getProductId();
	}

	public String getProduct() {
		return hidDevice == null ? "" : hidDevice.getProduct();
	}

	public short getVendorId() {
		return hidDevice == null ? 0 : hidDevice.getVendorId();
	}

	public String getVendor() {
		return hidDevice == null ? "" : hidDevice.getManufacturer();
	}

	@Override
	public void hidDeviceAttached(HidServicesEvent arg0) {
		//@TODO 
	}

	@Override
	public void hidDeviceDetached(HidServicesEvent arg0) {
		//@TODO 
	}

	@Override
	public void hidFailure(HidServicesEvent arg0) {
		//@TODO 
	}

}
