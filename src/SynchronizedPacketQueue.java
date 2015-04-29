import java.net.DatagramPacket;

/**
 * Abstract class that uses DatagramPackets to catch comipler errors.
 *  
 */
public abstract class SynchronizedPacketQueue extends SynchronizedLinkedListQueue 
{
	/**
	 * 
	 * Miguel Velez
	 * April 14, 2015
	 * 
	 * This class implements a synchronized queue of packets to use the compiler to catch data
	 * type errors.
	 * 
	 * Class variables:
	 *  
	 * Constructors:
	 * 		public SynchronizedPacketQueue () 
	 * 			construct a queue
	 *  
	 * Methods:
	 * 
	 * 		public DatagramPacket peek() 
	 * 			returns but does not remove the first DatagramPacket in the queue.
	 * 
	 * 		public DatagramPacket deQueue()
	 * 			returns and removes the first DatagramPacket in the queue.
	 * 
	 * 		public void enQueue(DatagramPacket packet) 
	 * 			adds a DatagramPacket at the end of the queue.
	 * 
	 * 		public void enQueue(SynchronizedPacketQueue queue) 
	 * 			adds a SynchronizedPacketQueue at the end of the queue.
	 * 
	 * Modification History:
	 * 		April 14, 2015
	 * 			Original version
	 * 
	 * 		April 28, 2015
	 * 			Added some comments.
	 */


	public SynchronizedPacketQueue () 
	{
		// Construct a queue
		super();
	}
	
	/**
	 * Returns but does not remove the first DatagramPacket in the queue
	 * 
	 * @return
	 */
	@Override
	public DatagramPacket peek() 
	{
		DatagramPacket result = null;
		
		// Check if not null
		if(!super.isEmpty()) 
		{
			result = (DatagramPacket) super.peek();
		}
		
		return result;
		
	}
	
	/**
	 * Returns and removes the first DatagramPacket in the queue
	 * 
	 * @return
	 */
	@Override
	public DatagramPacket deQueue() 
	{
		DatagramPacket result = null;
		
		// Check if not null
		if(!super.isEmpty()) 
		{
			result = (DatagramPacket) super.deQueue();
		}
		
		return result; 
	}
	
	/**
	 * Adds a DatagramPacket at the end of the queue
	 * 
	 * @param packer
	 */
	public void enQueue(DatagramPacket packet) 
	{
		super.enQueue(packet);
	}
	
	/**
	 * Adds a SynchronizedPacketQueue at the end of the queue
	 * 
	 * @param queue
	 */
	public void enQueue(SynchronizedPacketQueue queue) 
	{
		super.enQueue(queue);
	}
	
}
