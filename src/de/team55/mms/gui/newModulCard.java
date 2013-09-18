package de.team55.mms.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import de.team55.mms.data.Fach;
import de.team55.mms.data.Feld;
import de.team55.mms.data.Modul;
import de.team55.mms.data.Modulhandbuch;
import de.team55.mms.data.Nachricht;
import de.team55.mms.data.User;
import de.team55.mms.function.ServerConnection;

import javax.swing.JComboBox;

public class newModulCard {

	private static JFrame frame;
	private static JPanel pnl_newmod = new JPanel();
	private static JPanel modul_panel = new JPanel();
	private static ArrayList<String> labels = new ArrayList<String>();
	private HashMap<JButton, Integer> buttonmap = new HashMap<JButton, Integer>();
	private final Dimension preferredSize = new Dimension(120, 20);
	private DefaultComboBoxModel<Fach> cbModelF = new DefaultComboBoxModel<Fach>();

	public newModulCard(ArrayList<Feld> defaultFelder, ArrayList<Modulhandbuch> mbs, final ServerConnection serverConnection,
			final User current) {

		// Alle vorhandenen Felder entfernen
		modul_panel.removeAll();

		// Liste dynamischer Buttons leeren
		if (!buttonmap.isEmpty()) {
			for (int i = 0; i < buttonmap.size(); i++)
				buttonmap.remove(i);
		}

		pnl_newmod.setLayout(new BorderLayout(0, 0));

		JPanel pnl_bottom = new JPanel();
		pnl_newmod.add(pnl_bottom, BorderLayout.SOUTH);

		// Button zum erstellen eines neuen Feldes
		JButton btnNeuesFeld = new JButton("Neues Feld");
		btnNeuesFeld.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String text = "Name des Feldes";
				// Abfrage des Namen des Feldes
				String name = JOptionPane.showInputDialog(frame, text);
				try {
					// Prüfe, ob Name leer oder schon vorhanden ist
					while (name.isEmpty() || labels.contains(name)) {
						Object[] params = { "Bitte geben Sie eine gültige Bezeichnung ein!", text };
						name = JOptionPane.showInputDialog(frame, params);
					}
					labels.add(name);
					JPanel pnl_tmp = new JPanel();
					modul_panel.add(pnl_tmp);
					// Platzhalter
					modul_panel.add(Box.createRigidArea(new Dimension(0, 5)));

					// Abfrage der Anzahl an Panels, die bereits vorhanden sind
					int numOfPanels = modul_panel.getComponentCount();
					pnl_tmp.setLayout(new BoxLayout(pnl_tmp, BoxLayout.X_AXIS));

					JLabel label_tmp = new JLabel(name);
					label_tmp.setPreferredSize(preferredSize);
					pnl_tmp.add(label_tmp);

					JTextArea txt_tmp = new JTextArea();
					txt_tmp.setLineWrap(true);
					pnl_tmp.add(txt_tmp);

					// Button, um das Feld wieder zu entfernen
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

							// ids der Buttons ändern, damit auch ein Feld aus
							// der Mitte gelöcht werden kann
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
					// hinzufügen
					buttonmap.put(btn_tmp_entf, numOfPanels - 2);

					pnl_tmp.add(btn_tmp_entf);

					modul_panel.revalidate();

				} catch (NullPointerException npe) {
					// nichts tuen bei Abbruch
				}
			}
		});
		pnl_bottom.add(btnNeuesFeld);

		JScrollPane scrollPane = new JScrollPane(modul_panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		modul_panel.setLayout(new BoxLayout(modul_panel, BoxLayout.Y_AXIS));

		JPanel pnl_modbuch = new JPanel();
		modul_panel.add(pnl_modbuch);
		modul_panel.add(Box.createRigidArea(new Dimension(0, 5)));
		pnl_modbuch.setLayout(new BoxLayout(pnl_modbuch, BoxLayout.X_AXIS));

		JLabel lblModulhandbuch = new JLabel("Modulhandbuch: ");
		lblModulhandbuch.setPreferredSize(preferredSize);
		pnl_modbuch.add(lblModulhandbuch);

		final DefaultComboBoxModel<Modulhandbuch> cbModelMb = new DefaultComboBoxModel<Modulhandbuch>();
		for (int i = 0; i < mbs.size(); i++) {
			cbModelMb.addElement(mbs.get(i));
		}

		final JComboBox<Modulhandbuch> cb_modbuch = new JComboBox<Modulhandbuch>(cbModelMb);
		cb_modbuch.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				cbModelF.removeAllElements();
				Modulhandbuch m = (Modulhandbuch) cb_modbuch.getSelectedItem();
				ArrayList<Fach> fs = m.getFach();
				for (int i = 0; i < fs.size(); i++) {
					cbModelF.addElement(fs.get(i));
				}

			}
		});
		cb_modbuch.setPreferredSize(preferredSize);
		pnl_modbuch.add(cb_modbuch);

		JPanel pnl_fach = new JPanel();
		modul_panel.add(pnl_fach);
		modul_panel.add(Box.createRigidArea(new Dimension(0, 5)));
		pnl_fach.setLayout(new BoxLayout(pnl_fach, BoxLayout.X_AXIS));

		JLabel lblFach = new JLabel("Fach:");
		lblFach.setPreferredSize(preferredSize);
		pnl_fach.add(lblFach);

		JComboBox<Fach> cb_fach = new JComboBox<Fach>(cbModelF);
		cb_fach.setPreferredSize(preferredSize);
		pnl_fach.add(cb_fach);

		// Panel Zuordnung + Platzhalter
		JPanel pnl_Z = new JPanel();
		pnl_Z.setLayout(new BoxLayout(pnl_Z, BoxLayout.X_AXIS));
		JLabel label_MH = new JLabel("Zuordnung");

		label_MH.setPreferredSize(preferredSize);
		pnl_Z.add(label_MH);

		// Button zum Annehmen eines Modules
		JButton btnOk = new JButton("Einreichen");
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ArrayList<Zuordnung> zlist = new ArrayList<Zuordnung>();

				boolean filled = true;
				Modulhandbuch mb = (Modulhandbuch) cbModelMb.getSelectedItem();
				Fach f = (Fach) cbModelF.getSelectedItem();
				ArrayList<Feld> felder = new ArrayList<Feld>();
				String name = "";
				// Eintraege der Reihe nach auslesen
				for (int i = 4; i < modul_panel.getComponentCount(); i = i + 2) {
					JPanel tmp = (JPanel) modul_panel.getComponent(i);
					JLabel tmplbl = (JLabel) tmp.getComponent(0);
					JTextArea tmptxt = (JTextArea) tmp.getComponent(1);
					boolean dezernat2 = false;
					try {
						dezernat2 = ((JCheckBox) tmp.getComponent(2)).isSelected();
					} catch (ArrayIndexOutOfBoundsException ex) {
						dezernat2 = false;
					}catch(ClassCastException et){
						dezernat2 = false; 
					}
					String value = tmptxt.getText();
					String label = tmplbl.getText();
					// Prüfe, ob alle Felder ausgefüllt wurden
					if (value.isEmpty()) {
						filled = false;
						break;
					} else if (label.equals("Name") || label.equals("Name:")) {
						name = value;
					} else {
						felder.add(new Feld(label, value, dezernat2));
					}
				}
				// Wenn alle aussgefüllt wurden, neues Modul
				// erzeugen und bei Bestätigung einreichen
				if (filled == true) {
					System.out.println(name);
					int version = serverConnection.getModulVersion(name) + 1;

					Date d = new Date();
					Modul neu = new Modul(name, felder, version, d, 1, false, new ArrayList<String>(), "");
					int n = JOptionPane.showConfirmDialog(frame, "Sind Sie sicher, dass Sie dieses Modul einreichen wollen?",
							"Bestätigung", JOptionPane.YES_NO_OPTION);
					if (n == 0) {
						int x = serverConnection.setModul(neu, f.getName(), mb.getId()).getStatus();
						if (x == 201) {
							x = serverConnection.setModulVerwalter(current, name).getStatus();
							Nachricht na = new Nachricht();
							na.setAbsenderID(current.getId());
							na.setBetreff("Es wurde ein neues Modul eingereicht");
							String uname = current.getTitel()+" "+current.getVorname()+" "+current.getNachname();
							uname = uname.trim();
							na.setNachricht(uname+" hat ein neues Modul eingereicht.");
							na.setDatum(new Date());
							na.setEmpfaengerID(-1);
							x = serverConnection.setNachricht(na).getStatus();
						}
					}
				} // Fehler, wenn nicht alle ausgefüllt wurden
				else {
					JOptionPane.showMessageDialog(frame, "Bitte füllen Sie alle Felder aus!", "Eingabe Fehler", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		pnl_bottom.add(btnOk);

		pnl_newmod.add(scrollPane);

		// Liste mit bereits vorhandenen Felder erstellen und mit den
		// Standartfeldern füllen
		for (int i = 0; i < defaultFelder.size(); i++) {
			modul_panel.add(defaultmodulPanel(defaultFelder.get(i)));
			modul_panel.add(Box.createRigidArea(new Dimension(0, 5)));
		}

		btnOk.setToolTipText("Klicken, um ihr Modul einzureichen.");
		btnNeuesFeld.setToolTipText("Klicken, um ein neues Feld in ihrem Modul zu erstellen.");

	}

	/**
	 * Liefert ein Feld mit Label, TextArea und Checkbox
	 * 
	 * @return JPanel ausgefülltes Panel
	 * @param name
	 *            Beschriftung des Labels
	 * @param string
	 *            Inhalt der TextArea
	 * @param b
	 *            Gibt an, ob die Checkbox ausgewählt ist
	 */
	private JPanel defaultmodulPanel(Feld f) {
		String name = f.getLabel();
		String value = f.getValue();
		boolean b = f.isDezernat();

		labels.add(name);

		JPanel pnl = new JPanel();
		pnl.setLayout(new BoxLayout(pnl, BoxLayout.X_AXIS));

		JLabel label = new JLabel(name);
		label.setPreferredSize(preferredSize);
		pnl.add(label);

		JTextArea txt = new JTextArea(value);
		txt.setLineWrap(true);
		pnl.add(txt);

		if (b) {
			JCheckBox dez = new JCheckBox("Dezernat 2", b);
			dez.setEnabled(false);
			pnl.add(dez);
		}
		return pnl;
	}

	public JPanel getPanel() {
		return pnl_newmod;
	}
}
