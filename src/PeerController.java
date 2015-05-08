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
	 */
	
	// TODO should this have an incoming and outgoing queue?
	
//	private InetSocketAddress 		address;
//	private PortNumberPeerCommunity communityPort;
	private AtomicBoolean			done;
	private DatagramReceiver        receiveFromUI;
	private DatagramReceiver        receiveFromCommunity;
	private DatagramSender			sender;
	private IncomingPacketQueue 	incomingPacketsFromUIQueue;
	private IncomingPacketQueue 	incomingPacketsFromCommunityQueue;
	private OutgoingPacketQueue 	outgoingPacketsQueue;
	
	// TODO have to check what parameter we need to get
	public PeerController(PortNumberPeerCommunity communityPort, PortNumberUIPeer uiPort) 
	{
		// TODO what is packet size
		try {
//			this.address = new InetSocketAddress(InetAddress.getLocalHost(), uiPort.get());
			this.done = new AtomicBoolean(false);
			this.incomingPacketsFromUIQueue = new IncomingPacketQueue();
			this.incomingPacketsFromCommunityQueue = new IncomingPacketQueue();
			this.outgoingPacketsQueue = new OutgoingPacketQueue();
			this.receiveFromUI = new DatagramReceiver(new DatagramSocket(uiPort.get()), this.incomingPacketsFromUIQueue, 512);
			this.receiveFromCommunity = new DatagramReceiver(new DatagramSocket(communityPort.get()), this.incomingPacketsFromCommunityQueue, 512);
			this.sender = new DatagramSender(new DatagramSocket(), this.outgoingPacketsQueue, 512);
		} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
//		
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
				// Check Community
				
				
				// Sleep
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		this.receiveFromUI.stop();
		
	}

	private void processCommandFromUI() 
	{
		DatagramPacket packet = this.incomingPacketsFromUIQueue.deQueue();

		System.out.println(new String(packet.getData()));
	}
	
	public Thread startAsThread() 
	{
		Thread thread;
		
		thread = new Thread(this);
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
