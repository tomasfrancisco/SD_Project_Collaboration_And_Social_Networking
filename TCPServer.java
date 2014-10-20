import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;


class TCPServer extends Thread {
	int port;
	HashSet<TCPConnections> clients;

	TCPServer(int port) {
		this.port = port;
		init();
	}

	private void init() {
		clients = new HashSet<TCPConnections>();
		this.start();
	}

	public void run() {
		try {
			ServerSocket listenSocket = new ServerSocket(port);
			System.out.println("Listening port: " + port);
			while(true) {
				Socket clientSocket = listenSocket.accept();	// Blocking
				System.out.println("Accept");
				clients.add(new TCPConnections(clientSocket));
			}
		}
		catch(IOException ex) {
			System.out.println("IOException TCPServer.run: " + ex.getMessage());
		}
		catch(Exception ex) {
			System.out.println("Exception TCPServer.run: " + ex.getMessage());
		}
	}
}