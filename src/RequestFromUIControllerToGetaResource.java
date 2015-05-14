import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class RequestFromUIControllerToGetaResource extends RequestFromUIController 
{
	/**
	 * 
	 * Miguel Velez
	 * April 29, 2015
	 * 
	 * This class is a request from ui controller to get a resource
	 * 
	 * Class variables:
	 * 		
	 * 		boolean[] responses; 
	 * 			the part numbers that correspond to responses of this request
	 * 
	 * Constructors:
	 * 		
	 * 		public RequestFromUIControllerToGetaResource(ID id)  
	 * 			create a request to find resources from peers
	 * 
	 * Methods:
	 * 	 	
	 * 		public void updateRequest(UDPMessage udpMessage);
	 *			update the request
	 * 
	 *      
	 * Modification History:
	 *  
	 * 		May 7, 2015
	 * 			Original version.
	 * 
	 * 		May 13, 2015
	 * 			Implemented getting a part number and sending to UI.
	 *  
	 */
	
	private boolean[] responses; 
	
	public RequestFromUIControllerToGetaResource(ID id, InetSocketAddress uiController, OutgoingPacketQueue outgoingPacket, PartNumbers numberOfParts) 
	{
		// Create a request to find resources from peers
		// Call the super constructor
		super(id, uiController, outgoingPacket);
		
		this.responses = new boolean[numberOfParts.get()];
	}

	@Override
	public void updateRequest(UDPMessage udpMessage) 
	{
			
		// Send the datagram packet to the UI controller
		// Check if null
		if(udpMessage == null) 
		{
			throw new IllegalArgumentException("The UDP message that you provided is null");
		}
				
		// Get the request part
		byte[] partRequested = new byte[PartNumbers.getLengthInBytes()];
	
		System.arraycopy(udpMessage.getMessage(), ID.getLengthInBytes(), partRequested, 0, partRequested.length);
				
		// Get the integer representation of the part
		int partNumberRequested = 0;
	
		// Get an int from a byte array
		for(int i = 0; i < PartNumbers.getLengthInBytes(); i++) {
			partNumberRequested = partNumberRequested | ((partRequested[i] & 0xFF) << ((PartNumbers.getLengthInBytes() - 1 - i) * 8));
		}
		
		// Check if we have seen this response before
		if(!this.responses[partNumberRequested]) 
		{
			// Add a response with the part number
			this.responses[partNumberRequested] = true;

			// Get the response packet
			byte[] responseMessage = udpMessage.getMessage();
			
			// Get the bytes that we requested
			byte[] bytesToSend = new byte[480];
			
			// Pass the resource id
			System.arraycopy(udpMessage.getID1().getBytes(), 0, bytesToSend, 0, ID.getLengthInBytes());

			// Get the starting byte
			int startByte = partNumberRequested * (UDPMessage.getMaximumPacketSizeInBytes() - ID.getLengthInBytes() - PartNumbers.getLengthInBytes());

			byte[] byteNumber = new byte[PartNumbers.getLengthInBytes()];
			
			for(int i = 0; i < PartNumbers.getLengthInBytes(); i++) {
				byteNumber[i] = (byte) (startByte >> ((PartNumbers.getLengthInBytes() - 1 - i) * 8));
			}
			
			// Put the 4 bytes of the starting byte at the beginning of the bytes to send
			System.arraycopy(byteNumber, 0, bytesToSend, ID.getLengthInBytes(), PartNumbers.getLengthInBytes());
						
			// Get the end byte
			int endByte = startByte + (UDPMessage.getMaximumPacketSizeInBytes() - ID.getLengthInBytes() - PartNumbers.getLengthInBytes());
			
			byteNumber = new byte[PartNumbers.getLengthInBytes()];
			
			for(int i = 0; i < PartNumbers.getLengthInBytes(); i++) {
				byteNumber[i] = (byte) (endByte >> ((PartNumbers.getLengthInBytes() - 1 - i) * 8));
			}
			
			// Put the 4 bytes of the end byte in spot 4 of the bytes to send
			System.arraycopy(byteNumber, 0, bytesToSend, PartNumbers.getLengthInBytes() + ID.getLengthInBytes(), PartNumbers.getLengthInBytes());
						
			// Copy the bytes to send
			System.arraycopy(responseMessage, ID.getLengthInBytes() + PartNumbers.getLengthInBytes(), bytesToSend, (PartNumbers.getLengthInBytes() << 1) + ID.getLengthInBytes(), 456);
			

			// Create a new datagram
			DatagramPacket resourceBytes = new DatagramPacket(bytesToSend, bytesToSend.length);
						
			// Set the address of the UICOntroller
			resourceBytes.setAddress(this.getUIControllerAddress().getAddress());
			
			// Set the port of the UIController
			resourceBytes.setPort(this.getUIControllerAddress().getPort());
			
			
			
			
			// Send the bytes as start, end, bytes
			this.getQueue().enQueue(resourceBytes);
		}
		
	}

}
