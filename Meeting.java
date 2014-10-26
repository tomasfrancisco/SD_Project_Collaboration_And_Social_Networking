import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 * Created by pedromql on 25/10/14.
 */
public class Meeting implements Serializable {
    private Timestamp date;
    private int userID;
    private String desired_outcome;
    private String title;
    private String description;
    private String location;

    public Meeting() {

    }

    public Meeting(int userID, String desired_outcome, String title, String description, String location) {
        this.userID = userID;
        this.desired_outcome = desired_outcome;
        this.title = title;
        this.description = description;
        this.location = location;
    }

    public Timestamp getDate() {
        return date;
    }
    //@SuppressWarnings("deprecation")
    public void setDate(int year, int month, int day, int hour, int minute) {
        this.date = new Timestamp(year-1900,month-1,day,hour,minute,0,0);
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getDesired_outcome() {
        return desired_outcome;
    }

    public void setDesired_outcome(String desired_outcome) {
        this.desired_outcome = desired_outcome;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String toString() {
        return this.title + ", " + this.description + ", " + this.date + ", " + this.userID + ", " + this.location;
    }

    public void scheduleMeeting(Statement st) throws RemoteException {
        try {
            st.executeUpdate("INSERT INTO meeting (title, description, date, user_id, location) VALUES ('" + this.title +"', '"+this.description+"', '"+this.date+"', '"+this.userID+"','"+this.location+"')");

        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
