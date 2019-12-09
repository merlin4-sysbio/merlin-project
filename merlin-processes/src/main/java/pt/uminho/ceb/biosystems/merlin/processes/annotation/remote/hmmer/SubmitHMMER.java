/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.processes.annotation.remote.hmmer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.utilities.MySleep;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.enzymes.AnnotationEnzymesHomologuesData;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HmmerRemoteDatabasesEnum;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HomologySearchServer;
import pt.uminho.ceb.biosystems.merlin.processes.annotation.remote.RemoteDataRetriever;

/**
 * @author ODias
 *
 */
public class SubmitHMMER implements Runnable {
	
	final static Logger logger = LoggerFactory.getLogger(SubmitHMMER.class);

	private String[] organismTaxa;
	private int numberOfAlignments;
	private ConcurrentLinkedQueue<String> sequences;
	private ConcurrentLinkedQueue<AnnotationEnzymesHomologuesData> resultsList;
	private Map<String, ProteinSequence> query;
	private AtomicBoolean cancel;
	private HmmerRemoteDatabasesEnum database;
	private double eValue;
	private AtomicInteger sequencesCounter,
	errorCounter;
	private ConcurrentHashMap<String, String[]> taxonomyMap;
	private ConcurrentHashMap<String, Boolean> uniprotStar;
	private boolean uniprotStatus;
	private AtomicLong time;
	private long latencyWaitingPeriod;
	private Map<String, Long> ridsLatency;

	private long organismTaxonomyIdentifier;

	private PropertyChangeSupport changes;

	/**
	 * @param taxonomyMap
	 * @param uniprotStar
	 * @param sequences
	 * @param query
	 * @param resultsList
	 * @param sequencesCounter
	 * @param errorCounter
	 * @param cancel
	 * @param time
	 * @param organismTaxa
	 * @param expectedVal
	 * @param numberOfAlignments
	 * @param latencyWaitingPeriod
	 * @param organismTaxonomyIdentifier
	 * @param uniprotStatus
	 * @param database
	 */
	public SubmitHMMER(
			ConcurrentHashMap<String, String[]> taxonomyMap, 
			ConcurrentHashMap<String, Boolean> uniprotStar,
			ConcurrentLinkedQueue<String> sequences, Map<String, ProteinSequence> query, 
			ConcurrentLinkedQueue<AnnotationEnzymesHomologuesData> resultsList, 
			AtomicInteger sequencesCounter,  AtomicInteger errorCounter,
			AtomicBoolean cancel, 
			AtomicLong time,
			String[] organismTaxa, 
			double expectedVal, 
			int numberOfAlignments,
			long latencyWaitingPeriod,
			long organismTaxonomyIdentifier,
			boolean uniprotStatus,
			HmmerRemoteDatabasesEnum database
			) {

		this.taxonomyMap = taxonomyMap;
		this.uniprotStar = uniprotStar;
		this.sequences = sequences;
		this.query = query;
		this.resultsList = resultsList;
		this.sequencesCounter = sequencesCounter;
		this.errorCounter = errorCounter;
		this.cancel = cancel;
		this.time = time;
		this.organismTaxa = organismTaxa;
		this.eValue = expectedVal;
		this.numberOfAlignments = numberOfAlignments;
		this.latencyWaitingPeriod = latencyWaitingPeriod;
		this.organismTaxonomyIdentifier = organismTaxonomyIdentifier;
		this.uniprotStatus = uniprotStatus;
		this.database = database;
		
		this.ridsLatency = new HashMap<>();
		
		this.changes = new PropertyChangeSupport(this);
	}

	@Override
	public void run() {

		logger.info(Thread.currentThread().getName()+"\t"+Thread.currentThread().getId()+"\tstarted.");
		
		int counter = 0;
		Map<String, String> hmmerResultKeys = new HashMap<>();

		while(this.sequences.size()>0 && !this.cancel.get()) {

			if(this.cancel.get()) {

				this.sequences.clear();
				
				for(String link : hmmerResultKeys.values()) {
					
					try {
						
						ReadHmmertoList.deleteJob(link);
					} 
					catch (Exception e) {

						e.printStackTrace();
					}
				}
			}
			else {

				String sequence = null;

				try {

					boolean processed = false;
					ReadHmmertoList hmmerToList = null;

					synchronized(this.sequences) {

						sequence = this.sequences.poll();

						if(!this.ridsLatency.containsKey(sequence))
							this.ridsLatency.put(sequence, GregorianCalendar.getInstance().getTimeInMillis());

						this.sequences.notifyAll();

						MySleep.myWait(3000);

						long currentRequestTimer = GregorianCalendar.getInstance().getTimeInMillis();
						long timeSinceDeployment = currentRequestTimer - this.ridsLatency.get(sequence);

						if(timeSinceDeployment<this.latencyWaitingPeriod) {

							if(timeSinceDeployment>(this.latencyWaitingPeriod/2))
								MySleep.myWait(180000);

							if(sequence != null) { 
								
								hmmerToList = new ReadHmmertoList(this.query.get(sequence).getSequenceAsString(), sequence,
										this.database,this.numberOfAlignments,this.eValue, this.cancel);//, this.email);

								String jobID;

								if(hmmerResultKeys.containsKey(sequence)) {

									jobID = hmmerResultKeys.get(sequence);
								}
								else {
									
									long delay = -1;
									synchronized (this.time) {
										
										while(delay<1500 && !cancel.get()) {
											
											delay = System.currentTimeMillis() - this.time.get();
											this.time.set(System.currentTimeMillis());
											
											if (delay < 1500)
												MySleep.myWait(1500);
										}
									}

									jobID = hmmerToList.getJobID();
									hmmerResultKeys.put(sequence, jobID);
								}
								
								logger.trace("Requesting status for jobID {}",jobID);
								processed = hmmerToList.scan(jobID);		
								logger.trace("Status for jobID "+jobID+" "+processed);
								
								if(!processed) {
									
									long sleep = 15000;
									MySleep.myWait(sleep);
									this.sequences.offer(sequence);
									logger.trace("Sleeping..." + (sleep /1000) +" sec jobID "+jobID);
								}
							}
						}
						else {

							logger.debug("Timeout for rid waiting exceeded! Skiping sequence "+sequence);
							errorCounter.incrementAndGet();
						}
					}

					if(processed) {

						RemoteDataRetriever homologyDataClient;

						if(hmmerToList.getResults().size()>0 && !this.cancel.get()) {

							homologyDataClient = new RemoteDataRetriever(hmmerToList, this.organismTaxa, this.taxonomyMap, this.uniprotStar,this.cancel, this.uniprotStatus, HomologySearchServer.HMMER, this.organismTaxonomyIdentifier);

							if(homologyDataClient.getFastaSequence()==null)
								homologyDataClient.setFastaSequence(this.query.get(sequence).getSequenceAsString());
							
							if(homologyDataClient.isDataRetrieved()) {

								homologyDataClient.setDatabaseIdentifier(this.database.toString());
								
								resultsList.add(homologyDataClient.getHomologuesData());

								logger.debug("Gene\t"+homologyDataClient.getLocusTag()+"\tprocessed.");
								System.gc();
								
								changes.firePropertyChange("sequencesCounter", sequencesCounter.get(), sequencesCounter.incrementAndGet());
								if(this.resultsList.size()>25)
									changes.firePropertyChange("saveToDatabase", null, this.resultsList.size());
							}
							else
								this.sequences.offer(sequence);
						}
						else {

							if(processed && !this.cancel.get()) {

								homologyDataClient = new RemoteDataRetriever(hmmerToList.getQuery(),"hmmer",this.cancel, this.uniprotStatus, HomologySearchServer.HMMER, this.organismTaxonomyIdentifier);
								homologyDataClient.setFastaSequence(this.query.get(sequence).getSequenceAsString());
								homologyDataClient.setDatabaseIdentifier(this.database.toString());
								homologyDataClient.setBlastProgram("hmmer");
								homologyDataClient.setBlastVersion("");
								homologyDataClient.setNoSimilarity(true);
								changes.firePropertyChange("sequencesCounter", sequencesCounter.get(), sequencesCounter.incrementAndGet());
								if(this.resultsList.size()>25)
									changes.firePropertyChange("saveToDatabase", null, this.resultsList.size());
								logger.debug("Gene\t"+homologyDataClient.getLocusTag()+"\tprocessed. No similarities.");
							}
						}
					}
				}
				catch (Exception e) {

					counter = counter + 1 ;
					if(!this.cancel.get()) {
						
						if(counter<5) {

							logger.error("Reprocessing\t"+sequence+".\t counter:"+counter);
							if(sequence!=null) {

								this.sequences.offer(sequence);
								hmmerResultKeys.remove(sequence);
							}
						} 
						else {

							errorCounter.incrementAndGet();
							counter = 0;
							e.printStackTrace();
						}
					}
				}
			}

		}
		logger.info(Thread.currentThread().getName()+"\t"+Thread.currentThread().getId()+"\tended.");
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

	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
	}

}