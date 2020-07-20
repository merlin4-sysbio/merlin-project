package pt.uminho.ceb.biosystems.merlin.core.datatypes.auxiliary;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Oscar Dias
 *
 */
public class ModelReactionsMetabolitesEnzymesSets {
	
	private Set<String> enzymes;
	private Set<String> reactions = new HashSet<String>();
	private Set<String> compounds = new HashSet<>();

	public ModelReactionsMetabolitesEnzymesSets(){
		
	}
	
	/**
	 * @return the enzymes
	 */
	public Set<String> getEnzymes() {
		return enzymes;
	}
	/**
	 * @param enzymes the enzymes to set
	 */
	public void setEnzymes(Set<String> enzymes) {
		this.enzymes = enzymes;
	}
	/**
	 * @return the reactions
	 */
	public Set<String> getReactions() {
		return reactions;
	}
	/**
	 * @param reactions the reactions to set
	 */
	public void setReactions(Set<String> reactions) {
		this.reactions = reactions;
	}
	/**
	 * @return the compounds
	 */
	public Set<String> getCompounds() {
		return compounds;
	}
	/**
	 * @param compounds the compounds to set
	 */
	public void setCompounds(Set<String> compounds) {
		this.compounds = compounds;
	}

}
