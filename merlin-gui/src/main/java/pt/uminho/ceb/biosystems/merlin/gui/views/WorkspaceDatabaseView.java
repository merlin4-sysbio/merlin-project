package pt.uminho.ceb.biosystems.merlin.gui.views;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceTables;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceDatabaseAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceTableAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceTablesAIB;

public class WorkspaceDatabaseView extends javax.swing.JPanel {

	private static final long serialVersionUID = -8392087095290637656L;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JCheckBox jCheckBox1;
	private JList<String> jList1;
	private JList<WorkspaceEntity> jList2;
	private JPanel jPanel11;
	private WorkspaceDatabaseAIB databaseGUI;

	
	public WorkspaceDatabaseView(WorkspaceDatabaseAIB databaseGUI) {
		super();
		initGUI();
		this.databaseGUI = databaseGUI;
		fillComponents();
	}
	
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

				this.add(jPanel11, c);
				{
				}
				{
					jScrollPane1 = new JScrollPane();
					jPanel11.add(jScrollPane1);
					jScrollPane1.setBorder(new TitledBorder("Tables"));
					jScrollPane1.setBounds(12, 12, 350, 500);
				}
				{
					jScrollPane2 = new JScrollPane();
					jPanel11.add(jScrollPane2);
					jScrollPane2.setBounds(459, 12, 350, 500);
					jScrollPane2.setBorder(BorderFactory.createTitledBorder("Entities"));
				}
				{
					jCheckBox1 = new JCheckBox();
					jPanel11.add(jCheckBox1);
					jCheckBox1.setText("Hide empty tables");
					jCheckBox1.setBounds(12, 532, 316, 18);
					jCheckBox1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							jCheckBox1ActionPerformed(evt);
						}
					});
				}
				jList1 = new JList<>();
				jScrollPane1.setViewportView(jList1);
				jList2 = new JList<>();
				jScrollPane2.setViewportView(jList2);

			}
			setSize(400, 300);
			this.setPreferredSize(new java.awt.Dimension(975, 639));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fillComponents() {
		
		WorkspaceTablesAIB tables = (WorkspaceTablesAIB) databaseGUI.getTables();
		
		ArrayList<WorkspaceTableAIB> dbts = tables.getList();
		
		String[] adbts = new String[dbts.size()];
		
		for(int i=0;i<dbts.size();i++)
		{
			adbts[i] = dbts.get(i).getName();
		}
		
		ListModel<String> jList1Model = new DefaultComboBoxModel<>(adbts);
		jList1.setModel(jList1Model);
		
		ArrayList<WorkspaceEntity> entities = databaseGUI.getEntities().getEntitiesList();
		
		LinkedList<WorkspaceEntity> ents = new LinkedList<WorkspaceEntity>();
		
		for(int i=0;i<entities.size();i++)
		{
			addUnit(ents, entities.get(i));
		}

		WorkspaceEntity[] aents = new WorkspaceEntity[ents.size()];
		
		for(int i=0;i<ents.size();i++)
		{
			aents[i] = ents.get(i);
		}
		
		ListModel<WorkspaceEntity> jList2Model = new DefaultComboBoxModel<>(aents);
		jList2.setModel(jList2Model);
	}

	public void reFillComponents() throws Exception {
		
		WorkspaceTables tables = databaseGUI.getTables();

		
		String[] adbts;
		
		if(this.jCheckBox1.isSelected())
		{
			String[][] ndbts = tables.getTableNumbers();
			
			LinkedList<String> ga = new LinkedList<String>();
			
			for(int i=0;i<ndbts.length;i++)
			{
				if(!ndbts[i][1].equals("0"))
				{
					ga.add(ndbts[i][0]);
				}
			}
			
			adbts = new String[ga.size()];
			
			for(int i=0;i<ga.size();i++)
			{
				adbts[i] = ga.get(i);
			}
		}
		else
		{
			ArrayList<WorkspaceTableAIB> dbts = ((WorkspaceTablesAIB) tables).getList();
			adbts = new String[dbts.size()];
			
			for(int i=0;i<dbts.size();i++)
			{
				adbts[i] = dbts.get(i).getName();
			}
		}
		
		ListModel<String> jList1Model = new DefaultComboBoxModel<>(adbts);
		jList1.setModel(jList1Model);
	}

	private void jCheckBox1ActionPerformed(ActionEvent evt) {
		try {
			reFillComponents();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addUnit(LinkedList<WorkspaceEntity> lis, WorkspaceEntity ent)
	{
		lis.add(ent);
		
		ArrayList<WorkspaceEntity> entities = ent.getSubenties();
		
		for(int i=0;i<entities.size();i++)
		{
			addUnit(lis, entities.get(i));
		}
	}

}
