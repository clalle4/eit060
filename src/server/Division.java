package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Division {

	private ArrayList<String> patients= new ArrayList<String>();

	public Division(String FILENAME) {
		readDivisionFile(FILENAME);
	}


	private void readDivisionFile(String FILENAME) {
		StringBuilder sb = new StringBuilder("./files/Divisions/");
		sb.append(FILENAME);
		sb.append(".txt");
		try (BufferedReader br = new BufferedReader(new FileReader(sb.toString()))) {

			String currentLine;

			while ((currentLine = br.readLine()) != null) {
				patients.add(currentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isPatientPartOfYou(String FILENAME) {
		if (patients.contains(FILENAME)) {
			return true;
		}
		return false;
	}

}
