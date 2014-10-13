import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.net.*;
import java.io.*;

public class PrimaryTCPServer extends UnicastRemoteObject {

	PrimaryTCPServer() throws RemoteException {
		super();
	}

	public static void main(String args[]) {
		String msg = "I'm the TCP Server";
		int port = 7000;

		// Defines Security configurations, if no SecurityManager is specified, no dynamic
		// code downloading can take place
		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());

		try {
			DataServerObjectInterface server = (DataServerObjectInterface) LocateRegistry.getRegistry(port).lookup("DataServer");
			server.printOnServer(msg);
		}
		catch(Exception ex) {
			System.out.println("Exception in PrimaryTCPServer.main: " + ex.getMessage());
		}

	}
}