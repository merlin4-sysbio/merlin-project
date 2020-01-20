package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionHasModelProteinId;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;


public interface IModelReactionHasModelProteinDAO extends IGenericDao<ModelReactionHasModelProtein>{

	public void addModelReactionHasModelProtein(ModelReactionHasModelProtein modelReactionHasEnzyme); 
	
	public void addModelReactionHasModelProteinList(List<ModelReactionHasModelProtein> modelReactionHasEnzymeList); 
	
	public List<ModelReactionHasModelProtein> getAllModelReactionHasModelProtein(); 
	
	public ModelReactionHasModelProtein getModelReactionHasModelProtein(Integer id); 
	
	public void removeModelReactionHasModelProtein(ModelReactionHasModelProtein modelReactionHasEnzyme); 
	
	public void removeModelReactionHasModelProteinList(List<ModelReactionHasModelProtein> modelReactionHasEnzymeList); 
	
	public void updateModelReactionHasModelProteinList(List<ModelReactionHasModelProtein> modelReactionHasEnzymeList); 
	
	public void updateModelReactionHasModelProtein(ModelReactionHasModelProtein modelReactionHasEnzyme);

	public ModelReactionHasModelProteinId insertModelReactionHasModelProtein(Integer idReaction, Integer idprotein);

	public List<ModelReactionHasModelProtein> getAllModelReactionHasModelProteinOrderByReactionId();

	public List<String[]> getAllModelReactionHasModelProteinByreactionId(Integer idReaction);

	public List<String[]> getModelReactionHasModelProteinProteinIdAndEcNumberByreactionId(Integer reactionId);

	public List<String> getModelReactionHasModelProteinEcNumberByReactionId(Integer reactId);

	public List<String[]> getAllModelReactionHasModelProteinByreactionId2(Integer id);

	public List<ModelReactionHasModelProtein> getAllModelReactionHasModelProteinByReactionId(Integer reactionId);

	public boolean deleteModelReactionHasModelProteinByReactionId(Integer reactionId, Integer protId);
	
	public boolean deleteModelReactionHasModelProteinByReactionIdAndPathId(Integer reactionId, Integer protId);

	public List<ModelReactionHasModelProtein> getAllModelReactionHasModelProteinByAttributes(Integer idprotein,
			Integer idReaction);

	public List<String[]> getModelReactionAttributes(Integer rowId);

	public Map<String, List<Integer>> getEcNumbersByReactionId();

	public List<Pair<String, String>> getModelReactionHasModelProteinData(boolean isCompartimentalised);

	public List<Pair<Integer, String>> getModelReactionHasModelProteinData2(boolean isCompartimentalised);
	
	
}
