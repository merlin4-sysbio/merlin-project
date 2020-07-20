package pt.uminho.ceb.biosystems.merlin.alignments.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.JAXBContext;

import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.alignments.core.ModelMerge.BlastAlignment;
import pt.uminho.ceb.biosystems.merlin.alignments.core.ModelMerge.ModelAlignments;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.CreateGenomeFile;
import pt.uminho.ceb.biosystems.merlin.core.containers.alignment.AlignmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.AlignmentPurpose;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.AlignmentScoreType;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.Method;
import pt.uminho.ceb.biosystems.merlin.utilities.blast.ncbi_blastparser.BlastOutput;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

/**
 * @author ODias
 *
 */
public class RunSimilaritySearch implements PropertyChangeListener {

	private Map<String, AbstractSequence<?>> staticGenesSet;
	private AtomicBoolean cancel;
	private AtomicInteger counter;
	private AtomicInteger querySize;
	private double similarity_threshold;
	private Method method;
	private ConcurrentHashMap<String, AbstractSequence<?>> querySequences;
	private List<String> annotatedGenes;
	private ConcurrentLinkedQueue<String> sequencesWithoutSimilarities;
	private String ec_number;
	private Map<String, Set<Integer>> modules;
	private Map<String, Set<String>> closestOrthologs;
	private int referenceTaxonomyScore;
	private Map<String, Integer> kegg_taxonomy_scores;
	private double referenceTaxonomyThreshold;
	private boolean compareToFullGenome;
	private AlignmentScoreType alignmentScoreType;
//	private String tcdbFastaFilePath;
	private String subjectFastaFilePath;
	private boolean gapsIdentification;
	private String workspaceTaxonomyFolderPath;
	private PropertyChangeSupport changes;
	
	final static Logger logger = LoggerFactory.getLogger(RunSimilaritySearch.class);


	/**
	 * Run similarity searches constructor.
	 * 
	 * @param dbAccess
	 * @param staticGenesSet
	 * @param minimum_number_of_helices
	 * @param similarity_threshold
	 * @param method
	 * @param querySequences
	 * @param cancel
	 * @param querySize
	 * @param counter
	 * @param project_id
	 * @param alignmentScoreType
	 * @throws Exception
	 */
	public RunSimilaritySearch(Map<String, AbstractSequence<?>> staticGenesSet, double similarity_threshold, Method method, ConcurrentHashMap<String, AbstractSequence<?>> querySequences, 
			AtomicBoolean cancel, AtomicInteger querySize, AtomicInteger counter, AlignmentScoreType alignmentScoreType) throws Exception {

		this.changes = new PropertyChangeSupport(this);
		
		this.setCounter(counter);
		this.setQuerySize(querySize);
		this.setCancel(cancel);
		this.staticGenesSet = staticGenesSet;
		this.similarity_threshold = similarity_threshold;
		this.method = method; 
		this.querySequences = querySequences;
		this.sequencesWithoutSimilarities = null;
		this.alignmentScoreType = alignmentScoreType;
		
		this.gapsIdentification = false;
	}
	
	///////////////////////////////////
	
	/**
	 * Run the transport similarity searches.
	 * If some threshold parameters were null, this method use the default values.
	 * 
	 * Default values: evalueThreshold(1E-6), bitScoreThreshold(50), queryCoverageThreshold(0.80)
	 * 
	 * @param isTransportersSearch
	 * @param eValueThreshold 
	 * @param bitScoreThreshold
	 * @param queryCoverageThreshold
	 * @return
	 * @throws Exception
	 */
	public ConcurrentLinkedQueue<AlignmentContainer> runBlastSearch(boolean isTransportersSearch, Double eValueThreshold, Double bitScoreThreshold, 
			Double queryCoverageThreshold, Double targetCoverageThreshold) throws Exception {
		
		List<Thread> threads = new ArrayList<Thread>();
//		ConcurrentLinkedQueue<String> queryArray = new ConcurrentLinkedQueue<String>(this.querySequences.keySet());
		int numberOfCores = Runtime.getRuntime().availableProcessors();
		//int numberOfCores = new Double(Runtime.getRuntime().availableProcessors()*1.5).intValue();

		if(this.querySequences.keySet().size()<numberOfCores)
			numberOfCores=this.querySequences.keySet().size();

		this.querySize.set(new Integer(this.querySequences.size()));

		//Distribute querySequences into fastaFiles
		
		logger.debug("Writting query sequences temporary fasta files... ");
		
		List<String> queryFilesPaths = new ArrayList<>();
		List<Map<String,AbstractSequence<?>>> queriesSubSetList = new ArrayList<>();
		
		String path = this.workspaceTaxonomyFolderPath.concat("/queryBlast");
		
		File f = new File (path);
		if(!f.exists())
			f.mkdir();
		
		CreateGenomeFile.buildSubFastaFiles(path, this.querySequences, queriesSubSetList, queryFilesPaths, numberOfCores);
		
		
		ConcurrentLinkedQueue<AlignmentContainer> alignmentContainerSet = new ConcurrentLinkedQueue<>();
		JAXBContext jc = JAXBContext.newInstance(BlastOutput.class);
		
		for(int i=0; i<numberOfCores; i++) {
			
			ModelAlignments blastAlign;
			
			if(eValueThreshold!=null && bitScoreThreshold!=null && queryCoverageThreshold!=null){
				
				blastAlign = new BlastAlignment(queryFilesPaths.get(i), this.subjectFastaFilePath, queriesSubSetList.get(i), 
						this.similarity_threshold, eValueThreshold, bitScoreThreshold, queryCoverageThreshold, targetCoverageThreshold, 
						isTransportersSearch, this.cancel, alignmentContainerSet, jc);
			}
			else{
				blastAlign= new BlastAlignment(queryFilesPaths.get(i), this.subjectFastaFilePath, queriesSubSetList.get(i), 
					this.similarity_threshold, isTransportersSearch, this.cancel, alignmentContainerSet, jc);
				
				if(eValueThreshold!=null)
					((BlastAlignment) blastAlign).setEvalueThreshold(eValueThreshold);
				if(bitScoreThreshold!=null)
					((BlastAlignment) blastAlign).setBitScoreThreshold(bitScoreThreshold);
				if(queryCoverageThreshold!=null)
					((BlastAlignment) blastAlign).setQueryCoverageThreshold(queryCoverageThreshold);
				if(targetCoverageThreshold!=null)
					((BlastAlignment) blastAlign).setTargetCoverageThreshold(targetCoverageThreshold);
			}
			
			((BlastAlignment) blastAlign).addPropertyChangeListener(this); 

			Thread thread = new Thread(blastAlign);
			threads.add(thread);
			thread.start();
		}
		
		for(Thread thread :threads)
			thread.join();
		
		return alignmentContainerSet;
	}
	///////////////////////////////
	
	/**
	 * @throws Exception
	 */
	public Pair<ConcurrentLinkedQueue<AlignmentContainer>,ConcurrentLinkedQueue<AlignmentContainer>> runBBBlastHits(String queryGenomeFilePath, 
			String subjectGenomeFilePath, boolean isTransportersSearch, Double eValueThreshold, Double bitScoreThreshold, 
			Double queryCoverageThreshold, Double targetCoverageThreshold) throws Exception{
		
		String bbhBlastFolderPath = this.getWorkspaceTaxonomyFolderPath().concat("BBH_Blast/");
		File firstBlastFolder = new File(bbhBlastFolderPath.concat("Blast1/"));
		firstBlastFolder.mkdirs();
		
		this.setWorkspaceTaxonomyFolderPath(firstBlastFolder.getAbsolutePath());
		this.setSubjectFastaFilePath(subjectGenomeFilePath);

		File subjectGenomeFastaFile = new File(this.subjectFastaFilePath);
		ConcurrentHashMap<String, AbstractSequence<?>> secondBlastQuerySequences= new ConcurrentHashMap<String, AbstractSequence<?>>();
		secondBlastQuerySequences.putAll(FastaReaderHelper.readFastaProteinSequence(subjectGenomeFastaFile));
		
		Map<String, AbstractSequence<?>> secondBlastSubjectSequences = new HashMap<>(this.querySequences);
		
		Pair<ConcurrentLinkedQueue<AlignmentContainer>,ConcurrentLinkedQueue<AlignmentContainer>> bbHits = new Pair<>(null, null);
		ConcurrentLinkedQueue<AlignmentContainer> alignmentContainerSet = new ConcurrentLinkedQueue<>();
		
		//First similiarity search//
		alignmentContainerSet =  this.runBlastSearch(false,eValueThreshold,bitScoreThreshold,queryCoverageThreshold,targetCoverageThreshold);
		bbHits.setA(alignmentContainerSet);
		
		//Second similarity search//
		this.querySequences = secondBlastQuerySequences;
		this.staticGenesSet = secondBlastSubjectSequences;
		this.setSubjectFastaFilePath(queryGenomeFilePath);
		File secondBlastFolder = new File(bbhBlastFolderPath.concat("Blast2/"));
		secondBlastFolder.mkdirs();
		this.setWorkspaceTaxonomyFolderPath(secondBlastFolder.getAbsolutePath());
		
		alignmentContainerSet =  this.runBlastSearch(false,eValueThreshold,bitScoreThreshold,queryCoverageThreshold,targetCoverageThreshold);
		bbHits.setB(alignmentContainerSet);
		
		/////////////////
		
		return bbHits;
	}
	
	
	/**
	 * Run the transport similarity searches.
	 * 
	 * @param allSequences
	 * @throws Exception
	 */
	public ConcurrentLinkedQueue<AlignmentContainer> runTransportSearch(Map <String, Double> querySpecificThreshold) throws Exception {
		
		List<Thread> threads = new ArrayList<Thread>();
		ConcurrentLinkedQueue<String> queryArray = new ConcurrentLinkedQueue<String>(this.querySequences.keySet());
		int numberOfCores = Runtime.getRuntime().availableProcessors();
		//int numberOfCores = new Double(Runtime.getRuntime().availableProcessors()*1.5).intValue();

		if(this.querySequences.keySet().size()<numberOfCores)
			numberOfCores=this.querySequences.keySet().size();

		this.querySize.set(new Integer(this.querySequences.size()));
		this.changes.firePropertyChange("size", -1, this.querySize);

		ConcurrentLinkedQueue<AlignmentContainer> alignmentContainerSet = new ConcurrentLinkedQueue<>();
		
		for(int i=0; i<numberOfCores; i++) {
			
			Runnable lc	= new PairwiseSequenceAlignement(this.method, this.querySequences, this.staticGenesSet, queryArray, this.similarity_threshold,
				this.counter, this.cancel, AlignmentPurpose.TRANSPORT, this.alignmentScoreType, alignmentContainerSet);
			
			((PairwiseSequenceAlignement) lc).setQuerySpecificThreshold(querySpecificThreshold);
			((PairwiseSequenceAlignement) lc).addPropertyChangeListener(this); 
			Thread thread = new Thread(lc);
			threads.add(thread);
			thread.start();
		}

		for(Thread thread :threads)
			thread.join();
		
		return alignmentContainerSet;
	}


	/**
	 * @throws Exception
	 */
	public ConcurrentLinkedQueue<AlignmentContainer> run_OrthologGapsSearch(Map<String, Set<String>> sequenceIdsSet, ConcurrentLinkedQueue<AlignmentContainer> alignmentContainerSet) throws Exception {

		boolean recursive = false;
		
		ConcurrentHashMap<String, AbstractSequence<?>> all_sequences = new ConcurrentHashMap<>(querySequences);
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if(all_sequences.keySet().size()>0) {

			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			List<Thread> threads = new ArrayList<Thread>();
			ConcurrentLinkedQueue<String> queryArray = new ConcurrentLinkedQueue<String>(querySequences.keySet());

			this.querySize.set(new Integer(all_sequences.size()));
			this.changes.firePropertyChange("size", -1, this.querySize.get());

			Map<String, AbstractSequence<?>> ecNumberAnnotations = new HashMap<>();
			ecNumberAnnotations.putAll(this.staticGenesSet);
			
			if(this.sequencesWithoutSimilarities==null) {

				if(this.annotatedGenes!= null && !this.annotatedGenes.isEmpty()) 
					ecNumberAnnotations.keySet().retainAll(this.annotatedGenes);
					
				if(!recursive) {

					this.sequencesWithoutSimilarities = new ConcurrentLinkedQueue<String>();
					this.sequencesWithoutSimilarities.addAll(queryArray);
				}
			}
			else  {
				recursive = true;
				queryArray.retainAll(this.sequencesWithoutSimilarities);
			}
			
//			System.out.println("EC NUBMER ANNOT--->"+ecNumberAnnotations.keySet());
//			System.out.println("EC NUBMER ANNOT--->"+ecNumberAnnotations.size());

			int numberOfCores = Runtime.getRuntime().availableProcessors();

			if(queryArray.size()<numberOfCores)
				numberOfCores=queryArray.size();
			
			if(this.method.equals(Method.Blast)){// && !isGapsIdentification()){
				
				//Distribute querySequences into fastaFiles
				logger.info("Writting query sequences temporary fasta files... ");
				
				List<String> queryFilesPaths = new ArrayList<>();
				List<Map<String,AbstractSequence<?>>> queriesSubSetList = new ArrayList<>();
				
				String path = this.workspaceTaxonomyFolderPath.concat("/queryBlast");
				
				File f = new File (path);
				if(!f.exists())
					f.mkdir();
				
				CreateGenomeFile.buildSubFastaFiles(path, all_sequences, queriesSubSetList, queryFilesPaths, numberOfCores);
				//Distribute querySequences into fastaFiles
				
				//Subject Fasta File
				CreateGenomeFile.buildFastaFile(this.subjectFastaFilePath, ecNumberAnnotations);
				//Subject Fasta File
				
				JAXBContext jc = JAXBContext.newInstance(BlastOutput.class);
				
				logger.info("Starting Blast homology searches... ");
				
				for(int i=0; i<numberOfCores; i++) {
					
					ModelAlignments blastAlign	= new BlastAlignment(queryFilesPaths.get(i), this.subjectFastaFilePath, queriesSubSetList.get(i), 
							this.similarity_threshold, false, this.cancel, alignmentContainerSet, jc);
					
					((BlastAlignment) blastAlign).setEc_number(this.ec_number);
					((BlastAlignment) blastAlign).setModules(this.modules);
					((BlastAlignment) blastAlign).setClosestOrthologs(this.closestOrthologs);
					((BlastAlignment) blastAlign).setSequencesWithoutSimilarities(this.sequencesWithoutSimilarities);
					
					((BlastAlignment) blastAlign).setReferenceTaxonomyScore(this.referenceTaxonomyScore);
					((BlastAlignment) blastAlign).setKegg_taxonomy_scores(this.kegg_taxonomy_scores);
					((BlastAlignment) blastAlign).setReferenceTaxonomyThreshold(this.referenceTaxonomyThreshold);
					((BlastAlignment) blastAlign).setSequenceIdsSet(sequenceIdsSet);
					((BlastAlignment) blastAlign).setBlastPurpose(AlignmentPurpose.ORTHOLOGS);
					
					((BlastAlignment) blastAlign).addPropertyChangeListener(this); 

					Thread thread = new Thread(blastAlign);
					threads.add(thread);
					thread.start();
				}
			}		
			else{
				
				logger.info("Starting pairwise sequence alignements... ");

				for(int i=0; i<numberOfCores; i++) {

					Runnable lc	= new PairwiseSequenceAlignement(method, all_sequences, ecNumberAnnotations, queryArray,
							similarity_threshold, this.counter, this.cancel, AlignmentPurpose.ORTHOLOGS, this.alignmentScoreType, 
							alignmentContainerSet);

					((PairwiseSequenceAlignement) lc).setSequencesWithoutSimilarities(this.sequencesWithoutSimilarities);
					((PairwiseSequenceAlignement) lc).setEc_number(this.ec_number);
					((PairwiseSequenceAlignement) lc).setModules(this.modules);
					((PairwiseSequenceAlignement) lc).setClosestOrthologs(this.closestOrthologs);
					((PairwiseSequenceAlignement) lc).setReferenceTaxonomyScore(this.referenceTaxonomyScore);
					((PairwiseSequenceAlignement) lc).setKegg_taxonomy_scores(this.kegg_taxonomy_scores);
					((PairwiseSequenceAlignement) lc).setReferenceTaxonomyThreshold(this.referenceTaxonomyThreshold);
					((PairwiseSequenceAlignement) lc).setSequenceIdsSet(sequenceIdsSet);

					((PairwiseSequenceAlignement) lc).addPropertyChangeListener(this); 
					Thread thread = new Thread(lc);
					threads.add(thread);
					thread.start();
				}
			}

			for(Thread thread :threads)
				thread.join();
		}
		
		return alignmentContainerSet;
	}
	
	/**
	 * @param sequenceIdsSet
	 * @param alignmentContainerSet
	 * @return
	 * @throws Exception
	 */
	public ConcurrentLinkedQueue<AlignmentContainer> run_OrthologsSearch(Map<String, Set<String>> sequenceIdsSet, ConcurrentLinkedQueue<AlignmentContainer> alignmentContainerSet) throws Exception {
		
		Boolean recursive = false;
		
		ConcurrentHashMap<String, AbstractSequence<?>> all_sequences = new ConcurrentHashMap<>(querySequences);

		if(all_sequences.keySet().size()>0) {

			if(this.sequencesWithoutSimilarities!=null)
				recursive = true;

			this.run_OrthologGapsSearch(sequenceIdsSet, alignmentContainerSet);


			if(this.compareToFullGenome && !recursive && this.sequencesWithoutSimilarities!=null && !this.sequencesWithoutSimilarities.isEmpty())
				this.run_OrthologsSearch(sequenceIdsSet, alignmentContainerSet);

		}
		return alignmentContainerSet;
	}

	

//	/**
//	 * @return the alreadyProcessed
//	 */
//	public boolean isAlreadyProcessed() {
//		return alreadyProcessed;
//	}
//
//	/**
//	 * @param alreadyProcessed the alreadyProcessed to set
//	 */
//	public void setAlreadyProcessed(boolean alreadyProcessed) {
//		this.alreadyProcessed = alreadyProcessed;
//	}
//
//	/**
//	 * @return the processed
//	 */
//	public boolean isProcessed() {
//		return processed;
//	}
//
//	/**
//	 * @param processed the processed to set
//	 */
//	public void setProcessed(boolean processed) {
//		this.processed = processed;
//	}

	/**
	 * @param counter the counter to set
	 */
	public void setCounter(AtomicInteger counter) {
		this.counter = counter;
	}

	/**
	 * @param querySize the querySize to set
	 */
	public void setQuerySize(AtomicInteger querySize) {
		this.querySize = querySize;
	}

	/**
	 * @return the querySize
	 */
	public AtomicInteger getQuerySize() {
		return querySize;
	}


	/**
	 * @param cancel the cancel to set
	 */
	public void setCancel(AtomicBoolean cancel) {
		this.cancel = cancel;
	}

	/**
	 * @param annotatedGenes
	 */
	public void setAnnotatedGenes(List<String> annotatedGenes) {

		this.annotatedGenes = annotatedGenes;		
	}

	/**
	 * @return
	 */
	public List<String> getAnnotatedGenes() {

		return this.annotatedGenes;		
	}

	/**
	 * @return the ec_number
	 */
	public String getEc_number() {
		return ec_number;
	}

	/**
	 * @param ec_number the ec_number to set
	 */
	public void setEc_number(String ec_number) {
		this.ec_number = ec_number;
	}

	/**
	 * @return the idModule
	 */
	public Map<String, Set<Integer>> getModules() {
		return modules;
	}

	/**
	 * @param genes_ko_modules the idModule to set
	 */
	public void setModules(Map<String, Set<Integer>> modules) {
		this.modules = modules;
	}

	/**
	 * @return the sequencesWithoutSimilarities
	 */
	public ConcurrentLinkedQueue<String> getSequencesWithoutSimilarities() {
		return sequencesWithoutSimilarities;
	}

	/**
	 * @param sequencesWithoutSimilarities the sequencesWithoutSimilarities to set
	 */
	public void setSequencesWithoutSimilarities(
			ConcurrentLinkedQueue<String> sequencesWithoutSimilarities) {
		this.sequencesWithoutSimilarities = sequencesWithoutSimilarities;
	}

	/**
	 * @param revertMapFromSet
	 */
	public void setClosestOrthologs(Map<String, Set<String>> closestOrthologs) {

		this.closestOrthologs = closestOrthologs;
	}

	public void setReferenceTaxonomyScore(int referenceTaxonomyScore) {
		this.referenceTaxonomyScore = referenceTaxonomyScore;

	}

	public void setKegg_taxonomy_scores(Map<String, Integer> kegg_taxonomy_scores) {

		this.kegg_taxonomy_scores = kegg_taxonomy_scores;
	}

	public double getReferenceTaxonomyThreshold() {
		return referenceTaxonomyThreshold;
	}

	public void setReferenceTaxonomyThreshold(double referenceTaxonomyThreshold) {
		this.referenceTaxonomyThreshold = referenceTaxonomyThreshold;
	}
	
	
	/**
	 * @return
	 */
	public String getSubjectFastaFilePath() {
		return this.subjectFastaFilePath;
	}

	/**
	 * @param subjectFastaFilePath
	 */
	public void setSubjectFastaFilePath(String subjectFastaFilePath) {
		this.subjectFastaFilePath = subjectFastaFilePath;
	}

//	/**
//	 * @return the tcdbFastaFilePath
//	 */
//	public String getTcdbFastaFilePath() {
//		return tcdbFastaFilePath;
//	}
//
//	/**
//	 * @param tcdbFastaFilePath the tcdbFastaFilePath to set
//	 */
//	public void setTcdbFastaFilePath(String tcdbFastaFilePath) {
//		this.tcdbFastaFilePath = tcdbFastaFilePath;
//	}

//	/**
//	 * @return the blastOutputFolderPath
//	 */
//	public String getBlastOutputFolderPath() {
//		return blastOutputFolderPath;
//	}
//
//	/**
//	 * @param blastOutputFolderPath the blastOutputFolderPath to set
//	 */
//	public void setBlastOutputFolderPath(String blastOutputFolderPath) {
//		this.blastOutputFolderPath = blastOutputFolderPath;
//	}

	public boolean isCompareToFullGenome() {
		return compareToFullGenome;
	}

	public void setCompareToFullGenome(boolean compareToFullGenome) {
		this.compareToFullGenome = compareToFullGenome;
	}
	
	/**
	 * @return the gapsIdentification
	 */
	public boolean isGapsIdentification() {
		return gapsIdentification;
	}

	/**
	 * @param gapsIdentification the gapsIdentification to set
	 */
	public void setGapsIdentification(boolean gapsIdentification) {
		this.gapsIdentification = gapsIdentification;
	}

	public String getWorkspaceTaxonomyFolderPath() {
		return workspaceTaxonomyFolderPath;
	}

	public void setWorkspaceTaxonomyFolderPath(String workspaceTaxonomyFolderPath) {
		this.workspaceTaxonomyFolderPath = workspaceTaxonomyFolderPath;
	}

	/**
	 * @param l
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}

	/**
	 * @param l
	 */
	public void removePropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		this.changes.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());				
	}

}
