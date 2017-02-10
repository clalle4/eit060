package server;

public abstract class User {
	private String name;
	private String password;
	
	public User(String name, String password) {
		this.name = name;
		this.password = password;
	}
	
	public abstract void read();
	/*same authentication process for all users, implemented only here*/
	public void authenticate(){
		
	}
}
