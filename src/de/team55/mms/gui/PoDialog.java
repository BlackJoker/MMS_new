package de.team55.mms.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import de.team55.mms.data.Studiengang;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PoDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtJahr;
	private int status;
	private DefaultComboBoxModel stud= new DefaultComboBoxModel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PoDialog dialog = new PoDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PoDialog() {
		setModal(true);
		setTitle("Neue Pr\u00FCfungsordnung");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel left = new JPanel();
			contentPanel.add(left, BorderLayout.WEST);
			left.setLayout(new GridLayout(0, 1, 0, 0));
			{
				JLabel lblJahr = new JLabel("Jahr: ");
				left.add(lblJahr);
			}
			{
				JLabel lblStudiengang = new JLabel("Studiengang: ");
				left.add(lblStudiengang);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			panel.setLayout(new GridLayout(2, 0, 0, 0));
			{
				txtJahr = new JTextField();
				panel.add(txtJahr);
				txtJahr.setColumns(10);
			}
			{
				JComboBox comboBox = new JComboBox(stud);
				panel.add(comboBox);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
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
					public void actionPerformed(ActionEvent e) {
						status=0;
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public int showDialog(JFrame frame, ArrayList<Studiengang> slist){
		stud.removeAllElements();
		for(int i = 0;i<slist.size();i++){
			stud.addElement(slist.get(i));
		}
		setLocationRelativeTo(frame);
		pack();
		setVisible(true);
		return status;
	}

	public String getJahr() {
		// TODO Auto-generated method stub
		return txtJahr.getText();
	}
	
	public Studiengang getStudiengang(){
		return (Studiengang) stud.getSelectedItem();
	}

}
