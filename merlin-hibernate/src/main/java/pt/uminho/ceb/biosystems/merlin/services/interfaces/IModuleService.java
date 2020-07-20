package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.ModuleContainer;

public interface IModuleService {

	public Map<String, Integer> getModelModuleEntryIdAndId() throws Exception;

	public Integer insertNewModelModule(ModuleContainer container) throws Exception;

	public void insertNewModelModuleHasModelOrthology(Integer moduleId, Integer orthologyId) throws Exception;

	boolean checkModelModuleHasModelOrthology(Integer moduleId, Integer orthologyId) throws Exception;

	public Integer getModuleIdByReactionAndDefinitionAndEntryId(String entryId, String reaction, String definition) throws Exception;

	public void insertModelModuleHasModelProtein(Integer proteinId, Integer moduleId) throws Exception;

	public boolean checkModelModuletHasProteinData(Integer proteinId, Integer moduleId) throws Exception;

	public void updateECNumberModuleStatus(String ecNumber, String status) throws Exception;
	
	public Map<Integer, String> getModuleIdAndNoteByGeneIdAndProteinId(Integer geneId, Integer protId) throws Exception;


}
