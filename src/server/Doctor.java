package server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Doctor extends Nurse {

	public Doctor(String name, ArrayList<Division> divisions) {
		super(name, divisions);
		// TODO Auto-generated constructor stub
	}

	/**
	 * In addition, the doctor can create new records for a patient provided
	 * that the doctor is treating the patient. When creating the record, the
	 * doctor also associates a nurse with the record.
	 */
	public boolean createPatient(String FILENAME) {
		File usersDir = new File("./files/PatientRecords/");
		File[] userlist = usersDir.listFiles();

		if (userlist != null) {
			// see if this patient already exists
			for (File fil : userlist) {
				if (FILENAME.equals(fil.getName().replaceAll(".txt", ""))) {
					return false;
				}
			}
		}
		try {
			StringBuilder sb = new StringBuilder("./files/PatientRecords/");
			sb.append(FILENAME);
			sb.append(".txt");
			PrintWriter writer = new PrintWriter(sb.toString(), "UTF-8");
			writer.close();
			return true;
		} catch (IOException e) {
			// do something
		}
		return false;
	}

}
