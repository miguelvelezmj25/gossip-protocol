// TODO ask jarvis if comparable type is command
public abstract class Command implements Cloneable, Comparable<Command>, ActionInterface {

	private String commandName;
	private String description;
	private String parameters; 
	
	public Command() {
		// TODO what should the default be?
		this.commandName = "none";
		this.description = "A none command"; 
		this.parameters = "";
	}
	
	public Command(String commandName, String description) {
		this.commandName = commandName;
		this.description = description;
		this.parameters = "";
	}
	
	/**
	 * Execute the command
	 */
	@Override
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
	
	/**
	 * Compares the command names of the commands. Usually used when the command is help
	 * so that all available commands are printed in order
	 */
	@Override
	public int compareTo(Command other) {
		// Order the commands in alphabetical order
		return this.commandName.compareToIgnoreCase(other.commandName);
	}
	
	/**
	 * Compares the command name
	 * @param text
	 * @return
	 */
	public boolean equals(String text) {
		// TODO correct?
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
		
		// Check if the instance variables do not match
		if(!this.commandName.equals(object.getCommandName())) {
			result = false;
		}
		
		if(!this.description.equals(object.getDescription())) {
			result = false;
		}
		
		if(!this.parameters.equals(object.getParameters())) {
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
	
	/**
	 * Return an array of string with the parameters of the command
	 * @param delimiters
	 * @return
	 */
	public String[] getParameters(String delimiters) {
		// Split the string based on the delimiters
		return this.parameters.split(delimiters);
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
