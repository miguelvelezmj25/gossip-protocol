import java.util.*;
public class GossipPartners
{
	/*
	Courtney Karppi
	CISC 370 - GossipProtocol
	May 5, 2015
	*/

	private static GossipPartners instance;
	private Collection<GossipPartner> gossipPartners;

	private GossipPartners()
	{
		gossipPartners = new Vector<GossipPartner>();
	}//constructor

	public static GossipPartners getInstance()
	{
		if(instance == null)
		{
			instance = new GossipPartners();
		}
		return instance;
	}//getInstance

	public static GossipPartners newInstance()
	{
		return getInstance();
	}//newInstance

	public void addPartner(GossipPartner gossipPartner)
	{
		gossipPartners.add(gossipPartner);
	}//addPartner

}//class
