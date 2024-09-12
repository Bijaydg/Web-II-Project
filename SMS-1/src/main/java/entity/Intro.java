package entity;

public class Intro {
private int ID;
	public int getID() {
	return ID;
}
public void setID(int iD) {
	ID = iD;
}
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	private String Email;
	private String Password;
	
	
	public String username;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String toString() {
		return "Intro [ID=" + ID + ", name=" + name + ", Email=" + Email + ", Password=" + Password + ", username="
				+ username + "]";
	}
	
	
}
