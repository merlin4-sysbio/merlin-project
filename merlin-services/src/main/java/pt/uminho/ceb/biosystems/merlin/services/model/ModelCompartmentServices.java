package pt.uminho.ceb.biosystems.merlin.services.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;

public class ModelCompartmentServices {

	public static Integer insertNameAndAbbreviation(String databaseName, String name, String abb) throws Exception{
		return InitDataAccess.getInstance().getDatabaseService(databaseName).insertNameAndAbbreviation(name, abb);
	}

	public static Map<Integer, String> getModelCompartmentIdAndName(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getModelCompartmentIdAndName();
	}

	public static Map<Integer, String> getModelCompartmentIdAndAbbreviation(String databaseName) throws Exception {

		Map<Integer, String> compartments = new HashMap<>();

		List<CompartmentContainer> res = getCompartmentsInfo(databaseName);

		for(CompartmentContainer container : res)
			compartments.put(container.getCompartmentID(), container.getAbbreviation());

		return compartments;
	}
	
	public static Map<String, Integer> getAbbreviationAndCompartmentId(String databaseName) throws Exception {

		Map<String, Integer> compartments = new HashMap<>();

		List<CompartmentContainer> res = getCompartmentsInfo(databaseName);

		for(CompartmentContainer container : res)
			compartments.put(container.getAbbreviation(), container.getCompartmentID());

		return compartments;
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static List<CompartmentContainer> getCompartmentsInfo(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getCompartmentsInfo();
	}

	/**
	 * @param databaseName
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static CompartmentContainer getCompartmentByName(String databaseName, String name) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).findCompartmentByName(name);
	}
	
	/**
	 * @param databaseName
	 * @param abbreviation
	 * @return
	 * @throws Exception
	 */
	public static CompartmentContainer getCompartmentByAbbreviation(String databaseName, String abbreviation) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).findCompartmentByAbbreviation(abbreviation);
	}
	
	/**
	 * @param databaseName
	 * @param name
	 * @param abb
	 * @return
	 * @throws Exception
	 */
	public static Integer getCompartmentIdByNameAndAbbreviation(String databaseName, String name, String abb) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getCompartmentIdByNameAndAbbreviation(name, abb);
	}

	/**
	 * @param databaseName
	 * @param compartments
	 * @throws Exception
	 */
	public static void  initCompartments(String databaseName, Map<String, String> compartments) throws Exception {

		for(String abbreviation : compartments.keySet()) {
			String name = compartments.get(abbreviation);
			String abb = abbreviation.toUpperCase();
			Map<Integer, String> compartmentIdAndName = InitDataAccess.getInstance().getDatabaseService(databaseName).getModelCompartmentIdAndName();
			if(compartmentIdAndName!=null && compartmentIdAndName.isEmpty()) {
				InitDataAccess.getInstance().getDatabaseService(databaseName).insertNameAndAbbreviation(name, abb);
			}
		}
	}

	public static List<Object[]> getReactantsOrProductsInCompartment(String databaseName, Boolean isCompartimentalized, Boolean Reactants, Boolean Products) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getReactantsOrProductsInCompartment(isCompartimentalized, Reactants, Products);

	}

	public static int getCompartmentID(String databaseName, String localisation) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getCompartmentID(localisation);

	}

	public static ArrayList<String> getCompartments(String databaseName, Boolean isMetabolites, Boolean isCompartimentalized) throws Exception {

		ArrayList<String> cls = new ArrayList<String>();
		Set<String> compartmentNames = InitDataAccess.getInstance().getDatabaseService(databaseName).getCompartments();

		for(String name: compartmentNames) {
			Boolean addCompartment = true;
			if(isCompartimentalized && (name.contains("inside") || name.contains("outside")))
				addCompartment = false;
			if(isMetabolites && name.contains("membrane"))
				addCompartment = false;
			if(addCompartment)
				cls.add(name);

		}
		return cls;
	}


	public static ArrayList<String[]> getCompartmentDataByName(String databaseName, String name) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getCompartmentDataByName(name);

	}

	public static Map<String, Set<Integer>> getCompartIdAndEcNumbAndProtId(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getCompartIdAndEcNumbAndProtId();

	}

	/**
	 * Method for retrieving the compartments database identifiers.
	 * 
	 * @param databaseName
	 * @param primaryCompartment
	 * @param primaryCompartmentAbb
	 * @param secondaryCompartmens
	 * @param secondaryCompartmensAbb
	 * @param compartmentsDatabaseIDs
	 * @return
	 * @throws Exception
	 */
	public static Map<String,Integer> getCompartmentsDatabaseIDs(String databaseName, List<CompartmentContainer> compartments) throws Exception {

		Map<String,Integer> compartmentsDatabaseIDs = new HashMap<>();

		for(CompartmentContainer compartment:compartments) {

			String name = compartment.getName();
			String abb = compartment.getAbbreviation();

			abb = abb.toUpperCase();

				CompartmentContainer container = getCompartmentByName(databaseName, name);

				if(container == null) {
					
					Integer id = insertNameAndAbbreviation(databaseName, name, abb);
					compartmentsDatabaseIDs.put(name, id);
				}
				else {
					
					compartmentsDatabaseIDs.put(name, container.getCompartmentID());
				}
		}
		return compartmentsDatabaseIDs;
	} 
	
	
	public static Map<String,Integer> getCompartmentsDatabaseIDs(String databaseName, Map<String, String> annotationCompartments) throws Exception {

		Map<String,Integer> compartmentsDatabaseIDs = new HashMap<>();
		
		for(String name : annotationCompartments.keySet()) {

			String abb = annotationCompartments.get(name);

			abb = abb.toUpperCase();

				CompartmentContainer container = getCompartmentByName(databaseName, name);

				if(container == null) {
					
					Integer id = insertNameAndAbbreviation(databaseName, name, abb);
					compartmentsDatabaseIDs.put(name, id);
				}
				else {
					
					compartmentsDatabaseIDs.put(name, container.getCompartmentID());
				}
		}
		return compartmentsDatabaseIDs;
	} 

	/**
	 * @param databaseName
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static CompartmentContainer getCompartmentById(String databaseName, Integer id) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).findCompartmentById(id);
	}
	
	/**
	 * @param databaseName
	 * @param compartmentId
	 * @throws Exception
	 */
	public static void deleteCompartmentById(String databaseName, Integer compartmentId) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).deleteCompartment(compartmentId);
	}
	
	/**
	 * @param databaseName
	 * @param compartmentIdToKeep
	 * @param compartmentIdToReplace
	 * @throws Exception
	 */
	public static void mergeCompartments(String databaseName, Integer compartmentIdToKeep, Integer compartmentIdToReplace) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).replaceReactionCompartment(compartmentIdToKeep, compartmentIdToReplace);
		InitDataAccess.getInstance().getDatabaseService(databaseName).replaceStoichiometryCompartment(compartmentIdToKeep, compartmentIdToReplace);
		InitDataAccess.getInstance().getDatabaseService(databaseName).replaceCompartment(compartmentIdToKeep, compartmentIdToReplace);
	}
	/**
	 * @param databaseName
	 * @throws Exception
	 */
	public static void removeCompartmentsNotInUse(String databaseName) throws Exception {
		
		InitDataAccess.getInstance().getDatabaseService(databaseName).removeCompartmentsNotInUse();
		
		
	}
}
