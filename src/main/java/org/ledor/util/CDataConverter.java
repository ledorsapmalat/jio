/**
 * 
 */
package org.ledor.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ledorsapmalat
 *
 */
public class CDataConverter {

	/**
	 * returns byte data from Hex Char
	 * @param c
	 * @return
	 */
	public static byte byteValueFromHex(char c) {
		byte b = -1;
		if (c >= '0' && c <= '9') b = (byte)(c - '0');
		else if (c >= 'a' && c <= 'f') b = (byte)((c - 'a')+10);
		return b;
	}
	
	/**
	 * 
	 * @param ascii
	 * @return
	 */
	public static String stringHex(String ascii) {

		char[] chars = ascii.toCharArray();

		StringBuffer hex = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			hex.append(Integer.toHexString((int) chars[i]));
		}

		return hex.toString();
	}

	/**
	 * Convert HEX to Decimal Value
	 * @param hexString
	 * @return
	 */
	public static String toAscii(String hexString) {

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();

		// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
		for (int i = 0; i < hexString.length() - 1; i += 2) {

			// grab the hex in pairs
			String output = hexString.substring(i, (i + 2));
			// convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			sb.append((char) decimal);
			temp.append(decimal);
		}
		return sb.toString();
	}
	
	/**
	 * returns an unsigned byte in int format from hex string
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static int[] toUBytes(String data) throws Exception {
		
		int dataLength = data.length();
		if (dataLength > 0 && dataLength%2==1) throw new Exception("Invalid data, length is not even: " + dataLength + ". (data = " + data + ")");
		int[] bytes = new int[dataLength/2];
		data = data.toLowerCase();
		for (int i = 0; i < dataLength; i++) {
			
			int byteIdx = i/2;
			
			char c1 = data.charAt(i);
			char c2 = data.charAt(++i);
			
			byte b1 = byteValueFromHex(c1);
			byte b2 = byteValueFromHex(c2);

			//LOGGER.debug(b1 + ":" + b2);
			
			if (b1 == -1) throw new Exception("Invalid hex value: " + c1);
			if (b2 == -1) throw new Exception("Invalid hex value: " + c2);
			
			byte b = (byte)((b1 << 4) | b2);
			bytes[byteIdx] = (int)b & 0xff;;
		}
		
		return bytes;
	}
	
	/**
	 * returns byte from hex string
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] toBytes(String data) throws Exception {
		
		int dataLength = data.length();
		if (dataLength>0 && dataLength%2==1) throw new Exception("Invalid data, length is not even: " + dataLength + ". (data = " + data + ")");
		byte[] bytes = new byte[dataLength/2];
		data = data.toLowerCase();
		for (int i = 0; i < dataLength; i++) {
			
			int byteIdx = i/2;
			
			char c1 = data.charAt(i);
			char c2 = data.charAt(++i);
			
			byte b1 = byteValueFromHex(c1);
			byte b2 = byteValueFromHex(c2);
			
			//LOGGER.debug(b1 + ":" + b2);
	
			if (b1 == -1) throw new Exception("Invalid hex value: " + c1);
			if (b2 == -1) throw new Exception("Invalid hex value: " + c2);
			
			byte b = (byte)((b1 << 4) | b2);
			bytes[byteIdx] = b;
		}
		
		return bytes;
	}
	
	/**
	 * Convert into specific byte order
	 * @param byteArray
	 * @param order
	 * @return
	 */
	public static byte[] toByteOrder(byte[] byteArray, ByteOrder order){
		ByteBuffer bb = ByteBuffer.allocate(byteArray.length);
		bb.put(byteArray);
		bb.order(order);
		
		return bb.array();
	}
	
	/**
	 * Convert into specific byte order
	 * @param byteArray
	 * @param order
	 * @return
	 */
	public static String toLittleEndian(int[] byteArray){
		String retval = "";
		for (int b: byteArray)
			retval = stringHex(b).concat(retval);
		
		return retval;
	}
	
	/**
	 * returns byte from hex string
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte toByte(byte m, byte n) throws Exception {
		
		if(m>47 && m<58)
			m -= 48;
		else if (m>64 && m<91)
			m -= 55;
		else m -= 87;
		
		if(n>47 && n<58)
			n -= 48;
		else if (n>64 && n<91)
			n -= 55;
		else n -= 87;
		
		return (byte)(16*m+n);	
	}

	/**
	 * converts byte array to Hex String
	 * @param bytes
	 * @return
	 */
	public static String stringHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			byte b1 = (byte)((b & 240) >> 4);
			byte b2 = (byte)((b & 15));
			byte [] temps = new byte[2];
			temps[0] = b1;
			temps[1] = b2;
			for (int j = 0; j < temps.length; j++) {
				byte bb = temps[j];
				if (bb < 10) builder.append((char)('0'+bb));
				else builder.append((char)('a' + (bb-10)));
			}
		}
		return builder.toString();
	}
	
	/**
	 * converts unsigned byte (in int format) array to Hex String
	 * @param bytes
	 * @return
	 */
	public static String stringUHex(int[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			int b = bytes[i];
			byte b1 = (byte)((b & 240) >> 4);
			byte b2 = (byte)((b & 15));
			byte [] temps = new byte[2];
			temps[0] = b1;
			temps[1] = b2;
			for (int j = 0; j < temps.length; j++) {
				byte bb = temps[j];
				if (bb < 10) builder.append((char)('0'+bb));
				else builder.append((char)('a' + (bb-10)));
			}
		}
		return builder.toString();
	}

	/**
	 * converts a specific size of byte array in Hex String
	 * @param value
	 * @param sizeInBytes
	 * @return
	 */
	public static String stringHex(int value, int sizeInBytes) {
		
		if (sizeInBytes > 4) throw new IllegalArgumentException("Size must be 0 to 4 bytes.");
		
		int temp = value;
		byte[] bytes = new byte[sizeInBytes];
		for (int i = 0; i < sizeInBytes; i++) {
			byte b = (byte)(temp & 255);
			temp = temp >> 8; // shift 1 byte
			bytes[sizeInBytes-(i+1)] = b;
		}
		return stringHex(bytes);
	}
	
	/**
	 * converts int to hex string
	 * @param value
	 * @return
	 */
	public static String stringHex(int value) {
		List<Byte> byteList = new ArrayList<Byte>();
		int temp = value;
		if (temp==0)
			byteList.add((byte)0x00);
		else
			while (temp > 0) {
				byte b = (byte)(temp & 255);
				temp = temp >> 8; // shift 1 byte
				byteList.add(b);
			}
		int length = byteList.size();
		byte[] bytes = new byte[length];
		for (int i = 0; i < bytes.length; i++) bytes[i]=0;
		int i = 1;
		for (Byte b : byteList) {
			bytes[length-i] = b;
			i++;
		}
		return stringHex(bytes);
	}

	/**
	 * Return true if data is in Byte
	 * @param b
	 * @param bitIdx
	 * @return
	 */
	public static boolean isBitOn(byte b, int bitIdx) {
		// 1, 2, 4, 8, 16, 32, 64, 128
		int pow = (int)Math.pow(2,bitIdx-1);
		int res = b & pow;
		return res == pow;
	}

	/**
	 * retrieve the byte value from a byte array
	 * @param bytes
	 * @param idx
	 * @return
	 */
	public static byte[] bytesFrom(byte[] bytes, int idx) {
		byte[] chopped = new byte[bytes.length-idx];
		for (int i = idx; i < bytes.length; i++) {
			chopped[i-idx] = bytes[i];
		}
		return chopped;
	}

	/**
	 * retrieve the byte value from a byte array
	 * @param bytes
	 * @param idx
	 * @return
	 */
	public static byte[] bytesTo(byte[] bytes, int idx) {
		byte[] chopped = new byte[idx];
		for (int i = 0; i < idx; i++) {
			chopped[i] = bytes[i];
		}
		return chopped;
	}
	
	/**
	 * 
	 * @param hex
	 * @return
	 */
	public static String toBinary(String hex) {
	    String hex_char,bin_char,binary;
	    binary = "";
	    int len = hex.length()/2;
	    for(int i=0;i<len;i++){
	        hex_char = hex.substring(2*i,2*i+2);
	        int conv_int = Integer.parseInt(hex_char,16);
	        bin_char = Integer.toBinaryString(conv_int);
	        bin_char = zero_pad_bin_char(bin_char);
	        if(i==0) binary = bin_char; 
	        else binary = binary+bin_char;
	    }
	    return binary;
	}
	
	/**
	 * 
	 * @param bin_char
	 * @return
	 */
	private static String zero_pad_bin_char(String bin_char){
	    int len = bin_char.length();
	    if(len == 8) return bin_char;
	    String zero_pad = "0";
	    for(int i=1;i<8-len;i++) zero_pad = zero_pad + "0"; 
	    return zero_pad + bin_char;
	}
	
	/**
	 * 
	 * @param hexString
	 * @return
	 */
	public static int hexToNum(String hexString){
		return Integer.parseInt(hexString,16);
	}
}
