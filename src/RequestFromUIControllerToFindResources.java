import java.net.InetSocketAddress;
import java.util.ArrayList;


public class RequestFromUIControllerToFindResources extends RequestFromUIController 
{
	/**
	 * 
	 * Miguel Velez
	 * April 29, 2015
	 * 
	 * This class is a request from the ui controller to find resources
	 * 
	 * Class variables:
	 * 
	 * 
	 * Constructors:
	 * 		
	 * 		public RequestFromUIControllerToFindResources(ID id)  
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
	 * 		April 29, 2015
	 * 			Original version.
	 * 
	 * 		May 7, 2015
	 * 			Rename class.
	 */
	
	private ArrayList<ID> responses; // TODO this is not shared among the class
	
	public RequestFromUIControllerToFindResources(ID id, InetSocketAddress uiControllerAccess, OutgoingPacketQueue outgoingPacketQueue) 
	{
		// Create a request to find resources from peers
		// Call the super constructor
		super(id, uiControllerAccess, outgoingPacketQueue);
		
		this.responses = new ArrayList<ID>();
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
		
		
		// Check if we have received this response
		if(!responses.contains(udpMessage.getID1()))
		{
			// Add to our responses
			responses.add(udpMessage.getID1());
			
			// Print the ID and the message
			System.out.println(udpMessage.getID1());
			System.out.println(new String(udpMessage.getMessage()));
		}
		
		// TODO save in a collection and print 6: ID, mime type, length
	}
	
	
	// TODO might need a private class to save resources

}
