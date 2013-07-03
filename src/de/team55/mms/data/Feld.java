package de.team55.mms.data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Field")
@XmlType(propOrder = { "label", "value", "dezernat"})
public class Feld {
	private boolean dezernat;
	private String label;
	private String value;
	
	public Feld(){}
	
	public Feld(String label, String value, boolean dezernat){
		this.label=label;
		this.value=value;
		this.dezernat=dezernat;
	}
	
	public String getLabel() {
		return label;
	}

	public String getValue() {
		return value;
	}

	public boolean isDezernat() {
		return dezernat;
	}

	public void setDezernat(boolean dezernat) {
		this.dezernat = dezernat;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Feld [label=" + label + ", value=" + value + ", dezernat="
				+ dezernat + "]";
	}

}
