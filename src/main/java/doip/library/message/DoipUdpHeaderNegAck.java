package doip.library.message;

import doip.logging.Level;
import doip.logging.LogManager;
import doip.logging.Logger;

public class DoipUdpHeaderNegAck extends DoipUdpMessage implements DoipHeaderNegAck {

	private static Logger logger = LogManager.getLogger(DoipUdpHeaderNegAck.class);
	private int code = -1;

	public DoipUdpHeaderNegAck(int code) {
		this.code = code;
		this.log(Level.INFO);
	}
	
	public String getMessageName() {
		return getPayloadTypeAsString(0x0000);
	}

	public void log(Level level) {
		logger.log(level, "----------------------------------------");
		logger.log(level, "DoIP header negative acknowledgement (UDP):");
		logger.log(level, "    Code = " + String.format("0x%02X (", code) + getCodeAsString() + ")");
		logger.log(level, "----------------------------------------");
	}
	
	public String getCodeAsString() {
		switch (code) {
		case 0x00:
			return "incorrect pattern format";
		case 0x01:
			return "unknown payload type";
		case 0x02:
			return "message too large";
		case 0x03:
			return "out of memory";
		case 0x04:
			return "invalid payload length";
		default:
			return "reserved by this document";
		}
	}

	@Override
	public byte[] getMessage() {
		byte[] message = new byte[] { 0x03, (byte) 0xFC, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00 };
		message[8] = (byte) (code & 0xFF);
		return message;
	}
}
