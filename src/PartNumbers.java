public class PartNumbers
{
	/*
	Courtney Karppi
	CISC 370 - GossipProtocol
	May 5, 2015
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
		//returns the instance variable numberOfParts
		return this.numberOfParts;
	}//get

//need to finish
	public static boolean[] getBytes(int size)
	{
		boolean[]	array;
		array = new boolean[size];

		for (int i=0; i<size; i++)
		{
			array[i]= false;
		}

		return array;

	}//getBytes()

	public static int getLengthInBytes()
	{
		//returns size of this.getBytes()
		return 4;
	}//getLengthInBytes

	public int numberOfMissingParts()
	{
		//returns the total number of missing parts
		return this.numberOfPartsMissing;
	}//numberOfMissingParts

}//class
