import java.net.InetSocketAddress;

public abstract class RequestFromUIController 
{
	/**
	 * 
	 * Miguel Velez
	 * April 28, 2015
	 * 
	 * This abstract class represents a request from the ui controller.
	 * 
	 * Class variables:
	 * 
	 * 		private ID requestID
	 * 			the request id
	 * 
	 * 		private OutgoingPacketQueue outgoingPacketQueue; 
	 * 			the outgoing packet queue to send packets to the UI
	 * 
	 * 		private InetSocketAddress uiControllerAddress;
	 * 			the UI controller address which should be local host and the port number
	 * 			that it listen to 
	 * 
	 * Constructors:
	 * 		
	 * 		public RequestFromUIController(ID id) 
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
	 *		public OutgoingPacketQueue getQueue() 
	 *			return the outgoing packet queue
	 *
	 *		public InetSocketAddress getUIControllerAddress()
	 *			return the ui controller address
	 * 
	 *      
	 * Modification History:
	 *  
	 * 		April 28, 2015
	 * 			Original version.
	 * 
	 * 		May 7, 2015
	 * 			Renamed the class. Added new functionality.
	 *  
	 */
		
	private ID 					requestID;
	private OutgoingPacketQueue outgoingPacketQueue; 
	private InetSocketAddress	uiControllerAddress; 
	
	public RequestFromUIController(ID id, InetSocketAddress uiController, OutgoingPacketQueue outgoingPacketQueue) 
	{
		// Check if null
		if(id == null) 
		{
			throw new IllegalArgumentException("The request id cannot be null");
		}
		
		this.requestID = id;
		this.outgoingPacketQueue = outgoingPacketQueue;
		this.uiControllerAddress = uiController;
	}

	public ID getID() 
	{
		// Get the id from the request
		return this.requestID;
	}
	
	// Update the request
	public abstract void updateRequest(UDPMessage udpMessage);
	
	public OutgoingPacketQueue getQueue() 
	{
		// Return the outgoing packet queue
		return this.outgoingPacketQueue;
	}
	
	public InetSocketAddress getUIControllerAddress()
	{
		// Return the ui controller address
		return this.uiControllerAddress;
	}

}
