package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Nurse extends User {

	private Division division;
	private ArrayList<String> patients = new ArrayList<String>();

	public Nurse(String name, ArrayList<Division> divisionRefs) {
		super(name);
		setUp(divisionRefs);
	}

	private void setUp(ArrayList<Division> divisionRefs) {

		StringBuilder sb = new StringBuilder("./files/Users/");
		sb.append(name);
		sb.append(".txt");
		try (BufferedReader br = new BufferedReader(new FileReader(sb.toString()))) {

			String currentLine;

			// get to "role" (=Nurse/Doctor)
			currentLine = br.readLine();
			// get to "division"
			currentLine = br.readLine();
			setdivision(currentLine, divisionRefs);

			// next is the list of patients
			while ((currentLine = br.readLine()) != null) {
				patients.add(currentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void addPatient(String name) {
		StringBuilder sb = new StringBuilder("./files/Users/");
		sb.append(this.name);
		sb.append(".txt");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(sb.toString(), true));
			bw.append("\n" + name);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Saves a reference to the user�s division. **/
	private void setdivision(String myDiv, ArrayList<Division> divisionRefs) {
		for (Division d : divisionRefs) {
			if (myDiv.equals(d.getName())) {
				division = d;
			}
		}
	}

	/**
	 * A nurse may write to all records associated with him/her, and also read
	 * all records associated with the same division.
	 **/
	// public void write(String FILENAME, String text) {// this will replace old
	// // file
	// if(isWriteRequestAvailable(FILENAME)){
	// StringBuilder sb = new StringBuilder("./files/PatientRecords/");
	// sb.append(FILENAME);
	// sb.append(".txt");
	//
	// try {
	// PrintWriter writer = new PrintWriter(sb.toString(), "UTF-8");
	// writer.print(text);
	// writer.close();
	// } catch (IOException e) {
	// // do something
	// }}
	// }

	public String writeLog(String FILENAME, String log) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		if (isReadRequestAvailable(FILENAME)) {
			StringBuilder sb = new StringBuilder("./files/PatientRecords/");
			sb.append(FILENAME);
			sb.append(".txt");
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(sb.toString(), true));
				bw.append(System.lineSeparator() + timestamp.toString() + "]: " + log);
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "Log complete with timestamp:" + timestamp;
		}
		return "You are not allowed to write to that file.";
		// BufferedWriter bw = new BufferedWriter(new FileWriter(./file));
	}

	private boolean isWriteRequestAvailable(String FILENAME) {
		if (patients.contains(FILENAME)) {
			return true;
		} else {
			return false;
		}
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
	public ArrayList<FileRights> listAvailableFiles() {

		ArrayList<FileRights> records = new ArrayList<FileRights>();
		ArrayList<String> divisionPatientList = division.getPatientList();

		for (String patient : patients) {

			records.add(new FileRights(patient, true, true));

		}
		boolean isAlreadyAdded = false;
		for (String patientInDivision : divisionPatientList) {
			for (String patient : patients) {
				if (patient.equals(patientInDivision)) {
					isAlreadyAdded = true;
				}

			}
			if (!isAlreadyAdded) {
				records.add(new FileRights(patientInDivision, true, false));
			}
			isAlreadyAdded = false;
		}

		return records;
	}

}
