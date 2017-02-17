import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import server.*;
import org.junit.Test;

public class WritingTest {

	@Test
	public void test() {// once implemented remake file
		ArrayList<Division> b = new ArrayList<Division>();
		b.add(new Division("D1"));
		Nurse a = new Nurse("Nurse1", b);
		a.write("Demi", "Demi Info" + System.lineSeparator() + "written new info");
		assertEquals( "Demi Info" + System.lineSeparator() + "written new info",a.read("Demi"));

		// remaking original file
		StringBuilder sb = new StringBuilder("./files/PatientRecords/");
		sb.append("Demi");
		sb.append(".txt");

		try {
			PrintWriter writer = new PrintWriter(sb.toString(), "UTF-8");
			writer.print("Name: Demi" + System.lineSeparator() + "Password: " + System.lineSeparator()
					+ "ID: 9405067890" + System.lineSeparator() + "Info: Sjukdom4");
			writer.close();
		} catch (IOException e) {
			// do something
		}

	}

}
