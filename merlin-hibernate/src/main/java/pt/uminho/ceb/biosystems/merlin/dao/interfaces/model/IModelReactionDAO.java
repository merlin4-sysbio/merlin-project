package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;

public interface IModelReactionDAO extends IGenericDao<ModelReaction> {

	public void addModelReaction(ModelReaction modelReaction); 
	
	public void addModelReactionList(List<ModelReaction> modelReaction); 
	
	public List<ModelReaction> getAllModelReaction(); 
	
	public ModelReaction getModelReaction(Integer id); 
	
	public void removeModelReaction(ModelReaction modelReaction); 
	
	public void removeModelReactionList(List<ModelReaction> modelReactionList); 
	
	public void updateModelReactionList(List<ModelReaction> modelReactionList); 
	
	public void updateModelReaction(ModelReaction ModelReaction);

//	public List<ModelReaction> getAllModelReactionWithEnzyme(boolean isCompartimentalized);

	public List<ModelReaction> getAllModelReactionsByAttributes(int idCompartment, boolean inModel);

	public Map<Integer, ReactionContainer> getModelReactionsData(String ecnumber, Integer id, boolean isCompartimentalized);
	
	public List<String[]> getModelReactionsData2();
	
//	public List<ModelReaction> getModelReactionsBySourceAndCompartmentalized(String string);

	public boolean deleteModelReactionByReactionId(Integer reactionId);

	public List<String[]> getModelReactionNameAndECNumber(boolean isCompartimentalized);

//	public List<ModelReaction> getAllModelReactionByName(String name);

	public List<String[]> getModelReactions(Integer compartment, Integer id, boolean isCompartimentalized);

	public Long countModelReactionDistinctReactionsIdsByInModel(boolean isCompartimentalized);

	public Long countModelReactionDistinctReactionsIdsByReactionId(Integer id);

	public ModelReaction getModelReactionByReactionId(Integer reactionId);

	public List<Integer> getDistinctModelReactionIdByEcNumber(String ecnumber);

	public List<String[]> getModelReactionStoichiometryData(boolean isCompartimentalized);

	public List<ReactionContainer> getAllModelReactionByInModel();

	public String getModelReactionBooleanRuleByReactionId(Integer id);

	public List<String[]> getDistinctModelReactionAttributesByPathwayIdAndReactionId(Integer path, Integer reaction);

	public List<ReactionContainer> getAllModelReactionByCompartmentalized(boolean isCompartimentalized);
	
	public Map<Integer, ReactionContainer> getAllModelReactionAttributesByReactionId(boolean isCompartimentalized);

	public void updateInModelByReactionId(Integer idreaction, boolean inModel);
	
	public void updateModelReactionNotesByReactionId(Integer reactionId, String notes);

	public Long countModelReactionDistinctReactionsIdsBySourceAndInModel(boolean isCompartimentalized, boolean inModel, String source);

//	public String getModelReactionNameByReactionId(Integer id);

	public boolean getModelReactionInModelByReactionId(Integer reactionId);

	public List<Object[]> getModelReactionDataBySource(String string);

	public List<ModelReaction> getAllModelReactionsByTransportersType(boolean isTransporters);

	public ReactionContainer getModelReactionAttributesByReactionId(Integer idreaction, boolean isCompartimentalized);

	public List<String[]> getModelReactionByInModelAndConditions(boolean isCompartimentalized);

	public List<Integer> getModelReactionIdByName(String name);
	

	public Long countModelReactionDistinctReactionsId();

	public Integer getModelReactionLabelByReactionId(Integer reactionId);

	public List<ReactionContainer> getAllModelReactionAttributes(boolean isCompartimentalized);

	public Boolean isCompartimentalizedModel();

	public List<ReactionContainer> getAllModelReactionsByAttributesAndSource(int idCompartment, boolean inModel,
			String source);

	public List<ReactionContainer> getAllModelReactionWithLabelsByCompartmentalized(boolean isCompartimentalized);

	public Map<String, List<Integer>> getModelReactionIdsRelatedToNames();

	public List<ModelReaction> getModelReactionsBySourceAndCompartmentalized(String source, boolean transporter);

	public List<Integer> getAllModelReactionsIDsByAttributes(String equation, boolean inModel, boolean isGeneric,
			boolean isSpontaneous, boolean isNonEnzymatic, String source, boolean isCompartimentalized);
	
	public Boolean checkReactionNotLikeSourceAndNotReversible(String source);

	public List<Object[]> getReactionsList(Integer pathwayID, boolean noEnzymes, boolean isCompartimentalized);

	public List<String> getModelReactionNamesByCompound(String name);
	
	public void updateReactionReversibility(Long lower, Integer reactionId);

	public List<String[]> getDataFromReactionForBlockedReactionsTool(boolean isCompartimentalized);

	public List<Object[]> getAllModelReactionsByTransportersAndIsCompartimentalized(boolean isTransporters);

	public Map<Integer, ReactionContainer> getAllModelReactionAttributesbySource(boolean isCompartimentalized, String source);

	public Map<Integer, ReactionContainer> getAllModelReactionAttributesByReactionIdWithPathways(boolean isCompartimentalized);

	public List<Integer> getDistinctReactionsByEnzymeAndCompartmentalized(Integer proteinId, boolean hasPathway,
			boolean isCompartimentalized);

	public List<ProteinContainer> getEnzymesForReaction(int rowID);

	public List<ReactionContainer> getDistinctReactionByProteinIdAndCompartimentalized(Integer proteinID, 
			boolean isCompartimentalized);

	public List<ReactionContainer> getReactionIdFromProteinIdWithPathwayIdNull(Integer proteinId);

	public List<Object[]> GetCompoundIDAndStoichiometricCoefficientAndCompartmentIDByReactionID(Integer reactionID);

	public List<String[]> getReactionIdAndEcNumberAndProteinId();

	public List<Integer[]> getReactionIdAndPathwayId();

	public List<String[]> getReacIdEcNumProtIdWhereSourceEqualTransporters();

	public List<Integer> getDistinctReactionIdWhereSourceTransporters(Boolean isTransporter);

	public void removeNotOriginalReactions(Boolean isTransporter);

	public List<String[]> getReactionGenes();

	public Set<Integer> getModelDrains();

	public List<String[]> getDataForGPRAssignment();

	public List<ReactionContainer> getModelReactionIdsRelatedToName(String name);

	public Integer countTotalOfReactions(boolean isCompartmentalized);

	public void replaceCompartment(Integer compartmentIdToKeep, Integer compartmentIdToReplace);

}
