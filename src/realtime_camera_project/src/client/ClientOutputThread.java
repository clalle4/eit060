package client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * A thread class that sends requests from our client to servers, asking for
 * images at intervalls.
 */
public class ClientOutputThread extends Thread {
	private ClientMonitor mon;
	private Socket sock;
	private static final byte[] CRLF = { 13, 10 };

	/**
	 * Creates an instance of ClientOutputThread.
	 * 
	 * @param mon
	 *            The client's monitor.
	 * @param sock
	 *            The socket that connects the server and the client.
	 */
	public ClientOutputThread(ClientMonitor mon, Socket sock) {
		this.mon = mon;
		this.sock = sock;
	}

	/**
	 * Starts the thread. When the thread is running in it's basic state, it
	 * sends an image request every five seconds. If motionDetected i true, the
	 * thread will ask for an image every 40 ms.
	 */
	public void run() {
		while (true) {
			if (!mon.motionDetected()) {
				try {
					synchronized (this) {
						this.wait(5000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				try {
					Thread.sleep(40); // 25 FPS
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			try {
				OutputStream os = sock.getOutputStream();
				sendRequest(os);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Sends an image request trough the socket.
	 * 
	 * @param os
	 *            The socket's outputstream.
	 */
	private void sendRequest(OutputStream os) {
		try {
			putLine(os, "GET /image.jpg");
			String mode;
			if (mon.motionDetected()) {
				mode = "Movie";
			} else {
				mode = "Idle";
			}
			String s;
			if (mon.getCameraMode() == ClientMonitor.AUTO) {
				s = "Auto";
			} else {
				s = "Manual";
			}
			putLine(os, "Camera mode: " + mode + " (" + s + ")");
			putLine(os, "");// end of the request
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send a line on OutputStream 's', terminated by CRLF. The CRLF should not
	 * be included in the string str.
	 */
	private static void putLine(OutputStream s, String str) throws IOException {
		s.write(str.getBytes());
		s.write(CRLF);
	}

}
