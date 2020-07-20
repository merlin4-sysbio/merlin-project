package pt.uminho.ceb.biosystems.merlin.gui.views;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.AIBenchUtils;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MyJTable;

/**
 * @author ODias
 *
 */
public class WorkspaceEntityAIBView extends WorkspaceUpdatablePanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane1;
	private MyJTable jTable;
	private WorkspaceEntity entity;

	/**
	 * @param e
	 */
	public WorkspaceEntityAIBView(WorkspaceEntity e) {

		super(e);
		initGUI(e);
		this.addListenersToGraphicalObjects();
	}

	/**
	 * @param ent
	 */
	private void initGUI(WorkspaceEntity ent) {

		try {

			this.entity = ent;
			
			BorderLayout thisLayout = new BorderLayout();
			this.setLayout(thisLayout);
			setPreferredSize(new Dimension(400, 300));
			{
				jScrollPane1 = new JScrollPane();
				this.add(jScrollPane1, BorderLayout.CENTER);
				jTable = new MyJTable();
				jTable.setShowGrid(false);
				jTable.setEnabled(false);
				this.setTable();
				jTable.setToolTipText("Click to refresh");
				jScrollPane1.setViewportView(jTable);
				jScrollPane1.setBorder(new TitledBorder(ent.getName()+" data"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void setTable() {
		
		try {
			entity.setStats(null);  //to reset the structure

			TableModel jTable1Model = new DefaultTableModel(entity.getStats(), new String[] { "", "" }); 
			jTable.setModel(jTable1Model);
			jTable.setSortableFalse();
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

		this.setTable();
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
				AIBenchUtils.updateView(entity.getWorkspace().getName(), entity.getClass());
			}
		});

		this.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {}

			@Override
			public void focusGained(FocusEvent arg0) {

				updateTableUI();
				AIBenchUtils.updateView(entity.getWorkspace().getName(), entity.getClass());
			}
		});
	}
}
