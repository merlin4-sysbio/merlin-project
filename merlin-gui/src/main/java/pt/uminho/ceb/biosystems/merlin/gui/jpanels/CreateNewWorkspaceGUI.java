package pt.uminho.ceb.biosystems.merlin.gui.jpanels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.NewWorkspaceRequirements;
import pt.uminho.ceb.biosystems.merlin.services.DatabaseServices;

public class CreateNewWorkspaceGUI extends javax.swing.JDialog{
	private static final long serialVersionUID = -1L;
	private JPanel jPanel1, jPanel11;
	private JTextField jTextField1;
	private List<String> existingDatabases;

	/**
	 * @param dataTable
	 * @param windowName
	 * @param name
	 * @param reaction
	 */
	public CreateNewWorkspaceGUI(List<String> existingDatabases) {

		super(Workbench.getInstance().getMainFrame());
		this.existingDatabases = existingDatabases;
		this.initGUI();
		Utilities.centerOnOwner(this);
		this.setVisible(true);		
		this.setAlwaysOnTop(true);
		this.toFront();
	}

	/**
	 * @param querydatas
	 * @param windowName
	 * @param name
	 */
	private void initGUI() {

		this.setModal(true);
		{
			this.setTitle("new workspace name");
			jPanel1 = new JPanel();
			getContentPane().add(jPanel1, BorderLayout.CENTER);
			GridBagLayout jPanel1Layout = new GridBagLayout();
			jPanel1Layout.columnWeights = new double[] {0.0, 0.1, 0.0};
			jPanel1Layout.columnWidths = new int[] {7, 7, 7};
			jPanel1Layout.rowWeights = new double[] {0.1, 0.0, 0.1};
			jPanel1Layout.rowHeights = new int[] {7, 7, 7};
			jPanel1.setLayout(jPanel1Layout);
		}
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

		jTextField1 = new JTextField();
		jPanel11.add(jTextField1, new GridBagConstraints(1, 0, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));


		JButton button1 = new JButton("create workspace");
		jPanel11.add(button1, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt){

				try {

					String name = jTextField1.getText();

					if(existingDatabases.contains(name))
						Workbench.getInstance().warn("a database named '" + name + "' already exists!");
					else {
						DatabaseServices.generateDatabase(name);

						NewWorkspaceRequirements.injectRequiredDataToNewWorkspace(name);

						Workbench.getInstance().info("workspace "+name+" successfuly created.");

						finish();
					}
				}
				catch (Exception e) {
					Workbench.getInstance().error("there was an error when trying to create the workspace");
					e.printStackTrace();
				}
			}
		});

		this.setSize(250, 100);
	}

	/**
	 * 
	 */
	public void finish() {

		this.setVisible(false);
		this.dispose();
	}

}
