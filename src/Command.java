
@SuppressWarnings("rawtypes")
public abstract class Command implements Cloneable, Comparable {

	private String commandName;
	private String description;
	private String parameters;
	
	public Command() {
		// TODO
	}
	
	public Command(String commandName, String description) {
		this.commandName = commandName;
		this.description = description;
		// TODO
	}
	
	/**
	 * Execute the command
	 */
	public abstract void execute();
	
	@Override
	public Object clone() {
		// TODO
		return null;
	}
	
	public int compareTo(Command other) {
		// TODO
		return 0;
	}
	
	public boolean equals(String text) {
		// TODO
		return false;
	}
	
	@Override
	public boolean equals(Object other) {
		// TODO
		return false;
	}
	
	/** 
	 * Return the command name
	 * @return
	 */
	public String getCommandName() {
		return this.commandName;
	}
	
	/**
	 * Return the description
	 * @return
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Return the parameters
	 * @return
	 */
	public String getParameters() {
		return this.parameters;
	}
	
	public String[] getParameters(String delimiters) {
		// TODO
		return null;
	}
	
	@Override
	public int hashCode() {
		// TODO
		return 0;
	}
	
	/**
	 * Reutrn if there are paremeters
	 * @return
	 */
	public boolean hasParameters() {
		return this.parameters.isEmpty();
	}
	
	/**
	 * Print the message without a new line
	 * @param message
	 */
	public void print(String message) {
		System.out.print(message);
	}
	
	/**
	 * Print a new line
	 * @param message
	 */
	public void println() {
		this.println("");
	}
	
	/**
	 * Print a message with a new line
	 * @param message
	 */
	public void println(String message) {
		this.print(message + "\n");
	}
	
	/** 
	 * Setting the parameters
	 * @param parameters
	 */
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	
	public String toString() {
		// TODO
		return "";
	}
}
