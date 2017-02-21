package server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.security.KeyStore;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.X509Certificate;

public class server implements Runnable {
	private ServerSocket serverSocket = null;
	private static int NBRCONNECTEDCLIENTS = 0;
	private Hub hub;

	public server(ServerSocket ss) throws IOException {
		serverSocket = ss;
		newListener();
	}

	public void run() {
		try {
			SSLSocket socket = (SSLSocket) serverSocket.accept();
			newListener();
			SSLSession session = socket.getSession();
			X509Certificate cert = (X509Certificate) session.getPeerCertificateChain()[0];
			String subject = cert.getSubjectDN().getName();
			String issuer = cert.getIssuerDN().getName();
			String serial = cert.getSerialNumber().toString();
			NBRCONNECTEDCLIENTS++;
			System.out.println("client connected");
			System.out.println("client name (cert subject DN field): " + subject);
			System.out.println("issuer name (cert issuer DN field): " + issuer);
			System.out.println("serial number (serial number field): " + serial);

			System.out.println(NBRCONNECTEDCLIENTS + " concurrent connection(s)\n");

			hub = new Hub();
			PrintWriter out = null;
			BufferedReader in = null;
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String clientMsg = null;
			String login = cert.getSubjectDN().getName();
			while ((clientMsg = in.readLine()) != null) {
				try {
					String[] MsgContent = (clientMsg).split(":");
					System.out.println(clientMsg);
					if (clientMsg.charAt(0) > '9' || clientMsg.charAt(0) < '0') {
						out.println("Wrong format, please review line.\nEOF");
						out.flush();
					}else{
					switch (clientMsg.charAt(0)) {
					case '1':
						out.println(handleRead(MsgContent, login ));
						out.flush();
						break;
					case '2':
						out.println(handleWrite(MsgContent, login) + "\nEOF");
						out.flush();
						break;
					}
					}
				} catch (NullPointerException e) {
					out.println("Wrong format, please review line.\nEOF");
					out.flush();
					e.printStackTrace();
				}
				/*
				 * The old program String rev = new
				 * StringBuilder(clientMsg).reverse().toString();
				 * System.out.println("received '" + clientMsg + "' from client"
				 * ); System.out.print("sending '" + rev + "' to client...");
				 * out.println(rev); out.flush(); System.out.println("done\n");
				 */
			}
			in.close();
			out.close();
			socket.close();
			NBRCONNECTEDCLIENTS--;
			System.out.println("client disconnected");
			System.out.println(NBRCONNECTEDCLIENTS + " concurrent connection(s)\n");
		} catch (IOException e) {
			System.out.println("Client died: " + e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	private void newListener() {
		(new Thread(this)).start();
	} // calls run()

	public static void main(String args[]) {
		System.out.println("\nServer Started\n");
		int port = -1;
		if (args.length >= 1) {
			port = Integer.parseInt(args[0]);
		}
		String type = "TLS";
		try {
			ServerSocketFactory ssf = getServerSocketFactory(type);
			ServerSocket ss = ssf.createServerSocket(port);
			((SSLServerSocket) ss).setNeedClientAuth(true); // enables client
															// authentication
			new server(ss);
		} catch (IOException e) {
			System.out.println("Unable to start Server: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static ServerSocketFactory getServerSocketFactory(String type) {
		if (type.equals("TLS")) {
			SSLServerSocketFactory ssf = null;
			try { // set up key manager to perform server authentication
				SSLContext ctx = SSLContext.getInstance("TLS");
				KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
				TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
				KeyStore ks = KeyStore.getInstance("JKS");
				KeyStore ts = KeyStore.getInstance("JKS");
				char[] password = "password".toCharArray();

				ks.load(new FileInputStream("serverkeystore"), password); // keystore
																			// password
																			// (storepass)
				ts.load(new FileInputStream("servertruststore"), password); // truststore
																			// password
																			// (storepass)
				kmf.init(ks, password); // certificate password (keypass)
				tmf.init(ts); // possible to use keystore as truststore here
				ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
				ssf = ctx.getServerSocketFactory();
				return ssf;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return ServerSocketFactory.getDefault();
		}
		return null;
	}

	private String handleRead(String[] request, String login) {
		return ("Log follows:\n" + hub.readRequest(request, login) + "\nEOF");
	}

	private String handleWrite(String[] request, String login) {
		return hub.writeRequest(request, login);
	}
}
