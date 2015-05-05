import java.net.*;
public class GossipPartner extends GossipPartners
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
	}

	public boolean equals(Object other)
	{

	}//equals

	public InetSocketAddress getGossipPartnerAddress()
	{
		return this.gossipPartnerAddress;
	}//getGossipPartnerAddress

	public int hashcode()
	{

	}//hashcode

	public boolean isAlive()
	{
		return this.isAlive();
	}//isAlive

	public void send(UDPMessage message)
	{

	}//send

}//gossipPartner
