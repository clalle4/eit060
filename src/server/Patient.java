package server;

import java.io.File;
import java.util.ArrayList;

public class Patient extends User {

	public Patient(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	// A patient is allowed to read his/her own list of records.
	@Override
	protected boolean isReadRequestAvailable(String FILENAME) {
		if (name.compareTo(FILENAME)==0) {
			return true;
		}
		return false;
	}
	public boolean isNurse(){
		return false;
	}

	@Override
	public ArrayList<FileRights> listAvailableFiles() {
		ArrayList<FileRights> records = new ArrayList<FileRights>();

		File patientRecords = new File("./files/PatientRecords/");
		File[] patientRecordList = patientRecords.listFiles();

		if (patientRecordList != null) {
			for (File fil : patientRecordList) {
				if (name.equals(fil.getName().replaceAll(".txt", ""))) {
					records.add(new FileRights(name, true, false));
					return records;
				}
			}
		}

		return records;
	}

}
