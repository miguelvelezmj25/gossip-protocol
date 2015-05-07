import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;


public abstract class DatagramSenderReceiver implements Runnable
{
	
	private AtomicBoolean 				done;
	protected DatagramSocket 				datagramSocket; // TODO CHANGE TO PRIVATE
	private int							packetSize;
	private SynchronizedPacketQueue 	queue;
	
	/**
	 * Binds the datagramSocket to the inetSocketAddress, and instantiates the queue and packet size
	 * instance variables. Also sets the atomic boolean variable to false.
	 * @param inetSocketAddress
	 * @param queue
	 * @param packetSize
	 * @throws SocketException
	 */
	public DatagramSenderReceiver(DatagramSocket datagramSocket, SynchronizedPacketQueue queue, int packetSize) throws SocketException 
	{	
		this.done = new AtomicBoolean(); //atomicBoolean constructor defaults to false
		this.packetSize = packetSize;
		this.queue = queue;
		this.datagramSocket = datagramSocket;
	}
	
	/**
	 * Basic getter.
	 * @return
	 * 		packet size
	 */
	public int getPacketSize()
	{
		return this.packetSize;
	}
	
	/**
	 * While the atomic boolean is not set to done, the method runs and performs the given action, sleeping
	 * 100 miliseconds after every action.
	 */
	public void run()
	{
		while(!this.isStopped())
		{
			System.out.println("Start Receive from peer");
			try {
				System.out.println("Receive from peer performing action");
				this.action(this.datagramSocket, this.queue);
				System.out.println("Receive from peer done action");
				
//				System.out.println("DatagramSenderReceiver Sleeping");
				Thread.sleep(50);
			} catch (InterruptedException e) {
				System.err.println("Thread sleep method was interrupted.");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Sets the atomic boolean to true, and closes the datagram socket.
	 */
	public void stop()
	{
		this.done.set(true);
		if(!this.datagramSocket.isClosed())
		{
			System.out.println("Receive from peer port trying to close: " + this.datagramSocket.getPort());
			this.datagramSocket.close();
			System.out.println("Receive from peer closed port");
		}
	}
	
	/**
	 * Determines if the run method has been stopped.
	 * @return
	 * 		The get method on the atomic boolean.
	 */
	public boolean isStopped()
	{
		System.out.println("Send to peer done: " + this.done.get());
		return this.done.get();
	}
	
	/**
	 * Starts a new thread and returns a reference to the thread.
	 * @return
	 * 		A reference to the newly started thread. 
	 */
	public Thread startAsThread() 
	{
		Thread thread;
		
		thread = new Thread(this);
		thread.start();
		
		return thread;
	}
	
	public abstract void action(DatagramSocket datagramSocket, SynchronizedPacketQueue queue);
	
}
