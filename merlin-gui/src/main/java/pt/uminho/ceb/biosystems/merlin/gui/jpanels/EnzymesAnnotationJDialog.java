package pt.uminho.ceb.biosystems.merlin.gui.jpanels;

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
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.annotation.AnnotationEnzymesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.ComboBoxColumn;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.CreateImageIcon;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.LinkOut;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MyJTable;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;

public class EnzymesAnnotationJDialog extends javax.swing.JDialog {

	private static final long serialVersionUID = -1L;
	private JPanel jPanel1, jPanel2;
	private JScrollPane jScrollPane;
	private JTextField jTextField;
	private JLabel jLabel1, jLabel2;
	private int locus_tagColumnNumber;
	private int ecnumbersColumnNumber;
	private int ecScoreColumnNumber;
	private MouseListener enzymesMouseAdapter;
	private ItemListener enzymesItemListener;
	private PopupMenuListener enzymesPopupMenuListener;
	private Map<Integer, String> itemsList;
	private Map<Integer, String> values = new HashMap<>();
	private MyJTable newjTable = new MyJTable();
	private AnnotationEnzymesAIB homologyDataContainer;
	private Map<Integer,String> ecMap = new TreeMap<Integer,String>();
	private ComboBoxColumn ecList;
	private int sampleSize;
	private MouseAdapter tableMouseAdapator;
	private TableModelListener tableModelListener;
	@SuppressWarnings("unused")
	private int selectedModelRow;
	private WorkspaceDataTable data;
	private String blastDatabase;

	/**
	 * @param sampleSize
	 * @param ecnumbersColumnNumber
	 * @param ecScoreColumnNumber
	 * @param values
	 * @param itemsList
	 * @param locus_tagColumnNumber
	 * @param data
	 * @param homologyDataContainer
	 */
	public EnzymesAnnotationJDialog(String blastDatabase, int sampleSize, int ecnumbersColumnNumber, int ecScoreColumnNumber, Map<Integer, String> values, Map<Integer, String> itemsList, 
			int locus_tagColumnNumber, WorkspaceDataTable data, AnnotationEnzymesAIB homologyDataContainer, Map<Integer,String> ecMap) {

		super(Workbench.getInstance().getMainFrame());
		this.locus_tagColumnNumber = locus_tagColumnNumber;
		this.ecnumbersColumnNumber = ecnumbersColumnNumber;
		this.ecScoreColumnNumber = ecScoreColumnNumber;
		this.itemsList = itemsList;
		this.homologyDataContainer = homologyDataContainer;
		this.values = values;
		this.data = data;
		this.sampleSize = sampleSize;
		this.ecMap = ecMap;
		this.blastDatabase = blastDatabase;

		this.addMouseListener();
		this.addTableModelListener();
		if(this.enzymesItemListener==null)
			this.enzymesItemListener = this.getComboBoxEnzymesItemListener();
		if(this.enzymesMouseAdapter==null)
			this.enzymesMouseAdapter = this.getComboBoxEnzymesMouseListener();
		if(this.enzymesPopupMenuListener==null)
			this.enzymesPopupMenuListener = this.getComboBoxEnzymesPopupMenuListener();

		initGUI();
		Utilities.centerOnOwner(this);
		this.setVisible(true);		
//		this.setAlwaysOnTop(true);
		this.toFront();
	}

	private void initGUI() {

		try {
			
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
			thisLayout.columnWidths = new int[] {7, 7, 7};
			thisLayout.rowWeights = new double[] {0.0, 200.0, 0.0, 0.0, 0.0};
			thisLayout.rowHeights = new int[] {7, 50, 7, 3, 7};
			this.setLayout(thisLayout);
			this.setPreferredSize(new Dimension(875, 585));
			this.setSize(550, 600);
			{
				this.setTitle("sample selection window");

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
				jScrollPane.setPreferredSize(new java.awt.Dimension(700, 700));
				jScrollPane.setSize(900, 420);
				{
					jPanel2 = new JPanel();
					GridBagLayout jPanel2Layout = new GridBagLayout();
					jPanel1.add(jPanel2, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanel2.setBounds(1, 41, 376, 79);
					jPanel2.setBorder(BorderFactory.createTitledBorder("options"));
					jPanel2Layout.rowWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0, 0.1};
					jPanel2Layout.rowHeights = new int[] {7, 7, 7, 7, 7, 7};
					jPanel2Layout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
					jPanel2Layout.columnWidths = new int[] {7, 20, 7, 7};
					jPanel2.setLayout(jPanel2Layout);
					{

						jLabel1 = new JLabel();
						jPanel2.add(jLabel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabel1.setText("sample size:");
						jLabel1.setLabelFor(jTextField);

						jLabel2 = new JLabel();
						jPanel2.add(jLabel2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

						jTextField = new JTextField(""+getSampleSize(), 2);
						jPanel2.add(jTextField, new GridBagConstraints(1, 1, 2, 1, 2.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jTextField.setText(""+getSampleSize());
						jTextField.setToolTipText("Enter the size for the sample");
						jTextField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED),null));
						jTextField.setBounds(164, 57, 36, 20);

						JButton jButtonBestAlpha = new JButton();
						jPanel2.add(jButtonBestAlpha, new GridBagConstraints(4, 2, 2, 1, 50.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jButtonBestAlpha.setText("find best parameters");
						jButtonBestAlpha.setToolTipText("Press to find best parameters.");
						jButtonBestAlpha.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Find.png")),0.1).resizeImageIcon());
						jButtonBestAlpha.setBounds(1, 1, 40, 20);
						jButtonBestAlpha.addActionListener(new ActionListener() {

							public void actionPerformed(ActionEvent arg0) {

								Map<Integer, String> ecCurated = ecList.getValues();

								try {

									writeNewFile();

									ParamSpec[] paramsSpec = new ParamSpec[]{
											new ParamSpec("ecCurated", Map.class, ecCurated, null),
											new ParamSpec("ecMap", Map.class, ecMap, null),
											new ParamSpec("homologyDataContainer", AnnotationEnzymesAIB.class, homologyDataContainer, null),
											new ParamSpec("ecnumbersColumnNumber", Integer.class, ecnumbersColumnNumber, null),
											new ParamSpec("blastDatabase", String.class, blastDatabase, null),
											new ParamSpec("ecScoreColumnNumber", Integer.class, ecScoreColumnNumber, null)
									};

									for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
										if (def.getID().equals("operations.AnnotationEnzymesBestAlpha.ID")){

											Workbench.getInstance().executeOperation(def, paramsSpec);
										}
									}

								}
								catch (Exception e) {
									e.printStackTrace();
									Workbench.getInstance().error("an error occurred while calculating best parameters");
									
								}

								simpleFinish();
							}});
					}
					{
						JButton jButtonNewSample = new JButton();
						jPanel2.add(jButtonNewSample, new GridBagConstraints(4, 1, 1, 1, 50.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jButtonNewSample.setText("new sample");
						jButtonNewSample.setToolTipText("generate new sample");
						jButtonNewSample.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Synchronize.png")),0.1).resizeImageIcon());
						jButtonNewSample.setBounds(1, 1, 40, 20);
						jButtonNewSample.addActionListener(new ActionListener() {

							public void actionPerformed(ActionEvent arg0) {

								if (isInteger(jTextField.getText()) == true){
									int size = Integer.parseInt(jTextField.getText());
									if (size > 0){
										updateSampleSize(size);
//										generateTable(false);
										
										ParamSpec[] paramsSpec = new ParamSpec[]{
												new ParamSpec("locusTagColumnNumber", Integer.class, locus_tagColumnNumber, null),
												new ParamSpec("homologyDataContainer", AnnotationEnzymesAIB.class, homologyDataContainer, null),
												new ParamSpec("ecnumbersColumnNumber", Integer.class, ecnumbersColumnNumber, null),
												new ParamSpec("ecScoreColumnNumber", Integer.class, ecScoreColumnNumber, null),
												new ParamSpec("sampleSize", Integer.class, sampleSize, null),
												new ParamSpec("itemsList", Map.class, itemsList, null),
												new ParamSpec("blastDatabase", String.class, blastDatabase, null),
												new ParamSpec("searchFile", Boolean.class, false, null),
										};

										for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
											if (def.getID().equals("operations.AnnotationEnzymesParametersSetting.ID")){

												Workbench.getInstance().executeOperation(def, paramsSpec);
											}
										}
										
										simpleFinish();
										
//										jLabel2.setText("sample retrieved: " + getTableSize());
									}
									else{
										JOptionPane.showMessageDialog(rootPane, "just positive values allowed");
									}
								}
								else{
									JOptionPane.showMessageDialog(rootPane, "please insert an integer");
								}
							}});
					}
					
					{
						JButton jButtonExport = new JButton();
						jPanel2.add(jButtonExport, new GridBagConstraints(5, 1, 1, 1, 50.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jButtonExport.setText("export file");
						jButtonExport.setToolTipText("export to excel file (xls)");
						jButtonExport.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Download.png")),0.1).resizeImageIcon());
						jButtonExport.setBounds(1, 1, 40, 20);
						jButtonExport.addActionListener(new ActionListener() {

							public void actionPerformed(ActionEvent arg0) {

								try {

									JFileChooser fc = new JFileChooser();
									fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
									fc.setDialogTitle("Select directory");
									int returnVal = fc.showOpenDialog(new JTextArea());

									if (returnVal == JFileChooser.APPROVE_OPTION) {

										File file = fc.getSelectedFile();
										String excelFileName = file.getAbsolutePath();
										Calendar cal = new GregorianCalendar();

										// Get the components of the time
										int hour24 = cal.get(Calendar.HOUR_OF_DAY);     // 0..23
										int min = cal.get(Calendar.MINUTE);             // 0..59
										int day = cal.get(Calendar.DAY_OF_YEAR);		//0..365
										
										String name = homologyDataContainer.getWorkspace().getName();
										
										excelFileName += "/"+name+"_"+hour24+"_"+min+"_"+day+".xlsx";
										
										String sheetName = "annotation";

										XSSFWorkbook wb = new XSSFWorkbook();
										Sheet sheet = wb.createSheet(sheetName) ;
										
										WorkspaceDataTable table = createTableToExport();
										
										Row row = sheet.createRow(0);
										
										TableColumnModel tc = newjTable.getColumnModel();

										int i = 0;
										
										while (i < tc.getColumnCount()) {
											
											row.createCell(i).setCellValue(tc.getColumn(i).getHeaderValue().toString());
											i++;
										}


										for (int r=0;r < table.getRowCount(); r++ )
										{
											row = sheet.createRow(r+2);
									
											//iterating c number of columns
											for (int c=0;c < table.getColumnCount(); c++ )
											{
												Cell cell = row.createCell(c);
												
												cell.setCellValue(table.getValueAt(r, c).toString());
											}
										}
										
										FileOutputStream fileOut = new FileOutputStream(excelFileName);
										
										//write this workbook to an Outputstream.
										wb.write(fileOut);
										fileOut.flush();
										wb.close();
										fileOut.close();
										
										Workbench.getInstance().info("data successfully exported.");
									}
								} catch (Exception e) {

									Workbench.getInstance().error("an error occurred while performing this operation. Error "+e.getMessage());
									e.printStackTrace();
								}
							}});
					}
					
					{
						JButton jButtonSave = new JButton();
						jPanel2.add(jButtonSave, new GridBagConstraints(6, 1, 1, 1, 50.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jButtonSave.setText("save");
						jButtonSave.setToolTipText("save data");
						jButtonSave.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Save.png")),0.1).resizeImageIcon());
						jButtonSave.setBounds(1, 1, 40, 20);
						jButtonSave.addActionListener(new ActionListener() {

							public void actionPerformed(ActionEvent arg0) {

								try {
									writeNewFile();
									JOptionPane.showMessageDialog(rootPane, "data saved successfully");
								} catch (IOException e) {

									e.printStackTrace();
									JOptionPane.showMessageDialog(rootPane, "error - data not saved!");
								}
							}});
					}
					{
						JButton jButtonClose = new JButton();
						jPanel2.add(jButtonClose, new GridBagConstraints(6, 2, 1, 1, 50.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jButtonClose.setText("close");
						jButtonClose.setToolTipText("close window");
						jButtonClose.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Cancel.png")),0.1).resizeImageIcon());
						jButtonClose.setBounds(1, 1, 40, 20);
						jButtonClose.addActionListener(new ActionListener() {

							public void actionPerformed(ActionEvent arg0) {
								
								simpleFinish();
							}});
					}
				}
			}
			
			newjTable.setModel(data);
			newjTable.setSortableFalse();
			ecList = new ComboBoxColumn(newjTable, 1, values , this.enzymesItemListener, this.enzymesMouseAdapter, this.enzymesPopupMenuListener);
			
			newjTable.setRowHeight(20);
					
			jScrollPane.setViewportView(newjTable);

			jLabel2.setText("sample retrieved: " + getTableSize());

//			this.setModal(true);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private void updateSampleSize (int newSize){
		sampleSize=newSize;
	}

	private int getSampleSize(){
		return sampleSize;
	}

	private boolean isInteger(String str){
		try {
			Integer.parseInt(str);
			return true;
		} 
		catch (NumberFormatException nfe) {
			return false;
		}
	}
	private int getTableSize(){
		return ecMap.size();
	}

//	private void generateTable(boolean searchFile){ 		
//
//		double userThreshold = homologyDataContainer.getThreshold();
//		double userAlpha = homologyDataContainer.getAlpha();
//		homologyDataContainer.setThreshold(0.0);
//		homologyDataContainer.setAlpha(0.5);
//
//		
//		
//		
//		
//		if(data!=null && searchFile == true){
//
//			DataTable fileTable = eaps.buildTable(data);
//
//			newjTable.setModel(fileTable);
//.setSortableFalse();
//			ecMap = eaps.getEcMap();
//			
//			for (int i: data.keySet())
//				values.put(Integer.parseInt(ecMap.get(i)), data.get(i));
//
//		}
//		else{
//
//			eaps.generateRandomSample(sampleSize);
//			
//			ecMap = eaps.getEcMap();
//			
//			DataTable randomTable = eaps.buildTable(ecMap);
//
//			newjTable.setModel(randomTable);
//
//			for(int i  : ecMap.keySet())
//				values.put(Integer.parseInt(ecMap.get(i)), this.itemsList.get(i));
//
//		}
//
//		ecList = new ComboBoxColumn(newjTable, 1, values , this.enzymesItemListener, this.enzymesMouseAdapter, this.enzymesPopupMenuListener);
//		
//		newjTable.setRowHeight(20);
//				
//		jScrollPane.setViewportView(newjTable);
//		
//
//		homologyDataContainer.setAlpha(userAlpha);
//		homologyDataContainer.setThreshold(userThreshold);
//	}

	private ItemListener getComboBoxEnzymesItemListener() {

		return new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {

				processEnzymesComboBoxChange(e);
			}
		};
	}

	private MouseAdapter getComboBoxEnzymesMouseListener() {

		return new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				Point p = e.getPoint();

				int  columnNumber = newjTable.columnAtPoint(p);
				newjTable.setColumnSelectionInterval(columnNumber, columnNumber);

				int selectedModelRow = newjTable.getSelectedRow();
				homologyDataContainer.setSelectedRow(selectedModelRow);

				int myRow = newjTable.getSelectedRow();

				if(myRow>-1 && newjTable.getRowCount()>0 && newjTable.getRowCount()> myRow) {

					newjTable.setRowSelectionInterval(myRow, myRow);
					scrollToVisible(newjTable.getCellRect(myRow, -1, true));
				}

				processEnzymesComboBoxChange(e);
			}
		};		
	}

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

	@SuppressWarnings("unchecked")
	private void processEnzymesComboBoxChange(EventObject e) {

		boolean go = false;
		JComboBox<String> comboBox = null;

		if(e.getClass()==MouseEvent.class) {

			Object obj = ((MouseEvent) e).getSource();

			if(obj instanceof JComboBox)
				comboBox = (JComboBox<String>) obj;

			ListSelectionModel model = newjTable.getSelectionModel();
			model.setSelectionInterval( ecList.getSelectIndex(comboBox), ecList.getSelectIndex(comboBox));

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
			int row = ecList.getSelectIndex(comboBox);
			comboBox.setSelectedIndex(comboBox.getSelectedIndex());
			ecList.getValues().put(row, comboBox.getSelectedItem().toString());
			ecList.setComboBox(ecList.getSelectIndex(comboBox), comboBox);
		}
	}

	private void scrollToVisible(final Rectangle visible) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				newjTable.scrollRectToVisible(visible);
			}
		});
	}

	private void addMouseListener() {

		if(this.tableMouseAdapator==null)
			this.tableMouseAdapator = this.getTableMouseAdapator();

		newjTable.addMouseListener(this.tableMouseAdapator);
	}

	private MouseAdapter getTableMouseAdapator() {

		MouseAdapter mouseAdapter = new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {

				selectedModelRow=newjTable.getSelectedRow();

				if(newjTable.getSelectedRow()>-1 && newjTable.getRowCount()>0 && newjTable.getRowCount()> newjTable.getSelectedRow()) {

					newjTable.setRowSelectionInterval(newjTable.getSelectedRow(), newjTable.getSelectedRow());
					scrollToVisible(newjTable.getCellRect(newjTable.getSelectedRow(), -1, true));
				}

				{
					Point p = arg0.getPoint();
					int  columnNumber = newjTable.columnAtPoint(p);
					newjTable.setColumnSelectionInterval(columnNumber, columnNumber);
				}
			}
		};
		return mouseAdapter;
	}

	private void addTableModelListener() {

		if(this.tableModelListener == null)
			this.tableModelListener = this.getTableModelListener();

		newjTable.getModel().addTableModelListener(this.tableModelListener);

	}

	private TableModelListener getTableModelListener() {

		TableModelListener tableModelListener = new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {

				if(newjTable.getSelectedRow()>-1) {

					selectedModelRow=newjTable.getSelectedRow();
				}
			}};
			return tableModelListener;
	}

	public void simpleFinish() {
		
		this.setVisible(false);
		this.dispose();
	}

	private void writeNewFile() throws IOException{
		
		try {
			String projectName = homologyDataContainer.getWorkspace().getName();
			
			Long taxonomyID = homologyDataContainer.getWorkspace().getTaxonomyID();
			
			String fileName =  "AutoGeneSelection_" + this.blastDatabase + ".txt";
			
			if(blastDatabase.isEmpty())
				fileName =  "AutoGeneSelection.txt";
			
			String path = FileUtils.getWorkspaceTaxonomyFolderPath(projectName, taxonomyID) + fileName;
			File file = new File(path);
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
	
			for (int i: ecMap.keySet())
				out.write(i + "\t" + ecList.getValues().get(Integer.parseInt(ecMap.get(i))) + "\n");
			
			out.close();
		}
		catch (IOException e) {
			
			System.out.println("FILE NOT FOUND!!");
			
//			e.printStackTrace();
		}
	}

	/**
	 * Method to generate a table containing the information to be exported.
	 * @return DataTable
	 */
	private WorkspaceDataTable createTableToExport(){
		
		List<String> columnsNames = Arrays.asList("gene", "EC number");
		
		WorkspaceDataTable data = new WorkspaceGenericDataTable(columnsNames, "", "");  
		
		for (int i = 0; i < newjTable.getRowCount(); i++){
			
			ArrayList<Object> line = new ArrayList<>();
			
			line.add(newjTable.getValueAt(i, 0));
			line.add(ecList.getSelectItem(i));
			
			data.addLine(line);
		}
		return data;		
	}

}



