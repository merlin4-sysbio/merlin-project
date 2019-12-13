package pt.uminho.ceb.biosystems.merlin.gui.operations.workspace;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.CreateGenomeFile;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.utilities.Enumerators.FileExtensions;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SequenceType;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MerlinUtils;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.TimeLeftProgress;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelSequenceServices;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;


/**
 * @author amaromorais
 *
 */
@Operation(description="set the genome files")
public class SetGenomeFastaFiles {

	private WorkspaceAIB project=null;
	private File faaFastaFile, fnaFastaFile, genBankFile;
	private FileExtensions fileExtension;
	private String databaseName;
	private Long taxID;
	private boolean searchLocus = false;
	private TimeLeftProgress progress = new TimeLeftProgress();
	private AtomicBoolean cancel = new AtomicBoolean(false);
	private AtomicInteger querySize;
	private String message;
	private AtomicInteger counter = new AtomicInteger(0);
	private long startTime;
	
	
	// mudar order para o ultimo valor se for para ativar o filtro em baixo
	@Port(direction=Direction.INPUT, name="select Workspace", validateMethod = "checkProject", order=5)
	public void setProject(WorkspaceAIB project) {

	}
	
	/**
	 * @param project
	 * @throws Exception 
	 */
	public void checkProject(WorkspaceAIB project) throws Exception {


        this.project = project;
        if(this.project==null) {
            throw new IllegalArgumentException("please select a workspace.");
        }
        else {
            this.databaseName = project.getName();
            this.taxID = this.project.getTaxonomyID();
        }
        boolean exists = false;
        // Verificar se este verificador esta a funcionar corretamente
        List<SequenceType> seqTypes = this.fileExtension.getSequenceType();
        for(SequenceType seqType : seqTypes)
            if(ModelSequenceServices.checkGenomeSequences(databaseName, seqType))
                exists =  true;
        if(exists)
            throw new IllegalArgumentException("merlin detected that you already have a genome available in the workspace. please clean 'all information' (check advanced options) in the workspace to load a new genome.");
	}
	
	@Port(direction=Direction.INPUT, name="search locus tags",defaultValue="false", description="automatically search locus tags in Uniprot/NCBI if .gbff file not available.",order=2)
	public void setEValueAutoSelection(boolean searchLocus){
		
		this.searchLocus = searchLocus;
	}
	
	@Port(direction=Direction.INPUT, validateMethod = "checkFileExtension",name="file type", description="",order=3)
	public void setExtensions(FileExtensions fileExtension) {
		this.fileExtension = fileExtension;
	}
	
	/**
	 * @param fileExtension
	 */
	public void checkFileExtension(FileExtensions fileExtension){
		this.fileExtension = fileExtension;
	}
	
	
	@Port(direction=Direction.INPUT,validateMethod="checkFiles",name="file", description="path to file.",order=4)
	public void setFiles(File file) {
		
		try{
			
			System.out.println("file exte" );
			
			this.startTime = GregorianCalendar.getInstance().getTimeInMillis();

			this.progress.setTime(GregorianCalendar.getInstance().getTimeInMillis() - this.startTime, 0, 4, "uploading genes...");
			
			Map<String, String[]> genesData= new HashMap<>(), rRNAdata= new HashMap<>(), tRNAdata = new HashMap<>();
			Map<Integer, String[]> sequencesData = new HashMap<>();
			
			
			if(faaFastaFile != null) { 
				
				File destiny = new File(FileUtils.getWorkspaceTaxonomyFolderPath(this.databaseName, this.taxID).concat(FileExtensions.PROTEIN_FAA.getName()));
				
				Files.copy(faaFastaFile.toPath(), destiny.toPath() , StandardCopyOption.REPLACE_EXISTING);
				
				CreateGenomeFile.createGenomeFileFromFasta(this.databaseName, this.taxID, faaFastaFile, this.fileExtension, genesData);
			}
			else if(fnaFastaFile != null) {
				
				if(fileExtension.equals(FileExtensions.RNA_FROM_GENOMIC)){
					
					rRNAdata = new HashMap<>();
					tRNAdata = new HashMap<>();
					
					CreateGenomeFile.divideAndBuildRNAGenomicFastaFile(this.databaseName, this.taxID, fnaFastaFile);
					File tRNA_File = new File(FileUtils.getWorkspaceTaxonomyFolderPath(this.databaseName, this.taxID) + "trna_from_genomic.fna");
					File rRNA_File = new File(FileUtils.getWorkspaceTaxonomyFolderPath(this.databaseName, this.taxID) + "rrna_from_genomic.fna");
					CreateGenomeFile.createGenomeFileFromFasta(this.databaseName, this.taxID, tRNA_File, this.fileExtension, tRNAdata);
					CreateGenomeFile.createGenomeFileFromFasta(this.databaseName, this.taxID, rRNA_File, this.fileExtension, rRNAdata);
				}
				
				else {
					
					//CDS_FROM_GENOMIC AND GENOMIC_FNA
					CreateGenomeFile.createGenomeFileFromFasta(this.databaseName, this.taxID, fnaFastaFile, this.fileExtension, genesData);
				}
			}
			else if(genBankFile != null) {
				
				Path source, destiny = null;
				
				if(file.getAbsolutePath().endsWith(".gbff")) {
					
					source = file.toPath();
					destiny = new File(FileUtils.getWorkspaceTaxonomyFolderPath(this.databaseName, this.taxID).concat(FileExtensions.CUSTOM_GENBANK_FILE.getName()).concat(".gbff")).toPath();
				
					Files.copy(source, destiny, StandardCopyOption.REPLACE_EXISTING);
				}
				else {
					
					source = file.toPath();
					destiny = new File(FileUtils.getWorkspaceTaxonomyFolderPath(this.databaseName, this.taxID).concat(FileExtensions.CUSTOM_GENBANK_FILE.getName()).concat(".gpff")).toPath();
				
					Files.copy(source, destiny, StandardCopyOption.REPLACE_EXISTING);
				}
			}
			
			//LOAD GENOME SEQUENCES INTO DATABASE
			if(genesData!=null && !genesData.isEmpty()) {
				
				boolean toLoadGenes = true;
				
				SequenceType seqType;
				
				if(this.fileExtension.equals(FileExtensions.PROTEIN_FAA))
					seqType = SequenceType.PROTEIN;
				else
					seqType = SequenceType.CDS_DNA;
				
				Map<String, Integer> sequenceIDs = ModelGenesServices.getGeneIDsByQuery(databaseName);
				
				this.querySize = new AtomicInteger(genesData.size()); 
				
				int counter = 0;
				
				//Load genes
				for(String sequenceID : genesData.keySet()){
					
					this.progress.setTime(GregorianCalendar.getInstance().getTimeInMillis() - this.startTime, counter, this.querySize.get(), "uploading genes to workspace...");

					String[] seqInfo = genesData.get(sequenceID);
					Integer geneID;
							
					if(toLoadGenes) {
						
						geneID = ModelGenesServices.loadGene(new Pair<String, String>(seqInfo[0], seqInfo[3]), sequenceID, sequenceIDs, "fasta file", project.getName());
//						geneID = ModelAPI.loadGene(new Pair<String, String>(seqInfo[0], seqInfo[3]), sequenceID, sequenceIDs, stmt, "fasta file");
					}
					else {
						geneID = ModelGenesServices.getGeneByQuery(this.databaseName, sequenceID).getIdGene();
					}

					sequencesData.put(geneID, Arrays.copyOfRange(seqInfo, 1, seqInfo.length));
					
					counter++;
				}
				
				this.progress.setTime(GregorianCalendar.getInstance().getTimeInMillis() - this.startTime, counter, this.querySize.get(), "uploading genes to workspace...");

				
				this.querySize = new AtomicInteger(sequencesData.size()); 
				
				counter = 0;		//resolver problema do contador de upload das sequencias
				
				this.progress.setTime(GregorianCalendar.getInstance().getTimeInMillis() - this.startTime, counter, this.querySize.get(), "uploading sequences to workspace...");
				
				//Load sequences
				ModelSequenceServices.loadFastaSequences(this.databaseName, sequencesData, seqType);
				
				//Update genes locus_tags
				if(toLoadGenes) {
					
					try  {
						ParamSpec[] paramsSpec = new ParamSpec[]{
								new ParamSpec("searchLocusOnline", boolean.class, this.searchLocus, null),
								new ParamSpec("workspace", WorkspaceAIB.class, this.project, null),
						};

						for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations())
							if (def.getID().equals("operations.UpdateLocusTags.ID"))
								Workbench.getInstance().executeOperation(def, paramsSpec);
					}
					catch (Exception e) {

						Workbench.getInstance().error("error uploading gene locus tags!");
						e.printStackTrace();
					}

					project.setGenomeLocusTag(ModelGenesServices.getGenesLocusTagBySequenceId(this.databaseName));
				}
			}
			
			//RNA SEQUENCES
			else if(!rRNAdata.isEmpty() || !tRNAdata.isEmpty()){
				
				int i = 0;
				if(!rRNAdata.isEmpty()){

					for(String sequenceID : rRNAdata.keySet()){

						String[] seqInfo = rRNAdata.get(sequenceID);
						sequencesData.put(i, Arrays.copyOfRange(seqInfo, 1, seqInfo.length));
						i++;
					}

					//Load sequences
					ModelSequenceServices.loadFastaSequences(databaseName, sequencesData, SequenceType.RRNA);
				}

				sequencesData = new HashMap<>();

				if(!tRNAdata.isEmpty()){

					for(String sequenceID : tRNAdata.keySet()){

						String[] seqInfo = tRNAdata.get(sequenceID);
						sequencesData.put(i, Arrays.copyOfRange(seqInfo, 1, seqInfo.length));
						i++;
					}

					//Load sequences
					ModelSequenceServices.loadFastaSequences(databaseName, sequencesData, SequenceType.TRNA);
				}
			}
			
			if(fnaFastaFile!= null || faaFastaFile!=null)
				Workbench.getInstance().info("project's '"+ this.fileExtension.getName() +"' fasta file successfully added!");
			else
				Workbench.getInstance().info("project's genbank file successfully added");

			MerlinUtils.updateGeneView(project.getName());
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("error uploading fasta files!");
		} 
	}
		
	
	/**
	 * @param file
	 */
	public void checkFiles(File file){

		if(file == null || file.toString().isEmpty() || !file.isFile()) {

			throw new IllegalArgumentException("fasta file not set!");
		}
		else {

			this.faaFastaFile = null;
			this.fnaFastaFile = null;
			this.genBankFile = null;
			
			if(file.getAbsolutePath().endsWith(".faa")){
				if(fileExtension.equals(FileExtensions.PROTEIN_FAA))
					faaFastaFile = file;
				else
					throw new IllegalArgumentException("inserted file format ('.faa') does not match the selected file type '"+ this.fileExtension.getName() +"'!");
			}
			
			else if(file.getAbsolutePath().endsWith(".fna")){
				if(fileExtension.equals(FileExtensions.CDS_FROM_GENOMIC) || fileExtension.equals(FileExtensions.RNA_FROM_GENOMIC) || fileExtension.equals(FileExtensions.GENOMIC_FNA))
					fnaFastaFile = file;
				else
					throw new IllegalArgumentException("inserted file format ('.fna') does not match the selected file type '"+ this.fileExtension.getName() +"'!");
			}

			else if(file.getAbsolutePath().endsWith(".gbff") || file.getAbsolutePath().endsWith(".gpff")){
				if(fileExtension.equals(FileExtensions.CUSTOM_GENBANK_FILE))
					genBankFile = file;
				else
					throw new IllegalArgumentException("inserted file format ('.gbff'/'.gpff') does not match the selected file type '"+ this.fileExtension.getName() +"'!");
			}

			if(faaFastaFile == null && fnaFastaFile == null && genBankFile == null)
				throw new IllegalArgumentException("please insert a file with a valid format ('.faa', '.fna', '.gpff' or '.gbff')!");
		}
	}
	
	/**
	 * @return the progress
	 */
	@Progress(progressDialogTitle = "import genome", modal = false, workingLabel = "importing genome to workspace", preferredWidth = 400, preferredHeight=300)
	public TimeLeftProgress getProgress() {

		return progress;
	}

	/**
	 * @param cancel the cancel to set
	 */
	@Cancel
	public void setCancel() {

		progress.setTime(0, 0, 0);
		this.cancel.set(true);
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {

		this.progress.setTime(GregorianCalendar.getInstance().getTimeInMillis() - this.startTime, this.counter.get(), this.querySize.get(), message);
	}

	
//	/**
//	 * @return
//	 */
//	private boolean toReplaceGenes() {
//		
//		int i =CustomGUI.stopQuestion("load genome sequences", "Protein/DNA_cds genome sequences are already loaded for this organism.\n"
//				+ "do you want to replace them with the '"+ this.fileExtension.getName() +"' sequences?",
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
//			Workbench.getInstance().info("merlin has detected that there are genome sequences already present in your model's workspace.\n"
//					+ "Loading a new genome sequences file into your database will reset all information present in the workspace.\n"
//					+ "If you choose 'No' the sequences will still be loaded but will not replace the existing ones.");
//
//			return toReplaceGenes();
//		}
//	}

}
