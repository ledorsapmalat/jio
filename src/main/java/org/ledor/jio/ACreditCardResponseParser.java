/**
 * 
 */
package org.ledor.jio;

import org.ledor.jio.command.AIOData;
import org.ledor.jio.command.AIODataParser;
import org.ledor.util.CDataConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ledorsapmalat
 *
 */
public abstract class ACreditCardResponseParser extends AIODataParser {

	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private boolean isMOIR;
	private boolean isAES;

	private boolean hasSessionId;

	@SuppressWarnings("unused")
	private boolean hasDeviceSN;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hl.tln.common.io.parser.AIODataParser#parse()
	 */
	@Override
	public AIOData parse() {
		try {
			String s = command.create();
			isMOIR = s.substring(0, 2).trim().equals("60");
			boolean isSwipe = ((AIOCreditCardResponse) command).isSwipeAction();
			if (isMOIR) {
				command.setPayloadSize(s.substring(2, 6));
				((AIOCreditCardResponse) command).setPayload(s.substring(6, s.length() - 4));
				// should be LRC if MOIR
				((AIOCreditCardResponse) command).setCheckSum(s.substring(s.length() - 4, s.length() - 2));
				processSwipeByte(command, isSwipe);

				LOGGER.debug("\nPayload: " + ((AIOCreditCardResponse) command).getPayload() + "\n");

			}
			return command;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unused")
	private void processSwipeByte(AIOData command, boolean isSwipe) throws Exception {
		String s = command.create();
		LOGGER.debug("\nRESPONSE: " + s + "\n");
		String formatCode = s.substring(6, 8);
		if (isSwipe) { // parse all relevant information specially Track 2 data
			final int MAX_LENGTH = 255;

			byte STX = 0;
			byte[] LENGTH = new byte[2];
			byte CARD_ENCODE_TYPE = 0;
			byte TRACK_STATUS = 0;
			byte TK1_UNENCRYPTED_DATA_LENGTH = 0;
			byte TK2_UNENCRYPTED_DATA_LENGTH = 0;
			byte TK3_UNENCRYPTED_DATA_LENGTH = 0;
			byte CLEAR_MASKED_DATA_STATUS = 0;
			byte ENCRYPTED_HASH_DATA_STATUS = 0;
			byte[] TK1_CLEAR_MASKED_DATA = new byte[MAX_LENGTH];
			byte[] TK2_CLEAR_MASKED_DATA = new byte[MAX_LENGTH];
			byte[] TK3_CLEAR_MASKED_DATA = new byte[MAX_LENGTH];
			byte[] ACCOUNT_NUMBER = new byte[19];
			byte ACCOUNT_NUMBER_LENGTH = 0;
			byte[] CARDHOLDER_NAME = new byte[26];
			byte CARDHOLDER_NAME_LENGTH = 0;
			byte[] EXPIRATION_DATE = new byte[4];
			byte TK1_ENCRYPTED_DATA_LENGTH = 0;
			byte TK2_ENCRYPTED_DATA_LENGTH = 0;
			byte TK3_ENCRYPTED_DATA_LENGTH = 0;
			byte[] TK1_ENCRYPTED_DATA = new byte[MAX_LENGTH];
			byte[] TK2_ENCRYPTED_DATA = new byte[MAX_LENGTH];
			byte[] TK3_ENCRYPTED_DATA = new byte[MAX_LENGTH];
			byte[] SESSION_ID = new byte[16];
			byte[] TK1_HASH_DATA = new byte[20];
			byte[] TK2_HASH_DATA = new byte[20];
			byte[] TK3_HASH_DATA = new byte[20];
			byte[] DEVICE_SERIAL_NUMBER = new byte[10];
			byte[] KEY_SERIAL_NUMBER = new byte[10];
			byte LRC;
			byte CHECKSUM;
			byte ETX;
			byte[] CARD_DATA = new byte[1000]; // Reader output in array format

			int TOTAL_UNENCRYPTED_DATA_LENGTH = 0, TOTAL_ENCRYPTED_DATA_LENGTH = 0, TOTAL_HASH_DATA_LENGTH = 0;

			for (int i = 0; i < s.length() / 2; i++)
				CARD_DATA[i] = CDataConverter.toByte((byte) s.charAt(2 * i), (byte) s.charAt(2 * i + 1));

			// STX
			STX = CARD_DATA[0];
			// Length of the output (excluded STX, two byte length, LRC,
			// CheckSum
			// and ETX)
			LENGTH[0] = CARD_DATA[1]; // Low byte
			LENGTH[1] = CARD_DATA[2]; // High byte
			// Card Encode Type
			// 0x80 ABA/ISO Card Format;
			// 0x83 Other Format;
			// 0x84 Raw data format
			CARD_ENCODE_TYPE = CARD_DATA[3];
			// Track 1-3 Status
			// bit 0,1,2:T1,2,3 decode; bit 3,4,5:T1,2,3 sampling
			TRACK_STATUS = CARD_DATA[4];
			// Track 1 Unencrypted Data Length
			TK1_UNENCRYPTED_DATA_LENGTH = CARD_DATA[5];

			// Track 2 Unencrypted Data Length
			TK2_UNENCRYPTED_DATA_LENGTH = CARD_DATA[6];

			// Track 3 Unencrypted Data Length
			TK3_UNENCRYPTED_DATA_LENGTH = CARD_DATA[7];

			// Clear/Masked Data Status
			// Bit 0: 1 --- if TK1 clear/masked data present
			// Bit 1: 1 --- if TK2 clear/masked data present
			// Bit 2: 1 --- if TK3 clear/masked data present
			// Bit 3: 0
			// Bit 4: 0 --- if TDES; 1 --- if AES
			// Bit 5: 0
			// Bit 6: 0
			// Bit 7: 1 --- if Device Serial Number present
			CLEAR_MASKED_DATA_STATUS = CARD_DATA[8];
			// Encrypted data sent status
			// Bit 0: if 1 - tk1 encrypted data present
			// Bit 1: if 1 - tk2 encrypted data present
			// Bit 2: if 1 - tk3 encrypted data present
			// Bit 3: if 1 - tk1 hash data present
			// Bit 4: if 1 - tk2 hash data present
			// Bit 5: if 1 - tk3 hash data present
			// Bit 6: if 1 - session ID present
			// Bit 7: if 1 - KSN present
			ENCRYPTED_HASH_DATA_STATUS = CARD_DATA[9];

			/*****************************************************/
			/****** Parse Track 1 Clear / Masked data ******/
			/*****************************************************/
			if ((CLEAR_MASKED_DATA_STATUS & 1) != 0) {
				for (int i = 0; i < TK1_UNENCRYPTED_DATA_LENGTH; i++)
					TK1_CLEAR_MASKED_DATA[i] = CARD_DATA[10 + i];

				TOTAL_UNENCRYPTED_DATA_LENGTH = TK1_UNENCRYPTED_DATA_LENGTH;
			}
			/*****************************************************/
			/****** Parse Track 2 Clear / Masked data ******/
			/*****************************************************/
			if ((CLEAR_MASKED_DATA_STATUS & 2) != 0) {
				for (int i = 0; i < TK2_UNENCRYPTED_DATA_LENGTH; i++)
					TK2_CLEAR_MASKED_DATA[i] = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + 10 + i];

				TOTAL_UNENCRYPTED_DATA_LENGTH += TK2_UNENCRYPTED_DATA_LENGTH;
			}

			/*****************************************************/
			/****** Parse Track 3 Clear / Masked data ******/
			/*****************************************************/

			if ((CLEAR_MASKED_DATA_STATUS & 4) != 0) {
				for (int i = 0; i < TK3_UNENCRYPTED_DATA_LENGTH; i++)
					TK3_CLEAR_MASKED_DATA[i] = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + 10 + i];

				TOTAL_UNENCRYPTED_DATA_LENGTH += TK3_UNENCRYPTED_DATA_LENGTH;
			}

			if (!formatCode.trim().equalsIgnoreCase("b0")) { // reading swiped
																// data
				((AIOCreditCardResponse) command).setCardStateResponse(false);
				// If CARD_ENCODE_TYPE = 0x80, parse account number, account
				// name
				// and
				// expiration data field
				if (CARD_ENCODE_TYPE == 0x80) {
					// If track 1 masked data presents
					if ((CLEAR_MASKED_DATA_STATUS & 1) != 0) {
						/*********************************************/
						/****** Parse Account Number ******/
						/*********************************************/
						do {
							// Skip Start Sentinel and character B from track 1
							// clear /
							// masked data
							ACCOUNT_NUMBER[ACCOUNT_NUMBER_LENGTH] = TK1_CLEAR_MASKED_DATA[ACCOUNT_NUMBER_LENGTH + 2];
							ACCOUNT_NUMBER_LENGTH++;
							// Check if the next character is the separator '^'
							if (TK1_CLEAR_MASKED_DATA[ACCOUNT_NUMBER_LENGTH + 2] == 0x5E)
								break;
						} while (ACCOUNT_NUMBER_LENGTH < 20);

						/*********************************************/
						/****** Parse Card Holder Name ******/
						/*********************************************/
						do {
							// Skip Start Sentinel, character B, account number
							// and
							// the
							// first separator '^'
							CARDHOLDER_NAME[CARDHOLDER_NAME_LENGTH] = TK1_CLEAR_MASKED_DATA[CARDHOLDER_NAME_LENGTH
									+ ACCOUNT_NUMBER_LENGTH + 3];
							CARDHOLDER_NAME_LENGTH++;
							// Check if the next character is the separator '^'
							if (TK1_CLEAR_MASKED_DATA[CARDHOLDER_NAME_LENGTH + ACCOUNT_NUMBER_LENGTH + 3] == 0x5E)
								break;

						} while (CARDHOLDER_NAME_LENGTH < 26);

						/*********************************************/
						/****** Parse Expiration Date ******/
						/*********************************************/
						for (int i = 0; i < 4; i++)

							// Skip Start Sentinel, character B, account number,
							// card
							// holder name and two separator '^'
							EXPIRATION_DATE[i] = TK1_CLEAR_MASKED_DATA[CARDHOLDER_NAME_LENGTH + ACCOUNT_NUMBER_LENGTH
									+ 4 + i];
					}

					// If track 1 does not exist, track 2 masked data presents
					else if ((CLEAR_MASKED_DATA_STATUS & 2) != 0) {

						/*********************************************/
						/****** Parse Account Number ******/
						/*********************************************/
						do {
							// Skip Start Sentinel from track 2 clear / masked
							// data
							ACCOUNT_NUMBER[ACCOUNT_NUMBER_LENGTH] = TK2_CLEAR_MASKED_DATA[ACCOUNT_NUMBER_LENGTH + 1];

							ACCOUNT_NUMBER_LENGTH++;

							// Check if the next character is the separator '='
							if (TK2_CLEAR_MASKED_DATA[ACCOUNT_NUMBER_LENGTH + 1] == 0x3D)
								break;

						} while (ACCOUNT_NUMBER_LENGTH < 20);

						/*********************************************/
						/****** Parse Expiration Date ******/
						/*********************************************/
						for (int i = 0; i < 4; i++)

							// Skip Start Sentinel, account number and the
							// separator
							// '='
							EXPIRATION_DATE[i] = TK2_CLEAR_MASKED_DATA[ACCOUNT_NUMBER_LENGTH + 2 + i];
					}
				}

				/*************************************************/
				/****** Parse Track 1 encrypted data ******/
				/*************************************************/

				if ((ENCRYPTED_HASH_DATA_STATUS & 1) != 0) {
					if ((CLEAR_MASKED_DATA_STATUS & 0x10) != 0) // AES
					{ // Divisible 16
						if ((TK1_UNENCRYPTED_DATA_LENGTH % 16) == 0)
							TK1_ENCRYPTED_DATA_LENGTH = TK1_UNENCRYPTED_DATA_LENGTH;
						else
							TK1_ENCRYPTED_DATA_LENGTH = (byte) ((TK1_UNENCRYPTED_DATA_LENGTH / 16 + 1) * 16);
					}

					else // TDES
					{ // Divisible 8
						if ((TK1_UNENCRYPTED_DATA_LENGTH % 8) == 0)
							TK1_ENCRYPTED_DATA_LENGTH = TK1_UNENCRYPTED_DATA_LENGTH;
						else
							TK1_ENCRYPTED_DATA_LENGTH = (byte) ((TK1_UNENCRYPTED_DATA_LENGTH / 8 + 1) * 8);
					}

					// Parse Track 1 Encrypted Data
					for (int i = 0; i < TK1_ENCRYPTED_DATA_LENGTH; i++)
						TK1_ENCRYPTED_DATA[i] = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + 10 + i];

					TOTAL_ENCRYPTED_DATA_LENGTH = TK1_ENCRYPTED_DATA_LENGTH;

				}

				/*************************************************/
				/****** Parse Track 2 encrypted data ******/
				/*************************************************/
				if ((ENCRYPTED_HASH_DATA_STATUS & 2) != 0) {
					if ((CLEAR_MASKED_DATA_STATUS & 0x10) != 0) // AES
					{ // Divisible 16
						if ((TK2_UNENCRYPTED_DATA_LENGTH % 16) == 0)
							TK2_ENCRYPTED_DATA_LENGTH = TK2_UNENCRYPTED_DATA_LENGTH;
						else
							TK2_ENCRYPTED_DATA_LENGTH = (byte) ((TK2_UNENCRYPTED_DATA_LENGTH / 16 + 1) * 16);
					}

					else // TDES
					{ // Divisible 8
						if ((TK2_UNENCRYPTED_DATA_LENGTH % 8) == 0)
							TK2_ENCRYPTED_DATA_LENGTH = TK2_UNENCRYPTED_DATA_LENGTH;
						else
							TK2_ENCRYPTED_DATA_LENGTH = (byte) ((TK2_UNENCRYPTED_DATA_LENGTH / 8 + 1) * 8);
					}

					// Parse Track 2 Encrypted Data
					for (int i = 0; i < TK2_ENCRYPTED_DATA_LENGTH; i++)
						TK2_ENCRYPTED_DATA[i] = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH
								+ 10 + i];

					TOTAL_ENCRYPTED_DATA_LENGTH += TK2_ENCRYPTED_DATA_LENGTH;

				}

				/*************************************************/
				/****** Parse Track 3 encrypted data ******/
				/*************************************************/
				if ((ENCRYPTED_HASH_DATA_STATUS & 4) != 0) {
					if ((CLEAR_MASKED_DATA_STATUS & 0x10) != 0) // AES
					{ // Divisible 16
						if ((TK3_UNENCRYPTED_DATA_LENGTH % 16) == 0)
							TK3_ENCRYPTED_DATA_LENGTH = TK3_UNENCRYPTED_DATA_LENGTH;
						else
							TK3_ENCRYPTED_DATA_LENGTH = (byte) ((TK3_UNENCRYPTED_DATA_LENGTH / 16 + 1) * 16);
					}

					else // TDES
					{ // Divisible 8
						if ((TK3_UNENCRYPTED_DATA_LENGTH % 8) == 0)
							TK3_ENCRYPTED_DATA_LENGTH = TK3_UNENCRYPTED_DATA_LENGTH;
						else
							TK3_ENCRYPTED_DATA_LENGTH = (byte) ((TK3_UNENCRYPTED_DATA_LENGTH / 8 + 1) * 8);
					}
					// Parse Track 3 Encrypted Data
					for (int i = 0; i < TK3_ENCRYPTED_DATA_LENGTH; i++)
						TK3_ENCRYPTED_DATA[i] = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH
								+ 10 + i];
					TOTAL_ENCRYPTED_DATA_LENGTH += TK3_ENCRYPTED_DATA_LENGTH;
				}
				/*****************************************/
				/****** Parse Session ID ******/
				/*****************************************/
				if ((ENCRYPTED_HASH_DATA_STATUS & 0x40) != 0) {
					if ((CLEAR_MASKED_DATA_STATUS & 0x10) != 0) // AES
					{
						for (int i = 0; i < 16; i++)
							SESSION_ID[i] = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH + 10
									+ i];
						TOTAL_ENCRYPTED_DATA_LENGTH += 16;
					} else // TDES
					{
						for (int i = 0; i < 8; i++)
							SESSION_ID[i] = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH + 10
									+ i];
						TOTAL_ENCRYPTED_DATA_LENGTH += 8;
					}
				}
				/*************************************************/
				/****** Parse Track 1 Hash Data ******/
				/*************************************************/
				if ((ENCRYPTED_HASH_DATA_STATUS & 0x08) != 0) {
					for (int i = 0; i < 20; i++)
						TK1_HASH_DATA[i] = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH + 10
								+ i];
					TOTAL_HASH_DATA_LENGTH = 20;
				}
				/*************************************************/
				/****** Parse Track 2 Hash Data ******/
				/*************************************************/
				if ((ENCRYPTED_HASH_DATA_STATUS & 0x10) != 0) {
					for (int i = 0; i < 20; i++)
						TK2_HASH_DATA[i] = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH
								+ TOTAL_HASH_DATA_LENGTH + 10 + i];
					TOTAL_HASH_DATA_LENGTH += 20;
				}

				/*************************************************/
				/****** Parse Track 3 Hash Data ******/
				/*************************************************/
				if ((ENCRYPTED_HASH_DATA_STATUS & 0x20) != 0) {
					for (int i = 0; i < 20; i++)
						TK3_HASH_DATA[i] = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH
								+ TOTAL_HASH_DATA_LENGTH + 10 + i];
					TOTAL_HASH_DATA_LENGTH += 20;
				}

				/*************************************************/
				/****** Parse Device Serial Number ******/
				/*************************************************/
				if ((CLEAR_MASKED_DATA_STATUS & 0x80) != 0) {
					for (int i = 0; i < 10; i++)
						DEVICE_SERIAL_NUMBER[i] = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH
								+ TOTAL_HASH_DATA_LENGTH + 10 + i];
					/*************************************************/
					/****** Parse Key Serial Number ******/
					/*************************************************/
					if ((ENCRYPTED_HASH_DATA_STATUS & 0x80) != 0) {
						for (int i = 0; i < 10; i++)
							KEY_SERIAL_NUMBER[i] = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH
									+ TOTAL_HASH_DATA_LENGTH + 20 + i];
						// LRC
						LRC = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH
								+ TOTAL_HASH_DATA_LENGTH + 30];
						// CheckSum
						CHECKSUM = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH
								+ TOTAL_HASH_DATA_LENGTH + 31];
						// ETX
						ETX = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH
								+ TOTAL_HASH_DATA_LENGTH + 32];
					} else {
						// LRC
						LRC = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH
								+ TOTAL_HASH_DATA_LENGTH + 20];
						// CheckSum
						CHECKSUM = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH
								+ TOTAL_HASH_DATA_LENGTH + 21];
						// ETX
						ETX = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH
								+ TOTAL_HASH_DATA_LENGTH + 22];
					}
				} else {
					/*************************************************/
					/****** Parse Key Serial Number ******/
					/*************************************************/

					if ((ENCRYPTED_HASH_DATA_STATUS & 0x80) != 0) {
						for (int i = 0; i < 10; i++)
							KEY_SERIAL_NUMBER[i] = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH
									+ TOTAL_HASH_DATA_LENGTH + 10 + i];
						// LRC
						LRC = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH
								+ TOTAL_HASH_DATA_LENGTH + 20];
						// CheckSum
						CHECKSUM = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH
								+ TOTAL_HASH_DATA_LENGTH + 21];
						// ETX
						ETX = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH
								+ TOTAL_HASH_DATA_LENGTH + 22];
					} else {
						// LRC
						LRC = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH
								+ TOTAL_HASH_DATA_LENGTH + 10];

						// CheckSum
						CHECKSUM = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH
								+ TOTAL_HASH_DATA_LENGTH + 11];

						// ETX
						ETX = CARD_DATA[TOTAL_UNENCRYPTED_DATA_LENGTH + TOTAL_ENCRYPTED_DATA_LENGTH
								+ TOTAL_HASH_DATA_LENGTH + 12];
					}
				}

				// set the responses
				((AIOCreditCardResponse) command).setKeySerialNo(KEY_SERIAL_NUMBER);
				String[] hashTrackData = new String[3];
				hashTrackData[0] = CDataConverter.stringHex(TK1_HASH_DATA);
				hashTrackData[1] = CDataConverter.stringHex(TK2_HASH_DATA);
				hashTrackData[2] = CDataConverter.stringHex(TK3_HASH_DATA);
				((AIOCreditCardResponse) command).setHashedTrackData(hashTrackData);
				byte[][] encTrackData = new byte[3][];
				encTrackData[0] = TK1_ENCRYPTED_DATA;
				byte[] encData2 = new byte[TK2_ENCRYPTED_DATA_LENGTH];
				for (int i = 0; i < TK2_ENCRYPTED_DATA_LENGTH; i++) {
					encData2[i] = TK2_ENCRYPTED_DATA[i];
				}
				encTrackData[1] = encData2;
				encTrackData[2] = TK3_ENCRYPTED_DATA;
				((AIOCreditCardResponse) command).setEncryptedTrackData(encTrackData);
				String[] maskTrackData = new String[3];
				maskTrackData[0] = CDataConverter.stringHex(TK1_CLEAR_MASKED_DATA);
				maskTrackData[1] = CDataConverter.stringHex(TK2_CLEAR_MASKED_DATA);
				maskTrackData[2] = CDataConverter.stringHex(TK3_CLEAR_MASKED_DATA);
				((AIOCreditCardResponse) command).setMaskedTrackData(maskTrackData);

			} else {
				// if b0 ==> card read/write present status
				String statusCode = s.substring(8, 10); // create an ENUM for this
				String msg = "";
				switch (statusCode.toLowerCase()) {
				case "08":
					msg = "Card present detected status.";
					break;
				case "0a":
					msg = "Card present and card seated status.";
					break;
				default:
					msg = "Unknown card status.";
					break;
				}
				((AIOCreditCardResponse) command).setDescription(msg);
				((AIOCreditCardResponse) command).setCardStateResponse(true);
			}
		} else {
			if (formatCode.trim().equalsIgnoreCase("90")) {
				((AIOCreditCardResponse) command).setDescription("SUCCESS");
			}
		}
	}

	public ACreditCardResponseParser data(String data) {
		((AIOCreditCardResponse) command).setResponse(data);
		return this;
	}

	public ACreditCardResponseParser swipe() {
		((AIOCreditCardResponse) command).setSwipeAction(true);
		return this;
	}
}
