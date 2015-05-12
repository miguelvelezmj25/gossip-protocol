
/**
 * The PartsNumber class is used by a Peer that has requested a resource from another peer.
 * Since it knows the resource size in bytes and the number of bytes per part 
 * (except for the last part), it is able to calculate the number of parts it 
 * will need to get from that other peer in order to have the complete resource (file).
 * 
 * The Peer is going to request a part periodically, but, since it is IP, it doesn't know 
 * if that request will arrive of if that part will be received. So, it keeps track of the 
 * parts it has received using the values in range thing.
 * 
 * It continues to get missing part numbers from the values in range and keeps updating 
 * the values in range when it receives parts until all parts have been received.


I am still confused about the getBytes method because you only know the total number of parts missing and not the individual part numbers
 * 
 * 
 * */


public class PartNumbers
{
	/*
	Courtney Karppi
	CISC 370 - GossipProtocol
	May 11, 2015

	Class Variables:
		numberOfParts
			An int containing the total number of parts
		numberOfPartsMissing
			An int containg the total number of missing parts
	Constructors:
		PartNumbers(int numberOfParts)
			sets the instance variable numberOfParts
	Methods:
		public int get()
			Returns the instance variable numberOfParts
		public static byte[] getBytes(int size)

		public static int getLengthInBytes()
			Returns 4
		public int numberOfMissingParts()
			Returns the instance variable numberOfPartsMissing
	*/
	private int numberOfParts;
	private int numberOfPartsMissing;

	public PartNumbers(int numberOfParts)
	{
		if(numberOfParts>0)
		{
			this.numberOfParts = numberOfParts;
		}
	}//constructor

	public int get()
	{
		//Returns the instance variable numberOfParts
		return this.numberOfParts;
	}//get

//CONFUSED ON THIS
	public static byte[] getBytes(int size)
	{
		//
		byte[]	array;

		array = new byte[size];

		return array;
	}//getBytes()

	public static int getLengthInBytes()
	{
		//Returns 4
		return 4;
	}//getLengthInBytes

	public int numberOfMissingParts()
	{
		//Returns the instance variable numberOfPartsMissing
		return this.numberOfPartsMissing;
	}//numberOfMissingParts

}//class
