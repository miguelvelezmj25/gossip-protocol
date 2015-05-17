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
	 * 		private static ArrayList<CommunityResources> communityResources
	 * 			all the resources that the UI requested and got a response to
	 * 		
	 * 		private static int communityResourcesId
	 * 			the total number of resources that the ui requested and got a response to
	 * 
	 * 		private ArrayList<ID> responses
	 * 			the specific responses that the ui requested 
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
	 *		public static ID getResource(int communityResourceID) 
	 *			get the resource that the UI got based on the community
	 *			resource ID
	 * 
	 *      
	 * Modification History:
	 *  
	 * 		April 29, 2015
	 * 			Original version.
	 * 
	 * 		May 7, 2015
	 * 			Rename class.
	 * 
	 * 		May 10, 2015
	 * 			Added the community resources object.
	 * 
	 * 	 	May 11, 2015
	 * 			Was saving the wrong ID in the responses.
	 * 
 	 * 	 	May 14, 2015
	 * 			Saving the find response
	 */
	
	private static ArrayList<CommunityResources> communityResources = new ArrayList<RequestFromUIControllerToFindResources.CommunityResources>();
	private static int							 communityResourcesId = 0;
	
	private ArrayList<ID> responses; 
	
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
			
			// Get the description of the resource
			byte[] resourceDescription = new byte[udpMessage.getMessage().length];
			resourceDescription = udpMessage.getMessage();
			
			// Get the text part
			byte[] resourceText = new byte[resourceDescription.length - ID.getLengthInBytes()];			
			System.arraycopy(resourceDescription, ID.getLengthInBytes(), resourceText, 0, resourceText.length);
			
			// Get the response to a string
			String response = new String(resourceText);
						
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
									") Length (" + resource.getLength() + ")");	
	
			// Add this resource to our history
			RequestFromUIControllerToFindResources.communityResources.add(communityResourcesId, resource);
			RequestFromUIControllerToFindResources.communityResourcesId++;
			
			// Create a new resource from a find request and save it in the resourceManager
			PeerResource peerResource = new PeerResource(udpMessage.getID1(), response, delimiter);
						
			PeerResourceManager.getInstance().addResource(peerResource);
			
		}
		
	}
	

	private class CommunityResources 
	{
		/**
		 * 
		 * Miguel Velez
		 * May 10, 2015
		 * 
		 * This class is a resource from the community that the UI searched for
		 * 
		 * Class variables:
		 * 
		 * 
		 * Constructors:
		 * 		
		 * 		public CommunityResources(ID resourceID, String delimiter, String mimeType, String length, String description)
		 * 			create a resource from the community
		 * 
		 * Methods:
		 * 
		 * 		public String getDelimiter
		 * 			gets the delimiter
		 * 
		 * 		public String getMimeType
		 * 			gets the mime type
		 * 
		 * 		public String getLength
		 * 			gets the length()
		 * 
		 * 		public String getDescription
		 * 			gets the description
		 * 
		 * 		public ID getResourceID
		 * 			gets the resource ID
		 * 
		 *        
		 * Modification History:
		 *   
		 * 		May 10, 2015
		 * 			Original version.
		 */
		
		private ID		resourceID;
		private String 	delimiter;
		private String 	mimeType;
		private String 	length;
		private String 	description;
		
		public CommunityResources(ID resourceID, String delimiter, String mimeType, String length, String description)
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

		public String getLength()
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
