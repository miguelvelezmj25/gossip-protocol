import java.net.*;
/**
 * This takes the packets from the outside world and transforms them into
 * UDPMessages according to our protocol. This is where the InvalidPacket
 * exception will be used.
 */
public class UDPMessage
{
	/*
 		Courtney Karppi
 		CISC 370 
 		May 11, 2015

 		Class Variables:
 			id1
				An ID which contains the request ID
 			id2
				An ID which contains the random ID
 			ttl
				a TimeToLive object which stores the time the message stays alive
 			message
 				a byte[] that contains the text message
 		Constructors:
 			UDPMessage(ID id1, ID id2, TimeToLive timeToLive, String message)
				Calls the other constructor while converting the string to a byte[]
			UDPMessage(ID id1, ID id2, TimeToLive timeToLive, byte[] message)
				This constructor sets the parameters it is passed
			UDPMessage(DatagramPacket datagramPacket):
				The constructor takes a datagramPacket and gets the data from it. If the datagramPacket includes
				the four parts of the message then it determines the size of the byte[] for the message.
				If it doesn't then it calles the InvalidPacketFormatException class.
 		Methods:
			public DatagramPacket getDatagramPacket()
				Calls the other getDatagramPacket() with the parameter this.getMessage
			public DatagramPacket getDatagramPacket(String payload)
				Calls the getDatagramPacket and passes it the payload in the form of a byte[]
			public DatagramPacket getDatagramPacket(byte[] payload)
				Creates the buffer and then creates the datagramPacket
			public ID getID1()
				Returns the instance variable id1
			public ID getID2()
				Returns the instance variable id2
			public TimeToLive getTimeToLive()
				Returns the instance variable timeToLive
			public byte[] getMessage()
				Returns the instance variable message
			public static int getMaximumPacketSizeInBytes()
				Returns the maximum size of the packet by subtracting the length of both IDs and TimeToLive from 512
			public static int getMinimumPacketSizeInBytes()
				Returns the minimum size of the packet. This is the length of both IDs plus the TimeToLive
 	*/
	private ID 			id1;
	private ID 			id2;
	private TimeToLive 	timeToLive;
	private byte[] 		message;

	public UDPMessage(ID id1, ID id2, TimeToLive timeToLive, String message)
	{
		//Calls the other constructor while converting the string to a byte[]
		this(id1, id2, timeToLive, message.getBytes());
	}//UDPMessage

	public UDPMessage(ID id1, ID id2, TimeToLive timeToLive, byte[] message)
	{
		//This constructor sets the parameters it is passed
		this.id1 		= id1;
		this.id2 		= id2;
		this.timeToLive = timeToLive;
		this.message 	= message;
	}//UDPMessage

	public UDPMessage(DatagramPacket datagramPacket)
	{
		//The constructor takes a datagramPacket and gets the data from it. If the datagramPacket includes
		//the four parts of the message then it determines the size of the byte[] for the message.
		//If it does not then it calles the InvalidPacketFormatException class.
		byte[]		data;
		byte[]		ttl;
		int			messageLength;

		data = datagramPacket.getData();

		if(data.length>0 && data.length<=512)
		{
			id1 = new ID(datagramPacket, 0);
			id2 = new ID(datagramPacket, ID.getLengthInBytes());
			ttl = new byte[TimeToLive.getLengthInBytes()];

			System.arraycopy(data,ID.getLengthInBytes()*2,ttl,0,ttl.length);

			timeToLive 		= new TimeToLive(ttl);
			messageLength 	= datagramPacket.getLength() - ID.getLengthInBytes()*2 - timeToLive.getBytes().length;
			message 		= new byte[messageLength];

			System.arraycopy(data, ID.getLengthInBytes()*2 + timeToLive.getBytes().length, message, 0, messageLength);
		}
		else
		{//gives an exception using the InvalidPacketFormatException class
			throw new InvalidPacketFormatException(datagramPacket, "Error: Data is the wrong size");
		}
	}//UDPMessage

	public DatagramPacket getDatagramPacket()
	{
		//Calls the other getDatagramPacket with the parameter this.message
		return getDatagramPacket(this.message);
	}//getDatagramPacket

	public DatagramPacket getDatagramPacket(String payload)
	{
		//Calls the getDatagramPacket and passes it the payload in the form of a byte[]
		return getDatagramPacket(payload.getBytes());
	}//getDatagramPacket

	public DatagramPacket getDatagramPacket(byte[] payload)
	{
		//Creates the buffer and then creates the datagramPacket
		int			size;
		byte[]		buffer;

		size	= (ID.getLengthInBytes() *2) + TimeToLive.getLengthInBytes() + payload.length;
		buffer 	= new byte[size];

		System.arraycopy(id1.getBytes(), 0, buffer, 0, ID.getLengthInBytes());
		System.arraycopy(id2.getBytes(), 0, buffer, ID.getLengthInBytes(), ID.getLengthInBytes());
		System.arraycopy(timeToLive.getBytes(), 0, buffer, ID.getLengthInBytes()*2, TimeToLive.getLengthInBytes());
		System.arraycopy(payload, 0, buffer, ID.getLengthInBytes()*2 + TimeToLive.getLengthInBytes(), payload.length);

		return new DatagramPacket(buffer, size);
	}//getDatagramPacket

	public ID getID1()
	{
		//Returns the instance variable id1
		return this.id1;
	}//ID1

	public ID getID2()
	{
		//Returns the instance variable id2
		return this.id2;
	}//ID2

	public TimeToLive getTimeToLive()
	{
		//Returns the instance variable timeToLive
		return this.timeToLive;
	}//getTimeToLive

	public byte[] getMessage()
	{
		//Returns the instance variable message
		return this.message;
	}//getMessage

	public static int getMaximumPacketSizeInBytes()
	{
		//Returns the maximum size of the packet by subtracting the length of both IDs and TimeToLive from 512
		return 512-(ID.getLengthInBytes()*2)-TimeToLive.getLengthInBytes();
	}//getMaximumPacketSizeInBytes

	public static int getMinimumPacketSizeInBytes()
	{
		//Returns the minimum size of the packet. This is the length of both IDs plus the TimeToLive
		return (ID.getLengthInBytes()*2)-TimeToLive.getLengthInBytes();
	}//getMinimumPacketSizeInByte

}//class
