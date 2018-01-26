import java.io.*;
import java.net.*;
import java.util.Arrays;

public class IntermediateHost extends Helper {

   DatagramPacket sendPacketClient, receivePacketClient, sendPacketServer, receievePacketServer;
   DatagramSocket sendSocket, receiveSocket, sendReceiveSocket;
 
   public IntermediateHost()
   {
      try {
         // Construct a datagram socket and bind it to any available 
         // port on the local host machine. This socket will be used to
    	  	sendReceiveSocket = new DatagramSocket();
    	  	
    	  	sendSocket = new DatagramSocket();

         // Construct a datagram socket and bind it to port 23 
         // on the local host machine. This socket will be used to
         receiveSocket = new DatagramSocket(intermediatePort);
         
         
      } 
      catch (SocketException se) {
         se.printStackTrace();
         System.exit(1);
      } 
   }

   public void receiveAndSend() throws Exception
   {
	   
	   while(true) {
		   
	      /** Construct a DatagramPacket for receiving packets up to 100 bytes long (the length of the byte array). **/
	      byte rdata[] = new byte[100];
	      receivePacketClient = new DatagramPacket(rdata, rdata.length);
	      System.out.println("Intermediate Host: Waiting for Packet.\n");
	      
	      /** Block until a datagram packet is received from server. **/
	      try {        
	         System.out.println("Waiting..."); // so we know we're waiting
	         receiveSocket.receive(receivePacketClient);
	         System.out.println("Intermediate: Packet Received. \n");
	      } catch (IOException e) {
	         System.out.print("IO Exception: likely:");
	         System.out.println("Receive Socket Timed Out.\n" + e);
	         e.printStackTrace();
	         System.exit(1);
	      }
	      
	      /**Process the received datagram in byte and string format**/
	      byte[] receiveData = Arrays.copyOfRange(receivePacketClient.getData(), 0, receivePacketClient.getLength());
	      printPacketData(receivePacketClient, receiveData, true, "Server (Intermediate Host)");
	      
	      //Store Client Port and Client Address
	      int cPort = receivePacketClient.getPort();
	      InetAddress cAddress = receivePacketClient.getAddress();
	      
	      // Slow things down (wait 5 seconds)
	      try {
	          Thread.sleep(5000);
	      } catch (InterruptedException e ) {
	          e.printStackTrace();
	          System.exit(1);
	      }
	      
	      /** Create a new datagram packet containing the string received from the client, to port 69. **/
	      sendPacketServer = new DatagramPacket(rdata, receivePacketClient.getLength(), receivePacketClient.getAddress(), serverPort);
	      
	      // Print packet sent in byte and string format
	      byte[] sendServerData = Arrays.copyOfRange(sendPacketServer.getData(), 0, sendPacketServer.getLength());
	      printPacketData(sendPacketServer, sendServerData, false, "Server (Intermediate Host)");
	      
	      
	      /** Send the datagram packet to the server via the send/receive socket.  **/
	      try {
	    	  	sendReceiveSocket.send(sendPacketServer);
	      } 
	      catch (IOException e) {
	         e.printStackTrace();
	         System.exit(1);
	      }

	      System.out.println("Intermediate Host: packet sent to Server");
	      

	      
	      /** Construct a DatagramPacket for sending packets up to 100 bytes long (the length of the byte array). **/
	      byte sdata[] = new byte[100];
	      receievePacketServer = new DatagramPacket(sdata, sdata.length);
	      System.out.println("Intermediate Host: Waiting for Packet.\n");
	      
	      // Block until a datagram packet is received from server.
	      try {        
	         System.out.println("Waiting..."); // so we know we're waiting
	         sendReceiveSocket.receive(receievePacketServer);
	         System.out.println("Intermediate: Packet Received from Client. \n");
	      } catch (IOException e) {
	         System.out.print("IO Exception: likely:");
	         System.out.println("Receive Socket Timed Out.\n" + e);
	         e.printStackTrace();
	         System.exit(1);
	      }
	      
	      /** Process the receieved datagram in byte and string format **/
	      byte[] receieveData = Arrays.copyOfRange(receievePacketServer.getData(), 0, receievePacketServer.getLength());
	      printPacketData(receievePacketServer, receieveData, true, "Client (Intermediate Host)");
	      
	      // Slow things down (wait 5 seconds)
	      try {
	          Thread.sleep(5000);
	      } catch (InterruptedException e ) {
	          e.printStackTrace();
	          System.exit(1);
	      }
	      
	      /** Create a new datagram packet received from server forwarded to server using stored Client Port and Address. **/
	      sendPacketClient = new DatagramPacket(sdata, receievePacketServer.getLength(), cAddress, cPort);
	      
	      // Print packet sent in byte and string format
	      byte[] sendClientData = Arrays.copyOfRange(sendPacketClient.getData(), 0, sendPacketClient.getLength());
	      printPacketData(sendPacketClient, sendClientData, false, "Client (Intermediate Host)");
	      
	      
	      /** Send the datagram packet to the client via the send socket received from the server.  **/
	      try {
	         sendSocket.send(sendPacketClient);
	      } catch (IOException e) {
	         e.printStackTrace();
	         System.exit(1);
	      }

	      System.out.println("Intermediate Host: Packet Sent to Client");
	      
	   }

   }
   
   public static void main( String args[] ) throws Exception
   {
      IntermediateHost ih = new IntermediateHost();
      ih.receiveAndSend();
   }
}

