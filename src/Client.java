import java.io.*;
import java.net.*;
import java.util.Arrays;


public class Client extends Helper {

   private DatagramPacket sendPacket, receivePacket;
   private DatagramSocket sendReceiveSocket;
   private String fileName, modeName;
   private byte[] fileNameByte, modeNameByte;

   public Client()
   {
      try {
         // Construct a datagram socket and bind it to any available 
         // port on the local host machine. This socket will be used to
         sendReceiveSocket = new DatagramSocket();
      } catch (SocketException se) {   // Can't create the socket.
         se.printStackTrace();
         System.exit(1);
      }
   }

   public void sendAndReceive() throws Exception
   {
	   
	   for(int i=1; i<=11; i++) {
		   
		   System.out.println("Client: Cycle #" +i + " . . .");
		   
		   
		   
		   byte msg[] = new byte[100];
		   int currentByteIndex = 0;
		   
		   /** Adding First 0 Byte **/
		   msg[currentByteIndex] = zero;
		   currentByteIndex++;
		   
		   /** Add Second Byte as 1 if Read Request and 2 if Write Request. Alternates each cycle **/
		   if( i<=10 && i%2 == 1) { //Read Request
			   System.out.println("Client: Read Request");
			   msg[currentByteIndex] = one;
			   currentByteIndex++;
		   }
		   
		   else { //Write Request
			   System.out.println("Client: Write Request");
			   msg[currentByteIndex] = two;
			   currentByteIndex++;
		   }
		   
		   /** Add a filename 'test.txt' as a byte to the message **/
		   fileName = "test.txt";
		   fileNameByte = fileName.getBytes();
		   System.arraycopy(fileNameByte, 0, msg, currentByteIndex, fileNameByte.length);
		   currentByteIndex = currentByteIndex + fileNameByte.length;
		   
		   /** Add Another 0 Byte **/
		   msg[currentByteIndex] = zero;
		   currentByteIndex++;
		   
		   /** Add mode "netascii" into the message **/
		   modeName = "netascii";
		   modeNameByte = modeName.getBytes();
		   System.arraycopy(modeNameByte, 0, msg, currentByteIndex, modeNameByte.length);
		   currentByteIndex = currentByteIndex + modeNameByte.length;
		   
		   /** Add Another 0 Byte **/
		   msg[currentByteIndex] = zero;
		   currentByteIndex++;
		   
		   /** Add Another 1 Byte on 11th cycle to turn this request invalid **/
		   if( i == 11) {
			   msg[currentByteIndex] = 1;
			   currentByteIndex++;
		   }
		   
		   	msg = Arrays.copyOfRange(msg, 0, currentByteIndex);

		   
		   /** Construct a datagram packet that is to be sent to a specified port on an intermediate host port 23**/
		   try {
			   sendPacket = new DatagramPacket(msg, msg.length,
			   InetAddress.getLocalHost(), intermediatePort);
		   } 
		   catch (UnknownHostException e) {
			   e.printStackTrace();
    	  		   System.exit(1);
		   }
	      
		   /** Printing Packet sent's information in Byte and String formats **/
		   printPacketData(sendPacket, msg, false, "Client");
		   
		   /** Send the datagram packet to the Host via the send/receive socket. **/
		   try {
			   sendReceiveSocket.send(sendPacket);
		   } 
		   catch (IOException e) {
			   e.printStackTrace();
			   System.exit(1);
		   }		   
		   System.out.println("Client: Packet sent.\n");
		   
		   
	      /*** Construct a DatagramPacket for receiving packets up 
	       to 100 bytes long (the length of the byte array). **/
	      byte data[] = new byte[100];
	      receivePacket = new DatagramPacket(data, data.length);

	      try {
	         // Block until a datagram is received via sendReceiveSocket.  
	    	  	 System.out.print("Client. Waiting for Packet . . . ");
	         sendReceiveSocket.receive(receivePacket);
	         System.out.println("Client: Package Received. \n");
	      } catch(IOException e) {
	         e.printStackTrace();
	         System.exit(1);
	      }

	      /** Printing Packet recieved's information in Byte and String formats **/
	      byte[] receiveData = Arrays.copyOfRange(receivePacket.getData(), 0, receivePacket.getLength());
	      printPacketData(receivePacket, receiveData, true, "Client");
	      
	      /** End on 11th Cycle **/
		   if(i == 11 ) {
			   System.out.println("---- END ---");
			   System.exit(1);
		   }
	   }

      // We're finished, so close the socket.
      sendReceiveSocket.close();
   }

   public static void main(String args[]) throws Exception
   {
	   Client c = new Client();
      c.sendAndReceive();
   }
}
