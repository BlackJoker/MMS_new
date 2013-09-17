package de.team55.mms.function;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xhtmlrenderer.pdf.ITextRenderer;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import de.team55.mms.data.Feld;
import de.team55.mms.data.Modul;
import de.team55.mms.data.Modulhandbuch;
import de.team55.mms.data.Nachricht;
import de.team55.mms.data.StellvertreterList;
import de.team55.mms.data.Studiengang;
import de.team55.mms.data.User;
import de.team55.mms.data.UserRelation;
import de.team55.mms.data.UserUpdateContainer;
import de.team55.mms.data.pordnung;
//import de.team55.mms.data.Zuordnung;

public class ServerConnection {

	private static final int LOGINFALSE = 1;
	private static final int NOCONNECTION = 0;
	private static final int SUCCESS = 2;
	private Client client;
	private int connected;
	private String email;
	private HTTPBasicAuthFilter filter = null;
	private String password;

	private String serverPath = "http://localhost:8080/";
	private WebResource webResource;

	public ServerConnection() {
		client = Client.create();
	}

	/**
	 * Verbindet zum Server
	 * 
	 * @param eMail
	 *            e-Mail des Users
	 * @param password
	 *            Password des Users
	 * @return status, ob erfolgreich
	 */
	public int connect(String eMail, String password) {
		this.email = eMail;
		this.password = password;
		if (filter != null)
			client.removeFilter(filter);
		filter = new HTTPBasicAuthFilter(email, password);
		client.addFilter(filter);
		webResource = client.resource(serverPath);
		try {
			ClientResponse response = webResource.post(ClientResponse.class);
			if (response.getStatus() != 401)
				connected = SUCCESS;
			else
				connected = LOGINFALSE;
		} catch (com.sun.jersey.api.client.ClientHandlerException e) {
			connected = NOCONNECTION;
		}
		return connected;
	}

	/**
	 * L�scht einen User
	 * 
	 * @param mail
	 *            e-Mail des Users
	 * @return Response Code, null wenn nicht vorhanden
	 */
	public ClientResponse deluser(String mail) {
		if (connect(email, password) == SUCCESS) {
			webResource.path("user/delete").path(mail).type(MediaType.APPLICATION_XML).delete();
			return webResource.path("user/get").path(mail).get(ClientResponse.class);
		}
		return null;

	}

	/**
	 * Gibt ein Modul aus
	 * 
	 * @param name
	 *            Name des Moduls
	 * @return das gew�nschte Modul, null wenn nicht vorhanden
	 */
	public Modul getModul(String name) {
		if (connect(email, password) == SUCCESS) {
			return webResource.path("modul/get").path(name).accept(MediaType.APPLICATION_XML).get(Modul.class);
		} else {
			return null;
		}

	}

	/**
	 * Gibt ein Modul als XML String aus
	 * 
	 * @param name
	 *            Name des Moduls
	 * @return das Modul als XML, null wenn nicht vorhanden
	 */
	public String getModulXML(String name) {
		if (connect(email, password) == SUCCESS) {
			JAXBContext context;
			try {
				context = JAXBContext.newInstance(Modul.class);
				Marshaller mar = context.createMarshaller();
				mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				StringWriter stringWriter = new StringWriter();
				Modul m = webResource.path("modul/get").path(name).accept(MediaType.APPLICATION_XML).get(Modul.class);
				if (m.getName() != null) {
					mar.marshal(m, stringWriter);
					return stringWriter.toString();
				}
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		} else {
			return null;
		}

	}
	
	public ArrayList<Feld> getDefaultFelder(){
		if (connect(email, password) == SUCCESS) {
			return webResource.path("feld/get").accept(MediaType.APPLICATION_XML)
					.get(new GenericType<ArrayList<Feld>>() {
					});
		}
		return null;
	}

	public ClientResponse setDefaultFelder(ArrayList<Feld> felder) {
		if (connect(email, password) == SUCCESS) {
			return webResource.path("feld/post").type(MediaType.APPLICATION_XML).post(ClientResponse.class, new GenericEntity<ArrayList<Feld>>(felder){});
		}
		return null;
	}
	
	/**
	 * Gibt eine Liste von Modulen aus
	 * 
	 * @param b
	 *            gibt an, ob man akzeptierte oder nicht akzeptierte Module
	 *            m�chte
	 * @return eine Liste von Modulen
	 */
	public ArrayList<Modul> getModule(boolean b) {
		String accepted = "false";
		if (b)
			accepted = "true";
		if (connect(email, password) == SUCCESS) {
			return webResource.path("modul/get/List").path(accepted).accept(MediaType.APPLICATION_XML)
					.get(new GenericType<ArrayList<Modul>>() {
					});
		}
		return null;
	}

	/**
	 * Gibt Modulhandb�cher aus
	 * 
	 * @param studiengang
	 *            Der gew�nschte Studiengang
	 * @return Liste von Modulhandb�hern
	 */
	public ArrayList<Modulhandbuch> getModulhandbuch(String studiengang) {
		if (connect(email, password) == SUCCESS) {
			return webResource.path("modulhandbuch/get/allat").path(studiengang).accept(MediaType.APPLICATION_XML)
					.get(new GenericType<ArrayList<Modulhandbuch>>() {
					});
		}
		return null;
	}

	/**
	 * Gibt die neueste Version eines Modules aus
	 * 
	 * @param name
	 *            Des Modules
	 * @return Version des Modules
	 */
	public int getModulVersion(String name) {
		if (connect(email, password) == SUCCESS) {
			String id = webResource.path("modul/get/Version").path(name).accept(MediaType.APPLICATION_XML).get(String.class);
			return Integer.parseInt(id);
		}
		return 0;
	}

	/**
	 * Gibt eine Liste mit Stellvertretern aus
	 * 
	 * @param eMail
	 *            e-Mail des Benutzers, von dem die Stellvertreter abgefragt
	 *            werden soll
	 * @return Liste mit Benutzern
	 */
	public ArrayList<User> getStellvertreter(String eMail) {
		if (eMail.isEmpty())
			return new ArrayList<User>();
		if (connect(email, password) == SUCCESS) {
			return webResource.path("user/get/stellv").path(eMail).accept(MediaType.APPLICATION_XML).get(new GenericType<ArrayList<User>>() {
			});
		}
		return new ArrayList<User>();
	}

	/**
	 * Gibt eine Liste mit Studieng�ngen aus
	 * 
	 * @return Liste mit Studieng�ngen
	 */
	public ArrayList<Studiengang> getStudiengaenge(boolean bool) {
		String boolst = "";
		if(bool){
			boolst = "true";
		}else{
			boolst = "false";
		}
		if (connect(email, password) == SUCCESS) {
			return webResource.path("studiengang/get/all").path(boolst).accept(MediaType.APPLICATION_XML).get(new GenericType<ArrayList<Studiengang>>() {
			});
		}
		return null;
	}

	/**
	 * Gibt die ID eines Studienganges aus
	 * 
	 * @param name
	 *            Name des Studienganges
	 * @return ID des Studienganges
	 */
	public int getStudiengangID(String name) {
		if (connect(email, password) == SUCCESS) {
			String id = webResource.path("studiengang/get/ID").path(name).accept(MediaType.APPLICATION_XML).get(String.class);
			return Integer.parseInt(id);
		}
		return 0;
	}

	/**
	 * Gibt eine Liste von Zuordnungen aus
	 * 
	 * @return Liste mit Zuordnungen
	 */
//	public ArrayList<Zuordnung> getZuordnungen() {
//		if (connect(email, password) == SUCCESS) {
//			return webResource.path("zuordnung/getList").accept(MediaType.APPLICATION_XML).get(new GenericType<ArrayList<Zuordnung>>() {
//			});
//		}
//		return null;
//	}

	/**
	 * Gibt an, ob man mit dem Server verbunden ist
	 * 
	 * @return status, ob man verbunden ist
	 */
	public int isConnected() {
		return connected;
	}

	/**
	 * Funktion zum einloggen eines Users
	 * 
	 * @param eMail
	 *            e-Mail des Users
	 * @param password
	 *            Passwort des Users
	 * @return Gibt den User zur�ck, null wenn fehlgeschlagen
	 */
	public User login(String eMail, String password) {
		if (connect(eMail, password) == SUCCESS) {
			return webResource.path("login").path(eMail).path(password).accept(MediaType.APPLICATION_XML).get(User.class);
		} else {
			return null;
		}

	}

	/**
	 * Reicht ein Modul ein
	 * 
	 * @param neu
	 *            Das Modul
	 * @return Response Code
	 */
	public ClientResponse setModul(Modul neu, String fach, int buchid) {
		if (connect(email, password) == SUCCESS) {
			return webResource.path("modul/post").path(fach).path(buchid+"").type(MediaType.APPLICATION_XML).post(ClientResponse.class, neu);
		}
		return null;
	}
	
	public ClientResponse setModulHandbuchAccepted(Modulhandbuch mb) {
		if (connect(email, password) == SUCCESS) {
			return webResource.path("/modulhandbuch/post/accept/").type(MediaType.APPLICATION_XML).post(ClientResponse.class, mb);
		}
		return null;
	}

	/**
	 * Reicht eine Liste von Stellvertretern ein
	 * 
	 * @param sl
	 *            Liste von Stellvertretern
	 * @return Response Code
	 */
	public ClientResponse setStellvertreter(StellvertreterList sl) {
		if (connect(email, password) == SUCCESS) {
			return webResource.path("user/post/stellv").type(MediaType.APPLICATION_XML).post(ClientResponse.class, sl);
		}
		return null;
	}

	/**
	 * Reicht einen Studiengang ein
	 * 
	 * @param name
	 *            Name des Studienganges
	 * @return Response Code
	 */
	public ClientResponse setStudiengang(String name, String abschluss) {
		if (connect(email, password) == SUCCESS) {
			return webResource.path("studiengang/post").type(MediaType.APPLICATION_XML).post(ClientResponse.class, new Studiengang(name,abschluss));
		}
		return null;

	}

	/**
	 * Reicht eine Zuordnung ein
	 * 
	 * @param z
	 *            die Zuordnung
	 * @return Response Code
	 */
//	public ClientResponse setZuordnung(Zuordnung z) {
//		if (connect(email, password) == SUCCESS) {
//			return webResource.path("zuordnung/post").type(MediaType.APPLICATION_XML).post(ClientResponse.class, z);
//		}
//		return null;
//	}

	/**
	 * Frag alle User ab
	 * 
	 * @return eine Liste von Usern
	 */
	public ArrayList<User> userload(String s) {
		if (connect(email, password) == SUCCESS) {
			return webResource.path("user/get/all").path(s).accept(MediaType.APPLICATION_XML).get(new GenericType<ArrayList<User>>() {
			});
		} else {
			return null;
		}
	}

	/**
	 * Reicht einen neuen User ein
	 * 
	 * @param tmp
	 *            der User
	 * @return Response Code
	 */
	public ClientResponse usersave(User tmp) {
		if (connect(email, password) == SUCCESS) {
			return webResource.path("user/post").type(MediaType.APPLICATION_XML).post(ClientResponse.class, tmp);
		}
		return null;

	}

	/**
	 * Updated einen User
	 * 
	 * @param tmp
	 *            der User
	 * @param mail
	 *            die alte e-Mail des Users
	 * @return Response Code
	 */
	public ClientResponse userupdate(User tmp, String mail) {
		if (connect(email, password) == SUCCESS) {
			UserUpdateContainer uuc = new UserUpdateContainer(tmp, mail);
			return webResource.path("user/update").type(MediaType.APPLICATION_XML).post(ClientResponse.class, uuc);
		}
		return null;
	}

	/**
	 * Fragt die Userrelation ab
	 * 
	 * @param eMail
	 *            e-Mail des Benutzers
	 * @return Liefert eine Liste mit Benutzernamen von Vorgesetzten und
	 *         Stellvertretern
	 */
	public ArrayList<String> getUserRelation(String eMail) {
		if (connect(email, password) == SUCCESS) {
			return webResource.path("/user/get/Relation/").path(eMail).accept(MediaType.APPLICATION_XML).get(UserRelation.class)
					.getRelation();
		}
		return null;
	}

	/**
	 * Gibt eine Liste von Modulen Zur�ck
	 * 
	 * @param studiengang
	 *            Studiengang des Moduls
	 * @param modultyp
	 *            Zuordnung des Moduls
	 * @param modulhandbuch
	 *            Jahrgang des Moduls
	 * @return
	 */
	public ArrayList<Modul> getselectedModul(String studiengang, String modultyp, String modulhandbuch) {
		if (connect(email, password) == SUCCESS) {
			return webResource.path("/modul/get/selectedModul/").path(studiengang).path(modultyp).path(modulhandbuch)
					.accept(MediaType.APPLICATION_XML).get(new GenericType<ArrayList<Modul>>() {
					});
		}
		return null;
	}

	/**
	 * Gibt ein Modul aus
	 * @param name Name des Moduls
	 * @param v Version des Moduls
	 * @return Modul
	 */
	public Modul getModul(String name, int v) {
		if (connect(email, password) == SUCCESS) {
			return webResource.path("modul/get").path(name).path(v + "").accept(MediaType.APPLICATION_XML).get(Modul.class);
		} else {
			return null;
		}
	}

	/**
	 * akzeptiert ein Modul
	 * @param m Modul, das akzeptiert werden soll
	 * @return Response Code
	 */
	public ClientResponse acceptModul(Modul m) {
		if (connect(email, password) == SUCCESS) {
			return webResource.path("modul/accept").type(MediaType.APPLICATION_XML).post(ClientResponse.class, m);
		} else {
			return null;
		}

	}

	/**
	 * HTML to HTML (UTF-8)
	 */
	public void specialreplacer() {
		String content = "";
		try { // html-file zu html-string
			BufferedReader in = new BufferedReader(new FileReader("modul.html"));
			String str;
			while ((str = in.readLine()) != null) {

				content += str;
			}
			// ersetzt die sonderzeichentags durch utf-8 sonderzeichen
			content = content.replaceAll("&uuml;", "�");
			content = content.replaceAll("&auml;", "�");
			content = content.replaceAll("&ouml;", "�");
			content = content.replaceAll("&Uuml;", "�");
			content = content.replaceAll("&Auml;", "�");
			content = content.replaceAll("&Ouml;", "�");
			content = content.replaceAll("<META", "");
			in.close();
		} catch (IOException e) {
		}
		// nimmt html-string und erzeugt html-file
		PrintWriter out = null;
		try {
			out = new PrintWriter(new File("modul.html"), "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.print(content);
		out.close();
	}

	/**
	 * xmlstring zu xml file
	 * @param name xml String
	 */
	public void getModulXMLFile(String name)
	{
		PrintWriter out = null;
		try {
			out = new PrintWriter(new File("modul.xml"), "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.print(getModulXML(name));
		out.close();
	}

	/**
	 * erzeugt eine PDF
	 * @param name name des Modules
	 * @throws IOException
	 * @throws DocumentException
	 * @throws TransformerException
	 * @throws TransformerConfigurationException
	 * @throws FileNotFoundException
	 */
//	public void toPdf(String name) throws IOException, DocumentException, TransformerException,
//
//	TransformerConfigurationException, FileNotFoundException {
//
//		getModulXMLFile(name); // nach einigen verschachtelten aufrufen wird
//								// hier die xml-datei erstellt
//
//		String pdfname = (name + getPDFname() + ".pdf"); // pdf-datei name wird
//															// generiert
//
//		// transformer mit xsl datei wird erzeugt
//		TransformerFactory tFactory = TransformerFactory.newInstance();
//		Transformer transformer = tFactory.newTransformer(new StreamSource("style.xsl"));
//
//		// eiegnschaften des transformer werden ver�ndert/spezifiziert
//		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//
//		// eigentliche umwandlung von xml + xsl zu html
//		transformer.transform(new StreamSource("modul.xml"), new StreamResult(new OutputStreamWriter(new FileOutputStream("modul.html"),
//				"UTF-8")));
//
//		specialreplacer(); // ersetzt sonderzeichentags in utf-8 sonderzeichen
//
//		String File_To_Convert = "modul.html";
//		String url = new File(File_To_Convert).toURI().toURL().toString();
//
//		// umwandlung von html zu pdf
//		String HTML_TO_PDF = pdfname;
//
//		FileOutputStream os = new FileOutputStream(HTML_TO_PDF);
//		ITextRenderer renderer = new ITextRenderer();
//		renderer.setDocument(url);
//		renderer.layout();
//		renderer.createPDF(os);
//
//		os.close();
//
//		Desktop.getDesktop().open(new File(pdfname));// erstellte Pdf wird
//														// ge�ffnet
//		dclean(); // r�umt die xml-datei welche nicht mehr ben�tigt wird auf...
//	}

	/**
	 * generiert pdfnamen aus erstellungszeitpunkt
	 * @return Name der PDF
	 */
	public String getPDFname() {
		SimpleDateFormat date = new SimpleDateFormat("HHmmss");
		String date1 = date.format(new Date());
		return date1;
	}

	/**
	 * l�scht die xml-datei erstellte wieder
	 */
	public void dclean() { 
		File file = new File("modul.xml");
		file.delete();
	}

	/**
	 * Pr�ft, ob ein Modul in Bearbeitung ist
	 * @param name Name des Moduls
	 * @return Status, ob es in Bearbeitung ist
	 */
	public boolean getModulInEdit(String name) {
		if (connect(email, password) == SUCCESS) {
			String b = webResource.path("modul/get/InEdit").path(name).accept(MediaType.APPLICATION_XML).get(String.class);
			if (b.equals("true")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Markiert ein Modul als in Bearbeitung
	 * @param m Das Modul
	 * @return Response Code
	 */
	public ClientResponse setModulInEdit(Modul m) {
		if (connect(email, password) == SUCCESS) {
			return webResource.path("modul/set/InEdit").type(MediaType.APPLICATION_XML).post(ClientResponse.class, m);
		} else {
			return null;
		}
	}
	
	public ClientResponse setNachricht(Nachricht n) {
		if (connect(email, password) == SUCCESS) {
			return webResource.path("nachrichten/post").type(MediaType.APPLICATION_XML).post(ClientResponse.class, n);
		} else {
			return null;
		}
	}
	
	public ArrayList<Nachricht> getNachrichten(String mail) {
		if (connect(email, password) == SUCCESS) {
			return webResource.path("/nachrichten/get").path(mail)
					.accept(MediaType.APPLICATION_XML).get(new GenericType<ArrayList<Nachricht>>() {
					});
		}
		return null;
	}

	public ClientResponse savedate(Date date) {
		String d = Long.toString(date.getTime());
		if (connect(email, password) == SUCCESS) {
			return webResource.path("/datum/set").type(MediaType.APPLICATION_XML).post(ClientResponse.class, d);
		}
		return null;
	}
	
	public Date getDate() {
		if (connect(email, password) == SUCCESS) {
			String d = webResource.path("/datum/get").accept(MediaType.APPLICATION_XML).get(String.class);
			long l = Long.parseLong(d);
			return new Date(l);
		}
		return null;
	}
	
	public ArrayList<ArrayList<User>> getModulverwalter(String modname){
		if (connect(email, password) == SUCCESS) {
			return webResource.path("/modverwa/getall").path(modname).accept(MediaType.APPLICATION_XML).get(new GenericType<ArrayList<ArrayList<User>>>() {
					});
		}
		return null;
	
	}
	
	public ArrayList<String> getallModulnames(){
		if(connect(email, password) == SUCCESS){
			return webResource.path("/modnames/getall").accept(MediaType.APPLICATION_XML).get(new GenericType<ArrayList<String>>(){});
		}
		return null;
	}

	public void deleteNachricht(Nachricht nachricht) {
		if (connect(email, password) == SUCCESS) {
			String id = Integer.toString(nachricht.getId());
			webResource.path("nachrichten/delete").path(id).type(MediaType.APPLICATION_XML).delete();
		//	return webResource.path("user/get").path(mail).get(ClientResponse.class);
		}
		//return null;
	}

	public ClientResponse updateNachricht(Nachricht n) {
		if (connect(email, password) == SUCCESS) {
			return webResource.path("nachrichten/update").type(MediaType.APPLICATION_XML).put(ClientResponse.class, n);
		}
		return null;
	}
	
	public ArrayList<pordnung> getPOs(){
		if(connect(email, password) == SUCCESS){
			return webResource.path("/po/get").accept(MediaType.APPLICATION_XML).get(new GenericType<ArrayList<pordnung>>(){});
		}
		return null;
	}

	public ClientResponse setModulHandbuchAccepted(Studiengang s) {
		if (connect(email, password) == SUCCESS) {
			return webResource.path("modulhandbuch/post").type(MediaType.APPLICATION_XML).post(ClientResponse.class, s);
		} else {
			return null;
		}
	}
	
	public ArrayList<String> getFach(){
		if(connect(email, password) == SUCCESS){
			return webResource.path("/fach/get").type(MediaType.APPLICATION_XML).get(new GenericType<ArrayList<String>>(){});
		}else {
			return null;
		}
	}
}
