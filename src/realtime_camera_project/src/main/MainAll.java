package main;

import java.io.IOException;

import client.ClientMain;
import server.ServerMain;

public class MainAll {
	/**
	 * 
	 * Main method for running both sides of the program.
	 */
	public static void main(String[] args) {
		try {
			ServerMain.main(null);
			ClientMain.main(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
