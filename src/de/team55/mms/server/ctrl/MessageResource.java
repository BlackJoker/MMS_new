package de.team55.mms.server.ctrl;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.team55.mms.data.Modul;
import de.team55.mms.data.Modulhandbuch;
import de.team55.mms.data.StellvertreterList;
import de.team55.mms.data.Studiengang;
import de.team55.mms.data.User;
import de.team55.mms.data.UserRelation;
import de.team55.mms.data.UserUpdateContainer;
import de.team55.mms.data.Zuordnung;
import de.team55.mms.server.db.sql;

@Path("")
public class MessageResource {

	/**
	 * login Funktion
	 * 
	 * @param user
	 *            e-Mail des Users
	 * @param pass
	 *            Passwort des User
	 * @return eingeloggter User
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/login/{user}/{pass}")
	public User userLogin(@PathParam("user") String user, @PathParam("pass") String pass) {
		if (user.equals("gast@gast.gast")) {
			System.out.println("Gast hat sich angemeldet");
		} else {
			System.out.println("User " + user + " hat sich angemeldet");
		}
		User tmp = new sql().getUser(user, pass);
		if (tmp == null)
			return new User();
		else
			return tmp;
	}

	/**
	 * Prüft, ob ein User exsistiert
	 * 
	 * @param mail
	 *            e-Mail des Users
	 * @return Reponse Code
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/user/get/{user}")
	public Response userCheck(@PathParam("user") String mail) {
		System.out.println("User " + mail + " abgefragt");
		int status = new sql().getUser(mail);
		if (status == 1)
			return Response.status(201).build();
		else
			return Response.status(500).build();
	}

	/**
	 * Fragt eine Userrelation ab
	 * 
	 * @param eMail
	 *            e-Mail des Users
	 * @return Userrelation
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/user/getRelation/{user}")
	public UserRelation userRelation(@PathParam("user") String eMail) {
		System.out.println("Stellvertreter und Vorgesetzte von " + eMail + " abgefragt");
		return new UserRelation(new sql().getUserRelation(eMail));
	}

	/**
	 * gibt alle User aus
	 * 
	 * @return Liste von Usern
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/user/getall")
	public ArrayList<User> getAllUsers() {
		System.out.println("Userliste abgefragt");
		return new sql().userload();
	}

	/**
	 * Reicht einen User ein
	 * 
	 * @param user
	 *            der User
	 * @return Response Code
	 */
	@POST
	@Path("/user/post/")
	@Consumes(MediaType.APPLICATION_XML)
	public Response userPost(User user) {
		int status = new sql().usersave(user);
		if (status == 1) {
			System.out.println("Neuer User " + user.geteMail() + " hinzugefügt");
			return Response.status(201).build();
		} else {
			System.out.println("Neuer User " + user.geteMail() + " wurde nicht hinzugefügt");
			return Response.status(500).build();
		}
	}

	/**
	 * Updated einen User
	 * 
	 * @param uuc
	 *            Container mit User und alter e-Mail des Users
	 * @return Response Code
	 */
	@POST
	@Path("/user/update/")
	@Consumes(MediaType.APPLICATION_XML)
	public Response userUpdate(UserUpdateContainer uuc) {
		int status = new sql().userupdate(uuc.getUser(), uuc.getEmail());
		if (status == 1) {
			System.out.println("User " + uuc.getEmail() + " aktualisiert");
			return Response.status(201).build();
		} else {
			System.out.println("User " + uuc.getEmail() + " wurde nicht aktualisiert");
			return Response.status(500).build();
		}
	}

	/**
	 * Reicht eine Liste von Stellvertretern ein
	 * 
	 * @param sl
	 *            Liste von Stellvertretern
	 * @return Response COde
	 */
	@POST
	@Path("/user/stellv/post/")
	@Consumes(MediaType.APPLICATION_XML)
	public Response stellvPost(StellvertreterList sl) {
		int status = new sql().setStellvertreter(sl);
		if (status == 1) {
			System.out.println("Stellvertreter für " + sl.geteMail() + " aktualisiert");
			return Response.status(201).build();
		} else {
			System.out.println("Stellvertreter für " + sl.geteMail() + " wurde nicht aktualisiert");
			return Response.status(500).build();
		}
	}

	/**
	 * Liefert eine Liste von Stellverteren
	 * 
	 * @param mail
	 *            e-Mail des Users
	 * @return Liste von Benutzern
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/user/stellv/{user}")
	public ArrayList<User> getStellv(@PathParam("user") String mail) {
		System.out.println("Stellvertreter und Vorgesetzte von " + mail + " abgefragt");
		return new sql().getStellv(mail);
	}

	/**
	 * Löscht einen Benutzer
	 * 
	 * @param mail
	 *            e-Mail des Benutzers
	 */
	@DELETE
	@Path("/user/delete/{mail}")
	@Consumes(MediaType.APPLICATION_XML)
	public void userDelete(@PathParam("mail") String mail) {
		System.out.println("User " + mail + " wurde gelöscht");
		new sql().deluser(mail);
	}

	/**
	 * Gibt die neuste Version eines Moduls aus
	 * 
	 * @param name
	 *            Name des Moduls
	 * @return Versionsnummer
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/modul/getVersion/{name}")
	public String getModulVersion(@PathParam("name") String name) {
		System.out.println("Version vom Modul " + name + " abgefragt");
		return Integer.toString(new sql().getModulVersion(name));
	}

	/**
	 * Gibt ein Modul aus
	 * 
	 * @param name
	 *            Name des Moduls
	 * @return Modul
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/modul/get/{name}")
	public Modul getModul(@PathParam("name") String name) {
		System.out.println("Modul " + name + " abgefragt");
		return new sql().getModul(name);
	}

	/**
	 * Gibt ein Modul mit bestimmter Version aus
	 * 
	 * @param name
	 *            Name des Moduls
	 * @param version
	 *            Versionsnummer
	 * @return Modul
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/modul/get/{name}/{version}")
	public Modul getModul(@PathParam("name") String name, @PathParam("version") String version) {
		int v = 0;
		try {
			v = Integer.parseInt(version);
		} catch (NumberFormatException nf) {
			System.out.println("Modul " + name + " ,Version " + version + " nicht erfolgreich abgefragt");
			return null;
		}
		System.out.println("Modul " + name + " ,Version " + version + " abgefragt");
		return new sql().getModul(name, v);
	}

	/**
	 * Gibt eine Liste von Modulen aus, die akzeptiert oder nicht akzeptiert
	 * sind
	 * 
	 * @param a
	 *            Gibt an, ob akzeptiert oder nicht
	 * @return Liste von Modulen
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/modul/getList/{accepted}")
	public ArrayList<Modul> getModulList(@PathParam("accepted") String a) {
		boolean b = false;
		if (a.equals("true")) {
			b = true;
			System.out.println("Alle akzeptierten Module abfragen");
		} else {
			System.out.println("Alle nicht akzeptierten Module abfragen");
		}
		return new sql().getModule(b);
	}

	/**
	 * Reicht ein Modu ein
	 * 
	 * @param m
	 *            Das Modul
	 * @return Response Code
	 */
	@POST
	@Path("/modul/post/")
	@Consumes(MediaType.APPLICATION_XML)
	public Response modulPost(Modul m) {
		int status = new sql().setModul(m);
		if (status == 1) {
			System.out.println("Modul " + m.getName() + " hinzugefügt");
			return Response.status(201).build();
		} else {
			System.out.println("Modul " + m.getName() + " wurde nicht hinzugefügt");
			return Response.status(500).build();
		}
	}

	/**
	 * Akzeptiert ein Modul
	 * 
	 * @param m
	 *            Das Modul
	 * @return Response Code
	 */
	@POST
	@Path("/modul/accept/")
	@Consumes(MediaType.APPLICATION_XML)
	public Response modulAccept(Modul m) {
		int status = new sql().acceptModul(m.getName(), m.getVersion());
		if (status == 1) {
			System.out.println("Modul " + m.getName() + " akzeptiert");
			return Response.status(201).build();
		} else {
			System.out.println("Modul " + m.getName() + " wurde nicht akzeptiert");
			return Response.status(500).build();
		}
	}

	/**
	 * Markiert ein Modul als in Bearbeitung
	 * 
	 * @param m
	 *            Das Modul
	 * @return Response Code
	 */
	@POST
	@Path("/modul/setInEdit/")
	@Consumes(MediaType.APPLICATION_XML)
	public Response modulSetInEdit(Modul m) {
		int status = new sql().setInEdit(m);
		if (status == 1) {
			System.out.println("Modul " + m.getName() + " Bearbeitungsstatus geändert");
			return Response.status(201).build();
		} else {
			System.out.println("Modul " + m.getName() + " Bearbeitungsstatus wurde nicht geändert");
			return Response.status(500).build();
		}
	}

	/**
	 * Gibt eine Lisete von Zuordnungen aus
	 * 
	 * @return Liste von Zuordnungen
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/zuordnung/getList/")
	public ArrayList<Zuordnung> getZurdnungen() {
		System.out.println("Alle Zuordnungen abfragen");
		return new sql().getZuordnungen();
	}

	/**
	 * Reicht eine Zuordnung ein
	 * 
	 * @param z
	 *            Zuordnung
	 * @return Response Code
	 */
	@POST
	@Path("/zuordnung/post/")
	@Consumes(MediaType.APPLICATION_XML)
	public Response zuordnungPost(Zuordnung z) {
		int status = new sql().setZuordnung(z);
		if (status == 1) {
			System.out.println("Zuordnung " + z.getName() + " hinzugefügt");
			return Response.status(201).build();
		} else {
			System.out.println("Zuordnung " + z.getName() + " nicht hinzugefügt");
			return Response.status(500).build();
		}
	}

	/**
	 * Gibt die ID eines Studienganges aus
	 * 
	 * @param name
	 *            Name des Studienganges
	 * @return ID des Studienganges
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/studiengang/getID/{name}")
	public String getStudiengangID(@PathParam("name") String name) {
		System.out.println("Studiengang ID von " + name + " abgefragt");
		return Integer.toString(new sql().getStudiengangID(name));
	}

	/**
	 * Gibt alle Modulhandbücher eines Studienganges aus
	 * 
	 * @param studiengang
	 *            Name des Studienganges
	 * @return Liste von Modulhandbüchern
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/modulhandbuch/getallat/{studiengang}")
	public ArrayList<Modulhandbuch> getModulhandbuch(@PathParam("studiengang") String studiengang) {
		System.out.println("Alle Modulhandbücher vom Studiengang " + studiengang + " abgefragt");
		return new sql().getModulhandbuch(studiengang);
	}

	/**
	 * Gibt alle Studiengänge aus
	 * 
	 * @return Liste von Studiengängen
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/studiengang/getall")
	public ArrayList<Studiengang> getStudiengaenge() {
		System.out.println("Alle Studiengänge abgefragt");
		return new sql().getStudiengaenge();
	}

	/**
	 * Reicht einen Studiengang ein
	 * 
	 * @param name
	 *            Name des Studienganges
	 * @return Response Code
	 */
	@POST
	@Path("/studiengang/post/")
	@Consumes(MediaType.APPLICATION_XML)
	public Response studiengangPost(String name) {
		int status = new sql().setStudiengang(name);
		if (status == 1) {
			System.out.println("Studiengang " + name + " hinzugefügt");
			return Response.status(201).build();
		} else {
			System.out.println("Studiengang " + name + " wurde nicht hinzugefügt");
			return Response.status(500).build();
		}
	}

	/**
	 * Gibt eine Liste von Modulen aus
	 * 
	 * @param studiengang
	 *            Studiengang des Moduls
	 * @param modultyp
	 *            Zuordnung des Moduls
	 * @param modulhandbuch
	 *            Jahrgang des Moduls
	 * @return Liste von Modulen
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/modul/getselectedModul/{studiengang}/{modultyp}/{modulhandbuch}")
	public ArrayList<Modul> getselectedModul(@PathParam("studiengang") String studiengang, @PathParam("modultyp") String modultyp,
			@PathParam("modulhandbuch") String modulhandbuch) {
		System.out.println("Ausgewählte Module abfragen");
		return new sql().getselectedModul(studiengang, modultyp, modulhandbuch);
	}

	/**
	 * Gibt an, ob ein Modul in Bearbeitung ist
	 * 
	 * @param name
	 *            Name des Moduls
	 * @return Status des Moduls
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/modul/getInEdit/{name}")
	public String getModulInEdit(@PathParam("name") String name) {
		System.out.println("Abfrage, ob " + name + " in Bearbeitung ist");
		boolean b = new sql().getModulInEdit(name);
		if (b) {
			return "true";
		} else {
			return "false";
		}
	}

}