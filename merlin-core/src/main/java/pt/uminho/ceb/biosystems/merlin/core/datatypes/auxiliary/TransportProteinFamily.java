
package pt.uminho.ceb.biosystems.merlin.core.datatypes.auxiliary;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TransportProteinFamily  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tc_family;
	private Map<String,Double> tc_numbers;
	private Map<String,String> uniprot_ids_tc_numbers;
	private Map<String,String> uniprot_taxonomy;
	private double similarity_sum=0;
	private double taxonomy_sum = 0;


	/**
	 * @param tc_family
	 */
	public TransportProteinFamily(String tc_family){
		this.setTc_family(tc_family);
		this.setTc_numbers(new HashMap<String, Double>());
		this.setEntries_taxonomy(new HashMap<String, String>());
		this.setUniprot_ids_tc_numbers(new HashMap<String, String>());
	}

	/**
	 * @param uniprot_id
	 * @param tc_number
	 * @param similarity
	 * @param id_taxonomy
	 * @param taxonomy
	 */
	public void add_tc_number(String uniprot_id, String tc_number, double similarity, String id_taxonomy, double taxonomy){
		this.uniprot_ids_tc_numbers.put(uniprot_id,tc_number);
		this.tc_numbers.put(uniprot_id, similarity);
		this.uniprot_taxonomy.put(uniprot_id, id_taxonomy);
		this.similarity_sum+=similarity;
		this.taxonomy_sum+=taxonomy;
	}

	/**
	 * @param tc_family the tc_family to set
	 */
	public void setTc_family(String tc_family) {
		this.tc_family = tc_family;
	}
	/**
	 * @return the tc_family
	 */
	public String getTc_family() {
		return tc_family;
	}
	/**
	 * @param tc_numbers the tc_numbers to set
	 */
	public void setTc_numbers(Map<String,Double> tc_numbers) {
		this.tc_numbers = tc_numbers;
	}
	/**
	 * @return the tc_numbers
	 */
	public Map<String,Double> getTc_numbers() {
		return tc_numbers;
	}
	/**
	 * @param similarity_sum the similarity_sum to set
	 */
	public void setSimilarity_sum(double similarity_sum) {
		this.similarity_sum = similarity_sum;
	}
	/**
	 * @return the similarity_sum
	 */
	public double getSimilarity_sum() {
		return similarity_sum;
	}

	/**
	 * @param tc_numbers_taxonomy the tc_numbers_taxonomy to set
	 */
	public void setEntries_taxonomy(Map<String,String> tc_numbers_taxonomy) {
		this.uniprot_taxonomy = tc_numbers_taxonomy;
	}

	/**
	 * @return the tc_numbers_taxonomy
	 */
	public Map<String,String> getEntries_taxonomy() {
		return uniprot_taxonomy;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 10;
		return "ProteinFamily [similarity_sum="
		+ similarity_sum
		+ ", "
		+ (tc_family != null ? "tc_family=" + tc_family + ", " : "")
		+ (tc_numbers != null ? "tc_numbers="
				+ toString(tc_numbers.entrySet(), maxLen) : "") + "]";
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext()
				&& i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

	/**
	 * @param taxonomy_sum the taxonomy_sum to set
	 */
	public void setTaxonomy_sum(double taxonomy_sum) {
		this.taxonomy_sum = taxonomy_sum;
	}

	/**
	 * @return the taxonomy_sum
	 */
	public double getTaxonomy_sum() {
		return taxonomy_sum;
	}

	/**
	 * @return the uniprot_ids_tc_numbers
	 */
	public Map<String,String> getUniprot_ids_tc_numbers() {
		return uniprot_ids_tc_numbers;
	}

	/**
	 * @param uniprot_ids_tc_numbers the uniprot_ids_tc_numbers to set
	 */
	public void setUniprot_ids_tc_numbers(Map<String,String> uniprot_ids_tc_numbers) {
		this.uniprot_ids_tc_numbers = uniprot_ids_tc_numbers;
	}

}
