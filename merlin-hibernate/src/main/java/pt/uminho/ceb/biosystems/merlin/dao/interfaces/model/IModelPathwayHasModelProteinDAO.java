package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwayContainer;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasModelProteinId;


public interface IModelPathwayHasModelProteinDAO extends IGenericDao<ModelPathwayHasModelProtein>{
	
	public void addModelPathwayHasModelProtein(ModelPathwayHasModelProtein modelPathwayHasProtein); 
	
	public void addModelPathwayHasModelProteinList(List<ModelPathwayHasModelProtein> modelPathwayHasEnzymeList); 
	
	public List<ModelPathwayHasModelProtein> getAllModelPathwayHasModelProtein(); 
	
	public ModelPathwayHasModelProtein getModelPathwayHasModelProtein(Integer id); 
	
	public void removeModelPathwayHasModelProtein(ModelPathwayHasModelProtein modelPathwayHasEnzyme); 
	
	public void removeModelPathwayHasModelProteinList(List<ModelPathwayHasModelProtein> modelPathwayHasEnzymeList); 
	
	public void updateModelPathwayHasModelProteinList(List<ModelPathwayHasModelProtein> modelPathwayHasEnzymeList); 
	
	public void updateModelPathwayHasModelProtein(ModelPathwayHasModelProtein modelPathwayHasEnzyme);

	public Map<Integer, PathwayContainer> getDistinctModelPathwayHasModelProteinCodeAndNameByAttributes(Integer proteinId);

	public List<ModelPathwayHasModelProtein> getAllModelPathwayHasModelProteinByEcNumberAndPathwayIdAndProteinId(Integer pathId, Integer idProtein);

	public List<Integer> getModelPathwayHasModelProteinReactionIdByPathwayIdAndProteinId(Integer pathwayId, Integer proteinId);

	public List<Integer> getModelPathwayHasModelProteinReactionIdByReactionIdAndProteinId(Integer reactId, Integer proteinId);

	public List<Integer> getAllModelPathwayHasModelProteinIdByPathId(Integer id);

	public List<ModelPathwayHasModelProtein> getAllModelPathwayHasModelProteinByEcNumber(Integer idProtein);

	public List<ModelPathwayHasModelProtein> getAllModelPathwayHasModelProteinByPathwayIdAndProteinId(Integer pathId,
			Integer enzymeProteinIdprotein);

	public ModelPathwayHasModelProteinId insertModelPathwayHasModelProtein(Integer pathId, Integer proteinId);
	
	public boolean deleteModelPathwayHasModelProteinByPathwayId(Integer pathId);
	
	public List<String[]> getModelPathwayHasModelProteinAttributesByPathwayId(Integer pathId, boolean isCompartimentalized);

	public boolean deleteModelPathwayHasModelProteinByIdProtein(Integer proteinId);
}
