package pt.uminho.ceb.biosystems.merlin.gui.jpanels;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import es.uvigo.ei.aibench.workbench.Workbench;

public class CustomGUI {

	/**
	 * @param title
	 * @param question
	 * @param options
	 * @return
	 */
	public static int stopQuestion(String title, String question, Object[] options) {
		
		int result = showOptionPane(title, question, options);
		return result;
	}

	/** Presents a option pane with the given title, question and options 
	 * 
	 * @param title
	 * @param question
	 * @param options
	 * @return
	 */
	private static int showOptionPane(String title, String question, Object[] options) {
		
		JOptionPane option_pane = new JOptionPane(question, JOptionPane.INFORMATION_MESSAGE);
		option_pane.setOptions(options);
		option_pane.setVisible(true);		
		
		//option_pane.setIcon(new ImageIcon("plugins_src/pt.uminho.di.anote/icons/messagebox_question.png"));
//		for(int i = 0; i<options.length;i++)
//		{
			
//		}
		JDialog dialog = option_pane.createDialog(Workbench.getInstance().getMainFrame(), title);
		dialog.setVisible(true);

		Object choice = option_pane.getValue();

		for(int i=0; i<options.length; i++)
			if(options[i].equals(choice))
				return i;

		return -1;       
	}
}
