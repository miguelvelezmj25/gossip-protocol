
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
	public String toString() {
		return ("" + this.portNumber);
	}
	
}
