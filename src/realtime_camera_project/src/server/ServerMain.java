package server;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * 
 * A simple main method for starting two cameras on ports 8011 and 8012. not to
 * be used for other then testing.
 *
 */
public class ServerMain {
	public static void main(String[] args) throws UnknownHostException, IOException {
		CameraThread[] camera = new CameraThread[2];
		// JPEGHTTPServer[] HTTP = new JPEGHTTPServer[2];
		int index = 0;
		for (int i = 0; i < 2; i++) {

			String server = args[index];
			int port = Integer.parseInt(args[index + 1]);
			index += 2;
			camera[i] = new CameraThread(server, port);
			// HTTP[i] = new JPEGHTTPServer();
		}
		for (CameraThread cam : camera) {
			cam.start();
		}
		// for (JPEGHTTPServer s : HTTP) {
		// s.start();
		// }
	}

}
