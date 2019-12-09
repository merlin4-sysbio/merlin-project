package pt.uminho.ceb.biosystems.merlin.gui.views;
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
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.interfaces.IEntityAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.ButtonColumn;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.CreateImageIcon;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.ExportToXLS;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MyJTable;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.SearchInTable;
import pt.uminho.ceb.biosystems.merlin.gui.views.windows.GenericDetailWindow;

/**
 * @author ODias
 *
 */
public class WorkspaceGenericEntityDataView extends WorkspaceUpdatablePanel  {

	private static final long serialVersionUID = 1L;
	protected JScrollPane jScrollPane1;
	protected JPanel jPanel1;
	protected JPanel jPanel2;
	protected MyJTable jTable;
	protected IEntityAIB entity;
	protected JPanel jPanel4;
	protected JButton jButton1ExportTxt;
	protected WorkspaceGenericDataTable dataTable;
	protected int infoSelectedRow;
	protected SearchInTable searchInEntity;
	protected ButtonColumn buttonColumn;


	/**
	 * @param entity
	 */
	public WorkspaceGenericEntityDataView(IEntityAIB entity) {

		super();
		this.entity = entity;
		List<Integer> nameTabs = new ArrayList<>();
		nameTabs.add(1);
		nameTabs.add(2);
		this.searchInEntity= new SearchInTable(nameTabs);
		initGUI();
		fillList();
		super.setVisible(true);		
	}

	/**
	 * 
	 */
	private void initGUI() {

		GridBagLayout jPanel1Layout = new GridBagLayout();
		jPanel1Layout.columnWeights = new double[] {0.0, 0.1, 0.0};
		jPanel1Layout.columnWidths = new int[] {7, 7, 7};
		jPanel1Layout.rowWeights = new double[] {0.0, 3.5, 0.0, 0.1, 0.0};
		jPanel1Layout.rowHeights = new int[] {5, 25, 5, 5, 5};
		this.setLayout(jPanel1Layout);

		jPanel2 = new JPanel();
		GridBagLayout jPanel2Layout = new GridBagLayout();
		jPanel2Layout.rowWeights = new double[] {0.1};
		jPanel2Layout.rowHeights = new int[] {7};
		jPanel2Layout.columnWeights = new double[] {0.1, 0.0, 0.0};
		jPanel2Layout.columnWidths = new int[] {7, 7, 7};
		jPanel2.setLayout(jPanel2Layout);
		this.add(jPanel2, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		{
			jPanel4 = new JPanel();
			jPanel2.add(jPanel4, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			GridBagLayout searchPanelLayout = new GridBagLayout();
			searchPanelLayout.rowWeights = new double[] {0.0, 0.0};
			searchPanelLayout.rowHeights = new int[] {22};
			searchPanelLayout.columnWeights = new double[] {0.1, 0.0};
			searchPanelLayout.columnWidths = new int[] {100, 7};
			jPanel4.setLayout(searchPanelLayout);
			jPanel4.setBorder(BorderFactory.createTitledBorder("Export"));
			jPanel4.setBounds(30, 26, 676, 34);
			{
				jButton1ExportTxt = new JButton();
				jPanel4.add(jButton1ExportTxt, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButton1ExportTxt.setToolTipText("export to text file (xls)");
				jButton1ExportTxt.setText("export file");
				jButton1ExportTxt.setIcon(new CreateImageIcon(new ImageIcon((getClass().getClassLoader().getResource("icons/Download.png"))),0.1).resizeImageIcon());
				jButton1ExportTxt.setBounds(532, 72, 174, 38);
				jButton1ExportTxt.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent arg0)  {

						try  {

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

								filePath += "/"+entity.getName()+"_"+entity.getWorkspace().getName()+"_"+hour24+"_"+min+"_"+day+".xlsx";
								
								ExportToXLS.exportToXLS(filePath, dataTable, jTable);
								
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

		jPanel2.add(this.searchInEntity.addPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jPanel1 = new JPanel();
		GridBagLayout scrollLayout = new GridBagLayout();
		scrollLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
		scrollLayout.columnWidths = new int[] {7, 7, 7};
		scrollLayout.rowWeights = new double[] {0.0, 0.1, 0.0};
		scrollLayout.rowHeights = new int[] {7, 7, 7};
		jPanel1.setLayout(scrollLayout);
		this.add(jPanel1, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		{
			jScrollPane1 = new JScrollPane();
			jPanel1.add(jScrollPane1, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			{
				jTable = new MyJTable();
				jTable.setShowGrid(false);
				jScrollPane1.setViewportView(jTable);
			}
		}

		this.setPreferredSize(new java.awt.Dimension(887, 713));
	}

	/**
	 * 
	 */
	public void fillList() {
		
		dataTable = this.entity.getMainTableData();

		jTable.setModel(dataTable);
		jTable.setSortableFalse();
		jTable.setAutoCreateRowSorter(true);
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

		this.searchInEntity.setMyJTable(jTable);
		this.searchInEntity.setMainTableData(this.entity.getMainTableData());
		this.searchInEntity.setSearchTextField("");
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
				button = (JButton)((ActionEvent) arg0).getSource();

			button.setSelected(true);

			ListSelectionModel model = jTable.getSelectionModel();
			model.setSelectionInterval( buttonColumn.getSelectIndex(button), buttonColumn.getSelectIndex(button));

			int entityIdentifier = dataTable.getRowID(jTable.convertRowIndexToModel(jTable.getSelectedRow()));
			
			boolean refresh = (this.infoSelectedRow == jTable.getSelectedRow());

			WorkspaceDataTable[] table = entity.getRowInfo(entityIdentifier, refresh);
			
			this.infoSelectedRow = jTable.getSelectedRow();
			
			new GenericDetailWindow(table, dataTable.getWindowName(), entity.getSingular() + jTable.getValueAt(jTable.getSelectedRow(), 1));
		} 
		catch (Exception e) {
			Workbench.getInstance().error(e);
			e.printStackTrace();
		}
	}

	public String getSingular()  {
		return "metabolite: ";
	}

	@Override
	public void updateTableUI() {
		
		this.fillList();
		this.updateUI();
		this.revalidate();
		this.repaint();		
	}

	@Override
	public void addListenersToGraphicalObjects() {}
}
