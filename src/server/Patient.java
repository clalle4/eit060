package server;

public class Patient extends User {

	public Patient(String name, String password) {
		super(name, password);
		// TODO Auto-generated constructor stub
	}
	// A patient is allowed to read his/her own list of records.
	@Override
	protected boolean isReadRequestAvailable(String FILENAME) {
		if(name==FILENAME){return true;}
		return false;
	}
	@Override
	protected void listAvailableFiles() {
		// TODO Auto-generated method stub
		
	}


	
}
