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
	
	public String getName() {
		return getPayloadTypeAsString(0x0000);
	}

	public void log(Level level) {
		logger.log(level, "----------------------------------------");
		logger.log(level, "DoIP header negative acknowledgement (UDP):");
		logger.log(level, "    Code = " + this.code);
		logger.log(level, "");
		logger.log(level, "----------------------------------------");
	}

	@Override
	public byte[] getMessage() {
		byte[] message = new byte[] { 0x02, (byte) 0xFD, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00 };
		message[8] = (byte) (code & 0xFF);
		return message;
	}
}
