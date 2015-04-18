import java.net.*;
import java.util.*;

/**
 * @author jens3048
 *	
 */
public class UIController
{
	/**
	 * 
	 */
	private CommandProcessor commandProcessor;
	
	/**
	 * 
	 */
	private boolean done;
	
	/**
	 * 
	 */
	private IncomingPacketQueue incomingPacketQueue;
	
	/**
	 * 
	 */
	private OutgoingPacketQueue outgoingPacketQueue;
	
	/**
	 * 
	 */
	private DatagramReceiver receiveFromPeer;
	
	/**
	 * 
	 */
	private DatagramSender sendToPeer;
	
	/**
	 * @param incomingPortNumber
	 * @param outgoingPortNumber
	 * @param packetSize
	 */
	public UIController(PortNumber portNumber, int packetSize)
	{
		InetSocketAddress socketAddress;
		
		socketAddress = new InetSocketAddress(portNumber.get());
		
		incomingPacketQueue = new IncomingPacketQueue();
		outgoingPacketQueue = new OutgoingPacketQueue();
		try
		{
			receiveFromPeer = new DatagramReceiver(socketAddress, incomingPacketQueue, packetSize);
		}
		catch(SocketException e)
		{
			System.out.println("Caught socket exception " + e.getMessage());
		}
		try
		{
			sendToPeer = new DatagramSender(socketAddress, outgoingPacketQueue, packetSize);
		}
		catch(SocketException e)
		{
			System.out.println("Caught socket exception " + e.getMessage());
		}			
		
		done = false;
		
		commandProcessor = new CommandProcessor(new CommandError(), new CommandNone());
		
		
	}
	
	/**
	 * 
	 */
	public void start()
	{
		UIControllerCommand command;
		Scanner	scanner;
		String	commandType;
		
		scanner = new Scanner(System.in);
		
		while(!done)
		{
			System.out.println("Type in a type of command to send or done");
			commandType = scanner.nextLine();
			command = null;
			if(commandType.toLowerCase().equals("done"))
			{
				done = true;
			}
			if(commandType.toLowerCase().equals("error"))
			{
				command = new CommandError();
			}	
			if(commandType.toLowerCase().equals("none"))
			{
				command = new CommandNone();
			}
			if(commandType.toLowerCase().equals("help"))
			{
				command = new CommandHelp();
			}
			
			if(command != null)
			{
				command.sendToPeer();
			}
		}
	}
	
	/**
	 * @author jens3048
	 *
	 */
	public abstract class UIControllerCommand extends Command
	{
		
		/**
		 * 
		 */
		public UIControllerCommand()
		{
			super();
		}
		
		/**
		 * @param commandName
		 * @param description
		 */
		public UIControllerCommand(String commandName, String description)
		{
			super(commandName, description);
		}
		
		/**
		 * @return
		 */
		public CommandProcessor getCommandProcessor()
		{
			return commandProcessor;
		}
		
		/**
		 * @return
		 */
		public boolean getDoneFlag()
		{
			return done;
		}
		
		/* (non-Javadoc)
		 * @see Command#print(java.lang.String)
		 */
		public void print(String message)
		{
			super.print(message);
		}
		
		/* (non-Javadoc)
		 * @see Command#println()
		 */
		public void println()
		{
			super.println();
		}
		
		/* (non-Javadoc)
		 * @see Command#println(java.lang.String)
		 */
		public void println(String data)
		{
			super.println(data);
		}
		
		/**
		 * @param flag
		 */
		public void setDoneFlag(boolean flag)
		{
			done = flag;
		}
		
		/**
		 * 
		 */
		public void sendToPeer()
		{
			sendToPeer.run();
		}
		
	}
	
	/**
	 * @author jens3048
	 * A command depicting an error
	 */
	public class CommandError extends UIControllerCommand
	{
		/**
		 * Creates the command
		 */
		public CommandError()
		{
			super();
		}
		
		/**
		 * Informs the user that there is an error somewhere.
		 */
		public void execute()
		{
			println("You are seeing this message because an errenous message was received. Fix that.");
			setDoneFlag(true);
		}

		/**
		 * Always goes first, unless there is another error or a help command
		 */
		public int compareTo(Object o) 
		{
			int result;
			result = 1;
			if(((Command)o).getCommandName().equals("Error") || ((Command)o).getCommandName().equals("Help"))
			{
				result = 0;
			}
			return result;
		}

	}
	
	/**
	 * @author jens3048
	 * A command requesting help.
	 */
	public class CommandHelp extends UIControllerCommand
	{
		/**
		 * Creates the command.
		 */
		public CommandHelp()
		{
			super();
		}
		
		/**
		 * TODO come up with a help message
		 */
		public void execute()
		{
			
		}

		/**
		 * Always goes first, unless there is an error or another request for help
		 */
		@Override
		public int compareTo(Object o) 
		{
			int result;
			result = 1;
			if(((Command)o).getCommandName().equals("Error") || ((Command)o).getCommandName().equals("Help"))
			{
				result = 0;
			}
			return result;
		}

	}

	/**
	 * @author jens3048
	 * A command that does nothing
	 */
	public class CommandNone extends UIControllerCommand
	{
		/**
		 * Creates the command
		 */
		public CommandNone()
		{
			super();
		}
		
		/**
		 * Does nothing
		 */
		public void execute()
		{
			setDoneFlag(true);
		}

		/**
		 * Order doesn't matter
		 */
		public int compareTo(Object o) 
		{
			return 0;
		}

	}
}
