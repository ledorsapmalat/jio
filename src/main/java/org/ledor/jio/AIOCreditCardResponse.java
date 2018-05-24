/**
 * 
 */
package org.ledor.jio;

/**
 * @author ledorsapmalat
 *
 */
public abstract class AIOCreditCardResponse extends AIOCreditCardData {

	private String response;
	private String payload;
	private boolean swipeAction;
	private boolean cardStateResponse;
	
	private String description;
	
	private int[] trackSize;
	private byte[][] encryptedTrackData;
	private String[] maskedTrackData;
	private String[] hashedTrackData;

	private int[][] trackState;
	private int[][] maskState;
	private int[][] hashState;
	
	private byte[] keySerialNo;
	private String formatCode;
	
	/**
	 * @param response the response to set
	 */
	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * @param payload
	 */
	public void setPayload(String payload){
		this.payload = payload;
	}
	
	/**
	 * @return the payload
	 */
	public String getPayload(){
		return this.payload;
	}
	
	/**
	 * @return the swipeAction
	 */
	public boolean isSwipeAction() {
		return swipeAction;
	}

	/**
	 * @param swipeAction the swipeAction to set
	 */
	public void setSwipeAction(boolean swipeAction) {
		this.swipeAction = swipeAction;
	}

	public void setCheckSum(String checkSum){
		this.checkSum = checkSum;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the encryptedTrackData
	 */
	public byte[][] getEncryptedTrackData() {
		return encryptedTrackData;
	}

	/**
	 * @param encryptedTrackData the encryptedTrackData to set
	 */
	public void setEncryptedTrackData(byte[][] encryptedTrackData) {
		this.encryptedTrackData = encryptedTrackData;
	}

	/**
	 * @return the maskedTrackData
	 */
	public String[] getMaskedTrackData() {
		return maskedTrackData;
	}

	/**
	 * @param maskedTrackData the maskedTrackData to set
	 */
	public void setMaskedTrackData(String[] maskedTrackData) {
		this.maskedTrackData = maskedTrackData;
	}

	/**
	 * @return the hashedTrackData
	 */
	public String[] getHashedTrackData() {
		return hashedTrackData;
	}

	/**
	 * @param hashedTrackData the hashedTrackData to set
	 */
	public void setHashedTrackData(String[] hashedTrackData) {
		this.hashedTrackData = hashedTrackData;
	}

	/**
	 * @return the keySerialNo
	 */
	public byte[] getKeySerialNo() {
		return keySerialNo;
	}

	/**
	 * @param keySerialNo the keySerialNo to set
	 */
	public void setKeySerialNo(byte[] keySerialNo) {
		this.keySerialNo = keySerialNo;
	}

	/**
	 * @return the formatCode
	 */
	public String getFormatCode() {
		return formatCode;
	}

	/**
	 * @param formatCode the formatCode to set
	 */
	public void setFormatCode(String formatCode) {
		this.formatCode = formatCode;
	}

	/**
	 * @return the cardStateResponse
	 */
	public boolean isCardStateResponse() {
		return cardStateResponse;
	}

	/**
	 * @param cardStateResponse the cardStateResponse to set
	 */
	public void setCardStateResponse(boolean cardStateResponse) {
		this.cardStateResponse = cardStateResponse;
	}

	/**
	 * @return the trackSize
	 */
	public int[] getTrackSize() {
		return trackSize;
	}

	/**
	 * @param trackSize the trackSize to set
	 */
	public void setTrackSize(int[] trackSize) {
		this.trackSize = trackSize;
	}

	/**
	 * @return the trackState
	 */
	public int[][] getTrackState() {
		return trackState;
	}

	/**
	 * @param trackState the trackState to set
	 */
	public void setTrackState(int[][] trackState) {
		this.trackState = trackState;
	}

	/**
	 * @return the maskState
	 */
	public int[][] getMaskState() {
		return maskState;
	}

	/**
	 * @param maskState the maskState to set
	 */
	public void setMaskState(int[][] maskState) {
		this.maskState = maskState;
	}

	/**
	 * @return the hashState
	 */
	public int[][] getHashState() {
		return hashState;
	}

	/**
	 * @param hashState the hashState to set
	 */
	public void setHashState(int[][] hashState) {
		this.hashState = hashState;
	}

	@Override
	public String getCheckSum(){
		return this.checkSum;
	}
	
	/**
	 * create the command
	 */
	@Override
	public String create(){
		return this.response.toLowerCase();		
	}
}
