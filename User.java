import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by pedromql on 25/10/14.
 */
public class User implements Serializable {
    private String username;
    private String name;
    private int user_id;
    private String email;
    private String password;

    public User() {

    }

    public User(String username, String name, int user_id, String email, String password) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.user_id = user_id;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword() {
        this.password = password;
    }



    public String toString() {
        return this.username + ", " + this.user_id + ", " + this.email + ", " + this.name + ", " + this.password;
    }


    /*public User login(String username, String password, Statement st) throws RemoteException {
        User user;
        ResultSet rs;
        try {
            rs = st.executeQuery("SELECT user_id, username, email, name, password FROM user WHERE (username = '"+username+"' AND password = '"+password+"')");
            rs.next();
            user = new User(rs.getString("username"),rs.getString("name"),rs.getString("user_id"),rs.getString("email"), rs.getString("password"));
            System.out.println(user);
            return user;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public void register(String username, String email, String password, String name, Statement st) throws RemoteException {
        try {
            st.executeUpdate("INSERT INTO user (username, email, password, name) VALUES ('" + username +"', '"+email+"', '"+password+"', '"+name+"')");

        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }*/

}
