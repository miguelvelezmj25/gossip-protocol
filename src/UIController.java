import java.net.*;
import java.util.*;


// TODO need instance variable peerAddress
/**
 * @author jens3048
 *
 */
public class UIController
{

	private CommandProcessor commandProcessor;
	private boolean done;
	private IncomingPacketQueue incomingPacketsFromPeerQueue;
	private OutgoingPacketQueue outgoingPacketsToPeerQueue;
	private InetSocketAddress peerAddress;
	private DatagramReceiver receiveFromPeer;
	private DatagramSender sendToPeer;
//	private int packetSize; // TODO what is this?

	/**
	 * @param incomingPortNumber
	 * @param outgoingPortNumber
	 * @param packetSize
	 */
	public UIController(PortNumber portNumberForReceiving, PortNumber portNumberForSending, int packetSize)
	{
		DatagramSocket dsForReceiving;
		DatagramSocket dsForSending;
		
		
		
//		this.packetSize = packetSize; // TODO what is this
		incomingPacketsFromPeerQueue = new IncomingPacketQueue();
		outgoingPacketsToPeerQueue = new OutgoingPacketQueue();
		try
		{
			dsForReceiving = new DatagramSocket(portNumberForReceiving.get());
			receiveFromPeer = new DatagramReceiver(dsForReceiving, incomingPacketsFromPeerQueue, packetSize);
		}
		catch(SocketException e)
		{
			System.out.println("Caught socket exception " + e.getMessage());
		}
		try
		{
			dsForSending = new DatagramSocket(portNumberForSending.get());
			sendToPeer = new DatagramSender(dsForSending, outgoingPacketsToPeerQueue, packetSize);
		}
		catch(SocketException e)
		{
			System.out.println("Caught socket exception " + e.getMessage());
		}
		
		

		done = false;

		commandProcessor = new CommandProcessor(new CommandNone(), new CommandError());
		// TODO add all the commands
		this.commandProcessor.register(new CommandHelp());
		this.commandProcessor.register(new CommandQuit());
		this.commandProcessor.register(new CommandSend());


	}

	/**
	 *
	 */
	public void start()
	{
		
		UIControllerCommand command;
		Scanner	scanner;
		String	userCommand;
		receiveFromPeer.startAsThread();

		scanner = new Scanner(System.in);

		while(!done)
		{
			System.out.println("Type in a command: ");
			userCommand = scanner.nextLine();
			command = null;
			
			
			command = (UIControllerCommand) this.commandProcessor.getCommand(userCommand.toLowerCase());
			
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
			UDPMessage udpMessage;
			TimeToLive ttl;
			
			ttl = new TimeToLive(70);
			
			id1 = ID.idFactory();
			id2 = ID.idFactory();
			
//			udpMessage = new UDPMessage(id1, id2, ttl, this.getCommandName());
//			DatagramPacket dp = udpMessage.getDatagramPacket();
//			outgoingPacketQueue.enQueue(dp);
			
		
			
			String message = "hello";
			byte[] buffer = new byte[message.getBytes().length];
			
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
			
//			System.out.println("Set address to local host");
			try {
				dp.setAddress(InetAddress.getLocalHost());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			System.out.println("Set port 12345");
			dp.setPort(12345);
			
//			System.out.println("Set packet data");
			dp.setData(message.getBytes());
			
			outgoingPacketsToPeerQueue.enQueue(dp);
			sendToPeer.startAsThread();

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
			this.print("Quitting the application");
			

			receiveFromPeer.stop();
			sendToPeer.stop();
			
			setDoneFlag(true);
			System.out.println("Quit successful");
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
	
	// TODO might start on the datagram sender receiver
	public class CommandStart extends UIControllerCommand
	{
		/**
		 * Creates the command
		 */
		public CommandStart()
		{
			super("start", "start sender and receiver");
		}

		/**
		 * Does nothing
		 */
		public void run()
		{
			this.println("");
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
//			try {
//				DatagramSocket sendSocket = new DatagramSocket((new PortNumber(12345)).get());
				this.sendToPeer();
				
				
//			} catch (SocketException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}

	}
}


// TODO implement start, stop, get, find, join, leave
