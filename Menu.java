import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public abstract class Menu {
	
	public static void sendUTF(ObjectOutputStream out, String msg) throws IOException {
		out.writeUTF(msg);
		out.flush();
	}
	
	public static void session(ObjectInputStream in, ObjectOutputStream out, Session clientSession) throws IOException {
		String option;
		
		sendUTF(out, "1. Login\n"
				   + "2. Register");
		clientSession.setLastMenu("session");
		option = in.readUTF();		
		switch (option) {
			case "1":
				Menu.login(in, out, clientSession);
				break;
			case "2":
				Menu.register(in, out, clientSession);
				break;
			default:
				break;
		}
	}
	
	public static void login(ObjectInputStream in, ObjectOutputStream out, Session clientSession) throws IOException {
		String username;
		String password;
		clientSession.setLastMenu("login");
		
		sendUTF(out, "username:");
		username = in.readUTF();
		System.out.println(username);
		sendUTF(out, "password:");
		password = in.readUTF();		
		System.out.println(password);
		System.out.println("Set Login");
		clientSession.setLastMenu("login");
	}
	
	public static void register(ObjectInputStream in, ObjectOutputStream out, Session clientSession) throws IOException {
		String username;
		String password;
		String retypePassword;
		
		sendUTF(out, "username:");
		username = in.readUTF();
		System.out.println(username);
		sendUTF(out, "password:");
		password = in.readUTF();
		System.out.println(password);
		sendUTF(out, "retype password:");
		retypePassword = in.readUTF();
		System.out.println(retypePassword);
		System.out.println("Set Register");
		clientSession.setLastMenu("register");
	}
	
}
