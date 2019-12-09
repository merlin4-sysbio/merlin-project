package pt.uminho.ceb.biosystems.merlin.gui.jpanels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.SoftBevelBorder;
import javax.swing.text.NumberFormatter;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.CreateGenomeFile;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.DocumentSummary;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.DocumentSummarySet;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.AIBenchUtils;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.CreateImageIcon;
import pt.uminho.ceb.biosystems.merlin.processes.WorkspaceProcesses;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;

/**
 * @author ODias
 *
 */
public class DownloadNcbiFilesGUI extends javax.swing.JDialog implements InputGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanel1, jPanel3=null, jPanel4=null;
	private JLabel jTaxonomyID;
	private JComboBox<String> workspace;
	private JPanel jPanel11;
	private JPanel jPanel12;
	private ParamsReceiver rec = null;
	private boolean isImported=false;
	public static boolean toDownload = false;
	private JTextArea jTextArea1, jTextArea2;
	private String[] workspaces;
	private JLabel jLabelTaxID;


	/**
	 * New project Gui constructor
	 */
	public DownloadNcbiFilesGUI() {

		super(Workbench.getInstance().getMainFrame());
		initGUI();
		this.setSize(400, 200);
		Utilities.centerOnOwner(this);
	}

	/**
	 * Initiate gui method
	 */
	private void initGUI() {

		this.setModal(true);
		{

			jPanel1 = new JPanel();
			getContentPane().add(jPanel1, BorderLayout.CENTER);
			GridBagLayout jPanel1Layout = new GridBagLayout();
			jPanel1Layout.columnWeights = new double[] {0.1, 0.0, 0.1};
			jPanel1Layout.columnWidths = new int[] {7, 7, 7};
			jPanel1Layout.rowWeights = new double[] {0.1, 0.0, 0.1, 0.0};
			jPanel1Layout.rowHeights = new int[] {7, 7, 20, 7, 30};
			jPanel1.setLayout(jPanel1Layout);

			jPanel11 = new JPanel();
			GridBagLayout jPanel11Layout = new GridBagLayout();
			jPanel11.setLayout(jPanel11Layout);
			jPanel11Layout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0};
			jPanel11Layout.columnWidths = new int[] {7, 7, 7, 7, 7};
			jPanel11Layout.rowWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0};
			jPanel11Layout.rowHeights = new int[] {7, 7, 7, 7, 7, 7, 7, 7, 7};

			jPanel1.add(jPanel11, new GridBagConstraints(0, 0, 3, 2, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

			jPanel12 = new JPanel();
			GridBagLayout jPanel12Layout = new GridBagLayout();
			jPanel12.setLayout(jPanel12Layout);
			jPanel12Layout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.0};
			jPanel12Layout.columnWidths = new int[] {3, 20, 7, 50};
			jPanel12Layout.rowWeights = new double[] {0.1};
			jPanel12Layout.rowHeights = new int[] {7};
			jPanel12.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

			jPanel1.add(jPanel12, new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

			jPanel4 = new JPanel();
			GridBagLayout jPanel4Layout = new GridBagLayout();
			jPanel4.setLayout(jPanel4Layout);
			jPanel1Layout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0};
			jPanel1Layout.columnWidths = new int[] {7, 7, 7, 7, 7};
			jPanel1Layout.rowWeights = new double[] {0.1};
			jPanel1Layout.rowHeights = new int[] {7};
			jPanel4.setBorder(BorderFactory.createTitledBorder("Assembly Record Information"));


			{
				List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(WorkspaceAIB.class);
				workspaces = new String[cl.size()];
				for (int i = 0; i < cl.size(); i++) {
					workspaces[i] = (cl.get(i).getName());
				}
				workspace = new JComboBox<>(workspaces);
				workspace.setSize(232, 33);
				jPanel11.add(workspace, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

				if (workspaces.length > 0) {
					WorkspaceAIB projectData = (WorkspaceAIB) Core.getInstance().getClipboard().getItemsByClass(WorkspaceAIB.class)
							.get(0).getUserData();
					workspace.setSelectedItem(projectData.getName());
				}
				
				String taxID = "";
				if(AIBenchUtils.getAllProjects()!=null && !AIBenchUtils.getAllProjects().isEmpty()){
					WorkspaceAIB projectData = (WorkspaceAIB) Core.getInstance().getClipboard().getItemsByClass(WorkspaceAIB.class)
							.get(0).getUserData();
					Long longTaxID = projectData.getTaxonomyID();
					taxID = longTaxID.toString();
				}

				jLabelTaxID = new JLabel();
				jLabelTaxID.setText(taxID);
				jLabelTaxID.setSize(232, 33);
				jPanel11.add(jLabelTaxID, new GridBagConstraints(3, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

			}


			JButton okButton = new JButton("ok");
			okButton.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Ok.png")),0.1).resizeImageIcon());
			jPanel12.add(okButton, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			okButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt){

					boolean go=true;

					String database = null;
					if(workspace.getSelectedItem()==null) {

						go=false;
						Workbench.getInstance().error("please select a workspace");
					}
					else {

						database = workspace.getSelectedItem().toString();
					}
					
					boolean isGenBank = AssemblyPanel.isGenBank;

					String taxonomyID = jLabelTaxID.getText();
					Long taxIdLong = Long.parseLong(taxonomyID);
					
					if(isImported)
						go=isDownloadAndRewriteFiles();
					
					if(toDownload && go) {

						DocumentSummary docSummary = AssemblyPanel.selectedDocSummary;

						rec.paramsIntroduced(
								new ParamSpec[]{
										new ParamSpec("workspaceName", String.class, database, null),
										//new ParamSpec("name", String.class, database, null),
										new ParamSpec("taxonomyID", Long.class,taxIdLong,null),
										new ParamSpec("docSummary", DocumentSummary.class, docSummary, null),
										new ParamSpec("isGenBank", Boolean.class, isGenBank, null),
								}
								);
					}

					else {
						Workbench.getInstance().info("files not downloaded!");
						finish();
					}

				}
			});

			JButton button2 = new JButton("cancel");
			button2.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Cancel.png")),0.1).resizeImageIcon());
			jPanel12.add(button2, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			button2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					finish();
				}
			});

			JLabel jLabel5 = new JLabel();
			jLabel5.setText("workspace");
			jPanel11.add(jLabel5, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 5, 6, 5), 0, 0));

			JButton searchButton = new JButton();
			jPanel11.add(searchButton, new GridBagConstraints(4, 6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 5, 6, 5), 0, 0));
			searchButton.setText("search");
			searchButton.setToolTipText("search assembly records");
			searchButton.setEnabled(true);

			jTextArea1 = new JTextArea();
			jTextArea1.setEditable(false);
			jTextArea2 = new JTextArea();
			jTextArea2.setEditable(false);
			jPanel4.add(jTextArea1, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 5, 6, 5), 0, 0));
			jPanel4.add(jTextArea2, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 5, 6, 5), 0, 0));

			workspace.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {

					if (e.getStateChange() == 1) {

						if(jPanel3 != null) {
							removePanel(jPanel3);
							toDownload = false;
						}

						if(jPanel4 != null)
							removePanel(jPanel4);

						String databaseName = workspace.getSelectedItem().toString();

						if (databaseName != null){

							String taxID = null;
							try {
								taxID = getTaxonomyID(databaseName);
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							if(taxID != null){
								Long taxIDLong = Long.parseLong(taxID);

								//								jComboBoxTaxIDs.setSelectedItem(taxID);
								jLabelTaxID.setText(taxID);

								if (WorkspaceProcesses.isFaaFiles(databaseName, taxIDLong) || WorkspaceProcesses.isFnaFiles(databaseName, taxIDLong)){

									//									jbutton3.setEnabled(false);
									isImported = true;
									File assemblyRecord = new File(FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, taxIDLong) + "assemblyRecordInfo.txt");
									if(assemblyRecord.exists()) { 
										updateTextArea(databaseName);
										jPanel1.add(jPanel4, new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
										Workbench.getInstance().info("Files already downloaded");
									}
									else
										Workbench.getInstance().info("Files already manually loaded");
								}
								else{
									searchButton.setEnabled(true);
									isImported = false;
								}
							}
							else{
								searchButton.setEnabled(false);
								toDownload = false;
							}

						}
					}

				}
			}); 

			searchButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) {

					//					if(jComboBoxTaxIDs.getSelectedItem() != null  && Long.parseLong(jComboBoxTaxIDs.getSelectedItem().toString())>0) {

					if(jPanel3 != null) {
						removePanel(jPanel3);
						//							toDownload = false;
					}

					if(jPanel4 != null)
						removePanel(jPanel4);

					if(workspace.getSelectedItem()!=null){

						String dbName = workspace.getSelectedItem().toString();
						//						String taxID = jComboBoxTaxIDs.getSelectedItem().toString();
						String taxID = jLabelTaxID.getText();

						Long taxIDlong = Long.parseLong(taxID);

						CreateGenomeFile.createFolder(dbName, taxIDlong);

						if (WorkspaceProcesses.isFaaFiles(dbName, taxIDlong) || WorkspaceProcesses.isFnaFiles(dbName,taxIDlong)){
							isImported = true;
							File assemblyRecord = new File(FileUtils.getWorkspaceTaxonomyFolderPath(dbName, taxIDlong) +"assemblyRecordInfo.txt");
							if(assemblyRecord.exists()) { 
								updateTextArea(dbName);
								jPanel1.add(jPanel4, new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
										new Insets(0, 0, 0, 0), 0, 0));
								
								Workbench.getInstance().info("Files already downloaded");

							}
							else
								Workbench.getInstance().info("Files already manually loaded");
							
						}
						AssemblyPanel panel = new AssemblyPanel();
						jPanel3 = panel.constructPanel(taxID);
						jPanel1.add(jPanel3, new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jPanel3.setBorder(BorderFactory.createTitledBorder("Download from NCBI ftp"));
						setComboBoxAssembly(taxID);

						if(!AssemblyPanel.jComboBox1.getSelectedItem().toString().isEmpty()){
							panel.updateTextArea(AssemblyPanel.jComboBox1.getSelectedItem().toString());
							toDownload = true;
						}
					}

				}});

			{
				NumberFormat format = NumberFormat.getInstance();
				format.setMinimumIntegerDigits(1);
				format.setMaximumIntegerDigits(15);
				format.setGroupingUsed(false);
				format.setParseIntegerOnly(true);
				NumberFormatter formatter = new NumberFormatter(format);
				formatter.setValueClass(Integer.class);
				formatter.setMinimum(0);
				formatter.setMaximum(Integer.MAX_VALUE);
				formatter.setCommitsOnValidEdit(true);


				jTaxonomyID = new JLabel();
				jPanel11.add(jTaxonomyID, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
				jTaxonomyID.setText("taxonomy ID:");
			}
		}
		this.setSize(400, 200);
		Utilities.centerOnOwner(this);
	}


	/* (non-Javadoc)
	 * @see es.uvigo.ei.aibench.workbench.InputGUI#finish()
	 */
	public void finish() {

		this.setVisible(false);
		this.dispose();
	}

	/* (non-Javadoc)
	 * @see es.uvigo.ei.aibench.workbench.InputGUI#init(es.uvigo.ei.aibench.workbench.ParamsReceiver, es.uvigo.ei.aibench.core.operation.OperationDefinition)
	 */
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {

		this.rec = arg0;
		this.setTitle(arg1.getName());
		this.setVisible(true);
	}

	/* (non-Javadoc)
	 * @see es.uvigo.ei.aibench.workbench.InputGUI#onValidationError(java.lang.Throwable)
	 */
	public void onValidationError(Throwable arg0) {

		Workbench.getInstance().error(arg0);
	}


	/**
	 * Get TaxonomyId for the given database
	 * 
	 * @param dbname
	 * @return
	 */
	private String getTaxonomyID(String dbname) throws SQLException{

		WorkspaceAIB project = AIBenchUtils.getProject(dbname);

		String taxID = null;
		Long longTaxID;

		if(project.getName().equals(dbname)){
			longTaxID = project.getTaxonomyID();
			taxID = longTaxID.toString();
		}

		return taxID;
	}


	/**
	 * @param taxID
	 */
	private void setComboBoxAssembly(String taxID) {

		this.setSize(550, 400);
		Utilities.centerOnOwner(this);

		try{
			DefaultComboBoxModel<String> aNames = new DefaultComboBoxModel<>();

			DocumentSummarySet docSummaryset = CreateGenomeFile.getESummaryFromNCBI(taxID);
			List<String> assemblyNames = CreateGenomeFile.getAssemblyNames(docSummaryset);
			assemblyNames.add("don't download");

			aNames = new DefaultComboBoxModel<>(assemblyNames.toArray(new String[assemblyNames.size()]));

			AssemblyPanel.jComboBox1.setModel(aNames);
			AssemblyPanel.jComboBox1.updateUI();

		}

		catch(Exception e){
			Workbench.getInstance().warn("Assembly records not found for given taxonomyID.");
			e.printStackTrace();
		}
	}

	/**
	 * @param dbName
	 */
	private void updateTextArea(String dbName) {

		//		String taxonomyID = jComboBoxTaxIDs.getSelectedItem().toString();
		String taxonomyID = jLabelTaxID.getText();

		List<String> assemblyInfo = CreateGenomeFile.getAssemblyRecordInfo(dbName, taxonomyID);
		String text1 = assemblyInfo.get(0);
		String text2 = assemblyInfo.get(4);

		for(int i=1; i<4; i++) {
			text1 += "\n" + assemblyInfo.get(i);
			text2 += "\n" + assemblyInfo.get(i+4);
		}

		jTextArea1.setText(text1);
		jTextArea2.setText(text2);
		this.setSize(550, 350);
		Utilities.centerOnOwner(this);
	}


	/**
	 * @param panel
	 */
	private void removePanel(Component panel){
		this.jPanel1.remove(panel);
		this.setSize(400, 200);
		Utilities.centerOnOwner(this);
	}
	
	
	/**
	 * @return
	 */
	private boolean isDownloadAndRewriteFiles() {
		
		int i =CustomGUI.stopQuestion("Continue download operation?", "Files already downloaded. Do you wish to continue and download new files?",
				new String[]{"Continue", "Cancel", "Info"});

		if(i<2) {

			switch (i)
			{
			case 0:return true;
			case 1: return false;
			default:return true;
			}
		}
		else{
			Workbench.getInstance().info("merlin has detected that the genome sequences and annotation files are already present\n"
					+ "in your model's workspace folder. Thus, if you continue the download operation those files will be replaced\n"
					+ "by the newly downloaded ones.\n"
					+ "For further information please contact us to support@merlin-sysbio.org");

			return isDownloadAndRewriteFiles();
		}
	}
}
