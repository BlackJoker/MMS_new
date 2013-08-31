package de.team55.mms.data;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Nachricht")
public class Nachricht {

	private int id;
	private String absender;
	private int absenderID;
	private String betreff;
	private Date datum;
	private boolean gelesen;
	private String empfaenger;
	private int empfaengerID;
	private String nachricht;

	public Nachricht(int id, int absenderID, int empfaengerID, String betreff, Date datum, boolean gelesen, String nachricht) {
		this.id = id;
		this.absenderID = absenderID;
		this.empfaengerID = empfaengerID;
		this.betreff = betreff;
		this.datum = datum;
		this.gelesen = gelesen;
		this.nachricht = nachricht;
	}

	public Nachricht() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAbsender() {
		return absender;
	}

	public int getAbsenderID() {
		return absenderID;
	}

	public String getBetreff() {
		return betreff;
	}

	public Date getDatum() {
		return datum;
	}

	public String getDatumString() {
		return datum.toString();
	}

	public String getEmpfaenger() {
		return empfaenger;
	}

	public int getEmpfaengerID() {
		return empfaengerID;
	}

	public String getNachricht() {
		return nachricht;

	}

	public boolean isGelesen() {
		return gelesen;
	}

	public void setAbsender(String absender) {
		this.absender = absender;
	}

	public void setAbsenderID(int absenderID) {
		this.absenderID = absenderID;
	}

	public void setBetreff(String betreff) {
		this.betreff = betreff;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public void setEmpfaenger(String empfaenger) {
		this.empfaenger = empfaenger;
	}

	public void setEmpfaengerID(int empfaengerID) {
		this.empfaengerID = empfaengerID;
	}

	public void setGelesen(boolean gelesen) {
		this.gelesen = gelesen;
	}

	public void setNachricht(String nachricht) {
		this.nachricht = nachricht;
	}

	@Override
	public String toString() {
		return "Nachricht [absender=" + absender + ", betreff=" + betreff + ", datum=" + datum + ", gelesen=" + gelesen + ", empfaenger="
				+ empfaenger + ", nachricht=" + nachricht + "]";
	}

}
