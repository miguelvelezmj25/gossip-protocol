import java.util.*;
public class CommandProcessor
{
	/*
		Courtney Karppi
		CISC 370
		April 18, 2015

		Class Variables:
			commandRegistry
				A map that contains all the commands and command name
			noSuchCommand
				A command that is used when the command doesn't exist
			nothingEnteredCommand
				A command that is used when nothing is entered
		Constructors:
			CommandProcessor(Command noSuchCommand, Command nothingEnteredCommand)
				Sets the instance variable and creates a new hashmap
		Methods:
			public Command[] getAllCommands()
				Returns all the commands in the commandRegistry in the form of a Command[]
			public void register(Command command)
				Registers a new command in the commandRegistry
			public Command getCommand(String commandText)
				Runs the command associated with the parameter
			private static String convertToKey(String data)
				Removes the white space from the data and converts it to lower case
	*/
	private Map<String,Command>		commandRegistry;
	private Command 				noSuchCommand;
	private Command 				nothingEnteredCommand;

	public CommandProcessor(Command noSuchCommand, Command nothingEnteredCommand)
	{
		this.noSuchCommand			= noSuchCommand;
		this.nothingEnteredCommand 	= nothingEnteredCommand;
		this.commandRegistry 		= new HashMap<String, Command>();
	}//CommandProcessor

	public Command[] getAllCommands()
	{
		//Returns all the commands in the commandRegistry in the form of a Command[]
		return commandRegistry.values().toArray(new Command[0]);
	}//getAllCommands

	public void register(Command command)
	{
		//Registers a new command in the commandRegistry
		String 		commandText;

		commandText = convertToKey(command.getCommandName());
		//checks the parameter to make sure it is not blank or not in the commandRegistry
		if(!(commandText.equals("")||commandRegistry.containsKey(commandText)))
		{
			commandRegistry.put(commandText, command);
		}
	}//register

	public Command getCommand(String commandText)
	{
		//Runs the command associated with the parameter
		Command 	command;
		if(commandText.length() == 0)
		{
			command = this.noSuchCommand;
		}
		else
		{
			command = commandRegistry.get(convertToKey(commandText));
		}
		if (command == null)
		{
			command = this.nothingEnteredCommand;
		}
		return command;
	}//getCommand

	private static String convertToKey(String data)
	{
		//Removes the white space from the data and converts it to lower case
		return data.trim().toLowerCase();
	}//convertToKey

}//class
