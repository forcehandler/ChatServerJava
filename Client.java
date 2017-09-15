// Java implementation for multithreaded chat client
// Save file as Client.java
 
import java.io.*;
import java.net.*;
import java.util.Scanner;
 
public class Client 
{
    final static int ServerPort = 1234;
 
    public static void main(String args[]) throws UnknownHostException, IOException 
    {
        Scanner scn = new Scanner(System.in);
         
        // getting localhost ip
        InetAddress ip = InetAddress.getByName("localhost");
         
        // establish the connection
        Socket s = new Socket(ip, ServerPort);
         
        // obtaining input and out streams
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
 
        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable() 
        {
            @Override
            public void run() {
                while (true) {
 
 										try {
                    // read the message to deliver.
                    String msg = scn.nextLine();
                    
                    
                    int count = 1; 
										System.out.print(String.format("\033[%dA",count)); // Move up
										System.out.print("\033[2K"); //Erase line content
                     
                    
                        // write on the output stream
                        dos.writeUTF(msg);
                        
                        if(msg.equals("logout")){
                    		
                    			break;
                    	}
                    } catch (IOException e) {
                        
                        try{
								 						dis.close();
														dos.close();
														s.close();
								 				}catch(Exception e1){
								 					e1.printStackTrace();
								 				}
								 				break;
                    }
                }
            }
        });
         
        // readMessage thread
        Thread readMessage = new Thread(new Runnable() 
        {
            @Override
            public void run() {
 
                while (true) {
                    try {
                        // read the message sent to this client
                        String msg = dis.readUTF();
                        System.out.println(msg);
                    } catch (IOException e) {
                        
                        try{
								 						dis.close();
														dos.close();
														s.close();
								 				}catch(Exception e1){
								 					e1.printStackTrace();
								 				}
								 				break;
                    }
                }
            }
        });
 
        sendMessage.start();
        readMessage.start();
 
 				
    }
}
