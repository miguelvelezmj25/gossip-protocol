import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry.Entry;

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
	 * 	 	May 11, 2015
	 * 			Implemented sending and receiving a part
	 * 
	 */
	
	private AtomicBoolean					done;
	private IncomingPacketQueue 			incomingPacketsFromCommunityQueue;
	private IncomingPacketQueue 			incomingPacketsFromUIQueue;
	private DatagramReceiver       			receiveFromUI;
	private OutgoingPacketQueue 			outgoingPacketsQueue;
	private DatagramReceiver        		receiveFromCommunity;
	private HashMap<ID, ResourceRequester> 	resourceRequesters;
	private DatagramSender					sender;
	private InetSocketAddress				uiControllerAddress;
	
	// TODO might need a hashmap of partRequesters
	
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
			
			this.resourceRequesters = new HashMap<ID, ResourceRequester>();
			
			// Testing
			GossipPartners.getInstance().addPartner(new GossipPartner(
														new InetSocketAddress(
																this.uiControllerAddress.getAddress(),
																12345)
															, this.outgoingPacketsQueue));

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
		
//		Iterator<Map.Entry<ID,ResourceRequester>> requesterThreads = this.resourceRequesters.entrySet().iterator();
//		
//		while(requesterThreads.hasNext())
//		{
//			ResourceRequester requester = requesterThreads.next().getValue();
//			
//			requester.setDoneFlag(true);
//			requester.notify();
//		}
		
		
	}

	private void processCommandFromCommunity() {
		// Process a command from the community
		
		// Dequeue the packet from the community
		DatagramPacket communityPacket = this.incomingPacketsFromCommunityQueue.deQueue();
			
		// Create a UDP message
		UDPMessage communityMessage = new UDPMessage(communityPacket);
		
		// Pass the message to my peers
//		GossipPartners.getInstance().send(communityMessage);
						
		// Check if the ID2 matches is one of our responses
		RequestFromUIController request = RequestManager.getInstance().getRequest(communityMessage.getID2()); 
		
		// Check if the ID2 matches is one of our resources
		Resource resource = ResourceManager.getInstance().getResourceFromID(communityMessage.getID2()); 
		
		if(request != null) 
		{			
			// Check if it is a find
			if(request.getClass() == RequestFromUIControllerToFindResources.class)  
			{
				// Get the find request
				RequestFromUIControllerToFindResources findRequest = (RequestFromUIControllerToFindResources) request;  		
				
				// Update this request
				findRequest.updateRequest(communityMessage);				
			}
			
			// Check if it is a find or a get
			else if(request.getClass() == RequestFromUIControllerToGetaResource.class) 	
			{
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
		
			System.arraycopy(communityMessage.getMessage(), ID.getLengthInBytes(), partRequested, 0, partRequested.length);
			
			// Get the integer representation of the part
			int partNumberRequested = 0;
		
			// Get an int from a byte array
			for(int i = 0; i < PartNumbers.getLengthInBytes(); i++) {
				partNumberRequested = partNumberRequested | ((partRequested[i] & 0xFF) << ((PartNumbers.getLengthInBytes() - 1 - i) * 8));
			}
			
			// Get a random ID
			StringBuilder resourceResponse = new StringBuilder(new String(ID.idFactory().getBytes()));
			
			// Attach part requested
			resourceResponse.append(new String(partRequested));
						
			// Attache the bytes of the resource
			resourceResponse.append(new String(resource.getBytesForPart(partNumberRequested)));
			
			// Create a message with format resourceID, requestID, TTL, randomId, part number, bytes. 
			UDPMessage resourceMessage = new UDPMessage(communityMessage.getID2(), communityMessage.getID1(), new TimeToLive(), resourceResponse.toString());
			
			// Send to my peers
			GossipPartners.getInstance().send(resourceMessage);
			
/////////////////// TODO delete testing
////		System.out.println("id1: " + findMessage.getID1());
////		System.out.println("id2: " + findMessage.getID2());
////		System.out.println("tll: " + findMessage.getTimeToLive());
////		System.out.println(new String(getMessage.getMessage()));
//
//			DatagramPacket send = resourceMessage.getDatagramPacket();
//				
//			send.setPort(12345);
//			
//			try {
//				send.setAddress(InetAddress.getLocalHost());
//	//			send.setAddress(InetAddress.getByName("140.209.121.69"));
//			} catch (UnknownHostException e) {
//				e.printStackTrace();
//			}
//			
//			outgoingPacketsQueue.enQueue(send);
///////////////////// TODO delete	testing
			
		}
		// Check if the text criteria matches something we might have
		else if(ResourceManager.getInstance().getResourcesThatMatch(new String(communityMessage.getMessage())).length != 0)
		{
			// Get all the resources that match the criteria
			Resource[] resources = ResourceManager.getInstance().getResourcesThatMatch(new String(communityMessage.getMessage()));
			
			// Process each resource
			for(Resource ourResource : resources)
			{
				// Adding random ID
				StringBuilder resourceInfo = new StringBuilder(new String(ID.idFactory().getBytes()));
								
				// Add mimeType, length, and description
				resourceInfo.append(",");
				resourceInfo.append(ourResource.getMimeType());
				resourceInfo.append(",");
				resourceInfo.append(ourResource.getSizeInBytes());
				resourceInfo.append(",");
				resourceInfo.append(ourResource.getDescription());
						
				// Create a message with format resourceID, peerID, TTL, description
				UDPMessage resourceMessage = new UDPMessage(ourResource.getID(), communityMessage.getID1(), new TimeToLive(), resourceInfo.toString());
				
				// Send to my peers
				GossipPartners.getInstance().send(resourceMessage);	
				
//////////////////////////////////////// TODO delete testing
////				System.out.println("id1: " + resourceMessage.getID1().getBytes().length);
////				System.out.println("id2: " + resourceMessage.getID2().getBytes().length);
////				System.out.println("tll: " + resourceMessage.getTimeToLive());
////				System.out.println("message of the file we just sent: " + new String(resourceMessage.getMessage()));
//	
//				DatagramPacket send = resourceMessage.getDatagramPacket();
//					
//				send.setPort(12345);
//				
//				try {
//					send.setAddress(InetAddress.getLocalHost());
////					send.setAddress(InetAddress.getByName("140.209.121.69"));
//				} catch (UnknownHostException e) {
//					e.printStackTrace();
//				}
//				
//				this.outgoingPacketsQueue.enQueue(send);
//////////////////////////////////////// TODO delete	testing
				
			}
		}
		else {
			System.out.println("Testing that the peer is listening to the community: " + new String(communityMessage.getMessage()).trim());
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
			
/////////////////// TODO delete testing
////			System.out.println("id1: " + findMessage.getID1());
////			System.out.println("id2: " + findMessage.getID2());
////			System.out.println("tll: " + findMessage.getTimeToLive());
////			System.out.println(new String(findMessage.getMessage()));
//
//			DatagramPacket send = findMessage.getDatagramPacket();
//				
//			send.setPort(12345);
//			
//			try {
//				send.setAddress(InetAddress.getLocalHost());
////				send.setAddress(InetAddress.getByName("140.209.121.69"));
//			} catch (UnknownHostException e) {
//				e.printStackTrace();
//			}
//			
//			this.outgoingPacketsQueue.enQueue(send);
///////////////////// TODO delete	testing
		}
		// Check if it is a get request
		else if(uiCommand.indexOf(delimiter + "get") == 0) 
		{
			// Get the resource id			
			ID resourceID = RequestFromUIControllerToFindResources.getResource(Integer.parseInt(uiCommand.substring(5)));
						
			int partNumbers = (int) Math.ceil(ResourceManager.getInstance().getResourceFromID(resourceID).getSizeInBytes() / (double) 456);
						
			// Create and run the class that will request the resource
			ResourceRequester resourceRequester = new ResourceRequester(resourceID, partNumbers);
			
//			// Save the requester in our collection
//			this.resourceRequesters.put(resourceID, resourceRequester);

			resourceRequester.startAsThread();
		}
		// The UI send an invalid command, send error back
		else
		{
			System.out.println("UIController, you sent a bad request to the PeerController");
			
			// TODO leave this here in case we want to test later sending to UI
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
		
	
	
	private class ResourceRequester implements Runnable {
		/**
		 * 
		 * Miguel Velez
		 * May 13, 2015
		 * 
		 * This class is in charge of requesting parts of resource
		 * 
		 * Class variables:
		 * 
		 * 		ID resourceID
		 * 			the resource id we want to get
		 * 
		 * 		PartNumbers partNumbers
		 * 			the part numbers that we need to get
		 * 
		 * 		AtomicBoolean done
		 * 			Check if we are done processing packets
		 * 
		 *  
	 	 * Constructors:
	 	 * 
	 	 * 		public ResourceRequester(ID resourceID, int partNumbers) 
	 	 * 			create a resource requester
	 	 * 
	 	 *  
		 * Methods:
		 * 	
		 * 		public void run() 
		 * 			request one part at a time
		 * 
		 *  	public Thread startAsThread()
		 *  		start this class as a thread
		 *  	
		 *  	public boolean isStopped()
		 *  		check if done requesting all parts
		 *  
		 *  	public void setDoneFlag(boolean flag)
		 *  		set the done flag
		 *    	 
		 *          
		 * Modification History:
		 * 		May 13, 2015
		 * 			Original version
		 *  
		 */

		private ID 				resourceID;
		private PartNumbers 	partNumbers;
		private AtomicBoolean	done;
		
		public ResourceRequester(ID resourceID, int partNumbers) 
		{
			this.resourceID = resourceID;
			this.partNumbers = new PartNumbers(partNumbers);
			this.done = new AtomicBoolean(false);
		}
		
		@Override
		public void run() {		
			
			while(!this.isStopped()) 
			{
				// Get all the part numbers
//			for(int i = 0; i < this.partNumbers.get(); i++)
				for(int i = 0; i < 1; i++)
				{
					// Get a byte array from the part number
					byte[] partNumber = new byte[4];
					
					for(int j = 0; j < PartNumbers.getLengthInBytes(); j++) {
						partNumber[j] = (byte) (i >> ((PartNumbers.getLengthInBytes() - 1 - j) * 8));
					}
					
					// Create a get request id
					ID getId = ID.idFactory();
					
					// create a get request
					RequestFromUIControllerToGetaResource getRequest = new RequestFromUIControllerToGetaResource(getId, uiControllerAddress, outgoingPacketsQueue, this.partNumbers);
					
					// Save it in our request manager
					RequestManager.getInstance().insertRequest(getRequest);
					
					// Create a UDP message with format RequestID, ResourceID, TTL, RandomID, partNumber			
					UDPMessage getMessage = new UDPMessage(getId, resourceID, new TimeToLive(), new String(ID.idFactory().getBytes()) + new String(partNumber)); 
					
					// Send to peers
					GossipPartners.getInstance().send(getMessage);	
					
					
					
/////////////////// TODO delete testing
////			System.out.println("id1: " + findMessage.getID1());
////			System.out.println("id2: " + findMessage.getID2());
////			System.out.println("tll: " + findMessage.getTimeToLive());
//				System.out.println(new String(getMessage.getMessage()));
//
//			DatagramPacket send = getMessage.getDatagramPacket();
//				
//			send.setPort(12345);
//			
//			try {
//				send.setAddress(InetAddress.getLocalHost());
////				send.setAddress(InetAddress.getByName("140.209.121.69"));
//			} catch (UnknownHostException e) {
//				e.printStackTrace();
//			}
//			
//			outgoingPacketsQueue.enQueue(send);
///////////////////// TODO delete	testing
					
//					System.out.println("yield");
//					try {
//						wait();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
					try {
						Thread.sleep(400);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
//					
//					System.out.println("woke up");
//					
//					
				}
				
				// stop this thread
				this.done.set(true);
				
//				System.out.println("STOP REQUESTER");
				
			}

			
			
		}
		
		public Thread startAsThread() {
			// Start this class a a thread
			Thread thread;
			
			thread = new Thread(this);
			
			// Execute the run method
			thread.start();
			
			return thread;
		}
		
		public boolean isStopped()
		{
			// Check iff the peer is done processing packets
			return this.done.get();
		}
		
		public void setDoneFlag(boolean flag)
		{
			// Setter for the done flag
			this.done.set(flag);
		}
			
	}
}
