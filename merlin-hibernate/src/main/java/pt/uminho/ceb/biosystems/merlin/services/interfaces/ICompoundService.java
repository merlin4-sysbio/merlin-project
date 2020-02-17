package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.auxiliary.ModelCompoundType;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompound;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;


public interface ICompoundService {

	//public Map<Integer, String> getModelInformationForBiomass(List<String> metaboliteIDs) throws Exception;
	public Integer insertCompoundToDatabase(String name, double molecularWeight) throws Exception;
	
	public List<String[]> getMetabolitesProperties(Boolean isCompartimentalized, Boolean inModel, List<ModelCompoundType> types) throws Exception;
	
	public List<String[]> getMetabolitesWithBothProperties(Boolean isCompartimentalized, Boolean inModel, List<ModelCompoundType> types) throws Exception;
	
	public Boolean isMetabolicDataLoaded() throws Exception;
	
	public List<String> getEntryType(Integer id) throws Exception;
	
	public List<String[]> getCompoundWithBiologicalRoles() throws Exception;
	
	public List<String[]> getCompoundReactions(Integer id) throws Exception;
	
	public Long countCompoundsByName(String name) throws Exception;
	
	public List<String[]> getMetaboliteData(String name) throws Exception;
	
	public int getCompoundIDbyExternalIdentifier(String externalId) throws Exception;
	
	public Map<String, Pair<Integer, String>> getModelInformationForBiomass(List<String> metaboliteIDs) throws Exception;
	
	public Map<Integer, MetaboliteContainer> getAllMetabolites() throws Exception;
	
	public List<Integer[]> getAllModelCompoundIdWithCompartmentIdCountedReactions(Boolean isCompartimentalized, Boolean inModel, List<ModelCompoundType> type, Boolean withTransporters) throws Exception;

	public List<ModelCompound> getMetabolitesNotInModel(List<ModelCompoundType> typeList) throws Exception;
	
	public void insertCompoundGeneratingExternalID(String name, String entryType, String formula, String molecularW, String charge) throws Exception;
	
	public void updateModelCompoundAttributes(String name, String entryType, String formula, String molecularW, Short charge,
			String externalIdentifier) throws Exception;

	public Integer getMetaboliteIdByName(String name) throws Exception;

	public Integer insertModelCompound(String name, String inchi, String entry_type,
			String external_identifier, String formula, String molecular_weight, String neutral_formula, Short charge,
			String smiles, Boolean hasBiologicalRoles) throws Exception;

	public List<String> getAllCompoundsInModel() throws Exception;

	public Map<String, Integer> getExternalIdentifierAndIdCompound() throws Exception;

	public MetaboliteContainer getCompoundByExternalIdentifier(String identifier) throws Exception;

	public void removeCompoundByExternalIdentifier(String identifier) throws Exception;

	public MetaboliteContainer getModelCompoundByName(String name) throws Exception;

	public String getCompoundExternalIdentifierByInternalID(Integer internalID) throws Exception;
}

