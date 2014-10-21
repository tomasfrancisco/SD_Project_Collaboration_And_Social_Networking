import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Client {
	
	static String serverIP;
	static int primaryPort;
	static int secondaryPort;
	static int clientTry = 0;
	static int attemptsLimit = 3;
	static boolean primary = true;
	static Thread send = null;
	static Thread receive = null;
	
	public static void getCommunications(Socket socket) throws IOException, Exception, EOFException {
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		send = new SendTCP(out);
		receive = new ReceiveTCP(in);
		
		receive.join();
		send.interrupt();
	}
	
	public static void getConnection(Socket socket, String serverIP, int port) throws IOException, Exception, EOFException {
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
					getConnection(socket, serverIP, primaryPort);
					clientTry = 0;
				}
				else {
					System.out.println("Conneting to Secondary...");
					getConnection(socket, serverIP, secondaryPort);
					clientTry = 0;
				}
			}
			catch(EOFException ex) {
				System.out.println("EOFException Client.main: " + ex.getMessage());
			}
			catch(IOException ex) {
				System.out.println("IOException Client.main: " + ex.getMessage());
				try {
					//if(send.isAlive())
						//send.interrupt();
					if(receive != null && receive.isAlive()) 
						receive.interrupt();
					Thread.sleep(1000);
				}
				catch(SecurityException SecurityEx) {
					System.out.println("SecurityException Client.main: " + SecurityEx.getMessage());
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
