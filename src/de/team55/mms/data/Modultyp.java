package de.team55.mms.data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "Modultyp")
@XmlType(propOrder = { "name", "id"})

public class Modultyp {
	
	int id;
	String name;
	
		
	public Modultyp(){
		
	}
	
	public Modultyp(String name, int id){
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Modultyp [id=" + id + ", name=" + name + "]";
	}
	

}
