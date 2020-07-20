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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.SoftBevelBorder;
import javax.swing.text.NumberFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.CreateImageIcon;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.CreateGenomeFile;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.NcbiAPI;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.DocumentSummary;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.DocumentSummarySet;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.Workspace;
import pt.uminho.ceb.biosystems.merlin.core.utilities.EmailValidator;
import pt.uminho.ceb.biosystems.merlin.processes.WorkspaceProcesses;
import pt.uminho.ceb.biosystems.merlin.services.DatabaseServices;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;

/**
 * @author ODias
 *
 */
public class WorkspaceNewGUI extends javax.swing.JDialog implements InputGUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanel1, jPanel3=null, jPanel4=null;
	private JLabel jTaxonomyID;
	private JFormattedTextField jTextField4;
	private JComboBox<String> jComboBox1;
	private JPanel jPanel11;
	private JPanel jPanel12;
	private ParamsReceiver rec = null;
	private JButton btnNewDB;
	private boolean isImported=false;
	public static boolean toDownload;
	private JTextArea jTextArea1, jTextArea2;
	private boolean emailValid;
	private JLabel jEmail;
	private JTextField jTextField5;
	private List<String> schemas;

	private static final Logger logger = LoggerFactory.getLogger(WorkspaceNewGUI.class);

	/**
	 * New project Gui constructor
	 */
	public WorkspaceNewGUI() {

		super(Workbench.getInstance().getMainFrame());
		initGUI();
		updateComboBox();
		Utilities.centerOnOwner(this);
		fill();
	}

	/**
	 * Initiate gui method
	 */
	private void initGUI() {

		this.setModal(true);
		WorkspaceNewGUI.toDownload = false;

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
			jPanel4.setBorder(BorderFactory.createTitledBorder("assembly record information"));

			JButton button1 = new JButton("ok");
			button1.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Ok.png")),0.1).resizeImageIcon());
			jPanel12.add(button1, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			button1.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt){
					emailValid = saveConfigurations();
					if (!emailValid) 
						return;

					boolean go=true;

					String databaseName = null;
					if(jComboBox1.getSelectedItem()==null) {

						go=false;
						Workbench.getInstance().error("please select a workspace");
					}
					else {

						databaseName = jComboBox1.getSelectedItem().toString();
					}

					List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(Workspace.class);

					boolean exists = false;

					for (ClipboardItem item : cl) {

						String databaseName_previous = ((WorkspaceAIB)item.getUserData()).getName();

						if(!exists) {

							exists = (databaseName!= null && databaseName.equals(databaseName_previous));

						}
					}

					if(exists) {

						go=false;
						Workbench.getInstance().error("workspace connected to the same data base already exists!\nplease select another workspace.");
					}

					String taxonomyID = jTextField4.getText();

					if(!isImported) {

						Long taxIdLong = null;

						if(taxonomyID!= null && !taxonomyID.isEmpty() && Integer.parseInt(taxonomyID)>0) {

							taxIdLong = Long.parseLong(taxonomyID);

							try {

								if(ProjectServices.getOrganismID(databaseName) == null)
									NcbiAPI.getTaxonomyFromNCBI(taxIdLong, 0);
							} 
							catch (Exception e) {

								throw new IllegalArgumentException("error validating taxonomy identifier, please verify your input and try again.");
							}
						}
						else {

							Workbench.getInstance().error("please insert a valid taxonomy identifier!");
							go = false;
						}

						if(toDownload && go) {

							boolean isGenBank = AssemblyPanel.isGenBank;
							DocumentSummary docSummary = AssemblyPanel.selectedDocSummary;

							try  {

								ParamSpec[] paramsSpec = new ParamSpec[]{
										new ParamSpec("workspaceName", String.class, databaseName, null),
										new ParamSpec("taxonomyID", Long.class,taxIdLong,null),
										new ParamSpec("docSummary", DocumentSummary.class, docSummary, null),
										new ParamSpec("isGenBank", Boolean.class, isGenBank, null),
								};

								for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()) {

									if (def.getID().equals("operations.DownloadNcbiFtpFiles.ID")) {

										Workbench.getInstance().executeOperation(def, paramsSpec);
									}
								}
							}

							catch (Exception e) {

								Workbench.getInstance().error("error downloading files!");
								e.printStackTrace();
							}
						}
						else {

							try {
								if(go && !ProjectServices.genomeFilesLoaded(databaseName))
									Workbench.getInstance().warn("genome files not loaded!");
							} 
							catch (Exception e) {
								Workbench.getInstance().error(e);
								e.printStackTrace();
							}
						}
					}

					if(go) {

						try {
							ProjectServices.updateOrganismID(databaseName, Long.valueOf(taxonomyID));

							CreateGenomeFile.createFolder(databaseName, Long.parseLong(taxonomyID));

							rec.paramsIntroduced(
									new ParamSpec[]{
											new ParamSpec("Database",String.class,databaseName,null),
											new ParamSpec("TaxonomyID",long.class,Long.parseLong(taxonomyID),null),
									}
									);

						}
						catch (Exception e) {
							Workbench.getInstance().error("an error occurred while opening the workspace");
							e.printStackTrace();
						}


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

			btnNewDB = new JButton(" create ");
			//btnNewDB.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Add.png")),0.1).resizeImageIcon());
			btnNewDB.setToolTipText("add");
			jPanel11.add(btnNewDB, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
			btnNewDB.setSize(232, 33);
			btnNewDB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					createNewWorkspace();
					updateComboBox();
				}
			});

			JLabel jLabel5 = new JLabel();
			jLabel5.setText("workspace");
			jPanel11.add(jLabel5, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 5, 6, 5), 0, 0));
			{
				jComboBox1 = new JComboBox<>();
				jComboBox1.setSize(232, 33);
				jPanel11.add(jComboBox1, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));
			}

			JButton searchButton = new JButton();
			jPanel11.add(searchButton, new GridBagConstraints(4, 6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 5, 6, 5), 0, 0));
			searchButton.setText(" search ");
			searchButton.setToolTipText("search assembly records");
			searchButton.setEnabled(true);


			jTextArea1 = new JTextArea();
			jTextArea1.setEditable(false);
			jTextArea2 = new JTextArea();
			jTextArea2.setEditable(false);
			jPanel4.add(jTextArea1, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 5, 6, 5), 0, 0));
			jPanel4.add(jTextArea2, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 5, 6, 5), 0, 0));


			jComboBox1.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {

					try {
						if (e.getStateChange() == 1) {

							if(jPanel3 != null) {
								removePanel(jPanel3);
								toDownload = false;
							}

							if(jPanel4 != null)
								removePanel(jPanel4);

							String databaseName = jComboBox1.getSelectedItem().toString();

							if (databaseName != null){

								boolean dropConnection = false;			//check if the service always fails to minimal changes (extra column in a table for instance). If so, the exception below is enough and this validation is not needed, what will cause merlin to open a workspace faster.

								try {
									DatabaseServices.getValidationDatabaseService(databaseName);
									dropConnection = true;
								} 
								catch (Exception e1) {
									logger.warn("It was not possible to create a database service with validation method for database '" + databaseName + "'! Retrying with uptade to the database structure!");
									//e1.printStackTrace();
									DatabaseServices.getDatabaseService(databaseName);
									logger.info("Success with update method");
									dropConnection = false;
								}

								Integer taxID = ProjectServices.getOrganismID(databaseName);

								if(dropConnection)
									DatabaseServices.dropConnection(databaseName);

								Long taxIDLong = null;

								if(taxID != null && !taxID.toString().equalsIgnoreCase("null")){
									taxIDLong = Long.parseLong(taxID+"");

									jTextField4.setText(taxID+"");

									if (WorkspaceProcesses.isFaaFiles(databaseName, taxIDLong) || WorkspaceProcesses.isFnaFiles(databaseName, taxIDLong)){

										searchButton.setEnabled(false);
										isImported = true;
										File assemblyRecord = new File(FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, taxIDLong) + "assemblyRecordInfo.txt");
										if(assemblyRecord.exists()) { 
											updateTextArea(databaseName);
											jPanel1.add(jPanel4, new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
											Workbench.getInstance().info("Files already imported");
										}
										else
											Workbench.getInstance().info("Files already manually imported");
									}
									else{
										searchButton.setEnabled(true);
										isImported = false;
									}
								}

								else{
									jTextField4.setText("");
									searchButton.setEnabled(true);
									isImported = false;
								}

							}
						}
					}
					catch (Exception e1) {
						Workbench.getInstance().error(e1);
						e1.printStackTrace();
					}

				}
			}); 

			searchButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0) {

					String taxID = jTextField4.getText();

					if(taxID != null && !jTextField4.getText().isEmpty() && Long.parseLong(taxID)>0) {

						if(jPanel3 != null) {
							removePanel(jPanel3);
							toDownload = false;
						}

						if(jPanel4 != null)
							removePanel(jPanel4);

						String dbName = jComboBox1.getSelectedItem().toString();

						Long taxIDlong = Long.parseLong(taxID);

						CreateGenomeFile.createFolder(dbName, taxIDlong);

						if (WorkspaceProcesses.isFaaFiles(dbName, taxIDlong) || WorkspaceProcesses.isFnaFiles(dbName,taxIDlong)){
							isImported = true;
							searchButton.setEnabled(false);
							File assemblyRecord = new File(FileUtils.getWorkspaceTaxonomyFolderPath(dbName, taxIDlong) +"assemblyRecordInfo.txt");
							if(assemblyRecord.exists()) { 
								updateTextArea(dbName);
								jPanel1.add(jPanel4, new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

								Workbench.getInstance().info("files already imported");
							}
							else
								Workbench.getInstance().info("files already manually imported");
						}
						else {
							AssemblyPanel panel = new AssemblyPanel();
							jPanel3 = panel.constructPanel(taxID);
							jPanel1.add(jPanel3, new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							jPanel3.setBorder(BorderFactory.createTitledBorder("download from NCBI ftp"));
							setComboBoxAssembly(taxID);

							if(!AssemblyPanel.jComboBox1.getSelectedItem().toString().isEmpty()){
								panel.updateTextArea(AssemblyPanel.jComboBox1.getSelectedItem().toString());
								toDownload = true;
							}
						}

					}
					else 
						Workbench.getInstance().error("please insert a valid taxonomy identifier!");

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

				jTextField4 = new JFormattedTextField(formatter);
				//jTextField4.setFocusLostBehavior(JFormattedTextField.PERSIST);
				jPanel11.add(jTextField4, new GridBagConstraints(3, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));
				//jTextField4.setEnabled(false);
				jTextField4.setEditable(true);
				jTextField4.addKeyListener(new KeyListener() {

					@Override
					public void keyTyped(KeyEvent e) {

						searchButton.setEnabled(true);
						if(jPanel3 != null) {
							removePanel(jPanel3);
							toDownload = false;
						}

						if(jPanel4 != null)
							removePanel(jPanel4);
					}

					@Override
					public void keyPressed(KeyEvent e) {
					}

					@Override
					public void keyReleased(KeyEvent e) {
					}

				});

				jTaxonomyID = new JLabel();
				jPanel11.add(jTaxonomyID, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
				jTaxonomyID.setText("taxonomy identifier");

				jEmail = new JLabel();
				jPanel11.add(jEmail, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
				jEmail.setText("email");

				jTextField5 = new JTextField();
				jPanel11.add(jTextField5, new GridBagConstraints(3, 9, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0 ));
				jTextField5.setEditable(true);
			}
		}
		this.setSize(600, 200);
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
	 * @throws Exception 
	 * @throws IOException 
	 * 
	 */
	private void updateComboBox() {

		try {
			setComboBox();

			if(!this.schemas.isEmpty()) {

				String databaseName = jComboBox1.getSelectedItem()+"";
				boolean dropConnection = false;			//check if the service always fails to minimal changes (extra column in a table for instance). If so, the exception below is enough and this validation is not needed, what will cause merlin to open a workspace faster.

				try {
					DatabaseServices.getValidationDatabaseService(databaseName);
					dropConnection = true;
				} 
				catch (Exception e) {
					logger.warn("It was not possible to create a database service with validation method for database '" + databaseName + "'! Retrying with uptade to the database structure!");
					//e.printStackTrace();
					DatabaseServices.getDatabaseService(databaseName);
					logger.info("Success with update method");
					dropConnection = false;
				}

				Integer taxId = ProjectServices.getOrganismID(databaseName);

				if(dropConnection)
					DatabaseServices.dropConnection(databaseName);


				if(taxId != null && !taxId.toString().equalsIgnoreCase("null"))
					jTextField4.setText(taxId.toString());
				else
					jTextField4.setText("");
			}
		} 
		catch (Exception e1) {

			Workbench.getInstance().error("no connection! cause: " +e1.getMessage());
			e1.printStackTrace();
		}
	}


	/**
	 * @throws Exception 
	 * 
	 */
	private void setComboBox() throws Exception  {

		DefaultComboBoxModel<String> sch = new DefaultComboBoxModel<>();
		try {			
		this.schemas = DatabaseServices.getDatabasesAvailable();
		}

		catch (org.h2.jdbc.JdbcSQLInvalidAuthorizationSpecException e) {
			e.printStackTrace();
			this.schemas = new ArrayList<>();
			Workbench.getInstance().warn("Authentication failed to access the H2 database! Wrong username or password. Please go to settings -> database configuration and enter the correct credentials.");
		}
		
		catch (java.lang.Exception e) {
			String message = e.getMessage();
			if(message.contains("Access denied for user")) {
				e.printStackTrace();
				this.schemas = new ArrayList<>();
				Workbench.getInstance().warn("Authentication failed to access the SQL database! Wrong username or password. Please go to settings -> database configuration and enter the correct credentials.");
			}
		}
	


		//get list of databases already used by projects, and remove those from schemas

		if(schemas.isEmpty()) {
			if(createDatabase()) {
				createNewWorkspace();
				schemas = DatabaseServices.getDatabasesAvailable();
			}
			else {

				Workbench.getInstance().warn("no workspace available for merlin");
			}
		}
		sch = new DefaultComboBoxModel<>(schemas.toArray(new String[schemas.size()]));
		//		}
		//		else {
		//			Workbench.getInstance().error("check your database configuration file in merlin directory at \\conf\\database_settings.conf");
		//		}		

		jComboBox1.setModel(sch);
		jComboBox1.updateUI();

	}

	/**
	 * @param unAnnotated
	 * @param initialSize
	 * @param location
	 * @return
	 */
	private static boolean createDatabase(){

		int i =CustomGUI.stopQuestion("create new workspace?",
				"merlin could not detect any compatible workspace, do you wish to create one?",
				new String[]{"yes", "no"});


		switch (i) {

		case 0:return true;
		case 1: return false;
		}
		return false;
	}

	private void createNewWorkspace() {

		new CreateNewWorkspaceGUI(this.schemas);
	}

	//	/**
	//	 * @param name
	//	 * @return
	//	 */
	//	private String buildName(String name) {
	//
	//		Project.setCounter(Project.getCounter()+1);
	//		name="MyProject_"+Project.getCounter();
	//		return name;
	//	}


	private void setComboBoxAssembly(String taxID) {

		this.setSize(550, 400);
		Utilities.centerOnOwner(this);

		try{
			DefaultComboBoxModel<String> aNames = new DefaultComboBoxModel<>();

			DocumentSummarySet docSummaryset = CreateGenomeFile.getESummaryFromNCBI(taxID);
			List<String> assemblyNames = CreateGenomeFile.getAssemblyNames(docSummaryset);
			assemblyNames.add("do not download");

			aNames = new DefaultComboBoxModel<>(assemblyNames.toArray(new String[assemblyNames.size()]));

			AssemblyPanel.jComboBox1.setModel(aNames);
			AssemblyPanel.jComboBox1.updateUI();

		}

		catch(Exception e){
			Workbench.getInstance().warn("assembly records not found for provided taxonomy identifier");
			e.printStackTrace();
		}
	}

	private void updateTextArea(String dbName) {

		String taxonomyID = jTextField4.getText();

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

	private void removePanel(Component panel){
		this.jPanel1.remove(panel);
		this.setSize(600, 200);
		Utilities.centerOnOwner(this);
	}

	public void fill () {

		ArrayList<String> listLines= new ArrayList<>();
		String confPath = FileUtils.getConfFolderPath().concat("email.conf");
		File configfile= new File(confPath);
		try {

			if(!configfile.exists())
				configfile.createNewFile();

			Scanner file= new Scanner(configfile);
			while(file.hasNextLine()==true) {
				listLines.add(file.nextLine());
			}
			file.close();
		} catch (IOException e) {

			Workbench.getInstance().error(e.getMessage());
		}

		for (String item : listLines) {

			if(item.startsWith("email")) {

				String[]parts=item.split(":");
				jTextField5.setText(parts[1].trim());
				continue;

			}

		}
	}

	public boolean saveConfigurations() {
		boolean isValid = true;
		if(!jTextField5.getText().equals("")) {

			if (EmailValidator.isEmailValid(jTextField5.getText())) {

				String path = FileUtils.getConfFolderPath().concat("email.conf");

				try {
					PrintWriter confFile = new PrintWriter(path); 

					String newline = "email: ".concat(jTextField5.getText().trim());

					confFile.println(newline);

					confFile.close();

				} catch (FileNotFoundException e) {
					Workbench.getInstance().error(e.getMessage());
					isValid=false;

				}
			}
			else {
				Exception ex = new Exception("please insert a valid e-mail!");
				onValidationError(ex);
				isValid=false;
			}
		}else {
			Exception ex = new Exception("e-mail field must be filled!");
			onValidationError(ex);
			isValid=false;
		}

		return isValid;

	}
}
