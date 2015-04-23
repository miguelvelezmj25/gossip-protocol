import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;


public abstract class DatagramSenderReceiver implements Runnable
{
	
	private AtomicBoolean 				done;
	private DatagramSocket 				datagramSocket;
	private int							packetSize;
	private SynchronizedLinkedListQueue queue;
	
	/**
	 * Binds the datagramSocket to the inetSocketAddress, and instantiates the queue and packet size
	 * instance variables. Also sets the atomic boolean variable to false.
	 * @param inetSocketAddress
	 * @param queue
	 * @param packetSize
	 * @throws SocketException
	 */
	public DatagramSenderReceiver(InetSocketAddress inetSocketAddress, SynchronizedLinkedListQueue queue, int packetSize) throws SocketException 
	{	
		this.done.set(false);
		this.packetSize = packetSize;
		this.queue = queue;
		datagramSocket = new DatagramSocket(inetSocketAddress);
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
			this.action(this.datagramSocket, this.queue);
			try {
				Thread.sleep(100);
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
	
	public abstract void action(DatagramSocket datagramSocket, SynchronizedLinkedListQueue queue);
	
}
