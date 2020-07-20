package pt.uminho.ceb.biosystems.merlin.gui.views.windows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.SoftBevelBorder;

import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MyJTable;

public class GenericDetailWindow extends javax.swing.JDialog {
	private static final long serialVersionUID = 2256787982718755877L;
	private JPanel jPanel1;
	private JScrollPane jScrollPane1;
	private MyJTable jTable1;
	private JPanel jPanel11;
	private JPanel jPanel12;
	private JComboBox<String> searchComboBox;
	private WorkspaceDataTable[] dataTable;
	private String reaction;

	/**
	 * @param dataTable
	 * @param windowName
	 * @param name
	 * @param reaction
	 */
	public GenericDetailWindow(WorkspaceDataTable[] dataTable, String windowName, String name, String reaction) {

		super(Workbench.getInstance().getMainFrame());
		this.reaction = reaction;
		this.dataTable = dataTable;
		this.initGUI(dataTable, windowName, name);
		Utilities.centerOnOwner(this);
		this.setIconImage((new ImageIcon(getClass().getClassLoader().getResource("icons/merlin.png"))).getImage());
		this.setVisible(true);		
		this.setAlwaysOnTop(true);
		this.toFront();
	}

	/**
	 * @param frame
	 * @param querydatas
	 * @param windowName
	 * @param name
	 */
	public GenericDetailWindow(WorkspaceDataTable[] querydatas, String windowName, String name) {

		super(Workbench.getInstance().getMainFrame());
		this.dataTable = querydatas;
		initGUI(querydatas, windowName, name);
		Utilities.centerOnOwner(this);
		this.setIconImage((new ImageIcon(getClass().getClassLoader().getResource("icons/merlin.png"))).getImage());
		this.setVisible(true);		
		this.setAlwaysOnTop(true);
		this.toFront();
	}

	/**
	 * @param querydatas
	 * @param windowName
	 * @param name
	 */
	private void initGUI(WorkspaceDataTable[] querydatas, String windowName, String name) {

		try {

			this.setTitle(windowName);

			jPanel1 = new JPanel();
			GridBagLayout jPanel1Layout = new GridBagLayout();
			jPanel1.setLayout(jPanel1Layout);
			getContentPane().add(jPanel1, BorderLayout.CENTER);
			jPanel1.setPreferredSize(new java.awt.Dimension(426, 297));

			GridBagConstraints c = new GridBagConstraints();

			c.fill = GridBagConstraints.BOTH;


			jPanel11 = new JPanel();
			BorderLayout jPanel11Layout = new BorderLayout();
			jPanel11.setLayout(jPanel11Layout);

			c.weightx = 1.0;
			c.weighty = 0.015;
			c.gridx = 0;
			c.gridy = 0;
			jPanel1.add(jPanel11, c);

			if(!name.equals("")) {

				JLabel j = new JLabel(name);
				jPanel1.add(j, c);
			}

			int zad = 1;

			if(this.reaction!=null) {

				c.weightx = 1.0;
				c.weighty = 0.015;
				c.gridx = 0;
				c.gridy = zad;
				zad++;
				JLabel rec = new JLabel(this.reaction);
				jPanel1.add(rec, c);
			}

			c.fill = GridBagConstraints.BOTH;

			c.weightx = 1.0;
			c.weighty = 0.92;
			c.gridx = 0;
			c.gridy = zad;
			zad++;

			if(!name.equals("")) c.insets = new Insets(10, 0, 0, 0);

			jPanel1.add(jPanel11, c);
			jScrollPane1 = new JScrollPane();
			jPanel11.add(jScrollPane1, BorderLayout.CENTER);
			jTable1 = new MyJTable();
			jTable1.setShowGrid(false);
			jScrollPane1.setViewportView(jTable1);
			jTable1.setModel(querydatas[0]);

			c.insets = new Insets(0, 0, 0, 0);

			c.fill = GridBagConstraints.BOTH;

			c.weightx = 1.0;
			c.weighty = 0.05;
			c.gridx = 0;
			c.gridy = zad;
			zad++;

			jPanel12 = new JPanel();
			jPanel12.setLayout(new GridBagLayout());
			jPanel12.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

			jPanel1.add(jPanel12, c);

			GridBagConstraints c2 = new GridBagConstraints();

			if(querydatas.length==1) {
				
				JButton button1 = new JButton("Close");
				c2.weightx = 0.95;
				c2.weighty = 1;
				c2.gridx = 0;
				c2.gridy = 0;
				c2.anchor = GridBagConstraints.CENTER;
				jPanel12.add(button1, c2);
				button1.setPreferredSize(new Dimension(75,33));
				button1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						finish();
					}
				});
			}
			else {

				JButton button1 = new JButton("Close");
				c2.weightx = 0.95;
				c2.weighty = 1;
				c2.gridx = 0;
				c2.gridy = 0;
				c2.anchor = GridBagConstraints.CENTER;
				jPanel12.add(button1, c2);
				button1.setPreferredSize(new Dimension(75,33));
				button1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						finish();
					}
				});

				String[] chc = new String[dataTable.length];

				for(int s=0;s<this.dataTable.length;s++)					
					chc[s] = this.dataTable[s].getName();

				ComboBoxModel<String> searchComboBoxModel = new DefaultComboBoxModel<>(chc);
				searchComboBox = new JComboBox<>();
				searchComboBox.setModel(searchComboBoxModel);

				c2.weightx = 0.05;
				c2.weighty = 1;
				c2.gridx = 1;
				c2.gridy = 0;
				c2.anchor = GridBagConstraints.EAST;
				jPanel12.add(searchComboBox, c2);

				searchComboBox.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent evt) {

						searchComboBoxActionPerformed(evt);
					}
				});
			}

			this.setModal(true);
			this.setSize(502, 385);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void finish() {

		this.setVisible(false);
		this.dispose();
	}

	/**
	 * @param evt
	 */
	private void searchComboBoxActionPerformed(ActionEvent evt) {

		jTable1 = new MyJTable();
		jTable1.setShowGrid(false);
		jTable1.setModel(this.dataTable[this.searchComboBox.getSelectedIndex()]);
		//jTable1.setSortableFalse();
		jScrollPane1.setViewportView(jTable1);
		jTable1.setAutoCreateRowSorter(this.dataTable[this.searchComboBox.getSelectedIndex()].getRowCount()>0);
	}

}
