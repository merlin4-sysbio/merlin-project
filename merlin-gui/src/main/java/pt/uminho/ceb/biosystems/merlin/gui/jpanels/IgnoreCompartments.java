package pt.uminho.ceb.biosystems.merlin.gui.jpanels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.Workspace;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.annotation.AnnotationCompartmentsAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.CreateImageIcon;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MyJTable;
import pt.uminho.ceb.biosystems.merlin.services.annotation.AnnotationCompartmentsServices;

public class IgnoreCompartments extends javax.swing.JDialog{

	private static final long serialVersionUID = -1L;
	private boolean biochemical, transporters;
	private JPanel jPanel0, jPanel1, jPanel2, jPanel3;
	private JScrollPane jScrollPane;
	private MyJTable newjTable = new MyJTable();
	private Map<String, String> compartments;
	private AnnotationCompartmentsAIB compartmentsContainer;
	private JRadioButton jRadioButtonDefaultMembrane, jRadioButtonInputMembrane;
	private ButtonGroup buttonGroupMembrane;
	private JTextField textInputMembrane;


	/**
	 * @param biochemical
	 * @param transporters
	 * @param compartmentsContainer
	 * @param statement
	 */
	public IgnoreCompartments(AnnotationCompartmentsAIB compartmentsContainer) {

		super(Workbench.getInstance().getMainFrame());
		this.compartmentsContainer = compartmentsContainer;
		this.compartments = new HashMap<>();

		initGUI();
		Utilities.centerOnOwner(this);
		this.setVisible(true);		
		this.setAlwaysOnTop(true);
		this.toFront();
	}

	private void initGUI() {

		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
		thisLayout.columnWidths = new int[] {7, 7, 7};
		thisLayout.rowWeights = new double[] {0.0, 200.0, 0.0, 0.0, 0.0};
		thisLayout.rowHeights = new int[] {7, 50, 7, 3, 7};
		this.setLayout(thisLayout);
		this.setPreferredSize(new Dimension(875, 585));
		this.setSize(500, 500);
		{
			this.setTitle("Compartments integration");
			
			
			// First panel with compartments to ignore
			
			jPanel0 = new JPanel();
			jPanel0.setBorder(BorderFactory.createTitledBorder("Please choose the compartments to ignore"));
			GridBagLayout jPanel0Layout = new GridBagLayout();
			jPanel0.setLayout(jPanel0Layout);
			jPanel0Layout.columnWeights = new double[] { 0.1};
			jPanel0Layout.columnWidths = new int[] {7};
			jPanel0Layout.rowWeights = new double[] {0.1};
			jPanel0Layout.rowHeights = new int[] {0};
			this.add(jPanel0, new GridBagConstraints(0, 0, 2, 2, 2, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

			
			jPanel1 = new JPanel();
			GridBagLayout jPanel1Layout = new GridBagLayout();
			jPanel1Layout.rowWeights = new double[] {0.1};
			jPanel1Layout.rowHeights = new int[] {7};
			jPanel1Layout.columnWeights = new double[] {0.1};
			jPanel1Layout.columnWidths = new int[] {7};
			jPanel1.setLayout(jPanel1Layout);
			jPanel0.add(jPanel1, new GridBagConstraints(0, 1, 2, 1, 1.0, 2, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

			jScrollPane = new JScrollPane();
			jPanel1.add(jScrollPane, new GridBagConstraints(0, 0, 2, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jScrollPane.setPreferredSize(new java.awt.Dimension(700, 700));
			jScrollPane.setSize(500, 420);
			{	
				
				// Second panel (between the compartments list and options)
				jPanel3 = new JPanel();
				this.add(jPanel3, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanel3.setBorder(BorderFactory.createTitledBorder("Default membrane"));
				GridBagLayout jPanel13Layout = new GridBagLayout();
				jPanel3.setLayout(jPanel13Layout);
				jPanel13Layout.columnWeights = new double[] { 0.0, 0.1, 0.0, 0.0 };
				jPanel13Layout.columnWidths = new int[] { 3, 20, 7, 50 };
				jPanel13Layout.rowWeights = new double[] { 0.1 };
				jPanel13Layout.rowHeights = new int[] { 7 };
				
				// first button in the second panel
				jRadioButtonDefaultMembrane = new JRadioButton();
				jPanel3.add(jRadioButtonDefaultMembrane,
						new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH, new Insets(0, 0,
										0, 0), 0, 0));
				jRadioButtonDefaultMembrane.setText("automatic");
				jRadioButtonDefaultMembrane.setToolTipText("default membrane set by merlin");
				jRadioButtonDefaultMembrane.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
					}
				});
				
				// second button in the second panel
				jRadioButtonInputMembrane = new JRadioButton();
				jPanel3.add(jRadioButtonInputMembrane,
						new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH, new Insets(0, 0,
										0, 0), 0, 0));
				jRadioButtonInputMembrane.setText("default membrane");
				jRadioButtonInputMembrane.setToolTipText("user input default membrane");
				jRadioButtonInputMembrane.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
					}
				});
				
				// text field with user input default membrane rightmost of the second radio button
				
				textInputMembrane = new JTextField("");
				jPanel3.add(textInputMembrane, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH, new Insets(0, 0,
								0, 0), 0, 0));
				textInputMembrane.setToolTipText("enter a default membrane");
				textInputMembrane.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
					}
				});

				
				// button group, this groups the previous two buttons together making sure that there can only be one button selected and one button only
				buttonGroupMembrane = new ButtonGroup();
				buttonGroupMembrane.add(jRadioButtonDefaultMembrane);
				jRadioButtonDefaultMembrane.setSelected(true);
				buttonGroupMembrane.add(jRadioButtonInputMembrane);
				
				
				jPanel2 = new JPanel();
				this.add(jPanel2, new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanel2.setBorder(BorderFactory.createTitledBorder("options"));
				GridBagLayout jPanel12Layout = new GridBagLayout();
				jPanel2.setLayout(jPanel12Layout);
				jPanel12Layout.columnWeights = new double[] { 0.0, 0.1, 0.0, 0.0 };
				jPanel12Layout.columnWidths = new int[] { 3, 20, 7, 50 };
				jPanel12Layout.rowWeights = new double[] { 0.1 };
				jPanel12Layout.rowHeights = new int[] { 7 };
				
				{
					JButton jButtonSave = new JButton();
					jButtonSave.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Ok.png")),0.1).resizeImageIcon());
					jPanel2.add(jButtonSave, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jButtonSave.setText("ok");
					jButtonSave.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent arg0) {

							List<String> ignore = readTable();
							String membrane = "auto";
							
							if (jRadioButtonInputMembrane.isSelected())
								membrane = textInputMembrane.getText().trim();
							
							ParamSpec[] paramsSpec = new ParamSpec[]{
									new ParamSpec("biochemical", Boolean.class, true, null),
									new ParamSpec("transporters", Boolean.class, true, null),
									new ParamSpec("ignore", List.class, ignore, null),
									new ParamSpec("project", Workspace.class, compartmentsContainer.getWorkspace(), null),
									new ParamSpec("compartment", String.class, membrane, null),
									new ParamSpec("geneCompartments", Map.class, compartmentsContainer.runCompartmentsInterface(compartmentsContainer.getThreshold()), null)		
							};

							for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
								if (def.getID().equals("operations.ModelCompartmentsIntegration.ID")){

									Workbench.getInstance().executeOperation(def, paramsSpec);
								}
							}

							simpleFinish();
						}});
				}
				{
					JButton jButtonClose = new JButton();
					jButtonClose.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Cancel.png")),0.1).resizeImageIcon());
					jPanel2.add(jButtonClose, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jButtonClose.setText("cancel");
					jButtonClose.setToolTipText("close window without continue");
					jButtonClose.setBounds(1, 1, 40, 20);
					jButtonClose.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent arg0) {

							simpleFinish();
						}});
				}
			}

		}
		newjTable.setModel(buildTable());
		newjTable.setSortableFalse();
		newjTable.setRowHeight(30);

		jScrollPane.setViewportView(newjTable);

		this.setModal(true);
	}


	public void simpleFinish() {

		this.setVisible(false);
		this.dispose();
	}

	/**
	 * Method to build the table containing the compartments and the respective checkbox.
	 * 
	 * @return
	 */
	private WorkspaceDataTable buildTable(){

		ArrayList<String> columnsNames = new ArrayList<String>();

		columnsNames.add("compartment");
		columnsNames.add("ignore");

		WorkspaceGenericDataTable data = new WorkspaceGenericDataTable(columnsNames, "genes", "gene data"){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int col) {

				if (col==1) {

					return true;
				}
				else return false;
			}
		};

		try {

			Map<String, String> compartments = AnnotationCompartmentsServices.getNameAndAbbreviation(this.compartmentsContainer.getWorkspace().getName());

			setCompartments(compartments);

			List<Object> ql = null;

			for(String name: compartments.keySet()){

				ql = new ArrayList<Object>();

				ql.add(name);
				ql.add(false);

				data.addLine(ql, -1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * Reads the compartments selected to ignore.
	 * 
	 */
	private List<String> readTable(){

		List<String> toIgnore =new ArrayList<>();

		Map<String, String> comp = getCompartments();

		for(int i = 0; i < newjTable.getRowCount(); i++){

			if((boolean) newjTable.getValueAt(i, 1) == true) {

				String compartment = comp.get((String) newjTable.getValueAt(i, 0));

				toIgnore.add(compartment);
			}
		}
		return toIgnore;
	}

	/**
	 * @return the compartments
	 */
	public Map<String, String> getCompartments() {
		return compartments;
	}

	/**
	 * @param compartments the compartments to set
	 */
	private void setCompartments(Map<String, String> compartments) {
		this.compartments = compartments;
	}

}
