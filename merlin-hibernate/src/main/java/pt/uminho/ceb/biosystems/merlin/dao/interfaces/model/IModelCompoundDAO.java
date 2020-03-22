package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.auxiliary.ModelCompoundType;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompound;



public interface IModelCompoundDAO extends IGenericDao<ModelCompound>{
	
	public void addModelCompound(ModelCompound modelCompound); 
	
	public void addModelCompoundList(List<ModelCompound> modelCompoundList); 
	
	public List<ModelCompound> getAllModelCompound(); 
	
	public ModelCompound getModelCompound(Integer id); 
	
	public void removeModelCompound(ModelCompound modelCompound); 
	
	public void removeModelCompoundList(List<ModelCompound> modelCompoundList); 
	
	public void updateModelCompoundList(List<ModelCompound> modelCompoundList); 
	
	public void updateModelCompound(ModelCompound modelCompound);

	public List<ModelCompound> getAllModelCompoundByName(String name);

	public List<String[]> getMetabolitesProperties(Boolean isOriginalReaction, Boolean inModel, List<ModelCompoundType> types);

	public List<ModelCompound> getAllModelCompoundData(List<ModelCompoundType> typeList);

	public List<String[]> getMetabolitesWithBothProperties(Boolean isCompartimentalized, Boolean inModel, List<ModelCompoundType> type);

	public Boolean checkIfIsFilled();

	public List<String> getAllModelCompoundEntryTypeByCompoundId(Integer id);

	public List<ModelCompound> getAllModelCompoundDataOrderByKeggId();

	public Long countCompoundsByName(String name);

	public List<String[]> getAllModelCompoundAttributesByName(String name);

	public Integer getModelCompoundIdByExternalIdentifier(String keggid);

	public Map<Integer, String> getModelCompoundIdAndMolecularWeightByKeggId(String name);

	public List<String[]> getAllModelCompoundAttributesByName2();

	public Integer getModelCompoundIdByName(String string);

	public Integer insertModelCompound(String name, String keggId, String entryType, String molecularWeight,
			boolean hasBiologicalRoles);

	public List<ModelCompound> getAllModelCompoundData2(ModelCompoundType type);
	
	public List<Integer[]> getAllModelCompoundIdWithCompartmentIdCountedReactions(Boolean isCompartimentalized, Boolean inModel, List<ModelCompoundType> type, Boolean withTransporters);

	public void updateModelCompoundAttributes(String name, String entryType, String formula, String molecularW, Short charge,
			String externalIdentifier);

	public List<String> getAllCompoundsInModel();

	public Map<String, Integer> getExternalIdentifierAndIdCompound();

	public void updateModelCompoundAttributesByInternalId(Integer id, String name, String entryType, String formula,
			String molecularW, Short charge, String externalIdentifier);

	public List<String> getAllCompounds();

}
