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
//		System.out.println(this.responses.length);
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
	
		System.arraycopy(udpMessage.getMessage(), ID.getLengthInBytes(), partRequested, 0, PartNumbers.getLengthInBytes());
				
//		System.out.println(partRequested[0] + " - " + partRequested[1] + " - " + partRequested[2] + " - " + partRequested[3]);
		
		// Get the integer representation of the part
		int partNumberRequested = ByteBuffer.wrap(partRequested).getInt();//0;
	
//		// Get an int from a byte array
//		for(int i = 0; i < PartNumbers.getLengthInBytes(); i++) {
//			partNumberRequested = partNumberRequested | ((partRequested[i] & 0xFF) << ((PartNumbers.getLengthInBytes() - 1 - i) * 8));
//		}

//		System.out.println("Part requested: " + partNumberRequested);
//		
//		System.out.println("Before synchronize");
		
		// Synchronize the responses array 
		synchronized(this.responses) 
		{
//			System.out.println("Have we seen this part: " + partNumberRequested + " - " + this.responses[partNumberRequested]);
			// Check if we have seen this response before
			if(!this.responses[partNumberRequested]) 
			{
//				System.out.println("Part not seen before");
				
				// Add a response with the part number
				this.responses[partNumberRequested] = new Boolean(true);
				
				// Get the response packet
				byte[] responseMessage = udpMessage.getMessage();
				
				// Get the bytes that we requested
				byte[] bytesToSend = new byte[488];
				
				// Pass the resource id
				System.arraycopy(udpMessage.getID1().getBytes(), 0, bytesToSend, 0, ID.getLengthInBytes());
				
				// Get the starting byte
				long startByte = partNumberRequested * (UDPMessage.getMaximumPacketSizeInBytes() - ID.getLengthInBytes() - (4));
				
				System.out.println("start bytes: " + startByte);
				
				
				byte[] byteNumber = ByteBuffer.allocate(8).putLong(startByte).array();
				
				System.out.println(byteNumber[0] + " - " + byteNumber[1] + " - " + byteNumber[2] + " - " + byteNumber[3] + " - " + byteNumber[4] + " - " + byteNumber[5] + " - " + byteNumber[6] + " - " + byteNumber[7]);
				
				
				// Put the 4 bytes of the starting byte at the 16 slot to send
				System.arraycopy(byteNumber, 0, bytesToSend, ID.getLengthInBytes(), 8);
				
				// Get the end byte
				long endByte = startByte + (UDPMessage.getMaximumPacketSizeInBytes() - ID.getLengthInBytes() - (4));
				
				System.out.println("End bytes: " + endByte);
				
				byteNumber = ByteBuffer.allocate(8).putLong(endByte).array();
				
				System.out.println(byteNumber[0] + " - " + byteNumber[1] + " - " + byteNumber[2] + " - " + byteNumber[3] + " - " + byteNumber[4] + " - " + byteNumber[5] + " - " + byteNumber[6] + " - " + byteNumber[7]);
				
				
				// Put the 4 bytes of the end byte in spot 20 of the bytes to send
				System.arraycopy(byteNumber, 0, bytesToSend, (8) + ID.getLengthInBytes(), (8));
				
				// Copy the bytes to send
				System.arraycopy(responseMessage, ID.getLengthInBytes() + 4, bytesToSend, (16) + ID.getLengthInBytes(), 456);
								
				// Create a new datagram
				DatagramPacket resourceBytes = new DatagramPacket(bytesToSend, bytesToSend.length);
				
				// Set the address of the UICOntroller
				resourceBytes.setAddress(this.getUIControllerAddress().getAddress());
				
				// Set the port of the UIController
				resourceBytes.setPort(this.getUIControllerAddress().getPort());
								
				//System.out.println(new String(bytesToSend));
				
				// Send the bytes as start, end, bytes
				this.getQueue().enQueue(resourceBytes);
				
//				System.out.println("Sent to ui part:" + partNumberRequested);
				
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
				
//				for(int j = 0; j < PartNumbers.getLengthInBytes(); j++) {
//					partNumber[j] = (byte) (i >> ((PartNumbers.getLengthInBytes() - 1 - j) * 8));
//				}
				
				int temp = i;
//				
//				System.out.println("\n" + i);
								
				for(int j = PartNumbers.getLengthInBytes() - 1; j >= 0; j--) {
				
					partNumber[j] = (byte) (temp & 0xFF);
						temp = temp >>> 8;
//					System.out.println(">>>> "+ (int)partNumber[j]);
				}
//				partNumber = ByteBuffer.allocate(4).putInt(i).array();
				
//				System.out.println(i+":"+ByteBuffer.wrap(partNumber).getInt());
				
				byte[] message = new byte[ID.getLengthInBytes() + PartNumbers.getLengthInBytes()];
				
//				new String(ID.idFactory().getBytes()) + new String(partNumber)
//				
				System.arraycopy(ID.idFactory().getBytes(), 0, message, 0, ID.getLengthInBytes());
				System.arraycopy(partNumber, 0, message, ID.getLengthInBytes(), PartNumbers.getLengthInBytes());
				
				// Create a UDP message with format RequestID, ResourceID, TTL, RandomID, partNumber			
				UDPMessage getMessage = new UDPMessage(this.getID(), this.resourceID, new TimeToLive(), message); 
				
				// Send to peers
				GossipPartners.getInstance().send(getMessage);	
								
//				System.out.println("Requested part: " + i);

				// While we have not receive the part number that we requested
				while(!this.responses[i])
				{
					try 
					{
						// Wait
						this.responses.wait(); 
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
		Thread thread = new Thread();
		
		thread = new Thread(this);
		
		// Execute the run method
		thread.start();
		
		return thread;
	}

}
