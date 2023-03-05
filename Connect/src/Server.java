import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class ServerSetup implements User 
{

    private ChatFrame serverChatFrame;
    private InputStream input;//receives message
    private OutputStream output;//sends message 
    private Socket socket;//creates a special connection between server and client
    private ServerSocket serverSocket;//acts as operator who waits for client requests and then performs alloted tasks
    private String client = "person 2"; //change client name here

    public ServerSetup() 
    {
        serverChatFrame = new ChatFrame(this, "Server", client, false); //if true, window opens in the centre
    }

    public void run() throws IOException 
    {
        serverSocket = new ServerSocket(9999);//here 9999 is port number
        System.out.println("SERVER:- Started listening to 9999");

        serverChatFrame.setActive(false);//during start up, "Active Now" is not displayed
        System.out.println("SERVER:- Waiting for client");//in console
        socket = serverSocket.accept();//server socket accepts to receive and send messages from server to client
        //server socket waits for client's socket request, and then it establishes connection between server and client
        input = socket.getInputStream();//received message from other user
        output = socket.getOutputStream();//sent message from YOU

        System.out.println("SERVER:- Connected to Client!");
        serverChatFrame.resetChat();//clears chat panel
        serverChatFrame.setActive(true);//when client is active, it shows "Active Now" under their user name
    }

    @Override
    public void sendMessage() throws IOException 
    {
        String send = serverChatFrame.getMessage();
        if (send != null && !send.equals("")) 
        {
            output.write(send.getBytes());//the message in your message box is taken as your outputstream
            serverChatFrame.addMessage("YOU", send);//in chatpanel, your message is delivered by"YOU"
            System.out.println("SERVER:- Message sent to client: " + send);//in console
        }
    }

    @Override
    public int receiveMessage() throws IOException 
    {
        byte request[] = new byte[1000];
        int status = input.read(request);
        String received = new String(request).trim();
        if (received != null && !received.equals("")) 
        {
            serverChatFrame.addMessage(client, received);
            System.out.println("SERVER:- Received message from client: " + received);
        }
        return status;
    }

    public void reset() throws IOException  
    {
        serverChatFrame.setActive(false); 
        serverSocket.close(); // any server socket accepted socket is closed, so communication is halted
        this.run(); //new server socket is created, and it begins to wait for client socket request
    }
}

public class Server 
{
    public static void main(String[] args) throws IOException 
    {
        ServerSetup server = new ServerSetup();
        server.run();
        while (true) 
        {
            int status = server.receiveMessage();
            if (status == -1) 
            {
                server.reset();
            }
        }
    }
}
