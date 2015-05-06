import java.net.InetSocketAddress;

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
	
	private InetSocketAddress 		address;
	private PortNumberPeerCommunity communityPort;
	private boolean 				done;
	private PortNumberUIPeer 		uiPort;
	
	// TODO have to check what parameter we need to get
	public PeerController(InetSocketAddress address, PortNumberPeerCommunity communityPort, PortNumberUIPeer uiPort) 
	{
		this.done = false;
	}
	
	public InetSocketAddress getAddress() 
	{
		return this.address;
	}
	
	public void setDoneFlag(boolean flag)
	{
		done = flag;
	}
	
	public void findRequest() 
	{
		
	}

	public void getRequest()
	{
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
