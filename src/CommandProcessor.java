import java.util.*;

// TODO Test
public class CommandProcessor
{
	/*
		Courtney Karppi
		CISC 370
		April 18, 2015
	*/
	private Map<String,Command>commandRegistry;
	private Command noSuchCommand;
	private Command nothingEnteredCommand;

	public CommandProcessor(Command noSuchCommand, Command nothingEnteredCommand)
	{
		this.noSuchCommand = noSuchCommand;
		this.nothingEnteredCommand = nothingEnteredCommand;
		this.commandRegistry = new HashMap<String, Command>();
		
		// TODO ask Jarvis what do we do with the noSuch and nothingEntered commands
	}//CommandProcessor

	//getAllCommands method returns all the commands in the commandRegistry
	public Command[] getAllCommands()
	{
		//returns all the commands in the form of a Command[]
		return commandRegistry.values().toArray(new Command[0]);
	}//getAllCommands

	//registers a new command in the commandRegistry
	public void register(Command command)
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
	public Command getCommand(String commandText)
	{
		return commandRegistry.get(convertToKey(commandText));
	}//getCommand

	//Removes the white space from the data and converts it to lower case
	private static String convertToKey(String data)
	{
		return data.trim().toLowerCase();
	}//convertToKey

}//class
