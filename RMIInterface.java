import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RMIInterface extends Remote {
	public Session getSession(String address) throws RemoteException;
    public User getUserByID(int userID) throws RemoteException;
    public int getUserIDByUsername(String username) throws RemoteException;
    public User login(String username, String password) throws RemoteException;
    public int register(String username, String email, String password, String name) throws RemoteException;
    public Meeting scheduleMeeting(int userID, String desired_outcome, String title, String description, String location, int year, int month, int day, int hour, int minute) throws RemoteException;
    public int inviteToMeeting(String username, int meeting_id) throws RemoteException;
    public int acceptMeetingInvite(String username, int meeting_id) throws RemoteException;
    public ArrayList<Meeting> listUpcomingMeetings(String username) throws RemoteException;
}