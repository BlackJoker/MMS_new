package de.team55.mms.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import de.team55.mms.data.*;
import de.team55.mms.function.ServerConnection;

public class mainscreen {

	private static JFrame frame;

	private static final int SUCCES = 2;
	private final Dimension btnSz = new Dimension(140, 50);
	public ServerConnection database = new ServerConnection();

	// Variablen
	private static User current = new User("gast", "gast", "", "gast@gast.gast", "d4061b1486fe2da19dd578e8d970f7eb",
			false, false, false, false); // Gast
	String studtransferstring = ""; // uebergabe String fuer Tabellen -
									// studiengang
	String modbuchtransferstring = ""; // uebergabe String fuer Tabellen -
										// modulbuch
	String modtyptransferstring = ""; // uebergabe String fuer Tabellen -
										// modultyp
	String modulselectionstring = ""; // ubergabe String des ausgewaehlten
										// Moduls

	// Listen
	private ArrayList<User> worklist = null; // Liste mit Usern
	private ArrayList<Studiengang> studienlist = null; // Liste mit
														// Studieng�ngen
	private ArrayList<Modul> selectedmodullist = null; // Liste der Module im
														// durchstoebern segment
	private ArrayList<Modulhandbuch> modulhandlist = null; // Liste der
															// Modulhandbuecher
															// des ausgewaehlten
															// Studiengangs
	private ArrayList<Zuordnung> typen = null; // Liste mit Zuordnungen
	private HashMap<JButton, Integer> buttonmap = new HashMap<JButton, Integer>(); // Map
																					// der
																					// Dynamischen
																					// Buttons
	private ArrayList<String> defaultlabels = new ArrayList<String>();

	// Modelle
	private DefaultTableModel tmodel;
	private DefaultTableModel studmodel;
	private DefaultTableModel modbuchmodel;
	private DefaultTableModel modtypmodel;
	private DefaultTableModel modshowmodel;
	private DefaultComboBoxModel<Studiengang> cbmodel = new DefaultComboBoxModel<Studiengang>();
	private DefaultComboBoxModel<Zuordnung> cbmodel_Z = new DefaultComboBoxModel<Zuordnung>();
	private DefaultListModel<Modul> lm = new DefaultListModel<Modul>();
	private DefaultListModel<Modul> lm_ack = new DefaultListModel<Modul>();

	// Komponenten
	private static JPanel cards = new JPanel();
	private JPanel mod = new JPanel();
	private static JPanel modul_panel = new JPanel();
	private static JPanel modul_panel_edit = new JPanel();
	private static JButton btnModulEinreichen = new JButton("Modul Einreichen");
	private static JButton btnModulVerwaltung = new JButton("Verwaltung");
	private static JButton btnModulBearbeiten = new JButton("Modul bearbeiten");
	private static JButton btnMHB = new JButton("<html>Modulhandb\u00fccher<br>Durchst\u00f6bern");
	private static JButton btnUserVerwaltung = new JButton("User Verwaltung");
	private static JButton btnLogin = new JButton("Einloggen");

	// main Frame
	public mainscreen() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		centerscr();
		topscr();
		leftscr();

		frame.setVisible(true);
	}

	// center Frame
	private void centerscr() {

		frame.getContentPane().add(cards, BorderLayout.CENTER);
		cards.setLayout(new CardLayout(0, 0));

		defaultlabels.add("Zuordnung");
		defaultlabels.add("K�rzel");
		defaultlabels.add("Pr�fungsform");

		defaultlabels.add("Jahrgang");
		defaultlabels.add("Name");
		defaultlabels.add("K\u00fcrzel");
		defaultlabels.add("Titel");
		defaultlabels.add("Leistungspunkte");
		defaultlabels.add("Dauer");
		defaultlabels.add("Turnus");
		defaultlabels.add("Modulverantwortlicher");
		defaultlabels.add("Dozenten");
		defaultlabels.add("Inhalt");
		defaultlabels.add("Lernziele");
		defaultlabels.add("Literatur");
		defaultlabels.add("Sprache");
		defaultlabels.add("Pr\u00fcfungsform");
		defaultlabels.add("Notenbildung");

		homecard();
		usermgtcard();
		newmodulecard();
		modulbearbeitenCard();
		studiengangCard();

	}

	// top frame part
	private void topscr() {
		JPanel top = new JPanel();
		FlowLayout flowLayout = (FlowLayout) top.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		frame.getContentPane().add(top, BorderLayout.NORTH);

		JLabel lblMMS = new JLabel("Modul Management System");
		lblMMS.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblMMS.setHorizontalAlignment(SwingConstants.LEFT);
		lblMMS.setLabelFor(frame);
		top.add(lblMMS);
	}

	// funktionen zum hinzufuegen von Element in die jeweiligen Tabellen
	private void addToTable(User usr) {
		tmodel.addRow(new Object[] { usr.getTitel(), usr.getVorname(), usr.getNachname(), usr.geteMail(),
				usr.getManageUsers(), usr.getCreateModule(), usr.getAcceptModule(), usr.getReadModule() });
	}

	private void addToTable(Studiengang stud) {
		studmodel.addRow(new Object[] { stud.getName() });
	}

	private void addToTable(Modul mod) {
		modshowmodel.addRow(new Object[] { mod.getName() });
	}

	private void addToTable(Modulhandbuch modbuch) {
		modbuchmodel.addRow(new Object[] { modbuch.getJahrgang() });
	}

	private void addToTable(String modtyp) {
		modtypmodel.addRow(new Object[] { modtyp });
	}

	private JPanel defaultmodulPanel(String name, String string, boolean b) {
		final Dimension preferredSize = new Dimension(120, 20);

		JPanel pnl = new JPanel();
		// panel.add(pnl);
		pnl.setLayout(new BoxLayout(pnl, BoxLayout.X_AXIS));

		JLabel label = new JLabel(name);
		label.setPreferredSize(preferredSize);
		pnl.add(label);

		JTextArea txt = new JTextArea(string);
		txt.setLineWrap(true);
		pnl.add(txt);

		JCheckBox dez = new JCheckBox("Dezernat 2", b);
		pnl.add(dez);

		return pnl;
	}

	private void homecard() {
		JPanel welcome = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) welcome.getLayout();
		flowLayout_2.setVgap(20);
		cards.add(welcome, "welcome page");

		JLabel lblNewLabel = new JLabel("Willkommen beim Modul Management System");
		welcome.add(lblNewLabel);

	}

	private void leftscr() {
		JPanel leftpan = new JPanel();
		frame.getContentPane().add(leftpan, BorderLayout.WEST);

		JPanel left = new JPanel();
		leftpan.add(left);
		left.setLayout(new GridLayout(0, 1, 5, 20));

		left.add(btnModulEinreichen);
		btnModulEinreichen.setEnabled(false);
		btnModulEinreichen.setPreferredSize(btnSz);
		btnModulEinreichen.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnModulEinreichen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				typen = database.getZuordnungen();
				studienlist = database.getStudiengaenge();
				cbmodel.removeAllElements();
				for (int i = 0; i < studienlist.size(); i++) {
					cbmodel.addElement(studienlist.get(i));
				}
				cbmodel_Z.removeAllElements();
				for (int i = 0; i < typen.size(); i++) {
					cbmodel_Z.addElement(typen.get(i));
				}
				showCard("newmodule");
			}

		});

		left.add(btnModulBearbeiten);
		btnModulBearbeiten.setEnabled(false);
		btnModulBearbeiten.setPreferredSize(btnSz);
		btnModulBearbeiten.setAlignmentX(Component.CENTER_ALIGNMENT);

		btnModulBearbeiten.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<Modul> module = database.getModule(false);
				lm.removeAllElements();
				for (int i = 0; i < module.size(); i++) {
					lm.addElement(module.get(i));
				}

				module = database.getModule(true);
				lm_ack.removeAllElements();
				for (int i = 0; i < module.size(); i++) {
					lm_ack.addElement(module.get(i));
				}
				showCard("modulbearbeiten");
			}

		});

		left.add(btnLogin);
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				current = database.login(current.geteMail(), current.getPassword());
				if (current != null) {
					if (current.geteMail().equals("gast@gast.gast")) {
						logindialog log = new logindialog(frame, "Login", database);
						int resp = log.showCustomDialog();
						if (resp == 1) {
							current = log.getUser();
							database = log.getServerConnection();
							btnLogin.setText("Ausloggen");
							checkRights();
						}
					} else {
						current = new User("gast", "gast", "", "gast@gast.gast", "d4061b1486fe2da19dd578e8d970f7eb",
								false, false, false, false);
						if (database.isConnected() == SUCCES) {
							checkRights();
						}
						btnLogin.setText("Einloggen");
						btnUserVerwaltung.setText("User Verwaltung");
						btnUserVerwaltung.setEnabled(false);
						showCard("welcome page");
					}
				} else {
					noConnection();
				}
			}
		});
		btnLogin.setPreferredSize(btnSz);
		btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

		btnUserVerwaltung.setEnabled(false);
		left.add(btnUserVerwaltung);
		btnUserVerwaltung.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (current.getManageUsers()) {
					// Tabelle leeren
					tmodel.setRowCount(0);

					// Tabelle mit neuen daten f\u00fcllen
					worklist = database.userload();
					for (int i = 0; i < worklist.size(); i++) {
						addToTable(worklist.get(i));
					}
					showCard("user managment");
				} else {
					userdialog dlg = new userdialog(frame, "User bearbeiten", current, false, database);
					int response = dlg.showCustomDialog();
					// Wenn ok gedr�ckt wird
					// neuen User abfragen
					if (response == 1) {
						User tmp = dlg.getUser();
						if (database.userupdate(tmp, current.geteMail()).getStatus() == 201) {
							current = tmp;
							checkRights();
						} else
							JOptionPane.showMessageDialog(frame, "Update Fehlgeschlagen!", "Update Error",
									JOptionPane.ERROR_MESSAGE);

					}
				}
			}
		});
		btnUserVerwaltung.setPreferredSize(btnSz);
		btnUserVerwaltung.setAlignmentX(Component.CENTER_ALIGNMENT);

		left.add(btnModulVerwaltung);
		btnModulVerwaltung.setEnabled(false);
		btnModulVerwaltung.setPreferredSize(btnSz);
		btnModulVerwaltung.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Jemand ne bessere idee f\u00fcr einen Button mit Zeilenumbruch?
		left.add(btnMHB);
		btnMHB.setEnabled(true);
		btnMHB.setPreferredSize(btnSz);
		btnMHB.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnMHB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				current = database.login(current.geteMail(), current.getPassword());
				if (current != null) {
					studmodel.setRowCount(0);
					studienlist = database.getStudiengaenge();
					for (int i = 0; i < studienlist.size(); i++) {
						addToTable(studienlist.get(i));
					}

					typen = database.getZuordnungen();

					showCard("studiengang show");
				} else {
					noConnection();
				}
			}

		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	private void newmodulecard() {
		modul_panel.removeAll();
		final JPanel pnl_newmod = new JPanel();
		if (!buttonmap.isEmpty()) {
			for (int i = 0; i < buttonmap.size(); i++)
				buttonmap.remove(i);
		}
		final ArrayList<String> labels = new ArrayList<String>();
		labels.addAll(defaultlabels);
		final Dimension preferredSize = new Dimension(120, 20);
		pnl_newmod.setLayout(new BorderLayout(0, 0));

		JPanel pnl_bottom = new JPanel();
		pnl_newmod.add(pnl_bottom, BorderLayout.SOUTH);

		JButton btnNeuesFeld = new JButton("Neues Feld");
		btnNeuesFeld.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String text = "Name des Feldes";
				String name = JOptionPane.showInputDialog(frame, text);
				try {
					while (name.isEmpty() || labels.contains(name)) {
						Object[] params = { "Bitte geben Sie eine g�ltige Bezeichnung ein!", text };
						name = JOptionPane.showInputDialog(frame, params);
					}
					labels.add(name);
					// Platzhalter
					JPanel pnl_tmp = new JPanel();
					modul_panel.add(pnl_tmp);
					modul_panel.add(Box.createRigidArea(new Dimension(0, 5)));

					int numOfPanels = modul_panel.getComponentCount();
					pnl_tmp.setLayout(new BoxLayout(pnl_tmp, BoxLayout.X_AXIS));

					JLabel label_tmp = new JLabel(name);
					label_tmp.setPreferredSize(preferredSize);
					pnl_tmp.add(label_tmp);

					JTextArea txt_tmp = new JTextArea();
					txt_tmp.setLineWrap(true);
					pnl_tmp.add(txt_tmp);

					JCheckBox dez = new JCheckBox("Dezernat 2", false);
					pnl_tmp.add(dez);

					JButton btn_tmp_entf = new JButton("Entfernen");
					btn_tmp_entf.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							int id = buttonmap.get(e.getSource());
							// Bezeichnung aus Liste entfernen
							String name = ((JLabel) ((JPanel) modul_panel.getComponent(id)).getComponent(0)).getText();
							labels.remove(name);

							// Feld mit ID id von Panel entfernen
							modul_panel.remove(id);
							// Platzhalter entfernen
							modul_panel.remove(id - 1);
							// Aus ButtonMap entfernen
							buttonmap.remove(e.getSource());

							// ids der Buttons �ndern, damit auch ein Feld aus
							// der Mitte gel�scht werden kann
							HashMap<JButton, Integer> tmpmap = new HashMap<JButton, Integer>();
							Iterator<Entry<JButton, Integer>> entries = buttonmap.entrySet().iterator();
							while (entries.hasNext()) {
								Entry<JButton, Integer> thisEntry = entries.next();
								JButton key = thisEntry.getKey();
								int value = thisEntry.getValue();
								if (value > id) {
									value = value - 2;
								}
								tmpmap.put(key, value);
							}
							buttonmap = tmpmap;
							modul_panel.revalidate();

						}
					});

					// Button btn_tmp_entf mit ID (numOfPanels-2) zu ButtonMap
					buttonmap.put(btn_tmp_entf, numOfPanels - 2);

					pnl_tmp.add(btn_tmp_entf);

					modul_panel.revalidate();

				} catch (NullPointerException npe) {
					// nichts tuen
				}
			}
		});
		pnl_bottom.add(btnNeuesFeld);

		JButton btnHome = new JButton("Zur\u00fcck");
		btnHome.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modul_panel.removeAll();
				modul_panel.revalidate();
				newmodulecard();
				showCard("welcome page");
			}
		});
		pnl_bottom.add(btnHome);

		JScrollPane scrollPane = new JScrollPane(modul_panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		modul_panel.setLayout(new BoxLayout(modul_panel, BoxLayout.Y_AXIS));

		// Panel Zuordnung + Platzhalter
		JPanel pnl_Z = new JPanel();
		pnl_Z.setLayout(new BoxLayout(pnl_Z, BoxLayout.X_AXIS));
		JLabel label_MH = new JLabel("Zuordnung");

		label_MH.setPreferredSize(preferredSize);
		pnl_Z.add(label_MH);

		final DefaultListModel<Zuordnung> lm = new DefaultListModel<Zuordnung>();
		JList<Zuordnung> zlist = new JList<Zuordnung>(lm);

		zlist.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, false, false);

				return this;
			}
		});
		pnl_Z.add(zlist);

		final JComboBox cb_Z = new JComboBox(cbmodel_Z);
		cb_Z.setMaximumSize(new Dimension(cb_Z.getMaximumSize().width, 20));

		pnl_Z.add(cb_Z);

		JButton z_btn = new JButton("Zuordnung ausw\u00e4hlen");
		z_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!lm.contains((Zuordnung) cb_Z.getSelectedItem()))
					lm.addElement((Zuordnung) cb_Z.getSelectedItem());
			}
		});
		pnl_Z.add(z_btn);

		modul_panel.add(pnl_Z);
		modul_panel.add(Box.createRigidArea(new Dimension(0, 5)));

		for (int i = 3; i < defaultlabels.size(); i++) {
			modul_panel.add(defaultmodulPanel(defaultlabels.get(i), "", false));
			modul_panel.add(Box.createRigidArea(new Dimension(0, 5)));
		}

		JButton btnOk = new JButton("Annehmen");
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Zuordnung> zlist = new ArrayList<Zuordnung>();
				String jg = ((JTextArea) ((JPanel) modul_panel.getComponent(2)).getComponent(1)).getText();
				int jahrgang;
				try {
					jahrgang = Integer.parseInt(jg);
				} catch (NumberFormatException nfe) {
					jahrgang = 0;
				}
				for (int i = 0; i < lm.getSize(); i++) {
					zlist.add(lm.getElementAt(i));
				}

				if (!zlist.isEmpty()) {

					if (jahrgang != 0) {

						String Name = ((JTextArea) ((JPanel) modul_panel.getComponent(4)).getComponent(1)).getText();

						if (Name.isEmpty()) {
							JOptionPane.showMessageDialog(frame, "Bitte f�llen Sie alle Felder aus!", "Eingabe Fehler",
									JOptionPane.ERROR_MESSAGE);
						} else {

							boolean filled = true;
							ArrayList<Feld> felder = new ArrayList<Feld>();
							// Eintraege der Reihe nach auslesen
							for (int i = 6; i < modul_panel.getComponentCount(); i = i + 2) {
								JPanel tmp = (JPanel) modul_panel.getComponent(i);
								JLabel tmplbl = (JLabel) tmp.getComponent(0);
								JTextArea tmptxt = (JTextArea) tmp.getComponent(1);

								boolean dezernat2 = ((JCheckBox) tmp.getComponent(2)).isSelected();
								String value = tmptxt.getText();
								String label = tmplbl.getText();
								if (label.isEmpty()) {
									filled = false;
									break;
								}
								felder.add(new Feld(label, value, dezernat2));
							}
							if (filled == true) {
								int version = database.getModulVersion(Name) + 1;

								Date d = new Date();

								Modul neu = new Modul(Name, zlist, jahrgang, felder, version, d, false, false, current
										.geteMail());
								database.setModul(neu);
								labels.removeAll(labels);
								modul_panel.removeAll();
								modul_panel.revalidate();
								newmodulecard();
								showCard("newmodule");
							} else {
								JOptionPane.showMessageDialog(frame, "Bitte f�llen Sie alle Felder aus!",
										"Eingabe Fehler", JOptionPane.ERROR_MESSAGE);
							}
						}
					} else {
						JOptionPane.showMessageDialog(frame,
								"Bitte geben Sie einen g�ltigen Wert f�r den Jahrgang ein!", "Eingabe Fehler",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Bitte w�hlen Sie min. einen Zuordnung aus!",
							"Eingabe Fehler", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		pnl_bottom.add(btnOk);

		pnl_newmod.add(scrollPane);
		cards.add(pnl_newmod, "newmodule");

	}

	private void removeFromTable(int rowid) {
		tmodel.removeRow(rowid);
	}

	private static void showCard(String card) {
		((CardLayout) cards.getLayout()).show(cards, card);
	}

	@SuppressWarnings("serial")
	private void usermgtcard() {
		JPanel usrmg = new JPanel();
		cards.add(usrmg, "user managment");
		usrmg.setLayout(new BorderLayout(0, 0));

		JPanel usrpan = new JPanel();
		FlowLayout fl_usrpan = (FlowLayout) usrpan.getLayout();
		fl_usrpan.setAlignment(FlowLayout.RIGHT);
		usrmg.add(usrpan, BorderLayout.SOUTH);

		final JTable usrtbl = new JTable();
		JScrollPane ussrscp = new JScrollPane(usrtbl);
		usrtbl.setBorder(new LineBorder(new Color(0, 0, 0)));
		usrtbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//
		// Inhalt der Tabelle
		//
		tmodel = new DefaultTableModel(new Object[][] {}, new String[] { "Titel", "Vorname", "Nachnahme", "e-Mail",
				"Benutzer verwalten", "Module einreichen", "Module Annehmen", "Verwaltung" }) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] { String.class, String.class, String.class, String.class, boolean.class,
					boolean.class, boolean.class, boolean.class };

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		};

		usrtbl.setModel(tmodel);

		JButton btnUserAdd = new JButton("User hinzuf\u00fcgen");
		btnUserAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				userdialog dlg = new userdialog(frame, "User hinzuf\u00fcgen", database);
				int response = dlg.showCustomDialog();
				// Wenn ok ged\u00fcckt wird
				// neuen User abfragen
				// save!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				if (response == 1) {
					User tmp = dlg.getUser();
					database.usersave(tmp);
					addToTable(tmp);
				}
			}

		});
		usrpan.add(btnUserAdd);

		JButton btnUserEdit = new JButton("User bearbeiten");
		btnUserEdit.setToolTipText("Zum Bearbeiten Benutzer in der Tabelle markieren");
		btnUserEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = usrtbl.getSelectedRow();
				if (row != -1) {
					String t = (String) usrtbl.getValueAt(row, 0);
					String vn = (String) usrtbl.getValueAt(row, 1);
					String nn = (String) usrtbl.getValueAt(row, 2);
					String em = (String) usrtbl.getValueAt(row, 3);
					boolean r1 = (boolean) usrtbl.getValueAt(row, 4);
					boolean r2 = (boolean) usrtbl.getValueAt(row, 5);
					boolean r3 = (boolean) usrtbl.getValueAt(row, 6);
					boolean r4 = (boolean) usrtbl.getValueAt(row, 7);
					User alt = new User(vn, nn, t, em, null, r1, r2, r3, r4);

					userdialog dlg = new userdialog(frame, "User bearbeiten", alt, true, database);
					int response = dlg.showCustomDialog();
					// Wenn ok ged\u00fcckt wird
					// neuen User abfragen
					if (response == 1) {
						User tmp = dlg.getUser();
						if (database.userupdate(tmp, em).getStatus() == 201) {
							removeFromTable(row);
							addToTable(tmp);
							if (em.equals(current.geteMail())) {
								current = tmp;
								checkRights();
							}
						} else
							JOptionPane.showMessageDialog(frame, "Update Fehlgeschlagen", "Update Fehler",
									JOptionPane.ERROR_MESSAGE);

					}

				}
			}
		});
		usrpan.add(btnUserEdit);

		JButton btnUserDel = new JButton("User l\u00f6schen");
		btnUserDel.setToolTipText("Zum L\u00f6schen Benutzer in der Tabelle markieren");
		btnUserDel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = usrtbl.getSelectedRow();
				if (row != -1) {
					if (database.deluser((String) usrtbl.getValueAt(row, 3)).getStatus() != 201) {
						removeFromTable(row);
					} else
						JOptionPane.showMessageDialog(frame, "L\u00f6schen Fehlgeschlagen", "Fehler beim L\u00f6schen",
								JOptionPane.ERROR_MESSAGE);

				}
			}
		});
		usrpan.add(btnUserDel);

		JButton btnHome = new JButton("Zur\u00fcck");
		btnHome.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showCard("welcome page");
			}
		});
		usrpan.add(btnHome);

		JPanel usrcenter = new JPanel();
		usrmg.add(usrcenter, BorderLayout.CENTER);
		usrcenter.setLayout(new BorderLayout(5, 5));

		usrcenter.add(ussrscp);
		JPanel leftpan = new JPanel();
		frame.getContentPane().add(leftpan, BorderLayout.WEST);

	}

	protected void checkRights() {
		if (current.getCreateModule())
			btnModulEinreichen.setEnabled(true);
		else
			btnModulEinreichen.setEnabled(false);
		if (current.getAcceptModule()) {
			btnModulVerwaltung.setEnabled(true);
			btnModulBearbeiten.setEnabled(true);
		} else {
			btnModulVerwaltung.setEnabled(false);
			btnModulBearbeiten.setEnabled(false);
		}
		btnUserVerwaltung.setEnabled(true);
		if (current.getManageUsers()) {
			btnUserVerwaltung.setText("User Verwaltung");
		} else {
			btnUserVerwaltung.setText("Account bearbeiten");
			showCard("welcome page");
		}
	}

	public void modulbearbeitenCard() {

		JPanel pnl_modedit = new JPanel();
		pnl_modedit.setLayout(new BorderLayout(0, 0));
		JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
		pnl_modedit.add(tabs);

		JPanel nichtakzeptiert = new JPanel();
		tabs.addTab("Noch nicht akzeptierte Module", null, nichtakzeptiert, null);
		nichtakzeptiert.setLayout(new BorderLayout(0, 0));
		final JList<Modul> list_notack = new JList<Modul>(lm);
		list_notack.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_notack.setLayoutOrientation(JList.VERTICAL_WRAP);

		nichtakzeptiert.add(list_notack);

		JPanel buttonpnl = new JPanel();
		nichtakzeptiert.add(buttonpnl, BorderLayout.SOUTH);

		JButton btnModulBearbeiten = new JButton("Modul bearbeiten");
		btnModulBearbeiten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Modul m = list_notack.getSelectedValue();
				if (!m.isInbearbeitung()) {
					boolean rights = false;
					if (m.getUser().equals(current.geteMail())) {
						rights = true;
					} else {
						ArrayList<String> rel = database.getUserRelation(current.geteMail());
						if (rel.contains(m.getUser())) {
							rights = true;
						}
					}
					if (rights) {
						mod = modeditCard(m);
						cards.add(mod, "modBearbeiten");
						showCard("modBearbeiten");
					} else {
						JOptionPane.showMessageDialog(frame,
								"Sie besitzen nicht die n�tigen Rechte, um dieses Modul zu bearbeiten!",
								"Zugriff verweigert", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Dieses Modul befindet sich gerade in bearbeitung!",
							"Zugriff verweigert", JOptionPane.ERROR_MESSAGE);

				}

			}
		});
		buttonpnl.add(btnModulBearbeiten);

		JButton btnModulAkzeptieren = new JButton("Modul akzeptieren");
		btnModulAkzeptieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		buttonpnl.add(btnModulAkzeptieren);

		JButton btnZurck = new JButton("Zur\u00FCck");
		buttonpnl.add(btnZurck);

		JPanel akzeptiert = new JPanel();
		tabs.addTab("akzeptierte Module", null, akzeptiert, null);
		tabs.setEnabledAt(1, true);
		akzeptiert.setLayout(new BorderLayout(0, 0));

		JList<Modul> list_ack = new JList<Modul>(lm_ack);
		list_ack.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_ack.setLayoutOrientation(JList.VERTICAL_WRAP);
		akzeptiert.add(list_ack);

		JPanel buttonpnl2 = new JPanel();
		akzeptiert.add(buttonpnl2, BorderLayout.SOUTH);

		JButton btnModulBearbeiten2 = new JButton("Modul bearbeiten");
		btnModulBearbeiten2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		buttonpnl2.add(btnModulBearbeiten2);

		JButton btnModulAkzeptieren2 = new JButton("Modul akzeptieren");
		btnModulAkzeptieren2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		buttonpnl2.add(btnModulAkzeptieren2);

		JButton btnZurck2 = new JButton("Zur\u00FCck");
		btnZurck2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		buttonpnl2.add(btnZurck2);
		cards.add(pnl_modedit, "modulbearbeiten");

	}

	private JPanel modeditCard(Modul m) {
		final JPanel pnl_editmod = new JPanel();
		modul_panel_edit.removeAll();
		if (!buttonmap.isEmpty()) {
			for (int i = 0; i < buttonmap.size(); i++)
				buttonmap.remove(i);
		}
		final ArrayList<Feld> felder = m.getFelder();
		final ArrayList<String> labels = new ArrayList<String>();
		for (int i = 0; i < felder.size(); i++) {
			labels.add(felder.get(i).getLabel());
		}

		final Dimension preferredSize = new Dimension(120, 20);
		pnl_editmod.setLayout(new BorderLayout(0, 0));

		JPanel pnl_bottom = new JPanel();
		pnl_editmod.add(pnl_bottom, BorderLayout.SOUTH);

		JButton btnNeuesFeld = new JButton("Neues Feld");
		btnNeuesFeld.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String text = "Name des Feldes";
				String name = JOptionPane.showInputDialog(frame, text);
				try {
					while (name.isEmpty() || labels.contains(name)) {
						Object[] params = { "Bitte geben Sie eine g�ltige Bezeichnung ein!", text };
						name = JOptionPane.showInputDialog(frame, params);
					}
					labels.add(name);
					// Platzhalter
					JPanel pnl_tmp = new JPanel();
					modul_panel_edit.add(pnl_tmp);
					modul_panel_edit.add(Box.createRigidArea(new Dimension(0, 5)));

					int numOfPanels = modul_panel_edit.getComponentCount();
					pnl_tmp.setLayout(new BoxLayout(pnl_tmp, BoxLayout.X_AXIS));

					JLabel label_tmp = new JLabel(name);
					label_tmp.setPreferredSize(preferredSize);
					pnl_tmp.add(label_tmp);

					JTextArea txt_tmp = new JTextArea();
					txt_tmp.setLineWrap(true);
					pnl_tmp.add(txt_tmp);

					JCheckBox dez = new JCheckBox("Dezernat 2", false);
					pnl_tmp.add(dez);

					JButton btn_tmp_entf = new JButton("Entfernen");
					btn_tmp_entf.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							int id = buttonmap.get(e.getSource());
							// Bezeichnung aus Liste entfernen
							String name = ((JLabel) ((JPanel) modul_panel_edit.getComponent(id)).getComponent(0))
									.getText();
							labels.remove(name);

							// Feld mit ID id von Panel entfernen
							modul_panel_edit.remove(id);
							// Platzhalter entfernen
							modul_panel_edit.remove(id - 1);
							// Aus ButtonMap entfernen
							buttonmap.remove(e.getSource());

							// ids der Buttons �ndern, damit auch ein Feld aus
							// der Mitte gel�scht werden kann
							HashMap<JButton, Integer> tmpmap = new HashMap<JButton, Integer>();
							Iterator<Entry<JButton, Integer>> entries = buttonmap.entrySet().iterator();
							while (entries.hasNext()) {
								Entry<JButton, Integer> thisEntry = entries.next();
								JButton key = thisEntry.getKey();
								int value = thisEntry.getValue();
								if (value > id) {
									value = value - 2;
								}
								tmpmap.put(key, value);
							}
							buttonmap = tmpmap;
							modul_panel_edit.revalidate();

						}
					});

					// Button btn_tmp_entf mit ID (numOfPanels-2) zu ButtonMap
					buttonmap.put(btn_tmp_entf, numOfPanels - 2);

					pnl_tmp.add(btn_tmp_entf);

					modul_panel_edit.revalidate();

				} catch (NullPointerException npe) {
					// nichts tuen
				}
			}
		});
		pnl_bottom.add(btnNeuesFeld);

		JButton btnHome = new JButton("Zur\u00fcck");
		btnHome.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modul_panel_edit.removeAll();
				modul_panel_edit.revalidate();
				showCard("modulbearbeiten");
			}
		});
		pnl_bottom.add(btnHome);

		JScrollPane scrollPane = new JScrollPane(modul_panel_edit, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		modul_panel_edit.setLayout(new BoxLayout(modul_panel_edit, BoxLayout.Y_AXIS));

		// Panel Zuordnung + Platzhalter
		JPanel pnl_Z = new JPanel();
		pnl_Z.setLayout(new BoxLayout(pnl_Z, BoxLayout.X_AXIS));
		JLabel label_MH = new JLabel("Zuordnung");

		label_MH.setPreferredSize(preferredSize);
		pnl_Z.add(label_MH);

		final DefaultListModel<Zuordnung> lm = new DefaultListModel<Zuordnung>();
		typen = m.getZuordnungen();
		for (int i = 0; i < typen.size(); i++) {
			lm.addElement(typen.get(i));
		}
		JList<Zuordnung> zlist = new JList<Zuordnung>(lm);

		zlist.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, false, false);

				return this;
			}
		});
		pnl_Z.add(zlist);
		typen = database.getZuordnungen();
		cbmodel_Z.removeAllElements();
		for (int i = 0; i < typen.size(); i++) {
			cbmodel_Z.addElement(typen.get(i));
		}
		final JComboBox cb_Z = new JComboBox(cbmodel_Z);
		cb_Z.setMaximumSize(new Dimension(cb_Z.getMaximumSize().width, 20));

		pnl_Z.add(cb_Z);

		JButton z_btn = new JButton("Zuordnung ausw\u00e4hlen");
		z_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!lm.contains((Zuordnung) cb_Z.getSelectedItem()))
					lm.addElement((Zuordnung) cb_Z.getSelectedItem());
			}
		});
		pnl_Z.add(z_btn);

		modul_panel_edit.add(pnl_Z);
		modul_panel_edit.add(Box.createRigidArea(new Dimension(0, 5)));
		

		modul_panel_edit.add(defaultmodulPanel("Jahrgang", m.getJahrgang() + "", false));
		modul_panel_edit.add(Box.createRigidArea(new Dimension(0, 5)));

		modul_panel_edit.add(defaultmodulPanel("Name", m.getName(), false));
		modul_panel_edit.add(Box.createRigidArea(new Dimension(0, 5)));


		for (int i = 0; i < m.getFelder().size(); i++) {
			Feld f = m.getFelder().get(i);
			JPanel feld = defaultmodulPanel(f.getLabel(), f.getValue(), f.isDezernat());
			if (!defaultlabels.contains(f.getLabel())) {
				JButton btn_tmp_entf = new JButton("Entfernen");
				btn_tmp_entf.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int id = buttonmap.get(e.getSource());
						// Bezeichnung aus Liste entfernen
						String name = ((JLabel) ((JPanel) modul_panel_edit.getComponent(id)).getComponent(0)).getText();
						labels.remove(name);

						// Feld mit ID id von Panel entfernen
						modul_panel_edit.remove(id);
						// Platzhalter entfernen
						modul_panel_edit.remove(id - 1);
						// Aus ButtonMap entfernen
						buttonmap.remove(e.getSource());

						// ids der Buttons �ndern, damit auch ein Feld aus
						// der Mitte gel�scht werden kann
						HashMap<JButton, Integer> tmpmap = new HashMap<JButton, Integer>();
						Iterator<Entry<JButton, Integer>> entries = buttonmap.entrySet().iterator();
						while (entries.hasNext()) {
							Entry<JButton, Integer> thisEntry = entries.next();
							JButton key = thisEntry.getKey();
							int value = thisEntry.getValue();
							if (value > id) {
								value = value - 2;
							}
							tmpmap.put(key, value);
						}
						buttonmap = tmpmap;
						modul_panel_edit.revalidate();

					}
				});

				// Button btn_tmp_entf mit ID (numOfPanels-2) zu ButtonMap
				int numOfPanels = modul_panel_edit.getComponentCount();
				buttonmap.put(btn_tmp_entf, numOfPanels - 2);

				feld.add(btn_tmp_entf);
			}
			modul_panel_edit.add(feld);
			modul_panel_edit.add(Box.createRigidArea(new Dimension(0, 5)));
		}

		JButton btnOk = new JButton("Annehmen");
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Zuordnung> zlist = new ArrayList<Zuordnung>();
				String jg = ((JTextArea) ((JPanel) modul_panel_edit.getComponent(2)).getComponent(1)).getText();
				int jahrgang;
				try {
					jahrgang = Integer.parseInt(jg);
				} catch (NumberFormatException nfe) {
					jahrgang = 0;
				}
				for (int i = 0; i < lm.getSize(); i++) {
					zlist.add(lm.getElementAt(i));
				}

				if (!zlist.isEmpty()) {

					if (jahrgang != 0) {

						String Name = ((JTextArea) ((JPanel) modul_panel_edit.getComponent(4)).getComponent(1))
								.getText();

						if (Name.isEmpty()) {
							JOptionPane.showMessageDialog(frame, "Bitte f�llen Sie alle Felder aus!", "Eingabe Fehler",
									JOptionPane.ERROR_MESSAGE);
						} else {

							boolean filled = true;
							ArrayList<Feld> felder = new ArrayList<Feld>();
							// Eintraege der Reihe nach auslesen
							for (int i = 6; i < modul_panel_edit.getComponentCount(); i = i + 2) {
								JPanel tmp = (JPanel) modul_panel_edit.getComponent(i);
								JLabel tmplbl = (JLabel) tmp.getComponent(0);
								JTextArea tmptxt = (JTextArea) tmp.getComponent(1);

								boolean dezernat2 = ((JCheckBox) tmp.getComponent(2)).isSelected();
								String value = tmptxt.getText();
								String label = tmplbl.getText();
								if (label.isEmpty()) {
									filled = false;
									break;
								}
								felder.add(new Feld(label, value, dezernat2));
							}
							if (filled == true) {
								int version = database.getModulVersion(Name) + 1;

								Date d = new Date();

								Modul neu = new Modul(Name, zlist, jahrgang, felder, version, d, false, false, current
										.geteMail());
								database.setModul(neu);
								labels.removeAll(labels);
								modul_panel_edit.removeAll();
								modul_panel_edit.revalidate();
								newmodulecard();
								showCard("modulbearbeiten");
							} else {
								JOptionPane.showMessageDialog(frame, "Bitte f�llen Sie alle Felder aus!",
										"Eingabe Fehler", JOptionPane.ERROR_MESSAGE);
							}
						}
					} else {
						JOptionPane.showMessageDialog(frame,
								"Bitte geben Sie einen g�ltigen Wert f�r den Jahrgang ein!", "Eingabe Fehler",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Bitte w�hlen Sie min. einen Zuordnung aus!",
							"Eingabe Fehler", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		pnl_bottom.add(btnOk);

		pnl_editmod.add(scrollPane);

		return pnl_editmod;

	}

	@SuppressWarnings("serial")
	private void studiengangCard() {

		JPanel studiengangshow = new JPanel();
		cards.add(studiengangshow, "studiengang show");
		studiengangshow.setLayout(new BorderLayout(0, 0));
		JButton goforit = new JButton("oeffnen");
		final JTable studtable = new JTable();
		JScrollPane studscp = new JScrollPane(studtable);
		studtable.setBorder(new LineBorder(new Color(0, 0, 0)));
		studtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		studiengangshow.add(studscp);
		studiengangshow.add(goforit, BorderLayout.SOUTH);

		studmodel = new DefaultTableModel(new Object[][] {}, new String[] { "Studiengang" }) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] { String.class };

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		};
		studtable.setModel(studmodel);
		studtransferstring = "";
		goforit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int openrow = studtable.getSelectedRow();
				studtransferstring = (String) studtable.getValueAt(openrow, 0);
				modhandshowCard();
				showCard("modbuch show");
			}
		});

	}

	@SuppressWarnings("serial")
	private void modhandshowCard() {
		JPanel modbuchshow = new JPanel();
		cards.add(modbuchshow, "modbuch show");
		modbuchshow.setLayout(new BorderLayout(0, 0));
		JButton goforit = new JButton("oeffnen");
		final JTable modbuchtable = new JTable();
		JScrollPane modtypscp = new JScrollPane(modbuchtable);
		modbuchtable.setBorder(new LineBorder(new Color(0, 0, 0)));
		modbuchtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		modbuchshow.add(modtypscp);
		modbuchshow.add(goforit, BorderLayout.SOUTH);

		modbuchmodel = new DefaultTableModel(new Object[][] {}, new String[] { "Modulhandbuch Jahrgang" }) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] { String.class };

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		};

		modbuchtable.setModel(modbuchmodel);
		modbuchmodel.setRowCount(0);
		modulhandlist = database.getModulhandbuch(studtransferstring);
		for (int i = 0; i < modulhandlist.size(); i++) {

			addToTable(modulhandlist.get(i));

		}
		modbuchtransferstring = "";
		goforit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int openrow = modbuchtable.getSelectedRow();
				modbuchtransferstring = (String) modbuchtable.getValueAt(openrow, 0);
				modtypshowCard();
				showCard("modtyp show");
			}
		});
	}

	@SuppressWarnings("serial")
	private void modtypshowCard() {
		JPanel modtypshow = new JPanel();
		cards.add(modtypshow, "modtyp show");
		modtypshow.setLayout(new BorderLayout(0, 0));
		JButton goforit = new JButton("oeffnen");
		final JTable modtyptable = new JTable();
		JScrollPane modtypscp = new JScrollPane(modtyptable);
		modtyptable.setBorder(new LineBorder(new Color(0, 0, 0)));
		modtyptable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		modtypshow.add(modtypscp);
		modtypshow.add(goforit, BorderLayout.SOUTH);

		modtypmodel = new DefaultTableModel(new Object[][] {}, new String[] { "Modul Typ" }) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] { String.class };

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		};
		modtyptable.setModel(modtypmodel);
		modtypmodel.setRowCount(0);
		int test = 0;
		for (int i = 0; i < studienlist.size(); i++) {
			if (studienlist.get(i).getName().equalsIgnoreCase(studtransferstring)) {
				test = studienlist.get(i).getId();
				break;
			}
		}

		for (int i = 0; i < typen.size(); i++) {
			if (test == (typen.get(i).getSid()))
				addToTable(typen.get(i).getName());
		}
		modtyptransferstring = "";
		goforit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int openrow = modtyptable.getSelectedRow();
				modtyptransferstring = (String) modtyptable.getValueAt(openrow, 0);
				modshowCard();
				showCard("mod show");
			}
		});
	}

	@SuppressWarnings("serial")
	private void modshowCard() {
		JPanel modshow = new JPanel();
		cards.add(modshow, "mod show");
		modshow.setLayout(new BorderLayout(0, 0));
		JButton goforit = new JButton("oeffnen");
		final JTable modshowtable = new JTable();
		JScrollPane modtypscp = new JScrollPane(modshowtable);
		modshowtable.setBorder(new LineBorder(new Color(0, 0, 0)));
		modshowtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		modshow.add(modtypscp);
		modshow.add(goforit, BorderLayout.SOUTH);

		System.out.println(modtyptransferstring);
		System.out.println(modbuchtransferstring);
		System.out.println(studtransferstring);

		modshowmodel = new DefaultTableModel(new Object[][] {}, new String[] { "Module" }) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] { String.class };

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		};

		modshowtable.setModel(modshowmodel);
		modshowmodel.setRowCount(0);
		selectedmodullist = database.getselectedModul(studtransferstring, modtyptransferstring, modbuchtransferstring);
		for (int i = 0; i < selectedmodullist.size(); i++) {
			addToTable(selectedmodullist.get(i));
		}
		modulselectionstring = "";
		goforit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int openrow = modshowtable.getSelectedRow();
				modulselectionstring = (String) modshowtable.getValueAt(openrow, 0);
				// modCard();
				showCard("sel mod show");
				// test
			}
		});

	}

	public static void noConnection() {
		JOptionPane.showMessageDialog(frame, "Keine Verbindung zum Server!", "Verbindungsfehler",
				JOptionPane.ERROR_MESSAGE);
		current = new User("gast", "gast", "", "gast@gast.gast", "d4061b1486fe2da19dd578e8d970f7eb", false, false,
				false, false);
		btnModulEinreichen.setEnabled(false);
		btnModulVerwaltung.setEnabled(false);
		btnModulBearbeiten.setEnabled(false);
		btnMHB.setEnabled(false);
		btnUserVerwaltung.setEnabled(false);
		btnLogin.setText("Einloggen");
		showCard("welcome page");
	}

}
