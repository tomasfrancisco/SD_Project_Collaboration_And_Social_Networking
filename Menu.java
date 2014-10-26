import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public abstract class Menu {
	
	public static void sendUTF(ObjectOutputStream out, String msg) throws IOException {
		out.writeUTF(msg);
		out.flush();
	}
	
	public static void session(ObjectInputStream in, ObjectOutputStream out) throws IOException {
		String option;
		
		sendUTF(out, "1. Login\n"
				   + "2. Register");
		option = in.readUTF();	
		System.out.println(option);
	}
	
	public static void login(ObjectInputStream in, ObjectOutputStream out) throws IOException {
		String username;
		String password;
		
		sendUTF(out, "username:");
		username = in.readUTF();
		System.out.println(username);
		sendUTF(out, "password:");
		password = in.readUTF();		
		System.out.println(password);
	}
	
	public static void register(ObjectInputStream in, ObjectOutputStream out) throws IOException {
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
	}
	
}
