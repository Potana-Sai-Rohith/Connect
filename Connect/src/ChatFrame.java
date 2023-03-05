import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class ChatFrame extends JFrame implements ActionListener // A window with action implementation
{

    private JPanel chatPanel;//chat panel is created
    private JPanel contactPanel;//contact panel is created

    private JLabel talkingTo;
    private JLabel activeNow;

    private JButton sendButton;//action button

    private JScrollPane textPane;//text box to display previous messages
    private JScrollPane messagePane;//message box to show your input messages

    private JTextArea textArea;
    private JTextArea messageBox;

    private final Font bf = new Font(Font.SANS_SERIF, Font.BOLD, 17);
    private final Font f = new Font(Font.SANS_SERIF, Font.ITALIC, 10);

    private User user;

    private final static int WIDTH = 500;//you can change window width here
    private final static int HEIGHT = 800;//you can change window height here

    public ChatFrame(User user, String from, String to, boolean centre) //parameterized constructor
    {
        this.user = user;

        chatPanel = new JPanel();//chat panel stores chat history
        chatPanel.setLayout(null);
        chatPanel.setBackground(new Color(233, 219, 232));//chat history background color is assigned
        chatPanel.setBounds(1, 50, WIDTH, HEIGHT - 50);
        chatPanel.setVisible(true);//if not, everything below user name panel is blank

        contactPanel = new JPanel();//contact panel shows the details of the user you are chatting with
        contactPanel.setLayout(null);//no designs, just flat
        contactPanel.setBackground(new Color(137, 41, 133));
        contactPanel.setBounds(1, 1, WIDTH, 50);
        contactPanel.setVisible(true);// if not, everything is blank

        talkingTo = new JLabel(to);
        talkingTo.setFont(bf);
        talkingTo.setForeground(Color.WHITE);//shows color of user name text
        talkingTo.setBounds(10, 1, 200, 40);
        contactPanel.add(talkingTo);//user name of other entity is displayed on your window

        activeNow = new JLabel("Active Now");//active now is displayed
        activeNow.setFont(f);
        activeNow.setForeground(Color.WHITE);
        activeNow.setBounds(10, 35, 100, 10);
        contactPanel.add(activeNow);//added below user name of other entity
        activeNow.setVisible(false);//"active now" is not visible at start up, and shows up after client is also active

        textArea = new JTextArea(5, 20);//contructs a new empty text area with 5 rows and 20 columns
        textArea.setBounds(1, 1, WIDTH, HEIGHT - 200);//describes size of text area
        textArea.setBackground(new Color(233, 219, 232));//describes color of text area
        textArea.setText("");//it clears text area upon start up
        textArea.setEditable(false);//read only
        textPane = new JScrollPane(textArea);//allows the chat panel to naturally scroll horizontally for long messages
        textPane.setBounds(1, 1, WIDTH, HEIGHT - 200);//describes size of text panel
        textPane.setBackground(new Color(233, 219, 232));//describes color of text pane(here its same as text area)
        chatPanel.add(textPane);//text panel has been added onto the chat panel

        messageBox = new JTextArea(5, 20);//contructs a new empty text area with 5 rows and 20 columns
        messageBox.setText("");//it clears message box upon start up
        messageBox.setBounds(1, HEIGHT - 200, WIDTH - 50, 200);
        messagePane = new JScrollPane(messageBox);//message pane is added onto message box and 
                                                  //scrolls horizontally if your input message to long
        messagePane.setBounds(1, HEIGHT - 200, WIDTH - 50, 200);
        chatPanel.add(messagePane);//message pane is added into chat panel

        sendButton = new JButton("Send");
        sendButton.setOpaque(true);
        sendButton.setBounds(WIDTH - 50, HEIGHT - 200, 50, 20);
        sendButton.addActionListener(this);//action is added onto button
        chatPanel.add(sendButton);//this button is added onto chat panel

        setTitle(from);//window title(client or server)
        setSize(WIDTH, HEIGHT);//window size
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//x button to close window

        add(chatPanel);//add to Jframe
        add(contactPanel);//add to Jframe

        if (centre) 
        {
            this.setLocationRelativeTo(null);//window can appear in any side of the computer screen
        }
        this.setVisible(true);//window will be shown
    }

    public void setActive(boolean active) 
    {
        activeNow.setVisible(active);//active now is shown when client is active
    }

    public String getMessage() 
    {
        return messageBox.getText();
    }

    public void addMessage(String from, String message) 
    {
        textArea.append(from + ": " + message + "\n");//text area displays the user and his message
    }

    public void resetChat() 
    {
        textArea.setText("");//clears chat history
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() == sendButton) 
        {
            try 
            {
                user.sendMessage();
            }
            catch (IOException e1) 
            {
                e1.printStackTrace();//displays the entire error
            }
            messageBox.setText("");//message box is cleared after the message is sent
        }
    }

}