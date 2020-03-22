package pt.uminho.ceb.biosystems.merlin.services.implementation;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.auxiliary.ModelCompoundType;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelCompoundDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelStoichiometryDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompound;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.ICompoundService;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public class CompoundServiceImpl implements ICompoundService{


	private IModelCompoundDAO compoundDAO;
	private IModelStoichiometryDAO stoichservice;

	public CompoundServiceImpl(IModelCompoundDAO compoundDAO, IModelStoichiometryDAO stoichservice) {
		this.compoundDAO  = compoundDAO;
		this.stoichservice = stoichservice;

	}

	
	private static MetaboliteContainer buildMetaboliteContainer(ModelCompound compound) {
		
		MetaboliteContainer container = new MetaboliteContainer(compound.getExternalIdentifier());
		
		container.setName(compound.getName());
		container.setMetaboliteID(compound.getIdcompound());
		container.setFormula(compound.getFormula());
		container.setEntryType(container.getEntryType());
		container.setMolecular_weight(compound.getMolecularWeight());
		if(compound.getCharge() != null)
			container.setCharge(compound.getCharge().intValue());

		return container;
	}
	
	@Override
	public Integer insertCompoundToDatabase(String name, double molecularWeight) throws Exception {
		List<ModelCompound> res = this.compoundDAO.getAllModelCompoundByName(name);

		if (res == null) {
			return this.insertCompoundToDatabase(name, molecularWeight);
		}
		return null;
	}

	@Override
	public List<String[]> getMetabolitesProperties(Boolean isCompartimentalized, Boolean inModel, List<ModelCompoundType> types) throws Exception {
		return this.compoundDAO.getMetabolitesProperties(isCompartimentalized, inModel, types);
	}

	@Override
	public List<ModelCompound> getMetabolitesNotInModel(List<ModelCompoundType> typeList) throws Exception {
		return this.compoundDAO.getAllModelCompoundData(typeList);
	}

	@Override
	public List<String[]> getMetabolitesWithBothProperties(Boolean isCompartimentalized, Boolean inModel, List<ModelCompoundType> types) throws Exception {
		return this.compoundDAO.getMetabolitesWithBothProperties(isCompartimentalized, inModel, types);
	}

	@Override
	public Boolean isMetabolicDataLoaded() throws Exception {
		return this.compoundDAO.checkIfIsFilled();
	}

	@Override
	public List<String> getEntryType(Integer id) throws Exception {
		return this.compoundDAO.getAllModelCompoundEntryTypeByCompoundId(id);
	}

	@Override
	public List<String[]> getCompoundWithBiologicalRoles() throws Exception { 
		List<ModelCompound> list = this.compoundDAO.getAllModelCompoundDataOrderByKeggId();

		if(list.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(ModelCompound item: list) {
				String[] res = new String[6];
				res[0] = String.valueOf(item.getIdcompound());
				res[1] = (String) item.getName();
				res[2] = (String) item.getInchi();
				res[3] = (String) item.getExternalIdentifier();
				res[4] = (String) item.getEntryType();
				res[5] = (String) item.getFormula();
				parsedList.add(res);
			}			
			return parsedList;
		}
		return null;
	}

	@Override
	public List<String[]> getCompoundReactions(Integer id) throws Exception {
		return this.stoichservice.getAllModelStoichiometryAttributesByCompoundId(id);
	}

	@Override
	public Long countCompoundsByName(String name) throws Exception {
		return this.compoundDAO.countCompoundsByName(name);
	}

	@Override
	public List<String[]> getMetaboliteData(String name) throws Exception {
		return this.compoundDAO.getAllModelCompoundAttributesByName(name);
	}

	@Override
	public Integer getMetaboliteIdByName(String name) throws Exception {
		List<ModelCompound> res = this.compoundDAO.getAllModelCompoundByName(name);

		if(res != null) {
			for(ModelCompound compound : res)
				return compound.getIdcompound();
		}

		return -1;
	}

	@Override
	public int getCompoundIDbyExternalIdentifier(String externalId) throws Exception {
		int res = -1;

		Integer result = this.compoundDAO.getModelCompoundIdByExternalIdentifier(externalId);
		if (result != null)
			res = result;

		return res;
	}

	@Override
	public Map<String, Pair<Integer, String>> getModelInformationForBiomass(List<String> metaboliteIDs) throws Exception {

		Map<String, Pair<Integer, String>> map = new HashMap<String, Pair<Integer, String>>();

		for(String name : metaboliteIDs) {

			Map<Integer, String> res = this.compoundDAO.getModelCompoundIdAndMolecularWeightByKeggId(name);

			if(res != null) {
				for ( Integer x : res.keySet()) {

					Pair<Integer, String> pair = new Pair<Integer, String>(x, res.get(x));
					map.put(name, pair);
				}
			}
		}
		return map;
	}

	@Override
	public MetaboliteContainer getModelCompoundByName(String name) throws Exception {
		
		ModelCompound compound = this.compoundDAO.findUniqueByAttribute("name", name);
		
		if(compound == null)
			return null;

		MetaboliteContainer container = new MetaboliteContainer(compound.getExternalIdentifier());
		container.setName(compound.getName());
		container.setMetaboliteID(compound.getIdcompound());
		container.setFormula(compound.getFormula());
		container.setEntryType(container.getEntryType());
		container.setMolecular_weight(compound.getMolecularWeight());
		if(compound.getCharge() != null)
			container.setCharge(compound.getCharge().intValue());
		
		return container;
	}

	@Override
	public Map<Integer, MetaboliteContainer> getAllMetabolites() throws Exception {

		Map<Integer, MetaboliteContainer> res = new HashMap<>();

		List<String[]> list = this.compoundDAO.getAllModelCompoundAttributesByName2(); 

		if (list!= null) {

			for (String[] x : list) {

				//				("name"), compound.get("formula"), compound.get("idcompound"),compound.get("externalIdentifier")); 
				int i  = Integer.parseInt(x[2]);
				MetaboliteContainer metabolite = new MetaboliteContainer(i, x[1], x[0], x[3]);
				res.put(i, metabolite);
			}
		}
		return res;
	}



	@Override
	public List<Integer[]> getAllModelCompoundIdWithCompartmentIdCountedReactions(Boolean isCompartimentalized,
			Boolean inModel, List<ModelCompoundType> type, Boolean withTransporters) throws Exception {
		return this.compoundDAO.getAllModelCompoundIdWithCompartmentIdCountedReactions(isCompartimentalized, inModel, type, withTransporters);
	}

	@Override
	public void insertCompoundGeneratingExternalID(String name, String entryType, String formula, String molecularW,
			String charge) throws Exception {
		ModelCompound compound = new ModelCompound();
		compound.setName(name);
		compound.setEntryType(entryType);
		compound.setFormula(formula);
		compound.setMolecularWeight(molecularW);
		compound.setCharge(Short.valueOf(charge));

		Serializable id = this.compoundDAO.save(compound);

		compound.setExternalIdentifier("M"+String.valueOf(id));
		this.compoundDAO.update(compound);
	}

	@Override
	public void updateModelCompoundAttributes(String name, String entryType, String formula, String molecularW, Short charge, String externalIdentifier) {
		this.compoundDAO.updateModelCompoundAttributes(name, entryType, formula, molecularW, charge, externalIdentifier);
	}
	
	@Override
	public void updateModelCompoundAttributesByInternalId(Integer id, String name, String entryType, String formula, String molecularW, Short charge, String externalIdentifier) {
		this.compoundDAO.updateModelCompoundAttributesByInternalId(id, name, entryType, formula, molecularW, charge, externalIdentifier);
	}

	@Override
	public List<String> getAllCompoundsInModel() {
		return this.compoundDAO.getAllCompoundsInModel();
	}

	
	@Override
	public List<String> getAllCompounds() {
		return this.compoundDAO.getAllCompounds();
	}
	
	@Override
	public MetaboliteContainer getCompoundByExternalIdentifier(String identifier) {

		Map<String, Serializable> eqRestrictions = new HashMap<>();

		eqRestrictions.put("externalIdentifier", identifier);

		List<ModelCompound> compounds = this.compoundDAO.findByAttributes(eqRestrictions);

		if(compounds != null && compounds.size() > 0) {

			ModelCompound comp = compounds.get(0);
			MetaboliteContainer container = buildMetaboliteContainer(comp);	
			return container;
		}

		return null;
	}

	@Override
	public Integer insertModelCompound(String name, String inchi, String entry_type, 
			String external_identifier, String formula, String molecular_weight, 
			String neutral_formula, Short charge, 
			String smiles, Boolean hasBiologicalRoles) throws Exception {

		ModelCompound modelcompound = new ModelCompound();
		modelcompound.setName(name);
		modelcompound.setInchi(inchi);
		modelcompound.setFormula(formula);
		modelcompound.setExternalIdentifier(external_identifier);
		modelcompound.setEntryType(entry_type);
		modelcompound.setMolecularWeight(molecular_weight);
		modelcompound.setNeutralFormula(neutral_formula);
		modelcompound.setCharge(charge);
		modelcompound.setSmiles(smiles);
		modelcompound.setHasBiologicalRoles(hasBiologicalRoles);
		return (Integer) this.compoundDAO.save(modelcompound);

	}

	@Override
	public  Map<String, Integer> getExternalIdentifierAndIdCompound() {
		return this.compoundDAO.getExternalIdentifierAndIdCompound();
	}

	@Override
	public void removeCompoundByExternalIdentifier(String identifier) {

		List<ModelCompound> compounds = this.compoundDAO.findBySingleAttribute("externalIdentifier", identifier);

		if(compounds != null) {
			for(ModelCompound compound : compounds)
				this.compoundDAO.delete(compound);
		}
	}
	
	@Override
	public String getCompoundExternalIdentifierByInternalID(Integer internalID) {
		
		ModelCompound compound = this.compoundDAO.getModelCompound(internalID);
		
		return compound.getExternalIdentifier();
	}
}
