package de.team55.mms.function;

import java.io.StringWriter;
import java.util.ArrayList;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

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

	public ArrayList<Modulhandbuch> getModulhandbuecher() {
		// TODO Auto-generated method stub
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
	
	
	

}
