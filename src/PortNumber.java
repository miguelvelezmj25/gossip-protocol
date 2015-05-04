
public class PortNumber 
{
	/** 
	 *	Miguel Velez
	 * 	April 16, 2015
	 * 
	 * 	The port number used for communication.
	 * 
	 * Class variables:
	 * 
	 * 		private int portNumber
	 * 			the port number
	 * 	
	 * Constructors:
	 * 	
	 * 		public PortNumber(int portNumber)
	 * 			creates a port number
	 * 			
	 * Methods:
	 * 
	 * 		public int get() 
	 * 			returns the port number
	 * 
	 * 		public String toString() 
	 * 			return a string of the port number
	 * 
	 * 	Modification History:
	 * 		April 16, 2015
	 * 			Original version
	 * 
	 * 	 	April 28, 2015
	 * 			Added some comments.
	 * 
	 */
	 
	private int portNumber;
	
	public PortNumber(int portNumber) 
	{
		// Check if less than 0
		if(portNumber < 0)
		{
			throw new IllegalArgumentException("The port number cannot be null");
		}
		
		// Check if greater than the allowed number
		if(portNumber > 65535)
		{
			throw new IllegalArgumentException("The port number you provdided (" + portNumber + ") is"
					+ "greater than the maximum allowed port number (65535)");
		}
		
		// Creates a port number
		this.portNumber = portNumber;
	}
	
	public int get() 
	{
		// Returns the port number
		return this.portNumber;
	}
	
	@Override
	public String toString() 
	{
		// Return a string of the port number
		return ("Port number: " + this.portNumber);
	}
	
}
