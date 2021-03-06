package de.team55.mms.data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
//
@XmlRootElement(name = "User")
@XmlType(propOrder = { "id", "eMail", "titel", "vorname", "nachname", "password",
		"manageUsers", "acceptModule", "createModule", "manageSystem", "redaktion" ,"freigeschaltet"})
public class User {
	private String Vorname;
	private String Nachname;
	private String eMail;
	private String Password;
	private String Titel;
	private int id;
	
	private boolean manageUsers;
	private boolean manageSystem;
	private boolean createModule;
	private boolean acceptModule;
	private boolean redaktion;
	private boolean freigeschaltet=false;
	
	public User() {
		this.Vorname = "null";
		this.Nachname = "null";
		this.Titel = "null";
		this.eMail = "null";
		this.Password = "null";
		this.manageUsers = false;
		this.manageSystem = false;
		this.createModule = false;
		this.acceptModule = false;
		this.redaktion = false;
		this.freigeschaltet=false;
	}
	
	public User(int id, String Vorname, String Nachname, String Titel, String eMail,
			String Password, boolean manageUsers, boolean createModule,
			boolean acceptModule, boolean manageSystem, boolean redaktion, boolean freigeschaltet) {
		this.id=id;
		this.Vorname = Vorname;
		this.Nachname = Nachname;
		this.Titel = Titel;
		this.eMail = eMail;
		this.Password = Password;
		this.manageUsers = manageUsers;
		this.manageSystem = manageSystem;
		this.createModule = createModule;
		this.acceptModule = acceptModule;
		this.redaktion = redaktion;
		this.freigeschaltet=freigeschaltet;
	}
	
	public User(String Vorname, String Nachname, String Titel, String eMail,
			String Password, boolean manageUsers, boolean createModule,
			boolean acceptModule, boolean manageSystem, boolean redaktion, boolean freigeschaltet) {
		this.Vorname = Vorname;
		this.Nachname = Nachname;
		this.Titel = Titel;
		this.eMail = eMail;
		this.Password = Password;
		this.manageUsers = manageUsers;
		this.manageSystem = manageSystem;
		this.createModule = createModule;
		this.acceptModule = acceptModule;
		this.redaktion = redaktion;
		this.freigeschaltet=freigeschaltet;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (Nachname == null) {
			if (other.Nachname != null)
				return false;
		} else if (!Nachname.equals(other.Nachname))
			return false;
		if (Password == null) {
			if (other.Password != null)
				return false;
		} else if (!Password.equals(other.Password))
			return false;
		if (Titel == null) {
			if (other.Titel != null)
				return false;
		} else if (!Titel.equals(other.Titel))
			return false;
		if (Vorname == null) {
			if (other.Vorname != null)
				return false;
		} else if (!Vorname.equals(other.Vorname))
			return false;
		if (acceptModule != other.acceptModule)
			return false;
		if (createModule != other.createModule)
			return false;
		if (eMail == null) {
			if (other.eMail != null)
				return false;
		} else if (!eMail.equals(other.eMail))
			return false;
		if (manageUsers != other.manageUsers)
			return false;
		if (manageSystem != other.manageSystem)
			return false;
		return true;
	}
	public boolean getAcceptModule() {
		return acceptModule;
	}
	
	
	public boolean getCreateModule() {
		return createModule;
	}

	public String geteMail() {
		return eMail;
	}

	public int getId() {
		return id;
	}

	public boolean getManageSystem() {
		return manageSystem;
	}
	
	public boolean getManageUsers() {
		return manageUsers;
	}

	public String getNachname() {
		return Nachname;
	}

	// @XmlTransient
	public String getPassword() {
		return Password;
	}

	public boolean getRedaktion(){
		return redaktion;
	}

	public String getTitel() {
		return Titel;
	}

	public String getVorname() {
		return Vorname;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((Nachname == null) ? 0 : Nachname.hashCode());
		result = prime * result
				+ ((Password == null) ? 0 : Password.hashCode());
		result = prime * result + ((Titel == null) ? 0 : Titel.hashCode());
		result = prime * result + ((Vorname == null) ? 0 : Vorname.hashCode());
		result = prime * result + (acceptModule ? 1231 : 1237);
		result = prime * result + (createModule ? 1231 : 1237);
		result = prime * result + ((eMail == null) ? 0 : eMail.hashCode());
		result = prime * result + (manageUsers ? 1231 : 1237);
		result = prime * result + (manageSystem ? 1231 : 1237);
		return result;
	}

	public boolean isFreigeschaltet() {
		return freigeschaltet;
	}

	public boolean isManageSystem() {
		return manageSystem;
	}

	public void setAcceptModule(boolean acceptModule) {
		this.acceptModule = acceptModule;
	}

	public void setCreateModule(boolean createModule) {
		this.createModule = createModule;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	
	public void setFreigeschaltet(boolean freigeschaltet) {
		this.freigeschaltet = freigeschaltet;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setmanageSystem(boolean manageSystem) {
		this.manageSystem = manageSystem;
	}

	public void setManageSystem(boolean manageSystem) {
		this.manageSystem = manageSystem;
	}

	public void setManageUsers(boolean manageUsers) {
		this.manageUsers = manageUsers;
	}

	public void setNachname(String nachname) {
		Nachname = nachname;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public void setRedaktion(boolean redaktion) {
		this.redaktion = redaktion;
	}

	public void setRedaktion(Boolean redaktion) {
		this.redaktion = redaktion;
	}

	public void setTitel(String titel) {
		Titel = titel;
	}

	public void setVorname(String vorname) {
		Vorname = vorname;
	}
	
	@Override
	public String toString() {
		return eMail;
	}

}
