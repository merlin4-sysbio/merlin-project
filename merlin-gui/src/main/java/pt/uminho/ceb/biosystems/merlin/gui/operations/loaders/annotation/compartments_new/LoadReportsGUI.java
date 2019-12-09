package pt.uminho.ceb.biosystems.merlin.gui.operations.loaders.annotation.compartments_new;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.sing_group.gc4s.dialog.AbstractInputJDialog;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.combobox.ExtendedJComboBox;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.CreateImageIcon;


public class LoadReportsGUI extends AbstractInputJDialog implements InputGUI{

	private static final long serialVersionUID = 1L;
	private ParamsReceiver rec;
	protected Object project;
	private ExtendedJComboBox<String> tool;
	private Image merlin = new ImageIcon(getClass().getClassLoader().getResource("icons/merlin.png")).getImage();

	Map<String,String> operationsCompartments;


	private ExtendedJComboBox<String> models;


	public LoadReportsGUI (){

		super(new JFrame());

		this.setIconImage(merlin);

		//fill();
	}

	public String getDialogTitle() {
		return "Load reports";
	}

	public String getDescription() {
		return "Load compartments reports' to merlin.";
	}

	public JPanel getInputComponentsPane() {

		//		Enumerators.CompartmentsTool.;
		
		List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(WorkspaceAIB.class);                    
		
		String[] workspaces = new String[cl.size()];
		for (int i = 0; i < cl.size(); i++) {

			workspaces[i] = (cl.get(i).getName());
		}
		this.models = new ExtendedJComboBox<String>(workspaces);

		String[] toolsList = { "LocTree3", "WoLFPSORT", "PSortb3" };
		
		this.tool = new ExtendedJComboBox<String>(toolsList);

		InputParameter[] inPar = getInputParameters();
		return new InputParametersPanel(inPar);
	}



	@Override
	protected Component getButtonsPane() {
		final JPanel buttonsPanel = new JPanel(new FlowLayout());

		okButton = new JButton("proceed");
		okButton.setEnabled(true);
		okButton.setToolTipText("proceed");
		okButton.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Ok.png")),0.1).resizeImageIcon());
		ActionListener listener= new ActionListener() {


			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();

				rec.paramsIntroduced(
						new ParamSpec[]{
								new ParamSpec("Workspace", String.class,models.getSelectedItem().toString(),null),
								new ParamSpec("Tool", String.class,tool.getSelectedItem().toString(),null)

						}
						);

			}
		};
		okButton.addActionListener(listener);

		cancelButton = new JButton("cancel");
		cancelButton.setToolTipText("cancel");
		cancelButton.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Cancel.png")),0.1).resizeImageIcon());
		cancelButton.addActionListener(event -> {

			//String[] options = new String[2];
			//options[0] = "yes";
			//options[1] = "no";

			//int result = CustomGUI.stopQuestion("cancel confirmation", "are you sure you want to cancel the operation?", options);

			//if(result == 0) {
			canceled = true;
			dispose();
			//}

		});


		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);

		getRootPane().setDefaultButton(okButton);
		InputMap im = okButton.getInputMap();
		im.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		im.put(KeyStroke.getKeyStroke("released ENTER"), "released");

		return buttonsPanel;


	}

	private InputParameter[] getInputParameters() {
		InputParameter[] parameters = new InputParameter[2];
		parameters[1] = 

				new InputParameter(
						"Tools", 
						tool, 
						"Select the tool"
						);

		parameters[0] = 

				new InputParameter(
						"Workspace", 
						models, 
						"Select the workspace"
						);

		return parameters;
	}


	@Override
	public void setVisible(boolean b) {
		this.pack();
		super.setVisible(b);
	}



	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		this.rec = arg0;
		this.setTitle(arg1.getName());
		this.setVisible(true);		
	}

	/* (non-Javadoc)
	 * @see es.uvigo.ei.aibench.workbench.InputGUI#onValidationError(java.lang.Throwable)
	 */
	public void onValidationError(Throwable arg0) {

		Workbench.getInstance().error(arg0);
	}

	@Override
	public void finish() {

	}



} 
