
package pt.uminho.ceb.biosystems.merlin.services.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import pt.uminho.ceb.biosystems.merlin.core.containers.gpr.ProteinsGPR_CI;
import pt.uminho.ceb.biosystems.merlin.core.containers.gpr.ReactionsGPR_CI;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ModelContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwayContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.auxiliary.ModelBlockedReactions;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.auxiliary.ModelReactionsmetabolitesEnzymesSets;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.view.ModelDesnormalizedReactionPathwayAndCompartment;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.Compartments;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.Pathways;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

/**
 * @author ODias
 *
 */
public class ModelReactionsServices {

	/**
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static List<Integer> getStats(String databaseName) throws Exception {

		int num=0;
		int noName=0;
		int noEquation=0;
		int reversible=0;
		int irreversible=0;

		List<Integer> res = new ArrayList<>();

		boolean isCompartimentalized = ProjectServices.isCompartmentalisedModel(databaseName);

		List<ReactionContainer> data = InitDataAccess.getInstance().getDatabaseService(databaseName).getReactionDataForStats(isCompartimentalized);

		for(int i=0; i<data.size(); i++){
			ReactionContainer reaction = data.get(i);

			num++;
			if(reaction.getExternalIdentifier()==null || reaction.getExternalIdentifier().trim().equals(""))
				noName++;

			if(reaction.getEquation()==null || reaction.getEquation().trim().equals(""))
				noEquation++;

			if(reaction.isReversible())
				reversible++;
			else
				irreversible++;
		}

		int value = 0;

		value = (int) InitDataAccess.getInstance().getDatabaseService(databaseName).countReactionsInModel(isCompartimentalized);
		res.add(value);

		value = (int) InitDataAccess.getInstance().getDatabaseService(databaseName).countReactionsByInModelAndSource(isCompartimentalized, true, SourceType.KEGG.toString());
		res.add(value);

		value = (int) InitDataAccess.getInstance().getDatabaseService(databaseName).countReactionsByInModelAndSource(isCompartimentalized, true, SourceType.HOMOLOGY.toString());
		res.add(value);

		value = (int) InitDataAccess.getInstance().getDatabaseService(databaseName).countReactionsByInModelAndSource(isCompartimentalized, true, SourceType.TRANSPORTERS.toString());
		res.add(value);

		value = (int) InitDataAccess.getInstance().getDatabaseService(databaseName).countReactionsByInModelAndSource(isCompartimentalized, true, SourceType.MANUAL.toString());
		res.add(value);

		res.add(0);

		res.add(num);

		res.add(reversible);

		res.add(irreversible);

		value = (int) InitDataAccess.getInstance().getDatabaseService(databaseName).countReactionsByInModelAndSource(isCompartimentalized, false, SourceType.KEGG.toString());
		res.add(value);

		value = (int) InitDataAccess.getInstance().getDatabaseService(databaseName).countReactionsByInModelAndSource(isCompartimentalized, false, SourceType.TRANSPORTERS.toString());
		res.add(value);

		res.add(noName);

		res.add(noEquation);

		value = ModelPathwaysServices.countPathwayHasReaction(databaseName, isCompartimentalized).intValue();
		res.add(num-value);

		Pair<Double, Double> result = InitDataAccess.getInstance().getDatabaseService(databaseName).getReactantsAndProducts(isCompartimentalized);

		Double dValue = result.getA()/Double.valueOf(num);

		res.add(dValue.intValue());

		dValue = result.getB()/(Double.valueOf(num));
		res.add(dValue.intValue());

		return res;
	}

	public static Integer loadReaction(String databaseName, ReactionContainer container, Integer proteinId) throws Exception {

		Integer newReactionID = null;

		if(container != null) {

			List<ReactionContainer> reacs = getModelReactionIdsRelatedToName(databaseName, container.getExternalIdentifier());

			for(ReactionContainer reaction : reacs) {

				if(reaction.getLocalisation() != null) {
					int compartmentId = reaction.getLocalisation().getCompartmentID();

					if(container.getLocalisation().getCompartmentID() == compartmentId) 
						newReactionID = reaction.getReactionID();
				}
			}

			if(newReactionID == null) {

				newReactionID = insertNewReaction(databaseName, container);

				ModelMetabolitesServices.addCompounds(databaseName, container.getReactantsStoichiometry(), true, newReactionID);
				ModelMetabolitesServices.addCompounds(databaseName, container.getProductsStoichiometry(), false, newReactionID);
			}

			List<ProteinContainer> proteins = container.getEnzymes();
			List<PathwayContainer> pathways = container.getPathways();


			if (proteinId==null) {
				for(ProteinContainer protein : proteins)
					addReaction_has_Enzyme(databaseName, protein.getIdProtein(), newReactionID);	
			}
			else{
				addReaction_has_Enzyme(databaseName, proteinId, newReactionID);	
			}

			if(pathways!=null && !pathways.isEmpty()){
				for(PathwayContainer pathway : pathways)
					InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelPathwayHasReaction(newReactionID, pathway.getIdPathway());
			}

			return newReactionID;
		}
		return newReactionID;

	}

	/**
	 * @param encodedOnly
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static ModelContainer getMainTableData(boolean encodedOnly, String databaseName) throws Exception {

		boolean isCompartamentalized = ProjectServices.isCompartmentalisedModel(databaseName);
		List<ModelDesnormalizedReactionPathwayAndCompartment> view = InitDataAccess.getInstance().getDatabaseService(databaseName).getAllReactionsView(isCompartamentalized, encodedOnly);

		//		
		//		for(ModelDesnormalizedReactionPathwayAndCompartment model : view)
		//			System.out.println(model.getPathwayName() + "\t" + model.getIdreaction() + "\t" + model.getReactionEquation() + "\t" + model);

		Map<Integer, Integer> identifiers = new HashMap<>(); 
		Map<Integer,String> namesIndex = new HashMap<>();
		Set<String> activeReactions = new HashSet<>();
		Map<Integer,String> formulasIndex = new HashMap<>();
		List<String> pathwaysList = new ArrayList<>();
		List<Integer> reactionsOrder = new ArrayList<>();
		Map <String, Integer> pathID = new TreeMap<>();
		Set<String> pathwaysSet=new TreeSet<>();

		Map<Integer, Pair<Integer, List<Object>>> reactionsData = new HashMap<>();

		Map<Integer, String> compartments = ModelCompartmentServices.getModelCompartmentIdAndName(databaseName);

		int r=0;

		for(ModelDesnormalizedReactionPathwayAndCompartment entry : view) {
			activeReactions.add(entry.getReactionName());
			identifiers.put(r, entry.getIdreaction());

			ArrayList<Object> ql = new ArrayList<Object>();
			ql.add("");

			if(entry.getPathwayName()!=null)
				ql.add(entry.getPathwayName());
			else
				ql.add("");

			ql.add(entry.getReactionName());
			ql.add(entry.getReactionEquation());
			if(isCompartamentalized) {
				if(entry.getCompartmentId() != null)
					ql.add(compartments.get(entry.getCompartmentId()));
				else
					ql.add("");
			}
			else
				ql.add(entry.getReactionSource());
			ql.add(entry.getReactionNotes());
			ql.add(entry.isReversible());
			ql.add(entry.getReactionInModel());

			int pathway = 0;
			if(entry.getIdpathway()!=null && entry.getIdpathway()>0)
				pathway = entry.getIdpathway();

			Pair<Integer, List<Object>> pair = new Pair<>(pathway, ql);
			reactionsData.put(r, pair);

			reactionsOrder.add(r, entry.getIdreaction());

			namesIndex.put(entry.getIdreaction(), entry.getReactionName());
			formulasIndex.put(entry.getIdreaction(), entry.getReactionEquation());

			if(entry.getPathwayName() != null) {

				pathID.put(entry.getPathwayName(), entry.getIdpathway());
				pathwaysSet.add(entry.getPathwayName());
			}

			r++;
		}

		return new ModelContainer(identifiers, namesIndex, activeReactions, formulasIndex, pathwaysList,
				pathID, pathwaysSet, reactionsData, reactionsOrder);


	}

	/**
	 * @param idReaction
	 * @param pathway
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static String[] getEnzymes(int idReaction, int pathway, String databaseName) throws Exception {

		if(pathway < 0) {
			String[] res = InitDataAccess.getInstance().getDatabaseService(databaseName).getEnzymesByReaction2(idReaction);

			return res;
		}
		String[] res = InitDataAccess.getInstance().getDatabaseService(databaseName).getEnzymesByReactionAndPathway2(idReaction, pathway);

		return res;
	}

	/**
	 * Retrieves reactions information from reaction table
	 * @param conditions
	 * @return List<ReactionContainer>
	 * @throws Exception 
	 * @throws IOException 
	 */		
	public static List<ReactionContainer> getReactions(String databaseName, boolean isCompartimentalised) throws IOException, Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getDataForReactionContainer(isCompartimentalised);

	}


	/**
	 * @param databaseName
	 * @param source
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static Boolean checkReactionNotLikeSourceAndNotReversible(String databaseName,String source) throws IOException, Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkReactionNotLikeSourceAndNotReversible(source);

	}

	/**
	 * @param databaseName
	 * @param isCompartimentalised
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static Map<Integer, ReactionContainer> getReactionsByReactionId(String databaseName, boolean isCompartimentalised) throws IOException, Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllModelReactionAttributesByReactionId(isCompartimentalised);

	}



	public static Map<Integer, String> getLabelsByExternalIdentifiers(String databaseName, String name) throws IOException, Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getLabelsByExternalIdentifiers(name);

	}

	/**
	 * @param rowID
	 * @return
	 * @throws Exception 
	 */
	public static List<ProteinContainer> getEnzymesForReaction(String databaseName, int rowID) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getEnzymesForReaction(rowID);

	}

	/**

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getRowInfo(java.lang.String)
	 */
	public static Pair<Map<String,String>, List<List<List<String>>>> getRowInfo(int id, String name, String databaseName) throws Exception {

		List<List<List<String>>> results = new ArrayList<>();
		Map<String, String> metabolites = new HashMap<>();

		int counter = 0;

		List<String> resultsList = new ArrayList<String>();
		List<String[]> result = ModelReactionsServices.getCompoundDataFromStoichiometry(databaseName, id);
		List<List<String>> resultLists = new ArrayList<>();

		for(int i=0; i<result.size(); i++){

			String[] list = result.get(i);

			resultsList = new ArrayList<String>();

			if(list[0]==null || list[0].isEmpty())
				resultsList.add(list[4]);
			else
				resultsList.add(list[0]);

			resultsList.add(list[1]);
			resultsList.add(list[4]);
			resultsList.add(list[2]);
			resultsList.add(list[3]);
			metabolites.put(resultsList.get(2), resultsList.get(0));
			resultLists.add(resultsList);
		}
		results.add(counter++, resultLists);

		List<String[]> result2 = InitDataAccess.getInstance().getDatabaseService(databaseName).getEnzymesByReaction(id);
		resultLists = new ArrayList<>();
		for(int i=0; i<result2.size(); i++){
			String[] list = result2.get(i);

			resultsList = new ArrayList<String>();

			resultsList.add(list[0]);
			resultsList.add(list[2]);
			resultsList.add(list[3]);

			resultLists.add(resultsList);
		}

		results.add(counter++, resultLists);

		ReactionContainer reactionContainer = getDatabaseReactionContainer(databaseName, id); 

		List<List<String>> resultListsGeneric = new ArrayList<>();
		List<List<String>> resultListsSpontaneous = new ArrayList<>();
		List<List<String>> resultListsNonEnzymatic = new ArrayList<>();
		List<List<String>> upperBound = new ArrayList<>();
		List<List<String>> lowerBound = new ArrayList<>();
		resultLists = new ArrayList<>();
		//			for(int i=0; i<result.size(); i++){
		//				String[] list = result.get(i);

		resultsList = new ArrayList<String>();
		resultsList.add("Generic");
		resultsList.add(String.valueOf(reactionContainer.isGeneric()));
		resultListsGeneric.add(resultsList);
		resultLists.addAll(resultListsGeneric);

		resultsList = new ArrayList<String>();
		resultsList.add("Spontaneous");
		resultsList.add(String.valueOf(reactionContainer.isSpontaneous()));
		resultListsSpontaneous.add(resultsList);
		resultLists.addAll(resultListsSpontaneous);

		resultsList = new ArrayList<String>();
		resultsList.add("Non Enzymatic");
		resultsList.add(String.valueOf(reactionContainer.isNon_enzymatic()));
		resultListsNonEnzymatic.add(resultsList);
		resultLists.addAll(resultListsNonEnzymatic);

		String lb = reactionContainer.getLowerBound().toString();

		if((lb == null || lb.equalsIgnoreCase("null")) || lb.isEmpty()) {

			lb = "0";
			if(reactionContainer.isReversible())
				lb = "-999999";
		}

		String ub = reactionContainer.getUpperBound().toString();

		if(((ub == null || ub.equalsIgnoreCase("null")) || ub.isEmpty()))
			ub = "999999";

		resultsList = new ArrayList<String>();
		resultsList.add("Lower Bound");
		resultsList.add(lb);
		lowerBound.add(resultsList);
		resultLists.addAll(lowerBound);

		resultsList = new ArrayList<String>();
		resultsList.add("Upper Bound");
		resultsList.add(ub);
		upperBound.add(resultsList);
		resultLists.addAll(upperBound);
		//			}
		results.add(counter++, resultLists);

		int aliasIdentifier = id;

		//		boolean isCompartmentalisedModel = ProjectServices.isCompartmentalisedModel(databaseName);
		//
		//		if(isCompartmentalisedModel) {
		//			
		//			List<Integer> aux = ModelReactionsServices.getModelReactionLabelIdByName(databaseName, name, isCompartmentalisedModel);
		//
		//			if(aux != null && aux.size() > 0)
		//				id = aux.get(0);
		//		}

		List<String> aliases = InitDataAccess.getInstance().getDatabaseService(databaseName).getAliasClassR(aliasIdentifier);
		resultLists = new ArrayList<>();
		if(aliases.size() == 0)
			aliases.add("");
		
		for(String alias : aliases) {

			resultsList = new ArrayList<String>();
			resultsList.add(alias);
			resultLists.add(resultsList);
		}
		results.add(counter++, resultLists);

		String[] pathways = ModelPathwaysServices.getExistingPathwaysNamesByReactionId(databaseName, id);
		resultLists = new ArrayList<>();
		for(String pathway : pathways) {

			resultsList = new ArrayList<String>();
			resultsList.add(pathway);
			resultLists.add(resultsList);
		}
		results.add(counter++, resultLists);


		resultLists = new ArrayList<>();
		resultsList = new ArrayList<String>();
		resultsList.add(reactionContainer.getSource().toString());
		resultLists.add(resultsList);

		results.add(counter++, resultLists);

		String booleanRule = ModelReactionsServices.getReaction(databaseName, id).getGeneRule();

		List<List<Pair<String, String>>> geneRules = parseBooleanRule(databaseName, booleanRule);
		resultLists = new ArrayList<>();

		for(List<Pair<String, String>> geneRule : geneRules) {

			resultsList = new ArrayList<String>();

			String rule = null;
			for(Pair<String, String> gene : geneRule) {

				if(rule == null)
					rule = "";
				else
					rule = rule.concat(" AND ");

				rule = rule.concat(gene.getA());

				if(gene.getB() != null && !gene.getB().isEmpty())
					rule = rule.concat(" (").concat(gene.getB()).concat(")");

			}
			resultsList.add(rule);
			resultLists.add(resultsList);
		}

		if(resultLists.size()>0)
			results.add(counter++, resultLists);

		return new Pair<> (metabolites, results);
	}

	/**
	 * @param name
	 * @return
	 */
	public static List<Integer> getReactionID(String name, String databaseName) throws Exception { // use .getReactionsRelatedToLabelName(name) instead because we can find several reactions using only the name

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getReactionsRelatedToLabelName(name);
	}

	/**
	 * @param selectedRow
	 * 
	 * duplicate a given reaction
	 * @throws Exception 
	 */
	public static void duplicateReaction(int reactionID, String databaseName) throws Exception {

		Map<String, Set<String>> selectedEnzymesPathway = new TreeMap<String, Set<String>>();

		ReactionContainer container = ModelReactionsServices.getReaction(databaseName, reactionID);

		String name=container.getExternalIdentifier(),
				equation=container.getEquation(),
				boolean_rule = container.getGeneRule();
		double  lowerBound = container.getLowerBound(),
				upperBound = container.getUpperBound();
		boolean //reversibility = container.isReversible(),
		inModel = container.isInModel(),
		isSpontaneous = container.isSpontaneous(), 
		isNonEnzymatic = container.isNon_enzymatic(),
		isGeneric = container.isGeneric();

		SourceType source = container.getSource();
		Integer compartmentId = null;

		if(container.getLocalisation() != null)
			compartmentId = container.getLocalisation().getCompartmentID(); 

		Map<String, String> compartment=new TreeMap<>();
		Map<String, Double>	metabolites=new TreeMap<>();

		List<Object[]> list = GetCompoundIDAndStoichiometricCoefficientAndCompartmentIDByReactionID(databaseName, reactionID);

		for(Object[] subList : list) {

			if(subList[0].toString().startsWith("-")) {

				metabolites.put("-"+subList[1], Double.valueOf((Integer)subList[0]));
				compartment.put("-"+subList[1], subList[2]+"");
			}
			else {

				metabolites.put(subList[1]+"", Double.valueOf((Integer)subList[0]));
				compartment.put(subList[1]+"", subList[2]+"");
			}
		}

		String[] pathways = ModelPathwaysServices.getExistingPathwaysNamesByReactionId(databaseName, reactionID);

		for(String pathway: pathways) {

			Set<String> enzymesSet=new TreeSet<String>();
			int pathwayIdentifier = ModelPathwaysServices.getPathwayIDbyName(databaseName, pathway);
			String[] enzymes = ModelReactionsServices.getEnzymes(reactionID, pathwayIdentifier, databaseName);
			enzymesSet.addAll(new TreeSet<String>(Arrays.asList(enzymes)));
			selectedEnzymesPathway.put(pathway, enzymesSet);
		}

		List<ProteinContainer> enzymesForReaction = ModelReactionsServices.getEnzymesForReaction(databaseName,reactionID);

		Set<String> format = new HashSet<String>();

		for(ProteinContainer container1 : enzymesForReaction) {

			format.add(container1.getExternalIdentifier() + "___" + 
					container1.getIdProtein() + "___" + container1.getName());
		}

		if(selectedEnzymesPathway.isEmpty())
			selectedEnzymesPathway.put("-1allpathwaysinreaction", format);

		ModelReactionsServices.insertNewReaction(incrementName(databaseName, name), equation, compartment,
				metabolites, inModel, selectedEnzymesPathway, 
				compartmentId, isSpontaneous, isNonEnzymatic, isGeneric, lowerBound, upperBound, source, boolean_rule, databaseName);
	}

	/**
	 * @param databaseName
	 * @param names
	 * @param rules
	 * @param message
	 * @throws IOException
	 * @throws Exception
	 */
	public static void updateBooleanRuleAndNotes(String databaseName, List<String> names, Map<String, String> rules,
			String message) throws IOException, Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).updateBooleanRuleAndNotes(names, rules, message);
	}

	/**
	 * @param databaseName
	 * @param istransporter
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static boolean checkBiochemicalReactions(String databaseName, boolean istransporter) throws IOException, Exception {

		return  InitDataAccess.getInstance().getDatabaseService(databaseName).checkBiochemicalOrTransportReactions(istransporter);
	}

	/**
	 * Insert a new reaction on the database
	 * 
	 * @throws Exception 
	 */
	public static Integer insertNewReaction(String databaseName, ReactionContainer container) throws Exception {

		CompartmentContainer comp = container.getLocalisation();
		if(comp != null && comp.getCompartmentID() == 0 && comp.getName() != null)
			container.setLocalisation(ModelCompartmentServices.getCompartmentByName(databaseName, comp.getName()));

		return InitDataAccess.getInstance().getDatabaseService(databaseName).insertNewReaction(container);

	}


	/**
	 * Insert a new reaction on the database
	 * 
	 * @param name
	 * @param equation
	 * @param reversibility
	 * @param metabolitesChains
	 * @param metabolitesCompartments
	 * @param metabolitesStoichiometry
	 * @param inModel
	 * @param enzymesInPathway
	 * @param reactionCompartment
	 * @param isSpontaneous
	 * @param isNonEnzymatic
	 * @param isGeneric
	 * @param lowerBound
	 * @param upperBound
	 * @param source 
	 * @param boolean_rule 
	 * @throws Exception 
	 */
	public static void insertNewReaction(String name, String equation,
			Map<String, String > metabolitesCompartments, Map<String, Double> metabolitesStoichiometry, boolean inModel, Map<String, 
			Set<String>> enzymesInPathway, Integer compartmentId, boolean isSpontaneous, boolean isNonEnzymatic,
			boolean isGeneric, double lowerBound, double upperBound, SourceType source, String boolean_rule, String databaseName) throws Exception {


		//			boolean compartmentalisedModel = ProjectServices.isCompartmentalisedModel(databaseName);


		if(!name.startsWith("R") && !name.startsWith("T")&& !name.startsWith("K") && !name.toLowerCase().contains("biomass"))
			name = "R_"+name;

		if(name.toLowerCase().equals("biomass"))
			name = "R_"+name;

		boolean exists = InitDataAccess.getInstance().getDatabaseService(databaseName).checkIfReactioLabelNameAlreadyExists(name);

		if(exists) {

			throw new  Exception("Reaction with the same name ("+name+") already exists. Aborting operation! You can either update the reaction or change its name!");
		}
		else {

			Integer idNewReaction = InitDataAccess.getInstance().getDatabaseService(databaseName).
					insertNewReaction(inModel, lowerBound, upperBound, boolean_rule, equation, isGeneric, isNonEnzymatic, isSpontaneous, name, source.toString(), compartmentId, null);

			//PATHWAYS AND ENZYMES PROCESSING
			{
				Map<Integer,Set<String>> newPathwaysID = new TreeMap<Integer,Set<String>>();
				enzymesInPathway.remove("");
				{
					if(enzymesInPathway.containsKey("-1allpathwaysinreaction") && enzymesInPathway.get("-1allpathwaysinreaction").size()>0) {

						for(String enzyme : enzymesInPathway.get("-1allpathwaysinreaction")) {

							int idProtein = Integer.valueOf(enzyme.split("___")[2]);

							exists = checkReactionHasEnzymeData(databaseName, idProtein, idNewReaction);

							if(!exists)
								insertModelReactionHasModelProtein(databaseName, idNewReaction, idProtein);
						}
					}
					enzymesInPathway.remove("-1allpathwaysinreaction");
				}

				if(enzymesInPathway.size()>0) {

					for(String pathway:enzymesInPathway.keySet()) {

						int pathID = InitDataAccess.getInstance().getDatabaseService(databaseName).getPathwayID(pathway);

						Set<String> p = new TreeSet<String>();
						if (enzymesInPathway.get(pathway).size()>0)
							p =  new TreeSet<String>(enzymesInPathway.get(pathway));

						newPathwaysID.put(pathID, p);
					}

					//when pathways are deleted, they are just removed from the pathway has reaction association
					//insert the new pathways

					for(int pathway:newPathwaysID.keySet()) {

						exists = ModelPathwaysServices.checkPathwayHasReactionData(databaseName, idNewReaction, pathway);

						if(!exists) {

							InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelPathwayHasReaction(idNewReaction, pathway);

							for(String enzyme: newPathwaysID.get(pathway)) {

								int idProtein = Integer.valueOf(enzyme.split("___")[2]);

								exists = ModelPathwaysServices.checkPathwayHasModelProtein(databaseName, pathway, idProtein);

								if(!exists) {

									ModelPathwaysServices.insertModelPathwayHasModelProtein(databaseName, pathway, idProtein);
								}

								exists = checkReactionHasEnzymeData(databaseName, idProtein, idNewReaction);


								if(!exists) {
									insertModelReactionHasModelProtein(databaseName, idNewReaction, idProtein);

								}
							}
						}
					}
				}
			}

			Integer biomass_id = InitDataAccess.getInstance().getDatabaseService(databaseName).getMetaboliteIdByName("Biomass");

			for(String m :metabolitesStoichiometry.keySet()) {

				if(m!=null && !m.equalsIgnoreCase("null")){

					Integer idCompartment = InitDataAccess.getInstance().getDatabaseService(databaseName).getCompartmentID(metabolitesCompartments.get(m));

					InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelStoichiometry(idNewReaction, Integer.valueOf(m.replace("-", "")), idCompartment, (float) metabolitesStoichiometry.get(m).doubleValue());

					if(m.replace("-", "").equalsIgnoreCase(biomass_id+"")) {

						Integer idBiomassPath = InitDataAccess.getInstance().getDatabaseService(databaseName).getPathwayID("Biomass Pathway");

						if(idBiomassPath == null) {						
							idBiomassPath = InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelPathwayCodeAndName("B0001", "Biomass Pathway");
						}

						boolean existsBiomassPathway = InitDataAccess.getInstance().getDatabaseService(databaseName).checkPathwayHasReactionData(idNewReaction, idBiomassPath);

						if(!existsBiomassPathway) {
							InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelPathwayHasReaction(idNewReaction, idBiomassPath);
						}
					}
				}
			}
		}



	}

	/**
	 * @param selectedRow
	 * 
	 * remove a given reaction
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static void removeReactionByReactionId(String databaseName, int reaction_id) throws IOException, Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).removeSelectedReaction(reaction_id);

	}
	
	/**
	 * @param selectedRow
	 * 
	 * remove a given reaction
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static void removeReactionLabelByReactionLabelId(String databaseName, int reactionLabelId) throws IOException, Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).removeSelectedReactionLabel(reactionLabelId);

	}


	/**
	 * @param databaseName
	 * @param source
	 * @throws IOException
	 * @throws Exception
	 */
	public static void removeReactionsBySource(String databaseName, SourceType source) throws IOException, Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).removeReactionsBySource(source.toString());

	}

	/**
	 * @param databaseName
	 * @param source
	 * @throws IOException
	 * @throws Exception
	 */
	public static boolean checkReactionsBySource(String databaseName, SourceType source) throws IOException, Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkReactionsBySource(source.toString());

	}

	/**
	 * @param rowID
	 * @param name
	 * @param equation
	 * @param reversibility
	 * @param chains
	 * @param compartment
	 * @param metabolites
	 * @param inModel
	 * @param selectedEnzymesPathway
	 * @param localisation
	 * @param isSpontaneous
	 * @param isNonEnzymatic
	 * @param isGeneric
	 * @param lowerBound
	 * @param upperBound
	 * @param boolean_rule 
	 */
	public static void updateReaction(Integer idReaction, String name, String equation, Boolean reversibility, //Set<String> enzymes,
			Map<String, String > compartment, Map<String, Double> metabolites, Boolean inModel, 
			Map<String, Set<String>> selectedEnzymesPathway, Integer compartmentId, Boolean isSpontaneous, Boolean isNonEnzymatic,
			Boolean isGeneric, Long lowerBound, Long upperBound, String boolean_rule, String databaseName) throws Exception{

		if(equation.contains(" <= ")) {

			String [] equationArray = equation.split(" <= ");
			equation = equationArray[1]+" => "+equationArray[0];
		}

		if(!name.startsWith("R") && !name.startsWith("T") && !name.startsWith("K") && !name.toLowerCase().contains("biomass"))
			name = "R_"+name;

		//boolean inModelReaction= InitDataAccess.getInstance().getDatabaseService(databaseName).isReactionInModel(idReaction);


		InitDataAccess.getInstance().getDatabaseService(databaseName).updateReaction(name, equation, reversibility, compartmentId, isSpontaneous, isNonEnzymatic, isGeneric, boolean_rule, lowerBound, upperBound, idReaction, inModel);

		//PATHWAYS AND ENZYMES PROCESSING
		{
			List<Integer> existingPathwaysID = new ArrayList<Integer>();
			Map<Integer,Set<String>> newPathwaysID = new HashMap<Integer,Set<String>>();
			Map<Integer,Set<String>>  editedPathwaysID = new HashMap<Integer,Set<String>> ();
			selectedEnzymesPathway.remove("");


			List<PathwayContainer> existingPathways = ModelPathwaysServices.getPathwaysByReactionId(databaseName, idReaction);

			for(PathwayContainer container : existingPathways)
				existingPathwaysID.add(container.getIdPathway());

			// IF There are enzymes and no pathway! or add to all pathways
			List<String> existingEnzymesID = new ArrayList<String>();

			if(selectedEnzymesPathway!= null && selectedEnzymesPathway.get("-1allpathwaysinreaction")!= null && selectedEnzymesPathway.get("-1allpathwaysinreaction").size()>0) {

				existingEnzymesID =  new ArrayList<>(Arrays.asList(InitDataAccess.getInstance().getDatabaseService(databaseName).getEnzymesByReaction2(idReaction)));

				for(String enzyme: new ArrayList<String>(selectedEnzymesPathway.get("-1allpathwaysinreaction"))) {

					if(existingEnzymesID.contains(enzyme)) {

						existingEnzymesID.remove(enzyme);
						selectedEnzymesPathway.get("-1allpathwaysinreaction").remove(enzyme);
					}
				}

				for(String enzyme: selectedEnzymesPathway.get("-1allpathwaysinreaction")) {

					int idProtein = Integer.parseInt(enzyme.split("___")[2]);

					boolean exists = checkReactionHasEnzymeData(databaseName, idProtein, idReaction);

					if(!exists) {
						addReaction_has_Enzyme(databaseName, idProtein, idReaction);
					}
				}

				for(String enzyme:existingEnzymesID) {

					Integer idProtein = Integer.valueOf(enzyme.split("___")[2]);

					InitDataAccess.getInstance().getDatabaseService(databaseName).deleteModelReactionHasModelProteinByReactionId(idReaction, idProtein);
				}
			}
			else {

				existingEnzymesID =  Arrays.asList(InitDataAccess.getInstance().getDatabaseService(databaseName).getEnzymesByReaction2(idReaction));

				for(String enzyme:existingEnzymesID) {

					String idProtein = enzyme.split("___")[2];

					InitDataAccess.getInstance().getDatabaseService(databaseName).deleteModelReactionHasModelProteinByReactionId(Integer.valueOf(idReaction), Integer.valueOf(idProtein));
				}
			}

			Set<String> genericECNumbers = new TreeSet<String>();
			if(selectedEnzymesPathway.get("-1allpathwaysinreaction") != null) {

				genericECNumbers.addAll(selectedEnzymesPathway.get("-1allpathwaysinreaction"));
			}

			selectedEnzymesPathway.remove("-1allpathwaysinreaction");

			if(selectedEnzymesPathway.size()>0) {

				existingPathwaysID = new ArrayList<>();

				existingPathways = ModelPathwaysServices.getPathwaysByReactionId(databaseName, idReaction);

				for(PathwayContainer container : existingPathways)
					existingPathwaysID.add(container.getIdPathway());

				for(String pathway : selectedEnzymesPathway.keySet()) {

					int res = InitDataAccess.getInstance().getDatabaseService(databaseName).getPathwayID(pathway);

					Set<String> enzymes = new TreeSet<String>(selectedEnzymesPathway.get(pathway));
					if(enzymes.isEmpty()) {

						enzymes.addAll(genericECNumbers);
					}
					newPathwaysID.put(res, enzymes);
				}

				for(Integer pathway: new HashSet<>(existingPathwaysID)) {

					if(newPathwaysID.containsKey(pathway)) {

						editedPathwaysID.put(pathway, new TreeSet<String>(newPathwaysID.get(pathway)));
						newPathwaysID.remove(pathway);
						existingPathwaysID.remove(pathway);
					}
				}

				//when pathways are deleted, they are just removed from the pathway has reaction association
				for(int pathway:existingPathwaysID) {

					InitDataAccess.getInstance().getDatabaseService(databaseName).deleteModelPathwayHasReactionByReactionIdAndPathwayId(idReaction, pathway);
				}

				Map<Integer,Set<Integer>> pathsEnzymesIn = new TreeMap<Integer,Set<Integer>>();
				Map<Integer,Set<Integer>> pathsReactionsIn = new TreeMap<Integer,Set<Integer>>();
				//insert the new pathways

				for(int pathway : newPathwaysID.keySet()) {

					InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelPathwayHasReaction(idReaction, pathway);


					for(String enzyme: newPathwaysID.get(pathway)) {

						Set<Integer> existsEnzReaction=new TreeSet<Integer>();
						Set<Integer> existsEnzPathway=new TreeSet<Integer>();

						Integer idProtein = Integer.parseInt(enzyme.split("___")[2]);

						boolean exists = ModelPathwaysServices.checkPathwayHasModelProtein(databaseName, pathway, idProtein);

						if(!exists) {
							ModelPathwaysServices.insertModelPathwayHasModelProtein(databaseName, pathway, idProtein);


							existsEnzPathway.add(idProtein);
							pathsEnzymesIn.put(pathway, existsEnzPathway);
						}

						exists = checkReactionHasEnzymeData(databaseName, idProtein, idReaction);
						if(!exists) {
							insertModelReactionHasModelProtein(databaseName, idReaction, idProtein);

							existsEnzReaction.add(idProtein);
							pathsReactionsIn.put(pathway, existsEnzReaction);
						}
					}
				}
				// edited pathways
				for(Integer pathway:editedPathwaysID.keySet()) {

					String[] enzymes = InitDataAccess.getInstance().getDatabaseService(databaseName).getEnzymesByReactionAndPathway2(idReaction, pathway);

					existingEnzymesID = new ArrayList<String>(Arrays.asList(enzymes));
					editedPathwaysID.get(pathway).remove("");

					for(String ecnumber: new TreeSet<String>(editedPathwaysID.get(pathway))) {

						for(String existingEcnumber :new TreeSet<String>(existingEnzymesID)) {

							if(existingEcnumber.equals(ecnumber)) {

								editedPathwaysID.get(pathway).remove(ecnumber);
								existingEnzymesID.remove(existingEcnumber);
							}
						}
					}

					for(String enzyme: new TreeSet<String>(editedPathwaysID.get(pathway))) {

						Set<Integer> existsEnzReaction=new TreeSet<Integer>();
						Set<Integer> existsEnzPathway=new TreeSet<Integer>();

						int idProtein = Integer.parseInt(enzyme.split("___")[2]);

						boolean exists = ModelPathwaysServices.checkPathwayHasModelProtein(databaseName, pathway, idProtein);

						if(!exists) {
							ModelPathwaysServices.insertModelPathwayHasModelProtein(databaseName, pathway, idProtein);

							existsEnzPathway.add(idProtein);
							pathsEnzymesIn.put(pathway, existsEnzPathway);
						}

						exists = checkReactionHasEnzymeData(databaseName, idProtein, idReaction);

						if(!exists) {
							insertModelReactionHasModelProtein(databaseName, idReaction, idProtein);

							existsEnzReaction.add(idProtein);
							pathsReactionsIn.put(pathway, existsEnzReaction);
						}
						//}

					}

					for(String enzyme: new TreeSet<String>(existingEnzymesID)) {

						Integer idProtein = Integer.parseInt(enzyme.split("___")[2]);

						List<Integer> reactionsID = InitDataAccess.getInstance().getDatabaseService(databaseName).getModelPathwayHasEnzymeReactionIdByEcNumberAndPathwayIdAndProteinId(pathway, idProtein);

						//reactionsID.remove(idReaction);

						if(reactionsID.size()==0) {

							InitDataAccess.getInstance().getDatabaseService(databaseName).deleteModelPathwayHasModelProteinByPathwayId(Integer.valueOf(enzyme));
						}

						List<Integer> pathwayID = InitDataAccess.getInstance().getDatabaseService(databaseName).getModelPathwayHasEnzymeReactionIdByEcNumberAndReactionIdAndProteinId(idReaction, idProtein);
						pathwayID.remove(pathway);


						if(reactionsID.size()==0) {

							InitDataAccess.getInstance().getDatabaseService(databaseName).deleteModelReactionHasModelProteinByReactionId(idReaction, idProtein);

						}
					}
				}
			}
			else {

				//when pathways are deleted, they are just removed from the pathway has reaction association
				for(int pathway:existingPathwaysID) {
					InitDataAccess.getInstance().getDatabaseService(databaseName).deleteModelPathwayHasReactionByReactionIdAndPathwayId(idReaction, pathway);
				}
			}
		}

		Map<String,Pair<String,Pair<Double,String>>> existingMetabolitesID = InitDataAccess.getInstance().getDatabaseService(databaseName).getExistingMetabolitesID(idReaction);

		for(String m: new ArrayList<String>(metabolites.keySet())) {

			if(existingMetabolitesID.keySet().contains(m) && existingMetabolitesID.get(m).getB().getA()==(metabolites.get(m))) {

				if(existingMetabolitesID.get(m).getB().getB().equalsIgnoreCase(compartment.get(m))) {

					existingMetabolitesID.remove(m);
					metabolites.remove(m);
				}
			}
		}

		for(String compound : existingMetabolitesID.keySet()) {
			InitDataAccess.getInstance().getDatabaseService(databaseName).deleteModelStoichiometryByStoichiometryId(Integer.valueOf(existingMetabolitesID.get(compound).getA()));
		}

		for(String m :metabolites.keySet()) {

			int idCompartment = InitDataAccess.getInstance().getDatabaseService(databaseName).getCompartmentID(compartment.get(m)); 

			Integer idstoichiometry = ModelStoichiometryServices.getStoichiometryID(databaseName, idReaction, m, idCompartment, metabolites.get(m));

			if(idstoichiometry != null) {
				ModelStoichiometryServices.updateModelStoichiometryByStoichiometryId(databaseName ,Integer.valueOf(idstoichiometry), metabolites.get(m), idCompartment, Integer.valueOf(m.replace("-", "")));

			}
			else {
				InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelStoichiometry(idReaction, Integer.valueOf(m.replace("-", "")), idCompartment, metabolites.get(m));

			}
		}


		//		//		InitDataAccess.getInstance().getDatabaseService(databaseName).updateReaction(idReaction, name, equation, reversibility, chains, 
		//		compartment, metabolites, inModel, selectedEnzymesPathway, localisation, isSpontaneous, isNonEnzymatic,
		//		isGeneric, lowerBound, upperBound, boolean_rule);;
	}

	public static boolean addReaction_has_Enzyme(String databaseName, Integer idprotein, Integer idReaction) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).addReaction_has_Enzyme(idprotein, idReaction);
	}

	/**
	 * @param rowID
	 * @return
	 * @throws Exception 
	 */
	public static ReactionContainer getReaction(String databaseName, int idReaction) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getDatabaseReactionContainer(idReaction);

		//		res.setGeneRule(RulesParser.getGeneRule(list[11], ModelAPI.getGenesFromDatabase(statement)));
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	public static Map<Integer, MetaboliteContainer> getAllMetabolites(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllMetabolites();
	}

	/**
	 * @param pathwayID
	 * @param blockedReactions
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static ModelReactionsmetabolitesEnzymesSets getEnzymesIdentifiersList(int pathwayID, ModelBlockedReactions blockedReactions, String databaseName) throws Exception {

		Set<String> enzymes = new HashSet<String>(), reactions = new HashSet<String>(), compounds = new HashSet<>();
		Map<String, Set<String>> enzymesGapReactions = new HashMap<>();
		ModelReactionsmetabolitesEnzymesSets rca = new ModelReactionsmetabolitesEnzymesSets();

		boolean isCompartimentalized = ProjectServices.isCompartmentalisedModel(databaseName);
		List<String[]> data = InitDataAccess.getInstance().getDatabaseService(databaseName).getPathwayHasEnzymeData(pathwayID, isCompartimentalized);

		for(int i=0; i<data.size(); i++){
			
			System.out.println(Arrays.asList(data.get(i)));
			
			String[] list = data.get(i);

			String reaction_id = list[3];
			String surrogateEnzID = list[0]+"___"+list[5]; // +"___"+list[1]

			if(reaction_id.contains("_"))
				reaction_id=reaction_id.substring(0,reaction_id.indexOf("_"));

			if(Boolean.valueOf(list[2]) )//&& Boolean.valueOf(list[4]))
				enzymes.add(surrogateEnzID);

			if(Boolean.valueOf(list[2]))
				reactions.add(reaction_id);

			//blockedReactions
			if(blockedReactions!= null) {

				String metabolite = list[5];
				compounds.add(metabolite);

				Set<String> reactionsSet = new HashSet<String>();
				if(enzymesGapReactions.containsKey(surrogateEnzID))
					reactionsSet = enzymesGapReactions.get(surrogateEnzID);
				reactionsSet.add(reaction_id);
				enzymesGapReactions.put(surrogateEnzID, reactionsSet);
			}
		}

		if(blockedReactions!= null) {

			for(String ecn:enzymesGapReactions.keySet()) {

				boolean remove = false;

				for(String pathwayReaction : enzymesGapReactions.get(ecn))
					for(String gapReaction : blockedReactions.getAllReactions().keySet())
						if(gapReaction.contains(pathwayReaction))
							remove = true;

				if(remove)
					enzymes.remove(ecn);
			}
		}

		rca.setCompounds(compounds);
		rca.setEnzymes(enzymes);
		rca.setReactions(reactions);

		return rca;
	}

	/**
	 * @param noEnzymes
	 * @param pathwayID
	 * @param blockedReactions
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static ModelReactionsmetabolitesEnzymesSets getReactionsList(boolean noEnzymes, int pathwayID, ModelBlockedReactions blockedReactions, String databaseName) throws Exception {

		Set<String> reactions = new HashSet<>(), compounds = new HashSet<>(), pathwayGapReactions = new HashSet<>();
		ModelReactionsmetabolitesEnzymesSets rca = new ModelReactionsmetabolitesEnzymesSets();

		boolean isCompartimentalized = ProjectServices.isCompartmentalisedModel(databaseName);

		List<Object[]> result = InitDataAccess.getInstance().getDatabaseService(databaseName).getReactionsList(pathwayID, noEnzymes, isCompartimentalized);


		for(int i=0; i<result.size(); i++) {

			Object[] list = result.get(i);

			String composed_id = list[0].toString();
			String reaction_id = list[0].toString();

			if(reaction_id.contains("_"))
				reaction_id=reaction_id.substring(0,reaction_id.indexOf("_"));

			if(blockedReactions!=null && blockedReactions.getAllReactions().keySet().contains(composed_id))
				pathwayGapReactions.add(reaction_id);

			if(pathwayGapReactions.contains(reaction_id) && !blockedReactions.getAllReactions().keySet().contains(composed_id))
				pathwayGapReactions.remove(reaction_id);

			String metabolite = list[1].toString();
			compounds.add(metabolite);
			reactions.add(reaction_id);

		}

		rca.setReactions(reactions);
		rca.setCompounds(compounds);

		return rca;
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	public static List<String> getCompartments(String databaseName, boolean isMetabolites, boolean isCompartmentalisedModel) throws Exception {

		return ModelCompartmentServices.getCompartments(databaseName, isMetabolites, isCompartmentalisedModel);
	}


	/**
	 * @param name
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static String incrementName(String databaseName, String name) throws Exception {

		if(name.contains(".")) {

			String[] rName = name.split("\\.");
			int version = Integer.parseInt(rName[1]);
			version=version+1;
			name=name.replace("."+rName[1], "."+version);
		}
		else{name=name.concat(".1");}

		boolean exists = ModelReactionsServices.checkIfReactionNameExists(databaseName, name);

		if(exists)
			name = incrementName(databaseName, name);

		return name;
	}

	/**
	 * @param databaseName
	 * @param name
	 * @return 
	 * @throws Exception
	 */
	public static boolean checkIfReactionNameExists(String databaseName, String name) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkIfReactioLabelNameAlreadyExists(name);
	}

	/**
	/**
	 * @param reactionID
	 * @param columnNumber
	 * @param object
	 * @param connection
	 * @param NOTES_COLUMN
	 * @param IS_REVERSIBLE_COLUMN
	 * @param IN_MODEL_COLUMN
	 */
	public static void updateReactionProperties(int reactionID, int columnNumber, Object object, 
			Integer notesColumn, Integer isReversibleColumn, Integer inModelColun, String databaseName) throws Exception{


		if(columnNumber == notesColumn) {

			InitDataAccess.getInstance().getDatabaseService(databaseName).updateModelReactionNotesByReactionId(reactionID, object.toString());

		}
		else {

			boolean value = (Boolean) object;

			Pair<Boolean, Boolean> pair = InitDataAccess.getInstance().getDatabaseService(databaseName).checkReactionIsReversibleAndInModel(reactionID);

			if((columnNumber == isReversibleColumn && value!=pair.getA()) || //(columnNumber == IS_GENERIC_COLUMN && value!=rs.getBoolean(2)) ||
					(columnNumber == inModelColun && value!=pair.getB())) {

				String equation="", source="", lowerBound = "0";

				Pair<String, String> res = InitDataAccess.getInstance().getDatabaseService(databaseName).getEquationAndSourceFromReaction(reactionID);

				equation = res.getA();
				source = res.getB();

				if(columnNumber==isReversibleColumn) {

					if(value) {

						equation=equation.replace(" => ", " <=> ").replace(" <= ", " <=> ");
						lowerBound = "-999999";
					}
					else {

						equation=equation.replace("<=>", "=>");
					}
					InitDataAccess.getInstance().getDatabaseService(databaseName).updateModelReactionReversibleAndLowerBoundAndEquationByReactionId(value, Long.valueOf(lowerBound), reactionID, equation);

				}
				else {

					if(source.equalsIgnoreCase("KEGG"))
						source = "MANUAL";

					InitDataAccess.getInstance().getDatabaseService(databaseName).updateInModelAndSourceByReactionId(reactionID, value, source);

				}
			}
		}

		InitDataAccess.getInstance().getDatabaseService(databaseName).updateReactionProperties(reactionID, columnNumber, object, notesColumn,
				isReversibleColumn, inModelColun);
	}

	/**
	 * @param reversible
	 * @param lower
	 * @param id
	 * @param equation
	 * @throws Exception
	 */
	public static void updateModelReactionReversibleAndLowerBoundAndEquationByReactionId(Boolean reversible, Long lower, Integer id, String equation, String databaseName) throws Exception {
		InitDataAccess.getInstance().getDatabaseService(databaseName).updateModelReactionReversibleAndLowerBoundAndEquationByReactionId(reversible, lower, id, equation);
	}

	/**
	 * @param reactionId
	 * @param inModel
	 * @throws Exception
	 */
	public static void updateModelReactionInModelByReactionId(String databaseName, Integer reactionId, Boolean inModel) throws Exception{
		InitDataAccess.getInstance().getDatabaseService(databaseName).updateModelReactionInModelByReactionId(reactionId, inModel);
	}

	/**
	 * Retrieves information from reaction and stoichiometry and compound table
	 * @param conditions
	 * @return idstoichiometry, reaction_idreaction, compound_idcompound, stoichiometry.compartment_idcompartment, " +
				"stoichiometric_coefficient, numberofchains, compound.name, compound.formula, compound.kegg_id
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static List<String[]> getStoichiometryInfo(String databaseName, boolean isCompartmentalized) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getStoichiometry(isCompartmentalized);
	}

	/**
	 * Method for retrieving the reactions container for a reaction.
	 * 
	 * @param databaseName
	 * @param idReaction
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static ReactionContainer getDatabaseReactionContainer(String databaseName, int idReaction) throws IOException, Exception {

		ReactionContainer container = InitDataAccess.getInstance().getDatabaseService(databaseName).getDatabaseReactionContainer(idReaction);

		if (container != null) {

			List<Pair<Integer, String>> result = InitDataAccess.getInstance().getDatabaseService(databaseName).getAllModelReactionHasModelProteinByReactionId(idReaction);

			for(Pair<Integer, String> pair : result)
				container.addProteinPair(pair);

			Map<Integer, MetaboliteContainer> metabolites = ModelStoichiometryServices.getStoichiometryDataForReaction(databaseName, idReaction);

			for(MetaboliteContainer mContainer : metabolites.values()) {

				if(Double.valueOf(mContainer.getStoichiometric_coefficient()) > 0)
					container.addProduct(mContainer);
				else
					container.addReactant(mContainer);
			}
		}

		return container;
	}

	/**
	 * @param databaseName
	 * @param identifiers
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static Map<Integer, String> getReactionsNames(String databaseName, List<Integer> identifiers) throws IOException, Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getReactionsNames(identifiers);

	}

	/**
	 * Method for inserting new reactions in model database.
	 * 
	 * 
	 * @param name
	 * @param equation
	 * @param reversibility
	 * @param metabolitesChains
	 * @param metabolitesCompartments
	 * @param metabolitesStoichiometry
	 * @param inModel
	 * @param enzymesInPathway
	 * @param reactionCompartment
	 * @param isSpontaneous
	 * @param isNonEnzymatic
	 * @param isGeneric
	 * @param lowerBound
	 * @param upperBound
	 * @param source
	 * @param boolean_rule
	 * @param compartmentalisedModel
	 * @param 
	 * @param statement
	 * @throws Exception
	 */
	public static void insertNewReaction_BiomassRelated(String databaseName, String name, String equation, boolean reversibility, //Set<String> pathways, Set<String> enzymes, 
			Map<String,String> metabolitesChains, Map<String, String > metabolitesCompartments, Map<String, String> metabolitesStoichiometry, boolean inModel, Map<String, 
			Set<String>> enzymesInPathway, String reactionCompartment, boolean isSpontaneous, boolean isNonEnzymatic,
			boolean isGeneric, double lowerBound, double upperBound, SourceType source, String boolean_rule, boolean compartmentalisedModel) throws Exception {

		if(boolean_rule!=null)
			boolean_rule = "'"+boolean_rule+"'";

		if(!name.startsWith("R") && !name.startsWith("T")&& !name.startsWith("K") && !name.toLowerCase().contains("biomass"))
			name = "R_"+name;

		if(name.toLowerCase().equals("biomass"))
			name = "R_"+name;


		List<Integer> res = ModelReactionsServices.getModelReactionLabelIdByName(databaseName, name, compartmentalisedModel);

		if(res.size() > 0) {

			throw new  Exception("Reaction with the same name ("+name+") already exists. Aborting operation!");
		}
		else {

			Integer idCompartment = null;
			if(compartmentalisedModel) {

				CompartmentContainer compartment = ModelCompartmentServices.getCompartmentByName(databaseName, reactionCompartment);
				if(compartment == null)
					idCompartment =  ModelCompartmentServices.insertNameAndAbbreviation(databaseName, reactionCompartment, "biom");
				else
					idCompartment = compartment.getCompartmentID();
			}

			ReactionContainer container = new ReactionContainer(name);

			container.setEquation(equation);
			container.setInModel(inModel);
			if (idCompartment != null)
				container.setLocalisation(idCompartment);
			container.setSource(source);
			container.setSpontaneous(isSpontaneous);
			container.setNon_enzymatic(isNonEnzymatic);
			container.setGeneric(isGeneric);
			container.setLowerBound(lowerBound);
			container.setUpperBound(upperBound);
			container.setGeneRule(boolean_rule);

			Integer idNewReaction = insertNewReaction(databaseName, container);

			//PATHWAYS AND ENZYMES PROCESSING
			{
				Map<Integer,Set<String>> newPathwaysID = new TreeMap<>();
				enzymesInPathway.remove("");
				{
					if(enzymesInPathway.containsKey("-1allpathwaysinreaction") && enzymesInPathway.get("-1allpathwaysinreaction").size()>0) {

						for(String enzyme : enzymesInPathway.get("-1allpathwaysinreaction")) {

							Integer idProtein = Integer.valueOf(enzyme.split("___")[2]);

							boolean exists = ModelReactionsServices.checkReactionHasEnzymeData(databaseName, idProtein, idNewReaction);

							if(!exists)
								ModelReactionsServices.insertModelReactionHasModelProtein(databaseName, idNewReaction, idProtein);
						}
					}
					enzymesInPathway.remove("-1allpathwaysinreaction");
				}

				if(enzymesInPathway.size()>0) {

					for(String pathway : enzymesInPathway.keySet()) {

						Integer id = ModelPathwaysServices.getPathwayIDbyName(databaseName, pathway);

						Set<String> p = new TreeSet<String>();
						if (enzymesInPathway.get(pathway).size()>0)
							p =  new TreeSet<String>(enzymesInPathway.get(pathway));

						newPathwaysID.put(id, p);
					}

					//when pathways are deleted, they are just removed from the pathway has reaction association
					//insert the new pathways

					for(Integer pathwayId : newPathwaysID.keySet()) {

						boolean exists = ModelPathwaysServices.checkPathwayHasReactionData(databaseName, pathwayId, idNewReaction);

						if(!exists) {

							ModelPathwaysServices.insertModelPathwayHasModelReaction(databaseName, pathwayId, idNewReaction);

							for(String enzyme: newPathwaysID.get(pathwayId)) {

								Integer idProtein = Integer.valueOf(enzyme.split("___")[2]);

								exists = ModelPathwaysServices.checkPathwayHasModelProtein(databaseName, pathwayId, idProtein);

								if(!exists) 
									ModelPathwaysServices.insertModelPathwayHasModelProtein(databaseName, pathwayId, idProtein);

								exists = ModelReactionsServices.checkReactionHasEnzymeData(databaseName, idProtein, idNewReaction);

								if(!exists) {
									ModelReactionsServices.insertModelReactionHasModelProtein(databaseName, idNewReaction, idProtein);
								}
							}
						}
					}
				}
			}

			int biomass_id = -1;

			MetaboliteContainer metabolite = ModelMetabolitesServices.getModelCompoundByName(databaseName, "Biomass");

			if(metabolite != null)
				biomass_id = metabolite.getMetaboliteID();

			for(String m :metabolitesStoichiometry.keySet()) {

				if(m!=null && !m.equalsIgnoreCase("null")){

					CompartmentContainer compartment = ModelCompartmentServices.getCompartmentByName(databaseName, metabolitesCompartments.get(m));

					if(compartment == null) {

						compartment = ModelCompartmentServices.getCompartmentByName(databaseName, Compartments.inside.toString());

						if(compartment != null)
							idCompartment = compartment.getCompartmentID();
					}
					else
						idCompartment = compartment.getCompartmentID();

					ModelStoichiometryServices.insertStoichiometry(databaseName, idNewReaction, Integer.valueOf(m.replace("-", "")), idCompartment, Double.valueOf(metabolitesStoichiometry.get(m)));

					if(m.replace("-", "").equalsIgnoreCase(biomass_id+"")) {

						Integer idBiomassPath = ModelPathwaysServices.getPathwayIDbyName(databaseName, Pathways.BIOMASS.getName());

						if(idBiomassPath == null)				
							idBiomassPath = ModelPathwaysServices.insertModelPathwayCodeAndName(databaseName, Pathways.BIOMASS.getCode(), Pathways.BIOMASS.getName());

						boolean exists = ModelPathwaysServices.checkPathwayHasReactionData(databaseName, idBiomassPath, idNewReaction);

						if(!exists) {
							ModelPathwaysServices.insertModelPathwayHasModelReaction(databaseName, idBiomassPath, idNewReaction);
						}
					}
				}
			}
		}
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static Map<String, List<Integer>> getModelReactionIdsRelatedToNames(String databaseName) throws IOException, Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getModelReactionIdsRelatedToNames();
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer, List<Integer>> getEnzymesReactions2(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getEnzymesReactions2();
	}

	/**
	 * @param databaseName
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static List<String> getRelatedReactions(String databaseName, String name) throws Exception {        

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getRelatedReactions(name);        
	}

	/**
	 * @param databaseName
	 * @param name
	 * @param isCompartimentalized
	 * @return
	 * @throws Exception
	 */
	public static List<Integer> getModelReactionLabelIdByName(String databaseName,String name, boolean isCompartimentalized) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getModelReactionLabelIdByName(name, isCompartimentalized);
	}

	/**
	 * @param databaseName
	 * @param names
	 * @param keepReactionsWithNotes
	 * @param keepManualReactions
	 * @throws Exception
	 */
	public static void removeReactionsFromModelByBooleanRule(String databaseName, List<String> names, boolean keepReactionsWithNotes, boolean keepManualReactions) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).removeReactionsFromModelByBooleanRule(names, keepReactionsWithNotes, keepManualReactions);
	}


	/**
	 * @param databaseName
	 * @param isCompartimentalized
	 * @return
	 * @throws Exception
	 */
	public static List<String[]> getDataFromReactionForBlockedReactionsTool(String databaseName, boolean isCompartimentalized) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getDataFromReactionForBlockedReactionsTool(isCompartimentalized);
	}

	/**
	 * @param databaseName
	 * @param isTransporters
	 * @return
	 * @throws Exception
	 */
	public static List<Object[]> getAllModelReactionsByTransportersAndIsCompartimentalized(String databaseName, boolean isTransporters) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllModelReactionsByTransportersAndIsCompartimentalized(isTransporters);
	}


	/**
	 * @param databaseName
	 * @param isCompartimentalized
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer, ReactionContainer> getAllModelReactionAttributesbySource(String databaseName, boolean isCompartimentalized,
			SourceType source)  throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllModelReactionAttributesbySource(isCompartimentalized, source.toString());
	}

	/**
	 * @param databaseName
	 * @param idReaction
	 * @param idprotein
	 * @throws Exception
	 */
	public static void insertModelReactionHasModelProtein(String databaseName, Integer idReaction, Integer idprotein)  throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelReactionHasModelProtein(idReaction, idprotein);
	}

	/**
	 * @param databaseName
	 * @param idProtein
	 * @param idReaction
	 * @return
	 * @throws Exception
	 */
	public static boolean checkReactionHasEnzymeData(String databaseName, Integer idProtein, Integer idReaction)  throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkReactionHasEnzymeData(idProtein, idReaction);

	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, List<Integer>> getEcNumbersByReactionId(String databaseName)  throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getEcNumbersByReactionId();

	}

	/**
	 * @param databaseName
	 * @param proteinID
	 * @param isCompartimentalized
	 * @return
	 * @throws Exception
	 */
	public static List<ReactionContainer> getDistinctReactionByProteinIdAndCompartimentalized(String databaseName, Integer proteinID, boolean isCompartimentalized)  throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getDistinctReactionByProteinIdAndCompartimentalized(proteinID, isCompartimentalized);

	}

	/**
	 * @param databaseName
	 * @param proteinId
	 * @return
	 * @throws Exception
	 */
	public static List<ReactionContainer> getReactionIdFromProteinIdWithPathwayIdNull(String databaseName, Integer proteinId)  throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getReactionIdFromProteinIdWithPathwayIdNull(proteinId);

	}

	/**
	 * @param databaseName
	 * @param reactionId
	 * @return
	 * @throws Exception
	 */
	public static Integer getIdReactionLabelFromReactionId(String databaseName, Integer reactionId)  throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getIdReactionLabelFromReactionId(reactionId);

	}

	public static List<Object[]> GetCompoundIDAndStoichiometricCoefficientAndCompartmentIDByReactionID(String databaseName, Integer reactionID) throws Exception { 
		return InitDataAccess.getInstance().getDatabaseService(databaseName).getCompoundIDAndStoichiometricCoefficientAndCompartmentIDByReactionID(reactionID);

	}

	/**
	 * @param databaseName
	 * @param reactionLabelId
	 * @param source
	 * @throws Exception
	 */
	public static void updateSourceByReactionLabelId(String databaseName, Integer reactionLabelId, String source)  throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).updateSourceByReactionLabelId(reactionLabelId, source);

	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static List<String[]> getReactionIdAndEcNumberAndProteinId(String databaseName)  throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getReactionIdAndEcNumberAndProteinId();

	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static List<Integer[]> getReactionIdAndPathwayId(String databaseName)  throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getReactionIdAndPathwayId();
	}


	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static List<String[]> getReacIdEcNumProtIdWhereSourceEqualTransporters(String databaseName)  throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getReacIdEcNumProtIdWhereSourceEqualTransporters();
	}

	/**
	 * @param databaseName
	 * @param isTransporter
	 * @return
	 * @throws Exception
	 */
	public static List<Integer> getDistinctReactionIdWhereSourceTransporters(String databaseName, Boolean isTransporter)  throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getDistinctReactionIdWhereSourceTransporters(isTransporter);
	}

	/**
	 * @param databaseName
	 * @param isTransporter
	 * @return
	 * @throws Exception
	 */
	public static Set<String> getAllReactionsNames(String databaseName)  throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllReactionsNames();
	}

	/**
	 * @param databaseName
	 * @param type
	 * @param isCompartimentalized
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer, ReactionContainer> getReactionIdAndPathwayID(String databaseName, SourceType type, boolean isCompartimentalized)  throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getReactionIdAndPathwayID(type.toString(), isCompartimentalized);
	}

	/**
	 * 
	 * boolean to select transport or biochemical, null to select all
	 * 
	 * @param databaseName
	 * @param source
	 * @throws Exception
	 */
	public static void removeNotOriginalReactions(String databaseName, Boolean isTransporter) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).removeNotOriginalReactions(isTransporter);

	}

	/**
	 * @param databaseName
	 * @param idreaction
	 * @return
	 * @throws Exception
	 */
	public static List<String[]> getCompoundDataFromStoichiometry(String databaseName, Integer idreaction)  throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getCompoundDataFromStoichiometry(idreaction);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static List<String[]> getReactionGenes(String databaseName)  throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getReactionGenes();
	}

	/**
	 * @param databaseName
	 * @param pathwayId
	 * @return
	 * @throws Exception
	 */
	public static List<Integer> getReactionIdByPathwayID(String databaseName, Integer pathwayId)  throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getReactionIdByPathwayID(pathwayId);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Set<Integer> getModelDrains(String databaseName)  throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getModelDrains();
	}

	/**
	 * @param databaseName
	 * @param reaction_id
	 * @return
	 * @throws Exception
	 */
	public static boolean removeSelectedReaction(String databaseName, int reaction_id)  throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).removeSelectedReaction(reaction_id);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static boolean checkIfReactionsHasData(String databaseName)  throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkIfReactionsHasData();
	}

	/**
	 * Parse boolean_rule from reaction for a given reactionID.
	 * 
	 * @param databaseName
	 * @param id
	 * @return String
	 * @throws Exception
	 */
	public static List<List<Pair<String, String>>> parseBooleanRule(String databaseName, String rawData) throws Exception {

		List<List<Pair<String, String>>> res = new ArrayList<>();;

		if(rawData != null) {

			rawData = rawData.toUpperCase().replace("'", "");

			String [] rules = rawData.split(" OR ");

			for(String rule : rules) {

				String [] ids = rule.split(" AND ");

				List<Pair<String, String>> pairList= new ArrayList<>();

				for(String idString : ids) {

					if(!idString.isEmpty()) {

						idString = idString.replaceAll("[()]", "");

						int geneId = Integer.parseInt(idString.trim());

						GeneContainer gene = ModelGenesServices.getGeneDataById(databaseName, geneId);

						if(gene != null) {
							
							String locus = gene.getLocusTag();
							
							if(locus == null || locus.isEmpty())
								locus = gene.getExternalIdentifier();

							Pair<String, String> pair = new Pair<String, String> (locus, gene.getName());
							pairList.add(pair);
						}
					}
				}
				res.add(pairList);
			}
		}
		return res;
	}

	/**
	 * @param databaseName
	 * @param setInModel
	 * @param keepSpontaneousReactions
	 * @throws Exception
	 */
	public static void setAllReactionsInModel(String databaseName, boolean setInModel, boolean keepSpontaneousReactions) throws Exception{

		InitDataAccess.getInstance().getDatabaseService(databaseName).setAllReactionsInModel(setInModel, keepSpontaneousReactions);
	}

	/**
	 * @param databaseName
	 * @param inModel
	 * @param notes
	 * @param booleanRule
	 * @param name
	 * @throws Exception
	 */
	public static void updateModelReactionSetInModelAndNotesAndBooleanRuleByReactionName(String databaseName,
			boolean inModel, String notes, String booleanRule, String name) throws Exception{

		InitDataAccess.getInstance().getDatabaseService(databaseName).updateModelReactionSetInModelAndNotesAndBooleanRuleByReactionName(inModel, notes, booleanRule, name);
	}

	/**
	 * Run gene-protein reactions assignment.
	 * 
	 * @param threshold 
	 * @throws Exception 
	 */
	public static Map<String, ReactionsGPR_CI> runGPRsAssignment(String databaseName, double threshold) throws Exception {


		List<String[]> data = InitDataAccess.getInstance().getDatabaseService(databaseName).getDataForGPRAssignment();

		Map<String, ReactionsGPR_CI> rpgs = new HashMap<>();

		for(String[] line : data) {

			if(//rs.getString("note")==null || !rs.getString("note").equalsIgnoreCase("unannotated") || (rs.getString("note").equalsIgnoreCase("unannotated") && 
					line[5] != null && Double.valueOf(line[5])>=threshold)
				//				)
			{

				String reaction = line[0];
				String ecNumber = line[1];
				String definition = line[2];


				ReactionsGPR_CI rpg = new ReactionsGPR_CI(reaction);

				if(rpgs.containsKey(reaction))
					rpg  = rpgs.get(reaction);


				ProteinsGPR_CI pga = new ProteinsGPR_CI(ecNumber, definition);
				pga.addSubunit(definition.split(" OR "));

				if(rpg.getProteins()!= null && rpg.getProteins().containsKey(ecNumber))
					pga = rpg.getProteins().get(ecNumber);

				String idGene = line[3];
				String entryId = line[4];
	
				pga.addLocusTag(entryId, idGene);
				rpg.addProteinGPR_CI(pga);
				rpgs.put(rpg.getReaction(), rpg);
			}
		}

		return rpgs;
	}

	/**
	 * @param databaseName
	 * @return 
	 * @throws Exception
	 */
	public static List<Pair<String, String>> getReactionHasEnzyme(String databaseName, boolean isCompartimentalised) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getReactionHasEnzyme(isCompartimentalised);

	}

	/**
	 * @param databaseName
	 * @return 
	 * @throws Exception
	 */
	public static List<Pair<Integer, String>> getReactionHasEnzyme2(String databaseName, boolean isCompartimentalised) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getReactionHasEnzyme2(isCompartimentalised);

	}

	/**
	 * @param databaseName
	 * @param reactionId
	 * @return
	 * @throws Exception
	 */
	public static List<Integer> getProteinsByReactionId(String databaseName, Integer reactionId) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getProteinsByReactionId(reactionId);

	}

	/**
	 * @param databaseName
	 * @param reactionId
	 * @return
	 * @throws Exception
	 */
	public static CompartmentContainer getReactionCompartment(String databaseName, Integer reactionId) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getReactionCompartment(reactionId);

	}

	/**
	 * @param databaseName
	 * @param reactionContainer
	 * @throws Exception
	 */
	public static void insertReactionHasProtein(String databaseName, ReactionContainer reactionContainer) throws Exception {

		for(Pair<String, String> idgene : reactionContainer.getGenes()){

			Integer idReaction = reactionContainer.getReactionID();

			List<ProteinContainer> proteins =  ModelProteinsServices.getProteinIdByIdGene(databaseName, Integer.valueOf(idgene.getA()));

			for(ProteinContainer protein : proteins) {
				insertModelReactionHasModelProtein(databaseName, idReaction, protein.getIdProtein());
			}
		}
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static List<ReactionContainer> getGenesReactionsBySubunit(String databaseName) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getGenesReactionsBySubunit();

	}

	/**
	 * @param databaseName
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static List<ReactionContainer> getModelReactionIdsRelatedToName(String databaseName, String name) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getModelReactionIdsRelatedToName(name);

	}
}