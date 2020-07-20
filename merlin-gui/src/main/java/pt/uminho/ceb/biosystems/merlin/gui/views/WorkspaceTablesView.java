package pt.uminho.ceb.biosystems.merlin.gui.views;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceTablesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MyJTable;

/**
 * @author ODias
 *
 */
public class WorkspaceTablesView extends javax.swing.JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane1;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JPanel jPanel3;
	private MyJTable jTable1;

	/**
	 * @param dbts
	 */
	public WorkspaceTablesView(WorkspaceTablesAIB dbts) {
		super();
		initGUI(dbts);
	}
	
	/**
	 * @param dbts
	 */
	private void initGUI(WorkspaceTablesAIB dbts) {
		try {
			GridBagLayout jPanel1Layout = new GridBagLayout();
			this.setLayout(jPanel1Layout);
			
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;

			c.weightx = 1.0;
			c.weighty = 0.08;
			c.gridx = 0;
			c.gridy = 1;
			
			jPanel2 = new JPanel();
			jPanel2.setLayout(new GridBagLayout());
			this.add(jPanel2, c);

			GridBagConstraints c2 = new GridBagConstraints();
			
			JLabel jLabel1 = new JLabel("Number of tables: "+dbts.getList().size());
			c2.weightx = 0.95;
			c2.weighty = 1;
			c2.gridx = 0;
			c2.gridy = 1;
			c2.anchor = GridBagConstraints.WEST;
			jPanel2.add(jLabel1, c2);
			
			c.weightx = 1.0;
			c.weighty = 0.92;
			c.gridx = 0;
			c.gridy = 0;
			
			jPanel1 = new JPanel();
			GridBagLayout thisLayout = new GridBagLayout();
			jPanel1.setLayout(thisLayout);
			this.add(jPanel1, c);
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;

			gbc.weightx = 1.0;
			gbc.weighty = 1;
			gbc.gridx = 0;
			gbc.gridy = 0;
			
			
			jPanel3 = new JPanel();
			BorderLayout jPanel3Layout = new BorderLayout();
			jPanel3.setLayout(jPanel3Layout);
			jPanel1.add(jPanel3, gbc);
			jPanel3.setPreferredSize(new java.awt.Dimension(731, 446));
			jScrollPane1 = new JScrollPane();
			jPanel3.add(jScrollPane1);
			jScrollPane1.setPreferredSize(new java.awt.Dimension(731, 674));
			this.setBorder(BorderFactory.createTitledBorder("Database tables"));
			jTable1 = new MyJTable();
			jTable1.setShowGrid(false);
			jScrollPane1.setViewportView(jTable1);

			TableModel jTable1Model = 
				new DefaultTableModel(
						dbts.getTableNumbers(),
						new String[] { "Table", "Number of entries" });

			jTable1.setModel(jTable1Model);
			jTable1.setSortableFalse();
			
			
			this.setPreferredSize(new java.awt.Dimension(741, 727));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
