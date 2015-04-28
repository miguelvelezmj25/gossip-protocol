import java.util.HashMap;

public class RequestManager {
	
	private static RequestManager 	instance;
	
	private HashMap<ID, Request>	requestsDirectory;
	
	public RequestManager() {
		RequestManager.instance = this;
		this.requestsDirectory = new HashMap<ID, Request>();
	}
	
	/**
	 * Return the instance of the class
	 * 
	 * @return
	 */
	public static RequestManager getInstance() {
		return RequestManager.instance;
	}
	
	/**
	 * Return a new instance of the class
	 * 
	 * @return
	 */
	public static RequestManager newInstance() {
		return RequestManager.getInstance();
	}
	
	/**
	 * Get a request from the directory
	 * 
	 * @param id
	 * @return
	 */
	public Request getRequest(ID id) {
		return this.requestsDirectory.get(id);
	}
	
	/**
	 * Insert a request in the directory
	 * 
	 * @param request
	 */
	public void insertRequest(Request request) {
		this.requestsDirectory.put(request.getID(), request);
	}

}
