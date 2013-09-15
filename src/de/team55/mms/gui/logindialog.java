package de.team55.mms.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import de.team55.mms.data.User;
import de.team55.mms.function.Hash;
import de.team55.mms.function.ServerConnection;

public class logindialog extends JDialog {
	private JFrame owner;

	public static final int OK_OPTION = 1;
	public static final int CANCEL_OPTION = 0;
	private static final int NOCONNECTION = 0;
	private static final int LOGINFALSE = 1;
	private static final int SUCCES = 2;
	private int userResponse;

	private JTextField textMail;
	private JPasswordField textPass;
	public ServerConnection serverConnection;

	private User usr = null;

	// Dialog erzeugen
	public logindialog(JFrame owner, String title, ServerConnection database) {
		super(owner, title, true);
		this.setResizable(false);
		this.serverConnection = database;
		createDialog();
	}

	/**
	 * Gibt den eingeloggte Benutzer zurück
	 * 
	 * @return User der eingeloggte Benutzer
	 */
	public User getUser() {
		return usr;
	}

	/**
	 * Zeigt den Dialog an und gibt den Wert des gedrückten Buttons aus
	 * 
	 * @return int der Wert des gedrückten Buttons
	 */
	public int showCustomDialog() {
		this.setLocationRelativeTo(owner);
		this.show();
		return userResponse;

	}

	/**
	 * Überprüft die Logindaten
	 * 
	 * @return boolean true wenn Login Daten gültig sind, false wenn ungültig
	 */
	private boolean checkLogin() {
		try {
			usr = serverConnection.login(textMail.getText(), Hash.getMD5(textPass.getText()));
			if (usr != null) {
				return true;
			}
		} catch (NullPointerException e) {
			return false;
		}
		return false;
	}

	/**
	 * Erzeugt den Dialog
	 */
	private void createDialog() {

		JPanel pnl_Dialog = new JPanel();
		pnl_Dialog.setLayout(new BorderLayout(0, 0));

		JLabel lblText = new JLabel("Geben Sie ihre Login Daten ein");
		pnl_Dialog.add(lblText, BorderLayout.NORTH);
		lblText.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel pbl_Daten = new JPanel();
		pnl_Dialog.add(pbl_Daten, BorderLayout.CENTER);
		pbl_Daten.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel pnl_Mail = new JPanel();
		pbl_Daten.add(pnl_Mail);
		pnl_Mail.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);
		pnl_Mail.add(panel);

		JLabel lblEmail = new JLabel("e-Mail");
		panel.add(lblEmail);

		JPanel panel_1 = new JPanel();
		pnl_Mail.add(panel_1);

		textMail = new JTextField();
		panel_1.add(textMail);
		textMail.setColumns(10);

		JPanel pnl_Pass = new JPanel();
		pbl_Daten.add(pnl_Pass);
		pnl_Pass.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
		flowLayout_1.setAlignment(FlowLayout.TRAILING);
		pnl_Pass.add(panel_2);

		JLabel lblPassword = new JLabel("Password");
		panel_2.add(lblPassword);

		JPanel panel_3 = new JPanel();
		pnl_Pass.add(panel_3);

		textPass = new JPasswordField();
		panel_3.add(textPass);
		textPass.setColumns(10);

		JPanel pnl_footer = new JPanel();
		pnl_Dialog.add(pnl_footer, BorderLayout.SOUTH);

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Prüfe ob alle Felder ausgefüllt sind
				if (textMail.getText().isEmpty() || textPass.getText().isEmpty()) {
					textPass.setText("");
					JOptionPane.showMessageDialog(owner, "Geben Sie gültige Daten ein!", "Fehler", JOptionPane.ERROR_MESSAGE);
				} else {
					// Überprüfe die Login Daten
					if (checkLogin()) {
						userResponse = OK_OPTION;
						hide();
					} else {
						// Wenn Login fehlgeschalgen ist, prüfe ob Verbindung
						// zum Server besteht
						// Wenn Verbindung besteht, waren die Daten falsch
						if (serverConnection.isConnected() == LOGINFALSE) {
							textPass.setText("");
							JOptionPane.showMessageDialog(owner, "Login Daten falsch", "Fehler", JOptionPane.ERROR_MESSAGE);
						} else {
							// Ansonsten keine Verbindung zum Server
							textPass.setText("");
							JOptionPane.showMessageDialog(owner, "Keine Verbindung zum Server!", "Fehler", JOptionPane.ERROR_MESSAGE);
						}
					}

				}
			}

		});
		pnl_footer.add(btnOk);
		getRootPane().setDefaultButton(btnOk);

		// Bei abbruch Dialog schließen
		JButton btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				userResponse = CANCEL_OPTION;
				hide();
			}
		});

		// Button zur Regestrierung
		JButton btnRegistrierung = new JButton("Registrierung");
		btnRegistrierung.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Dialog zur eingabe der Daten erzeugen
				userdialog dlg = new userdialog(owner, "Registrierung", serverConnection, true);
				int response = dlg.showCustomDialog();
				// Wenn ok gedrückt wird
				// neuen User eintragen
				if (response == 1) {
					User tmp = dlg.getUser();
					if(serverConnection.usersave(tmp).getStatus()==201){
					JOptionPane
							.showMessageDialog(owner,
									"Ihre Anmeldung wird von eimem Administrator geprüft. Sie werden per e-Mail benachricht, sobald Sie freigeschaltet werden.");
					} else {
						JOptionPane
						.showMessageDialog(owner,
								"Ihre Anmeldung ist fehlgeschlagen, bitte versuchen Sie es später erneut.", "Fehler",JOptionPane.ERROR_MESSAGE);
					}
					userResponse = CANCEL_OPTION;
					hide();
				}
			}
		});
		pnl_footer.add(btnRegistrierung);
		pnl_footer.add(btnAbbrechen);
		this.setContentPane(pnl_Dialog);
		this.pack();
	}

	/**
	 * Gibt die Serververbindung zurück, die die Authentifizierungsdaten enthält
	 * 
	 * @return ServerConnection Server Verbindung mit Authentifizierungsdaten
	 */
	public ServerConnection getServerConnection() {
		return serverConnection;
	}
}
