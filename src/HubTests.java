import static org.junit.Assert.*;
import server.*;
import org.junit.Test;

public class HubTests {

	@Test
	public void test() {
	Hub b = new Hub();
	assertEquals("[[Alice, Bob], [Connor, Demi]]{Nurse1=Nurse1, Nurse2=Nurse2, Nurse3=Nurse3, Doctor2=Doctor2, Doctor1=Doctor1, Gov=Gov}",b.toString());
	}

}
