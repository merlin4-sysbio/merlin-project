package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwayContainer;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathway;


public interface IModelPathwayDAO extends IGenericDao<ModelPathway>{

	public void addModelPathway(ModelPathway modelPathway); 
	
	public void addModelPathwayList(List<ModelPathway> modelPathwayList); 
	
	public List<ModelPathway> getAllModelPathway(); 
	
	public ModelPathway getModelPathway(Integer id); 
	
	public void removeModelPathway(ModelPathway modelPathway); 
	
	public void removeModelPathwayList(List<ModelPathway> modelPathwayList); 
	
	public void updateModelPathwayList(List<ModelPathway> modelPathwayList); 
	
	public void updateModelPathway(ModelPathway modelPathway);

	public Map<String, Set<String>> getPathwayIdAndNameAndEcNumber();

	public Map<String, Set<String>> getEnzymesPathwaysEcNumber();

	public boolean checkIfModelPathwayNameExists(String name);

	public Integer insertModelPathwayCodeAndName(String code, String name);

	public String getModelPathwayNameByReactionId(Integer id);

	public List<String[]> getModelPathwayIdAndName();

	public List<ModelPathway> getAllModelPathwayByCode(String string);

	public Integer getPathwayIdByName(String name);

	public List<Integer> getPathwayIdByReactionId(Integer idReaction);

	public List<PathwayContainer> getAllFromPathwaySortedByName();

	public Map<Integer, String> getAllModelPathwayIdAndName();

	public List<ModelPathway> getAllModelPathwayByNameAndCode(String name, String code);

	public String getPathwayCodeByName(String name);

	public Map<Integer, String> getPathwayIdAndNameByName(String name);

	public List<ModelPathway> getAllModelPathwayByName(String string);
	
	public Map<Integer, Set<String>> getAllPathwayIdAndNameAndEcNumber();

	public List<String> getAllPathwaysNamesOrdered();

	public List<String> getAllPathwaysOrderedByNameInModelWithReaction(Boolean inModel);

	public List<String[]> getUpdatedPathways(boolean isCompartimentalized, boolean encodedOnly);

	public List<Object[]> getPathwayIdAndReactionId(boolean isCompartimentalized);

	public List<Object[]> countProteinIdByPathwayID();

	public Map<String, Integer> getPathwayCodeAndIdpathway();

	public Long countPathwayHasReaction(boolean isCompartimentalized);

}
