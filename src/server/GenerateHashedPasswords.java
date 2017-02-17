package server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import javax.crypto.*;

public class GenerateHashedPasswords {
	public static void main(String[] args) {
		//Random för att kunna skapa slumpat salt
		Random rand = new Random();
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Lösenorden i klartext + salt
		String alicePass =  "%Alice*Pass";
		System.out.println("Alices okrypterade lösenord: " + alicePass);
		
//		String bobPass = String.valueOf(rand.nextInt(10)) + String.valueOf(rand.nextInt(10)) + "%Bob*Pass";
//		System.out.println("Bobs okrypterade lösenord: " + bobPass);
//		String connorPass = String.valueOf(rand.nextInt(10)) + String.valueOf(rand.nextInt(10)) + "%Connor*Pass";
//		System.out.println("Connors okrypterade lösenord: " + connorPass);
//		String demiPass = String.valueOf(rand.nextInt(10)) + String.valueOf(rand.nextInt(10)) + "%Demi*Pass";
//		System.out.println("Demis okrypterade lösenord: " + demiPass);
		
		System.out.println("\n");
		
		//Lösenorden hashas
		String salt = String.valueOf(rand.nextInt(10)) + String.valueOf(rand.nextInt(10));
		
		byte[] aliceHash = alicePass.getBytes();
		md.update(aliceHash);
		md.update(salt.getBytes());
		aliceHash = md.digest(aliceHash);
		System.out.println("Alices krypterade lösenord: " + aliceHash);
		
		System.out.println("\n");
		
//		User u = new Patient("Alice");
//		if (u.authenticate("Alice", "%Alice*Pass")) {
//			System.out.println("Autentiseringsmetoden fungerar. :)");
//		} else {
//			System.out.println("Autentiseringsmetoden fungerar inte. :(");
//		}
	}
}
