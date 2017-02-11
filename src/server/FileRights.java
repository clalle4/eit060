package server;

public class FileRights {
	
	private String name;
	private boolean read;
	private boolean write;
	
	public FileRights(String name, boolean read, boolean write){
		this.name = name;
		this.read=read;
		this.write=write;
		
	}
	public String toString(){
		StringBuilder sb = new StringBuilder("Name: "+name+"   Read: "+read+"   Write: "+write);
		return sb.toString();
	}
	

}
