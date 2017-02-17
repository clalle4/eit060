import static org.junit.Assert.*;
import server.*;
import org.junit.Test;

public class HubTests {

	@Test
	public void test() {
	Hub b = new Hub();
	assertEquals("[[Alise, Bob], [Connor, Demi]][Doctor1, Doctor2, Gov, Nurse1, Nurse2, Nurse3]",b.toString());
	}

}
