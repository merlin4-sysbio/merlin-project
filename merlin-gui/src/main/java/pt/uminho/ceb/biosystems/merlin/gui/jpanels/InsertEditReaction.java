package pt.uminho.ceb.biosystems.merlin.gui.jpanels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
import javax.swing.text.NumberFormatter;

import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.ModelReactionsAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.CreateImageIcon;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelCompartmentServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelProteinsServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelReactionsServices;

/**
 * @author ODias
 *
 */
public class InsertEditReaction extends JDialog {

	private static final long serialVersionUID = -1L;
	private ModelReactionsAIB reactionsInterface;

	private String selectedPathway, defaultCompartment;
	private int rowID;
	private String[] enzymesModel, pathwaysModel, reactants, 
	reactantsStoichiometry, productsStoichiometry,
	products, //reactantsChains, productsChains,
	reactantsCompartments, productsCompartments, 
	metabolitesCompartmentsModel, reactionsCompartmentsModel, genesModel;
	private Map<Integer, MetaboliteContainer> metabolitesModel;
	private boolean applyPressed;
	protected boolean inModel;
	private JPanel jPanelDialogReaction, jPanelName, jPanelEquation, jPanelReversible, jPanelSaveClose,
	panelReactants, panelProducts, jPanelCompartmentReaction;
	private JTextField jTextFieldName, jTextFieldEquation;
	private JCheckBox jNonEnzymatic, jSpontaneous, jIsGeneric, jCheckBoxInModel;
	private JComboBox<String> jComboBoxLocalisation;
	private JRadioButton jBackword, jForward, jRadioButtonReversible, jRadioButtonIrreversible;
	private JScrollPane jScrollPaneEnzymes, jScrollPanePathways, jScrollPaneReactants, jScrollPaneProducts, jScrollPaneGeneRule;
	private JButton jApply, jButtonSave, jButtonCancel;
	private List<JComboBox<String>> reactantsField, reactantsExternalIdentifiersField, productsCompartmentsBox, enzymesField, pathwaysField, reactantsCompartmentsBox, productsField, productsExternalIdentifiersField;
	private List<List<JComboBox<String>>> genesField;
	private JTextField[] reactantsStoichiometryField, productsStoichiometryField;
	private JFormattedTextField lowerBoundary, upperBoundary;
	private ButtonGroup reversibility, direction;
	private boolean applied = false;

	private Map<Integer, MetaboliteContainer> reactionMetabolites;
	protected Map<String, Set<String>> selectedEnzymesAndPathway;
	private List<List<Integer>> geneRule_AND_OR;
	private Map<String,List<String>> mapMetabolites;
	private boolean isCompartmentalisedModel;
	private Map<String,String> mapMetabolitesIndex;

	/**
	 * @param reactionsInterface
	 * @param rowID
	 * @throws Exception 
	 */
	public InsertEditReaction(ModelReactionsAIB reactionsInterface, int rowID) throws Exception {

		super(Workbench.getInstance().getMainFrame());
		this.reactionsInterface = reactionsInterface;
		this.applyPressed=false;
		this.metabolitesModel = reactionsInterface.getAllMetabolites();
		initializeMapMetabolites();
		this.isCompartmentalisedModel = ProjectServices.isCompartmentalisedModel(reactionsInterface.getWorkspace().getName());
		this.metabolitesCompartmentsModel = reactionsInterface.getCompartments(true, isCompartmentalisedModel);
		this.reactionsCompartmentsModel = reactionsInterface.getCompartmentsForReactions(false, this.isCompartmentalisedModel);
		this.pathwaysModel = reactionsInterface.getAllPathways(false);
		this.enzymesModel = reactionsInterface.getEnzymesModel();
		this.genesModel = reactionsInterface.getGenesModel();
		Set<String> enzymesSet = new TreeSet<String>();
		Set<String> allEnzymes = new TreeSet<String>();
		this.selectedPathway="-1allpathwaysinreaction";
		this.selectedEnzymesAndPathway = new TreeMap<String, Set<String>>();
		this.geneRule_AND_OR = new ArrayList<>();
		this.defaultCompartment = null;

		this.rowID=rowID;

		if(rowID < 0) {

			this.setTitle("insert reaction");
			enzymesSet=new TreeSet<String>();
			this.selectedEnzymesAndPathway.put("-1allpathwaysinreaction", enzymesSet);
			//this.selectedEnzymesAndPathway.put("", new HashSet<String>());
		}
		else {

			this.setTitle("edit reaction");
			String[] pathways = reactionsInterface.getPathways(rowID);

			if(pathways == null || pathways.length==0) {

				allEnzymes = reactionsInterface.getEnzymesForReaction(rowID);
			} 
			else {

				for(String pathway : pathways) {

					String[] e = null;
					try {
						e = reactionsInterface.getEnzymes(rowID, reactionsInterface.getPathwayID(pathway), true);
					} 
					catch (Exception e1) {
						Workbench.getInstance().error(e1);
						e1.printStackTrace();
					}

					enzymesSet=new TreeSet<String>();
					enzymesSet.addAll(new TreeSet<String>(Arrays.asList(e)));
					allEnzymes.addAll(enzymesSet);
					this.selectedEnzymesAndPathway.put(pathway, enzymesSet);
				}
			}

			this.selectedEnzymesAndPathway.put(this.selectedPathway, allEnzymes);
		}

		
		initGUI();
		
		if(rowID == -10) {

			this.jRadioButtonReversible.setSelected(true);
			this.jCheckBoxInModel.setSelected(true);
		}
		else {

			this.startFields();
		}
		
	
		

		enzymesSet=new TreeSet<>();
		Utilities.centerOnOwner(this);
		this.setIconImage((new ImageIcon(getClass().getClassLoader().getResource("icons/merlin.png"))).getImage());
		this.setVisible(true);		
		this.setAlwaysOnTop(true);
		this.toFront();
		Utilities.centerOnOwner(this);
	}

	private void initializeMapMetabolites() {

		this.mapMetabolites = new HashMap<>();
		this.mapMetabolitesIndex = new HashMap<>();

		for(MetaboliteContainer metaboliteContainer : this.metabolitesModel.values()) {

			List<String> elements = new ArrayList<String>();

			//First element of the list is Name
			elements.add(metaboliteContainer.getName());
			//Second is Formula
			elements.add(metaboliteContainer.getFormula());
			//Key is ID
			mapMetabolites.put(metaboliteContainer.getExternalIdentifier(), elements);
			
			// previously we only used the metabolite name to fill the mapMetabolitesIndex, however there are different metabolites with the same name (glucans) thus it was necessary to use the external identifier instead
			mapMetabolitesIndex.put(metaboliteContainer.getExternalIdentifier(), metaboliteContainer.getMetaboliteID()+"");

		}

		//		for(int i : this.metabolitesModel.keySet()) {
		//
		//			List<String> elements = new ArrayList<String>();
		//
		//			if(splitedList.size() == 3) {
		//				//First element of the list is Name
		//				elements.add(splitedList.get(0));
		//				//Second is Formula
		//				elements.add(splitedList.get(1));
		//
		//				//Key is ID
		//				mapMetabolites.put(splitedList.get(2), elements);
		//
		//			}
		//			else if(splitedList.size() == 2){
		//				//Only element of the list is Name
		//				elements.add(splitedList.get(0));
		//				//Key is ID
		//				mapMetabolites.put(splitedList.get(1), elements);
		//
		//			}
		//			else if(splitedList.size() == 1) {
		//				//In this case, the name is the ID
		//				mapMetabolites.put(splitedList.get(0), elements);
		//			}
		//
		//			mapMetabolitesIndex.put(splitedList.get(0), this.metabolitesModel[0][i]); 
		//
		//		}
	}

	/**
	 * @throws Exception 
	 * @throws IOException 
	 * 
	 */
	private void initGUI() throws IOException, Exception {

		GroupLayout thisLayout = new GroupLayout((JPanel)getContentPane());
		this.getContentPane().setLayout(thisLayout);
		this.jPanelDialogReaction = new JPanel(new GridBagLayout());
		thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addContainerGap().addComponent(jPanelDialogReaction, 0, 740, Short.MAX_VALUE));
		thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup().addComponent(jPanelDialogReaction, 0, 1000, Short.MAX_VALUE));

		GridBagLayout jPanelDialogReactionLayout = new GridBagLayout();
		jPanelDialogReactionLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.1, 0.0, 0.1, 0.0, 0.1, 0.1, 0.0};
		jPanelDialogReactionLayout.columnWidths = new int[] {8, 450, 15, 10, 7, 150, 50, 200, 15, 8};
		jPanelDialogReactionLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1, 0.0, 0.1, 0.0, 0.0, 0.1, 0.0, 0.0};
		jPanelDialogReactionLayout.rowHeights = new int[] {7, 10, 10, 7, 20, 7, 15, 7, 12, 145, 12, 170, 7, 12, 170, 10, 15};
		this.jPanelDialogReaction.setLayout(jPanelDialogReactionLayout);

		{
			jPanelName = new JPanel();
			GridBagLayout jPanelNameLayout = new GridBagLayout();
			this.jPanelDialogReaction.add(this.jPanelName, new GridBagConstraints(1, 1, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelNameLayout.rowWeights = new double[] {0.1};
			jPanelNameLayout.rowHeights = new int[] {7};
			jPanelNameLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
			jPanelNameLayout.columnWidths = new int[] {7, 7, 7};
			this.jPanelName.setLayout(jPanelNameLayout);
			this.jPanelName.setBorder(BorderFactory.createTitledBorder(null, "Reaction Name", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
			{
				this.jTextFieldName = new JTextField();
				this.jTextFieldName.setPreferredSize(new Dimension(180,26));
				this.jPanelName.add(this.jTextFieldName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		{
			jScrollPanePathways = new JScrollPane();
			jScrollPanePathways.setViewportView(addPathways());
			jPanelDialogReaction.add(jScrollPanePathways, new GridBagConstraints(1, 8, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDialogReaction.add(jButtonAddPathways(), new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		{
			JPanel boundaries = new JPanel();
			GridBagLayout jPanelBoundariesLayout = new GridBagLayout();
			jPanelBoundariesLayout.rowWeights = new double[] {0.1, 0.0, 0.1};
			jPanelBoundariesLayout.rowHeights = new int[] {20, 7, 20};
			jPanelBoundariesLayout.columnWeights = new double[] {0.1, 0.0, 0.1};
			jPanelBoundariesLayout.columnWidths = new int[] {20, 7, 20};
			boundaries.setLayout(jPanelBoundariesLayout);
			boundaries.setBorder(BorderFactory.createTitledBorder(null, "Flux Boundaries", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
			jPanelDialogReaction.add(boundaries, new GridBagConstraints(5, 5, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			JLabel lowerBoundaries = new JLabel("Lower");
			boundaries.add(lowerBoundaries, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			JLabel upperBoundaries = new JLabel("Upper");
			boundaries.add(upperBoundaries, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

			NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
			format.setGroupingUsed(false);
			NumberFormatter formatter = new NumberFormatter(format);
			formatter.setValueClass(Double.class);
			formatter.setMinimum((-Double.MAX_VALUE));
			formatter.setMaximum(Double.MAX_VALUE);
			formatter.setCommitsOnValidEdit(true);

			this.lowerBoundary = new JFormattedTextField(formatter);
			this.lowerBoundary.setText("-10000");
			boundaries.add(this.lowerBoundary, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			this.upperBoundary = new JFormattedTextField(formatter);
			this.upperBoundary.setText("10000");
			boundaries.add(this.upperBoundary, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		{
			jPanelReversible = new JPanel();
			GridBagLayout jPanelReversibleLayout = new GridBagLayout();
			jPanelDialogReaction.add(jPanelReversible, new GridBagConstraints(5, 1, 1, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelReversibleLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelReversibleLayout.rowHeights = new int[] {7, 20, 20};
			jPanelReversibleLayout.columnWeights = new double[] {0.0, 0.1};
			jPanelReversibleLayout.columnWidths = new int[] {75, 20};
			jPanelReversible.setLayout(jPanelReversibleLayout);
			jPanelReversible.setBorder(BorderFactory.createTitledBorder(null, "Reversibility", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
			{
				jRadioButtonReversible = new JRadioButton();
				jPanelReversible.add(jRadioButtonReversible, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jRadioButtonReversible.setText("Reversible");
				jRadioButtonReversible.setToolTipText("Reversible");
				jRadioButtonReversible.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {

						if(jRadioButtonReversible.isSelected()) {

							String equation = jTextFieldEquation.getText().replace(" <= ", " <=> ").replace(" => ", " <=> ");
							jTextFieldEquation.setText(equation);
							jTextFieldEquation.setToolTipText(equation);
							lowerBoundary.setText("-10000");
							jForward.setEnabled(false);
							jBackword.setEnabled(false);
							panelReactants.setBorder(BorderFactory.createTitledBorder(null, "Reactants", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
							panelProducts.setBorder(BorderFactory.createTitledBorder(null, "Products", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
						}
					}
				});
			}
			{
				jRadioButtonIrreversible = new JRadioButton();
				jPanelReversible.add(jRadioButtonIrreversible, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jRadioButtonIrreversible.setText("Irreversible");
				jRadioButtonIrreversible.setToolTipText("Irreversible");
				jRadioButtonIrreversible.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						if(jRadioButtonIrreversible.isSelected()) {

							jForward.setEnabled(true);
							jForward.setSelected(true);
							String equation  = jTextFieldEquation.getText().replace(" <=> ", " => ").replace(" <= ", " => ");
							jTextFieldEquation.setText(equation);
							jTextFieldEquation.setToolTipText(equation);
							lowerBoundary.setText("0");
							jBackword.setEnabled(true);
							panelReactants.setBorder(BorderFactory.createTitledBorder(null, "Reactants", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
							panelProducts.setBorder(BorderFactory.createTitledBorder(null, "Products", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
						}
					}
				});
			}
			{
				reversibility = new ButtonGroup();
				reversibility.add(jRadioButtonIrreversible);
				reversibility.add(jRadioButtonReversible);
			}

			{
				jForward = new JRadioButton();
				jForward.setEnabled(false);
				jForward.setText("=>");
				jForward.setToolTipText("Forward");
				jPanelReversible.add(jForward, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jForward.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {

						if(jForward.isSelected()) {

							String equation  = jTextFieldEquation.getText().replace(" <=> ", " => ").replace(" <= ", " => ");
							jTextFieldEquation.setText(equation);
							jTextFieldEquation.setToolTipText(equation);
							panelReactants.setBorder(BorderFactory.createTitledBorder(null, "reactants", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
							panelProducts.setBorder(BorderFactory.createTitledBorder(null, "products", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
						}
					}
				});

				jBackword = new JRadioButton();
				jBackword.setEnabled(false);
				jBackword.setText("<=");
				jBackword.setToolTipText("Backward");
				jPanelReversible.add(jBackword, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jBackword.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {

						if(jBackword.isSelected()) {

							String equation = jTextFieldEquation.getText().replace(" <=> ", " <= ").replace(" => ", " <= ");
							jTextFieldEquation.setText(equation);
							jTextFieldEquation.setToolTipText(equation);
							panelReactants.setBorder(BorderFactory.createTitledBorder(null, "products", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
							panelProducts.setBorder(BorderFactory.createTitledBorder(null, "reactants", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
						}
					}
				});
			}
			{
				direction = new ButtonGroup();
				direction.add(jForward);
				direction.add(jBackword);
			}
		}

		{
			jPanelEquation = new JPanel();
			GridBagLayout jPanelEquationLayout = new GridBagLayout();
			jPanelDialogReaction.add(jPanelEquation, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

			jPanelEquationLayout.rowWeights = new double[] {0.1};
			jPanelEquationLayout.rowHeights = new int[] {7};
			jPanelEquationLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
			jPanelEquationLayout.columnWidths = new int[] {7, 7, 7};
			jPanelEquation.setLayout(jPanelEquationLayout);
			jPanelEquation.setBorder(BorderFactory.createTitledBorder(null, "Equation", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
			{
				jTextFieldEquation = new JTextField();
				jPanelEquation.add(jTextFieldEquation, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jTextFieldEquation.setToolTipText(jTextFieldEquation.getText());
			}
		}

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
				jButtonSave.setText("save");
				jButtonSave.setToolTipText("save");
				jButtonSave.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Save.png")),0.1).resizeImageIcon());
				jButtonSave.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						//	boolean metabolites = false;

						if(jTextFieldName.getText().isEmpty()) {

							Workbench.getInstance().warn("please name the reaction.");
						}
						else {
							if(!applied)
								try {
									saveData();
									closeAndUpdate();
								} catch (NumberFormatException e1) {
									Workbench.getInstance().error("please insert a valid stoichiometry value!");
									e1.printStackTrace();
								} catch (Exception e1) {
									Workbench.getInstance().error(e1);
									e1.printStackTrace();
								}
						}
					}
				});
			}
			{
				jButtonCancel = new JButton();
				jPanelSaveClose.add(jButtonCancel, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jButtonCancel.setText("close");
				jButtonCancel.setToolTipText("close");
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
				jApply.setText("apply");
				jApply.setToolTipText("apply");
				jApply.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Ok.png")), 0.1).resizeImageIcon());
				jApply.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						//						boolean metabolites = false;

						if(jTextFieldName.getText().isEmpty()) {

							Workbench.getInstance().warn("please name the reaction.");
						}
						else {

							try {
								saveData();
							}
							catch (NumberFormatException e1) {
								e1.printStackTrace();
							}
							catch (Exception e1) {
								Workbench.getInstance().error(e1);
								e1.printStackTrace();
							}
//							try {
//								rowID = ModelReactionsServices.getReactionID(jTextFieldName.getText(), reactionsInterface.getWorkspace().getName(), jComboBoxLocalisation.getSelectedItem().toString()); // é preciso procurar as reacçoes por nome e compartimento, caso contrário vai dar várias
//							} catch (Exception e1) {
//								Workbench.getInstance().error(e1);
//								e1.printStackTrace();
//							}
//							if(rowID<0)
//								try {
//									rowID = ModelReactionsServices.getReactionID("R_"+jTextFieldName.getText(), reactionsInterface.getWorkspace().getName());
//								} catch (Exception e1) {
//									Workbench.getInstance().error(e1);
//									e1.printStackTrace();
//								}
//							if(rowID<0)
//								try {
//									rowID = ModelReactionsServices.getReactionID("T_"+jTextFieldName.getText(), reactionsInterface.getWorkspace().getName());
//								} catch (Exception e1) {
//									Workbench.getInstance().error(e1);
//									e1.printStackTrace();
//								}


							applied = true;
						}
					}
				});

			}
			jPanelDialogReaction.add(jPanelSaveClose, new GridBagConstraints(1, 17, 7, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}

		{
			reactants = new String[0]; //reactants = new String[2][0];
			reactantsStoichiometry = new String[0];
//			reactantsChains= new String[0]; reactantsCompartments= new String[0];
			products = new String[0]; //products = new String[2][0];
			productsStoichiometry = new String[0];
//			productsChains= new String[0];	productsCompartments= new String[0];

			jScrollPaneReactants = new JScrollPane();
			panelReactants = this.addReactantsPanel();
			jScrollPaneReactants.setViewportView(panelReactants);
			jScrollPaneProducts = new JScrollPane();
			panelProducts = this.addProductsPanel();
			jScrollPaneProducts.setViewportView(panelProducts);

			jPanelDialogReaction.add(jScrollPaneReactants, new GridBagConstraints(1, 10, 7, 2, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDialogReaction.add(jScrollPaneProducts, new GridBagConstraints(1, 13, 7, 2, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}

		//enzymes pane
		{
			jScrollPaneEnzymes = new JScrollPane();
			jScrollPaneEnzymes.setViewportView(addEnzymes());
			jPanelDialogReaction.add(jScrollPaneEnzymes, new GridBagConstraints(3, 8, 5, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDialogReaction.add(jButtonAddEnzyme(), new GridBagConstraints(8, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		//ButtonAddReactant
		{
			jPanelDialogReaction.add(jButtonAddReactant(), new GridBagConstraints(8, 10, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		//ButtonAddProduct
		{
			jPanelDialogReaction.add(jButtonAddProduct(), new GridBagConstraints(8, 13, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		}
		//  Check Box In Model
		{

			GridBagLayout jPanelPropertiesLayout = new GridBagLayout();
			jPanelPropertiesLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelPropertiesLayout.rowHeights = new int[] {10, 5, 10, 5, 10, 5, 10};
			jPanelPropertiesLayout.columnWeights = new double[] {0.1, 0.1};
			jPanelPropertiesLayout.columnWidths = new int[] {20, 20};
			JPanel jPanelProperties = new JPanel(jPanelPropertiesLayout);
			jPanelProperties.setBorder(BorderFactory.createTitledBorder(null, "Properties", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
			jPanelDialogReaction.add(jPanelProperties, new GridBagConstraints(3, 1, 2, 6, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

			jCheckBoxInModel = new JCheckBox();
			jCheckBoxInModel.setText("In Model");
			jCheckBoxInModel.setToolTipText("In Model");
			jPanelProperties.add(jCheckBoxInModel, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jSpontaneous = new JCheckBox();
			jSpontaneous.setText("Spontaneous");
			jSpontaneous.setToolTipText("Spontaneous");
			jPanelProperties.add(jSpontaneous, new GridBagConstraints(2, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jNonEnzymatic = new JCheckBox();
			jNonEnzymatic.setText("Non Enzymatic");
			jNonEnzymatic.setToolTipText("Non Enzymatic");
			jPanelProperties.add(jNonEnzymatic, new GridBagConstraints(2, 4, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jIsGeneric = new JCheckBox();
			jIsGeneric.setText("Is Generic");
			jIsGeneric.setToolTipText("Is Generic");
			jPanelProperties.add(jIsGeneric, new GridBagConstraints(2, 6, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		}
		//reactionlocalisation
		{
			ComboBoxModel<String> jComboBoxLocalisationModel = new DefaultComboBoxModel<>(reactionsCompartmentsModel);
			jComboBoxLocalisation = new JComboBox<>();
			jComboBoxLocalisation.setModel(jComboBoxLocalisationModel);
			jPanelCompartmentReaction = new JPanel();
			GridBagLayout jPanelCompartmentReactionLayout = new GridBagLayout();
			jPanelCompartmentReaction.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Localization", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
			jPanelCompartmentReactionLayout.rowWeights = new double[] {0.0};
			jPanelCompartmentReactionLayout.rowHeights = new int[] {7};
			jPanelCompartmentReactionLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
			jPanelCompartmentReactionLayout.columnWidths = new int[] {7, 7, 7};
			jPanelCompartmentReaction.setLayout(jPanelCompartmentReactionLayout);
			jPanelCompartmentReaction.add(jComboBoxLocalisation, new GridBagConstraints(1, -1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDialogReaction.add(jPanelCompartmentReaction, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}

		{
			jScrollPaneGeneRule = new JScrollPane();
			jScrollPaneGeneRule.setViewportView(this.addGeneRules());
			jPanelDialogReaction.add(jScrollPaneGeneRule, new GridBagConstraints(6, 1, 2, 6, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelDialogReaction.add(jButtonAddGeneRule_OR(), new GridBagConstraints(8, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}

		this.setModal(true);
		pack();

	}

	/**
	 * @param row 
	 * @return
	 */
	private JButton jButtonAddGeneRule_AND(int row) {

		JButton jButtonAddGeneRule = new JButton();
		jButtonAddGeneRule.setIcon((new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Add.png")), 0.04).resizeImageIcon()));
		jButtonAddGeneRule.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				List<Integer> rule = geneRule_AND_OR.get(row);
				rule.add(0);
				geneRule_AND_OR.set(row,rule);
				try {
					jScrollPaneGeneRule.setViewportView(addGeneRules());
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		return jButtonAddGeneRule;
	}

	/**
	 * @return
	 */
	private JButton jButtonAddGeneRule_OR() {

		JButton jButtonAddGeneRule = new JButton();
		jButtonAddGeneRule.setIcon((new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Add.png")), 0.04).resizeImageIcon()));
		jButtonAddGeneRule.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				List<Integer> rule = new ArrayList<>();
				rule.add(0);
				geneRule_AND_OR.add(rule);
				try {
					jScrollPaneGeneRule.setViewportView(addGeneRules());
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		return jButtonAddGeneRule;
	}

	/**
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	private JPanel addGeneRules() throws Exception, NumberFormatException {

		JPanel jPanelGeneRule;
		jPanelGeneRule = new JPanel();
		GridBagLayout jPanelGeneRuleLayout = new GridBagLayout();
		jPanelGeneRuleLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
		jPanelGeneRuleLayout.columnWidths = new int[] {7, 7, 7, 7, 7};
		jPanelGeneRuleLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
		jPanelGeneRuleLayout.rowHeights = new int[] {7, 7, 7};

		jPanelGeneRule.setBorder(BorderFactory.createTitledBorder(null, "Gene Rule", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));

		int rowsSize = geneRule_AND_OR.size();
		int columnsSize = 0;

		for(List<Integer> list : this.geneRule_AND_OR)
			if(list.size()>columnsSize)
				columnsSize = list.size();

		if(rowsSize>0) {

			int rowsLength = rowsSize*3+rowsSize-1;
			jPanelGeneRuleLayout.rowWeights = new double[rowsLength];
			jPanelGeneRuleLayout.rowHeights  = new int[rowsLength];

			for(int rh=0; rh<rowsLength; rh++) {

				jPanelGeneRuleLayout.rowWeights[rh]=0.1;
				jPanelGeneRuleLayout.rowHeights[rh]=7;
			}
		}

		if(columnsSize>0) {

			int columnsLength = columnsSize*3+columnsSize+4;

			jPanelGeneRuleLayout.columnWeights = new double[columnsLength];
			jPanelGeneRuleLayout.columnWidths  = new int[columnsLength];

			for(int rh=0; rh<columnsLength; rh++) {

				jPanelGeneRuleLayout.columnWeights[rh]=0.1;
				jPanelGeneRuleLayout.columnWidths[rh]=7;
			}
		}

		jPanelGeneRule.setLayout(jPanelGeneRuleLayout);

		this.genesField = new ArrayList<>(rowsSize);	

		for(int row=0; row<rowsSize; row++) {

			List<JComboBox<String>> and = new ArrayList<>();
			this.genesField.add(row, and);
			int rowLocation = (row+1) *3 + row;

			if(geneRule_AND_OR.get(row).size()>0)
				jPanelGeneRule.add(new JLabel("("), new GridBagConstraints(1, (rowLocation-2), 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0)); // ver isto

			for(int column=0; column<columnsSize; column++) {

				if(geneRule_AND_OR.get(row).size()>column) {

					int columnlocation = (column+1)*3 + column+1;

					JComboBox<String> gene = new JComboBox<>(this.genesModel);
					Integer geneId = geneRule_AND_OR.get(row).get(column);
					GeneContainer geneContainer = ModelGenesServices.getGeneDataById(reactionsInterface.getWorkspace().getName(), geneId);
					if(geneContainer != null && geneContainer.getLocusTag() != null && !geneContainer.getLocusTag().isEmpty()) {
	
					String geneLocusTag = geneContainer.getLocusTag();
					String GeneLocusTagParsed = geneLocusTag+ " (" + geneLocusTag + ")";
					
					gene.setSelectedIndex(Arrays.asList(genesModel).indexOf(GeneLocusTagParsed));
					if(gene.getSelectedItem()!=null)
						gene.setToolTipText(gene.getSelectedItem().toString());
					final List<JComboBox<String>> geneRule = genesField.get(row);
				
					gene.addActionListener(new ActionListener() {

						@SuppressWarnings("unchecked")
						@Override
						public void actionPerformed(ActionEvent arg0) {

							comboBoxActionListener(geneRule,(JComboBox<String>) arg0.getSource());
							String selectedItem = ((JComboBox<String>) arg0.getSource()).getSelectedItem().toString();
							((JComponent) arg0.getSource()).setToolTipText(selectedItem);

							geneRule_AND_OR = new ArrayList<>();
							
							for(int g = 0; g<genesField.size(); g++) {

								List<JComboBox<String>> genes = genesField.get(g);
								List<Integer> genesList = new ArrayList<>();
								for(int j=0; j<genes.size();j++) {
									
									JComboBox<String> jcb = genes.get(j);
									
									String query = jcb.getSelectedItem().toString();
									
									if(query.contains("(")) {
										query = query.split("//s+")[0].trim();
									}
									Integer geneIdentifier = null;
									
									try {
										GeneContainer gene = ModelGenesServices.getGeneByQuery(reactionsInterface.getWorkspace().getName(), query);
										
										if(gene != null)		//muito martelado, arranjar solucao para isto
											geneIdentifier = gene.getIdGene();
										else {
											geneIdentifier = ModelGenesServices.getGeneIdByLocusTag(reactionsInterface.getWorkspace().getName(), query);
										}
									} 
									catch (Exception e) {
										e.printStackTrace();
									}
									
									genesList.add(j, geneIdentifier);
								}

								geneRule_AND_OR.add(g, genesList);
							}

						}
						
					});
					}

					jPanelGeneRule.add(gene, new GridBagConstraints((columnlocation-2), (rowLocation-2), 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

					if(column<geneRule_AND_OR.get(row).size()-1) {

						JLabel andField = new JLabel("AND");
						jPanelGeneRule.add(andField, new GridBagConstraints((columnlocation), (rowLocation-2), 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					}
					else {

						JLabel label = new JLabel(")");
						jPanelGeneRule.add(label, new GridBagConstraints((columnlocation), (rowLocation-2), 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jPanelGeneRule.add(this.jButtonAddGeneRule_AND(row), new GridBagConstraints((columnlocation+2), (rowLocation-2), 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					}

					and.add(gene);
				}
			}

			if(row<rowsSize-1) {

				JLabel orField = new JLabel("OR");
				jPanelGeneRule.add(orField, new GridBagConstraints(2, rowLocation, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			this.genesField.set(row,and);
		}

		return jPanelGeneRule;
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
	private JPanel addReactantsPanel() {

		JPanel panelReactants = new JPanel();
		GridBagLayout jPanelReactantsLayout = new GridBagLayout();

		//number of rows (array size) 
		if(reactants.length==0) {

			jPanelReactantsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelReactantsLayout.rowHeights = new int[] {7, 7, 7, 7};
		}
		else {

			jPanelReactantsLayout.rowWeights = new double[reactants.length*2+2];
			jPanelReactantsLayout.rowHeights  = new int[reactants.length*2+2];

			for(int rh=0; rh<reactants.length*2+2; rh++) {

				jPanelReactantsLayout.rowWeights[rh]=0.1;
				jPanelReactantsLayout.rowHeights[rh]=7;
			}
		}
		//number of columns (array size)
		jPanelReactantsLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0};
		jPanelReactantsLayout.columnWidths = new int[] {1, 7, 1, 7, 1, 7, 1, 7, 1};
		//set layout
		panelReactants.setLayout(jPanelReactantsLayout);
		panelReactants.setBorder(BorderFactory.createTitledBorder(null, "Reactants", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
		panelReactants.add(new JLabel("Metabolite"), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panelReactants.add(new JLabel("Formula"), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panelReactants.add(new JLabel("External identifier"), new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panelReactants.add(new JLabel("Stoichiometry"), new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		//		panelReactants.add(new JLabel("Chains number"), new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panelReactants.add(new JLabel("Localization"), new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panelReactants.add(this.addReactantCompartmentButton(), new GridBagConstraints(8, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		if(reactants.length==0) {

			reactants = new String[1];
			reactants[0]= "";
			reactantsField = new ArrayList<>(1);
			reactantsExternalIdentifiersField = new ArrayList<>(1);

			//			JComboBox<String> rbox = reactantsField.get(0);
			//			JComboBox<String> rCompartmentsBox = reactantsCompartmentsBox.get(0);

			List<String> metaboliteNames = new ArrayList<String>();
			List<String> metaboliteKeggId = new ArrayList<String>();
			metaboliteNames.add("");
			metaboliteKeggId.add("");

			for (String listedMetabolite : mapMetabolites.keySet()) {

					metaboliteKeggId.add(listedMetabolite);

					if(mapMetabolites.get(listedMetabolite) != null && mapMetabolites.get(listedMetabolite).get(0)!=null)						
						metaboliteNames.add(mapMetabolites.get(listedMetabolite).get(0));
					else
						metaboliteNames.add(listedMetabolite);
			}

			java.util.Collections.sort(metaboliteNames);
			java.util.Collections.sort(metaboliteKeggId);

			String[] arrayKegg = new String[metaboliteKeggId.size()];
			String[] array = new String[metaboliteNames.size()];
			JLabel rFormula = new JLabel();
			JComboBox<String> rId = new JComboBox<String>(metaboliteKeggId.toArray(arrayKegg));
			JComboBox<String> rbox = new JComboBox<String>(metaboliteNames.toArray(array));

			rbox.setPreferredSize(new Dimension(300, 26));
			rbox.setSelectedIndex(0);
			if(rbox.getSelectedItem()!=null)
				rbox.setToolTipText(rbox.getSelectedItem().toString());
			rbox.addActionListener(new ActionListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void actionPerformed(ActionEvent arg0) {

					comboBoxActionListener(reactantsField,(JComboBox<String>) arg0.getSource());

					if(((JComboBox<String>) arg0.getSource()).getSelectedItem() != null) {
						((JComponent) arg0.getSource()).setToolTipText(((JComboBox<String>) arg0.getSource()).getSelectedItem().toString());

						String element = ((JComboBox<String>) arg0.getSource()).getSelectedItem().toString();
						
						
						updateFieldsOnActionPerformedMetabolites(element, rId, rFormula);

					}
				}
			});

			rId.setSelectedIndex(0);
			if(rId.getSelectedItem()!=null)
				rId.setToolTipText(rbox.getSelectedItem().toString());
			rId.addActionListener(new ActionListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void actionPerformed(ActionEvent arg0) {

					comboBoxActionListener(reactantsField,(JComboBox<String>) arg0.getSource());

					if(((JComboBox<String>) arg0.getSource()).getSelectedItem() != null) {
						((JComponent) arg0.getSource()).setToolTipText(((JComboBox<String>) arg0.getSource()).getSelectedItem().toString());

						String element = ((JComboBox<String>) arg0.getSource()).getSelectedItem().toString();

						updateFieldsOnActionPerformedKeggId(element, rbox, rFormula);

					}
				}
			});


			reactantsStoichiometry = new String[1];
			reactantsStoichiometry[0] = "-1";
			reactantsStoichiometryField = new JTextField[1];
			reactantsStoichiometryField[0] = new JTextField();
			reactantsStoichiometryField[0].setText(reactantsStoichiometry[0]);

			//			reactantsChainsField = new JTextField[1];
			//			reactantsChains = new String[1];
			//			reactantsChains[0]="1";
			//			reactantsChainsField[0]= new JTextField();
			//			reactantsChainsField[0].setText(reactantsChains[0]);

			reactantsCompartments=new String[1];
			reactantsCompartments[0] = this.defaultCompartment;

			reactantsCompartmentsBox = new ArrayList<>(1);


			JComboBox<String> rCompartmentsBox = new JComboBox<String>();

			if(metabolitesCompartmentsModel!=null && metabolitesCompartmentsModel.length>0) {

				rCompartmentsBox = new JComboBox<String>(metabolitesCompartmentsModel); 
				rCompartmentsBox.setSelectedIndex(0);
			}
			
			panelReactants.add(rbox, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			panelReactants.add(rFormula, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			panelReactants.add(rId, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			panelReactants.add(reactantsStoichiometryField[0], new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			//			panelReactants.add(reactantsChainsField[0], new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			panelReactants.add(rCompartmentsBox, new GridBagConstraints(8, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

			reactantsField.add(0,rbox);
			reactantsCompartmentsBox.add(0,rCompartmentsBox);
			reactantsExternalIdentifiersField.add(0, rId);
		}
		else {

			int size = 1;
			if(reactants.length>1)
				size = reactants.length;

			reactantsField = new ArrayList<>(size);
			reactantsStoichiometryField = new JTextField[size];
			reactantsCompartmentsBox = new ArrayList<>(size);
			reactantsExternalIdentifiersField = new ArrayList<>(size);

			for(int s=0; s<size; s++) {

				List<String> metaboliteNames = new ArrayList<String>();
				List<String> metaboliteKeggId = new ArrayList<String>();
				metaboliteNames.add("");
				metaboliteKeggId.add("");

				for (Map.Entry<String,List<String>> metaboliteName : mapMetabolites.entrySet()) {

					if(metaboliteName != null) {

						try {

							if(metaboliteName.getValue() != null && metaboliteName.getValue().get(0) !=null && metaboliteName.getValue().get(0) !="") {

								metaboliteKeggId.add(metaboliteName.getKey());
								metaboliteNames.add(metaboliteName.getValue().get(0));
							}
							else {

								metaboliteNames.add(metaboliteName.getKey());
								metaboliteKeggId.add(metaboliteName.getKey());
							}
						}
						catch(IndexOutOfBoundsException ex) {

							metaboliteNames.add(metaboliteName.getKey());
							metaboliteKeggId.add(metaboliteName.getKey());
						}
					}
				}

				java.util.Collections.sort(metaboliteNames);
				java.util.Collections.sort(metaboliteKeggId);

				String[] arrayKegg = new String[metaboliteKeggId.size()];
				String[] array = new String[metaboliteNames.size()];
				JLabel rFormula = new JLabel();
				JComboBox<String> rId = new JComboBox<String>(metaboliteKeggId.toArray(arrayKegg));
				JComboBox<String> rbox = new JComboBox<String>(metaboliteNames.toArray(array));

				rbox.setPreferredSize(new Dimension(300, 26));

				int selectedElementIdentifier = -1;
				
				if(reactants[s]!= null && !reactants[s].isEmpty() &&Integer.valueOf(reactants[s].toString())>0 )	
					selectedElementIdentifier = metabolitesModel.get(Integer.valueOf(reactants[s].toString())).getMetaboliteID();

				if(selectedElementIdentifier>0) {

					MetaboliteContainer selectedMetabolite = metabolitesModel.get(selectedElementIdentifier);

					if(selectedMetabolite != null) {

						if(reactants[s]==null)
							rbox.setSelectedIndex(0);
						else
							rbox.setSelectedIndex(metaboliteNames.indexOf(selectedMetabolite.getName()));

						if(selectedMetabolite.getFormula()!=null)							
							rFormula.setText(selectedMetabolite.getFormula());
						else
							rFormula.setText("-----------");

						if(selectedMetabolite.getExternalIdentifier()!=null)	
							rId.setSelectedItem(selectedMetabolite.getExternalIdentifier());
					}
					else {

						rbox.setSelectedIndex(0);
					}
				}
				else {

					rId.setSelectedIndex(0);
					rbox.setSelectedIndex(0);
					rFormula.setText("");
				}

				if(rbox.getSelectedItem()!=null)
					rbox.setToolTipText(rbox.getSelectedItem().toString());

				rbox.addActionListener(new ActionListener() {

					@SuppressWarnings("unchecked")
					@Override
					public void actionPerformed(ActionEvent arg0) {

						comboBoxActionListener(reactantsField,(JComboBox<String>) arg0.getSource());

						if(((JComboBox<String>) arg0.getSource()).getSelectedItem() != null) {
							((JComponent) arg0.getSource()).setToolTipText(((JComboBox<String>) arg0.getSource()).getSelectedItem().toString());

							String element = ((JComboBox<String>) arg0.getSource()).getSelectedItem().toString();

							updateFieldsOnActionPerformedMetabolites(element, rId, rFormula);
						}
					}
				});


				if(rId.getSelectedItem()!=null)
					rId.setToolTipText(rbox.getSelectedItem().toString());

				rId.addActionListener(new ActionListener() {

					@SuppressWarnings("unchecked")
					@Override
					public void actionPerformed(ActionEvent arg0) {

						comboBoxActionListener(reactantsField,(JComboBox<String>) arg0.getSource());

						if(((JComboBox<String>) arg0.getSource()).getSelectedItem() != null) {
							((JComponent) arg0.getSource()).setToolTipText(((JComboBox<String>) arg0.getSource()).getSelectedItem().toString());

							String element = ((JComboBox<String>) arg0.getSource()).getSelectedItem().toString();

							updateFieldsOnActionPerformedKeggId(element, rbox, rFormula);

						}
					}
				});


				reactantsStoichiometryField[s] = new JTextField();
				//				reactantsChainsField[s] = new JTextField();;
				if(reactantsStoichiometry.length<=s) {

					reactantsStoichiometry[s]="-1";
//					reactantsChains[s]="1";
				}
				reactantsStoichiometryField[s].setText(reactantsStoichiometry[s]);
				//				reactantsChainsField[s].setText(reactantsChains[s]);
				JComboBox<String> rCompartmentsBox = new JComboBox<String>(metabolitesCompartmentsModel);
				if(reactantsCompartments[s]==null)
					rCompartmentsBox.setSelectedIndex(0);
				else
					rCompartmentsBox.setSelectedItem(reactantsCompartments[s]);

				int r =s*2+2;
				panelReactants.add(rbox, new GridBagConstraints(1, r, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				panelReactants.add(rFormula, new GridBagConstraints(3, r, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				panelReactants.add(rId, new GridBagConstraints(4, r, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				panelReactants.add(reactantsStoichiometryField[s], new GridBagConstraints(6, r, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				panelReactants.add(rCompartmentsBox, new GridBagConstraints(8, r, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

				reactantsField.add(s,rbox);
				reactantsCompartmentsBox.add(s,rCompartmentsBox);
				reactantsExternalIdentifiersField.add(s, rId);
			}
		}

		return panelReactants;
	}

	/**
	 * @return products panel
	 */
	private JPanel addProductsPanel() {

		JPanel panelProducts = new JPanel();
		GridBagLayout jPanelProductsLayout = new GridBagLayout();

		//number of rows (array size)
		if(products.length==0) {

			jPanelProductsLayout.rowWeights = new double[] {0.1, 0.0, 0.1};
			jPanelProductsLayout.rowHeights = new int[] {7, 7, 7};
		}
		else {

			jPanelProductsLayout.rowWeights = new double[products.length*2+1];
			jPanelProductsLayout.rowHeights  = new int[products.length*2+1];

			for(int rh=0; rh<products.length*2+1; rh++) {

				jPanelProductsLayout.rowWeights[rh]=0.1;
				jPanelProductsLayout.rowHeights[rh]=7;
			}
		}

		//number of columns (array size)
		jPanelProductsLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0};
		jPanelProductsLayout.columnWidths = new int[] {1, 7, 1, 7, 1, 7, 1, 7, 1};
		//set layout
		panelProducts.setLayout(jPanelProductsLayout);
		panelProducts.setBorder(BorderFactory.createTitledBorder(null, "Products", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));
		panelProducts.add(new JLabel("Metabolite"), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panelProducts.add(new JLabel("Formula"), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panelProducts.add(new JLabel("External identifier"), new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panelProducts.add(new JLabel("Stoichiometry"), new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		//		panelProducts.add(new JLabel("Chains number"), new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panelProducts.add(new JLabel("Localization"), new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panelProducts.add(this.addProductCompartmentButton(), new GridBagConstraints(8, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		if(products.length==0) {

			products = new String[1];
			products[0]= "";
			productsField = new ArrayList<>(1);
			productsStoichiometryField = new JTextField[1];
			//			productsChainsField = new JTextField[1];
			productsCompartmentsBox = new ArrayList<>(1);
			productsExternalIdentifiersField = new ArrayList<>(1);

			List<String> metaboliteNames = new ArrayList<String>();
			List<String> metaboliteKeggId = new ArrayList<String>();
			metaboliteNames.add("");
			metaboliteKeggId.add("");

			for (Map.Entry<String,List<String>> metaboliteName : mapMetabolites.entrySet()) {
				if(metaboliteName != null) {

					try {
						if(metaboliteName.getValue() != null && metaboliteName.getValue().get(0) !=null && metaboliteName.getValue().get(0) !="") {
							metaboliteKeggId.add(metaboliteName.getKey());
							metaboliteNames.add(metaboliteName.getValue().get(0));

						}
						else {
							metaboliteNames.add(metaboliteName.getKey());
							metaboliteKeggId.add(metaboliteName.getKey());
						}

					}
					catch(IndexOutOfBoundsException ex) {
						metaboliteNames.add(metaboliteName.getKey());
						metaboliteKeggId.add(metaboliteName.getKey());
					}
				}

			}

			java.util.Collections.sort(metaboliteNames);
			java.util.Collections.sort(metaboliteKeggId);

			String[] arrayKegg = new String[metaboliteKeggId.size()];
			String[] array = new String[metaboliteNames.size()];
			JLabel pFormula = new JLabel();
			JComboBox<String> pId = new JComboBox<String>(metaboliteKeggId.toArray(arrayKegg));
			JComboBox<String> pBox = new JComboBox<String>(metaboliteNames.toArray(array));

			pBox.setPreferredSize(new Dimension(300, 26));
			pBox.setSelectedIndex(0);


			if(pBox.getSelectedItem()!=null)
				pBox.setToolTipText(pBox.getSelectedItem().toString());
			pBox.addActionListener(new ActionListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void actionPerformed(ActionEvent arg0) {

					comboBoxActionListener(productsField,(JComboBox<String>) arg0.getSource());



					if(((JComboBox<String>) arg0.getSource()).getSelectedItem() != null) {
						((JComponent) arg0.getSource()).setToolTipText(((JComboBox<String>) arg0.getSource()).getSelectedItem().toString());

						String element = ((JComboBox<String>) arg0.getSource()).getSelectedItem().toString();

						updateFieldsOnActionPerformedMetabolites(element, pId, pFormula);

					}

				}});

			pId.setSelectedIndex(0);
			if(pId.getSelectedItem()!=null)
				pId.setToolTipText(pBox.getSelectedItem().toString());
			pId.addActionListener(new ActionListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void actionPerformed(ActionEvent arg0) {

					comboBoxActionListener(productsField,(JComboBox<String>) arg0.getSource());

					if(((JComboBox<String>) arg0.getSource()).getSelectedItem() != null) {
						((JComponent) arg0.getSource()).setToolTipText(((JComboBox<String>) arg0.getSource()).getSelectedItem().toString());

						String element = ((JComboBox<String>) arg0.getSource()).getSelectedItem().toString();

						updateFieldsOnActionPerformedKeggId(element, pBox, pFormula);

					}
				}
			});

			productsStoichiometry = new String[1];
			productsStoichiometry[0]= "1";
			productsStoichiometryField = new JTextField[1];
			productsStoichiometryField[0] = new JTextField();
			productsStoichiometryField[0].setText("1");

//			productsChains = new String[1];
//			productsChains[0]="1";
			//			productsChainsField = new JTextField[1];
			//			productsChainsField[0] = new JTextField();
			//			productsChainsField[0].setText("1");

			productsCompartments=new String[1];
			productsCompartments[0]=this.defaultCompartment;
			JComboBox<String> pCompartmentsBox = new JComboBox<String>();
			if(metabolitesCompartmentsModel!=null && metabolitesCompartmentsModel.length>0) {

				pCompartmentsBox = new JComboBox<String>(metabolitesCompartmentsModel); 
				pCompartmentsBox.setSelectedIndex(0);
			}


			panelProducts.add(pBox, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			panelProducts.add(productsStoichiometryField[0], new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			//			panelProducts.add(productsChainsField[0], new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			panelProducts.add(pCompartmentsBox, new GridBagConstraints(8, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			panelProducts.add(pFormula, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			panelProducts.add(pId, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

			productsField.add(0,pBox);
			productsCompartmentsBox.add(0,pCompartmentsBox);
			productsExternalIdentifiersField.add(pId);

		}
		else {

			int size = 1;
			if(products.length>1)
				size = products.length;

			productsField = new ArrayList<>(size);
			productsStoichiometryField = new JTextField[size];
			//			productsChainsField = new JTextField[size];
			productsCompartmentsBox = new ArrayList<>(size);
			productsExternalIdentifiersField = new ArrayList<>(size);

			for(int s=0; s<size; s++) {

				//				JComboBox<String> pBox = productsField.get(s);
				//				JComboBox<String> pCompartmentsBox = productsCompartmentsBox.get(s);

				List<String> metaboliteNames = new ArrayList<String>();
				List<String> metaboliteKeggId = new ArrayList<String>();
				metaboliteNames.add("");
				metaboliteKeggId.add("");

				for (Map.Entry<String,List<String>> metaboliteName : mapMetabolites.entrySet()) {
					if(metaboliteName != null) {

						try {
							if(metaboliteName.getValue() != null && metaboliteName.getValue().get(0) !=null && metaboliteName.getValue().get(0) !="") {
								metaboliteKeggId.add(metaboliteName.getKey());
								metaboliteNames.add(metaboliteName.getValue().get(0));

							}
							else {
								metaboliteNames.add(metaboliteName.getKey());
								metaboliteKeggId.add(metaboliteName.getKey());
							}

						}
						catch(IndexOutOfBoundsException ex) {
							metaboliteNames.add(metaboliteName.getKey());
							metaboliteKeggId.add(metaboliteName.getKey());
						}
					}

				}
				
				java.util.Collections.sort(metaboliteNames);
				java.util.Collections.sort(metaboliteKeggId);

				String[] arrayKegg = new String[metaboliteKeggId.size()];
				String[] array = new String[metaboliteNames.size()];
				JLabel pFormula = new JLabel();
				JComboBox<String> pId = new JComboBox<String>(metaboliteKeggId.toArray(arrayKegg));
				JComboBox<String> pBox = new JComboBox<String>(metaboliteNames.toArray(array));

				pBox.setPreferredSize(new Dimension(300, 26));

				int selectedElementIdentifier = -1;
				
				if(products[s]!= null && !products[s].isEmpty() &&Integer.valueOf(products[s].toString())>0)
					selectedElementIdentifier = metabolitesModel.get(Integer.valueOf(products[s].toString())).getMetaboliteID();

				if(selectedElementIdentifier>0) {

					MetaboliteContainer selectedMetabolite = metabolitesModel.get(selectedElementIdentifier);

					if(selectedMetabolite != null) {

						if(products[s]==null)
							pBox.setSelectedIndex(0);
						else
							pBox.setSelectedIndex(metaboliteNames.indexOf(selectedMetabolite.getName()));

						if(selectedMetabolite.getFormula()!=null)							
							pFormula.setText(selectedMetabolite.getFormula());
						else
							pFormula.setText("-----------");

						if(selectedMetabolite.getExternalIdentifier()!=null)
							pId.setSelectedItem(selectedMetabolite.getExternalIdentifier());

					}
				}
				else {
					pId.setSelectedIndex(0);
					pBox.setSelectedIndex(0);
					pFormula.setText("");
				}


				if(pBox.getSelectedItem()!=null)
					pBox.setToolTipText(pBox.getSelectedItem().toString());
				pBox.addActionListener(new ActionListener() {

					@SuppressWarnings("unchecked")
					@Override
					public void actionPerformed(ActionEvent arg0) {

						JComboBox<String> jCombo = (JComboBox<String>) arg0.getSource();

						comboBoxActionListener(productsField, jCombo);

						if(((JComboBox<String>) arg0.getSource()).getSelectedItem() != null) 
							((JComponent) arg0.getSource()).setToolTipText(jCombo.getSelectedItem().toString());

						String element = ((JComboBox<String>) arg0.getSource()).getSelectedItem().toString();

						updateFieldsOnActionPerformedMetabolites(element, pId, pFormula);

					}

				});


				if(pId.getSelectedItem()!=null)
					pId.setToolTipText(pBox.getSelectedItem().toString());
				pId.addActionListener(new ActionListener() {

					@SuppressWarnings("unchecked")
					@Override
					public void actionPerformed(ActionEvent arg0) {

						comboBoxActionListener(productsField,(JComboBox<String>) arg0.getSource());

						if(((JComboBox<String>) arg0.getSource()).getSelectedItem() != null) {
							((JComponent) arg0.getSource()).setToolTipText(((JComboBox<String>) arg0.getSource()).getSelectedItem().toString());

							String element = ((JComboBox<String>) arg0.getSource()).getSelectedItem().toString();

							updateFieldsOnActionPerformedKeggId(element, pBox, pFormula);

						}
					}
				});


				productsStoichiometryField[s] = new JTextField();
				if(productsStoichiometry.length<=s) {

					productsStoichiometry[s]="1";
//					productsChains[s]="1";
				}
				productsStoichiometryField[s].setText(productsStoichiometry[s]);
				//				productsChainsField[s].setText(productsChains[s]);
				JComboBox<String> pCompartmentsBox = new JComboBox<String>(metabolitesCompartmentsModel);

				if(productsCompartments[s]==null)
					pCompartmentsBox.setSelectedIndex(0);
				else
					pCompartmentsBox.setSelectedItem(productsCompartments[s]);

				int r =s*2+2;
				panelProducts.add(pBox, new GridBagConstraints(1, r, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				panelProducts.add(productsStoichiometryField[s], new GridBagConstraints(6, r, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				panelProducts.add(pCompartmentsBox, new GridBagConstraints(8, r, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				panelProducts.add(pFormula, new GridBagConstraints(3, r, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				panelProducts.add(pId, new GridBagConstraints(4, r, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));


				productsField.add(s,pBox);
				productsCompartmentsBox.add(s,pCompartmentsBox);
				productsExternalIdentifiersField.add(s, pId);
			}
		}

		return panelProducts;
	}

	/**
	 * @param pathway
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 */
	private JPanel addEnzymes() throws IOException, Exception {

		JPanel jPanelEnzyme;
		jPanelEnzyme = new JPanel();
		GridBagLayout jPanelEnzymeLayout = new GridBagLayout();
		boolean noEnzyme=false;

		String[] enzymes = new String[0];

		if(this.selectedEnzymesAndPathway.containsKey(selectedPathway)) {

			enzymes = new String[this.selectedEnzymesAndPathway.get(selectedPathway).size()];

			int i = 0;
			for(String enzyme : this.selectedEnzymesAndPathway.get(selectedPathway)) {
				enzymes[i] = enzyme;
				i++;
			}
		}

		if(enzymes.length==0) {

			jPanelEnzymeLayout.rowWeights = new double[] {0.1, 0.0, 0.1};
			jPanelEnzymeLayout.rowHeights = new int[] {7, 7, 7};
			enzymes=new String[1];
			noEnzyme=true;
		}
		else {

			jPanelEnzymeLayout.rowWeights = new double[enzymes.length*2+1];
			jPanelEnzymeLayout.rowHeights  = new int[enzymes.length*2+1];

			for(int rh=0; rh<enzymes.length*2+1; rh++) {

				jPanelEnzymeLayout.rowWeights[rh]=0.1;
				jPanelEnzymeLayout.rowHeights[rh]=7;
			}
		}
		jPanelEnzymeLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
		jPanelEnzymeLayout.columnWidths = new int[] {7, 7, 7};
		jPanelEnzyme.setLayout(jPanelEnzymeLayout);

		jPanelEnzyme.setBorder(BorderFactory.createTitledBorder(null, "enzymes in "+selectedPathway, TitledBorder.LEADING, TitledBorder.ABOVE_TOP));

		if(selectedPathway.equals("-1allpathwaysinreaction"))
			jPanelEnzyme.setBorder(BorderFactory.createTitledBorder(null, "enzymes", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));

		if(noEnzyme) {

			enzymesField = new ArrayList<>(1);
			//JComboBox<String> eBox = enzymesField.get(0);
			JComboBox<String> eBox = new JComboBox<>(enzymesModel);

			if(enzymes[0]!=null && !enzymes[0].equals("")) {

				eBox.setSelectedItem(enzymes[0]);
				eBox.setToolTipText(eBox.getSelectedItem().toString());
				
			}

			jPanelEnzyme.add(eBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			Set<String> enzymesSet=new TreeSet<String>();

			if(!eBox.getSelectedItem().toString().equals(""))
				enzymesSet.add(eBox.getSelectedItem().toString());

			if(selectedEnzymesAndPathway.size()>0)
				selectedEnzymesAndPathway.put(selectedPathway, enzymesSet);

			eBox.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent arg0) {

					enzymesAction(arg0);
				}
			});

			enzymesField.add(0,eBox);
		}
		else {

			Set<String> enzymesSet=new TreeSet<String>();
			enzymesField = new ArrayList<>(enzymes.length);

			for(int e=0; e<enzymes.length;e++) {

				int r =e*2+1;

				//JComboBox<String> eBox = enzymesField.get(e);
				JComboBox<String> eBox = new JComboBox<>(enzymesModel);

				if(enzymes[e]!=null && !enzymes[e].equals("")) {
					
					String supposedProteinName = enzymes[e].split("___")[1];
					Integer isProteinNameinDB = ModelProteinsServices.getProteinIDFromName(reactionsInterface.getWorkspace().getName(), supposedProteinName);
					
					// if the protein has already been formatted (AKA manually inserted)
					if(isProteinNameinDB == null) {
						String enzymeWellFormated = enzymes[e].split("___")[0].concat("___").concat(enzymes[e].split("___")[2]).concat("___").concat(enzymes[e].split("___")[1]);
						eBox.setSelectedItem(enzymeWellFormated);
						eBox.setToolTipText(eBox.getSelectedItem().toString());
					}
					else {
						System.out.println(enzymes[e]);
						eBox.setSelectedItem(enzymes[e]);
						System.out.println(eBox.getSelectedItem());
						eBox.setToolTipText(eBox.getSelectedItem().toString());
					}
				}

				jPanelEnzyme.add(eBox, new GridBagConstraints(1, r, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

				if(!eBox.getSelectedItem().toString().equals(""))
					enzymesSet.add(eBox.getSelectedItem().toString());

				if(selectedEnzymesAndPathway.size()>0)
					selectedEnzymesAndPathway.put(selectedPathway, enzymesSet);

				eBox.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent arg0) {

						enzymesAction(arg0);

					}
				});

				enzymesField.add(e,eBox);
			}
		}
		return jPanelEnzyme;
	}

	/**
	 * @return
	 */
	private JPanel addPathways() {

		JPanel jPanelPathway;
		GridBagLayout jPanelPathwayLayout = new GridBagLayout();
		boolean noPathway=false;
		String[] pathways = new String[this.selectedEnzymesAndPathway.size()-1];
		int i = 0;
		jPanelPathwayLayout.rowWeights = new double[] {0.1, 0.0, 0.1};
		jPanelPathwayLayout.rowHeights = new int[] {7, 7, 7};
		jPanelPathwayLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
		jPanelPathwayLayout.columnWidths = new int[] {7, 7, 7};

		for(String pathway : this.selectedEnzymesAndPathway.keySet()) {

			if(pathway.equals("-1allpathwaysinreaction")) {

				//pathways[0] = pathway;
			}
			else {

				pathways[i] = pathway;
				i++;
			}
		}

		if(pathways.length == 0) {


			pathways=new String[1];
			noPathway=true;
		}
		else {

			jPanelPathwayLayout.rowWeights = new double[pathways.length*2+1];
			jPanelPathwayLayout.rowHeights  = new int[pathways.length*2+1];

			for(int rh = 0; rh < pathways.length *2+1; rh++) {

				jPanelPathwayLayout.rowWeights[rh]=0.1;
				jPanelPathwayLayout.rowHeights[rh]=7;
			}
		}

		jPanelPathway= new JPanel();
		jPanelPathway.setLayout(jPanelPathwayLayout);
		jPanelPathway.setBorder(BorderFactory.createTitledBorder(null, "Pathways", TitledBorder.LEADING, TitledBorder.ABOVE_TOP));

		if(noPathway) {

			pathwaysField = new ArrayList<>(1);
			// JComboBox<String> pBox = pathwaysField.get(0);
			JComboBox<String> pBox = new JComboBox<String>(pathwaysModel);

			if(pathways[0]!= null && !pathways[0].equals("")) {

				pBox.setSelectedItem(pathways[0]);
				pBox.setToolTipText(pBox.getSelectedItem().toString());
			}

			pBox.setPreferredSize(new Dimension(28, 26));
			jPanelPathway.add(pBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			pBox.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {}
				@Override
				public void mousePressed(MouseEvent e) {}
				@Override
				public void mouseExited(MouseEvent e) {}
				@Override
				public void mouseEntered(MouseEvent e) {}
				@SuppressWarnings("unchecked")
				@Override
				public void mouseClicked(MouseEvent e) {

					pathwaysClick(((JComboBox<String>) e.getSource()).getSelectedItem().toString());
				}
			});
			pBox.addItemListener(new ItemListener() {
				@SuppressWarnings("unchecked")
				@Override
				public void itemStateChanged(ItemEvent arg0) {

					List<String> paths = new ArrayList<String>();
					paths.add("-1allpathwaysinreaction");

					for(JComboBox<String> jPathway : pathwaysField) {

						String pathway = jPathway.getSelectedItem().toString();

						if(!pathway.equals(""))
							paths.add(pathway);
					}

					pathwaysAction(paths, ((JComboBox<String>) arg0.getSource()).getSelectedItem().toString());
				}
			});
			pathwaysField.add(0, pBox);
		}
		else {

			pathwaysField = new ArrayList<>(pathways.length);

			for(int p = 0 ; p < pathways.length ; p++) {

				//JComboBox<String> pBox = pathwaysField.get(p);
				JComboBox<String> pBox = new JComboBox<String>(pathwaysModel);

				if(pathways[p]!= null && !pathways[p].equals("")) {

					pBox.setSelectedItem(pathways[p]);
					pBox.setToolTipText(pBox.getSelectedItem().toString());
				}
				pBox.addMouseListener(new MouseListener() {
					@Override
					public void mouseReleased(MouseEvent e) {}
					@Override
					public void mousePressed(MouseEvent e) {}
					@Override
					public void mouseExited(MouseEvent e) {}
					@Override
					public void mouseEntered(MouseEvent e) {}
					@SuppressWarnings("unchecked")
					@Override
					public void mouseClicked(MouseEvent e) {

						pathwaysClick(((JComboBox<String>) e.getSource()).getSelectedItem().toString());
					}
				});
				pBox.addItemListener(new ItemListener() {
					@SuppressWarnings("unchecked")
					@Override
					public void itemStateChanged(ItemEvent arg0) {


						List<String> paths = new ArrayList<String>();
						//paths.add("-1allpathwaysinreaction");

						for(JComboBox<String> jPathway : pathwaysField) {

							String pathway = jPathway.getSelectedItem().toString();

							if(!pathway.equals(""))
								paths.add(pathway);
						}

						pathwaysAction(paths, ((JComboBox<String>) arg0.getSource()).getSelectedItem().toString());
					}
				});

				pBox.setPreferredSize(new Dimension(28, 26));
				int r =p*2+1;
				jPanelPathway.add(pBox, new GridBagConstraints(1, r, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

				pathwaysField.add(p, pBox);
			}
		}
		jPanelPathway.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {

				pathwaysPanelClick();
			}
		});
		return jPanelPathway;
	}

	/**
	 * @return
	 */
	private Component addReactantCompartmentButton() {

		JButton add = new JButton();
		add.setIcon((new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Add.png")), 0.03).resizeImageIcon()));
		add.setText("Compartment");
		add.setToolTipText("add Compartment");
		add.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new InsertCompartment(reactionsInterface)
				{
					private static final long serialVersionUID = 1L;
					public void finish() {
						this.setVisible(false);
						this.dispose();
						try {
							metabolitesCompartmentsModel = reactionsInterface.getCompartments(true, isCompartmentalisedModel);
						} catch (Exception e) {
							e.printStackTrace();
						}
						jScrollPaneReactants.setViewportView(addReactantsPanel());
						jScrollPaneProducts.setViewportView(addProductsPanel());
						System.gc();
					}						
				};

			}});
		return add;
	}

	/**
	 * @return
	 */
	private Component addProductCompartmentButton() {


		JButton add = new JButton();
		add.setIcon((new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Add.png")), 0.03).resizeImageIcon()));
		add.setText("Compartment");
		add.setToolTipText("add Compartment");
		add.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new InsertCompartment(reactionsInterface)
				{
					private static final long serialVersionUID = 1L;
					public void finish() {
						this.setVisible(false);
						this.dispose();
						try {
							metabolitesCompartmentsModel = reactionsInterface.getCompartments(true, isCompartmentalisedModel);
						} catch (Exception e) {
							e.printStackTrace();
						}
						jScrollPaneProducts.setViewportView(addProductsPanel());
						jScrollPaneReactants.setViewportView(addReactantsPanel());
						System.gc();
					}						
				};

			}});
		return add;
	}

	/**
	 * Save data 
	 * @throws Exception 
	 * @throws NumberFormatException 


	 */
	private void saveData() throws NumberFormatException, Exception {

		Map<String, Double> metabolites = new TreeMap<>();
		Map<String, String > compartments = new TreeMap<String, String >();
		boolean go = true;

		for(int i=0; i< reactantsExternalIdentifiersField.size(); i++) {

			String reactantIdentifier = reactantsExternalIdentifiersField.get(i).getSelectedItem().toString();
	
			String reactantIndex = mapMetabolitesIndex.get(reactantIdentifier);
			int selectedIndex = -1;
			
			if (reactantIndex != null)
				selectedIndex = Integer.parseInt(reactantIndex);

			if(selectedIndex>0) {

				String signal = "-";

				if(jBackword.isSelected())
					signal = "";

				reactants[i]= signal+reactantIndex;
				String stoich = reactantsStoichiometryField[i].getText();

				if(stoich.startsWith("-")) {

					if(signal.isEmpty())
						stoich=stoich.substring(1);
				}
				else {

					if(signal.equalsIgnoreCase("-"))
						stoich=signal+stoich;
				}

				metabolites.put(reactants[i].trim(), Double.valueOf(stoich));
				//				chains.put(reactants[i].trim(), reactantsChainsField[i].getText().trim());
				compartments.put(reactants[i].trim(), reactantsCompartmentsBox.get(i).getSelectedItem().toString().trim());

				if (stoich.length()>18) {

					BigDecimal bigDecimal = new BigDecimal(stoich);
					bigDecimal = bigDecimal.setScale(18, RoundingMode.HALF_UP);

					//					go = false;
					//					Workbench.getInstance().error("Cannot add reaction. Stoichiometry value ("+stoich+") for "+reactantsField.get(i).getSelectedItem()+" to large.");
					//					i = reactantsField.size();

					stoich = bigDecimal.doubleValue()+"";
				}
			}
		}

		if(go)
			for(int i=0; i< productsExternalIdentifiersField.size(); i++) {

				String productIdentifier = productsExternalIdentifiersField.get(i).getSelectedItem().toString();
				String productIndex = mapMetabolitesIndex.get(productIdentifier);
				
				int selectedIndex = -1;
				if (productIndex != null)
					selectedIndex = Integer.parseInt(productIndex);

				if(selectedIndex>0) {

					String signal = "";

					if(jBackword.isSelected())
						signal = "-";

					products[i]= signal+productIndex;
					String stoich = productsStoichiometryField[i].getText();

					if(stoich.startsWith("-")) {

						if(signal.isEmpty())
							stoich=stoich.substring(1);
					}
					else {

						if(signal.equalsIgnoreCase("-"))
							stoich=signal+stoich;
					}

					metabolites.put(products[i].trim(), Double.valueOf(stoich.trim()));
					compartments.put(products[i].trim(), productsCompartmentsBox.get(i).getSelectedItem().toString().trim());

					if (stoich.length()>18) {

						BigDecimal bigDecimal = new BigDecimal(stoich);
						bigDecimal = bigDecimal.setScale(18, RoundingMode.HALF_UP);
						stoich = bigDecimal.doubleValue()+"";
					}
				}
			}

		String boolean_rule = null;
		{
			if(this.genesField.size()>0)
				boolean_rule="";

			Map<String, Integer> geneModelIDs = reactionsInterface.getGenesModelID();

			for(int l=0;l <this.genesField.size();l++) {

				List<JComboBox<String>> genesAnd = this.genesField.get(l);

				String boolean_rule_row="";
				for(int j=0; j<genesAnd.size();j++) {

					if(genesAnd.get(j).getSelectedItem()!=null) {

						String geneLocusTag = genesAnd.get(j).getSelectedItem().toString().split(" ")[0];

						if(!geneLocusTag.isEmpty()) {	
							
							Integer geneId = geneModelIDs.get(geneLocusTag);
	
							if(j>0)
								boolean_rule_row = boolean_rule_row.concat(" AND ");

							boolean_rule_row = boolean_rule_row.concat(geneId.toString());
						}
					}
				}

				if(l>0 && !boolean_rule_row.isEmpty())
					boolean_rule = boolean_rule.concat(" OR ");

				boolean_rule = boolean_rule.concat(boolean_rule_row);
			}
		}

		if(go) {

			this.updatePathways();
			
			String compName = jComboBoxLocalisation.getSelectedItem().toString();
			Integer compId = null;
			
			if(compName != null && !compName.isEmpty())
				compId = ModelCompartmentServices.getCompartmentByName(reactionsInterface.getWorkspace().getName(), compName).getCompartmentID();
			
			if(rowID < 0) {
				try {
				ModelReactionsServices.insertNewReaction(jTextFieldName.getText(), jTextFieldEquation.getText(),
						compartments, metabolites, jCheckBoxInModel.isSelected(), selectedEnzymesAndPathway, compId,
						jSpontaneous.isSelected(), jNonEnzymatic.isSelected(), jIsGeneric.isSelected(),
						Double.valueOf(lowerBoundary.getText()), Double.valueOf(upperBoundary.getText()), SourceType.MANUAL, boolean_rule,
						reactionsInterface.getWorkspace().getName());
				Workbench.getInstance().info("Reaction successfully added!");
				}
				catch (Exception exc) {
					Workbench.getInstance().error(exc);
					exc.printStackTrace();
				}

			}
			else {
				ModelReactionsServices.updateReaction(rowID, jTextFieldName.getText(), jTextFieldEquation.getText(), jRadioButtonReversible.isSelected(),
						compartments, metabolites, jCheckBoxInModel.isSelected(), selectedEnzymesAndPathway, compId, 
						jSpontaneous.isSelected(), jNonEnzymatic.isSelected(), jIsGeneric.isSelected(),
						(long) Double.valueOf(lowerBoundary.getText()).doubleValue(), (long) Double.valueOf(upperBoundary.getText()).doubleValue(), boolean_rule,
						reactionsInterface.getWorkspace().getName());
				
				Workbench.getInstance().info("Reaction successfully edited!");

			}
			this.inModel = jCheckBoxInModel.isSelected();
			applyPressed=true;
		}
	}

	/**
	 * @throws Exception 
	 * 
	 */
	private void startFields() throws Exception {

		ReactionContainer data = reactionsInterface.getReaction(rowID);
		jTextFieldName.setText(data.getExternalIdentifier());
		jTextFieldName.setToolTipText(data.getExternalIdentifier());
		jTextFieldEquation.setText(data.getEquation());
		jTextFieldEquation.setToolTipText(data.getEquation());

		if(data.isReversible()) {

			jRadioButtonReversible.setSelected(true);
		}
		else {

			jRadioButtonIrreversible.setSelected(true);
			jForward.setSelected(true);
			jForward.setEnabled(true);
			jBackword.setEnabled(true);
		}

		jCheckBoxInModel.setSelected(data.isInModel());
				
		String compName = "";
		
		if(data.getLocalisation() != null)
			compName = data.getLocalisation().getName();
		
		jComboBoxLocalisation.setSelectedItem(compName);
		jSpontaneous.setSelected(data.isSpontaneous());
		jNonEnzymatic.setSelected(data.isNon_enzymatic());
		jIsGeneric.setSelected(data.isGeneric());

//		if(data.isReversible())
//			this.lowerBoundary.setText("-999999");
//		else
//			this.lowerBoundary.setText("0");
		if(data.getLowerBound()!=null)
			this.lowerBoundary.setText(data.getLowerBound().toString());

//		this.upperBoundary.setText("999999");
		if(data.getUpperBound()!=null)
			this.upperBoundary.setText(data.getUpperBound().toString());

		if(data.getGeneRule()!= null) {
			
			geneRule_AND_OR = pt.uminho.ceb.biosystems.merlin.utilities.Utilities.parseStringRuleToList(data.getGeneRule());

		}

		List<String> r = new ArrayList<String>();
		List<String> p = new ArrayList<String>();
		List<String> rs = new ArrayList<String>();
//		List<String> rc = new ArrayList<String>();
		List<String> ps = new ArrayList<String>();
//		List<String> pc = new ArrayList<String>();
		List<String> compartmentReactant = new ArrayList<>();
		List<String> compoundID_R = new ArrayList<>();
		List<String> compartmentProduct = new ArrayList<>();
		List<String> compoundID_P = new ArrayList<>();

		this.reactionMetabolites = reactionsInterface.getMetabolites(rowID);

		for(Integer stoichId : reactionMetabolites.keySet()) {

			if(reactionMetabolites.get(stoichId).getStoichiometric_coefficient().toString().startsWith("-")) {

				r.add(reactionMetabolites.get(stoichId).getCompartment_name());
				rs.add(reactionMetabolites.get(stoichId).getStoichiometric_coefficient()+"");
				compartmentReactant.add(reactionMetabolites.get(stoichId).getCompartment_name());
				compoundID_R.add(reactionMetabolites.get(stoichId).getMetaboliteID()+"");
			}
			else {

				p.add(reactionMetabolites.get(stoichId).getCompartment_name());
				ps.add(reactionMetabolites.get(stoichId).getStoichiometric_coefficient()+"");
				compartmentProduct.add(reactionMetabolites.get(stoichId).getCompartment_name());
				compoundID_P.add(reactionMetabolites.get(stoichId).getMetaboliteID()+"");
			}
		}

		reactants = compoundID_R.toArray(reactants);
		reactantsStoichiometry = rs.toArray(reactantsStoichiometry);
//		reactantsChains = rc.toArray(reactantsChains);
		reactantsCompartments = compartmentReactant.toArray(reactantsCompartments);

		products = compoundID_P.toArray(products);
		productsStoichiometry = ps.toArray(productsStoichiometry);
//		productsChains = pc.toArray(productsChains);
		productsCompartments = compartmentProduct.toArray(productsCompartments);

		panelReactants = this.addReactantsPanel();
		jScrollPaneReactants.setViewportView(panelReactants);
		panelProducts = this.addProductsPanel();
		jScrollPaneProducts.setViewportView(panelProducts);

		jScrollPaneGeneRule.setViewportView(this.addGeneRules());

	}

	/**
	 * @return
	 */
	private JButton jButtonAddReactant() {

		JButton jButtonAddReactant = new JButton();
		jButtonAddReactant.setIcon((new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Add.png")), 0.04).resizeImageIcon()));
		jButtonAddReactant.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					
					String[] newReactants = new String[reactantsField.size()+1];
					String[] newReactantsStoichiometry = new String[reactantsStoichiometryField.length+1];
					String[] newReactantsComp = new String[reactantsCompartmentsBox.size()+1];

					for(int i=0; i<reactantsField.size();i++) {
						
						String element = reactantsExternalIdentifiersField.get(i).getSelectedItem().toString();
						String reactantIndex = mapMetabolitesIndex.get(element);
						
						newReactants[i]=reactantIndex;
						newReactantsStoichiometry[i] = reactantsStoichiometryField[i].getText().toString();
						newReactantsComp[i] = reactantsCompartmentsBox.get(i).getSelectedItem().toString();
					}

					newReactants[reactantsField.size()]="-1";
					newReactantsStoichiometry[reactantsStoichiometryField.length]="-1.0";
					newReactantsComp[reactantsCompartmentsBox.size()] = defaultCompartment;
					reactants=newReactants;
					reactantsStoichiometry = newReactantsStoichiometry;
					reactantsCompartments=newReactantsComp;
					jScrollPaneReactants.setViewportView(addReactantsPanel());

				}
				catch (ArrayIndexOutOfBoundsException e1) {
				
					Workbench.getInstance().warn("please select a reactant metabolite before add a new one");
				}
				catch (Exception e2){
					e2.printStackTrace();
				}
			}
		});
		return jButtonAddReactant;
	}

	/**
	 * @return
	 */
	private JButton jButtonAddProduct() {

		JButton jButtonAddProduct = new JButton();
		jButtonAddProduct.setIcon((new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Add.png")), 0.04).resizeImageIcon()));
		jButtonAddProduct.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					String[] newProduct = new String[productsField.size()+1];
					String[] newProductStoichiometry = new String[productsStoichiometryField.length+1];
					String[] newProductComp = new String[productsCompartmentsBox.size()+1];

					for(int i=0; i<productsField.size();i++) {
						String element = productsExternalIdentifiersField.get(i).getSelectedItem().toString();
						String reactantIndex = mapMetabolitesIndex.get(element);
						newProduct[i]=reactantIndex;
						newProductStoichiometry[i] = productsStoichiometryField[i].getText().toString();
						newProductComp[i] = productsCompartmentsBox.get(i).getSelectedItem().toString();
					}

					newProduct[productsField.size()]="-1";
					newProductStoichiometry[productsStoichiometryField.length]="1.0";
					newProductComp[productsCompartmentsBox.size()] = defaultCompartment;

					products=newProduct;
					productsStoichiometry = newProductStoichiometry;
					productsCompartments=newProductComp;
					jScrollPaneProducts.setViewportView(addProductsPanel());
				} 
				catch (ArrayIndexOutOfBoundsException e1) {
					e1.printStackTrace();
					Workbench.getInstance().warn("please select a product metabolite before add a new one.");
				}
				catch (Exception e2){
					e2.printStackTrace();
				}					
			}
		});

		return jButtonAddProduct ;
	}

	/**
	 * @return
	 */
	private JButton jButtonAddEnzyme() {

		JButton jButtonAddEnzyme = new JButton();
		jButtonAddEnzyme.setIcon((new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Add.png")), 0.04).resizeImageIcon()));
		jButtonAddEnzyme.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				selectedEnzymesAndPathway.get(selectedPathway).add("");
				try {
					jScrollPaneEnzymes.setViewportView(addEnzymes());
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		return jButtonAddEnzyme;
	}

	/**
	 * @return
	 */
	private JButton jButtonAddPathways() {

		JButton jButtonAddPathways = new JButton();
		jButtonAddPathways.setIcon((new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Add.png")), 0.04).resizeImageIcon()));
		jButtonAddPathways.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {

				selectedEnzymesAndPathway.put("", new HashSet<String>());
				jScrollPanePathways.setViewportView(addPathways());					
			}
		});
		return jButtonAddPathways;
	}

	/**
	 * @param comboArray
	 * @param comboBox
	 */
	private boolean comboBoxActionListener(List<JComboBox<String>> comboArray, JComboBox<String> comboBox) {
		
		int productCompartmentIndex = -1;
		int index = -1;
		for(JComboBox<String> p: productsField) {
			index++;
			if(comboBox.getSelectedItem().toString() == p.getSelectedItem().toString())
				productCompartmentIndex = index;
		}
		
		int i = -1;
		int match = 0;
		for(JComboBox<String> r: reactantsField) {
			i++;
			if(comboBox.getSelectedItem().toString() ==  r.getSelectedItem().toString() && !comboBox.getSelectedItem().toString().equals("") && i != reactantsField.size()) {			
				if(productCompartmentIndex>-1) {	
					// product to be inserted matches already inserted reactant, by name AND compartment
					if(reactantsCompartmentsBox.get(i).getSelectedItem().toString() == productsCompartmentsBox.get(productCompartmentIndex).getSelectedItem().toString()) {
						Workbench.getInstance().warn("The product you are trying to enter is already registered as a reactant in the same compartment!");
						comboBox.setSelectedIndex(0);
						return false;
					}
					}
				else {
					match ++;
					if (match >1) {
						// reactant to be inserted matches with already inserted reactant
						Workbench.getInstance().warn("Warning, reactant already selected!");
						return false;
					}
				}
			}
		}
		return true;
	}


	/**
	 * @param arg0
	 */
	private void pathwaysAction(List<String> paths, String editedPathway) {

		Map<String, Set<String>> newSelectedEnzymesAndPathway = new HashMap<String, Set<String>>();

		for(String pathway : paths) {

			Set<String> enz = new TreeSet<String>();

			if(selectedEnzymesAndPathway.containsKey(pathway))
				enz = selectedEnzymesAndPathway.get(pathway);

			newSelectedEnzymesAndPathway.put(pathway, enz);
		}

		selectedEnzymesAndPathway = newSelectedEnzymesAndPathway;
		pathwaysClick(editedPathway);
	}


	/**
	 * @param pathway
	 */
	private void pathwaysClick(String pathway) {

		if(pathway.equals(""))
			this.selectedPathway="-1allpathwaysinreaction";
		else
			this.selectedPathway=pathway;

		try {
			this.jScrollPaneEnzymes.setViewportView(addEnzymes());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}


	/**
	 * @param arg0
	 */
	private void enzymesAction(ItemEvent arg0) {

		if(arg0.getStateChange()==ItemEvent.SELECTED) {

			Set<String> enzs = new HashSet<String>();
			for(JComboBox<String> jEnzymes : this.enzymesField) {

				String enzyme = jEnzymes.getSelectedItem().toString();

				if(!enzyme.equals(""))
					enzs.add(enzyme);
			}

			@SuppressWarnings("unchecked")
			boolean newECnumber=comboBoxActionListener(this.enzymesField, (JComboBox<String>) arg0.getSource());
			boolean isReallySelected = this.verifyIfIsSelected(this.enzymesField, enzs);

			if(newECnumber && isReallySelected)
				this.selectedEnzymesAndPathway.put(this.selectedPathway,enzs);
		}
	}

	/**
	 * 
	 */
	private void updatePathways () {

		//if all pathways, put new enzyme in all pathways
		if(selectedPathway.equalsIgnoreCase("-1allpathwaysinreaction")) {

			Set<String> allEnzymes = new HashSet<>();					

			if(selectedEnzymesAndPathway.containsKey("-1allpathwaysinreaction"))
				allEnzymes = selectedEnzymesAndPathway.get("-1allpathwaysinreaction");

			for(String pathway : selectedEnzymesAndPathway.keySet()) {

				if(!pathway.equalsIgnoreCase("-1allpathwaysinreaction")) {

					Set<String> enzymes = selectedEnzymesAndPathway.get(pathway);

					enzymes.retainAll(allEnzymes);
					selectedEnzymesAndPathway.put(pathway, enzymes);
				}
			}
		}
		else {

			// collect ec numbers for this reaction
			Set<String> newEnzymes = new HashSet<String> ();
			for(String pathway : selectedEnzymesAndPathway.keySet()) {

				Set<String> enzymes = selectedEnzymesAndPathway.get(pathway);
				newEnzymes.addAll(enzymes);
			}

			selectedEnzymesAndPathway.put("-1allpathwaysinreaction", newEnzymes);
		}
	}

	/**
	 * @param enzymesField2
	 * @param enzs
	 * @return
	 */
	private boolean verifyIfIsSelected(List<JComboBox<String>> comboArray, Set<String> enzs) {

		Set<String> surrogateEnzs = new HashSet<>(enzs);

		for(JComboBox<String> r:comboArray) {

			String ec = r.getSelectedItem().toString();

			if(enzs.contains(ec))
				surrogateEnzs.remove(ec);
		}

		return surrogateEnzs.isEmpty();
	}

	/**
	 * 
	 */
	private void pathwaysPanelClick() {

		this.selectedPathway="-1allpathwaysinreaction";
		try {
			this.jScrollPaneEnzymes.setViewportView(addEnzymes());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateFieldsOnActionPerformedKeggId (String element,  JComboBox<String> Metabolites, JLabel formula) {

		for (Map.Entry<String,List<String>> metaboliteName : mapMetabolites.entrySet()) {
			if(metaboliteName != null) {

				try {

					if (element.equals("")) {
						Metabolites.setSelectedIndex(0);
						formula.setText("");
						break;
					}
					else if(metaboliteName.getKey().equals(element)) {
						
						
						if(metaboliteName.getValue().size() > 0 && metaboliteName.getValue().get(0) != null) 
							Metabolites.setSelectedItem(metaboliteName.getValue().get(0));
						else {
							Metabolites.setSelectedItem(metaboliteName.getKey());
						}

						if(metaboliteName.getValue().size() > 1 && metaboliteName.getValue().get(1) != null) 
							formula.setText(metaboliteName.getValue().get(1));
						else {
							formula.setText("------------");
						}
						break;
					}
				}
				catch(Exception ex) {

				}

			}

		}

	}

	private void updateFieldsOnActionPerformedMetabolites (String element, JComboBox<String> Id, JLabel Formula) {
		
		// this method verifies if the newly selected metabolite matches the previously selected one by name. 
		// Then it verifies by formula
		
		for (Map.Entry<String,List<String>> metaboliteName : mapMetabolites.entrySet()) {
			if(metaboliteName != null) {

				try {
					if (element.equals("")) {
						Id.setSelectedIndex(0);
						Formula.setText("");
						break;
					}
					else if(metaboliteName.getKey().equals(element)) {
						Id.setSelectedItem(metaboliteName.getKey());
						Formula.setText("------------");
						break;
					}
					else if(metaboliteName.getValue().get(0).equals(element)) {

						String newFormula = metaboliteName.getValue().get(1) ;

						if(newFormula == null)
							newFormula = "------------";
						
						String formula = Formula.getText();

						if(!newFormula.equalsIgnoreCase(formula)) {
							Id.setSelectedItem(metaboliteName.getKey());

							if(metaboliteName.getValue().size()>1 && newFormula!="") 
								Formula.setText(newFormula);	
							else {
								Formula.setText("------------");
							}
							break;
						}
					}

				}
				catch(Exception ex) {

				}
			}

		}
	}
}
