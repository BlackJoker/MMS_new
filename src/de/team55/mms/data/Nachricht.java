package de.team55.mms.data;

import java.util.Date;

public class Nachricht {

	@Override
	public String toString() {
		return "Nachricht [absender=" + absender + ", betreff=" + betreff + ", datum=" + datum + ", gelesen=" + gelesen + ", empfaenger="
				+ empfaenger + ", nachricht=" + nachricht + "]";
	}

	private String absender;
	private String betreff;
	private Date datum;
	private boolean gelesen;
	private String empfaenger;
	private String nachricht;

	public Nachricht(String absender, String empfaenger, String betreff, Date datum, boolean gelesen, String nachricht) {
		super();
		this.absender = absender;
		this.empfaenger=empfaenger;
		this.betreff = betreff;
		this.datum = datum;
		this.gelesen = gelesen;
		this.nachricht = nachricht;
	}

	public String getEmpfaenger() {
		return empfaenger;
	}

	public void setEmpfaenger(String empfaenger) {
		this.empfaenger = empfaenger;
	}

	public String getAbsender() {
		if (gelesen) {
			return absender;
		} else {
			return "<html><b>" + absender + "</b></html>";
		}
	}

	public void setAbsender(String absender) {
		this.absender = absender;
	}

	public String getBetreff() {
		if (gelesen) {
			return betreff;
		} else {
			return "<html><b>" + betreff + "</b></html>";
		}
	}

	public void setBetreff(String betreff) {
		this.betreff = betreff;
	}

	public String getDatumString() {
		if (gelesen) {
			return datum.toString();
		} else {
			return "<html><b>" + datum.toString() + "</b></html>";
		}
	}
	
	public Date getDatum(){
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public boolean isGelesen() {
		return gelesen;
	}

	public void setGelesen(boolean gelesen) {
		this.gelesen = gelesen;
	}

	public String getNachricht() {
		return nachricht;

	}

	public void setNachricht(String nachricht) {
		this.nachricht = nachricht;
	}

}
