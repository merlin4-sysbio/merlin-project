package pt.uminho.ceb.biosystems.merlin.gui.jpanels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.CreateGenomeFile;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.DocumentSummary;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.DocumentSummarySet;


/**
 * @author amaromorais
 *
 */
public class AssemblyPanel {

	private JPanel jPanel1, jPanel2;
	public static JComboBox<String> jComboBox1;
	//	private JButton jButton1;
	private JTextArea jTextArea1, jTextArea2;
	private JRadioButton jRadioButtonGenBank, jRadioButtonRefSeq;
	private JLabel jLabel1;
	public static boolean isGenBank = true;
	private String taxonomyID;
	public static DocumentSummary selectedDocSummary;




	public JPanel constructPanel(String taxID){
		this.taxonomyID = taxID;
		{
			jPanel1 = new JPanel();
			GridBagLayout jPanel1Layout = new GridBagLayout();
			jPanel1.setLayout(jPanel1Layout);
			jPanel1Layout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0};
			jPanel1Layout.columnWidths = new int[] {7, 7, 7, 7, 7};
			jPanel1Layout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.0};
			jPanel1Layout.rowHeights = new int[] {7, 10, 7, 7};


			jComboBox1 = new JComboBox<>();
			jPanel1.add(jComboBox1, new GridBagConstraints(1, 0, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

			jComboBox1.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					String assemblyName = jComboBox1.getSelectedItem().toString();

					if(jComboBox1.getSelectedItem().toString().equals("don't download")){
						jTextArea1.setVisible(false);
						jTextArea2.setVisible(false);
						updateTextArea(assemblyName);
					}
					else{
						updateTextArea(assemblyName);
						jTextArea1.setVisible(true);
						jTextArea2.setVisible(true);
					}
				}
			}); 


			//			@Override
			//			public void Item (ActionEvent e) {
			//				
			//				String assemblyName = jComboBox1.getSelectedItem().toString();
			//				DocumentSummarySet docSumSet = CreateGenomeFile.getESummaryFromNCBI(taxonomyID);
			//				DocumentSummary docSum = new DocumentSummary();
			//				
			//				for(int i=0 ; i < docSumSet.documentSummary.size(); i++){
			//					DocumentSummary docSummary = docSumSet.documentSummary.get(i);
			//					if(docSummary.assemblyName == assemblyName)
			//						docSum = docSummary;
			//				}
			//				String uid = docSum.uid;
			//				String speciesName = docSum.speciesName;
			//				String assemblyAccession = docSum.assemblyAccession;
			//				String lastupdateDate = docSum.lastupdateDate.substring(0,10);
			//				String accessionGenBank = docSum.accessionGenBank;
			//				String genBankStatus = docSum.propertyList.get(3);
			//				String accessionRefSeq = docSum.accessionRefSeq;
			//				String refSeqStatus = docSum.propertyList.get(4);
			//				
			//				jTextArea1.setText("uid: " + uid + "\n" + "assemblyAccession: " + assemblyAccession + "\n" + "accessionGeneBank: "
			//						+ accessionGenBank + "\n" + "accessionRefSeq: " + accessionRefSeq );
			//				jTextArea2.setText("speciesName: " + speciesName + "\n" +  "lastUpdateDate: " + lastupdateDate + "\n" 
			//						+ "genbankStatus: " + genBankStatus + "\n" + "refSeqStatus: " + refSeqStatus);
			//			}
			//		});
			//		
			jLabel1 = new JLabel();
			jLabel1.setText("Assembly Record");
			jPanel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 5, 6, 5), 0, 0));
			//		{
			//			jButton1 = new JButton();
			//			jPanel1.add(jButton1, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 5, 6, 5), 0, 0));
			//			jButton1.setText("view");
			//			jButton1.setToolTipText("view assembly record information");
			//			jButton1.setEnabled(true);
			////			jButton1.addActionListener(new ActionListener(){
			////				
			////				public void actionPerformed(ActionEvent arg0) {
			////					
			////				}
			////			});
			//		}
			//		
			jTextArea1 = new JTextArea();
			jTextArea1.setEditable(false);
			jPanel1.add(jTextArea1, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 5, 6, 5), 0, 0));

			jTextArea2 = new JTextArea();jTextArea1.setEditable(false);
			jTextArea2.setEditable(false);
			jPanel1.add(jTextArea2, new GridBagConstraints(2, 1, 2, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 5, 6, 5), 0, 0));

			jPanel2 = new JPanel();
			GridBagLayout jPanel2Layout = new GridBagLayout();
			jPanel2Layout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.0};
			jPanel2Layout.columnWidths = new int[] {3, 20, 7, 50};
			jPanel2Layout.rowWeights = new double[] {0.1};
			jPanel2Layout.rowHeights = new int[] {7};
			jPanel2.setLayout(jPanel2Layout);
			jPanel2.setBorder(BorderFactory.createTitledBorder("Files retriever"));

			jPanel1.add(jPanel2, new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

			{
				jRadioButtonGenBank = new JRadioButton();
				jPanel2.add(jRadioButtonGenBank, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jRadioButtonGenBank.setSelected(true);
				jRadioButtonGenBank.setText("GenBank");
				jRadioButtonGenBank.setToolTipText("get files from GenBank");
				jRadioButtonGenBank.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						jRadioButtonRefSeq.setSelected(false);
						isGenBank = true;
					}
				});
			}
			{
				jRadioButtonRefSeq = new JRadioButton();
				jPanel2.add(jRadioButtonRefSeq, new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jRadioButtonRefSeq.setSelected(false);
				jRadioButtonRefSeq.setText("RefSeq");
				jRadioButtonRefSeq.setToolTipText("get files from RefSeq");
				jRadioButtonRefSeq.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						
						jRadioButtonGenBank.setSelected(false);
						isGenBank = false;
					}
				});
			}
			ButtonGroup buttonGroup = new ButtonGroup();
			buttonGroup.add(jRadioButtonRefSeq);
			buttonGroup.add(jRadioButtonGenBank);
		}

		return jPanel1;
	}



	//	private void setComboBox(String taxID) {
	//		
	//		DefaultComboBoxModel<String> aNames = new DefaultComboBoxModel<>();
	//		
	//		DocumentSummarySet docSummaryset = CreateGenomeFile.getESummaryFromNCBI(taxID);
	//		List<String> assemblyNames = CreateGenomeFile.getAssemblyNames(docSummaryset);
	//		
	//		aNames = new DefaultComboBoxModel<>(assemblyNames.toArray(new String[assemblyNames.size()]));
	//		
	//		jComboBox1.setModel(aNames);
	//		jComboBox1.updateUI();
	//		
	//	}

	public void updateTextArea(String assemblyName){

		try {
			if(assemblyName.equals("don't download")){
				WorkspaceNewGUI.toDownload = false;
				jPanel2.setVisible(false);
				DownloadNcbiFilesGUI.toDownload = false;
			}

			else{
				jPanel2.setVisible(true);

				DocumentSummarySet docSumSet = CreateGenomeFile.getESummaryFromNCBI(taxonomyID);
				DocumentSummary docSum = new DocumentSummary();

				for(int i=0 ; i < docSumSet.documentSummary.size(); i++){
					DocumentSummary docSummary = docSumSet.documentSummary.get(i);

					if(docSummary.assemblyName.equals(assemblyName)) {

						for(String property : docSummary.propertyList) {

							if(property.contains("genbank")){

								if(docSummary.propertyList.get(docSummary.propertyList.indexOf(property)-1).equals("latest")) {

									docSum = docSummary;
								}
							}
						}
					}
				}

				selectedDocSummary = docSum;
				
				String uid = docSum.uid;
				String speciesName = docSum.speciesName;
				String assemblyAccession = docSum.assemblyAccession;
				String lastupdateDate = docSum.lastupdateDate.substring(0, 10);
				String accessionGenBank = docSum.accessionGenBank;
				String accessionRefSeq = docSum.accessionRefSeq;
				String submitter = docSum.submitter;
				String assemblyLevel = docSum.assemblyLevel;
				String genBankStatus = "n/a";
				String refSeqStatus = "n/a";

				for(String property : docSum.propertyList) {

					if(property.contains("genbank")){
						
						if(!genBankStatus.equals("latest_genbank")){

							genBankStatus = "outdated";

							if(property.equals("latest_genbank"))
								genBankStatus = property;
						}
					}

					if(property.contains("refseq")){

						if(!refSeqStatus.equals("latest_refseq")){

							refSeqStatus = "outdated";

							if(property.equals("latest_refseq"))
								refSeqStatus = property;
						}
					}
				}

				if(genBankStatus.equals("n/a"))
					accessionGenBank = "n/a";

				if(refSeqStatus.equals("n/a"))
					accessionRefSeq = "n/a";

				if(submitter == null){
					submitter = "n/a";
				}

				if(speciesName.length() > 22){
					speciesName = speciesName.substring(0, 22) + "...";
				}

				jTextArea1.setText("UID: " + uid + "\n" + "Assembly Accession: " + assemblyAccession + "\n" + "Accession GenBank: "
						+ accessionGenBank + "\n" + "Accession RefSeq: " + accessionRefSeq + "\n" + "Submitter: " + submitter );
				jTextArea2.setText("Species Name: " + speciesName + "\n" +  "Last Update: " + lastupdateDate + "\n" 
						+ "GenBank Status: " + genBankStatus + "\n" + "RefSeq Status: " + refSeqStatus + "\n" + "Assembly Level: " + assemblyLevel);

				jRadioButtonGenBank.setEnabled(true);
				jRadioButtonRefSeq.setEnabled(true);
				jRadioButtonGenBank.setSelected(true);

				if(!genBankStatus.contains("latest")){
					jRadioButtonGenBank.setEnabled(false);
					jRadioButtonGenBank.setSelected(false);
					jRadioButtonRefSeq.setSelected(true);
				}

				if(!refSeqStatus.contains("latest")){
					jRadioButtonRefSeq.setEnabled(false);
					jRadioButtonRefSeq.setSelected(false);
					jRadioButtonGenBank.setSelected(true);
				}
				WorkspaceNewGUI.toDownload = true;	
				DownloadNcbiFilesGUI.toDownload = true;
				docSum.genBankStatus = genBankStatus;
				docSum.refSeqStatus = refSeqStatus;
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

	public void setTaxonomyID(String taxID){

		this.taxonomyID = taxID;
	}

}
