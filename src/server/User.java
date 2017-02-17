package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import javax.crypto.*;

public abstract class User {
	protected String name;
	private String password;

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
	 * Writes the content of a patients records
	 * 
	 * @param FILENAME
	 * The name of the patient whose records you want to access
	 */
	public String read(String FILENAME) {
		StringBuilder contents = new StringBuilder();
		if (isReadRequestAvailable(FILENAME)) {
			StringBuilder sb = new StringBuilder("./files/PatientRecords/");
			sb.append(FILENAME);
			sb.append(".txt");

			try (BufferedReader br = new BufferedReader(new FileReader(sb.toString()))) {

				String currentLine;

				if ((currentLine = br.readLine()) != null) {
					contents.append(currentLine);
				}
				
				while ((currentLine = br.readLine()) != null) {
					contents.append(System.lineSeparator() + currentLine);
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
	protected boolean authenticate(String username, String password) {
		MessageDigest md;
		byte[] hash = password.getBytes();
		try {
			md = MessageDigest.getInstance("SHA-512");
			md.update(hash);
			hash = md.digest();
			
			File f = new File("./files/PatientRecords/");
			File[] patientRecordList = f.listFiles();
			
			StringBuilder contents = new StringBuilder();
			BufferedReader br = null;

			if (patientRecordList != null) {
				
				for (File fil : patientRecordList) {
					
					if (name.equals(fil.getName().replaceAll(".txt", ""))) {
						
						try {
							br = new BufferedReader(new FileReader("./files/PatientRecords/" + name + ".txt"));
							String currentLine = br.readLine();
							currentLine = br.readLine();
							String passWithSalt = currentLine.substring(10);
							String passWithoutSalt;
							passWithoutSalt = passWithSalt.substring(2);
							byte[] bytePass = passWithoutSalt.getBytes();
							if (MessageDigest.isEqual(hash, bytePass)) {
								return true;
							} else {
								return false;
							}
							
						} catch (IOException e) {
							e.printStackTrace();
							System.err.println("Could not find user.");
						} finally {
							try {
								br.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * A double check method to be called when "read" is called, to make sure
	 * user is allowed access to file
	 **/
	protected abstract boolean isReadRequestAvailable(String FILENAME);

	protected abstract ArrayList<FileRights> listAvailableFiles();

}
