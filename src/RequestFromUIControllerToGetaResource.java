import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

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
	 * 		public void run() 
	 * 			Process packets from the UI and community
	 *  
	 *  	public Thread startAsThread() 
	 *  		Start this class as a thread
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
 	 * 		May 14, 2015
	 * 			Using a thread to synchronize the sending and receiving.
	 * 
	 * 		May 16, 2015
	 * 			Correctly requesting and receiving parts and sending it to UI.
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
				
		// Get the request part which is an int
		byte[] partRequested = new byte[PartNumbers.getLengthInBytes()];
	
		// Get the part 4 bytes for part which is 16 spots from the message. Put it in 4 bytes
		System.arraycopy(udpMessage.getMessage(), ID.getLengthInBytes(), partRequested, 0, PartNumbers.getLengthInBytes());
						
		// Get the integer representation of the part
		int partNumberRequested = ByteBuffer.wrap(partRequested).getInt();
			
		// Synchronize the responses array 
		synchronized(this.responses) 
		{
			// Check if we have seen this response before
			if(!this.responses[partNumberRequested]) 
			{		
				// Add a response with the part number
				this.responses[partNumberRequested] = true;
				
				
				// Get the bytes that we requested. We are sending ID(16), start(8), end(8), bytes(456) 
				byte[] bytesToSend = new byte[488]; // TODO check if we can use formulas
								
				// Pass the resource id(16) at the beginning of the packet that we are going to send
				System.arraycopy(udpMessage.getID1().getBytes(), 0, bytesToSend, 0, ID.getLengthInBytes());
				
				// Get the starting byte which is a multiple of 456
				long startByte = partNumberRequested * (UDPMessage.getMaximumPacketSizeInBytes() - ID.getLengthInBytes() - (4));
				// Create an array of 8 bytes to hold the long that represent the starting byte
				byte[] byteNumber = ByteBuffer.allocate(8).putLong(startByte).array();
				// Put the 8 bytes of the starting byte after the resourceID
				System.arraycopy(byteNumber, 0, bytesToSend, ID.getLengthInBytes(), 8);			
				
				// Get the end byte which is 456 more bytes than the starting byte
				long endByte = startByte + (UDPMessage.getMaximumPacketSizeInBytes() - ID.getLengthInBytes() - (4));
				// Create a new 8 byte array
				byteNumber = ByteBuffer.allocate(8).putLong(endByte).array();
				// Put the 8 bytes of the end byte after the start byte array
				System.arraycopy(byteNumber, 0, bytesToSend, (8) + ID.getLengthInBytes(), (8));
				
				// Copy the bytes to send after the end byte
				System.arraycopy(udpMessage.getMessage(), ID.getLengthInBytes() + 4, bytesToSend, (16) + ID.getLengthInBytes(), 456);
				

				// Create a new datagram
				DatagramPacket resourceBytes = new DatagramPacket(bytesToSend, bytesToSend.length);
				
				// Set the address of the UICOntroller
				resourceBytes.setAddress(this.getUIControllerAddress().getAddress());
				
				// Set the port of the UIController
				resourceBytes.setPort(this.getUIControllerAddress().getPort());
								
				resourceBytes.setData(bytesToSend);
				//System.out.println(new String(bytesToSend));
				
				// Send the bytes as start, end, bytes
				this.getQueue().enQueue(resourceBytes);
								
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
			// Synchronize the responses array
			synchronized(this.responses)
			{
							
				// Get a byte array from the part number
				byte[] partNumber = new byte[4];
				
				// Get the current loop counter
				int temp = i;
								
				// Transform an int to a byte array
				for(int j = PartNumbers.getLengthInBytes() - 1; j >= 0; j--) {
					partNumber[j] = (byte) (temp & 0xFF);
					temp = temp >>> 8;
				}
				
				// Create array to put random ID and part number
				byte[] partRequested = new byte[ID.getLengthInBytes() + PartNumbers.getLengthInBytes()];
				
				// Copy a random id in the array
				System.arraycopy(ID.idFactory().getBytes(), 0, partRequested, 0, ID.getLengthInBytes());
				
				// Copy the part number after the ID
				System.arraycopy(partNumber, 0, partRequested, ID.getLengthInBytes(), PartNumbers.getLengthInBytes());
				
				// Create a UDP message with format RequestID, ResourceID, TTL, RandomID, partNumber			
				UDPMessage requestPartFromResourceMessage = new UDPMessage(this.getID(), this.resourceID, new TimeToLive(), partRequested); 
				
				// Send to peers
				GossipPartners.getInstance().send(requestPartFromResourceMessage);	
								
//				System.out.println("Requested part: " + i);

				// While we have not receive the part number that we requested
				while(!this.responses[i])
				{
					try 
					{
						// Wait until the thread is notified that the part request was recieved or 
						// has passed 4 seconds
						this.responses.wait(4000); 
						
						// If we waited 4 seconds and did not get a part
						if(!this.responses[i]) {
//							System.out.println("did not get part" + i);
							// Decrement the loop counter to request the part again and break from
							// the waiting
							i--;
							break;
						}

					} 
					catch (InterruptedException ei) 
					{

					}			
					
				}
									        
	       	} 
	       
	    }
		
	}
	
	public Thread startAsThread() 
	{
		// Start this class as a thread
		Thread thread = new Thread();
		
		thread = new Thread(this);
		
		// Execute the run method
		thread.start();
		
		return thread;
	}

}
