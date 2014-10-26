import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

class RMIServer extends UnicastRemoteObject implements RMIInterface {
	private static final long serialVersionUID = -4359562983393030357L;
	static String serverIP = "localhost";
	static int port = 7000;
	static String remoteObjectName = "DataServer";
	
	ArrayList<Session> sessions;

	RMIServer(String serverIP, int port) throws RemoteException {
		super();
		sessions = new ArrayList<Session>();
		init(serverIP, port);
	}

	public void init(String serverIP, int port) {
		// Defines Security configurations, if no SecurityManager is specified, no dynamic
		// code downloading can take place
		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());

		try {
			Registry reg = LocateRegistry.createRegistry(port);
			reg.rebind(remoteObjectName, this);
			System.out.println("Data Server is ready on " + port);
		}
		catch(Exception ex) {
			System.out.println("Exception in DataServer.main: " + ex.getMessage());
		}
	}

	@Override
	public Session getSession(String address) throws RemoteException {
		for(Session session : sessions) {
			if(session.getAddress().equals(address)) {
				return session;
			}
		}
		Session newSession = new Session(address);
		sessions.add(newSession);
		return newSession;
	}
}