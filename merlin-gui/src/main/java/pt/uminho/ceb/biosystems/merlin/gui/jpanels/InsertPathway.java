package pt.uminho.ceb.biosystems.merlin.gui.jpanels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.ModelReactionsAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.CreateImageIcon;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MerlinUtils;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelPathwaysServices;


public class InsertPathway extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanel1;
	private JTextField jTextField1;
	private JButton jButton1;
	private JButton jButton2;
	private JComboBox<Object> jSuperPathwayComboBox;
	private ModelReactionsAIB reactionsModelGUI;

	/**
	 * @param reactionsInterface
	 * @throws SQLException 
	 */
	public InsertPathway(ModelReactionsAIB reactionsInterface){

		super(Workbench.getInstance().getMainFrame());
		this.reactionsModelGUI=reactionsInterface;
		this.initGUI("New Pathway");
		Utilities.centerOnOwner(this);
		this.setIconImage((new ImageIcon(getClass().getClassLoader().getResource("icons/merlin.png"))).getImage());
		this.setVisible(true);		
		this.setAlwaysOnTop(true);
		this.toFront();
	}

	/**
	 * @param windowName
	 */
	private void initGUI(String windowName) {
		this.setTitle(windowName);
		jPanel1 = new JPanel();
		getContentPane().add(jPanel1, BorderLayout.NORTH);
		GridBagLayout jPanel1Layout = new GridBagLayout();
		jPanel1Layout.columnWeights = new double[] {0.0, 0.1, 0.2, 0.0, 0.2, 0.1, 0.0};
		jPanel1Layout.columnWidths = new int[] {7, 20, 7, 20, 7, 20, 7};
		jPanel1Layout.rowWeights = new double[] {0.0, 0.2, 0.0, 0.1, 0.0, 0.1, 0.0};
		jPanel1Layout.rowHeights = new int[] {7, 7, 7, 20, 7, 20, 7};
		jPanel1.setLayout(jPanel1Layout);
		jPanel1.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		jPanel1.setPreferredSize(new java.awt.Dimension(431, 156));
		{
			jTextField1 = new JTextField();
			jPanel1.add(jTextField1, new GridBagConstraints(2, 1, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jTextField1.setBorder(BorderFactory.createTitledBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null), "Insert Manual Entry", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
		}
		{
			jSuperPathwayComboBox = new JComboBox<>(reactionsModelGUI.getAllPathways(false));
			jPanel1.add(jSuperPathwayComboBox, new GridBagConstraints(2, 3, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		{
			jButton1 = new JButton();
			jPanel1.add(jButton1, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jButton1.setText("Save");
			jButton1.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Save.png")),0.1).resizeImageIcon());
			jButton1.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent evt) {insertData();}});
		}
		{
			jButton2 = new JButton();
			jPanel1.add(jButton2, new GridBagConstraints(4, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jButton2.setText("Cancel");
			jButton2.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Cancel.png")),0.1).resizeImageIcon());			
			jButton2.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent evt) {simpleFinish();}});
		}

		this.setModal(true);
		this.pack();
	}

	public void simpleFinish()
	{
		this.setVisible(false);
		this.dispose();
	}

	public void finish() {};

	public void insertData(){
		try
		{
			
			Integer pathwayID = ModelPathwaysServices.getPathwayIDbyName(reactionsModelGUI.getWorkspace().getName(), jTextField1.getText());
			
			if(pathwayID != null){
				Workbench.getInstance().warn("Pathway "+jTextField1.getText()+" already exists in database!");
			}
			else {
				int lastId = ModelPathwaysServices.insertModelPathwayCodeAndName(reactionsModelGUI.getWorkspace().getName(), ",", jTextField1.getText());

				if(jSuperPathwayComboBox.getSelectedIndex()>0) {
					
					ModelPathwaysServices.insertModelPathwayIdAndSuperPathway(this.reactionsModelGUI.getWorkspace().getName(), lastId, reactionsModelGUI.getPathwayID((String) jSuperPathwayComboBox.getSelectedItem()));
				}
				
				finish();
			}
			
			MerlinUtils.updateReactionsView(reactionsModelGUI.getWorkspace().getName());
		}
		catch (Exception e){
			Workbench.getInstance().error(e);
			e.printStackTrace();}
	}
}