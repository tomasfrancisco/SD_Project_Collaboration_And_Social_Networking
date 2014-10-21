import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


class TCPConnections {
	Thread rConnection;
	Thread sConnection;

	TCPConnections(Socket clientSocket) {
		try {
			rConnection = new ReceiveTCP(new ObjectInputStream(clientSocket.getInputStream()));
			sConnection = new SendTCP(new ObjectOutputStream(clientSocket.getOutputStream()));
			System.out.println("New TCP Connections created");
		}
		catch(IOException ex) {
			System.out.println("IOException TCPConnections.Constructor: " + ex.getMessage());
			try {
				if(rConnection.isAlive()) {
					rConnection.interrupt();
				}
				else if(sConnection.isAlive()) {
					sConnection.interrupt();
				}
			}
			catch(SecurityException SecurityEx) {
				System.out.println("SecurityException TCPConnections.Constructor: " + SecurityEx.getMessage());
			}
		}
	}
}

class SendTCP extends Thread {
	ObjectOutputStream out;
	
	SendTCP(ObjectOutputStream out) {
		this.out = out;
		this.start();
	}
	
	public void run() {
		
		String texto = "";
	    InputStreamReader input = new InputStreamReader(System.in);
	    BufferedReader reader = new BufferedReader(input);
	    System.out.println("Introduza texto:");
	    		
	    while (true) {
	    	try {
			    texto = reader.readLine();
			    
				out.writeObject(texto);
				out.flush();
				//System.out.println("Enviado...");
	    	}
	    	catch(IOException ex) {
	    		System.out.println("IOException SendTCP.run: " + ex.getMessage());
	    		if(out != null) {
	    			try {
	    				out.close();
	    			}
	    			catch(IOException IOex) {
	    				System.out.println("IOException SendTCP.run: " + IOex.getMessage());
	    			}
	    		}
	    		break;
	    	}
	    }
	}
}

class ReceiveTCP extends Thread {
	ObjectInputStream in;
	
	ReceiveTCP(ObjectInputStream in) {
		this.in = in;
		this.start();
	}
	
	public void run() {
			    		
	    while (true) {
	    	try {
	    		while(true) {
	    			String data = (String) in.readObject();
	    			System.out.println("Received: " + data);
	    		}
	    	}
	    	catch(IOException ex) {
	    		System.out.println("IOException ReceiveTCP.run: " + ex.getMessage());
	    		try {
    				in.close();
    			}
    			catch(IOException IOex) {
    				System.out.println("IOException SendTCP.run: " + IOex.getMessage());
    			}
	    		break;
	    	}
	    	catch (ClassNotFoundException ex) {
	    		System.out.println("ClassNotFoundException ReceiveTCP.run: " + ex.getMessage());
	    		break;
			}
	    }
	}
}
