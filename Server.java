// Java implementation of  Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java
 
import java.io.*;
import java.util.*;
import java.net.*;
 
// Server class
public class Server 
{
 
    // Vector to store active clients
    static Vector<ClientHandler> ar = new Vector<>();
     
    
 
    public static void main(String[] args) throws IOException 
    {
        
        ServerSocket ss = new ServerSocket(1234);
         
        Socket s;
         
        Scanner sc = new Scanner(System.in);
        
        while (true) 
        {           
            s = ss.accept();
 
            System.out.println("New client request received : " + s);
             
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                        
            ClientHandler mtch = new ClientHandler(s, dis, dos);
 
            Thread t = new Thread(mtch);                    
             
            ar.add(mtch);
            
            t.start();
 
          	//String s = sc.nextLine();
          	//if(s.equals("close")){
          	//	break;
          	//}
 
        }
    }
}
 
// ClientHandler class
class ClientHandler implements Runnable 
{
    Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;
     
    // constructor
    public ClientHandler(Socket s,
                            DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = "anonymous";
        this.s = s;
        this.isloggedin=true;
    }
 
    @Override
    public void run() {
 
 				String userId = "";
 				String received;
 				
 				try{
 								dos.writeUTF("Welcome to the amazing chat app!!");
            		dos.writeUTF("Enter your preferred userId");
            		userId = dis.readUTF();
            		dos.writeUTF("Welcome " + userId);
            		this.name = userId;
            		System.out.println(userId + " connected");
 
 				}
 				catch(IOException e){
 					e.printStackTrace();
 				}
        
        while (true) 
        {
            try
            {
            		// receive the string
                received = dis.readUTF();
                 
                System.out.println(received);
                 
                if(received.equals("logout")){
                    this.isloggedin=false;
                    for (ClientHandler mc : Server.ar) 
                		{
		                  if(mc.name != this.name && mc.isloggedin){
		                  	mc.dos.writeUTF("\033[3m*user " + userId + " left the chat!\033[0m");
		                  }
                		}
                    this.s.close();
                    break;
                }
                 
                
                String MsgToSend = received;
                
                System.out.println("<" + userId + "> " + MsgToSend);
               
                for (ClientHandler mc : Server.ar) 
                {
                    if(mc.name != this.name && mc.isloggedin){
                    	mc.dos.writeUTF("<" + userId + "> " + MsgToSend);
                    }
                }
                this.dos.writeUTF("<me> " + MsgToSend);
            } catch (IOException e) {
                 
                e.printStackTrace();
            }
             
        }
        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();
             
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
