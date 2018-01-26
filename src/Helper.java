import java.net.DatagramPacket;
import java.util.Arrays;

public abstract class Helper {
	
	protected final static byte zero = 0;
	protected final static byte one = 1;
	protected final static byte two = 2;
	protected final static byte three = 3;
	protected final static byte four = 4;
	
	protected final static byte[] readReply = new byte[] {zero, three, zero, one};
	protected final static byte[] writeReply = new byte[] {zero, four, zero, zero};
	
	protected final static int intermediatePort = 23;
	protected final static int serverPort =  69;



	
	
	public void printPacketData(DatagramPacket packet, byte[] byteArray, boolean receieve, String packetinfo) {
		String SendOrReceieve = receieve ? "Received" : "Sending";
		String ToOrFrom = receieve ? "From" : "To";
		System.out.println("Packet Information: ");
		System.out.println(packetinfo+": " + SendOrReceieve  + " packet:");
	    System.out.println(ToOrFrom +" host: " + packet.getAddress());
	    System.out.println("Destination host port: " + packet.getPort());
	    int len = packet.getLength();
	    System.out.println("Length: " + len);
	    System.out.print("Containing: ");
	    System.out.println(new String(packet.getData(),0,len)); 
	    System.out.println("Byte format: " + Arrays.toString(byteArray) + "\n" + "=======================");
	}
	

}
