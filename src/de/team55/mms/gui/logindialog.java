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

import de.team55.mms.function.Hash;
import de.team55.mms.function.ServerConnection;
import de.team55.mms.data.User;;

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
	public ServerConnection database;

	private User usr = null;

	public logindialog(JFrame owner, String title, ServerConnection database) {
		super(owner, title, true);
		this.setResizable(false);
		this.database = database;
		createDialog();
	}

	public User getUser() {
		return usr;
	}

	public int showCustomDialog() {
		this.setLocationRelativeTo(owner);
		this.show();
		return userResponse;

	}

	private boolean checkLogin() {
		try {
			usr = database.login(textMail.getText(),
					Hash.getMD5(textPass.getText()));
			if (usr != null) {
				return true;
			}
		} catch (NullPointerException e) {
			return false;
		}
		return false;
	}

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
				if (textMail.getText().isEmpty()
						|| textPass.getText().isEmpty()) {
					textPass.setText("");
					JOptionPane.showMessageDialog(owner,
							"Geben Sie gültige Daten ein!", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				} else {

					if (checkLogin()) {
						userResponse = OK_OPTION;
						hide();
					} else {
						if (database.isConnected()==LOGINFALSE) {
							textPass.setText("");
							JOptionPane.showMessageDialog(owner,
									"Login Daten falsch", "Fehler",
									JOptionPane.ERROR_MESSAGE);
						} else {
							textPass.setText("");
							JOptionPane.showMessageDialog(owner,
									"Keine Verbindung zum Server!", "Fehler",
									JOptionPane.ERROR_MESSAGE);
						}
					}

				}
			}

		});
		pnl_footer.add(btnOk);
		getRootPane().setDefaultButton(btnOk);

		JButton btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				userResponse = CANCEL_OPTION;
				hide();
			}
		});
		
		JButton btnRegestrierung = new JButton("Regestrierung");
		btnRegestrierung.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				userdialog dlg = new userdialog(null, "Regestrierung",
						database,true);
				int response = dlg.showCustomDialog();
				// Wenn ok gedrückt wird
				// neuen User abfragen
				if (response == 1) {
					User tmp = dlg.getUser();
					database.usersave(tmp);
					JOptionPane.showConfirmDialog(owner,
							"Ihre Anmeldung wird von eimem Administrator geprüft. Sie werden per e-Mail benachricht, sobald Sie freigeschaltet werden.",
							"Bestätigung", JOptionPane.YES_OPTION);
					userResponse = CANCEL_OPTION;
					hide();
				}
			}
		});
		pnl_footer.add(btnRegestrierung);
		pnl_footer.add(btnAbbrechen);
		this.setContentPane(pnl_Dialog);
		this.pack();
	}

	private boolean validateMail(String eMail) {
		String pat = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(pat);
		Matcher matcher = pattern.matcher(eMail);
		return matcher.matches();
	}

	public ServerConnection getServerConnection() {
		return database;
	}
}
