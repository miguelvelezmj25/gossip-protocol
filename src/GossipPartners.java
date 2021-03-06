import java.util.*;
public class GossipPartners
{
	/*
	Courtney Karppi
	CISC 370 - GossipProtocol
	May 5, 2015

	Class Variables:
		instance
			An instnace of GossipPartners
		gossipPartners
			a collection of all the partners
	Constructors:
		GossipPartners()
			creates a new vector for the instance variable
	Methods:
		public static GossipPartners getInstance()
			If the instance is null it sets a new GossipPartners
		public static GossipPartners newInstance()
			Calls getInstance
		public void addPartner(GossipPartner gossipPartner)
			Adds a partner to the collection
		public void send(UDPMessage udpMessage)
			Sends the message to all the gossipPartners if the TTL is not zero
	*/
	private static GossipPartners 		instance;
	private Collection<GossipPartner> 	gossipPartners;

	private GossipPartners()
	{
		gossipPartners = new ArrayList<GossipPartner>();
	}//constructor

	public static GossipPartners getInstance()
	{
		//If the instance is null it sets a new GossipPartners
		if(instance == null)
		{
			instance = new GossipPartners();
		}
		return instance;
	}//getInstance

	public static GossipPartners newInstance()
	{
		//Calls getInstance
		return getInstance();
	}//newInstance

	public void addPartner(GossipPartner gossipPartner)
	{
		//Adds a partner to the collection
		gossipPartners.add(gossipPartner);
	}//addPartner

	public void send(UDPMessage udpMessage)
	{
		//Sends the message to all the gossipPartners if the TTL is not zero
		UDPMessage	message;

		if(udpMessage.getTimeToLive().get() <= 1)
		{
			//doesn't send the message
		}
		else
		{
			Iterator<GossipPartner> ie;

			ie 		= gossipPartners.iterator();
			message = new UDPMessage(udpMessage.getID1(),udpMessage.getID2(),new TimeToLive(udpMessage.getTimeToLive().get()-1), udpMessage.getMessage());

			while(ie.hasNext())
			{
				ie.next().send(message);
			}
		}
	}//send

}//class
