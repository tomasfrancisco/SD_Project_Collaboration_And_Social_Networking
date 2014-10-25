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
		} catch(UnknownHostException | SocketException ex) {
			ex.printStackTrace();
		}	
	}

	public boolean ping() throws SocketTimeoutException, IOException {
		buffer = "s".getBytes();
		out = new DatagramPacket(buffer, buffer.length, host, serverPort);
		link.send(out);
		in = new DatagramPacket(buffer, buffer.length);
		link.receive(in);

		if(new String(in.getData(), 0, in.getLength()).equals(serverId)) return true;
		else return false;
	}

	public void run() {
		while(true) {
			try {			
				Thread.sleep(pingInterval);
				clientTry++;
				if(!ping() && clientTry > attemptsLimit) {
					//Turn this server into principal network server
					clientTry = 0;
				}
			} catch(SocketTimeoutException ex) {
				if(clientTry > attemptsLimit) {
					clientTry = 0;
					//Turn this server into principal network server
					return;
				}
			} catch(InterruptedException | IOException ex) {
				ex.printStackTrace();	
			}
		}
		
	}
}