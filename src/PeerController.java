import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
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
	 * 
 	 * Constructors:
 	 * 
 	 * 		public Command() 
 	 * 			create a default command
 	 * 
 	 * 
	 * Methods:
	 *	
	 *		public abstract void run();
	 * 			execute the command
	 * 
	 *      
	 * Modification History:
	 * 		May 3, 2015
	 * 			Original version
	 * 
	 * 		May 8, 2015
	 * 			Implemented receiving from the UI
	 */
	
	private AtomicBoolean			done;
	private IncomingPacketQueue 	incomingPacketsFromCommunityQueue;
	private IncomingPacketQueue 	incomingPacketsFromUIQueue;
	private DatagramReceiver        receiveFromUI;
	private OutgoingPacketQueue 	outgoingPacketsQueue;
	private DatagramReceiver        receiveFromCommunity;
	private DatagramSender			sender;
	
	// TODO have to check what parameter we need to get
	public PeerController(PortNumberPeerCommunity communityPort, PortNumberPeerUI uiPort) 
	{
		// TODO what is packet size
		try {
			this.done = new AtomicBoolean(false);
			
			this.incomingPacketsFromUIQueue = new IncomingPacketQueue();
			this.receiveFromUI = new DatagramReceiver(new DatagramSocket(uiPort.get()), this.incomingPacketsFromUIQueue, 512);
			this.incomingPacketsFromCommunityQueue = new IncomingPacketQueue();
			this.receiveFromCommunity = new DatagramReceiver(new DatagramSocket(communityPort.get()), this.incomingPacketsFromCommunityQueue, 512);
	
			this.outgoingPacketsQueue = new OutgoingPacketQueue();
			this.sender = new DatagramSender(new DatagramSocket(), this.outgoingPacketsQueue, 512);
			
		} catch (SocketException se) {
				se.printStackTrace();
			} 
	
	}
	
	@Override
	public void run() 
	{
		// Start listening for messages from the UI
		this.receiveFromUI.startAsThread();
		
		// Start listening for messages from the Community
//		this.receiveFromCommunity.startAsThread();
		
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
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		this.receiveFromUI.stop();
		
	}

	private void processCommandFromCommunity() {
		
	}

	private void processCommandFromUI() 
	{
		// Process a command from the UI
		// Dequeue the packet from the UI
		DatagramPacket packet = this.incomingPacketsFromUIQueue.deQueue();

		System.out.println("We got: " + new String(packet.getData()));
		
		// TODO check if find
		// TODO check if get
		// TODO otherwise, it is trash
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
		this.done.set(flag);
	}
	
	public boolean isStopped()
	{
		return this.done.get();
	}
	
	public void findRequest() 
	{
		
	}

	public void getRequest()
	{
		
	}

}
