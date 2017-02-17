package server;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Hub {
	private ArrayList<User> users = new ArrayList<User>();
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
							users.add(new Doctor(fil.getName().replaceAll(".txt", ""), divisions));
						} else if (currentLine.equals("Nurse")) {
							users.add(new Nurse(fil.getName().replaceAll(".txt", ""), divisions));
						} else if (currentLine.equals("Gov")) {
							users.add(new Gov(fil.getName().replaceAll(".txt", "")));
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

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(divisions.toString());
		sb.append(users.toString());

		return sb.toString();
	}

}
