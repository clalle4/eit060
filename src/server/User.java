package server;

public abstract class User {
	// testing my first push
	private String name;
	private String password;
	
	public User(String name, String password) {
		this.name = name;
		this.password = password;
	}
	
	public abstract void read();
	
	public abstract void authenticate();
}
