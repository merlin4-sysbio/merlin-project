package pt.uminho.ceb.biosystems.merlin.gui.utilities;

import java.awt.Component;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


/**
 * @author Oscar
 *
 */
public class ComboBoxColumn { 

	private Map<Integer, String> values;
	private Map<Integer, JComboBox<String>> comboBoxList;
	private ItemListener eventListener;
	private MouseListener mouseListener;
	private PopupMenuListener popupMenuListener;
	private Set<Integer> rowsWithBoxes;


	/**
	 * @param jTable
	 * @param column
	 * @param values
	 * @param eventListener
	 * @param mouseListener
	 * @param popupMenuListener
	 */
	public ComboBoxColumn(JTable jTable, int column, Map<Integer,String> values, ItemListener eventListener, MouseListener mouseListener, PopupMenuListener popupMenuListener) {

		this.popupMenuListener = popupMenuListener;
		this.eventListener=eventListener;
		this.mouseListener = mouseListener;
		this.values = values;
		this.comboBoxList = new TreeMap<>();
		this.rowsWithBoxes = new HashSet<>();
		TableColumnModel columnModel = jTable.getColumnModel();
		TableColumn dataColumn = columnModel.getColumn(column);
		this.build(dataColumn);
	}

	/**
	 * @param dataColumn
	 */
	public void build(TableColumn dataColumn) {

		dataColumn.setCellRenderer(new TableCellRenderer() {

			public Component getTableCellRendererComponent(JTable table, Object value, 
					boolean isSelected, boolean hasFocus, int row, int column) {
				JComboBox<String> comboBox = null;

				if(row > -1) {
					
					if(comboBoxList.containsKey(row)) {

						if(comboBoxList.get(row) != null) {

							comboBox = comboBoxList.get(row);
							comboBox.setModel(new DefaultComboBoxModel<>((String[])value));

							if(values.containsKey(row))
								comboBox.setSelectedItem(values.get(row));
						}
					}
					else {

						if(rowsWithBoxes.contains(row)) {

							comboBoxList.put(row, null);
							System.err.println("box deleted from map");
							comboBox = createComboBox(value,row);
						}
						else {
							
							comboBox = createComboBox(value,row);
							rowsWithBoxes.add(row);
						}
						
						if(values.containsKey(row))
							comboBox.setSelectedItem(values.get(row));
						
						comboBoxList.put(row, comboBox);
					}
					
				}
				else {

					System.err.println("row "+row);
				}

				return comboBox;
			}
		});

		dataColumn.setCellEditor(new TableCellEditor() {

			public Component getTableCellEditorComponent(JTable table, Object value, boolean flag, int row, int column) {

				if(row != -1) {

					if(values.containsKey(row)){
						
						comboBoxList.get(row).setSelectedItem(values.get(row));
					
					
					}
					comboBoxList.get(row).setToolTipText((String)comboBoxList.get(row).getSelectedItem());
				}

				return comboBoxList.get(row);
			}
			public void addCellEditorListener(CellEditorListener l){}
			public void cancelCellEditing() {}
			public Object getCellEditorValue() {return null;}
			public boolean isCellEditable(EventObject anEvent) {return true;}
			public void removeCellEditorListener(CellEditorListener l){}
			public boolean shouldSelectCell(EventObject anEvent) {return true;}
			public boolean stopCellEditing() {return true;}
		});

	}

	/**
	 * @param value
	 * @param row
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JComboBox<String> createComboBox(Object value, int row) {

		JComboBox<String>  comboBox = null;

		if(value instanceof String[]) {

			comboBox = new JComboBox<String>((String[])value);
			comboBox.setName(row+"");
			comboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
			comboBox.setRenderer(new BasicComboBoxRenderer () {

				private static final long serialVersionUID = 1L;
				public Component getListCellRendererComponent( @SuppressWarnings("rawtypes") JList list,  
						Object value, int index, boolean isSelected, boolean cellHasFocus) {

					if (isSelected) {

						setBackground(list.getSelectionBackground());
						setForeground(list.getSelectionForeground());
						list.setToolTipText((String)value);
					}
					else {

						setBackground(list.getBackground());
						setForeground(list.getForeground());
					}       
					setFont(list.getFont());
					setText((value == null) ? "" : value.toString());      
					return this;
				}   
			});

			if(values.containsKey(row))
				comboBox.setSelectedItem(values.get(row));

			addListenerToComboBox(comboBox);
			addMouseListenerToComboBox(comboBox);
			addPopupMenuListenerComboBox(comboBox);
			comboBox.setToolTipText((String)comboBox.getSelectedItem());
		}
		
		return comboBox;
	}

	/**
	 * @param comboBox
	 */
	private void addListenerToComboBox(JComboBox<String> comboBox) {

		comboBox.addItemListener((ItemListener)eventListener);
	}

	/**
	 * @param comboBox
	 */
	private void addMouseListenerToComboBox(JComboBox<String> comboBox) {

		comboBox.addMouseListener(mouseListener);
	}

	/**
	 * @param comboBox
	 */
	private void addPopupMenuListenerComboBox(JComboBox<String> comboBox) {

		comboBox.addPopupMenuListener(popupMenuListener);
	}

	/**
	 * @param row
	 * @return
	 */
	public String getSelectItem(Integer row) {

		if(comboBoxList.containsKey(row) && comboBoxList.get(row).getSelectedItem()!=null)
				return comboBoxList.get(row).getSelectedItem().toString();
		else
			return values.get(row);
	}

	/**
	 * @param comboBox
	 * @return
	 */
	public int getSelectIndex(JComboBox<String> comboBox) {

		for(int i : comboBoxList.keySet())
			if(comboBoxList.get(i).getName().equalsIgnoreCase(comboBox.getName()))
				return i;

			return -1;
	}

	/**
	 * @param row
	 * @return
	 */
	public JComboBox<String> getComboBox(int row) {

		return comboBoxList.get(row);
	}

	/**
	 * @param row
	 * @return
	 */
	public void setComboBox(int row, JComboBox<String> jComboBox) {

		comboBoxList.put(row,jComboBox);
	}

	/**
	 * @return the values
	 */
	public Map<Integer, String> getValues() {

		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(Map<Integer, String> values) {

		this.values = values;
	}

	/**
	 * @return the comboBoxList
	 */
	public Map<Integer, JComboBox<String>> getComboBoxList() {
		return comboBoxList;
	}

	/**
	 * @param comboBoxList the comboBoxList to set
	 */
	public void setComboBoxList(Map<Integer, JComboBox<String>> comboBoxList) {
		this.comboBoxList = comboBoxList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return "ComboBoxColumn ["
				+ (values != null ? "valueItem=" + values + ", " : "")
				+ (comboBoxList != null ? "valueArray=" + comboBoxList + ", " : "")
				+ (eventListener != null ? "eventListener=" + eventListener
						+ ", " : "")
						+ (mouseListener != null ? "mouseListener=" + mouseListener
								: "") + "]";
	}


}
