package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.EbiWebServices;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.EbiEnumerators.EbiTool;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.interpro.InterProParser;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.interpro.InterProResultsList;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.phobius.PhobiusParser;

/**
 * @author Oscar Dias
 *
 */
public class EbiRunnable extends Observable implements Runnable {

	private ConcurrentLinkedQueue<String> requests;
	private Map<String, AbstractSequence<?>> genome;
	private ConcurrentHashMap<String, Object> results;
	private AtomicBoolean cancel;
	private EbiTool tool;
	private AtomicInteger errorCounter;
	private AtomicInteger sequencesCounter;
	private long waitingPeriod;
//	private static final String email = "odias@deb.uminho.pt";
	private String email;
	final static Logger logger = LoggerFactory.getLogger(EbiRunnable.class);
	private Map<String,String> ec2go, sl2go, interpro2go;

	/**
	 * Run EBI webservices in multithreading.
	 * 
	 * @param tool
	 * @param requests
	 * @param genome
	 * @param results
	 * @param errorCounter
	 * @param sequencesCounter
	 * @param cancel
	 * @param waitingPeriod
	 */
	public EbiRunnable(EbiTool tool, ConcurrentLinkedQueue<String> requests, Map<String, AbstractSequence<?>> genome, ConcurrentHashMap<String, Object> results, 
			 AtomicInteger errorCounter, AtomicInteger sequencesCounter, AtomicBoolean cancel, long waitingPeriod, String email) {

		super();
		this.requests = requests;
		this.genome = genome;
		this.results = results;
		this.cancel = cancel;
		this.tool = tool;
		this.errorCounter = errorCounter;
		this.waitingPeriod = waitingPeriod;
		this.sequencesCounter = sequencesCounter;
		this.email = email;
	}

	/**
	 * Run EBI webservices in multithreading.
	 *  
	 * @param tool
	 * @param requests
	 * @param genome
	 * @param results
	 * @param waitingPeriod
	 * @param email
	 */
	public EbiRunnable(EbiTool tool, ConcurrentLinkedQueue<String> requests, Map<String, AbstractSequence<?>> genome, 
			ConcurrentHashMap<String, Object> results, long waitingPeriod, String email) {

		super();
		this.requests = requests;
		this.genome = genome;
		this.results = results;
		this.cancel = new AtomicBoolean(false);
		this.tool = tool;
		this.errorCounter = new AtomicInteger(0);
		this.sequencesCounter = new AtomicInteger(0);
		this.waitingPeriod = waitingPeriod;
		this.email = email;
	}


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		while(this.requests.size()>0 && !this.cancel.get()) {

			String query = this.requests.poll();
			AbstractSequence<?> sequence = this.genome.get(query);
			int error = 0;
			
			if(this.tool.equals(EbiTool.PHOBIUS))
				this.processPhobius(query, sequence, error, this.waitingPeriod);
			else
				this.processInterPro(query, sequence, error, this.waitingPeriod);
						
			this.sequencesCounter.incrementAndGet();
			
			setChanged();
			notifyObservers();
			
			if(!this.cancel.get()) {
				//this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), this.sequencesCounter.get(), this.size, this.message);
				logger.debug("Counter {} for thread {} {}", this.sequencesCounter,Thread.currentThread().getName(), Thread.currentThread().getId());
			}
		} 
	}

	/**
	 * Execute the Phobius process.
	 * 
	 * @param query
	 * @param sequence
	 * @param error
	 * @param waitingPeriod 
	 */
	private void processPhobius(String query, AbstractSequence<?> sequence, int error, long waitingPeriod)  {

		try {
			
			String ebi = EbiWebServices.runPhobius(email, query, sequence);
			List<String>  result = EbiRestful.getXml(ebi, "phobius", waitingPeriod, this.cancel);
			int h = PhobiusParser.getNumberOfHelices(result);
			this.results.put(query, h);

		}
		catch (IOException | InterruptedException e) {

			if (error<6) {

				error++;
				this.processPhobius(query, sequence, error, waitingPeriod);
			}
			else {

				e.printStackTrace();
				this.errorCounter.incrementAndGet();
			}
		}	
	}
	
	
	/**
	 * Execute the InterPro process.
	 * 
	 * @param query
	 * @param sequence
	 * @param error
	 * @param waitingPeriod
	 */
	private void processInterPro(String query, AbstractSequence<?> sequence, int error, long waitingPeriod)  {

		try {
			
			String ebi = EbiWebServices.runInterProScan(email, query, sequence, "true", "true", new ArrayList<String>());
			List<String>  result = EbiRestful.getXml(ebi, "iprscan5", waitingPeriod, this.cancel);
			
			InterProResultsList interProResultsList = InterProParser.getXmlInformation(result, this.getEc2go(), this.getSl2go(), this.getInterpro2go());
			interProResultsList.setQuerySequence(sequence.getSequenceAsString());
			interProResultsList.setQuery(query);
			this.results.put(query, interProResultsList);
			logger.trace("query {} result {} ", query, interProResultsList);
		}
		catch (IOException | InterruptedException e) {
			
			if (error<6) {

				error++;
				this.processInterPro(query, sequence, error, waitingPeriod);
			}
			else {
				
				e.printStackTrace();
				this.errorCounter.incrementAndGet();
			}
		}	
	}

	/**
	 * @return the ec2go
	 */
	public Map<String,String> getEc2go() {
		return ec2go;
	}

	/**
	 * @param ec2go the ec2go to set
	 */
	public void setEc2go(Map<String,String> ec2go) {
		this.ec2go = ec2go;
	}

	/**
	 * @return the sl2go
	 */
	public Map<String,String> getSl2go() {
		return sl2go;
	}

	/**
	 * @param sl2go the sl2go to set
	 */
	public void setSl2go(Map<String,String> sl2go) {
		this.sl2go = sl2go;
	}

	/**
	 * @return the interpro2go
	 */
	public Map<String,String> getInterpro2go() {
		return interpro2go;
	}

	/**
	 * @param interpro2go the interpro2go to set
	 */
	public void setInterpro2go(Map<String,String> interpro2go) {
		this.interpro2go = interpro2go;
	}

}
