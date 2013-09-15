package de.team55.mms.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import de.team55.mms.data.Nachricht;
import de.team55.mms.data.User;
import de.team55.mms.function.SendMail;
import de.team55.mms.function.ServerConnection;

public class HomeCard extends JPanel {

	private static JPanel welcome = new JPanel();
	private static JPanel pnl_content = new JPanel();
	private JTable tblmessages;
	private User user;
	private DefaultTableModel messagemodel;
	private ServerConnection serverConnection;
	private ArrayList<Nachricht> nachrichten = new ArrayList<Nachricht>();
	private ArrayList<User> neueUser;

	public HomeCard() {
		super();
		this.setLayout(new BorderLayout(0, 0));
		add(welcome, BorderLayout.CENTER);
		welcome.setLayout(new BorderLayout(0, 0));
		final ArrayList<String> dialogs = new ArrayList<String>();
		pnl_content.setLayout(new BoxLayout(pnl_content, BoxLayout.Y_AXIS));

		JPanel pnl_day = new JPanel();
		pnl_content.add(pnl_day);

		JLabel lblStichtag = new JLabel("Stichtag f\u00FCr das Einreichen von Modulen: 30.08.13");
		pnl_day.add(lblStichtag);
		lblStichtag.setHorizontalAlignment(SwingConstants.CENTER);
		lblStichtag.setAlignmentY(0.0f);
		lblStichtag.setForeground(Color.RED);
		lblStichtag.setFont(new Font("Tahoma", Font.BOLD, 14));

		JPanel pnl_messages = new JPanel();
		pnl_content.add(pnl_messages);
		pnl_messages.setLayout(new BoxLayout(pnl_messages, BoxLayout.Y_AXIS));

		JPanel pnl_mestop = new JPanel();
		pnl_messages.add(pnl_mestop);
		pnl_mestop.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel lblNachrichten = new JLabel("Nachrichten:");
		pnl_mestop.add(lblNachrichten);
		lblNachrichten.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNachrichten.setHorizontalAlignment(SwingConstants.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		pnl_messages.add(scrollPane);

		messagemodel = new DefaultTableModel(new Object[][] { { Boolean.FALSE, "", null, null }, }, new String[] { "", "Von", "Betreff",
				"Datum" }) {
			Class[] columnTypes = new Class[] { Boolean.class, String.class, String.class, String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[] { true, false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		refreshMessageTable();

		tblmessages = new JTable(messagemodel);
		scrollPane.setViewportView(tblmessages);
		tblmessages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblmessages.setFillsViewportHeight(true);
		tblmessages.setShowVerticalLines(false);

		tblmessages.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = tblmessages.getSelectedRow();
					final Nachricht n = nachrichten.get(row);
					nachrichten.remove(row);
					n.setGelesen(true);
					if (!dialogs.contains(n.toString())) {
						dialogs.add(n.toString());
						if(n.getBetreff().contains("hat sich angemeldet")){
							for(int i = 0; i<neueUser.size();i++){
								String u = neueUser.get(i).getId()+"";
								if(u.equals(n.getNachricht())){
									userdialog dlg = new userdialog(null, "User bestätigen", neueUser.get(i), true, serverConnection);
									int response = dlg.showCustomDialog();
									// Bei Bestätigung, neuen User freischalten und e-Mail
									// senden
									User tmp = dlg.getUser();
									if (response == 1) {
										tmp.setFreigeschaltet(true);
										if (SendMail.send(user.geteMail(), neueUser.get(i).geteMail(), "Sie wurden freigeschaltet!") == 1) {
											tmp.setFreigeschaltet(true);
											if (serverConnection.userupdate(tmp, tmp.geteMail()).getStatus() == 201) {
												neueUser.remove(i);
											}
										}
										dialogs.remove(n.toString());
										// Ansonsten möglichkeit ihn wieder zu löschen
									} else {
										int a = JOptionPane.showConfirmDialog(null, "Möchten Sie diesen Benutzer löschen", "Bestätigung",
												JOptionPane.YES_NO_OPTION);
										if (a == 0) {
											serverConnection.deluser(tmp.geteMail());
										}
										dialogs.remove(n.toString());
									}
									break;
								}						
							}
						} else {
							final MessageDialog dialog = new MessageDialog(n);
							dialog.addWindowListener(new WindowListener() {  
								  public void windowClosed(WindowEvent e) {  
								     if (e.getSource() == dialog) {  
											dialogs.remove(n.toString());
								     }  
								  }

								@Override
								public void windowActivated(WindowEvent arg0) {
									// TODO Auto-generated method stub
									
								}

								@Override
								public void windowClosing(WindowEvent arg0) {
									// TODO Auto-generated method stub
									
								}

								@Override
								public void windowDeactivated(WindowEvent arg0) {
									// TODO Auto-generated method stub
									
								}

								@Override
								public void windowDeiconified(WindowEvent arg0) {
									// TODO Auto-generated method stub
									
								}

								@Override
								public void windowIconified(WindowEvent arg0) {
									// TODO Auto-generated method stub
									
								}

								@Override
								public void windowOpened(WindowEvent arg0) {
									// TODO Auto-generated method stub
									
								}  
								   
								  // other window callbacks  
								});
							dialog.setVisible(true);
							
						}
					}
					nachrichten.add(n);
					refreshMessageTable();
				}
			}
		});

		JPanel pnl_mesbot = new JPanel();
		pnl_messages.add(pnl_mesbot);

		JButton btnAlsGelesenMarkieren = new JButton("Als gelesen markieren");
		btnAlsGelesenMarkieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (int i = 0; i < messagemodel.getRowCount(); i++) {
					if ((boolean) messagemodel.getValueAt(i, 0)) {
						nachrichten.get(i).setGelesen(true);
					}
				}
				refreshMessageTable();
			}
		});
		pnl_mesbot.add(btnAlsGelesenMarkieren);

		JButton btnAlsUngelesenMarkieren = new JButton("Als ungelesen markieren");
		btnAlsUngelesenMarkieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < messagemodel.getRowCount(); i++) {
					if ((boolean) messagemodel.getValueAt(i, 0)) {
						nachrichten.get(i).setGelesen(false);
					}
				}
				refreshMessageTable();
			}
		});
		pnl_mesbot.add(btnAlsUngelesenMarkieren);

		JButton btnLschen = new JButton("L\u00F6schen");
		btnLschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Nachricht> tmp = new ArrayList<Nachricht>();
				for (int i = 0; i < messagemodel.getRowCount(); i++) {
					if ((boolean) messagemodel.getValueAt(i, 0)) {
						tmp.add(nachrichten.get(i));
					}
				}
				nachrichten.removeAll(tmp);
				refreshMessageTable();
			}
		});
		pnl_mesbot.add(btnLschen);

		JPanel pnl_welc = new JPanel();
		welcome.add(pnl_welc, BorderLayout.NORTH);

		JLabel lblNewLabel = new JLabel("Willkommen beim Modul Management System");
		pnl_welc.add(lblNewLabel);
	}

	public void refreshMessageTable() {
		Collections.sort(nachrichten, new Comparator<Nachricht>() {
			public int compare(Nachricht n1, Nachricht n2) {
				return n1.getDatum().compareTo(n2.getDatum()) * -1;
			}
		});
		messagemodel.setRowCount(0);
		for (int i = 0; i < nachrichten.size(); i++) {
			addToTable(nachrichten.get(i));
		}
	}

	private void addToTable(Nachricht neu) {
		if (neu.isGelesen()) {
			messagemodel.addRow(new Object[] { false, neu.getAbsender(), neu.getBetreff(), neu.getDatumString() });
		} else {
			messagemodel.addRow(new Object[] { false, "<html><b>" + neu.getAbsender() + "</b></html>",
					"<html><b>" + neu.getBetreff() + "</b></html>", "<html><b>" + neu.getDatumString() + "</b></html>" });
		}
	}

	public void setMessageView(boolean b) {
		welcome.remove(pnl_content);
		if (b) {
			welcome.add(pnl_content, BorderLayout.CENTER);
		}
		welcome.repaint();
	}

	public void setUser(User u) {
		this.user = u;
	}

	public void refreshMessages() {
		neueUser = serverConnection.userload("false");
		for (int i = 0; i < neueUser.size(); i++) {
			User u = neueUser.get(i);
			String name = u.getTitel()+" "+u.getVorname()+" "+u.getNachname();
			name.trim();
			
			Nachricht n = new Nachricht();
			n.setAbsender(user.geteMail());
			n.setAbsenderID(u.getId());
			n.setEmpfaengerID(user.getId());
			n.setBetreff(name+" hat sich angemeldet");
			n.setNachricht(u.getId()+"");
			n.setGelesen(false);
			n.setDatum(new Date());
			nachrichten.add(n);
			serverConnection.setNachricht(n);
		}
		nachrichten = serverConnection.getNachrichten(user.geteMail());
		refreshMessageTable();
	}

	public void setConnection(ServerConnection serverConnection) {
		this.serverConnection = serverConnection;
	}

}
