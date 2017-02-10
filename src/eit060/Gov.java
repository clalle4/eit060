package eit060;

public class Gov extends User {

	public Gov(String name, String password) {
		super(name, password);
		// TODO Auto-generated constructor stub
	}

	// A government agency is allowed to read all types of records.
	@Override
	public void read() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void authenticate() {
		// TODO Auto-generated method stub
		
	}
	
	//A government agency is allowed to delete all types of records.
	public void delete() {
		
	}

}
