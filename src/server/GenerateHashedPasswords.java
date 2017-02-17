package server;

import java.util.Random;

public class GenerateHashedPasswords {
	public static void main(String[] args) {
		//Random för att kunna skapa slumpat salt
		Random rand = new Random();
		
		//Lösenorden i klartext + salt
		String alicePass = String.valueOf(rand.nextInt(10)) + String.valueOf(rand.nextInt(10)) + "%Alice*Pass";
		System.out.println("Alices okrypterade lösenord: " + alicePass);
		String bobPass = String.valueOf(rand.nextInt(10)) + String.valueOf(rand.nextInt(10)) + "%Bob*Pass";
		System.out.println("Bobs okrypterade lösenord: " + bobPass);
		String connorPass = String.valueOf(rand.nextInt(10)) + String.valueOf(rand.nextInt(10)) + "%Connor*Pass";
		System.out.println("Connors okrypterade lösenord: " + connorPass);
		String demiPass = String.valueOf(rand.nextInt(10)) + String.valueOf(rand.nextInt(10)) + "%Demi*Pass";
		System.out.println("Demis okrypterade lösenord: " + demiPass);
		System.out.println("\n");
		
		//Lösenorden hashas
		int aliceHash = alicePass.hashCode();
		System.out.println("Alices hashade lösenord: " + aliceHash);
		aliceHash = alicePass.hashCode();
		System.out.println("Alices hashade lösenord: " + aliceHash);
		int bobHash = bobPass.hashCode();
		System.out.println("Bobs hashade lösenord: " + bobHash);
		int connorHash = connorPass.hashCode();
		System.out.println("Connors hashade lösenord: " + connorHash);
		int demiHash = demiPass.hashCode();
		System.out.println("Demis hashade lösenord: " + demiHash);
		System.out.println("\n");
		
		User u = new Patient("Alice");
		if (u.authenticate("Alice", "%Alice*Pass")) {
			System.out.println("Autentiseringsmetoden fungerar. :)");
		} else {
			System.out.println("Autentiseringsmetoden fungerar inte. :(");
		}
		
	}
}
