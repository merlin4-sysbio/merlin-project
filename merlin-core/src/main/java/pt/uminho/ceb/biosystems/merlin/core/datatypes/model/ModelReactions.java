package pt.uminho.ceb.biosystems.merlin.core.datatypes.model;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.auxiliary.ModelReactionsMetabolitesEnzymesSets;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.model.datatables.ModelPathwayReactions;
import pt.uminho.ceb.biosystems.merlin.core.interfaces.IEntity;

/**
 * @author ODias
 *
 */
public class ModelReactions extends WorkspaceEntity implements IEntity{

	private Map<Integer,String> formulasIndex;
	private Map<Integer, Integer> selectedPathIndexID;
	private String[] pathwaysList;
	private Set<String> activeReactions;
	private boolean newBlockedReaction;
	private ModelPathwayReactions reactionsData;
	private String[] enzymes, modelGenes, enzymesModel;
	private Map<String, Integer> modelGenesIdentifier;
	private Map<String, Integer> genesModelMap;
	private Set<String> enzymesForReaction;
	private String[] pathways;
	private String[] allPathways;
	private int pathwayIdentifier;
	private String pathwayCode;
	private ReactionContainer reactionContainer;
	private Map<String, MetaboliteContainer> metabolitesMap;
	private Map<Integer, MetaboliteContainer> allMetabolites;
	private ModelReactionsMetabolitesEnzymesSets enzymesIdentifiersList;
	private ModelReactionsMetabolitesEnzymesSets reactionsList;
	private String[] compartments;
	
	/**
	 * @param dbt
	 * @param name
	 */
	public ModelReactions(WorkspaceTable dbt, String name) {

		super(dbt, name);
		this.selectedPathIndexID = new TreeMap<Integer, Integer>();
		this.setNewBlockedReaction(false);
	}


	/**
	 * @param encodedOnly
	 * @param completeOnly
	 * @return	 */
	public ModelPathwayReactions getReactionsData() {
		
		return this.reactionsData;
	}

	public void setReactionsData(ModelPathwayReactions reactionsData) {
		
		this.reactionsData = reactionsData;
	}


	/**
	 * @return
	 */
	public Map<String, Integer> getGenesModelID() {

		return modelGenesIdentifier;
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Integer> getGenesModelMap() {

		return this.genesModelMap;
	}
	

	public void setGenesModelMap(Map<String, Integer> genesModelMap) {
		
		this.genesModelMap = genesModelMap;
	}

	/**
	 * @return
	 */
	public String[] getEnzymesModel() {
		
		return this.enzymesModel;
	}

	/**
	 * @param rowID
	 * @return
	 */
	public Set<String> getEnzymesForReaction() {

		return this.enzymesForReaction;
	}
	
	/**
	 * @param rowID
	 * @return
	 */
	public void setEnzymesForReaction(Set<String> enzymesForReaction) {

		this.enzymesForReaction = enzymesForReaction;
	}

	/**
	 * @param rowID
	 * @return
	 */
	public String[] getPathways() {

		return this.pathways;
	}
	
	/**
	 * @param rowID
	 * @return
	 */
	public void setPathways(String[] pathways) {

		this.pathways = pathways;
	}

	/**
	 * @return list of all pathways, including superpathways
	 */
	public String[] getAllPathways() {

		return this.allPathways; 
	}
	
	/**
	 * set list of all pathways, including superpathways
	 * @param allPathways
	 */
	public void setAllPathways(String[] allPathways) {

		this.allPathways = allPathways; 
	}

	/**
	 * @param name
	 * @return
	 */
	public int getPathwayID() {

		return this.pathwayIdentifier;
	}

	/**
	 * @param name
	 * @return
	 */
	public void setPathwayID(int pathwayIdentifier) {

		this.pathwayIdentifier = pathwayIdentifier;
	}

	/**
	 * @param name
	 * @return
	 */
	public String getPathwayCode() {

		return this.pathwayCode;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public void setPathwayCode(String pathwayCode) {

		this.pathwayCode = pathwayCode;
	}

	/**
	 * @param identifier
	 * @return
	 */
	public String getReactionName(int identifier) {

		return this.getNameFromIndex(identifier);
	}

	/**
	 * @param identifier
	 * @return
	 */
	public String getFormula(int identifier) {

		if(this.formulasIndex.containsKey(identifier))
			return this.formulasIndex.get(identifier);
		else
			return null;
	}


	/**
	 * @param rowID
	 * @return
	 */
	public ReactionContainer getReaction() {
		
		return this.reactionContainer;
	}

	/**
	 * @param rowID
	 * @return
	 */
	public void getReaction(ReactionContainer reactionContainer) {
		
		this.reactionContainer = reactionContainer;
	}
	
		
	/**
	 * @param rowID
	 * @return
	 */
	public void setMetabolites(Map<String, MetaboliteContainer> metabolitesMap) {

		this.metabolitesMap = metabolitesMap;
	}
	
	/**
	 * @param rowID
	 * @return
	 */
	public Map<String, MetaboliteContainer> getMetabolites() {
		
		return metabolitesMap;
	}


	/**
	 * @return
	 * @throws Exception 
	 */
	public Map<Integer, MetaboliteContainer> getAllMetabolites() throws Exception {

		return allMetabolites;
	}
	
	/**
	 * @return
	 */
	public void setAllMetabolites(Map<Integer, MetaboliteContainer> allMetabolites) {

		this.allMetabolites = allMetabolites;
	}

	/**
	 * @param pathwayID
	 * @return
	 */
	public ModelReactionsMetabolitesEnzymesSets getEnzymesIdentifiersList() {
		
		return this.enzymesIdentifiersList;
	}
	
	/**
	 * @param reactionsCompoundsEnzymesSets
	 */
	public void getEnzymesIdentifiersList(ModelReactionsMetabolitesEnzymesSets enzymesIdentifiersList) {
		
		this.enzymesIdentifiersList = enzymesIdentifiersList;
	}

	/**
	 * @param noEnzymes 
	 * @param pathwayID
	 * @return
	 */
	public ModelReactionsMetabolitesEnzymesSets getReactionsList() {
		
		return this.reactionsList; 
	}
	
	/**
	 * @param noEnzymes 
	 * @param pathwayID
	 * @return
	 */
	public void setReactionsList(ModelReactionsMetabolitesEnzymesSets reactionsList) {
		
		this.reactionsList = reactionsList;
	}

	/**
	 * @return
	 */
	public String[] getCompartments() {
		
		return this.compartments;
	}
	
	/**
	 * @return
	 */
	public void setCompartments(String[] compartments) {
		
		this.compartments = compartments;
	}

	/**
	 * @return the selectedPathIndexID
	 */
	public Map<Integer, Integer> getSelectedPathIndexID() {
		return selectedPathIndexID;
	}

	/**
	 * @return
	 */
	public String[] getPathwaysList() {

		return pathwaysList;
	}


	/**
	 * @param pathwaysList
	 */
	public void setPathwaysList(String[] paths) {
		this.pathwaysList = paths;
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getName()
	 */
	public String getName() {

		return "reactions";
	}

	/**
	 * @return the activeReactions
	 */
	public Set<String> getActiveReactions() {
		return activeReactions;
	}


	/**
	 * @param activeReactions the activeReactions to set
	 */
	public void setActiveReactions(Set<String> activeReactions) {
		this.activeReactions = activeReactions;
	}


	public Map<Integer, String> getFormulasIndex() {
		return this.formulasIndex;
	}


	public void setFormulasIndex(Map<Integer, String> formulasIndex) {
		this.formulasIndex = formulasIndex;
	}


	public void setSelectedPathIndexID(Map<Integer, Integer> selectedPathIndexID) {
		this.selectedPathIndexID = selectedPathIndexID;
	}


	/**
	 * @return the enzymes
	 */
	public String[] getEnzymes() {
		return enzymes;
	}


	/**
	 * @param enzymes the enzymes to set
	 */
	public void setEnzymes(String[] enzymes) {
		this.enzymes = enzymes;
	}


	/**
	 * @return the modelGenes
	 */
	public String[] getModelGenes() {
		return modelGenes;
	}


	/**
	 * @param modelGenes the modelGenes to set
	 */
	public void setModelGenes(String[] modelGenes) {
		this.modelGenes = modelGenes;
	}
	
	/**
	 * @return the newGaps
	 */
	public boolean isNewBlockedReaction() {
		return newBlockedReaction;
	}


	/**
	 * @param newBlockedReaction the newGaps to set
	 */
	public void setNewBlockedReaction(boolean newBlockedReaction) {
		this.newBlockedReaction = newBlockedReaction;
	}


	/**
	 * @return the modelGenesIdentifier
	 */
	public Map<String, Integer> getModelGenesIdentifier() {
		return modelGenesIdentifier;
	}


	/**
	 * @param modelGenesIdentifier the modelGenesIdentifier to set
	 */
	public void setModelGenesIdentifier(Map<String, Integer> modelGenesIdentifier) {
		this.modelGenesIdentifier = modelGenesIdentifier;
	}


	/**
	 * @param enzymesModel the enzymesModel to set
	 */
	public void setEnzymesModel(String[] enzymesModel) {
		this.enzymesModel = enzymesModel;
	}
	
	@Override
	public void resetDataStuctures() {
		
		this.reactionsData=null;
		super.resetDataStuctures();
	}

}
