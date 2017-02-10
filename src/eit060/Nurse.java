package eit060;

public class Nurse extends User {

	public Nurse(String name, String password) {
		super(name, password);
		// TODO Auto-generated constructor stub
	}

	/**
	 * A nurse may read to all records associated with him/her, and also read all records
	 * associated with the same division.
	**/
	@Override
	public void read() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void authenticate() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * A nurse may write to all records associated with him/her, and also read all records
	 * associated with the same division.
	**/
	public void write() {
		
	}

}
