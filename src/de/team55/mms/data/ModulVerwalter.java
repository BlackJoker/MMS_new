package de.team55.mms.data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ModulVerwalter") 
public class ModulVerwalter {
	
	private User user;
	
	public ModulVerwalter(User user, String modname) {
		super();
		this.user = user;
		this.modname = modname;
	}
	
	private String modname;
	
	public ModulVerwalter() {
		super();
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getModname() {
		return modname;
	}
	public void setModname(String modname) {
		this.modname = modname;
	}
	

}
