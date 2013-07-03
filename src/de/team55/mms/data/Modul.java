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

//	private ArrayList<String> labels = new ArrayList<String>();
//	private ArrayList<String> values = new ArrayList<String>();
//	private ArrayList<Studiengang> studiengang;
//	private ArrayList<Boolean> dezernat = new ArrayList<Boolean>();
//	private ArrayList<Modulhandbuch> modulhandbuch = new ArrayList<Modulhandbuch>();
//	
	public Modul() {

	}
	
//	public Modul(String name, ArrayList<Studiengang> studiengang,
//			String modulhandbuch, int jahrgang, ArrayList<String> labels,
//			ArrayList<String> values, int version, ArrayList<Boolean> dez,
//			String user) {
//		this.name = name;
//		this.studiengang = studiengang;
//		this.jahrgang = jahrgang;
//		this.labels = labels;
//		this.values = values;
//		this.version = version;
//		this.datum = new Date();
//		this.dezernat = dez;
//		this.akzeptiert = false;
//		this.inbearbeitung = false;
//		this.user = user;
//	}
//
//	public Modul(String name, ArrayList<Studiengang> studiengang2,
//			String modulhandbuch, int version, Date datum,
//			ArrayList<String> labels, ArrayList<String> values,
//			ArrayList<Boolean> dez, boolean akzeptiert, boolean inbearbeitung,
//			String user) {
//		this.name = name;
//		this.studiengang = studiengang2;
//		this.version = version;
//		this.datum = datum;
//		this.labels = labels;
//		this.values = values;
//		this.dezernat = dez;
//		this.akzeptiert = akzeptiert;
//		this.inbearbeitung = inbearbeitung;
//		this.user = user;
//	}

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

//	public Modul(String name, ArrayList<Zuordnung> zlist, int jahrgang,
//			ArrayList<String> labels, ArrayList<String> values, int version,
//			ArrayList<Boolean> dezernat, String user) {
//		this.name = name;
//		this.zuordnungen = zlist;
//		this.jahrgang = jahrgang;
//		this.labels = labels;
//		this.values = values;
//		this.dezernat = dezernat;
//		this.version = version;
//		this.datum = new Date();
//		this.user = user;
//	}

//	public Modul(String name2, ArrayList<Zuordnung> zs, int jg,
//			ArrayList<String> labels2, ArrayList<String> values2, int version2,
//			ArrayList<Boolean> dezernat2, String user2, Date datum2) {
//		this.name = name2;
//		this.zuordnungen = zs;
//		this.jahrgang = jg;
//		this.labels = labels2;
//		this.values = values2;
//		this.version = version2;
//		this.dezernat = dezernat2;
//		this.user = user2;
//		this.datum = datum2;
//	}

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

//	public ArrayList<Boolean> getDezernat() {
//		return dezernat;
//	}

	@XmlElementWrapper(name = "felder")
	@XmlElement(name = "feld")
	public ArrayList<Feld> getFelder() {
		return felder;
	}

	public int getJahrgang() {
		return jahrgang;
	}

//	public ArrayList<String> getLabels() {
//		return labels;
//	}
//
//	public ArrayList<Modulhandbuch> getModulhandbuch() {
//		return modulhandbuch;
//	}

	public String getName() {
		return name;
	}

//	public ArrayList<Studiengang> getStudiengang() {
//		return studiengang;
//	}

	public String getUser() {
		return user;
	}

//	public ArrayList<String> getValues() {
//		return values;
//	}

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

//	public void setDezernat(ArrayList<Boolean> dezernat) {
//		this.dezernat = dezernat;
//	}

	public void setFelder(ArrayList<Feld> felder) {
		this.felder = felder;
	}

	public void setInbearbeitung(boolean inbearbeitung) {
		this.inbearbeitung = inbearbeitung;
	}

	public void setJahrgang(int jahrgang) {
		this.jahrgang = jahrgang;
	}

//	public void setLabels(ArrayList<String> labels) {
//		this.labels = labels;
//	}
//
//	public void setModulhandbuch(ArrayList<Modulhandbuch> modulhandbuch) {
//		this.modulhandbuch = modulhandbuch;
//	}

	public void setName(String name) {
		this.name = name;
	}

//	public void setStudiengang(ArrayList<Studiengang> studiengang) {
//		this.studiengang = studiengang;
//	}

	public void setUser(String user) {
		this.user = user;
	}

//	public void setValues(ArrayList<String> values) {
//		this.values = values;
//	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setZuordnungen(ArrayList<Zuordnung> zuordnungen) {
		this.zuordnungen = zuordnungen;
	}

	@Override
	public String toString() {
		return "Modul [name=" + name + ", jahrgang=" + jahrgang + ", version="
				+ version + ", datum=" + datum + ", akzeptiert=" + akzeptiert
				+ ", inbearbeitung=" + inbearbeitung + ", user=" + user
				+ ", zuordnungen=" + zuordnungen + ", felder=" + felder + "]";
	}

//	@Override
//	public String toString() {
//		return name;
//	}

}
