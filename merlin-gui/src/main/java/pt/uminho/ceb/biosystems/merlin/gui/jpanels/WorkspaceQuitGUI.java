package pt.uminho.ceb.biosystems.merlin.gui.jpanels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.ei.aibench.Launcher;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.uniprot.UniProtAPI;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.CreateImageIcon;


/**
 * @author ODias
 *
 */
public class WorkspaceQuitGUI extends JDialog implements InputGUI{

	/**
	 * 
	 */
	final static Logger logger = LoggerFactory.getLogger(WorkspaceQuitGUI.class);
	private static final long serialVersionUID = 1L;
	private JLabel questionLabel;
	private JButton noButton;
	private JButton yesButton;
//	private JLabel questionLabel2;

	public WorkspaceQuitGUI() {
		
		super(Workbench.getInstance().getMainFrame());
		Utilities.centerOnOwner(this);
	}

	/* (non-Javadoc)
	 * @see es.uvigo.ei.aibench.workbench.InputGUI#init(es.uvigo.ei.aibench.workbench.ParamsReceiver, es.uvigo.ei.aibench.core.operation.OperationDefinition)
	 */
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {

		this.setTitle("quit");
		this.setIconImage((new ImageIcon(getClass().getClassLoader().getResource("icons/merlin.png"))).getImage());
		this.setVisible(true);		
		this.setAlwaysOnTop(true);
		this.toFront();
		initGUI();
	}

	public void onValidationError(Throwable arg0) {
		Workbench.getInstance().error(arg0);
	}

	public void initGUI() {
		
		GridBagLayout quitConfirmationDialogLayout = new GridBagLayout();
		quitConfirmationDialogLayout.rowWeights = new double[] {0.1, 1, 0.1, 1, 0.1, 1, 0.1};
		quitConfirmationDialogLayout.rowHeights = new int[] { 7, 7, 7, 7, 7, 7, 7};
		quitConfirmationDialogLayout.columnWeights = new double[] {0.1, 1, 0.1, 1, 0.1};
		quitConfirmationDialogLayout.columnWidths = new int[] { 7, 7, 7 , 7, 7};
		getContentPane().setLayout(quitConfirmationDialogLayout);
		{
			yesButton = new JButton();
			getContentPane().add(yesButton, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
			yesButton.setText("yes");
			yesButton.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/LogOut.png")),0.1).resizeImageIcon());
			yesButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					closeAll(0);
				}
			});
		}
		{
			noButton = new JButton();
			getContentPane().add(noButton, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
			noButton.setText("no");
			noButton.setIcon(new CreateImageIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Cancel.png")),0.1).resizeImageIcon());
			noButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					finish();
				}
			});
		}
		{
			questionLabel = new JLabel();
			this.getContentPane().add(questionLabel, new GridBagConstraints(1, 1,3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			questionLabel.setText("are you sure you want to quit?");
		}
		{
			this.setEnabled(true);
			Utilities.centerOnOwner(this);
			this.setTitle("quit confirmation");
			this.setSize(248, 132);
			this.setModal(true); //SEGURA A IMAGEM
//			this.setVisible(true);
//			System.out.println("thds "+this);
		}
	}

	public void finish() {

		this.setVisible(false);
		this.dispose();	
	}

	private void closeAll(int j){
		
		Workbench.getInstance().getMainFrame().dispose();
		new Thread(){ //killer thread
			public void run(){
				Launcher.getPluginEngine().shutdown();
				System.exit(j);
			}
		}.start();
		
		UniProtAPI.stopUniProtService();
		finish();
		System.exit(j);
	}
}

