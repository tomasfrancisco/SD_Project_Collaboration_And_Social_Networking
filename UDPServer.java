import java.net.*;
import java.io.*;

class UDPServer extends Thread {
	//Server attributes
	DatagramSocket link = null;
	int serverPort;
	String serverId = "s";
	byte[] buffer;

	//Server connections
	DatagramPacket in;
	DatagramPacket out;	

	UDPServer(int serverPort) {
		this.serverPort = serverPort;
		init();
	}

	private void init() {
		try {
			buffer = new byte[2];
			link = new DatagramSocket(serverPort);
			System.out.println("UDPServer is listening at " + serverPort);
			this.start();
		}
		catch(SocketException ex) {
			System.out.println("SocketException in UDPServer.run: " + ex.getMessage());
		}
	}

	private void pong() throws InterruptedException, IOException {
		in = new DatagramPacket(buffer, buffer.length);
		link.receive(in);	//Blocking
		buffer = serverId.getBytes();
		out = new DatagramPacket(buffer, buffer.length, in.getAddress(), in.getPort());
		link.send(out);
	}

	public void run() {
		while(true) {
			try{
				pong();
				System.out.println("I'm still the PRINCIPAL server");
			}	
			catch(InterruptedException ex) {
				System.out.println("InterruptedException in UDPClient.run: " + ex.getMessage());	
			}
			catch(IOException ex) {
				System.out.println("IOException in UDPServer.run: " + ex.getMessage());	
			}
		}
		
	}
}