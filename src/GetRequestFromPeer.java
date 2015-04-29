import java.io.*;
import java.nio.file.Files;


public class GetRequestFromPeer extends RequestFromPeer
{
	public GetRequestFromPeer(UDPMessage message)
	{
		super(message);
	}
	
	public void run()
	{
		ID identification = super.getUDPMessage().getID2();
		ResourceManager rm = ResourceManager.getInstance();
		Resource rs = rm.getResourceFromID(identification);
		File file = rs.getLocation();
		
		
		
	}
}
