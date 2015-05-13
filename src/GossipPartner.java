import java.net.*;
public class GossipPartner
{
	/*
	Courtney Karppi
	CISC 370 - GossipProtocol
	May 5, 2015

	Class Variables:
		gossipPartnerAddress
			an InetSocketAddress that contains the partners address
		isAlive
			A boolean that is true for alive, false for not
		queue
			An OutgoingPacketQueue that contains the queue
	Constructors:
		GossipPartner(InetSocketAddress gossipPartnerAddress, OutgoingPacketQueue queue)
			Initalizes both of the instance variables
	Methods:
		public boolean equals(Object other)
			Checks to see if this hashcode is equal to the paramters hashcode
		public InetSocketAddress getGossipPartnerAddress()
			Returns the instance variable getGossipPartnerAddress
		public int hashCode()
			Returns a hashcode value of the object
		public boolean isAlive()
			Returns the instance variable isAlive
		public void send(UDPMessage message)
			Creates the datagramSocket and datagramPacket sets the IP address of the machine to
			which this datagram is being sent. Then the messages datagramPacket is put in queue.
	*/
	private InetSocketAddress 	gossipPartnerAddress;
	private boolean 			isAlive;
	private OutgoingPacketQueue queue;

	public GossipPartner(InetSocketAddress gossipPartnerAddress, OutgoingPacketQueue queue)
	{
		this.gossipPartnerAddress 	= gossipPartnerAddress;
		this.queue 					= queue;
	}//constructor

	public boolean equals(Object other)
	{
		//Checks to see if this hashcode is equal to the paramters hashcode
		return this.hashCode() == ((GossipPartner)other).hashCode();
	}//equals

	public InetSocketAddress getGossipPartnerAddress()
	{
		//Returns the instance variable getGossipPartnerAddress
		return this.gossipPartnerAddress;
	}//getGossipPartnerAddress

	public int hashCode()
	{
		//Returns a hashcode value of the object
		return this.gossipPartnerAddress.toString().hashCode();
	}//hashcode

	public boolean isAlive()
	{
		//Returns the instance variable isAlive
		return this.isAlive;
	}//isAlive

	public void send(UDPMessage message)
	{
		//Creates the datagramSocket and datagramPacket sets the IP address of the machine to
		//which this datagram is being sent. Then the messages datagramPacket is put in queue.
		//DatagramSocket			socket;
		DatagramPacket			dp;
		try
		{
			//socket = (this.getGossipPartnerAddress());
			dp = message.getDatagramPacket();
			dp.setAddress(this.getGossipPartnerAddress().getAddress());
			this.queue.enQueue(dp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}//send

}//gossipPartner
