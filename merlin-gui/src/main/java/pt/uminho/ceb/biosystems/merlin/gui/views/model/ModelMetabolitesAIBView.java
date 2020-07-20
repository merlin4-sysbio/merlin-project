package pt.uminho.ceb.biosystems.merlin.gui.views.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.ModelMetabolitesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.jpanels.CustomGUI;
import pt.uminho.ceb.biosystems.merlin.gui.jpanels.InsertEditMetabolite;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.ButtonColumn;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.CreateImageIcon;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.ExportToXLS;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MyJTable;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.SearchInTable;
import pt.uminho.ceb.biosystems.merlin.gui.views.WorkspaceUpdatablePanel;
import pt.uminho.ceb.biosystems.merlin.gui.views.windows.GenericDetailWindow;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelMetabolitesServices;

/**
 * @author ODias
 * 
 */
public class ModelMetabolitesAIBView extends WorkspaceUpdatablePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane1;
	private JButton jButton1ExportTxt;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private MyJTable jTable;
	private ModelMetabolitesAIB modelMetabolites;
	private JRadioButton jRadioAllReactions;
	private JRadioButton jRadioButtonEncoded;
	private JPanel jPanelReactions;
	private JPanel jPanel5;
	private JPanel jPanel4;
	private WorkspaceGenericDataTable dataTable;
	private JPanel jPanel3;
	private JRadioButton jRadioButton1;
	private JRadioButton jRadioButton2;
	private JRadioButton jRadioButton3;
	private ButtonGroup buttonGroup1;
	private ButtonGroup buttonGroup2;
	private int infoSelectedRow;
	private ButtonColumn buttonColumn;
	private SearchInTable searchInTable;
	private JPanel jPanelInsertEdit;
	private JButton jButtonInsert;
	private JButton jButtonEdit;
	private JButton jButtonRemove;
	private int metaboliteNameColumn = 1;
	private int metaboliteKeggIdColumn = 4;
	private JPanel jPanelEntryTypes;
	private JCheckBox jCheckBoxDrugs;
	private JCheckBox jCheckBoxCompound;
	private JCheckBox jCheckBoxGlycan;
	private JCheckBox jCheckBoxAll;;

	/**
	 * @param rp
	 */
	public ModelMetabolitesAIBView(ModelMetabolitesAIB rp) {

		super(rp);
		this.modelMetabolites = rp;
		List<Integer> nameTabs = new ArrayList<>();
		nameTabs.add(1);
		nameTabs.add(3);
		nameTabs.add(4);
		this.searchInTable = new SearchInTable(nameTabs);
		initGUI();
		fillList(true);
	}

	/**
	 * 
	 */
	private void initGUI() {

		try {

			GridBagLayout jPanel1Layout = new GridBagLayout();
			jPanel1Layout.columnWeights = new double[] { 0.0, 0.1, 0.0 };
			jPanel1Layout.columnWidths = new int[] { 7, 7, 7 };
			jPanel1Layout.rowWeights = new double[] { 0.0, 200.0, 0.0, 0.0, 0.0 };
			jPanel1Layout.rowHeights = new int[] { 7, 50, 7, 3, 7 };
			// jPanel1Layout.columnWeights = new double[] {0.0, 0.1, 0.0};
			// jPanel1Layout.columnWidths = new int[] {7, 7, 7};
			// jPanel1Layout.rowWeights = new double[] {0.0, 3.5, 0.0, 0.1,
			// 0.0};
			// jPanel1Layout.rowHeights = new int[] {5, 25, 5, 5, 5};
			this.setLayout(jPanel1Layout);

			jPanel2 = new JPanel();
			GridBagLayout jPanel2Layout = new GridBagLayout();
			jPanel2Layout.rowWeights = new double[] { 0.1 };
			jPanel2Layout.rowHeights = new int[] { 7 };
			jPanel2Layout.columnWeights = new double[] { 0.1, 0.0, 0.0, 0.0,
					0.0, 0.0, 0.0 };
			jPanel2Layout.columnWidths = new int[] { 50, 7, 6, 7, 6, 7, 6 };
			jPanel2.setLayout(jPanel2Layout);
			this.add(jPanel2, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.5,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			
			jPanelEntryTypes = new JPanel();
			jPanel2.add(jPanelEntryTypes, new GridBagConstraints(7, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			GridBagLayout jPanel6Layout = new GridBagLayout();
			jPanel6Layout.rowWeights = new double[] { 0.0, 0.0, 0.0 };
			jPanel6Layout.rowHeights = new int[] { 7, 7, 7 };
			jPanel6Layout.columnWeights = new double[] { 0.1 };
			jPanel6Layout.columnWidths = new int[] { 6 };
			jPanelEntryTypes.setLayout(jPanel6Layout);
			jPanelEntryTypes.setBorder(BorderFactory.createTitledBorder("entry types"));
			{
				
				{
					jCheckBoxGlycan = new JCheckBox("glycan");
					jPanelEntryTypes.add(jCheckBoxGlycan, new GridBagConstraints(0, 0, 1, 1,
							0.0, 0.0, GridBagConstraints.WEST,
							GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jCheckBoxGlycan.setSelected(true);
					jCheckBoxGlycan.addActionListener( new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {
							
							jCheckBoxAll.setSelected(false);
							
							fillList(jRadioButtonEncoded.isSelected());
						}
					});
					
				}
				{
					jCheckBoxCompound = new JCheckBox("compound");
					jPanelEntryTypes.add(jCheckBoxCompound, new GridBagConstraints(0, 1, 1, 1,
							0.0, 0.0, GridBagConstraints.WEST,
							GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jCheckBoxCompound.setSelected(true);
					jCheckBoxCompound.addActionListener( new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {
							
							jCheckBoxAll.setSelected(false);
							
							fillList(jRadioButtonEncoded.isSelected());
						}
					});
				}
				{
					jCheckBoxDrugs = new JCheckBox("drugs");
					jPanelEntryTypes.add(jCheckBoxDrugs, new GridBagConstraints(1, 0, 1, 1,
							0.0, 0.0, GridBagConstraints.WEST,
							GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jCheckBoxDrugs.setSelected(true);
					jCheckBoxDrugs.addActionListener( new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {
							
							jCheckBoxAll.setSelected(false);
							
							fillList(jRadioButtonEncoded.isSelected());
						}
					});
				}
				{
					jCheckBoxAll = new JCheckBox("all");
					jPanelEntryTypes.add(jCheckBoxAll, new GridBagConstraints(1, 1, 1, 1,
							0.0, 0.0, GridBagConstraints.WEST,
							GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jCheckBoxAll.setSelected(true);
					jCheckBoxAll.addActionListener( new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {
							
							jCheckBoxCompound.setSelected(false);
							jCheckBoxDrugs.setSelected(false);
							jCheckBoxGlycan.setSelected(false);
							
							fillList(jRadioButtonEncoded.isSelected());
						}
					});
				}
				
				//jPanel3.add(buttonGroup3);
				
			}

			jPanel3 = new JPanel();
			jPanel2.add(jPanel3, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			GridBagLayout jPanel3Layout = new GridBagLayout();
			jPanel3Layout.rowWeights = new double[] { 0.0, 0.0, 0.0 };
			jPanel3Layout.rowHeights = new int[] { 7, 7, 7 };
			jPanel3Layout.columnWeights = new double[] { 0.1 };
			jPanel3Layout.columnWidths = new int[] { 6 };
			jPanel3.setLayout(jPanel3Layout);
			jPanel3.setBorder(BorderFactory.createTitledBorder("types"));

			jPanel4 = new JPanel();
			jPanel2.add(jPanel4, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE,
					new Insets(0, 0, 0, 0), 0, 0));
			GridBagLayout searchPanelLayout = new GridBagLayout();
			searchPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.0 };
			searchPanelLayout.rowHeights = new int[] { 7, 7, 7 };
			searchPanelLayout.columnWeights = new double[] { 0.1 };
			searchPanelLayout.columnWidths = new int[] { 7 };
			jPanel4.setLayout(searchPanelLayout);
			jPanel4.setBorder(BorderFactory.createTitledBorder("export"));
			jPanel4.setBounds(30, 26, 676, 34);

			{
				jPanelInsertEdit = new JPanel();
				GridBagLayout jPanelInsertEditLayout = new GridBagLayout();
				jPanelInsertEditLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0, 0.1};
				jPanelInsertEditLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7};
				jPanelInsertEditLayout.rowWeights = new double[] {0.0};
				jPanelInsertEditLayout.rowHeights = new int[] {5};
				jPanelInsertEdit.setLayout(jPanelInsertEditLayout);
				jPanel2.add(jPanelInsertEdit, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelInsertEdit.setBounds(7, 56, 277, 61);
				jPanelInsertEdit.setBorder(BorderFactory.createTitledBorder("insert/edit/remove"));
				{
					jButtonInsert = new JButton();
					jPanelInsertEdit.add(jButtonInsert, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jButtonInsert.setIcon( new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Upload.png")),0.1).resizeImageIcon());
					jButtonInsert.setText("insert");
					jButtonInsert.setToolTipText("insert new metabolite");
					jButtonInsert.setSize(90, 40);
					jButtonInsert.addActionListener( new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {

							new InsertEditMetabolite(-1, "", "", modelMetabolites);
							fillList(jRadioButtonEncoded.isSelected());
							
						}
					}
							);
				}
				{
					jButtonEdit = new JButton();
					jPanelInsertEdit.add(jButtonEdit, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jButtonEdit.setText("edit");
					jButtonEdit.setToolTipText("edit metabolite data");
					jButtonEdit.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Edit.png")),0.1).resizeImageIcon());
					jButtonEdit.setSize(90, 40);
					jButtonEdit.addActionListener( new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {
							
							int row = jTable.getSelectedRow();
							
							if(jTable.getSelectedRow()>-1) {
								
								String metaboliteName = jTable.getValueAt(row, metaboliteNameColumn).toString();
								String keggID = jTable.getValueAt(row, metaboliteKeggIdColumn).toString();
								new InsertEditMetabolite(row, metaboliteName, keggID, modelMetabolites);
								
							}
							else {

								Workbench.getInstance().warn("Please Select a Row!");
							}
							fillList(jRadioButtonEncoded.isSelected());
						}
					}
							);
				}
				{
					jButtonRemove = new JButton();
					jPanelInsertEdit.add(jButtonRemove, new GridBagConstraints(4, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jButtonRemove.setText("remove");
					jButtonRemove.setToolTipText("remove metabolite");
					jButtonRemove.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Remove.png")),0.1).resizeImageIcon());
					jButtonRemove.setSize(90, 40);
					jButtonRemove.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {

							try {
								if(jTable.getSelectedRow()>-1) {
									
									String metaboliteName = jTable.getValueAt(jTable.getSelectedRow(), metaboliteNameColumn).toString();
									String metaboliteKeggID = jTable.getValueAt(jTable.getSelectedRow(), metaboliteKeggIdColumn).toString();
									
									List<String> reactions = modelMetabolites.getRelatedReactions(metaboliteName);
									
									if (reactions.size() > 0){
										String aux = "reactions: \n";
										
										if (reactions.size() == 1)
											aux = "reaction: \n";
										
										Workbench.getInstance().error("The selected metabolite is present in the following " + aux 
												+ reactions.toString().replaceAll("\\[", "").replaceAll("\\]", "") + "\n\n Total number of reactions: "+reactions.size());
									}
									else{
										boolean discard = discardData(metaboliteName, reactions);
										
										if (discard){
											ModelMetabolitesServices.removeCompoundByExternalIdentifier(modelMetabolites.getWorkspace().getName(), metaboliteKeggID);
											fillList(jRadioButtonEncoded.isSelected());
										}
									}
									
								}
								else {

									Workbench.getInstance().warn("Please Select a Row!");
								}
							} 
							catch (Exception e) {
								Workbench.getInstance().error(e);
								e.printStackTrace();
							}
							
							
						}
					});
				}

				
//				{
//					ComboBoxModel<String> jComboBox1Model = 
//							new DefaultComboBoxModel<>(
//									new String[] { "Remove Row", "Remove All" });
//							jComboBoxRemove = new JComboBox<>();
//							jPanelRemove.add(jComboBoxRemove, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//							jComboBoxRemove.setBounds(8, 19, 107, 26);
//							jComboBoxRemove.setModel(jComboBox1Model);
//				}
			}
			
			
			{
				//jPanel2.add(searchInTable.addPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

				jPanel5 = new JPanel();
				GridBagLayout jPanel5Layout = new GridBagLayout();
				//jPanel2.add(jPanel5, new GridBagConstraints(0, 0, 1, 1, 0.0,
						//0.0, GridBagConstraints.CENTER,
						//GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanel5Layout.rowWeights = new double[] { 0.1 };
				jPanel5Layout.rowHeights = new int[] { 7 };
				jPanel5Layout.columnWeights = new double[] { 0.1, 0.0 };
				jPanel5Layout.columnWidths = new int[] { 15, 3 };
				jPanel5.setBorder(BorderFactory.createTitledBorder("search"));
				jPanel5.setLayout(jPanel5Layout);
				{
					// searchTextField = new JTextField();
					jPanel2.add(searchInTable.addPanel(),
							new GridBagConstraints(0, 0, 8, 1, 0.0, 0.0,
									GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(
											0, 0, 0, 0), 0, 0));
					
					
					// searchTextField.setBorder(BorderFactory.createCompoundBorder(
					// BorderFactory.createEtchedBorder(SoftBevelBorder.LOWERED),
					// null)
					// );
					//
					// searchTextField.addKeyListener(new KeyAdapter() {
					// @Override
					// public void keyTyped(KeyEvent evt) {
					// searchInTable(evt);
					// }
					// });

					// ComboBoxModel typeComboBoxModel = new
					// DefaultComboBoxModel(new String[]{"All", "Reagents",
					// "Products"});
					// typeComboBox = new JComboBox();
					// jPanel5.add(typeComboBox, new GridBagConstraints(1, 0, 1,
					// 1, 0.0, 0.0, GridBagConstraints.CENTER,
					// GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					// typeComboBox.setModel(typeComboBoxModel);

					jPanel2.add(jPanel4, new GridBagConstraints(4, 1, 1, 1,
							0.0, 0.0, GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
							0));
					jPanel4.setBounds(30, 26, 676, 34);
					{
						jButton1ExportTxt = new JButton();
						jPanel4.add(jButton1ExportTxt, new GridBagConstraints(
								0, 1, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.NONE,
								new Insets(0, 0, 0, 0), 0, 0));
						jButton1ExportTxt.setText("export file");
						jButton1ExportTxt
								.setToolTipText("Export to text file (xls)");
						jButton1ExportTxt.setIcon(new CreateImageIcon(
								new ImageIcon((getClass().getClassLoader()
										.getResource("icons/Download.png"))),
								0.1).resizeImageIcon());
						jButton1ExportTxt.setBounds(532, 72, 174, 38);
						jButton1ExportTxt
								.addActionListener(new ActionListener() {

									public void actionPerformed(ActionEvent arg0) {

										try {

											JFileChooser fc = new JFileChooser();
											fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
											fc.setDialogTitle("Select directory");
											int returnVal = fc
													.showOpenDialog(new JTextArea());

											if (returnVal == JFileChooser.APPROVE_OPTION) {

												File file = fc
														.getSelectedFile();
												String filePath = file
														.getAbsolutePath();
												Calendar cal = new GregorianCalendar();

												// Get the components of the
												// time
												int hour24 = cal.get(Calendar.HOUR_OF_DAY); // 0.23
												int min = cal.get(Calendar.MINUTE); // 0.59
												int day = cal.get(Calendar.DAY_OF_YEAR); // 0.365

												//System.out.println(reactantsProducts.getName().replace("\\", "_").replace("/", "_"));

												filePath += "/"
														+ modelMetabolites
																.getName().replace("\\", "_").replace("/", "_")
														+ "_"
														+ modelMetabolites
																.getWorkspace()
																.getName().replace("\\", "_").replace("/", "_")
														+ "_" + hour24 + "_"
														+ min + "_" + day
														+ ".xlsx";
												
												ExportToXLS.exportToXLS(filePath, dataTable, jTable);
												
												Workbench.getInstance().info("Data successfully exported.");
											}
										} catch (Exception e) {

											Workbench.getInstance().error(
													"An error occurred while performing this operation. Error "
															+ e.getMessage());
											e.printStackTrace();
										}
									}

								});
					}
				}
			}
			{
				jPanelReactions = new JPanel();
				jPanel2.add(jPanelReactions, new GridBagConstraints(6, 1, 1, 1,
						0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0,
						0));
				GridBagLayout jPanelReactionsLayout = new GridBagLayout();
				jPanelReactionsLayout.columnWidths = new int[] { 7, 7 };
				jPanelReactionsLayout.rowHeights = new int[] { 7, 7 };
				jPanelReactionsLayout.columnWeights = new double[] { 0.1, 0.1 };
				jPanelReactionsLayout.rowWeights = new double[] { 0.1, 0.1 };
				jPanelReactions.setBorder(BorderFactory
						.createTitledBorder("metabolites"));
				jPanelReactions.setLayout(jPanelReactionsLayout);
				{
					jRadioButtonEncoded = new JRadioButton();
					jPanelReactions.add(jRadioButtonEncoded,
							new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER,
									GridBagConstraints.BOTH, new Insets(0, 0,
											0, 0), 0, 0));
					jRadioButtonEncoded.setText("in model");
					jRadioButtonEncoded.setToolTipText("in model");
					jRadioButtonEncoded.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							fillList(true);
						}
					});
				}
				{
					jRadioAllReactions = new JRadioButton();
					jPanelReactions.add(jRadioAllReactions,
							new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER,
									GridBagConstraints.BOTH, new Insets(0, 0,
											0, 0), 0, 0));
					jRadioAllReactions.setText("all metabolites");
					jRadioAllReactions.setToolTipText("all metabolites");
					jRadioAllReactions.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							fillList(false);
							
						}
					});
				}

				buttonGroup2 = new ButtonGroup();
				buttonGroup2.add(jRadioButtonEncoded);
				jRadioButtonEncoded.setSelected(true);
				buttonGroup2.add(jRadioAllReactions);

			}
			{
				jRadioButton1 = new JRadioButton();
				jPanel3.add(jRadioButton1, new GridBagConstraints(0, 0, 1, 1,
						0.0, 0.0, GridBagConstraints.WEST,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jRadioButton1.setText("all");
			}
			{
				jRadioButton2 = new JRadioButton();
				jPanel3.add(jRadioButton2, new GridBagConstraints(0, 1, 1, 1,
						0.0, 0.0, GridBagConstraints.WEST,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jRadioButton2.setText("reactants");
				jRadioButton2.setForeground(Color.decode("#008000"));
			}
			{
				jRadioButton3 = new JRadioButton();
				jPanel3.add(jRadioButton3, new GridBagConstraints(0, 2, 1, 1,
						0.0, 0.0, GridBagConstraints.WEST,
						GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jRadioButton3.setText("products");
				jRadioButton3.setForeground(Color.decode("#0000FF"));
			}

			buttonGroup1 = new ButtonGroup();
			buttonGroup1.add(jRadioButton1);
			jRadioButton1.setSelected(true);
			jRadioButton1.setBounds(17, 22, 124, 18);
			jRadioButton1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					fillList(jRadioButtonEncoded.isSelected());
				}
			});
			buttonGroup1.add(jRadioButton2);
			jRadioButton2.setBounds(17, 45, 124, 18);
			jRadioButton2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					fillList(jRadioButtonEncoded.isSelected());
				}
			});
			buttonGroup1.add(jRadioButton3);
			jRadioButton3.setBounds(17, 70, 124, 18);
			jRadioButton3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					fillList(jRadioButtonEncoded.isSelected());
				}
			});

			jPanel1 = new JPanel();
			GridBagLayout thisLayout = new GridBagLayout();
			jPanel1.setLayout(thisLayout);
			this.add(jPanel1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			{
				jScrollPane1 = new JScrollPane();
				this.add(jScrollPane1, new GridBagConstraints(1, 1, 1, 1, 0.0,
						0.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					jTable = new MyJTable();
					jTable.setShowGrid(false);
					jScrollPane1.setViewportView(jTable);
				}
			}
			
			this.setPreferredSize(new java.awt.Dimension(887, 713));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void fillList(boolean encoded) {
		
		try {
			// 1 - get all
			// 2 - get reagents
			// 3 - get products
			
//		jTable = new MyJTable();
//		jTable.setShowGrid(false);
//		jScrollPane1.setViewportView(jTable);
			
			ArrayList<Integer> types = new ArrayList<>(Arrays.asList(0,0,0,0)); 
			int count = 0;
			
			//array structure
			// [Glycan , Compound , Drugs , All]
			
			if (jCheckBoxGlycan.isSelected()){
				types.set(0, 1);
				count++;
			}
			if (jCheckBoxCompound.isSelected()){
				types.set(1, 1);
				count++;
			}
			if (jCheckBoxDrugs.isSelected()){
				types.set(2, 1);
				count++;
			}
			if (jCheckBoxAll.isSelected()){
				types.set(3, 1);
				count++;
			}
			
			if(count==0)
				Workbench.getInstance().warn("please select an entry type");
			else{
				if (jRadioButton1.isSelected())
					dataTable = this.modelMetabolites.getMainTableData(1, encoded, types);
				else if (jRadioButton2.isSelected())
					dataTable = this.modelMetabolites.getMainTableData(2, encoded, types);
				else 	
					dataTable = this.modelMetabolites.getMainTableData(3, encoded, types);
			}
			
			jTable.setModel(dataTable);
			jTable.setSortableFalse();
			
			jTable.getColumnModel().getColumn(1).setPreferredWidth(200);
			jTable.getColumnModel().getColumn(3).setPreferredWidth(130);
			
			jTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

				private static final long serialVersionUID = 1L;

				public Component getTableCellRendererComponent(JTable table,
						Object value, boolean isSelected, boolean hasFocus,
						int row, int column) {
					
					this.setHorizontalAlignment( SwingUtilities.CENTER );

					int modelRow = jTable.convertRowIndexToModel(row);
					super.getTableCellRendererComponent(table, value, isSelected,
							hasFocus, modelRow, column);
					
					WorkspaceGenericDataTable dtt = (WorkspaceGenericDataTable) jTable.getModel();
					
					if (isSelected) {
						
						setBackground(new Color(237, 240, 242));
						setForeground(Color.BLACK);
					} 
					else if (encoded){
						if (dtt.getRowType(modelRow).equalsIgnoreCase("reactant")){
							setBackground(Color.decode("#90EE90"));}
						else if (dtt.getRowType(modelRow).equalsIgnoreCase("product"))
							setBackground(Color.decode("#87CEEB"));
						else if (dtt.getRowType(modelRow).equalsIgnoreCase("both"))
							setBackground(Color.decode("#778899"));
						
						setForeground(Color.decode("#000000"));
					}
					return this;
				}
			});			

			buttonColumn = new ButtonColumn(jTable, 0, new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
			

					processButton(arg0);
				}
			}, new MouseAdapter() {

				public void mouseClicked(MouseEvent e) {
					// {
					// get the coordinates of the mouse click
					Point p = e.getPoint();

					// get the row index that contains that coordinate
					int rowNumber = jTable.rowAtPoint(p);
					int columnNumber = jTable.columnAtPoint(p);
					jTable.setColumnSelectionInterval(columnNumber, columnNumber);
					// Get the ListSelectionModel of the MyJTable
					ListSelectionModel model = jTable.getSelectionModel();
					// set the selected interval of rows. Using the "rowNumber"
					// variable for the beginning and end selects only that one row.
					model.setSelectionInterval(rowNumber, rowNumber);
					processButton(e);
				}
			}, new ArrayList<>());
			
			this.jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.jTable.setRowSelectionAllowed(true);
			this.jScrollPane1.setViewportView(this.jTable);

			TableColumnModel tc = jTable.getColumnModel();
			tc.getColumn(0).setMaxWidth(35); // button
			tc.getColumn(0).setResizable(false);
			tc.getColumn(0).setModelIndex(0);
			
			jTable.setAutoCreateRowSorter(true);
			jTable.setSortableFalse();
			this.searchInTable.setMyJTable(jTable);
			this.searchInTable.setMainTableData(dataTable);
			this.searchInTable.setSearchTextField("");
		}
		catch (Exception e) {

			Workbench.getInstance().error(e);
			e.printStackTrace();
		}
	}

	/**
	 * @param arg0
	 * @throws Exception 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws NoSuchMethodException 
	 */
	private void processButton(EventObject arg0) {

		try {
			JButton button = null;

			if (arg0.getClass() == ActionEvent.class)
				button = (JButton) ((ActionEvent) arg0).getSource();

			if (arg0.getClass() == MouseEvent.class)
				button = (JButton) ((ActionEvent) arg0).getSource();
   
			ListSelectionModel model = jTable.getSelectionModel();
			model.setSelectionInterval(buttonColumn.getSelectIndex(button), buttonColumn.getSelectIndex(button));
			
			int metaboliteIdentifier = dataTable.getRowID(jTable.convertRowIndexToModel(jTable.getSelectedRow()));
			
			boolean refresh = (this.infoSelectedRow != jTable.getSelectedRow());
			
			WorkspaceDataTable[] table = modelMetabolites.getRowInfo(metaboliteIdentifier, refresh);
			
			this.infoSelectedRow = jTable.getSelectedRow();
			
			new GenericDetailWindow(table, dataTable.getWindowName(), "metabolite: " + (String) dataTable.getValueAt(jTable.convertRowIndexToModel(jTable.getSelectedRow()), 1));
		}
		catch (Exception e) {

			Workbench.getInstance().error(e);
			e.printStackTrace();
		}
		
	}

	/* (non-Javadoc)
	 * @see merlin_utilities.UpdateUI#updateGraphicalObject()
	 */
	@Override
	public void updateTableUI() {

		this.modelMetabolites.resetDataStuctures();
		this.fillList(this.jRadioButtonEncoded.isSelected());
		this.updateUI();
		this.revalidate();
		this.repaint();
	}

	/* (non-Javadoc)
	 * @see merlin_utilities.UpdateUI#addListenersToGraphicalObjects(javax.swing.JPanel, javax.swing.MyJTable)
	 */
	@Override
	public void addListenersToGraphicalObjects() {}
	
	private boolean discardData(String name, List<String> reactions) {

		int n = reactions.size();
		
		int i = CustomGUI.stopQuestion("Delete metabolite?",
				name + " is present in "+ n + " reactions. Do you wish continue?",
				new String[]{"Yes", "No"});
		
		switch (i)
		{
		case 0:return true;
		default:return false;
		}
	
	}
}
