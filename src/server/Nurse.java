package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Nurse extends User {

	private Division division;
	private ArrayList<String> patients = new ArrayList<String>();

	public Nurse(String name, ArrayList<Division> divisionRefs) {
		super(name);
		setUp(divisionRefs);
	}

	
	
	private void setUp(ArrayList<Division> divisionRefs){
		
		StringBuilder sb = new StringBuilder("./files/Users/");
		sb.append(name);
		sb.append(".txt");
		try (BufferedReader br = new BufferedReader(new FileReader(sb.toString()))) {

			String currentLine;
			
			//get to "role" (=Nurse/Doctor)
			currentLine = br.readLine();
			//get to "division"
			currentLine = br.readLine();
			setdivision(currentLine, divisionRefs);
			
			//next is the list of patients
			while ((currentLine = br.readLine()) != null) {
				patients.add(currentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**Saves a reference to the user´s division.**/
	private void setdivision(String myDiv, ArrayList<Division> divisionRefs){
		for(Division d : divisionRefs){
			if(myDiv.equals(d.getName())){
				division = d;
			}
		}
	}
	
	
	
	/**
	 * A nurse may write to all records associated with him/her, and also read
	 * all records associated with the same division.
	 **/
	public void write() {

	}

	/**
	 * A nurse may read to all records associated with him/her, and also read
	 * all records associated with the same division.
	 **/
	@Override
	protected boolean isReadRequestAvailable(String FILENAME) {
		if (patients.contains(FILENAME)) {
			return true;
		} else if (division.isPatientPartOfYou(FILENAME)) {
			return true;
		} else {
			return false;
		}
	}

	
	
	@Override
	public ArrayList<FileRights> listAvailableFiles() {
		return null;

	}

}
