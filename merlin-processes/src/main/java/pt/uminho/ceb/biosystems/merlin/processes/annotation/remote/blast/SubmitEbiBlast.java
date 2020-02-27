package pt.uminho.ceb.biosystems.merlin.processes.annotation.remote.blast;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.axis.AxisFault;
import org.apache.jcs.access.exception.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.blast.org.biojava3.ws.alignment.qblast.NCBIQBlastAlignmentProperties;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.blast.org.biojava3.ws.alignment.qblast.NCBIQBlastOutputProperties;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.blast.EbiBlastClientRest;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.utilities.MySleep;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.enzymes.AnnotationEnzymesHomologuesData;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HomologySearchServer;
import pt.uminho.ceb.biosystems.merlin.processes.annotation.remote.RemoteDataRetriever;
import pt.uminho.ceb.biosystems.merlin.utilities.blast.ebi_blastparser.BlastIterationData;
import pt.uminho.ceb.biosystems.merlin.utilities.blast.ebi_blastparser.EbiBlastParser;
import pt.uminho.ceb.biosystems.merlin.utilities.blast.ebi_blastparser.THit;

/**
 * @author Oscar
 *
 */
public class SubmitEbiBlast implements Runnable {

	final static Logger logger = LoggerFactory.getLogger(SubmitEbiBlast.class);

	private static final int _SAVE_LIST_SIZE = 50;
	private static final int _ERRORS_LIMIT = 3;
	private static final int _LATENCY = 60000;

	private PropertyChangeSupport changes;

	private ConcurrentLinkedQueue<String> rids;
	private ConcurrentLinkedQueue<AnnotationEnzymesHomologuesData> resultsList;

	private NCBIQBlastAlignmentProperties rqb;
	private NCBIQBlastOutputProperties rof;
	private EbiBlastClientRest rbw;

	private Map<String, String> queryRIDMap;

	private String[] organismTaxa;

	private AtomicBoolean cancel;

	private int thread_number;

	private long latencyWaitingPeriod, taxonomyIdentifier;

	private AtomicInteger sequencesCounter, errorsCounter;

	private ConcurrentHashMap<String, String[]> taxonomyMap;

	private ConcurrentHashMap<String, Boolean> uniprotStar;

	private boolean uniprotStatus;

	private Map<String, Long> ridsLatency;

	private Double userEval;
	
	private Float identityLowerThreshold;
	
	private Float identityUpperThreshold;
	
	private Float positives;
	
	private Float queryCoverage;
	
	private Float targetCoverage;



	/**
	 * http://www.ebi.ac.uk/Tools/webservices/services/sss/ncbi_blast_rest
	 * 
	 * @param rbw
	 * @param rqb
	 * @param taxonomyMap
	 * @param uniprotStar
	 * @param rids
	 * @param resultsList
	 * @param sequencesCounter
	 * @param errorCounter
	 * @param cancel
	 * @param queryRIDMap
	 * @param orgArray
	 * @param latencyWaitingPeriod
	 * @param taxonomyIdentifier
	 * @param uniprotStatus
	 * @throws Exception 
	 */
	public SubmitEbiBlast(EbiBlastClientRest rbw, NCBIQBlastAlignmentProperties rqb, 
			ConcurrentHashMap<String, String[]> taxonomyMap,  ConcurrentHashMap<String, Boolean> uniprotStar, 
			ConcurrentLinkedQueue<String> rids, 
			ConcurrentLinkedQueue<AnnotationEnzymesHomologuesData> resultsList, 
			AtomicInteger sequencesCounter,  AtomicInteger errorCounter,
			AtomicBoolean cancel, 
			Map<String, String> queryRIDMap, 
			String[] orgArray, 
			long latencyWaitingPeriod, long taxonomyIdentifier, 
			boolean uniprotStatus, Double userEval, Float identityLowerThreshold, Float identityUpperThreshold, Float positives,
			Float queryCoverage, Float targetCoverage) throws Exception  {

		this.sequencesCounter = sequencesCounter;
		this.organismTaxa=orgArray;
		this.rqb = rqb;
		this.queryRIDMap = queryRIDMap;
		this.rbw = rbw;
		this.rids = rids;
		this.taxonomyIdentifier = taxonomyIdentifier;
		this.userEval = userEval;
		this.identityLowerThreshold = identityLowerThreshold;
		this.identityUpperThreshold = identityUpperThreshold;
		this.positives = positives;
		this.queryCoverage = queryCoverage;
		this.targetCoverage = targetCoverage;
		
		this.rof = new NCBIQBlastOutputProperties();
		if(rqb.getOrganism()!=null)
			this.rof.setOrganisms(rqb.getOrganism());
		this.rof.setAlignmentNumber(rqb.getHitlistSize());
			
		this.cancel = cancel;
		this.taxonomyMap = taxonomyMap;
		this.uniprotStar = uniprotStar;
		this.errorsCounter = errorCounter;
		this.uniprotStatus = uniprotStatus;
		this.latencyWaitingPeriod = latencyWaitingPeriod;
		this.resultsList = resultsList;

		this.ridsLatency = new HashMap<String, Long>();

		this.changes = new PropertyChangeSupport(this);
		
		
		}

	@Override
	public void run(){

		logger.info(Thread.currentThread().getName()+"\t"+Thread.currentThread().getId()+"\tstarted.");

		int errorCounter = 0;
		long lastRequestTimer = GregorianCalendar.getInstance().getTimeInMillis();

		while(this.rids.size()>0) {

			if(this.cancel.get()) {

				this.rids.clear();
			}
			else {

				String aRid = null;
				boolean requestReady = false;

				try {

					synchronized(this.rids){

						aRid = this.rids.poll();

						if(!this.ridsLatency.containsKey(aRid))
							this.ridsLatency.put(aRid, GregorianCalendar.getInstance().getTimeInMillis());

						this.rids.notifyAll();

						if(aRid!=null) {

							long currentRequestTimer = GregorianCalendar.getInstance().getTimeInMillis();
							long timeSinceDeployment = currentRequestTimer - this.ridsLatency.get(aRid);

							if(timeSinceDeployment<this.latencyWaitingPeriod) {

								if(timeSinceDeployment>(this.latencyWaitingPeriod/2)) {
									
									MySleep.myWait(timeSinceDeployment/60);

								}

								if(currentRequestTimer - lastRequestTimer > _LATENCY) {

									logger.debug("Requesting status for RID "+aRid);
									requestReady = this.rbw.isReady(aRid, GregorianCalendar.getInstance().getTimeInMillis());
									logger.debug("Status for RID "+aRid+" "+requestReady);
									lastRequestTimer = currentRequestTimer;
								}
								else {

									long sleep  = (_LATENCY + 3000) - (currentRequestTimer - lastRequestTimer); 
									logger.debug("Sleeping..." + (sleep/1000) +" sec "+aRid);
									MySleep.myWait(sleep);
								}

								if(!requestReady)
									if(!this.cancel.get()) 
										this.rids.offer(aRid);
							}
							else {

								logger.debug("Timeout for rid waiting exceeded! Skiping RID "+aRid);
								errorsCounter.incrementAndGet();
							}
						}
					}

					if(requestReady) {


						InputStream stream = this.rbw.getAlignmentResults(aRid, this.rof);
						
						//File outputXml = new File ("C:\\Users\\Diogo\\Desktop\\merlin_debug\\streamXML.xml");
						//FileUtils.copyInputStreamToFile(stream, outputXml);
						//NcbiBlastParser ncbiBLASTparser = new NcbiBlastParser(stream);
						
						EbiBlastParser ebiBlastParser = new EbiBlastParser(stream);

						stream.close();

						if(ebiBlastParser.isReprocessQuery()) {

							if(!this.cancel.get())
								this.reprocessQuery(aRid,this.queryRIDMap.get(aRid),0);
						}
						else {
							
							List<BlastIterationData> ebiBlastResultsWithValidEval = filterByUserEval(ebiBlastParser, this.userEval, this.identityLowerThreshold, this.identityUpperThreshold,
									this.positives, this.queryCoverage, this.targetCoverage);   // blast results that passed the Eval threshold

							if(ebiBlastParser.isSimilarityFound() && !ebiBlastResultsWithValidEval.isEmpty()) { // if we found any homologues and they passed the Eval threshold
								
								ebiBlastParser.setResults(ebiBlastResultsWithValidEval); // use only the results that passed the Eval threshold
								logger.debug("Similarity found for "+ebiBlastParser.getResults().get(0).getQueryID());

								if(!this.cancel.get()) {

									RemoteDataRetriever homologyDataEbiClient = new RemoteDataRetriever(ebiBlastParser, this.organismTaxa, this.taxonomyMap, this.uniprotStar, this.cancel, 
											HomologySearchServer.EBI, this.rqb.getHitlistSize(), this.uniprotStatus, this.taxonomyIdentifier);

									if(homologyDataEbiClient.getFastaSequence()==null)
										homologyDataEbiClient.setFastaSequence(this.queryRIDMap.get(aRid).split("\n")[1]);

									if(homologyDataEbiClient.getHomologuesData() != null && homologyDataEbiClient.isDataRetrieved()) {

										homologyDataEbiClient.setDatabaseIdentifier(rqb.getBlastDatabase());
										resultsList.add(homologyDataEbiClient.getHomologuesData());

										logger.debug("Gene\t"+homologyDataEbiClient.getLocusTag()+"\tprocessed. "+this.rids.size()+" genes left in queue "+thread_number);
										if(this.rids.size()<100)
											MySleep.myWait(1000);

										changes.firePropertyChange("sequencesCounter", sequencesCounter.get(), sequencesCounter.incrementAndGet());
										if(this.resultsList.size()>_SAVE_LIST_SIZE || rids.isEmpty())
											changes.firePropertyChange("saveToDatabase", null, this.resultsList.size());
									}
									else {

										if(!this.cancel.get()) {

											logger.debug("Reprocessing "+aRid);
											this.reprocessQuery(aRid,this.queryRIDMap.get(aRid),0);
										}
									}
								}
							}
							else {

								if(!this.cancel.get()) {

									RemoteDataRetriever homologyDataEbiClient = new RemoteDataRetriever(
											this.queryRIDMap.get(aRid).split("\n")[0].replace(">", ""),
											this.rqb.getBlastProgram(),this.cancel, this.uniprotStatus, HomologySearchServer.EBI, taxonomyIdentifier);

									homologyDataEbiClient.setFastaSequence(this.queryRIDMap.get(aRid).split("\n")[1]);
									homologyDataEbiClient.setDatabaseIdentifier(rqb.getBlastDatabase());
									if(homologyDataEbiClient.getHomologuesData() == null) {
										throw new Exception("Homologues data is null");
									}
									else {
										resultsList.add(homologyDataEbiClient.getHomologuesData());
										changes.firePropertyChange("sequencesCounter", sequencesCounter.get(), sequencesCounter.incrementAndGet());
										if(this.resultsList.size()>_SAVE_LIST_SIZE)
											changes.firePropertyChange("saveToDatabase", null, this.resultsList.size());

										errorCounter = 0;
										logger.debug("Gene\t"+homologyDataEbiClient.getLocusTag()+"\tprocessed. No similarities. "+this.rids.size()+" genes left in queue "+thread_number);
									}

								}
							}
						}
					}
				}
				catch (AxisFault e) {

					if(!this.cancel.get()) {

						logger.error("Submit blast NCBI server not responding. Aborting thread.");
						this.rids.clear();
					}
				}
				catch(IllegalArgumentException e) {
//					this.setCancel(new AtomicBoolean(true));
//					Thread t = Thread.currentThread();
					this.changes.firePropertyChange("invalidEmail",null, null);
//				    t.getUncaughtExceptionHandler().uncaughtException(t, e);
					
				}

				catch (Exception e) {
					
					e.printStackTrace();

					errorCounter = errorCounter + 1;
					if(!this.cancel.get()) {

						if(errorCounter<_ERRORS_LIMIT) {

							logger.warn("Submit Blast Exception "+e.getMessage()+"\n Reprocessing:\t"+aRid+" Error ounter: "+errorCounter+"");
							this.rids.remove(aRid);
							this.reprocessQuery(aRid,this.queryRIDMap.get(aRid),0);
						}
						else {

							this.errorsCounter.incrementAndGet();
							errorCounter = 0;
						}
					}
				}

			}
			//			changes.firePropertyChange("sequencesCounter", sequencesCounter, sequencesCounter.incrementAndGet());
		}

		logger.info(Thread.currentThread().getName()+"\t"+Thread.currentThread().getId()+"\t ended!");
	}

	/**
	 * @param aRid
	 * @param sequence
	 * @param counter
	 * @return
	 * @throws Exception 
	 */
	private String reprocessQuery(String aRid, String sequence, int counter) {

		try {

			MySleep.myWait(3000);

			String newRid = this.rbw.sendAlignmentRequest(sequence,this.rqb);

			if(newRid == null) {

				if(counter<_ERRORS_LIMIT) {

					return this.reprocessQuery(aRid,sequence,counter++);
				}
				else {

					logger.error("Error getting new rid for rid \t"+aRid+" for sequence \t"+sequence);
				}
			}
			else {

				this.rids.offer(newRid);
				this.queryRIDMap.put(newRid, sequence);
				this.queryRIDMap.remove(aRid);
				this.rids.remove(aRid);
				return newRid;
			}

			// http://www.ncbi.nlm.nih.gov/staff/tao/URLAPI/new/node96.html
			// b. For URLAPI scripts, do NOT send requests faster than once every 3 seconds. 

		}
		catch (Exception e){

			if(e.getMessage() == null)  {

				logger.error("Cannot perform Blast at this time, try again later!");
			}
			else {

				if(e.getMessage().contains("NCBI QBlast refused this request because")) {

					logger.error("Cannot perform Blast at this time, try again later!");
				}
				else if(e.getMessage().contains("Cannot get RID for alignment!")) {

					logger.error("Cannot get RID for sequence "+sequence+". Retrying query!");
					return this.reprocessQuery(aRid,sequence,counter++);
				}
				else {

					return this.reprocessQuery(aRid,sequence,counter++);
				}
			}
		}
		return null;
	}


	/**
	 * @return the cancel
	 */
	public AtomicBoolean isCancel() {
		return cancel;
	}

	/**
	 * @param cancel the cancel to set
	 */
	public void setCancel(AtomicBoolean cancel) {
		this.cancel = cancel;
	}

	public int getThread_number() {
		return thread_number;
	}

	public void setThread_number(int thread_number) {
		this.thread_number = thread_number;
	}

	/**
	 * @param ebiBlastParser
	 * @return
	 */
	private  List<BlastIterationData> filterByUserEval(EbiBlastParser ebiBlastParser, Double eval, Float identityLowerThreshold, 
			Float identityUpperThreshold, Float positivesThreshold, Float queryCoverageThreshold, Float targetCoverageThreshold) {

		List<BlastIterationData> blastResults = new ArrayList<BlastIterationData>();
		if(ebiBlastParser != null) 
			blastResults = ebiBlastParser.getResults();
		List<THit> hitsThatPassedEvalueThreshold = new ArrayList<THit>();

		for (BlastIterationData result : blastResults) {

			List<THit> hits = result.getHits();

			for (int i = 0; i<hits.size();i++ ){

				THit hit = hits.get(i);
				String id = hit.getId();

				if(id!=null) {

					Integer hitQuerySeqLength = result.getHitQuerySeqLength(hit);
					Integer queryLength = result.getQueryLen();
					Integer hitLength = result.getHitLength(i+1+"");
					Integer hitTargetSeqLength = result.getHitMatchSeqLength(hit);
					
					Float queryCoverage = (float) hitQuerySeqLength / queryLength;
					Float targetCoverage = (float) hitTargetSeqLength / hitLength;
					Float identity = (float) result.getHitIdentity(hit) / 100;
					
					// See later if it is worth it to implement the POSITIVES threshold
					if(Double.parseDouble(hit.getAlignments().getAlignment().get(0).getExpectation() +"") <= eval && identity>=identityLowerThreshold && identity<=identityUpperThreshold && queryCoverage>=queryCoverageThreshold && targetCoverage>=targetCoverageThreshold)
						hitsThatPassedEvalueThreshold.add(hit);
				}
			}
		}
		
		blastResults.get(0).setHits(hitsThatPassedEvalueThreshold);
		return blastResults;
	}
	
	

	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
	}
}
