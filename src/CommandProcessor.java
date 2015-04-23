import java.util.*;

// TODO getCommand is missing
public class CommandProcessor
{
	/*
		Courtney Karppi
		CISC 370
		April 18, 2015
	*/
	private static 	Map<String,Command>commandRegistry; // TODO static?
	private Command noSuchCommand; 
	private Command nothingEnteredCommand;

	public CommandProcessor(Command noSuchCommand, Command nothingEnteredCommand)
	{
		this.noSuchCommand = noSuchCommand;
		this.nothingEnteredCommand = nothingEnteredCommand;
		// TODO commandRegistry???
	}//CommandProcessor

	//getAllCommands method returns all the commands in the commandRegistry
	public Command[] getAllCommands() // TODO test
	{
		//returns all the commands in the form of a Command[]
		return commandRegistry.values().toArray(new Command[0]);
	}//getAllCommands

	//registers a new command in the commandRegistry
	public void register(Command command) // TODO test
	{
		String commandText;
		commandText = convertToKey(command.getCommandName());

		//checks the parameter to make sure it is not blank or not in the commandRegistry
		if(!(commandText.equals("")||commandRegistry.containsKey(commandText)))
		{
			commandRegistry.put(commandText, command);
		}
	}//register

	//runs the command associated with the parameter
	public void runCommand(String commandText) // TODO what is this?
	{
		Command command;

		//checks to see if the paramter is not blank or already in the commandRegistry
		//if its not then it executes the associated command
		if(!convertToKey(commandText).equals(""))
		{
			command = this.nothingEnteredCommand;
		}
		else if (!commandRegistry.containsKey(convertToKey(commandText)))
		{
			command = this.noSuchCommand;
		}
		else
		{
			command = commandRegistry.get(convertToKey(commandText));
		}
		command.execute();
	}//runCommand

	//Removes the white space from the data and converts it to lower case
	private static String convertToKey(String data)
	{
		return data.trim().toLowerCase();
	}//convertToKey

}//class
