import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class Client {
	
	static String serverIP;
	static int port;
	static int clientTry = 0;
	static int attemptsLimit = 3;
	static boolean primary = true;
	
	static TCPConnections connection;
	
	public static void getCommunications(Socket socket) throws IOException, Exception {
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		//DataInputStream in = new DataInputStream(socket.getInputStream());
		
		String texto = "";
	    InputStreamReader input = new InputStreamReader(System.in);
	    BufferedReader reader = new BufferedReader(input);
	    System.out.println("Introduza texto:");
		
	    // 3o passo
	    while (true) {
			// READ STRING FROM KEYBOARD
		    texto = reader.readLine();

			// WRITE INTO THE SOCKET
			out.writeUTF(texto);
	    }
	}
	
	public static void getConnections(String serverIP, int port) throws IOException, Exception {
		Socket socket = new Socket(serverIP, port);
		getCommunications(socket);		
	}
	
	public static void main(String args[]) {	
		serverIP = "localhost";
		port = 2000;
		
		while(true) {
			try {	
				clientTry++;
				if(primary) {
					System.out.println("Conneting to Primary...");					
					getConnections(serverIP, port);
					clientTry = 0;
				}
				else {
					System.out.println("Conneting to Secondary...");
					getConnections(serverIP, port);
					clientTry = 0;
				}
			}
			catch(IOException ex) {
				System.out.println("IOException Client.main: " + ex.getMessage());
				try {
					Thread.sleep(1000);
				}
				catch(InterruptedException threadex) {
					System.out.println("InterruptedException Client.main: " + threadex.getMessage());
				}
				if(clientTry == attemptsLimit) {
					clientTry = 0;
					primary = !primary;
					System.out.println(primary);
				}
			} 
			catch(Exception ex) {
				System.out.println("Exception Client.main: " + ex.getMessage());
			}
		}			
	}
}
