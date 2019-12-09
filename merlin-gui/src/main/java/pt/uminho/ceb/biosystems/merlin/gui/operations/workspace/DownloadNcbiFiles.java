package pt.uminho.ceb.biosystems.merlin.gui.operations.workspace;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.CreateGenomeFile;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.DocumentSummary;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.utilities.Enumerators.FileExtensions;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SequenceType;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceDatabaseAIB;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelSequenceServices;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

@Operation(name="download NCBI files", description="download files from NCBI genBank or refSeq ftp")
public class DownloadNcbiFiles {

	private DocumentSummary docSum;
	private boolean isGenBank;
	private String workspaceName;
	private Long taxID;
//	private WorkspaceAIB workspace;
	
	
	/**
	 * @param databaseAccess
	 */
	@Port(direction=Direction.INPUT, name="workspace", order=1)
	public void setDatabase(String workspaceName) {
		
		this.workspaceName = workspaceName;
	}
	
	/**
	 * @param docSummary
	 */
	@Port(direction=Direction.INPUT, name="taxonomyID", order=2)
	public void setTaxonomyID(Long taxonomyID) {
		this.taxID = taxonomyID;
	}
	
	
	/**
	 * @param docSummary
	 */
	@Port(direction=Direction.INPUT, name="docSummary", order=3)
	public void setDocumentSummary(DocumentSummary docSummary) {
		this.docSum = docSummary;
	}
	
	
	/**
	 * @param isGenBank
	 */
	@Port(direction=Direction.INPUT, name="isGenBank", order=4)
	public void startDownload(boolean isGenBank) {
		this.isGenBank = isGenBank;
		
		try {
			
			
			ArrayList<String> ftpUrlInfo;
			String ftpSource = null;
			
			if(isGenBank)
				ftpSource = "genBank";
			else
				ftpSource = "refSeq";
			
			CreateGenomeFile.saveAssemblyRecordInfo(docSum, workspaceName);
			
			ftpUrlInfo = CreateGenomeFile.getFtpURLFromAssemblyUID(docSum, this.isGenBank);
			CreateGenomeFile.getFilesFromFtpURL(ftpUrlInfo, workspaceName);
			
			File proteinFastaFile = new File(FileUtils.getWorkspaceTaxonomyFolderPath(workspaceName, taxID) + FileExtensions.PROTEIN_FAA.getName());
			File cdsFastaFile = new File(FileUtils.getWorkspaceTaxonomyFolderPath(workspaceName, taxID) + FileExtensions.CDS_FROM_GENOMIC.getName());
			File rnaFastaFile = new File(FileUtils.getWorkspaceTaxonomyFolderPath(workspaceName, taxID) + FileExtensions.RNA_FROM_GENOMIC.getName());
			
			Map<String, String[]> proteinData = new HashMap<>(), mRnaData = new HashMap<>(), rRnaData= new HashMap<>(), tRnaData = new HashMap<>();
			Map<Integer, String[]> sequencesData = new HashMap<>();
			
			
			//Genome files
			if(!proteinFastaFile.exists() && !cdsFastaFile.exists()){
				
				Workbench.getInstance().warn("unable to download'.faa' and '.fna' " + ftpSource + " ftp files, "
						+ "please import files manually");
			}
			else if (!cdsFastaFile.exists()){
				
				CreateGenomeFile.createGenomeFileFromFasta(workspaceName, taxID, proteinFastaFile, FileExtensions.PROTEIN_FAA, proteinData);
				
				Workbench.getInstance().info("unable to download '.fna' " + ftpSource + " file");
			}
			else if (!proteinFastaFile.exists()){

				CreateGenomeFile.createGenomeFileFromFasta(workspaceName, taxID, cdsFastaFile, FileExtensions.CDS_FROM_GENOMIC, mRnaData);
				
				Workbench.getInstance().info("unable to download '.faa' " + ftpSource + " file");
			}
			else {
				CreateGenomeFile.createGenomeFileFromFasta(workspaceName, taxID, proteinFastaFile, FileExtensions.PROTEIN_FAA, proteinData);
				CreateGenomeFile.createGenomeFileFromFasta(workspaceName, taxID, cdsFastaFile, FileExtensions.CDS_FROM_GENOMIC, mRnaData);
				
				if(rnaFastaFile.exists())
					Workbench.getInstance().info("all " + ftpSource + " ftp files successfuly downloaded");				
			}
			
			//RNA files
			if(rnaFastaFile.exists()){
				
				File tRNA_File = new File(FileUtils.getWorkspaceTaxonomyFolderPath(this.workspaceName, this.taxID) + "trna_from_genomic.fna");
				File rRNA_File = new File(FileUtils.getWorkspaceTaxonomyFolderPath(this.workspaceName, this.taxID) + "rrna_from_genomic.fna");
				CreateGenomeFile.createGenomeFileFromFasta(this.workspaceName, this.taxID, tRNA_File, FileExtensions.RNA_FROM_GENOMIC, tRnaData);
				CreateGenomeFile.createGenomeFileFromFasta(this.workspaceName, this.taxID, rRNA_File, FileExtensions.RNA_FROM_GENOMIC, rRnaData);
				
			}
			else {
				
				Workbench.getInstance().warn("unable to download " + FileExtensions.RNA_FROM_GENOMIC.getName() + " file, "
						+ "from " + ftpSource + " ftp, please import file manually");

			}
			
			// update projects table in database
			
			ProjectServices.updateProjectsByGenomeID(this.workspaceName, this.taxID, this.docSum.toHashMap());
			
			Map<String, Integer> queryIDs = ModelGenesServices.getGeneIDsByQuery(workspaceName);
			
			if(queryIDs.isEmpty()){
				
				boolean updateLocus = false;
				
//				ModelAPI.cleanGeneAndSequenceTables(stmt);

				if(proteinData!=null && !proteinData.isEmpty()){

					Map<Integer, String[]> cdsSequencesData = new HashMap<>();
					
//					Map<String, Integer> sequenceIDs = ModelAPI.getGeneIds(stmt);
					
					//Load genes
					for(String sequenceID : proteinData.keySet()){

						String[] seqInfo = proteinData.get(sequenceID);
						
						Integer geneID = ModelGenesServices.loadGene(new Pair<String, String>(seqInfo[0], seqInfo[3]), sequenceID, queryIDs, "fasta file", workspaceName);

						sequencesData.put(geneID, Arrays.copyOfRange(seqInfo, 1, seqInfo.length));

						if(mRnaData.get(sequenceID)!=null)
							cdsSequencesData.put(geneID, Arrays.copyOfRange(mRnaData.get(sequenceID), 1, mRnaData.get(sequenceID).length));
						
					}

					//Load sequences
					ModelSequenceServices.loadFastaSequences(this.workspaceName, sequencesData, SequenceType.PROTEIN);
					
					if(!cdsSequencesData.isEmpty()){
						
						ModelSequenceServices.loadFastaSequences(this.workspaceName, cdsSequencesData, SequenceType.CDS_DNA);
					}

					updateLocus = true;

				}
				
				
				//Loading mRNA
				if(!mRnaData.isEmpty() && mRnaData!=null){

					for(String sequenceID : mRnaData.keySet()){

						String[] seqInfo = mRnaData.get(sequenceID);

//						Integer geneID = ModelAPI.loadGene(new Pair<String, String>(seqInfo[0], seqInfo[3]), sequenceID, queryIDs, stmt, "fasta file");
						Integer geneID = ModelGenesServices.loadGene(new Pair<String, String>(seqInfo[0], seqInfo[3]), sequenceID, queryIDs, "fasta file", workspaceName);
						
						sequencesData.put(geneID, Arrays.copyOfRange(seqInfo, 1, seqInfo.length));
					}

					//Load sequences
					ModelSequenceServices.loadFastaSequences(this.workspaceName, sequencesData, SequenceType.CDS_DNA);

					updateLocus = true;

				}

				//Loading rRNA and tRNA
				if(!rRnaData.isEmpty() || !tRnaData.isEmpty()){
					
					sequencesData = new HashMap<>();

					int i = 0;

					if(!rRnaData.isEmpty()){

						//Load genes
						for(String sequenceID : rRnaData.keySet()){

							String[] seqInfo = rRnaData.get(sequenceID);

							sequencesData.put(i, Arrays.copyOfRange(seqInfo, 1, seqInfo.length));

							i++;
						}

						//Load sequences
						ModelSequenceServices.loadFastaSequences(this.workspaceName, sequencesData, SequenceType.RRNA);
					}

					sequencesData = new HashMap<>();

					if(!tRnaData.isEmpty()){

						//Load genes
						for(String sequenceID : tRnaData.keySet()){

							String[] seqInfo = tRnaData.get(sequenceID);

							sequencesData.put(i, Arrays.copyOfRange(seqInfo, 1, seqInfo.length));

							i++;
						}

						//Load sequences
						ModelSequenceServices.loadFastaSequences(this.workspaceName, sequencesData, SequenceType.TRNA);
					}

				}
				
				if(updateLocus) {
					
					try  {
						WorkspaceAIB auxWorkspaceAIB = new WorkspaceAIB(new WorkspaceDatabaseAIB(this.workspaceName));
						auxWorkspaceAIB.setTaxonomyID(this.taxID);
						
						ParamSpec[] paramsSpec = new ParamSpec[]{
								new ParamSpec("searchLocusOnline", boolean.class, true, null),
								new ParamSpec("workspace", WorkspaceAIB.class, auxWorkspaceAIB, null),
						};

						for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()) {

							if (def.getID().equals("operations.UpdateLocusTags.ID")) {

								Workbench.getInstance().executeOperation(def, paramsSpec);
							}
						}
					}
					catch (Exception e) {

						Workbench.getInstance().error("error uploading genes locus tag information");
						e.printStackTrace();
					}
					
					
					
				}

			}
		}
		catch (Exception e) {

			e.printStackTrace();
		}
	}

//
//	/**
//	 * @return
//	 */
//	private boolean replaceGenomeData() {
//		
//		int i =CustomGUI.stopQuestion("Replace genome sequences", "Genome sequences for this organism are already loaded into database.\n"
//				+ "Do you want to rewrite them and load the newly downloaded sequences?",
//				new String[]{"Yes", "No", "Info"});
//
//		if(i<2) {
//
//			switch (i)
//			{
//			case 0:return true;
//			case 1: return false;
//			default:return true;
//			}
//		}
//		else{
//			Workbench.getInstance().info("merlin has detected that there are genome sequences already present in your model's database.\n"
//					+ "If you accept to load the newly downloaded genome sequences into your database, the current ones will be deleted.\n"
//					+ "For further information please contact us to support@merlin-sysbio.org");
//
//			return replaceGenomeData();
//		}
//	}
//	
//	
//	/**
//	 * @return
//	 */
//	private boolean loadGenomeData() {
//		
//		int i =CustomGUI.stopQuestion("Load genome sequences", "Do you wish to load the genome sequences present in the downloaded files into database?",
//				new String[]{"Yes", "No", "Info"});
//
//		if(i<2) {
//
//			switch (i)
//			{
//			case 0:return true;
//			case 1: return false;
//			default:return true;
//			}
//		}
//		else{
//			Workbench.getInstance().info("merlin has detected that there are no genome sequences present in your model's database. Thus,\n"
//					+ "if you click option 'yes' the newly downloaded genome sequences will be loaded into the database. This can preventing\n"
//					+ "undesired issues inherent of fasta file’s deletion or alteration. Furthermore, you can replace these sequences anytime\n"
//					+ "you want by using 'import genome' operation available at 'workspace'> 'import genome'.\n"
//					+ "For further information please contact us to support@merlin-sysbio.org");
//
//			return loadGenomeData();
//		}
//	}
	
}
