import java.net.InetSocketAddress;

public class UIController
{
	private CommandProcessor commandProcessor;
	
	private boolean done;
	
	private IncomingPacketQueue incomingPacketQueue;
	
	private OutgoingPacketQueue outgoingPacketQueue;
	
	private DatagramReceiver receiveFromPeer;
	
	private DatagramSender sendToPeer;
	
	public UIController(PortNumberForReceiving incomingPortNumber, PortNumberForSending outgoingPortNumber, int packetSize)
	{
		InetSocketAddress outgoing;
		InetSocketAddress incoming;
		
		incoming = new InetSocketAddress(incomingPortNumber.get());
		outgoing = new InetSocketAddress(outgoingPortNumber.get());
		
		incomingPacketQueue = new IncomingPacketQueue();
		outgoingPacketQueue = new OutgoingPacketQueue();
		
		receiveFromPeer = new DatagramReceiver(incoming, incomingPacketQueue, packetSize);
		sendToPeer = new DatagramSender(outgoing, outgoingPacketQueue, packetSize);
		
		done = false;
		
		commandProcessor = new CommandProcessor(new CommandError(), new CommandNone());
		
		
	}
	
	public void start()
	{
		
	}
	
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
			sendToPeer.run();
		}
		
	}
	
	public class CommandError extends UIControllerCommand
	{
		public CommandError()
		{
			super();
		}
		
		public void execute()
		{
			
		}
	}
	
	public class CommandHelp extends UIControllerCommand
	{
		public CommandHelp()
		{
			super();
		}
		
		public void execute()
		{
			
		}
	}

	public class CommandNone extends UIControllerCommand
	{
		public CommandNone()
		{
			super();
		}
		
		public void execute()
		{
			
		}
	}
}
