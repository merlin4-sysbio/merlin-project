/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.processes.annotation.remote;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.jcs.access.exception.InvalidArgumentException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.blast.org.biojava3.ws.alignment.RemotePairwiseAlignmentService;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.blast.org.biojava3.ws.alignment.qblast.NCBIQBlastAlignmentProperties;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.blast.org.biojava3.ws.alignment.qblast.NCBIQBlastOutputFormat;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.blast.org.biojava3.ws.alignment.qblast.NCBIQBlastOutputProperties;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.blast.EbiBlastClientRest;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.uniprot.UniProtScrapers;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.utilities.MySleep;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.enzymes.AnnotationEnzymesHomologuesData;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HmmerRemoteDatabasesEnum;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HomologySearchServer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.Matrix;
import pt.uminho.ceb.biosystems.merlin.processes.annotation.remote.blast.SubmitEbiBlast;
import pt.uminho.ceb.biosystems.merlin.processes.annotation.remote.hmmer.SubmitHMMER;

/**
 * @author ODias
 *
 */
public class HomologueSequencesSearch implements PropertyChangeListener{

	final static Logger logger = LoggerFactory.getLogger(HomologueSequencesSearch.class);

	private static final int _MAX_REQUESTS = 50;
	private static final int _NUMBER_OF_THREADS = 5;
	private ConcurrentLinkedQueue<String> blosum62, blosum80, pam30, pam70, smaller, otherSequences;
	private short gapExtensionPenalty, gapOpenPenalty;
	private Set<String> loadedGenes;
	private String[] organismTaxa;
	private short wordSize;
	private Matrix blastMatrix;
	private Map<String, AbstractSequence<?>> sequenceFile;
	private String organism;
	private AtomicBoolean cancel;
	private boolean similaritySearchProcessAvailable;
	private int sequences_size;
	private ArrayList<Runnable> runnables;
	private AtomicInteger sequencesCounter;
	private ConcurrentHashMap<String,String[]> taxonomyMap;
	private ConcurrentHashMap<String, Boolean> uniprotStar;
	private long latencyWaitingPeriod;
	private boolean uniprotStatus, reBlast = true;
	private int sequencesWithErrors;
	private HomologySearchServer source;
	private String email;

	private double expectedVal;

	private PropertyChangeSupport changes;

	private long organismTaxonomyIdentifier;

	private ConcurrentLinkedQueue<AnnotationEnzymesHomologuesData> resultsList;


	/**
	 * @param resultsList
	 * @param sequences
	 * @param source
	 * @param email
	 * @throws Exception
	 */
	public HomologueSequencesSearch(ConcurrentLinkedQueue<AnnotationEnzymesHomologuesData> resultsList, Map<String, AbstractSequence<?>> sequences, HomologySearchServer source, String email, long organismTaxonomyIdentifier) throws Exception {

		this.changes = new PropertyChangeSupport(this);
		this.gapExtensionPenalty=-1;
		this.gapOpenPenalty=-1;
		this.wordSize=-1;
		this.blastMatrix = null;
		this.organism=null;
		this.sequenceFile = sequences;
		this.cancel = new AtomicBoolean(false);
		this.similaritySearchProcessAvailable=true;
		this.sequencesCounter = new AtomicInteger(0);
		this.taxonomyMap = new ConcurrentHashMap<String, String[]>();
		this.uniprotStar = new ConcurrentHashMap<String, Boolean>();
		this.resultsList = resultsList;
		this.source = source;
		this.email = email;
		this.organismTaxonomyIdentifier = organismTaxonomyIdentifier;
	}


	/**
	 * @param organismTaxonomyIdentifier
	 * @param organismName
	 * @param organismLineage
	 * @param database
	 * @throws Exception
	 */
	public void setTaxonomyNames(String organismName, String organismLineage, HmmerRemoteDatabasesEnum database) throws Exception {

		String[] orgData = new String[2];
		orgData[0] = organismName;
		orgData[1] = organismLineage;
		this.organismTaxa = orgData;

		if(!database.equals(HmmerRemoteDatabasesEnum.pdb))
			this.organismTaxa = this.ebiNewTaxID(organismTaxonomyIdentifier);

		this.taxonomyMap.put(String.valueOf(organismTaxonomyIdentifier), this.organismTaxa);
	}

	/**
	 * @param organismName
	 * @param organismLineage
	 * @throws Exception
	 */
	public void setTaxonomyNames(String organismName, String organismLineage) throws Exception {

		String[] orgData = new String[2];
		orgData[0] = organismName;
		orgData[1] = organismLineage;
		this.organismTaxa = orgData;

		if(HomologySearchServer.EBI.equals(this.source))
			this.organismTaxa = this.ebiNewTaxID(this.organismTaxonomyIdentifier);

		this.taxonomyMap.put(Long.toString(this.organismTaxonomyIdentifier), this.organismTaxa);
	}

	/**
	 * @param map
	 * @return
	 */
	public ConcurrentLinkedQueue<String> getRequestsList(Map<String, AbstractSequence<?>> map) {
		ConcurrentLinkedQueue<String> result = new ConcurrentLinkedQueue<String>();
		String request="";

		String beginning = "%3E", returnCode="%0D%0A";

		if(this.source==HomologySearchServer.EBI) {

			beginning = ">";
			returnCode="\n";
		}

		changes.firePropertyChange("updateLoadedGenes", false, true);

		for(String key: map.keySet()) {

			if(!(this.getLoadedGenes()!=null && this.getLoadedGenes().contains(key))) {
				this.sequences_size++;
				request += beginning+key.trim()+returnCode;
				request += map.get(key).getSequenceAsString()+returnCode;
				result.add(request);
				request="";
			}
		}
		return result;
	} 

	/**
	 * @param program
	 * @param database
	 * @param numberOfAlignments
	 * @param expectedVal
	 * @param requests
	 * @param matrix
	 * @param gapExtensionPenalty
	 * @param gapOpenPenalty
	 * @return
	 * @throws Exception 
	 */
	private int blastProcessSubListEbi(String program, String database, int numberOfAlignments, ConcurrentLinkedQueue<String> requests,
			Matrix matrix, short gapExtensionPenalty, short gapOpenPenalty) throws Exception {

		sequencesWithErrors = 0;

		Map<String,String> queryRIDMap = new HashMap<String, String>();

		NCBIQBlastAlignmentProperties rqb = new NCBIQBlastAlignmentProperties();
		rqb.setBlastProgram(program);
		rqb.setBlastDatabase(database);
		rqb.setBlastExpectEBI(expectedVal);	
		rqb.setBlastMatrix(matrix.toString().toUpperCase());

		if(gapOpenPenalty!=-1)
			rqb.setBlastGapCreation(gapOpenPenalty);

		if(gapExtensionPenalty!=-1)
			rqb.setBlastGapExtension(gapExtensionPenalty);

		rqb.setHitlistSize(numberOfAlignments);

		if(!this.cancel.get()) {

			AtomicInteger errorCounter = new AtomicInteger(0);

			int threadsNumber=0;
			List<Thread> threads = new ArrayList<Thread>();
			this.runnables = new ArrayList<Runnable>();

			if(requests.size()<_NUMBER_OF_THREADS)
				threadsNumber=requests.size();
			else
				threadsNumber=_NUMBER_OF_THREADS;

			List<ConcurrentLinkedQueue<String>> rids = new ArrayList<ConcurrentLinkedQueue<String>>();
			EbiBlastClientRest[] rbwArray = new EbiBlastClientRest[threadsNumber];

			int t=0;
			while( t<threadsNumber) {

				ConcurrentLinkedQueue<String> rid = new ConcurrentLinkedQueue<String>();
				rids.add(t,rid);
				rbwArray[t] = new EbiBlastClientRest(30000, this.email);
				t++;
			}

			t=0;

			int serverErrors = 0;

			while(!requests.isEmpty() && !this.cancel.get()) {

				if(!this.similaritySearchProcessAvailable || this.cancel.get())
					requests.clear();


				String newRid = "";

				try {

					if(!requests.isEmpty()) {

						String query=requests.poll();

						newRid = this.processQuery(query, rbwArray[t], rqb, 0);

						if (newRid == null) {

							requests.add(query);

							serverErrors++;

							if(serverErrors == 3) {

								this.similaritySearchProcessAvailable = false;
								this.setReBlast(true);		//to restart the entire blast process

								MySleep.myWait(60000);
							}
						}
						else {

							rids.get(t).offer(newRid);
							queryRIDMap.put(newRid, query);
							t++;
							if (t >= threadsNumber)
								t = 0;
						}

					}
				}
				catch (Exception e) {

					e.printStackTrace();

				}
			}

			if(this.similaritySearchProcessAvailable  && !this.cancel.get() && queryRIDMap.size()>0) {
				try {
					for(int i=0; i<threadsNumber; i++) {

						Runnable lc	= new SubmitEbiBlast(rbwArray[i], rqb, taxonomyMap, uniprotStar, rids.get(i), resultsList,
								sequencesCounter, errorCounter, cancel, queryRIDMap, organismTaxa, latencyWaitingPeriod, this.organismTaxonomyIdentifier, uniprotStatus, expectedVal);

						((SubmitEbiBlast) lc).addPropertyChangeListener(this);
						Thread thread = new Thread(lc);
						this.runnables.add(lc);
						threads.add(thread);
						logger.info("Start "+i);
						thread.start();


					}

					for(Thread thread :threads)
						thread.join();
				}
				catch (IllegalArgumentException e) {
					this.changes.firePropertyChange("invalidEmail",null, null);

				}
				if(errorCounter.get()>0) {

					sequencesWithErrors += errorCounter.get();
					errorCounter.set(0);
				}
			}
		}

		return sequencesWithErrors;
	}

	/**
	 * @param sequence
	 * @param rbwArray
	 * @param rqb
	 * @param counter
	 * @return
	 */
	private String processQuery (String sequence, RemotePairwiseAlignmentService rbwArray, NCBIQBlastAlignmentProperties rqb, int counter) {

		try {

			MySleep.myWait(3000);
			String newRid = rbwArray.sendAlignmentRequest(sequence,rqb);

			if(newRid == null) {

				if(counter<10) {

					counter++;
					return this.processQuery(sequence, rbwArray, rqb, counter);
				}
				else {

					logger.error("Error getting rid for sequence \t"+sequence);
				}
			}
			else {

				return newRid;
			}

			// http://www.ncbi.nlm.nih.gov/staff/tao/URLAPI/new/node96.html
			// b. For URLAPI scripts, do NOT send requests faster than once every 3 seconds. 

		}
		catch (IOException e) {

			counter++;

			if(counter<3) {

				return this.processQuery(sequence, rbwArray, rqb, counter);
			}
			else {

				logger.warn("IO exception request for "+sequence+" Aborting.");

				e.printStackTrace();

				return null;   	//davide
			}
		}
		catch (Exception e) {

			counter++;

			if(e!=null && e.getMessage()!=null && e.getMessage().contains("NCBI QBlast refused this request because")) {

				if(counter<3) {

					MySleep.myWait(5000);
					return this.processQuery(sequence, rbwArray, rqb, counter);
				}
				else {

					logger.error("NCBI QBlast refused this request for "+sequence+"\n" +
							"because: "+e.getMessage().replace("<ul id=\"msgR\" class=\"msg\"><li class=\"error\"><p class=\"error\">", "")+" Aborting.");
				}
			}
			else if(e!=null && e.getMessage()!=null && e.getMessage().contains("Cannot get RID for alignment")) {

				if(counter<3) {

					MySleep.myWait(5000);
					return this.processQuery(sequence, rbwArray, rqb, counter);
				}
				else {

					logger.warn("Cannot get RID for sequence "+sequence+"\nRetrying query! Trial\t"+counter);
				}
			}
			else {

				if(counter<3) {

					MySleep.myWait(5000);
					return this.processQuery(sequence, rbwArray, rqb, counter);
				}
				else {

					logger.error("Message: "+e.getMessage());
					//e.printStackTrace();
				}
			}

		}

		logger.error("Blast Failed for sequence:\t"+sequence);
		sequencesWithErrors++;

		//Workbench.getInstance().error("Cannot perform Blast at this time, try again later!");
		this.similaritySearchProcessAvailable = false;

		return null;
	}

	/**
	 * @param program
	 * @param database
	 * @param numberOfAlignments
	 * @param expectedVal 
	 * @param eVal
	 * @param eValueAutoAdjust
	 * @param sequenceType
	 * @return
	 * @throws SQLException 
	 * @throws Exception
	 */
	public int blastSequencesEBI(String program, String database, int numberOfAlignments, double expectedVal, boolean eValueAutoAdjust, String sequenceType) throws Exception {

		int errorCount = 0;
		this.expectedVal = expectedVal;

		if(blastMatrix==null) {

			Map<String, AbstractSequence<?>> smaller = new HashMap<>();
			Map<String, AbstractSequence<?>> pam30 = new HashMap<>();
			Map<String, AbstractSequence<?>> pam70 = new HashMap<>();
			Map<String, AbstractSequence<?>> blosum62 = new HashMap<>();
			Map<String, AbstractSequence<?>> blosum80 = new HashMap<>();

			int unitLength = 1;

			if(!program.equalsIgnoreCase("blastp") ) {

				unitLength=3;

				if(expectedVal==1E-10)
					expectedVal = 10;
			}

			for(String key:this.sequenceFile.keySet()) {

				int seqSize = this.sequenceFile.get(key).getLength()/unitLength;

				if(seqSize<15)
					smaller.put(key,this.sequenceFile.get(key));
				else if(seqSize<35)
					pam30.put(key,this.sequenceFile.get(key));
				else if(seqSize<50)
					pam70.put(key,this.sequenceFile.get(key));
				else if(seqSize<85)
					blosum80.put(key,this.sequenceFile.get(key));
				else
					blosum62.put(key,this.sequenceFile.get(key)); 
			}

			if(!this.cancel.get()) {

				blastMatrix = this.selectMatrix(86);

				changes.firePropertyChange("updateLoadedGenes", false, true);
				this.setBlosum62(this.getRequestsList(blosum62));

				if(this.blosum62.size()>0 && !this.cancel.get())
					errorCount +=	this.blastProcessGenesListEbi(this.blosum62, program, database, numberOfAlignments);
			}

			if(!this.cancel.get()) {

				blastMatrix = this.selectMatrix(80);

				changes.firePropertyChange("updateLoadedGenes", false, true);
				this.setBlosum80(this.getRequestsList(blosum80));

				if(this.blosum80.size()>0 && !this.cancel.get())
					errorCount +=	this.blastProcessGenesListEbi(this.blosum80, program, database, numberOfAlignments);
			}

			if(!this.cancel.get()) {

				blastMatrix = this.selectMatrix(40);

				changes.firePropertyChange("updateLoadedGenes", false, true);
				this.setPam70(this.getRequestsList(pam70));

				if(this.pam70.size()>0 && !this.cancel.get())
					errorCount +=this.blastProcessGenesListEbi(this.pam70, program, database, numberOfAlignments);
			}

			double oldEval = expectedVal;

			if(!this.cancel.get()) {

				if (eValueAutoAdjust) {

					expectedVal = 1000;
					logger.info("setting e-value to "+1000+" for <35mer sequences.");
				}
				blastMatrix= this.selectMatrix(30);

				changes.firePropertyChange("updateLoadedGenes", false, true);
				this.setPam30(this.getRequestsList(pam30));
				if(this.pam30.size()>0 && !this.cancel.get())
					errorCount += this.blastProcessGenesListEbi(this.pam30, program, database, numberOfAlignments);
			}

			expectedVal = oldEval;
			if(!this.cancel.get()) {

				if (eValueAutoAdjust) {

					expectedVal = 1000;
					logger.info("setting e-value to "+expectedVal+" for 10-15mer or shorter sequences.");
				}
				blastMatrix= this.selectMatrix(15);

				changes.firePropertyChange("updateLoadedGenes", false, true);
				this.setSmaller(this.getRequestsList(smaller));

				if(this.smaller.size()>0 && !this.cancel.get())
					errorCount += this.blastProcessGenesListEbi(this.smaller,program, database, numberOfAlignments);
			}
		}
		else {

			Map<String, AbstractSequence<?>> query = new HashMap<>();

			for(String key:this.sequenceFile.keySet())
				query.put(key,this.sequenceFile.get(key));

			this.setOtherSequences(this.getRequestsList(query));

			if(this.wordSize == -1)
				this.wordSize=3;

			if(!this.cancel.get())
				errorCount += this.blastProcessGenesListEbi(this.otherSequences,program, database, numberOfAlignments);
		}

		return errorCount;
	}

	/**
	 * @param list
	 * @param program
	 * @param database
	 * @param numberOfAlignments
	 * @param eVal
	 * @param matrix
	 * @param maxRequests
	 * @return
	 * @throws Exception
	 */
	private int blastProcessGenesListEbi(ConcurrentLinkedQueue<String> list, String program, String database, int numberOfAlignments) throws Exception  {

		int errorCount = 0;
		int requests = 0;
		logger.info(blastMatrix+" size "+list.size());
		ConcurrentLinkedQueue<String> sequencesSubmited = new ConcurrentLinkedQueue<String>();

		while(list.size()>0) {

			if(this.cancel.get()) {

				list.clear();
			}
			else {

				sequencesSubmited.add(list.poll());
				requests++;

				if(requests>_MAX_REQUESTS) {

					errorCount += this.blastProcessSubListEbi(program, database, numberOfAlignments, sequencesSubmited, blastMatrix, this.gapExtensionPenalty, this.gapOpenPenalty);
					sequencesSubmited = new ConcurrentLinkedQueue<String>();
					requests = 0;
				}
			}
		}

		if(sequencesSubmited.size()>0 && !this.cancel.get()) {

			errorCount += this.blastProcessSubListEbi(program, database, numberOfAlignments, sequencesSubmited, blastMatrix, this.gapExtensionPenalty, this.gapOpenPenalty);
		}

		return errorCount;
	}

	/**
	 * @param seqLength
	 * @return
	 */
	private Matrix selectMatrix(int seqLength) {

		if(seqLength<16) {

			if(this.wordSize==-1)
				this.wordSize=2; 

			return Matrix.PAM30;
		}
		if(seqLength<35) {

			if(this.wordSize==-1)
				this.wordSize=2; 

			return Matrix.PAM30;
		}
		else if(seqLength<50) {

			if(this.wordSize==-1)
				this.wordSize=3; 

			return Matrix.PAM70;
		}
		else if(seqLength<85) {

			if(this.wordSize==-1)
				this.wordSize=3;

			return Matrix.BLOSUM80;
		}
		else {

			if(this.wordSize==-1)
				this.wordSize=3; 

			return Matrix.BLOSUM62;
		}
	}


	/**
	 * @param database
	 * @param numberOfAlignments
	 * @param expectedVal 
	 * @param eVal
	 * @param uniprotStatus
	 * @return
	 */
	public int hmmerSearchSequences(HmmerRemoteDatabasesEnum database, int numberOfAlignments, double expectedVal, boolean uniprotStatus) {

		int errorCount = 0;
		this.expectedVal = expectedVal;

		try {
			Map<String, ProteinSequence> query = new HashMap<String, ProteinSequence>();
			int maxRequests = 0;

			if(!this.cancel.get()) {

				changes.firePropertyChange("updateLoadedGenes", false, true);

				maxRequests = Runtime.getRuntime().availableProcessors()*2*10;
				query = new HashMap<String, ProteinSequence>();
				this.uniprotStatus = uniprotStatus;
			}

			for(String key:this.sequenceFile.keySet()) {
				if(!this.cancel.get()) {

					if(!(this.getLoadedGenes()!=null && this.getLoadedGenes().contains(key)))
						query.put(key, new ProteinSequence(this.sequenceFile.get(key).getSequenceAsString()));
				}
			}

			errorCount += this.hmmerProcessGenesList(query, database, numberOfAlignments, maxRequests);

		}

		catch(Exception e){e.printStackTrace();return -1;}
		return errorCount;
	}


	/**
	 * @param query
	 * @param database
	 * @param numberOfAlignments
	 * @param maxRequests
	 * @return
	 * @throws Exception
	 */
	private int hmmerProcessGenesList(Map<String, ProteinSequence> query, HmmerRemoteDatabasesEnum database, int numberOfAlignments, int maxRequests) throws Exception{

		int errorCount = 0;
		int requests = 0;
		ConcurrentLinkedQueue<String> list = new ConcurrentLinkedQueue<String>(query.keySet());
		logger.info("HMMER size "+list.size());
		this.sequences_size = list.size();
		ConcurrentLinkedQueue<String> sequencesSubmited = new ConcurrentLinkedQueue<String>();

		while(list.size()>0 && !this.cancel.get()) {

			if(this.cancel.get()) {

				list.clear();
			}
			else {

				sequencesSubmited.add(list.poll());
				requests++;

				if(requests>maxRequests) {

					errorCount += this.hmmerSearchSingleSequence(database, numberOfAlignments, sequencesSubmited, query);
					sequencesSubmited = new ConcurrentLinkedQueue<String>();
					requests = 0;
				}
			}
		}
		if(sequencesSubmited.size()> 0 && !this.cancel.get()) {

			errorCount += this.hmmerSearchSingleSequence(database, numberOfAlignments, sequencesSubmited, query);
		}

		return errorCount;
	}

	/**
	 * @param database
	 * @param numberOfAlignments
	 * @param requests
	 * @param query
	 * @throws InterruptedException
	 */
	private int hmmerSearchSingleSequence(HmmerRemoteDatabasesEnum database, int numberOfAlignments,  ConcurrentLinkedQueue<String> requests, Map<String, ProteinSequence> query) throws InterruptedException {

		sequencesWithErrors = 0;

		if(!this.cancel.get()) {

			int threadsNumber=0;
			int numberOfCores = Runtime.getRuntime().availableProcessors()*2;
			List<Thread> threads = new ArrayList<Thread>();
			this.runnables = new ArrayList<Runnable>();
			if(requests.size()<numberOfCores){threadsNumber=requests.size();}
			else{threadsNumber=numberOfCores;}
			AtomicInteger errorCounter = new AtomicInteger(0);
			AtomicLong time = new AtomicLong(System.currentTimeMillis());


			if(this.similaritySearchProcessAvailable  && !this.cancel.get() && query.size()>0) {

				for(int i=0; i<threadsNumber; i++) {

					Runnable lc	= new SubmitHMMER(taxonomyMap, uniprotStar, requests, query, resultsList, sequencesCounter, errorCounter,cancel, time, organismTaxa, expectedVal, numberOfAlignments, latencyWaitingPeriod, organismTaxonomyIdentifier, uniprotStatus, database);
					((SubmitHMMER) lc).addPropertyChangeListener(this);
					Thread thread = new Thread(lc);
					this.runnables.add(lc);
					threads.add(thread);
					logger.info("Start "+i);
					thread.start();
				}

				for(Thread thread :threads) {

					thread.join();
				}

				if(errorCounter.get()>0) {

					sequencesWithErrors += errorCounter.get();
					errorCounter.set(0);
				}

			}
		}

		return sequencesWithErrors;
	}

	/**
	 * @param blosum62 the blosum62 to set
	 */
	public void setBlosum62(ConcurrentLinkedQueue<String> blosum62) {
		this.blosum62 = blosum62;
	}

	/**
	 * @return the blosum62
	 */
	public ConcurrentLinkedQueue<String> getBlosum62() {
		return blosum62;
	}

	/**
	 * @param blosum80 the blosum80 to set
	 */
	public void setBlosum80(ConcurrentLinkedQueue<String> blosum80) {
		this.blosum80 = blosum80;
	}

	/**
	 * @return the blosum80
	 */
	public ConcurrentLinkedQueue<String> getBlosum80() {
		return blosum80;
	}

	/**
	 * @param pam30 the pam30 to set
	 */
	public void setPam30(ConcurrentLinkedQueue<String> pam30) {
		this.pam30 = pam30;
	}

	/**
	 * @return the pam30
	 */
	public ConcurrentLinkedQueue<String> getPam30() {
		return pam30;
	}

	/**
	 * @param pam70 the pam70 to set
	 */
	public void setPam70(ConcurrentLinkedQueue<String> pam70) {
		this.pam70 = pam70;
	}

	/**
	 * @return the pam70
	 */
	public ConcurrentLinkedQueue<String> getPam70() {
		return pam70;
	}

	/**
	 * @return
	 */
	public Set<String> getLoadedGenes() {
		return loadedGenes;
	}

	/**
	 * @param loadedGenes
	 */
	public void setLoadedGenes(Set<String> loadedGenes) {
		this.loadedGenes = loadedGenes;
	}



	/**
	 * @param orgID
	 * @return
	 * @throws Exception 
	 */
	public String[] ebiNewTaxID(Long orgID) throws Exception {

		try {

			String[] newTax = UniProtScrapers.getUniProtTaxonomy(orgID, 0);

			if(newTax == null)
				throw new IllegalArgumentException("service unavailable. please check your internet connection.");

			return newTax;
		}

		catch (Error e) {

			e.printStackTrace();

			throw new Error("Service unavailable");

		}
		catch (Exception e) {

			this.similaritySearchProcessAvailable = false;
			throw new IllegalArgumentException("Service unavailable. Please check your internet connection.");
		}
	}

	/**
	 * @return the smaller
	 */
	public ConcurrentLinkedQueue<String> getSmaller() {
		return smaller;
	}

	/**
	 * @param smaller the smaller to set
	 */
	public void setSmaller(ConcurrentLinkedQueue<String> smaller) {
		this.smaller = smaller;
	}

	public short getGapExtensionPenalty() {
		return gapExtensionPenalty;
	}

	public void setGapExtensionPenalty(short gapExtensionPenalty) {
		this.gapExtensionPenalty = gapExtensionPenalty;
	}

	public short getGapOpenPenalty() {
		return gapOpenPenalty;
	}

	public void setGapOpenPenalty(short gapOpenPenalty) {
		this.gapOpenPenalty = gapOpenPenalty;
	}

	public short getWordSize() {
		return wordSize;
	}

	public void setWordSize(short wordSize) {
		this.wordSize = wordSize;
	}

	/**
	 * @return the organismTaxa
	 */
	public String[] getOrgArray() {
		return organismTaxa;
	}

	/**
	 * @param organismTaxa the organismTaxa to set
	 */
	public void setOrgArray(String[] orgArray) {
		this.organismTaxa = orgArray;
	}

	/**
	 * @return the blastMatrix
	 */
	public Matrix getBlastMatrix() {
		return blastMatrix;
	}

	/**
	 * @param blastMatrix the blastMatrix to set
	 */
	public void setBlastMatrix(Matrix blastMatrix) {
		this.blastMatrix = blastMatrix;
	}

	/**
	 * @return the otherSequences
	 */
	public ConcurrentLinkedQueue<String> getOtherSequences() {
		return otherSequences;
	}

	/**
	 * @param otherSequences the otherSequences to set
	 */
	public void setOtherSequences(ConcurrentLinkedQueue<String> otherSequences) {
		this.otherSequences = otherSequences;
	}

	/**
	 * @return the organism
	 */
	public String getOrganism() {
		return organism;
	}

	/**
	 * @param organism the organism to set
	 */
	public void setOrganism(String organism) {
		this.organism = organism;
	}

	/**
	 * @return the cancel
	 */
	public AtomicBoolean isCancel() {
		return cancel;
	}

	/**
	 * 
	 */
	public void setCancel() {

		this.cancel.set(true);
		for(Runnable lc :this.runnables) {

			if(lc.getClass().equals(SubmitEbiBlast.class)) {

				((SubmitEbiBlast) lc).setCancel(this.cancel);
			}
			else {

				((SubmitHMMER) lc).setCancel(this.cancel);
			}
		}
	}

	/**
	 * @param uniprotStatus
	 */
	public void setRetrieveUniprotStatus(boolean uniprotStatus) {

		this.uniprotStatus = uniprotStatus;
	}


	public long getLatencyWaitingPeriod() {
		return latencyWaitingPeriod;
	}

	public void setLatencyWaitingPeriod(long latencyWaitingPeriod) {
		this.latencyWaitingPeriod = latencyWaitingPeriod;
	}


	/**
	 * @return the reBlast
	 */
	public boolean isReBlast() {
		return reBlast;
	}


	/**
	 * @param reBlast the reBlast to set
	 */
	public void setReBlast(boolean reBlast) {
		this.reBlast = reBlast;
	}


	/**
	 * @return the similaritySearchProcessAvailable
	 */
	public boolean isSimilaritySearchProcessAvailable() {
		return similaritySearchProcessAvailable;
	}


	/**
	 * @param similaritySearchProcessAvailable the similaritySearchProcessAvailable to set
	 */
	public void setSimilaritySearchProcessAvailable(boolean similaritySearchProcessAvailable) {
		this.similaritySearchProcessAvailable = similaritySearchProcessAvailable;
	}


	/**
	 * @return the sequences_size
	 */
	public int getSequences_size() {
		return sequences_size;
	}


	/**
	 * @param sequences_size the sequences_size to set
	 */
	public void setSequences_size(int sequences_size) {
		this.sequences_size = sequences_size;
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