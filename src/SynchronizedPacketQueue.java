import java.net.DatagramPacket;

/**
 * Abstract class that uses DatagramPackets to catch comipler errors.
 *  
 */
public abstract class SynchronizedPacketQueue extends SynchronizedLinkedListQueue {
	
	public SynchronizedPacketQueue () {
		
	}
	
	/**
	 * Returns but does not remove the first DatagramPacket in the queue
	 * 
	 * @return
	 */
	public DatagramPacket peek() {
		DatagramPacket result = null;
		
		// Check if not null
		if(!super.isEmpty()) {
			result = (DatagramPacket) super.peek();
		}
		
		return result;
		
	}
	
	/**
	 * Returns and removes the first DatagramPacket in the queue
	 * 
	 * @return
	 */
	public DatagramPacket deQueue() {
		DatagramPacket result = null;
		
		// Check if not null
		if(!super.isEmpty()) {
			result = (DatagramPacket) super.deQueue();
		}
		
		return result; 
	}
	
	/**
	 * Adds a DatagramPacket at the end of the queue
	 * 
	 * @param packer
	 */
	public void enQueue(DatagramPacket packet) {
		super.enQueue(packet);
	}
	
	/**
	 * Adds a SynchronizedPacketQueue at the end of the queue
	 * 
	 * @param queue
	 */
	public void enQueue(SynchronizedPacketQueue queue) {
		super.enQueue(queue);
	}
	
}
