import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

class RMIClient extends Thread {
	private String serverIP;
	private int port; 
	static String remoteObjectName = "DataServer";
	private RMIInterface rmiServer;

	RMIClient(String serverIP, int port) {
		this.serverIP = serverIP;
		this.port = port;
		init();
	}

	private void init() {
		// Defines Security configurations, if no SecurityManager is specified, no dynamic
		// code downloading can take place
		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());
		
		try {
			rmiServer = (RMIInterface) Naming.lookup("rmi://" + serverIP + ":" + port + "/" + remoteObjectName);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public Session getSession(String address) throws RemoteException {
		return rmiServer.getSession(address);
	}
}