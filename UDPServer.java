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
	}

	public void init() {
		try {
			buffer = new byte[2];
			link = new DatagramSocket(serverPort);
			System.out.println("UDPServer is listening at " + serverPort);
		}
		catch(SocketException ex) {
			ex.printStackTrace();
		}
	}

	private void pong() throws InterruptedException, IOException {
		in = new DatagramPacket(buffer, buffer.length);
		link.receive(in);
		buffer = serverId.getBytes();
		out = new DatagramPacket(buffer, buffer.length, in.getAddress(), in.getPort());
		link.send(out);
	}

	public void run() {
		init();
		while(true) {
			try {
				pong();
				//Acting as server
			}	
			catch(InterruptedException | IOException ex) {
				ex.printStackTrace();	
			}
		}
		
	}
}