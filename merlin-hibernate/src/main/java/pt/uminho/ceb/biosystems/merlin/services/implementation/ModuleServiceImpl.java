package pt.uminho.ceb.biosystems.merlin.services.implementation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.ModuleContainer;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelModuleDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelModuleHasModelProteinDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelModuleHasOrthologyDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelProteinDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModule;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModuleHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModuleHasModelProteinId;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModuleHasOrthology;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModuleHasOrthologyId;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProtein;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IModuleService;

public class ModuleServiceImpl implements IModuleService{

	private IModelModuleDAO moduleDAO;
	private IModelModuleHasOrthologyDAO moduleHasOrthologyDAO;
	private IModelProteinDAO proteinDAO;
	private IModelModuleHasModelProteinDAO moduleHasProtDAO;
	
	public ModuleServiceImpl(IModelModuleDAO moduleDAO, IModelModuleHasOrthologyDAO moduleHasOrthologyDAO, IModelProteinDAO proteinDAO, 
								IModelModuleHasModelProteinDAO moduleHasProtDAO) {
		
		this.moduleDAO = moduleDAO;
		this.moduleHasOrthologyDAO = moduleHasOrthologyDAO;
		this.proteinDAO = proteinDAO;
		this.moduleHasProtDAO = moduleHasProtDAO;
	}
	
	@Override
	public Map<String, Integer> getModelModuleEntryIdAndId(){
		
		return this.moduleDAO.getModelModuleEntryIdAndId();
		
	}

	@Override
	public Integer insertNewModelModule(ModuleContainer container){
		
		ModelModule module = new ModelModule();
		
		module.setEntryId(container.getExternalIdentifier());
		
		if(container.getReaction() != null)
			module.setReaction(container.getReaction());
		
		if(container.getModuleType() != null)
			module.setType(container.getModuleType());
		
		if(container.getName() != null)
			module.setName(container.getName());
		
		if(container.getStoichiometry() != null)
			module.setStoichiometry(container.getStoichiometry());
		
		if(container.getDefinition() != null)
			module.setDefinition(container.getDefinition());
		
		if(container.getModuleHieralchicalClass() != null)
			module.setHieralchicalClass(container.getModuleHieralchicalClass());
			
		return (Integer) this.moduleDAO.save(module);
	}
	
	@Override
	public void insertNewModelModuleHasModelOrthology(Integer moduleId, Integer orthologyId){
		
		ModelModuleHasOrthology has = new ModelModuleHasOrthology();
		
		ModelModuleHasOrthologyId id = new ModelModuleHasOrthologyId(moduleId, orthologyId);
		
		has.setId(id);
		
		this.moduleHasOrthologyDAO.save(has);
	}
	
	@Override
	public boolean checkModelModuleHasModelOrthology(Integer moduleId, Integer orthologyId){
		
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.modelModuleId", moduleId);
		dic.put("id.modelOrthologyId", orthologyId);
		List<ModelModuleHasOrthology> list =  this.moduleHasOrthologyDAO.findByAttributes(dic);

		return (list != null && !list.isEmpty());
	}
	
	
	@Override
	public Integer getModuleIdByReactionAndDefinitionAndEntryId(String entryId, String reaction, String definition){
		
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("entryId", entryId);
		dic.put("reaction", reaction);
		dic.put("definition", definition);
		
		List<ModelModule> res = this.moduleDAO.findByAttributes(dic);

		if(res == null || res.isEmpty())
			return null;
		
		return res.get(0).getId();
	}
	
	@Override
	public void insertModelModuleHasModelProtein(Integer proteinId, Integer moduleId) throws Exception{

		ModelModule module = this.moduleDAO.getModelModule(moduleId);
		ModelModuleHasModelProteinId moduleHasProteinId = new ModelModuleHasModelProteinId(moduleId, proteinId);
		ModelProtein protein = this.proteinDAO.getModelProtein(proteinId);
		ModelModuleHasModelProtein moduleHasProtein = new ModelModuleHasModelProtein(moduleHasProteinId, module, protein);
	
		this.moduleDAO.save(moduleHasProtein);
	}
	
	@Override
	public boolean checkModelModuletHasProteinData(Integer proteinId, Integer moduleId) throws Exception{

		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		
		dic.put("id.modelProteinIdprotein", proteinId);
		dic.put("id.modelModuleId", moduleId);
		
		ModelModuleHasModelProtein modelModuleHasProt = this.moduleHasProtDAO.findUniqueByAttributes(dic);
		
		return modelModuleHasProt != null;
	}
	
	@Override
	public void updateECNumberModuleStatus(String ecNumber, String status) throws Exception {

		  List<ModelModule> modules = this.moduleDAO.getModulesIdsByEcNumber(ecNumber);

		for(ModelModule module : modules) {

			module.setGprStatus(status);

			this.moduleDAO.update(module);
		}

	}
	
	@Override
	public Map<Integer, String> getModuleIdAndNoteByGeneIdAndProteinId(Integer geneId, Integer protId) throws Exception {

		return this.moduleDAO.getModuleIdAndNoteByGeneIdAndProteinId(geneId, protId);

	}
	
}
