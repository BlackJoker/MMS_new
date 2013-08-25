package de.team55.mms.data;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Stellvertreter")
public class StellvertreterList {

	private String modul;
	private ArrayList<User> usr;
	
	public StellvertreterList(){
	}
	
	public String getModul() {
		return modul;
	}
	public void setModul(String modul) {
		this.modul = modul;
	}
	public ArrayList<User> getUsr() {
		return usr;
	}
	public void setUsr(ArrayList<User> usr) {
		this.usr = usr;
	}

	

}
