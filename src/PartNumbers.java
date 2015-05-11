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
