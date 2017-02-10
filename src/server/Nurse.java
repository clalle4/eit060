package server;

import java.util.ArrayList;

public class Nurse extends User {
	
	private String division;
	private ArrayList<String> patients;

	public Nurse(String name, String password, String division) {
		super(name, password);
		this.division = division;
	}

	/**
	 * A nurse may read to all records associated with him/her, and also read all records
	 * associated with the same division.
	**/
	@Override
	public void read() {
		// TODO Auto-generated method stub
		
	}


	
	/**
	 * A nurse may write to all records associated with him/her, and also read all records
	 * associated with the same division.
	**/
	public void write() {
		
	}

}
