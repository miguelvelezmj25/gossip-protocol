import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class DatagramReceiver extends DatagramSenderReceiver {
	/**
	 * 
	 * @throws SocketException 
	 *  
	 */
	public DatagramReceiver(InetSocketAddress inetSocketAddress, IncomingPacketQueue queue, int packetSize) throws SocketException 
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
