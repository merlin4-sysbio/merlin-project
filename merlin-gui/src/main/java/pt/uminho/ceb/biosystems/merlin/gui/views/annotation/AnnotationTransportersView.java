package pt.uminho.ceb.biosystems.merlin.gui.views.annotation;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.annotation.AnnotationTransportersAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.ButtonColumn;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.CreateImageIcon;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.ExportToXLS;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MerlinUtils;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MyJTable;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.SearchInTable;
import pt.uminho.ceb.biosystems.merlin.gui.views.WorkspaceUpdatablePanel;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelReactionsServices;

public class AnnotationTransportersView extends WorkspaceUpdatablePanel {

	private static final long serialVersionUID = -1;
	private JScrollPane jScrollPane1;
	private JPanel jPanelExport, jPanelIntegration, jPanelReactions, jPanelDatabase;
	private JButton jButtonExportXLS, jButtonIntegration;
	private JPanel jPanel1, jPanel2;
	private MyJTable jTable;
	private AnnotationTransportersAIB annotationTransporters;
	private WorkspaceGenericDataTable mainTableData;
	private SearchInTable searchInGenes;
	private ButtonColumn buttonColumn;
	private JButton jButtonCleanIntegration;

	/**
	 * @param genes
	 */
	public AnnotationTransportersView(AnnotationTransportersAIB transportersContainer) {

		super(transportersContainer);
		this.annotationTransporters = transportersContainer;
		List<Integer> nameTabs = new ArrayList<>();
		nameTabs.add(1);
		nameTabs.add(3);
		
		String[] searchParams = new String[] { "name", "all", "metabolite" };
		this.searchInGenes = new SearchInTable(nameTabs, searchParams);
		
		initGUI();
		fillList();
	}


	/**
	 * 
	 */
	private void initGUI() {

		try  {

			GridBagLayout jPanelLayout = new GridBagLayout();
			jPanelLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
			jPanelLayout.columnWidths = new int[] {7, 7, 7};
			jPanelLayout.rowWeights = new double[] {0.0, 200.0, 0.0, 0.0, 0.0};
			jPanelLayout.rowHeights = new int[] {7, 50, 7, 3, 7};

			this.setLayout(jPanelLayout);
			jPanel2 = new JPanel();
			GridBagLayout jPanel2Layout = new GridBagLayout();
			jPanel2Layout.columnWeights = new double[] {0.1, 0.0, 0.1, 0.0, 0.1, 0.0, 0.1};
			jPanel2Layout.columnWidths = new int[] {7, 7, 7, 7, 7, 7, 7};
			jPanel2Layout.rowWeights = new double[] {0.1, 0.0};
			jPanel2Layout.rowHeights = new int[] {7, 7};
			jPanel2.setLayout(jPanel2Layout);
			this.add(jPanel2, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			{
				jPanelDatabase = new JPanel();
				GridBagLayout jPanelDatabaseLayout = new GridBagLayout();
				jPanelDatabaseLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
				jPanelDatabaseLayout.columnWidths = new int[] {7, 7, 7};
				jPanelDatabaseLayout.rowWeights = new double[] {0.0};
				jPanelDatabaseLayout.rowHeights = new int[] {5};
				jPanelDatabase.setLayout(jPanelDatabaseLayout);
				jPanel2.add(jPanelDatabase, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelDatabase.setBounds(567, 56, 139, 61);
				jPanelDatabase.setBorder(BorderFactory.createTitledBorder("TRIAGE"));
			}
			{
				jPanelReactions = new JPanel();
				GridBagLayout jPanelReactionsLayout = new GridBagLayout();
				jPanel2.add(jPanelReactions, new GridBagConstraints(2, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelReactions.setBounds(325, 41, 376, 79);
				jPanelReactions.setBorder(BorderFactory.createTitledBorder("reactions"));
				jPanelReactionsLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
				jPanelReactionsLayout.columnWidths = new int[] {7, 7, 7};
				jPanelReactionsLayout.rowWeights = new double[] {0.0};
				jPanelReactionsLayout.rowHeights = new int[] {5};
				
				jPanelReactions.setLayout(jPanelReactionsLayout);
			}
			{
				jPanelExport = new JPanel();
				GridBagLayout jPanelExportLayout = new GridBagLayout();
				jPanelExportLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
				jPanelExportLayout.columnWidths = new int[] {7, 7, 7};
				jPanelExportLayout.rowWeights = new double[] {0.0};
				jPanelExportLayout.rowHeights = new int[] {5};
				jPanelExport.setLayout(jPanelExportLayout);
				jPanel2.add(jPanelExport, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelExport.setBounds(567, 56, 139, 61);
				jPanelExport.setBorder(BorderFactory.createTitledBorder("export"));
				{
					jButtonExportXLS = new JButton();
					jPanelExport.add(jButtonExportXLS, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jButtonExportXLS.setText("export file");
					jButtonExportXLS.setToolTipText("export to excel file (xls)");
					jButtonExportXLS.setIcon(new CreateImageIcon(new ImageIcon((getClass().getClassLoader().getResource("icons/Download.png"))),0.1).resizeImageIcon());
					jButtonExportXLS.setPreferredSize(new Dimension(200, 40));
					jButtonExportXLS.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent arg0)  {

							try {

								JFileChooser fc = new JFileChooser();
								fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
								fc.setDialogTitle("Select directory");
								int returnVal = fc.showOpenDialog(new JTextArea());

								if (returnVal == JFileChooser.APPROVE_OPTION) {


									File file = fc.getSelectedFile();
									String filePath = file.getAbsolutePath();
									Calendar cal = new GregorianCalendar();

									// Get the components of the time
									int hour24 = cal.get(Calendar.HOUR_OF_DAY);     // 0..23
									int min = cal.get(Calendar.MINUTE);             // 0..59
									int day = cal.get(Calendar.DAY_OF_YEAR);		//0..365

									filePath += "/"+annotationTransporters.getWorkspace().getName()+"_"+annotationTransporters.getWorkspace().getName()+"_"+hour24+"_"+min+"_"+day+".xlsx";
									
									ExportToXLS.exportToXLS(filePath, mainTableData, jTable);

									Workbench.getInstance().info("data successfully exported.");
								}
							} catch (Exception e) {

								Workbench.getInstance().error("an error occurred while performing this operation. error "+e.getMessage());
								e.printStackTrace();
							}
						}
					});
				}
			}
			{
				jPanelIntegration = new JPanel();
				GridBagLayout jPanelIntegrationLayout = new GridBagLayout();
				jPanelIntegrationLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
				jPanelIntegrationLayout.columnWidths = new int[] {7, 7, 7};
				jPanelIntegrationLayout.rowWeights = new double[] {0.0};
				jPanelIntegrationLayout.rowHeights = new int[] {5};
				jPanelIntegration.setLayout(jPanelIntegrationLayout);
				jPanel2.add(jPanelIntegration, new GridBagConstraints(5, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelIntegration.setBounds(567, 56, 139, 61);
				jPanelIntegration.setBorder(BorderFactory.createTitledBorder("integration"));
				{
					jButtonIntegration = new JButton();
					jPanelIntegration.add(jButtonIntegration, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jButtonIntegration.setText("integrate to model");
					jButtonIntegration.setToolTipText("integrates the generated transport reactions with the model reactions");
					jButtonIntegration.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Integrate.png")),0.1).resizeImageIcon());
					jButtonIntegration.setPreferredSize(new Dimension(200, 40));
					jButtonIntegration.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent arg0)  {

							if(annotationTransporters == null) {
								Workbench.getInstance().error("no transporters information on this Project!");
							}

							try {
								
//								Map<Integer, AnnotationCompartmentsGenes> geneCompartment = null;
//								
//								AnnotationCompartmentsAIB c = null;
//								
//								for(WorkspaceEntity e: annotationTransporters.getWorkspace().getDatabase().getAnnotations().getEntitiesList())
//									if(e.getName().equalsIgnoreCase("compartments"))
//										c=(AnnotationCompartmentsAIB) e;
//								
//								if(ProjectServices.isCompartmentalisedModel(annotationTransporters.getWorkspace().getName()))
//									geneCompartment = c.runCompartmentsInterface(c.getThreshold());
//								
//								ParamSpec[] paramsSpec = new ParamSpec[]{
//										new ParamSpec("compartments", Map.class, geneCompartment, null),
//										new ParamSpec("workspace", Workspace.class, annotationTransporters.getWorkspace(), null)
//									};
//									
//									for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
//										if (def.getID().equals("operations.IntegrateTransporterstoDatabase.ID")){
//											
//											Workbench.getInstance().executeOperation(def, paramsSpec);
//										}
//									}
//
//									checkButtonsStatus();
							}
							catch(Exception ex){
								ex.printStackTrace();
							}
						}
							
					});
				}
				{
					jButtonCleanIntegration = new JButton();
					jPanelIntegration.add(jButtonCleanIntegration, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jButtonCleanIntegration.setText("clean integration");
					jButtonCleanIntegration.setToolTipText("clean the integrated transport reactions");
					jButtonCleanIntegration.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Delete.png")),0.1).resizeImageIcon());
					jButtonCleanIntegration.setPreferredSize(new Dimension(200, 40));
					jButtonCleanIntegration.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent arg0)  {

							try {
								
								ModelReactionsServices.removeReactionsBySource(annotationTransporters.getWorkspace().getName(), SourceType.TRANSPORTERS);
								
								Workbench.getInstance().info("Integration successfully cleaned!");
								
								checkButtonsStatus();
								
							} catch (Exception e) {
								
								Workbench.getInstance().error("error while cleaning integration!");
								e.printStackTrace();
							}
						}
							
					});
				}
			}
			{
				jPanel2.add(searchInGenes.addPanel(), new GridBagConstraints(0, 1, 7, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}

			jPanel1 = new JPanel();
			GridBagLayout thisLayout = new GridBagLayout();
			jPanel1.setLayout(thisLayout);
			this.add(jPanel1, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

			jScrollPane1 = new JScrollPane();
			jTable = new MyJTable();
			jTable.setShowGrid(false);
			jScrollPane1.setViewportView(jTable);
			jPanel1.add(jScrollPane1,new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

			checkButtonsStatus();
			
			this.setPreferredSize(new java.awt.Dimension(887, 713));

		} 
		catch (Exception e) {e.printStackTrace();}
	}
	
	/**
	 * Method to check which buttons should be active.
	 */
	private void checkButtonsStatus(){
		
		try {
			boolean integrated = ModelReactionsServices.checkReactionsBySource(annotationTransporters.getWorkspace().getName(), SourceType.TRANSPORTERS);
			
			if(integrated){
				jButtonIntegration.setEnabled(false);
				jButtonCleanIntegration.setEnabled(true);
			}
			else{
				jButtonIntegration.setEnabled(true);
				jButtonCleanIntegration.setEnabled(false);
			}
			
			MerlinUtils.updateTransportersAnnotationView(annotationTransporters.getWorkspace().getName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void fillList() {
		
		mainTableData = this.annotationTransporters.getData();
		
		jTable.setModel(mainTableData);
		jTable.setSortableFalse();
		
		buttonColumn =  new ButtonColumn(jTable,0, new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				processButton(arg0);
			}},
			new MouseAdapter(){
				public void mouseClicked(MouseEvent e) {
					// {
					// get the coordinates of the mouse click
					Point p = e.getPoint();

					// get the row index that contains that coordinate
					int rowNumber = jTable.rowAtPoint(p);
					int  columnNumber = jTable.columnAtPoint(p);
					jTable.setColumnSelectionInterval(columnNumber, columnNumber);
					// Get the ListSelectionModel of the MyJTable
					ListSelectionModel model = jTable.getSelectionModel();
					// set the selected interval of rows. Using the "rowNumber"
					// variable for the beginning and end selects only that one row.
					model.setSelectionInterval( rowNumber, rowNumber );
					processButton(e);
				}
			}, new ArrayList<>());
		TableColumnModel tc = jTable.getColumnModel();
		tc.getColumn(0).setMaxWidth(35);				//button
		tc.getColumn(0).setResizable(false);
		tc.getColumn(0).setModelIndex(0);
		
		this.searchInGenes.setMyJTable(jTable);
		this.searchInGenes.setMainTableData(mainTableData);
		this.searchInGenes.setSearchTextField("");
		
	}


	/**
	 * @param arg0
	 */
	private void processButton(EventObject arg0) {
		
		JButton button = null;
		if(arg0.getClass()==ActionEvent.class) {

			button = (JButton)((ActionEvent) arg0).getSource();
		}

		if(arg0.getClass()==MouseEvent.class) {

			button = (JButton)((MouseEvent) arg0).getSource();
		}

		ListSelectionModel model = jTable.getSelectionModel();
		model.setSelectionInterval( buttonColumn.getSelectIndex(button), buttonColumn.getSelectIndex(button));

//		selectedRowID = mainTableData.getRowID(jTable.convertRowIndexToModel(jTable.getSelectedRow()));

//		WorkspaceDataTable[] informationTable = annotationTransporters.getRowInfo(selectedRowID);
		
//		if(this.annotationTransporters.getWorkspace().getTransportContainer()!=null)
//			new GenericDetailWindow(informationTable, "Gene data", "Gene: "+annotationTransporters.getGeneName(selectedRowID)+"   user alpha: "+this.annotationTransporters.getAlpha()+"   reactions alpha: "+this.annotationTransporters.getWorkspace().getTransportContainer().getAlpha());
//		else
//			new GenericDetailWindow(informationTable, "Gene data", "Gene: "+annotationTransporters.getGeneName(selectedRowID)+"   user alpha: "+this.annotationTransporters.getAlpha());
	}
	
	/* (non-Javadoc)
	 * @see merlin_utilities.UpdateUI#updateGraphicalObject()
	 */
	@Override
	public void updateTableUI() {

		this.annotationTransporters.resetDataStuctures();
		this.fillList();
		this.updateUI();
		this.revalidate();
		this.repaint();
	}

	/* (non-Javadoc)
	 * @see merlin_utilities.UpdateUI#addListenersToGraphicalObjects(javax.swing.JPanel, javax.swing.MyJTable)
	 */
	@Override
	public void addListenersToGraphicalObjects() {

		jTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {

				updateTableUI();
			}
		});

		this.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {}

			@Override
			public void focusGained(FocusEvent arg0) {

				updateTableUI();
			}
		});
	}
}
