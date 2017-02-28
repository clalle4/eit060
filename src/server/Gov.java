package server;

import java.io.File;
import java.util.ArrayList;

public class Gov extends User {

	public Gov(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	// A government agency is allowed to delete all types of records.
	public boolean delete(String FILENAME) {
		StringBuilder sb = new StringBuilder("./files/PatientRecords/");
		sb.append(FILENAME);
		sb.append(".txt");
		File destructable = new File(sb.toString());
		return destructable.delete();
	}
	public boolean isNurse(){
		return false;
	}

	// A government agency is allowed to read all types of records.
	@Override
	protected boolean isReadRequestAvailable(String FILENAME) {
		return true;
	}

	@Override
	public ArrayList<FileRights> listAvailableFiles() {
		ArrayList<FileRights> records = new ArrayList<FileRights>();

		File patientRecords = new File("./files/PatientRecords/");
		File[] patientRecordList = patientRecords.listFiles();

		if (patientRecordList != null) {
			for (File fil : patientRecordList) {
				records.add(new FileRights(fil.getName().replaceAll(".txt", ""), true, false));
			}
		}

		return records;
	}

}
