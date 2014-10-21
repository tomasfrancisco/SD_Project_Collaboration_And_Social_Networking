import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Client {
	
	static String serverIP;
	static int primaryPort;
	static int secondaryPort;
	static int clientTry = 0;
	static int attemptsLimit = 3;
	static boolean primary = true;
	
	static TCPConnections connection;
	
	public static void getCommunications(Socket socket) throws IOException, Exception, EOFException {
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		//ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		
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
			out.flush();
			System.out.println("Enviado...");
	    }
	}
	
	public static void getConnections(Socket socket, String serverIP, int port) throws IOException, Exception, EOFException {
		socket = new Socket(serverIP, port);
		getCommunications(socket);		
	}
	
	public static void main(String args[]) {	
		serverIP = "localhost";
		primaryPort = 2000;
		secondaryPort = 2050;
		
		Socket socket = null;
		while(true) {
			try {	
				clientTry++;
				if(primary) {
					System.out.println("Conneting to Primary...");					
					getConnections(socket, serverIP, primaryPort);
					clientTry = 0;
				}
				else {
					System.out.println("Conneting to Secondary...");
					getConnections(socket, serverIP, secondaryPort);
					clientTry = 0;
				}
			}
			catch(EOFException ex) {
				System.out.println("EOFException Client.main: " + ex.getMessage());
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
			finally {
				if(socket != null) {
					try {
						socket.close();
					}
					catch(IOException ex) {
						System.out.println("IOException Client.main: " + ex.getMessage());
					}
				}
			}
		}			
	}
}
