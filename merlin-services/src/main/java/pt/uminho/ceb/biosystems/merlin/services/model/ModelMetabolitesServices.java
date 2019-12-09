package pt.uminho.ceb.biosystems.merlin.services.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import pt.uminho.ceb.biosystems.merlin.auxiliary.ModelCompoundType;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompound;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public class ModelMetabolitesServices {


	private static final int _ID_CREATOR = 1000000;

	/**
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static List<Pair<Integer,String>> getStats(String databaseName) throws Exception {

		List<Pair<Integer,String>> res = new ArrayList<>();

		boolean isCompartimentalized = ProjectServices.isCompartmentalisedModel(databaseName);

		List<Set<String>> result = InitDataAccess.getInstance().getDatabaseService(databaseName).getCompoundsReactions(isCompartimentalized);
		if(!result.isEmpty()) {
			Set<String> reactants = result.get(0);
			Set<String> products = result.get(1);
			Set<String> reactionsReactants = result.get(2);
			Set<String> productsReactants = result.get(3);

			res.add(new Pair<Integer, String>(reactants.size(),""));
			res.add(new Pair<Integer, String>(products.size(),""));
			res.add(new Pair<Integer, String>(reactionsReactants.size(),""));
			res.add(new Pair<Integer, String>(productsReactants.size(),""));

			Set<String> metabolites = new HashSet<String>(products);
			metabolites.retainAll(reactants);

			res.add(new Pair<Integer, String>(metabolites.size(), ""));

			List<Object[]> data = ModelCompartmentServices.getReactantsOrProductsInCompartment(databaseName, isCompartimentalized, true, false);

			for(int i=0; i<data.size(); i++) {
				Object[] list = new String[2];
				list = data.get(i);
				res.add(new Pair<Integer, String>(Integer.parseInt(list[1].toString()),list[0].toString()));
			}

			data = ModelCompartmentServices.getReactantsOrProductsInCompartment(databaseName, isCompartimentalized, false, true);

			for(int i=0; i<data.size(); i++) {
				Object[] list = new String[2];
				list = data.get(i);
				res.add(new Pair<Integer, String>(Integer.parseInt(list[1].toString()),list[0].toString()));
			}

		}

		return res;
	}

	/**
	 * @param selection
	 * @param encoded
	 * @param types
	 * @param typeMap
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static Map<Integer, List<Object>> getMainTableData(String databaseName, int selection, boolean encoded, ArrayList<Integer> types,
			Map<Integer, String> typeMap) throws Exception {

		ArrayList<Integer> index = new ArrayList<>();

		Map<Integer, List<Object>> metabolitesMap = new HashMap<>();


		Boolean isCompartimentalized = ProjectServices.isCompartmentalisedModel(databaseName);

		List<ModelCompoundType> typeList = new ArrayList<>();
		if (types.get(3) != 1){
			if (types.get(0) == 1)
				typeList.add(ModelCompoundType.GLYCAN);
			if (types.get(1) == 1){
				if (types.get(0) == 1)
					typeList.add(ModelCompoundType.COMPOUND);
				else 
					typeList.add(ModelCompoundType.COMPOUND);
			}
			if (types.get(2) == 1){
				if (types.get(0) == 1 || types.get(1) == 1)
					typeList.add(ModelCompoundType.DRUGS);
				else
					typeList.add(ModelCompoundType.DRUGS);
			}

		}


		List<Integer[]> data = InitDataAccess.getInstance().getDatabaseService(databaseName).getAllModelCompoundIdWithCompartmentIdCountedReactions(isCompartimentalized, encoded, typeList, false);


		Map<Integer, Integer> sum_not_transport = new HashMap<>();

		for(int i=0; i<data.size(); i++) {

			Object[] list = data.get(i);
			int speciesId = Integer.valueOf(list[0]+"") + (Integer.valueOf(list[1]+"")*_ID_CREATOR);
			sum_not_transport.put(speciesId, Integer.valueOf(list[2]+""));
		}

		data = InitDataAccess.getInstance().getDatabaseService(databaseName).getAllModelCompoundIdWithCompartmentIdCountedReactions(isCompartimentalized, encoded, typeList, true);



		Map<Integer, Integer> sum_transport = new HashMap<>();

		for(int i=0; i<data.size(); i++) {

			Object[] list = data.get(i);
			int speciesId = Integer.valueOf(list[0]+"") + (Integer.valueOf(list[1]+"")*_ID_CREATOR);
			sum_not_transport.put(speciesId, Integer.valueOf(list[2]+""));
		}

		//calculate which metabolites have both properties and add to table 
		//aux = original e inmodel
		List<Integer> reversibleCompounds = InitDataAccess.getInstance().getDatabaseService(databaseName).getReversibilities(encoded, isCompartimentalized);

		Set<Integer> both = new HashSet<>();

		List<String[]> datas = InitDataAccess.getInstance().getDatabaseService(databaseName).getMetabolitesWithBothProperties(isCompartimentalized, encoded, typeList);


		for(int i=0; i<datas.size(); i++){
			String[] list = datas.get(i);

			//				String speciesId = list[3]+"_"+list[7];
			int speciesId = Integer.parseInt(list[3]) + (Integer.parseInt(list[7])*_ID_CREATOR);

			if(!index.contains(speciesId)) {

				List<Object> ql = new ArrayList<Object>();

				ql.add("");
				ql.add(list[0]);
				ql.add(list[6]);
				ql.add(list[1]);
				ql.add(list[4]);

				int sumNT = 0;
				if(sum_not_transport.containsKey(speciesId))
					sumNT = sum_not_transport.get(speciesId);
				ql.add(sumNT+"");

				int sumT = 0;
				if(sum_transport.containsKey(speciesId))
					sumT = sum_transport.get(speciesId);
				ql.add(sumT+"");

				index.add(speciesId); // marker for species identifier

				if(Integer.parseInt(list[2])>1 || reversibleCompounds.contains(Integer.parseInt(list[3]))) {

					typeMap.put(speciesId, "both");
					both.add(speciesId);
				}
				else {

					typeMap.put(speciesId, "other");
				}
				metabolitesMap.put(speciesId, ql);
			}
		}

		// add metabolites have one property
		datas = InitDataAccess.getInstance().getDatabaseService(databaseName).getMetabolitesProperties(isCompartimentalized, encoded, typeList);

		for(int i=0; i<datas.size(); i++){

			String[] list = datas.get(i);
			//				String speciesId = list[3] + "_" + list[7];
			int speciesId = Integer.parseInt(list[3]) + (Integer.parseInt(list[7])*_ID_CREATOR);
			if(!both.contains(speciesId)) {

				List<Object> ql = new ArrayList<Object>();
				ql.add("");
				ql.add(list[0]);
				ql.add(list[6]);
				ql.add(list[1]);
				ql.add(list[4]);

				int sumNT = 0;
				if(sum_not_transport.containsKey(speciesId))
					sumNT = sum_not_transport.get(speciesId);
				ql.add(sumNT);

				int sumT = 0;
				if(sum_transport.containsKey(speciesId))
					sumT = sum_transport.get(speciesId);
				ql.add(sumT);

				String type = "both";

				if(Double.parseDouble(list[2])>0 && selection != 2)
					type = "product";
				else if(Double.parseDouble(list[2])<0 && selection != 3)
					type = "reactant";

				typeMap.put(speciesId, type);
				metabolitesMap.put(speciesId, ql);
			}
		}

		if(!encoded) {

			List<ModelCompound> metabolites = InitDataAccess.getInstance().getDatabaseService(databaseName).getMetabolitesNotInModel(typeList);

			for(ModelCompound metabolite : metabolites){

				List<Object> ql = new ArrayList<Object>();

				ql.add("");
				ql.add(metabolite.getName());
				ql.add("-");
				ql.add(metabolite.getFormula());
				ql.add(metabolite.getExternalIdentifier());
				ql.add("0");
				ql.add("0");

				//					String speciesId = list[0];
				int speciesId = metabolite.getIdcompound();

				//					speciesId = speciesId.concat("_0"); //to avoid error

				typeMap.put(speciesId, "");
				metabolitesMap.put(speciesId, ql);
			}
		}


		return metabolitesMap;
	}	

	/**
	 * @param metaboliteIdentifier
	 * @param compartment
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, List<ArrayList<String>>> getRowInfo(String databaseName, int metaboliteIdentifier) throws Exception {

		Map<String, List<ArrayList<String>>> output = new HashMap<>();

		int compoundIdentifier = metaboliteIdentifier%_ID_CREATOR;
		int compartmentIdentifier = (metaboliteIdentifier-compoundIdentifier)/_ID_CREATOR;

		output.put("entry type", listToLists( InitDataAccess.getInstance().getDatabaseService(databaseName).getEntryType(compoundIdentifier)));

		output.put("synonyms", listToLists( InitDataAccess.getInstance().getDatabaseService(databaseName).getAliasClassC(compoundIdentifier)));

		boolean isCompartimentalizedModel = ProjectServices.isCompartmentalisedModel(databaseName);

		output.put("reactions",InitDataAccess.getInstance().getDatabaseService(databaseName).getReactions(compoundIdentifier, compartmentIdentifier, isCompartimentalizedModel));

		return output;
	}

	private static List<ArrayList<String>> listToLists(List<String> list){

		List<ArrayList<String>> out = new ArrayList<>();

		for(String s:list) {

			ArrayList<String> l = new ArrayList<>();
			l.add(s);			
			out.add(l);
		}

		return out;
	}

	/**
	 * @param name
	 * @param entryType
	 * @param formula
	 * @param molecularW
	 * @param charge
	 * @param metabolite
	 * @param keggID
	 * @throws IOException 
	 */
	public static void updateData(String name, String entryType, String formula, String molecularW, Short charge, String externalIdentifier, String databaseName) throws Exception{

		InitDataAccess.getInstance().getDatabaseService(databaseName).updateModelCompoundAttributes(name, entryType, formula, molecularW, charge, externalIdentifier);

	}

	/**
	 * @param name
	 * @param entryType
	 * @param formula
	 * @param molecularW
	 * @param charge
	 * @param connection
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static void insertData(String name, String entryType, String formula, String molecularW, String charge, String  databaseName) throws IOException, Exception{
		InitDataAccess.getInstance().getDatabaseService(databaseName).insertCompoundGeneratingExternalID(name, entryType, formula, molecularW, charge);
	}

	/**
	 * @param databaseName
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public static long getMetaboliteOccurrence (String databaseName, String name) throws Exception{
		return InitDataAccess.getInstance().getDatabaseService(databaseName).countCompoundsByName(name);
	}

	/**
	 * @param databaseName
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static String getKeggIdOccurence (String databaseName, String name) throws Exception{

		int count = (int) getMetaboliteOccurrence(databaseName, name);

		if(count == 0)
			return "";

		if(count == 1) {
			MetaboliteContainer compound = getModelCompoundByName(databaseName, name);			

			if(compound != null)
				return compound.getExternalIdentifier();
		}

		return null;
	}

	/**
	 * @param databaseName
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static MetaboliteContainer getModelCompoundByName (String databaseName, String name) throws Exception {
		return InitDataAccess.getInstance().getDatabaseService(databaseName).getModelCompoundByName(name);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static boolean isMetabolicDataLoaded(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).isMetabolicDataLoaded();

	}

	/**
	 * @param metaboliteContainer
	 * @param isReactant
	 * @param newCompartmentID 
	 * @param statement 
	 * @param reactionID 
	 * @throws SQLException 
	 */
	public static void addCompounds(String databaseName, List<MetaboliteContainer> metabolitesContainer, boolean isReactant, Integer reactionID) throws Exception {

		for(MetaboliteContainer metaboliteContainer : metabolitesContainer) {

			int compartmentID = metaboliteContainer.getCompartmentID();

			double stoichiometry = metaboliteContainer.getStoichiometric_coefficient();

			if(isReactant && stoichiometry>0)
				stoichiometry = stoichiometry*-1;

			Integer stoichId = ModelStoichiometryServices.getStoichiometryID(databaseName, reactionID, metaboliteContainer.getMetaboliteID()+"", compartmentID, stoichiometry);

			if(stoichId == null){
				//					System.out.println("Insert into model_stoichiometry");
				ModelStoichiometryServices.insertStoichiometry(databaseName, reactionID, metaboliteContainer.getMetaboliteID(), compartmentID, stoichiometry);
			}
		}


	}

	/**
	 * Add biomass compound to model.
	 * 
	 * @param databaseName
	 * @param name
	 * @param inchi
	 * @param external_identifier
	 * @param entry_type
	 * @param formula
	 * @param molecular_weight
	 * @param neutral_formula
	 * @param charge
	 * @param smiles
	 * @param hasBiologicalRoles
	 * @param statement
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static Integer insertCompoundToDatabase(String databaseName, String name, String inchi, String external_identifier, String entry_type, String formula,
			String molecular_weight, String neutral_formula, Short charge, String smiles, Boolean hasBiologicalRoles) throws IOException, Exception {

		MetaboliteContainer compound = getModelCompoundByName(databaseName, name);
		
		Integer ret = null;

		if(compound == null) {

			ret = insertCompound(databaseName, name, inchi, entry_type, external_identifier, 
					formula, molecular_weight, neutral_formula, charge, smiles, hasBiologicalRoles);
		}
		else 
			ret = compound.getMetaboliteID();

		return ret;
	}

	public static Integer insertCompoundToDatabase(String databaseName, String eproteinName, String externalIdentifier,
			String eProteinMolecularWeight) throws IOException, Exception {


		return insertCompoundToDatabase(databaseName, eproteinName, null, externalIdentifier, "COMPOUND", null, eProteinMolecularWeight, null, null, null, null);
	}

	public static Integer insertCompoundToDatabase(String databaseName, String eproteinName, 
			String eProteinMolecularWeight) throws IOException, Exception {


		return insertCompoundToDatabase(databaseName, eproteinName, null, null, null, null, eProteinMolecularWeight, null, null, null, null);
	}



	public static Integer insertCompound(String databaseName, String name, String inchi, String entry_type, String external_identifier, 
			String formula, String molecular_weight, String neutral_formula, 
			Short charge, String smiles, Boolean hasBiologicalRoles) throws IOException, Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelCompound(name, inchi, entry_type, external_identifier, formula, molecular_weight, neutral_formula, charge, smiles, hasBiologicalRoles);

	}

	/**
	 * @param databaseName
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static List<String> getAllCompoundsInModel(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllCompoundsInModel();

	}

	/**
	 * @param databaseName
	 * @param externalID
	 * @return
	 * @throws Exception
	 */
	public static int getCompoundIDbyExternalIdentifier(String databaseName, String externalID) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getCompoundIDbyExternalIdentifier(externalID);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Integer> getExternalIdentifierAndIdCompound(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getExternalIdentifierAndIdCompound();
	}

	/**
	 * @param databaseName
	 * @param compoundIdentifier
	 * @return
	 * @throws Exception
	 */
	public static MetaboliteContainer getCompoundByExternalIdentifier(String databaseName, String compoundIdentifier) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getCompoundByExternalIdentifier(compoundIdentifier);
	}

	/**
	 * @param databaseName
	 * @param compoundIdentifier
	 * @throws Exception
	 */
	public static void removeCompoundByExternalIdentifier(String databaseName, String compoundIdentifier) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).removeCompoundByExternalIdentifier(compoundIdentifier);
	}

	/**
	 * Load metabolites
	 * 
	 * @param databaseName
	 * @param metabolites
	 * @param metabolites_id
	 * @param concurrentLinkedQueue
	 * @param statement
	 * @param 
	 * @throws Exception
	 */
	public static void loadMetabolites(String databaseName, ConcurrentLinkedQueue<MetaboliteContainer> metabolites, ConcurrentHashMap<String,Integer> metabolites_id, 
			ConcurrentLinkedQueue<String> concurrentLinkedQueue, boolean importedFromSBML) throws Exception {

		for (MetaboliteContainer metaboliteContainer : metabolites) {

			if(!metabolites_id.containsKey(metaboliteContainer.getExternalIdentifier())){

				String entry_type = null;

				if(importedFromSBML){
					entry_type="COMPOUND";
				}
				else{
					if(metaboliteContainer.getExternalIdentifier().startsWith("C"))
					{entry_type="COMPOUND";}
					if(metaboliteContainer.getExternalIdentifier().startsWith("G"))
					{entry_type="GLYCAN";}
					if(metaboliteContainer.getExternalIdentifier().startsWith("D"))
					{entry_type="DRUGS";}
					if(metaboliteContainer.getExternalIdentifier().startsWith("B"))
					{entry_type="BIOMASS";}
				}

				String name = null;
				String formula = null;
				String mw = null;
				boolean chbr = false;

				if(metaboliteContainer.getName()!=null)
					name = (metaboliteContainer.getName());

				if(metaboliteContainer.getFormula()!=null)
					formula = (metaboliteContainer.getFormula());

				if(metaboliteContainer.getMolecular_weight()!=null)
					mw = (metaboliteContainer.getMolecular_weight());

				if(concurrentLinkedQueue.contains(metaboliteContainer.getExternalIdentifier()))
					chbr = true;

				Integer id = insertCompound(databaseName, name, null, entry_type, metaboliteContainer.getExternalIdentifier(), formula, mw, null, null, null, chbr);
				
				metaboliteContainer.setMetaboliteID(id);
			}
		}
	}
}