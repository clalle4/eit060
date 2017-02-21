package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public abstract class User {
	
	protected String name;
	private String password;
	private byte[] salt;
	private static Random rand;

	public User(String name) {
		this.name = name;
		salt = new byte[16];
		rand = new Random();
		rand.nextBytes(this.salt);
		try {
			password = hashPassword(password, salt);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			System.err.println("Could not hash password :(");
		}
	}

	/**
	 * Remove this for the final version
	 **/
	public String toString() {
		return name;
	}

	/**
	 * Writes the content of a patient's records
	 * 
	 * @param FILENAME
	 * The name of the patient whose records you want to access
	 */
	public String read(String FILENAME) {
		StringBuilder contents = new StringBuilder();
		if (isReadRequestAvailable(FILENAME)) {
			StringBuilder sb = new StringBuilder("./files/PatientRecords/");
			sb.append(FILENAME);
			sb.append(".txt");

			try {
				BufferedReader br = new BufferedReader(new FileReader(sb.toString()));
				String currentLine = br.readLine();
				if (currentLine != null) {
					contents.append(currentLine);
					currentLine = br.readLine();
					
					while (currentLine != null) {
						contents.append(System.lineSeparator() + currentLine);
						currentLine = br.readLine();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return contents.toString();

		}
		contents.append("You are not allowed to access that file");
		return contents.toString();
	}

	/** Same authentication process for all users, implemented only here */
	protected boolean authenticate(String username, String password) {
		MessageDigest md;
		
		try {
			md = MessageDigest.getInstance("SHA-512");
			byte[] hash = password.getBytes();
			md.update(hash);
			hash = md.digest(hash);
			System.out.println("Test av hash: " + hash);
			
			File f = new File("./files/PatientRecords/");
			File[] patientRecordList = f.listFiles();
			
			StringBuilder contents = new StringBuilder();
			BufferedReader br = null;

			if (patientRecordList != null) {
				
				for (File fil : patientRecordList) {
					
					if (name.equals(fil.getName().replaceAll(".txt", ""))) {

						try {
							br = new BufferedReader(new FileReader("./files/PatientRecords/" + name + ".txt"));
							String currentLine = br.readLine();
							currentLine = br.readLine();
							String filePassword = currentLine.substring(10);
							//String passWithSalt = currentLine.substring(10);
							//String passWithoutSalt;
							//passWithoutSalt = passWithSalt.substring(2);
							//byte[] bytePass = passWithoutSalt.getBytes();
							byte[] bytePass = filePassword.getBytes();
							if (MessageDigest.isEqual(hash, bytePass)) {
								return true;
							} else {
								return false;
							}
						} catch (IOException e) {
							e.printStackTrace();
							System.err.println("Could not find user.");
						} finally {
							try {
								br.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = f.generateSecret(spec).getEncoded();
		Base64.Encoder enc = Base64.getEncoder();
		return enc.encodeToString(hash);
	}

	/**
	 * A double check method to be called when "read" is called, to make sure
	 * user is allowed access to file
	 **/
	protected abstract boolean isReadRequestAvailable(String FILENAME);

	protected abstract ArrayList<FileRights> listAvailableFiles();

}
