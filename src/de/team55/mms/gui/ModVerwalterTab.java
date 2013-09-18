package de.team55.mms.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import de.team55.mms.data.Studiengang;
import de.team55.mms.data.User;
import de.team55.mms.function.ServerConnection;

public class ModVerwalterTab extends JPanel {
	private static JFrame frame;
	private ServerConnection serverConnection = null;
	private static JPanel main = new JPanel();
	private static JPanel content = new JPanel();
	private static JPanel btn_panel = new JPanel();
	private ArrayList<String> module = null;
	private ArrayList<User> verwalter = null;
	private ArrayList<User> all = null;
	private DefaultListModel<String> mm = new DefaultListModel<String>();
	private DefaultListModel<User> vm = new DefaultListModel<User>();
	private DefaultListModel<User> am = new DefaultListModel<User>();
	private JList<String> modulelist = new JList<String>(mm);;
	private JList<User> verwalterlist = new JList<User>(vm);;
	private JList<User> alllist = new JList<User>(am);;
	private String modul = "";

	
	public ModVerwalterTab() {
		super();
		this.setLayout(new BorderLayout(0, 0));
		add(main, BorderLayout.CENTER);
		main.setLayout(new BorderLayout(0, 0));
		content.setLayout(new BorderLayout(0, 0));
		btn_panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		main.add(content, BorderLayout.CENTER);
		main.add(btn_panel, BorderLayout.SOUTH);


	}
	
	public void fillLists(ArrayList<String> module){
		this.setModule(module);
		mm.removeAllElements();
		vm.removeAllElements();
		am.removeAllElements();
		for(int i = 0; i < module.size(); i++ ){
			mm.addElement(module.get(i));
		}
		JScrollPane modscp = new JScrollPane();
		modscp.add(modulelist);
		content.add(modscp, BorderLayout.WEST);
		
		JButton laden = new JButton("Laden");
		JButton add = new JButton("<= Hinzufügen");
		JButton remove = new JButton("Entfernen =>");
		JButton save = new JButton("Speichern");
		btn_panel.add(laden);
		btn_panel.add(add);
		btn_panel.add(remove);
		btn_panel.add(save);
		
		
		laden.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				modul = modulelist.getSelectedValue();
				vm.removeAllElements();
				am.removeAllElements();
				ArrayList<ArrayList<User>> bothuser = serverConnection.getModulverwalter(modul);
				verwalter = new ArrayList<User>(bothuser.get(1));
				all = new ArrayList<User>(bothuser.get(0));
				
				for(int i = 0; i < verwalter.size(); i++ ){
					vm.addElement(verwalter.get(i));
				}
				for(int i = 0; i < all.size(); i++ ){
					am.addElement(all.get(i));
				}
				
			}
		});
		
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				User zws = alllist.getSelectedValue();
				am.removeElement(zws);
				all.remove(zws);
				vm.addElement(zws);
				verwalter.remove(zws);
			}
		});
		
		remove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				User zws = alllist.getSelectedValue();
				vm.removeElement(zws);
				verwalter.remove(zws);
				am.addElement(zws);
				all.add(zws);
			}
		});
		
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				serverConnection.setStellvertreter(verwalter, modul);
			}
		});
		
	}
	
	public ArrayList<String> getModule() {
		return module;
	}

	public void setModule(ArrayList<String> module) {
		this.module = module;
	}

	public ArrayList<User> getVerwalter() {
		return verwalter;
	}

	public void setVerwalter(ArrayList<User> verwalter) {
		this.verwalter = verwalter;
	}

	public ArrayList<User> getAll() {
		return all;
	}

	public void setAll(ArrayList<User> all) {
		this.all = all;
	}

	public ServerConnection getServerConnection() {
		return serverConnection;
	}

	public void setServerConnection(ServerConnection serverConnection) {
		this.serverConnection = serverConnection;
	}

}
