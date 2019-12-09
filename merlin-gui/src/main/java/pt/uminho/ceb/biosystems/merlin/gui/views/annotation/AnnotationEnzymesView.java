package pt.uminho.ceb.biosystems.merlin.gui.views.annotation;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.annotation.AnnotationEnzymesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.jpanels.CustomGUI;
import pt.uminho.ceb.biosystems.merlin.gui.jpanels.InsertRemoveDataWindow;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.ButtonColumn;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.ComboBoxColumn;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.CreateImageIcon;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.LinkOut;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MyJTable;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.SearchInTable;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.StarColumn;
import pt.uminho.ceb.biosystems.merlin.gui.views.WorkspaceUpdatablePanel;
import pt.uminho.ceb.biosystems.merlin.gui.views.windows.GenericDetailWindowBlast;
import pt.uminho.ceb.biosystems.merlin.processes.annotation.AnnotationEnzymesProcesses;
import pt.uminho.ceb.biosystems.merlin.processes.annotation.remote.blast.WriteGBFile;
import pt.uminho.ceb.biosystems.merlin.services.annotation.AnnotationEnzymesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelMetabolitesServices;
import pt.uminho.ceb.biosystems.merlin.utilities.OpenBrowser;


/**
 * @author oDias
 *
 */
public class AnnotationEnzymesView extends WorkspaceUpdatablePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private JScrollPane jScrollPane;

	private JButton jButtonIntegration,
	saveButton,
	jButton1ExportXLS, 
	jButtonGbk; 

	private JLabel jLabeldAlpha, 
	jLabelAlphaText 
	,jLabelLowerThresholdText, 
	jLabelLowerThreshold,
	jLabelUpperThresholdText,
	jLabelUpperThreshold;

	private JPanel
	jPanel1, 
	jPanel2, 
	jPanel21,
	jPanel23, 
	commitPane;

	private MyJTable jTable;

	private WorkspaceGenericDataTable mainTableData;

	private AnnotationEnzymesAIB annotationEnzymes;

	private JFileChooser fc;

	private int //selectedModelRow, 
	infoSelectedRow,
	infoColumnNumber,
	uniprotStarColumnNumber,
	locus_tagColumnNumber,
	geneNameColumnNumber,
	namesColumnNumber, 
	namesScoreColumnNumber, 
	ecnumbersColumnNumber, 
	ecScoreColumnNumber, 
	notesColumnNumber,
	currentDatabaseIndex;

	private ComboBoxColumn productsColumn, 
	enzymesColumn;

	private ButtonColumn buttonColumn;

	private StarColumn buttonStarColumn;

	private Map<Integer, List<String>> getUniprotECnumbersTable;

	private MouseAdapter 
	namesMouseAdapter, 
	enzymesMouseAdapter, 
	buttonMouseAdapter, 
	starMouseAdapter, 
	tableMouseAdapator;

	private ItemListener 
	namesItemListener, 
	enzymesItemListener;

	private ActionListener buttonActionListener, 
	starActionListener;

	private PopupMenuListener namesPopupMenuListener, 
	enzymesPopupMenuListener;

	private SearchInTable searchInHomology;

	private TableModelListener tableModelListener;
	private List<Map<Integer, String>> itemsList;

	private String blastDatabase;  		


	/**
	 * @param homologyDataContainer
	 */
	public AnnotationEnzymesView(AnnotationEnzymesAIB homologyDataContainer) {

		super(homologyDataContainer);

		try {

		//	Connection connection = homologyDataContainer.getConnection();
			//Statement statement = connection.createStatement();

			this.blastDatabase = AnnotationEnzymesServices.getLastestUsedBlastDatabase(homologyDataContainer.getWorkspace().getName());
//			this.blastDatabase = "";
			
			homologyDataContainer.getCommitedScorerData(blastDatabase);

			this.annotationEnzymes = homologyDataContainer;

			if(this.annotationEnzymes.getCommittedAlpha()>-1) 
				AnnotationEnzymesProcesses.updateSettings(false, homologyDataContainer);
			else 
				AnnotationEnzymesProcesses.updateSettings(true, homologyDataContainer);

			this.mainTableData = this.annotationEnzymes.getAllGenes(this.blastDatabase, false);

			List<Integer> nameTabs = new ArrayList<>();
			String[] searchParams = {"name", "all", "notes"};
			this.searchInHomology= new SearchInTable(nameTabs,searchParams);

			this.initGUI();

			Rectangle visible = null;

			if(annotationEnzymes.getSelectedRow()>-1 && jTable.getRowCount()>0 && jTable.getRowCount()> annotationEnzymes.getSelectedRow())
				visible = this.jTable.getCellRect(annotationEnzymes.getSelectedRow(), -1, true);

			this.fillList(visible);

			if(annotationEnzymes.getSelectedRow()>-1 && jTable.getRowCount()>annotationEnzymes.getSelectedRow()) {

				this.jTable.setRowSelectionInterval(annotationEnzymes.getSelectedRow(), annotationEnzymes.getSelectedRow());
				this.jTable.scrollRectToVisible(this.jTable.getCellRect(annotationEnzymes.getSelectedRow(), -1, true));
			}

			AnnotationEnzymesServices.initializeScorerConfig(annotationEnzymes.getWorkspace().getName(), (float) this.annotationEnzymes.getThreshold().doubleValue(), (float) this.annotationEnzymes.getUpperThreshold().doubleValue(),
					(float) this.annotationEnzymes.getAlpha().doubleValue(), (float) this.annotationEnzymes.getBeta().doubleValue(), this.annotationEnzymes.getMinimumNumberofHits());
		}
		catch(Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void intitialiseTableColumns() {

		int number=0;
		this.infoColumnNumber = number++;
		this.locus_tagColumnNumber = number++;
		this.uniprotStarColumnNumber = number++;
		this.geneNameColumnNumber = number++;
		this.namesColumnNumber = number++;
		this.namesScoreColumnNumber = number++;
		this.ecnumbersColumnNumber = number++;
		this.ecScoreColumnNumber = number++;
		this.notesColumnNumber = number++;

		List<Integer> nameTabs = new ArrayList<>();
		nameTabs.add(locus_tagColumnNumber);
		nameTabs.add(geneNameColumnNumber);
		this.searchInHomology.setNameTabsAux(nameTabs, this.notesColumnNumber);

	}

	/**
	 * 
	 */
	private void addListeners() {

		this.addMouseListener();
		this.addTableModelListener();
		if(this.namesItemListener==null)
			this.namesItemListener = this.getComboBoxNamesItemListener();
		if(this.enzymesItemListener==null)
			this.enzymesItemListener = this.getComboBoxEnzymesItemListener();
		if(this.namesMouseAdapter==null)
			this.namesMouseAdapter = this.getComboBoxNamesMouseListener();
		if(this.enzymesMouseAdapter==null)
			this.enzymesMouseAdapter = this.getComboBoxEnzymesMouseListener();
		if(this.namesPopupMenuListener==null)
			this.namesPopupMenuListener = this.getComboBoxNamesPopupMenuListener();
		if(this.enzymesPopupMenuListener==null)
			this.enzymesPopupMenuListener = this.getComboBoxEnzymesPopupMenuListener();
		if(this.buttonActionListener==null)
			this.buttonActionListener = this.getButtonActionListener();
		if(this.buttonMouseAdapter==null)
			this.buttonMouseAdapter = this.getButtonMouseAdapter();
		if(this.starActionListener==null)
			this.starActionListener = this.getStarActionListener();
		if(this.starMouseAdapter==null)
			this.starMouseAdapter = this.getStarMouseAdapter();
	}

	/**
	 * initiate graphical user interface
	 */
	private void initGUI() {

		try {

			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
			thisLayout.columnWidths = new int[] {7, 7, 7};
			thisLayout.rowWeights = new double[] {0.0, 200.0, 0.0, 0.0, 0.0};
			thisLayout.rowHeights = new int[] {7, 50, 7, 3, 7};
			this.setLayout(thisLayout);
			this.setPreferredSize(new Dimension(875, 585));

			{
				jPanel1 = new JPanel();
				GridBagLayout jPanel1Layout = new GridBagLayout();
				jPanel1Layout.rowWeights = new double[] {0.1};
				jPanel1Layout.rowHeights = new int[] {7};
				jPanel1Layout.columnWeights = new double[] {0.1};
				jPanel1Layout.columnWidths = new int[] {7};
				jPanel1.setLayout(jPanel1Layout);
				this.add(jPanel1, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

				jScrollPane = new JScrollPane();
				jPanel1.add(jScrollPane, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jScrollPane.setPreferredSize(new java.awt.Dimension(7, 7));
				jScrollPane.setSize(900, 420);
			}
			{				
				jPanel2 = new JPanel();
				GridBagLayout jPanel2Layout = new GridBagLayout();
				jPanel2Layout.rowWeights = new double[] {0.0, 0.0};
				jPanel2Layout.rowHeights = new int[] {3, 3};
				jPanel2Layout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0, 0.1};
				jPanel2Layout.columnWidths = new int[] {7, 7, 7, 7, 7, 7, 7, 7};
				jPanel2.setLayout(jPanel2Layout);
				this.add(jPanel2, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					{
						{
							jPanel21 = new JPanel();
							jPanel2.add(jPanel21, new GridBagConstraints(3, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							GridBagLayout jPanel21Layout = new GridBagLayout();
							jPanel21.setBounds(14, 41, 294, 63);
							jPanel21.setBorder(BorderFactory.createTitledBorder("export"));
							jPanel21Layout.rowWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0};
							jPanel21Layout.rowHeights = new int[] {7, 20, 7, 20, 7};
							jPanel21Layout.columnWeights = new double[] {0.1};
							jPanel21Layout.columnWidths = new int[] {7};
							jPanel21.setLayout(jPanel21Layout);
							{
								jButton1ExportXLS = new JButton();
								jButton1ExportXLS.setText("export file");
								jButton1ExportXLS.setToolTipText("export to excel file (xls)");
								jPanel21.add(jButton1ExportXLS, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jButton1ExportXLS.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Download.png")),0.1).resizeImageIcon());
								jButton1ExportXLS.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent arg0) {

										fc.setDialogTitle("select directory");
										int returnVal = fc.showOpenDialog(new JTextArea());
										if (returnVal == JFileChooser.APPROVE_OPTION) {

											File file = fc.getSelectedFile();
											String path;
											if(file.isDirectory())
												path = file.getAbsolutePath();
											else
												path = file.getParentFile().getPath();

											exportToXls(exportAllData(),path);
										}
									}	
								});
							}
							{
								jButtonGbk = new JButton();
								jPanel21.add(jButtonGbk, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jButtonGbk.setText("genbank file");
								jButtonGbk.setToolTipText("update genbank file");
								jButtonGbk.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Download.png")),0.1).resizeImageIcon());
								jButtonGbk.addActionListener(new ActionListener(){
									public void actionPerformed(ActionEvent arg0) {
										try {

											saveGenbankFile();
										} 
										catch (IOException e) {

											e.printStackTrace();
										}
									}});
							}
						}
					}
				}
				{
					fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				}
				{
					jPanel23 = new JPanel();
					jPanel2.add(jPanel23, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					GridBagLayout jPanel21Layout = new GridBagLayout();
					jPanel23.setBounds(14, 41, 294, 63);
					jPanel23.setBorder(BorderFactory.createTitledBorder("scores parameters"));
					jPanel21Layout.rowWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0};
					jPanel21Layout.rowHeights = new int[] {7, 20, 7, 20, 7};
					jPanel21Layout.columnWeights = new double[] {10, 7, 7, 10};
					jPanel21Layout.columnWidths = new int[] {10, 7, 7, 10};
					jPanel23.setLayout(jPanel21Layout);

					{
						{
							jLabelLowerThreshold = new JLabel();
							jPanel23.add(jLabelLowerThreshold, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							jLabelLowerThreshold.setText(annotationEnzymes.getThreshold().toString());
							jLabelLowerThreshold.setToolTipText("this parameter is not editable");
							jLabelLowerThreshold.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED), null));
							jLabelLowerThreshold.setBounds(164, 57, 36, 20);
							jLabelLowerThreshold.setHorizontalAlignment(SwingConstants.CENTER);
						}
						{
							jLabelLowerThresholdText = new JLabel();
							jPanel23.add(jLabelLowerThresholdText, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							jLabelLowerThresholdText.setText("lower threshold:");
						}
						{
							jLabelUpperThreshold = new JLabel();
							jPanel23.add(jLabelUpperThreshold, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							jLabelUpperThreshold.setText(annotationEnzymes.getUpperThreshold().toString());
							jLabelUpperThreshold.setToolTipText("this parameter is not editable");
							jLabelUpperThreshold.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED), null));
							jLabelUpperThreshold.setBounds(164, 57, 36, 20);
							jLabelUpperThreshold.setHorizontalAlignment(SwingConstants.CENTER);
						}
						{
							jLabelUpperThresholdText = new JLabel();
							jPanel23.add(jLabelUpperThresholdText, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							jLabelUpperThresholdText.setText("upper threshold:");
						}

						{
							jLabeldAlpha = new JLabel();
							jPanel23.add(jLabeldAlpha, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							jLabeldAlpha.setText(this.annotationEnzymes.getAlpha().toString());
							jLabeldAlpha.setToolTipText("this parameter is not editable");
							jLabeldAlpha.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED),null));
							jLabeldAlpha.setBounds(164, 57, 36, 20);
							jLabeldAlpha.setHorizontalAlignment(SwingConstants.CENTER);

							jLabelAlphaText = new JLabel();
							jPanel23.add(jLabelAlphaText, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							jLabelAlphaText.setText("alpha value:");
						}


					}
				}
				{
					commitPane = new JPanel();
					GridBagLayout commitPaneLayout = new GridBagLayout();
					commitPane.setLayout(commitPaneLayout);
					commitPaneLayout.rowWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0};
					commitPaneLayout.rowHeights = new int[] {7, 7, 7, 20, 7};
					commitPaneLayout.columnWeights = new double[] {0.1};
					commitPaneLayout.columnWidths = new int[] {7};
					jPanel2.add(commitPane, new GridBagConstraints(5, 1, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					commitPane.setToolTipText("integration");
					commitPane.setBorder(BorderFactory.createTitledBorder("integration"));
					{
						saveButton = new JButton();
						commitPane.add(saveButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						saveButton.setText("save");
						saveButton.setToolTipText("save annotation to database");
						saveButton.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Save.png")),0.1).resizeImageIcon());
						saveButton.addActionListener(new ActionListener(){

							public void actionPerformed(ActionEvent arg0) {	

								Rectangle visible = jTable.getVisibleRect();

								try {
									annotationEnzymes.commitToDatabase(blastDatabase);

									if(AnnotationEnzymesServices.checkCommitedData(annotationEnzymes.getWorkspace().getName()))
										annotationEnzymes.setHasCommittedData();

									Workbench.getInstance().info("data successfully loaded into database!");
									fillList(visible);
								}
								catch(Exception e) {
									e.printStackTrace();
									Workbench.getInstance().error(e);
								}
							}});
					}
					{
						jButtonIntegration = new JButton();
						commitPane.add(jButtonIntegration, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jButtonIntegration.setText("integrate to model");
						jButtonIntegration.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Integrate.png")),0.1).resizeImageIcon());
						jButtonIntegration.addActionListener(new ActionListener() {

							public void actionPerformed(ActionEvent arg0) {	

								try {
									Rectangle visible = jTable.getVisibleRect();

									if(annotationEnzymes == null || annotationEnzymes.getInitialLocus().size()==0) {

										Workbench.getInstance().error("no homology information on the selected project!");
									}
									else if(!ModelMetabolitesServices.isMetabolicDataLoaded(getWorkspaceName())) {

										Workbench.getInstance().error("no metabolic information on the selected project!");
									}
									else {

										fillList(visible);

										for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
											if (def.getID().equals("operations.ModelEnzymesIntegration.ID")){

												Workbench.getInstance().executeOperation(def);
											}
										}

									}
								} 
								catch (Exception e) {
									Workbench.getInstance().error(e);
									e.printStackTrace();
								}
							}
						});
					}
				}
				{
					{
						JPanel searchPanel = searchInHomology.addPanel();
						jPanel2.add(searchPanel, new GridBagConstraints(1, 0, 6, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					}
					{
						JPanel setupPanel = new JPanel();
						GridBagLayout setupPanelPaneLayout = new GridBagLayout();
						setupPanel.setLayout(setupPanelPaneLayout);
						setupPanelPaneLayout.rowWeights = new double[] {0.0, 0.1};
						setupPanelPaneLayout.rowHeights = new int[] {7};
						setupPanelPaneLayout.columnWeights = new double[] {0.1};
						setupPanelPaneLayout.columnWidths = new int[] {7};
						jPanel2.add(setupPanel, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						setupPanel.setBorder(BorderFactory.createTitledBorder("view"));
						{
							String[] databases = null;

							try {

								databases = AnnotationEnzymesServices.getBlastDatabasesAsArrayIncludingEmpty(this.annotationEnzymes.getWorkspace().getName());

							}
							catch (Exception e) {
								Workbench.getInstance().error(e);
								e.printStackTrace();
							}

							JComboBox<String> option = new JComboBox<String>(new DefaultComboBoxModel<>(databases));
							setupPanel.add(option, new GridBagConstraints(0, 0, 0, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							option.setToolTipText("select database");
							option.setSelectedItem(this.blastDatabase);
							currentDatabaseIndex = option.getSelectedIndex();
							option.addActionListener(new ActionListener() {

								public void actionPerformed(ActionEvent arg0) {

									try {

										if(option.getSelectedIndex() != currentDatabaseIndex) {

											currentDatabaseIndex = option.getSelectedIndex();

											if(currentDatabaseIndex == 0)
												blastDatabase = "";
											else
												blastDatabase = option.getSelectedItem().toString();

											annotationEnzymes.getCommitedScorerData(blastDatabase);

											if(annotationEnzymes.getCommittedAlpha() > -1) {

												AnnotationEnzymesProcesses.updateSettings(false, annotationEnzymes);

												jLabelLowerThreshold.setText(annotationEnzymes.getThreshold().toString());
												jLabelUpperThreshold.setText(annotationEnzymes.getUpperThreshold().toString());
												jLabeldAlpha.setText(annotationEnzymes.getAlpha().toString());

											}
											else {

												AnnotationEnzymesProcesses.updateSettings(true, annotationEnzymes);
											}

											AnnotationEnzymesServices.updateScorerConfigSetLatest(annotationEnzymes.getWorkspace().getName(), false);
											AnnotationEnzymesServices.updateScorerConfigSetLatestByBlastDatabase(annotationEnzymes.getWorkspace().getName(), true, blastDatabase);

											updateTableUI(true);
										}

									} 
									catch (Exception e) {
										Workbench.getInstance().error(e);
										e.printStackTrace();
									}

								}});
						}
					}

				}
//				{
//					jPanel3 = new JPanel();
//					jPanel2.add(jPanel3, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//					GridBagLayout jPanel21Layout = new GridBagLayout();
//					jPanel3.setBounds(14, 41, 294, 63);
//					jPanel3.setBorder(BorderFactory.createTitledBorder("SamPler"));
//					jPanel21Layout.rowWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0};
//					jPanel21Layout.rowHeights = new int[] {7, 20, 7, 20, 7};
//					jPanel21Layout.columnWeights = new double[] {7};
//					jPanel21Layout.columnWidths = new int[] {7};
//
//					jPanel3.setLayout(jPanel21Layout);
//					{
//						jButtonReset = new JButton();
//						jPanel3.add(jButtonReset, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//						jButtonReset.setText("reset");
//						jButtonReset.setToolTipText("resets the scorer configurations");
//						jButtonReset.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Synchronize.png")),0.1).resizeImageIcon());
//						jButtonReset.addActionListener(new ActionListener() {
//							public void actionPerformed(ActionEvent arg0) {
//
//								if(resetScorer(blastDatabase)) {
//
//									AnnotationEnzymesProcesses.updateSettings(true, annotationEnzymes);
//
//									updateTableUI();
//
//									Workbench.getInstance().info("parameters successfully reset!");
//								}
//
//							}
//						});
//					}
//
//					{
//						jButtonAnnotation = new JButton();
//						jPanel3.add(jButtonAnnotation, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//						jButtonAnnotation.setText("selection");
//						jButtonAnnotation.setToolTipText("find best parameters for enzymes annotation");
//						jButtonAnnotation.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Synchronize.png")),0.1).resizeImageIcon());
//						jButtonAnnotation.addActionListener(new ActionListener() {
//
//							public void actionPerformed(ActionEvent arg0) {
//
//								//									double currentAlpha = homologyDataContainer.getAlpha();
//								//									double currentThreshold = homologyDataContainer.getThreshold();
//
//								try {
//									ParamSpec[] paramsSpec = new ParamSpec[]{
//											new ParamSpec("locusTagColumnNumber", Integer.class, locus_tagColumnNumber, null),
//											new ParamSpec("homologyDataContainer", AnnotationEnzymesAIB.class, annotationEnzymes, null),
//											new ParamSpec("ecnumbersColumnNumber", Integer.class, ecnumbersColumnNumber, null),
//											new ParamSpec("ecScoreColumnNumber", Integer.class, ecScoreColumnNumber, null),
//											new ParamSpec("sampleSize", Integer.class, 50, null),
//											new ParamSpec("itemsList", Map.class, itemsList.get(1), null),
//											new ParamSpec("blastDatabase", String.class, blastDatabase, null),
//											new ParamSpec("searchFile", Boolean.class, true, null),
//									};
//
//									for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
//										if (def.getID().equals("operations.AnnotationEnzymesParametersSetting.ID")){
//
//											Workbench.getInstance().executeOperation(def, paramsSpec);
//										}
//									}
//
//								} catch (IllegalArgumentException e) {
//									e.printStackTrace();
//								}
//
//								//									double bestAlpha = homologyDataContainer.getAlpha();
//								//									double bestThreshold = homologyDataContainer.getThreshold();
//
//								//									System.out.println(currentAlpha);
//								//									System.out.println(bestAlpha);
//								//									
//								//									if (currentAlpha != bestAlpha || currentThreshold != bestThreshold){
//								//										
//								//										System.out.println("yohh");
//								//										
//								//										Rectangle visible = jTable.getVisibleRect();
//								//										jTextFieldThreshold.setText(""+ bestThreshold);
//								//										jTextFieldAlpha.setText(" "+bestAlpha);
//								//										
//								//										mainTableData = homologyDataContainer.getAllGenes(blastDatabase, false);
//								//										jTable.setModel(mainTableData);
//								//										fillList(visible, true);
//								//									}
//								//									
//
//							}});
//					}
//				}
			}

			jTable = new MyJTable();
			jTable.setModel(mainTableData);
			jTable.setSortableFalse();
			jScrollPane.setViewportView(jTable);

		}
		catch(Exception e){

			e.printStackTrace();
		}
	}

	/**
	 *	fill entities lists 
	 * @param visible
	 * @param userInput
	 */
	public void fillList(Rectangle visible) {

		try {

			this.intitialiseTableColumns();

			int myRow = annotationEnzymes.getSelectedRow();

			jTable= new MyJTable();
			jTable.setShowGrid(false);
			jTable.setModel(mainTableData);
			jTable.setSortableFalse();
			itemsList = this.updateData();
			this.annotationEnzymes.setItemsList(this.itemsList);
			List<Integer> interProRows = this.annotationEnzymes.getInterProRows();

			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			this.addListeners();

			{
				this.buttonColumn = this.buildButtonColumn(this.infoColumnNumber, interProRows);

				if(this.getUniprotECnumbersTable == null) 
				{

					this.getUniprotECnumbersTable = this.getUniprotECnumbersTable(itemsList.get(2));
				}
				this.buttonStarColumn = this.buildStarColumn(this.uniprotStarColumnNumber, this.compareAnnotations(itemsList.get(1)));
				this.productsColumn = this.buildComboBoxColumn(this.namesColumnNumber, itemsList.get(0));
				this.enzymesColumn = this.buildComboBoxColumn(this.ecnumbersColumnNumber, itemsList.get(1));
			}

			this.jScrollPane.setViewportView(jTable);

			if(visible!=null) {

				//this.selectedRow = this.homologyDataContainer.getSelectedRow();

				this.jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

				if(myRow>-1 && jTable.getRowCount()>myRow)
					this.jTable.setRowSelectionInterval(myRow, myRow);

				scrollToVisible(visible);
			}

			this.setTableColumunModels();

			//			jTable.setAutoCreateRowSorter(true);
			//			jTable.getTableHeader().setEnabled(false);
			this.searchInHomology.setMyJTable(jTable);
			this.searchInHomology.setMainTableData(mainTableData);
			this.searchInHomology.setSearchTextField("");

		}
		catch (Exception e) {

			e.printStackTrace();
		}

	}


	/**
	 * update data lists 
	 * @return 
	 */
	private List<Map<Integer,String>> updateData() {

		try {
			
			if(this.annotationEnzymes.hasCommittedData())
				this.annotationEnzymes.getCommittedHomologyData();

			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			// commited product list
			if(this.annotationEnzymes.getCommittedProductList()!= null)
				for(int row : this.annotationEnzymes.getCommittedProductList().keySet())
					this.jTable.setValueAt(annotationEnzymes.getCommittedProductList().get(row), row, this.namesColumnNumber);

			//container product list
			for(int key : this.annotationEnzymes.getEditedProductData().keySet()) {

				if(this.annotationEnzymes.getTableRowIndex().containsKey(key)) {

					int row = annotationEnzymes.getTableRowIndex().get(key);
					this.jTable.setValueAt(annotationEnzymes.getEditedProductData().get(key), row, this.namesColumnNumber);
				}
			}

			///////////////////////////////////////////////////////////////////////////////////////////////////////

			// commited product item
			Map<Integer, String> mappedProdItem = this.annotationEnzymes.getInitialProdItem();

			if(this.annotationEnzymes.getCommittedProdItem()!= null) {

				for(int row : this.annotationEnzymes.getCommittedProdItem().keySet()) {

					if(this.annotationEnzymes.getCommittedProdItem().get(row)!=null && !this.annotationEnzymes.getCommittedProdItem().get(row).equalsIgnoreCase("null")) {

						mappedProdItem.put(row, this.annotationEnzymes.getCommittedProdItem().get(row));

						if(!annotationEnzymes.getProductList().containsKey(this.annotationEnzymes.getKeys().get(row))) {

							this.annotationEnzymes.getProductList().put((this.annotationEnzymes.getKeys().get(row)),mappedProdItem.get(row));
						}
					}
				}
			}

			// score 
			for(int key : this.annotationEnzymes.getProductList().keySet()) {

				if(this.annotationEnzymes.getTableRowIndex().containsKey(key)) {

					int row = annotationEnzymes.getTableRowIndex().get(key);

					mappedProdItem.put(row,this.annotationEnzymes.getProductList().get(key));

					String pdWeigth = this.annotationEnzymes.getProductPercentage(mappedProdItem.get(row), row);

					this.jTable.setValueAt(pdWeigth, row, this.namesScoreColumnNumber);

					this.annotationEnzymes.getProductList().put(key,mappedProdItem.get(row));
				}
			}

			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			if(this.annotationEnzymes.getCommittedEnzymeList()!= null) {

				for(int row : this.annotationEnzymes.getCommittedEnzymeList().keySet()) {

					//					for(String s : this.annotationEnzymes.getCommittedEnzymeList().get(row))
					//						System.out.println("\t"+row+"\t"+s);

					this.jTable.setValueAt(this.annotationEnzymes.getCommittedEnzymeList().get(row), row, this.ecnumbersColumnNumber);
				}
			}

			for(int key : this.annotationEnzymes.getEditedEnzymeData().keySet()) {

				if(this.annotationEnzymes.getTableRowIndex().containsKey(key)) {

					int row = annotationEnzymes.getTableRowIndex().get(key);

					//					for(String s : this.annotationEnzymes.getEditedEnzymeData().get(key))
					//						System.out.println(key+"\t"+row+"\t"+s);

					this.jTable.setValueAt(this.annotationEnzymes.getEditedEnzymeData().get(key), row, this.ecnumbersColumnNumber);
				}
			}

			Map<Integer, String> mappedEcItem = this.annotationEnzymes.getInitialEcItem();
			
			if(this.annotationEnzymes.getCommittedEcItem()!= null) {

				for(int row : this.annotationEnzymes.getCommittedEcItem().keySet()) {

					if(this.annotationEnzymes.getCommittedEcItem().get(row)!=null && !this.annotationEnzymes.getCommittedEcItem().get(row).equalsIgnoreCase("null")) {

						mappedEcItem.put(row, this.annotationEnzymes.getCommittedEcItem().get(row));

						int key = (this.annotationEnzymes.getKeys().get(row));

						if(!annotationEnzymes.getEnzymesList().containsKey(key))
							this.annotationEnzymes.getEnzymesList().put(key,mappedEcItem.get(row));
					}
				}
			}

			for(int key : this.annotationEnzymes.getEnzymesList().keySet()) {

				if(this.annotationEnzymes.getTableRowIndex().containsKey(key)) {

					int row = annotationEnzymes.getTableRowIndex().get(key);
					
					mappedEcItem.put(row,this.annotationEnzymes.getEnzymesList().get(key));
					
					String ecWeigth = this.annotationEnzymes.getECPercentage(mappedEcItem.get(row), row);

					//					if(!ecWeigth.equalsIgnoreCase("manual") && !ecWeigth.isEmpty() && Double.parseDouble(ecWeigth) < this.homologyDataContainer.getThreshold()) {
					//
					//						ecWeigth = "<"+this.homologyDataContainer.getThreshold();
					//					}
					this.jTable.setValueAt(ecWeigth, row, this.ecScoreColumnNumber);

					this.annotationEnzymes.getEnzymesList().put(key,mappedEcItem.get(row));
				}
			}

			//			for(int row :mappedEcItem.keySet()) {
			//				
			//				if(mappedEcItem.get(row) != null) {
			//					
			//					this.homologyDataContainer.getEnzymesList().put(Integer.parseInt(this.homologyDataContainer.getKeys().get(row)),mappedEcItem.get(row));
			//				}
			//			}


			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			Map<Integer, String> mappedLocusList = this.annotationEnzymes.getInitialLocus();

			if(this.annotationEnzymes.getCommittedLocusList() != null && jTable.getRowCount()>0) {

				for(int row : this.annotationEnzymes.getCommittedLocusList().keySet()) {

					if(this.annotationEnzymes.getCommittedLocusList().get(row)!=null && !this.annotationEnzymes.getCommittedLocusList().get(row).equalsIgnoreCase("null")) {

						mappedLocusList.put(row, this.annotationEnzymes.getCommittedLocusList().get(row));
						this.jTable.setValueAt(mappedLocusList.get(row), row, this.locus_tagColumnNumber);
					}
				}
			}

			for(int key : this.annotationEnzymes.getLocusList().keySet()) {

				if(this.annotationEnzymes.getTableRowIndex().containsKey(key)) {

					int row = annotationEnzymes.getTableRowIndex().get(key);
					mappedLocusList.put(row,this.annotationEnzymes.getLocusList().get(key));
					this.jTable.setValueAt(mappedLocusList.get(row), row, this.locus_tagColumnNumber);
				}
			}

			Map<Integer, String> mappedNamesList = this.annotationEnzymes.getInitialNames();

			if(this.annotationEnzymes.getCommittedNamesList() != null && jTable.getRowCount()>0) {

				for(int row : this.annotationEnzymes.getCommittedNamesList().keySet()) {

					if(this.annotationEnzymes.getCommittedNamesList().get(row)!=null && !this.annotationEnzymes.getCommittedNamesList().get(row).equalsIgnoreCase("null")) {

						mappedNamesList.put(row, this.annotationEnzymes.getCommittedNamesList().get(row));
						this.jTable.setValueAt(mappedNamesList.get(row), row, this.geneNameColumnNumber);
					}
				}
			}

			for(int key : this.annotationEnzymes.getNamesList().keySet()) {

				if(this.annotationEnzymes.getTableRowIndex().containsKey(key)) {

					int row = annotationEnzymes.getTableRowIndex().get(key);

					mappedNamesList.put(row,this.annotationEnzymes.getNamesList().get(key));

					this.jTable.setValueAt(mappedNamesList.get(row), row, this.geneNameColumnNumber);
				}
			}

			Map<Integer, String> mappedNotesMap = new TreeMap<Integer, String>();

			if(this.annotationEnzymes.getCommittedNotesMap() != null && jTable.getRowCount()>0) {

				for(int row : this.annotationEnzymes.getCommittedNotesMap().keySet()) {

					if(this.annotationEnzymes.getCommittedNotesMap().get(row) != null) {

						mappedNotesMap.put(row, this.annotationEnzymes.getCommittedNotesMap().get(row));
						this.jTable.setValueAt(mappedNotesMap.get(row), row, this.notesColumnNumber);
					}
				}
			}

			for(int key : this.annotationEnzymes.getNotesMap().keySet()) {

				if(this.annotationEnzymes.getTableRowIndex().containsKey(key)) {

					int row = annotationEnzymes.getTableRowIndex().get(key);

					mappedNotesMap.put(row,this.annotationEnzymes.getNotesMap().get(key));
					this.jTable.setValueAt(mappedNotesMap.get(row), row, this.notesColumnNumber);
				}
			}

			// prepate items for integration
			{
				this.annotationEnzymes.setIntegrationLocusList(mappedLocusList);
				this.annotationEnzymes.setIntegrationNamesList(mappedNamesList);
				this.annotationEnzymes.setIntegrationProdItem(mappedProdItem);
				this.annotationEnzymes.setIntegrationEcItem(mappedEcItem);
			}

			List<Map<Integer, String>> object = new ArrayList<Map<Integer,String>>();
			object.add(0,mappedProdItem);
			object.add(1,mappedEcItem);
			object.add(2,mappedLocusList);

			return object;
		}
		catch (Exception e) {

			this.jTable.setModel(this.mainTableData);
			annotationEnzymes.setSelectedRow(-1);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 */
	private void addMouseListener() {

		if(this.tableMouseAdapator==null)
			this.tableMouseAdapator = this.getTableMouseAdapator();

		jTable.addMouseListener(this.tableMouseAdapator);

	}

	/**
	 * @return
	 */
	private MouseAdapter getTableMouseAdapator() {

		MouseAdapter mouseAdapter = new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {

				annotationEnzymes.setSelectedRow(jTable.convertRowIndexToModel(jTable.getSelectedRow()));

				if(jTable.getSelectedRow()>-1 && jTable.getRowCount()>0 && jTable.getRowCount()> jTable.getSelectedRow()) {

					jTable.setRowSelectionInterval(jTable.getSelectedRow(), jTable.getSelectedRow());
					scrollToVisible(jTable.getCellRect(jTable.getSelectedRow(), -1, true));
				}

				int selectedColumn=-1;
				{
					Point p = arg0.getPoint();
					int  columnNumber = jTable.columnAtPoint(p);
					jTable.setColumnSelectionInterval(columnNumber, columnNumber);
					selectedColumn=columnNumber;
				}

				//EC number popup
				if(selectedColumn==(Integer.valueOf(ecScoreColumnNumber))) {

					annotationEnzymes.setSelectedRow(jTable.convertRowIndexToModel(jTable.getSelectedRow()));

					new InsertRemoveDataWindow(annotationEnzymes ,"scores description", true) {

						private static final long serialVersionUID = -1;
						public void finishClose() {

							Rectangle visible = jTable.getVisibleRect();

							this.setVisible(false);
							this.dispose();
							fillList(visible);
						}						
					};
				}

				// products popup
				if(selectedColumn==namesScoreColumnNumber) {

					annotationEnzymes.setSelectedRow(jTable.convertRowIndexToModel(jTable.getSelectedRow()));

					new InsertRemoveDataWindow(annotationEnzymes, "scores description", false) {

						private static final long serialVersionUID = -1;

						public void finishClose() {

							Rectangle visible = jTable.getVisibleRect();

							this.setVisible(false);
							this.dispose();
							fillList(visible);
						}						
					};
				}

				//genes Linkout
				if(selectedColumn==locus_tagColumnNumber || selectedColumn==geneNameColumnNumber) {

					if(arg0.getButton()==MouseEvent.BUTTON3 && jTable.getSelectedRow()>0) {

						List<Integer> dbs = new ArrayList<Integer>();
						dbs.add(0);
						dbs.add(1);
						new LinkOut(dbs, (String)jTable.getValueAt(jTable.getSelectedRow(), selectedColumn)).show(arg0.getComponent(),arg0.getX(), arg0.getY());
					}
				}

				//proteins linkout
				if(selectedColumn==namesColumnNumber || selectedColumn==ecnumbersColumnNumber) {

					if(arg0.getButton()==MouseEvent.BUTTON3 && jTable.getSelectedRow()>0) {

						List<Integer> dbs = new ArrayList<Integer>();

						String text=null;
						if(selectedColumn==namesColumnNumber) {

							dbs.add(1);
							dbs.add(2);
							text=productsColumn.getSelectItem(annotationEnzymes.getSelectedRow());
						}

						if(selectedColumn==ecnumbersColumnNumber) {
							dbs.add(1);
							dbs.add(3);
							text=enzymesColumn.getSelectItem(jTable.getSelectedRow());
						}

						if(text!=null) {

							new LinkOut(dbs, text).show(arg0.getComponent(),arg0.getX(), arg0.getY());
						}
					}
				}
			}
		};
		return mouseAdapter;
	}

	/**
	 * 
	 */
	private void addTableModelListener() {

		if(this.tableModelListener == null)
			this.tableModelListener = this.getTableModelListener();

		jTable.getModel().addTableModelListener(this.tableModelListener);

	}

	/**
	 * @return
	 */
	private TableModelListener getTableModelListener() {

		TableModelListener tableModelListener = new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {

				if(jTable.getSelectedRow()>-1) {

					annotationEnzymes.setSelectedRow(jTable.convertRowIndexToModel(jTable.getSelectedRow()));
					int key = (annotationEnzymes.getKeys().get(jTable.getSelectedRow()));
					annotationEnzymes.getLocusList().put(key , (String) jTable.getValueAt(jTable.getSelectedRow(), locus_tagColumnNumber));
					annotationEnzymes.getNamesList().put(key , (String) jTable.getValueAt(jTable.getSelectedRow() , geneNameColumnNumber));
					annotationEnzymes.getNotesMap().put(key , (String) jTable.getValueAt(jTable.getSelectedRow(), notesColumnNumber));
				}
			}};
			return tableModelListener;
	}

	/**
	 * 
	 */
	private void setTableColumunModels() {

		TableColumnModel tc = jTable.getColumnModel();
		tc.getColumn(infoColumnNumber).setMaxWidth(35);				
		tc.getColumn(infoColumnNumber).setResizable(false);
		tc.getColumn(infoColumnNumber).setModelIndex(infoColumnNumber);

		tc.getColumn(uniprotStarColumnNumber).setMaxWidth(50);				
		tc.getColumn(uniprotStarColumnNumber).setResizable(false);
		tc.getColumn(uniprotStarColumnNumber).setModelIndex(uniprotStarColumnNumber);

		tc.getColumn(locus_tagColumnNumber).setMinWidth(120);
		tc.getColumn(locus_tagColumnNumber).setModelIndex(locus_tagColumnNumber);

		tc.getColumn(geneNameColumnNumber).setMinWidth(100);
		tc.getColumn(geneNameColumnNumber).setModelIndex(geneNameColumnNumber);

		tc.getColumn(namesColumnNumber).setMinWidth(210);
		tc.getColumn(namesColumnNumber).setModelIndex(namesColumnNumber);

		tc.getColumn(namesScoreColumnNumber).setMinWidth(90);
		tc.getColumn(namesScoreColumnNumber).setMaxWidth(120);
		tc.getColumn(namesScoreColumnNumber).setModelIndex(namesScoreColumnNumber);

		tc.getColumn(ecnumbersColumnNumber).setMinWidth(135);
		tc.getColumn(ecnumbersColumnNumber).setModelIndex(ecnumbersColumnNumber);

		tc.getColumn(ecScoreColumnNumber).setMinWidth(90);
		tc.getColumn(ecScoreColumnNumber).setMaxWidth(120);
		tc.getColumn(ecScoreColumnNumber).setModelIndex(ecScoreColumnNumber);

		tc.getColumn(notesColumnNumber).setResizable(true);
		tc.getColumn(notesColumnNumber).setModelIndex(notesColumnNumber);

		jTable.setColumnModel(tc);
		jTable.setRowHeight(20);
		jTable.setAutoResizeMode(MyJTable.AUTO_RESIZE_ALL_COLUMNS);
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean discardData() {

		int i =CustomGUI.stopQuestion("Discard manually selected data?",
				"Do you wish to discard all the edited information?" +
						"\n(If you select 'No' edited data will available for later use)",
						new String[]{"Yes", "No", "Info"});
		if(i<2)
		{
			switch (i)
			{
			case 0:return true;
			default:return false;
			}
		}
		else
		{
			Workbench.getInstance().warn(
					"If you discard the edited information, all previously selected genes, enzymes and gene products" +
							"\nwill be returned their default alpha values, as well as edited gene names, identifiers (locus tag)" +
							"\nand chromosomes (if available)."+
							"\nUser inserted data such as ec numbers or product names unavailable on Blast for a certain gene," +
							"\nwill also be discarded, as well as deleted ec numbers and products."+
							"\nIf you do not discard the edited data, you can revert to your previously reviewed information" +
					"\nwhen selecting the 'Manual Selection'.");
			return discardData();
		}
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	private boolean resetScorer(String blastDatabase) throws Exception {

		int i;

		if(blastDatabase.equals("")) {
			i =CustomGUI.stopQuestion("Reset", 
					"Reset score configurations for this database or for all databases?", 
					new String[]{"Yes for all", "Yes", "No"});

		}
		else {
			i =CustomGUI.stopQuestion("Reset", 
					"Reset score just for " + blastDatabase +" or for all databases?",
					new String[]{"Yes for all", "just for "+ blastDatabase, "No"});
		}

		try {
			switch (i)
			{
			case 0:
			{
				AnnotationEnzymesServices.resetAllScorers(this.getWorkspaceName());
				return true;
			}
			case 1:
			{
				AnnotationEnzymesServices.resetDatabaseScorer(this.getWorkspaceName(), blastDatabase);
				return true;
			}
			default:
			{
				return false;
			}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param i
	 * @return
	 */
	private ButtonColumn buildButtonColumn(final int i, List<Integer> interProRows) {

		return new ButtonColumn(jTable, i, this.buttonActionListener, this.buttonMouseAdapter, interProRows);
	}

	/**
	 * @return
	 */
	private ActionListener getButtonActionListener() {

		return new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				processButton(arg0);}
		};
	}

	/**
	 * @return
	 */
	private MouseAdapter getButtonMouseAdapter() {

		return new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				Point p = e.getPoint();

				int  columnNumber = jTable.columnAtPoint(p);
				jTable.setColumnSelectionInterval(columnNumber, columnNumber);
				annotationEnzymes.setSelectedRow(jTable.convertRowIndexToModel(jTable.getSelectedRow()));
				processButton(e);
			}
		};
	}

	/**
	 * @param i
	 * @param starsColorMap 
	 * @return
	 */
	private StarColumn buildStarColumn(final int i, Map<Integer, Integer> starsColorMap) {

		return new StarColumn(jTable, i, this.starActionListener, this.starMouseAdapter, starsColorMap);
	}

	/**
	 * @return
	 */
	private ActionListener getStarActionListener() {

		return new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				processStarButton(arg0);
			}
		};
	}

	/**
	 * @return
	 */
	private MouseAdapter getStarMouseAdapter() {

		return new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				Point p = e.getPoint();
				int  columnNumber = jTable.columnAtPoint(p);
				jTable.setColumnSelectionInterval(columnNumber, columnNumber);
				annotationEnzymes.setSelectedRow(jTable.convertRowIndexToModel(jTable.getSelectedRow()));
				processStarButton(e);
			}
		};
	}

	/**
	 * @param column
	 * @param items
	 * @return
	 */
	private ComboBoxColumn buildComboBoxColumn(final int column, Map<Integer,String> items) {


		if(column == this.namesColumnNumber)
			return  new ComboBoxColumn(jTable, column, items , this.namesItemListener, this.namesMouseAdapter, this.namesPopupMenuListener);
		else
			return  new ComboBoxColumn(jTable, column, items , this.enzymesItemListener, this.enzymesMouseAdapter, this.enzymesPopupMenuListener);
	}

	/**
	 * @return
	 */
	private ItemListener getComboBoxNamesItemListener() {

		return new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {

				processProductComboBoxChange(e);
			}
		};
	}

	/**
	 * @return
	 */
	private PopupMenuListener getComboBoxNamesPopupMenuListener() {

		return new PopupMenuListener() {

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

				processProductComboBoxChange(e);
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {

			}
		};
	}

	/**
	 * @return
	 */
	private ItemListener getComboBoxEnzymesItemListener() {

		return new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {

				processEnzymesComboBoxChange(e);
			}
		};
	}

	/**
	 * @return
	 */
	private PopupMenuListener getComboBoxEnzymesPopupMenuListener() {

		return new PopupMenuListener() {

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

				processEnzymesComboBoxChange(e);
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {

			}
		};
	}

	/**
	 * @return
	 */
	private MouseAdapter getComboBoxNamesMouseListener() {

		return new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				Point p = e.getPoint();

				int  columnNumber = jTable.columnAtPoint(p);
				jTable.setColumnSelectionInterval(columnNumber, columnNumber);

				annotationEnzymes.setSelectedRow(jTable.convertRowIndexToModel(jTable.getSelectedRow()));

				int myRow = jTable.getSelectedRow();

				if(myRow>-1 && jTable.getRowCount()>0 && jTable.getRowCount()> myRow) {

					jTable.setRowSelectionInterval(myRow, myRow);
					scrollToVisible(jTable.getCellRect(myRow, -1, true));
				}

				processProductComboBoxChange(e);
			}
		};		
	}

	/**
	 * @return
	 */
	private MouseAdapter getComboBoxEnzymesMouseListener() {

		return new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				Point p = e.getPoint();

				int  columnNumber = jTable.columnAtPoint(p);
				jTable.setColumnSelectionInterval(columnNumber, columnNumber);

				annotationEnzymes.setSelectedRow(jTable.convertRowIndexToModel(jTable.getSelectedRow()));

				int myRow = jTable.getSelectedRow();

				if(myRow>-1 && jTable.getRowCount()>0 && jTable.getRowCount()> myRow) {

					jTable.setRowSelectionInterval(myRow, myRow);
					scrollToVisible(jTable.getCellRect(myRow, -1, true));
				}

				processEnzymesComboBoxChange(e);
			}
		};		
	}

	/**
	 * @param e
	 */
	@SuppressWarnings("unchecked")
	private void processProductComboBoxChange(EventObject e) {

		boolean go = false;
		JComboBox<String> comboBox = null;

		if(e.getClass()==MouseEvent.class) {

			Object obj = ((MouseEvent) e).getSource();

			if(obj instanceof JComboBox) {

				comboBox = (JComboBox<String>) obj;
			}

			ListSelectionModel model = jTable.getSelectionModel();
			model.setSelectionInterval( productsColumn.getSelectIndex(comboBox), productsColumn.getSelectIndex(comboBox));

			if(comboBox != null)
				go = true;

			if(((MouseEvent) e).getButton()==MouseEvent.BUTTON3 ) {

				List<Integer> dbs = new ArrayList<Integer>();

				String text=null;

				dbs.add(1);
				dbs.add(2);
				text=comboBox.getSelectedItem().toString();

				if(text!=null) 
					new LinkOut(dbs, text).show(((MouseEvent) e).getComponent(),((MouseEvent) e).getX(), ((MouseEvent) e).getY());
			}
		}
		else if((e.getClass()==ItemEvent.class && ((ItemEvent) e).getStateChange() == ItemEvent.SELECTED) ) {

			Object obj = ((ItemEvent) e).getSource();

			if(obj instanceof JComboBox)
				comboBox = (JComboBox<String>) obj;

			if(comboBox != null)
				go = true;

		}

		else if(e.getClass() == PopupMenuEvent.class) {

			Object obj = ((PopupMenuEvent) e).getSource();

			if(obj instanceof JComboBox)
				comboBox = (JComboBox<String>) obj;

			if(comboBox != null)
				go = true;

		}

		if(go) {

			this.annotationEnzymes.setSelectedRow(jTable.convertRowIndexToModel(productsColumn.getSelectIndex(comboBox)));

			if(annotationEnzymes.getSelectedRow() < 0)
				annotationEnzymes.setSelectedRow(jTable.convertRowIndexToModel(Integer.parseInt(comboBox.getName())));

			String selectedItem = (String) comboBox.getSelectedItem();

			if(annotationEnzymes.getSelectedRow()>-1 && productsColumn.getValues().containsKey(annotationEnzymes.getSelectedRow()) && !selectedItem.trim().equals(productsColumn.getValues().get(annotationEnzymes.getSelectedRow()))) {

				int row = this.annotationEnzymes.getSelectedRow();
				this.updateProductsComboBox(comboBox, selectedItem, row);
			}
		}
	}

	/**
	 * @param comboBox
	 * @param selectedItem
	 * @param row
	 */
	private void updateProductsComboBox(JComboBox<String> comboBox, String selectedItem, int row ) {

		comboBox.setToolTipText((String) comboBox.getSelectedItem());
		this.productsColumn.getValues().put(row , selectedItem);

		String pdWeigth = this.annotationEnzymes.getProductPercentage(selectedItem, row );
		this.jTable.setValueAt(pdWeigth, row , namesScoreColumnNumber);
		int key = (annotationEnzymes.getKeys().get(row));
		this.annotationEnzymes.getProductList().put(key, selectedItem);
	}

	/**
	 * @param e
	 */
	@SuppressWarnings("unchecked")
	private void processEnzymesComboBoxChange(EventObject e) {

		boolean go = false;
		JComboBox<String> comboBox = null;

		if(e.getClass()==MouseEvent.class) {

			Object obj = ((MouseEvent) e).getSource();

			if(obj instanceof JComboBox)
				comboBox = (JComboBox<String>) obj;

			ListSelectionModel model = jTable.getSelectionModel();
			model.setSelectionInterval( enzymesColumn.getSelectIndex(comboBox), enzymesColumn.getSelectIndex(comboBox));

			if(((MouseEvent) e).getButton()==MouseEvent.BUTTON3 ) {

				List<Integer> dbs = new ArrayList<Integer>();

				String text=null;

				dbs.add(1);
				dbs.add(3);
				text=comboBox.getSelectedItem().toString();

				if(text!=null) 
					new LinkOut(dbs, text).show(((MouseEvent) e).getComponent(),((MouseEvent) e).getX(), ((MouseEvent) e).getY());
			}
		}

		else if((e.getClass()==ItemEvent.class && ((ItemEvent) e).getStateChange() == ItemEvent.SELECTED) ) {

			Object obj = ((ItemEvent) e).getSource();

			if(obj instanceof JComboBox) 
				comboBox = (JComboBox<String>) obj;

			if(comboBox != null) 
				go = true;
		}

		else if(e.getClass() == PopupMenuEvent.class) {

			Object obj = ((PopupMenuEvent) e).getSource();

			if(obj instanceof JComboBox) 
				comboBox = (JComboBox<String>) obj;

			if(comboBox != null) 
				go = true;
		}

		if(go) {


			annotationEnzymes.setSelectedRow(jTable.convertRowIndexToModel(enzymesColumn.getSelectIndex(comboBox)));

			if(annotationEnzymes.getSelectedRow() < 0)
				annotationEnzymes.setSelectedRow(jTable.convertRowIndexToModel(Integer.parseInt(comboBox.getName())));

			String selectedItem = (String) comboBox.getSelectedItem();

			if(annotationEnzymes.getSelectedRow()>-1 && enzymesColumn.getValues().containsKey(annotationEnzymes.getSelectedRow()) && !selectedItem.trim().equalsIgnoreCase(enzymesColumn.getValues().get(annotationEnzymes.getSelectedRow()))) {

				int row = this.annotationEnzymes.getSelectedRow();
				this.updateEnzymesComboBox(comboBox, selectedItem, row );
			}
		}
	}

	/**
	 * @param comboBox
	 * @param selectedItem
	 * @param row
	 */
	private void updateEnzymesComboBox(JComboBox<String> comboBox, String selectedItem, int row ) {

		List<String> merlin_ecs = new ArrayList<String>();
		String[] ecs = ((String) comboBox.getSelectedItem()).split(",");

		for(String ec : ecs)
			merlin_ecs.add(ec.trim());

		int result = -10;
		if(this.getUniprotECnumbersTable.containsKey(row)) {

			List<String> uniprot_ecs = new ArrayList<String>(this.getUniprotECnumbersTable.get(row));
			result = this.compareAnnotationsLists(merlin_ecs, uniprot_ecs);
		}
		else {

			if(!merlin_ecs.get(0).equalsIgnoreCase("null") && !merlin_ecs.get(0).equalsIgnoreCase(""))
				result = 0;
		}

		//this.buttonStarColumn.getValueArray().get(row).setBackground(StarColumn.getBackgroundColor(result));
		boolean reviewed = this.buttonStarColumn.getStarsReviewedMap().get(row);
		String path = StarColumn.getBackgroundColor(reviewed,result);
		this.buttonStarColumn.getValueArray().get(row).setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource(path)),0.08).resizeImageIcon());
		this.buttonStarColumn.getValueArray().get(row).updateUI();
		this.buttonStarColumn.getValueArray().get(row).repaint();
		this.buttonStarColumn.getValueArray().get(row).paintImmediately(this.buttonStarColumn.getValueArray().get(row).getBounds());

		comboBox.setToolTipText((String) comboBox.getSelectedItem());
		this.enzymesColumn.getValues().put(row, selectedItem);

		String ecWeigth = annotationEnzymes.getECPercentage(selectedItem,row);
		if(selectedItem.isEmpty())
			ecWeigth = "";

		jTable.setValueAt(ecWeigth, row, ecScoreColumnNumber);
		//		jTable.setValueAt(new Boolean(true), row, selectColumnNumber);
		//		jRadioButtonManSel.setSelected(true);
		int key = (annotationEnzymes.getKeys().get(row));
		annotationEnzymes.getEnzymesList().put(key, selectedItem);
		//		homologyDataContainer.getSelectedGene().put(key , true);
	}

	/**
	 * @param arg0
	 */
	private void processButton(EventObject arg0) {

		try {
			JButton button = null;
			if(arg0.getClass()==ActionEvent.class) {

				button = (JButton)((ActionEvent) arg0).getSource();
				ListSelectionModel model = jTable.getSelectionModel();

				int row = buttonColumn.getSelectIndex(button);
				model.setSelectionInterval(row, row);
			}		
			else if(arg0.getClass()==MouseEvent.class) {

				Point p = ((MouseEvent) arg0).getPoint();
				int  columnNumber = jTable.columnAtPoint(p);
				jTable.setColumnSelectionInterval(columnNumber, columnNumber);
				button = (JButton) buttonColumn.getValueArray().get(jTable.getSelectedRow());
				annotationEnzymes.setSelectedRow(jTable.convertRowIndexToModel(jTable.getSelectedRow()));
			}

			if(button!=null) {

				int row = buttonColumn.getSelectIndex(button);
				annotationEnzymes.setSelectedRow(jTable.convertRowIndexToModel(jTable.getSelectedRow()));

				int enzymesAnnotationIdentifier = annotationEnzymes.getKeys().get(row);

				boolean refresh = (this.infoSelectedRow != jTable.getSelectedRow());

				new GenericDetailWindowBlast(annotationEnzymes.getRowInfo(enzymesAnnotationIdentifier, refresh), "enzymes annotation", "gene: " + annotationEnzymes.getGeneLocus(row));

				this.infoSelectedRow = jTable.getSelectedRow();

				if(jTable.isEditing())
					jTable.getCellEditor().stopCellEditing();
			}
		}
		catch (Exception e) {
			Workbench.getInstance().error(e);
			e.printStackTrace();
		}
	}

	/**
	 * @param arg0
	 */
	private void processStarButton(EventObject arg0) {

		JButton button = null;
		if(arg0.getClass()==ActionEvent.class) {

			button = (JButton)((ActionEvent) arg0).getSource();
			ListSelectionModel model = jTable.getSelectionModel();
			int row = buttonColumn.getSelectIndex(button);
			model.setSelectionInterval(row, row);

		}		
		else if(arg0.getClass()==MouseEvent.class) {

			Point p = ((MouseEvent) arg0).getPoint();
			int  columnNumber = jTable.columnAtPoint(p);
			jTable.setColumnSelectionInterval(columnNumber, columnNumber);
			button = (JButton) buttonStarColumn.getValueArray().get(jTable.getSelectedRow());
			annotationEnzymes.setSelectedRow(jTable.convertRowIndexToModel(jTable.getSelectedRow()));
		}

		if(button!=null) {

			OpenBrowser  openUrl = new OpenBrowser();
			openUrl.setUrl("http://www.uniprot.org/uniprot/?query="+(String)jTable.getValueAt(jTable.getSelectedRow(), locus_tagColumnNumber)+"&sort=score");
			openUrl.openURL();
			if(jTable.isEditing())
				jTable.getCellEditor().stopCellEditing();
		}
	}

	/**
	 * @return
	 */
	private boolean exportAllData() {

		int i =CustomGUI.stopQuestion("Export all available data?",
				"Do you wish to export all information, including the data available inside the dropdown boxes?",
				new String[]{"Yes", "No", "Info"});
		if(i<2) {

			switch (i)
			{
			case 0:return true;
			default:return false;
			}
		}
		else {

			Workbench.getInstance().warn("If you select 'No' only data selected on the dropdown boxes will be exported.\n" +
					"If you select yes all homology data, including the one inside dropdown boxes, will be exported.");
			return exportAllData();
		}
	}

	/**
	 * @param path
	 * 
	 * Export Data to xls tabbed files
	 * 
	 */
	public void exportToXls(boolean allData, String path) {

		String excelFileName = System.getProperty("user.home");

		if(!path.equals("")) {

			excelFileName=path;
		}

		Calendar cal = new GregorianCalendar();

		// Get the components of the time
		int hour24 = cal.get(Calendar.HOUR_OF_DAY);     // 0..23
		int min = cal.get(Calendar.MINUTE);             // 0..59
		int day = cal.get(Calendar.DAY_OF_YEAR);		//0..365

		String db = this.blastDatabase+"_";

		if(this.blastDatabase.isEmpty())
			db = "all_";

		excelFileName+="/homologyData_"+annotationEnzymes.getWorkspace().getName()+"_"+db+hour24+"_"+min+"_"+day+".xlsx";

		try {

			String sheetName = "Enzymes Annotation";//name of sheet

			Workbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet(sheetName) ;

			TableColumnModel tc = jTable.getColumnModel();
			int headerSize = tc.getColumnCount();
			int h = 1; // skip info column

			Row row = sheet.createRow(0);

			while (h < headerSize) {

				row.createCell(h-1).setCellValue(tc.getColumn(h).getHeaderValue().toString());
				h++;
			}

			int p = 3, pS = 4, e = 5, eS = 6;

			int addrow = 1;

			for(int r=0; r < mainTableData.getTable().size(); r++) {

				String[] productsToXLS = null, enzymesToXLS = null;
				String pdWeigth, ecWeigth;
				boolean existsProduct=false, existsEnzyme=false;
				String product=null, enzyme=null;

				row = sheet.createRow(addrow);

				for (int j=1; j < mainTableData.getRow(r).length; j++) {

					if(j==namesColumnNumber && mainTableData.getRow(r)[j].getClass()==String[].class) {

						productsToXLS = (String[])mainTableData.getRow(r)[j]; 

						product = this.productsColumn.getSelectItem(r);

						row.createCell(j-1).setCellValue(product);

						productsToXLS = this.removeElement(productsToXLS, product);
						existsProduct=true;
					}
					else if(j==namesScoreColumnNumber && existsProduct) {

						existsProduct=false;
						row.createCell(j-1).setCellValue(annotationEnzymes.getProductPercentage(product,r));
						product=null;
					}
					else if(j==ecnumbersColumnNumber && mainTableData.getRow(r)[j].getClass()==String[].class) {

						enzymesToXLS = (String[])mainTableData.getRow(r)[j];
						enzyme = this.enzymesColumn.getSelectItem(r);

						if(enzyme!=null) {

							row.createCell(j-1).setCellValue(enzyme);
							enzymesToXLS = this.removeElement(enzymesToXLS, enzyme);
							existsEnzyme=true;
						}
					}
					else if(j==ecScoreColumnNumber && existsEnzyme) {

						existsEnzyme=false;
						row.createCell(j-1).setCellValue(annotationEnzymes.getECPercentage(enzyme,r)+"");
						enzyme=null;
					}
					else {

						if(j==locus_tagColumnNumber) {

							row.createCell(j-1).setCellValue(mainTableData.getRow(r)[j]+"");
						}
						else if(j==notesColumnNumber ) {

							row.createCell(j-1).setCellValue(mainTableData.getRow(r)[j]+"");
						}
						else if(j==uniprotStarColumnNumber) {

							String text = mainTableData.getRow(r)[j]+"";
							if(text.equalsIgnoreCase("1")) {

								text = "reviewed";
							}
							if(text.equalsIgnoreCase("0")) {

								text = "unreviewed";
							}
							if(text.equalsIgnoreCase("-1")) {

								text = "unavailable";
							}

							row.createCell(j-1).setCellValue("\t"+text);
						}
						else {
							row.createCell(j-1).setCellValue(mainTableData.getRow(r)[j]+"");
						}
					}
				}

				if(allData) {

					if(productsToXLS!=null && enzymesToXLS!=null) {

						int maxlength=0;

						if(productsToXLS.length>enzymesToXLS.length) {

							maxlength=productsToXLS.length;
						}
						else {

							maxlength=enzymesToXLS.length;
						}

						for(int k=0;k<maxlength;k++) {

							addrow++;
							row = sheet.createRow(addrow);

							if(k<productsToXLS.length&&k<enzymesToXLS.length) {

								pdWeigth = annotationEnzymes.getProductPercentage(productsToXLS[k].trim(),r);
								ecWeigth = annotationEnzymes.getECPercentage(enzymesToXLS[k].trim(),r);
								if(productsToXLS[k].trim()==""){productsToXLS[k]="\t";}
								if(enzymesToXLS[k].trim()==""){enzymesToXLS[k]="\t";}

								row.createCell(p).setCellValue(productsToXLS[k].trim());
								row.createCell(pS).setCellValue(pdWeigth);
								row.createCell(e).setCellValue(enzymesToXLS[k].trim());
								row.createCell(eS).setCellValue(ecWeigth);

							}
							else if(k<productsToXLS.length) {

								pdWeigth = annotationEnzymes.getProductPercentage(productsToXLS[k].trim(),r);
								if(productsToXLS[k].trim()==""){productsToXLS[k]="\t";}

								row.createCell(p).setCellValue(productsToXLS[k].trim());
								row.createCell(pS).setCellValue(pdWeigth);
							}
							else if(k<enzymesToXLS.length) {

								ecWeigth = annotationEnzymes.getECPercentage(enzymesToXLS[k],r);
								if(enzymesToXLS[k].trim()==""){enzymesToXLS[k]="\t";}

								row.createCell(e).setCellValue(enzymesToXLS[k].trim());
								row.createCell(eS).setCellValue(ecWeigth);
							}
						}
					}
					else if(productsToXLS!=null) {

						addrow++;
						row = sheet.createRow(addrow);

						for(int k=1;k<productsToXLS.length;k++) {

							pdWeigth = annotationEnzymes.getProductPercentage(productsToXLS[k].trim(),r);
							if(productsToXLS[k].trim()==""){productsToXLS[k]="\t";}

							row.createCell(p).setCellValue(productsToXLS[k].trim());
							row.createCell(pS).setCellValue(pdWeigth);
						}
					}
					else if(enzymesToXLS!=null) {

						addrow++;
						row = sheet.createRow(addrow);

						for(int k=1;k<enzymesToXLS.length;k++) {

							ecWeigth = annotationEnzymes.getECPercentage(enzymesToXLS[k].trim(),r);
							if(enzymesToXLS[k].trim()=="")
								enzymesToXLS[k]="\t";

							row.createCell(e).setCellValue(enzymesToXLS[k].trim());
							row.createCell(eS).setCellValue(ecWeigth);
						}
					}
				}

				if(allData){
					addrow++;
					row = sheet.createRow(addrow);
				}

				addrow++;	
			}

			FileOutputStream fileOut = new FileOutputStream(excelFileName);

			//write this workbook to an Outputstream.
			wb.write(fileOut);
			fileOut.flush();
			wb.close();
			fileOut.close();

			Workbench.getInstance().info("Data successfully exported.");
		} 
		catch (Exception e) {

			Workbench.getInstance().error("An error occurred while performing this operation. Error "+e.getMessage());
			e.printStackTrace();
		}
	}


	/**
	 * @author ODias
	 * Filter GenBank files
	 */
	class GBKFileFilter extends javax.swing.filechooser.FileFilter {

		public boolean accept(File f) {

			return f.isDirectory() || f.getName().toLowerCase().endsWith(".gbk");
		}

		public String getDescription() {

			return ".gbk files";
		}
	}

	/**
	 * Save re-annotated GenBank file
	 * @throws IOException 
	 */
	private void saveGenbankFile() throws IOException{

		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setFileFilter(new GBKFileFilter());
		fc.setDialogTitle("Select gbk files directory");
		int returnVal = fc.showOpenDialog(new JTextArea());

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			File[]	files;
			if(fc.getSelectedFile().isDirectory()) {

				files = fc.getSelectedFile().listFiles();
			}
			else {

				files = (new File(fc.getSelectedFile().getParent())).listFiles();
			}

			int countFiles=0;

			for(File file:files) {

				if(file.getAbsolutePath().endsWith(".gbk"))
					countFiles+=1;
			}
			if(countFiles<=0) {

				Workbench.getInstance().error("The selected directory does not contain any Genbank file (*.gbk)");
			}
			else {

				TreeMap<String,String> gene = new TreeMap<String, String>();
				TreeMap<String,String> ecn = new TreeMap<String, String>();
				TreeMap<String,String> prod = new TreeMap<String, String>();

				for(int i=0; i < mainTableData.getTable().size(); i++) {

					//					if((Boolean) mainTableData.getRow(i)[selectColumnNumber]) {

					if(this.productsColumn.getSelectItem(i)!=null) {

						prod.put((String) mainTableData.getRow(i)[locus_tagColumnNumber], this.productsColumn.getSelectItem(i));
					}

					if(jTable.getValueAt(i,geneNameColumnNumber)!=null && !((String) jTable.getValueAt(i,geneNameColumnNumber)).isEmpty()) {

						gene.put((String) mainTableData.getRow(i)[locus_tagColumnNumber], ((String) jTable.getValueAt(i,geneNameColumnNumber)));
					}

					if(this.enzymesColumn.getSelectItem(i)!=null) {

						ecn.put((String) mainTableData.getRow(i)[locus_tagColumnNumber], this.enzymesColumn.getSelectItem(i));
					}
				}						                             

				for(File f: files)
				{
					if(f.getAbsolutePath().endsWith(".gbk"))
					{
						WriteGBFile wgbf = new WriteGBFile(f, ecn, prod, gene);
						wgbf.writeFile();
					}
				}
			}
		}
	}

	//	/**
	//	 * 
	//	 */
	//	private void selectThreshold() {
	//
	//		//		this.homologyDataContainer.setSelectedGene(new HashMap<Integer, Boolean>());
	//		//		this.jRadioButtonTresh.setSelected(true);
	//
	//		if(Double.parseDouble(this.jLabelLowerThreshold.getText())<0 || Double.parseDouble(this.jLabelLowerThreshold.getText())>1) {
	//
	//			this.jLabelLowerThreshold.setText(this.homologyDataContainer.getThreshold().toString());
	//			Workbench.getInstance().warn("The value must be between 0 and 1");
	//		}
	//		else {
	//
	//			Rectangle visible = jTable.getVisibleRect();
	//
	//			this.homologyDataContainer.setThreshold(Double.parseDouble(this.jLabelLowerThreshold.getText()));
	//			this.mainTableData = this.homologyDataContainer.getAllGenes(blastDatabase, false);
	//			//this.jTable.setModel(this.mainTableData);
	//			this.fillList(visible);
	//			//			this.jRadioButtonTresh.setSelected(true);
	//		}
	//
	//	}

	/**
	 * @param locusMap
	 * @return
	 */
	private Map<Integer, List<String>> getUniprotECnumbersTable(Map<Integer, String> locusMap) {

		Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();
		try {
			Map<String, List<String>> uniprotData = this.annotationEnzymes.getUniprotECnumbers();

			Map<String, Integer> inv = new HashMap<String, Integer>(); 

			for (Entry<Integer, String> entry : locusMap.entrySet()) {

				inv.put(entry.getValue(), entry.getKey());
			}

			if(uniprotData != null) {

				for(String locus : uniprotData.keySet()) {

					if(inv.containsKey(locus)) {

						result.put(inv.get(locus), uniprotData.get(locus));
					}
				}
			}
		} 
		catch (Exception e) {
			Workbench.getInstance().error(e);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param uniprot
	 * @param merlin
	 * @return
	 * 
	 * 
	 *  0 distinct
	 * -1 partial match uniprot more
	 *  1 match
	 *  2 partial match merlin more
	 * 
	 */
	private Map<Integer, Integer> compareAnnotations(Map<Integer, String> ecItems) {

		Map<Integer, Integer> result = new HashMap<Integer, Integer>();

		Map<Integer, List<String>> merlin = new HashMap<Integer, List<String>>();
		for(int row : ecItems.keySet()) {

			if(ecItems.get(row) != null) {

				List<String> ecnumbers = new ArrayList<String>();
				String[] ecs = ecItems.get(row).split(", ");

				for(String ec : ecs)
					ecnumbers.add(ec.trim());

				merlin.put(row, ecnumbers);
			}
		}

		List<Integer> merlinKeySet = new ArrayList<Integer>(merlin.keySet());
		Map<Integer, List<String>> uniprotECnumbersTable_clone = new HashMap<Integer, List<String>>(this.getUniprotECnumbersTable);

		for(int row : merlinKeySet) {

			List<String> merlin_ecs = new ArrayList<String> (merlin.get(row));

			if(uniprotECnumbersTable_clone.containsKey(row) && !uniprotECnumbersTable_clone.get(row).get(0).equalsIgnoreCase("null") && !uniprotECnumbersTable_clone.get(row).get(0).equalsIgnoreCase("")) {

				List<String> uni_ecs = new ArrayList<String> (uniprotECnumbersTable_clone.get(row));

				result.put(row, this.compareAnnotationsLists(merlin_ecs, uni_ecs));
				uniprotECnumbersTable_clone.remove(row);
			}
			else{

				if(!merlin_ecs.get(0).equalsIgnoreCase("null") && !merlin_ecs.get(0).equalsIgnoreCase(""))
					result.put(row, 0);
			}
			merlin.remove(row);
		}

		List<Integer> uniprotKeySet = new ArrayList<Integer>(uniprotECnumbersTable_clone.keySet());

		for(int row : uniprotKeySet) {

			if(!uniprotECnumbersTable_clone.get(row).get(0).equalsIgnoreCase("null") && !uniprotECnumbersTable_clone.get(row).get(0).equalsIgnoreCase("")) {

				result.put(row, 0);
			}
			uniprotECnumbersTable_clone.remove(row);
		}

		return result;
	}


	/**
	 * @param merlin_ecs
	 * @param uni_ecs
	 * @return
	 */
	private int compareAnnotationsLists(List<String> merlin_ecs, List<String> uniprot_ecs) {

		List<String> uni_ecs = new ArrayList<String>(uniprot_ecs);
		List<String> merlin_ecs_clone = new ArrayList<String> (merlin_ecs);
		List<String> uni_ecs_clone = new ArrayList<String> (uni_ecs);
		int uni_initial_size = uni_ecs_clone.size();
		int merlin_initial_size =  merlin_ecs_clone.size();

		if(merlin_ecs.size() == uni_ecs.size()) {

			for(String ecnumber :  merlin_ecs_clone) {

				merlin_ecs.remove(ecnumber);
				if(uni_ecs.contains(ecnumber)) {

					uni_ecs.remove(ecnumber);
				}
			}

			if(uni_ecs.isEmpty()) {

				return 1;
			}
			else if(!uni_ecs.isEmpty() && !uni_ecs.get(0).equalsIgnoreCase("null") && !uni_ecs.get(0).equalsIgnoreCase("")) {

				return 0;
			}
		}
		else {

			if(merlin_ecs.size() > uni_ecs.size()) {

				for(String ecnumber :  merlin_ecs_clone) {

					merlin_ecs.remove(ecnumber);
					if(uni_ecs.contains(ecnumber)) {

						uni_ecs.remove(ecnumber);
					}
				}

				if(uni_ecs.isEmpty()) {

					return 2;
				}
				else if(!uni_ecs.isEmpty() && !uni_ecs.get(0).equalsIgnoreCase("null") && !uni_ecs.get(0).equalsIgnoreCase("")) {

					if(uni_initial_size == uni_ecs.size()) {

						return 0;
					}
					else {

						return -1;
					}
				}
			}
			else {

				for(String ecnumber :  uni_ecs_clone) {

					uni_ecs.remove(ecnumber);
					if(merlin_ecs.contains(ecnumber)) {

						merlin_ecs.remove(ecnumber);
					}
				}

				if(merlin_ecs.isEmpty()) {

					return -1;
				}
				else if(!merlin_ecs.isEmpty() && !merlin_ecs.get(0).equalsIgnoreCase("null") && !merlin_ecs.get(0).equalsIgnoreCase("")) {

					if(merlin_initial_size == merlin_ecs.size()) {

						return 0;
					}
					else {

						return -1;
					}
				}
			}

		}
		return -10;
	}

	/**
	 * @param array
	 * @param element
	 * @return
	 */
	private String[] removeElement(String[] array, String element) {
		if(Arrays.asList(array).contains(element)) {

			String[] newArray = new String[array.length-1];
			boolean reachedElement = false;

			for(int i=0; i<(array.length-1); i++) {

				if(!array[i].equals(element)) {

					if(reachedElement) {

						newArray[i]=array[i+1];
					}
					else {

						newArray[i]=array[i];
					}
				}
				else {

					reachedElement = true;
					newArray[i]=array[i+1];
				}
			}
			return newArray;
		}
		return array;
	}

	/**
	 * @param visible
	 */
	private void scrollToVisible(final Rectangle visible) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				jTable.scrollRectToVisible(visible);
			}
		});
	}

	/* (non-Javadoc)
	 * @see merlin_utilities.UpdateUI#updateGraphicalObject()
	 */
	@Override
	public void updateTableUI() {

		this.updateTableUI(true);
		
	}
	
	private void updateTableUI(boolean refresh) {
		
		jLabeldAlpha.setText(annotationEnzymes.getAlpha().toString());
		jLabelLowerThreshold.setText(annotationEnzymes.getThreshold().toString());
		jLabelUpperThreshold.setText(annotationEnzymes.getUpperThreshold().toString());

		Rectangle visible = null;

		if(annotationEnzymes.getSelectedRow()>-1 && jTable.getRowCount()>0 && jTable.getRowCount()> annotationEnzymes.getSelectedRow())
			visible = this.jTable.getCellRect(annotationEnzymes.getSelectedRow(), -1, true);

		this.annotationEnzymes.resetDataStuctures();

		try {
			this.mainTableData = this.annotationEnzymes.getAllGenes(blastDatabase, refresh);
		} 
		catch (Exception e) {
			Workbench.getInstance().error(e);
			e.printStackTrace();
		}

		this.fillList(visible);

		this.updateUI();
		this.revalidate();
		this.repaint();

		
	}

	/* (non-Javadoc)
	 * @see merlin_utilities.UpdateUI#addListenersToGraphicalObjects(javax.swing.JPanel, javax.swing.MyJTable)
	 */
	@Override
	public void addListenersToGraphicalObjects() {}

}
