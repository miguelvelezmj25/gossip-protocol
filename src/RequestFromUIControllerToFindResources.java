import java.net.InetSocketAddress;
import java.util.ArrayList;

//TODO comment

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
	
	private static ArrayList<CommunityResources> communityResources = new ArrayList<RequestFromUIControllerToFindResources.CommunityResources>();
	private static int							 communityResourcesId = 0;
	
	private ArrayList<ID> responses; // TODO this is not shared among the class
	
	public RequestFromUIControllerToFindResources(ID id, InetSocketAddress uiControllerAccess, OutgoingPacketQueue outgoingPacketQueue) 
	{
		// Create a request to find resources from peers
		// Call the super constructor
		super(id, uiControllerAccess, outgoingPacketQueue);
		
		this.responses = new ArrayList<ID>();
	}
	
	public static ID getResource(int communityResourceID) {
		return RequestFromUIControllerToFindResources.communityResources.get(communityResourceID).getResourceID();
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
			
			System.out.println(udpMessage.getID1());
			
			// Get the response to a string
			String response = new String(udpMessage.getMessage());
			
			// Get the delimiter
			char delimiter = response.charAt(0);
			
			// Split the response
			String[] responseFields = response.split(""+ delimiter);
									
			// Create a communityResources object
			CommunityResources resource = new CommunityResources(udpMessage.getID1(), delimiter + "", responseFields[1], responseFields[2], responseFields[3]);
			
			// Print to the UI the resource that we got
			System.out.println("Resource(" + RequestFromUIControllerToFindResources.communityResourcesId + 
									") Description(" + resource.getDescription() + 
									") MimeType(" + resource.getMimeType() +
									") Length (" + resource.getlength() + ")");	
			
			// Add this resource to our history
			RequestFromUIControllerToFindResources.communityResources.add(communityResourcesId, resource);
			RequestFromUIControllerToFindResources.communityResourcesId++;
			
		}
		
		// TODO save in a collection and print 6: ID, mime type, length
	}
	
	
	// TODO Comment
	
	private class CommunityResources 
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
		
		private ID		resourceID;
		private String 	delimiter;
		private String 	mimeType;
		private String 	length;
		private String 	description;
		
		private CommunityResources(ID resourceID, String delimiter, String mimeType, String length, String description)
		{
			this.resourceID = resourceID; // TODO do i have to clone this?
			this.delimiter = delimiter;
			this.mimeType = mimeType;
			this.length = length;
			this.description = description.trim();
		}
		
		@SuppressWarnings("unused")
		public String getDelimiter()
		{
			return this.delimiter;
		}
		
		public String getMimeType()
		{
			return this.mimeType;
		}

		public String getlength()
		{
			return this.length;
		}

		public String getDescription()
		{
			return this.description;
		}
		
		public ID getResourceID() 
		{
			return this.resourceID;
		}
		
	}

}
