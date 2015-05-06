public class PortNumberUIPeer extends PortNumber 
{

	/** 
	 *	Miguel Velez
	 * 	April 16, 2015
	 * 
	 * 	The port number used for communicating between the UI and my peer. 54321
	 * 
	 *  Class variables:
	 * 
	 *  Constructors:
	 * 	
	 * 		public PortNumberUIPeer(int portNumber) 
	 * 			construct a port for sending from the UI to the Peer
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
	 * 			Changed name of class and assigned number 54321.
	 * 
	 */
	
	public PortNumberUIPeer(int portNumber) 
	{
		// Construct a port for sending
		super(portNumber);
	}

}
