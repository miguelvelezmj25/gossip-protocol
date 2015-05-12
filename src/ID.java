import java.net.DatagramPacket;
import java.security.SecureRandom;

//TODO comment

public class ID 
{
	/**
	 * 
	 * Miguel Velez
	 * April 14, 2015
	 * 
	 * This class creates an ID to use in the packets
	 * 
	 * Class variables:
	 * 		private static int idLengthInBytes;
	 * 			the length of the id.
	 * 
	 * 		private static LinkedListQueue idQueue
	 * 			the queue of ids
	 * 
	 *		private static int maxQueueLength
	 *			the max queue length
	 *
	 *		private static int queueLength
	 *			the current queue length
	 *
	 * 		private static SecureRandom secureRandom
	 * 			get secure random bytes
	 * 
	 * 		private static ID zeroID
	 * 	 		the id of zeros
	 * 
	 * 		private byte[] id;
	 * 			the actual id
	 *  	
	 * Constructors:
	 *		private ID()
	 *			populate the id with new bytes
	 * 
	 * 		public ID (byte[] byteArray)
	 * 			constructs a new ID by using the input byte array as the ID.
	 * 
	 * 		public ID(DatagramPacket packet, int startingByte)
	 * 			constructs an id from a packet
	 * 
	 * Methods:
	 * 
	 * 		public synchronized static ID idFactory()
	 * 			checks if the queue length is equal to 0. If so, returns an ID from 
	 * 			the queue. If not, constructs a new ID.
	 * 
	 * 		private static ID createZeroID()
	 * 			creates a ID consisting entirely of zeros. 
	 * 
	 * 		public static synchronized void generateID()
	 * 			generates an ID if the queue has enough space for a new one.
	 * 
	 * 		public String getAsHex() 
	 * 			get the id as a hex string
	 * 
	 * 		public synchronized static int getLengthInBytes()
	 * 			return the length in bytes
	 * 
	 * 		public static synchronized LinkedListQueue getQueue() 
	 * 			basic getter for the queue containing IDs.
	 * 
	 * 		public static synchronized int getMaxQueueLength() 
	 * 			return the maximum length of the queue
	 * 
	 * 		public synchronized static int getQueueLength() 
	 * 			return the queue length
	 * 
	 * 		public static ID getZeroID()
	 * 			return the zero ID
	 * 
	 * 		public static synchronized void setLengthInBytes(int lengthInBytes) 
	 * 			set the length in bytes
	 * 
	 * 		public static synchronized void setMaxQueueLength(int maxQueueLength) 
	 * 			set the max length of the queue
	 * 
	 * 		public byte[] getBytes()
	 * 			returns the internal byte representation of this ID.
	 * 
	 * 		public boolean equals(Object other)
	 * 			checks to see if two ID objects have the same internal bytes. 
	 * 
	 * 		public int hashCode()
	 * 			returns the hashCode of the toString method.
	 * 
	 * 		public boolean isZero()
	 * 			determines if the current ID is the zero ID.
	 * 		
	 * 		public String toString()
	 * 			converts each byte to hex, and returns the string consisting of 
	 * 			consecutive hex characters.
	 *      
	 * Modification History:
	 * 		April 14, 2015
	 * 			Original version
	 * 
	 * 		April 28, 2015
	 * 			Added some comments.
	 * 
	 * 		May 10, 2015
	 * 			Fixed hashcode
	 *  
	 */

	private static int 				idLengthInBytes = 16;
	private static LinkedListQueue 	idQueue = new LinkedListQueue();
	private static int 				maxQueueLength = 50;
	private static int 				queueLength  = 0;
	private static SecureRandom 	secureRandom = new SecureRandom();
	private static ID 				zeroID = ID.createZeroID();
	
	private byte[] id;
	
	private ID()
	{
		// Populate the id with new bytes		
		this.id = new byte[ID.getLengthInBytes()];
		
		ID.secureRandom.nextBytes(this.id);
	}
	
	public ID (byte[] byteArray)
	{
		// Constructs a new ID by using the input byte array as the ID.
		// Check if null
		if(byteArray == null) 
		{
			throw new IllegalArgumentException("The byte array that you provided is null");
		}
		
		// Check if the length is not the same as the length in bytes
		if(byteArray.length != ID.idLengthInBytes)
		{
			throw new IllegalArgumentException("Byte array has to be exactly " + ID.idLengthInBytes+ " bytes long. You"
					+ "privoded (" + byteArray.length + ")");
		}
		
		// Clone the id that we got
		this.id = byteArray.clone();
	}
	
	public ID(DatagramPacket packet, int startingByte) 
	{
		// Constructs an id from a packet
		// Check if packet is null
		if(packet == null) 
		{
			throw new IllegalArgumentException("The packet that you sent is null");
		}
		
		// Check the starting byte
		if(startingByte < 0) 
		{
			throw new IllegalArgumentException("The starting byte that you provided "
					+ "(" + startingByte + ") is less than 0");
		}
		
		if(startingByte > packet.getData().length) 
		{
			throw new IllegalArgumentException("The starting byte that you provided "
					+ "(" + startingByte + ") is greater than the length of the packet that"
							+ "you sent");
		}
		
		this.id = new byte[ID.idLengthInBytes];
		
		// Copy the data from the packet starting at the specified byte to the id
		System.arraycopy(packet.getData(), startingByte, this.id, 0, ID.idLengthInBytes);
	}
		
	public synchronized static ID idFactory()
	{
		// Checks if the queue length is equal to 0. If so, returns an ID from the queue. If not, constructs a new ID.
		// TODO How is this used
		ID returnID;

		// If there are not any ids available, get a new one
		if(ID.queueLength==0)
		{
			returnID = new ID();
		} 
		else
		{
			// Otherwise get one from the queue
			returnID = (ID) ID.getQueue().deQueue();
			
			ID.queueLength = ID.queueLength - 1;
		}
		
		return returnID;
	}
	
	private static ID createZeroID()
	{
		// Creates a ID consisting entirely of zeros. 
		byte[] zeroByteArray = new byte[idLengthInBytes];
		
		// Just to make sue
		for(int i = 0; i< idLengthInBytes;i++)
		{
			zeroByteArray[i] = 0;
		}
		
		return new ID(zeroByteArray);
	}
	
	
	public static synchronized void generateID()
	{
		// Generates an ID if the queue has enough space for a new one.
		if(ID.queueLength<ID.maxQueueLength)
		{
			// Enqueue the new id
			ID.idQueue.enQueue(new ID());
			
			// Increase the length of the queue
			ID.queueLength++;
		} 
	}
	
	public String getAsHex() {
		// Get the id as a hex string
		String hex = "0x";
		String tmp = "";
		
		// For each byte
		for(byte singleByte : this.id) 
		{
			// Get the hex string representation
			tmp = Integer.toHexString(singleByte);
			
			// Add a zero in front of it if the string is only 1 character
			if(tmp.length() < 2) 
			{
				tmp = "0" + tmp;
			}
			
			// Append the string
			hex = hex + tmp;
		}
		
		return hex;
	}
	
	public synchronized static int getLengthInBytes() 
	{
		// Return the length in bytes
		return ID.idLengthInBytes;
	}
	
	public static synchronized LinkedListQueue getQueue() 
	{
		// Basic getter for the queue containing IDs.
		return ID.idQueue;
	}
	
	public static synchronized int getMaxQueueLength() 
	{
		// Return the maximum length of the queue
		return ID.maxQueueLength;
	}
	
	public synchronized static int getQueueLength() 
	{
		// Return the queue length
		return ID.queueLength;
	}

	public static ID getZeroID()
	{
		// Return the zero ID
		return ID.zeroID;
	}
	
	public static synchronized void setLengthInBytes(int lengthInBytes) 
	{
		// Set the length in bytes
		if(lengthInBytes < 1) {
			throw new IllegalArgumentException("The length in bytes that you provided (" + lengthInBytes 
					+") is less than 1.");
		}
		
		ID.idLengthInBytes = lengthInBytes;
	}
	
	public static synchronized void setMaxQueueLength(int maxQueueLength) 
	{
		// Set the max length of the queue
		if(maxQueueLength < 0) {
			throw new IllegalArgumentException("The max length queue that you provided (" + maxQueueLength 
					+") is less than 0.");
		}
		
		ID.maxQueueLength = maxQueueLength;
	}
	
	public byte[] getBytes()
	{
		// Returns the internal byte representation of this ID.
		return this.id.clone();
	}
	
	@Override
	public boolean equals(Object other)
	{
		// Checks to see if two ID objects have the same internal bytes. 
		if(this == other) {
			return true;
		}
		
		if(other == null || (this.getClass() != other.getClass())) {
			return false;
		}

		// Cast and create an ID object 
		ID object = (ID) other;
		
		// Check if lengths are different
		if(this.id.length != object.getBytes().length) {
			return false;
		}
		
		for(int i = 0; i < this.id.length; i++) {
			if(this.id[i] != object.getBytes()[i]) {
				return false;
			}			
		}	
		
		return true;
	}
	
	@Override
	public int hashCode()
	{
		// Returns the hashCode of the toString method.
		return new String(this.id).hashCode();
	}
	
	public boolean isZero()
	{
		// Determines if the current ID is the zero ID.
		return this.equals(ID.getZeroID());
	}

	public String toString()
	{ 
		// Converts each byte to hex, and returns the string consisting of consecutive hex characters.
		return "This id: " + this.getAsHex();
	}
	
}
