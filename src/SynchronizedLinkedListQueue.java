
public class SynchronizedLinkedListQueue extends LinkedListQueue
{
	/**
	 * Instantiates the internal vector.
	 */
	public SynchronizedLinkedListQueue()
	{
		super();
	}
	
	/**
	 * Reinstantiates the internal vector.
	 */
	@Override
	public synchronized void removeAll()
	{
		super.removeAll();
	}
	
	/**
	 * Returns the first element of the queue.
	 * @return
	 * 		The first element of the queue.
	 */
	@Override
	public synchronized Object peek()
	{
		return super.peek();
	}
	
	/**
	 * Returns and removes the first element of the queue. 
	 * @return
	 * 		The first element of the queue.
	 */
	@Override
	public synchronized Object deQueue()
	{
		return super.deQueue();
	}
	
	/**
	 * Addds the object to the end of the queue.
	 * @param data
	 */
	@Override
	public synchronized void enQueue(Object data)
	{
		super.enQueue(data);
	}
	
	/**
	 * Calls the internal vectors addAll method on the internal vector of the parameter, 
	 * which adds the entirety of the passed queue onto the end of the current queue.
	 * @param queue
	 */
	public synchronized void enQueue(SynchronizedLinkedListQueue queue)
	{
		super.enQueue(queue);
	}
	
	/**
	 * Returns the internal vector's isEmpty() function.
	 * @return
	 * 		True if the internal vector is empty, false if not.
	 */
	@Override
	public synchronized boolean isEmpty()
	{
		return super.isEmpty();
	}
	
//	@Override
//	/**
//	 * Constructs a string representation of the entire queue by using
//	 * the two string methods on the objects in the queue.
//	 */
//	public String toString()
//	{
//		String toReturn;
//		toReturn = "";
//		for(Object o : this.internalVector)
//		{
//			toReturn = toReturn + " "+ o;
//		}
//		
//		return toReturn;
//	}
}
