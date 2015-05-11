import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

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
	 * 		ResourceManager resourceManager
	 * 			Manager for this peer's resources
	 * 
	 * 		RequestManager requestManager
	 * 			Manager for this peer's requests
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
	 * 		May 8, 2015
	 * 			Process requests from the UI
	 * 
	 */
	
	private AtomicBoolean			done;
	private IncomingPacketQueue 	incomingPacketsFromCommunityQueue;
	private IncomingPacketQueue 	incomingPacketsFromUIQueue;
	private DatagramReceiver        receiveFromUI;
	private OutgoingPacketQueue 	outgoingPacketsQueue;
	private DatagramReceiver        receiveFromCommunity;
	private DatagramSender			sender;
	private InetSocketAddress		uiControllerAddress;
	
	// TODO save resources
	
	// TODO have to check what parameter we need to get
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
		GossipPartners.getInstance().send(communityMessage);
						
		// Check if the ID2 matches is one of our responses
		if(RequestManager.getInstance().getRequest(communityMessage.getID1()) != null) // TODO should be 2
		{
			// Check if it is a find
			if(RequestManager.getInstance().getRequest(communityMessage.getID1()).getClass() == RequestFromUIControllerToFindResources.class)  // TODO should be 2
			{
				// TODO done
				// Get the find request
				RequestFromUIControllerToFindResources findRequest = (RequestFromUIControllerToFindResources) RequestManager.getInstance().getRequest(communityMessage.getID1());  // TODO should be 2
				
				// Update this request
				findRequest.updateRequest(communityMessage);				
			}
			// Check if it is a find or a get
			else if(RequestManager.getInstance().getRequest(communityMessage.getID1()).getClass() == RequestFromUIControllerToGetaResource.class) // TODO should be 2
			{
				// TODO still need to implement
				RequestFromUIControllerToGetaResource getRequest = (RequestFromUIControllerToGetaResource) RequestManager.getInstance().getRequest(communityMessage.getID1());  // TODO should be 2

				System.out.println("get: " + getRequest.getID());
			}
				
		}
		// Check if the ID2 matches one of our resources
		else if(ResourceManager.getInstance().getResourceFromID(communityMessage.getID2()) != null)
		{
			// TODO still need to implement
			// TODO the community has requested an specific resource
			// Create a message with format resourceID, requestID, TTL, description // TODO
			UDPMessage resourceMessage = new UDPMessage(communityMessage.getID2(), communityMessage.getID1(), new TimeToLive(), "");
			
			// Send to my peers
			GossipPartners.getInstance().send(resourceMessage);		
			
		}
		// Check if the text criteria matches something we might have
		else if(ResourceManager.getInstance().getResourcesThatMatch(new String(communityMessage.getMessage())).length != 0)
		{
			// TODO still need to implement
			// Get all the resources that match the criteria
			Resource[] resources = ResourceManager.getInstance().getResourcesThatMatch(new String(communityMessage.getMessage()));
			
			// Process each resource
			for(Resource resource : resources)
			{
				// Adding random ID
				StringBuilder resourceInfo = new StringBuilder(ID.idFactory().getAsHex());
				
				// Add mimeType, length, and description
				resourceInfo.append(",");
				resourceInfo.append(resource.getMimeType());
				resourceInfo.append(",");
				resourceInfo.append(resource.getSizeInBytes());
				resourceInfo.append(",");
				resourceInfo.append(resource.getDescription());
				resourceInfo.append(",");
						
				// Create a message with format resourceID, peerID, TTL, description
				UDPMessage resourceMessage = new UDPMessage(ID.idFactory(), communityMessage.getID1(), new TimeToLive(), resourceInfo.toString());
				
				// Send to my peers
				GossipPartners.getInstance().send(resourceMessage);		
			}
		}
		else {
			System.out.println("Testing receiving from community");
		}
	}

	private void processCommandFromUI() 
	{
		// Process a command from the UI
		
		// Dequeue the packet from the UI
		DatagramPacket uiPacket = this.incomingPacketsFromUIQueue.deQueue();
		
		// Set the command to lower case
		String uiCommand = new String(uiPacket.getData()).toLowerCase();
		
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
									
//			GossipPartners.getInstance().send(findMessage);			// TODO uncomment
			
/////////////////// TODO delete since we are done
//			System.out.println("id1: " + findMessage.getID1());
//			System.out.println("id2: " + findMessage.getID2());
//			System.out.println("tll: " + findMessage.getTimeToLive());
//			System.out.println(new String(findMessage.getMessage()));

			DatagramPacket send = findMessage.getDatagramPacket();
			
			send.setPort(12345);
			
			try {
				send.setAddress(InetAddress.getLocalHost());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.outgoingPacketsQueue.enQueue(send);
/////////////////// TODO delete	
		}
		// Check if it is a get request
		else if(uiCommand.indexOf(delimiter + "get") == 0) 
		{
			ID getId = ID.idFactory();
			
			// create a get request
			RequestFromUIControllerToGetaResource getRequest = new RequestFromUIControllerToGetaResource(getId, uiControllerAddress, this.outgoingPacketsQueue);
			
			// save it in our request manager
			RequestManager.getInstance().insertRequest(getRequest);
						
			// Split the response
			String uiResourceID = uiCommand.split(""+ delimiter)[2];
			
			ID resourceID = RequestFromUIControllerToFindResources.getResource(Integer.parseInt(uiResourceID));
			
			// Create a UDP message with format RequestID, ResourceID, TTL, text // TODO we need to get the resource id
			UDPMessage getMessage = new UDPMessage(getId, resourceID, new TimeToLive(), uiCommand.split(""+ delimiter)[3]);
			
//			GossipPartners.getInstance().send(getMessage);			// TODO uncomment
			
		System.out.println("id1: " + getMessage.getID1());
		System.out.println("id2: " + getMessage.getID2());
		System.out.println("tll: " + getMessage.getTimeToLive());
		System.out.println(new String(getMessage.getMessage()));
			
/////////////////// TODO delete
//
//		DatagramPacket send = getMessage.getDatagramPacket();
//		
//		send.setPort(12345);
//		
//		try {
//			send.setAddress(InetAddress.getLocalHost());
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		this.outgoingPacketsQueue.enQueue(send);
/////////////////// TODO delete	
		}
		// The UI send an invalid command, send error back
		else
		{
			// TODO We are done
			System.out.println("UIController, you sent a bad request to the PeerController");
//			String message = "UIController, you sent a bad request to the PeerController"; 
//			byte[] buffer = new byte[message.getBytes().length];
//			
//			// Create a packet to send the error message
//			DatagramPacket errorPacket = new DatagramPacket(buffer, buffer.length);
//						
//			// Set the address
//			errorPacket.setAddress(this.uiControllerAddress.getAddress());;
//				
//			// Set the listening port of the UI
//			errorPacket.setPort(this.uiControllerAddress.getPort());
//			
//			// Set the data
//			errorPacket.setData(message.getBytes());
//			
//			// Enqueue the packet in the outgoing queue
//			outgoingPacketsQueue.enQueue(errorPacket);
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
