/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.core.datatypes;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author ODias
 *
 */
public class WorkspaceInitialData {
	
	private int compartmentIdentifier;
	private ConcurrentHashMap<String,Integer> genesIdentifier, metabolitesIdentifier, 
	proteinsIdentifier, pathwaysIdentifier, modulesIdentifier,orthologuesIdentifier, similarMetabolitesLoad;
	private ConcurrentHashMap<String, List<Integer>> reactionsIdentifier;
	private ConcurrentHashMap<Integer,Set<String>> reactionsPathway, metabolitesPathway, modulesPathway;
	private ConcurrentHashMap<String,Set<String>> enzymesPathway;
	private ConcurrentLinkedQueue<Integer> reactionsPathwayList, metabolitesPathwayList, modulesPathwayList;
	private ConcurrentLinkedQueue<String> enzymesPathwayList;
	private ConcurrentLinkedQueue<String> enzymesInModel;
	private ConcurrentHashMap<String, List<Integer>> enzymesReactions;
	/**
	 * @return the compartmentIdentifier
	 */
	public int getCompartmentIdentifier() {
		return compartmentIdentifier;
	}
	/**
	 * @param compartmentIdentifier the compartmentIdentifier to set
	 */
	public void setCompartmentIdentifier(int compartmentIdentifier) {
		this.compartmentIdentifier = compartmentIdentifier;
	}
	/**
	 * @return the genesIdentifier
	 */
	public ConcurrentHashMap<String, Integer> getGenesIdentifier() {
		return genesIdentifier;
	}
	/**
	 * @param genesIdentifier the genesIdentifier to set
	 */
	public void setGenesIdentifier(ConcurrentHashMap<String, Integer> genesIdentifier) {
		this.genesIdentifier = genesIdentifier;
	}
	/**
	 * @return the metabolitesIdentifier
	 */
	public ConcurrentHashMap<String, Integer> getMetabolitesIdentifier() {
		return metabolitesIdentifier;
	}
	/**
	 * @param metabolitesIdentifier the metabolitesIdentifier to set
	 */
	public void setMetabolitesIdentifier(ConcurrentHashMap<String, Integer> metabolitesIdentifier) {
		this.metabolitesIdentifier = metabolitesIdentifier;
	}
	/**
	 * @return the proteinsIdentifier
	 */
	public ConcurrentHashMap<String, Integer> getProteinsIdentifier() {
		return proteinsIdentifier;
	}
	/**
	 * @param proteinsIdentifier the proteinsIdentifier to set
	 */
	public void setProteinsIdentifier(ConcurrentHashMap<String, Integer> proteinsIdentifier) {
		this.proteinsIdentifier = proteinsIdentifier;
	}
	/**
	 * @return the reactionsIdentifier
	 */
	public ConcurrentHashMap<String, List<Integer>> getReactionsIdentifier() {
		return reactionsIdentifier;
	}
	/**
	 * @param reactionsIdentifier the reactionsIdentifier to set
	 */
	public void setReactionsIdentifier(ConcurrentHashMap<String, List<Integer>> reactionsIdentifier) {
		this.reactionsIdentifier = reactionsIdentifier;
	}
	/**
	 * @return the pathwaysIdentifier
	 */
	public ConcurrentHashMap<String, Integer> getPathwaysIdentifier() {
		return pathwaysIdentifier;
	}
	/**
	 * @param pathwaysIdentifier the pathwaysIdentifier to set
	 */
	public void setPathwaysIdentifier(ConcurrentHashMap<String, Integer> pathwaysIdentifier) {
		this.pathwaysIdentifier = pathwaysIdentifier;
	}
	/**
	 * @return the modulesIdentifier
	 */
	public ConcurrentHashMap<String, Integer> getModulesIdentifier() {
		return modulesIdentifier;
	}
	/**
	 * @param modulesIdentifier the modulesIdentifier to set
	 */
	public void setModulesIdentifier(ConcurrentHashMap<String, Integer> modulesIdentifier) {
		this.modulesIdentifier = modulesIdentifier;
	}
	/**
	 * @return the orthologuesIdentifier
	 */
	public ConcurrentHashMap<String, Integer> getOrthologuesIdentifier() {
		return orthologuesIdentifier;
	}
	/**
	 * @param orthologuesIdentifier the orthologuesIdentifier to set
	 */
	public void setOrthologuesIdentifier(ConcurrentHashMap<String, Integer> orthologuesIdentifier) {
		this.orthologuesIdentifier = orthologuesIdentifier;
	}
	/**
	 * @return the similarMetabolitesLoad
	 */
	public ConcurrentHashMap<String, Integer> getSimilarMetabolitesLoad() {
		return similarMetabolitesLoad;
	}
	/**
	 * @param similarMetabolitesLoad the similarMetabolitesLoad to set
	 */
	public void setSimilarMetabolitesLoad(ConcurrentHashMap<String, Integer> similarMetabolitesLoad) {
		this.similarMetabolitesLoad = similarMetabolitesLoad;
	}
	/**
	 * @return the reactionsPathway
	 */
	public ConcurrentHashMap<Integer, Set<String>> getReactionsPathway() {
		return reactionsPathway;
	}
	/**
	 * @param reactionsPathway the reactionsPathway to set
	 */
	public void setReactionsPathway(ConcurrentHashMap<Integer, Set<String>> reactionsPathway) {
		this.reactionsPathway = reactionsPathway;
	}
	/**
	 * @return the metabolitesPathway
	 */
	public ConcurrentHashMap<Integer, Set<String>> getMetabolitesPathway() {
		return metabolitesPathway;
	}
	/**
	 * @param metabolitesPathway the metabolitesPathway to set
	 */
	public void setMetabolitesPathway(ConcurrentHashMap<Integer, Set<String>> metabolitesPathway) {
		this.metabolitesPathway = metabolitesPathway;
	}
	/**
	 * @return the modulesPathway
	 */
	public ConcurrentHashMap<Integer, Set<String>> getModulesPathway() {
		return modulesPathway;
	}
	/**
	 * @param modulesPathway the modulesPathway to set
	 */
	public void setModulesPathway(ConcurrentHashMap<Integer, Set<String>> modulesPathway) {
		this.modulesPathway = modulesPathway;
	}
	/**
	 * @return the enzymesPathway
	 */
	public ConcurrentHashMap<String, Set<String>> getEnzymesPathway() {
		return enzymesPathway;
	}
	/**
	 * @param enzymesPathway the enzymesPathway to set
	 */
	public void setEnzymesPathway(ConcurrentHashMap<String, Set<String>> enzymesPathway) {
		this.enzymesPathway = enzymesPathway;
	}
	/**
	 * @return the reactionsPathwayList
	 */
	public ConcurrentLinkedQueue<Integer> getReactionsPathwayList() {
		return reactionsPathwayList;
	}
	/**
	 * @param reactionsPathwayList the reactionsPathwayList to set
	 */
	public void setReactionsPathwayList(ConcurrentLinkedQueue<Integer> reactionsPathwayList) {
		this.reactionsPathwayList = reactionsPathwayList;
	}
	/**
	 * @return the metabolitesPathwayList
	 */
	public ConcurrentLinkedQueue<Integer> getMetabolitesPathwayList() {
		return metabolitesPathwayList;
	}
	/**
	 * @param metabolitesPathwayList the metabolitesPathwayList to set
	 */
	public void setMetabolitesPathwayList(ConcurrentLinkedQueue<Integer> metabolitesPathwayList) {
		this.metabolitesPathwayList = metabolitesPathwayList;
	}
	/**
	 * @return the modulesPathwayList
	 */
	public ConcurrentLinkedQueue<Integer> getModulesPathwayList() {
		return modulesPathwayList;
	}
	/**
	 * @param modulesPathwayList the modulesPathwayList to set
	 */
	public void setModulesPathwayList(ConcurrentLinkedQueue<Integer> modulesPathwayList) {
		this.modulesPathwayList = modulesPathwayList;
	}
	/**
	 * @return the enzymesPathwayList
	 */
	public ConcurrentLinkedQueue<String> getEnzymesPathwayList() {
		return enzymesPathwayList;
	}
	/**
	 * @param enzymesPathwayList the enzymesPathwayList to set
	 */
	public void setEnzymesPathwayList(ConcurrentLinkedQueue<String> enzymesPathwayList) {
		this.enzymesPathwayList = enzymesPathwayList;
	}
	/**
	 * @return the enzymesInModel
	 */
	public ConcurrentLinkedQueue<String> getEnzymesInModel() {
		return enzymesInModel;
	}
	/**
	 * @param enzymesInModel the enzymesInModel to set
	 */
	public void setEnzymesInModel(ConcurrentLinkedQueue<String> enzymesInModel) {
		this.enzymesInModel = enzymesInModel;
	}
	/**
	 * @return the enzymesReactions
	 */
	public ConcurrentHashMap<String, List<Integer>> getEnzymesReactions() {
		return enzymesReactions;
	}
	/**
	 * @param enzymesReactions the enzymesReactions to set
	 */
	public void setEnzymesReactions(ConcurrentHashMap<String, List<Integer>> enzymesReactions) {
		this.enzymesReactions = enzymesReactions;
	}
	
	
	
	
}