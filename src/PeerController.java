import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
	private RequestManager			requestManager;
	private ResourceManager			resourceManager;
	private DatagramSender			sender;
	
	// TODO have to check what parameter we need to get
	public PeerController(PortNumberPeerCommunity communityPort, PortNumberPeerUI uiPort) 
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
			
			this.requestManager = new RequestManager();
			this.resourceManager = new ResourceManager();
					
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
//		this.receiveFromCommunity.startAsThread(); // TODO uncomment
		
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
//		this.receiveFromCommunity.stop(); // TODO uncomment
		
	}


	private void processCommandFromCommunity() {
		// Process a command from the community
		// Dequeue the packet from the community
		DatagramPacket communityPacket = this.incomingPacketsFromCommunityQueue.deQueue();
		
		// Create a UDP message
		UDPMessage response = new UDPMessage(communityPacket);
		
		// TODO send this UDP to the gossip partners
		
		// Check if the ID2 matches one of our responses
		if(this.requestManager.getRequest(response.getID2()) != null)
		{
			// TODO what happens here
			
		}
		// Check if the ID2 matches one of our resources
		else if(this.resourceManager.getResourceFromID(response.getID2()) != null)
		{
			// TODO what happens here
		}
		// Check if the text criteria matches something we have
		else if(this.resourceManager.getResourcesThatMatch(new String(response.getMessage())) != null)
		{
			// TODO what happens here
		}
	}

	private void processCommandFromUI() 
	{
		// Process a command from the UI
		// Dequeue the packet from the UI
		DatagramPacket packet = this.incomingPacketsFromUIQueue.deQueue();

//		System.out.println("We got: " + new String(packet.getData()));
		
		// Set the request to lower case
		String request = new String(packet.getData()).toLowerCase();
		
		// Check if it is a find request
		if(request.contains("find")) 
		{
			// Get an ID for the find request
			ID id = ID.idFactory();
			
			// create a find request
			RequestFromUIControllerToFindResources findRequest = new RequestFromUIControllerToFindResources(id);	
			
			// Save it in our request manager
			this.requestManager.insertRequest(findRequest);
			
//			System.out.println(this.requestManager.getRequest(id));
			
			// Create a UDP message with format RequestID, random ID, TTL, text
			// TODO is this ok for TTL?
			UDPMessage findMessage = new UDPMessage(id, ID.idFactory(), new TimeToLive(new Random().nextInt(100) + 1), "frogs");
			
			// TODO call send message in gossip partners by passing a UDP message
			
		}
		// Check if it is a get request
		else if(request.contains("get")) 
		{
			// TODO call send message in gossip partners by passing a UDP message
		}
		// The UI send an invalid command, send error back
		else
		{
			String message = "UIController, you sent a bad request to the PeerController"; 
			byte[] buffer = new byte[message.getBytes().length];
			
			// Create a packet to send the error message
			DatagramPacket errorPacket = new DatagramPacket(buffer, buffer.length);
			
			try {
				// Send to the UI controller
				errorPacket.setAddress(InetAddress.getLocalHost());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			
			// Set the listening port of the UI
			// TODO do not hard code
			errorPacket.setPort(new PortNumberUIPeer(54321).get());
			
			// Set the data
			errorPacket.setData(message.getBytes());
			
			// Enqueue the packet in the outgoing queue
			outgoingPacketsQueue.enQueue(errorPacket);
			
			System.out.println("Sent error message");
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
	
//	public void findRequest() 
//	{
//		
//	}
//
//	public void getRequest()
//	{
//		
//	}

}
