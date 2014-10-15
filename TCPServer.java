import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.net.*;
import java.io.*;

public class TCPServer extends UnicastRemoteObject {

	TCPServer() throws RemoteException {
		super();
	}

	public static void main(String args[]) {
		String serverNumber = "";

		if(args.length == 0) {
			serverNumber = "1";
			new UDPServer(5000);
			new UDPClient("localhost", 5001);
		}
		else if(args.length == 1) {
			serverNumber = args[0];
			new UDPServer(5001);
			new UDPClient("localhost", 5000);
		}
		else {
				System.exit(1);
		}

		String msg = "I'm the TCP Server: " + serverNumber;
		int port = 7000;
		DatagramSocket dgSocket = null;

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