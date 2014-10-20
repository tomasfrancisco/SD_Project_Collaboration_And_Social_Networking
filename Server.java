import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject {

	Server() throws RemoteException {
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
		TCPServer server = new TCPServer(2000);
	}
}