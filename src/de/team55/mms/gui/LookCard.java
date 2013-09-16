package de.team55.mms.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;






import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;


import javax.swing.tree.DefaultTreeCellRenderer;

import de.team55.mms.data.Studiengang;
import de.team55.mms.function.ServerConnection;

public class LookCard extends JPanel{
	private ServerConnection serverConnection = null;
	private static JPanel looking = new JPanel();
	private JTree tree;
	private ArrayList<Studiengang> studienlist = null;
	
	public LookCard(){
		super();
		this.setLayout(new BorderLayout(0, 0));
		add(looking, BorderLayout.CENTER);
		looking.setLayout(new BorderLayout(0, 0));

		
	    
		
	}
	
	public void buildTree(){
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Universität Ulm");
	    DefaultMutableTreeNode child;
	    DefaultMutableTreeNode grandChild;
	    DefaultMutableTreeNode t3Child;
	    DefaultMutableTreeNode t4Child;
	    
	    for(int i = 0; i < studienlist.size(); i++) {
	      child = new DefaultMutableTreeNode(studienlist.get(i).getName());
	      root.add(child);
	      for(int j = 0; j < studienlist.get(i).getModbuch().size(); j++) {
	        grandChild =
	          new DefaultMutableTreeNode("Modulhandbuch "+studienlist.get(i).getModbuch().get(j).getJahrgang()+" PO "+studienlist.get(i).getModbuch().get(j).getPruefungsordnungsjahr());
	        child.add(grandChild);
	        for(int k = 0; k < studienlist.get(i).getModbuch().get(j).getFach().size(); k++) {
	        	t3Child =
		          new DefaultMutableTreeNode(studienlist.get(i).getModbuch().get(j).getFach().get(k).getName());
	        	grandChild.add(t3Child);
	        	for(int l = 0; l < studienlist.get(i).getModbuch().get(j).getFach().get(k).getModlist().size(); l++) {
		        	t4Child =
			          new DefaultMutableTreeNode(studienlist.get(i).getModbuch().get(j).getFach().get(k).getModlist().get(l).getName());
			        t3Child.add(t4Child);
			        
			    }
		    }
	      }
	    }
	    
	    tree = new JTree(root);
	    DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {
            {
                setLeafIcon(new ImageIcon("leafIcon.jpg"));
                setOpenIcon(new ImageIcon("openIcon.jpg"));
                setClosedIcon(new ImageIcon("closeIcon.jpg"));
            }
        };
 
        BasicTreeUI ui = (BasicTreeUI) tree.getUI();
        ui.setCollapsedIcon(new ImageIcon("collapsedIcon.jpg"));
        ui.setExpandedIcon(new ImageIcon("expandedIcon.jpg"));
 
        tree.setCellRenderer(renderer);
	    looking.add(tree);
	}
	
	
	public void setConnection(ServerConnection serverConnection) {
		this.serverConnection = serverConnection;
	}
	
	public void setStudienlist(ArrayList<Studiengang> studienlist){
		this.studienlist = studienlist;
	}

}
