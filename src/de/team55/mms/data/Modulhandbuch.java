package de.team55.mms.data;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Modulhandbuch")
public class Modulhandbuch {

	private int id;
	private String name;
	private String prosa;
	private String jahrgang;
	private int pruefungsordnungsjahr;
	private boolean akzeptiert;
	private ArrayList<Fach> fach = new ArrayList<Fach>();
	
	public Modulhandbuch(int id, String jahrgang, String prosa, int pordnung) {
		this.id = id;
		this.jahrgang = jahrgang;
		this.prosa = prosa;
		this.pruefungsordnungsjahr = pordnung;
	}
	
	public Modulhandbuch(int id, String jahrgang, String prosa, int pordnung, ArrayList<Fach> fach) {
		this.id = id;
		this.jahrgang = jahrgang;
		this.prosa = prosa;
		this.pruefungsordnungsjahr = pordnung;
		this.fach = new ArrayList<Fach>(fach);
	}
	
	public Modulhandbuch(){
		
	}

	
	@Override
	public String toString() {
		if(name!=null){
			return name+", PO "+pruefungsordnungsjahr+", " +jahrgang;
		}
		return "PO "+pruefungsordnungsjahr+", " +jahrgang;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (akzeptiert ? 1231 : 1237);
		result = prime * result
				+ ((jahrgang == null) ? 0 : jahrgang.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Modulhandbuch other = (Modulhandbuch) obj;
		if (akzeptiert != other.akzeptiert)
			return false;
		if (jahrgang == null) {
			if (other.jahrgang != null)
				return false;
		} else if (!jahrgang.equals(other.jahrgang))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public boolean isAkzeptiert() {
		return akzeptiert;
	}

	public void setAkzeptiert(boolean akzeptiert) {
		this.akzeptiert = akzeptiert;
	}

//	public Modulhandbuch(String name, String studiengang, String jahrgang, boolean akzeptiert) {
//		this.name = name;
//		this.studiengang = studiengang;
//		this.jahrgang = jahrgang;
//		this.akzeptiert=akzeptiert;
//	}


	public String getName() {
		return name;
	}

//	public String getStudiengang() {
//		return studiengang;
//	}

	public String getJahrgang() {
		return jahrgang;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public void setStudiengang(String studiengang) {
//		this.studiengang = studiengang;
//	}

	public void setJahrgang(String jahrgang) {
		this.jahrgang = jahrgang;
	}

	public String getProsa() {
		return prosa;
	}

	public void setProsa(String prosa) {
		this.prosa = prosa;
	}

	public int getPruefungsordnungsjahr() {
		return pruefungsordnungsjahr;
	}

	public void setPruefungsordnungsjahr(int pruefungsordnungsjahr) {
		this.pruefungsordnungsjahr = pruefungsordnungsjahr;
	}

	public ArrayList<Fach> getFach() {
		return fach;
	}

	public void setFach(ArrayList<Fach> fach) {
		this.fach = new ArrayList<Fach>(fach);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
