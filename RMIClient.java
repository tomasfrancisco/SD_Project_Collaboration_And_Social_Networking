import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;

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

    public User getUserByID(int userID) throws RemoteException {
        return rmiServer.getUserByID(userID);
    }

    public int getUserIDByUsername(String username) throws RemoteException {
        return rmiServer.getUserIDByUsername(username);
    }

    public User login(String username, String password) throws RemoteException {
        return rmiServer.login(username, password);
    }

    public int register(String username, String email, String password, String name) throws RemoteException {
        return rmiServer.register(username, email, password, name);
    }

    public Meeting scheduleMeeting(int userID, String desired_outcome, String title, String description, String location, int year, int month, int day, int hour, int minute) throws RemoteException {
        return rmiServer.scheduleMeeting(userID, desired_outcome, title, description, location, year, month, day, hour, minute);
    }

    public int inviteToMeeting(String username, int meeting_id) throws RemoteException {
        return rmiServer.inviteToMeeting(username, meeting_id);
    }

    public int acceptMeetingInvite(String username, int meeting_id) throws RemoteException {
        return rmiServer.acceptMeetingInvite(username, meeting_id);
    }

    public ArrayList<Meeting> listUpcomingMeetings(String username) throws RemoteException {
        return rmiServer.listUpcomingMeetings(username);
    }
}