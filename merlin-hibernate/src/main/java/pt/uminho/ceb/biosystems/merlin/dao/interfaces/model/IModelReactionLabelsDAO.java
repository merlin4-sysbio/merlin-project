package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionLabels;

public interface IModelReactionLabelsDAO extends IGenericDao<ModelReactionLabels> {

	public void addModelReactionLabels(ModelReactionLabels modelReaction); 
	
	public void addModelReactionLabelsList(List<ModelReactionLabels> modelReaction); 
	
	public List<ModelReactionLabels> getAllModelReactionLabels(); 
	
	public ModelReactionLabels getModelReactionLabels(Integer id); 
	
	public void removeModelReactionLabels(ModelReactionLabels modelReaction); 
	
	public void removeModelReactionLabelsList(List<ModelReactionLabels> modelReactionList); 
	
	public void updateSourceByReactionLabelId(Integer reactionLabelId, String source);
	
	public void updateModelReactionLabelsList(List<ModelReactionLabels> modelReactionList); 
	
	public void updateModelReactionLabels(ModelReactionLabels ModelReactionLabels);

	public List<ModelReactionLabels> getAllModelReactionLabelssByAttributes(String name, String equation, boolean generic, boolean spontaneous,
			boolean nonEnzymatic, String reactionSource);

	public Integer insertModelReactionLabels(String name, String equation, boolean isSpontaneous, 
			boolean isGeneric, boolean isNonEnzymatic, String source);
	
	public boolean deleteModelReactionLabelsById(Integer reactionId);

	public ModelReactionLabels getModelReactionLabelByName(String name);

	public String getModelReactionLabelsNameByReactionId(Integer id);

	public List<String[]> getModelReactionLabelsDataBySource(String string);

	public void updateAllModelReactionLabels(String name, String equation,
			Boolean isSpontaneous, Boolean isNonEnzymatic, Boolean isGeneric, 
			Integer reactionLabelId);

	public void updateEquationByReactionLabelId(Integer reactionLabelId, String equation);

	public Map<Integer, String> getLabelsByExternalIdentifiers(String name);

	public List<Integer> getModelReactionsIdByName(String name, boolean isCompartimentalized);

	public Integer getIdReactionLabelFromReactionId(Integer reactionId);

}
