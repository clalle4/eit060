import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.Test;

import server.*;

public class CreateNDeletePatients {
	// cleanup might be required after failed use

	@Test
	public void testCreatePatient1() {
		boolean madeIt = false;
		ArrayList<Division> b = new ArrayList<Division>();
		b.add(new Division("D1"));
		Doctor d = new Doctor("Doctor1", b);
		d.createPatient("Pepepepepepepepepepepepe Bichiguso Maru");

		File Dir = new File("./files/PatientRecords/");
		File[] list = Dir.listFiles();

		if (list != null)
			for (File fil : list) {
				if (fil.getName().replaceAll(".txt", "").equals("Pepepepepepepepepepepepe Bichiguso Maru")) {
					madeIt = true;
				}
			}
		assertTrue(madeIt);

		File destructable = new File("./files/PatientRecords/Pepepepepepepepepepepepe Bichiguso Maru.txt");
		destructable.delete();
	}

	@Test
	public void testTryToOverwritePatient1() {
		try {

			PrintWriter writer = new PrintWriter("./files/PatientRecords/Pepepepepepepepepepepepe Bichiguso Maru.txt",
					"UTF-8");
			writer.close();
		} catch (IOException e) {
			// do something
		}
		ArrayList<Division> b = new ArrayList<Division>();
		b.add(new Division("D1"));
		Doctor d = new Doctor("Doctor1", b);
		assertFalse(d.createPatient("Pepepepepepepepepepepepe Bichiguso Maru"));
		File destructable = new File("./files/PatientRecords/Pepepepepepepepepepepepe Bichiguso Maru.txt");
		destructable.delete();

	}

	@Test
	public void testDeletePatient1() {
		try {

			PrintWriter writer = new PrintWriter("./files/PatientRecords/Pepepepepepepepepepepepe Bichiguso Maru.txt",
					"UTF-8");
			writer.close();
		} catch (IOException e) {
			// do something
		}

		boolean itIsThere = false;
		Gov g = new Gov("Lasse");
		g.delete("Pepepepepepepepepepepepe Bichiguso Maru");

		File Dir = new File("./files/PatientRecords/");
		File[] list = Dir.listFiles();
		if (list != null)
			for (File fil : list) {
				if (fil.getName().replaceAll(".txt", "").equals("Pepepepepepepepepepepepe Bichiguso Maru")) {
					itIsThere = true;
				}
			}
		assertFalse(itIsThere);
	}
}
