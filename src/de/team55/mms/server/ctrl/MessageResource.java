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
import de.team55.mms.data.Nachricht;
import de.team55.mms.data.StellvertreterList;
import de.team55.mms.data.Studiengang;
import de.team55.mms.data.User;
import de.team55.mms.data.UserRelation;
import de.team55.mms.data.UserUpdateContainer;
//import de.team55.mms.data.Zuordnung;
import de.team55.mms.server.db.sql;

@Path("")
public class MessageResource {

	/**
	 * gibt alle User aus
	 * 
	 * @return Liste von Usern
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/user/get/all")
	public ArrayList<User> getAllUsers() {
		System.out.println("Userliste abgefragt");
		return new sql().userload();
	}

	/**
	 * Gibt Anzahl an Studiengaengen aus
	 * 
	 * @return Anzhal
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/studiengang/get/Anzahl")
	public String getAnzahlStudiengaenge() {
		System.out.println("Anzahl an Studiengaengen wurde abgefragt");
		return Integer.toString(new sql().getAnzahlStudiengaenge());
	}

	/**
	 * Gibt eine Liste von Versionen eines Moduls aus
	 * 
	 * @param name
	 *            Name des Moduls
	 * @return Liste von Versionen eines Module
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/modul/get/{name}")
	public ArrayList<Modul> getModul(@PathParam("name") String name) {
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
	 * Gibt alle Modulhandbücher eines Studienganges aus
	 * 
	 * @param studiengang
	 *            Name des Studienganges
	 * @return Liste von Modulhandbüchern
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/modulhandbuch/get/allat/{studiengang}")
	public ArrayList<Modulhandbuch> getModulhandbuch(@PathParam("studiengang") String studiengang) {
		System.out.println("Alle Modulhandbücher vom Studiengang " + studiengang + " abgefragt");
		return new sql().getModulhandbuch(studiengang);
	}

	@POST
	@Path("/modulhandbuch/post/accept/")
	@Consumes(MediaType.APPLICATION_XML)
	public Response modulhanbuchAccept(Modulhandbuch mh) {
		int status = new sql().acceptModulHandBuch(mh.getId());
		if (status == 1) {
			System.out.println("Modulhandbuch " + mh.toString() + " akzeptiert");
			return Response.status(201).build();
		} else {
			System.out.println("Modulhandbuch " + mh.toString() + " wurde nicht akzeptiert");
			return Response.status(500).build();
		}
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
	@Path("/modul/get/InEdit/{name}")
	public String getModulInEdit(@PathParam("name") String name) {
		System.out.println("Abfrage, ob " + name + " in Bearbeitung ist");
		boolean b = new sql().getModulInEdit(name);
		if (b) {
			return "true";
		} else {
			return "false";
		}
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
	@Path("/modul/get/List/{accepted}")
	public ArrayList<Studiengang> getModulList(@PathParam("accepted") String a) {
		boolean b = false;
		if (a.equals("true")) {
			b = true;
			System.out.println("Alle akzeptierten Module abfragen");
		} else {
			System.out.println("Alle nicht akzeptierten Module abfragen");
		}
		return new sql().getAllActiveData(b);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/nachrihten/get/{User}")
	public ArrayList<Nachricht> getNachrichten(@PathParam("User") User u){
		System.out.println("Nachrichten von "+u.geteMail()+" abgefragt");
		return new sql().readMessages(u.getId());
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
	@Path("/modul/get/Version/{name}")
	public String getModulVersion(@PathParam("name") String name) {
		System.out.println("Version vom Modul " + name + " abgefragt");
		return Integer.toString(new sql().getModulVersion(name));
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
	@Path("/modul/get/selectedModul/{studiengang}/{modultyp}/{modulhandbuch}")
	public ArrayList<Modul> getselectedModul(@PathParam("studiengang") String studiengang, @PathParam("modultyp") String modultyp,
			@PathParam("modulhandbuch") String modulhandbuch) {
		System.out.println("Ausgewählte Module abfragen");
		return new sql().getselectedModul(studiengang, modultyp, modulhandbuch);
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
	@Path("/user/get/stellv/{modul}")
	public ArrayList<User> getStellv(@PathParam("modul") String modul) {
		System.out.println("Stellvertreter für " + modul + " abgefragt");
		return new sql().getStellv(modul);
	}

	/**
	 * Gibt alle Studiengänge aus
	 * 
	 * @return Liste von Studiengängen
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/studiengang/get/all")
	public ArrayList<Studiengang> getStudiengaenge() {
		System.out.println("Alle Studiengänge abgefragt");
		return new sql().getStudiengaenge();
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
	@Path("/studiengang/get/ID/{s}")
	public String getStudiengangID(@PathParam("s") Studiengang s) {
		System.out.println("Studiengang ID von " + s.getAbschluss() + ", " + s.getName() + " abgefragt");
		return Integer.toString(new sql().getStudiengangID(s.getName(), s.getAbschluss()));
	}

	// /**
	// * Gibt eine Lisete von Zuordnungen aus
	// *
	// * @return Liste von Zuordnungen
	// */
	// @GET
	// @Produces(MediaType.APPLICATION_XML)
	// @Path("/zuordnung/get/List/")
	// public ArrayList<Zuordnung> getZurdnungen() {
	// System.out.println("Alle Zuordnungen abfragen");
	// return new sql().getZuordnungen();
	// }

	/**
	 * Akzeptiert ein Modul
	 * 
	 * @param m
	 *            Das Modul
	 * @return Response Code
	 */
	@POST
	@Path("/modul/post/accept/")
	@Consumes(MediaType.APPLICATION_XML)
	public Response modulAccept(Modulhandbuch m) {
		int status = new sql().acceptModulHandBuch(m.getId());
		if (status == 1) {
			System.out.println("Modul " + m.getName() + " akzeptiert");
			return Response.status(201).build();
		} else {
			System.out.println("Modul " + m.getName() + " wurde nicht akzeptiert");
			return Response.status(500).build();
		}
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
	 * Markiert ein Modul als in Bearbeitung
	 * 
	 * @param m
	 *            Das Modul
	 * @return Response Code
	 */
	@POST
	@Path("/modul/post/setInEdit/")
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
	 * Reicht eine Liste von Stellvertretern ein
	 * 
	 * @param sl
	 *            Liste von Stellvertretern
	 * @return Response COde
	 */
	@POST
	@Path("/user/post/stellv/")
	@Consumes(MediaType.APPLICATION_XML)
	public Response stellvPost(StellvertreterList sl) {
		int status = new sql().setStellvertreter(sl.getUsr(), sl.getModul());
		if (status == 1) {
			System.out.println("Stellvertreter für " + sl.getModul() + " aktualisiert");
			return Response.status(201).build();
		} else {
			System.out.println("Stellvertreter für " + sl.getModul() + " wurde nicht aktualisiert");
			return Response.status(500).build();
		}
	}

	/**
	 * Reicht einen Studiengang ein
	 * 
	 * @param s
	 *            Studiengang, der angelegt werden soll
	 * @return Response Code
	 */
	@POST
	@Path("/studiengang/post/")
	@Consumes(MediaType.APPLICATION_XML)
	public Response studiengangPost(Studiengang s) {
		int status = new sql().setStudiengang(s.getName(), s.getAbschluss());
		if (status == 1) {
			System.out.println("Studiengang " + s.getAbschluss() + ", " + s.getName() + " hinzugefügt");
			return Response.status(201).build();
		} else {
			System.out.println("Studiengang " + s.getAbschluss() + ", " + s.getName() + " wurde nicht hinzugefügt");
			return Response.status(500).build();
		}
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
	 * Fragt eine Userrelation ab
	 * 
	 * @param eMail
	 *            e-Mail des Users
	 * @return Userrelation
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/user/get/Relation/{user}")
	public ArrayList<Modul> userRelation(@PathParam("user") String eMail) {
		System.out.println("Stellvertreter und Vorgesetzte von " + eMail + " abgefragt");
		return new sql().getUserModulRelation(eMail);
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

//	/**
//	 * Reicht eine Zuordnung ein
//	 * 
//	 * @param z
//	 *            Zuordnung
//	 * @return Response Code
//	 */
//	@POST
//	@Path("/zuordnung/post/")
//	@Consumes(MediaType.APPLICATION_XML)
//	public Response zuordnungPost(Zuordnung z) {
//		int status = new sql().setZuordnung(z);
//		if (status == 1) {
//			System.out.println("Zuordnung " + z.getName() + " hinzugefügt");
//			return Response.status(201).build();
//		} else {
//			System.out.println("Zuordnung " + z.getName() + " nicht hinzugefügt");
//			return Response.status(500).build();
//		}
//	}

}