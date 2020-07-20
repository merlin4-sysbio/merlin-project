package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelStoichiometry;



public interface IModelStoichiometryDAO extends IGenericDao<ModelStoichiometry>{
	
	public void addModelStoichiometry(ModelStoichiometry modelStoichiometry); 
	
	public void addModelStoichiometryList(List<ModelStoichiometry> modelStoichiometryList); 
	
	public List<ModelStoichiometry> getAllModelStoichiometry(); 
	
	public ModelStoichiometry getModelStoichiometry(Integer id); 
	
	public void removeModelStoichiometry(ModelStoichiometry modelStoichiometry); 
	
	public void removeModelStoichiometryList(List<ModelStoichiometry> modelStoichiometryList); 
	
	public void updateModelStoichiometryList(List<ModelStoichiometry> modelStoichiometryList); 
	
	public void updateModelStoichiometry(ModelStoichiometry modelStoichiometry);

	public List<String[]> getAllModelStoichiometryAttributesByCompoundId(Integer id);

	public List<ModelStoichiometry> getAllModelStoichiometryByAttributes(Integer reactionId, Integer compartmentId, Integer compoundId, Float stoichiometricCoef);

	public List<Integer> getDistinctReversibleCompoundIds(Boolean inModel, Boolean isOriginalReaction);

	public List<String[]> getModelStoichiometryAttributesByReactionId(int reactionID);

	public Integer getIdStoichiometry(int idReaction, int m, int idCompartment, double coefficient);

	public List<ModelStoichiometry> getDistinctModelStoichiometryCompoundId();

	public List<String[]> getAllModelStoichiometryAttributesByReactionId(Integer id);

	public List<String[]> getModelStoichiometryCompoundIdAndStoichiometryCoef();

	public List<String[]> getModelStoichiometryDifferentAttributesByReactionId(Integer reactionId);

	public List<String[]> getAllModelStoichiometryByReactionId2(Integer reactionId);

	public List<String[]> getModelStoichiometryAttributes(boolean isCompartmentalized);

	public List<ModelStoichiometry> getAllModelStoichiometryBySourceAndOriginalReaction(String string, boolean b);

	public List<ModelStoichiometry> getAllModelStoichiometryByReactionId(Integer reactionId);
	
	public boolean deleteModelStoichiometryByStoichiometryId(Integer stoichId);
	
	public void updateModelStoichiometryByStoichiometryId(Integer id, float coeff, Integer compartmentId, Integer compoundId);

	public List<ModelStoichiometry> getAllModelStoichiometryBySourceAndOriginalReaction2(String source,
			boolean originalReaction);

	public List<ModelStoichiometry> getAllModelStoichiometryAttributes();

	public List<String[]> getModelStoichiometryCompartmentNameAndDistinctCompoundId();

	public Map<Integer, MetaboliteContainer> getModelStoichiometryByReactionId(Integer reactionID);

	public List<Object[]> getAllModelStoichiometryAttributes2(boolean isCompartimentalizedModel);

	public List<Integer[]> getStoichiometryDataFromTransportersSource();

	public Map<Integer, ReactionContainer> getAllOriginalTransportersFromStoichiometry();

	public List<String[]> getCompoundDataFromStoichiometry(Integer idreaction);

	public Set<String> checkUndefinedStoichiometry(boolean isCompartimentalized);

	public void replaceCompartment(Integer compartmentIdToKeep, Integer compartmentIdToReplace);

}
