package pt.uminho.ceb.biosystems.merlin.gui.views.model;
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
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.ModelGenesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.jpanels.InsertEditGene;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.ButtonColumn;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.CreateImageIcon;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.ExportToXLS;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MerlinUtils;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MyJTable;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.SearchInTable;
import pt.uminho.ceb.biosystems.merlin.gui.views.WorkspaceUpdatablePanel;
import pt.uminho.ceb.biosystems.merlin.gui.views.windows.GenericDetailWindow;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;


public class ModelGenesAIBView extends WorkspaceUpdatablePanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane1;
	private JButton jButtonEdit;
	private JButton jButtonInsert;
	private JPanel jPanelInsertEdit;
	private JPanel jPanelExport;
	private JPanel jPanelRemove,jPanelGenes;
	private JButton jButtonRemove, jButtonExportTxt;
	private JRadioButton jRadioButton1, jRadioButton4; //, jRadioButton3, jRadioButton2 ;
	private ButtonGroup buttonGroup1;
	private JPanel jPanel1, jPanel2;
	private MyJTable jTable;
	private ModelGenesAIB modelGenes;
	private JComboBox<String> jComboBoxRemove;
	private WorkspaceGenericDataTable mainTableData;
	private int infoSelectedRow;
	private SearchInTable searchInGenes;
	private ButtonColumn buttonColumn;


	/**
	 * @param genes
	 */
	public ModelGenesAIBView(ModelGenesAIB genes) {

		super(genes);
		this.modelGenes = genes;
		List<Integer> nameTabs = new ArrayList<>();
		nameTabs.add(1);
		nameTabs.add(2);
		this.searchInGenes = new SearchInTable(nameTabs);
		initGUI();
		fillList(true);
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
				jPanelGenes = new JPanel();
				GridBagLayout jPanelGenesLayout = new GridBagLayout();
				jPanelGenesLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
				jPanelGenesLayout.columnWidths = new int[] {7, 7, 7};
				jPanelGenesLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				jPanelGenesLayout.rowHeights = new int[] {5, 5, 5};
				jPanelGenes.setLayout(jPanelGenesLayout);
				jPanel2.add(jPanelGenes, new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelGenes.setBounds(718, 6, 157, 115);
				jPanelGenes.setBorder(BorderFactory.createTitledBorder("gene types"));
				{
					jRadioButton1 = new JRadioButton();
					jPanelGenes.add(jRadioButton1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jRadioButton1.setText("all genes");
				}
				{
					jRadioButton4 = new JRadioButton();
					jPanelGenes.add(jRadioButton4, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jRadioButton4.setText("encoding genes");
				}
				buttonGroup1 = new ButtonGroup();
				buttonGroup1.add(jRadioButton1);
				jRadioButton1.setSelected(true);
				jRadioButton1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						fillList(true);
					}
				});
				buttonGroup1.add(jRadioButton4);
				jRadioButton4.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent evt) {

						fillList(true);
					}
				});
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
					jButtonExportTxt = new JButton();
					jPanelExport.add(jButtonExportTxt, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jButtonExportTxt.setText("export file");
					jButtonExportTxt.setToolTipText("export to xls tabbed file");
					jButtonExportTxt.setBounds(11, 8, 118, 48);
					jButtonExportTxt.setIcon(new CreateImageIcon(new ImageIcon((getClass().getClassLoader().getResource("icons/Download.png"))),0.1).resizeImageIcon());
					jButtonExportTxt.setPreferredSize(new Dimension(90, 40));
					jButtonExportTxt.setSize(90, 40);
					jButtonExportTxt.addActionListener(new ActionListener() {

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

									filePath += "/"+modelGenes.getName()+"_"+modelGenes.getWorkspace().getName()+"_"+hour24+"_"+min+"_"+day+".xlsx";

									ExportToXLS.exportToXLS(filePath, mainTableData, jTable);

									Workbench.getInstance().info("Data successfully exported.");
								}
							} catch (Exception e) {

								Workbench.getInstance().error("An error occurred while performing this operation. Error "+e.getMessage());
								e.printStackTrace();
							}
						}
					});
				}
			}
			{
				jPanelRemove = new JPanel();
				GridBagLayout jPanelRemoveLayout = new GridBagLayout();
				jPanelRemoveLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0};
				jPanelRemoveLayout.columnWidths = new int[] {7, 7, 7, 7, 7};
				jPanelRemoveLayout.rowWeights = new double[] {0.0};
				jPanelRemoveLayout.rowHeights = new int[] {5};
				jPanelRemove.setLayout(jPanelRemoveLayout);
				jPanel2.add(jPanelRemove, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelRemove.setBounds(297, 56, 256, 61);
				jPanelRemove.setBorder(BorderFactory.createTitledBorder("remove"));
				{
					jButtonRemove = new JButton();
					jPanelRemove.add(jButtonRemove, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jButtonRemove.setText("remove");
					jButtonRemove.setToolTipText("remove gene data");
					jButtonRemove.setPreferredSize(new Dimension(90, 40));
					jButtonRemove.setSize(90, 40);
					jButtonRemove.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {

							try {
								if(jComboBoxRemove.getSelectedItem()=="remove row" && jTable.getSelectedRow()!=-1) {

									int selectedRow = jTable.convertRowIndexToModel(jTable.getSelectedRow());
									int geneIdentifier = modelGenes.getIdentifiers().get(selectedRow);
									ModelGenesServices.removeGene(modelGenes.getWorkspace().getName(), geneIdentifier, jRadioButton4.isSelected());
									fillList(false);
									MerlinUtils.updateGeneView(modelGenes.getWorkspace().getName());
									MerlinUtils.updateMetabolicViews(modelGenes.getWorkspace().getName());
									Workbench.getInstance().info("Gene removed successfully!");
								}
								else {

									if(jComboBoxRemove.getSelectedItem()=="remove all") {

										ModelGenesServices.removeGenes(modelGenes.getWorkspace().getName(), jRadioButton4.isSelected());
										fillList(false);
									}
									else {

										Workbench.getInstance().warn("Please Select a Row,\n  or Remove all rows!");	
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

				jButtonRemove.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Remove.png")),0.1).resizeImageIcon());
				{
					ComboBoxModel<String> jComboBox1Model = 
							new DefaultComboBoxModel<>(
									new String[] { "remove row", "remove all" });
					jComboBoxRemove = new JComboBox<>();
					jPanelRemove.add(jComboBoxRemove, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jComboBoxRemove.setBounds(8, 19, 107, 26);
					jComboBoxRemove.setModel(jComboBox1Model);
				}

			}
			{
				jPanelInsertEdit = new JPanel();
				GridBagLayout jPanelInsertEditLayout = new GridBagLayout();
				jPanelInsertEditLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0};
				jPanelInsertEditLayout.columnWidths = new int[] {7, 7, 7, 7, 7};
				jPanelInsertEditLayout.rowWeights = new double[] {0.0};
				jPanelInsertEditLayout.rowHeights = new int[] {5};
				jPanelInsertEdit.setLayout(jPanelInsertEditLayout);
				jPanel2.add(jPanelInsertEdit, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelInsertEdit.setBounds(7, 56, 277, 61);
				jPanelInsertEdit.setBorder(BorderFactory.createTitledBorder("insert/edit"));
				{
					jButtonInsert = new JButton();
					jPanelInsertEdit.add(jButtonInsert, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jButtonInsert.setIcon( new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Upload.png")),0.1).resizeImageIcon());
					jButtonInsert.setText("insert");
					jButtonInsert.setToolTipText("insert new gene");
					jButtonInsert.setPreferredSize(new Dimension(90, 40));
					jButtonInsert.setSize(90, 40);
					jButtonInsert.addActionListener( new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {

							try {
								new InsertEditGene(-10, modelGenes);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							fillList(false);

						}
					}
							);
				}
				{
					jButtonEdit = new JButton();
					jPanelInsertEdit.add(jButtonEdit, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jButtonEdit.setText("edit");
					jButtonEdit.setToolTipText("edit gene data");
					//					jButtonEdit.setIcon(new ImageIcon(getClass().getClassLoader().getResource(getClass().getClassLoader().getResource("icons/export.png"))));
					jButtonEdit.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Edit.png")),0.1).resizeImageIcon());
					jButtonEdit.setPreferredSize(new Dimension(90, 40));
					jButtonEdit.setSize(90, 40);
					jButtonEdit.addActionListener( new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {

							try {
								int editedRow = jTable.convertRowIndexToModel(jTable.getSelectedRow());

								if(jTable.getSelectedRow()>-1) {

									modelGenes.setGeneData(null);
									new InsertEditGene(jTable.convertRowIndexToModel(jTable.getSelectedRow()), modelGenes);
									fillList(false);
									if(editedRow>-1 && editedRow<jTable.getRowCount()) {

										jTable.setRowSelectionInterval(editedRow, editedRow);
										jTable.scrollRectToVisible(jTable.getCellRect(editedRow, -1, true));
									}
								}
								else {

									Workbench.getInstance().warn("Please Select a Row!");
								}
							} catch (IndexOutOfBoundsException e1) {
								Workbench.getInstance().warn("Please Select a Row!");
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
							);
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

			this.setPreferredSize(new java.awt.Dimension(887, 713));

		} 
		catch (Exception e) {e.printStackTrace();}
	}

	/**
	 * 
	 */
	public void fillList(boolean update) {

		//		jTable = new MyJTable();
		//		jTable.setShowGrid(false);
		//		jScrollPane1.setViewportView(jTable);

		try {
			boolean encoded = this.jRadioButton4.isSelected();

			mainTableData = this.modelGenes.getMainTableData(encoded, update);

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
		catch (Exception e) {
			Workbench.getInstance().error(e);
			e.printStackTrace();
		}
	}


	/**
	 * @param arg0
	 */
	private void processButton(EventObject arg0) {

		try {
			JButton button = null;
			if(arg0.getClass()==ActionEvent.class)
				button = (JButton)((ActionEvent) arg0).getSource();

			if(arg0.getClass()==MouseEvent.class)
				button = (JButton)((MouseEvent) arg0).getSource();

			ListSelectionModel model = jTable.getSelectionModel();
			model.setSelectionInterval( buttonColumn.getSelectIndex(button), buttonColumn.getSelectIndex(button));

			int geneIdentifier = mainTableData.getRowID(jTable.convertRowIndexToModel(jTable.getSelectedRow()));

			boolean refresh = (this.infoSelectedRow != jTable.getSelectedRow());

			WorkspaceDataTable[] informationTable = modelGenes.getRowInfo(geneIdentifier, refresh);

			this.infoSelectedRow = jTable.getSelectedRow();

			new GenericDetailWindow(informationTable, "Gene data", "Gene: "+modelGenes.getGeneName(geneIdentifier));
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

		this.modelGenes.resetDataStuctures();
		this.fillList(true);
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
