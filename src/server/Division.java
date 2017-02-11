package server;

import java.util.ArrayList;
import java.util.List;

public class Division {
	
	private ArrayList<String> patients;
	
	public Division(String FILENAME) {
		//find and read file to make object
	}
	public boolean isPatientPartOfYou(String FILENAME){
		if(patients.contains(FILENAME)){
			return true;
		}
		return false;
	}
	
}
