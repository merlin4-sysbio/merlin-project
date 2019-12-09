package pt.uminho.ceb.biosystems.merlin.gui.jpanels;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;

import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.gui.operations.integration.ModelEnzymesIntegration;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.CreateImageIcon;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MyJTable;


public class IntegrationConflictsGUI extends JDialog {

	private static final long serialVersionUID = -1L;
	private JPanel jPanelLocalDatabase;
	private JPanel jPanel1BLAST;
	private JButton jButtonClose;
	private JButton jButtonSaveClose;
	private JScrollPane jScrollPaneSelect;
	private JTextField jTextField2;
	private JTextField jTextField;
	private JTextArea jTextArea1;
	private JPanel jPanelPreviousNext;
	private JComboBox<String> jComboBoxSelect;
	private JButton jButtonNext;
	private JButton jButtonPrevious;
	private JPanel jPanelSelect;
	private JScrollPane jScrollPaneBLAST;
	private JScrollPane jScrollPaneLocalDatabase;
	private Map<String, String[]> localDatabase;
	private Map<String, String[]> blast;
	private boolean type;
	private String selectedKey;
	private int count;
	private ModelEnzymesIntegration integrateBLASTData;
	protected Map<String, String> selectedItems;

	/**
	 * @param integrateHomologyData
	 * @param type
	 */
	public IntegrationConflictsGUI(ModelEnzymesIntegration integrateHomologyData, boolean type) {
		super(Workbench.getInstance().getMainFrame());
		
		this.integrateBLASTData = integrateHomologyData;
		if(type) {
			
			this.setTitle("Name conflicts");
			this.localDatabase = integrateHomologyData.getNameConflictsDatabase();
			this.blast = integrateHomologyData.getNameConflictsHomology();
		}
//		else
//		{
//			this.setTitle("Product conflicts");
//			this.localDatabase = integrateBLASTData.getProductConflictsDatabase();
//			this.blast = integrateBLASTData.getProductConflictsBLAST();
//		}
		this.type= type;
		this.count=0;
		this.selectedItems = new TreeMap<String, String>();
		this.selectedKey = this.getKey(blast,count);
		initGUI();
		Utilities.centerOnOwner(this);
		this.setIconImage((new ImageIcon(getClass().getClassLoader().getResource("icons/merlin.png"))).getImage());
		this.setVisible(true);		
		this.setAlwaysOnTop(true);
		this.toFront();
	}

	/**
	 * 
	 */
	private void initGUI() {
		try
		{
			{
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.columnWidths = new int[] {7, 7, 7, 425, 7, 425, 7, 7, 7};
				thisLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
				thisLayout.rowWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0};
				thisLayout.rowHeights = new int[] {7, 225, 7, 7, 7};
				getContentPane().setLayout(thisLayout);
				{
					jPanelLocalDatabase = new JPanel();
					GridBagLayout jPanelLocalDatabaseLayout = new GridBagLayout();
					getContentPane().add(jPanelLocalDatabase, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelLocalDatabaseLayout.rowWeights = new double[] {0.0, 0.1, 0.0};
					jPanelLocalDatabaseLayout.rowHeights = new int[] {7, 7, 7};
					jPanelLocalDatabaseLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
					jPanelLocalDatabaseLayout.columnWidths = new int[] {7, 7, 7};
					jPanelLocalDatabase.setLayout(jPanelLocalDatabaseLayout);
					jPanelLocalDatabase.setBorder(BorderFactory.createTitledBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null), "Local Database", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
					{
						int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
						int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
						jScrollPaneLocalDatabase = new JScrollPane(v, h);
						jPanelLocalDatabase.add(jScrollPaneLocalDatabase, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jScrollPaneLocalDatabase.setSize(350, 290);
						jScrollPaneLocalDatabase.setMaximumSize(new java.awt.Dimension(350, 290));
						jScrollPaneLocalDatabase.setViewportView(this.nextTable(this.getString(this.localDatabase,count), type));
					}
				}
				{
					jPanel1BLAST = new JPanel();
					GridBagLayout jPanel1BLASTLayout = new GridBagLayout();
					getContentPane().add(jPanel1BLAST, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanel1BLASTLayout.rowWeights = new double[] {0.0, 0.1, 0.0};
					jPanel1BLASTLayout.rowHeights = new int[] {7, 7, 7};
					jPanel1BLASTLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
					jPanel1BLASTLayout.columnWidths = new int[] {7, 7, 7};
					jPanel1BLAST.setLayout(jPanel1BLASTLayout);
					jPanel1BLAST.setBorder(BorderFactory.createTitledBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null), "Blast Database", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
					{
						int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
						int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
						jScrollPaneBLAST = new JScrollPane(v, h);
						jPanel1BLAST.add(jScrollPaneBLAST, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jScrollPaneBLAST.setSize(350, 290);
						jScrollPaneBLAST.setMaximumSize(new java.awt.Dimension(350, 290));
						jScrollPaneBLAST.setViewportView(this.nextTable(this.getString(blast,count), type));
					}
				}
				{
					jScrollPaneSelect = new JScrollPane();
					jPanelSelect = new JPanel();
					getContentPane().add(jScrollPaneSelect, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jScrollPaneSelect.setBorder(BorderFactory.createTitledBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null), "Select data", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
					jScrollPaneSelect.setViewportView(jPanelSelect);
					GridBagLayout jPanelSelectLayout = new GridBagLayout();
					jPanelSelectLayout.rowWeights = new double[] {0.0, 0.1, 0.0};
					jPanelSelectLayout.rowHeights = new int[] {7, 7, 7};
					jPanelSelectLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
					jPanelSelectLayout.columnWidths = new int[] {7, 7, 7};
					jPanelSelect.setLayout(jPanelSelectLayout);
					{
						jComboBoxSelect = new JComboBox<>(nextBox(this.getString(this.blast,count),this.getString(this.localDatabase,count)));
						jPanelSelect.add(jComboBoxSelect, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						selectedItems.put(selectedKey,jComboBoxSelect.getSelectedItem().toString());
						jComboBoxSelect.setToolTipText(jComboBoxSelect.getSelectedItem().toString());
						jComboBoxSelect.setPreferredSize(new Dimension(200, 26));
						jComboBoxSelect.updateUI();
						jComboBoxSelect.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent arg0) {
								jComboBoxSelect.setToolTipText(jComboBoxSelect.getSelectedItem().toString());
								selectedItems.put(selectedKey,jComboBoxSelect.getSelectedItem().toString());

							}});
					}
				}
				{
					jPanelPreviousNext = new JPanel();
					GridBagLayout jPanelPreviousNextLayout = new GridBagLayout();
					getContentPane().add(jPanelPreviousNext, new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelPreviousNext.setBorder(BorderFactory.createTitledBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null), "Gene "+selectedKey, TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
					jPanelPreviousNextLayout.rowWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0};
					jPanelPreviousNextLayout.rowHeights = new int[] {7, 7, 7, 7, 7};
					jPanelPreviousNextLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0};
					jPanelPreviousNextLayout.columnWidths = new int[] {7, 50, 7, 50, 7, 50, 7, 50, 7};
					jPanelPreviousNext.setLayout(jPanelPreviousNextLayout);
					{
						jButtonPrevious = new JButton();
						jPanelPreviousNext.add(jButtonPrevious, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jButtonPrevious.setText("Previous");
						jButtonPrevious.setPreferredSize(new java.awt.Dimension(100, 23));
						jButtonPrevious.setSize(100, 23);
						jButtonPrevious.setIcon((new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Previous.png")), 0.1).resizeImageIcon()));
						jButtonPrevious.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent arg0) {
								count-=1;
								if(count<0)
								{
									count = blast.size()-1;
								}
								int i = new Integer(count);
								jTextField.setText((i+1)+"");
								jTextField.setText((count+1)+"");
								selectedKey = getKey(blast, count);
								jScrollPaneBLAST.setViewportView(nextTable(getString(blast,count), type));
								jScrollPaneLocalDatabase.setViewportView(nextTable(getString(localDatabase,count), type));
								jComboBoxSelect.setModel(new DefaultComboBoxModel<String>(nextBox(getString(blast,count),getString(localDatabase,count))));
								jComboBoxSelect.setPreferredSize(new Dimension(200, 26));
								jComboBoxSelect.updateUI();
								jComboBoxSelect.setToolTipText(jComboBoxSelect.getSelectedItem().toString());
								jPanelPreviousNext.setBorder(BorderFactory.createTitledBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null), "Gene "+selectedKey, TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
								selectedItems.put(selectedKey,jComboBoxSelect.getSelectedItem().toString());
							}});


						jTextArea1 = new JTextArea();
						jTextArea1.setText("of");
						jTextArea1.setEditable(false);
						jPanelPreviousNext.add(jTextArea1, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

						jTextField = new JTextField();
						jTextField.setEditable(false);
						jTextField.setText("1");
						//						jTextField.setSize(75, 23);
						jPanelPreviousNext.add(jTextField, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jTextField.setPreferredSize(new java.awt.Dimension(100, 23));
						jTextField.setSize(100, 23);

						jTextField2 = new JTextField();
						jTextField2.setEditable(false);
						//						jTextField2.setSize(75, 23);
						jTextField2.setText(blast.keySet().size()+"");
						jPanelPreviousNext.add(jTextField2, new GridBagConstraints(7, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jTextField2.setPreferredSize(new java.awt.Dimension(100, 23));
						jTextField2.setSize(100, 23);

						jButtonSaveClose = new JButton();
						jButtonSaveClose.setText("Save & Close");
						jButtonSaveClose.setIcon((new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Save.png")), 0.1).resizeImageIcon()));
						jPanelPreviousNext.add(jButtonSaveClose, new GridBagConstraints(1, 3, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jButtonSaveClose.setPreferredSize(new java.awt.Dimension(205, 23));
						jButtonSaveClose.setSize(205, 23);
						jButtonSaveClose.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent arg0) {
//								if(type)
//								{
//									integrateBLASTData.setNewNameConflicts(selectedItems);
//								}
//								else
//								{
//									integrateBLASTData.setNewProductsConflicts(selectedItems);
//								}
								close();
							}});

						jButtonClose = new JButton();
						jButtonClose.setText("Close");
						jButtonClose.setIcon((new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Cancel.png")), 0.1).resizeImageIcon()));
						jPanelPreviousNext.add(jButtonClose, new GridBagConstraints(5, 3, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jButtonClose.setPreferredSize(new java.awt.Dimension(205, 23));
						jButtonClose.setSize(205, 23);
						jButtonClose.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent arg0) {
								close();
							}});

						jButtonNext = new JButton();
						jPanelPreviousNext.add(jButtonNext, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jButtonNext.setText("Next");
						jButtonNext.setPreferredSize(new java.awt.Dimension(100, 23));
						jButtonNext.setSize(100, 23);
						jButtonNext.setIcon((new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Next.png")), 0.1).resizeImageIcon()));
						jButtonNext.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent arg0) {
								count+=1;
								if(count==blast.size())
								{
									count = 0;
								}
								int i = new Integer(count);
								jTextField.setText((i+1)+"");
								selectedKey = getKey(blast, count);
								jScrollPaneBLAST.setViewportView(nextTable(getString(blast,count), type));
								jScrollPaneLocalDatabase.setViewportView(nextTable(getString(localDatabase,count), type));
								jComboBoxSelect.setModel(new DefaultComboBoxModel<String>(nextBox(getString(blast,count),getString(localDatabase,count))));
								jComboBoxSelect.setPreferredSize(new Dimension(200, 26));
								jComboBoxSelect.updateUI();
								jComboBoxSelect.setToolTipText(jComboBoxSelect.getSelectedItem().toString());
								jPanelPreviousNext.setBorder(BorderFactory.createTitledBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null), "Gene "+selectedKey, TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
								selectedItems.put(selectedKey,jComboBoxSelect.getSelectedItem().toString());
							}});
					}
				}

			}
			this.setPreferredSize(new java.awt.Dimension(900, 400));
			this.setModal(true);
			pack();
		}
		catch(Exception e) {e.printStackTrace();}
	}

	/**
	 * @param data
	 * @param names
	 * @return a table containing information about a database 
	 */
	private MyJTable nextTable(String[] data, boolean names){
		MyJTable jTable= new MyJTable();
		List<String> columunNames = new ArrayList<String>();
		columunNames.add("Info");
		String name, windowName;
		if(names){columunNames.add("Gene names");name = "Gene Names";windowName = "Gene Conflicts";}
		else{columunNames.add("Product names");name = "Product Names";windowName = "Product Conflicts";}
		WorkspaceDataTable dataTable = new WorkspaceGenericDataTable(columunNames, name, windowName);
		for(String dataName: data)
		{
			ArrayList<String> myNames =  new ArrayList<String>();
			myNames.add("");
			myNames.add(dataName);
			dataTable.addLine(myNames);
		}
		jTable.setModel(dataTable);
		jTable.setSortableFalse();
		TableColumnModel tc = jTable.getColumnModel();
		tc.getColumn(0).setMaxWidth(35);				//button
		tc.getColumn(0).setResizable(false);
		return jTable;
	}

	/**
	 * @param data1
	 * @param data2
	 * @return the Array containing the information from both databases
	 */
	private String[] nextBox(String[] data1,String[] data2){
		List<String> dataN1 = Arrays.asList(data1);
		List<String> dataN2 = Arrays.asList(data2);
		List<String> all = new ArrayList<String>();
		all.addAll(dataN2);
		all.addAll(dataN1);
		return (String[])all.toArray(new String[all.size()]);
	}

	/**
	 * @param data
	 * @param index
	 * @return the Array associated to the index
	 */
	private String[] getString(Map<String, String[]> data, int index){
		return data.get(getKey(data,index));
	}

	/**
	 * @param data
	 * @param index
	 * @return the key associated to the provided index
	 */
	private String getKey(Map<String, String[]> data, int index){
		List<String> keys = new ArrayList<String>(data.keySet());
		return keys.get(index);
	}

	/**
	 * Close window 
	 */
	private void close(){
		if(this.selectedItems.size()<this.localDatabase.size())
		{
			Workbench.getInstance().warn("Items not fully selected!\nLoading local database defaults first for unselected items.");
			for(String gene:this.localDatabase.keySet())
			{
				if(!this.selectedItems.containsKey(gene)&&!this.localDatabase.get(gene)[0].isEmpty()){this.selectedItems.put(gene, this.localDatabase.get(gene)[0]);}
			}
			for(String gene:this.blast.keySet())
			{
				if(!this.selectedItems.containsKey(gene)&&!this.blast.get(gene)[0].isEmpty()){this.selectedItems.put(gene, this.blast.get(gene)[0]);}
			}
		}
		else
			if(this.selectedItems.size()<this.blast.size())
			{
				Workbench.getInstance().warn("Items not fully selected!\nLoading local blast defaults first for unselected items.");
				for(String gene:this.blast.keySet())
				{
					if(!this.selectedItems.containsKey(gene)&&!this.localDatabase.get(gene)[0].isEmpty()){this.selectedItems.put(gene, this.blast.get(gene)[0]);}
				}
				for(String gene:this.localDatabase.keySet())
				{
					if(!this.selectedItems.containsKey(gene)&&!this.localDatabase.get(gene)[0].isEmpty()){this.selectedItems.put(gene, this.localDatabase.get(gene)[0]);}
				}
			}
		if(type)
		{
			integrateBLASTData.setNewNameConflicts(selectedItems);
		}
//		else
//		{
//			integrateBLASTData.setNewProductsConflicts(selectedItems);
//		}
		this.setVisible(false);
		this.dispose();
	}

}
