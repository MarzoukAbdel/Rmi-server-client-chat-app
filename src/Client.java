import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Client extends UnicastRemoteObject implements RemoteInterface {
    private RemoteInterface server;
    private JFrame frame;
    private JTextArea textArea;
    private JTextField inputField;
    private JButton sendButton;

    protected Client() throws RemoteException {
        super();
        initializeGUI();
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        // Not used in the client
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        System.out.println(message);
        textArea.append(message + "\n");
    }

    @Override
    public void setClient(RemoteInterface client) throws RemoteException {
        // Not used in the client
    }

    private void initializeGUI() {
        frame = new JFrame("Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 400);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        inputField = new JTextField();
        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = inputField.getText();
                if (!message.isEmpty()) {
                    try {
                        server.receiveMessage("Client : " + message);
                        inputField.setText("");
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputField, BorderLayout.PAGE_END);
        frame.add(sendButton, BorderLayout.LINE_END);

        frame.setVisible(true);
    }

    public void connectToServer() {
        try {
            server = (RemoteInterface) Naming.lookup("rmi://localhost/ChatServer");
            server.setClient(this);

            System.out.println("Connected to the server");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.connectToServer();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
