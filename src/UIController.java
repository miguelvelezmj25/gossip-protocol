import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author jens3048
 *
 */
public class UIController
{

	private CommandProcessor 			commandProcessor;
	private boolean 					done;
	private IncomingPacketQueue 		incomingPacketsFromPeerQueue;
	private OutgoingPacketQueue 		outgoingPacketsToPeerQueue;
	private PeerController				ourPeerController;
	private InetSocketAddress 			peerAddress;
	private DatagramReceiver 			receiveFromPeer;
	private DatagramSender 				sendToPeer;
	private ScannerHandler				scannerHandler;
	private HashMap<ID,FileRebuilder>	fileRebuilders;
	
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
		this.scannerHandler = new ScannerHandler();
		
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
		this.commandProcessor.register(new CommandGet());

		
		this.fileRebuilders = new HashMap<ID, FileRebuilder>();
	}

	/**
	 *	Do Everything
	 */
	public void start()
	{
		
		ResourceManager.getInstance().loadResourcesFrom(new File("resource/ResourceList.txt"));
		//Start Threads
		receiveFromPeer.startAsThread();
		sendToPeer.startAsThread();
		scannerHandler.startAsThread();
		while(!done)
		{
			if(!incomingPacketsFromPeerQueue.isEmpty())
			{
				byte[] data = incomingPacketsFromPeerQueue.deQueue().getData();
				byte[] id = new byte[ID.getLengthInBytes()];
				ID identity;
				byte[] start = new byte[4];
				byte[] end = new byte[4];
				byte[] other = new byte[data.length - id.length - start.length - end.length];
				FileRebuilder fr;
				
				System.arraycopy(data, 0, id, 0, id.length);
				System.arraycopy(data, id.length, start, 0, start.length);
				System.arraycopy(data, id.length + start.length, end, 0, end.length);
				System.arraycopy(data, id.length + start.length + end.length, other, 0, other.length);
				
				identity = new ID(id);
				
				if(this.fileRebuilders.containsKey(identity))
				{
					fr = this.fileRebuilders.get(identity);
				}
				else
				{
					fr = new FileRebuilder(identity);
					this.fileRebuilders.put(identity, fr);
				}
				fr.rebuild(start, end, other);
			}
		}
		//Finish everything
		ourPeerController.setDoneFlag(true);
		
		Iterator<FileRebuilder> itr = fileRebuilders.values().iterator();
		
		while(itr.hasNext())
		{
			itr.next().setComplete(true);
		}
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
		
		public void sendToPeer(String message)
		{
			byte[] buffer;
			
			//Ask user what they would like to send

			
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
			
//			System.out.println(new String(message.getBytes()));
			
			outgoingPacketsToPeerQueue.enQueue(dp);

//			ID id1;
//			ID id2;
//			UDPMessage udpMessage;
//			TimeToLive ttl;
//			
//			ttl = new TimeToLive(70);
//			
//			id1 = ID.idFactory();
//			id2 = ID.idFactory();
//			
////			udpMessage = new UDPMessage(id1, id2, ttl, this.getCommandName());
////			DatagramPacket dp = udpMessage.getDatagramPacket();
//			
//		
//			
//			String message = "jklsadf"; // TODO do not hardcode a message
//			byte[] buffer = new byte[message.getBytes().length];
//			
//			DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
//			
////			System.out.println("Set address to local host");
//			try {
//				dp.setAddress(InetAddress.getLocalHost());
//			} catch (UnknownHostException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
////			System.out.println("Set port: " + peerAddress.getPort());
//			dp.setPort(peerAddress.getPort());
//			
////			System.out.println("Set packet data");
//			dp.setData(message.getBytes());
//			
//			// TODO we are not using outgoing queue
//			outgoingPacketsToPeerQueue.enQueue(dp);
////			sendToPeer.startAsThread();
			
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
				this.sendToPeer("");
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
			this.sendToPeer("");
		}

	}
	

	public class CommandGet extends UIControllerCommand
	{
		/**
		 * Creates the command
		 */
		public CommandGet()
		{
			super("get", "Search for something");
		}

		public void run()
		{
			this.sendToPeer("");
		}

	}
	
	public class ScannerHandler implements Runnable
	{
		public ScannerHandler()
		{
			
		}
		
		public void run()
		{
			String userCommand;
			UIControllerCommand command;
			Scanner scan = new Scanner(System.in);
			while(!done)
			{
				System.out.print("Type in a command: ");
				if(scan.hasNextLine())
				{
					userCommand = scan.nextLine();
					command = (UIControllerCommand) commandProcessor.getCommand(userCommand.toLowerCase());
					if(command.getCommandName().equals("find") || command.getCommandName().equals("get"))
					{
						System.out.print("Please type what you would like to " + command.getCommandName() + ": ");
						String message = "," + command.getCommandName() + "," + scan.nextLine();
						command.sendToPeer(message);
					}
					else
					{
						command.run();	
					}
					
				}
			}
			scan.close();
		}
		
		public Thread startAsThread() 
		{
			Thread thread;
			
			thread = new Thread(this);
			thread.start();
			
			return thread;
		}
	}
	
	
	public class FileRebuilder
	{
		RandomAccessFile raf;
		AtomicBoolean isComplete;
		ID identity;
		int length;
		int written;
		String mimeType;
		
		public FileRebuilder(ID identity)
		{
			this.identity = identity;
			isComplete = new AtomicBoolean();
			PeerResource rs;
			String filePath;
			File file;
			rs = PeerResourceManager.getInstance().getResourceFromID(identity);
			if(rs != null)
			{
				try 
				{
					this.mimeType = rs.getMimeType();
					filePath = "responses/" + makeValidFileName(rs.getDescription());
					System.out.println(filePath);
					file = new File(filePath);
					file.createNewFile();
					raf = new RandomAccessFile(file,"rws");
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				written = 0;
				length = rs.getLength();
			}
			else
			{
				throw new IllegalArgumentException("File not in PeerResourceManager");
			}
		}

		public synchronized void rebuild(byte[] start, byte[] end, byte[] data) 
		{
			rebuild(ByteBuffer.wrap(start).getInt(), ByteBuffer.wrap(end).getInt(), data);
			
		}

		public synchronized void rebuild(int start, int end, byte[] data)
		{
			if(!isComplete())
			{
				System.out.println("start is: " + start + " and end is " + end);
				try 
				{
					byte[] toWrite;
					toWrite = new byte[end - start];
					System.arraycopy(data, 0, toWrite, 0, toWrite.length);
					
					raf.write(toWrite);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				if(end >= length)
				{
					setComplete(true);
				}
			}

		}
		
		public void setComplete(boolean complete)
		{
			if(!isComplete())
			{
				try 
				{
					raf.close();
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			isComplete.set(complete);

		}
		
		public boolean isComplete()
		{
			return isComplete.get();
		}
		
		
		public String makeValidFileName(String name)
		{
			char[] illegalCharacters = { ' ', ',', '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };
		
			String result;
			
			result = name;
			
			for(int i = 0; i < illegalCharacters.length; i = i + 1)
			{
				for(int j = 0; j < result.length(); j = j + 1)
				{
					if(result.charAt(j) == illegalCharacters[i])
					{
						result = result.substring(0, j) + result.substring(j + 1, result.length());
					}
				}
			}
			result = result + getMimeExt();
			return result;
		}
		
		public String getMimeExt()
		{
			return ".txt";
		}
		//TODO: figure out mime types
	}
}


// TODO implement start, stop, get, find, join, leave
