import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {
    void sendMessage(String message) throws RemoteException;
    void receiveMessage(String message) throws RemoteException;
    void setClient(RemoteInterface client) throws RemoteException;
}
