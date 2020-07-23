package pt.uminho.ceb.biosystems.merlin.services.model.loaders;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.Workspace;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelEnzymesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelReactionsServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelStoichiometryServices;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

/**
 * @author Oscar Dias
 *
 */
public class ModelDatabaseLoadingServices {


	/**
	 * Method for loading the genome annotation.
	 * 
	 * @param locusTag
	 * @param sequence_id
	 * @param geneName
	 * @param direction
	 * @param left_end
	 * @param right_end
	 * @param ecNumbers
	 * @param proteinName
	 * @param statement
	 * @param integratePartial
	 * @param integrateFull
	 * @param insertProductNames
	 * @param project
	 * @param informationType
	 * @return
	 * @throws Exception
	 */
	public static Map<String, List<Integer>> loadGeneAnnotation(String databaseName, String locusTag, String  sequence_id, String geneName, String direction, String left_end, String right_end, Set<String> ecNumbers,
			String proteinName,	boolean integratePartial, boolean integrateFull, boolean insertProductNames, Workspace project, SourceType informationType) throws Exception {

		Map<String, List<Integer>> enzymesReactions = null;

		int idGene = ModelGenesServices.loadGene(databaseName, locusTag, sequence_id, geneName, direction, left_end, right_end, informationType);

		if (! ecNumbers.isEmpty())			
			enzymesReactions = ModelEnzymesServices.loadEnzymeGetReactions(databaseName, idGene, ecNumbers, proteinName, integratePartial, integrateFull, insertProductNames, ProjectServices.isCompartmentalisedModel(databaseName));

		return enzymesReactions;
	}


	/**
	 *  * Method for loading not original reactions into database.
	 * 
	 * @param databaseName
	 * @param reaction
	 * @param proteinId
	 * @param isTransport
	 * @return
	 * @throws Exception
	 */
	public static Integer loadReaction(String databaseName, ReactionContainer reaction, Integer proteinId, boolean isTransport) throws Exception {

		if(!reaction.isInModel() && !isTransport)
			reaction.setSource(SourceType.KEGG);
		
		return ModelReactionsServices.loadReaction(databaseName, reaction, proteinId);

	}

	/**
	 * Method for loading the genome annotation.
	 * Integrates all ecnumbers and protein names
	 * 
	 * @param locusTag
	 * @param sequence_id
	 * @param geneName
	 * @param direction
	 * @param left_end
	 * @param right_end
	 * @param ecNumber
	 * @param proteinName
	 * @param statement
	 * @param project
	 * @param informationType
	 * @return
	 * @throws Exception
	 */
	public static Map<String, List<Integer>> loadGeneAnnotation(String databaseName, String locusTag, String sequence_id, String geneName, String direction, String left_end, String right_end,
			Set<String> ecNumber, String proteinName, Workspace project, SourceType informationType) throws Exception {

		return ModelDatabaseLoadingServices.loadGeneAnnotation(databaseName, locusTag, sequence_id, geneName, direction, left_end, right_end, ecNumber, proteinName, true, true, true, project, informationType);
	}

	/**
	 * Load Enzyme Information
	 * Returns reactions associated to the given enzymes in database.
	 * 
	 * Integrates all ecnumbers and protein names
	 * 
	 * @param idGene
	 * @param ecNumber
	 * @param proteinName
	 * @param statement
	 * @return
	 * @throws Exception 
	 */
	public static  Map<String, List<Integer>> loadEnzymeGetReactions(String databaseName, int idGene, Set<String> ecNumber, String proteinName) throws Exception {

		return ModelEnzymesServices.loadEnzymeGetReactions(databaseName, idGene, ecNumber, proteinName, true, true, true, ProjectServices.isCompartmentalisedModel(databaseName));
	}

	/**
	 * Method for loading gene information retrieved from homology data for a given sequence_id.
	 * 
	 * @param sequence_id
	 * @param statement
	 * @param informationType
	 * @return
	 * @throws SQLException
	 */
	public static int loadGeneLocusFromHomologyData (String databaseName, String sequence_id, SourceType informationType) throws Exception {

		return ModelGenesServices.loadGeneLocusFromGeneHomology(databaseName, sequence_id, informationType);
	}



	/**
	 * Load Gene Information
	 * Returns gene id in database.
	 * 
	 * @param locusTag
	 * @param sequence_id
	 * @param geneName
	 * @param statement
	 * @param informationType
	 * @return
	 * @throws SQLException
	 */
	public static int loadGene(String databaseName, String locusTag, String sequence_id, String geneName, String direction, String left_end, String right_end, SourceType informationType) throws Exception {

		return ModelGenesServices.loadGene(databaseName, locusTag, sequence_id, geneName, direction, left_end, right_end, informationType);
	}

	/**
	 * Method for loading the genome annotation.
	 * 
	 * 
	 * @param locusTag
	 * @param sequence_id
	 * @param ecNumber
	 * @param statement
	 * @param project 
	 * @param informationType 
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, List<Integer>> loadGeneAnnotation(String databaseName, String locusTag, String sequence_id, Set<String> ecNumber, Statement statement, Workspace project, SourceType informationType) throws Exception {

		String proteinName = null;
		String geneName = null;
		String direction = null;
		String left_end = null;
		String right_end = null;
		return ModelDatabaseLoadingServices.loadGeneAnnotation(databaseName, locusTag, sequence_id, geneName, direction, left_end, right_end, ecNumber, proteinName, project, informationType);
	}

	/**
	 * Method for retrieving reaction containers associated to reactions.
	 * 
	 * @param statement
	 * @return
	 * @throws Exception 
	 */
	public static Map<Integer, ReactionContainer> getEnzymesReactionsMap(String databaseName, boolean isTransporters) throws Exception {

		Map<Integer, ReactionContainer> reactionsMap = new HashMap<>();
		
		Map<Integer, Map<String, Object>> reactionsMap2 = new HashMap<>();
	
		List<Object[]> rs = ModelReactionsServices.getAllModelReactionsByTransportersAndIsCompartimentalized(databaseName, isTransporters);
		
		for (Object[] reaction : rs) {

			String name = reaction[0].toString();
			String equation = reaction[1].toString();
			boolean inModel = (boolean) reaction[2];
			boolean isGeneric = (boolean) reaction[3];
			boolean isSpontaneous = (boolean) reaction[4];
			boolean isNonEnzymatic = (boolean) reaction[5];
			String source = reaction[6].toString();
			int id = (int) reaction[7];
			String lowerBound = reaction[8].toString(); 
			String upperBound = reaction[9].toString();
			String notes ="";
			if(reaction[10]!= null)
				notes = reaction[10].toString();

			Map<String, Object> subMap = new HashMap<>();

			subMap.put("name", name);

			subMap.put("equation", equation);

			subMap.put("inModel", inModel);

			subMap.put("isGeneric", isGeneric);

			subMap.put("isSpontaneous", isSpontaneous);

			subMap.put("isNonEnzymatic", isNonEnzymatic);

			subMap.put("source", source);

			subMap.put("id", id);

			subMap.put("lowerBound", lowerBound);

			subMap.put("upperBound", upperBound);

			subMap.put("notes", notes);

			reactionsMap2.put(id, subMap);
		}


		List<String[]> reactionContainer = ModelReactionsServices.getReactionIdAndEcNumberAndProteinId(databaseName);
		
		for(String[] item : reactionContainer) {

			List<Pair<String, String>> proteinsPairs = new ArrayList<>();

			if(reactionsMap2.containsKey(item[0])) { //reactionId

				if(reactionsMap2.get(item[0]).containsKey("proteins"))			
					proteinsPairs =  (List<Pair<String, String>>) reactionsMap2.get(item[0]).get("proteins");

				proteinsPairs.add(new  Pair<>(item[1], item[2]));
				reactionsMap2.get(item[0]).put("proteins", proteinsPairs);
			}

		}

		List<Integer[]> reacIdAndPathId = ModelReactionsServices.getReactionIdAndPathwayId(databaseName);
		
		for(Integer[] item : reacIdAndPathId){

			List<String> pathways = new ArrayList<>();

			if(reactionsMap2.containsKey(item[0])) {

				if(reactionsMap2.get(item[0]).containsKey("pathways"))
					pathways = (List<String>) reactionsMap2.get(item[0]).get("pathways");

				pathways.add(item[1].toString());

				reactionsMap2.get(item[0]).put("pathways",pathways);
			}
		}

		List<Integer[]> stoichiometricInfo = ModelStoichiometryServices.getStoichiometryDataFromTransportersSource(databaseName);
		
		for(Integer[] item : stoichiometricInfo) {
			
			List<String[]> entry = new ArrayList<>();
			
			if(reactionsMap2.containsKey(item[1])) {

				if(reactionsMap2.get(item[1]).containsKey("entry"))
					entry = (List<String[]>) reactionsMap2.get(item[1]).get("entry");

				String[] ent = new String[3];
				ent[0] = item[2].toString(); // compoundId
				ent[1] = item[4].toString(); // stoichiometricCoefficient 
				ent[2] = item[3].toString(); // compartmentId
				entry.add(ent);

				reactionsMap2.get(item[1]).put("metabolites",entry);
			}
		}
		

		for(int id : reactionsMap2.keySet()) {

			ReactionContainer reactionContainer1 = ModelReactionsServices.getReaction(databaseName, id);
			reactionsMap.put(id, reactionContainer1);
		}
		
		return reactionsMap;

	}

}
