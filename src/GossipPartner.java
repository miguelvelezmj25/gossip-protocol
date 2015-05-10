import java.net.*;
public class GossipPartner
{
	/*
	Courtney Karppi
	CISC 370 - GossipProtocol
	May 5, 2015
	*/

	private InetSocketAddress gossipPartnerAddress;
	private boolean isAlive;
	private OutgoingPacketQueue queue;

	public GossipPartner(InetSocketAddress gossipPartnerAddress, OutgoingPacketQueue queue)
	{
		this.gossipPartnerAddress = gossipPartnerAddress;
		this.queue = queue;
	}//constructor

	public boolean equals(Object other)
	{
		return this.hashCode() == ((GossipPartner)other).hashCode();
	}//equals

	public InetSocketAddress getGossipPartnerAddress()
	{
		return this.gossipPartnerAddress;
	}//getGossipPartnerAddress

	public int hashCode()
	{
		return this.gossipPartnerAddress.toString().hashCode();
	}//hashcode

	public boolean isAlive()
	{
		return this.isAlive;
	}//isAlive

	public void send(UDPMessage message)
	{;
		DatagramSocket			socket;
		DatagramPacket			dp;
		try
		{
			socket = new DatagramSocket(this.getGossipPartnerAddress());
			dp = message.getDatagramPacket();
			dp.setAddress(socket.getInetAddress());
			this.queue.enQueue(message.getDatagramPacket());

		}
		catch(SocketException se)
		{
			System.out.println(se.getMessage());
		}
	}//send

}//gossipPartner
