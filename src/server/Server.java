package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.security.KeyStore;
import java.sql.Timestamp;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.X509Certificate;

public class Server implements Runnable {
	private ServerSocket serverSocket = null;
	private static int NBRCONNECTEDCLIENTS = 0;
	private Hub hub;

	public Server(ServerSocket ss) throws IOException {
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
			String login = cert.getSubjectDN().getName().substring(3);
			// program start. IMPORTANT: all returns to client must end with
			// "\nEOF" to signify the end of the action, regardless of
			// success
			// or failure!
			while ((clientMsg = in.readLine()) != null) {
				String lastAction = "failed";
				
				try {
					String[] MsgContent = (clientMsg).split(":");
					System.out.println(clientMsg);
					if (clientMsg.charAt(0) > '9' || clientMsg.charAt(0) < '0') {
						out.println("Wrong format, please review line.\nEOF");
						out.flush();
					} else {
						switch (clientMsg.charAt(0)) {
						case '1':
							if(MsgContent.length != 2){
								throw new NullPointerException();
							}
							out.println(handleRead(MsgContent, login));
							out.flush();
							lastAction = "Read File Of User '"+MsgContent[1]+"'";
							break;
						case '2':
							if(MsgContent.length != 3){
								throw new NullPointerException();
							}
							out.println(handleWrite(MsgContent, login) + "\nEOF");
							out.flush();
							lastAction = "Write New Log in file '"+MsgContent[1]+"'";
							break;

						case '3':
							if(MsgContent.length != 1){
								throw new NullPointerException();
							}
							out.println("Your rights are:\n "+getRights(login) + "\nEOF");
							out.flush();
							lastAction = "Get Rights";
							break;
						case '4':
							if(MsgContent.length != 6){
								throw new NullPointerException();
							}
							switch (createPatient(MsgContent, login)) {
							case 0:
								out.println("Patient " + MsgContent[1] + " Created." + "\nEOF");
								lastAction = "Create New User '"+MsgContent[1]+"'";
								break;
							case 1:
								out.println("ERROR: You are not a doctor." + "\nEOF");
								break;
							case 2:
								out.println("ERROR: Nurse does not exist." + "\nEOF");
								break;
							case 3:
								out.println("ERROR: Patient already exist." + "\nEOF");
								break;
							case 4:
								out.println("ERROR: Division format is wrong, please enter a single number instead."
										+ "\nEOF");
								break;
							}
							out.flush();
							break;
						case '5':
							if(MsgContent.length != 2){
								throw new NullPointerException();
							}
							if(handleRemove(MsgContent, login)){
								out.println(MsgContent[1]+" removed.\nEOF");
								lastAction = "Remove User '"+MsgContent[1]+"'";
							}else{
								System.out.println("user not authorized or file nonexistant\nEOF");
							}
							break;
						}
						
					}
				} catch (NullPointerException e) {
					out.println("Wrong format, please review line.\nEOF");
					out.flush();
				}
				/*
				 * The old program String rev = new
				 * StringBuilder(clientMsg).reverse().toString();
				 * System.out.println("received '" + clientMsg + "' from client"
				 * ); System.out.print("sending '" + rev + "' to client...");
				 * out.println(rev); out.flush(); System.out.println("done\n");
				 */
				if(lastAction.compareTo("failed") != 0){
					BufferedWriter pw = new BufferedWriter(new FileWriter("./files/auditLog.txt",true));
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					pw.append("[" + timestamp.toString() + "]: " + "Action "+lastAction+" performed by: "+login+".\n");
					pw.close();
				}
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
			new Server(ss);
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

	private int createPatient(String[] request, String login) {
		return hub.createPatient(request, login);
	}

	private String getRights(String login) {
		return hub.getRights(login);
	}

	private boolean handleRemove(String[] msg, String login){
		return hub.removeUser(msg, login);
		
	}
}
