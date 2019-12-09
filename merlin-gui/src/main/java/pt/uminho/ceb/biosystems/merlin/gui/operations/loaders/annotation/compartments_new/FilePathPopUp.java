package pt.uminho.ceb.biosystems.merlin.gui.operations.loaders.annotation.compartments_new;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class FilePathPopUp extends javax.swing.JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7134391317104039643L;
	private File selectedFile;
	private Icon folderImage;

	public FilePathPopUp() {


		super(Workbench.getInstance().getMainFrame());
		this.selectedFile = null;
		initGUI();
		Utilities.centerOnOwner(this);
		this.setIconImage((new ImageIcon(getClass().getClassLoader().getResource("icons/merlin.png"))).getImage());
		this.folderImage = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/folder.png")).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
		this.setVisible(true);		
		this.setAlwaysOnTop(true);
		this.toFront();
	}

	public void initGUI() {



		this.setModal(true);
		JPanel jPanel1;
		{
			this.setTitle("predictions file");
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

		JLabel label1 = new JLabel("Introduce the predictions file for PSortb3 prediction");
		
		jPanel11.add(label1, new GridBagConstraints(1, 0, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			
		JTextField jTextField1 = new JTextField();
		jTextField1.setEditable(false);
		jPanel11.add(jTextField1, new GridBagConstraints(1, 2, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));


		JButton button1 = new JButton(this.folderImage);
		jPanel11.add(button1, new GridBagConstraints(2, 2, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));



		button1.addActionListener(new ActionListener() {


			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser filePath = new JFileChooser();

				filePath.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

					}
				});


				int status = filePath.showOpenDialog(null);

				if (status == JFileChooser.APPROVE_OPTION) {
					setFile(filePath.getSelectedFile());
					jTextField1.setText(filePath.getSelectedFile().toString());

				} 
				else if (status == JFileChooser.CANCEL_OPTION) {
					setFile(null);			
				}

			}});
		
		JButton buttonProceed = new JButton("proceed");
		jPanel11.add(buttonProceed, new GridBagConstraints(1, 5, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0, 0, 0, 0), 0, 0));
		buttonProceed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				finish();
			}
		});
		
		
		this.setSize(400, 120);
	}

	private void setFile(File file) {
		this.selectedFile=file;
	}
	
	public File getFile() {
		return selectedFile;
	}

	public void finish() {

		this.setVisible(false);
		this.dispose();
	}
}
