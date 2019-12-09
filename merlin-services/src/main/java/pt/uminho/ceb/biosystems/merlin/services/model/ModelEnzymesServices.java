package pt.uminho.ceb.biosystems.merlin.services.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwayContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;

/**
 * @author ODias
 *
 */
public class ModelEnzymesServices {

	/**
	 * @param encoded
	 * @return
	 * @throws Exception 
	 */
	public static List<String[]> getAllEnzymes(String databaseName, boolean encoded) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllEnzymes(ProjectServices.isCompartmentalisedModel(databaseName), encoded);
	}

	/**
	 * @param ecNumber
	 * @param identifier
	 * @param connection 
	 * @return
	 * @throws Exception 
	 */
	public static List<List<String[]>> getRowInfo(String databaseName, String ecNumber, Integer identifier) throws Exception {

		List<List<String[]>> ret = new ArrayList<>();

		int i = 0;

		boolean isCompartimentalized = ProjectServices.isCompartmentalisedModel(databaseName);
		Map<Integer, ReactionContainer> table = InitDataAccess.getInstance().getDatabaseService(databaseName).getReactionsData(ecNumber, identifier, isCompartimentalized);
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

		result = InitDataAccess.getInstance().getDatabaseService(databaseName).getGeneData(identifier);

		ret.add(i++,result);

		Map<Integer, PathwayContainer> table1 = ModelPathwaysServices.getPathways(databaseName, identifier);
		for(Integer key : table1.keySet()) {

			PathwayContainer pathcontainer = table1.get(key);
			String[] list = new String[2];

			list[0]= pathcontainer.getCode();
			list[1]= pathcontainer.getName();

			result.add(list);
		}

		ret.add(i++,result);

		List<ProteinContainer> data = ModelSubunitServices.getModelSubunitAttributes(databaseName, identifier);

		List<String[]> subunitAtributes = new ArrayList<String[]>();

		for(ProteinContainer item : data) {
			String[] list = new String[3];

			list[0]= item.getName();
			list[1]= item.getClass_();
			list[2]= item.getExternalIdentifier();

			subunitAtributes.add(list);

		}

		ret.add(i++,subunitAtributes);

		result =   InitDataAccess.getInstance().getDatabaseService(databaseName).getAliasClassP(identifier);

		ret.add(i++,result);

		result = ModelGenesServices.getGeneData2(databaseName, ecNumber, identifier);

		ret.add(i++,result);

		return ret;

	}

	/**
	 * @param databaseName
	 * @param model_protein_idprotein
	 * @param ecnumber
	 * @param inModel
	 * @param source
	 * @throws IOException
	 * @throws Exception
	 */
	public static void updateEnzyme(String databaseName, Integer model_protein_idprotein, String ecnumber, boolean inModel, SourceType source) throws IOException, Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).updateProteinSetEcNumberSourceAndInModel(model_protein_idprotein, ecnumber, inModel, source.toString());

	}

	/**
	 * Load Enzyme Information
	 * Returns reactions associated to the given enzymes in database.
	 * 
	 * @param idGene
	 * @param ecNumber
	 * @param proteinName
	 * @param statement
	 * @param integratePartial
	 * @param integrateFull
	 * @param insertProductNames
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static Map<String, List<Integer>> loadEnzymeGetReactions(String databaseName, Integer idGene, Set<String> ecNumber, String proteinName, 
			boolean integratePartial, boolean integrateFull, boolean insertProductNames, boolean compartmentalisedModel) throws IOException, Exception {

		Integer idProtein = null;
		Map<String, List<Integer>> enzymesReactions = new HashMap<>();
		List<ProteinContainer> proteinsContainers = ModelSubunitServices.getModelSubunitAttributes(databaseName, idGene);
		Set<String> ecs = new HashSet<>();

		for(ProteinContainer item : proteinsContainers) {
			ecs.add(item.getClass_());
		}


		for(String enzyme : ecNumber) {

			List<Integer> reactions_ids = new ArrayList<>();

			if(((enzyme.contains(".-") && integratePartial) || (!enzyme.contains(".-") && integrateFull)) && !enzyme.isEmpty()) {

				ProteinContainer container = ModelProteinsServices.getProteinByEcNumber(databaseName, enzyme);

				boolean go = false;

				if(container != null) {

					idProtein = container.getIdProtein();

					ModelSubunitServices.removeSubunitByGeneIdAndProteinId(databaseName, idGene, idProtein);

					go = !container.getInModel();

				}
				else {

					if(proteinName==null)
						proteinName = enzyme;

					idProtein = ModelProteinsServices.getProteinIDFromName(databaseName, proteinName);

					if(idProtein == null) {

						idProtein = ModelProteinsServices.insertProtein(databaseName, proteinName, null);
						insertProductNames = false;
					}

					go = true;
				}	

				if(go) {

					updateEnzyme(databaseName, idProtein, enzyme, true, SourceType.HOMOLOGY);

					if(!enzyme.contains(".-")) {

						List<ReactionContainer> containers = ModelReactionsServices.getDistinctReactionByProteinIdAndCompartimentalized(databaseName, idProtein, compartmentalisedModel);

						for(ReactionContainer item : containers) {
							reactions_ids.add(item.getReactionID());
						}

						List<ReactionContainer> containers2 = ModelReactionsServices.getReactionIdFromProteinIdWithPathwayIdNull(databaseName, idProtein);

						for(ReactionContainer item : containers2) {
							reactions_ids.add(item.getReactionID());
						}

						for(int idreaction: reactions_ids) {

							ModelReactionsServices.updateModelReactionInModelByReactionId(databaseName, idreaction, true);

							Integer idReactionLabel = ModelReactionsServices.getIdReactionLabelFromReactionId(databaseName, idreaction);

							ModelReactionsServices.updateSourceByReactionLabelId(databaseName, idReactionLabel, "HOMOLOGY");

						}

					}
				}

						
				boolean exists = ModelSubunitServices.checkModelSubunitEntry(databaseName, idGene, idProtein);
		
				if(!exists)
					ModelSubunitServices.insertModelSubunit(databaseName, idGene, idProtein);

				if(insertProductNames)
					ModelAliasesServices.insertNewModelAliasEntry(databaseName, "p", idProtein, proteinName);
			}
			enzymesReactions.put(enzyme, reactions_ids);
		}
		return enzymesReactions;
	}


	/**
	 * @param databaseName
	 * @param reactionID
	 * @return
	 * @throws Exception
	 */
	public static List<String> getEcNumbersList(String databaseName, Integer reactionID) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getEcNumbersList(reactionID);

	}

	public static List<String> getECNumbersWithModules(String databaseName) throws IOException, Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getECNumbersWithModules();

	}

	public static void updateECNumberModuleStatus(String databaseName,String ecNumber, String status) throws IOException, Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).updateECNumberModuleStatus(ecNumber, status);

	}

	public static Map<String, List<String>> getECNumbers(String databaseName) throws IOException, Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getECNumbers();

	}
}
