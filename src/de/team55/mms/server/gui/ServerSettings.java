package de.team55.mms.server.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class ServerSettings extends JDialog {

	private static int response;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtDbHost;
	private JTextField txtDbName;
	private JPasswordField txtDbPasswort;
	private JTextField txtDbPort;
	private JTextField txtDbUser;
	private JTextField txtServerPort;

	/**
	 * Create the dialog.
	 * 
	 * @param dbPass
	 * @param dbUser
	 * @param dbName
	 * @param dbPort
	 * @param dbHost
	 * @param serverPort
	 */
	public ServerSettings(String serverPort, String dbHost, String dbPort, String dbName, String dbUser, String dbPass) {
		initGUI();
		txtServerPort.setText(serverPort);
		txtDbHost.setText(dbHost);
		txtDbPort.setText(dbPort);
		txtDbName.setText(dbName);
		txtDbUser.setText(dbUser);
		txtDbPasswort.setText(dbPass);
	}

	public String getDbHost() {
		return txtDbHost.getText();
	}

	public String getDbName() {
		return txtDbName.getText();
	}

	public String getDbPasswort() {
		return txtDbPasswort.getText();
	}

	public String getDbPort() {
		return txtDbPort.getText();
	}

	public String getDbUser() {
		return txtDbUser.getText();
	}

	public String getServerPort() {
		return txtServerPort.getText();
	}

	private void initGUI() {
		setModal(true);
		setBounds(100, 100, 280, 180);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 2, 5, 0));

		JLabel lblServerPort = new JLabel("Server Port:");
		contentPanel.add(lblServerPort);

		txtServerPort = new JTextField();
		contentPanel.add(txtServerPort);
		txtServerPort.setColumns(10);

		JLabel lblDatenbankHost = new JLabel("Datenbank Host:");
		contentPanel.add(lblDatenbankHost);

		txtDbHost = new JTextField();
		contentPanel.add(txtDbHost);
		txtDbHost.setColumns(10);

		JLabel lblDatenbankPort = new JLabel("Datenbank Port:");
		contentPanel.add(lblDatenbankPort);

		txtDbPort = new JTextField();
		contentPanel.add(txtDbPort);
		txtDbPort.setColumns(10);

		JLabel lblDatenbankName = new JLabel("Datenbank Name:");
		contentPanel.add(lblDatenbankName);

		txtDbName = new JTextField();
		contentPanel.add(txtDbName);
		txtDbName.setColumns(10);
		
		JLabel lblDatenbankBenutzer = new JLabel("Datenbank Benutzer:");
		contentPanel.add(lblDatenbankBenutzer);
		
		txtDbUser = new JTextField();
		contentPanel.add(txtDbUser);
		txtDbUser.setColumns(10);

		JLabel lblDatenbankPasswort = new JLabel("Datenbank Passwort:");
		contentPanel.add(lblDatenbankPasswort);
		
		txtDbPasswort = new JPasswordField();
		contentPanel.add(txtDbPasswort);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(txtDbHost.getText().isEmpty()||txtDbName.getText().isEmpty()||txtDbPasswort.getText().isEmpty()||txtDbPort.getText().isEmpty()|| txtDbUser.getText().isEmpty()||txtServerPort.getText().isEmpty()){
					JOptionPane.showMessageDialog(null,"Bitte alle Felder ausfüllen!", "Eingabefehler",
							JOptionPane.ERROR_MESSAGE);
				} else{
					boolean portsOK=true;
					try{
						int port = Integer.parseInt(txtServerPort.getText());
						if((port<0)||(port>65535)){
							portsOK=false;
						}
						port = Integer.parseInt(txtDbPort.getText());
						if((port<0)||(port>65535)){
							portsOK=false;
						}
					} catch (NumberFormatException nf){
						portsOK=false;
					}
					if(portsOK){
						response = 1;
						setVisible(false);
					} else{
						JOptionPane.showMessageDialog(null,"Bitte geben Sie gültige Werte für die Ports ein!", "Eingabefehler",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Abbrechen");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				response = 0;
				setVisible(false);
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		pack();
	}

	public int showDialog() {
		setVisible(true);
		return response;
	}

}
