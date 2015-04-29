/**
 * Use port 12345. Might not need this class since we are using the same port
 * for sending and receiving.
 * 
 * TODO this whole class is going to change.
 */
public class PortNumberForSending extends PortNumber 
{

	/** 
	 *	Miguel Velez
	 * 	April 16, 2015
	 * 
	 * 	The port number used for sending. // TODO
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
	 */
	
	public PortNumberForSending(int portNumber) 
	{
		// Construc a port for sending
		super(portNumber);
	}

}
