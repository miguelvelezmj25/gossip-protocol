import java.util.ArrayList;


public class RequestToFindResources extends Request 
{
	/**
	 * 
	 * Miguel Velez
	 * April 29, 2015
	 * 
	 * This class is a request to find resources from other peers
	 * 
	 * Class variables:
	 * 
	 * 		private ArrayList<ID> responses;
	 * 			Where responses are stored
	 * 
	 * Constructors:
	 * 		
	 * 		public RequestToFindResources(ID id)  
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
	 */
	
	private ArrayList<ID> responses;
	
	public RequestToFindResources(ID id) 
	{
		// Create a request to find resources from peers
		// Call the super constructor
		super(id);
		
		// TODO do we need to include something else in this variable?
		this.responses = new ArrayList<ID>();
	}

	@Override
	public void updateRequest(UDPMessage udpMessage) 
	{
	// TODO	what needs to be here?
		// Check if null
		if(udpMessage == null) 
		{
			throw new IllegalArgumentException("The UDP message that you provided is null");
		}
	}

}
