import java.net.*;
import java.util.*;


/**
 * @author jens3048
 *
 */
public class UIController
{

	private CommandProcessor 	commandProcessor;
	private boolean 			done;
	private IncomingPacketQueue incomingPacketsFromPeerQueue;
	private OutgoingPacketQueue outgoingPacketsToPeerQueue;
	private PeerController		ourPeerController;
	private InetSocketAddress 	peerAddress;
	private DatagramReceiver 	receiveFromPeer;
	private Scanner				scanner;
	private DatagramSender 		sendToPeer;

	/**
	 * @param incomingPortNumber
	 * @param outgoingPortNumber
	 * @param packetSize
	 */
	public UIController(PortNumberUIPeer portNumberUIPeer, PortNumberPeerUI portNumberPeerUI, int packetSize)
	{
		DatagramSocket dsForReceiving;
		DatagramSocket dsForSending;
		
		//Set instance variables
		this.peerAddress = new InetSocketAddress(portNumberPeerUI.get());
		incomingPacketsFromPeerQueue = new IncomingPacketQueue();
		outgoingPacketsToPeerQueue = new OutgoingPacketQueue();
		
		try
		{
			dsForReceiving = new DatagramSocket(portNumberUIPeer.get());
			receiveFromPeer = new DatagramReceiver(dsForReceiving, incomingPacketsFromPeerQueue, packetSize);
		}
		catch(SocketException e)
		{
			System.out.println("Caught socket exception " + e.getMessage());
		}
		try
		{
			//Sending socket doesn't matter
			dsForSending = new DatagramSocket();
			sendToPeer = new DatagramSender(dsForSending, outgoingPacketsToPeerQueue, packetSize);
		}
		catch(SocketException e)
		{
			System.out.println("Caught socket exception " + e.getMessage());
		}
				
		//Create PeerController
		try {
			ourPeerController = new PeerController(new PortNumberPeerCommunity(12345), new PortNumberPeerUI(peerAddress.getPort()), new InetSocketAddress(InetAddress.getLocalHost(),portNumberUIPeer.get()));

			ourPeerController.startAsThread();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		done = false;
		commandProcessor = new CommandProcessor(new CommandNone(), new CommandError());
		
		// TODO add all the commands
		this.commandProcessor.register(new CommandHelp());
		this.commandProcessor.register(new CommandQuit());
		this.commandProcessor.register(new CommandSend());
		this.commandProcessor.register(new CommandFind());
		

	}

	/**
	 *	Do Everything
	 */
	public void start()
	{
		
		UIControllerCommand command;
		String				userCommand;
		
		
		//Start Threads
		receiveFromPeer.startAsThread();
		sendToPeer.startAsThread();
		//Check for uer input
		scanner = new Scanner(System.in);

		
		
		while(!done)
		{
			System.out.print("Type in a command: ");
			if(scanner.hasNextLine())
			{
				userCommand = scanner.nextLine();
				command = (UIControllerCommand) this.commandProcessor.getCommand(userCommand.toLowerCase());

				// Command will run. If it is meant for a peer, its run method should handle the sending.
				command.run();	
			}
			if(!incomingPacketsFromPeerQueue.isEmpty())
			{
				System.out.println(new String(incomingPacketsFromPeerQueue.deQueue().getData()));
			}
		}
		//Finish everything
		ourPeerController.setDoneFlag(true);
		scanner.close();
	}

	/**
	 * @author jens3048
	 *
	 */
	public abstract class UIControllerCommand extends Command
	{
		
		public UIControllerCommand()
		{
			super();
		}
		
		public UIControllerCommand(String commandName, String description)
		{
			super(commandName, description);
		}

		public CommandProcessor getCommandProcessor()
		{
			return commandProcessor;
		}

		public boolean getDoneFlag()
		{
			return done;
		}

		public void print(String message)
		{
			super.print(message);
		}

		public void println()
		{
			super.println();
		}

		public void println(String data)
		{
			super.println(data);
		}

		public void setDoneFlag(boolean flag)
		{
			done = flag;
		}
		
		public void sendToPeer()
		{
			byte[] buffer;
			
			//Ask user what they would like to send
			System.out.println("Please type what you would like to " + super.getCommandName() + ":");
			String message = "," + super.getCommandName() + "," + scanner.nextLine();
			
			//Create DatagramPacket
			buffer = message.getBytes();
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
			
			try 
			{
				dp.setAddress(InetAddress.getLocalHost());
			} 
			catch (UnknownHostException e) 
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
			dp.setPort(peerAddress.getPort());
			dp.setData(message.getBytes());
			
			outgoingPacketsToPeerQueue.enQueue(dp);

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
			this.println("Quitting the application");
			

			receiveFromPeer.stop();
			sendToPeer.stop();
			
			setDoneFlag(true);
//			System.out.println("Done with QuitCommand");
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
		}

	}
	
	public class CommandSend extends UIControllerCommand
	{
		/**
		 * Creates the command
		 */
		public CommandSend()
		{
			super("send", "send a packet from the outgoing queue");
		}

		public void run()
		{
				this.sendToPeer();
		}

	}
	
	public class CommandFind extends UIControllerCommand
	{
		/**
		 * Creates the command
		 */
		public CommandFind()
		{
			super("find", "Search for something");
		}

		public void run()
		{
			this.sendToPeer();
		}

	}
	

}


// TODO implement start, stop, get, find, join, leave
