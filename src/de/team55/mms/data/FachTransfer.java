package de.team55.mms.data;

public class FachTransfer {
	private String neu;
	private String old;
	
	public FachTransfer(){}
	
	public FachTransfer(String old, String neu){
		this.setNeu(neu);
		this.setOld(old);
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
