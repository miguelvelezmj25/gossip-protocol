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

	*/
	private int 		numberOfParts;
//	private int 		numberOfPartsMissing;
// everything in the boolean[] is set to false whenever the object is created

	private boolean[]	partsReceived;

	public PartNumbers(int numberOfParts)
	{
		if(numberOfParts>0)
		{
			this.numberOfParts = numberOfParts;
		}
		partsReceived = new boolean[numberOfParts];
		for(int i=0; i<numberOfParts; i++)
		{
			partsReceived[i] = false;
		}
	}//constructor

	public int get()
	{
		//Returns the instance variable numberOfParts
		return this.numberOfParts;
	}//get


//after each part number is recieved, you then call this to change it to true(received)
	public boolean[] getBytes(int numberReceived)
	{
		//
		this.partsReceived[numberReceived] = true;
		return this.partsReceived;
	}//getBytes()

	public int getLengthInBytes()
	{
		//Returns 4
		return 4;
	}//getLengthInBytes

	public int numberOfMissingParts()
	{
		int missingParts;
		missingParts = 0;
		for(int i=0; i<get(); i++)
		{
			if(this.partsReceived[i]==false)
			{
				missingParts = missingParts+1;
			}
		}
		return missingParts;
	}//numberOfMissingParts

}//class
