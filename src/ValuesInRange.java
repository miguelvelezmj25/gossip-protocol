public class ValuesInRange
{
	/*
	
	Courtney Karppi
	CISC 370
	February 12, 2015

	This class gives general processing of bitmaps.

	Class Variables:
		bitMap
			An int array that contains 32 bits in each element.
		maximumValueInRange
			A number that is the maximum in the range.
		minimumValueInRange
			A number that is the minimum in the range.
	Constructors:
	 	ValuesInRange(int minimumValueInRange, int maximumValueInRange)
	 		Creates an object set to the value of the parameters

	 Methods:
	 	public int getMinimumValueInRange()
	 		returns the class variable minimumValueInRange
	 	public int getMaximumValueInRange()
	 		returns the class variable maximumValueInRange
	 	public void setFor(int value)
			sets the old bitmap to the new bit number it is passed
	 	public boolean contains(int value)
	 		returns true or false on whether or not the value
	 		is contained in the bitMap array
	 	public void clearFor(int value)
			sets the old bitmap to a new bitmap with the value = 0
	 	private void checkValue(int value)
	 		throws an exception if the value is not within the
	 		range of the max and min variables
	 	private int getBitNumberFor(int value)
			returns an int value of what bit the parameter
			value is in
	 	private int getElementNumberFor(int value)
			returns an int value of what element the bitmap
			of the parameter value is in
		private int getNumberOfBitsInEachMap()
			returns an int of how many bits are in an element

	Modification History
		Original
			February 12, 2015
	*/
	private int[]	bitMap;
	private int 	maximumValueInRange;
	private int 	minimumValueInRange;

	public ValuesInRange(int minimumValueInRange, int maximumValueInRange)
	{
		if(maximumValueInRange < minimumValueInRange)
		{
			throw new IllegalArgumentException("The maximum value parameter (" + maximumValueInRange + ") is less than the minimum value parameter (" + minimumValueInRange +")");
		}
		this.maximumValueInRange=maximumValueInRange;
		this.minimumValueInRange=minimumValueInRange;
		this.bitMap = new int[((maximumValueInRange - minimumValueInRange +1)/this.getNumberOfBitsInEachMap())+1];
	}//ValuesInRange

	public int getMinimumValueInRange(){return this.minimumValueInRange;}//returns the class variable minimumValueInRange
	public int getMaximumValueInRange(){return this.maximumValueInRange;}//returns the class variable maximumValueInRange

	public void setFor(int value)
	{//sets the old bitmap to the new bit number it is passed
		int element;
		int bitNumber;

		checkValue(value);

		element = getElementNumberFor(value);
		bitNumber = getBitNumberFor(value);

		this.bitMap[element]= (this.bitMap[element]|(1<<bitNumber));
	}//setFor

	public boolean contains(int value)
	{//returns true or false on whether or not the value is contained in the bitMap array
		int 	element;
		int 	bitNumber;

		checkValue(value);

		bitNumber = getBitNumberFor(value);
		element = getElementNumberFor(value);

		return (1 << bitNumber) == (this.bitMap[element] & (1 << bitNumber));
	}//contains

	public void clearFor(int value)
	{//sets the old bitmap to a new bitmap with the value = 0
		int element;
		int bitNumber;

		checkValue(value);

		element = getElementNumberFor(value);
		bitNumber = getBitNumberFor(value);

		this.bitMap[element]= (bitMap[element] &~ (1<<bitNumber));
	}//clearFor

	private void checkValue(int value)
	{//returns an int value of what bit the parameter value is in
		if (value<this.getMinimumValueInRange())
		{
			throw new IllegalArgumentException("Parameter value (" + value + ") is less than the minimum value in the range (" + this.getMinimumValueInRange()+ ")");
		}
		if(value>this.getMaximumValueInRange())
		{
			throw new IllegalArgumentException("Parameter value (" + value + ") is less than the minimum value in the range (" + this.getMaximumValueInRange()+ ")");
		}
	}//checkValue

	private int getBitNumberFor(int value)
	{//returns an int value of what element the bitmap of the parameter value is in
		return (value-this.getMinimumValueInRange()) % getNumberOfBitsInEachMap();
	}//getBitNumberFor

	private int getElementNumberFor(int value)
	{//returns an int value of what element the bitmap of the parameter value is in
		return (value-this.getMinimumValueInRange())/this.getNumberOfBitsInEachMap();
	}//getElementNumberFor
	private int getNumberOfBitsInEachMap(){return 32;} //returns an int of how many bits are in an element
}//class
