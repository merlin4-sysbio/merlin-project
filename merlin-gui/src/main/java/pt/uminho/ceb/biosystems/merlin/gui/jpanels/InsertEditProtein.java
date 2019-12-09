package pt.uminho.ceb.biosystems.merlin.gui.jpanels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.ModelProteinsAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.AIBenchUtils;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.CreateImageIcon;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelProteinsServices;

/**
 * @author ODias
 *
 */
public class InsertEditProtein extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int selectedRow;
	private ModelProteinsAIB proteins;
	private JPanel jPanelDialogProtein;
	private JPanel jPanelName;
	private JTextField jTextFieldName;
	private JScrollPane jScrollPaneProtein;
	private JScrollPane jScrollPaneEnzyme;
	private JTextField jTextFieldClass;
	private JPanel jPanelClass;
	private JPanel jPanelMolecularWeight;
	private JPanel jPanel1;
	private JTextField jTextFieldUser;
	private JPanel jPanel2;
	private JTextField jTextFieldExp;
	private JPanel jPanel3;
	private JTextField jTextFieldKD;
	private JPanel jPanel4;
	private JTextField jTextFieldSeq;
	private JPanel jPanelPi;
	private JTextField jTextFieldPi;
	private JPanel jPanelSaveClose;
	private JButton jButtonSave;
	private JButton jButtonCancel;
	private JPanel jPanelInchi;
	private JTextField jTextFieldInchi;
	private String[] synonyms;
	private String[] oldSynonyms;
	private String[] enzymes;
	private String[] oldEnzymes;
	private Boolean[] inModel;
	private Boolean[] oldInModel;
	private JCheckBox[] enzymeCheckBox;
	private JTextField[] textField;
	private JTextField[] enzymeField;

	/**
	 * @param selectedRow
	 * @param proteins
	 * @throws Exception 
	 */
	public InsertEditProtein(int selectedRow, ModelProteinsAIB proteins) throws Exception {

		super(Workbench.getInstance().getMainFrame());
		this.proteins = proteins;
		String[] data;

		if(selectedRow!=-10) {

			data = proteins.getProteinData(selectedRow, true);
			this.setTitle("Edit Protein");

			//get protein array data (used to determine relative position of sqlIndex and sql String on the comboBox)
			synonyms = proteins.getSynonyms(selectedRow, true);
			if(synonyms == null){synonyms = new String[0];}
			//store original proteins associated to such gene, immutable
			oldSynonyms = Arrays.copyOf(proteins.getSynonyms(selectedRow, false), synonyms.length);
			if(oldSynonyms == null){oldSynonyms = new String[0];}

			enzymes = data[9].split(";");
			if(enzymes == null){enzymes = new String[0];}
			oldEnzymes = data[9].split(";");
			if(oldEnzymes == null){oldEnzymes = new String[0];}

			String[] isInModel = data[10].split(";");
			inModel = new Boolean[isInModel.length];
			oldInModel = new Boolean[isInModel.length];

			for(int i=0;i<isInModel.length;i++)
			{
				inModel[i]= Boolean.parseBoolean(isInModel[i]);
				oldInModel[i]= Boolean.parseBoolean(isInModel[i]);
			}
			this.initGUI();
			this.startFields(data);
		}
		else {

			synonyms = new String[0];
			oldSynonyms = new String[0];
			enzymes = new String[0];
			oldEnzymes = new String[0];
			inModel = new Boolean[0];
			oldInModel = new Boolean[0];
			this.setTitle("Insert Protein");
			initGUI();
		}
		Utilities.centerOnOwner(this);
		this.selectedRow = selectedRow;
		this.setIconImage((new ImageIcon(getClass().getClassLoader().getResource("icons/merlin.png"))).getImage());
		this.setVisible(true);		
		this.setAlwaysOnTop(true);
		this.toFront();

	}

	private void initGUI() {

		GroupLayout thisLayout = new GroupLayout((JPanel)getContentPane());
		getContentPane().setLayout(thisLayout);
		{
			jPanelDialogProtein = new JPanel(new GridBagLayout());
			GridBagLayout jPanelDialogProteinLayout = new GridBagLayout();
			jPanelDialogProteinLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.0, 0.0, 0.0};
			jPanelDialogProteinLayout.columnWidths = new int[] {8, 200, 7, 200, 6, 8};
			jPanelDialogProteinLayout.rowWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0, 0.0, 0.1, 0.0, 0.0, 0.1, 0.0, 0.1, 0.0};
			jPanelDialogProteinLayout.rowHeights = new int[] {7, 7, 7, 7, 6, 7, 7, 7, 7, 7, 7, 20, 7};
			jPanelDialogProtein.setLayout(jPanelDialogProteinLayout);
			{
				jPanelName = new JPanel();
				GridBagLayout jPanelNameLayout = new GridBagLayout();
				jPanelDialogProtein.add(jPanelName, new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelNameLayout.rowWeights = new double[] {0.1};
				jPanelNameLayout.rowHeights = new int[] {7};
				jPanelNameLayout.columnWeights = new double[] {0.1};
				jPanelNameLayout.columnWidths = new int[] {7};
				jPanelName.setLayout(jPanelNameLayout);
				jPanelName.setBorder(BorderFactory.createTitledBorder(null, "Protein Name", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
				{
					jTextFieldName = new JTextField();
					jTextFieldName.setAlignmentX(LEFT_ALIGNMENT);
					jPanelName.add(jTextFieldName, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

				}
			}

			{
				jTextFieldClass = new JTextField();
				jTextFieldClass.setAlignmentX(LEFT_ALIGNMENT);
				jPanelClass = new JPanel();
				GridBagLayout jPanelClassLayout = new GridBagLayout();
				jPanelClass.setBorder(BorderFactory.createTitledBorder("Class"));
				jPanelClassLayout.rowWeights = new double[] {0.1};
				jPanelClassLayout.rowHeights = new int[] {7};
				jPanelClassLayout.columnWeights = new double[] {0.1};
				jPanelClassLayout.columnWidths = new int[] {7};
				jPanelClass.setLayout(jPanelClassLayout);
				jPanelClass.add(jTextFieldClass, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelDialogProtein.add(jPanelClass, new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}

			{
				jPanelInchi = new JPanel();
				GridBagLayout jPanelInchiLayout = new GridBagLayout();
				jPanelDialogProtein.add(jPanelInchi, new GridBagConstraints(3, 9, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelInchiLayout.rowWeights = new double[] {0.1};
				jPanelInchiLayout.rowHeights = new int[] {7};
				jPanelInchiLayout.columnWeights = new double[] {0.1};
				jPanelInchiLayout.columnWidths = new int[] {7};
				jPanelInchi.setLayout(jPanelInchiLayout);
				jPanelInchi.setBorder(BorderFactory.createTitledBorder(null, "Inchi", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
				{
					jTextFieldInchi = new JTextField();
					jTextFieldInchi.setAlignmentX(LEFT_ALIGNMENT);
					jPanelInchi.add(jTextFieldInchi, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

					//jTextFieldInchi.setPreferredSize(new java.awt.Dimension(120, 25));
				}
			}


			{
				jPanelMolecularWeight= new JPanel();
				GridBagLayout jPanelMolecularWeighLayout = new GridBagLayout();
				jPanelDialogProtein.add(jPanelMolecularWeight, new GridBagConstraints(1, 6, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelMolecularWeighLayout.rowWeights = new double[] {0.1, 0.1};
				jPanelMolecularWeighLayout.rowHeights = new int[] {7, 7};
				jPanelMolecularWeighLayout.columnWeights = new double[] {0.1, 0.0, 0.1};
				jPanelMolecularWeighLayout.columnWidths = new int[] {7, 7, 7};
				jPanelMolecularWeight.setLayout(jPanelMolecularWeighLayout);
				jPanelMolecularWeight.setBorder(BorderFactory.createTitledBorder(null, "Molecular Weight", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
				{
					jPanel1 = new JPanel();
					GridBagLayout jPanel1Layout = new GridBagLayout();
					jPanelMolecularWeight.add(jPanel1, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanel1Layout.rowWeights = new double[] {0.1};
					jPanel1Layout.rowHeights = new int[] {7};
					jPanel1Layout.columnWeights = new double[] {0.1};
					jPanel1Layout.columnWidths = new int[] {7};
					jPanel1.setLayout(jPanel1Layout);
					jPanel1.setBorder(BorderFactory.createTitledBorder(null, "User", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
					{
						jTextFieldUser = new JTextField();
						jPanel1.add(jTextFieldUser, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					}
				}

				{
					jPanel2 = new JPanel();
					GridBagLayout jPanel2Layout = new GridBagLayout();
					jPanelMolecularWeight.add(jPanel2, new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanel2Layout.rowWeights = new double[] {0.1};
					jPanel2Layout.rowHeights = new int[] {7};
					jPanel2Layout.columnWeights = new double[] {0.1};
					jPanel2Layout.columnWidths = new int[] {7};
					jPanel2.setLayout(jPanel2Layout);
					jPanel2.setBorder(BorderFactory.createTitledBorder(null, "Experiments", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
					{
						jTextFieldExp = new JTextField();
						jPanel2.add(jTextFieldExp, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					}
				}

				{
					jPanel3 = new JPanel();
					GridBagLayout jPanel3Layout = new GridBagLayout();
					jPanelMolecularWeight.add(jPanel3, new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanel3.setBorder(BorderFactory.createTitledBorder(null, "KD", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
					jPanel3Layout.rowWeights = new double[] {0.1};
					jPanel3Layout.rowHeights = new int[] {7};
					jPanel3Layout.columnWeights = new double[] {0.1};
					jPanel3Layout.columnWidths = new int[] {7};
					jPanel3.setLayout(jPanel3Layout);
					{
						jTextFieldKD = new JTextField();
						jPanel3.add(jTextFieldKD, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					}
				}

				{
					jPanel4 = new JPanel();
					GridBagLayout jPanel4Layout = new GridBagLayout();
					jPanelMolecularWeight.add(jPanel4, new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanel4.setBorder(BorderFactory.createTitledBorder(null, "Sequence", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
					jPanel4Layout.rowWeights = new double[] {0.1};
					jPanel4Layout.rowHeights = new int[] {7};
					jPanel4Layout.columnWeights = new double[] {0.1};
					jPanel4Layout.columnWidths = new int[] {7};
					jPanel4.setLayout(jPanel4Layout);
					{
						jTextFieldSeq = new JTextField();
						jPanel4.add(jTextFieldSeq, new GridBagConstraints(0, -1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					}
				}

			}

			{
				jPanelPi= new JPanel();
				GridBagLayout jPanelPiLayout = new GridBagLayout();
				jPanelDialogProtein.add(jPanelPi, new GridBagConstraints(1, 9, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelPiLayout.rowWeights = new double[] {0.1};
				jPanelPiLayout.rowHeights = new int[] {7};
				jPanelPiLayout.columnWeights = new double[] {0.1};
				jPanelPiLayout.columnWidths = new int[] {7};
				jPanelPi.setLayout(jPanelPiLayout);
				jPanelPi.setBorder(BorderFactory.createTitledBorder(null, "Pi", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
				{
					jTextFieldPi = new JTextField();
					jTextFieldPi.setAlignmentX(LEFT_ALIGNMENT);
					jPanelPi.add(jTextFieldPi, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
			}

			{
				jScrollPaneProtein = new JScrollPane();
				jScrollPaneProtein.setViewportView(this.addSynonymsPanel());
				//					jScrollPaneProtein1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				jPanelDialogProtein.add(jScrollPaneProtein, new GridBagConstraints(3, 6, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}

			//ButtonAddSynonym
			{
				JButton jButtonAddSynonym = new JButton();
				jButtonAddSynonym.setIcon((new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Add.png")), 0.04).resizeImageIcon()));
				jPanelDialogProtein.add(jButtonAddSynonym, new GridBagConstraints(4, 7, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jButtonAddSynonym.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e)
					{
						String[] newSynonyms = new String[synonyms.length+1];
						for(int i=0; i<synonyms.length;i++)
						{
							newSynonyms[i]=synonyms[i];
						}
						newSynonyms[synonyms.length]=""; 
						synonyms=newSynonyms;
						jScrollPaneProtein.setViewportView(addSynonymsPanel());
					}});
			}

			{
				jScrollPaneEnzyme = new JScrollPane();
				jScrollPaneEnzyme.setViewportView(this.addEnzymesPanel());
				//jScrollPaneProtein1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				jPanelDialogProtein.add(jScrollPaneEnzyme, new GridBagConstraints(3, 1, 1, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}

			//ButtonAddEnzyme
			// UNCOMMENT THE FOLLOWING BLOCK IF YOU WANT TO ACTIVATE THE OPTION OF ADDING MULTIPLE ENZYMES FOR A GIVEN PROTEIN
//			{
//				JButton jButtonAddProtein = new JButton();
//				jButtonAddProtein.setIcon((new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Add.png")), 0.04).resizeImageIcon()));
//				jPanelDialogProtein.add(jButtonAddProtein, new GridBagConstraints(4, 4, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//				jButtonAddProtein.addActionListener(new ActionListener(){
//
//					@Override
//					public void actionPerformed(ActionEvent e)
//					{
//						String[] newEnzymes = new String[enzymes.length+1];
//						Boolean[] inEnzymes = new Boolean[inModel.length+1];
//						for(int i=0; i<enzymes.length;i++)
//						{
//							newEnzymes[i]=enzymes[i];
//							inEnzymes[i] = inModel[i];
//						}
//						newEnzymes[enzymes.length]="";
//						inEnzymes[inModel.length] = false;
//
//						enzymes = newEnzymes;
//						inModel = inEnzymes;
//
//						jScrollPaneEnzyme.setViewportView(addEnzymesPanel());
//					}});
//			}

			{
				jPanelSaveClose = new JPanel(new GridBagLayout());
				GridBagLayout jPanelSaveCloseLayout = new GridBagLayout();
				jPanelSaveCloseLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				jPanelSaveCloseLayout.rowHeights = new int[] {5, 10, 5};
				jPanelSaveCloseLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
				jPanelSaveCloseLayout.columnWidths = new int[] {8, 130, 24, 130, 8};
				//set layout
				jPanelSaveClose.setLayout(jPanelSaveCloseLayout);

				{
					jButtonSave = new JButton();
					jPanelSaveClose.add(jButtonSave, new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jButtonSave.setText("Save");
					jButtonSave.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Save.png")),0.1).resizeImageIcon());
					jButtonSave.addActionListener(new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent e) {
							try {
							String name;
							name = jTextFieldName.getText();
							if(name.isEmpty())
								 throw new Exception("Please enter a name");
							
							ProteinContainer protein = new ProteinContainer(""); 
							if(enzymes.length>0) {
								protein.setExternalIdentifier(enzymes[0]);
							}
							
							protein.setName(name);
							
							if(jTextFieldClass.getText() != null && !jTextFieldClass.getText().isEmpty())
								protein.setClass_(jTextFieldClass.getText());
							
							if(jTextFieldInchi.getText() != null && !jTextFieldInchi.getText().isEmpty())
								protein.setInchi(jTextFieldInchi.getText());
							
							if(jTextFieldUser.getText() != null && !jTextFieldUser.getText().isEmpty())
								protein.setMolecularWeight(Float.parseFloat(jTextFieldUser.getText()));
							
							if(jTextFieldKD.getText() != null && !jTextFieldKD.getText().isEmpty())
								protein.setMolecularWeightExp(Float.parseFloat(jTextFieldKD.getText()));
							
							if(jTextFieldExp.getText() != null && !jTextFieldExp.getText().isEmpty())
								protein.setMolecularWeightKd(Float.parseFloat(jTextFieldExp.getText()));
							
							if(jTextFieldSeq.getText() != null && !jTextFieldSeq.getText().isEmpty())
								protein.setMolecularWeightSeq(Float.parseFloat(jTextFieldSeq.getText()));
							
							if(jTextFieldPi.getText() != null && !jTextFieldPi.getText().isEmpty())
								protein.setPi(Float.parseFloat(jTextFieldPi.getText()));
							
							if(inModel[0] != null)
								protein.setInModel(inModel[0]);

								if(selectedRow == -10)
								{
									protein.setSource(SourceType.MANUAL.toString());
									ModelProteinsServices.insertProtein(proteins.getWorkspace().getName(), protein, synonyms, enzymes, inModel);
									Workbench.getInstance().info("Protein successfully added!");
									close();
								}
								else
								{
									protein.setIdProtein(proteins.getIdentifiers().get(selectedRow));
									ModelProteinsServices.updateProtein(proteins.getWorkspace().getName(), protein, synonyms, oldSynonyms,  enzymes, oldEnzymes, inModel, oldInModel);
									Workbench.getInstance().info("Protein successfully edited!");
									close();
								}
								AIBenchUtils.updateView(proteins.getWorkspace().getName(), ModelProteinsAIB.class);
							} catch (Exception e1) {
								Workbench.getInstance().error(e1);
								e1.printStackTrace();
							}

						}});
				}
				{
					jButtonCancel = new JButton();
					jPanelSaveClose.add(jButtonCancel, new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jButtonCancel.setText("Close");
					jButtonCancel.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Cancel.png")), 0.1).resizeImageIcon());
					jButtonCancel.addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent arg0) {close();}
					});
				}
				jPanelDialogProtein.add(jPanelSaveClose, new GridBagConstraints(0, 11, 5, 2, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

			}

			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(jPanelDialogProtein, 0, 515, Short.MAX_VALUE));
			thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
					.addComponent(jPanelDialogProtein, 0, 488, Short.MAX_VALUE));
			this.setModal(true);
			this.pack();
		} 
	}


	/**
	 * Close window 
	 */
	private void close(){
		this.setVisible(false);
		this.dispose();
	}


	/**
	 * @param synonym
	 * @return
	 */
	private JTextField addSynonymField(String synonym) {

		JTextField jTextField = new JTextField(synonym);
		jTextField.setPreferredSize(new Dimension(100, 26));
		jTextField.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent arg0) {}

			@Override
			public void keyPressed(KeyEvent arg0) {}

			@Override
			public void keyReleased(KeyEvent arg0) {

				JTextField jTextField = (JTextField) arg0.getSource();
				int selectedField = 0;

				for(int i=0; i<textField.length;i++)
				{
					if(textField[i]==jTextField)// find my object
					{
						selectedField=i;
						i=textField.length;
					}
				}

				for(int i=0; i<textField.length;i++)
				{
					if(i!=selectedField && synonyms[i].equals(jTextField.getText()))//find objects that may have the same input has mine
					{
						Workbench.getInstance().warn("Text already inserted on field "+(i+1)+"!\nPlease insert another entry!");
						jTextField.setText(synonyms[selectedField].toString());
						i=synonyms.length;
					}
				}
				synonyms[selectedField]=jTextField.getText();	
				int width = ((String) jTextField.getText()).length()*7;
				if(width<100)
				{jTextField.setPreferredSize(new Dimension(100, 26));}
				else
				{jTextField.setPreferredSize(new Dimension(width,26));}
			}
		});

		return jTextField;
	}


	/**
	 * @return
	 */
	private JPanel addSynonymsPanel() {

		JPanel panelSynonym = new JPanel(new GridBagLayout());
		GridBagLayout jPanelSynonymLayout = new GridBagLayout();
		panelSynonym.setBorder(BorderFactory.createTitledBorder(new SoftBevelBorder(BevelBorder.LOWERED,null,null,null,null),"Synonyms",TitledBorder.LEADING,TitledBorder.DEFAULT_POSITION));

		//number of rows (array size)
		if(synonyms.length==0)
		{
			jPanelSynonymLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelSynonymLayout.rowHeights = new int[] {7, 7, 7};
		}
		else
		{
			jPanelSynonymLayout.rowWeights = new double[synonyms.length*2+1];
			jPanelSynonymLayout.rowHeights  = new int[synonyms.length*2+1];

			for(int rh=0; rh<synonyms.length*2+1; rh++)
			{
				jPanelSynonymLayout.rowWeights[rh]=0.1;
				jPanelSynonymLayout.rowHeights[rh]=7;
			}
		}

		//number of columns (array size)
		jPanelSynonymLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
		jPanelSynonymLayout.columnWidths = new int[] {7, 10, 7};
		//set layout
		panelSynonym.setLayout(jPanelSynonymLayout);
		//panelSynonym.setPreferredSize(new java.awt.Dimension(215, 204));

		if(synonyms.length==0)
		{
			synonyms = new String[1];
			synonyms[0]= "";
			textField = new JTextField[1];
			textField[0]=new JTextField("");
			textField[0]=addSynonymField("");
			textField[0].setAlignmentX(LEFT_ALIGNMENT);
			panelSynonym.add( textField[0], new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		else
		{
			textField = new JTextField[synonyms.length];
			for(int s=0; s<synonyms.length; s++)
			{
				textField[s]= addSynonymField(synonyms[s]);
				textField[s].setAlignmentX(LEFT_ALIGNMENT);
				int r =s*2+1;
				panelSynonym.add(textField[s], new GridBagConstraints(1, r, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
		}

		return panelSynonym;
	}

	/**
	 * @param enzyme
	 * @return
	 */
	private JTextField addEnzymeField(String enzyme) {

		JTextField jTextField = new JTextField(enzyme);
		//jTextField.setMinimumSize(new Dimension(100, 26));
		jTextField.setPreferredSize(new Dimension(100, 26));
		jTextField.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent arg0) {}

			@Override
			public void keyPressed(KeyEvent arg0) {}

			@Override
			public void keyReleased(KeyEvent arg0) {

				JTextField jTextField = (JTextField) arg0.getSource();
				int selectedField = 0;

				for(int i=0; i<enzymeField.length;i++)
				{
					if(enzymeField[i]==jTextField)// find my object
					{
						selectedField=i;
						i=enzymeField.length;
					}
				}

				for(int i=0; i<enzymeField.length;i++)
				{
					if(i!=selectedField && enzymes[i].equals(jTextField.getText()))//find objects that may have the same input has mine
					{
						Workbench.getInstance().warn("Text already inserted on field "+(i+1)+"!\nPlease insert another entry!");
						jTextField.setText(enzymes[selectedField].toString());
						i=enzymes.length;
					}
				}

				enzymes[selectedField]=jTextField.getText();	

				int width = ((String) jTextField.getText()).length()*7;


				if(width<100)
				{
					jTextField.setPreferredSize(new Dimension(100, 26));
				}
				else
				{
					jTextField.setPreferredSize(new Dimension(width,26));
				}
			}
		});

		return jTextField;
	}

	/**
	 * @param isInModel
	 * @return
	 */
	private JCheckBox addEnzymeCB(boolean isInModel) {

		JCheckBox jCheckBox = new JCheckBox();
		jCheckBox.setSelected(isInModel);
		jCheckBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JCheckBox jCheckBox = (JCheckBox) arg0.getSource();
				int selectedField = 0;

				for(int i=0; i<enzymeCheckBox.length;i++)
				{
					if(enzymeCheckBox[i]== jCheckBox)// find my object
					{
						selectedField=i;
						i=enzymeCheckBox.length;
					}
				}

				inModel[selectedField]=jCheckBox.isSelected();
			}

		});
		return jCheckBox;
	}

	/**
	 * @return
	 */
	private JPanel addEnzymesPanel() {

		JPanel panelEnzymes = new JPanel(new GridBagLayout());
		GridBagLayout jPanelTextLayout = new GridBagLayout();
		panelEnzymes.setBorder(BorderFactory.createTitledBorder(new SoftBevelBorder(BevelBorder.LOWERED,null,null,null,null),"Enzymes",TitledBorder.LEADING,TitledBorder.DEFAULT_POSITION));

		//number of rows (array size)
		if(enzymes.length==0) {

			jPanelTextLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelTextLayout.rowHeights = new int[] {7, 7, 7};
		}
		else {

			jPanelTextLayout.rowWeights = new double[enzymes.length*2+1];
			jPanelTextLayout.rowHeights  = new int[enzymes.length*2+1];

			for(int rh=0; rh<enzymes.length*2+1; rh++)
			{
				jPanelTextLayout.rowWeights[rh]=0.1;
				jPanelTextLayout.rowHeights[rh]=7;
			}
		}

		//number of columns (array size)
		jPanelTextLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0};
		jPanelTextLayout.columnWidths = new int[] {7, 10, 7, 10, 7};
		//set layout
		panelEnzymes.setLayout(jPanelTextLayout);
		//		panelEnzymes.setPreferredSize(new java.awt.Dimension(206, 165));
		panelEnzymes.add(new JLabel("EC number"), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		panelEnzymes.add(new JLabel("In Model"), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		if(enzymes.length==0) {

			enzymes = new String[1];
			enzymes[0]= "";
			enzymeField = new JTextField[1];
			//enzymeField[0]=new JTextField("");
			enzymeField[0]=addEnzymeField("");
			enzymeField[0].setAlignmentX(LEFT_ALIGNMENT);

			inModel = new Boolean[1];
			inModel[0] = false;
			enzymeCheckBox = new JCheckBox[1];
			//enzymeCheckBox[0] = new JCheckBox();
			enzymeCheckBox[0] = addEnzymeCB(false);
			panelEnzymes.add( enzymeField[0], new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			panelEnzymes.add( enzymeCheckBox[0], new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			enzymeCheckBox[0].setSelected(true);

		}
		else
		{
			enzymeCheckBox = new JCheckBox[inModel.length];
			enzymeField = new JTextField[enzymes.length];
			
			for(int s=0; s<enzymes.length; s++)
			{
				enzymeField[s]= addEnzymeField(enzymes[s]);
				enzymeField[s].setAlignmentX(LEFT_ALIGNMENT);
				enzymeCheckBox[s] = addEnzymeCB(inModel[s]);

				//				if(s<inModel.length)
				//				{
				//					jCheckBox = enzymeCheckBox[s];
				//				}

				int r =s*2+1;
				panelEnzymes.add(enzymeField[s], new GridBagConstraints(1, r, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				panelEnzymes.add( enzymeCheckBox[s], new GridBagConstraints(3, r, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				enzymeCheckBox[s].setSelected(true);
			}
		}

		return panelEnzymes;
	}

	private void startFields(String[] data){
		if(data[1]==null){jTextFieldName.setText("");}
		else
		{jTextFieldName.setText(data[1]);
		jTextFieldName.setAlignmentX(LEFT_ALIGNMENT);}

		if(data[2]==null){jTextFieldClass.setText("");}
		else
		{jTextFieldClass.setText(data[2]);
		jTextFieldClass.setAlignmentX(LEFT_ALIGNMENT);}

		if(data[3]==null){jTextFieldInchi.setText("");}
		else
		{jTextFieldInchi.setText(data[3]);
		jTextFieldInchi.setAlignmentX(LEFT_ALIGNMENT);}

		if(data[4]==null){jTextFieldUser.setText("");}
		else
		{jTextFieldUser.setText(data[4]);
		jTextFieldUser.setAlignmentX(LEFT_ALIGNMENT);}

		if(data[5]==null){jTextFieldExp.setText("");}
		else
		{jTextFieldExp.setText(data[5]);
		jTextFieldExp.setAlignmentX(LEFT_ALIGNMENT);}

		if(data[6]==null){jTextFieldKD.setText("");}
		else
		{jTextFieldKD.setText(data[6]);
		jTextFieldKD.setAlignmentX(LEFT_ALIGNMENT);}

		if(data[7]==null){jTextFieldSeq.setText("");}
		else
		{jTextFieldSeq.setText(data[7]);
		jTextFieldSeq.setAlignmentX(LEFT_ALIGNMENT);}

		if(data[8]==null){	jTextFieldPi.setText("");}
		else
		{jTextFieldPi.setText(data[8]);
		jTextFieldPi.setAlignmentX(LEFT_ALIGNMENT);}
				
	}


}
