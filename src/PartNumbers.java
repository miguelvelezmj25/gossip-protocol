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
	}

	//???????????????????
	public int get()
	{
		return this.numberOfParts;
	}

	public byte[] getBytes()
	{

	}

	public int getLengthInBytes()
	{
		return size(this.numberOfParts);
	}

	public int numberOfMissingParts(){return this.numberOfPartsMissing;}

}//class
