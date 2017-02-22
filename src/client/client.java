package client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.X509Certificate;

/*
 * This example shows how to set up a key manager to perform client
 * authentication.
 *
 * This program assumes that the client is not inside a firewall.
 * The application can be modified to connect to a server outside
 * the firewall by following SSLSocketClientWithTunneling.java.
 */
public class client {

	public static void main(String[] args) throws Exception {
		String host = null;
		int port = -1;
		for (int i = 0; i < args.length; i++) {
			System.out.println("args[" + i + "] = " + args[i]);
		}
		if (args.length < 2) {
			System.out.println("USAGE: java client host port");
			System.exit(-1);
		}
		try { /* get input parameters */
			host = args[0];
			port = Integer.parseInt(args[1]);
		} catch (IllegalArgumentException e) {
			System.out.println("USAGE: java client host port");
			System.exit(-1);
		}

		try { /* set up a key manager for client authentication */
			SSLSocketFactory factory = null;
			BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
			try {
				System.out.println("Input user ID:");
				System.out.print("> ");
				String userID = read.readLine();
				System.out.println("Input password:");
				System.out.print("> ");
				char[] password = read.readLine().toCharArray();
				char[] tspassword = "password".toCharArray();
				KeyStore ks = KeyStore.getInstance("JKS");
				KeyStore ts = KeyStore.getInstance("JKS");
				KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
				TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
				SSLContext ctx = SSLContext.getInstance("TLS");

				ks.load(new FileInputStream(userID + "keystore"), password); // keystore
																				// password
																				// (storepass)
				ts.load(new FileInputStream("clienttruststore"), tspassword); // truststore
																				// password
																				// (storepass);
				kmf.init(ks, password); // user password (keypass)
				tmf.init(ts); // keystore can be used as truststore here
				ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
				factory = ctx.getSocketFactory();
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			}
			SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
			System.out.println("\nsocket before handshake:\n" + socket + "\n");

			/*
			 * send http request
			 *
			 * See SSLSocketClient.java for more information about why there is
			 * a forced handshake here when using PrintWriters.
			 */
			socket.startHandshake();

			SSLSession session = socket.getSession();
			X509Certificate cert = (X509Certificate) session.getPeerCertificateChain()[0];
			String subject = cert.getSubjectDN().getName();
			String issuer = cert.getIssuerDN().getName();
			String serial = cert.getSerialNumber().toString();
			System.out.println(
					"certificate name (subject DN field) on certificate received from server:\n" + subject + "\n");
			System.out.println("issuer name (issuer DN field): " + issuer);
			System.out.println("serial number (serial number field): " + serial);
			System.out.println("socket after handshake:\n" + socket + "\n");
			System.out.println("secure connection established\n\n");

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String msg;
			// Current design states that a request worded as "task:user"
			// If more is required further should be requested as with Write
			// Client is assumed to be the user Doctor1
			// Entire request is thusly "action:targer" (+":log entry" for
			// read)
			System.out.println("Temp menu: \nExit: 0\nRead: 1:person\nWrite: 2:person");
			for (;;) {
				System.out.print("> ");
				msg = read.readLine();
				if (msg.charAt(0) == '0') {
					break;
				} else if (msg.charAt(0) == '2') {
					System.out.println("New entry in " + msg.split(":")[1] + "'s log:");
					System.out.print("> ");
					msg = msg + ":" + read.readLine();
				}
				out.println(msg);
				out.flush();
				System.out.println("Request sent.\n...");
				String temp = in.readLine();
				while (temp.compareTo("EOF") != 0) {
					System.out.println(temp);
					temp = in.readLine();
				}
			}

			/*
			 * This is the original request System.out.print("sending '" + msg +
			 * "' to server..."); out.println(msg); out.flush();
			 * System.out.println("done"); System.out.println("received '" +
			 * in.readLine() + "' from server\n");
			 */

			in.close();
			out.close();
			read.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
