package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionLabels;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public interface IReactionService{

//	public void loadReaction(Integer idCompartment, List<Integer> compounds, List<Integer> compartments, List<Float> stoichiometry, String ecNumber, 	//this method should be one level below
//			List<Integer> proteins, List<String> enzymes, Map<String, List<Integer>> ecNumbers, boolean inModel,
//			boolean isTransport, String name, String equation, boolean generic, boolean spontaneous, 
//			boolean nonEnzymatic, String reactionSource, boolean isSpontaneous, boolean isGeneric, 
//			boolean isNonEnzymatic, List<Integer> pathways, 
//			String notes, double lowerBound, double upperBound, String boolean_rule) throws Exception;
	
	public boolean removeSelectedReaction(Integer reactionId) throws Exception;
	
	public Map<Integer, ReactionContainer> getReactionsData(String ecnumber, Integer id, boolean isCompartimentalized) throws Exception;
	
	public List<ModelReactionHasModelProtein> getProteinHasReaction() throws Exception;
	
	public List<String[]> getModelReactionNameAndECNumber (boolean originalreactions) throws Exception;
	
	public List<ArrayList<String>> getReactions(Integer id, Integer compartment, boolean isCompartimentalized) throws Exception;
	
	public long countReactionsInModel(boolean isCompartimentalized) throws Exception;
	
	public long countPathwayHasReactionByReactionId(Integer id) throws Exception;
	
	public List<ProteinContainer> getEnzymesForReaction(Integer idReaction) throws Exception;
	
	public List<ModelReaction> getReactionData(Integer id) throws Exception;
	
	public Pair<Boolean, Boolean> checkReactionIsReversibleAndInModel(Integer reactionID) throws Exception;
	
	public Pair<String, String> getEquationAndSourceFromReaction(Integer reactionID) throws Exception;
	
	public List<Integer> getReactionsIDs(Integer proteinId, String ecNumber) throws Exception;
	
	public List<String[]> getReactionHasEnzymeData2(Integer reactionId) throws Exception;
	
	public List<Integer> getDataFromReaction(String ecnumber) throws Exception;
	
	public List<Pair<String, String>> getReactionHasEnzyme(boolean isCompartimentalised) throws Exception;
	
	public List<Pair<Integer, String>> getReactionHasEnzyme2(boolean isCompartimentalised) throws Exception;
	
	public List<String[]> getStoichiometry(boolean isCompartmentalisedModel) throws Exception;
	
	public List<String[]> getDataFromReaction2() throws Exception;
	
	public List<ReactionContainer> getAllModelReactionByInModel() throws Exception;
	
	public String[] getBooleanRuleFromReaction(Integer id) throws Exception;
	
	public String[] getReactionData2(Integer reactionID) throws Exception;
	
	public List<String[]> getEnzymeProteinID(Integer idReaction) throws Exception;
	
	public List<String> getEcNumbersList(Integer reactionID) throws Exception;
	
	public List<String[]> getEnzymesByReaction(Integer idReaction) throws Exception;
	
	public List<String[]> getEnzymesByReactionAndPathway(Integer idReaction, Integer pathway) throws Exception;
	
//	public boolean containsReactionByOriginalReaction(boolean originalReaction) throws Exception;
	
	public List<Integer> getReactionsRelatedToLabelName(String name) throws Exception;
	
	public Map<Integer, String> getReactionsNames(List<Integer> reaction) throws Exception;
	
	public List<String> getRelatedReactions(String name) throws Exception;
	
	public boolean isReactionInModel(Integer reactionId) throws Exception;
	
	public List<String> getExistingEnzymesID(Integer idReaction) throws Exception;
	
	public List<String> getExistingEnzymesID2(Integer idReaction) throws Exception;
	
	public boolean checkIfReactioLabelNameAlreadyExists(String name) throws Exception;
	
	public Integer getReactionLabelIdByName(String name) throws Exception;
	
	public ModelReactionLabels getReactionLabelByName(String name) throws Exception;
	
	public List<Integer> getReactionID(Integer proteinID, String ecNumber) throws Exception;
	
	public Map<Integer, List<Integer>> getEnzymesReactions2() throws Exception;
	
	public boolean checkBiochemicalOrTransportReactions(boolean transporter) throws Exception;
	
//	public boolean checkTransportersReactions() throws Exception;
	
	public String[] getEnzymesByReaction2(Integer idReaction) throws Exception;
	
	public String[] getEnzymesByReactionAndPathway2(Integer idReaction, Integer pathway) throws Exception;
	
	public Map<Integer, ReactionContainer> getEnzymesReactionsMap(boolean isTransporters) throws Exception;

	public ReactionContainer getDatabaseReactionContainer(Integer idReaction) throws Exception;
	
	public List<String[]> getStoichiometryInfo (boolean isCompartmentalisedModel) throws Exception;
	
//	public List<String[]>  getReactionHasEnzymeData(Integer id) throws Exception;
	
	public List<String[]> countReactions(Integer id, boolean isCompartmentalized) throws Exception;
	
	public ReactionContainer getDataForReactionContainerByReactionId(Integer rowID, boolean isCompartimentalised) throws Exception;
	
	public List<ReactionContainer> getDataForReactionContainer(boolean isCompartimentalised) throws Exception;
	
	public void updateReactionProperties(int reactionID, int columnNumber, Object object, 
			Integer notesColumn, Integer isReversibleColumn, Integer inModelColun) throws Exception;
	
//	public int getReactionID(String name) throws Exception;  Deleted because now it can return a list because several reactions are connected to the same label
	
	public boolean isCompartimentalizedModel() throws Exception;
	
	public void updateModelReactionReversibleAndLowerBoundAndEquationByReactionId(Boolean reversible, Long lower, Integer id, String equation) throws Exception;

	public boolean deleteModelReactionHasModelProteinByReactionId(Integer reactionId, Integer protId)  throws Exception;
	
	public void updateModelReactionInModelByReactionId(Integer reactionId, Boolean inModel) throws Exception;
	
	public void updateReaction(String name, String equation, Boolean reversible, Integer compartmentId,
			Boolean isSpontaneous, Boolean isNonEnzymatic, Boolean isGeneric, String booleanRule, Long lower,
			Long upper, Integer reactionId, Boolean inModel) throws Exception;
	
	public void insertModelReactionHasModelProtein(Integer idReaction, Integer idprotein) throws Exception;
	
	public void updateModelReactionNotesByReactionId(Integer reactionId, String notes) throws Exception;
	
	public void updateInModelAndSourceByReactionId(Integer idreaction, boolean inModel, String source) throws Exception;

	public Map<Integer, ReactionContainer> getAllModelReactionAttributesByReactionId(boolean isCompartimentalised)
			throws Exception;

	public Integer insertNewReaction(boolean inModel, double lowerBound, double upperBound, String boolean_rule,
			String equation, boolean isGeneric, boolean isNonEnzymatic, boolean isSpontaneous, String name,
			String source, Integer compartmentId, String notes) throws Exception;
	
	public ModelReactionLabels insertNewModelReactionLabel(String equation, boolean isGeneric, boolean isNonEnzymatic, boolean isSpontaneous, String name, String source) throws Exception;

	public List<ReactionContainer> getReactionDataForStats(boolean isCompartimentalized) throws Exception;

	public long countReactionsByInModelAndSource(boolean idCompartimentalized, boolean inModel, String source)
			throws Exception;

	public Integer insertNewReaction(ReactionContainer container) throws Exception;

	public Map<String, List<Integer>> getModelReactionIdsRelatedToNames() throws Exception;

	public List<Integer> getAllModelReactionsIDsByAttributes(String equation, boolean inModel, boolean isGeneric,
			boolean isSpontaneous, boolean isNonEnzymatic, String source, boolean isCompartimentalized)
			throws Exception;

	public Boolean checkReactionNotLikeSourceAndNotReversible(String source) throws Exception;

	public Map<Integer, String> getLabelsByExternalIdentifiers(String name) throws Exception;

	public List<Object[]> getReactionsList(Integer pathwayID, boolean noEnzymes, boolean isCompartimentalized) throws Exception;
	
	public List<Integer> getModelReactionLabelIdByName(String name, boolean isCompartimentalized) throws Exception;
	
	public List<String[]> getDataFromReactionForBlockedReactionsTool(boolean isCompartimentalized) throws Exception;


	public void updateBooleanRuleAndNotes(List<String> names, Map<String, String> rules, String message) throws Exception;

	public void removeReactionsFromModelByBooleanRule(List<String> names, boolean keepReactionsWithNotes,
			boolean keepManualReactions) throws Exception;

	public List<Object[]> getAllModelReactionsByTransportersAndIsCompartimentalized(boolean isTransporters) throws Exception;

	public Map<Integer, ReactionContainer> getAllModelReactionAttributesbySource(boolean isCompartimentalized, String source) throws Exception;

	public List<Pair<Integer, String>> getAllModelReactionHasModelProteinByReactionId(Integer idreaction) throws Exception;

	public boolean checkReactionHasEnzymeData(Integer idProtein, Integer idReaction) throws Exception;

	public boolean addReaction_has_Enzyme(Integer idprotein, Integer idReaction) throws Exception;

	public Map<String, List<Integer>> getEcNumbersByReactionId() throws Exception;

	public List<ReactionContainer> getDistinctReactionByProteinIdAndCompartimentalized(Integer proteinID,
			boolean isCompartimentalized) throws Exception;

	public List<ReactionContainer> getReactionIdFromProteinIdWithPathwayIdNull(Integer proteinId) throws Exception;

	public Integer getIdReactionLabelFromReactionId(Integer reactionId) throws Exception;
	
	public void updateSourceByReactionLabelId(Integer reactionLabelId, String source) throws Exception;

	public List<String[]> getReactionIdAndEcNumberAndProteinId() throws Exception;

	public void removeReactionsBySource(String source) throws Exception;

	public boolean checkReactionsBySource(String source) throws Exception;

	public List<Object[]> getCompoundIDAndStoichiometricCoefficientAndCompartmentIDByReactionID(Integer reactionID) throws Exception;

	public List<Integer[]> getReactionIdAndPathwayId() throws Exception;
	
	public List<String[]> getReacIdEcNumProtIdWhereSourceEqualTransporters() throws Exception;

	public List<Integer> getDistinctReactionIdWhereSourceTransporters(Boolean isTransporter) throws Exception;

	public Set<String> getAllReactionsNames() throws Exception;

	public void removeNotOriginalReactions(Boolean isTransporter) throws Exception;

	public List<String[]> getReactionGenes() throws Exception;

	public Set<Integer> getModelDrains() throws Exception;

	public boolean checkIfReactionsHasData() throws Exception;

	public void setAllReactionsInModel(boolean setInModel, boolean keepSpontaneousReactions) throws Exception;

	public void updateModelReactionSetInModelAndNotesAndBooleanRuleByReactionName(boolean inModel, String notes,
			String booleanRule, String name) throws Exception;

	public List<String[]> getDataForGPRAssignment() throws Exception;

	public List<Integer> getProteinsByReactionId(Integer reactionId) throws Exception;

	public CompartmentContainer getReactionCompartment(Integer reactionId) throws Exception;

	public List<ReactionContainer> getGenesReactionsBySubunit() throws Exception;

	public List<ReactionContainer> getModelReactionIdsRelatedToName(String name) throws Exception;

	public Integer countTotalOfReactions(boolean isCompartmentalized) throws Exception;

	public void replaceReactionCompartment(Integer compartmentIdToKeep, Integer compartmentIdToReplace) throws Exception;

	public boolean removeSelectedReactionLabel(Integer reactionLabelId) throws Exception;

	public void updateReactionCompartment(Integer reactionId, Integer compartmentId) throws Exception;

}
