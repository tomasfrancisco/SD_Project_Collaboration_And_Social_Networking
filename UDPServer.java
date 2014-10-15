import java.net.*;
import java.io.*;

class UDPServer extends Thread {
	DatagramSocket link = null;
	DatagramPacket in;
	DatagramPacket out;
	int port;

	byte[] buffer;

	UDPServer(int port) {
		try {
			this.port = port;
			this.link = new DatagramSocket(port);
			System.out.println("UDPServer is listening at " + port);
			init();
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

				in = new DatagramPacket(buffer, buffer.length);
				link.receive(in);	//Blocking
				System.out.println(new String(in.getData(), 0, in.getLength()));
				buffer = "p".getBytes();
				out = new DatagramPacket(buffer, buffer.length, in.getAddress(), in.getPort());
				link.send(out);
			}	
			catch(InterruptedException ex) {
				System.out.println("InterruptedException in UDPClient.run: " + ex.getMessage());	
			}
			catch(IOException ex) {
				System.out.println("IOException in UDPServer.run: " + ex.getMessage());	
			}
			finally {
				if(link != null)
					link.close();
			}
		}
		
	}
}