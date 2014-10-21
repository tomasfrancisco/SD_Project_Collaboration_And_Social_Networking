import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;

public class TCPServer extends UnicastRemoteObject {
	private static final long serialVersionUID = -1580565047933309967L;

	TCPServer() throws RemoteException {
		super();
	}

	public static void main(String args[]) {

		/*if(args.length != 0) {
			new UDPServer(5000);
		}
		else {
			new UDPClient("localhost", 5000, 2000);
		}

		new RMIClient("localhost", 7000);*/
		HashSet<TCPConnections> clients = new HashSet<TCPConnections>();
		
		ServerSocket listenSocket = null;
		try {
			while(true) {
				listenSocket = new ServerSocket(Integer.parseInt(args[0]));
				System.out.println("Listening port: " + args[0]);
				
				Socket clientSocket = listenSocket.accept();	// Blocking
				clients.add(new TCPConnections(clientSocket));
			}
		}
		catch(IOException ex) {
			System.out.println("IOException TCPServer.run: " + ex.getMessage());
		}
		catch(Exception ex) {
			System.out.println("Exception TCPServer.run: " + ex.getMessage());
		}
		finally {
			if(listenSocket != null) {
				try {
					listenSocket.close();
				}
				catch(IOException ex) {
					System.out.println("IOException TCPServer.run: " + ex.getMessage());
				}				
			}
		}
	}
}