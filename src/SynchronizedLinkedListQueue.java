import java.util.*;


public class SynchronizedLinkedListQueue
{
	private LinkedList internalVector;
	
	/**
	 * Instantiates the internal vector.
	 */
	public SynchronizedLinkedListQueue()
	{
		this.internalVector = new LinkedList();
	}
	
	/**
	 * Reinstantiates the internal vector.
	 */
	public synchronized void removeAll()
	{
		this.internalVector = new LinkedList();
	}
	
	/**
	 * Returns the first element of the queue.
	 * @return
	 * 		The first element of the queue.
	 */
	public synchronized Object peek()
	{
		return this.internalVector.peek();
	}
	
	/**
	 * Returns and removes the first element of the queue. 
	 * @return
	 * 		The first element of the queue.
	 */
	public synchronized Object deQueue()
	{
		return this.internalVector.peek();
	}
	
	/**
	 * Addds the object to the end of the queue.
	 * @param data
	 */
	public synchronized void enQueue(Object data)
	{
		this.internalVector.add(data);
	}
	
	/**
	 * Calls the internal vectors addAll method on the internal vector of the parameter, 
	 * which adds the entirety of the passed queue onto the end of the current queue.
	 * @param queue
	 */
	public synchronized void enQueue(SynchronizedLinkedListQueue queue)
	{
		this.internalVector.addAll(queue.internalVector);
	}
	
	/**
	 * Returns the internal vector's isEmpty() function.
	 * @return
	 * 		True if the internal vector is empty, false if not.
	 */
	public synchronized boolean isEmpty()
	{
		return this.internalVector.isEmpty();
	}
	
	@Override
	/**
	 * Constructs a string representation of the entire queue by using
	 * the two string methods on the objects in the queue.
	 */
	public String toString()
	{
		String toReturn;
		toReturn = "";
		for(Object o : this.internalVector)
		{
			toReturn = toReturn + " "+ o;
		}
		
		return toReturn;
	}
}
