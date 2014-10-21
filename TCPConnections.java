import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;


public class TCPConnections {
	ReceiveConnection rConnection;
	SendConnection sConnection;

	TCPConnections(Socket clientSocket) {
		rConnection = new ReceiveConnection(clientSocket);
		sConnection = new SendConnection(clientSocket);
		System.out.println("New TCP Connections created");
	}
}

class ReceiveConnection extends Thread {
	Socket clientSocket;
	DataInputStream in;

	ReceiveConnection(Socket clientSocket) {
		this.clientSocket = clientSocket;
		init();
	}

	void init() {
		try {
			in = new DataInputStream(clientSocket.getInputStream());
			this.start();
		}
		catch(IOException ex){
			System.out.println("IOException TCPConnections.ReceiveConnection.init: " + ex.getMessage());
		}
	}

	public void run() {
		String buffer = "";
		try { 
			while(true) {
				if(in != null)
					buffer = in.readUTF();
				System.out.println("Recebido: " + buffer);
			}
		}
		catch(IOException ex) { 
			System.out.println("IOException TCPConnections.ReceiveConnection.run: " + ex.getMessage());
		}
		finally {
			if(clientSocket != null) {
				try {
					in.close();
					clientSocket.close();
				}
				catch(IOException ex) {
					System.out.println("IOException TCPConnections.ReceiveConnection.run: " + ex.getMessage());
				}
			}
		}
	}
}

class SendConnection extends Thread {
	Socket clientSocket;
	DataOutputStream out;
	ConcurrentLinkedQueue<String> fifo;

	SendConnection(Socket clientSocket) {
		this.clientSocket = clientSocket;
		init();
	}

	void init() {
		try {
			out = new DataOutputStream(clientSocket.getOutputStream());
			fifo = new ConcurrentLinkedQueue<String>();
			this.start();
		}
		catch(IOException ex){
			System.out.println("IOException TCPServer.SendConnection.init: " + ex.getMessage());
		}
	}

	public boolean sendMessage(String msg) {
		return fifo.add(msg);
	}

	public void run() {
		
		try {
			while(true) {
				int fifoSize = fifo.size();
				for(int i = 0; i < fifoSize; i++) {
					
						if(out != null) {
							// Tenta enviar a info
							out.writeUTF(fifo.peek());
							// Se correr bem, retira da fila
							fifo.poll();
						}
				}
			}
		}
		catch(IOException ex) {
			System.out.println("IOException TCPServer.SendConnection.run: " + ex.getMessage());
		}
		finally {
			if(clientSocket != null) {
				try {
					out.close();
					clientSocket.close();
				}
				catch(IOException ex) {
					System.out.println("IOException TCPConnections.ReceiveConnection.run: " + ex.getMessage());
				}
			}
		}
	}
}
