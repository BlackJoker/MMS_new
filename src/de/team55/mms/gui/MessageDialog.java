package de.team55.mms.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import de.team55.mms.data.Nachricht;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MessageDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private int result;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			MessageDialog dialog = new MessageDialog();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public MessageDialog(Nachricht n) {
		this.setName(n.toString());
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel topPanel = new JPanel();
			contentPanel.add(topPanel, BorderLayout.NORTH);
			topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
			{
				JPanel panel = new JPanel();
				FlowLayout flowLayout = (FlowLayout) panel.getLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				topPanel.add(panel);
				{
					JLabel lblVon = new JLabel("Absender: "+n.getAbsender());
					panel.add(lblVon);
				}
			}
			{
				JPanel panel2 = new JPanel();
				FlowLayout flowLayout = (FlowLayout) panel2.getLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				topPanel.add(panel2);
				{
					JLabel lblBetreff = new JLabel("Betreff: "+n.getBetreff());
					panel2.add(lblBetreff);
				}
			}
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				JTextPane textPane = new JTextPane();
				textPane.setText(n.getNachricht());
				textPane.setEditable(false);
				scrollPane.setViewportView(textPane);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Schlie\u00DFen");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						result=1;
						setVisible(false);
						dispose();
					}
				});
//				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
	
	public int getValue(){
	    return result;
	}

}
