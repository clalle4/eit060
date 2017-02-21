package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

public abstract class User {
	
	protected String name;

	public User(String name) {
		this.name = name;
	}

	/**
	 * Remove this for the final version
	 **/
	public String toString() {
		return name;
	}

	/**
	 * Writes the content of a patient's records
	 * 
	 * @param FILENAME
	 *            The name of the patient whose records you want to access
	 */
	public String read(String FILENAME) {
		StringBuilder contents = new StringBuilder();
		if (isReadRequestAvailable(FILENAME)) {
			StringBuilder sb = new StringBuilder("./files/PatientRecords/");
			sb.append(FILENAME);
			sb.append(".txt");

			try {
				BufferedReader br = new BufferedReader(new FileReader(sb.toString()));
				String currentLine = br.readLine();
				if (currentLine != null) {
					contents.append(currentLine);
					currentLine = br.readLine();

					while (currentLine != null) {
						contents.append(System.lineSeparator() + currentLine);
						currentLine = br.readLine();
					}
				}
				br.close();// needed for test createNdeletepatients with text
			} catch (IOException e) {
				e.printStackTrace();
			}
			return contents.toString();

		}
		contents.append("You are not allowed to access that file");
		return contents.toString();
	}

	/**
	 * A double check method to be called when "read" is called, to make sure
	 * user is allowed access to file
	 **/
	protected abstract boolean isReadRequestAvailable(String FILENAME);

	protected abstract ArrayList<FileRights> listAvailableFiles();

	public String writeLog(String FILENAME, String log) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		if (isReadRequestAvailable(FILENAME)) {
			StringBuilder sb = new StringBuilder("./files/PatientRecords/");
			sb.append(FILENAME);
			sb.append(".txt");
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(sb.toString(),true));
				bw.append("\n["+timestamp.toString()+"]: " + log);
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "Log complete with timestamp:"+timestamp;
		}
		return "You are not allowed to write to that file.";
		// BufferedWriter bw = new BufferedWriter(new FileWriter(./file));
	}
}
