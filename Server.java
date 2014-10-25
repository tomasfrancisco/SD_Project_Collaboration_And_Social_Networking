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

	Server() throws RemoteException {
		super();
	}
	
	public static void getClients() throws IOException {
		while(true) {
			Socket socket = listenSocket.accept();
			connections.add(new Connection(socket));
		}
	}
	
	public static ServerSocket getListener(int port) throws IOException {
		return new ServerSocket(port);
	}

	public static void main(String args[]) {
		if(args.length != 1) {
			System.out.println("java TCPServer <port>");
			System.exit(0);
		}
	
		try {
			listenSocket = new ServerSocket(Integer.parseInt(args[0]));
			
			while(true) {
				System.out.println("Listening...");
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
		String responseLine;
		try {
			while((responseLine = in.readUTF()) != null) {
				System.out.println(responseLine);
			}
		} catch(IOException e) {
			System.err.println("Lost connection");
			//e.printStackTrace();
		}
	}
}