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
		ID randID;
		Resource[] matches;
		UDPMessage udpMessage;
		String response;
		TimeToLive ttl;
		byte[] responseArray;
		
		udpMessage = super.getUDPMessage();
		

		mess = new String(udpMessage.getMessage());
		
		matches = ResourceManager.getInstance().getResourcesThatMatch(mess);
		
		if(matches.length > 0)
		{
			id2 = udpMessage.getID1();
			response = "";
			for(int i = 0; i < matches.length; i = i + 1)
			{
				randID = new ID(new byte[16]);
				id1 = matches[i].getID();
				response = "," + matches[i].getMimeType() + "," + matches[i].getSizeInBytes() + "," + matches[i].getDescription();
				ttl = new TimeToLive(70);
				responseArray = new byte[16 + response.getBytes().length];
				System.arraycopy(randID.getBytes(),0,responseArray,0,randID.getBytes().length);
				System.arraycopy(response.getBytes(),0,responseArray,16,response.getBytes().length);
				udpMessage = new UDPMessage(id1, id2, ttl, responseArray);
				//TODO: actually send the response
			}
			
			
			
			
		}
	}
}
