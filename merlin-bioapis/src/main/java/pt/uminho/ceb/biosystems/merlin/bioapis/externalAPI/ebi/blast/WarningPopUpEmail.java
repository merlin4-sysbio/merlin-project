package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.blast;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;


public class WarningPopUpEmail extends javax.swing.JDialog{
	

		/**
		 * 
		 */
		private static final long serialVersionUID = -7134391317104039643L;

		public WarningPopUpEmail() {


			super();
			this.setLocationRelativeTo(null);
			initGUI();
			this.setVisible(true);		
			this.setAlwaysOnTop(true);
			this.toFront();
		}

		public void initGUI() {



			this.setModal(true);
			JPanel jPanel1;
			{
				jPanel1 = new JPanel();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				GridBagLayout jPanel1Layout = new GridBagLayout();
				jPanel1Layout.columnWeights = new double[] {0.0, 0.1, 0.0};
				jPanel1Layout.columnWidths = new int[] {7, 7, 7};
				jPanel1Layout.rowWeights = new double[] {0.1, 0.0, 0.1};
				jPanel1Layout.rowHeights = new int[] {7, 7, 7};
				jPanel1.setLayout(jPanel1Layout);
			}

			JPanel jPanel11;
			{
				jPanel11 = new JPanel();
				GridBagLayout jPanel11Layout = new GridBagLayout();
				jPanel11.setLayout(jPanel11Layout);
				jPanel11Layout.columnWeights = new double[] {0.0, 0.1, 0.0};
				jPanel11Layout.columnWidths = new int[] {7, 7, 7};
				jPanel11Layout.rowWeights = new double[] {0.0, 0.1, 0.0};
				jPanel11Layout.rowHeights = new int[] {7, 7, 7};

				jPanel1.add(jPanel11, new GridBagConstraints(0, 0, 3, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}


			JTextArea textfield = new JTextArea("\nYour email is invalid, please introduce a valid one.\n"
					+ ""
					
					);
			
			// give the textfield the impression it's a label
			textfield.setBorder(jPanel11.getBorder());
			textfield.setEditable(false);
			textfield.setForeground(jPanel11.getForeground());
			textfield.setFont(UIManager.getFont("Label.font"));
			textfield.setBackground(jPanel11.getBackground());
			textfield.setVisible(true);

			jPanel11.add(textfield, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

			this.setSize(480, 250);
		}


		public void finish() {

			this.setVisible(false);
			this.dispose();
		}
	}


