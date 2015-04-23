import java.io.IOException;
import java.net.DatagramPacket;
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
		// TODO ??
		super(inetSocketAddress, queue, packetSize);
	}

	@Override
	/**
	 * Checks to see if the passed queue is empty. If not, removes and sends the first packet
	 * in the queue. Then sleeps for 100 milliseconds.
	 */
	public void action(DatagramSocket datagramSocket, SynchronizedLinkedListQueue queue)
	{
		while(!super.isStopped())
		{
			try 
			{
				if(!queue.isEmpty())
				{
					datagramSocket.send((DatagramPacket)queue.deQueue());
				}
				Thread.sleep(100);
			} 
			catch (IOException e) 
			{
				System.err.println("Shit man, IOException in the datagramSocket receive method. No idea what would cause this."); 
				e.printStackTrace();
			} 
			catch (InterruptedException e) 
			{
				System.err.println("Shit man, your sleep method got interrupted.");
				e.printStackTrace();
			}
			
		}

	}

}
