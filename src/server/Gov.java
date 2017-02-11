package server;

import java.io.File;

public class Gov extends User {

	public Gov(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	// A government agency is allowed to delete all types of records.
	public void delete(String FILENAME) {
		StringBuilder sb = new StringBuilder("./files/PatientRecords/");
		sb.append(FILENAME);
		sb.append(".txt");
		File destructable = new File(sb.toString());
		destructable.delete();
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
