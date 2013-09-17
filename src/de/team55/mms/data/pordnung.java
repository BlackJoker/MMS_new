package de.team55.mms.data;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "pordnung")
public class pordnung {
	private int id;
	private int sid;
	private int pjahr;
	private String studname;
	private String studabschluss;
	
	public pordnung(){}
	
	public pordnung(int id, int pjahr, int sid){
		this.id = id;
		this.sid = id;
		this.pjahr = pjahr;
	}
	
	public pordnung(int id, int pjahr, int sid, String studname, String studabschluss){
		this.id = id;
		this.sid = id;
		this.pjahr = pjahr;
		this.studname = studname;
		this.studabschluss = studabschluss;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return studname+" "+studabschluss+" PO:"+pjahr;
	}

	public String getStudname() {
		return studname;
	}

	public void setStudname(String studname) {
		this.studname = studname;
	}

	public String getStudabschluss() {
		return studabschluss;
	}

	public void setStudabschluss(String studabschluss) {
		this.studabschluss = studabschluss;
	}

	public int getPjahr() {
		return pjahr;
	}

	public void setPjahr(int pjahr) {
		this.pjahr = pjahr;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
