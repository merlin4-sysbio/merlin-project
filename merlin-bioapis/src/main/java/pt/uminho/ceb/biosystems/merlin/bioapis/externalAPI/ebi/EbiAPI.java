package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.EbiEnumerators.EbiTool;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.interpro.InterProParser;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.interpro.InterProResultsList;

/**
 * @author Oscar Dias
 *
 */
public class EbiAPI extends Observable implements Observer {

	final static Logger logger = LoggerFactory.getLogger(EbiAPI.class);
	//private String email = "odias@deb.uminho.pt";
	
	/**
	 * Get number of helices on phobius for every gene in a genome.
	 * 
	 * @param genome
	 * @param counter
	 * @param errorCounter
	 * @param cancel
	 * @param waitingPeriod
	 * @param sequencesCounter 
	 * @param email 
	 * @return
	 * @throws InterruptedException 
	 */
	public Map<String, Integer> getHelicesFromPhobius(Map<String, AbstractSequence<?>> genome, AtomicInteger errorCounter, AtomicBoolean cancel,
			long waitingPeriod, AtomicInteger sequencesCounter, String email) throws InterruptedException {

		ConcurrentHashMap<String, Object> results = new ConcurrentHashMap<>();
		ConcurrentLinkedQueue<String> requests = new ConcurrentLinkedQueue<>(genome.keySet());

		int threadsNumber = 50;
		List<Thread> threads = new ArrayList<Thread>();
		if(requests.size()<threadsNumber)
			threadsNumber=requests.size();

		if(!cancel.get() && genome.size()>0) {

			for(int i=0; i<threadsNumber; i++) {

				Runnable lc	= new EbiRunnable(EbiTool.PHOBIUS, requests, genome, results, errorCounter, sequencesCounter, cancel, waitingPeriod, email);

				((EbiRunnable) lc).addObserver(this);
				Thread thread = new Thread(lc);
				threads.add(thread);
				thread.start();
			}

			for(Thread thread :threads)
				thread.join();
		}

		Map<String, Integer> ret = new HashMap<>();
		
		for(String key : results.keySet())
			ret.put(key, (Integer) results.get(key));

		return ret;
	}

	/**
	 * Get number of helices on phobius for every gene in a genome.
	 * 
	 * @param genome
	 * @param waitingPeriod 
	 * @param email 
	 * @return
	 * @throws InterruptedException 
	 */
	public static Map<String, Integer> getHelicesFromPhobius(Map<String, AbstractSequence<?>> genome, long waitingPeriod, String email) throws InterruptedException {

		ConcurrentHashMap<String, Object> results = new ConcurrentHashMap<>();
		ConcurrentLinkedQueue<String> requests = new ConcurrentLinkedQueue<>(genome.keySet());

		int threadsNumber=50;

		List<Thread> threads = new ArrayList<Thread>();

		if(requests.size()<threadsNumber)
			threadsNumber=requests.size();

		if(genome.size()>0) {


			for(int i=0; i<threadsNumber; i++) {

				Runnable lc	= new EbiRunnable(EbiTool.PHOBIUS, requests, genome, results, waitingPeriod, email);

				Thread thread = new Thread(lc);
				threads.add(thread);
				thread.start();
			}

			for(Thread thread :threads)
				thread.join();

		}
		
		Map<String, Integer> ret = new HashMap<>();
		
		for(String key : results.keySet())
			ret.put(key, (Integer) results.get(key));

		return ret;
	}
	
	/**
	 * Get annotations from InterPro.
	 * 
	 * @param genome
	 * @param errorCounter
	 * @param cancel
	 * @param waitingPeriod
	 * @param sequencesCounter
	 * @return
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	public Map<String, InterProResultsList> getInterProAnnotations(Map<String, AbstractSequence<?>> genome, AtomicInteger errorCounter, AtomicBoolean cancel,
			long waitingPeriod, AtomicInteger sequencesCounter, String email) throws InterruptedException, IOException {

		ConcurrentHashMap<String, Object> results = new ConcurrentHashMap<>();
		ConcurrentLinkedQueue<String> requests = new ConcurrentLinkedQueue<>(genome.keySet());
		
		Map<String,String> ec2go = InterProParser.getEc2Go();
		Map<String,String> sl2go = InterProParser.getSl2Go();
		Map<String,String> interpro2go = InterProParser.getInterPro2Go();

		int threadsNumber=50;

		List<Thread> threads = new ArrayList<Thread>();

		if(requests.size()<threadsNumber)
			threadsNumber=requests.size();

		if(genome.size()>0) {

			for(int i=0; i<threadsNumber; i++) {

				Runnable lc	= new EbiRunnable(EbiTool.INTERPRO, requests, genome, results, errorCounter, sequencesCounter, cancel, waitingPeriod, email);
				
				((EbiRunnable) lc).setEc2go(ec2go);
				((EbiRunnable) lc).setSl2go(sl2go);
				((EbiRunnable) lc).setInterpro2go(interpro2go);
				((EbiRunnable) lc).addObserver(this);
				Thread thread = new Thread(lc);
				threads.add(thread);
				thread.start();
			}

			for(Thread thread :threads)
				thread.join();

		}
		
		Map<String, InterProResultsList> ret = new HashMap<>();
		
		for(String key : results.keySet())
			ret.put(key, (InterProResultsList) results.get(key));

		return ret;
	}
	

	/**
	 * Get annotations from InterPro.
	 * 
	 * @param genome
	 * @param waitingPeriod
	 * @param email
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static Map<String, InterProResultsList> getInterProAnnotations(Map<String, AbstractSequence<?>> genome, long waitingPeriod, String email) throws InterruptedException, IOException {

		ConcurrentHashMap<String, Object> results = new ConcurrentHashMap<>();
		ConcurrentLinkedQueue<String> requests = new ConcurrentLinkedQueue<>(genome.keySet());
		
		Map<String,String> ec2go = InterProParser.getEc2Go();
		Map<String,String> sl2go = InterProParser.getSl2Go();
		Map<String,String> interpro2go = InterProParser.getInterPro2Go();

		int threadsNumber=30;

		List<Thread> threads = new ArrayList<Thread>();

		if(requests.size()<threadsNumber)
			threadsNumber=requests.size();

		if(genome.size()>0) {

			for(int i=0; i<threadsNumber; i++) {

				Runnable lc	= new EbiRunnable(EbiTool.INTERPRO, requests, genome, results, waitingPeriod, email);
				
				((EbiRunnable) lc).setEc2go(ec2go);
				((EbiRunnable) lc).setSl2go(sl2go);
				((EbiRunnable) lc).setInterpro2go(interpro2go);
				Thread thread = new Thread(lc);
				threads.add(thread);
				thread.start();
			}

			for(Thread thread :threads)
				thread.join();
		}
		
		Map<String, InterProResultsList> ret = new HashMap<>();
		
		for(String key : results.keySet())
			ret.put(key, (InterProResultsList) results.get(key));

		return ret;
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {

		setChanged();
		notifyObservers();
	}
	
}
