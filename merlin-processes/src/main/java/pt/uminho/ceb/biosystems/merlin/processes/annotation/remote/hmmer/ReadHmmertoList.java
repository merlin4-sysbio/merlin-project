/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.processes.annotation.remote.hmmer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.hmmer.HmmerFetch;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.hmmer.HmmerResult;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.hmmer.HmmerScan;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.hmmer.HmmerSet;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HmmerRemoteDatabasesEnum;

/**
 * @author ODias
 *
 */
public class ReadHmmertoList {

	final static Logger logger = LoggerFactory.getLogger(ReadHmmertoList.class);

	private HmmerSet hmmJobResults;
	private HmmerScan hmmerStub;
	private String program;
	private int maxNumberofAlignments;
	private List<String> results; 
	private Map<String, Double> eValues, scores;
	private double maxEValue;
	private AtomicBoolean cancel;
	private HmmerRemoteDatabasesEnum database;

	/**
	 * 
	 */
	public ReadHmmertoList() {

	}

	/**
	 * @param seq
	 * @param header
	 * @param database
	 * @param maxNumberofAlignments
	 * @param maxEvalue
	 * @param cancel
	 * @param email
	 */
	public ReadHmmertoList(String seq, String header, HmmerRemoteDatabasesEnum database, 
			int maxNumberofAlignments, double maxEvalue, AtomicBoolean cancel) {//, String email) {

		this.hmmerStub = new HmmerScan(seq, header, database.toString());//, email);
		this.setDatabase(database);
		this.program = "hmmer";
		this.maxEValue = maxEvalue;
		this.maxNumberofAlignments = maxNumberofAlignments;
		this.scores = new HashMap<String, Double>();
		this.eValues = new HashMap<String, Double>();
		this.cancel = cancel;
	}

	/**
	 * @throws Exception 
	 * 
	 */
	public boolean scan(String jobID) throws Exception {

		try {

			this.hmmJobResults = new HmmerSet(HmmerFetch.fetch(jobID, this.maxNumberofAlignments));

			this.results = new ArrayList<String>();

			for (int i = 0; i < this.hmmJobResults.getResults().size(); i++) {

				HmmerResult hmmRes = this.hmmJobResults.getResults().get(i);

				if(this.maxEValue<0 || hmmRes.getEval()<=this.maxEValue) {

					String id = hmmRes.getAcc();
					id = id.split(",")[0].trim();
					this.scores.put(id, hmmRes.getScore());
					this.eValues.put(id, hmmRes.getEval());

					if(this.getDatabase().equals(HmmerRemoteDatabasesEnum.pdb))
						this.results.add(hmmRes.getGI());
					else
						this.results.add(hmmRes.getAcc());

				}
				else {

					i = hmmJobResults.getResults().size();
				}
			}
			return !this.cancel.get();
	}
	catch(JSONException e) {

		logger.warn("Waiting for results on query "+jobID);
	}
	return false;
}



/**
 * @param date
 * @param cancel
 * @return
 * @throws IOException
 */
public String getJobID() throws IOException {

	return hmmerStub.hmmerSubmitJob();
}

/**
 * @throws Exception 
 * 
 */
public static void deleteJob(String jobID) throws Exception{

	HmmerFetch.delete(jobID);
}

/**
 * @return
 */
public String getQuery() {

	return this.hmmerStub.getHeader();
}

/**
 * @return
 */
public String getDatabaseId() {
	return this.hmmerStub.getDatabase();
}

/**
 * @return
 */
public String getVersion() {
	return "";
}

/**
 * @return
 */
public String getProgram() {
	return this.program;
}

/**
 * @return
 */
public List<String> getResults() {
	return this.results;	
}

/**
 * @return
 */
public Map<String, Double> getScores() {
	return this.scores;
}

/**
 * @return
 */
public Map<String, Double> getEValues() {
	return this.eValues; 
}

/**
 * @return the database
 */
public HmmerRemoteDatabasesEnum getDatabase() {
	return database;
}

/**
 * @param database the database to set
 */
public void setDatabase(HmmerRemoteDatabasesEnum database) {
	this.database = database;
}
}
