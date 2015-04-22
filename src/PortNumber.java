/**
 * Use port 12345
 */
public class PortNumber {

	private int portNumber;
	
	public PortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	
	/**
	 * Returns the port number
	 * @return
	 */
	public int get() {
		return this.portNumber;
	}
	
	/**
	 * Return a string of the port number
	 */
	@Override
	public String toString() {
		return ("Port number: " + this.portNumber);
	}
	
}
