package server;

import java.io.IOException;
import java.io.OutputStream;

import se.lth.cs.eda040.fakecamera.AxisM3006V;

/**
 * Handles outgoing communication to the client
 */
public class ServerOutputThread extends Thread {
	private ServerMonitor mon;
	private OutputStream os;
	private boolean motionDetected;
	private byte[] jpeg;
	private byte[] time;
	private boolean sendSwitchResponse;
	// By convention, these bytes are always sent between lines
	// (CR = 13 = carriage return, LF = 10 = line feed)
	private static final byte[] CRLF = { 13, 10 };

	public ServerOutputThread(ServerMonitor serverMonitor) {
		mon = serverMonitor;

		// kanske kan kommentera bort dessa rader???
		jpeg = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
		time = new byte[AxisM3006V.TIME_ARRAY_SIZE];
		motionDetected = false;
	}

	/**
	 * If motion has been detected from the camera sends a (SWITCH) response
	 * which tells the client to switch to movie mode. Else creates a header
	 * containing type of response (RECEIVE), the length of the sent image,
	 * motion detected status and the time stamp of the sent image. Sends the
	 * header and the image to the client through the sockets outputStream
	 */
	public void run() {
		while (true) {

			motionDetected = mon.motionDetected();
			boolean gotARequest = mon.gotARequest();
			mon.waitForRequest();
			if (sendSwitchResponse && motionDetected) {
				try {
					putLine(os, "Switch to Movie mode!");
				} catch (IOException e) {
					System.out.println("Failed to send SWITCH response");
				}
				// sets the sendSwitchResponse to false so that the thread does
				// not send SWITCH response at high rate
				sendSwitchResponse = false;
			} else if (gotARequest) {
				jpeg = mon.getImage();
				time = mon.getTime();
				int length = mon.getImageLength();
				try {
					putLine(os, "RECEIVE image");
					putLine(os, length + "");
					String s;
					if (motionDetected) {
						s = "Motion has been detected";
					} else {
						s = "Motion has not been detected";
					}
					putLine(os, s);
					os.write(time); // end of header
					os.write(jpeg, 0, length); // send the image
				} catch (IOException e) {
					System.out.println("Failed to send RECEIVE response");
				}
				mon.setGotARequest(false);
				if (!mon.runningInMovieMode())
					sendSwitchResponse = true;
			}
		}
	}

	public void setOutputStream(OutputStream os) {
		this.os = os;
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
