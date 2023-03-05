import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class ClientSetup implements User {
    
    private OutputStream output;
    private InputStream input; 
    private ChatFrame clientChatFrame;
    private String server = "person 1";//change name here
    
    public ClientSetup() {
        clientChatFrame = new ChatFrame(this, "Client", server, true);
    }

    public void run() throws IOException {
            Socket socket = new Socket("127.0.0.1", 9999);//127.0.0.1 is local host where server socket is running,9999 is port number of server
            System.out.println("CLIENT:- Connected to Server!");//in console

            input = socket.getInputStream();//receive message from server
            output = socket.getOutputStream();//send message to server
    }

    @Override
    public void sendMessage() throws IOException //called in chatframe
    {
        String send = clientChatFrame.getMessage();
        if (send != null && !send.equals("")) {
            output.write(send.getBytes());
            clientChatFrame.addMessage("YOU", send);
            System.out.println("CLIENT:- Message sent to Server: " + send);
        }
    }

    @Override
    public int receiveMessage() throws IOException
    {
        byte[] response = new byte[1000];
        int status = input.read(response);
        String received = new String(response).trim();

        if (received != null && !received.equals("")) 
        {
            clientChatFrame.addMessage(server, received);
            System.out.println("CLIENT:- Received message from server: " + received);
        }
        return status;
    }

}

public class Client 
{    
    public static void main(String[] args) throws IOException 
    {
        ClientSetup client = new ClientSetup();
        client.run();

        while (true) {
            int status = client.receiveMessage();
            if (status == -1) //message reads end of file
            {
                System.exit(0);//client program is terminated
            }
        }
    }
}