/**
 * Use port 12345. Might not need this class since we are using the same port
 * for sending and receiving.
 * 
 */
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
	 * 		public PortNumberForSending(int portNumber) 
	 * 			construct a port for sending
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
