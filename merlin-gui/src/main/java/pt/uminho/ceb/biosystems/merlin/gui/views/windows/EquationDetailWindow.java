package pt.uminho.ceb.biosystems.merlin.gui.views.windows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.TableColumnModel;

import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MyJTable;

public class EquationDetailWindow extends javax.swing.JDialog {

	private static final long serialVersionUID = -4047571911204678774L;
	private JPanel jPanel1;
	private JScrollPane jScrollPane1;
	private MyJTable jTable1;
	private JPanel jPanel11;
	private JPanel jPanel12;

	/**
	 * @param querydata
	 * @param meta
	 */
	public EquationDetailWindow(WorkspaceDataTable querydata, String meta) {
		
		super(Workbench.getInstance().getMainFrame());
		this.initGUI(querydata, meta);
		Utilities.centerOnOwner(this);
		this.setIconImage((new ImageIcon(getClass().getClassLoader().getResource("icons/merlin.png"))).getImage());
		this.setVisible(true);		
		this.setAlwaysOnTop(true);
		this.toFront();
		
	}

	/**
	 * @param querydata
	 * @param meta
	 */
	private void initGUI(WorkspaceDataTable querydata, String meta) {
		
		try {
			
			this.setTitle("Reactions in which "+meta+" participates");
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
			c.weighty = 0.03;
			c.gridx = 0;
			c.gridy = 0;
			jPanel1.add(jPanel11, c);
			JLabel j = new JLabel("Metabolite: "+meta);
			jPanel1.add(j, c);
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1.0;
			c.weighty = 0.92;
			c.gridx = 0;
			c.gridy = 1;
			jPanel1.add(jPanel11, c);
			jScrollPane1 = new JScrollPane();
			jPanel11.add(jScrollPane1, BorderLayout.CENTER);
			jTable1 = new MyJTable();
			jScrollPane1.setViewportView(jTable1);
			
			jTable1.setAutoCreateRowSorter(true);

			jTable1.setModel(querydata);
			{
				TableColumnModel tc = jTable1.getColumnModel();
				tc.getColumn(0).setWidth(150);
				tc.getColumn(0).setModelIndex(0);
				tc.getColumn(1).setWidth(450);
				tc.getColumn(1).setModelIndex(1);
			}
			jTable1.setSortableFalse();
			
			c.fill = GridBagConstraints.BOTH;

			c.weightx = 1.0;
			c.weighty = 0.05;
			c.gridx = 0;
			c.gridy = 2;

			jPanel12 = new JPanel();
			jPanel12.setLayout(new GridBagLayout());
			jPanel12.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

			jPanel1.add(jPanel12, c);

			GridBagConstraints c2 = new GridBagConstraints();

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
			this.setModal(true);
			this.setSize(602, 385);
		}
		catch (Exception e) {e.printStackTrace();}
	}

	public void finish() {
		this.setVisible(false);
		this.dispose();
	}

}
