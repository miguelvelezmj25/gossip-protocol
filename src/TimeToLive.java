/**
 * This class determines the time to live of messages that are sent around.
 */
public class TimeToLive {

	private int timeToLive;
	
	/**
	 * Constructor that sets the time to live.
	 * 
	 * @param timeToLive
	 */
	public TimeToLive(int timeToLive) {
		this.timeToLive = timeToLive;
	}
	
	/**
	 * Constructor that takes the time to live from the message that is located 32
	 * bytes down the message
	 * @param byteArray
	 */
	public TimeToLive(byte[] byteArray) {
		int timeToLive  = 0;
		
		for(int i = 0; i < TimeToLive.getLengthInBytes(); i++) {
			timeToLive = timeToLive | (byteArray[i] << 
					((TimeToLive.getLengthInBytes() - 1 - i) * 8));
		}
		
		this.timeToLive = timeToLive;
	}
	
	/**
	 * Returns the number of bytes that time to live uses.
	 * @return
	 */
	public static int getLengthInBytes() {
		return 4;
	}
	
	/**
	 * Returns the time to live
	 * 
	 * @return
	 */
	public int get() {
		return this.timeToLive;
	}
	
	/**
	 * Sets the time to live
	 * 
	 * @param timeToLive
	 */
	public void set(int timeToLive) {
		this.timeToLive = timeToLive;
	}
	
	/**
	 * Returns the time to live as a byte array
	 * 
	 * @return
	 */
	public byte[] toByteArray() {
		
		byte[] result = new byte[4];
		
		for(int i = 0; i < TimeToLive.getLengthInBytes(); i++) {
			result[i] = (byte) (this.timeToLive >> ((TimeToLive.getLengthInBytes() - 1 - i) * 8));
		}
		
		return result;
	}
	
	/**
	 * Returns a string representation of the time to live
	 */
	public String toString() {
		return ("" + this.timeToLive);
	}
	
}
