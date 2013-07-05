package de.team55.mms.server.ctrl;

import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sun.jersey.api.container.ContainerFactory;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import de.team55.mms.data.User;
import de.team55.mms.server.db.sql;
import de.team55.mms.server.gui.ServerWindow;

public class StartRestServer {

	private HttpServer server;

	public StartRestServer(String port) {
		try {
			server = HttpServerFactory.create("http://localhost:" + port + "/");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
		ServerWindow frame = new ServerWindow();
		frame.setVisible(true);
	}

	public void startServer() {

		HttpHandler handler = ContainerFactory.createContainer(HttpHandler.class);
		server.removeContext("/");
		HttpContext cc = server.createContext("/", handler);

		cc.setAuthenticator(new BasicAuthenticator("mms") {
			@Override
			public boolean checkCredentials(String user, String pass) {
				User tmp = new sql().getUser(user, pass);
				if (tmp == null)
					return false;
				else
					return true;
			}
		});

		server.start();

	}

	public void stopServer() {
		try {
			server.stop(0);
		} catch (NullPointerException np) {
			// Server wurde bereits beendet
		}
	}

}
