
public class SynchronizedLinkedListQueue extends LinkedListQueue {
	/**
	 * 
	 * Miguel Velez
	 * March 7, 2015
	 * 
	 * This class implements a synchronized queue using a linked list to preserve worst case run time
	 * of sorting. This class extends the LinkedListQueue class.
	 * 
	 * Class variables:
	 * 		Node front;
	 * 			the front Node of the linked list.
	 * 
	 * 		Node rear;
	 * 			the rear node of the linked list.
	 *  
	 * Constructors:
	 * 		public LinkedListQueue()
	 * 			creates a LinkedListQueue object and set the front and rear Nodes
	 * 			to null.
	 * 
	 * Methods:
	 * 
	 *		public void removeAll()
	 *			calls super class to remove all the Nodes from the linked list.
	 *
	 *		public boolean isEmpty()
	 *			calls the super class to return if the linked list is empty.
	 *
	 *		public Object peek()
	 *			calls the super class to return, but do not remove, the front of the linked list.
	 *
	 *		public Object deQueue()
	 *			calls the super class to remove and return the front of the linked list.
	 *		
	 *		public void enQueue(Object data)
	 *			calls the super class to append the data object to the end of the linked list.
	 *		
	 *		public void enQueue(LinkedListQueue queue)
	 * 			calls the super class to append the passed queue to the end of this linked list.
	 *    
	 * Modification History:
	 * 		March 7, 2015
	 * 			Original version
	 * 
	 * 		March 9, 2015
	 * 			Added checks for empty in peek and deQueue
	 *  
	 */
	
	public SynchronizedLinkedListQueue()
	{
		super();
	}
	
	@Override
	public synchronized void removeAll()
	{
		super.removeAll();
	}
	
	@Override
	public boolean isEmpty() 
	{
		return super.isEmpty();
	}
	
	@Override
	public Object peek() 
	{
		Object result = null;
		
		if(!super.isEmpty()) 
		{
			result = super.peek();
		}
		
		return result;
	}
	
	@Override
	public Object deQueue() 
	{
		Object result = null;
		
		if(!super.isEmpty()) 
		{
			result = super.deQueue();
		}
		
		return result;
	}
	
	@Override
	public void enQueue(Object data)
	{
		super.enQueue(data);
	}
	
	public void enQueue(SynchronizedLinkedListQueue queue)
	{
		super.enQueue(queue);		
	}

}
