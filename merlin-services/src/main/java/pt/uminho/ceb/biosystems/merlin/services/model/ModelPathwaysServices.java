package pt.uminho.ceb.biosystems.merlin.services.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwayContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.Pathways;
import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;

/**
 * @author ODias
 *
 */
public class ModelPathwaysServices {

	public static List<Integer> getStats(String databaseName) {

		List<Integer> ret = new ArrayList<>(); 

		int num=0;
		int noname=0;
		int nosbml=0;

		try {

			List<PathwayContainer> containers = getAllFromPathwaySortedByName(databaseName);

			for(PathwayContainer container : containers){

				num++;
				if(container.getName()==null) noname++;
				if(container.getSbmlPath()==null) nosbml++;
			}

			ret.add(num);
			ret.add(noname);
			ret.add(nosbml);

		} 
		catch(Exception e) {
			e.printStackTrace();}
		return ret;
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception 
	 */
	public static List<PathwayContainer> getAllFromPathwaySortedByName(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllFromPathwaySortedByName();
	}

	/**
	 * @param names
	 * @param identifiers
	 * @param connection
	 * @return
	 */
	public static Map<Integer, List<Object>> getMainTableData(String databaseName, Map<Integer,String> names, Map<Integer,Integer> identifiers) {

		Map<Integer, List<Object>> ret = new HashMap<>();

		List<Integer> index = new ArrayList<>();
		Map<Integer, String[]> qls = new HashMap<>();

		try {

			List<PathwayContainer> containers = getAllFromPathwaySortedByName(databaseName);

			for(PathwayContainer container : containers){

				String[] ql = new String[4];
				ql[0] = container.getCode();
				ql[1] = container.getName();
				ql[2] = "0";
				ql[3] = "0";
				index.add(container.getIdPathway());
				qls.put(container.getIdPathway(), ql);
			}

			qls = InitDataAccess.getInstance().getDatabaseService(databaseName).countReactionsByPathwayID(qls, ProjectServices.isCompartmentalisedModel(databaseName));

			qls = InitDataAccess.getInstance().getDatabaseService(databaseName).countProteinIdByPathwayID(qls);

			for(int i=0;i<index.size();i++) {

				List<Object> ql = new ArrayList<Object>();
				String[] gark = qls.get(index.get(i));
				ql.add("");
				ql.add(gark[0]);
				ql.add(gark[1]);
				ql.add(gark[2]);
				ql.add(gark[3]);
				ret.put(index.get(i), ql);
				names.put(index.get(i), gark[0]);
			}
		}
		catch(Exception e)
		{e.printStackTrace();}

		return ret;
	}


	/**
	 * @param identifier
	 * @param connection 
	 * @return
	 * @throws Exception 
	 */
	public static Map<String,  List<List<String>>> getRowInfo(String databaseName, int pathwayIdentifier) throws Exception {

		Map<String, List<List<String>>> ret = new HashMap<>();

		List<String[]> result = InitDataAccess.getInstance().getDatabaseService(databaseName).countReactions(pathwayIdentifier, ProjectServices.isCompartmentalisedModel(databaseName));
		String key = "reactions";
		ret.put(key, new ArrayList<>());

		for(int i=0; i<result.size(); i++) {

			String[] list = result.get(i);
			List<String> ql = new ArrayList<String>();
			ql.add(list[1]);
			ql.add(list[2]);
			ret.get(key).add(ql);
		}

		List<ProteinContainer> format = ModelProteinsServices.getDataFromEnzyme(databaseName, pathwayIdentifier);

		List<String[]> data = new ArrayList<String[]>();

		for(ProteinContainer items : format) {

			String[] item = new String[4];
			item[0] = items.getExternalIdentifier();
			item[1] = items.getName();
			item[2] = items.getClass_();
//			item[3] = String.valueOf(items.getInModel());
			item[3] = "";    //Verify what this is		

			data.add(item);
		}

		result = data;
		key = "enzymes";
		ret.put(key, new ArrayList<>());

		for(int i=0; i<result.size(); i++){
			String[] list = result.get(i);

			List<String> ql = new ArrayList<String>();
			ql.add(list[0]);
			ql.add(list[1]);
			ql.add(list[2]);
			ql.add(list[3]);
			ret.get(key).add(ql);
		}

		return ret;

	}

	public static Integer insertModelPathwayCodeAndName(String databaseName, String code, String name) throws Exception {
		return InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelPathwayCodeAndName(code, name);
	}

	public static void insertModelPathwayIdAndSuperPathway(String databaseName, Integer id, Integer superID) throws Exception{
		InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelPathwayIdAndSuperPathway(id, superID);
	}

	public static boolean deleteModelPathwayHasEnzymeByPathwayId(String databaseName, Integer pathId) throws Exception {
		return InitDataAccess.getInstance().getDatabaseService(databaseName).deleteModelPathwayHasModelProteinByIdProtein(pathId);
	}

	public static boolean deleteModelPathwayHasReactionByPathwayId(String databaseName, Integer pathId) throws Exception {
		return InitDataAccess.getInstance().getDatabaseService(databaseName).deleteModelPathwayHasReactionByPathwayId(pathId);
	}

	public static boolean deleteModelPathwayHasEnzymeByProteinId(String databaseName, Integer proteinId) throws Exception{
		return InitDataAccess.getInstance().getDatabaseService(databaseName).deleteModelPathwayHasModelProteinByIdProtein(proteinId);
	}

	public static boolean deleteModelPathwayHasReactionByReactionId(String databaseName, Integer id) throws Exception{
		return InitDataAccess.getInstance().getDatabaseService(databaseName).deleteModelPathwayHasReactionByReactionId(id);
	}

	/**
	 * @param databaseName
	 * @return a list with all pathways, except the SuperPathways
	 * @throws Exception 
	 */
	public static List<String[]> getUpdatedPathways(boolean encoded, String databaseName) throws Exception {

		boolean isCompartimentalized = ProjectServices.isCompartmentalisedModel(databaseName);

		return  InitDataAccess.getInstance().getDatabaseService(databaseName).getUpdatedPathways(isCompartimentalized, encoded);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer, String> getPathwaysNames(String databaseName) throws Exception {

		return  InitDataAccess.getInstance().getDatabaseService(databaseName).getPathwaysNames();
	}

	/**
	 * @return list of all pathways, including superpathways
	 * @throws Exception 
	 */
	public static List<String> getPathways(String databaseName, boolean inModel) throws Exception {

		if(inModel)
			return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllPathwaysOrderedByNameInModelWithReaction(inModel);
		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllPathwaysNamesOrdered();

	}

	/**
	 * @param name
	 * @return
	 */
	public static Integer getPathwayIDbyName(String databaseName, String name) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getPathwayID(name);
	}

	/**
	 * @param name
	 * @return
	 */

	public static String getPathwayCode(String databaseName, String name) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getPathwayCodeByName(name);

	}

	/**
	 * @param rowID
	 * @return
	 * @throws Exception 
	 * @throws  
	 */
	public static String[] getExistingPathwaysNamesByReactionId(String databaseName, int reactionId) throws Exception {

		List<PathwayContainer> containers = getPathwaysByReactionId(databaseName, reactionId);

		String[] names = new String[containers.size()];

		for(int i = 0; i < containers.size(); i++)
			names[i] = containers.get(i).getName();

		return names;
	}

	/**
	 * @param databaseName
	 * @param name
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static int getPathwayIdByNameAndCode(String databaseName, String name, String code) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getPathwayIdByNameAndCode(name, code);
	}

	/**
	 * @param databaseName
	 * @param proteinId
	 * @return
	 * @throws Exception
	 */
	public static boolean checkPathwayHasEnzymeEntryByProteinId(String databaseName, Integer proteinId) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkPathwayHasEnzymeEntryByProteinId(proteinId);
	}

	/**
	 * @param databaseName
	 * @param proteinId
	 * @return
	 * @throws Exception
	 */
	public static boolean checkPathwayHasModelProtein(String databaseName, Integer pathwayId, Integer proteinId) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkPathwayHasEnzymeData(proteinId, pathwayId);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<String,Integer> getPathwayCodeAndIdpathway(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getPathwayCodeAndIdpathway();
	}

	/**
	 * @param databaseName
	 * @param pathwayId
	 * @param proteinId
	 * @return
	 * @throws Exception
	 */
	public static void insertModelPathwayHasModelProtein(String databaseName, Integer pathwayId, Integer proteinId) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelPathwayHasModelProtein(pathwayId, proteinId);
	}

	/**
	 * @param databaseName
	 * @param pathwayId
	 * @param proteinId
	 * @return
	 * @throws Exception
	 */
	public static void insertModelPathwayHasModelReaction(String databaseName, Integer idPathway, Integer idReaction) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelPathwayHasReaction(idReaction, idPathway);
	}

	/**
	 * @param databaseName
	 * @param idPathway
	 * @param compoundId
	 * @throws Exception
	 */
	public static void insertModelPathwayHasModelCompound(String databaseName, Integer idPathway, Integer compoundId) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelPathwayHasCompound(compoundId, idPathway);
	}

	/**
	 * @param databaseName
	 * @param idPathway
	 * @param idReaction
	 * @return
	 * @throws Exception
	 */
	public static boolean checkPathwayHasReactionData(String databaseName, Integer idPathway, Integer idReaction) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkPathwayHasReactionData(idReaction, idPathway);
	}

	/**
	 * @param databaseName
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer, PathwayContainer> getPathways(String databaseName,Integer id) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getPathways(id);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Set<String>> getAllPathways(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllPathways();
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Set<String>> getEnzymesPathways(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getEnzymesPathways();
	}

	/**
	 * @param databaseName
	 * @param pathID
	 * @param moduleId
	 * @throws Exception
	 */
	public static void insertModelPathwayHasModuleEntry(String databaseName, Integer pathID, Integer moduleId) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelPathwayHasModuleEntry(pathID, moduleId);
	}

	/**
	 * @param databaseName
	 * @param pathID
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public static boolean checkModelPathwayHasModuleEntry(String databaseName, Integer pathID, Integer moduleId) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkModelPathwayHasModuleEntry(pathID, moduleId);
	}

	/**
	 * @param databaseName
	 * @param reactionId
	 * @return
	 * @throws Exception
	 */
	public static boolean checkPathwayHasEnzymeEntryByReactionID(String databaseName, Integer reactionId) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkPathwayHasEnzymeEntryByReactionID(reactionId);
	}

	/**
	 * @param databaseName
	 * @param pathwayId
	 * @param superPathwayId
	 * @return
	 * @throws Exception
	 */
	public static boolean checkSuperPathwayData(String databaseName, Integer pathwayId, Integer superPathwayId) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkSuperPathwayData(pathwayId, superPathwayId);
	}

	/**
	 * @param databaseName
	 * @param pathwayId
	 * @param compoundId
	 * @return
	 * @throws Exception
	 */
	public static boolean checkPathwayHasCompoundData(String databaseName, Integer pathwayId, Integer compoundId) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkPathwayHasCompoundData(pathwayId, compoundId);
	}

	/**
	 * @param databaseName
	 * @param isCompartimentalized
	 * @return
	 * @throws Exception
	 */
	public static Long countPathwayHasReaction(String databaseName, boolean isCompartimentalized) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).countPathwayHasReaction(isCompartimentalized);
	}

	/**
	 * Add the pathway to model.
	 * @param statement 
	 * 
	 * @return The pathway database identifier.
	 * @throws Exception 
	 */
	public static String addPathway(String databaseName, Pathways pathway) throws Exception {

		Integer id = getPathwayIDbyName(databaseName, pathway.getName());

		if(id == null) 
			insertModelPathwayCodeAndName(databaseName, pathway.getCode(), pathway.getName());


		return pathway.getName();
	}

	/**
	 * @param databaseName
	 * @param source
	 * @throws IOException
	 * @throws Exception
	 */
	public static List<PathwayContainer> getPathwaysByReactionId(String databaseName, Integer reactionId) throws IOException, Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getPathwaysIDsByReactionID(reactionId);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static Map<Integer, List<PathwayContainer>> getPathwaysByReaction(String databaseName) throws IOException, Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getPathwaysByReaction();
	}
}
