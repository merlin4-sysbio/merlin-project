package pt.uminho.ceb.biosystems.merlin.gui.views.windows;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.CreateImageIcon;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.LinkOut;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MyJTable;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.StarColumn;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.TextAreaEditor;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.TextAreaRenderer;
import pt.uminho.ceb.biosystems.merlin.utilities.OpenBrowser;


public class GenericDetailWindowBlast extends javax.swing.JDialog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1;
	private JPanel jPanel1;
	private JScrollPane jScrollPane;
	private MyJTable jTable;
	private JPanel jPanel11;
	private JPanel jPanel12;
	private JComboBox<String> searchComboBox;
	private WorkspaceDataTable[] dataTable;
	private MouseAdapter mouseAdaptor;
	private StarColumn buttonStarColumn;
	private int previousPanel;

	/**
	 * @param frame
	 * @param dataTable
	 * @param windowName
	 * @param name
	 */
	public GenericDetailWindowBlast(WorkspaceDataTable[] dataTable, String windowName, String name) {
		
		super(Workbench.getInstance().getMainFrame());
		this.dataTable = dataTable;
		this.initGUI(windowName, name);
		Utilities.centerOnOwner(this);
		this.setIconImage((new ImageIcon(getClass().getClassLoader().getResource("icons/merlin.png"))).getImage());
		this.setVisible(true);		
		this.setAlwaysOnTop(true);
		this.toFront();
		this.previousPanel = 0;
		
	}

	/**
	 * @param windowName
	 * @param name
	 */
	private void initGUI(String windowName, String name) {
		
		try {
			
			this.setTitle(windowName);

			jPanel1 = new JPanel();
			GridBagLayout jPanel1Layout = new GridBagLayout();
			jPanel1.setLayout(jPanel1Layout);
			getContentPane().add(jPanel1, BorderLayout.CENTER);
			jPanel1.setPreferredSize(new java.awt.Dimension(850, 600));
			jPanel1Layout.columnWeights = new double[] {0.1};
			jPanel1Layout.columnWidths = new int[] {7};
			jPanel1Layout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanel1Layout.rowHeights = new int[] {7, 7, 7};

			jPanel11 = new JPanel();
			GridBagLayout jPanel11Layout = new GridBagLayout();
			jPanel11Layout.rowWeights = new double[] {0.1};
			jPanel11Layout.rowHeights = new int[] {7};
			jPanel11Layout.columnWeights = new double[] {0.1};
			jPanel11Layout.columnWidths = new int[] {7};
			jPanel11.setLayout(jPanel11Layout);

			if(!name.equals("")) {
				
				JLabel j = new JLabel(name);
				jPanel1.add(j, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.015, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}

			jPanel1.add(jPanel11, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.92, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 0, 0, 0), 0, 0));
			jScrollPane = new JScrollPane();
			jPanel11.add(jScrollPane, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jTable = new MyJTable();
			jTable.setShowGrid(false);
			jTable.setModel(dataTable[0]);
			//jTable.setSortableFalse();
			
			jScrollPane.setViewportView(jTable);
			jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

			jPanel12 = new JPanel();
			GridBagLayout jPanel12Layout = new GridBagLayout();
			jPanel12.setLayout(jPanel12Layout);
			jPanel12.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

			jPanel1.add(jPanel12, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.05, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanel12Layout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1};
			jPanel12Layout.columnWidths = new int[] {7, 20, 7, 7};
			jPanel12Layout.rowWeights = new double[] {0.1};
			jPanel12Layout.rowHeights = new int[] {7};

			JButton button1 = new JButton("Close");
			button1.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Cancel.png")),0.1).resizeImageIcon());
			jPanel12.add(button1, new GridBagConstraints(1, 0, 1, 1, 0.95, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			button1.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent evt) {
				
					finish();
				}
			});

			//jTable.setAutoCreateRowSorter(true);
			this.changeColumnsWidth();
			buttonStarColumn = this.buildStarColumn(2);
			
			if(dataTable[0].getRowCount()>0) {
				
				String[] names = new String[dataTable.length];
				
				for(int s=0;s<this.dataTable.length;s++)					
					names[s] = this.dataTable[s].getName();

				ComboBoxModel<String> searchComboBoxModel = new DefaultComboBoxModel<>(names);
				searchComboBox = new JComboBox<>();
				searchComboBox.setModel(searchComboBoxModel);

				jPanel12.add(searchComboBox, new GridBagConstraints(3, 0, 1, 1, 0.05, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				searchComboBox.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						searchComboBoxActionPerformed(evt);
					}
				});
			}
			
			this.setModal(true);
			this.setSize(1020, 700);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param i
	 * @return
	 */
	private StarColumn buildStarColumn(final int i) {
		return new StarColumn(jTable, i, new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				processStarButton(arg0);}
		},
		new MouseAdapter() {
			
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
				model.setSelectionInterval( rowNumber, rowNumber );
				jTable.setRowSelectionInterval(-1, -1);
				processStarButton(e);
			}
		}, null);
	}
	
	/**
	 * 
	 */
	public void finish() {
		this.setVisible(false);
		this.dispose();
		pack();
	}

	/**
	 * @param evt
	 */
	private void searchComboBoxActionPerformed(ActionEvent evt) {
		
		int row = this.jTable.getSelectedRow();
		
		if(row>-1)			
			row = this.jTable.convertRowIndexToModel(this.jTable.getSelectedRow());

		WorkspaceDataTable data = this.dataTable[this.searchComboBox.getSelectedIndex()];
		
		if(data.getRowCount()>0) {
			
			this.jTable.setModel(data);
			jTable.setSortableFalse();
		}
		
		if((searchComboBox.getSelectedItem().toString().contains("Homology") && !searchComboBox.getSelectedItem().toString().contains("InterPro")) || !searchComboBox.getSelectedItem().toString().contains("Taxonomy")) {
		//if((this.dataTable.length==4 && this.searchComboBox.getSelectedIndex()<2) || (this.dataTable.length>4 && this.searchComboBox.getSelectedIndex()<4)) {
			
			if(row>-1 && this.searchComboBox.getSelectedIndex()<2 && this.previousPanel<2) {
				
				//this.jTable.setAutoCreateRowSorter(true);
				ListSelectionModel model = jTable.getSelectionModel();
				model.setSelectionInterval( row, row);
			}
			else if(row>-1 && this.dataTable.length>4 && this.searchComboBox.getSelectedIndex()>1 && this.searchComboBox.getSelectedIndex()<4 && this.previousPanel>1 && this.previousPanel<4) {
				
				//this.jTable.setAutoCreateRowSorter(true);
				ListSelectionModel model = jTable.getSelectionModel();
				model.setSelectionInterval( row, row);
			}
			//this.changeColumnsWidth();
		}
		
		if(searchComboBox.getSelectedItem().toString().contains("Sequence")) {
		//if((this.dataTable.length==4 && this.searchComboBox.getSelectedIndex()==2) || (this.dataTable.length>4 && this.searchComboBox.getSelectedIndex()==4)) {
			
			TextAreaRenderer renderer = new TextAreaRenderer();
			TextAreaEditor editor = new TextAreaEditor();
			for (int j = 0; j < jTable.getColumnModel().getColumnCount(); j++) {
				
				jTable.getColumnModel().getColumn(j).setCellRenderer(renderer);
				jTable.getColumnModel().getColumn(j).setCellEditor(editor);
				jTable.setAutoResizeMode(MyJTable.AUTO_RESIZE_ALL_COLUMNS);
			}
			this.addlisteners(false);
		}
		
		if(searchComboBox.getSelectedItem().toString().contains("Homology")  && !searchComboBox.getSelectedItem().toString().contains("InterPro"))
//		if(this.searchComboBox.getSelectedIndex()==0 || (this.dataTable.length>4 && this.searchComboBox.getSelectedIndex()==2))			
			this.buttonStarColumn = this.buildStarColumn(2);

		this.changeColumnsWidth();
		this.previousPanel = this.searchComboBox.getSelectedIndex();
		
		this.jScrollPane.setViewportView(this.jTable);
	}

	// Method for changing the columns widths	
	/**
	 * 
	 */
	private void changeColumnsWidth() {
		
		jTable.setAutoResizeMode(MyJTable.AUTO_RESIZE_OFF);
		TableColumnModel colModel = jTable.getColumnModel();
		int colCount = colModel.getColumnCount();
		int headerWidth;
		/* default width set to prevent the columns width to be set to a very small value */
		int defaultWidth = 980/colCount; 

		/*Setting each column width as the maximum between the width of the header 
		 and a default width*/
		
		for(int i=0; i<colCount; i++)  {
			
			TableCellRenderer renderer = colModel.getColumn(i).getHeaderRenderer();
			
			if (renderer == null)				
				renderer = this.jTable.getTableHeader().getDefaultRenderer();

			Component comp = renderer.getTableCellRendererComponent(jTable,colModel.getColumn(i).getHeaderValue(), false, false, 0, 0);

			headerWidth = comp.getPreferredSize().width;

			if(headerWidth>=defaultWidth) {
				
				colModel.getColumn(i).setPreferredWidth(headerWidth);
				colModel.getColumn(i).setMaxWidth(headerWidth);
				colModel.getColumn(i).setMinWidth(headerWidth);
			}
			else {
				
				colModel.getColumn(i).setPreferredWidth(defaultWidth);
				colModel.getColumn(i).setMaxWidth(defaultWidth);
				colModel.getColumn(i).setMinWidth(defaultWidth);
			}
		}
		addlisteners(true);
	}

	/**
	 * @param addResize
	 */
	private void addlisteners(boolean addResize) {
		
		if(mouseAdaptor!=null)			
			jTable.removeMouseListener(mouseAdaptor);

		mouseAdaptor = newMouseAdapter(addResize);
		jTable.addMouseListener(mouseAdaptor);
	}
	
	/**
	 * @param addResize
	 * @return
	 */
	private MouseAdapter newMouseAdapter(final boolean addResize) {
		
		return (new MouseAdapter() {
		
			public void mouseClicked(MouseEvent e) {
				addLinkOutsListener(e);
			
				if(addResize)					
					addTableResizeListener(e);
			}
		});		
	}
	
	/**
	 * @param arg0
	 */
	private void processStarButton(EventObject arg0) {
		
		JButton button = null;
		if(arg0.getClass()==ActionEvent.class) {
			
			button = (JButton)((ActionEvent) arg0).getSource();
			ListSelectionModel model = jTable.getSelectionModel();
			model.setSelectionInterval( buttonStarColumn.getSelectIndex(button), buttonStarColumn.getSelectIndex(button));

		}
		
		if(arg0.getClass()==MouseEvent.class) {
			
			Point p = ((MouseEvent) arg0).getPoint();
			int rowNumber = jTable.rowAtPoint(p);
			int  columnNumber = jTable.columnAtPoint(p);
			jTable.setColumnSelectionInterval(columnNumber, columnNumber);
			button = (JButton) buttonStarColumn.getValueArray().get(rowNumber);
		}

		if(button!=null) {
			
			OpenBrowser  openUrl = new OpenBrowser();
			openUrl.setUrl("http://www.uniprot.org/uniprot/?query="+(String)jTable.getValueAt(jTable.getSelectedRow(), 1)+"&sort=score");
			openUrl.openURL();
		}
	}
	
	
	/**
	 * @param arg0
	 */
	private void addLinkOutsListener(MouseEvent arg0) {

		if(arg0.getButton()==MouseEvent.BUTTON3) {
			// get the coordinates of the mouse click
			Point p = arg0.getPoint();
			// get the row index that contains that coordinate
			int rowNumber = jTable.rowAtPoint(p);
			int  columnNumber = jTable.columnAtPoint(p);
			jTable.setColumnSelectionInterval(columnNumber, columnNumber);
			// Get the ListSelectionModel of the MyJTable
			ListSelectionModel model = jTable.getSelectionModel();
			// set the selected interval of rows. Using the "rowNumber"
			// variable for the beginning and end selects only that one row.
			model.setSelectionInterval( rowNumber, rowNumber );
		}
		
		if(searchComboBox.getSelectedItem().toString().contains("Homology") && !searchComboBox.getSelectedItem().toString().contains("InterPro") &&  jTable.getSelectedColumn()==(2)) {
//			if((searchComboBox.getSelectedIndex()==0 || (this.dataTable.length>4 && searchComboBox.getSelectedIndex()==2)) && (jTable.getSelectedColumn()==(new Integer(2)))) {
			if(arg0.getButton()==MouseEvent.BUTTON1) {
				//((JButton) arg0.getSource()).setSelected(true);
				processStarButton(arg0);
			}
		}
		
		if(searchComboBox.getSelectedItem().toString().contains("Homology") && !searchComboBox.getSelectedItem().toString().contains("InterPro") &&  jTable.getSelectedColumn()==(1)) {
		//if((searchComboBox.getSelectedIndex()==0 || (this.dataTable.length>4 && searchComboBox.getSelectedIndex()==2)) &&(jTable.getSelectedColumn()==(new Integer(0)) || jTable.getSelectedColumn()==(new Integer(1)))) {
			
			if(arg0.getButton()==MouseEvent.BUTTON3) {
				
				List<Integer> dbs = new ArrayList<Integer>();
				dbs.add(0);
				dbs.add(1);
				new LinkOut(dbs, (String)jTable.getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn())).show(arg0.getComponent(),arg0.getX(), arg0.getY());
			}
		}
		
		if(searchComboBox.getSelectedItem().toString().contains("Homology") && !searchComboBox.getSelectedItem().toString().contains("InterPro") &&  jTable.getSelectedColumn()==(new Integer(7))) {
		//if((searchComboBox.getSelectedIndex()==0 || (this.dataTable.length>4 && searchComboBox.getSelectedIndex()==2)) && jTable.getSelectedColumn()==(new Integer(7))) {
			
			if(arg0.getButton()==MouseEvent.BUTTON3) {
				List<Integer> dbs = new ArrayList<Integer>();
				dbs.add(1);
				dbs.add(3);
				new LinkOut(dbs, (String)jTable.getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn())).show(arg0.getComponent(),arg0.getX(), arg0.getY());
			}
		}
		
		if(searchComboBox.getSelectedItem().toString().contains("Homology") && !searchComboBox.getSelectedItem().toString().contains("InterPro") &&  jTable.getSelectedColumn()==(new Integer(6))) {
		//if((searchComboBox.getSelectedIndex()==0 || (this.dataTable.length>4 && searchComboBox.getSelectedIndex()==2)) && jTable.getSelectedColumn()==(new Integer(8))) {
		
			if(arg0.getButton()==MouseEvent.BUTTON3) {
				List<Integer> dbs = new ArrayList<Integer>();
				dbs.add(1);
				dbs.add(2);
				new LinkOut(dbs, (String)jTable.getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn())).show(arg0.getComponent(),arg0.getX(), arg0.getY());
			}
		}
		
		if(searchComboBox.getSelectedItem().toString().contains("Taxonomy") &&  jTable.getSelectedColumn()==(new Integer(0))) {
		//if((searchComboBox.getSelectedIndex()==1 || (this.dataTable.length>4 && searchComboBox.getSelectedIndex()==3)) &&(jTable.getSelectedColumn()==(new Integer(0)))) {
			
			if(arg0.getButton()==MouseEvent.BUTTON3) {
				List<Integer> dbs = new ArrayList<Integer>();
				dbs.add(5);
				new LinkOut(dbs, ((String)jTable.getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn())).replace(" ", "+")).show(arg0.getComponent(),arg0.getX(), arg0.getY());
			}
		}

		if(searchComboBox.getSelectedItem().toString().contains("Sequence") &&  jTable.getSelectedColumn()==(new Integer(0))) {
		//if(((this.dataTable.length==4 && searchComboBox.getSelectedIndex()==2 ) || (this.dataTable.length>4 && searchComboBox.getSelectedIndex()==4)) &&(jTable.getSelectedColumn()==(new Integer(0)))) {
			
			if(arg0.getButton()==MouseEvent.BUTTON3) {
				List<Integer> dbs = new ArrayList<Integer>();
				dbs.add(4);
				new LinkOut(dbs, ((String)jTable.getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn())).replace("\n", "")).show(arg0.getComponent(),arg0.getX(), arg0.getY());
			}
		}
		
		if(searchComboBox.getSelectedItem().toString().contains("InterPro") &&  jTable.getSelectedColumn()==(new Integer(8))) {
				
				if(arg0.getButton()==MouseEvent.BUTTON3) {
					
					List<Integer> dbs = new ArrayList<Integer>();
					dbs.add(6);
					new LinkOut(dbs, ((String)jTable.getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn())).replace("\n", "")).show(arg0.getComponent(),arg0.getX(), arg0.getY());
				}
			}
	}

	/**
	 * @param e
	 */
	private void addTableResizeListener(MouseEvent e) {
		
		if (e.getButton()==MouseEvent.BUTTON1 && e.getClickCount() == 2) {
			
			MyJTable target = (MyJTable)((MouseEvent) e).getSource();
			int colIndex = target.getSelectedColumn();

			if(colIndex>-1) {
				
				int width = 0;
				TableColumnModel colModel = target.getColumnModel();
				TableColumn col = colModel.getColumn(colIndex);
				int defaultWidth = 980/colModel.getColumnCount();

				TableCellRenderer renderer = col.getHeaderRenderer(); 
				if (renderer == null) {
					
					renderer = target.getTableHeader().getDefaultRenderer();
				}
				Component comp = renderer.getTableCellRendererComponent( target, col.getHeaderValue(), false, false, 0, 0);
				width = (int) comp.getPreferredSize().getWidth();

				/* If the width of the column equals the header width or the default width, 
			sets the width to the maximum width of column data */
				if(	//col.getMaxWidth()==width||
						col.getMaxWidth()==defaultWidth
				){						
					// Get maximum width of column data
					for (int r=0; r<target.getRowCount(); r++)
					{
						renderer = target.getCellRenderer(r, colIndex);
						comp = renderer.getTableCellRendererComponent(target, target.getValueAt(r, colIndex),false, false, r, colIndex);
						width = (int) Math.max(width, comp.getMaximumSize().getWidth());
					}

					// Add margin
					width += 4;

					// Set the width					
					col.setPreferredWidth(width);
					col.setMaxWidth(width);
					col.setMinWidth(width);
				}
				/* If the width of the column is already adjusted to the maximum width of column data, sets the column 
			width as the maximum between the width of the header or a default width */
				else {
					
					//					if(width>=defaultWidth) 
					//					{
					//						col.setPreferredWidth(width);
					//						col.setMaxWidth(width);
					//						col.setMinWidth(width); 
					//					}						
					//					else
					//					{
					col.setPreferredWidth(defaultWidth);
					col.setMaxWidth(defaultWidth);
					col.setMinWidth(defaultWidth); 
					//					}
				}
				e.setSource(e);
			}
		}
	}

}


