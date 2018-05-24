/**
 * 
 */
package org.ledor.jio;

import java.util.List;

import javax.usb.UsbConfiguration;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfacePolicy;

/**
 * @author ledorsapmalat
 *
 */
public class CUSBDeviceManager {

	@SuppressWarnings("unchecked")
	public static UsbDevice findDevice(UsbHub hub, short vendorId, short productId) {
		UsbDevice launcher = null;

		for (UsbDevice device : (List<UsbDevice>) hub.getAttachedUsbDevices()) {
			if (device.isUsbHub()) {
				launcher = findDevice((UsbHub) device, vendorId, productId);
				if (launcher != null)
					return launcher;
			} else {
				UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
				if (desc.idVendor() == vendorId
						&& desc.idProduct() == productId)
					return device;
			}
		}
		return null;
	}
	
	/**
	 * retrieve the max data size the device can receive 
	 * @param device
	 * @return
	 * @throws UsbException
	 */
	public static int getMaxPacketSize(UsbDevice device) throws UsbException{
		if (device!=null){
			UsbConfiguration configuration = device.getActiveUsbConfiguration();
			UsbInterface iface = (UsbInterface) configuration.getUsbInterfaces().get(0);
			try {
				iface.claim(new UsbInterfacePolicy()
				{        
				    @Override
				    public boolean forceClaim(UsbInterface usbInterface)
				    {
				        return true;
				    }
				});
				
				UsbEndpoint ep = (UsbEndpoint)iface.getUsbEndpoints().get(0);
				return ep.getUsbEndpointDescriptor().wMaxPacketSize();
			} catch (UsbException e) {
				throw e;
			} finally{
				if (iface!=null && iface.isClaimed())
					iface.release();
			}
		}
		return 0;
	}
}
