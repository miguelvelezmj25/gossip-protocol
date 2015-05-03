import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;


public abstract class DatagramSenderReceiver implements Runnable
{
	
	private AtomicBoolean 				done;
	private DatagramSocket 				datagramSocket;
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
//		System.out.println("DatagramSenderReceiver Running");
		while(!this.isStopped())
		{
//			System.out.println("DatagramSenderReceiver Performing action");
			this.action(this.datagramSocket, this.queue);
			
			try {
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
		// TODO ask jarvis what we need to do here since we are using the same datagram
		// socket for sending and receiving. So if we stop one thread, that thread
		// closes the socket, which creates an exception on the other thread when it
		// tries to close it.
		this.done.set(true);
		this.datagramSocket.close();
	}
	
	/**
	 * Determines if the run method has been stopped.
	 * @return
	 * 		The get method on the atomic boolean.
	 */
	public boolean isStopped()
	{
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
