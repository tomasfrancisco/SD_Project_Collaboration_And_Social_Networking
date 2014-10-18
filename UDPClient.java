import java.net.*;
import java.io.*;

class UDPClient extends Thread {
	//Server attributes
	DatagramSocket link = null;
	String serverAddress;
	InetAddress host;
	int serverPort;
	String serverId = "s";

	//Client connections
	private DatagramPacket in;
	private DatagramPacket out;

	//Client attributes
	private byte[] buffer;
	private int pingInterval;
	String clientId = "c";
	int clientTry = 0;
	int attemptsLimit = 3;

	UDPClient(String serverAddress, int serverPort, int pingInterval) {	
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.pingInterval = pingInterval;
		init();		
	}

	private void init() {
		try {
			host = InetAddress.getByName(serverAddress);
			buffer = new byte[2];
			link = new DatagramSocket();
			link.setSoTimeout(5000);
			this.start();
		}
		catch(UnknownHostException ex) {
			System.out.println("UnknownHostException in UDPClient.init: " + ex.getMessage());
		}
		catch(SocketException ex) {
			System.out.println("SocketException in UDPClient.init: " + ex.getMessage());
		}			
	}

	private boolean ping() throws SocketTimeoutException, IOException {
		buffer = "s".getBytes();
		out = new DatagramPacket(buffer, buffer.length, host, serverPort);
		link.send(out);
		in = new DatagramPacket(buffer, buffer.length);
		link.receive(in);

		if(new String(in.getData(), 0, in.getLength()).equals(serverId))
			return true;
		else
			return false;
	}

	public void run() {
		while(true) {
			try {			
				Thread.sleep(pingInterval);
				clientTry++;
				if(!ping() && clientTry > attemptsLimit) {
					//Turn this server into principal network server
					System.out.println("Now I'm the PRINCIPAL server");
					clientTry = 0;
				}
				else 
					System.out.println("I'm still the SECONDARY server");
			}
			catch(SocketTimeoutException ex) {
				if(clientTry > attemptsLimit) {
					clientTry = 0;
					//Turn this server into principal network server
					System.out.println("SocketTimoutException: Now I am the PRINCIPAL server");
				}
			}
			catch(InterruptedException ex) {
				System.out.println("InterruptedException in UDPClient.run: " + ex.getMessage());	
			}
			catch(IOException ex) {
				System.out.println("IOException in UDPClient.run: " + ex.getMessage());	
			}
		}
		
	}
}