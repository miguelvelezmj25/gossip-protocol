
// TODO comment

public class TimeToLive 
{
	/**
	 * 
	 * Miguel Velez
	 * February 19, 2015
	 * 
	 * This class determines the time to live of messages that are sent around.
	 * 
	 * Class variables:
	 * 		int timeToLive;
	 * 			the time to live of the packets that are sent.
	 *  
	 * Constructors:
	 * 		public TimeToLive(int timeToLive) 
	 * 			constructor that sets the time to live.
	 * 
	 * 		public TimeToLive(byte[] byteArray) 
	 * 			constructor that takes the time to live from the message that is located 32
	 * 			bytes down the message
	 * 
	 * 
	 * Methods:
	 * 
	 * 		public static int getLengthInBytes() 
	 * 			returns the number of bytes that time to live uses.
	 * 		
	 * 		public int get() 
	 * 			returns the time to live
	 * 		
	 * 		public void set(int timeToLive)
	 * 			sets the time to live
	 * 
	 * 		public byte[] getBytes()
	 * 			returns the time to live as a byte array
	 * 	
	 * 		public String toString() 
	 * 			returns a string representation of the time to live
	 *     
	 * Modification History:
	 * 		April 14, 2015
	 * 			Original version
	 * 
	 * 		April 28, 2015
	 * 			Added some comments.
	 *  
	 */

	private int timeToLive;

	public TimeToLive()
	{
		this(10);
	}
	
	public TimeToLive(int timeToLive) 
	{
		// Constructor that sets the time to live.
		if(timeToLive < 1) {
			throw new IllegalArgumentException("The time to live that you provided (" + timeToLive 
					+") is less than 1.");
		}
		
		this.timeToLive = timeToLive;
	}
	
	public TimeToLive(byte[] byteArray) 
	{
		// Constructor that takes the time to live from the message that is located 32
		// bytes down the message
		// Check if null
		if(byteArray == null) {
			throw new IllegalArgumentException("The byte array you provided is null");
		}
		
		// Check length
		if(byteArray.length < 1) {
			throw new IllegalArgumentException("The byte array you provided is empty");
		}
		
		// Check if the byte array is long enough
		if(byteArray.length < TimeToLive.getLengthInBytes()) {
			throw new IllegalArgumentException("The byte array you provided is too short"
					+ " and does not have a time to live.");
		}

		int timeToLive  = 0;
		
		// Get an int from a byte array
		for(int i = 0; i < TimeToLive.getLengthInBytes(); i++) {
			timeToLive = timeToLive | ((byteArray[i] & 0xFF) << (i * 8));
		}
		
		this.timeToLive = timeToLive;
	}
	
	public static int getLengthInBytes() 
	{
		// Returns the number of bytes that time to live uses.
		return 4;
	}
	
	public int get() 
	{
		// Returns the time to live
		return this.timeToLive;
	}
	
	public void set(int timeToLive) 
	{
		// Sets the time to live
		if(timeToLive < 1) {
			throw new IllegalArgumentException("The time to live that you provided (" + timeToLive 
					+") is less than 1.");
		}
		
		this.timeToLive = timeToLive;
	}
	
	public byte[] getBytes() 
	{
		// Returns the time to live as a byte array
		byte[] result = new byte[4];
		
		for(int i = 0; i < TimeToLive.getLengthInBytes(); i++) {
			result[i] = (byte) (this.timeToLive >> (i * 8));
		}
		
		return result;
	}
	
	@Override
	public String toString() 
	{
		// Returns a string representation of the time to live
		return ("The time to live is: " + this.timeToLive);
	}
	
}
