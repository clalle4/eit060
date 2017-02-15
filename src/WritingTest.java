import static org.junit.Assert.*;

import java.util.ArrayList;

import server.*;
import org.junit.Test;

public class WritingTest {

	@Test
	public void test() {// once implemented remake file
		ArrayList<Division> b = new ArrayList<Division>();
		b.add(new Division("D1"));
		Nurse a = new Nurse("Nurse1", b);
		a.write("Demi", "Demi Info\nwritten new info");
		assertEquals(a.read("Demi"), "Demi Info\nwritten new info");
	}

}
