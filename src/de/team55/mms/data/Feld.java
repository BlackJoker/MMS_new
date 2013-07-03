package de.team55.mms.data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Field")
@XmlType(propOrder = { "label", "value", "dezernat"})
public class Feld {
	String label;
	String value;
	boolean dezernat;
	
	public Feld(){}
	
	public Feld(String label, String value, boolean dezernat){
		this.label=label;
		this.value=value;
		this.dezernat=dezernat;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isDezernat() {
		return dezernat;
	}

	public void setDezernat(boolean dezernat) {
		this.dezernat = dezernat;
	}

}
