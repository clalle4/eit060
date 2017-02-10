package eit060;

public class Patient extends User {

	public Patient(String name, String password) {
		super(name, password);
		// TODO Auto-generated constructor stub
	}
	
	// A patient is allowed to read his/her own list of records.
	public void read() {
		
	}

	@Override
	public void authenticate() {
		// TODO Auto-generated method stub
		
	}
	
}
