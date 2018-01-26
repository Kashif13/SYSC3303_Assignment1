import java.io.*;
import java.net.*;
import java.util.Arrays;


public class Server extends Helper {

   DatagramPacket sendPacket, receivePacket;
   DatagramSocket sendSocket, receiveSocket;

   public Server()
   {
      try {
         // Construct a datagram socket and bind it to  
         // port 69 on the local host machine. This socket will be used to
    	  	receiveSocket = new DatagramSocket(serverPort);
    	  	sendSocket = new DatagramSocket();
      } catch (SocketException se) {   // Can't create the socket.
         se.printStackTrace();
         System.exit(1);
      }
   }

   public void receiveAndSend() throws Exception
   {
	   
	   while(true) {
		   
		   /** Construct a DatagramPacket for receiving packets up to 100 bytes long (the length of the byte array). **/
	      byte receivedMsg[] = new byte[100];
	      receivePacket = new DatagramPacket(receivedMsg, receivedMsg.length);
	      System.out.println("Server: Waiting for Packet.\n");
	      
	      /** Block until a datagram packet is received from host. **/
	      try {        
	         System.out.println("Waiting for packet from host..."); // so we know we're waiting
	         receiveSocket.receive(receivePacket);
	         System.out.println("Intermediate: Packet Received from server. \n");
	      } catch (IOException e) {
	         System.out.print("IO Exception: likely:");
	         System.out.println("Receive Socket Timed Out.\n" + e);
	         e.printStackTrace();
	         System.exit(1);
	      }
	      
	      //Data received
	      byte[] dataReceived = receivePacket.getData();
	      dataReceived = Arrays.copyOfRange(dataReceived, 0, receivePacket.getLength());
	      
	      /** Validate data received **/
	      if(!validateByteArray(dataReceived)) {
	    	  	System.out.println("Invalid Request . . .");
	    	  	throw new Exception();
	      }
	      
	      /** Display packet received information **/
	      printPacketData(receivePacket, dataReceived, true, "Server");
	      
	      // Slow things down (wait 5 seconds)
	      try {
	          Thread.sleep(5000);
	      } 
	      catch (InterruptedException e ) {
	          e.printStackTrace();
	          System.exit(1);
	      }
	      
	      System.out.print("READ OR WRITE?: "+ dataReceived[1]);
	      /** Valid Request. Send back 0 3 0 1 if Valid Read. 0 4 0 0 if Valid Write. **/
	      if(dataReceived[1] == one) { //Read	    	 	    
	    	  	sendPacket = new DatagramPacket(readReply, readReply.length,  receivePacket.getAddress(), receivePacket.getPort());	  
	      }
	      if(dataReceived[1] == two) { //Write
	    	  	sendPacket = new DatagramPacket(writeReply, writeReply.length,  receivePacket.getAddress(), receivePacket.getPort());	  
	      }
	      
	      /** print new packet's information **/
	      byte[] packetData = Arrays.copyOfRange(sendPacket.getData(), 0, sendPacket.getLength());
	      printPacketData(sendPacket, packetData, false, "Server");
	      
	      /** Send packet to client (intermediate host) **/
	      sendSocket.send(sendPacket);
	      
	      System.out.print("Server: Packet Sent.");
	      
	      // We're finished, so close the sockets.
	      /*receiveSocket.close();
	      sendSocket.close();*/
	   }
   }
   
   //Validate byte array received for format of 0 1 or 0 2, text, 0, text, 0, and nothing else after.
   public boolean validateByteArray(byte[] msgReceived)
   {
   	
   	if(msgReceived[0] == 0) // First 0
   	{
   		if (msgReceived[1] == one || msgReceived[1] == two) // Second as 1 or 2 (read or write)
   		{
   			if(msgReceived[2] >= 33 && msgReceived[2] <= 126) //Check for some text
   			{

   				int i = 0;
   				for (i = 3; i < msgReceived.length-2; i++)
   				{
   					if(msgReceived[i] == zero) {	// Another 0
   						break;
   					}
   				}	
   				
   				if(msgReceived[i+1] >= 33 && msgReceived[i+1] <= 126) //Check for some text
   						{
   							//check last zero
   							for (int j = i+2;i<=msgReceived.length;j++)
   							{
   							if (msgReceived[j] == zero) { //Check for final zero
   								if(j+1 == msgReceived.length) { //Should be at the end of the byteArray, else invalid
   								return true;
   								}	
   							 }	
   							}
   						}
   					}
   			}
   		}	
   	return false;
   }

   public static void main(String args[]) throws Exception
   {
	   Server s = new Server();
	   s.receiveAndSend();
   }
}
