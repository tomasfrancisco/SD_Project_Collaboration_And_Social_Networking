import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.net.*;
import java.io.*;

public class SecondaryTCPServer extends UnicastRemoteObject {

	SecondaryTCPServer() throws RemoteException {
		super();
	}

	public static void main(String args[]) {
		String msg = "I'm the 2nd TCP Server";
		int port = 7000;
		DatagramSocket dgSocket = null;

		// Defines Security configurations, if no SecurityManager is specified, no dynamic
		// code downloading can take place
		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());

		try {
			// Connect with Data Server
			DataServerObjectInterface server = (DataServerObjectInterface) LocateRegistry.getRegistry(port).lookup("DataServer");
			server.printOnServer(msg);
		}
		catch(Exception ex) {
			System.out.println("Exception in SecondaryTCPServer.main: " + ex.getMessage());
		}

		try{
			dgSocket = new DatagramSocket();

			byte [] udpmsg = new String("I'm the 2nd TCP Server").getBytes();

			InetAddress host = InetAddress.getByName("localhost");
			int serverPort = port + 1;
			DatagramPacket request = new DatagramPacket(udpmsg, udpmsg.length, host, serverPort);
			dgSocket.send(request);
		} 
		catch (SocketException ex) {
			System.out.println("Exception in SecondaryTCPServer.main: " + ex.getMessage());
		}
		catch(IOException ex) {
			System.out.println("Exception in PrimaryTCPServer.main: " + ex.getMessage());	
		}
		finally {
			if(dgSocket != null)
				dgSocket.close();
		}
	}
}