package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public abstract class User {
	protected String name;
	private String password;

	public User(String name) {
		this.name = name;
	}
	/**
	 * remove this for the final version 
	 **/
public String toString(){
	return name;
}
	/**Writes the content of a patients records
	 * @param FILENAME The name of the patient whose records you want to access
	 * */
	public String read(String FILENAME) {
		StringBuilder contents = new StringBuilder();
		if(isReadRequestAvailable(FILENAME)){
			StringBuilder sb = new StringBuilder("./files/PatientRecords/");
			sb.append(FILENAME);
			sb.append(".txt");
			
		try (BufferedReader br = new BufferedReader(new FileReader(sb.toString()))) {

			String currentLine;

			if((currentLine = br.readLine()) != null){}
			contents.append(currentLine);
			while ((currentLine = br.readLine()) != null) {
				contents.append("\n"+currentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return contents.toString();

	}
		contents.append("You are not allowed to access that file");
		return contents.toString();
	}
	/* same authentication process for all users, implemented only here */
	protected void authenticate() {

	}
	/**
	 * A double check method to be called when "read" is called,
	 * to make sure user is allowed access to file
	**/
	protected abstract boolean isReadRequestAvailable(String FILENAME);
	
	protected abstract ArrayList<FileRights> listAvailableFiles();
	
	//remove this
	public void setName(String n){
		name=n;
	}
}
