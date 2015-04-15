import java.security.SecureRandom;

public class ID {
	/**
	 * 
	 * Miguel Velez
	 * March 7, 2015
	 * 
	 * This class TODO
	 * 
	 * Class variables:
	 * 		static int idLengthInBytes
	 * 			The length in bytes of the ID.
	 * 		
	 * 		static LinkedListQueue idQueue
	 * 			The ID queue.
	 * 
	 * 		static int maxQueueLength
	 * 			The maximum length of the queue.
	 * 
	 * 		static int queueLength
	 * 			The current queue length.
	 * 
	 *  	static SecureRandom secureRandom
	 *  		TODO
	 *  
	 *   	static ID zeroID
	 *   		TODO
	 *   	
	 *   	byte[] id
	 *   		TODO
	 *  
	 * Constructors:
	 * 		private ID()
	 * 			TODO
	 * 	
	 * 		public ID(byte[] byteArray)
	 * 			TODO 
	 * 
	 * Methods:
	 *		public static ID idFactory() 
	 *			TODO
	 *
	 *		private static ID createZeroID()
	 *			TODO
	 *
	 *		public static void generateID() 
	 *			TODO
	 *
	 *		public static int getIDLength()
	 *			TODO
	 *
	 *		private static LinkedListQueue getQueue()
	 *			Return the id queue.
	 *
	 *		public static int getMaxQueueLength()
	 *			Return the max queue length.
	 *
	 * 		public static int getQueueLength()
	 * 			Return the queue length.
	 * 
	 * 		public static ID getZeroID()
	 * 			Return the zero ID.
	 * 
	 * 		public static void setIDLength(int lengthInByte)
	 * 			Sets the id length.
	 * 		
	 * 		public static void setMaxQueueLength(int length)
	 * 			Sets the max queue length.
	 * 
	 * 		public byte[] getBytes()
	 * 			TODO
	 * 
	 *  	public boolean equals(Object other)
	 *  		TODO
	 *  
	 *  	public int hashCode()
	 *  		TODO
	 *  
	 *   	public String toString()
	 *   		TODO
	 *       
	 * Modification History:
	 * 		March 7, 2015
	 * 			Original version
	 *  
	 */
	
	private static int 				idLengthInBytes = 16;
	private static LinkedListQueue 	idQueue = new LinkedListQueue();
	private static int				maxQueueLength = 0; // TODO length?
	private static int				queueLength = 0; // TODO min
	private static SecureRandom		secureRandom = new SecureRandom(); // TODO what is this?
	private static ID				zeroID; // TODO how to initialize
	
	private byte[] id;
	
	private ID()
	{
		//TODO
	}
	
	public ID(byte[] byteArray) 
	{
		// Check if byte array is null
		if(byteArray == null) {
			// TODO exception or warning message?
		}
		
		// Check if the byte array length is the same as the length in bytes of the id
		if (byteArray.length != ID.idLengthInBytes) {
			// TODO exception or warning message?
		}
		
		// TODO
		this.id = byteArray.clone();
		
	}
	
	public static ID idFactory() 
	{
		// TODO
		return null;	
	}
	
	private static ID createZeroID()
	{
		// TODO
		return null;
	}
	
	public static void generateID() 
	{
		// TODO
		if(ID.queueLength < ID.maxQueueLength) 
		{
			// TODO
		}
	}
	
	public static int getIDLength()
	{
		// TODO
		return ID.idLengthInBytes;
	}
	
	private static LinkedListQueue getQueue()
	{
		// Return the id queue
		return ID.idQueue;
	}
	
	public static int getMaxQueueLength() 
	{
		// Return the max queue length
		return ID.maxQueueLength;
	}
	
	public static int getQueueLength()
	{
		// Return the queue length
		return ID.queueLength;
	}
	
	public static ID getZeroID()
	{
		// Return the zero ID
		return ID.zeroID;
	}
	
	public static void setIDLength(int lengthInBytes)
	{
		// TODO check if >= 1
		// Sets the id length
		ID.idLengthInBytes = lengthInBytes;
	}
	
	public static void setMaxQueueLength(int length)
	{
		// TODO will use removeAll
		// TODO check parameter
		// Sets the max queue length
		ID.maxQueueLength = length;
	}
	
	public byte[] getBytes() 
	{
		// TODO
		return this.id.clone();
	}
	
	@Override
	public boolean equals(Object other)
	{
		boolean result = true;
		
		if(other == null)
		{
			result = false;
		}
		
		if(!this.getClass().equals(this.getClass()))
		{
			result = false;
		}
		
		if(this.id != ((ID) other).id)
		{
			result = false;
		}
		
		// TODO
		return result;
	}

	@Override
	public int hashCode() 
	{
		// TODO the id?
		// A = B and A != C, but A and C can have the same hash code.
		// A and B MUST have the same hashCode.
		return 0;
	}
	
	@Override
	public String toString()
	{
		// TODO return in hex
		return null;
	}
		
}
