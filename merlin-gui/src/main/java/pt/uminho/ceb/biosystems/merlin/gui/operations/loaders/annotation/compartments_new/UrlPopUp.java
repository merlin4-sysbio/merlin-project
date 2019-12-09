package pt.uminho.ceb.biosystems.merlin.gui.operations.loaders.annotation.compartments_new;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class UrlPopUp extends javax.swing.JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6479317489417299098L;
	private Object url;
	
	public UrlPopUp() {


		super(Workbench.getInstance().getMainFrame());
		this.url=null;
		initGUI();
		Utilities.centerOnOwner(this);
		this.setIconImage((new ImageIcon(getClass().getClassLoader().getResource("icons/merlin.png"))).getImage());
		this.setVisible(true);		
		this.setAlwaysOnTop(true);
		this.toFront();
	}

	public void initGUI() {



		this.setModal(true);
		JPanel jPanel1;
		{
			this.setTitle("predictions url");
			jPanel1 = new JPanel();
			getContentPane().add(jPanel1, BorderLayout.CENTER);
			GridBagLayout jPanel1Layout = new GridBagLayout();
			jPanel1Layout.columnWeights = new double[] {0.0, 0.1, 0.0};
			jPanel1Layout.columnWidths = new int[] {7, 7, 7};
			jPanel1Layout.rowWeights = new double[] {0.1, 0.0, 0.1};
			jPanel1Layout.rowHeights = new int[] {7, 7, 7};
			jPanel1.setLayout(jPanel1Layout);
		}
		
		JPanel jPanel11;
		{
			jPanel11 = new JPanel();
			GridBagLayout jPanel11Layout = new GridBagLayout();
			jPanel11.setLayout(jPanel11Layout);
			jPanel11Layout.columnWeights = new double[] {0.0, 0.1, 0.0};
			jPanel11Layout.columnWidths = new int[] {7, 7, 7};
			jPanel11Layout.rowWeights = new double[] {0.0, 0.1, 0.0};
			jPanel11Layout.rowHeights = new int[] {7, 7, 7};

			jPanel1.add(jPanel11, new GridBagConstraints(0, 0, 3, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}

		JLabel label1 = new JLabel("Introduce the predictions link");
		
		jPanel11.add(label1, new GridBagConstraints(1, 0, 1, 2, 0.0, 5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			
		JTextField jTextField1 = new JTextField();
		jTextField1.setEditable(true);
		jPanel11.add(jTextField1, new GridBagConstraints(1, 2, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		
		JButton buttonProceed = new JButton("proceed");
		jPanel11.add(buttonProceed, new GridBagConstraints(1, 5, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0), 0, 0));
		buttonProceed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setLink(jTextField1.getText());
				finish();
			}
		});
		
		this.setSize(400, 120);
}
	private void setLink(String file) {
		this.url=file;
	}
	
	public String getLink() {
		return (String) this.url;
	}

	public void finish() {

		this.setVisible(false);
		this.dispose();
	}
	}
