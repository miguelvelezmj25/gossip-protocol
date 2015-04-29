
public abstract class Command implements Cloneable, Comparable<Command>, Runnable 
{
	/**
	 * 
	 * Miguel Velez
	 * April 16, 2015
	 * 
	 * This class 
	 * 
	 * Class variables:
	 * 
	 * 		private String commandName
	 * 			the name of the command
	 * 		
	 * 		private String description
	 * 			the description of the command
	 * 
	 * 		private String parameters
	 * 			the parameters of the command
	 * 
 	 * Constructors:
 	 * 
 	 * 		public Command() 
 	 * 			create a default command
 	 * 	
 	 * 		public Command(String commandName, String description) 
 	 * 			create a command with the provided name and description
	 * 
	 * Methods:
	 *	
	 *		public abstract void run();
	 * 			execute the command
	 * 
	 * 		public Object clone() 
	 * 			perform a shallow clone
	 * 
	 * 		public int compareTo(Command other) 
	 * 			compares the command names of the commands. Usually used when the command is help
	 * 			so that all available commands are printed in order
	 * 			
	 * 		public boolean equals(String text) 
	 * 			compares the command name
	 * 
	 * 		public boolean equals(Object other)
	 * 			check if the command objects are the same
	 * 		
	 * 		public String getCommandName()
	 * 			return the command name
	 * 
	 * 		public String getDescription()
	 * 			return the description
	 * 
	 * 		public String getParameters()
	 * 			return the parameters
	 * 
	 * 		public String[] getParameters(String delimiters)
	 * 			return an array of string with the parameters of the command
	 * 
	 * 		public int hashCode() 
	 * 			return the hash code of the object
	 * 
	 * 		public boolean hasParameters()
	 * 			return if there are parameters
	 * 		
	 * 		public void print(String message)
	 * 			print the message without a new line
	 * 
	 * 		public void println()
	 * 			print a new line
	 * 
	 * 		public void println(String message)
	 * 			print a message with a new line
	 * 		
	 * 		public void setParameters(String parameters)
	 * 			setting the parameters
	 * 
	 * 		public String toString() 
	 * 			print the variables from the command 	
	 * 
	 *      
	 * Modification History:
	 * 		April 16, 2015
	 * 			Original version
	 * 
	 * 		April 29, 2015
	 * 			Added some comments.
	 *  
	 */
	
	private String commandName;
	private String description;
	private String parameters; 
	
	public Command() 
	{
		// TODO what should the default be?
		this.commandName = "none";
		this.description = "A none command"; 
		this.parameters = "";
	}
	
	public Command(String commandName, String description) 
	{
		this.commandName = commandName;
		this.description = description;
		this.parameters = "";
	}
	
	@Override
	// Execute the command
	public abstract void run();
	
	@Override
	public Object clone() 
	{
		// Perform a shallow clone
		// Try cloning
		try 
		{
			return super.clone();		
		}
		catch(CloneNotSupportedException cnse) 
		{
			throw new RuntimeException("The command could not be cloned");
		}
	}
	
	@Override
	public int compareTo(Command other) 
	{
		// Compares the command names of the commands. Usually used when the command is help
		// so that all available commands are printed in order
		// Check if null
		if(other == null)
		{
			throw new IllegalArgumentException("The command that you provided is null");
		}
		
		// Order the commands in alphabetical order
		return this.commandName.compareToIgnoreCase(other.commandName);
	}
	
	public boolean equals(String text) 
	{
		// Compares the command name
		return this.commandName.equalsIgnoreCase(text);
	}
	
	@Override
	public boolean equals(Object other) 
	{		
		// Check if the command objects are the same
		// TODO is this right
		// Check if they are the same command
		if(this == other) {
			return true;
		}
		
		// Check if the other is null or it is a different class
		if(other == null || (this.getClass() != other.getClass())) 
		{
			return false;
		}

		// Cast and create a command object 
		Command object = (Command) other;
		
		// Check if the instance variables do not match
		if(!this.commandName.equals(object.getCommandName())) 
		{
			return false;
		}
		
		if(!this.description.equals(object.getDescription())) 
		{
			return false;
		}
		
		if(!this.parameters.equals(object.getParameters())) 
		{
			return false;
		}	
		
		// After all those checks, the commands are the same
		return true;
	
	}
	
	public String getCommandName() 
	{
		// Return the command name
		return this.commandName;
	}
	
	public String getDescription() 
	{
		// Return the description
		return this.description;
	}
	
	public String getParameters() 
	{
		// Return the parameters
		return this.parameters;
	}
	
	public String[] getParameters(String delimiters) 
	{
		// Return an array of string with the parameters of the command
		// Check if the delimiters are null
		if(delimiters == null) 
		{
			throw new IllegalArgumentException("The delimiters that you provided are null");
		}
		
		// Check if the array is empty
		if(delimiters.isEmpty()) 
		{
			throw new IllegalArgumentException("You did not provided any delimiters");
		}
		
		// Split the string based on the delimiters
		return this.parameters.split(delimiters);
	}
	
	@Override
	public int hashCode() 
	{
		// Return the hash code of the object
		String variables = this.commandName + this.description + this.parameters;
		
		return variables.hashCode();
	}
	
	public boolean hasParameters() 
	{
		// Return if there are parameters
		return !this.parameters.isEmpty();
	}
	
	public void print(String message)
	{
		// Print the message without a new line
		System.out.print(message);
	}
	
	public void println() 
	{
		// Print a new line
		this.println("");
	}
	
	public void println(String message) 
	{
		// Print a message with a new line
		this.print(message + "\n");
	}
	
	public void setParameters(String parameters) 
	{
		// Setting the parameters
		// Check if they are null
		if(parameters == null) 
		{
			throw new IllegalArgumentException("The parameters that you provided are null");
		}
		
		this.parameters = parameters;
	}
	
	@Override
	public String toString() 
	{
		// Print the variables from the command
		return "Command name: " + this.commandName + " description: " + this.description
				+ " parameters: " + this.parameters;
	}

}
