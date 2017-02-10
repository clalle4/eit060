package eit060;

public abstract class User {
	
	private String name;
	private String password;
	
	public User(String name, String password) {
		this.name = name;
		this.password = password;
	}
	
	public abstract void read();
	
	public abstract void authenticate();
}
