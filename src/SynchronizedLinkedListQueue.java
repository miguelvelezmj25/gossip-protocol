
public class SynchronizedLinkedListQueue extends LinkedListQueue
{
	/**
	 * 
	 * Miguel Velez
	 * April 14, 2015
	 * 
	 * This class implements a synchronized queue using a linked list to preserve worst case run time
	 * of sorting.  
	 * 
	 * Class variables:
	 *  
	 * Constructors:
	 * 		public SynchronizedLinkedListQueue()
	 * 			creates a LinkedListQueue object.
	 * 
	 * Methods:
	 * 
	 *		public synchronized void removeAll()
	 *			removes all the Nodes from the linked list.
	 *
	 *		public synchronized boolean isEmpty()
	 *			returns if the linked list is empty.
	 *
	 *		public synchronized Object peek()
	 *			returns, but does not remove, the front of the linked list.
	 *
	 *		public synchronized Object deQueue()
	 *			removes and returns the front of the linked list.
	 *		
	 *		public synchronized void enQueue(Object data)
	 *			appends the data object to the end of the linked list.
	 *		
	 *		public synchronized void enQueue(LinkedListQueue queue)
	 * 			appends the passed queue to the end of this linked list.
	 *    
	 * Modification History:
	 * 		April 14, 2015
	 * 			Original version
	 * 
	 * 		April 28, 2015
	 * 			Added some comments.
	 *  
	 */

	public SynchronizedLinkedListQueue()
	{
		// Constructs a synchronized linked list queue
		super();
	}
	
	@Override
	public synchronized void removeAll()
	{
		// Removes all elements in the linked list
		super.removeAll();
	}
	
	@Override
	public synchronized Object peek()
	{
		// Returns but does not remove the first element of the queue.
		return super.peek();
	}

	@Override
	public synchronized Object deQueue()
	{
		// Returns and removes the first element of the queue. 
		return super.deQueue();
	}
	
	@Override
	public synchronized void enQueue(Object data)
	{
		// Adds the object to the end of the queue.
		super.enQueue(data);
	}
	
	public synchronized void enQueue(SynchronizedLinkedListQueue queue)
	{
		// Adds a linked list to the end of the queue
		super.enQueue(queue);
	}
	
	@Override
	public synchronized boolean isEmpty()
	{
		// Checks if the linked list is empty
		return super.isEmpty();
	}
	
}
