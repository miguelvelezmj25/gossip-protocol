public class FindRequestFromPeer extends RequestFromPeer
{
	public FindRequestFromPeer(UDPMessage message)
	{
		super(message);
	}
	
	public void run()
	{
		String mess;
		ID id2;
		ID id1;
		Resource[] matches;
		UDPMessage udpMessage;
		String response;
		TimeToLive ttl;
		
		udpMessage = super.getUDPMessage();
		

		mess = new String(udpMessage.getMessage());
		
		matches = ResourceManager.getInstance().getResourcesThatMatch(mess);
		
		if(matches.length > 0)
		{
			id2 = udpMessage.getID1();
			response = "";
			for(int i = 0; i < matches.length; i = i + 1)
			{
				id1 = matches[i].getID();
				response = "," + matches[i].getMimeType() + "," + matches[i].getSizeInBytes() + "," + matches[i].getDescription();
				ttl = new TimeToLive(70);
				udpMessage = new UDPMessage(id1, id2, ttl, response);
				//TODO: actually send the response
			}
			
			
			
			
		}
	}
}
