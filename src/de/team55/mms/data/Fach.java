package de.team55.mms.data;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Fach")
public class Fach {
	
	private String name;
	private ArrayList<Modul> modlist = new ArrayList<Modul>();
	
	public Fach(){}
	
	public Fach(String name){
		this.name = name;
	}
	
	public Fach(String name, ArrayList<Modul> modlist){
		
		this.name = name;
		this.modlist = new ArrayList<Modul>(modlist);
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Modul> getModlist() {
		return modlist;
	}
	public void setModlist(ArrayList<Modul> modlist) {
		this.modlist = new ArrayList<Modul>(modlist);
	}
	@Override
	public String toString(){
		return name;
		
	}
	
}
