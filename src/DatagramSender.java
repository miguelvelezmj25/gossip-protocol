import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class DatagramSender extends DatagramSenderReceiver {
	/**
	 * 
	 * Miguel Velez
	 * March 9, 2015
	 * 
	 * This class TODO
	 * 		
	 * Constructors:
	 * 		public DatagramSender(InetSocketAddress inetSocketAddress, OutgoingPacketQueue queue, int packetSize)
	 * 			TODO.
	 * 	 
	 * Methods:
	 *		public void action(DatagramSocket datagramSocket, SynchronizedLinkedListQueue queue) 
	 *			TODO.
	 *       
	 * Modification History:
	 * 		March 9, 2015
	 * 			Original version
	 *  
	 */
	
	public DatagramSender(InetSocketAddress inetSocketAddress, OutgoingPacketQueue queue, int packetSize) 
	{
		// TODO
		super(inetSocketAddress, queue, packetSize);
	}

	@Override
	public void action(DatagramSocket datagramSocket, SynchronizedLinkedListQueue queue) 
	{
		// TODO 

	}

}
