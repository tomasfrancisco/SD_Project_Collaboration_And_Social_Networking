import java.io.Serializable;
import java.util.Date;


public class Session implements Serializable {
	private static final long serialVersionUID = 1759058792408708534L;
	private String address;
	private Date lastCommit;
	private int lastMenu;
	
	public Session(String address) {
		this.address = address;
		this.lastCommit = new Date();
		this.lastMenu = 0;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getLastCommit() {
		return lastCommit;
	}
	public void setLastCommit(Date lastCommit) {
		this.lastCommit = lastCommit;
	}
	public int getLastMenu() {
		return lastMenu;
	}
	public void setLastMenu(int lastMenu) {
		this.lastMenu = lastMenu;
	}
}
