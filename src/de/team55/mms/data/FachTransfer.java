package de.team55.mms.data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FachTransfer")
public class FachTransfer {
	private String neu;
	private String old;
	
	public FachTransfer(){}
	
	public FachTransfer(String old, String neu){
		this.setOld(old);
		this.setNeu(neu);
	}

	public String getNeu() {
		return neu;
	}

	public void setNeu(String neu) {
		this.neu = neu;
	}

	public String getOld() {
		return old;
	}

	public void setOld(String old) {
		this.old = old;
	}
}
