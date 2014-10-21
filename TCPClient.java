import java.io.IOException;
import java.net.Socket;


class TCPClient extends Thread {
	String serverIP;
	int port;
	int clientTry = 0;
	int attemptsLimit = 3;
	boolean primary = true;
	
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
		Socket socket = null;
		try {
			while(clientTry < attemptsLimit && socket == null) {
				if(primary) {
					System.out.println("Conneting to Primary...");
					clientTry++;
					socket = new Socket(serverIP, port);
					System.out.println("Primary Connection port: " + port);
					connection = new TCPConnections(socket);
					clientTry = 0;
				}
				else {
					System.out.println("Conneting to Secondary...");
					clientTry++;
					socket = new Socket(serverIP, port);
					System.out.println("Secondary Connection port: " + port);
					connection = new TCPConnections(socket);
					clientTry = 0;
				}
			}
		}
		catch(IOException ex) {
			System.out.println("IOException TCPClient.run: " + ex.getMessage());
			clientTry++;
			if(clientTry == attemptsLimit) {
				clientTry = 0;
				primary = !primary;
			}
		} 
		catch(Exception ex) {
			System.out.println("Exception TCPClient.run: " + ex.getMessage());
		}
	}
}