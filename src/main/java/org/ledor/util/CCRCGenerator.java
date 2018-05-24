/**
 * 
 */
package org.ledor.util;

/**
 * @author ledorsapmalat
 *
 */
public class CCRCGenerator {
	static int[] CRC_Lookup_Lo = { 0x00, 0x5B, 0xB6, 0xED, 0x37, 0x6C, 0x81, 0xDA, 0x35, 0x6E, 0x83, 0xD8, 0x02, 0x59,
			0xB4, 0xEF, 0x6A, 0x31, 0xDC, 0x87, 0x5D, 0x06, 0xEB, 0xB0, 0x5F, 0x04, 0xE9, 0xB2, 0x68, 0x33, 0xDE, 0x85,
			0xD4, 0x8F, 0x62, 0x39, 0xE3, 0xB8, 0x55, 0x0E, 0xE1, 0xBA, 0x57, 0x0C, 0xD6, 0x8D, 0x60, 0x3B, 0xBE, 0xE5,
			0x08, 0x53, 0x89, 0xD2, 0x3F, 0x64, 0x8B, 0xD0, 0x3D, 0x66, 0xBC, 0xE7, 0x0A, 0x51, 0xF3, 0xA8, 0x45, 0x1E,
			0xC4, 0x9F, 0x72, 0x29, 0xC6, 0x9D, 0x70, 0x2B, 0xF1, 0xAA, 0x47, 0x1C, 0x99, 0xC2, 0x2F, 0x74, 0xAE, 0xF5,
			0x18, 0x43, 0xAC, 0xF7, 0x1A, 0x41, 0x9B, 0xC0, 0x2D, 0x76, 0x27, 0x7C, 0x91, 0xCA, 0x10, 0x4B, 0xA6, 0xFD,
			0x12, 0x49, 0xA4, 0xFF, 0x25, 0x7E, 0x93, 0xC8, 0x4D, 0x16, 0xFB, 0xA0, 0x7A, 0x21, 0xCC, 0x97, 0x78, 0x23,
			0xCE, 0x95, 0x4F, 0x14, 0xF9, 0xA2, 0xBD, 0xE6, 0x0B, 0x50, 0x8A, 0xD1, 0x3C, 0x67, 0x88, 0xD3, 0x3E, 0x65,
			0xBF, 0xE4, 0x09, 0x52, 0xD7, 0x8C, 0x61, 0x3A, 0xE0, 0xBB, 0x56, 0x0D, 0xE2, 0xB9, 0x54, 0x0F, 0xD5, 0x8E,
			0x63, 0x38, 0x69, 0x32, 0xDF, 0x84, 0x5E, 0x05, 0xE8, 0xB3, 0x5C, 0x07, 0xEA, 0xB1, 0x6B, 0x30, 0xDD, 0x86,
			0x03, 0x58, 0xB5, 0xEE, 0x34, 0x6F, 0x82, 0xD9, 0x36, 0x6D, 0x80, 0xDB, 0x01, 0x5A, 0xB7, 0xEC, 0x4E, 0x15,
			0xF8, 0xA3, 0x79, 0x22, 0xCF, 0x94, 0x7B, 0x20, 0xCD, 0x96, 0x4C, 0x17, 0xFA, 0xA1, 0x24, 0x7F, 0x92, 0xC9,
			0x13, 0x48, 0xA5, 0xFE, 0x11, 0x4A, 0xA7, 0xFC, 0x26, 0x7D, 0x90, 0xCB, 0x9A, 0xC1, 0x2C, 0x77, 0xAD, 0xF6,
			0x1B, 0x40, 0xAF, 0xF4, 0x19, 0x42, 0x98, 0xC3, 0x2E, 0x75, 0xF0, 0xAB, 0x46, 0x1D, 0xC7, 0x9C, 0x71, 0x2A,
			0xC5, 0x9E, 0x73, 0x28, 0xF2, 0xA9, 0x44, 0x1F, };

	static int[] CRC_Lookup_Hi = { 0x00, 0x75, 0xEA, 0x9F, 0xA0, 0xD5, 0x4A, 0x3F, 0x35, 0x40, 0xDF, 0xAA, 0x95, 0xE0,
			0x7F, 0x0A, 0x6A, 0x1F, 0x80, 0xF5, 0xCA, 0xBF, 0x20, 0x55, 0x5F, 0x2A, 0xB5, 0xC0, 0xFF, 0x8A, 0x15, 0x60,
			0xD4, 0xA1, 0x3E, 0x4B, 0x74, 0x01, 0x9E, 0xEB, 0xE1, 0x94, 0x0B, 0x7E, 0x41, 0x34, 0xAB, 0xDE, 0xBE, 0xCB,
			0x54, 0x21, 0x1E, 0x6B, 0xF4, 0x81, 0x8B, 0xFE, 0x61, 0x14, 0x2B, 0x5E, 0xC1, 0xB4, 0xDC, 0xA9, 0x36, 0x43,
			0x7C, 0x09, 0x96, 0xE3, 0xE9, 0x9C, 0x03, 0x76, 0x49, 0x3C, 0xA3, 0xD6, 0xB6, 0xC3, 0x5C, 0x29, 0x16, 0x63,
			0xFC, 0x89, 0x83, 0xF6, 0x69, 0x1C, 0x23, 0x56, 0xC9, 0xBC, 0x08, 0x7D, 0xE2, 0x97, 0xA8, 0xDD, 0x42, 0x37,
			0x3D, 0x48, 0xD7, 0xA2, 0x9D, 0xE8, 0x77, 0x02, 0x62, 0x17, 0x88, 0xFD, 0xC2, 0xB7, 0x28, 0x5D, 0x57, 0x22,
			0xBD, 0xC8, 0xF7, 0x82, 0x1D, 0x68, 0xCC, 0xB9, 0x26, 0x53, 0x6C, 0x19, 0x86, 0xF3, 0xF9, 0x8C, 0x13, 0x66,
			0x59, 0x2C, 0xB3, 0xC6, 0xA6, 0xD3, 0x4C, 0x39, 0x06, 0x73, 0xEC, 0x99, 0x93, 0xE6, 0x79, 0x0C, 0x33, 0x46,
			0xD9, 0xAC, 0x18, 0x6D, 0xF2, 0x87, 0xB8, 0xCD, 0x52, 0x27, 0x2D, 0x58, 0xC7, 0xB2, 0x8D, 0xF8, 0x67, 0x12,
			0x72, 0x07, 0x98, 0xED, 0xD2, 0xA7, 0x38, 0x4D, 0x47, 0x32, 0xAD, 0xD8, 0xE7, 0x92, 0x0D, 0x78, 0x10, 0x65,
			0xFA, 0x8F, 0xB0, 0xC5, 0x5A, 0x2F, 0x25, 0x50, 0xCF, 0xBA, 0x85, 0xF0, 0x6F, 0x1A, 0x7A, 0x0F, 0x90, 0xE5,
			0xDA, 0xAF, 0x30, 0x45, 0x4F, 0x3A, 0xA5, 0xD0, 0xEF, 0x9A, 0x05, 0x70, 0xC4, 0xB1, 0x2E, 0x5B, 0x64, 0x11,
			0x8E, 0xFB, 0xF1, 0x84, 0x1B, 0x6E, 0x51, 0x24, 0xBB, 0xCE, 0xAE, 0xDB, 0x44, 0x31, 0x0E, 0x7B, 0xE4, 0x91,
			0x9B, 0xEE, 0x71, 0x04, 0x3B, 0x4E, 0xD1, 0xA4, };

	public static int computeRangeCRC(int[] buffer, int min, int max) {
		int CRC, cnt, lo, hi, ind;

		if (max > buffer.length)
			return 0;
		if (min > max)
			return 0;

		hi = lo = 0;
		CRC = 0;

		for (cnt = min; cnt < max; cnt++) {
			ind = (hi ^ buffer[cnt]);
			hi = (lo ^ CRC_Lookup_Hi[ind]);
			lo = CRC_Lookup_Lo[ind];
		}
		CRC = ((hi * 256) + (lo));
		return CRC;
	}

	public static int computeLRC(int[] buffer, int length) {
		int lrc = 0;

		for (int i = 0; i < length; i++) {
			lrc = lrc ^ buffer[i];
		}

		return lrc;
	}

	public static int computeFletcher16(int[] buffer, int length) {
		int mod = 255;
		int sum1 = 0, sum2 = 0;
		for (int i = 0; i < length; i++) {
			sum1 = (sum1 + buffer[i]) % mod;
			sum2 = (sum2 + sum1) % mod;
		}

		return sum1 + (sum2 * (mod + 1));
	}
}
