
public abstract class Request 
{
	/**
	 * 
	 * Miguel Velez
	 * April 28, 2015
	 * 
	 * This abstract class represents a request.
	 * 
	 * Class variables:
	 * 
	 * 		private ID requestID
	 * 			the request id
	 * 
	 * Constructors:
	 * 		
	 * 		public Request(ID id) 
	 * 			create a request
	 * 
	 * Methods:
	 * 	
	 * 		public ID getID() 
	 * 			get the id from the request
	 * 	
	 * 		public abstract void updateRequest(UDPMessage udpMessage);
	 *			update the request
	 * 
	 *      
	 * Modification History:
	 *  
	 * 		April 28, 2015
	 * 			Original version.
	 *  
	 */
		
	private ID requestID;
	
	public Request(ID id) 
	{
		// Check if null
		if(id == null) 
		{
			throw new IllegalArgumentException("The request id cannot be null");
		}
		
		// Create a request
		this.requestID = id;
	}

	public ID getID() 
	{
		// Get the id from the request
		return this.requestID;
	}
	
	// Update the request
	public abstract void updateRequest(UDPMessage udpMessage);

}
