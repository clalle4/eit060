package server;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.KeyStore;
import javax.net.*;
import javax.net.ssl.*;
import javax.security.cert.X509Certificate;

public class server implements Runnable {
	private ServerSocket serverSocket = null;
	private static int numConnectedClients = 0;
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
			numConnectedClients++;
			System.out.println("client connected");
			System.out.println("client name (cert subject DN field): " + subject);
			System.out.println("issuer name (cert issuer DN field): " + issuer);
			System.out.println("serial number (serial number field): " + serial);

			System.out.println(numConnectedClients + " concurrent connection(s)\n");
			
			hub = new Hub();
			PrintWriter out = null;
			BufferedReader in = null;
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String clientMsg = null;
			boolean auth = false;
			String login = "0:0";
			while(!auth){
			login = in.readLine();
			auth = hub.login(login.split(":"));
			out.println(auth);
			out.flush();
			}
			while ((clientMsg = in.readLine()) != null) {
				try{				
					System.out.println(clientMsg);
					String[] MsgContent = (clientMsg+":"+login).split(":");
				switch(clientMsg.charAt(0)){
				case '1':
					out.println(handleRead(MsgContent));
					out.flush();
					break;
				case '2':
					out.println(handleWrite(MsgContent));
					out.flush();
					break;
				}
				}catch(NullPointerException e){
					e.printStackTrace();
					//out.println("Wrong format, please review line.");
					//out.flush();
				}
				/* The old program
				String rev = new StringBuilder(clientMsg).reverse().toString();
				System.out.println("received '" + clientMsg + "' from client");
				System.out.print("sending '" + rev + "' to client...");
				out.println(rev);
				out.flush();
				System.out.println("done\n");
				 */
			}
			in.close();
			out.close();
			socket.close();
			numConnectedClients--;
			System.out.println("client disconnected");
			System.out.println(numConnectedClients + " concurrent connection(s)\n");
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
	private String handleRead(String[] request){
		return ("Log follows:\n"+hub.readRequest(request)+"\nEOF");
	}
	private String handleWrite(String[] request){
		
		return request[1]+" log written: '" +request[2]+"'";
	}
}
