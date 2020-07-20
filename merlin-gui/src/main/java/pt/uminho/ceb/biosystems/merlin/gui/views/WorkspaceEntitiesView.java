package pt.uminho.ceb.biosystems.merlin.gui.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceEntityAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.WorkspaceModelEntitiesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MyJTable;

/**
 * @author ODias
 *
 */
public class WorkspaceEntitiesView extends javax.swing.JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane3;
	private JCheckBox jCheckBox1;
	private JList<WorkspaceEntityAIB> jList1;
	private JList<WorkspaceEntityAIB> jList2;
	private JPanel jPanel11;
	private WorkspaceModelEntitiesAIB entities;
	private MyJTable jTable1;


	/**
	 * @param entities
	 */
	public WorkspaceEntitiesView(WorkspaceModelEntitiesAIB entities) {
		super();
		initGUI();
		this.entities = entities;
		fillComponents();
	}

	/**
	 * 
	 */
	private void initGUI() {

		try {
			{
				GridBagLayout jPanel1Layout = new GridBagLayout();
				this.setLayout(jPanel1Layout);

				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.BOTH;

				c.weightx = 1.0;
				c.weighty = 0.95;
				c.gridx = 0;
				c.gridy = 0;

				jPanel11 = new JPanel();
				jPanel11.setLayout(null);

				this.add(jPanel11, new GridBagConstraints(0, 0, 2, 2, 1.0, 0.95, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
				}
				{
					jScrollPane1 = new JScrollPane();
					jPanel11.add(jScrollPane1);
					jScrollPane1.setBorder(new TitledBorder("Entities"));
					jScrollPane1.setBounds(12, 12, 340, 500);
				}
				{
					jScrollPane2 = new JScrollPane();
					jPanel11.add(jScrollPane2);
					jScrollPane2.setBounds(379, 12, 487, 250);
					jScrollPane2.setBorder(BorderFactory.createTitledBorder("Subunits"));
				}
				{
					jScrollPane3 = new JScrollPane();
					jTable1 = new MyJTable();
					jTable1.setShowGrid(false);
					TableModel jTable1Model = 
							new DefaultTableModel(
									new String[][]{},
									new String[] { "", "" });
					jScrollPane3.setViewportView(jTable1);
					jTable1.setModel(jTable1Model);
					jTable1.setSortableFalse();
					jPanel11.add(jScrollPane3);
					jScrollPane3.setBounds(379, 262, 487, 250);
					jScrollPane3.setBorder(BorderFactory.createTitledBorder("Table statistics"));
				}
				{
					jCheckBox1 = new JCheckBox();
					jPanel11.add(jCheckBox1);
					jCheckBox1.setText("Hide subentities");
					jCheckBox1.setBounds(12, 529, 501, 18);
					jCheckBox1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							hideNoHide();
						}
					});
				}
				jList1 = new JList<>();
				jScrollPane1.setViewportView(jList1);
				jList1.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						jList1MouseClicked(evt);
					}
				});
				jList2 = new JList<>();
				jScrollPane2.setViewportView(jList2);
				jList2.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						jList2MouseClicked(evt);
					}
				});
				jScrollPane2.setEnabled(false);

			}
			setSize(400, 300);
			this.setPreferredSize(new java.awt.Dimension(1006, 576));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void addUnit(LinkedList<WorkspaceEntityAIB> lis, WorkspaceEntityAIB entity) {
		
		lis.add(entity);

		List<WorkspaceEntityAIB> entities = entity.getSubentities();
		
		for(int i=0;i<entities.size();i++)
			addUnit(lis, entities.get(i));
	}

	public void fillComponents() {

		ArrayList<WorkspaceEntityAIB> entities = this.entities.getEntitiesGUIList();

		LinkedList<WorkspaceEntityAIB> ents = new LinkedList<WorkspaceEntityAIB>();

		for(int i=0;i<entities.size();i++)
			addUnit(ents, entities.get(i));

		WorkspaceEntityAIB[] elis = new WorkspaceEntityAIB[ents.size()];

		for(int i=0;i<ents.size();i++)
			elis[i] = ents.get(i);

		ListModel<WorkspaceEntityAIB> jList1Model = new DefaultComboBoxModel<>(elis);
		jList1.setModel(jList1Model);
	}

	private void jList1MouseClicked(MouseEvent evt) {
		
		WorkspaceEntityAIB entityGUI = (WorkspaceEntityAIB)jList1.getSelectedValue();

		setTable(entityGUI);

		List<WorkspaceEntityAIB> subs = entityGUI.getSubentities();

		if(subs.size()>0) {
			
			LinkedList<WorkspaceEntityAIB> ents = new LinkedList<WorkspaceEntityAIB>();

			for(int i=0;i<subs.size();i++)
				addUnit(ents, subs.get(i));

			WorkspaceEntityAIB[] elis = new WorkspaceEntityAIB[ents.size()];

			for(int i=0;i<ents.size();i++)
				elis[i] = ents.get(i);

			ListModel<WorkspaceEntityAIB> jList2Model = new DefaultComboBoxModel<>(elis);
			jList2.setModel(jList2Model);
			jScrollPane2.setEnabled(true);
		}
		else {
			
			WorkspaceEntityAIB[] a = new WorkspaceEntityAIB[0];
			ListModel<WorkspaceEntityAIB> jList2Model = new DefaultComboBoxModel<>(a);
			jList2.setModel(jList2Model);
			jScrollPane2.setEnabled(false);
		}
	}

	/**
	 * @param e
	 */
	private void setTable(WorkspaceEntityAIB e) {
		
		try {
			TableModel jTable1Model = new DefaultTableModel(e.getStats(), new String[] { "", "" });
			jTable1 = new MyJTable();
			jTable1.setShowGrid(false);
			jTable1.setModel(jTable1Model);
			jScrollPane3.setViewportView(jTable1);
		} 
		catch (Exception e1) {
			Workbench.getInstance().error(e1);
			e1.printStackTrace();
		}
	}

	private void jList2MouseClicked(MouseEvent evt) {

		if(jList2.getSelectedValue()!=null) {
			
			WorkspaceEntityAIB en = (WorkspaceEntityAIB)jList2.getSelectedValue();
			setTable(en);
		}
	}

	private void hideNoHide() {
		
		if(this.jCheckBox1.isSelected()) {
			
			ArrayList<WorkspaceEntityAIB> entities = this.entities.getEntitiesGUIList();

			LinkedList<WorkspaceEntityAIB> ents = new LinkedList<WorkspaceEntityAIB>();

			for(int i=0;i<entities.size();i++)
				ents.add(entities.get(i));

			WorkspaceEntityAIB[] elis = new WorkspaceEntityAIB[ents.size()];

			for(int i=0;i<ents.size();i++)
				elis[i] = ents.get(i);

			ListModel<WorkspaceEntityAIB> jList1Model = new DefaultComboBoxModel<>(elis);
			jList1.setModel(jList1Model);
		}
		else fillComponents();
	}

}
