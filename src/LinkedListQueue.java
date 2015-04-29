
public class LinkedListQueue 
{
	/**
	 * 
	 * Miguel Velez
	 * February 19, 2015
	 * 
	 * This class implements a queue using a linked list to preserve worst case run time
	 * of sorting.  
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
	 *			removes all the Nodes from the linked list.
	 *
	 *		public boolean isEmpty()
	 *			returns if the linked list is empty.
	 *
	 *		public Object peek()
	 *			returns, but does not remove, the front of the linked list.
	 *
	 *		public Object deQueue()
	 *			removes and returns the front of the linked list.
	 *		
	 *		public void enQueue(Object data)
	 *			appends the data object to the end of the linked list.
	 *		
	 *		public void enQueue(LinkedListQueue queue)
	 * 			appends the passed queue to the end of this linked list.
	 *    
	 * Modification History:
	 * 		September 12, 2015
	 * 			Original version
	 * 
	 * 		September 13, 2015
	 * 			Using QueueUnderflowException. Got answers about questions relating the use
	 * 			of if statements in some of the methods. Tested all methods.
	 * 
	 * 		September 1, 2015
	 * 			Added some comments.
	 *  
	 */
	
	private Node front;
	private Node rear;
	
	public LinkedListQueue()
	{
		// Creates a new LinkedListQueue and sets the front and rear Nodes to null
		this.front = null;
		this.rear = null;
	}
	

	public void removeAll()
	{
		// Removes all the Nodes from the linked list
		// Checks if the linked list is empty
		if(this.isEmpty()) 
		{
			// Throw unchecked exception
			throw new QueueUnderflowException();
		}
		
		this.front = null;
		this.rear = null;
	}
	
	public boolean isEmpty() 
	{
		// Returns if the linked list is empty
		return (this.front == null && this.rear == null);
	}
	
	public Object peek() 
	{
		// Returns, but does not remove, the front of the linked list
		// Checks if the linked list is empty
			if(this.isEmpty()) 
			{
				// Throw unchecked exception
				throw new QueueUnderflowException();
			}
			
		// Returns the data from the front Node
		return this.front.getData(); 
	}
	
	public Object deQueue() 
	{
		// Removes and returns the front of the linked list
		Node front;
		Node next;
		
		// Checks if the linked list is empty
		if(this.isEmpty())
		{
			// Throw unchecked exception
			throw new QueueUnderflowException();
		}
		
		// Get the next Node after the front Node
		next = this.front.getNext();
		
		// Get the front node
		front = this.front;
				
		// Set the next Node for the front as null since it is being removed
		front.setNext(null);
				
		// The new front is the next Node, which was the next one from the front
		this.front = next;
		
		// If the new front is null, it means that we removed the only element of 
		// the queue
		if(this.front == null) 
		{
			// Set the rear as null since there are no elements in the linked list
			this.rear = null;
		}
		
		// Return the data from the former front Node
		return front.getData(); 
	}
	
	public void enQueue(Object data)
	{
		// Appends the data object to the end of the linked list
		Node current;
		Node previous;

		if(data == null) 
		{
			// Throw exception
			throw new IllegalArgumentException("The data parameter (data) is null");
		}
		
		// The previous Node is the rear
		previous = this.rear;
		
		// The current Node is the one that we just created. The data of the node is
		// the passed parameter and there is no next Node.
		current = new Node(data, null);
			
		// Check if there is a previous
		if(!(previous == null))
		{
			// The next variable of the previous rear is the current node
			previous.setNext(current);			
		}
		else
		{
			// If no previous, then we are adding the first element in the linked list. 
			// Therefore, we set the current Node as the front of the list 
			this.front = current;
		}
		
		// The new rear of the list is the current node
		this.rear = current;
	}
	

	public void enQueue(LinkedListQueue queue)
	{
		// Appends the passed queue to the end of this linked list
		Node thisRear;
		Node queueFront;

		// Check if the passed queue is null
		if(queue == null) 
		{
			// Throw exception
			throw new IllegalArgumentException("The queue parameter (queue) is null");
		}
				
		//  If the passed queue is empty, ignore it
		if(!queue.isEmpty())
		{
			// Get the rear of this list
			thisRear = this.rear;
			
			// Get the front of the queue
			queueFront = queue.front;
			
			// Check if this queue is empty
			if(this.isEmpty()) 
			{
				// If empty, the front of the passed queue is the front of this queue
				this.front = queueFront;
			}
			else
			{
				// Make the next variable of this rear point to the front of the passed queue
				thisRear.setNext(queueFront);			
			}
			
			// The new rear of the list is the rear of the passed queue
			this.rear = queue.rear;
			
			// Remove all the Nodes from the passed queue that are now in this queue
			queue.removeAll();
		}	
		
	}
	

	private class Node 
	{
		/**
		 * 
		 * Miguel Velez
		 * February 19, 2015
		 * 
		 * This class creates a node of the linked list, sets its data, and the next node.
		 * 
		 * Class variables:
		 * 		data
		 * 			an Object that contains data.
		 * 
		 * 		next
		 * 			the next Node in the LinkedList. If no next node, it is null. 		
		 * 
		 * Constructors:
		 * 		Node(Object data, Node next) 
		 * 			creates a new Node object set to the values of the parameters.
		 * 
		 * Methods:
		 * 
		 * 		public Object getData() 
		 * 			returns the data object.
		 * 
		 * 		public Node getNext()
		 * 			returns the next node in the linked list.
		 * 
		 * 		public void setNext(Node next)
		 * 			sets the next node that follows this node.
		 *    
		 * Modification History:
		 * 		September 12, 2015
		 * 			Original tested and working version
		 *  
		 */
	
		private Object 	data; 
		private Node	next;
		
		public Node(Object data, Node next) 
		{
			// Creates a new Node object set the values of the parameters.	
			this.data = data;
			this.next = next;
		}
		
		public Object getData() 
		{
			// Returns the data object
			return this.data;
		}
		
		public Node getNext()
		{
			// returns the next node
			return this.next;
		}
		
		public void setNext(Node next) 
		{
			// Sets the next node
			this.next = next;
		}
		
	}

}
