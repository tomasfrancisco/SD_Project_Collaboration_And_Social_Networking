import java.rmi.*;

public interface RMIInterface extends Remote {
	public void printOnServer(String s) throws RemoteException;
}