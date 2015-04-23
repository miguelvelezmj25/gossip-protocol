import java.net.DatagramPacket;

public class InvalidPacketFormatException extends RuntimeException {

	private static final long serialVersionUID = 9184206436650524971L;
	
	public InvalidPacketFormatException(DatagramPacket packet, String errorMessage) {
		super(errorMessage);
		
		// TODO ask jarvis about sending more info to the message
//		StringBuilder error = new StringBuilder("\nThis is what the packet looks like:");
//		
//		error.append("\n");
//		error.append("Address: " + packet.getAddress() + "\n");
//		error.append("Length: " + packet.getLength() + "\n");
//		error.append("Port: " + packet.getPort() + "\n");
//		error.append("SocketAddress: " + packet.getSocketAddress() + "\n");
//		error.append("Data: ");
//		
//		for(byte singleByte : packet.getData()) {
//			error.append(singleByte + "\n");
//		}
		
		
		
	}

}
