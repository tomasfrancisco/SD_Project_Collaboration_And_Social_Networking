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
		String msg = "I'm the 1st TCP Server";
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

		try {
			dgSocket = new DatagramSocket(port + 1);
			System.out.println("Socket Datagram is listening at port " + (port + 1));
			while(true) {
				byte[] buffer = new byte[1000];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				dgSocket.receive(request);
				String udpmsg = new String(request.getData(), 0, request.getLength());
				System.out.println("Received by UDP connection: " + udpmsg);
			}
		}
		catch(SocketException ex) {
			System.out.println("Exception in PrimaryTCPServer.main: " + ex.getMessage());
		}
		catch(IOException ex) {
			System.out.println("Exception in PrimaryTCPServer.main: " + ex.getMessage());	
		}

	}
}