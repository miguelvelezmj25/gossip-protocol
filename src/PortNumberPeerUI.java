
public class PortNumberPeerUI extends PortNumber 
{

	/** 
	 *	Miguel Velez
	 * 	April 16, 2015
	 * 
	 * 	The port number that the peer uses to listen to the UI. 12346
	 * 
	 *  Class variables:
	 * 
	 *  Constructors:
	 * 	
	 * 		public PortNumberPeerUI(int portNumber) 
	 * 			construct a port for sending from peer to UI
	 * 			
	 *  Methods:
	 *  
	 * 	Modification History:
	 * 		April 16, 2015
	 * 			Original version
	 * 
	 * 	 	April 28, 2015
	 * 			Added some comments.
	 * 		
	 * 	 	April 30, 2015
	 * 			Changed name of class and assigned number 54322.
	 * 
	 */
	
	public PortNumberPeerUI(int portNumber) 
	{
		// Construct a port for sending
		super(portNumber);
	}

}
