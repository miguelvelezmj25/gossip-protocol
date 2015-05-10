import java.io.IOException;
import java.net.*;

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
//		System.out.println("\nDatagramReceiver Action");
		try 
		{
			DatagramPacket p;
			
			p = new DatagramPacket(new byte[super.getPacketSize()], super.getPacketSize());
		
//			System.out.println("DatagramReceiver Listening on port: " + super.datagramSocket.getPort());
//			System.out.println("DatagramReceiver Listening is port closed: " + super.datagramSocket.isClosed());
//			System.out.println("DatagramReceiver Listening is port connected: " + super.datagramSocket.isConnected() + "\n");
			datagramSocket.receive(p);
//			System.out.println("\nDatagramReceiver Receiving: " + new String(p.getData()));
			
			
//			System.out.println("\nDatagramReceiver Enqueue");
			queue.enQueue(new DatagramPacket(p.getData(), super.getPacketSize()));
		} 
		catch (IOException e) 
		{
			// TODO WHAT THE HECK
			System.out.println(e.getMessage());
//			e.printStackTrace();
		} 
				
	}

}
