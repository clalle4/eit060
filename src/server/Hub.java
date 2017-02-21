package server;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Hub {
	private HashMap<String,User> users = new HashMap<String,User>();
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

					while ((currentLine = br.readLine()) != null) {
						if (currentLine.equals("Doctor")) {
							users.put(fil.getName().replaceAll(".txt", ""),new Doctor(fil.getName().replaceAll(".txt", ""), divisions));
						} else if (currentLine.equals("Nurse")) {
							users.put(fil.getName().replaceAll(".txt", ""),new Nurse(fil.getName().replaceAll(".txt", ""), divisions));
						} else if (currentLine.equals("Gov")) {
							users.put(fil.getName().replaceAll(".txt", ""),new Gov(fil.getName().replaceAll(".txt", "")));
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
	 * point 0 = action
	 * point 1 is object
	 * point 2 is subject
	 * 
	 * **/
	public String readRequest(String[] request, String login){
		return users.get(login).read(request[1]);
	}
	public String writeRequest(String[] request, String login){	
		return users.get(login).writeLog(request[1],request[2]);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(divisions.toString());
		sb.append(users.toString());

		return sb.toString();
	}
}
