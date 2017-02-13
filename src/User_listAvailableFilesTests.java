import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import server.*;

public class User_listAvailableFilesTests {

	@Test
	public void test_listAvailableFilesPatient1() {
		Patient a = new Patient("Alice");
		ArrayList<FileRights> b= a.listAvailableFiles();
		for(FileRights fr: b){
			assertEquals(fr.toString(), "Name: Alice   Read: true   Write: false"); 
		}
	}
	@Test
	public void test_listAvailableFilesPatient2() {
		Patient a = new Patient("Pepepepepepepepepepepepe Bichiguso Maru");
		ArrayList<FileRights> b= a.listAvailableFiles();
		assertTrue(b.isEmpty());
	}
	@Test
	public void test_listAvailableFilesGov1() {
		Gov a = new Gov("007");
		ArrayList<FileRights> b= a.listAvailableFiles();
			assertEquals("[Name: Alice   Read: true   Write: false, Name: Bob   Read: true   Write: false, Name: Connor   Read: true   Write: false, Name: Demi   Read: true   Write: false]"
, b.toString()); 
		
	}
	@Test
	public void test_listAvailableFilesNurse1() {
		ArrayList<Division> b = new ArrayList<Division>();
		b.add(new Division("D1"));
		Nurse a = new Nurse("Nurse1", b);
		ArrayList<FileRights> r= a.listAvailableFiles();
			assertEquals("[Name: Demi   Read: true   Write: true, Name: Alise   Read: true   Write: false, Name: Bob   Read: true   Write: false]", r.toString()); 
		
	}

}
