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
		ID identification1 = super.getUDPMessage().getID2();
		ID identification2 = super.getUDPMessage().getID1();
		ID randID;
		ResourceManager rm = ResourceManager.getInstance();
		Resource rs = rm.getResourceFromID(identification1);
		File file = rs.getLocation();
		UDPMessage response;
		int numberOfParts;
		int length;
		int loc;
		//PartNumbers pN;
		byte[] sentArray;
		byte[] fileArray;
		
		
		try
		{
			fileArray = Files.readAllBytes(file.toPath());
			numberOfParts = (int)Math.ceil(fileArray.length / 456);
			
			//pN = new PartNumbers(numberOfParts);
			
			for( int i = 0; i <= numberOfParts; i = i + 1)
			{
				randID = new ID(new byte[16]);
				length = randID.getBytes().length;
				//length = length + pN.getBytes().length;
				length = length + Math.min(456, fileArray.length - (i * 456));
				
				sentArray = new byte[length];
				
				System.arraycopy(randID.getBytes(),0,sentArray,0,randID.getBytes().length);
				loc = randID.getBytes().length;
				//System.arraycopy(rN.getBytes(),0,sentArray,16,rN.getBytes().length);
				//loc = loc + rN.getBytes().length;
				System.arraycopy(fileArray, (i * 456), sentArray, loc, Math.min(456, fileArray.length - (i * 456)));
				response = new UDPMessage(identification1,identification2, new TimeToLive(70),sentArray);
				//TODO: send response
			}
		}
		catch(IOException ioe)
		{
			System.out.println(ioe.getMessage());
		}
		
	}
}
