import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class RequestFromUIControllerToGetaResource extends RequestFromUIController implements Runnable
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
		
	private boolean[] 	responses; 
	private ID			resourceID;
	
	public RequestFromUIControllerToGetaResource(ID id, ID resourceId, InetSocketAddress uiController, OutgoingPacketQueue outgoingPacket, int numberOfParts) 
	{
		// Create a request to find resources from peers
		// Call the super constructor
		super(id, uiController, outgoingPacket);
		
		this.resourceID = resourceId;
		this.responses = new boolean[numberOfParts];
			
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
		
		synchronized (this.responses) 
		{
			// Check if we have seen this response before
			if(!this.responses[partNumberRequested]) 
			{
				// Add a response with the part number
				this.responses[partNumberRequested] = new Boolean(true);
				
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
//				this.getQueue().enQueue(resourceBytes);
				
				System.out.println("Sent to ui part:" + partNumberRequested);
				
				// Notify to get a new part
				this.responses.notify();
			}
			
		}
		
		
	}

	@Override
	public void run() 
	{
		for (int i = 0; i < this.responses.length; i++)
	    {
			synchronized(this.responses)
			{
							
				// Get a byte array from the part number
				byte[] partNumber = new byte[4];
				
				for(int j = 0; j < PartNumbers.getLengthInBytes(); j++) {
					partNumber[j] = (byte) (i >> ((PartNumbers.getLengthInBytes() - 1 - j) * 8));
				}
									
				// Create a UDP message with format RequestID, ResourceID, TTL, RandomID, partNumber			
				UDPMessage getMessage = new UDPMessage(this.getID(), this.resourceID, new TimeToLive(), new String(ID.idFactory().getBytes()) + new String(partNumber)); 
				
				// Send to peers
//				GossipPartners.getInstance().send(getMessage);	
				
				DatagramPacket send = getMessage.getDatagramPacket();
				
				send.setPort(12345);
				
				try {
					send.setAddress(InetAddress.getLocalHost());
		//			send.setAddress(InetAddress.getByName("140.209.121.69"));
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				
				this.getQueue().enQueue(send);
				
				System.out.println("Requested part: " + i);

				while(!this.responses[i])
				{
//					System.out.println("While we dont have a response");
					try 
					{
//						System.out.println("Waiting");
						this.responses.wait(); 
//						System.out.println("Waited");
					} 
					catch (InterruptedException ei) 
					{
//						System.out.println("Exception while waiting");
					}			
					
				}
						        
	       	} 
	       
	    }
		
	}
	
	public Thread startAsThread() 
	{
		Thread thread = new Thread();
		
		thread = new Thread(this);
		
		// Execute the run method
		thread.start();
		
		return thread;
	}

}
