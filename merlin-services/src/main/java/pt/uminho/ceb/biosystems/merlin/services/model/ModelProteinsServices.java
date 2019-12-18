package pt.uminho.ceb.biosystems.merlin.services.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwayContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;

public class ModelProteinsServices  {

	/**
	 * @param connection
	 * @return
	 * @throws Exception 
	 * @throws  
	 */
	public static List<Integer> getStats(String databaseName) throws Exception {

		List<Integer> res = new ArrayList<>();

		int[] result = InitDataAccess.getInstance().getDatabaseService(databaseName).countProteins();

		int num = result[0];
		res.add(result[0]);
		res.add(result[1]);

		int synonyms = (int) InitDataAccess.getInstance().getDatabaseService(databaseName).countProteinsSynonyms("p");
		res.add(synonyms);

		Double averageSynonyms = 0.0;

		if(num>0)
			averageSynonyms = Math.round((synonyms/num)*1000.0)/1000.0;

		res.add(averageSynonyms.intValue());

		int value = (int) InitDataAccess.getInstance().getDatabaseService(databaseName).countProteinsEnzymesNotLikeSource("TRANSPORTERS");
		res.add(value);

		value = (int) InitDataAccess.getInstance().getDatabaseService(databaseName).countProteinsTransporters();
		res.add(value);

		value = (int) InitDataAccess.getInstance().getDatabaseService(databaseName).countProteinsComplexes();
		res.add(value);

		int p_g = InitDataAccess.getInstance().getDatabaseService(databaseName).countProteinsAssociatedToGenes();
		res.add(p_g);

		return res;
	}

	/**
	 * @param encoded
	 * @param proteins
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static List<String[]> getMainTableData(String databaseName, boolean encoded,  Map<String,Integer> proteins) throws Exception {

		proteins.putAll(InitDataAccess.getInstance().getDatabaseService(databaseName).getProteinsCountFromSubunit());
		
		if(encoded)
			return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllEncodedEnzymes();

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllEnzymes();

	}


	/**
	 * @param ecnumber
	 * @param id
	 * @return
	 */
	public static List<List<String[]>> getRowInfo(String databaseName, int proteinIdentifier) {

		List<List<String[]>> resultList = new ArrayList<>();

		try {

			boolean isCompartimentalized = ProjectServices.isCompartmentalisedModel(databaseName);
			Map<Integer, ReactionContainer> table = InitDataAccess.getInstance().getDatabaseService(databaseName).getReactionsData(null, proteinIdentifier, isCompartimentalized);
			List<String[]> result = new ArrayList<String[]>();
			for(Integer key : table.keySet()) {
				ReactionContainer container = table.get(key);
				String[] list = new String[5];

				list[0]= container.getExternalIdentifier();
				list[1]= container.getEquation();
				list[2]= container.getSource().toString();
				list[3]= String.valueOf(container.isInModel());
				list[4]= String.valueOf(container.isReversible());

				result.add(list);
			}


			resultList.add(result);

			result = InitDataAccess.getInstance().getDatabaseService(databaseName).getGeneData(proteinIdentifier);
			resultList.add(result);

			Map<Integer, PathwayContainer> table1 = ModelPathwaysServices.getPathways(databaseName, proteinIdentifier);
			result = new ArrayList<String[]>();
			for(Integer key : table1.keySet()) {

				PathwayContainer pathcontainer = table1.get(key);
				String[] list = new String[2];

				list[0]= pathcontainer.getCode();
				list[1]= pathcontainer.getName();

				result.add(list);
			}
			resultList.add(result);


			result = ModelSubunitServices.getGPRstatusAndReactionAndDefinition(databaseName, proteinIdentifier);
			resultList.add(result);

			result =  InitDataAccess.getInstance().getDatabaseService(databaseName).getAliasClassP(proteinIdentifier);
			resultList.add(result);

			String ecNumber = getEnzymeEcNumberByProteinID(databaseName, proteinIdentifier);

			result = ModelGenesServices.getGeneData2(databaseName, ecNumber, proteinIdentifier);
			resultList.add(result);


		}
		catch(Exception e) {

			e.printStackTrace();
		}

		return resultList;
	}

	/**
	 * @param row
	 * @throws Exception 
	 */
	public static void removeProtein(String databaseName, int identifier) throws Exception{

			List<String> enzymesIDs = new ArrayList<String>();
			Boolean[] inModel = new Boolean[1];
			
			ProteinContainer protein = getProteinData(databaseName, identifier);
			
			if(protein != null) {	//isto é estranho, ao fazer debug deve alterar-se os parametros do removeEnzymesAssignments
				
				enzymesIDs.add(protein.getExternalIdentifier());
//				inModel[0] = protein.getInModel();
				
			}
				
			for(String enz : enzymesIDs)
				ModelProteinsServices.removeEnzymesAssignments(databaseName, enz, enzymesIDs, inModel, identifier, true);

			ModelProteinsServices.removeProtein(databaseName, identifier);
	}

	/**
	 * @param selectedRow
	 * @return
	 * @throws Exception 
	 */
	public static ProteinContainer getProteinData(String databaseName, int identifierProtein) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getProteinData(identifierProtein);

	}

	/**
	 * @param selectedRow
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static List<String> getSynonyms(String databaseName, int identifierProtein) throws Exception {

		return ModelAliasesServices.getSynonyms(databaseName, "p", identifierProtein);
		
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static String[][] getProteins(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getProteins();
	}

	/**
	 * @param name
	 * @param clas
	 * @param inchi
	 * @param molecular_weight
	 * @param molecular_weight_kd
	 * @param molecular_weight_exp
	 * @param molecular_weight_seq
	 * @param pi
	 * @param selectedRow
	 * @param synonyms
	 * @param oldSynonyms
	 * @param enzymes
	 * @param oldEnzymes
	 * @param inModel
	 * @param oldInModel
	 * @param connection
	 * @return
	 */
	public static boolean updateProtein(String databaseName, ProteinContainer protein, String[] synonyms,
			String[] oldSynonyms, String[] enzymes, String[] oldEnzymes) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).updateProtein(protein, synonyms, oldSynonyms, enzymes, oldEnzymes);
	}


	/**
	 * @param databaseName
	 * @param name
	 * @param classString
	 * @return
	 * @throws Exception
	 */
	public static Integer insertProtein(String databaseName, String name, String classString)  throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).insertNewProteinEntry(name, classString);
	}
	
	/**
	 * @param databaseName
	 * @param container
	 * @return
	 * @throws Exception
	 */
	public static Integer insertProtein(String databaseName, ProteinContainer container)  throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).insertNewProteinEntry(container);
	}
	
	/**
	 * @param name
	 * @param classString
	 * @param inchi
	 * @param molecular_weight
	 * @param molecular_weight_kd
	 * @param molecular_weight_exp
	 * @param molecular_weight_seq
	 * @param pi
	 * @param synonyms
	 * @param enzymes
	 * @param inModel
	 * @param connection
	 * @return
	 */
	public static void insertProtein(String databaseName, ProteinContainer protein,
			String[] synonyms, String[] enzymes)  throws Exception{

		InitDataAccess.getInstance().getDatabaseService(databaseName).insertProtein(protein, synonyms, enzymes);
	}

	/**
	 * @param selectedRow
	 * @param ecnumber
	 * @param enzymes_ids
	 * @param inModel
	 * @param stmt
	 * @throws SQLException 
	 */
	public static void removeEnzymesAssignments(String databaseName, String ecnumber, List<String> enzymes_ids, Boolean[] inModel, int proteinID, boolean removeReaction) throws Exception {


		InitDataAccess.getInstance().getDatabaseService(databaseName).removeEnzymesAssignments(ecnumber, inModel, enzymes_ids, proteinID, removeReaction);
	}

	/**
	 * @param databaseName
	 * @param name
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static Integer getProteinIDFromName(String databaseName, String name) throws IOException, Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getProteinIDFromName(name);
	}

	/**
	 * @param databaseName
	 * @param name
	 * @param class_
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static int getProteinIDByNameAndClass(String databaseName, String name, String class_) throws IOException, Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getProteinIDFromNameAndClass(name, class_);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static List<String[]> getAllFromProteinComposition(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllFromProteinComposition();
	}

	public static List<Integer> getProteinIdByPathwayID(String databaseName, Integer proteinId) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getProteinIdByPathwayID(proteinId);

	}


	public static Map<String, Integer> getEnzymeEcNumberAndProteinID(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getEnzymeEcNumberAndProteinID();

	}

	public static Map<Integer, List<Integer>> getEnzymesCompartments(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getEnzymesCompartments();

	}

	public static String getEnzymeEcNumberByProteinID(String databaseName, Integer proteinId) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getEnzymeEcNumberByProteinID(proteinId);

	}

	public static List<ProteinContainer> getDataFromEnzyme(String databaseName, Integer pathId) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getDataFromEnzyme(pathId);

	}

	public static List<ProteinContainer> getProducts(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getProducts();

	}

	public static ProteinContainer getProteinEcNumberAndInModelByProteinID(String databaseName, Integer proteinID) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getProteinEcNumberAndInModelByProteinID(proteinID);

	}

	public static ProteinContainer getProteinByEcNumber(String databaseName, String ecNumber) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getProteinIdByEcNumber(ecNumber);

	}

	public static List<ProteinContainer> getProteinIdByIdGene(String databaseName, Integer idGene) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getProteinIdByIdgene(idGene);

	}

	public static void updateProteinSetEcNumber(String databaseName, Integer idProtein, String ecNumber) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).updateProteinSetEcNumber(idProtein, ecNumber);
	}

	public static void removeProtein(String databaseName, Integer idProtein) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).removeProtein(idProtein);
	}
	
	public static void insertModelEnzymaticCofactor(String databaseName, Integer idCompound, Integer idProtein) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelEnzymaticCofactor(idCompound, idProtein);
	}
	
	public static boolean checkModelEnzymaticCofactorEntry(String databaseName, Integer idCompound, Integer idProtein) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkModelEnzymaticCofactorEntry(idCompound, idProtein);
	}
	
	public static List<ProteinContainer> getEnzymeHasReaction(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getEnzymeHasReaction();
	}
	
	public static List<String[]> getProteinComposition(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getProteinComposition();
	}
	
	/**
	 * @return
	 * @throws Exception 
	 */
	public static List<ProteinContainer> getEnzymesModel(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getEnzymesModel();
	}
	
	/**
	 * @param databaseName
	 * @param proteinId
	 * @return
	 * @throws Exception
	 */
	public static List<CompartmentContainer> getProteinCompartmentsByProteinId(String databaseName, Integer proteinId) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getProteinCompartmentsByProteinId(proteinId);
	}

}

