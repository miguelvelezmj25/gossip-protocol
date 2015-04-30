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
	 * 		April 30, 2015
	 * 			Adding a good error message
	 * 
	 */
	
	private static final long serialVersionUID = 9184206436650524971L;
	
	public InvalidPacketFormatException(DatagramPacket packet, String errorMessage) 
	{
		super("Error in the packet:" + packet.getAddress() + " in port: " + packet.getPort() 
				+ " with error message: " + errorMessage);	
	}

}
