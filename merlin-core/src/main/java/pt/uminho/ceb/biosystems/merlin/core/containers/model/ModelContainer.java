package pt.uminho.ceb.biosystems.merlin.core.containers.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

/**
 * @author odias
 *
 */
public class ModelContainer {

	private Map<Integer, Integer> identifiers; 
	private Map<Integer,String> namesIndex;
	private Set<String> activeReactions;
	private Map<Integer,String> formulasIndex;
	private List<String> pathwaysList;
	private Map <String, Integer> pathID;
	private Set<String> pathwaysSet;
	private Map<Integer, Pair<Integer, List<Object>>> reactionsData;
	private List<Integer> reactionsOrder;
	
	/**
	 * @param identifiers
	 * @param namesIndex
	 * @param activeReactions
	 * @param formulasIndex
	 * @param pathwaysList
	 * @param pathID
	 * @param pathwaysSet
	 * @param reactionsData
	 * @param reactionsOrder 
	 */
	public ModelContainer(Map<Integer, Integer> identifiers, Map<Integer,String> namesIndex, Set<String> activeReactions, Map<Integer,String> formulasIndex,
			List<String> pathwaysList, Map <String, Integer> pathID, Set<String> pathwaysSet, Map<Integer, Pair<Integer, List<Object>>> reactionsData, List<Integer> reactionsOrder){
		
		this.identifiers = identifiers;
		this.namesIndex = namesIndex;
		this.activeReactions = activeReactions;
		this.formulasIndex = formulasIndex;
		this.pathwaysList = pathwaysList;
		this.pathID = pathID;
		this.pathwaysSet = pathwaysSet;
		this.reactionsData = reactionsData;
		this.setReactionsOrder(reactionsOrder);
	}
	
	public Map<Integer, Integer> getIdentifiers() {
		return identifiers;
	}

	public void setIdentifiers(Map<Integer, Integer> identifiers) {
		this.identifiers = identifiers;
	}

	public Map<Integer, String> getNamesIndex() {
		return namesIndex;
	}

	public void setNamesIndex(Map<Integer, String> namesIndex) {
		this.namesIndex = namesIndex;
	}

	public Set<String> getActiveReactions() {
		return activeReactions;
	}

	public void setActiveReactions(Set<String> activeReactions) {
		this.activeReactions = activeReactions;
	}

	public Map<Integer, String> getFormulasIndex() {
		return formulasIndex;
	}

	public void setFormulasIndex(Map<Integer, String> formulasIndex) {
		this.formulasIndex = formulasIndex;
	}

	public Map<Integer, Pair<Integer, List<Object>>> getReactionsData() {
		return reactionsData;
	}

	public void setReactionsData(Map<Integer, Pair<Integer, List<Object>>> reactionsData) {
		this.reactionsData = reactionsData;
	}

	public List<String> getPathwaysList() {
		return pathwaysList;
	}

	public void setPathwaysList(List<String> pathwaysList) {
		this.pathwaysList = pathwaysList;
	}

	public Map<String, Integer> getPathID() {
		return pathID;
	}

	public void setPathID(Map<String, Integer> pathID) {
		this.pathID = pathID;
	}

	public Set<String> getPathwaysSet() {
		return pathwaysSet;
	}

	public void setPathwaysSet(Set<String> pathwaysSet) {
		this.pathwaysSet = pathwaysSet;
	}

	/**
	 * @return the reactionsOrder
	 */
	public List<Integer> getReactionsOrder() {
		return reactionsOrder;
	}

	/**
	 * @param reactionsOrder the reactionsOrder to set
	 */
	public void setReactionsOrder(List<Integer> reactionsOrder) {
		this.reactionsOrder = reactionsOrder;
	}
	
}
