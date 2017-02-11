import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import server.*;

public class User_read {

	@Test
	public void test_UserReadPatient1() {
		Patient a = new Patient("Alice");
			assertEquals(a.read("Alice"), "D1\n"); // obs there will be a \n at the end that will lock invisible
		
	}
	@Test
	public void test_UserReadPatient2() {
		Patient a = new Patient("Alice");
			assertEquals(a.read("Bob"), "You are not allowed to access that file"); // obs there will be a \n at the end that will lock invisible
		
	}
	@Test
	public void test_UserReadNurse1() {
		Nurse a = new Nurse("Alice");
			assertEquals(a.read("Bob"), "You are not allowed to access that file"); // obs there will be a \n at the end that will lock invisible
		
	}

}
