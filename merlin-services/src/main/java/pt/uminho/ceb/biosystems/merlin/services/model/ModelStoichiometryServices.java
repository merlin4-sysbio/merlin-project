package pt.uminho.ceb.biosystems.merlin.services.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;

public class ModelStoichiometryServices {
	
	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static List<Integer[]> getStoichiometryDataFromTransportersSource(String databaseName)  throws Exception {
		 
		return InitDataAccess.getInstance().getDatabaseService(databaseName).getStoichiometryDataFromTransportersSource();
	 
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer, ReactionContainer> getAllOriginalTransportersFromStoichiometry(String databaseName)  throws Exception {
		 
		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllOriginalTransportersFromStoichiometry();
	 
	}
	/**
	 * @param databaseName
	 * @param reactionId
	 * @param compoundId
	 * @param compartmentId
	 * @param stoichiometric_coef
	 * @throws IOException
	 * @throws Exception
	 */
	public static void insertStoichiometry(String databaseName, Integer reactionId, Integer compoundId, Integer compartmentId,
			double stoichiometric_coef) throws IOException, Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelStoichiometry(reactionId, compoundId, compartmentId,
				stoichiometric_coef);

	}
	
	/**
	 * @param databaseName
	 * @param idReaction
	 * @param metaboliteId
	 * @param idCompartment
	 * @param coefficient
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static Integer getStoichiometryID(String databaseName, int idReaction, String metaboliteId, int idCompartment, double coefficient) throws IOException, Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getStoichiometryID(idReaction, Integer.valueOf(metaboliteId.replace("-", "")), idCompartment, coefficient);

	}
	
	/**
	 * @param databaseName
	 * @param reactionId
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer, MetaboliteContainer> getStoichiometryDataForReaction(String databaseName, int reactionId)  throws Exception {
		
		return InitDataAccess.getInstance().getDatabaseService(databaseName).getModelStoichiometryByReactionId(reactionId);
	}
	
	/**
	 * @param databaseName
	 * @param reactionID
	 * @throws xception
	 */
	public static void swapReactantsAndProducts(String databaseName, Integer reactionID) throws Exception {

		Map<Integer, MetaboliteContainer> data = getStoichiometryDataForReaction(databaseName, reactionID);
		
		Map<String, Set<String>> stoichiometryMetabolite = new HashMap<>();

		for(Integer stoichId : data.keySet()) {

			Set<String> set = new HashSet<>();
			if(stoichiometryMetabolite.containsKey(data.get(stoichId).getStoichiometric_coefficient().toString()))
				set = stoichiometryMetabolite.get(data.get(stoichId).getStoichiometric_coefficient().toString());

			set.add(data.get(stoichId).getMetaboliteID()+"");

			stoichiometryMetabolite.put(data.get(stoichId).getStoichiometric_coefficient().toString(),set);

		}

		for (String key : stoichiometryMetabolite.keySet()) {

			String stoichiometry = key;

			if(stoichiometry.startsWith("-"))
				stoichiometry = stoichiometry.replace("-", "");
			else
				stoichiometry = "-".concat(stoichiometry);

			for (String metabliteID : stoichiometryMetabolite.get(key))
				updateModelStoichiometryByReactionIdAndCompoundId(databaseName, Double.valueOf(stoichiometry), reactionID, Integer.valueOf(metabliteID));
		}
	}
	
	/**
	 * @param databaseName
	 * @param id
	 * @param coeff
	 * @param compartmentId
	 * @param compoundId
	 * @throws Exception
	 */
	public static void updateModelStoichiometryByStoichiometryId(String databaseName, Integer id, double coeff, Integer compartmentId, Integer compoundId) throws Exception {
		
		InitDataAccess.getInstance().getDatabaseService(databaseName).updateModelStoichiometryByStoichiometryId(id, coeff, compartmentId, compoundId);

	}
	
	/**
	 * @param databaseName
	 * @param coeff
	 * @param compartmentId
	 * @param compoundId
	 * @throws Exception
	 */
	public static void updateModelStoichiometryByReactionIdAndCompoundId(String databaseName, double coeff, Integer compartmentId, Integer compoundId) throws Exception {
		
		InitDataAccess.getInstance().getDatabaseService(databaseName).updateModelStoichiometryByReactionIdAndCompoundId(coeff, compartmentId, compoundId);

	}
	
	/**
	 * @param databaseName
	 * @param isCompartmentalisedModel
	 * @return 
	 * @throws Exception
	 */
	public static Set<String> checkUndefinedStoichiometry(String databaseName, boolean isCompartmentalisedModel) throws Exception {
		
		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkUndefinedStoichiometry(isCompartmentalisedModel);

	}
}
