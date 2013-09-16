package de.team55.mms.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import de.team55.mms.data.Fach;
import de.team55.mms.data.Feld;
import de.team55.mms.data.Modul;
import de.team55.mms.data.Modulhandbuch;
import de.team55.mms.data.Studiengang;
import de.team55.mms.function.ServerConnection;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

public class LookCard extends JPanel {
	private static JFrame frame;
	private ServerConnection serverConnection = null;
	private static JPanel looking = new JPanel();
	private static JPanel content = new JPanel();
	private static JPanel btn_panel = new JPanel();
	private JTree tree;
	private ArrayList<Studiengang> studienlist = null;
	private DefaultMutableTreeNode node;

	public LookCard() {
		super();
		this.setLayout(new BorderLayout(0, 0));
		add(looking, BorderLayout.CENTER);
		looking.setLayout(new BorderLayout(0, 0));
		content.setLayout(new BorderLayout(0, 0));
		btn_panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		looking.add(content, BorderLayout.CENTER);
		looking.add(btn_panel, BorderLayout.SOUTH);

	}

	public void buildTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Universität Ulm");
		DefaultMutableTreeNode child;
		DefaultMutableTreeNode grandChild;
		DefaultMutableTreeNode t3Child;
		DefaultMutableTreeNode t4Child;

		for (int i = 0; i < studienlist.size(); i++) {
			child = new DefaultMutableTreeNode(studienlist.get(i).getName());
			root.add(child);
			for (int j = 0; j < studienlist.get(i).getModbuch().size(); j++) {
				grandChild = new DefaultMutableTreeNode("Modulhandbuch " + studienlist.get(i).getModbuch().get(j).getJahrgang() + " PO "
						+ studienlist.get(i).getModbuch().get(j).getPruefungsordnungsjahr());
				child.add(grandChild);
				for (int k = 0; k < studienlist.get(i).getModbuch().get(j).getFach().size(); k++) {
					t3Child = new DefaultMutableTreeNode(studienlist.get(i).getModbuch().get(j).getFach().get(k).getName());
					grandChild.add(t3Child);
					for (int l = 0; l < studienlist.get(i).getModbuch().get(j).getFach().get(k).getModlist().size(); l++) {
						t4Child = new DefaultMutableTreeNode(studienlist.get(i).getModbuch().get(j).getFach().get(k).getModlist().get(l)
								.getName());
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
		tree.setCellRenderer(renderer);
		content.add(tree);
		
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TODO Auto-generated method stub
				node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();			
			}
		});
		
		JButton pdfbtn = new JButton("PDF ausgeben");
		btn_panel.add(pdfbtn);
		pdfbtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(node != null){
					System.out.println(node.toString());
					if(node.getParent() != null)
					System.out.println(node.getParent().toString());
					if((node.getParent().toString().equalsIgnoreCase("Universität Ulm") || node.getParent().getParent().toString().equalsIgnoreCase("Universität Ulm"))){
//						try {
//							if(node.getParent().getParent().toString().equalsIgnoreCase("Universität Ulm")){
//								MHBPDF(0, 0);
//							}else{
//								MHBPDF(0);
//							}
//						} catch (IOException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
					}else{
						JOptionPane.showMessageDialog(frame, "Bitte wählen sie entweder einen Studiengang oder ein Modulhandbuch aus.", "Input Error", JOptionPane.ERROR_MESSAGE);
						
					}
				}
			}
		});
		

	}

	public void setConnection(ServerConnection serverConnection) {
		this.serverConnection = serverConnection;
	}

	public void setStudienlist(ArrayList<Studiengang> studienlist) {
		this.studienlist = studienlist;
	}

	@SuppressWarnings("deprecation")
	public void MHBPDF(int stu, int mod) throws IOException {

		Chunk tab = new Chunk(new VerticalPositionMark(), 80); // Position
																// anpassen
		Chunk underline = new Chunk("Underline. ");
		underline.setUnderline(0.1f, -2f); // 0.1 thick, -2 y-location

		String studname = studienlist.get(stu).getName();
		String abschluss = studienlist.get(stu).getAbschluss();

		// for (int i=0; i<Stud.modbuch.size();i++){ //evtl keine schleife
		// sondern index des benötigten MHB angeben/übergeben

		Modulhandbuch ModHB = studienlist.get(stu).getModbuch().get(mod);

		String prosa = ModHB.getProsa();
		String jahrgang = ModHB.getJahrgang();
		int pruefjahr = ModHB.getPruefungsordnungsjahr();

		String pdfname = abschluss + "-" + studname + "-PO" + pruefjahr + "-" + jahrgang;

		Document document = new Document();
		try {
			// Writer Instanz erstellen
			PdfWriter.getInstance(document, new FileOutputStream(pdfname + ".pdf"));
			// step 3: Dokument öffnen
			document.open();
			// step 4: Absatz mit Text dem Dokument hinzufügen
			document.add(new Paragraph("Modulhandbuch: " + abschluss + "" + studname + " FSPO " + pruefjahr + "" + jahrgang));
			document.add(underline);
			document.add(new Paragraph(prosa));
	
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		for (int j = 0; j < ModHB.getFach().size(); j++) {

			Fach Fach = ModHB.getFach().get(j);

			String fachname = Fach.getName();

			try {
				// step 3: Dokument öffnen
				
				// step 4: Absatz mit Text dem Dokument hinzufügen
				document.add(new Chunk((j + 1) + ". " + fachname).setLocalDestination((j + 1) + ". " + fachname));
				document.add(Chunk.NEWLINE); // leerzeile
				
			} catch (DocumentException de) {
				System.err.println(de.getMessage());
			}

			for (int k = 0; k < Fach.getModlist().size(); k++) {
				Modul Dul = Fach.getModlist().get(k);

				String Modname = Dul.getName();

				try {
					// step 3: Dokument öffnen
				
					// step 4: Absatz mit Text dem Dokument hinzufügen
					document.add(new Paragraph(new Chunk((j + 1) + "." + (k + 1) + ". " + Modname).setLocalDestination((j + 1) + "."
							+ (k + 1) + ". " + Modname)));
				
				} catch (DocumentException de) {
					System.err.println(de.getMessage());
				}

				for (int l = 0; l < Dul.getFelder().size(); l++) {

					Feld Felder = Dul.getFelder().get(l);

					String label = Felder.getLabel();
					String value = Felder.getValue();

					try {
						// step 3: Dokument öffnen
					
						// step 4: Absatz mit Text dem Dokument hinzufügen

						document.add(new Chunk(String.format(label + ": ")));
						document.add(new Chunk(tab));
						document.add(new Chunk(value));

					
					} catch (DocumentException de) {
						System.err.println(de.getMessage());
					}

				}// 4

				document.newPage();
			}// 3 fs

			document.newPage();
		}// f-shleif 2
			// }//erste for-schleife

		document.close();
	}

}
