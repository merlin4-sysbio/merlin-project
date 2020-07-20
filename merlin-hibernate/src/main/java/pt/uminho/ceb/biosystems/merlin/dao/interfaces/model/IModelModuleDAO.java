package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModule;


public interface IModelModuleDAO extends IGenericDao<ModelModule>{

	public void addModelModule(ModelModule modelModule); 
	
	public void addModelModuleList(List<ModelModule> modelModuleList); 
	
	public List<ModelModule> getAllModelModule(); 
	
	public ModelModule getModelModule(Integer id); 
	
	public void removeModelModule(ModelModule modelModule); 
	
	public void removeModelModuleList(List<ModelModule> modelModuleList); 
	
	public void updateModelModuleList(List<ModelModule> modelModuleList); 
	
	public void updateModelModule(ModelModule modelModule);

	public Integer insertModelModule(String reaction, String entryId, String name, String definition, String type);

	public Map<Integer, String> getModelModuleIdAndDefinitionByEntryIdAndReactionAndDefinition(String entryId, String reaction,
			String definition);

	public Map<String, Integer> getModuleEntryidAndId();

	public Map<String, Integer> getModelModuleEntryIdAndId();

	public List<ModelModule> getModulesIdsByEcNumber(String ecNumber);

	public Map<Integer, String> getModuleIdAndNoteByGeneIdAndProteinId(Integer geneId, Integer protId);

}
