import java.rmi.RMISecurityManager;


public class DataServer {
	public static void main(String args[]) {
		String serverIP = "localhost";
		int port = 7000;
		String remoteObjectName = "DataServer";
		
		// Defines Security configurations, if no SecurityManager is specified, no dynamic
		// code downloading can take place
		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());

		try {
			RMIServer server = new RMIServer(serverIP, port);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
