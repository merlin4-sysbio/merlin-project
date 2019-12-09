package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwayContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasReaction;


public interface IModelPathwayHasReactionDAO  extends IGenericDao<ModelPathwayHasReaction>{
	
	public void addModelPathwayHasReaction(ModelPathwayHasReaction modelPathwayHasReaction); 
	
	public void addModelPathwayHasReactionList(List<ModelPathwayHasReaction> modelPathwayHasReactionList); 
	
	public List<ModelPathwayHasReaction> getAllModelPathwayHasReaction(); 
	
	public ModelPathwayHasReaction getModelPathwayHasReaction(Integer id); 
	
	public void removeModelPathwayHasReaction(ModelPathwayHasReaction modelPathwayHasReaction); 
	
	public void removeModelPathwayHasReactionList(List<ModelPathwayHasReaction> modelPathwayHasReactionList); 
	
	public void updateModelPathwayHasReactionList(List<ModelPathwayHasReaction> modelPathwayHasReactionList); 
	
	public void updateModelPathwayHasReaction(ModelPathwayHasReaction modelPathwayHasReaction);

	public List<ModelPathwayHasReaction> getAllModelPathwayHasReactionByAttributes(Integer idReaction,
			Integer idPathway);

	public void insertModelPathwayHasReaction(Integer idReaction, Integer idPathway);

	public List<String> getModelPathwayHasReactionAttributes();

	public List<String[]> getModelPathwayHasReactionData();

	public List<PathwayContainer> getModelPathwayHasReactionIdByReactionId(Integer idReaction);

	public Map<Integer, ReactionContainer> getModelPathwayHasReactionIdAndPathIdByAttributes(String source, Boolean isCompartimentalized);

	public List<Integer> getAllModelPathwayHasReactionIdReactionByPathId(Integer id);

	public List<ModelPathwayHasReaction> getAllModelPathwayHasReactionByReactionId(Integer id);

	public Integer getModelPathwayHasReactionIdByReactionIdAndPathId(Integer idReaction, Integer pathId);

	public List<String[]> getPathwayHasReactionAttributes(Integer id, boolean isCompartmentalized);
	
	public boolean deleteModelPathwayHasReactionByReactionIdAndPathwayId(Integer reactionId, Integer pathId);
	
	public boolean deleteModelPathwayHasReactionByPathwayId(Integer id);
	
	public boolean deleteModelPathwayHasReactionByReactionId(Integer id);

}
