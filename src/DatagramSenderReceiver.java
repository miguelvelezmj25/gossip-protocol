import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;


public abstract class DatagramSenderReceiver { // TODO does it extend Runnable?
	/**
	 * 
	 * Miguel Velez
	 * March 9, 2015
	 * 
	 * This class TODO
	 * 
	 * Class variables:
	 * 		static int idLengthInBytes
	 * 			The length in bytes of the ID.
	 * 
	 * 		AtomicBoolean done
	 * 			TODO
	 * 
	 * 		DatagramSocket	datagramSocket
	 * 			TODO
	 * 		
	 * 		int packetSize;
	 * 			TODO
	 * 
	 * 		SynchronizedLinkedListQueue queue
	 * 			TODO
	 * 		
	 * Constructors:
	 * 		public DatagramSenderReceiver(InetSocketAddress inetSocketAddress, SynchronizedLinkedListQueue queue, int packetSize)
	 * 			TODO
	 * 	 
	 * Methods:
	 *		public int getPacketSize
	 *			TODO
	 *
	 *		public void run
	 *			TODO
	 *
	 *		public void stop
	 *			TODO
	 *
	 *		public boolean isStopped
	 *			TODO
	 *			
	 *		public void startAsThread
	 *			TODO
	 *
	 * 		public abstract void action(DatagramSocket datagramSocket, SynchronizedLinkedListQueue queue)
	 * 			TODO
	 *        
	 * Modification History:
	 * 		March 9, 2015
	 * 			Original version
	 *  
	 */
	
	private AtomicBoolean 				done;
	private DatagramSocket 				datagramSocket;
	private int							packetSize;
	private SynchronizedLinkedListQueue queue;
	
	public DatagramSenderReceiver(InetSocketAddress inetSocketAddress, SynchronizedLinkedListQueue queue, int packetSize) 
	{
		// TODO
		// lazySet(boolean newValue)
		// set(boolean newValue)
		// getAndSet(boolean newValue)
		this.done.set(false);
		
		// TODO
		// DatagramSocket(int port, InetAddress laddr)
		// connect(InetAddress address, int port)
		//this.datagramSocket = new DatagramSocket();
		
	
		this.packetSize = packetSize;
		this.queue = queue;
		
	}
	
	public int getPacketSize()
	{
		return this.packetSize;
	}
	
	public void run()
	{
		// TODO
	}
	
	public void stop()
	{
		// TODO
		// lazySet(boolean newValue)
		// set(boolean newValue)
		// getAndSet(boolean newValue)
		this.done.set(false);
	}
		
	public boolean isStopped()
	{
		return this.done.get();
	}
	
	public void startAsThread() 
	{
		// TODO return reference of thread
	}
	
	public abstract void action(DatagramSocket datagramSocket, SynchronizedLinkedListQueue queue);
	
}
