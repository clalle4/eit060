package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Division extends ArrayList<String> {
	String name;

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

	public ArrayList<String> getPatientList(){
		return this;

	}
	public void newPatient(String name){
		this.add(name);
		StringBuilder sb = new StringBuilder("./files/Divisions/");
		sb.append(this.name);
		sb.append(".txt");
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(sb.toString(),true));
				bw.append("\n"+name);
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
