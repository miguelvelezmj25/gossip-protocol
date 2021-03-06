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
	 * 	 	May 7, 2015
	 * 			Rename classes.
	 * 
	 * 	 	May 10, 2015
	 * 			Inserted requests in the directory.
	 * 
	 * 	 	May 12, 2015
	 * 			Added check if there was a duplicate request.
	 *  
	 */
	
	private static RequestManager instance;
	
	private HashMap<ID, RequestFromUIController> requestsDirectory;
	
	public RequestManager() 
	{
		// Construct a request manager
		RequestManager.instance = this;
		this.requestsDirectory = new HashMap<ID, RequestFromUIController>();
	}
	
	public static RequestManager getInstance() 
	{
		if(RequestManager.instance == null)
		{
			RequestManager.instance = new RequestManager();
		}
		
		// Return the instance of the class
		return RequestManager.instance;
	}

	public static RequestManager newInstance() 
	{
		// Return a new instance of the class
		return RequestManager.getInstance();
	}
	
	public RequestFromUIController getRequest(ID id) 
	{
		// Get a request from the directory
		// Check if null
		if(id == null) {
			throw new IllegalArgumentException("The id you provided is null");
		}
		
		// Returns null if not found
		return this.requestsDirectory.get(id);
	}
	
	public void insertRequest(RequestFromUIController request) 
	{
		// Insert a request in the directory
		// Check if null
		if(request ==  null) {
			throw new IllegalArgumentException("The request you provided is null");
		}
		
		// Check if the hash map already has this request ID, which should not happen
		if(!this.requestsDirectory.containsKey(request.getID())) {
			// Insert in the hash map
			this.requestsDirectory.put(request.getID(), request);
		}
	}

}
