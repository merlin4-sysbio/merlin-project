package pt.uminho.ceb.biosystems.merlin.gui.operations.annotation.enzymes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.enzymes.AnnotationEnzymesHomologuesData;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.BlastProgram;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.BlastSource;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.FileExtension;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SequenceType;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.jpanels.CustomGUI;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MerlinUtils;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MyFileFilter;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.TimeLeftProgress;
import pt.uminho.ceb.biosystems.merlin.processes.annotation.remote.blast.BlastReportsLoader;
import pt.uminho.ceb.biosystems.merlin.services.annotation.AnnotationEnzymesServices;
import pt.uminho.ceb.biosystems.merlin.services.annotation.loaders.LoadSimilarityResultstoDatabase;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelSequenceServices;

/**
 * @author Antonio Dias and Oscar Dias
 *
 */
@Operation(name="load Blast reports", description="load local BLAST reports")
public class HomologyReportLoader implements PropertyChangeListener{

	final static Logger logger = LoggerFactory.getLogger(HomologyReportLoader.class);

	private File file;
	private TimeLeftProgress progress = new TimeLeftProgress();
	private AtomicBoolean cancel;
	private BlastSource blastSource;
	private BlastProgram blastProgram;
	private FileExtension extension;
	private ConcurrentLinkedQueue<AnnotationEnzymesHomologuesData> resultsList;
	private long startTime;
	private BlastReportsLoader blastReport ;
	private WorkspaceAIB project;
	private int sequencesSize;

	/**
	 * @param file
	 */
	@Port(direction=Direction.INPUT, name="files", description = "select folder containing BLAST results",order=1)
	public void setFileName(File file){
		this.file=file;
	}

	/**
	 * @param fileExtension
	 */
	@Port(direction=Direction.INPUT, name="extension", description = "set extension of the BLAST results files", defaultValue="TXT",order=2)
	public void fileNameExtension(FileExtension fileExtension) {

		this.extension = fileExtension;
	}

	/**
	 * @param blastSource
	 */
	@Port(direction=Direction.INPUT, name="source", description = "select file origin",order=3)
	public void setSource(BlastSource blastSource){
		this.blastSource = blastSource;
	}


	@Port(direction=Direction.INPUT, name="type", description = "BLAST type", order=4)
	public void setType(BlastProgram blastProgram){
		this.blastProgram = blastProgram;
	}

	/**
	 * @param project
	 * @throws Exception 
	 */
	@Port(direction=Direction.INPUT, name="workspace",description="select Project",validateMethod="checkProject", order=5)
	public void setProject(WorkspaceAIB project) throws Exception {

		String[] orgData = new String[2];
		orgData[0] = project.getOrganismName();
		orgData[1] = project.getOrganismLineage();
		String[] organismTaxa = orgData;

		this.cancel = new AtomicBoolean(false);
		this.startTime = GregorianCalendar.getInstance().getTimeInMillis();

		ConcurrentHashMap<String,String[]> taxonomyMap = new ConcurrentHashMap<>();
		taxonomyMap.put(project.getTaxonomyID()+"", organismTaxa);
		ConcurrentHashMap<String, Boolean> uniprotStar = new ConcurrentHashMap<>();
		ConcurrentLinkedQueue<String> existingGenes = new ConcurrentLinkedQueue<> (AnnotationEnzymesServices.getGenesFromDatabase(this.project.getName(),
				this.blastProgram.toString(), true));

		SequenceType seqType = SequenceType.PROTEIN;
		if(this.blastProgram.equals(BlastProgram.blastx))
			seqType = SequenceType.GENOMIC_DNA;

		Map<String, AbstractSequence<?>> sequences = ModelSequenceServices.getGenomeFromDatabase(project.getName(), seqType);

		FileFilter filter = new MyFileFilter(this.extension.toString().toLowerCase());

		if(!this.file.isDirectory())
			file = new File(this.file.getParent().toString());

		File[] fileArray = file.listFiles(filter);
		ConcurrentLinkedQueue<File> outFiles = new ConcurrentLinkedQueue<>(Arrays.asList(fileArray));

		this.sequencesSize = outFiles.size();
		AtomicInteger errorCounter = new AtomicInteger(0);
		AtomicInteger counter = new AtomicInteger(0);

		int threadsNumber=0;
		int numberOfCores = Runtime.getRuntime().availableProcessors()*4;
		List<Thread> threads = new ArrayList<Thread>();
		ArrayList<Runnable> runnables = new ArrayList<Runnable>();

		if(sequencesSize<numberOfCores)
			threadsNumber=outFiles.size();
		else
			threadsNumber=numberOfCores;

		for(int i=0; i<threadsNumber; i++) {		

			blastReport = new BlastReportsLoader(this.extension.toString().toLowerCase(), existingGenes, organismTaxa, 
					project.getTaxonomyID(), blastProgram, blastSource, outFiles, sequences, 
					taxonomyMap, uniprotStar, this.cancel, errorCounter, counter, resultsList);

			Thread thread = new Thread(blastReport);
			runnables.add(blastReport);
			threads.add(thread);
			logger.info("Start "+i);
			thread.start();
		}

		for(Thread thread :threads)
			thread.join();

		if(errorCounter.get()>0) {

			Workbench.getInstance().error("An error occurred when performing the operation!");
		}
		else {

			MerlinUtils.updateEnzymesAnnotationView(project.getName());
			Workbench.getInstance().info("Blast report successfully loaded.");
		}

	}

	/**
	 * @return
	 */
	@Progress
	public TimeLeftProgress getProgress() {

		return this.progress;
	}

	/**
	 * 
	 */

	@Cancel
	public void cancel(){
		String[] options = new String[2];
		options[0]="yes";
		options[1]="no";

		int result=CustomGUI.stopQuestion("Cancel confirmation", "Are you sure you want to cancel the operation?", options);

		if(result==0) {
			this.progress.setTime(0,1,1);
			this.cancel.set(true);

		}

	}

	/**
	 * @param project
	 */
	public void checkProject(WorkspaceAIB project) {

		if(project == null)
			throw new IllegalArgumentException("No ProjectGUISelected!");

		else{

			this.project = project;

			if(project.getTaxonomyID()<0)
				throw new IllegalArgumentException("Please enter the taxonomic identification from NCBI taxonomy.");

		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if(evt.getPropertyName().equalsIgnoreCase("sequencesCounter")) {

			int sequencesCounter =	(int) evt.getNewValue();			
			this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), sequencesCounter, this.sequencesSize);
		}
		else if(evt.getPropertyName().equalsIgnoreCase("resultsList")) {

			try {
				while(this.resultsList.size()>0) {

					LoadSimilarityResultstoDatabase lbr = new LoadSimilarityResultstoDatabase(this.project.getName(),this.resultsList.poll(), this.cancel);
					lbr.loadData(project.getName());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
			logger.warn("Property {} not being processed!", evt.getPropertyName());
	}

}
