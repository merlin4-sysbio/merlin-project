package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.interpro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author João Sequeira and Oscar Dias
 *
 */
public class InterProResultsList {

	private List<InterProResult> results;
	private String mostLikelyEC;
	private String query;
	private String querySequence;
	private String mostLikelyLocalization;
	private String name;
	private String md5;

	public InterProResultsList() {

		this.results = new ArrayList<>();
	}
	
	/**
	 * @return the md5
	 */
	public String getMd5() {
		return md5;
	}
	
	/**
	 * @param md5 the md5 to set
	 */
	public void setMd5(String md5) {
		
		this.md5 = md5;
	}

	/**
	 * @return length of results
	 */
	public int numberOfResults() {
		
		return results.size();
	}

	/**
	 * @return the results
	 */
	public List<InterProResult> getResults() {
		return results;
	}


	/**
	 * @param results the results to set
	 */
	public void addResult(InterProResult result) {
		
		this.results.add(result);
	}
	
	/**
	 * @return the mostLikelyEC
	 */
	public String getMostLikelyEC() {

		if(this.mostLikelyEC== null)
			this.setMostLikelyEC();

		return mostLikelyEC;
	}
	
	/**
	 * @param mostLikelyEC the mostLikelyEC to set
	 */
	public void setMostLikelyEC() {

		HashMap<String,Integer> ecCount = new HashMap<String,Integer>();
		for (InterProResult result:this.results) {

			if (result.getEC() != null) {

				if (!(ecCount.containsKey(result.getEC())))					
					ecCount.put(result.getEC(),1);
				else					
					ecCount.put(result.getEC(), ecCount.get(result.getEC())+1);			//increment by 1 for every occurrence
			}
		}

		int maxCount = 0;
		String maxEC = null;

		for (String ec : ecCount.keySet()) {

			if (ecCount.get(ec) > maxCount) {
				maxEC = ec;
				maxCount = ecCount.get(ec);
			}
		}
		this.mostLikelyEC = maxEC;
	}
	
	/**
	 * @return the query
	 */
	public String getQuery() {

		return query;
	}
	
	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}
	
	/**
	 * @return the querySequence
	 */
	public String getQuerySequence() {
		return querySequence;
	}
	
	/**
	 * @param querySequence the querySequence to set
	 */
	public void setQuerySequence(String querySequence) {
		this.querySequence = querySequence;
	}
	
	/**
	 * @return the mostLikelyLocalization
	 */
	public String getMostLikelyLocalization() {
		
		if(this.mostLikelyLocalization== null)
			this.setMostLikelyLocalization();

		return mostLikelyLocalization;
	}
	
	/**
	 * 
	 */
	public void setMostLikelyLocalization() {

		HashMap<String,Integer> counts = new HashMap<String,Integer>();
		for (InterProResult result:this.results) {

			if (result.getLocalization() != null) {

				if (!(counts.containsKey(result.getLocalization())))
					counts.put(result.getLocalization(),1);
				else
					counts.put(result.getLocalization(), counts.get(result.getLocalization())+1);			//increment by 1 for every occurrence
			}
		}

		int maxCount = 0;
		String maxKey = null;

		for (String key : counts.keySet()) {
			if (counts.get(key) > maxCount) {
				maxKey = key;
				maxCount = counts.get(key);
			}
		}

		this.mostLikelyLocalization = maxKey;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {

		if(this.name == null)
			this.setName();
		
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName() {
		
		HashMap<String,Integer> counts = new HashMap<String,Integer>();
		
		for (InterProResult result:this.results) {
			
			if (result.getGOName() != null){
				if (!(counts.containsKey(result.getGOName())))
					counts.put(result.getGOName(),1);
				else
					counts.put(result.getGOName(), counts.get(result.getGOName())+1);			//increment by 1 for every occurrence
			}
		}

		int maxCount = 0;
		String maxKey = null;

		for (String key : counts.keySet()) {
			if (counts.get(key) > maxCount) {
				maxKey = key;
				maxCount = counts.get(key);
			}
		}

		this.name = maxKey;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InterProResultsList [results=" + results + ", mostLikelyEC=" + this.mostLikelyEC + ", query=" + query
				+ ", querySequence=" + querySequence + ", mostLikelyLocalization=" + this.getMostLikelyLocalization() + ", name="
				+ this.getName() + ", md5=" + md5 + "]";
	}


}
