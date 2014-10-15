import java.net.*;
import java.io.*;

class UDPClient extends Thread {
	DatagramSocket link = null;
	int serverPort;
	String serverIP;

	DatagramPacket in;
	DatagramPacket out;

	byte[] buffer;

	UDPClient(String serverIP, int serverPort) {
		try {
			this.serverIP = serverIP;
			this.serverPort = serverPort;
			this.link = new DatagramSocket();
			System.out.println("UDPClient is listening at " + serverPort);
			this.start();
		}
		catch(SocketException ex) {
			System.out.println("SocketException in UDPServer.run: " + ex.getMessage());
		}
	}

	public void init() {
		buffer = new byte[2];
	}

	public void run() {
		while(true) {
			try{			
				Thread.sleep(2000);

				buffer = "s".getBytes();
				out = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(serverIP), serverPort);
				link.send(out);
				in = new DatagramPacket(buffer, buffer.length);
				link.receive(in);
				System.out.println(new String(in.getData(), 0, in.getLength()));
			}
			catch(InterruptedException ex) {
				System.out.println("InterruptedException in UDPClient.run: " + ex.getMessage());	
			}
			catch(IOException ex) {
				System.out.println("IOException in UDPClient.run: " + ex.getMessage());	
			}
			finally {
				if(link != null)
					link.close();
			}
		}
		
	}
}