package server;

public class Gov extends User {

	public Gov(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	
	


	
	//A government agency is allowed to delete all types of records.
	public void delete() {
		
	}
	// A government agency is allowed to read all types of records.
	@Override
	protected boolean isReadRequestAvailable(String FILENAME) {
		return true;
	}






	@Override
	protected void listAvailableFiles() {
		// TODO Auto-generated method stub
		
	}

}
