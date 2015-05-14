import java.net.InetSocketAddress;
import java.util.ArrayList;

public class RequestFromUIControllerToGetaResource extends RequestFromUIController 
{
	/**
	 * 
	 * Miguel Velez
	 * April 29, 2015
	 * 
	 * This class is a request from ui controller to get a resource
	 * 
	 * Class variables:
	 * 		
	 * 		private ArrayList<ID> responses; 
	 * 
	 * Constructors:
	 * 		
	 * 		public RequestFromUIControllerToGetaResource(ID id)  
	 * 			create a request to find resources from peers
	 * 
	 * Methods:
	 * 	 	
	 * 		public void updateRequest(UDPMessage udpMessage);
	 *			update the request
	 * 
	 *      
	 * Modification History:
	 *  
	 * 		May 7, 2015
	 * 			Original version.
	 *  
	 */
	
	private ArrayList<ID> responses; 
	
	public RequestFromUIControllerToGetaResource(ID id, InetSocketAddress uiController, OutgoingPacketQueue outgoingPacket) 
	{
		// Create a request to find resources from peers
		// Call the super constructor
		super(id, uiController, outgoingPacket);
	}

	@Override
	public void updateRequest(UDPMessage udpMessage) 
	{
		// Send the datagram packet to the UI controller
		// Check if null
		if(udpMessage == null) 
		{
			throw new IllegalArgumentException("The UDP message that you provided is null");
		}
		
		// TODO get the datagramPacket and send it to the UIController. Done in the peer controller
	}

}
