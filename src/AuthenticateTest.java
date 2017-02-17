import static org.junit.Assert.*;

import org.junit.Test;
import server.*;

public class AuthenticateTest {

	@Test
	public void test() {
		Hub b = new Hub();
		String[] s = {"Doctor1","Password"};
		assertTrue(b.login(s));
	}
}
