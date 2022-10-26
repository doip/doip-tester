package doip.library.message;

import doip.logging.Level;
import doip.logging.LogManager;
import doip.logging.Logger;

public class DoipUdpEntityStatusRequest extends DoipUdpMessage {
	
	private static Logger logger = LogManager.getLogger(DoipUdpEntityStatusRequest.class);
	
	public DoipUdpEntityStatusRequest() {
		this.log(Level.INFO);
	}
	
	public void log(Level level) {
		logger.log(level, "----------------------------------------");
		logger.log(level, "DoIP entity status request.");
		logger.log(level, "----------------------------------------");
	}
	
	public String getMessageName() {
		return getPayloadTypeAsString(0x4001);
	}

	@Override
	public byte[] getMessage() {
		byte[] msg = new byte[] {0x03, (byte) 0xFC, 0x40, 0x01, 0x00, 0x00, 0x00, 0x00};
		return msg;
	}

}
