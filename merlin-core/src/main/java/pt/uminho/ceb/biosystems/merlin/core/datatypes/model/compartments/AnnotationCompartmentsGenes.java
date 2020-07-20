/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.core.datatypes.model.compartments;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ODias
 *
 */
public class AnnotationCompartmentsGenes implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1;
	private int geneID;
	private String gene;
	private boolean dualLocalisation;
	private String primary_location, primary_location_abb;
	private Map<String,Double> secondary_location;
	private Map<String,String> secondary_location_abb;
	private double primary_score;
	private String locusTag;
	
	/**
	 * 
	 */
	public AnnotationCompartmentsGenes() {
	}
	
	/**
	 * @param geneID
	 * @param gene
	 * @param primary_location
	 * @param primary_location_abb
	 * @param primary_score
	 */
	public AnnotationCompartmentsGenes(int geneID, String gene, String primary_location, String primary_location_abb, double primary_score) {
		super();
		this.setPrimary_location_abb(primary_location_abb);
		this.setGene(gene);
		this.setGeneID(geneID);
		this.setDualLocalisation(false);
		this.setPrimary_location(primary_location);
		this.setPrimary_score(primary_score);
		this.setSecondary_location(new HashMap<String, Double>());
		this.setSecondary_location_abb(new HashMap<String, String>());
	}

	
	/**
	 * @param secondary_location
	 * @param secondary_location_abb
	 * @param secondary_score
	 */
	public void addSecondaryLocation(String secondary_location, String secondary_location_abb, double secondary_score){
		this.setDualLocalisation(true);
		this.secondary_location.put(secondary_location, secondary_score);
		this.secondary_location_abb.put(secondary_location, secondary_location_abb);
	}

	/**
	 * @return the geneID
	 */
	public int getGeneID() {
		return geneID;
	}

	/**
	 * @param geneID the geneID to set
	 */
	public void setGeneID(int geneID) {
		this.geneID = geneID;
	}

	/**
	 * @return the gene
	 */
	public String getGene() {
		return gene;
	}

	/**
	 * @param gene the gene to set
	 */
	public void setGene(String gene) {
		this.gene = gene;
	}

	/**
	 * @return the dualLocalisation
	 */
	public boolean isDualLocalisation() {
		return dualLocalisation;
	}

	/**
	 * @param dualLocalisation the dualLocalisation to set
	 */
	public void setDualLocalisation(boolean dualLocalisation) {
		this.dualLocalisation = dualLocalisation;
	}

	/**
	 * @return the primary_location
	 */
	public String getPrimary_location() {
		return primary_location;
	}

	/**
	 * @param primary_location the primary_location to set
	 */
	public void setPrimary_location(String primary_location) {
		this.primary_location = primary_location;
	}

	/**
	 * @return the primary_location_abb
	 */
	public String getPrimary_location_abb() {
		return primary_location_abb;
	}

	/**
	 * @param primary_location_abb the primary_location_abb to set
	 */
	public void setPrimary_location_abb(String primary_location_abb) {
		this.primary_location_abb = primary_location_abb;
	}

	/**
	 * @return the secondary_location
	 */
	public Map<String, Double> getSecondary_location() {
		return secondary_location;
	}

	/**
	 * @param secondary_location the secondary_location to set
	 */
	public void setSecondary_location(Map<String, Double> secondary_location) {
		this.secondary_location = secondary_location;
	}

	/**
	 * @return the secondary_location_abb
	 */
	public Map<String, String> getSecondary_location_abb() {
		return secondary_location_abb;
	}

	/**
	 * @param secondary_location_abb the secondary_location_abb to set
	 */
	public void setSecondary_location_abb(Map<String, String> secondary_location_abb) {
		this.secondary_location_abb = secondary_location_abb;
	}

	/**
	 * @return the primary_score
	 */
	public double getPrimary_score() {
		return primary_score;
	}

	/**
	 * @param primary_score the primary_score to set
	 */
	public void setPrimary_score(double primary_score) {
		this.primary_score = primary_score;
	}

	@Override
	public String toString() {
		return "AnnotationCompartmentsGenes [geneID=" + geneID + ", gene=" + gene + ", dualLocalisation="
				+ dualLocalisation + ", primary_location=" + primary_location + ", primary_location_abb="
				+ primary_location_abb + ", secondary_location=" + secondary_location + ", secondary_location_abb="
				+ secondary_location_abb + ", primary_score=" + primary_score + "]";
	}

	/**
	 * @return the locusTag
	 */
	public String getLocusTag() {
		return locusTag;
	}

	/**
	 * @param locusTag the locusTag to set
	 */
	public void setLocusTag(String locusTag) {
		this.locusTag = locusTag;
	}

	

}
