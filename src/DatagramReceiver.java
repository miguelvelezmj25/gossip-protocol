import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class DatagramReceiver extends DatagramSenderReceiver {
	/**
	 * 
	 * @throws SocketException 
	 *  
	 */
	public DatagramReceiver(DatagramSocket datagramSocket, IncomingPacketQueue queue, int packetSize) throws SocketException 
	{
		// TODO ??
		super(datagramSocket, queue, packetSize);
	}

	@Override
	/**
	 * Listens at the datagram socket until a packet is received. When the a packet is received, the
	 * method will immediately create a new datagram packet which will be placed in the passed queue. 
	 * Once thats done, the thread sleeps for one hundred milliseconds. 
	 */
	public void action(DatagramSocket datagramSocket, SynchronizedPacketQueue queue)
	{
		DatagramPacket p;
		
		p = new DatagramPacket(new byte[super.getPacketSize()], super.getPacketSize());
		while(!super.isStopped())
		{
			try 
			{
				datagramSocket.receive(p);
				queue.enQueue(new DatagramPacket(p.getData(), super.getPacketSize()));
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
