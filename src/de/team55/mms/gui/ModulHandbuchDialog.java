package de.team55.mms.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.DefaultComboBoxModel;

import de.team55.mms.data.Studiengang;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class ModulHandbuchDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtJahr;
	private int status;
	private JComboBox cbSem;
	private JTextArea txtrProsa;
	private JComboBox cbPO;
	private JComboBox cbSg;
	private DefaultComboBoxModel stud= new DefaultComboBoxModel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ModulHandbuchDialog dialog = new ModulHandbuchDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ModulHandbuchDialog() {
		setModal(true);
		setTitle("Modulhandbuch anlegen");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel prosa = new JPanel();
			contentPanel.add(prosa, BorderLayout.SOUTH);
			prosa.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblProsa = new JLabel("Prosa: ");
				prosa.add(lblProsa, BorderLayout.NORTH);
			}
			{
				txtrProsa = new JTextArea();
				prosa.add(txtrProsa, BorderLayout.CENTER);
				txtrProsa.setFont(new Font("Tahoma", Font.PLAIN, 11));
				txtrProsa.setRows(5);
			}
		}
		{
			JPanel left = new JPanel();
			contentPanel.add(left, BorderLayout.WEST);
			left.setLayout(new GridLayout(0, 1, 0, 0));
			{
				JLabel lblNewLabel_1 = new JLabel("Studiengang:");
				left.add(lblNewLabel_1);
			}
			{
				JLabel lblPrfungsordnung = new JLabel("Pr\u00FCfungsordnung: ");
				left.add(lblPrfungsordnung);
			}
			{
				JLabel lblSemester = new JLabel("Semester: ");
				left.add(lblSemester);
			}
			{
				JLabel lblJahr = new JLabel("Jahr: ");
				left.add(lblJahr);
			}
		}
		{
			JPanel right = new JPanel();
			contentPanel.add(right, BorderLayout.CENTER);
			right.setLayout(new GridLayout(0, 1, 0, 0));
			{
				cbSg = new JComboBox(stud);
				right.add(cbSg);
			}
			{
				cbPO = new JComboBox();
				cbPO.setModel(new DefaultComboBoxModel(new String[] {"2013"}));
				right.add(cbPO);
			}
			{
				cbSem = new JComboBox();
				right.add(cbSem);
				cbSem.setModel(new DefaultComboBoxModel(new String[] {"SS", "WS"}));
			}
			{
				txtJahr = new JTextField();
				right.add(txtJahr);
				txtJahr.setColumns(10);
			}
		}
		{
			JLabel lblNewLabel = new JLabel("New label");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						status=1;
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Abbrechen");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						status=0;
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		pack();
	}

	public int showDialog(JFrame frame, ArrayList<Studiengang> studienlist) {
		for(int i = 0;i<studienlist.size();i++){
			stud.addElement(studienlist.get(i));
		}
		setLocationRelativeTo(frame);
		setVisible(true);
		return status;
	}

	public String getJahr() {
		return txtJahr.getText();
	}

	public String getProsa() {
		// TODO Auto-generated method stub
		return txtrProsa.getText();
	}

	public String getSemester() {
		// TODO Auto-generated method stub
		return (String) cbSem.getSelectedItem();
	}

	public String getPO() {
		// TODO Auto-generated method stub
		return (String) cbPO.getSelectedItem();
	}

	public Studiengang getStudiengang() {
		// TODO Auto-generated method stub
		return (Studiengang) cbSg.getSelectedItem();
	}

}
