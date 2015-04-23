import java.net.DatagramPacket;
import java.security.SecureRandom;
//import java.util.Arrays;
//import java.util.LinkedList;


public class ID {
	private static int idLengthInBytes = 16;
	private static  LinkedListQueue idQueue = new LinkedListQueue();
	private static int maxQueueLength = 50;
	private static int queueLength  = 0;
	private static SecureRandom secureRandom = new SecureRandom();
	private static ID zeroID = ID.createZeroID();
	
	private byte[] id;
	
	private ID()
	{
		this.id = new byte[ID.getLengthInBytes()];
		ID.secureRandom.nextBytes(this.id);
	}
	
	/**
	 * Constructs a new ID by using the input byte array as the ID.
	 * @param byteArray
	 */
	
	public ID (byte[] byteArray)
	{
		if(byteArray == null) {
			throw new IllegalArgumentException("The byte array that you provided is null");
		}
		
		if(byteArray.length != idLengthInBytes)
		{
			throw new IllegalArgumentException("Byte array has to be exactly "+ID.idLengthInBytes+ " bytes long. You"
					+ "privoded (" + byteArray.length + ")");
		}
		
		this.id = byteArray.clone();
	}
	
	public ID(DatagramPacket packet, int startingByte) {
		// TODO error check 
		this.id = new byte[ID.idLengthInBytes];
		System.arraycopy(packet, 0, this.id, startingByte, ID.idLengthInBytes);
	}
	
//	public ID(String hexString) {
//		if(hexString == null) {
//			throw new IllegalArgumentException("The hex string you provided is null");
//		}
//		
//	}
	
	/**
	 * Checks if the queue length is equal to 0. If so, returns an ID from the queue. If not, constructs a new ID.
	 * @return 
	 * 		A return ID.
	 */
	public synchronized static ID idFactory()
	{
		// TODO How is this used
		ID returnID;

		if(ID.queueLength==0)
		{
			returnID = new ID();
		}else{
			returnID = (ID) ID.getQueue().deQueue();
			
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
		// TODO fill the queue?
		while(ID.queueLength<ID.maxQueueLength)
		{
			ID.idQueue.enQueue(new ID());
			
			ID.queueLength++;
		}//Add new IDs to the queue until we are at maximum.
	}
	
	public String getAsHex() {
		String hex = "0x";
		String tmp = "";
		
		for(byte singleByte : this.id) {
			tmp = Integer.toHexString(singleByte);
			
			if(tmp.length() < 2) {
				tmp = "0" + tmp;
			}
			
			hex = hex + tmp;
		}
		
		return hex;
	}
	
	/**
	 * Return the length in bytes
	 * @return
	 */
	public synchronized static int getLengthInBytes() {
		return ID.idLengthInBytes;
	}
	
	/**
	 * Basic getter for the queue containing IDs.
	 * @return
	 * 		A SynchronizedLinkedListQueue containing a list of IDs.
	 */
	public static synchronized LinkedListQueue getQueue() 
	{
		return ID.idQueue;
	}
	
	/**
	 * Basic getter.
	 * @return
	 * 		Returns the max queue length as determined by the maxQueueLength static variable.
	 */
	public static synchronized int getMaxQueueLength() 
	{
		return ID.maxQueueLength;
	}
	
	/**
	 * Basic getter.
	 * @return
	 * 		The current queue length. 
	 */
	public synchronized static int getQueueLength() {
		return ID.queueLength;
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
	 * Set the length in bytes
	 * @param lengthInBytes
	 */
	public static synchronized void setLengthInBytes(int lengthInBytes) {
		if(lengthInBytes < 1) {
			throw new IllegalArgumentException("The length in bytes that you provided (" + lengthInBytes 
					+") is less than 1.");
		}
		
		ID.idLengthInBytes = lengthInBytes;
	}
	
	/**
	 * Set the max queue length.
	 * @param maxQueueLength
	 */
	public static synchronized void setMaxQueueLength(int maxQueueLength) 
	{
		if(maxQueueLength < 0) {
			throw new IllegalArgumentException("The max length queue that you provided (" + maxQueueLength 
					+") is less than 0.");
		}
		
		ID.maxQueueLength = maxQueueLength;
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
	
	/**
	 * Checks to see if two ID objects have the same internal bytes. 
	 */
	@Override
	public boolean equals(Object other)
	{
		// TODO is this how it should be implemented
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
	
	/**
	 * Returns the hashCode of the toString method.
	 */
	@Override
	public int hashCode()
	{
		// TODO is this right
		return this.id.toString().hashCode();
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

	/**
	 * Converts each byte to hex, and returns the string consisting of consecutive hex characters.
	 */
	@Override
	public String toString()
	{ 
		return this.getAsHex();
	}

	public boolean isRequestID() {
		// TODO
		return false;
	}
	
	public boolean isResourceID() {
		// TODO
		return false;
	}
	
}
