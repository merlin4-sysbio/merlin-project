package pt.uminho.ceb.biosystems.merlin.gui.jpanels;

import java.awt.Component;
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
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.Workspace;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.CreateImageIcon;

public class AutomaticDraftReconstructorGUI extends JDialog{

	private String defaultCompartment;
	private String[] models, modelsField1,
	modelsField2, reactantsCompartments, metabolitesCompartmentsModel, reactionsCompartmentsModel;
	private String[][] metabolitesModel;
	private boolean applyPressed;
	protected boolean inModel;
	private JPanel jPanelDialogDraftReconstructor, jPanelName, jPanelEquation, jPanelReversible, jPanelSaveClose,
	panelModels, jPanelCompartmentReaction;
	private JTextField jTextFieldName, jTextFieldEquation;
	private JCheckBox jNonEnzymatic, jSpontaneous, jIsGeneric, jCheckBoxInModel;
	private JComboBox<String> jComboBoxNewModelWorkspace;
	private JRadioButton jBackword, jForward, jRadioButtonReversible, jRadioButtonIrreversible;
	private JScrollPane jScrollPaneModels;
	private JButton jApply, jButtonSave, jButtonCancel;
	private List<JComboBox<String>> reactantsField, reactantsCompartmentsBox;
	private JTextField[] reactantsStoichiometryField, reactantsChainsField;
	private JFormattedTextField lowerBoundary, upperBoundary;
	private ButtonGroup reversibility, direction;
	private boolean applied = false;
	
	private JComboBox<String> workspace;
	private JPanel jPanel11;
	public static boolean toImport = false;
	private String[] workspaces;
	private JLabel jLabelTaxID;

	protected Map<String, Set<String>> selectedEnzymesAndPathway;

	/**
	 * @param reactionsInterface
	 * @param rowID
	 */
	public AutomaticDraftReconstructorGUI() {

		super(Workbench.getInstance().getMainFrame());

		initGUI();

		this.startFields();

		Utilities.centerOnOwner(this);
		this.setIconImage((new ImageIcon(getClass().getClassLoader().getResource("icons/merlin.png"))).getImage());
		this.setVisible(true);		
		this.setAlwaysOnTop(true);
		this.toFront();
		Utilities.centerOnOwner(this);
	}

	/**
	 * 
	 */
	private void initGUI() {

		GroupLayout thisLayout = new GroupLayout((JPanel)getContentPane());
		this.getContentPane().setLayout(thisLayout);
		this.jPanelDialogDraftReconstructor = new JPanel(new GridBagLayout());
		thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addContainerGap().addComponent(jPanelDialogDraftReconstructor, 0, 740, Short.MAX_VALUE));
		thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup().addComponent(jPanelDialogDraftReconstructor, 0, 1000, Short.MAX_VALUE));

		GridBagLayout jPanelDialogReactionLayout = new GridBagLayout();
		jPanelDialogReactionLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.1, 0.0, 0.1, 0.0, 0.1, 0.1, 0.0};
		jPanelDialogReactionLayout.columnWidths = new int[] {8, 450, 15, 10, 7, 150, 50, 200, 15, 8};
		jPanelDialogReactionLayout.rowWeights = new double[] {0.0, 0.1, 0.0, 0.0, 0.1, 0.1, 0.1, 0.0, 0.0, 0.1, 0.0, 0.1, 0.0, 0.0, 0.1, 0.0, 0.0};
		jPanelDialogReactionLayout.rowHeights = new int[] {7, 145, 10, 7, 145, 145, 145, 7, 12, 145, 12, 170, 7, 12, 170, 10, 15};
		this.jPanelDialogDraftReconstructor.setLayout(jPanelDialogReactionLayout);

//		{
//			jPanelName = new JPanel();
//			GridBagLayout jPanelNameLayout = new GridBagLayout();
//			this.jPanelDialogReaction.add(this.jPanelName, new GridBagConstraints(1, 1, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//			jPanelNameLayout.rowWeights = new double[] {0.1};
//			jPanelNameLayout.rowHeights = new int[] {7};
//			jPanelNameLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
//			jPanelNameLayout.columnWidths = new int[] {7, 7, 7};
//			this.jPanelName.setLayout(jPanelNameLayout);
//			this.jPanelName.setBorder(BorderFactory.createTitledBorder(null, "Reaction Name", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
//			{
//				this.jTextFieldName = new JTextField();
//				this.jTextFieldName.setPreferredSize(new Dimension(180,26));
//				this.jPanelName.add(this.jTextFieldName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//			}
//		}
//		{
//			jScrollPanePathways = new JScrollPane();
//			jScrollPanePathways.setViewportView(addPathways());
//			jPanelDialogReaction.add(jScrollPanePathways, new GridBagConstraints(1, 8, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//			jPanelDialogReaction.add(jButtonAddPathways(), new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//		}
//		{
//			JPanel boundaries = new JPanel();
//			GridBagLayout jPanelBoundariesLayout = new GridBagLayout();
//			jPanelBoundariesLayout.rowWeights = new double[] {0.1, 0.0, 0.1};
//			jPanelBoundariesLayout.rowHeights = new int[] {20, 7, 20};
//			jPanelBoundariesLayout.columnWeights = new double[] {0.1, 0.0, 0.1};
//			jPanelBoundariesLayout.columnWidths = new int[] {20, 7, 20};
//			boundaries.setLayout(jPanelBoundariesLayout);
//			boundaries.setBorder(BorderFactory.createTitledBorder(null, "Flux Boundaries", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
//			jPanelDialogReaction.add(boundaries, new GridBagConstraints(5, 5, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//			JLabel lowerBoundaries = new JLabel("Lower");
//			boundaries.add(lowerBoundaries, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//			JLabel upperBoundaries = new JLabel("Upper");
//			boundaries.add(upperBoundaries, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//
//			NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
//			format.setGroupingUsed(false);
//			NumberFormatter formatter = new NumberFormatter(format);
//			formatter.setValueClass(Double.class);
//			formatter.setMinimum((-Double.MAX_VALUE));
//			formatter.setMaximum(Double.MAX_VALUE);
//			formatter.setCommitsOnValidEdit(true);
//
//			this.lowerBoundary = new JFormattedTextField(formatter);
//			this.lowerBoundary.setText("-10000");
//			boundaries.add(this.lowerBoundary, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//			this.upperBoundary = new JFormattedTextField(formatter);
//			this.upperBoundary.setText("10000");
//			boundaries.add(this.upperBoundary, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//		}
//		{
//			jPanelReversible = new JPanel();
//			GridBagLayout jPanelReversibleLayout = new GridBagLayout();
//			jPanelDialogReaction.add(jPanelReversible, new GridBagConstraints(5, 1, 1, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//			jPanelReversibleLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
//			jPanelReversibleLayout.rowHeights = new int[] {7, 20, 20};
//			jPanelReversibleLayout.columnWeights = new double[] {0.0, 0.1};
//			jPanelReversibleLayout.columnWidths = new int[] {75, 20};
//			jPanelReversible.setLayout(jPanelReversibleLayout);
//			jPanelReversible.setBorder(BorderFactory.createTitledBorder(null, "Reversibility", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
//			{
//				jRadioButtonReversible = new JRadioButton();
//				jPanelReversible.add(jRadioButtonReversible, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//				jRadioButtonReversible.setText("Reversible");
//				jRadioButtonReversible.setToolTipText("Reversible");
//				jRadioButtonReversible.addActionListener(new ActionListener(){
//
//					@Override
//					public void actionPerformed(ActionEvent e) {
//
//						if(jRadioButtonReversible.isSelected()) {
//
//							String equation = jTextFieldEquation.getText().replace(" <= ", " <=> ").replace(" => ", " <=> ");
//							jTextFieldEquation.setText(equation);
//							jTextFieldEquation.setToolTipText(equation);
//							lowerBoundary.setText("-10000");
//							jForward.setEnabled(false);
//							jBackword.setEnabled(false);
//							panelReactants.setBorder(BorderFactory.createTitledBorder(null, "Reactants", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
//							panelProducts.setBorder(BorderFactory.createTitledBorder(null, "Products", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
//						}
//					}
//				});
//			}
//			{
//				jRadioButtonIrreversible = new JRadioButton();
//				jPanelReversible.add(jRadioButtonIrreversible, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//				jRadioButtonIrreversible.setText("Irreversible");
//				jRadioButtonIrreversible.setToolTipText("Irreversible");
//				jRadioButtonIrreversible.addActionListener(new ActionListener() {
//
//					@Override
//					public void actionPerformed(ActionEvent e) {
//
//						if(jRadioButtonIrreversible.isSelected()) {
//
//							jForward.setEnabled(true);
//							jForward.setSelected(true);
//							String equation  = jTextFieldEquation.getText().replace(" <=> ", " => ").replace(" <= ", " => ");
//							jTextFieldEquation.setText(equation);
//							jTextFieldEquation.setToolTipText(equation);
//							lowerBoundary.setText("0");
//							jBackword.setEnabled(true);
//							panelReactants.setBorder(BorderFactory.createTitledBorder(null, "Reactants", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
//							panelProducts.setBorder(BorderFactory.createTitledBorder(null, "Products", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
//						}
//					}
//				});
//			}
//		}

//		{
//			jPanelEquation = new JPanel();
//			GridBagLayout jPanelEquationLayout = new GridBagLayout();
//			jPanelDialogReaction.add(jPanelEquation, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//
//			jPanelEquationLayout.rowWeights = new double[] {0.1};
//			jPanelEquationLayout.rowHeights = new int[] {7};
//			jPanelEquationLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
//			jPanelEquationLayout.columnWidths = new int[] {7, 7, 7};
//			jPanelEquation.setLayout(jPanelEquationLayout);
//			jPanelEquation.setBorder(BorderFactory.createTitledBorder(null, "Equation", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
//			{
//				jTextFieldEquation = new JTextField();
//				jPanelEquation.add(jTextFieldEquation, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//				jTextFieldEquation.setToolTipText(jTextFieldEquation.getText());
//			}
//		}

		{
			jPanelSaveClose = new JPanel(new GridBagLayout());
			GridBagLayout jPanelSaveCloseLayout = new GridBagLayout();
			jPanelSaveCloseLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelSaveCloseLayout.rowHeights = new int[] {5, 10, 5};
			jPanelSaveCloseLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelSaveCloseLayout.columnWidths = new int[] {8, 86, 13, 86, 13, 86, 8};
			//set layout
			jPanelSaveClose.setLayout(jPanelSaveCloseLayout);
			{
				jButtonSave = new JButton();
				jPanelSaveClose.add(jButtonSave, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jButtonSave.setText("Save");
				jButtonSave.setToolTipText("Save");
				jButtonSave.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Save.png")),0.1).resizeImageIcon());
				jButtonSave.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						//	boolean metabolites = false;

						if(jTextFieldName.getText().isEmpty()) {

							Workbench.getInstance().warn("Please name the reaction.");
						}
						else {
							if(!applied)
								saveData();

							closeAndUpdate();
						}
					}
				});
			}
			{
				jButtonCancel = new JButton();
				jPanelSaveClose.add(jButtonCancel, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jButtonCancel.setText("Close");
				jButtonCancel.setToolTipText("Close");
				jButtonCancel.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Cancel.png")), 0.1).resizeImageIcon());
				jButtonCancel.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if(applyPressed) {

							closeAndUpdate();
						}
						else {

							close();
						}
					}
				});			}
			{
				jApply = new JButton();
				jPanelSaveClose.add(jApply, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jApply.setText("Apply");
				jApply.setToolTipText("Apply");
				jApply.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Ok.png")), 0.1).resizeImageIcon());
				jApply.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						//						boolean metabolites = false;

						if(jTextFieldName.getText().isEmpty()) {

							Workbench.getInstance().warn("Please name the reaction.");
						}
						
					}
				});

			}
			jPanelDialogDraftReconstructor.add(jPanelSaveClose, new GridBagConstraints(1, 17, 7, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}

		{
			models = new String[0]; //reactants = new String[2][0];
			modelsField1 = new String[0];
			modelsField2= new String[0]; 
			reactantsCompartments= new String[0];

			jScrollPaneModels = new JScrollPane();
			panelModels = this.addModelsPanel();
			jScrollPaneModels.setViewportView(panelModels);

			jPanelDialogDraftReconstructor.add(jScrollPaneModels, new GridBagConstraints(1, 3, 7, 2, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}

		
		//ButtonAddReactant
		{
			jPanelDialogDraftReconstructor.add(jButtonAddReferenceModel(), new GridBagConstraints(8, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		//  Properties Panel
//		{
//
//			GridBagLayout jPanelPropertiesLayout = new GridBagLayout();
//			jPanelPropertiesLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
//			jPanelPropertiesLayout.rowHeights = new int[] {10, 5, 10, 5, 10, 5, 10};
//			jPanelPropertiesLayout.columnWeights = new double[] {0.1, 0.1};
//			jPanelPropertiesLayout.columnWidths = new int[] {20, 20};
//			JPanel jPanelProperties = new JPanel(jPanelPropertiesLayout);
//			jPanelProperties.setBorder(BorderFactory.createTitledBorder(null, "Properties", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
//			jPanelDialogReaction.add(jPanelProperties, new GridBagConstraints(3, 1, 2, 6, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//
//			jCheckBoxInModel = new JCheckBox();
//			jCheckBoxInModel.setText("In Model");
//			jCheckBoxInModel.setToolTipText("In Model");
//			jPanelProperties.add(jCheckBoxInModel, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//			jSpontaneous = new JCheckBox();
//			jSpontaneous.setText("Spontaneous");
//			jSpontaneous.setToolTipText("Spontaneous");
//			jPanelProperties.add(jSpontaneous, new GridBagConstraints(2, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//			jNonEnzymatic = new JCheckBox();
//			jNonEnzymatic.setText("Non Enzymatic");
//			jNonEnzymatic.setToolTipText("Non Enzymatic");
//			jPanelProperties.add(jNonEnzymatic, new GridBagConstraints(2, 4, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//			jIsGeneric = new JCheckBox();
//			jIsGeneric.setText("Is Generic");
//			jIsGeneric.setToolTipText("Is Generic");
//			jPanelProperties.add(jIsGeneric, new GridBagConstraints(2, 6, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//
//		}
		//reactionlocalisation
		{
			jComboBoxNewModelWorkspace = new JComboBox<>();
			jPanelCompartmentReaction = new JPanel();
			GridBagLayout jPanelCompartmentReactionLayout = new GridBagLayout();
			jPanelCompartmentReaction.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "New Model", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
			jPanelCompartmentReactionLayout.rowWeights = new double[] {0.0};
			jPanelCompartmentReactionLayout.rowHeights = new int[] {7};
			jPanelCompartmentReactionLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
			jPanelCompartmentReactionLayout.columnWidths = new int[] {7, 7, 7};
			jPanelCompartmentReaction.setLayout(jPanelCompartmentReactionLayout);
			jPanelCompartmentReaction.add(jComboBoxNewModelWorkspace, new GridBagConstraints(1, -1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDialogDraftReconstructor.add(jPanelCompartmentReaction, new GridBagConstraints(1, 1, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			
		}
		
		this.setModal(true);
		pack();
		this.setResizable(false);

	}

	/**
	 * 
	 */
	public void close(){}

	/**
	 * 
	 */
	public void closeAndUpdate(){}

	/**
	 * @return reactants panel
	 */
	private JPanel addModelsPanel() {

		JPanel panelModel = new JPanel();
		GridBagLayout jPanelModelLayout = new GridBagLayout();

		//number of rows (array size) 
		if(models.length==0) {

			jPanelModelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelModelLayout.rowHeights = new int[] {7, 7, 7, 7};
		}
		else {

			jPanelModelLayout.rowWeights = new double[models.length*2+2];
			jPanelModelLayout.rowHeights  = new int[models.length*2+2];

			for(int rh=0; rh<models.length*2+2; rh++) {

				jPanelModelLayout.rowWeights[rh]=0.1;
				jPanelModelLayout.rowHeights[rh]=7;
			}
		}
		//number of columns (array size)
		jPanelModelLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0};
		jPanelModelLayout.columnWidths = new int[] {1, 7, 1, 7, 1, 7, 1, 7, 1};
		//set layout
		panelModel.setLayout(jPanelModelLayout);
		panelModel.setBorder(BorderFactory.createTitledBorder(null, "Reference models", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
		panelModel.add(new JLabel("Model"), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panelModel.add(new JLabel("Stoichiometry"), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panelModel.add(new JLabel("Chains number"), new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panelModel.add(new JLabel("Localization"), new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//		panelReactants.add(this.addReactantCompartmentButton(), new GridBagConstraints(7, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		if(models.length==0) {

			models = new String[1];
			models[0]= "";
			reactantsField = new ArrayList<>(1);

			//			JComboBox<String> rbox = reactantsField.get(0);
			//			JComboBox<String> rCompartmentsBox = reactantsCompartmentsBox.get(0);

			JComboBox<String> rbox;

			if(workspaces==null)
				rbox=new JComboBox<>();
			else
				rbox=new JComboBox<String>(workspaces);
			rbox.setPreferredSize(new Dimension(580, 26));
//			rbox.setSelectedIndex(0);
			if(rbox.getSelectedItem()!=null)
				rbox.setToolTipText(rbox.getSelectedItem().toString());
			rbox.addActionListener(new ActionListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void actionPerformed(ActionEvent arg0) {

					comboBoxActionListener(reactantsField,(JComboBox<String>) arg0.getSource());

					if(((JComboBox<String>) arg0.getSource()).getSelectedItem() != null)
						((JComponent) arg0.getSource()).setToolTipText(((JComboBox<String>) arg0.getSource()).getSelectedItem().toString());
				}
			});

			modelsField1 = new String[1];
			modelsField1[0] = "-1";
			reactantsStoichiometryField = new JTextField[1];
			reactantsStoichiometryField[0] = new JTextField();
			reactantsStoichiometryField[0].setText(modelsField1[0]);

			reactantsChainsField = new JTextField[1];
			modelsField2 = new String[1];
			modelsField2[0]="1";
			reactantsChainsField[0]= new JTextField();
			reactantsChainsField[0].setText(modelsField2[0]);

			reactantsCompartments=new String[1];
			reactantsCompartments[0] = this.defaultCompartment;

			reactantsCompartmentsBox = new ArrayList<>(1);


//			JComboBox<String> rCompartmentsBox = new JComboBox<String>();

//			if(metabolitesCompartmentsModel!=null && metabolitesCompartmentsModel.length>0) {
//
//				rCompartmentsBox = new JComboBox<String>(metabolitesCompartmentsModel); 
//				rCompartmentsBox.setSelectedIndex(0);
//			}

			panelModel.add(rbox, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			panelModel.add(reactantsStoichiometryField[0], new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			panelModel.add(reactantsChainsField[0], new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//			panelModel.add(rCompartmentsBox, new GridBagConstraints(7, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

			reactantsField.add(0,rbox);
//			reactantsCompartmentsBox.add(0,rCompartmentsBox);
		}
		else {

			int size = 1;
			if(models.length>1)
				size = models.length;

			reactantsField = new ArrayList<>(size);
			reactantsStoichiometryField = new JTextField[size];
			reactantsChainsField =  new JTextField[size];
			reactantsCompartmentsBox = new ArrayList<>(size);

			for(int s=0; s<size; s++) {

				//					JComboBox<String> rbox = reactantsField.get(s);
				//					JComboBox<String> rCompartmentsBox = reactantsCompartmentsBox.get(s);

				JComboBox<String> rbox = new JComboBox<String>(workspaces);
				rbox.setPreferredSize(new Dimension(580, 26));

				if(models[s]==null)
					rbox.setSelectedIndex(0);
				else
					rbox.setSelectedIndex(Arrays.asList(workspaces).indexOf(models[s]));

				if(rbox.getSelectedItem()!=null)
					rbox.setToolTipText(rbox.getSelectedItem().toString());
				rbox.addActionListener(new ActionListener() {

					@SuppressWarnings("unchecked")
					@Override
					public void actionPerformed(ActionEvent arg0) {

//
//						comboBoxActionListener(reactantsField,(JComboBox<String>) arg0.getSource());
//
//						if(((JComboBox<String>) arg0.getSource()).getSelectedItem() != null)
//							((JComponent) arg0.getSource()).setToolTipText(((JComboBox<String>) arg0.getSource()).getSelectedItem().toString());

					}
				});

				reactantsStoichiometryField[s] = new JTextField();
				reactantsChainsField[s] = new JTextField();;
				if(modelsField1.length<=s) {

//					modelsField1[]="-1";
//					modelsField2[ss]="1";
				}
//				reactantsStoichiometryField[s].setText(modelsField1[s]);
//				reactantsChainsField[s].setText(modelsField2[s]);
				JComboBox<String> rCompartmentsBox = new JComboBox<String>(new String[1]);
//				if(reactantsCompartments[s]==null)
//					rCompartmentsBox.setSelectedIndex(0);
//				else
//					rCompartmentsBox.setSelectedItem(reactantsCompartments[s]);

				int r =s*2+2;
				panelModel.add(rbox, new GridBagConstraints(1, r, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				panelModel.add(reactantsStoichiometryField[s], new GridBagConstraints(3, r, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				panelModel.add(reactantsChainsField[s], new GridBagConstraints(5, r, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//				panelModel.add(rCompartmentsBox, new GridBagConstraints(7, r, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

				reactantsField.add(s,rbox);
//				reactantsCompartmentsBox.add(s,rCompartmentsBox);
			}
		}

		return panelModel;
	}


	/**
	 * @return
	 */
	private Component addReactantCompartmentButton() {

		JButton add = new JButton();
		add.setIcon((new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Add.png")), 0.03).resizeImageIcon()));
		add.setText("Compartment");
		add.setToolTipText("add Compartment");
//		add.addActionListener(new ActionListener(){
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				new InsertCompartment(reactionsInterface)
//				{
//					private static final long serialVersionUID = 1L;
//					public void finish() {
//						this.setVisible(false);
//						this.dispose();
//						boolean isCompartmentalisedModel = reactionsInterface.getProject().isCompartmentalisedModel();
//						metabolitesCompartmentsModel = reactionsInterface.getCompartments(true, isCompartmentalisedModel);
//						jScrollPaneReactants.setViewportView(addReactantsPanel());
//						jScrollPaneProducts.setViewportView(addProductsPanel());
//						System.gc();
//					}						
//				};
//
//			}});
		return add;
	}

	
	/**
	 * Save data 
	 */
	private void saveData() {

//		Map<String, String> metabolites = new TreeMap<String, String>();
//		Map<String, String > chains = new TreeMap<String, String >(), compartments = new TreeMap<String, String >();
//		boolean go = true;
//
//		for(int i=0; i< reactantsField.size(); i++) {
//
//			int selectedIndex = reactantsField.get(i).getSelectedIndex();
//
//			if(selectedIndex>0) {
//
//				String signal = "-";
//
//				if(jBackword.isSelected())
//					signal = "";
//
//				reactants[i]= signal+metabolitesModel[0][selectedIndex];
//				String stoich = reactantsStoichiometryField[i].getText();
//
//				if(stoich.startsWith("-")) {
//
//					if(signal.isEmpty())
//						stoich=stoich.substring(1);
//				}
//				else {
//
//					if(signal.equalsIgnoreCase("-"))
//						stoich=signal+stoich;
//				}
//
//				metabolites.put(reactants[i].trim(), stoich);
//				chains.put(reactants[i].trim(), reactantsChainsField[i].getText().trim());
//				compartments.put(reactants[i].trim(), reactantsCompartmentsBox.get(i).getSelectedItem().toString().trim());
//
//				if (stoich.length()>18) {
//
//					BigDecimal bigDecimal = new BigDecimal(stoich);
//					bigDecimal = bigDecimal.setScale(18, RoundingMode.HALF_UP);
//
//					//						go = false;
//					//						Workbench.getInstance().error("Cannot add reaction. Stoichiometry value ("+stoich+") for "+reactantsField.get(i).getSelectedItem()+" to large.");
//					//						i = reactantsField.size();
//
//					stoich = bigDecimal.doubleValue()+"";
//				}
//			}
//		}
//
//		if(go)
//			for(int i=0; i< productsField.size(); i++) {
//
//				int selectedIndex = productsField.get(i).getSelectedIndex();
//
//				if(selectedIndex>0) {
//
//					String signal = "";
//
//					if(jBackword.isSelected())
//						signal = "-";
//
//					products[i]= signal+metabolitesModel[0][selectedIndex];
//					String stoich = productsStoichiometryField[i].getText();
//
//					if(stoich.startsWith("-")) {
//
//						if(signal.isEmpty())
//							stoich=stoich.substring(1);
//					}
//					else {
//
//						if(signal.equalsIgnoreCase("-"))
//							stoich=signal+stoich;
//					}
//
//					metabolites.put(products[i].trim(), stoich.trim());
//					chains.put(products[i].trim(), productsChainsField[i].getText().trim());
//					compartments.put(products[i].trim(), productsCompartmentsBox.get(i).getSelectedItem().toString().trim());
//
//					if (stoich.length()>18) {
//
//						BigDecimal bigDecimal = new BigDecimal(stoich);
//						bigDecimal = bigDecimal.setScale(18, RoundingMode.HALF_UP);
//
//						//							go = false;
//						//							Workbench.getInstance().error("Cannot add reaction. Stoichiometry value ("+stoich+") for "+productsField.get(i).getSelectedItem()+" to large.");
//						//							i = productsField.size();
//
//						stoich = bigDecimal.doubleValue()+"";
//					}
//				}
//			}
//
//		String boolean_rule = null;
//		{
//			if(this.genesField.size()>0)
//				boolean_rule="";
//
//			Map<String, String> geneModelIDs = reactionsInterface.getGenesModelID();
//
//			for(int l=0;l <this.genesField.size();l++) {
//
//				List<JComboBox<String>> genesAnd = this.genesField.get(l);
//
//				String boolean_rule_row="";
//				for(int j=0; j<genesAnd.size();j++) {
//
//					if(genesAnd.get(j).getSelectedItem()!=null) {
//
//						String geneName = genesAnd.get(j).getSelectedItem().toString();
//
//						if(!geneName.isEmpty()) {
//
//							String gene = geneModelIDs.get(geneName);
//
//							if(j>0)
//								boolean_rule_row = boolean_rule_row.concat(" AND ");
//
//							boolean_rule_row = boolean_rule_row.concat(gene);
//						}
//					}
//				}
//
//				if(l>0 && !boolean_rule_row.isEmpty())
//					boolean_rule = boolean_rule.concat(" OR ");
//
//				boolean_rule = boolean_rule.concat(boolean_rule_row);
//			}
//		}
//
//		if(go) {
//
//			this.updatePathways();
//			if(rowID == -10) {
//
//				reactionsInterface.insertNewReaction(jTextFieldName.getText(), jTextFieldEquation.getText(), jRadioButtonReversible.isSelected(),
//						chains, compartments, metabolites, jCheckBoxInModel.isSelected(), selectedEnzymesAndPathway, jComboBoxLocalisation.getSelectedItem().toString(),
//						jSpontaneous.isSelected(), jNonEnzymatic.isSelected(), jIsGeneric.isSelected(),
//						Double.valueOf(lowerBoundary.getText()), Double.valueOf(upperBoundary.getText()), InformationType.MANUAL.toString(), boolean_rule);
//			}
//			else {
//
//				reactionsInterface.updateReaction(rowID, jTextFieldName.getText(), jTextFieldEquation.getText(), jRadioButtonReversible.isSelected(), //enzymesSet,
//						chains, compartments, metabolites, jCheckBoxInModel.isSelected(), selectedEnzymesAndPathway, jComboBoxLocalisation.getSelectedItem().toString(), 
//						jSpontaneous.isSelected(), jNonEnzymatic.isSelected(), jIsGeneric.isSelected(),
//						Double.valueOf(lowerBoundary.getText()), Double.valueOf(upperBoundary.getText()), boolean_rule);
//			}
//			this.inModel = jCheckBoxInModel.isSelected();
//			applyPressed=true;
//		}
	}

	/**
	 * 
	 */
	private void startFields() {
		
		{
			
			List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(Workspace.class);
			workspaces = new String[cl.size()];
			for (int i = 0; i < cl.size(); i++) {
				workspaces[i] = (cl.get(i).getName());
			}
			
			ComboBoxModel<String> jComboBoxLocalisationModel = new DefaultComboBoxModel<>(workspaces);
			jComboBoxNewModelWorkspace.setModel(jComboBoxLocalisationModel);
			
			
			if (workspaces.length > 0) {
				WorkspaceAIB projectData = (WorkspaceAIB) Core.getInstance().getClipboard().getItemsByClass(Workspace.class)
						.get(0).getUserData();
				jComboBoxNewModelWorkspace.setSelectedItem(projectData.getName());
			}
			
		}
		
		models = workspaces; //reactants = new String[2][0];

		panelModels = this.addModelsPanel();
		jScrollPaneModels.setViewportView(panelModels);
	}

	/**
	 * @return
	 */
	private JButton jButtonAddReferenceModel() {

		JButton jButtonAddModel = new JButton();
		jButtonAddModel.setIcon((new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Add.png")), 0.04).resizeImageIcon()));
		jButtonAddModel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					String[] newModels = new String[reactantsField.size()+1];
					String[] newReactantsStoichiometry = new String[reactantsStoichiometryField.length+1];
					String[] newReactantsChains = new String[reactantsChainsField.length+1];
					String[] newReactantsComp = new String[reactantsCompartmentsBox.size()+1];

					for(int i=0; i<reactantsField.size();i++) {

						newModels[i]=Arrays.asList(metabolitesModel[0]).get(reactantsField.get(i).getSelectedIndex()).toString();
						newReactantsStoichiometry[i] = reactantsStoichiometryField[i].getText().toString();
						newReactantsChains[i] = reactantsChainsField[i].getText().toString();
						newReactantsComp[i] = reactantsCompartmentsBox.get(i).getSelectedItem().toString();
					}

					newModels[reactantsField.size()]="";
					newReactantsStoichiometry[reactantsStoichiometryField.length]="-1";
					newReactantsChains[reactantsChainsField.length] = "1";
					newReactantsComp[reactantsCompartmentsBox.size()] = defaultCompartment;
					models=newModels;
					modelsField1 = newReactantsStoichiometry;
					modelsField2=newReactantsChains;
					reactantsCompartments=newReactantsComp;
					jScrollPaneModels.setViewportView(addModelsPanel());

				} catch (ArrayIndexOutOfBoundsException e1) {
					Workbench.getInstance().warn("please select a reactant metabolite before add a new one.");
				}
				catch (Exception e2){
					e2.printStackTrace();
				}
			}
		});
		return jButtonAddModel;
	}

	

	/**
	 * @param comboArray
	 * @param comboBox
	 */
	private boolean comboBoxActionListener(List<JComboBox<String>> comboArray, JComboBox<String> comboBox) {

		for(JComboBox<String> r:comboArray) {

			if(!comboBox.equals(r)) {

				if(comboBox.getSelectedIndex() == r.getSelectedIndex() && !comboBox.getSelectedItem().toString().equals("")) {

					Workbench.getInstance().warn("Entity already selected!");
					comboBox.setSelectedIndex(0);
					return false;
				}
			}
		}
		return true;
	}

}
