import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Thread {
	private static Socket clientSocket = null;

	private static ObjectInputStream in = null;
	private static ObjectOutputStream out = null;
	private static BufferedReader inputLine = null;
	
	private static int clientTry = 0;
	private static int attemptsLimit = 3;	
	private static boolean primary = true;
	private static String closed = "false";		
	
	Client() {
		this.start();
	}
	
	public static void main(String args[]) {				
		String tcpHostname1 = "localhost";
		int tcpPort1 = 2000;
		String tcpHostname2 = "localhost";
		int tcpPort2 = 2100;
		
		String responseLine;
		Thread reader = null;
		
		while(true) {
			try {
				clientTry++;
				//System.out.println("Trying to connect with server...");
				if(primary)
					clientSocket = new Socket(tcpHostname1, tcpPort1);
				else
					clientSocket = new Socket(tcpHostname2, tcpPort2);
				
				out = new ObjectOutputStream(clientSocket.getOutputStream());	
				in = new ObjectInputStream(clientSocket.getInputStream());
				
				inputLine = new BufferedReader(new InputStreamReader(System.in));
				
				if(clientSocket != null && out != null && in != null) {
					try {
						if(reader != null)
							synchronized (reader) {
								reader.notify();
							}
						else
							reader = new Client();
						synchronized (closed) {
							closed = "false";
						}
						while((responseLine = in.readUTF()) != null) {
							System.out.println(responseLine);
						}						
					}
					catch(IOException e) {
						synchronized (closed) {
							closed = "true";
						}
					}
				}			
			} catch (NumberFormatException | IOException e) {
				try {
					Thread.sleep(1000);
				}
				catch(InterruptedException ex) {
					System.err.println("An error occured");
				}
				if(clientTry == attemptsLimit) {
					//System.out.println("Reconnecting to server...");
					clientTry = 0;
					primary = !primary;
				}
			}
		}			
	}	
	
	public void run() {
		String responseLine;
		while(true) {
			try {				
				responseLine = inputLine.readLine().trim();
				synchronized (closed) {
					if(closed.equals("true"))
						synchronized (this) {
							this.wait();
						}
				}
				out.writeUTF(responseLine);
				out.flush();
			} catch(IOException | InterruptedException e) {
				System.err.println("An error accurred");
			}
		}
	}
}