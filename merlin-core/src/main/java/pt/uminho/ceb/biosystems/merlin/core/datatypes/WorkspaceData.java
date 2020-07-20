/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.core.datatypes;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ModuleContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwaysHierarchyContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;

/**
 * @author ODias
 *
 */
public class WorkspaceData {

	private ConcurrentLinkedQueue<MetaboliteContainer> resultMetabolites;
	private ConcurrentLinkedQueue<ProteinContainer> resultEnzymes;
	private ConcurrentLinkedQueue<ReactionContainer> resultReactions;
	private ConcurrentLinkedQueue<GeneContainer> resultGenes;
	private ConcurrentLinkedQueue<ModuleContainer> resultModules;
	private ConcurrentLinkedQueue<PathwaysHierarchyContainer> keggPathwaysHierarchy;
	private ConcurrentLinkedQueue<String> orthologueEntities,compoundsWithBiologicalRoles;
	private String organismID;
	private ConcurrentLinkedQueue<CompartmentContainer> resultCompartments;
	private ConcurrentLinkedQueue<PathwaysHierarchyContainer>resultPathwaysHierarchy;
	private String biomassReaction;
	private Map<String, String> locusTagsToProteinIdsMap;
	
	/**
	 * @param resultMetabolites
	 * @param resultEnzymes
	 * @param resultReactions
	 * @param resultGenes
	 * @param resultModules
	 * @param keggPathwaysHierarchy
	 * @param orthologueEntities
	 * @param compoundsWithBiologicalRoles
	 * @param organismID
	 */
	public WorkspaceData(ConcurrentLinkedQueue<MetaboliteContainer> resultMetabolites,
			ConcurrentLinkedQueue<ProteinContainer> resultEnzymes,
			ConcurrentLinkedQueue<ReactionContainer> resultReactions, ConcurrentLinkedQueue<GeneContainer> resultGenes,
			ConcurrentLinkedQueue<ModuleContainer> resultModules,
			ConcurrentLinkedQueue<PathwaysHierarchyContainer> keggPathwaysHierarchy,
			ConcurrentLinkedQueue<String> orthologueEntities,
			ConcurrentLinkedQueue<String> compoundsWithBiologicalRoles, String organismID) {
		super();
		this.resultMetabolites = resultMetabolites;
		this.resultEnzymes = resultEnzymes;
		this.resultReactions = resultReactions;
		this.resultGenes = resultGenes;
		this.resultModules = resultModules;
		this.keggPathwaysHierarchy = keggPathwaysHierarchy;
		this.orthologueEntities = orthologueEntities;
		this.compoundsWithBiologicalRoles = compoundsWithBiologicalRoles;
		this.organismID = organismID;
	}

	public WorkspaceData() {
		
		this.resultReactions = new ConcurrentLinkedQueue<>();
		this.resultGenes = new ConcurrentLinkedQueue<>();
		this.resultMetabolites = new ConcurrentLinkedQueue<>();
		this.resultEnzymes = new ConcurrentLinkedQueue<>();
		this.resultCompartments = new ConcurrentLinkedQueue<>();
		this.resultPathwaysHierarchy = new ConcurrentLinkedQueue<>();
	}

	/**
	 * @return the resultMetabolites
	 */
	public ConcurrentLinkedQueue<MetaboliteContainer> getResultMetabolites() {
		return resultMetabolites;
	}

	/**
	 * @param resultMetabolites the resultMetabolites to set
	 */
	public void setResultMetabolites(ConcurrentLinkedQueue<MetaboliteContainer> resultMetabolites) {
		this.resultMetabolites = resultMetabolites;
	}

	/**
	 * @return the resultEnzymes
	 */
	public ConcurrentLinkedQueue<ProteinContainer> getResultEnzymes() {
		return resultEnzymes;
	}

	/**
	 * @param resultEnzymes the resultEnzymes to set
	 */
	public void setResultEnzymes(ConcurrentLinkedQueue<ProteinContainer> resultEnzymes) {
		this.resultEnzymes = resultEnzymes;
	}

	/**
	 * @return the resultReactions
	 */
	public ConcurrentLinkedQueue<ReactionContainer> getResultReactions() {
		return resultReactions;
	}

	/**
	 * @param resultReactions the resultReactions to set
	 */
	public void setResultReactions(ConcurrentLinkedQueue<ReactionContainer> resultReactions) {
		this.resultReactions = resultReactions;
	}

	/**
	 * @return the resultGenes
	 */
	public ConcurrentLinkedQueue<GeneContainer> getResultGenes() {
		return resultGenes;
	}

	/**
	 * @param resultGenes the resultGenes to set
	 */
	public void setResultGenes(ConcurrentLinkedQueue<GeneContainer> resultGenes) {
		this.resultGenes = resultGenes;
	}

	/**
	 * @return the resultModules
	 */
	public ConcurrentLinkedQueue<ModuleContainer> getResultModules() {
		return resultModules;
	}

	/**
	 * @param resultModules the resultModules to set
	 */
	public void setResultModules(ConcurrentLinkedQueue<ModuleContainer> resultModules) {
		this.resultModules = resultModules;
	}

	/**
	 * @return the keggPathwaysHierarchy
	 */
	public ConcurrentLinkedQueue<PathwaysHierarchyContainer> getKeggPathwaysHierarchy() {
		return keggPathwaysHierarchy;
	}

	/**
	 * @param keggPathwaysHierarchy the keggPathwaysHierarchy to set
	 */
	public void setKeggPathwaysHierarchy(ConcurrentLinkedQueue<PathwaysHierarchyContainer> keggPathwaysHierarchy) {
		this.keggPathwaysHierarchy = keggPathwaysHierarchy;
	}

	/**
	 * @return the orthologueEntities
	 */
	public ConcurrentLinkedQueue<String> getOrthologueEntities() {
		return orthologueEntities;
	}

	/**
	 * @param orthologueEntities the orthologueEntities to set
	 */
	public void setOrthologueEntities(ConcurrentLinkedQueue<String> orthologueEntities) {
		this.orthologueEntities = orthologueEntities;
	}

	/**
	 * @return the compoundsWithBiologicalRoles
	 */
	public ConcurrentLinkedQueue<String> getCompoundsWithBiologicalRoles() {
		return compoundsWithBiologicalRoles;
	}

	/**
	 * @param compoundsWithBiologicalRoles the compoundsWithBiologicalRoles to set
	 */
	public void setCompoundsWithBiologicalRoles(ConcurrentLinkedQueue<String> compoundsWithBiologicalRoles) {
		this.compoundsWithBiologicalRoles = compoundsWithBiologicalRoles;
	}

	/**
	 * @return the organismID
	 */
	public String getOrganismID() {
		return organismID;
	}

	/**
	 * @param organismID the organismID to set
	 */
	public void setOrganismID(String organismID) {
		this.organismID = organismID;
	}

	public ConcurrentLinkedQueue<CompartmentContainer> getResultCompartments() {
		
		return this.resultCompartments;
	}

	/**
	 * @return the getResultPathwaysHierarchy
	 */
	public ConcurrentLinkedQueue<PathwaysHierarchyContainer> getResultPathwaysHierarchy() {
		return resultPathwaysHierarchy;
	}

	/**
	 * @param getResultPathwaysHierarchy the getResultPathwaysHierarchy to set
	 */
	public void setGetResultPathwaysHierarchy(ConcurrentLinkedQueue<PathwaysHierarchyContainer> resultPathwaysHierarchy) {
		this.resultPathwaysHierarchy = resultPathwaysHierarchy;
	}

	/**
	 * @return the biomassReaction
	 */
	public String getBiomassReaction() {
		return biomassReaction;
	}

	/**
	 * @return the locusTagsToProteinIdsMap
	 */
	public Map<String, String> getLocusTagsToProteinIdsMap() {
		return locusTagsToProteinIdsMap;
	}

	/**
	 * @param locusTagsToProteinIdsMap the locusTagsToProteinIdsMap to set
	 */
	public void setLocusTagsToProteinIdsMap(Map<String, String> locusTagsToProteinIdsMap) {
		this.locusTagsToProteinIdsMap = locusTagsToProteinIdsMap;
	}
	
}
