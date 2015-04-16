import java.security.SecureRandom;
import java.util.Arrays;
import java.util.LinkedList;


public class ID {
      
	private static int idLengthInBytes = 16;
	private static LinkedList<ID> idQueue = new LinkedList<ID>();
	private static int maxQueueLength = 50;
	private static int queueLength  = 0;
	private static SecureRandom secureRandom = new SecureRandom();
	private static ID zeroID = ID.createZeroID();
	
	private byte[] id;
	
	private ID()
	{
		this.id = new byte[ID.getIDLength()];
		ID.secureRandom.nextBytes(this.id);
	}
	
	/**
	 * Constructs a new ID by using the input byte array as the ID.
	 * @param byteArray
	 */
	
	public ID (byte[] byteArray)
	{
		if(byteArray.length < idLengthInBytes)
		{
			throw new IllegalArgumentException("Byte array has to be exactly "+ID.idLengthInBytes+ " bytes long. Shame.");
		}
		this.id = byteArray.clone();
	}
	
	/**
	 * Checks if the queue length is equal to 0. If so, returns an ID from the queue. If not, constructs a new ID.
	 * @return 
	 * 		A return ID.
	 */
	public synchronized static ID idFactory()
	{
		ID returnID;
		if(ID.queueLength==0)
		{
			returnID = new ID();
		}else{
			returnID = (ID)ID.getQueue().remove();
			ID.queueLength = ID.queueLength - 1;
		}
		
		return returnID;
	}
	
	/**
	 * Creates a ID consisting entirely of zeros. 
	 * @return
	 * 		An id consisting entirely of zeros. 
	 */
	private static ID createZeroID()
	{
		byte[] zeroByteArray = new byte[idLengthInBytes];
		for(int i = 0; i< idLengthInBytes;i++)
		{
			zeroByteArray[i] = 0;
		}
		
		return new ID(zeroByteArray);
	}
	
	/**
	 * Fills the queue to the amount specified by the maxQueueLength static parameter.
	 */
	public static synchronized void generateID()
	{
		while(ID.queueLength<ID.maxQueueLength)
		{
			idQueue.add(new ID());
			ID.queueLength++;
		}//Add new IDs to the queue until we are at maximum.
	}
	
	/**
	 * Basic getter.
	 * @return
	 * 		The length of the byte array contained in the ID objects.
	 */
	public static synchronized int getIDLength() {
		return idLengthInBytes;
	}

	/**
	 * Changes the variable that regulates the length of ID byte arrays.
	 * @param idLengthInBytes
	 */
	public static synchronized void setIDLength(int idLengthInBytes) 
	{
		ID.idLengthInBytes = idLengthInBytes;
	}

	/**
	 * Basic getter.
	 * @return
	 * 		Returns the max queue length as determined by the maxQueueLength static variable.
	 */
	public static synchronized int getMaxQueueLength() 
	{
		return maxQueueLength;
	}
	
	/**
	 * Basic setter.
	 * @param maxQueueLength
	 */
	public static synchronized void setMaxQueueLength(int maxQueueLength) 
	{
		ID.maxQueueLength = maxQueueLength;
	}

	/**
	 * Basic getter for the queue containing IDs.
	 * @return
	 * 		A SynchronizedLinkedListQueue containing a list of IDs.
	 */
	public static synchronized LinkedList getQueue() 
	{
		return idQueue;
	}

	/**
	 * Basic getter.
	 * @return
	 * 		The current queue length. 
	 */
	public static int getQueueLength() {
		return queueLength;
	}
	
	/**
	 * Basic getter.
	 * @return
	 * 		The zero id. 
	 */
	public static ID getZeroID()
	{
		return ID.zeroID;
	}
	
	/**
	 * Returns the internal byte representation of this ID.
	 * @return
	 * 		The byte array ID. 
	 */
	public byte[] getBytes()
	{
		return this.id.clone();
	}
	

	@Override
	/**
	 * Checks to see if two ID objects have the same internal bytes. 
	 */
	public boolean equals(Object other)
	{
		byte[] otherBytes;
		byte[] thisBytes;
		
		thisBytes = this.id;
		
		try
		{
			otherBytes = ((ID)other).getBytes();
		}catch(Exception e)
		{
			throw new IllegalArgumentException("Compared object is not of class ID");
		}
		
		return Arrays.equals(otherBytes,thisBytes);
		
	}
	
	@Override
	/**
	 * Returns the hashCode of the toString method.
	 */
	public int hashCode()
	{
		return this.toString().hashCode();
	}
	
	/**
	 * Determines if the current ID is the zero ID.
	 * @return
	 * 		Boolean. True if this object is 0, false otherwise.
	 */
	public boolean isZero()
	{
		return this.equals(ID.getZeroID());
	}
	
	@Override
	/**
	 * Converts each byte to hex, and returns the string consisting of consecutive hex characters.
	 */
	public String toString()
	{ 
		String toReturn;
		
		toReturn = "";
		for(byte b: this.getBytes())
		{
			toReturn = toReturn + Integer.toHexString(b);
		}
		
		return toReturn;
	}
	
}
