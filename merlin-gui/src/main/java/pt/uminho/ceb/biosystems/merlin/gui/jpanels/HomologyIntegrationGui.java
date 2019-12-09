/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.gui.jpanels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.SoftBevelBorder;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.Workspace;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.IntegrationType;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.annotation.AnnotationEnzymesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.CreateImageIcon;



/**
 * @author ODias
 *
 */
public class HomologyIntegrationGui extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private JPanel jPanel1;
	private JComboBox<IntegrationType> jComboBoxProducts;
//	private JCheckBox jCheckPartial;
	//private JCheckBox jCheckFull;
	private JLabel jLabelReports;
	private JComboBox<String> jComboBoxReports;
//	private JCheckBox jCheckBoxProducts;
	private JLabel jLabelGenes;
	private JComboBox<IntegrationType> jComboBoxEnzymes;
	private JComboBox<IntegrationType> jComboBoxGenes;
	private JPanel jPanel11;
	private JPanel jPanel12;
	private JLabel jLabelEnzymes;
	private JLabel jLabelProducts;
	private AnnotationEnzymesAIB homologyDataContainer;

	/**

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 */
	public HomologyIntegrationGui(String title, AnnotationEnzymesAIB homologyDataContainer) {
		
		super(Workbench.getInstance().getMainFrame());
		this.homologyDataContainer = homologyDataContainer;
		initGUI(title);
		Utilities.centerOnOwner(this);
		this.setIconImage((new ImageIcon(getClass().getClassLoader().getResource("icons/merlin.png"))).getImage());
		this.setVisible(true);		
		//this.setAlwaysOnTop(true);
		this.toFront();
	}

	/**
	 * Initiate gui method
	 * 
	 * @param title
	 */
	private void initGUI(String title) {

		{
			this.setTitle(title);	
			jPanel1 = new JPanel();
			getContentPane().add(jPanel1, BorderLayout.CENTER);
			GridBagLayout jPanel1Layout = new GridBagLayout();
			jPanel1Layout.columnWeights = new double[] {0.1, 0.0, 0.1, 0.0};
			jPanel1Layout.columnWidths = new int[] {7, 7, 7, 7};
			jPanel1Layout.rowWeights = new double[] {0.1, 0.0, 0.1, 0.0, 0.1, 0.0};
			jPanel1Layout.rowHeights = new int[] {7, 7, 20, 7, 20, 7};
			jPanel1.setLayout(jPanel1Layout);
			jPanel1.setPreferredSize(new java.awt.Dimension(386, 326));


			jPanel11 = new JPanel();
			GridBagLayout jPanel11Layout = new GridBagLayout();
			jPanel11.setLayout(jPanel11Layout);
			jPanel11Layout.columnWeights = new double[] {0.0, 0.1, 0.0};
			jPanel11Layout.columnWidths = new int[] {7, 7, 7};
			jPanel11Layout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.0, 0.0, 0.1, 0.0, 0.1, 0.1, 0.1, 0.0, 0.1, 0.1, 0.0};
			jPanel11Layout.rowHeights = new int[] {7, 20, 7, 7, 7, 7, 7, 20, 20, 7, 7, 20, 20, 7};

			jPanel1.add(jPanel11, new GridBagConstraints(0, 0, 3, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			{
				ComboBoxModel<IntegrationType> jComboBoxGenesModel = new DefaultComboBoxModel<> (IntegrationType.values());
				jComboBoxGenesModel.setSelectedItem(IntegrationType.MODEL);
				jComboBoxGenes = new JComboBox<>();
				jPanel11.add(jComboBoxGenes, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jComboBoxGenes.setModel(jComboBoxGenesModel);
				jLabelGenes = new JLabel();
				jPanel11.add(jLabelGenes, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelGenes.setText("gene names");
			}
			{
				ComboBoxModel<IntegrationType> jComboBoxEnzymesModel = 
						new DefaultComboBoxModel<>(IntegrationType.values());
				jComboBoxEnzymes = new JComboBox<>();
				jPanel11.add(jComboBoxEnzymes, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jComboBoxEnzymes.setModel(jComboBoxEnzymesModel);
				jLabelEnzymes = new JLabel();
				jPanel11.add(jLabelEnzymes, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelEnzymes.setText("enzymes");
			}
			{

				ComboBoxModel<IntegrationType> jComboBoxProductsModel = 
						new DefaultComboBoxModel<>(IntegrationType.values());
				jComboBoxProductsModel.setSelectedItem(IntegrationType.MODEL);
				jComboBoxProducts = new JComboBox<>();
				jPanel11.add(jComboBoxProducts, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jComboBoxProducts.setModel(jComboBoxProductsModel);
				jLabelProducts = new JLabel();
				jPanel11.add(jLabelProducts, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelProducts.setText("product names");
//				jCheckBoxProducts = new JCheckBox();
//				jPanel11.add(jCheckBoxProducts, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//				jCheckBoxProducts.setText("load product names for genes not encoding enzymes");
			}
			{
				jLabelReports = new JLabel();
				jPanel11.add(jLabelReports, new GridBagConstraints(1, 11, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

				jLabelReports.setText("output");
			}
			{
				ComboBoxModel<String> jComboBoxReportsModel = 
						new DefaultComboBoxModel<>(
								new String[] { "reports", "integration" });
				jComboBoxReports = new JComboBox<>();
				jPanel11.add(jComboBoxReports, new GridBagConstraints(1, 12, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jComboBoxReports.setModel(jComboBoxReportsModel);
			}

			jPanel12 = new JPanel();
			GridBagLayout jPanel12Layout = new GridBagLayout();
			jPanel12.setLayout(jPanel12Layout);
			jPanel12Layout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.0};
			jPanel12Layout.columnWidths = new int[] {3, 20, 7, 50};
			jPanel12Layout.rowWeights = new double[] {0.1};
			jPanel12Layout.rowHeights = new int[] {7};
			jPanel12.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

			jPanel1.add(jPanel12, new GridBagConstraints(0, 4, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			
//			{
//				jCheckFull = new JCheckBox();
//				jCheckFull.setSelected(true);
//				jPanel1.add(jCheckFull, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//				jCheckFull.setText("Integrate Full EC numbers");
//			}
//			{
//				jCheckPartial = new JCheckBox();
//				jCheckPartial.setSelected(true);
//				jPanel1.add(jCheckPartial, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//				jCheckPartial.setText("Integrate Partial EC numbers");
//			}

			JButton button1 = new JButton("ok");
			button1.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Ok.png")),0.1).resizeImageIcon());
			jPanel12.add(button1, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			button1.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent evt){
					
					try{
						ParamSpec[] paramsSpec = new ParamSpec[]{
								new ParamSpec("homologyDataContainer", Workspace.class, homologyDataContainer, null),
								new ParamSpec("integrationNames", IntegrationType.class, jComboBoxGenes.getSelectedItem(), null),
								new ParamSpec("integrationEnzymes", IntegrationType.class, jComboBoxEnzymes.getSelectedItem(), null),
								new ParamSpec("integrationProducts", IntegrationType.class, jComboBoxProducts.getSelectedItem(), null),
//								new ParamSpec("integrateFull", boolean.class, jCheckFull.isSelected(), null),
//								new ParamSpec("integratePartial", boolean.class, jCheckPartial.isSelected(), null),
//								new ParamSpec("processProteinNames", boolean.class, jCheckBoxProducts.isSelected(), null),
								new ParamSpec("repOrIntegration", String.class, jComboBoxReports.getSelectedItem(), null)
							};
							
							for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
								if (def.getID().equals("operations.ModelEnzymesIntegration.ID")){
									
									Workbench.getInstance().executeOperation(def, paramsSpec);
								}
							}
					}
					catch(Exception ex){
						ex.printStackTrace();
					}

					simpleFinish();
				}
			});

			JButton button2 = new JButton("cancel");
			button2.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Cancel.png")),0.1).resizeImageIcon());
			jPanel12.add(button2, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			button2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					simpleFinish();
				}
			});

		}
		this.setSize(400, 350);
		//} catch (Exception e) {e.printStackTrace();}
	}

	public void simpleFinish() {

		this.setVisible(false);
		this.dispose();
	}

}
