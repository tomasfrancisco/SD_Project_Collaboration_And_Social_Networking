import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

class RMIServer extends UnicastRemoteObject implements RMIInterface {
	private static final long serialVersionUID = -4359562983393030357L;
	static String serverIP = "localhost";
	static int port = 7000;
	static String remoteObjectName = "DataServer";
    static Connection conn;
    static Statement st;
    
	ArrayList<Session> sessions;

    public void connectDB() {
        try {
        	try {
        	    Class.forName("com.mysql.jdbc.Driver").newInstance();
        	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
        	    // error out
        	}
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        try {
            st = conn.createStatement();
        }
        catch (SQLException ex) {
            System.out.println("Bad Statement");
        }
    }

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
            connectDB();

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

    public User getUserByID(int userID) throws RemoteException {
        ResultSet rs;
        User user;
        try {
            rs = st.executeQuery("SELECT username, email, name, password FROM user WHERE (user_id = " + userID + ")");
            rs.next();
            user = new User(rs.getString("username"),rs.getString("name"),userID,rs.getString("email"), rs.getString("password"));
            System.out.println(user);
            return user;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int getUserIDByUsername(String username) throws RemoteException {
        ResultSet rs;
        try {
            rs = st.executeQuery("SELECT user_id FROM user WHERE (username = '"+username+"')");
            if (rs.isLast() == false) {
                rs.next();
                return rs.getInt("user_id");
            }
            return -1;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public Meeting getMeetingByID(int meeting_id) throws RemoteException {
        ResultSet rs;
        try {
            rs = st.executeQuery("SELECT user_id, desired_outcome, title, description, location, date FROM meeting WHERE (meeting_id = "+meeting_id+")");
            if (rs.next() == true) {
                Meeting meeting = new Meeting(rs.getInt("user_id"), rs.getString("desired_outcome"), rs.getString("title"), rs.getString("description"), rs.getString("location"));
                Timestamp date = rs.getTimestamp("date");
                meeting.setDate(date.getYear()+1900, date.getMonth()+1, date.getDay(), date.getHours(), date.getMinutes());
                //System.out.println(meeting);
                return meeting;
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public User login(String username, String password) throws RemoteException {
        ResultSet rs;
        User user;
        try {
            rs = st.executeQuery("SELECT user_id, username, email, name, password FROM user WHERE (username = '"+username+"' AND password = '"+password+"')");
            rs.next();
            user = new User(rs.getString("username"),rs.getString("name"),rs.getInt("user_id"),rs.getString("email"), rs.getString("password"));
            System.out.println(user);
            return user;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int register(String username, String email, String password, String name) throws RemoteException {

        try {
            st.executeUpdate("INSERT INTO user (username, email, password, name) VALUES ('" + username +"', '"+email+"', '"+password+"', '"+name+"')");
            System.out.println(username + " : registered with success!");
            return 1;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }



    public Meeting scheduleMeeting(int userID, String desired_outcome, String title, String description, String location, int year, int month, int day, int hour, int minute) throws RemoteException {
        Meeting meeting = new Meeting(userID, desired_outcome, title, description, location);
        meeting.setDate(year, month, day, hour, minute);
        meeting.scheduleMeeting(st);
        System.out.println(meeting);
        return meeting;
    }

    public int inviteToMeeting(String username, int meeting_id) throws RemoteException {
        ResultSet rs;
        int userID = getUserIDByUsername(username);
        System.out.println(userID);
        try {
            rs = st.executeQuery("SELECT user_id, meeting_id, status FROM user_meeting WHERE (user_id = "+ userID + " AND meeting_id = '"+meeting_id+"')");
            if (rs.next() == false) { //pode enviar convite
                st.executeUpdate("INSERT INTO user_meeting (user_id, meeting_id) VALUES ('"+userID+"','"+meeting_id+"')");
                System.out.println("Invited with sucess!");
                return -1;
            }

            if (rs.getInt("status") == 0) { //convidado mas não aceite
                System.out.println("Convidado mas não aceite");
                return -2;
            }
            else if (rs.getInt("status") == 1) { //convidado e aceite
                System.out.println("Convidado e aceite");
                return -3;
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public int acceptMeetingInvite(String username, int meeting_id) throws RemoteException {
        ResultSet rs;
        int userID = getUserIDByUsername(username);
        try {
            rs = st.executeQuery("SELECT user_id, meeting_id, status FROM user_meeting WHERE (user_id = "+ userID + " AND meeting_id = '"+meeting_id+"')");
            if (rs.next() == false) {
                System.out.println("O utilizador não está convidado para esse meeting!");
                return -1;
            }
            if (rs.getInt("status") == 0) {
                st.executeUpdate("UPDATE user_meeting SET status = 1 WHERE (user_id = "+ userID + " AND meeting_id = '"+meeting_id+"')");
                System.out.println("O utilizador aceitou o meeting");
                return -2;
            }
            else if (rs.getInt("status") == 1) {
                System.out.println("O utilizador já tinha aceite esse meeting");
                return -3;
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public int checkPastMeetings() {
        //NECESSÁRIO IMPLEMENTAR PARA MANTER CONSISTÊNCIA NOS MEETINGS A LISTAR NOS UPCOMING
       return 0;
    }

    public ArrayList<Meeting> listUpcomingMeetings(String username) throws RemoteException { //FALTA IMPRIMIR STATUS - PROVAVELMENTE CRIAR CLASSE USER_MEETING
        ResultSet rs;
        int userID = getUserIDByUsername(username);
        //System.out.println(userID);
        ArrayList<Meeting> meetings_array = new ArrayList<Meeting>();
        try {
            Statement user_meetings = conn.createStatement();
            rs = user_meetings.executeQuery("SELECT user_id, meeting_id, status FROM user_meeting WHERE (user_id = "+ userID+")");
            while (rs.next()) {                                                 //FIX THIS FUCKING SHIT
                int meeting_id = rs.getInt("meeting_id");
                meetings_array.add(getMeetingByID(meeting_id));
                System.out.println(getMeetingByID(meeting_id));

            }
            for (int i = 0; i < meetings_array.size(); i++) {
                System.out.println(meetings_array.get(i));
            }
            return meetings_array;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}