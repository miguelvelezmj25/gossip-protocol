import java.net.*;
import java.util.*;

import javax.smartcardio.CommandAPDU;

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
	private int packetSize;

	/**
	 * @param incomingPortNumber
	 * @param outgoingPortNumber
	 * @param packetSize
	 */
	public UIController(PortNumber portNumberForReceiving, PortNumber portNumberForSending, int packetSize)
	{
		DatagramSocket dsForReceiving;
		DatagramSocket dsForSending;
		
		
		
		this.packetSize = packetSize;
		incomingPacketQueue = new IncomingPacketQueue();
		outgoingPacketQueue = new OutgoingPacketQueue();
		try
		{
			dsForReceiving = new DatagramSocket(portNumberForReceiving.get());
			receiveFromPeer = new DatagramReceiver(dsForReceiving, incomingPacketQueue, packetSize);
		}
		catch(SocketException e)
		{
			System.out.println("Caught socket exception " + e.getMessage());
		}
		try
		{
			dsForSending = new DatagramSocket(portNumberForSending.get());
			sendToPeer = new DatagramSender(dsForSending, outgoingPacketQueue, packetSize);
		}
		catch(SocketException e)
		{
			System.out.println("Caught socket exception " + e.getMessage());
		}

		done = false;

		commandProcessor = new CommandProcessor(new CommandError(), new CommandNone());
		// TODO add all the commands
		this.commandProcessor.register(new CommandHelp());
		this.commandProcessor.register(new CommandQuit());


	}

	/**
	 *
	 */
	public void start()
	{
		UIControllerCommand command;
		Scanner	scanner;
		String	userCommand;

		scanner = new Scanner(System.in);

		while(!done)
		{
			System.out.println("Type in a command: ");
			userCommand = scanner.nextLine();
			command = null;
			
			if(userCommand.length() < 1) {
				// TODO is this right? Or do we use the command processor?
				command = new CommandNone();				
			}
			else {
				command = (UIControllerCommand) this.commandProcessor.getCommand(userCommand.toLowerCase());
			}
			
			if(command == null) {
				// TODO is this right? Or do we use the command processor?
				command = new CommandError();
			}
			
			/*
			 * Command will run. If it is meant for a peer, its run method should handle the sending.
			 */
			command.run();	
		
		}
		scanner.close();
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
			ID id1;
			ID id2;
			byte[] bArr1;
			byte[] bArr2;
			UDPMessage udpMessage;
			TimeToLive ttl;
			
			ttl = new TimeToLive(70);
			bArr1 = new byte[16];
			bArr2 = new byte[16];
			
			id1 = new ID(bArr1);
			id2 = new ID(bArr2);
			
			udpMessage = new UDPMessage(id1, id2, ttl, this.toString());
			DatagramPacket dp = udpMessage.getDatagramPacket();
			outgoingPacketQueue.enQueue(dp);
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
			super("error", "An error command");
		}

		/**
		 * Informs the user that there is an error somewhere.
		 */
		public void run()
		{
			println("You are seeing this message because an errenous message was received. Fix that.");
//			setDoneFlag(true);
		}

	}
	
	public class CommandQuit extends UIControllerCommand
	{
		/**
		 * Creates the command
		 */
		public CommandQuit()
		{
			super("quit", "quit the application");
		}

		/**
		 * Informs the user that there is an error somewhere.
		 */
		public void run()
		{
			this.print("Quiting the application");
			setDoneFlag(true);
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
			super("help", "A help command");
		}

		/**
		 * TODO come up with a help message
		 */
		public void run()
		{
			// Get all the commands
			Command[] allCommands = this.getCommandProcessor().getAllCommands();

			this.println("Here is a list of currently accepted commands:");
			
			// Sort the commands
			Arrays.sort(allCommands);
			
			// Print all the commands
			for(int i = 0; i < allCommands.length; i++) {
				this.println(allCommands[i].toString());
			}
			
			// Print a new line
			this.println();
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
			super("none", "nothing was entered");
		}

		/**
		 * Does nothing
		 */
		public void run()
		{
			this.println("You did not enter a command");
//			setDoneFlag(true);
		}

	}
}


// TODO implement start, stop, get, find, join, leave
