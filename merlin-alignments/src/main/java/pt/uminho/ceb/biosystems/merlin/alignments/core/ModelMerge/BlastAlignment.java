package pt.uminho.ceb.biosystems.merlin.alignments.core.ModelMerge;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.bind.JAXBContext;

import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.alignments.core.AlignmentsUtils;
import pt.uminho.ceb.biosystems.merlin.core.containers.alignment.AlignmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.AlignmentPurpose;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HomologySearchType;
import pt.uminho.ceb.biosystems.merlin.utilities.blast.ncbi_blastparser.BlastIterationData;
import pt.uminho.ceb.biosystems.merlin.utilities.blast.ncbi_blastparser.Hit;
import pt.uminho.ceb.biosystems.merlin.utilities.blast.ncbi_blastparser.NcbiBlastParser;


/**
 * @author amaromorais
 *
 */
public class BlastAlignment implements ModelAlignments, PropertyChangeListener {

//	private static final double ALIGNMENT_MIN_SCORE = 0.0;

	private NcbiBlastParser blout;
	private ConcurrentLinkedQueue<AlignmentContainer> alignmentContainerSet;
	private String alignmentMatrix, queryFasta, subjectFasta, blastOutputFolderPath;
	private boolean isTransportersSearch = false;
	private AtomicBoolean cancel; 
	private Map<String,AbstractSequence<?>> querySequences;
	private JAXBContext jc;
	private String ec_number;
	private Map<String,Set<String>> closestOrthologs;
	private Map<String,Set<Integer>> modules;
	private ConcurrentLinkedQueue<String> sequencesWithoutSimilarities;
	private AlignmentPurpose blastPurpose;
	
	private double threshold;
	private double evalueThreshold;
	private double bitScoreThreshold;
	private double queryCoverageThreshold;
	private double targetCoverageThreshold;
	
	private double alignmentMinScore;

	private Double referenceTaxonomyThreshold;
	private Map<String, Set<String>> sequenceIdsSet;
	private Map<String, Integer> kegg_taxonomy_scores;
	private Integer referenceTaxonomyScore;
	private PropertyChangeSupport changes;
	private HomologySearchType blastProgram = HomologySearchType.BLASTP;
	
	final static Logger logger = LoggerFactory.getLogger(BlastAlignment.class);
	

	

	/**
	 * Default values for evalueThreshold(1E-6), bitScoreThreshold(50), queryCoverageThreshold(0.80) and alignmentMinScore(0.0);
	 * 
	 * @param queryFasta
	 * @param subjectFasta
	 * @param querySequences
	 * @param treshold
	 * @param transportersSearch
	 * @param cancel
	 * @param alignmentContainerSet
	 * @param jc
	 */
	public BlastAlignment(String queryFasta, String subjectFasta, Map<String,AbstractSequence<?>> querySequences, double treshold,  boolean transportersSearch, AtomicBoolean cancel, ConcurrentLinkedQueue<AlignmentContainer> alignmentContainerSet, JAXBContext jc){

		this.changes = new PropertyChangeSupport(this);
		
		this.setEvalueThreshold(1E-6);
		this.setBitScoreThreshold(50);
		this.setQueryCoverageThreshold(0.80);
		this.setTargetCoverageThreshold(0.80);
		this.setAlignmentMinScore(0);
		this.queryFasta = queryFasta;
		this.subjectFasta = subjectFasta;
		this.threshold = treshold;
		this.isTransportersSearch = transportersSearch;
		this.querySequences = querySequences;
		this.alignmentContainerSet = alignmentContainerSet;
		this.cancel = cancel;
		this.jc = jc;

	}
	
	
	/**
	 * Default value for alignmentMinScore(0.0);
	 * 
	 * @param queryFasta
	 * @param subjectFasta
	 * @param querySequences
	 * @param treshold
	 * @param evalueThreshold
	 * @param bitScoreThreshold
	 * @param queryCoverageThreshold
	 * @param transportersSearch
	 * @param cancel
	 * @param alignmentContainerSet
	 * @param jc
	 */
	public BlastAlignment(String queryFasta, String subjectFasta, Map<String,AbstractSequence<?>> querySequences, double treshold, double evalueThreshold,
			double bitScoreThreshold, double queryCoverageThreshold, double targetCoverageThreshold, boolean transportersSearch, AtomicBoolean cancel, ConcurrentLinkedQueue<AlignmentContainer> alignmentContainerSet, JAXBContext jc){

		this.changes = new PropertyChangeSupport(this);
		
		this.setEvalueThreshold(evalueThreshold);
		this.setBitScoreThreshold(bitScoreThreshold);
		this.setQueryCoverageThreshold(queryCoverageThreshold);
		this.setTargetCoverageThreshold(targetCoverageThreshold);
		this.setAlignmentMinScore(0);
		this.queryFasta = queryFasta;
		this.subjectFasta = subjectFasta;
		this.threshold = treshold;
		this.isTransportersSearch = transportersSearch;
		this.querySequences = querySequences;
		this.alignmentContainerSet = alignmentContainerSet;
		this.cancel = cancel;
		this.jc = jc;

	}
	
	
	@Override
	public void run(){

		if(!this.cancel.get()) {

			try {
				
				File queryFile = new File(queryFasta);

				String outputFileName = queryFasta.substring(queryFasta.lastIndexOf("/")).replace(".faa", "").concat("_blastReport.xml");
				if(isTransportersSearch)
					outputFileName = outputFileName.replace(".xml", "_transporters.xml");
				
				File outputFile;
				
				if(this.blastOutputFolderPath!=null && !this.blastOutputFolderPath.isEmpty()){
					outputFile = new File(this.blastOutputFolderPath.concat(outputFileName));
				}
				else{
					outputFile = new File(queryFile.getParent().concat("\\..\\").concat("reports").concat(outputFileName));
//					outputFile = new File(tcdbfile.getParent().concat("\\..\\").concat("reports").concat(outputFileName));
				}
				
				outputFile.getParentFile().mkdirs();
				Process blastProcess = Runtime.getRuntime().exec(blastProgram.toString().toLowerCase().concat(" -query ") + this.queryFasta + " -subject " 
						+ this.subjectFasta + " -out " + outputFile.getAbsolutePath() + " -outfmt 5");

				int exitValue = blastProcess.waitFor();
				
				if (exitValue != 0) {
					logger.warn("Abnormal process termination");
				}
				else{
					logger.info("BLAST search completed with success!");
				}
				
				blastProcess.destroy();
				
				if(outputFile.exists()){
				
					this.blout = new NcbiBlastParser(outputFile, this.jc);
					this.alignmentMatrix = blout.getMatrix();

					buildAlignmentCapsules();
				}
				else{
					
					logger.warn("blast output .xml file wasn't generated on {}", outputFile.getAbsolutePath());
				}
				

			} catch (IOException | InterruptedException e) {

				e.printStackTrace();
			}
			catch (OutOfMemoryError oue) {

				oue.printStackTrace();
			}
			
			System.gc();
			this.changes.firePropertyChange("blastAlignment", false, true);
		}
	}

	/* (non-Javadoc)
	 * @see pt.uminho.ceb.biosystems.merlin.local.alignments.core.ModelMerge.ModelAlignments#buildAlignmentCapsules()
	 */
	public void buildAlignmentCapsules(){

		List<BlastIterationData> iterations = this.blout.getResults();

		Map<String, Double> queriesMaxScores = AlignmentsUtils.getSequencesAlignmentMaxScoreMap(querySequences, alignmentMatrix);
		
		for(BlastIterationData iteration : iterations){

			String queryID = iteration.getQueryDef().trim();
			Integer queryLength = iteration.getQueryLen();

			String [] query_array; 
			String query_org = "";
			String queryLocus = "";

			if(queryID.contains(":")) {
				query_array = queryID.split(":"); 
				query_org = query_array [0].trim();
				queryLocus = query_array[1].trim();
			}
			else {
				if(queryID.contains(" ")) {
					queryID = new StringTokenizer(queryID," ").nextToken();
				}
				
				if(this.blastPurpose!=null && this.blastPurpose.equals(AlignmentPurpose.ORTHOLOGS)) {					
					for(String seqID : this.querySequences.keySet()) {
						if(seqID.contains(queryID)) {
							queryID = seqID;
							query_array = queryID.split(":"); 
							query_org = query_array [0].trim();
							queryLocus = query_array[1].trim();
						}
					}
				}
			}

			if(this.blastPurpose==null || !this.blastPurpose.equals(AlignmentPurpose.ORTHOLOGS) || (!this.sequenceIdsSet.containsKey(queryLocus) || sequenceIdsSet.get(queryLocus).isEmpty())){

				double maxScore = queriesMaxScores.get(iteration.getQueryDef().trim());
				double specificThreshold = this.threshold;
				
				if(this.kegg_taxonomy_scores!=null && this.referenceTaxonomyScore!=null && this.referenceTaxonomyThreshold!=null)
					if(this.kegg_taxonomy_scores.get(query_org)>=this.referenceTaxonomyScore) 
						specificThreshold = this.referenceTaxonomyThreshold;

				List<Hit> hits = iteration.getHits();
				
				if(hits!=null && !hits.isEmpty()){

					for(Hit hit : hits){

						if(!this.cancel.get()){

							try {
								String tcdbID = "";
								String hitNum = hit.getHitNum();
								String target = hit.getHitId();
								
								Integer targetLength = iteration.getHitLength(hitNum);
								Integer alignmentLength = iteration.getHitAlignmentLength(hitNum);
								
								double score = iteration.getHitScore(hit);

								double alignmentScore = (score-this.alignmentMinScore)/(maxScore-this.alignmentMinScore);//alignmentMethod.getSimilarity(); //(((double)alignmentMethod.getScore()-alignmentMethod.getMinScore())/(alignmentMethod.getMaxScore()-alignmentMethod.getMinScore()))

								double bitScore = iteration.getHitBitScore(hit);
								double eValue = iteration.getHitEvalue(hit);
								
								double queryCoverage = iteration.getHitQueryCoverage(hitNum);//(double)(alingmentLength-iteration.getHitAlignmentGaps(hitNum))/(double)queryLength;
								double tragetCoverage = iteration.getHiTargetCoverage(hitNum);//(double)(alingmentLength-iteration.getHitAlignmentGaps(hitNum))/(double)targetLength;


								boolean go = false;
								
								if(isTransportersSearch || blastPurpose==null){
									if(eValue<this.evalueThreshold && bitScore>this.bitScoreThreshold && Math.abs(1-queryCoverage)<=(1-this.queryCoverageThreshold) 
											&& Math.abs(1-tragetCoverage)<=(1-this.targetCoverageThreshold)
											)
										go=true;
								}
								else{
									go = alignmentScore>specificThreshold;
								}
									
//								else if(blastPurpose.equals(AlignmentPurpose.ORTHOLOGS)){
//									if(score>specificThreshold)
//										go=true;
//								}
								
								if(go){

									if(this.sequencesWithoutSimilarities!=null && this.sequencesWithoutSimilarities.contains(queryID)) {
										System.out.println("REMOVING "+queryID+" from sequencesWithoutSimilarities");

										this.sequencesWithoutSimilarities.remove(queryID);
									}

									if(isTransportersSearch){

										String hitdef = hit.getHitDef();

										StringTokenizer st = new StringTokenizer(hitdef,"|");
										st.nextToken();
										st.nextToken();
										target = st.nextToken().toUpperCase().trim();
										tcdbID = st.nextToken().split("\\s+")[0].toUpperCase().trim();
									}

									AlignmentContainer alignContainer = new AlignmentContainer(queryID, target, tcdbID, this.alignmentMatrix, score);
									
									alignContainer.setMaxScore(maxScore);
									alignContainer.setMinScore(this.getAlignmentMinScore());
									alignContainer.setEvalue(eValue);
									alignContainer.setBitScore(bitScore);
									alignContainer.setAlignmentLength(alignmentLength);
									alignContainer.setQueryLength(queryLength);
									alignContainer.setTargetLength(targetLength);	
									alignContainer.setNumIdenticals(iteration.getHitIdentity(hitNum));
									alignContainer.setNumSimilars(iteration.getHitPositive(hitNum));
									alignContainer.setCoverageQuery(queryCoverage);
									alignContainer.setCoverageTarget(tragetCoverage);

									alignContainer.setEcNumber(this.ec_number);
									alignContainer.setClosestOrthologues(this.closestOrthologs);
									alignContainer.setModules(modules);
									
									//					alignContainer.setAlignedScore(alignedScore);

									//					iterationAlignments.add(align);
									this.alignmentContainerSet.add(alignContainer);
								}
							} 
							catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					//			this.alignments.put(queryID,iterationAlignments);
				}
				else{

					logger.info(iteration.getIteration().getIterationMessage().concat(" for {}"), queryID);
				}
			}
			else{
				if(this.sequencesWithoutSimilarities!=null && this.sequencesWithoutSimilarities.contains(queryID)) {
					System.out.println("REMOVING "+queryID+" from sequencesWithoutSimilarities");
					this.sequencesWithoutSimilarities.remove(queryID);
				}
			}
		}
	}


	public ConcurrentLinkedQueue<AlignmentContainer> getAlignmentsCapsules(){

		return this.alignmentContainerSet;
	}


	/**
	 * Method that convert the ConcurrentLinkedQueue of alignmentCapsules into a Map where the keys are the querySequence Ids 
	 * and the values list of the correspondent AlignmentCapsules
	 * 
	 * @return
	 */
	public Map<String,List<AlignmentContainer>> getAlignmentsByQuery(){

		Map<String,List<AlignmentContainer>> alignmentMap = new HashMap<>();

		for(AlignmentContainer alignContainer : this.alignmentContainerSet){

			String query = alignContainer.getQuery();

			if(alignmentMap.containsKey(query)){

				alignmentMap.get(query).add(alignContainer);
			}
			else{
				List<AlignmentContainer> containersList = new ArrayList<>();
				containersList.add(alignContainer);
				alignmentMap.put(query, containersList);
			}
		}

		return alignmentMap;
	}

	
	/**
	 * @return the threshold
	 */
	public double getThreshold() {
		return threshold;
	}

	/**
	 * @param threshold the threshold to set
	 */
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	/**
	 * @return the evalueThreshold
	 */
	public double getEvalueThreshold() {
		return evalueThreshold;
	}

	/**
	 * @param evalueThreshold the evalueThreshold to set
	 */
	public void setEvalueThreshold(double evalueThreshold) {
		this.evalueThreshold = evalueThreshold;
	}

	/**
	 * @return the bitScoreThreshold
	 */
	public double getBitScoreThreshold() {
		return bitScoreThreshold;
	}

	/**
	 * @param bitScoreThreshold the bitScoreThreshold to set
	 */
	public void setBitScoreThreshold(double bitScoreThreshold) {
		this.bitScoreThreshold = bitScoreThreshold;
	}

	/**
	 * @return the queryCoverageThreshold
	 */
	public double getQueryCoverageThreshold() {
		return queryCoverageThreshold;
	}

	/**
	 * @param queryCoverageThreshold the queryCoverageThreshold to set
	 */
	public void setQueryCoverageThreshold(double queryCoverageThreshold) {
		this.queryCoverageThreshold = queryCoverageThreshold;
	}

	/**
	 * @return the targetCoverageThreshold
	 */
	public double getTargetCoverageThreshold() {
		return targetCoverageThreshold;
	}


	/**
	 * @param targetCoverageThreshold the targetCoverageThreshold to set
	 */
	public void setTargetCoverageThreshold(double targetCoverageThreshold) {
		this.targetCoverageThreshold = targetCoverageThreshold;
	}


	/**
	 * @return the alignmentMinScore
	 */
	public double getAlignmentMinScore() {
		return alignmentMinScore;
	}


	/**
	 * @param alignmentMinScore the alignmentMinScore to set
	 */
	public void setAlignmentMinScore(double alignmentMinScore) {
		this.alignmentMinScore = alignmentMinScore;
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
	 * @return the closestOrthologs
	 */
	public Map<String,Set<String>> getClosestOrthologs() {
		return closestOrthologs;
	}

	/**
	 * @param closestOrthologs the closestOrthologs to set
	 */
	public void setClosestOrthologs(Map<String,Set<String>> closestOrthologs) {
		this.closestOrthologs = closestOrthologs;
	}

	/**
	 * @return the modules
	 */
	public Map<String,Set<Integer>> getModules() {
		return modules;
	}

	/**
	 * @param modules the modules to set
	 */
	public void setModules(Map<String,Set<Integer>> modules) {
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
	public void setSequencesWithoutSimilarities(ConcurrentLinkedQueue<String> sequencesWithoutSimilarities) {
		this.sequencesWithoutSimilarities = sequencesWithoutSimilarities;
	}

	/**
	 * @return the blastPurpose
	 */
	public AlignmentPurpose getBlastPurpose() {
		return blastPurpose;
	}

	/**
	 * @param blastPurpose the blastPurpose to set
	 */
	public void setBlastPurpose(AlignmentPurpose blastPurpose) {
		this.blastPurpose = blastPurpose;
	}

	/**
	 * @return the sequenceIdsSet
	 */
	public Map<String, Set<String>> getSequenceIdsSet() {
		return sequenceIdsSet;
	}

	/**
	 * @param sequenceIdsSet the sequenceIdsSet to set
	 */
	public void setSequenceIdsSet(Map<String, Set<String>> sequenceIdsSet) {
		this.sequenceIdsSet = sequenceIdsSet;
	}

	/**
	 * @return the kegg_taxonomy_scores
	 */
	public Map<String, Integer> getKegg_taxonomy_scores() {
		return kegg_taxonomy_scores;
	}

	/**
	 * @param kegg_taxonomy_scores the kegg_taxonomy_scores to set
	 */
	public void setKegg_taxonomy_scores(Map<String, Integer> kegg_taxonomy_scores) {
		this.kegg_taxonomy_scores = kegg_taxonomy_scores;
	}

	/**
	 * @return the referenceTaxonomyScore
	 */
	public int getReferenceTaxonomyScore() {
		return referenceTaxonomyScore;
	}

	/**
	 * @param referenceTaxonomyScore the referenceTaxonomyScore to set
	 */
	public void setReferenceTaxonomyScore(int referenceTaxonomyScore) {
		this.referenceTaxonomyScore = referenceTaxonomyScore;
	}

	/**
	 * @return the referenceTaxonomyThreshold
	 */
	public double getReferenceTaxonomyThreshold() {
		return referenceTaxonomyThreshold;
	}

	/**
	 * @param referenceTaxonomyThreshold the referenceTaxonomyThreshold to set
	 */
	public void setReferenceTaxonomyThreshold(double referenceTaxonomyThreshold) {
		this.referenceTaxonomyThreshold = referenceTaxonomyThreshold;
	}


	/**
	 * @return the blastOutputFolderPath
	 */
	public String getBlastOutputFolderPath() {
		return blastOutputFolderPath;
	}


	/**
	 * @param blastOutputFolderPath the blastOutputFolderPath to set
	 */
	public void setBlastOutputFolderPath(String blastOutputFolderPath) {
		this.blastOutputFolderPath = blastOutputFolderPath;
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
