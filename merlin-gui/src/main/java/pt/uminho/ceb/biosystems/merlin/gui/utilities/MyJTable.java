package pt.uminho.ceb.biosystems.merlin.gui.utilities;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;


public class MyJTable extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyJTable() {
		
		super();
//		this.getDefaultRenderer(Object.class);
		this.setRenderer(Object.class);
		this.showGrid(false);
		this.setAutoCreateRowSorter(true);
		this.getTableHeader().setResizingAllowed(true);
		this.getTableHeader().setReorderingAllowed(false);
		this.getTableHeader().setAutoscrolls(true);
		this.getTableHeader().setEnabled(true);
	}

	public MyJTable(TableModel dm) {
		super(dm);
//		this.getDefaultRenderer(Object.class);
		this.setRenderer(Object.class);
		this.showGrid(false);
		this.setAutoCreateRowSorter(true);
		this.getRowSorter().addRowSorterListener(null);
		this.getTableHeader().setResizingAllowed(true);
		this.getTableHeader().setReorderingAllowed(false);
		this.getTableHeader().setAutoscrolls(true);
		this.getTableHeader().setEnabled(true);
	}

	public MyJTable(TableModel dm, TableColumnModel cm) {
		super(dm, cm);
//		this.getDefaultRenderer(Object.class);
		this.setRenderer(Object.class);
		this.showGrid(false);
		this.setAutoCreateRowSorter(true);
		this.getRowSorter().addRowSorterListener(null);
		this.getTableHeader().setResizingAllowed(true);
		this.getTableHeader().setReorderingAllowed(false);
		this.getTableHeader().setAutoscrolls(true);
		this.getTableHeader().setEnabled(true);
		}

	public MyJTable(int numRows, int numColumns) {
		super(numRows, numColumns);
//		this.getDefaultRenderer(Object.class);
		this.setRenderer(Object.class);
		this.showGrid(false);
		this.setAutoCreateRowSorter(true);
		this.getRowSorter().addRowSorterListener(null);
		this.getTableHeader().setResizingAllowed(true);
		this.getTableHeader().setReorderingAllowed(false);
		this.getTableHeader().setAutoscrolls(true);
		this.getTableHeader().setEnabled(true);
	}

	@SuppressWarnings("rawtypes")
	public MyJTable(Vector<? extends Vector>  rowData, Vector<? extends Vector>  columnNames) {
		super(rowData, columnNames);
//		this.getDefaultRenderer(Object.class);
		this.setRenderer(Object.class);
		this.showGrid(false);
		this.setAutoCreateRowSorter(true);
		this.getRowSorter().addRowSorterListener(null);
		this.getTableHeader().setResizingAllowed(true);
		this.getTableHeader().setReorderingAllowed(false);
		this.getTableHeader().setAutoscrolls(true);
		this.getTableHeader().setEnabled(true);
	}

	public MyJTable(Object[][] rowData, Object[] columnNames) {
		super(rowData, columnNames);
//		this.getDefaultRenderer(Object.class);
		this.setRenderer(Object.class);
		this.showGrid(false);
		this.setAutoCreateRowSorter(true);
		this.getRowSorter().addRowSorterListener(null);
		this.getTableHeader().setResizingAllowed(true);
		this.getTableHeader().setReorderingAllowed(false);
		this.getTableHeader().setAutoscrolls(true);
		this.getTableHeader().setEnabled(true);
	}

	public MyJTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
		super(dm, cm, sm);
//		this.getDefaultRenderer(Object.class);
		this.setRenderer(Object.class);
		this.showGrid(false);
		this.setAutoCreateRowSorter(true);
		this.getRowSorter().addRowSorterListener(null);
		this.getTableHeader().setResizingAllowed(true);
		this.getTableHeader().setReorderingAllowed(false);
		this.getTableHeader().setAutoscrolls(true);
		this.getTableHeader().setEnabled(true);
	}

	private void setRenderer(Class<?> columnClass) {
		
		DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) super.getDefaultRenderer(columnClass);
		centerRenderer.setHorizontalAlignment( SwingUtilities.CENTER );
		super.setDefaultRenderer(columnClass, centerRenderer);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#setDefaultRenderer(java.lang.Class, javax.swing.table.TableCellRenderer)
	 */
	public void setDefaultRenderer(Class<?> columnClass, TableCellRenderer renderer) {

		super.setDefaultRenderer(columnClass,null);
		super.setDefaultRenderer(columnClass, renderer);
		
	}

	private void showGrid(boolean show) {

		super.setShowGrid(show);
	}
	
	public void setSortableFalse() {
		
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(this.getModel());
		this.setRowSorter(sorter);
		
		for(int i = 0; i< this.getColumnCount(); i++) {
			
			sorter.setSortable(i, false);
		}
	}

}
