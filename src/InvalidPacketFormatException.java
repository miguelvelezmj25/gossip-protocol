import java.net.DatagramPacket;


public class InvalidPacketFormatException extends RuntimeException {

	private static final long serialVersionUID = 9184206436650524971L;
	
	public InvalidPacketFormatException(DatagramPacket packet, String errorMessage) {
		// TODO what to do with the packet
		super(errorMessage);
	}

}
