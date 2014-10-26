import java.rmi.*;

public interface RMIInterface extends Remote {
	public Session getSession(String address) throws RemoteException;
}