import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Client {
	
	public static void main(String args[]) {	

		TCPClient link = new TCPClient("localhost", 2000);
		
	    String texto = "";
	    InputStreamReader input = new InputStreamReader(System.in);
	    BufferedReader reader = new BufferedReader(input);
	    System.out.println("Introduza texto:");

	    // 3o passo
	    while (true) {
			// READ STRING FROM KEYBOARD
			try {
			    texto = reader.readLine();
			} catch (Exception e) {
			}

			// WRITE INTO THE SOCKET
			System.out.println(texto);
			TCPConnections conn = link.connection;
			SendConnection send = conn.sConnection;
			send.sendMessage(texto);
			
	    }
		
			
	}
}
