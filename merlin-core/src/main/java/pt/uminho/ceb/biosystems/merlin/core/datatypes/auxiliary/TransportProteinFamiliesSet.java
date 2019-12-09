package pt.uminho.ceb.biosystems.merlin.core.datatypes.auxiliary;

/**
 * 
 */

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import pt.uminho.ceb.biosystems.merlin.utilities.ValueComparator;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;

/**
 * @author ODias
 *
 */
public class TransportProteinFamiliesSet implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Set<TransportProteinFamily> proteins;
	private String max_score_family; 
	private double max_score_value; 
	private Map<String, Double> tc_families_map, tc_families_above_half;
	private double originTaxonomy, alpha, beta, minimalFrequency;

	/**
	 * @param originTaxonomy
	 * @param alpha
	 * @param beta
	 * @param minimalFrequency
	 */
	public TransportProteinFamiliesSet(double originTaxonomy, double alpha, double beta, double minimalFrequency) {
		this.proteins = new HashSet<TransportProteinFamily>();
		this.originTaxonomy = originTaxonomy;
		this.alpha= alpha;
		this.beta = beta;
		this.minimalFrequency = minimalFrequency;
		this.setMax_score_value(-1);
	}


	/**
	 * @param tc_family
	 * @return
	 */
	public TransportProteinFamily get_protein_family(String tc_family){

		for(TransportProteinFamily proteinFamilyData:this.proteins)
			if(proteinFamilyData.getTc_family().equals(tc_family))
				return proteinFamilyData;

		TransportProteinFamily proteinFamily = new TransportProteinFamily(tc_family);
		this.proteins.add(proteinFamily);
		return proteinFamily;
	}


	/**
	 * 
	 */
	public void calculateTCfamily_score() {
		
		this.setMax_score_value(0);
		double score_sum=0;
		Map<String,Double> scores = new HashMap<String,Double>();

		for(TransportProteinFamily protein_family : this.proteins) {

			double score = this.get_tc_frequency(protein_family.getSimilarity_sum(), this.alpha, protein_family.getTaxonomy_sum(),  this.originTaxonomy, protein_family.getEntries_taxonomy().size(),  this.minimalFrequency,  this.beta);

			if(score>this.getMax_score_value()) {
				
				this.setMax_score_value(score);
				this.setMax_score_family(protein_family.getTc_family());
			}
			
			score_sum+=score;

			scores.put(protein_family.getTc_family(),score);
		}

		this.setMax_score_value(this.getMax_score_value()/score_sum);

		this.setTc_families_above_half(null);
		
		if(this.getMax_score_value()<0.5) {
			
			this.setTc_families_above_half(this.collect_unitl_greater_than(0.5,this.getPercentages(scores, score_sum)));
		}

	}

	/**
	 * @param similarity_sum
	 * @param alpha
	 * @param taxonomy_score_sum
	 * @param originTaxonomy
	 * @param frequency
	 * @param minimal_hits
	 * @param beta_penalty
	 * @return
	 */
	private double get_tc_frequency(double similarity_sum, double alpha, double taxonomy_score_sum, double originTaxonomy, double frequency, double minimal_hits, double beta_penalty){
		if(frequency>minimal_hits)
		{
			minimal_hits= frequency;
		}

		double first_half = similarity_sum *alpha;
		double penalty = (1-(minimal_hits-frequency)*beta_penalty);
		double second_half = (1-alpha)*(taxonomy_score_sum/(originTaxonomy*frequency*penalty));

		return  first_half+second_half;
	}

	/**
	 * @param data
	 * @param sum
	 * @return
	 */
	private Map<String, Double> getPercentages(Map<String,Double> data, double sum){
		Map<String, Double> result = new HashMap<String,Double>();
		String score;
		DecimalFormatSymbols separator = new DecimalFormatSymbols();
		separator.setDecimalSeparator('.');
		DecimalFormat formatter = new DecimalFormat("#.##", separator);
		
		for(String key:data.keySet()) {
			
			score = formatter.format(data.get(key)/sum);
			result.put(key,Double.parseDouble(score.replace(",", ".")));
		}
		return result;
	}


	/**
	 * @return the max_score_family
	 */
	public String getMax_score_family() {
		
		return max_score_family;
	}


	/**
	 * @return the tc_families_abov_half
	 */
	public Map<String, Double> getTc_families_above_half() {
		
		return tc_families_above_half;
	}


	/**
	 * @param tc_families_above_half the tc_families_abov_half to set
	 */
	public void setTc_families_above_half(Map<String, Double> tc_families_above_half) {
		this.tc_families_above_half = tc_families_above_half;
	}


	/**
	 * @param max_score_family the max_score_family to set
	 */
	public void setMax_score_family(String max_score_family) {
		this.max_score_family = max_score_family;
	}


	/**
	 * @return the max_score_value
	 */
	public double getMax_score_value() {
		
		return max_score_value;
	}


	/**
	 * @param max_score_value the max_score_value to set
	 */
	public void setMax_score_value(double max_score_value) {
		this.max_score_value = max_score_value;
	}


	/**
	 * @param threshold
	 * @param data
	 * @return
	 */
	private Map<String, Double> collect_unitl_greater_than(double threshold, Map<String,Double> data){
		
		HashMap<String,Double> map = new HashMap<String,Double>(data);
		
		this.setTc_families_map(new HashMap<String,Double>(data));
		
		ValueComparator vc =  new ValueComparator(map);
		
		TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(vc);
		sorted_map.putAll(data);
		sorted_map.descendingMap();

		IndexedHashMap<String, Double> indexed_sorted_map = new IndexedHashMap<String, Double>(0);
		indexed_sorted_map.putAll(sorted_map);

		IndexedHashMap<String,Double> newMap = new IndexedHashMap<String,Double>();

		double sum = 0;
		for(int i=0;i<indexed_sorted_map.size();i++) {
			
			if(sum<threshold) {
				
				newMap.put(indexed_sorted_map.getKeyAt(i),indexed_sorted_map.getValueAt(i)); 
				sum+=indexed_sorted_map.getValueAt(i);
			}
			else {
				
				i=indexed_sorted_map.size();
			}
		}

		sorted_map = new TreeMap<String,Double>(newMap);
		sorted_map.putAll(newMap);
		sorted_map.descendingMap();
		return sorted_map;
	}

	/**
	 * @param tc
	 * @param classCardinality
	 * @return
	 */
	public static String getTCNumberFamily (String tc, int classCardinality) {
		
		StringTokenizer t = new StringTokenizer(tc, "\\.");
		
		String ret = t.nextToken();
		
		int c = 0;
		
		while(c<classCardinality-1){
			
			c++;
			ret = ret.concat("."+t.nextToken());
		}
		
		return ret;
	} 

	/**
	 * @return the tc_families_map
	 */
	public Map<String, Double> getTc_families_map() {
		return tc_families_map;
	}


	/**
	 * @param tc_families_map the tc_families_map to set
	 */
	public void setTc_families_map(Map<String, Double> tc_families_map) {
		this.tc_families_map = tc_families_map;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProteinFamiliesSet [proteins=" + proteins
				+ ", max_score_family=" + max_score_family
				+ ", max_score_value=" + max_score_value + ", tc_families_map="
				+ tc_families_map + ", tc_families_above_half="
				+ tc_families_above_half + ", originTaxonomy=" + originTaxonomy
				+ ", alpha=" + alpha + ", beta=" + beta + ", minimalFrequency="
				+ minimalFrequency + "]";
	}




}
