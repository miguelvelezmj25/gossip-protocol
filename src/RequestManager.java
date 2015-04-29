import java.util.HashMap;

public class RequestManager 
{
	/**
	 * 
	 * Miguel Velez
	 * April 28, 2015
	 * 
	 * This singleton class manages the requests.
	 * 
	 * Class variables:
	 * 
	 * 		private static RequestManager instance
	 * 			an instance to this class
	 * 
	 * 		private HashMap<ID, Request> requestsDirectory
	 * 			the directory of the requests
	 * 
	 * Constructors:
	 * 		
	 * 		public RequestManager() 
	 * 			construct a request manager
	 * 
	 * Methods:
	 * 
	 * 		public static RequestManager getInstance() 
	 * 			return the instance of the class
	 * 
	 * 		public static RequestManager newInstance() 
	 * 			return a new instance of the class
	 * 
	 * 		public Request getRequest(ID id) 
	 * 			get a request from the directory
	 * 
	 * 		public void insertRequest(Request request) 
	 * 			insert a request in the directory
	 * 
	 *     
	 * Modification History:
	 *  
	 * 		April 28, 2015
	 * 			Original version.
	 *  
	 */
	
	private static RequestManager instance;
	
	private HashMap<ID, Request> requestsDirectory;
	
	public RequestManager() 
	{
		// Construct a request manager
		RequestManager.instance = this;
		this.requestsDirectory = new HashMap<ID, Request>();
	}
	
	public static RequestManager getInstance() 
	{
		// Return the instance of the class
		return RequestManager.instance;
	}

	public static RequestManager newInstance() 
	{
		// Return a new instance of the class
		return RequestManager.getInstance();
	}
	
	public Request getRequest(ID id) 
	{
		// Get a request from the directory
		// Check if null
		if(id ==  null) {
			throw new IllegalArgumentException("The id you provided is null");
		}
		
		// TODO what other checks?
		
		// Returns null if not found
		return this.requestsDirectory.get(id);
	}
	
	public void insertRequest(Request request) 
	{
		// Insert a request in the directory
		// Check if null
		if(request ==  null) {
			throw new IllegalArgumentException("The request you provided is null");
		}
		
		// TODO what other checks?
		
		// Insert in the hash map
		this.requestsDirectory.put(request.getID(), request);
	}

}
