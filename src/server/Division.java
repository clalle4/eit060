package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Division extends ArrayList<String> {
	String name;
<<<<<<< HEAD
	//private ArrayList<String> patients= new ArrayList<String>();
=======
	private ArrayList<String> patients = new ArrayList<String>();
>>>>>>> origin/master

	public Division(String FILENAME) {
		name = FILENAME;
		readDivisionFile(FILENAME);
	}

	private void readDivisionFile(String FILENAME) {
		StringBuilder sb = new StringBuilder("./files/Divisions/");
		sb.append(FILENAME);
		sb.append(".txt");
		try (BufferedReader br = new BufferedReader(new FileReader(sb.toString()))) {

			String currentLine;

			while ((currentLine = br.readLine()) != null) {
				this.add(currentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

<<<<<<< HEAD
	public ArrayList<String> getPatientList(){
		return this;
=======
	public ArrayList<String> getPatientList() {
		return patients;
>>>>>>> origin/master
	}

	public boolean isPatientPartOfYou(String FILENAME) {
		if (this.contains(FILENAME)) {
			return true;
		}
		return false;
	}

	public String getName() {
		return name;
	}
}
