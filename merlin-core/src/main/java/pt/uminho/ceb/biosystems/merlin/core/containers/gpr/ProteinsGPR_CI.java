package pt.uminho.ceb.biosystems.merlin.core.containers.gpr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.utilities.Utilities;

public class ProteinsGPR_CI {

	private String ecnumber;
	private String definition;
	private Set<Set<String>> rules;
	private Map<String, Set<String>> locusTags;
	private boolean isECnumberValid;

	/**
	 * @param ecnumber
	 * @param definition
	 */
	public ProteinsGPR_CI(String ecnumber, String definition) {
		super();
		this.ecnumber = ecnumber;
		this.definition = definition;
		this.rules = new HashSet<>();
		this.locusTags = new HashMap<>();
		this.isECnumberValid = false;
	}

	/**
	 * @param subunits
	 */
	public void addSubunit(String[] subunits) {

		for(String sub : subunits) {

			Set<String> set = new HashSet<String>();

			for(String s : sub.split(" AND ")) {

				set.add(s.trim());
			}

			this.rules.add(set);
		}
	}

	/**
	 * @return
	 */
	public String getGeneRule() {

		String ret = null;

		if(this.isECnumberValid()) {

			Set<Set<String>> new_rules = new HashSet<>();

			for (Set<String> orthologs : this.rules) {

				//for each OR rule
				//every ortholog in the next set has an AND rule which comes from the orthologs set
				Set<Set<String>> gene_rule = new HashSet<>();

				if(this.locusTags.keySet().containsAll(orthologs)) {

					for(String ko : orthologs ) {

							gene_rule = this.addSets(gene_rule, this.locusTags.get(ko));
					}
				}

				new_rules.addAll(gene_rule);
			}

			this.rules = new_rules;

			ret = Utilities.parseRuleToString(new_rules);
		}

		return ret;
	}
	
	/**
	 * @param ortholog
	 * @param locusTag
	 */
	public void addLocusTag(String ortholog, String idGene){

		Set<String> locus = new HashSet<>();
		
		if(this.locusTags.containsKey(ortholog))
			locus = this.getLocusTags().get(ortholog);

		locus.add(idGene);

		this.locusTags.put(ortholog, locus);

	}
	
	/**
	 * @return the ecnumber
	 */
	public String getEcnumber() {
		return ecnumber;
	}

	/**
	 * @param ecnumber the ecnumber to set
	 */
	public void setEcnumber(String ecnumber) {
		this.ecnumber = ecnumber;
	}

	/**
	 * @return the definition
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * @param definition the definition to set
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	/**
	 * @return the subunits
	 */
	public Set<Set<String>> getRules() {
		return rules;
	}

	/**
	 * @param rules the subunits to set
	 */
	public void setSubunits(Set<Set<String>> rules) {
		this.rules = rules;
	}

	/**
	 * @return the locusTags
	 */
	public Map<String, Set<String>> getLocusTags() {
		return locusTags;
	}

	/**
	 * @param locusTags the locusTags to set
	 */
	public void setLocusTags(Map<String, Set<String>> locusTags) {
		this.locusTags = locusTags;
	}

	/**
	 * @return the isECnumberValid
	 */
	public boolean isECnumberValid() {

		if(this.isECnumberValid)
			return this.isECnumberValid;

		for (Set<String> rule : this.rules) {

			if(this.locusTags.keySet().containsAll(rule)) {

				this.isECnumberValid = true;
				return this.isECnumberValid;
			}
		}

		return isECnumberValid;
	}

	/**
	 * @param newSet
	 * @param oldSet
	 * @return
	 */
	private Set<Set<String>> addSets(Set<Set<String>> newSet, Set<String> oldSet) {

		Set<Set<String>> ret = new HashSet<>();
		
		if(newSet.isEmpty())
			ret = this.addSet(newSet, oldSet);

		for(Set<String> set : newSet) {

			for(String add : oldSet) {

//				if(set.contains(add)) {
//					
//					Set<String> retSet = new HashSet<>();
//					retSet.addAll(set);
//					retSet.add(add);
//					ret.add(retSet);
//				}
//				else {

					Set<String> retSet = new HashSet<>();
					retSet.addAll(set);
					retSet.add(add);
					ret.add(retSet);
//				}
			}
		}

		return ret;
	}

	/**
	 * @param newSet
	 * @param oldSet
	 * @return
	 */
	private Set<Set<String>> addSet(Set<Set<String>> newSet, Set<String> oldSet) {

		Set<Set<String>> ret = new HashSet<>();

		for(String add : oldSet) {

			Set<String> retSet = new HashSet<>();
			retSet.add(add);
			ret.add(retSet);
		}

		return ret;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProteinsGPR_CI ["
				+ (ecnumber != null ? "ecnumber=" + ecnumber + ", " : "")
				+ (definition != null ? "definition=" + definition + ", " : "")
				+ (rules != null ? "subunits=" + rules + ", " : "")
				+ (locusTags != null ? "locusTags=" + locusTags + ", " : "")
				+ "isECnumberValid=" + isECnumberValid() + "]";
	}


}
