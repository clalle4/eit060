import static org.junit.Assert.*;

import org.junit.Test;

import server.*;

public class DivisionTests {

	@Test
	public void testDivisions1() {
		Division d = new Division("D1");
		assertTrue(d.isPatientPartOfYou("Alice"));
	}

	@Test
	public void testDivisions2() {
		Division d = new Division("D1");
		assertTrue(d.isPatientPartOfYou("Bob"));
	}

	@Test
	public void testDivisions3() {
		Division d = new Division("D1");
		assertFalse(d.isPatientPartOfYou("Connor"));
	}

	@Test
	public void testDivisions4() {
		Division d = new Division("D1");
		assertFalse(d.isPatientPartOfYou("Demi"));
	}

}
