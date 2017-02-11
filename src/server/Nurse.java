package server;

import java.util.ArrayList;

public class Nurse extends User {

	private Division division;
	private ArrayList<String> patients;

	public Nurse(String name, String password, Division division) {
		super(name, password);
		this.division = division;
	}

	/**
	 * A nurse may write to all records associated with him/her, and also read
	 * all records associated with the same division.
	 **/
	public void write() {

	}

	/**
	 * A nurse may read to all records associated with him/her, and also read
	 * all records associated with the same division.
	 **/
	@Override
	protected boolean isReadRequestAvailable(String FILENAME) {
		if (patients.contains(FILENAME)) {
			return true;
		} else if (division.isPatientPartOfYou(FILENAME)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void listAvailableFiles() {
		// TODO Auto-generated method stub

	}

}
