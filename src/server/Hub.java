package server;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Hub {
	private HashMap<String, User> users = new HashMap<String, User>();
	private ArrayList<Division> divisions = new ArrayList<Division>();

	public Hub() {
		startUp();
	}

	private void startUp() {
		readDivisionFiles();
		readUserFiles();
	}

	/**
	 * Fills the "users" list with the users saved in files
	 */
	private void readUserFiles() {
		File usersDir = new File("./files/Users/");
		File[] userlist = usersDir.listFiles();

		if (userlist != null) {
			for (File fil : userlist) {
				StringBuilder sb = new StringBuilder("./files/Users/");
				sb.append(fil.getName());

				try (BufferedReader br = new BufferedReader(new FileReader(sb.toString()))) {

					String currentLine;

					if ((currentLine = br.readLine()) != null) {
						if (currentLine.equals("Doctor")) {
							users.put(fil.getName().replaceAll(".txt", ""),
									new Doctor(fil.getName().replaceAll(".txt", ""), divisions));
						} else if (currentLine.equals("Nurse")) {
							users.put(fil.getName().replaceAll(".txt", ""),
									new Nurse(fil.getName().replaceAll(".txt", ""), divisions));
						} else if (currentLine.equals("Gov")) {
							users.put(fil.getName().replaceAll(".txt", ""),
									new Gov(fil.getName().replaceAll(".txt", "")));
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	/**
	 * Fills the "divisions" list with the divisions saved in files
	 */
	private void readDivisionFiles() {
		File divisionsDir = new File("./files/Divisions/");
		File[] divisionlist = divisionsDir.listFiles();

		if (divisionlist != null)
			for (File fil : divisionlist) {
				divisions.add(new Division(fil.getName().replaceAll(".txt", "")));
			}
	}

	/**
	 * 
	 * point 0 is action point 1 is object point 2 is subject
	 * 
	 **/
	public String readRequest(String[] request, String login) {
		User user = users.get(login);
		if (user == null) {
			user = new Patient(login);
		}
		try {
			return user.read(request[1]);
		} catch (NullPointerException e) {
			return "ERROR: no file by that name.";
		}

	}

	public String writeRequest(String[] request, String login) {
		Nurse user = (Nurse) users.get(login);
		try {
			return user.writeLog(request[1], request[2]);
		} catch (NullPointerException e) {
			return "ERROR: no file by that name.";
		}

	}

	public String getRights(String login) {
		User user = users.get(login);
		if (user == null) {
			user = new Patient(login);
		}
		try {
			StringBuilder sb = new StringBuilder();
			for (FileRights s : user.listAvailableFiles()) {
				sb.append("\n" + s.toString());
			}
			return sb.toString();
		} catch (NullPointerException e) {
			return "ERROR: no no rights available.";
		}
	}

	/**
	 * Makes a new patient with a proper request. Could be better to make into
	 * an int later to simplify identification of errors.
	 */
	public boolean createPatient(String[] request, String login) {
		Doctor user = (Doctor) users.get(login);
		if (user == null) {
			return false;
		}
		Nurse nurse = (Nurse) users.get(request[4]);
		if (nurse == null) {
			return false;
		}
		
		nurse.addPatient(request[1]);
		user.addPatient(request[1]);
		String content = "Name: " + request[1] + "\nID: " + request[2] + "\nDoctor: " + login + "\nNurse: " + request[4]
				+ "\nDivision: D"+request[5]+ "\nInfo: " + request[3];
		user.createPatient(request[1], content);
		divisions.get(Integer.parseInt(request[5])-1).newPatient(request[1]);
		users.put(login, new Doctor(login, divisions));
		users.put(request[4], new Nurse(request[4], divisions));
		return true;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(divisions.toString());
		sb.append(users.toString());

		return sb.toString();
	}
}
