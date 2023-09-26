import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Server extends UnicastRemoteObject implements RemoteInterface {
    private RemoteInterface client;
    private JFrame frame;
    private JTextArea textArea;
    private JTextField inputField;
    private JButton sendButton;

    protected Server() throws RemoteException {
        super();
        initializeGUI();
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        System.out.println("Client  : " + message);
        client.receiveMessage("Server : " + message);
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        System.out.println(message);
        textArea.append(message + "\n");
    }

    @Override
    public void setClient(RemoteInterface client) throws RemoteException {
        this.client = client;
    }

    private void initializeGUI() {
        frame = new JFrame("Server");
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
                        sendMessage(message);
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

    public static void main(String[] args) {
        try {
            Server server = new Server();

            LocateRegistry.createRegistry(1099); // Start the RMI registry on port 1099
            Naming.rebind("ChatServer", server);

            System.out.println("Server started");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
