import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.net.*;

public class PeerController implements Runnable {
	/**
	 * 
	 * Miguel Velez
	 * April 16, 2015
	 * 
	 * This class is the peer that we use to send and receive requests.
	 * 
	 * Class variables:
	 * 
	 * 		AtomicBoolean done
	 * 			Check if we are done processing packets
	 *  
	 * 		IncomingPacketQueue incomingPacketsFromCommunityQueue
	 * 			Queue with packets from the community
	 * 		
	 * 		IncomingPacketQueue incomingPacketsFromUIQueue
	 * 			Queue with packets from the UI
	 * 
	 * 		DatagramReceiver receiveFromUI
	 * 			Receiver for the UI
	 * 		
	 * 		OutgoingPacketQueue outgoingPacketsQueue
	 * 			Queue for sending out packets to the UI or the community
	 * 
	 * 		DatagramReceiver receiveFromCommunity
	 * 			Receiver for the community
	 * 		
	 * 		InetSocketAddress uiControllerAddress
	 * 			Address and port of the ui
	 *  
	 * 		DatagramSender sender
	 * 			Sender for the ui or the community
	 * 
	 * 
 	 * Constructors:
 	 * 
 	 * 		public PeerController(PortNumberPeerCommunity communityPort, PortNumberPeerUI uiPort) 
 	 * 			create a peer controller
 	 * 
 	 *  
	 * Methods:
	 * 	
	 * 		public void run() 
	 * 			Process packets from the UI and community
	 * 		
	 * 		private void processCommandFromCommunity()
	 * 			Process packet from the community
	 * 
	 *  	private void processCommandFromUI() 
	 *  		Process packets from the UI
	 *  
	 *  	public Thread startAsThread() 
	 *  		Start this class as a thread
	 *  
	 *  	public void setDoneFlag(boolean flag)
	 *  		Setter for the done flag
	 *  
	 *  	public boolean isStopped()
	 *  		Check if the peer has stopped processing packets
	 *   
	 *  	 
	 *      
	 * Modification History:
	 * 		May 3, 2015
	 * 			Original version
	 * 
	 * 		May 8, 2015
	 * 			Implemented receiving from the UI
	 * 
	 * 		May 9, 2015
	 * 			Process requests from the UI
	 * 
	 * 	 	May 11, 2015
	 * 			Receiving packets from the community
	 * 
	 * 	 	May 14, 2015
	 * 			Implemented sending and receiving a part
	 * 
	 * 	 	May 16, 2015
	 * 			Sending the correct information to UI, and gossips
	 * 
	 */
	
	private AtomicBoolean					done;
	private IncomingPacketQueue 			incomingPacketsFromCommunityQueue;
	private IncomingPacketQueue 			incomingPacketsFromUIQueue;
	private DatagramReceiver       			receiveFromUI;
	private OutgoingPacketQueue 			outgoingPacketsQueue;
	private DatagramReceiver        		receiveFromCommunity;
	private DatagramSender					sender;
	private InetSocketAddress				uiControllerAddress;
	
	public PeerController(PortNumberPeerCommunity communityPort, PortNumberPeerUI uiPort, InetSocketAddress uiControllerAddress) 
	{
		// TODO what is packet size
		try 
		{
			this.done = new AtomicBoolean(false);
			
			this.incomingPacketsFromUIQueue = new IncomingPacketQueue();
			this.receiveFromUI = new DatagramReceiver(new DatagramSocket(uiPort.get()), this.incomingPacketsFromUIQueue, 512);
			this.incomingPacketsFromCommunityQueue = new IncomingPacketQueue();
			this.receiveFromCommunity = new DatagramReceiver(new DatagramSocket(communityPort.get()), this.incomingPacketsFromCommunityQueue, 512);
	
			this.outgoingPacketsQueue = new OutgoingPacketQueue();
			this.sender = new DatagramSender(new DatagramSocket(), this.outgoingPacketsQueue, 512);			
			
			this.uiControllerAddress = uiControllerAddress;


		} 
		catch (SocketException se) 
		{
			se.printStackTrace();
		} 
	
	}
	
	@Override
	public void run() 
	{
		// Start listening for messages from the UI
		this.receiveFromUI.startAsThread();
		
		// Start listening for messages from the Community
		this.receiveFromCommunity.startAsThread();
		
		// Start sending packets from the outgoing queue
		this.sender.startAsThread();
		
		while(!this.isStopped()) 
		{
			try 
			{
				// Check UI
				if(this.incomingPacketsFromUIQueue.peek() != null)
				{
					this.processCommandFromUI();
				}
				
				// Sleep
				Thread.sleep(50);
				
				// Check Community
				if(this.incomingPacketsFromCommunityQueue.peek() != null)
				{
					this.processCommandFromCommunity();
				}
				
				// Sleep
				Thread.sleep(50);
				
				// Generate a new ID
				ID.generateID();
				
				// Sleep
				Thread.sleep(50);
								
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		// Stop all threads
		this.receiveFromUI.stop();
		this.sender.stop();
		this.receiveFromCommunity.stop();
			
	}

	private void processCommandFromCommunity() {
		// Process a command from the community
		
		// Dequeue the packet from the community
		DatagramPacket communityPacket = this.incomingPacketsFromCommunityQueue.deQueue();
			
		// Create a UDP message
		UDPMessage communityMessage = new UDPMessage(communityPacket);
		
		// Pass the message to my peers
		GossipPartners.getInstance().send(communityMessage); // TODO uncomment
						
		// Check if the ID2 matches is one of our responses
		RequestFromUIController request = RequestManager.getInstance().getRequest(communityMessage.getID2()); 
		
		// Check if the ID2 matches is one of our resources
		Resource resource = ResourceManager.getInstance().getResourceFromID(communityMessage.getID2()); 
		
		if(request != null) 
		{			
			// Check if it is a find
			if(request.getClass() == RequestFromUIControllerToFindResources.class)  
			{
//				System.out.println("I got a response to my find");
				
				// Get the find request
				RequestFromUIControllerToFindResources findRequest = (RequestFromUIControllerToFindResources) request;  		
				
				// Update this request
				findRequest.updateRequest(communityMessage);				
			}
			
			// Check if it is a find or a get
			else if(request.getClass() == RequestFromUIControllerToGetaResource.class) 	
			{
//				System.out.println("I got a response to my get");
				
				RequestFromUIControllerToGetaResource getRequest = (RequestFromUIControllerToGetaResource) request; 			
			
				// Update this request
				getRequest.updateRequest(communityMessage);
			}
				
		}
		// Check if the ID2 matches one of our specific resources that the community wants
		else if(resource != null) 
		{		
			// Get the request part
			byte[] partRequested = new byte[PartNumbers.getLengthInBytes()];
		
			System.arraycopy(communityMessage.getMessage(), ID.getLengthInBytes(), partRequested, 0, PartNumbers.getLengthInBytes());
			
			// Get the integer representation of the part
			int partNumberRequested = 0;
		
			// Convert from a array to int
			partNumberRequested = ByteBuffer.wrap(partRequested).getInt();
//			System.out.println("Somebody is asking part: " + partNumberRequested);
			
			// Decrement part number since we are starting at 0
			partNumberRequested -= 1;
			

			// Create an array to hold a random ID, a part number, and the bytes to send
			byte[] buffer = new byte[ID.getLengthInBytes() + PartNumbers.getLengthInBytes() + resource.getBytesForPart(partNumberRequested).length];
			
			// Copy the random id
			System.arraycopy(ID.idFactory().getBytes(), 0, buffer, 0, ID.getLengthInBytes());
			
			// Copy the part requested after the ID
			System.arraycopy(partRequested, 0, buffer, ID.getLengthInBytes(), PartNumbers.getLengthInBytes());
			
			// Copy the bytes requested after the part number
			System.arraycopy(resource.getBytesForPart(partNumberRequested), 0, buffer, ID.getLengthInBytes() + PartNumbers.getLengthInBytes(), resource.getBytesForPart(partNumberRequested).length);
				
			// Create a message with format resourceID, requestID, TTL, randomId, part number, bytes. 
			UDPMessage resourceMessage = new UDPMessage(communityMessage.getID2(), communityMessage.getID1(), new TimeToLive(), buffer);
			
			// Send to my peers
			GossipPartners.getInstance().send(resourceMessage);		
		}
		// Check if the text criteria matches something we might have
		else if(ResourceManager.getInstance().getResourcesThatMatch(new String(communityMessage.getMessage())).length != 0)
		{
			// Get all the resources that match the criteria
			Resource[] resources = ResourceManager.getInstance().getResourcesThatMatch(new String(communityMessage.getMessage()));
			
//			System.out.println("Somebody found: " + resources.length + " files from us");

			// Process each resource
			for(Resource ourResource : resources)
			{
				// Adding random ID
				StringBuilder resourceInfo = new StringBuilder(new String(ID.idFactory().getBytes()));
								
				// Add mimeType, length, and description
				resourceInfo.append('~');
				resourceInfo.append(ourResource.getMimeType());
				resourceInfo.append('~');
				resourceInfo.append(ourResource.getSizeInBytes());
				resourceInfo.append('~');
				resourceInfo.append(ourResource.getDescription());
						
				// Create a message with format resourceID, peerID, TTL, description
				UDPMessage resourceMessage = new UDPMessage(ourResource.getID(), communityMessage.getID1(), new TimeToLive(), resourceInfo.toString());
				
				// Send to my peers
				GossipPartners.getInstance().send(resourceMessage);	
			}
		}
		else {
//			System.out.println("Testing that the peer is listening to the community: " + new String(communityPacket.getData()) + "\n");
		}
	}

	private void processCommandFromUI() 
	{
		// Process a command from the UI
		
		// Dequeue the packet from the UI
		DatagramPacket uiPacket = this.incomingPacketsFromUIQueue.deQueue();
		
		// Set the command to lower case
		String uiCommand = new String(uiPacket.getData()).toLowerCase().trim();
		
		char delimiter = uiCommand.charAt(0);
				
		// Check if it is a find request
		if(uiCommand.indexOf(delimiter + "find") == 0) 
		{
			// Get an ID for the find request
			ID findId = ID.idFactory();
			
			// create a find request
			RequestFromUIControllerToFindResources findRequest = new RequestFromUIControllerToFindResources(findId, this.uiControllerAddress, this.outgoingPacketsQueue);	
			
			// Save it in our request manager
			RequestManager.getInstance().insertRequest(findRequest);
								
			// Create a UDP message with format RequestID, random ID, TTL, text
			UDPMessage findMessage = new UDPMessage(findId, ID.idFactory(), new TimeToLive(), uiCommand.substring(6));
									
			// Send to the peer controllers
			GossipPartners.getInstance().send(findMessage);
		}
		// Check if it is a get request
		else if(uiCommand.indexOf(delimiter + "get") == 0) 
		{
			// Get the resource id			
			ID resourceID = RequestFromUIControllerToFindResources.getResource(Integer.parseInt(uiCommand.substring(5)));
						
			// Get the part numbers from the resource that we want		
			int partNumbers = (int) Math.ceil(PeerResourceManager.getInstance().getResourceFromID(resourceID).getLength() / (double)  (UDPMessage.getMaximumPacketSizeInBytes() - ID.getLengthInBytes() - PartNumbers.getLengthInBytes()));
			
			// Create a get request id
			ID getId = ID.idFactory();
			
			// create a get request
			RequestFromUIControllerToGetaResource getRequest = new RequestFromUIControllerToGetaResource(getId, resourceID, uiControllerAddress, outgoingPacketsQueue, partNumbers);
			
			// Save it in our request manager
			RequestManager.getInstance().insertRequest(getRequest);

			// Start requesting parts
			getRequest.startAsThread();			
		}
		else if(uiCommand.indexOf(delimiter + "add") == 0)
		{
			try {
				GossipPartners.getInstance().addPartner(new GossipPartner(
															new InetSocketAddress(
																	InetAddress.getByName(uiCommand.substring(5)),
																	12345),
																	this.outgoingPacketsQueue));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}			
		}
		// The UI send an invalid command, send error back
		else
		{
			System.out.println("UIController, you sent a bad request to the PeerController");
		}
		
	}
		
	public Thread startAsThread() 
	{
		// Start this class a a thread
		Thread thread;
		
		thread = new Thread(this);
		
		// Execute the run method
		thread.start();
		
		return thread;
	}
	
	
	public void setDoneFlag(boolean flag)
	{
		// Setter for the done flag
		this.done.set(flag);
	}
	
	public boolean isStopped()
	{
		// Check iff the peer is done processing packets
		return this.done.get();
	}
	
}
