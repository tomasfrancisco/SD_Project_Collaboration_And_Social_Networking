import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;

public class Server extends UnicastRemoteObject {
	private static final long serialVersionUID = -1580565047933309967L;

	private static Socket clientSocket = null;
	private static ServerSocket listenSocket = null;
	private static HashSet<Connection> connections = new HashSet<Connection>();
	
	private static boolean primary = false;
	
	private static UDPServer udpServer = null;
	private static UDPClient udpClient = null;

	Server() throws RemoteException {
		super();
	}
	
	public static boolean testExistingServer(String serverAddress, int serverPort, int pingInterval) {
		int attempt = 0;
		int attemptLimit = 1;
		boolean alive = false;
	
		UDPClient test = new UDPClient(serverAddress, serverPort, pingInterval)	;
		while(attempt < attemptLimit) {
			try {
				attempt++;
				System.out.println("(" + attempt + ")" + " Testing if " + serverAddress + " is alive on port " + serverPort);
				alive = test.ping();
			} catch (IOException e) {}
		}
		return alive;
	}
	
	public static void main(String args[]) {
		String tcpHostname1 = "localhost";
		int tcpPort1 = 2000;
		String tcpHostname2 = "localhost";
		int tcpPort2 = 2100;
		
		int udpServerPort = 5000;
		
		boolean host1 = false;
		
		//Test if some server is up, otherwise will act like primary server
		if(!(host1 = testExistingServer(tcpHostname1, udpServerPort, 100)) && !testExistingServer(tcpHostname2, udpServerPort, 100)) {
			primary = true;
		}
		
		udpServer = new UDPServer(udpServerPort);
		
		if(primary) {
			System.out.println("Acting like primary");
			udpServer.start();
		}
		else {
			if(host1)
				udpClient = new UDPClient(tcpHostname1, udpServerPort, 500);
			else
				udpClient = new UDPClient(tcpHostname2, udpServerPort, 500);
			try{
				System.out.println("Acting as backup");
				udpClient.start();
				udpClient.join();
				System.out.println("Acting as server");
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			udpServer.start();
			primary = true;
		}		
	
		try {
			RMIClient dataServer = new RMIClient("localhost", 7000);
			System.out.println(dataServer.getSession("localhost"));
			
			if(host1)
				listenSocket = new ServerSocket(tcpPort2);
			else
				listenSocket = new ServerSocket(tcpPort1);
			
			while(true) {
				System.out.println("TCPServer is listening at " + listenSocket.getLocalPort());
				clientSocket = listenSocket.accept();
				System.out.println(clientSocket.toString());
				connections.add(new Connection(clientSocket));				
			}
		}
		catch(IOException ex) {
			System.err.println(ex.getMessage());
		}		
	}
}

class Connection extends Thread {	
	private static ObjectInputStream in = null;
	private static ObjectOutputStream out = null;
	
	public Connection(Socket clientSocket) {
		try {
			in = new ObjectInputStream(clientSocket.getInputStream());
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			
			this.start();	
		} catch(IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void run() {
		try {
			while(true) {
				Menu.session(in, out);
				Menu.login(in, out);
			}
		} catch(IOException e) {
			System.err.println("Lost connection");
		}
	}
}