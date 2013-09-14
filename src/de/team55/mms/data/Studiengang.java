package de.team55.mms.data;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Studiengang")
public class Studiengang {
	private int id;
	private String name;
	private String abschluss;
	private ArrayList<Modulhandbuch> modbuch = new ArrayList<Modulhandbuch>();
	public Studiengang(){
		
	}

	public Studiengang( String name, String abschluss) {
		this.name = name;
		this.abschluss = abschluss;
	}
	
	public Studiengang(int id, String name, String abschluss) {
		this.id = id;
		this.name = name;
		this.abschluss = abschluss;
	}
	
	public Studiengang(int id, String name, String abschluss, ArrayList<Modulhandbuch> modbuch) {
		this.id = id;
		this.name = name;
		this.abschluss = abschluss;
		this.modbuch = new ArrayList<Modulhandbuch>(modbuch);
	}
	
	@Override
	public String toString() {
		return name;
	}

	public int getId() {
		return id;
	}
	
	public String getAbschluss(){
		return abschluss;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Studiengang other = (Studiengang) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if(!abschluss.equals(other.abschluss))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setAbschluss(String abschluss){
		this.abschluss = abschluss;
	}

	public ArrayList<Modulhandbuch> getModbuch() {
		return modbuch;
	}

	public void setModbuch(ArrayList<Modulhandbuch> modbuch) {
		this.modbuch = new ArrayList<Modulhandbuch>(modbuch);
	}

}

