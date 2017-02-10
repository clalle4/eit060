package server;

import java.io.IOException;
import java.net.UnknownHostException;

public class Main {
	/**
	 * Main method for server to be run on cameras.
	 * 
	 * @param args
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		String server = null;
		int port = Integer.parseInt(args[0]);
		CameraThread camera = new CameraThread(server, port);
		JPEGHTTPServer HTTP = new JPEGHTTPServer();
		camera.start();
		HTTP.start();
	}
}
