package de.team55.mms.data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Modulhandbuch")
public class Modulhandbuch {

	private String name;
	//private String studiengang;
	private String jahrgang;
	private int studiengang;
	private boolean akzeptiert;
	
	public Modulhandbuch(String jahrgang, int studiengang) {
		this.jahrgang=jahrgang;
		this.studiengang=studiengang;
	}
	
	public Modulhandbuch(){
		
	}

//	public Modulhandbuch(String name, String studiengang, String jahrgang) {
//		this.name = name;
//		this.studiengang = studiengang;
//		this.jahrgang = jahrgang;
//		this.akzeptiert = false;
//	}
	
	@Override
	public String toString() {
		return name + ", " + studiengang
				+ ", " + jahrgang;
	}

	public int getStudiengang() {
		return studiengang;
	}

	public void setStudiengang(int studiengang) {
		this.studiengang = studiengang;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (akzeptiert ? 1231 : 1237);
		result = prime * result
				+ ((jahrgang == null) ? 0 : jahrgang.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + studiengang;
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
		if (studiengang != other.studiengang)
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

}
