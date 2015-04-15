import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class DatagramSender extends DatagramSenderReceiver {
	/**
	 * 
	 * @param inetSocketAddress
	 * @param queue
	 * @param packetSize
	 * @throws SocketException 
	 */
	public DatagramSender(InetSocketAddress inetSocketAddress, OutgoingPacketQueue queue, int packetSize) throws SocketException 
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
