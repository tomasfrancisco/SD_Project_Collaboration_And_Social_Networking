import java.rmi.*;

public interface DataServerObjectInterface extends Remote {
	public void printOnServer(String s) throws RemoteException;
}