package pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.enzymes;

import java.util.List;

public class AnnotationEnzymesRowInfo {
	
	private List<List<String>> homologyResults, taxonomyResults, interProResults, homologySearchSetup;
	private boolean interproAvailable;
	private String sequence;
	
	/**
	 * @param homologyResults
	 * @param taxonomyResults
	 * @param interProResults
	 * @param homologySearchSetup
	 * @param interproAvailable
	 * @param sequence
	 */
	public AnnotationEnzymesRowInfo(List<List<String>> homologyResults, List<List<String>> taxonomyResults,
			List<List<String>> interProResults, List<List<String>> homologySearchSetup,
			boolean interproAvailable, String sequence) {
		super();
		this.homologyResults = homologyResults;
		this.taxonomyResults = taxonomyResults;
		this.interProResults = interProResults;
		this.homologySearchSetup = homologySearchSetup;
		this.interproAvailable = interproAvailable;
		this.sequence = sequence;
	}

	/**
	 * @return the homologyResults
	 */
	public List<List<String>> getHomologyResults() {
		return homologyResults;
	}

	/**
	 * @param homologyResults the homologyResults to set
	 */
	public void setHomologyResults(List<List<String>> homologyResults) {
		this.homologyResults = homologyResults;
	}

	/**
	 * @return the taxonomyResults
	 */
	public List<List<String>> getTaxonomyResults() {
		return taxonomyResults;
	}

	/**
	 * @param taxonomyResults the taxonomyResults to set
	 */
	public void setTaxonomyResults(List<List<String>> taxonomyResults) {
		this.taxonomyResults = taxonomyResults;
	}

	/**
	 * @return the interProResults
	 */
	public List<List<String>> getInterProResults() {
		return interProResults;
	}

	/**
	 * @param interProResults the interProResults to set
	 */
	public void setInterProResults(List<List<String>> interProResults) {
		this.interProResults = interProResults;
	}

	/**
	 * @return the homologySearchSetup
	 */
	public List<List<String>> getHomologySearchSetup() {
		return homologySearchSetup;
	}

	/**
	 * @param homologySearchSetup the homologySearchSetup to set
	 */
	public void setHomologySearchSetup(List<List<String>> homologySearchSetup) {
		this.homologySearchSetup = homologySearchSetup;
	}

	/**
	 * @return the interproAvailable
	 */
	public boolean isInterproAvailable() {
		return interproAvailable;
	}

	/**
	 * @param interproAvailable the interproAvailable to set
	 */
	public void setInterproAvailable(boolean interproAvailable) {
		this.interproAvailable = interproAvailable;
	}

	/**
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

}
