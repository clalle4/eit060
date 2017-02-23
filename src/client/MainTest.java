package client;

import server.Server;

public class MainTest {

	public static void main(String[] args) {
		String[] clientIn = new String[2];
		String[] serverIn = new String[1];
		serverIn[0] = "9876";
		clientIn[0] = "localhost";
		clientIn[1] = "9876";
		Server.main(serverIn);
		try {
			Client.main(clientIn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
