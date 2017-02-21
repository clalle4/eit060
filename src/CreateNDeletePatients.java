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
		d.createPatient("Pepepepepepepepepepepepe Bichiguso Maru1", "");

		File Dir = new File("./files/PatientRecords/");
		File[] list = Dir.listFiles();

		if (list != null)
			for (File fil : list) {
				if (fil.getName().replaceAll(".txt", "").equals("Pepepepepepepepepepepepe Bichiguso Maru1")) {
					madeIt = true;
				}
			}
		assertTrue(madeIt);

		File destructable = new File("./files/PatientRecords/Pepepepepepepepepepepepe Bichiguso Maru1.txt");
		destructable.delete();
	}

	@Test
	public void testTryToOverwritePatient1() {
		try {

			PrintWriter writer = new PrintWriter("./files/PatientRecords/Pepepepepepepepepepepepe Bichiguso Maru3.txt",
					"UTF-8");
			writer.close();
		} catch (IOException e) {
			// do something
		}
		ArrayList<Division> b = new ArrayList<Division>();
		b.add(new Division("D1"));
		Doctor d = new Doctor("Doctor1", b);
		assertFalse(d.createPatient("Pepepepepepepepepepepepe Bichiguso Maru3", ""));
		File destructable = new File("./files/PatientRecords/Pepepepepepepepepepepepe Bichiguso Maru3.txt");
		destructable.delete();

	}

	@Test
	public void testDeletePatient1() {
		try {

			PrintWriter writer = new PrintWriter("./files/PatientRecords/Pepepepepepepepepepepepe Bichiguso Maru2.txt",
					"UTF-8");
			writer.close();
		} catch (IOException e) {
			// do something
		}

		boolean itIsThere = false;
		Gov g = new Gov("Lasse");
		g.delete("Pepepepepepepepepepepepe Bichiguso Maru2");

		File Dir = new File("./files/PatientRecords/");
		File[] list = Dir.listFiles();
		if (list != null)
			for (File fil : list) {
				if (fil.getName().replaceAll(".txt", "").equals("Pepepepepepepepepepepepe Bichiguso Maru2")) {
					itIsThere = true;
				}
			}
		assertFalse(itIsThere);
	}

	@Test
	public void testCreatePatientReadFile() {
		ArrayList<Division> b = new ArrayList<Division>();
		b.add(new Division("D1"));
		Doctor d = new Doctor("Doctor1", b);
		String text = ("Jugemu Jugemu Unko " + System.lineSeparator() + "Nageki Ototoi no Shin-chan no Pantsu "
				+ System.lineSeparator() + "Shinpachi no Jinsei Balmunk Fezarion " + System.lineSeparator()
				+ "Isaac Schneider San Bun no Ichi no Junjou " + System.lineSeparator()
				+ "na Kanjou no Nokotta San Bun no Ni wa Sakamuke " + System.lineSeparator()
				+ "ga Kininaru Kanjou Uragiri wa Boku no Namae " + System.lineSeparator()
				+ "wo Shitteiru you de Shiranai no wo Boku wa " + System.lineSeparator()
				+ "Shitteiru Rusu Surume Medaka Kazunoko Koedame " + System.lineSeparator()
				+ "Medaka... Kono Medaka wa Sakki to Chigau " + System.lineSeparator()
				+ "Yatsu Dakara Ikeno Medaka no Hou Dakara " + System.lineSeparator() + "Raa-yuu Yuuteimiyaouki Mukou "
				+ System.lineSeparator() + "Pepepepepepepepepepepepe Bichiguso Maru");

		d.createPatient("JugemJugem", text);

		assertEquals(text, d.read("JugemJugem"));

		File destructable = new File("./files/PatientRecords/JugemJugem.txt");
		destructable.delete();

	}
}
