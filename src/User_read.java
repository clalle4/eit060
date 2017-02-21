import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import server.*;

public class User_read {

	@Test
	public void test_UserReadPatient1() {
		Patient a = new Patient("Alice");
		assertEquals( "Name: Alice"+System.lineSeparator()+"Password: [B@4e25154f"+System.lineSeparator()+"ID: 9102034567"+System.lineSeparator()+"Info: Sjukdom1",a.read("Alice"));
		
	}

	@Test
	public void test_UserReadPatient2() {
		Patient a = new Patient("Alice");
		assertEquals( "You are not allowed to access that file",a.read("Bob"));

	}

	@Test
	public void test_UserReadNurse1() {
		ArrayList<Division> b = new ArrayList<Division>();
		b.add(new Division("D1"));
		Nurse a = new Nurse("Nurse1", b);
		assertEquals( "You are not allowed to access that file",a.read("Connor"));

	}

	@Test
	public void test_UserReadNurse2() {
		ArrayList<Division> b = new ArrayList<Division>();
		b.add(new Division("D1"));
		Nurse a = new Nurse("Nurse1", b);
		assertEquals("Name: Bob"+System.lineSeparator()+"Password: 1890552393"+System.lineSeparator()+"ID: 9203045678"+System.lineSeparator()+"Info: Sjukdom2",a.read("Bob"));

	}

	@Test
	public void test_UserReadGov1() {
		Gov a = new Gov("adam");
		assertEquals( "Name: Alice"+System.lineSeparator()+"Password: [B@4e25154f"+System.lineSeparator()+"ID: 9102034567"+System.lineSeparator()+"Info: Sjukdom1",a.read("Alice"));
	}
	@Test
	public void readThrouHubtest1() {
		Hub b = new Hub();
		String[] s = { "1", "Alice", "Doctor1" };
		assertEquals("Name: Alice" + System.lineSeparator() + "Password: [B@4e25154f" + System.lineSeparator() + "ID: 9102034567"
				+ System.lineSeparator() + "Info: Sjukdom1", b.readRequest(s));
	}

	@Test
	public void readThrouHubtest2() {
		Hub b = new Hub();
		String[] s = { "1", "Demi", "Doctor1" };
		assertEquals("You are not allowed to access that file", b.readRequest(s));
	}
	
	
}
