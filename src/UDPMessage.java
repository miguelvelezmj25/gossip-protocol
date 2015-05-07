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
		ID 			id1;
		ID			id2;
		TimeToLive	timeToLive;
		byte[]		message;
		byte[]		ttl;
		int			count;

		data = datagramPacket.getData();
		ttl = new byte[TimeToLive.getLengthInBytes()];

		count = 0;
		if(data.length>0 && data.length<456)
		{
			id1 = new ID(datagramPacket, 0);
			id2 = new ID(datagramPacket, ID.getLengthInBytes());
			for(int i= id1.getLengthInBytes()*2; i<((id1.getLengthInBytes()*2)+ttl.length); i++)
			{
				ttl[count] = data[i];
				count = count+1;
			}
			timeToLive = new TimeToLive(ttl);
			messageLength = datagramPacket.getLength()-id1.getLengthInBytes()*2 + ttl.length;
			message = new byte[messageLength];
			System.arraycopy(data, id1.getLengthInBytes()*2 + ttl.length, message, 0, messageLength);
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
		String 		myData;
		TimeToLive	ttl;
		int			size;
		byte[]		buffer;

		id1 = ID.idFactory();
		id2 = ID.idFactory();
		ttl = new TimeToLive(75);
		myData = "Hi";

		size = (ID.getLengthInBytes() *2) + timeToLive.getLengthInBytes() + myData.getBytes().length;
		buffer = new byte[size];

		System.arraycopy(id1.getBytes(), 0, buffer, 0, ID.getLengthInBytes());
		System.arraycopy(id2.getBytes(), 0, buffer, 0, ID.getLengthInBytes());
		System.arraycopy(ttl.getBytes(), 0, buffer, ID.getLengthInBytes()*2, TimeToLive.getLengthInBytes());
		System.arraycopy(myData.getBytes(), 0, buffer, ID.getLengthInBytes()*2 + TimeToLive.getLengthInBytes(), myData.getBytes().length);
		System.arraycopy(payload, 0, buffer, ID.getLengthInBytes()*2 + TimeToLive.getLengthInBytes(), myData.getBytes().length + myData.getBytes().length);

		return new DatagramPacket(buffer, size);

	}//getDatagramPacket

	public ID getID1(){return this.id1;}
	public ID getID2(){return this.id2;}
	public TimeToLive timeToLive(){return this.timeToLive;}
	public byte[] getMessage(){return this.message;}

}//class
