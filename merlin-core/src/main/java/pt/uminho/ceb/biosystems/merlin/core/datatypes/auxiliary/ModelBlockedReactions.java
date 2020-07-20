package pt.uminho.ceb.biosystems.merlin.core.datatypes.auxiliary;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Oscar Dias
 *
 */
public class ModelBlockedReactions implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Set<String> compounds;
	private Map<String, Set<String>> reactions;
	private Set<String> neighbourReactions;
	private Map<String, Set<String>> reactionsOriginal;
	private Set<String> neighbourReactionsOriginal;
	private Map<String, String> allReactions;
	
	
	/**
	 * @param compounds
	 * @param reactions
	 * @param neighbourReactions
	 */
	public ModelBlockedReactions(Set<String> compounds, Map<String, Set<String>> reactions,
			Set<String> neighbourReactions, Map<String,String> idsMap) {
		super();
		this.compounds = compounds;
		this.reactions = reactions;
		this.neighbourReactions = neighbourReactions;
		this.allReactions = new HashMap<>();
		
		for(String reactionName: reactions.keySet())
			this.allReactions.put(reactionName, idsMap.get(reactionName));
		
		for(String reactionName: neighbourReactions)
			this.allReactions.put(reactionName, idsMap.get(reactionName));
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
	/**
	 * @return the reactions
	 */
	public Map<String, Set<String>> getReactions() {
		return reactions;
	}
	/**
	 * @param reactions the reactions to set
	 */
	public void setReactions(Map<String, Set<String>> reactions) {
		this.reactions = reactions;
	}
	/**
	 * @return the neighbourReactions
	 */
	public Set<String> getNeighbourReactions() {
		return neighbourReactions;
	}
	/**
	 * @param neighbourReactions the neighbourReactions to set
	 */
	public void setNeighbourReactions(Set<String> neighbourReactions) {
		this.neighbourReactions = neighbourReactions;
	}

	/**
	 * @return the reactionsOriginal
	 */
	public Map<String, Set<String>> getReactionsOriginal() {
		return reactionsOriginal;
	}

	/**
	 * @param reactionsOriginal the reactionsOriginal to set
	 */
	public void setReactionsOriginal(Map<String, Set<String>> reactionsOriginal) {
		this.reactionsOriginal = reactionsOriginal;
	}

	/**
	 * @return the neighbourReactionsOriginal
	 */
	public Set<String> getNeighbourReactionsOriginal() {
		return neighbourReactionsOriginal;
	}

	/**
	 * @param neighbourReactionsOriginal the neighbourReactionsOriginal to set
	 */
	public void setNeighbourReactionsOriginal(Set<String> neighbourReactionsOriginal) {
		this.neighbourReactionsOriginal = neighbourReactionsOriginal;
	}

	public Map<String, String> getAllReactions() {

		return this.allReactions;
	}
	

}
