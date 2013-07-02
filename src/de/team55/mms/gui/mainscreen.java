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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
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

	// Konstanten
	private static final int NOCONNECTION = 0;
	private static final int LOGINFALSE = 1;
	private static final int SUCCES = 2;
	private final Dimension btnSz = new Dimension(140, 50);
	public ServerConnection database = new ServerConnection();

	// Variablen
	private User current = new User("gast", "gast", "", "gast@gast.gast",
			"d4061b1486fe2da19dd578e8d970f7eb", false, false, false, false); // Gast
																				// User
	private String uebergabeString = "test";

	// Listen
	private ArrayList<User> worklist = null; // Liste mit Usern
	private ArrayList<Studiengang> studienlist = null; // Liste mit
														// Studieng�ngen
	private ArrayList<Modulhandbuch> modbuchlist = null; // Liste mit
															// Modulhandb�chern
	private ArrayList<Zuordnung> typen = null; // Liste mit Zuordnungen
	private HashMap<JButton, Integer> buttonmap = new HashMap<JButton, Integer>(); // Map
																					// f�r
																					// Dynamische
																					// Buttons
	private ArrayList<String> modtyplist = null;

	// Modelle
	private DefaultTableModel tmodel;
	private DefaultTableModel studmodel;
	private DefaultTableModel modbuchmodel;
	private DefaultTableModel modtypmodel;
	private DefaultComboBoxModel<Studiengang> cbmodel = new DefaultComboBoxModel<Studiengang>();
	private DefaultComboBoxModel<Zuordnung> cbmodel_Z = new DefaultComboBoxModel<Zuordnung>();
	private DefaultListModel<Modul> lm = new DefaultListModel<Modul>();
	private DefaultListModel<Modul> lm_ack = new DefaultListModel<Modul>();

	// Komponenten
	private JPanel cards = new JPanel();
	private JPanel mod = new JPanel();
	private static JPanel panel = new JPanel();
	private JButton btnModulEinreichen = new JButton("Modul Einreichen");
	private JButton btnModulVerwaltung = new JButton("Modul Verwaltung");
	private JButton btnModulBearbeiten = new JButton("Modul bearbeiten");
	private JButton btnMHB = new JButton(
			"<html>Modulhandb�cher<br>Durchst�bern");
	private JButton btnUserVerwaltung = new JButton("User Verwaltung");
	private JButton btnLogin = new JButton("Einloggen");

	public mainscreen() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		current = database.login(current.geteMail(), current.getPassword());

		centerscr();
		topscr();
		leftscr();

		frame.setVisible(true);
	}

	private void centerscr() {

		frame.getContentPane().add(cards, BorderLayout.CENTER);
		cards.setLayout(new CardLayout(0, 0));
		// cards.add(mod);

		homecard();
		usermgtcard();
		newmodulecard();
		modulbearbeitenCard();
		studiengangCard();
		// modulhandbuchshowCard();

	}

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

	private void addToTable(User usr) {
		tmodel.addRow(new Object[] { usr.getTitel(), usr.getVorname(),
				usr.getNachname(), usr.geteMail(), usr.getManageUsers(),
				usr.getCreateModule(), usr.getAcceptModule(),
				usr.getReadModule() });
	}

	private void addToTable(Studiengang stud) {
		studmodel.addRow(new Object[] { stud.getName() });
	}

	private void addToTable(Modulhandbuch modbuch) {
		modbuchmodel.addRow(new Object[] { modbuch.getJahrgang() });
	}

	private JPanel defaultmodulPanel(String name, String string) {
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

		return pnl;
	}

	private void homecard() {
		JPanel welcome = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) welcome.getLayout();
		flowLayout_2.setVgap(20);
		cards.add(welcome, "welcome page");

		JLabel lblNewLabel = new JLabel(
				"Willkommen beim Modul Management System");
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
					current = new User("gast", "gast", "", "gast@gast.gast",
							"d4061b1486fe2da19dd578e8d970f7eb", false, false,
							false, false);
					if (database.isConnected() == SUCCES) {
						checkRights();
					}
					btnLogin.setText("Einloggen");
					btnUserVerwaltung.setText("User Verwaltung");
					btnUserVerwaltung.setEnabled(false);
					showCard("welcome page");
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

					// Tabelle mit neuen daten f�llen
					worklist = database.userload();
					for (int i = 0; i < worklist.size(); i++) {
						addToTable(worklist.get(i));
					}
					showCard("user managment");
				} else {
					userdialog dlg = new userdialog(frame, "User bearbeiten",
							current, false, database);
					int response = dlg.showCustomDialog();
					// Wenn ok ged�ckt wird
					// neuen User abfragen
					if (response == 1) {
						User tmp = dlg.getUser();
						if (database.userupdate(tmp, current.geteMail())
								.getStatus() == 201) {
							current = tmp;
							checkRights();
						} else
							JOptionPane.showMessageDialog(frame,
									"Update Fehlgeschlagen!", "Update Error",
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

		// Jemand ne bessere idee f�r einen Button mit Zeilenumbruch?
		left.add(btnMHB);
		btnMHB.setEnabled(true);
		btnMHB.setPreferredSize(btnSz);
		btnMHB.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnMHB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				studmodel.setRowCount(0);
				studienlist = database.getStudiengaenge();
				for (int i = 0; i < studienlist.size(); i++) {
					addToTable(studienlist.get(i));
				}
				
				typen = database.getZuordnungen();
				
				showCard("studiengang show");
			}

		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	private void newmodulecard() {
		final JPanel pnl_newmod = new JPanel();

		final Dimension preferredSize = new Dimension(120, 20);
		pnl_newmod.setLayout(new BorderLayout(0, 0));

		JPanel pnl_bottom = new JPanel();
		pnl_newmod.add(pnl_bottom, BorderLayout.SOUTH);

		JButton btnNeuesFeld = new JButton("Neues Feld");
		btnNeuesFeld.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Platzhalter
				JPanel pnl_tmp = new JPanel();
				panel.add(pnl_tmp);
				panel.add(Box.createRigidArea(new Dimension(0, 5)));

				int numOfPanels = panel.getComponentCount();
				pnl_tmp.setLayout(new BoxLayout(pnl_tmp, BoxLayout.X_AXIS));

				JCheckBox checkbox = new JCheckBox("Dezernat 2 Feld");
				String text = "Name des Feldes";
				Object[] params = { checkbox, text };
				String name = JOptionPane.showInputDialog(frame, params);
				boolean dezernat2 = checkbox.isSelected();

				JLabel label_tmp = new JLabel(name);
				label_tmp.setPreferredSize(preferredSize);
				pnl_tmp.add(label_tmp);

				JTextArea txt_tmp = new JTextArea();
				txt_tmp.setLineWrap(true);
				pnl_tmp.add(txt_tmp);

				JCheckBox dez = new JCheckBox("Dezernat 2", dezernat2);
				pnl_tmp.add(dez);

				JButton btn_tmp_entf = new JButton("Entfernen");
				btn_tmp_entf.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int id = buttonmap.get(e.getSource());
						// Feld mit ID id von Panel entfernen
						panel.remove(id);
						// Platzhalter entfernen
						panel.remove(id - 1);
						// Aus ButtonMap entfernen
						buttonmap.remove(e.getSource());

						// ids der Buttons �ndern, damit auch ein Feld aus der
						// Mitte gel�scht werden kann
						HashMap<JButton, Integer> tmpmap = new HashMap<JButton, Integer>();
						Iterator<Entry<JButton, Integer>> entries = buttonmap
								.entrySet().iterator();
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
						panel.revalidate();

					}
				});

				// Button btn_tmp_entf mit ID (numOfPanels-2) zu ButtonMap
				buttonmap.put(btn_tmp_entf, numOfPanels - 2);

				pnl_tmp.add(btn_tmp_entf);

				panel.revalidate();
			}
		});
		pnl_bottom.add(btnNeuesFeld);

		JButton btnHome = new JButton("Zur�ck");
		btnHome.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.removeAll();
				panel.revalidate();
				newmodulecard();
				showCard("welcome page");
			}
		});
		pnl_bottom.add(btnHome);

		JScrollPane scrollPane = new JScrollPane(panel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// Panel Zuordnung + Platzhalter
		JPanel pnl_MH = new JPanel();
		JLabel label_MH = new JLabel("Zuordnung");
		label_MH.setPreferredSize(preferredSize);
		pnl_MH.add(label_MH);

		final DefaultListModel<Zuordnung> lm = new DefaultListModel<Zuordnung>();
		JList<Zuordnung> zlist = new JList<Zuordnung>(lm);

		zlist.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, false,
						false);

				return this;
			}
		});
		pnl_MH.add(zlist);

		final JComboBox cb_Z = new JComboBox(cbmodel_Z);
		cb_Z.setMaximumSize(new Dimension(cb_Z.getMaximumSize().width, 20));

		pnl_MH.add(cb_Z);

		JButton z_btn = new JButton("Zuordnung ausw�hlen");
		z_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!lm.contains((Zuordnung) cb_Z.getSelectedItem()))
					lm.addElement((Zuordnung) cb_Z.getSelectedItem());
			}
		});
		pnl_MH.add(z_btn);

		JButton nMH_btn = new JButton("neue Zuordnung");
		nMH_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {

					JTextField neu_Name = new JTextField();
					JTextField neu_Abschluss = new JTextField();
					JComboBox<Studiengang> neu_sgbox = new JComboBox<Studiengang>(
							cbmodel);
					JPanel sp = new JPanel();
					sp.add(neu_sgbox);

					JButton nsg = new JButton("Neuer Studiengang");
					nsg.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {

							try {
								String name = JOptionPane.showInputDialog(
										frame, "Name des neuen Studiengangs:",
										"neuer Studiengang",
										JOptionPane.PLAIN_MESSAGE);

								while (name.isEmpty()) {
									name = JOptionPane
											.showInputDialog(
													frame,
													"Bitte g�ltigen Namen des neuen Studiengangs eingeben:",
													"neuer Studiengang",
													JOptionPane.PLAIN_MESSAGE);
								}

								studienlist = database.getStudiengaenge();
								boolean neu = true;
								for (int i = 0; i < studienlist.size(); i++) {
									if (studienlist.get(i).equals(name)) {
										neu = false;
										break;
									}
								}
								if (neu) {
									database.setStudiengang(name);
									cbmodel.removeAllElements();
									studienlist = database.getStudiengaenge();
									for (int i = 0; i < studienlist.size(); i++)
										cbmodel.addElement(studienlist.get(i));
								} else {
									JOptionPane
											.showMessageDialog(
													frame,
													"Studiengang ist schon vorhanden",
													"Fehler",
													JOptionPane.ERROR_MESSAGE);
								}
							} catch (NullPointerException np) {

							}
						}

					});

					sp.add(nsg);
					Object[] message = { "Name des Types:", neu_Name,
							"Abschluss:", neu_Abschluss, "Studiengang:", sp };

					int option = JOptionPane.showConfirmDialog(frame, message,
							"Neuen Typ anlegen", JOptionPane.OK_CANCEL_OPTION);
					if (option == JOptionPane.OK_OPTION) {

						while ((neu_Name.getText().isEmpty()
								|| (neu_sgbox.getSelectedItem() == null) || neu_Abschluss
								.getText().isEmpty())
								&& (option == JOptionPane.OK_OPTION)) {
							Object[] messageEmpty = {
									"Bitte alle Felder ausf�llen!",
									"Name des Types:", neu_Name, "Abschluss:",
									neu_Abschluss, "Studiengang:", sp };
							option = JOptionPane.showConfirmDialog(frame,
									messageEmpty, "Neuen Typ anlegen",
									JOptionPane.OK_CANCEL_OPTION);
						}
						if (option == JOptionPane.OK_OPTION) {
							Studiengang s = (Studiengang) neu_sgbox
									.getSelectedItem();
							Zuordnung z = new Zuordnung(neu_Name.getText(), s
									.getName(), s.getId(), neu_Abschluss
									.getText());

							boolean neu = true;
							for (int i = 0; i < typen.size(); i++) {
								if (typen.get(i).equals(z)) {
									neu = false;
									break;
								}
							}
							if (neu) {
								database.setZuordnung(z);
								cbmodel_Z.removeAllElements();
								typen = database.getZuordnungen();
								for (int i = 0; i < typen.size(); i++)
									cbmodel_Z.addElement(typen.get(i));
							} else {
								JOptionPane.showMessageDialog(frame,
										"Zuordnung ist schon vorhanden",
										"Fehler", JOptionPane.ERROR_MESSAGE);
							}
						}
					}

				} catch (NullPointerException np) {
					np.printStackTrace();
				}
			}

		});
		pnl_MH.add(nMH_btn);

		pnl_MH.setLayout(new BoxLayout(pnl_MH, BoxLayout.X_AXIS));
		panel.add(pnl_MH);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel Studiengang + Platzhalter
		// JPanel pnl_Sg = new JPanel();
		// JLabel label = new JLabel("Studiengang");
		// label.setPreferredSize(preferredSize);
		// pnl_Sg.add(label);
		//
		// final DefaultListModel<String> lm = new DefaultListModel<String>();
		// JList<String> st = new JList<String>(lm);
		//
		// st.setCellRenderer(new DefaultListCellRenderer() {
		// @Override
		// public Component getListCellRendererComponent(JList list,
		// Object value, int index, boolean isSelected,
		// boolean cellHasFocus) {
		// super.getListCellRendererComponent(list, value, index, false,
		// false);
		//
		// return this;
		// }
		// });
		// pnl_Sg.add(st);
		//
		// // final DefaultComboBoxModel cbmodel = new
		// // DefaultComboBoxModel(database.getStudiengaenge().toArray());
		//
		// final JComboBox sgbox = new JComboBox(cbmodel);
		// sgbox.setMaximumSize(new Dimension(sgbox.getMaximumSize().width,
		// 20));
		//
		// pnl_Sg.add(sgbox);
		//
		// JButton sg = new JButton("Studiengang ausw�hlen");
		// sg.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// if (!lm.contains(sgbox.getSelectedItem().toString()))
		// lm.addElement(sgbox.getSelectedItem().toString());
		// }
		// });
		// pnl_Sg.add(sg);

		// JButton nsg = new JButton("Neuer Studiengang");
		// nsg.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		//
		// try {
		// String name = JOptionPane.showInputDialog(frame,
		// "Name des neuen Studiengangs:",
		// "neuer Studiengang", JOptionPane.PLAIN_MESSAGE);
		//
		// while (name.isEmpty()) {
		// name = JOptionPane
		// .showInputDialog(
		// frame,
		// "Bitte g�ltigen Namen des neuen Studiengangs eingeben:",
		// "neuer Studiengang",
		// JOptionPane.PLAIN_MESSAGE);
		// }
		//
		// ArrayList<Studiengang> sgs = database.getStudiengaenge();
		// boolean neu = true;
		// for (int i = 0; i < sgs.size(); i++) {
		// if (sgs.get(i).equals(name)) {
		// neu = false;
		// break;
		// }
		// }
		// if (neu) {
		// database.setStudiengang(name);
		// cbmodel.removeAllElements();
		// sgs = database.getStudiengaenge();
		// for (int i = 0; i < sgs.size(); i++)
		// cbmodel.addElement(sgs.get(i));
		//
		// // cbmodel.addElement(name);
		// } else {
		// JOptionPane.showMessageDialog(frame,
		// "Studiengang ist schon vorhanden", "Fehler",
		// JOptionPane.ERROR_MESSAGE);
		// }
		// } catch (NullPointerException np) {
		//
		// }
		// }
		//
		// });
		// pnl_Sg.add(nsg);

		// pnl_Sg.setLayout(new BoxLayout(pnl_Sg, BoxLayout.X_AXIS));
		// panel.add(pnl_Sg);
		// panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel Jahrgang + Platzhalter
		panel.add(defaultmodulPanel("Jahrgang", ""));
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel Name + Platzhalter
		panel.add(defaultmodulPanel("Name", ""));
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel K�rzel + Platzhalter
		panel.add(defaultmodulPanel("K�rzel", ""));
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel Titel + Platzhaler
		panel.add(defaultmodulPanel("Titel", ""));
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel LP + Platzhalter
		panel.add(defaultmodulPanel("Leistungspunkte", ""));
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel Dauer + Platzhalter
		panel.add(defaultmodulPanel("Dauer", ""));
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel Turnus + Platzhalter
		panel.add(defaultmodulPanel("Turnus", ""));
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel Modulverantwortlicher + Platzhalter
		panel.add(defaultmodulPanel("Modulverantwortlicher", ""));
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel Dozenten + Platzhalter
		panel.add(defaultmodulPanel("Dozenten", ""));
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel Inhalt + Platzhalter
		panel.add(defaultmodulPanel("Inhalt", ""));
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel Ziele + Platzhalter
		panel.add(defaultmodulPanel("Lernziele", ""));
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel Literatur + Platzhalter
		panel.add(defaultmodulPanel("Literatur", ""));
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel Sprache + Platzhalter
		panel.add(defaultmodulPanel("Sprache", ""));
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel Pr�fungsform + Platzhalter
		panel.add(defaultmodulPanel("Pr�fungsform", ""));
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel Notenbildung + Platzhalter
		panel.add(defaultmodulPanel("Notenbildung", ""));
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		JButton btnOk = new JButton("Annehmen");
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Modulhandbuch> mblist = new ArrayList<Modulhandbuch>();
				ArrayList<Zuordnung> zlist = new ArrayList<Zuordnung>();
				String jg = ((JTextArea) ((JPanel) panel.getComponent(2))
						.getComponent(1)).getText();
				int jahrgang = Integer.parseInt(jg);

				for (int i = 0; i < lm.getSize(); i++) {
					zlist.add(lm.getElementAt(i));
				}

				String Name = ((JTextArea) ((JPanel) panel.getComponent(4))
						.getComponent(1)).getText();

				ArrayList<String> labels = new ArrayList<String>();
				ArrayList<String> values = new ArrayList<String>();
				ArrayList<Boolean> dez = new ArrayList<Boolean>();

				// Eintraege der Reihe nach auslesen
				for (int i = 6; i < panel.getComponentCount(); i = i + 2) {
					JPanel tmp = (JPanel) panel.getComponent(i);
					JLabel tmplbl = (JLabel) tmp.getComponent(0);
					JTextArea tmptxt = (JTextArea) tmp.getComponent(1);
					boolean dezernat2 = false;
					if (tmp.getComponentCount() > 3) {
						dezernat2 = ((JCheckBox) tmp.getComponent(2))
								.isSelected();
					}
					String value = tmptxt.getText();
					String label = tmplbl.getText();
					labels.add(label);
					values.add(value);
					dez.add(dezernat2);
				}
				int version = database.getModulVersion(Name) + 1;

				Modul neu = new Modul(Name, zlist, jahrgang, labels, values,
						version, dez);
				database.setModul(neu);
				panel.removeAll();
				panel.revalidate();
				newmodulecard();
				showCard("newmodule");
			}
		});
		pnl_bottom.add(btnOk);

		pnl_newmod.add(scrollPane);
		cards.add(pnl_newmod, "newmodule");

	}

	private void removeFromTable(int rowid) {
		tmodel.removeRow(rowid);
	}

	private void showCard(String card) {
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
		tmodel = new DefaultTableModel(new Object[][] {}, new String[] {
				"Titel", "Vorname", "Nachnahme", "e-Mail", "User bearbeiten",
				"Module einreichen", "Module Annehmen", "Module lesen" }) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] { String.class, String.class,
					String.class, String.class, boolean.class, boolean.class,
					boolean.class, boolean.class };

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

		JButton btnUserAdd = new JButton("User hinzuf�gen");
		btnUserAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				userdialog dlg = new userdialog(frame, "User hinzuf�gen",database);
				int response = dlg.showCustomDialog();
				// Wenn ok ged�ckt wird
				// neuen User abfragen
				//save!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				if (response == 1) {
					User tmp = dlg.getUser();
					database.usersave(tmp);
					addToTable(tmp);
				}
			}

		});
		usrpan.add(btnUserAdd);

		JButton btnUserEdit = new JButton("User bearbeiten");
		btnUserEdit
				.setToolTipText("Zum Bearbeiten Benutzer in der Tabelle markieren");
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

					userdialog dlg = new userdialog(frame, "User bearbeiten",
							alt, true, database);
					int response = dlg.showCustomDialog();
					// Wenn ok ged�ckt wird
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
							JOptionPane.showMessageDialog(frame,
									"Update Fehlgeschlagen", "Update Fehler",
									JOptionPane.ERROR_MESSAGE);

					}

				}
			}
		});
		usrpan.add(btnUserEdit);

		JButton btnUserDel = new JButton("User l�schen");
		btnUserDel
				.setToolTipText("Zum L�schen Benutzer in der Tabelle markieren");
		btnUserDel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = usrtbl.getSelectedRow();
				if (row != -1) {
					if (database.deluser((String) usrtbl.getValueAt(row, 3))
							.getStatus() != 201) {
						removeFromTable(row);
					} else
						JOptionPane.showMessageDialog(frame,
								"L�schen Fehlgeschlagen",
								"Fehler beim L�schen",
								JOptionPane.ERROR_MESSAGE);

				}
			}
		});
		usrpan.add(btnUserDel);

		JButton btnHome = new JButton("Zur�ck");
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

		JPanel panel = new JPanel();
		JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
		panel.add(tabs, BorderLayout.CENTER);

		JPanel nichtakzeptiert = new JPanel();
		tabs.addTab("Noch nicht akzeptierte Module", null, nichtakzeptiert,
				null);
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
				// System.out.print(list_notack.getSelectedValue());
				Modul m = list_notack.getSelectedValue();

				// cards.remove(mod);
				mod = modeditCard(m);
				cards.add(mod, "modBearbeiten");
				showCard("modBearbeiten");
			}
		});
		buttonpnl.add(btnModulBearbeiten);

		JButton btnModulAkzeptieren = new JButton("Modul akzeptieren");
		btnModulAkzeptieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		buttonpnl.add(btnModulAkzeptieren);

		JButton btnModulVerwerfen = new JButton("Modul verwerfen");
		btnModulVerwerfen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		buttonpnl.add(btnModulVerwerfen);

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
		cards.add(panel, "modulbearbeiten");

	}

	private JPanel modeditCard(Modul m) {
		final JPanel pnl_newmod = new JPanel();
		panel.removeAll();

		final Dimension preferredSize = new Dimension(120, 20);
		pnl_newmod.setLayout(new BorderLayout(0, 0));

		JPanel pnl_bottom = new JPanel();
		pnl_newmod.add(pnl_bottom, BorderLayout.SOUTH);

		JButton btnNeuesFeld = new JButton("Neues Feld");
		btnNeuesFeld.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Platzhalter
				JPanel pnl_tmp = new JPanel();
				panel.add(pnl_tmp);
				panel.add(Box.createRigidArea(new Dimension(0, 5)));

				int numOfPanels = panel.getComponentCount();
				pnl_tmp.setLayout(new BoxLayout(pnl_tmp, BoxLayout.X_AXIS));

				JCheckBox checkbox = new JCheckBox("Dezernat 2 Feld");
				String text = "Name des Feldes";
				Object[] params = { checkbox, text };
				String name = JOptionPane.showInputDialog(frame, params);
				boolean dezernat2 = checkbox.isSelected();

				JLabel label_tmp = new JLabel(name);
				label_tmp.setPreferredSize(preferredSize);
				pnl_tmp.add(label_tmp);

				JTextArea txt_tmp = new JTextArea();
				txt_tmp.setLineWrap(true);
				pnl_tmp.add(txt_tmp);

				JCheckBox dez = new JCheckBox("Dezernat 2", dezernat2);
				pnl_tmp.add(dez);

				JButton btn_tmp_entf = new JButton("Entfernen");
				btn_tmp_entf.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int id = buttonmap.get(e.getSource());
						// Feld mit ID id von Panel entfernen
						panel.remove(id);
						// Platzhalter entfernen
						panel.remove(id - 1);
						// Aus ButtonMap entfernen
						buttonmap.remove(e.getSource());

						// ids der Buttons �ndern, damit auch ein Feld aus der
						// Mitte gel�scht werden kann
						HashMap<JButton, Integer> tmpmap = new HashMap<JButton, Integer>();
						Iterator<Entry<JButton, Integer>> entries = buttonmap
								.entrySet().iterator();
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
						panel.revalidate();

					}
				});

				// Button btn_tmp_entf mit ID (numOfPanels-2) zu ButtonMap
				buttonmap.put(btn_tmp_entf, numOfPanels - 2);

				pnl_tmp.add(btn_tmp_entf);

				panel.revalidate();
			}
		});
		pnl_bottom.add(btnNeuesFeld);

		JButton btnHome = new JButton("Zur�ck");
		btnHome.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.removeAll();
				panel.revalidate();
				newmodulecard();
				showCard("welcome page");
			}
		});
		pnl_bottom.add(btnHome);

		JScrollPane scrollPane = new JScrollPane(panel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// Panel Modulhandbuch + Platzhalter
		JPanel pnl_MH = new JPanel();
		JLabel label_MH = new JLabel("Modulhandbuch");
		label_MH.setPreferredSize(preferredSize);
		pnl_MH.add(label_MH);

		// final DefaultComboBoxModel cbmodel_MH = new
		// DefaultComboBoxModel(database.getModulhandbuecher().toArray());
		final DefaultComboBoxModel cbmodel_MH = new DefaultComboBoxModel();

		final JComboBox cb_MH = new JComboBox(cbmodel_MH);
		cb_MH.setMaximumSize(new Dimension(cb_MH.getMaximumSize().width, 20));

		pnl_MH.add(cb_MH);

		JButton nMH_btn = new JButton("Neues Modulhandbuch");
		nMH_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {

					JTextField neu_Name = new JTextField();
					JTextField neu_Jahrgang = new JTextField();
					DefaultComboBoxModel cbm = new DefaultComboBoxModel(
							database.getStudiengaenge().toArray());
					JComboBox neu_sgbox = new JComboBox(cbm);
					Object[] message = { "Name des Modulhandbuches:", neu_Name,
							"Studiengang:", neu_sgbox, "Jahrgang:",
							neu_Jahrgang };

					int option = JOptionPane.showConfirmDialog(frame, message,
							"Neues Modulhandbuch anlegen",
							JOptionPane.OK_CANCEL_OPTION);
					if (option == JOptionPane.OK_OPTION) {

						while ((neu_Name.getText().isEmpty()
								|| (neu_sgbox.getSelectedItem() == null) || neu_Jahrgang
								.getText().isEmpty())
								&& (option == JOptionPane.OK_OPTION)) {
							Object[] messageEmpty = {
									"Bitte alle Felder ausf�llen!",
									"Name des Modulhandbuches:", neu_Name,
									"Studiengang:", neu_sgbox, "Jahrgang:",
									neu_Jahrgang };
							option = JOptionPane.showConfirmDialog(frame,
									messageEmpty,
									"Neues Modulhandbuch anlegen",
									JOptionPane.OK_CANCEL_OPTION);
						}
						if (option == JOptionPane.OK_OPTION) {
							Studiengang s = (Studiengang) neu_sgbox
									.getSelectedItem();
							int id = s.getId();
							Modulhandbuch neu_mh = new Modulhandbuch(
									neu_Jahrgang.getText(), id);
							ArrayList<Modulhandbuch> MHs = database
									.getModulhandbuecher();
							boolean neu = true;
							for (int i = 0; i < MHs.size(); i++) {
								Modulhandbuch alt = MHs.get(i);
								if (alt.equals(neu_mh)) {
									neu = false;
									break;
								}
							}
							if (neu) {
								database.setModulhandbuch(neu_mh);
								cbmodel_MH.removeAllElements();
								MHs = database.getModulhandbuecher();
								for (int i = 0; i < MHs.size(); i++)
									cbmodel_MH.addElement(MHs.get(i));
							} else {
								JOptionPane.showMessageDialog(frame,
										"Modulhandbuch ist schon vorhanden",
										"Fehler", JOptionPane.ERROR_MESSAGE);
							}
						}
					}

				} catch (NullPointerException np) {
					np.printStackTrace();
				}
			}

		});
		pnl_MH.add(nMH_btn);

		pnl_MH.setLayout(new BoxLayout(pnl_MH, BoxLayout.X_AXIS));
		panel.add(pnl_MH);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel Studiengang + Platzhalter
		JPanel pnl_Sg = new JPanel();
		JLabel label = new JLabel("Studiengang");
		label.setPreferredSize(preferredSize);
		pnl_Sg.add(label);

		final DefaultListModel<String> lm = new DefaultListModel<String>();
		JList<String> st = new JList<String>(lm);

		ArrayList<Studiengang> sgs = m.getStudiengang();
		for (int i = 0; i < sgs.size(); i++)
			lm.addElement(sgs.get(i).getName());
		st.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, false,
						false);

				return this;
			}
		});
		pnl_Sg.add(st);

		// final DefaultComboBoxModel cbmodel = new
		// DefaultComboBoxModel(database.getStudiengaenge().toArray());

		final JComboBox sgbox = new JComboBox(cbmodel);
		sgbox.setMaximumSize(new Dimension(sgbox.getMaximumSize().width, 20));

		pnl_Sg.add(sgbox);

		JButton sg = new JButton("Studiengang ausw�hlen");
		sg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!lm.contains(sgbox.getSelectedItem().toString()))
					lm.addElement(sgbox.getSelectedItem().toString());
			}
		});
		pnl_Sg.add(sg);

		JButton nsg = new JButton("Neuer Studiengang");
		nsg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					String name = JOptionPane.showInputDialog(frame,
							"Name des neuen Studiengangs:",
							"neuer Studiengang", JOptionPane.PLAIN_MESSAGE);

					while (name.isEmpty()) {
						name = JOptionPane
								.showInputDialog(
										frame,
										"Bitte g�ltigen Namen des neuen Studiengangs eingeben:",
										"neuer Studiengang",
										JOptionPane.PLAIN_MESSAGE);
					}

					ArrayList<Studiengang> sgs = database.getStudiengaenge();
					boolean neu = true;
					for (int i = 0; i < sgs.size(); i++) {
						if (sgs.get(i).equals(name)) {
							neu = false;
							break;
						}
					}
					if (neu) {
						database.setStudiengang(name);
						cbmodel.removeAllElements();
						sgs = database.getStudiengaenge();
						for (int i = 0; i < sgs.size(); i++)
							cbmodel.addElement(sgs.get(i));

						// cbmodel.addElement(name);
					} else {
						JOptionPane.showMessageDialog(frame,
								"Studiengang ist schon vorhanden", "Fehler",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (NullPointerException np) {

				}
			}

		});
		pnl_Sg.add(nsg);

		pnl_Sg.setLayout(new BoxLayout(pnl_Sg, BoxLayout.X_AXIS));
		panel.add(pnl_Sg);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel Jahrgang + Platzhalter
		panel.add(defaultmodulPanel("Jahrgang", Integer.toString(m.getJahrgang())));
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		// Panel Name + Platzhalter
		panel.add(defaultmodulPanel("Name", m.getName()));
		panel.add(Box.createRigidArea(new Dimension(0, 5)));

		ArrayList<String> l = m.getLabels();
		ArrayList<String> v = m.getValues();
		ArrayList<Boolean> d = m.getDezernat();

		for (int i = 0; i < l.size(); i++) {
			panel.add(defaultmodulPanel(l.get(i), v.get(i)));
			panel.add(Box.createRigidArea(new Dimension(0, 5)));
		}
		JButton btnOk = new JButton("Annehmen");
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String Name = ((JTextArea) ((JPanel) panel.getComponent(4))
						.getComponent(1)).getText();
				String jg = ((JTextArea) ((JPanel) panel.getComponent(2))
						.getComponent(1)).getText();
				int Jahrgang = Integer.parseInt(jg);

				ArrayList<String> labels = new ArrayList<String>();
				ArrayList<String> values = new ArrayList<String>();
				ArrayList<Boolean> dez = new ArrayList<Boolean>();
ArrayList<Zuordnung> zlist = new ArrayList<Zuordnung>();
				
				// Eintraege der Reihe nach auslesen
				for (int i = 6; i < panel.getComponentCount(); i = i + 2) {
					JPanel tmp = (JPanel) panel.getComponent(i);
					JLabel tmplbl = (JLabel) tmp.getComponent(0);
					JTextArea tmptxt = (JTextArea) tmp.getComponent(1);
					boolean dezernat2 = false;
					if (tmp.getComponentCount() > 3) {
						dezernat2 = ((JCheckBox) tmp.getComponent(2))
								.isSelected();
					}
					String value = tmptxt.getText();
					String label = tmplbl.getText();
					labels.add(label);
					values.add(value);
					dez.add(dezernat2);
				}
				int version = database.getModulVersion(Name) + 1;

				ArrayList<Studiengang> Studiengang = new ArrayList<Studiengang>();

				for (int i = 0; i < lm.getSize(); i++) {
//					zlist.add(lm.getElementAt(i));
				}
				Modul neu = new Modul(Name, zlist,
						Jahrgang, labels, values, version, dez);
				database.setModul(neu);
				panel.removeAll();
				panel.revalidate();
				newmodulecard();
				showCard("newmodule");
			}
		});
		pnl_bottom.add(btnOk);

		pnl_newmod.add(scrollPane);
		return pnl_newmod;

	}

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

		studmodel = new DefaultTableModel(new Object[][] {},
				new String[] { "Studiengang" }) {
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

		goforit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int openrow = studtable.getSelectedRow();
				String zwsstring = (String) studtable.getValueAt(openrow, 0);
				uebergabeString = zwsstring;
				modtypshowCard();
				showCard("modtyp show");
			}
		});

	}


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

		modtypmodel = new DefaultTableModel(new Object[][] {},
				new String[] { "Modul Typ" }) {
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
		System.out.println(typen.size());
		for (int i = 0; i < typen.size(); i++) {
			System.out.println(typen.get(i).getName());
			addToTable(typen.get(i).getName());

		}
		goforit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int openrow = modtyptable.getSelectedRow();
				String zwsstring = (String) modtyptable.getValueAt(openrow, 0);
				uebergabeString = zwsstring;
				modshowCard();
				showCard("mod show");
			}
		});
	}

	private void modshowCard() {
		JPanel modshow = new JPanel();
		cards.add(modshow, "mod show");

		// TODO rest der modelshowcard
	}

	public static void noConnection() {
		JOptionPane.showMessageDialog(frame, "Keine Verbindung zum Server!",
				"Connection error", JOptionPane.ERROR_MESSAGE);
	}

	private void addToTable(String modtyp) {
		modbuchmodel.addRow(new Object[] { modtyp });
	}

}