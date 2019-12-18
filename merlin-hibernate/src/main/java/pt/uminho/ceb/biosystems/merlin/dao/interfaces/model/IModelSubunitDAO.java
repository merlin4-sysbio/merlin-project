package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSubunit;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSubunitId;

public interface IModelSubunitDAO extends IGenericDao<ModelSubunit>{
	
	public void addSubunit(ModelSubunit subunit); 
	
	public void addSubunitList(List<ModelSubunit> subunitList); 
	
	public List<ModelSubunit> getAllSubunitList(); 
	
	public ModelSubunit getSubunit(Integer id); 
	
	public void removeSubunit(ModelSubunit subunit); 

	public void removeSubunitList(List<ModelSubunit> subunitList); 
	
	public void updateSubunitList(List<ModelSubunit> subunitList); 
	
	public void updateSubunit(ModelSubunit subunit);

	public void updateSubunitGeneIdWithLocusTagId(Integer idGeneNew, Integer idGeneOld);

	public List<String[]> getModelSubunitAttributesOrderByName();

	public List<String> getEcNumberByGeneId(Integer idgene);

	public List<ModelSubunit> getAllSubunitByGeneId(Integer idgene);

	public ModelSubunitId insertModelSubunit(Integer geneId, Integer proteinId);

	public List<Integer> getDistinctCompartmentIdByEcNumber(String ecNumber);

	public Map<Integer, String> getModelSubunitProteinIdAndEcNumberByGeneId(Integer geneID);

	public Long countModelSubunitDistinctGeneId();

	public Long countGenesInModel();

	public List<ProteinContainer> getModelSubunitAttributes(Integer id);

	public Map<Integer, String> getModelSubunitDistinctGeneIdAndSource();

	public Map<Integer, String> getModelSubunitDistinctEnzymeProteinIdAndEnzymeEcNumber();
	
	public void removeSubunitByGeneIdAndProteinId(Integer geneId, Integer protId);
	
	public List<ModelSubunit> getAllSubunitByProteinId(Integer protId);

	public Map<Integer, Integer> getModelSubunitGeneIdAndEnzymeProteinIdByEcNumber(String ecNumber);

	public List<String[]> getModelSubunitDistinctData();

	public boolean checkSubunitData(Integer id) throws Exception;

	public Map<String, Integer> countGenesReactionsBySubunit();

	public String[][] getSubunitsByGeneId(Integer idGene);

	public Map<String, Integer> getProteinsCount();

	public List<String[]> getGPRsECNumbers(boolean isCompartimentalized);

	public List<String[]> getModelEnzymeByEcNumberAndProteinId(Integer proteinId);

	public Integer checkProteinIdByGeneIdAndProteinId(Integer geneId, Integer proteinId);

	public Integer checkProteinByGeneIdAndProteinIdAndModuleId(Integer geneId, Integer proteinId, Integer moduleId);

	public List<String[]> getGPRstatusAndReactionAndDefinition(Integer proteinId);

	boolean isProteinEncodedByGenes(Integer proteinId);

}



