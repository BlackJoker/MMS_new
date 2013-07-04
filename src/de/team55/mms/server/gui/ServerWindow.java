package de.team55.mms.server.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;

import de.team55.mms.server.ctrl.StartRestServer;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class ServerWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5038037796823409534L;

	private String status = "offline";
	private String serverIP = "";
	private String serverPort = "";
	private String dbHost = "";
	private String dbName = "";
	private String dbPass = "";
	private String dbPort = "";
	private String dbUser = "";

	private Properties prop = new Properties();
	private StartRestServer server;

	private JPanel contentPane;
	private final JPanel pnl_south = new JPanel();
	private final JButton btnServerStarten = new JButton("Server starten");
	private final JButton btnServerBeenden = new JButton("Server beenden");
	private final JPanel pnl_north = new JPanel();
	private final JLabel lblStatus = new JLabel("Status:");
	private final JLabel lblOffline = new JLabel(status);
	private final JPanel pnl_center = new JPanel();
	private final JButton btnEinstellungen = new JButton("Einstellungen");
	private final JLabel lblServerIp = new JLabel("Server IP:");
	private final JLabel lblServerPort = new JLabel("Server Port:");
	private final JLabel lblDatenbankHost = new JLabel("Datenbank Host:");
	private final JLabel lblDatenbankPort = new JLabel("Datenbank Port:");
	private JLabel lblDbhost;
	private JLabel lblDbPort;
	private JLabel lblIp;
	private JLabel lblPort;

	/**
	 * Create the frame.
	 */
	public ServerWindow() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			serverIP = addr.getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initGUI();
	}

	private void initGUI() {
		try {
			prop.load(new FileInputStream("config.properties"));
			serverPort = prop.getProperty("serverPort");
			dbHost = prop.getProperty("dbHost");
			dbPort = prop.getProperty("dbPort");
			dbName = prop.getProperty("database");
			dbUser = prop.getProperty("dbuser");
			dbPass = prop.getProperty("dbpassword");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, "Keine Einstellungen gefunden, bitte neue Daten unter Einstellungen eingeben", "Ladefehler",
					JOptionPane.ERROR_MESSAGE);
			btnServerStarten.setEnabled(false);
		}

		lblDbhost = new JLabel(dbHost);
		lblDbPort = new JLabel(dbPort);
		lblIp = new JLabel(serverIP);
		lblPort = new JLabel(serverPort);

		setTitle("Server");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 387, 244);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(pnl_south, BorderLayout.SOUTH);
		btnServerStarten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnServerStarten.setEnabled(false);
				btnServerBeenden.setEnabled(true);
				btnEinstellungen.setEnabled(false);
				server = new StartRestServer(serverPort);
				try{
					server.startServer();
				} catch (NullPointerException n){
					JOptionPane.showMessageDialog(contentPane, "Der Server läuft bereits", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				}
				lblOffline.setText("online");
				lblOffline.setForeground(Color.GREEN);

			}
		});
		pnl_south.add(btnServerStarten);
		
		btnServerBeenden.setEnabled(false);
		btnServerBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnServerStarten.setEnabled(true);
				btnServerBeenden.setEnabled(false);
				server.stopServer();
				lblOffline.setText("offline");
				lblOffline.setForeground(Color.RED);
				btnEinstellungen.setEnabled(true);

			}
		});
		pnl_south.add(btnServerBeenden);
		
		btnEinstellungen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ServerSettings dialog = new ServerSettings(serverPort,dbHost,dbPort,dbName,dbUser,dbPass);
				int a = dialog.showDialog();
				if (a == 1) {
					serverPort = dialog.getServerPort();
					dbHost = dialog.getDbHost();
					dbPort = dialog.getDbPort();
					dbName = dialog.getDbName();
					dbUser = dialog.getDbUser();
					dbPass = dialog.getDbPasswort();
					lblPort.setText(serverPort);
					lblDbhost.setText(dbHost);
					lblDbPort.setText(dbPort);
					
					btnServerStarten.setEnabled(true);

					try {
						// set the properties value
						prop.setProperty("serverPort", serverPort);
						prop.setProperty("dbHost", dbHost);
						prop.setProperty("dbPort", dbPort);
						prop.setProperty("database", dbName);
						prop.setProperty("dbuser", dbUser);
						prop.setProperty("dbpassword", dbPass);

						// save properties to project root folder
						prop.store(new FileOutputStream("config.properties"),
								null);

					} catch (IOException ex) {
						JOptionPane.showMessageDialog(contentPane, "Speichern fehlgeschlagen, bitte erneut versuchen!", "Speicherfehler",
								JOptionPane.ERROR_MESSAGE);
					}

				}
			}
		});

		pnl_south.add(btnEinstellungen);

		contentPane.add(pnl_north, BorderLayout.NORTH);

		pnl_north.add(lblStatus);
		lblOffline.setForeground(Color.RED);

		pnl_north.add(lblOffline);

		contentPane.add(pnl_center, BorderLayout.CENTER);
		pnl_center.setLayout(new GridLayout(0, 2, 0, 0));
		pnl_center.add(lblServerIp);
		pnl_center.add(lblIp);
		pnl_center.add(lblServerPort);
		pnl_center.add(lblPort);
		pnl_center.add(lblDatenbankHost);
		pnl_center.add(lblDbhost);

		pnl_center.add(lblDatenbankPort);

		pnl_center.add(lblDbPort);

		lblIp.setText(serverIP);
		
		pack();

	}

}
