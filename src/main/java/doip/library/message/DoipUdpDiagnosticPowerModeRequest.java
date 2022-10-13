package doip.library.message;

import doip.logging.Level;
import doip.logging.LogManager;
import doip.logging.Logger;

public class DoipUdpDiagnosticPowerModeRequest extends DoipUdpMessage {
	
	private static Logger logger = LogManager.getLogger(DoipUdpDiagnosticPowerModeRequest.class);
	
	public DoipUdpDiagnosticPowerModeRequest() {
		this.log(Level.INFO);
	}

	public void log(Level level) {
		logger.log(level, "----------------------------------------");
		logger.log(level, "DoIP diagnostic power mode request.");
		logger.log(level, "----------------------------------------");
	}
	
	public String getName() {
		return getPayloadTypeAsString(0x4003);
	}
	
	@Override
	public byte[] getMessage() {
		byte[] message = new byte[] {0x03, (byte)0xFC, 0x40, 0x03, 0x00, 0x00, 0x00, 0x00};
		return message;
	}
}
