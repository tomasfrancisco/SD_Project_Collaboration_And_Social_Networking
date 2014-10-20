import java.io.IOException;
import java.net.Socket;


class TCPClient extends Thread {
	String serverIP;
	int port;
	
	TCPConnections connection;

	TCPClient(String serverIP, int port) {
		this.serverIP = serverIP;
		this.port = port;
		init();
	}

	private void init() {
		this.start();
	}

	public void run() {
		try {
			Socket socket = new Socket(serverIP, port);
			System.out.println("Listening port: " + port);
			connection = new TCPConnections(socket);
		}
		catch(IOException ex) {
			System.out.println("IOException TCPClient.run: " + ex.getMessage());
		} 
		catch(Exception ex) {
			System.out.println("Exception TCPClient.run: " + ex.getMessage());
		}
	}
}