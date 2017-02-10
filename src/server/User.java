package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class User {
	protected String name;
	private String password;

	public User(String name, String password) {
		this.name = name;
		this.password = password;
	}

	/**Writes the content of a patients records
	 * @param FILENAME The name of the patient whose records you want to access
	 * */
	protected void read(String FILENAME) {
		if(isReadRequestAvailable(FILENAME)){
			StringBuilder sb = new StringBuilder("./files/PatientRecords/");
			sb.append(FILENAME);
			sb.append(".txt");
		try (BufferedReader br = new BufferedReader(new FileReader(sb.toString()))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	}
	/* same authentication process for all users, implemented only here */
	protected void authenticate() {

	}
	/**
	 * A double check method to be called when "read" is called,
	 * to make sure user is allowed access to file
	**/
	protected abstract boolean isReadRequestAvailable(String FILENAME);
	
	protected abstract void listAvailableFiles();
}
