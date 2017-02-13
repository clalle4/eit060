import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import server.*;

public class User_read {

	@Test
	public void test_UserReadPatient1() {
		Patient a = new Patient("Alice");
		assertEquals(a.read("Alice"), "Alice Info");

	}

	@Test
	public void test_UserReadPatient2() {
		Patient a = new Patient("Alice");
		assertEquals(a.read("Bob"), "You are not allowed to access that file");

	}

	@Test
	public void test_UserReadNurse1() {
		ArrayList<Division> b = new ArrayList<Division>();
		b.add(new Division("D1"));
		Nurse a = new Nurse("Nurse1", b);
		assertEquals(a.read("Connor"), "You are not allowed to access that file");

	}

	@Test
	public void test_UserReadNurse2() {
		ArrayList<Division> b = new ArrayList<Division>();
		b.add(new Division("D1"));
		Nurse a = new Nurse("Nurse1", b);
		assertEquals(a.read("Bob"), "Bob Info");

	}

	@Test
	public void test_UserReadGov1() {
		Gov a = new Gov("adam");
		assertEquals(a.read("Alice"), "Alice Info");
	}
}
