import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class SendingReceivingTest {

	private static DatagramSocket socket;
	
	public static void main(String[] args) {
		try {
			socket = new DatagramSocket(12345);
			
			SendingReceivingTest.listen();
		
			SendingReceivingTest.send();
			
			System.out.println("Number of threads: " + Thread.activeCount());

		} catch (SocketException e) {
			e.printStackTrace();
		}
//		finally {
//			socket.close();
//		}
		
	}
	
	public static void listen() {
		try {
			IncomingPacketQueue inQueue = new IncomingPacketQueue();	
			
			System.out.println("Create datagramReceiver");
			DatagramReceiver dataReceiver = new DatagramReceiver(SendingReceivingTest.socket, inQueue, 512);
				
			System.out.println("Start dataReceiver as thread\n");
			dataReceiver.startAsThread();
			
//			Thread.sleep(5000);
//			System.out.println("\nListening Stopping");			
//			dataReceiver.stop();
//			
//			System.out.println("Sending End");
			
			} catch (SocketException e) {
				e.printStackTrace();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
			}
			
	}
	
	public static void send() {
		try {
			OutgoingPacketQueue outQueue = new OutgoingPacketQueue();
			
			
			String message = "hello";
			byte[] buffer = new byte[message.getBytes().length];
			
			for(int i = 0; i < 5; i++) {				
				System.out.println("\nCreate packet");
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				
				System.out.println("Set address to local host");
				packet.setAddress(InetAddress.getLocalHost());
				
				System.out.println("Set port 12345");
				packet.setPort(12345);
				
				System.out.println("Set packet data");
				packet.setData((message + i).getBytes());

				System.out.println("Put packet in outgoing queue");
				outQueue.enQueue(packet);
			}
						
			System.out.println("Create datagramSender");
			DatagramSender dataSender = new DatagramSender(SendingReceivingTest.socket, outQueue, buffer.length);
			
			System.out.println("Start dataSender as thread\n");
			dataSender.startAsThread();
			
//			Thread.sleep(1000);
//			System.out.println("\nSending Stopping");
//			dataSender.stop();
//			
//			System.out.println("Sending End");
			
			} catch (UnknownHostException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
//			} catch (InterruptedException e) {
//
//				e.printStackTrace();
			}
	}
	

}
