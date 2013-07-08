package de.team55.mms.data;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import de.team55.mms.data.Zuordnung;

@XmlRootElement(name = "Modul")
@XmlType(propOrder = { "name", "version", "jahrgang", "user","inbearbeitung","akzeptiert", "datum","zuordnungen","felder" })
public class Modul {

	private String name;
	private int jahrgang;
	private int version;
	private Date datum;
	private boolean akzeptiert = false;
	private boolean inbearbeitung = false;
	private String user;
	private ArrayList<Zuordnung> zuordnungen = new ArrayList<Zuordnung>();
	private ArrayList<Feld> felder = new ArrayList<Feld>();

	public Modul(String name2, ArrayList<Zuordnung> zs, int jahrgang2,
			ArrayList<Feld> felder, int version2, Date datum2,
			boolean akzeptiert2, boolean inbearbeitung2, String user2) {
		this.name = name2;
		this.zuordnungen = zs;
		this.jahrgang = jahrgang2;
		this.felder = felder;
		this.version = version2;
		this.datum = datum2;
		this.akzeptiert = akzeptiert2;
		this.inbearbeitung = inbearbeitung2;
		this.user = user2;
	}
	
	public Modul(String name2, int jahrgang2,
			ArrayList<Feld> felder, int version2, Date datum2,
			boolean akzeptiert2, boolean inbearbeitung2, String user2) {
		this.name = name2;
		this.jahrgang = jahrgang2;
		this.felder = felder;
		this.version = version2;
		this.datum = datum2;
		this.akzeptiert = akzeptiert2;
		this.inbearbeitung = inbearbeitung2;
		this.user = user2;
	}
	
	public Modul() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Modul other = (Modul) obj;
		if (akzeptiert != other.akzeptiert)
			return false;
		if (datum == null) {
			if (other.datum != null)
				return false;
		} else if (!datum.equals(other.datum))
			return false;
		if (felder == null) {
			if (other.felder != null)
				return false;
		} else if (!felder.equals(other.felder))
			return false;
		if (inbearbeitung != other.inbearbeitung)
			return false;
		if (jahrgang != other.jahrgang)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (version != other.version)
			return false;
		if (zuordnungen == null) {
			if (other.zuordnungen != null)
				return false;
		} else if (!zuordnungen.equals(other.zuordnungen))
			return false;
		return true;
	}

	public Date getDatum() {
		return datum;
	}

	@XmlElementWrapper(name = "felder")
	@XmlElement(name = "feld")
	public ArrayList<Feld> getFelder() {
		return felder;
	}

	public int getJahrgang() {
		return jahrgang;
	}

	public String getName() {
		return name;
	}

	public String getUser() {
		return user;
	}

	public int getVersion() {
		return version;
	}
	
	@XmlElementWrapper(name = "zuordnungen")
	@XmlElement(name = "zuordnung")
	public ArrayList<Zuordnung> getZuordnungen() {
		return zuordnungen;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (akzeptiert ? 1231 : 1237);
		result = prime * result + ((datum == null) ? 0 : datum.hashCode());
		result = prime * result + ((felder == null) ? 0 : felder.hashCode());
		result = prime * result + (inbearbeitung ? 1231 : 1237);
		result = prime * result + jahrgang;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + version;
		result = prime * result
				+ ((zuordnungen == null) ? 0 : zuordnungen.hashCode());
		return result;
	}

	public boolean isAkzeptiert() {
		return akzeptiert;
	}

	public boolean isInbearbeitung() {
		return inbearbeitung;
	}

	public void setAkzeptiert(boolean akzeptiert) {
		this.akzeptiert = akzeptiert;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public void setFelder(ArrayList<Feld> felder) {
		this.felder = felder;
	}

	public void setInbearbeitung(boolean inbearbeitung) {
		this.inbearbeitung = inbearbeitung;
	}

	public void setJahrgang(int jahrgang) {
		this.jahrgang = jahrgang;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setZuordnungen(ArrayList<Zuordnung> zuordnungen) {
		this.zuordnungen = zuordnungen;
	}

	@Override
	public String toString() {
		return name+", erstellt von: "+user;
	}

}
