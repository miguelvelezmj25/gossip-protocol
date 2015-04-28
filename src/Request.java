
public abstract class Request {
	
	private ID requestID;
	
	/**
	 * Create a request
	 * 
	 * @param id
	 */
	public Request(ID id) {
		this.requestID = id;
	}
	
	/**
	 * Get the id from the request
	 * 
	 * @return
	 */
	public ID getID() {
		return this.requestID;
	}
	
	/**
	 * Update the request
	 * 
	 * @param udpMessage
	 */
	public abstract void updateRequest(UDPMessage udpMessage);

}
