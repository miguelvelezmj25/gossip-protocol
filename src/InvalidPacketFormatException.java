import java.net.DatagramPacket;

public class InvalidPacketFormatException extends RuntimeException 
{
	/**
	 * 
	 *	Miguel Velez
	 * 	April 19, 2015
	 * 
	 * 	This class is a unchecked exception. It is thrown when there is an invalid
	 * 	packet format.
	 * 
	 * 	Modification History:
	 * 		April 19, 2015
	 * 			Original version
	 * 
	 * 		April 29, 2015
	 * 			Adding comments
	 * 
	 */
	
	private static final long serialVersionUID = 9184206436650524971L;
	
	public InvalidPacketFormatException(DatagramPacket packet, String errorMessage) 
	{
		super(errorMessage + " - packet data: " + packet.getAddress());
		
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
