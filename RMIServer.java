import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

class RMIServer extends UnicastRemoteObject implements RMIInterface {
	static String serverIP = "localhost";
	static int port = 7000;
	static String remoteObjectName = "DataServer";


	RMIServer() throws RemoteException {
		super();
	}

	public void printOnServer(String msg) throws RemoteException {
		System.out.println("By Data Server: " + msg);
	}

	public static void main(String args[]) {
		// Defines Security configurations, if no SecurityManager is specified, no dynamic
		// code downloading can take place
		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());

		try {
			RMIServer server = new RMIServer();
			Registry reg = LocateRegistry.createRegistry(port);
			Naming.rebind("rmi://" + serverIP + ":" + port + "/" + remoteObjectName, server);
			System.out.println("Data Server is ready.");
		}
		catch(Exception ex) {
			System.out.println("Exception in DataServer.main: " + ex.getMessage());
		}
	}
}