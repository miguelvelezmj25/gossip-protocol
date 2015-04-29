
public abstract class RequestFromPeer 
{
	private UDPMessage udpMessage;
	
	public RequestFromPeer(UDPMessage message)
	{
		udpMessage = message;
	}
	
	public UDPMessage getUDPMessage()
	{
		return this.udpMessage;
	}
	
	public abstract void run();
}
