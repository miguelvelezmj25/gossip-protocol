import java.io.IOException;
import java.net.*;

public class DatagramSender extends DatagramSenderReceiver {
	/**
	 * 
	 * @param inetSocketAddress
	 * @param queue
	 * @param packetSize
	 * @throws SocketException 
	 */
	public DatagramSender(DatagramSocket datagramSocket, OutgoingPacketQueue queue, int packetSize) throws SocketException 
	{
		// TODO ??
		
		super(datagramSocket, queue, packetSize);
	}

	@Override
	/**
	 * Checks to see if the passed queue is empty. If not, removes and sends the first packet
	 * in the queue. Then sleeps for 100 milliseconds.
	 */
	public void action(DatagramSocket datagramSocket, SynchronizedPacketQueue queue)
	{
//		System.out.println("\nDatagramSender Action");
		try 
		{
			if(!queue.isEmpty())
			{
//				System.out.println("DatagramSender Sending packet: " + new String(queue.peek().getData()));
				datagramSocket.send(queue.deQueue());
			}
//			else {
//				System.out.println("DatagramSender queue is empty");
//			}
			
		} 
		catch (IOException e) 
		{
//			System.err.println("Shit man, IOException in the datagramSocket receive method. No idea what would cause this."); 
			e.printStackTrace();
		} 

	}

}
