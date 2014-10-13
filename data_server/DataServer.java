import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.net.*;
import java.io.*;

public class DataServer extends UnicastRemoteObject implements DataServerObjectInterface {

	public DataServer() throws RemoteException {
		super();
	}

	public void printOnServer(String msg) throws RemoteException {
		System.out.println("By Data Server: " + msg);
	}

	// ==================================================================

	public static void main(String args[]) {
		String msg = "I'm the Data Server";
		int port = 7000;

		// Defines Security configurations, if no SecurityManager is specified, no dynamic
		// code downloading can take place
		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());

		try {
			DataServer server = new DataServer();
			Registry reg = LocateRegistry.createRegistry(port);
			reg.rebind("DataServer", server);
			//Naming.rebind("DataServer", server);
			System.out.println("Data Server is ready.");
		}
		catch (Exception ex) {
			System.out.println("Exception in DataServer.main: " + ex.getMessage());
		}
	}
}