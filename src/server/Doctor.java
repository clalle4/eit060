package server;

public class Doctor extends Nurse {

	public Doctor(String name, String password, Division division) {
		super(name, password, division);
		// TODO Auto-generated constructor stub
	}
	
	/** In addition, the doctor can create new records for a patient provided
	 * that the doctor is treating the patient. When creating the record, the
	 * doctor also associates a nurse with the record.
	 */
	public void createPatient() {
		
	}

}