import java.net.*;
/**
 * This takes the packets from the outside world and transforms them into
 * UDPMessages according to our protocol. This is where the InvalidPacket
 * exception will be used.
 */
public class UDPMessage
{
	private ID id1;
	private ID id2;
	private TimeToLive timeToLive;
	private byte[] message;

	public UDPMessage(ID id1, ID id2, TimeToLive timeToLive, String message)
	{
		this(id1, id2, timeToLive, message.getBytes());
	}//UDPMessage

	public UDPMessage(ID id1, ID id2, TimeToLive timeToLive, byte[] message)
	{
		this.id1 = id1;
		this.id2 = id2;
		this.timeToLive = timeToLive;
		this.message = message;
	}//UDPMessage

	public UDPMessage(DatagramPacket datagramPacket)
	{
		byte[]		data;
		int			messageLength;
		byte[]		message;

		data = datagramPacket.getData();


		if(data.length>0 && data.length<513)
		{
			id1 = new ID(datagramPacket, 0);
			id2 = new ID(datagramPacket, ID.getLengthInBytes());
			timeToLive = new TimeToLive();

			messageLength = datagramPacket.getLength() - ID.getLengthInBytes()*2 - timeToLive.getBytes().length;

			message = new byte[messageLength];
			System.arraycopy(data, ID.getLengthInBytes()*2 + timeToLive.getBytes().length, message, 0, messageLength);
		}
		else
		{
			throw new InvalidPacketFormatException(datagramPacket, "Error: Data is the wrong size");
		}
	}//UDPMessage

	public DatagramPacket getDatagramPacket()
	{
		return getDatagramPacket(this.message);
	}//getDatagramPacket

	public DatagramPacket getDatagramPacket(String payload)
	{
		return getDatagramPacket(payload.getBytes());
	}//getDatagramPacket

	public DatagramPacket getDatagramPacket(byte[] payload)
	{
		ID			id1;
		ID 			id2;
		TimeToLive	ttl;
		int			size;
		byte[]		buffer;

		id1 = ID.idFactory();
		id2 = ID.idFactory();
		ttl = new TimeToLive(75);

		size = (ID.getLengthInBytes() *2) + TimeToLive.getLengthInBytes() + payload.length;
		buffer = new byte[size];

		System.arraycopy(id1.getBytes(), 0, buffer, 0, ID.getLengthInBytes());
		System.arraycopy(id2.getBytes(), 0, buffer, 0, ID.getLengthInBytes());
		System.arraycopy(ttl.getBytes(), 0, buffer, ID.getLengthInBytes()*2, TimeToLive.getLengthInBytes());
		System.arraycopy(payload, 0, buffer, ID.getLengthInBytes()*2 + TimeToLive.getLengthInBytes(), payload.length);
		//System.arraycopy(payload, 0, buffer, ID.getLengthInBytes()*2 + TimeToLive.getLengthInBytes(), payload.length + payload.length);

		return new DatagramPacket(buffer, size);

	}//getDatagramPacket

	public ID getID1()
	{
		return this.id1;
	}//ID1

	public ID getID2()
	{
		return this.id2;
	}//ID2

	public TimeToLive getTimeToLive()
	{
		return this.timeToLive;
	}//getTimeToLive

	public byte[] getMessage()
	{
		return this.message;
	}//getMessage

	public static int getMaximumPacketSizeInBytes()
	{
		return 512-(ID.getLengthInBytes()*2)-TimeToLive.getLengthInBytes();
	}//getMaximumPacketSizeInBytes

	public static int getMinimumPacketSizeInBytes()
	{
		return (ID.getLengthInBytes()*2)-TimeToLive.getLengthInBytes();
	}//getMinimumPacketSizeInByte

}//class

