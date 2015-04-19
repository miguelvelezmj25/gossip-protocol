// TODO ask jarvis if comparable type is command
public abstract class Command implements Cloneable, Comparable<Command> {

	private String commandName;
	private String description; // TODO what is a description
	private String parameters; // TODO what is this?
	
	public Command() {
		// TODO what should the default be?
		this.commandName = "none";
		this.description = "A none command"; // TODO is this right?
		this.parameters = ""; // TODO
	}
	
	public Command(String commandName, String description) {
		this.commandName = commandName;
		this.description = description;
		this.parameters = ""; // TODO
	}
	
	/**
	 * Execute the command
	 */
	public abstract void execute();
	
	/**
	 * Perform a shallow clone
	 * 
	 * TODO should it throw this exception
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	@Override
	public int compareTo(Command other) {
		// TODO how to compare commands?
		return 0;
	}
	
	/**
	 * Compares the command name
	 * @param text
	 * @return
	 */
	public boolean equals(String text) {
		return this.commandName.equals(text);
	}
	
	/**
	 * Check if the command objects are the same
	 */
	@Override
	public boolean equals(Object other) {
		// TODO is this right
		boolean result =  true;
		
		// Cast and create a command object 
		Command object = (Command) other;
		
		// Check if the instance variables match
		if(this.commandName.equals(object.getCommandName())) {
			result = false;
		}
		
		if(this.description.equals(object.getDescription())) {
			result = false;
		}
		
		if(this.parameters.equals(object.getParameters())) {
			result = false;
		}
		
		// Return result
		return result;
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
	
	/**
	 * Return the hash code of the object
	 */
	@Override
	public int hashCode() {
		// TODO
		String variables = this.commandName + this.description + this.parameters;
		
		return variables.hashCode();
	}
	
	/**
	 * Return if there are parameters
	 * @return
	 */
	public boolean hasParameters() {
		return !this.parameters.isEmpty();
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
	
	/**
	 * Print the variables from the command
	 */
	public String toString() {
		return "Command name: " + this.commandName + " description: " + this.description
				+ " parameters: " + this.parameters;
	}

}
