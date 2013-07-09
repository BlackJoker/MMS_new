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

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import de.team55.mms.data.*;

public class ServerConnection {

	private static final int LOGINFALSE = 1;
	private static final int NOCONNECTION = 0;
	private static final int SUCCES = 2;
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
				connected = SUCCES;
			else
				connected = LOGINFALSE;
		} catch (com.sun.jersey.api.client.ClientHandlerException e) {
			connected = NOCONNECTION;
		}
		return connected;
	}

	public ClientResponse deluser(String mail) {
		if (connect(email, password) == SUCCES) {
			webResource.path("user/delete").path(mail)
					.type(MediaType.APPLICATION_XML).delete();
			return webResource.path("user/get").path(mail)
					.get(ClientResponse.class);
		}
		return null;

	}

	public Modul getModul(String name) {
		if (connect(email, password) == SUCCES) {
			return webResource.path("modul/get").path(name)
					.accept(MediaType.APPLICATION_XML).get(Modul.class);
		} else {
			return null;
		}

	}

	public String getModulXML(String name) {
		if (connect(email, password) == SUCCES) {
			JAXBContext context;
			try {
				context = JAXBContext.newInstance(Modul.class);
				Marshaller mar = context.createMarshaller();
				mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				StringWriter stringWriter = new StringWriter();
				Modul m = webResource.path("modul/get").path(name)
						.accept(MediaType.APPLICATION_XML)
						.get(Modul.class);
				if(m.getName()!=null){
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

	public ArrayList<Modul> getModule(boolean b) {
		String accepted = "false";
		if (b)
			accepted = "true";
		if (connect(email, password) == SUCCES) {
			return webResource.path("modul/getList").path(accepted)
					.accept(MediaType.APPLICATION_XML)
					.get(new GenericType<ArrayList<Modul>>() {
					});
		}
		return null;
	}

	public ArrayList<Modulhandbuch> getModulhandbuch(String studiengang) {
		if (connect(email, password) == SUCCES) {
			return webResource.path("modulhandbuch/getallat").path(studiengang)
					.accept(MediaType.APPLICATION_XML)
					.get(new GenericType<ArrayList<Modulhandbuch>>() {
					});
		}
		return null;
	}

	

	public int getModulVersion(String name) {
		if (connect(email, password) == SUCCES) {
			String id = webResource.path("modul/getVersion").path(name)
					.accept(MediaType.APPLICATION_XML).get(String.class);
			return Integer.parseInt(id);
		}
		return 0;
	}

	public ArrayList<User> getStellvertreter(String eMail) {
		if (eMail.isEmpty())
			return new ArrayList<User>();
		if (connect(email, password) == SUCCES) {
			return webResource.path("user/stellv").path(eMail)
					.accept(MediaType.APPLICATION_XML)
					.get(new GenericType<ArrayList<User>>() {
					});
		}
		return null;
	}

	public ArrayList<Studiengang> getStudiengaenge() {
		if (connect(email, password) == SUCCES) {
			return webResource.path("studiengang/getall")
					.accept(MediaType.APPLICATION_XML)
					.get(new GenericType<ArrayList<Studiengang>>() {
					});
		}
		return null;
	}

	public int getStudiengangID(String name) {
		if (connect(email, password) == SUCCES) {
			String id = webResource.path("studiengang/getID").path(name)
					.accept(MediaType.APPLICATION_XML).get(String.class);
			return Integer.parseInt(id);
		}
		return 0;
	}

	public ArrayList<Zuordnung> getZuordnungen() {
		if (connect(email, password) == SUCCES) {
			return webResource.path("zuordnung/getList")
					.accept(MediaType.APPLICATION_XML)
					.get(new GenericType<ArrayList<Zuordnung>>() {
					});
		}
		return null;
	}

	public int isConnected() {
		return connected;
	}

	public User login(String eMail, String password) {
		if (connect(eMail, password) == SUCCES) {
			return webResource.path("login").path(eMail).path(password)
					.accept(MediaType.APPLICATION_XML).get(User.class);
		} else {
			return null;
		}

	}

	public ClientResponse setModul(Modul neu) {
		if (connect(email, password) == SUCCES) {
			return webResource.path("modul/post")
					.type(MediaType.APPLICATION_XML)
					.post(ClientResponse.class, neu);
		}
		return null;
	}

	public void setModulhandbuch(Modulhandbuch neu_mh) {
		// TODO Auto-generated method stub

	}

	public ClientResponse setStellvertreter(StellvertreterList sl) {
		if (connect(email, password) == SUCCES) {
			return webResource.path("user/stellv/post")
					.type(MediaType.APPLICATION_XML)
					.post(ClientResponse.class, sl);
		}
		return null;
	}

	public ClientResponse setStudiengang(String name) {
		if (connect(email, password) == SUCCES) {
			return webResource.path("studiengang/post")
					.type(MediaType.APPLICATION_XML)
					.post(ClientResponse.class, name);
		}
		return null;

	}

	public ClientResponse setZuordnung(Zuordnung z) {
		if (connect(email, password) == SUCCES) {
			return webResource.path("zuordnung/post")
					.type(MediaType.APPLICATION_XML)
					.post(ClientResponse.class, z);
		}
		return null;
	}

	public ArrayList<User> userload() {
		if (connect(email, password) == SUCCES) {
			return webResource.path("user/getall")
					.accept(MediaType.APPLICATION_XML)
					.get(new GenericType<ArrayList<User>>() {
					});
		} else {
			return null;
		}
	}

	public ClientResponse usersave(User tmp) {
		if (connect(email, password) == SUCCES) {
			return webResource.path("user/post")
					.type(MediaType.APPLICATION_XML)
					.post(ClientResponse.class, tmp);
		}
		return null;

	}

	public ClientResponse userupdate(User tmp, String mail) {
		if (connect(email, password) == SUCCES) {
			UserUpdateContainer uuc = new UserUpdateContainer(tmp, mail);
			return webResource.path("user/update")
					.type(MediaType.APPLICATION_XML)
					.post(ClientResponse.class, uuc);
		}
		return null;
	}

	public ArrayList<String> getUserRelation(String eMail) {
		if (connect(email, password) == SUCCES) {
			return webResource.path("/user/getRelation/").path(eMail)
					.accept(MediaType.APPLICATION_XML)
					.get(UserRelation.class).getRelation();
		} 
		return null;
	}

	public ArrayList<Modul> getselectedModul(String studiengang, String modultyp, String modulhandbuch) {
		if(connect(email, password) == SUCCES){
			return webResource.path("/modul/getselectedModul/").path(studiengang).path(modultyp).path(modulhandbuch)
					.accept(MediaType.APPLICATION_XML)
					.get(new GenericType<ArrayList<Modul>>(){
					});
		}
		return null;
	}

	public Modul getModul(String name, int v) {
		if (connect(email, password) == SUCCES) {
			return webResource.path("modul/get").path(name).path(v+"")
					.accept(MediaType.APPLICATION_XML).get(Modul.class);
		} else {
			return null;
		}
	}
 
	public ClientResponse acceptModul(Modul m) {
		if (connect(email, password) == SUCCES) {
			return webResource.path("modul/accept")
					.type(MediaType.APPLICATION_XML)
					.post(ClientResponse.class, m);
		} else {
			return null;
		}
		
	}
	
	public void umlreplacer (){  	//HTML to HTML (UTF-8)
	    String content = "";
	    try {						//html-file to html-string
	    	 BufferedReader in = new BufferedReader(new FileReader("modul.html"));
	    	 String str;
	         while ((str = in.readLine()) != null) {

	             content +=str;
	         }
	        content=content.replaceAll("&uuml;","ü");
	     	content=content.replaceAll("&auml;","ä");
	     	content=content.replaceAll("&ouml;","ö");
	     	content=content.replaceAll("&Uuml;","Ü");
	     	content=content.replaceAll("&Auml;","Ä");
	     	content=content.replaceAll("&Ouml;","Ö");
	     	content=content.replaceAll("<META","");
	 	//System.out.println(content); //gibt den bearbeiteten HTML-String aus
	 	in.close();
	    } catch (IOException e) {
	    }
    
    PrintWriter out = null;			//takes html-string and creates html-file
	try {
		out = new PrintWriter(new File("modul.html"), "UTF-8");
	} catch (FileNotFoundException | UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	    out.print(content);
	    out.close();
	
	}
	
	 public void getModulXMLFile (String name)	// xmlstring to xml file
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
	
	
	public void toPdf(String name) throws IOException, DocumentException, TransformerException,

	TransformerConfigurationException, FileNotFoundException {
	
		getModulXMLFile(name);
	
		String pdfname=(name+getPDFname()+".pdf");
		
		
		TransformerFactory tFactory = TransformerFactory.newInstance();
		
		Transformer transformer = tFactory.newTransformer(new StreamSource("style.xsl"));
	
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	     	
		// eigentliche umwandlung
		transformer.transform(new StreamSource("modul.xml"), new StreamResult(
				new OutputStreamWriter(new FileOutputStream("modul.html"), "UTF-8")));
		
		umlreplacer();

		String File_To_Convert = "modul.html";
		String url = new File(File_To_Convert).toURI().toURL().toString();
	
		//System.out.println("" + url);  //gibt pfad der html datei aus
		String HTML_TO_PDF = pdfname;	
		
		FileOutputStream os = new FileOutputStream(HTML_TO_PDF);
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument(url);
		renderer.layout();
		renderer.createPDF(os);
	
		os.close();
		
		Desktop.getDesktop().open(new File(pdfname));
		dclean(); //räumt die Daten welche nicht mehr benötigt werden auf...
	}
	
	public String getPDFname() {
	
        SimpleDateFormat date=new SimpleDateFormat("HHmmss");
        String date1=date.format(new Date());
    return date1;
		
	}
	
	
	public void dclean(){
		File file1 = new File("modul.xml");
		File file2 = new File("modul.html");
		
		file1.delete();
		file2.delete();
	}
	
	public void dclean(String f){	//sollte was bestimmtes gelöscht werden :U
		File f0 = new File(f);
		f0.delete();
	}

	public boolean getModulInEdit(String name) {
		if (connect(email, password) == SUCCES) {
			String b = webResource.path("modul/getInEdit").path(name)
					.accept(MediaType.APPLICATION_XML).get(String.class);
			if(b.equals("true")){
				return true;
			}
		}
		return false;
	}

	public ClientResponse setModulInEdit(Modul m) {
		if (connect(email, password) == SUCCES) {
			return webResource.path("modul/setInEdit")
					.type(MediaType.APPLICATION_XML)
					.post(ClientResponse.class, m);
		} else {
			return null;
		}
	}
	

}
