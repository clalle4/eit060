package server;

public class Main {

	public static void main(String[] args) {
		System.out.println("godkänt, you can all go home now   ヽ(͡◕ ͜ʖ ͡◕)ﾉ");
	//	Hub h = new Hub();
		 test(); // use to test your methods, if u work on "Main" just remove
		// this
	}

	 public static void test(){
		 Doctor a = new Doctor("a");
		 if(a.createPatient("ABBA")){
			 System.out.println("success");
		 }else{System.out.println("denied");}
	 }

}
